package controller;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.Column;
import model.Relationship;
import model.SchemaModel;
import model.Table;
import model.exception.SchemaParsingException;
import model.factory.ParserFactory;
import model.parser.SchemaParser;
import view.SchemaView;
import java.io.File;
import java.util.List;

/**
 * The SchemaController class serves as the controller in the MVC architecture,
 * managing the interaction between the SchemaModel and the SchemaView.
 * It handles user actions, processes schema files, and updates the view
 * accordingly.
 */
public class SchemaController {
    private SchemaModel schemaModel;
    private SchemaView schemaView;

    private static final Logger logger = LogManager.getLogger(SchemaController.class);

    private static final String SELECT_TABLE_PROMPT = "Select Table:";

    /**
     * Constructs a SchemaController with the specified model and view.
     *
     * @param schemaModel the SchemaModel object to manage schema data
     * @param schemaView  the SchemaView object to manage user interactions and
     *                    display
     */
    public SchemaController(SchemaModel schemaModel, SchemaView schemaView) {
        this.schemaModel = schemaModel;
        this.schemaView = schemaView;
    }

    /**
     * Initializes the controller by setting up event handlers for view components.
     * Handles schema file upload and SQL generation functionality.
     */
    public void run() {
        schemaView.display();
        disableActionButtons();
        setupGenerateSQLButtonHandler();
        setupUploadButtonHandler();
        setupAddTableButtonHandler();
        setupAddRelationshipButtonHandler();
        setupAddColumnButtonHandler();
        setupEditOperationsHandlers();
        setupDeleteOperationsHandlers();
    }

    private void setupUploadButtonHandler() {
        schemaView.getUploadButton().setOnAction(e -> {
            File schemaFile = schemaView.handleFileUpload();
            if (schemaFile != null) {
                schemaModel.setFile(schemaFile);
                handleSchemaUpload(schemaFile);
                if (!schemaModel.getTables().isEmpty()) {
                    enableActionButtons();
                }
                updateViewWithTables();
                logger.info("Schema file uploaded and processed: {}", schemaFile.getAbsolutePath());
            } else {
                logger.warn("No file selected for upload.");
            }
        });
    }

    private void setupAddTableButtonHandler() {
        schemaView.getAddTableButton().setOnAction(e -> {
            String tableName = schemaView.showAddTableDialog();
            if (tableName != null && !tableName.isEmpty()) {
                Table newTable = new Table(tableName);
                addTable(newTable);
                logger.info("New table added: {}", tableName);
            } else {
                logger.warn("Table name is empty. Cannot add table.");
            }
        });
    }

    private void setupAddRelationshipButtonHandler() {
        schemaView.getAddRelationshipButton().setOnAction(e -> {
            List<String> tableNames = getAllTableNames();
            if (tableNames.isEmpty()) {
                schemaView.addMessage("No tables available to create a relationship.");
                return;
            }

            List<String> relationshipData = schemaView.showAddRelationshipDialog(
                    tableNames,
                    this::getAllColumnsForTable,
                    this::getAllColumnsForTable);

            if (relationshipData != null && !relationshipData.isEmpty()) {
                String baseTable = relationshipData.get(0);
                String baseTableForeignKey = relationshipData.get(1);
                String relatedTable = relationshipData.get(2);
                String relatedTableForeignKey = relationshipData.get(3);
                String relationshipType = relationshipData.get(4);
                String throughTable = relationshipData.get(5);

                Relationship relationship = new Relationship(
                        relationshipType, relatedTable, baseTableForeignKey, relatedTableForeignKey, throughTable);
                addRelationship(baseTable, relationship);
                logger.info("New relationship added between {} and {}: {}", baseTable, relatedTable, relationshipType);
            }
        });
    }

    private void setupAddColumnButtonHandler() {
        schemaView.getAddColumnButton().setOnAction(e -> {
            List<String> columnData = schemaView.showAddColumnDialog();
            if (columnData != null && !columnData.isEmpty()) {
                String selectedTable = columnData.get(0);
                String columnName = columnData.get(1);
                String columnType = columnData.get(2);
                boolean isPrimaryKey = Boolean.parseBoolean(columnData.get(3));

                Column column = new Column(columnName, columnType, isPrimaryKey);
                addColumn(selectedTable, column);
                logger.info("New column added to table {}: {}", selectedTable, columnName);
            }
        });
    }

    private void setupGenerateSQLButtonHandler() {
        schemaView.getGenerateSQLButton().setOnAction(e -> {
            if (schemaModel.getFile() != null) {
                String selectedSQLType = schemaView.getSqlTypePicker().getValue();
                String message = schemaModel.toScript(selectedSQLType);
                schemaView.addMessage(message != null ? message : "Populate all tables");
            } else {
                schemaView.addMessage("Please upload a schema file first.");
            }
        });
    }

    private void setupEditOperationsHandlers() {
        schemaView.getEditTableButton().setOnAction(e -> handleEditTable());
        schemaView.getEditColumnButton().setOnAction(e -> handleEditColumn());
        schemaView.getEditRelationshipButton().setOnAction(e -> handleEditRelationship());
    }

    private void setupDeleteOperationsHandlers() {
        schemaView.getDeleteTableButton().setOnAction(e -> {
            List<String> tableNames = getAllTableNames();
            String tableName = schemaView.showDeleteTableDialog(tableNames);
            if (tableName != null) {
                deleteTable(tableName);
            }
        });

        schemaView.getDeleteColumnButton().setOnAction(e -> {
            List<String> tableNames = getAllTableNames();
            List<String> columnDetails = schemaView.showDeleteColumnDialog(tableNames, this::getAllColumnsForTable);

            if (columnDetails != null) {
                String tableName = columnDetails.get(0);
                String columnName = columnDetails.get(1);
                deleteColumn(tableName, columnName);
                logger.info("Column deleted: {} from table {}", columnName, tableName);
            }
        });

        // Fixed delete relationship button handler
        schemaView.getDeleteRelationshipButton().setOnAction(e -> {
            List<String> tableNames = getAllTableNames();
            String selectedTable = schemaView.showSelectTableDialog(tableNames, SELECT_TABLE_PROMPT);
            if (selectedTable != null) {
                List<String> relationships = getAllRelationshipsForTable(selectedTable);
                String relatedTable = schemaView.showSelectRelationshipDialog(relationships,
                        "Select Relationship to Delete:");
                if (relatedTable != null) {
                    deleteRelationship(selectedTable, relatedTable);
                    logger.info("Relationship deleted between {} and {}", selectedTable, relatedTable);
                }
            }
        });
    }

    private void handleSchemaUpload(File schemaFile) {
        try {
            ParserFactory factory = new ParserFactory();
            SchemaParser parser = factory.get(getFileExtension(schemaFile.getName()));
            schemaModel = parser.parse(schemaFile);
            schemaModel.setFile(schemaFile);
            logger.info("Schema parsed successfully.");
        } catch (SchemaParsingException e) {
            logger.error("Error while parsing the schema file: {}", schemaFile.getAbsolutePath(), e);
            schemaView.addMessage("Error: Failed to parse the schema file.");
        }
    }

    private void disableActionButtons() {
        schemaView.getAddTableButton().setDisable(true);
        schemaView.getAddRelationshipButton().setDisable(true);
        schemaView.getAddColumnButton().setDisable(true);
        schemaView.getDeleteTableButton().setDisable(true);
        schemaView.getDeleteColumnButton().setDisable(true);
        schemaView.getDeleteRelationshipButton().setDisable(true);
        schemaView.getEditTableButton().setDisable(true);
        schemaView.getEditColumnButton().setDisable(true);
        schemaView.getEditRelationshipButton().setDisable(true);
    }

    private void enableActionButtons() {
        schemaView.getAddTableButton().setDisable(false);
        schemaView.getAddRelationshipButton().setDisable(false);
        schemaView.getAddColumnButton().setDisable(false);
        schemaView.getDeleteTableButton().setDisable(false);
        schemaView.getDeleteColumnButton().setDisable(false);
        schemaView.getDeleteRelationshipButton().setDisable(false);
        schemaView.getEditTableButton().setDisable(false);
        schemaView.getEditColumnButton().setDisable(false);
        schemaView.getEditRelationshipButton().setDisable(false);
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return (lastIndex > 0) ? fileName.substring(lastIndex + 1) : "";
    }

    private List<String> getAllTableNames() {
        return schemaModel.getTables().stream().map(Table::getTableName).toList();
    }

    public List<String> getAllColumnsForTable(String tableName) {
        return schemaModel.getTables().stream()
        .filter(table -> table.getTableName().equals(tableName))
        .flatMap(table -> table.getColumns().stream().map(Column::getName))
        .toList();
    }

    private List<String> getAllRelationshipsForTable(String tableName) {
        return schemaModel.getTables().stream()
        .filter(table -> table.getTableName().equals(tableName))
        .flatMap(table -> table.getRelationships().stream().map(Relationship::getRelatedTable))
        .toList();
    }

    private void updateViewWithTables() {
        List<String> tableNames = getAllTableNames();
        schemaView.setTableNames(tableNames);

        List<VBox> tableComponents = schemaModel.getTables().stream()
        .map(this::createTableComponent)
        .toList();

        schemaView.updateTablesLayout(tableComponents);
    }

    private VBox createTableComponent(Table table) {
        VBox tableBox = new VBox();
        Label tableNameLabel = new Label("Table: " + table.getTableName());
        tableNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        TableView<Object> columnsTable = new TableView<>();
        TableColumn<Object, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Object, String> detailsColumn = new TableColumn<>("Details");

        nameColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Column col) {
                return new javafx.beans.property.SimpleStringProperty(col.getName());
            } else if (cellData.getValue() instanceof Relationship rel) {
                return new javafx.beans.property.SimpleStringProperty("Relationship to " + rel.getRelatedTable());
            }
            return null;
        });

        detailsColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Column column) {
                return new javafx.beans.property.SimpleStringProperty(
                        column.getType() + (column.isPrimaryKey() ? " (Primary Key)" : ""));
            } else if (cellData.getValue() instanceof Relationship rel) {
                return new javafx.beans.property.SimpleStringProperty(
                        rel.getRelationshipType() + " - FK: " + rel.getForeignKey());
            }
            return null;
        });

        java.util.Collections.addAll(columnsTable.getColumns(), nameColumn, detailsColumn);
        columnsTable.getItems().addAll(table.getColumns());
        columnsTable.getItems().addAll(table.getRelationships());

        tableBox.getChildren().addAll(tableNameLabel, columnsTable);
        return tableBox;
    }

    private void addTable(Table table) {
        schemaModel.addTable(table);
        updateViewWithTables();
    }

    private void addColumn(String tableName, Column column) {
        schemaModel.storeData(column, tableName);
        updateViewWithTables();
    }

    private void addRelationship(String tableName, Relationship relationship) {
        schemaModel.storeData(relationship, tableName);
        updateViewWithTables();
    }

    private void deleteTable(String tableName) {
        schemaModel.deleteTable(tableName);
        updateViewWithTables();
    }

    private void deleteColumn(String tableName, String columnName) {
        schemaModel.deleteColumn(tableName, columnName);
        updateViewWithTables();
    }

    private void deleteRelationship(String tableName, String relatedTable) {
        schemaModel.deleteRelationship(tableName, relatedTable);
        updateViewWithTables();
    }

    public void handleEditTable() {
        List<String> tableNames = getAllTableNames();
        String oldTableName = schemaView.showSelectTableDialog(tableNames, "Select Table to Edit:");
        if (oldTableName != null) {
            String newTableName = schemaView.showEditTableDialog(oldTableName);
            if (newTableName != null && !newTableName.isEmpty()) {
                schemaModel.editTableName(oldTableName, newTableName);
                logger.info("Table name updated from {} to {}", oldTableName, newTableName);
                updateViewWithTables();
            } else {
                logger.warn("New table name is empty. Edit aborted.");
            }
        }
    }

    public void handleEditColumn() {
        List<String> tableNames = getAllTableNames();
        String selectedTable = schemaView.showSelectTableDialog(tableNames, SELECT_TABLE_PROMPT);
        if (selectedTable != null) {
            List<String> columnNames = getAllColumnsForTable(selectedTable);
            String oldColumnName = schemaView.showSelectColumnDialog(columnNames, "Select Column to Edit:");
            if (oldColumnName != null) {
                List<String> columnData = schemaView.showEditColumnDialog(oldColumnName);
                if (columnData != null) {
                    String newColumnName = columnData.get(0);
                    String columnType = columnData.get(1);
                    boolean isPrimaryKey = Boolean.parseBoolean(columnData.get(2));

                    Column updatedColumn = new Column(newColumnName, columnType, isPrimaryKey);
                    schemaModel.editColumn(selectedTable, oldColumnName, updatedColumn);
                    updateViewWithTables();
                }
            }
        }
    }

    public void handleEditRelationship() {
        List<String> tableNames = getAllTableNames();
        String selectedTable = schemaView.showSelectTableDialog(tableNames, SELECT_TABLE_PROMPT);
        if (selectedTable != null) {
            List<String> relationships = getAllRelationshipsForTable(selectedTable);
            String relatedTable = schemaView.showSelectRelationshipDialog(relationships,
                    "Select Relationship to Edit:");
            if (relatedTable != null) {
                List<String> relationshipData = schemaView.showEditRelationshipDialog();
                if (relationshipData != null) {
                    String relationshipType = relationshipData.get(0);
                    String foreignKey = relationshipData.get(1);
                    String relatedForeignKey = relationshipData.get(2);

                    Relationship updatedRelationship = new Relationship(
                            relationshipType,
                            relatedTable,
                            foreignKey,
                            relatedForeignKey,
                            selectedTable);
                    schemaModel.editRelationship(selectedTable, relatedTable, updatedRelationship);
                    updateViewWithTables();
                }
            }
        }
    }
}

package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * The SchemaView class represents the user interface for displaying and
 * interacting
 * with the schema. It provides components for file upload, SQL generation, and
 * dynamic
 * table visualization.
 */
public class SchemaView {

    private static final Logger logger = LogManager.getLogger(SchemaView.class);
    private static final String VARCHAR = "VARCHAR";

    private Stage primaryStage;
    private TextArea messageArea;
    private TextField filePathField;
    private ComboBox<String> sqlTypePicker;
    private Button generateSQLButton;
    private Button uploadButton;
    private VBox tablesLayout;

    private Button addTableButton;
    private Button addRelationshipButton;
    private Button addColumnButton;

    // New buttons for deletion
    private Button deleteTableButton;
    private Button deleteColumnButton;
    private Button deleteRelationshipButton;

    private List<String> tableNames = new ArrayList<>();

    private ComboBox<String> selectTableForColumnDropdown;
    private ComboBox<String> selectTableDropdown;
    private ComboBox<String> relatedTableDropdown;

    private Button editTableButton;
    private Button editColumnButton;
    private Button editRelationshipButton;

    private static final String BUTTON_STYLE = "-fx-padding: 10px; -fx-border-color: gray; -fx-border-radius: 5px; -fx-border-width: 1px;";

    private static final String TABLE_NAME_LABEL = "Table Name:";
    private static final String SELECT_TABLE_LABEL = "Select table";

    /**
     * Constructs the SchemaView with the specified primary stage.
     *
     * @param stage the primary stage of the JavaFX application
     */
    public SchemaView(Stage stage) {
        this.primaryStage = stage;
        logger.info("SchemaView initialized with primary stage.");
    }

    public void setTableNames(List<String> tableNames) {
        this.tableNames = tableNames != null ? tableNames : new ArrayList<>();

        if (selectTableDropdown != null) {
            selectTableDropdown.getItems().clear();
            selectTableDropdown.getItems().addAll(tableNames);
        }
        if (relatedTableDropdown != null) {
            relatedTableDropdown.getItems().clear();
            relatedTableDropdown.getItems().addAll(tableNames);
        }
        if (selectTableForColumnDropdown != null) {
            selectTableForColumnDropdown.getItems().clear();
            selectTableForColumnDropdown.getItems().addAll(tableNames);
        }
    }

    public Button getEditTableButton() {
        return editTableButton;
    }

    public Button getEditColumnButton() {
        return editColumnButton;
    }

    public Button getEditRelationshipButton() {
        return editRelationshipButton;
    }

    public Button getUploadButton() {
        return uploadButton;
    }

    public Button getGenerateSQLButton() {
        return generateSQLButton;
    }

    public Button getAddRelationshipButton() {
        return addRelationshipButton;
    }

    public Button getAddColumnButton() {
        return addColumnButton;
    }

    public Button getAddTableButton() {
        return addTableButton;
    }

    public Button getDeleteTableButton() {
        return deleteTableButton;
    }

    public Button getDeleteColumnButton() {
        return deleteColumnButton;
    }

    public Button getDeleteRelationshipButton() {
        return deleteRelationshipButton;
    }

    public ComboBox<String> getSqlTypePicker() {
        return sqlTypePicker;
    }

    public void display() {
        logger.info("Displaying the SchemaView.");

        // File upload section
        Label uploadLabel = new Label("Upload a File:");
        uploadButton = new Button("Upload File");
        filePathField = new TextField();
        filePathField.setEditable(false);
        filePathField.setPrefWidth(400);
        HBox uploadBox = new HBox(10, uploadLabel, uploadButton, filePathField);
        uploadBox.setStyle("-fx-padding: 10px; -fx-spacing: 10px;");

        // SQL generation section
        sqlTypePicker = new ComboBox<>();
        sqlTypePicker.getItems().addAll("MySQL", "Oracle");
        sqlTypePicker.setValue("MySQL");
        generateSQLButton = new Button("Generate SQL");
        HBox sqlBox = new HBox(10, sqlTypePicker, generateSQLButton);
        sqlBox.setStyle("-fx-padding: 10px; -fx-spacing: 10px;");

        // Message area for logs
        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setWrapText(true);
        messageArea.setPrefHeight(150);

        // Table display section
        tablesLayout = new VBox(10);
        ScrollPane tablesScrollPane = new ScrollPane(tablesLayout);
        tablesScrollPane.setFitToWidth(true);
        VBox tableSection = new VBox(new Label("Tables:"), tablesScrollPane);
        tableSection.setStyle("-fx-padding: 10px;");

        // Button layout with grouping
        addTableButton = new Button("Add Table");
        addRelationshipButton = new Button("Add Relationship");
        addColumnButton = new Button("Add Column");

        deleteTableButton = new Button("Delete Table");
        deleteColumnButton = new Button("Delete Column");
        deleteRelationshipButton = new Button("Delete Relationship");

        editTableButton = new Button("Edit Table");
        editColumnButton = new Button("Edit Column");
        editRelationshipButton = new Button("Edit Relationship");

        VBox addButtons = new VBox(10, addTableButton, addRelationshipButton, addColumnButton);
        addButtons
                .setStyle(BUTTON_STYLE);

        VBox deleteButtons = new VBox(10, deleteTableButton, deleteColumnButton, deleteRelationshipButton);
        deleteButtons
                .setStyle(BUTTON_STYLE);

        VBox editButtons = new VBox(10, editTableButton, editColumnButton, editRelationshipButton);
        editButtons
                .setStyle(BUTTON_STYLE);

        HBox buttonLayout = new HBox(20, addButtons, deleteButtons, editButtons);
        buttonLayout.setPadding(new Insets(10, 10, 10, 10)); // Adds padding around the entire button layout

        // Main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(uploadBox);
        mainLayout.setCenter(tableSection);
        mainLayout.setBottom(new VBox(sqlBox, new Label("Messages:"), messageArea));
        mainLayout.setRight(buttonLayout); // Add buttonLayout with padding

        // Scene setup
        Scene scene = new Scene(mainLayout, 1200, 700);
        primaryStage.setTitle("Schema Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void updateTablesLayout(List<VBox> tableComponents) {
        tablesLayout.getChildren().clear();
        tablesLayout.getChildren().addAll(tableComponents);
        logger.info("Tables layout updated in the view.");
    }

    public File handleFileUpload() {
        logger.info("File upload initiated.");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");

        File initialDirectory = new File(System.getProperty("user.dir"), "schema");
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Supported Files", "*.json", "*.xml"),
                new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml"));

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
            logger.info("File selected: {}", selectedFile.getAbsolutePath());
        }
        return selectedFile;
    }

    public void addMessage(String message) {
        logger.debug("Message added to message area: {}", message);
        messageArea.setText(message + "\n");
    }

    public String showAddTableDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText(TABLE_NAME_LABEL);
        dialog.setHeaderText("Enter Table Name:");
        dialog.setContentText(SELECT_TABLE_LABEL);
        return dialog.showAndWait().orElse(null);
    }

    public List<String> showAddRelationshipDialog(
            List<String> tableNames,
            Function<String, List<String>> baseTableColumnsProvider,
            Function<String, List<String>> relatedTableColumnsProvider) {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Add Relationship");

        ComboBox<String> baseTableDropdown = new ComboBox<>();
        baseTableDropdown.getItems().addAll(tableNames);
        baseTableDropdown.setPromptText("Select Base Table");

        ComboBox<String> baseTableColumnDropdown = new ComboBox<>();
        baseTableColumnDropdown.setPromptText("Select Base Table Foreign Key");

        relatedTableDropdown = new ComboBox<>();
        relatedTableDropdown.getItems().addAll(tableNames);
        relatedTableDropdown.setPromptText("Select Related Table");

        ComboBox<String> relatedTableColumnDropdown = new ComboBox<>();
        relatedTableColumnDropdown.setPromptText("Select Related Table Foreign Key");

        baseTableDropdown.setOnAction(e -> {
            String selectedBaseTable = baseTableDropdown.getValue();
            if (selectedBaseTable != null) {
                List<String> baseTableColumns = baseTableColumnsProvider.apply(selectedBaseTable);
                baseTableColumnDropdown.getItems().clear();
                baseTableColumnDropdown.getItems().addAll(baseTableColumns);
            }
        });

        relatedTableDropdown.setOnAction(e -> {
            String selectedRelatedTable = relatedTableDropdown.getValue();
            if (selectedRelatedTable != null) {
                List<String> relatedTableColumns = relatedTableColumnsProvider.apply(selectedRelatedTable);
                relatedTableColumnDropdown.getItems().clear();
                relatedTableColumnDropdown.getItems().addAll(relatedTableColumns);
            }
        });

        ComboBox<String> relationshipTypeDropdown = new ComboBox<>();
        relationshipTypeDropdown.getItems().addAll("one-to-one", "one-to-many", "many-to-one", "many-to-many");
        relationshipTypeDropdown.setPromptText("Select Relationship Type");

        TextField throughTableField = new TextField();
        throughTableField.setPromptText("Through Table (Optional)");

        VBox layout = new VBox(
                new Label("Base Table:"), baseTableDropdown,
                new Label("Base Table Foreign Key:"), baseTableColumnDropdown,
                new Label("Related Table:"), relatedTableDropdown,
                new Label("Related Table Foreign Key:"), relatedTableColumnDropdown,
                new Label("Relationship Type:"), relationshipTypeDropdown,
                new Label("Through Table:"), throughTableField);
        dialog.getDialogPane().setContent(layout);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return List.of(
                        baseTableDropdown.getValue(),
                        baseTableColumnDropdown.getValue(),
                        relatedTableDropdown.getValue(),
                        relatedTableColumnDropdown.getValue(),
                        relationshipTypeDropdown.getValue(),
                        throughTableField.getText());
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    public List<String> showAddColumnDialog() {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Add Column");

        selectTableForColumnDropdown = new ComboBox<>();
        selectTableForColumnDropdown.getItems().addAll(tableNames);
        selectTableForColumnDropdown.setPromptText(SELECT_TABLE_LABEL);

        TextField columnName = new TextField();
        columnName.setPromptText("Column Name");

        ComboBox<String> columnType = new ComboBox<>();
        columnType.getItems().addAll("INT", VARCHAR, "DATE", "FLOAT", "DOUBLE");
        columnType.setPromptText("Column Type");

        TextField varcharLength = new TextField();
        varcharLength.setPromptText("Length (only for VARCHAR)");
        varcharLength.setVisible(false);

        columnType.setOnAction(e -> {
            if (VARCHAR.equals(columnType.getValue())) {
                varcharLength.setVisible(true);
            } else {
                varcharLength.setVisible(false);
                varcharLength.clear();
            }
        });

        CheckBox primaryKey = new CheckBox("Primary Key");

        VBox vbox = new VBox(selectTableForColumnDropdown, columnName, columnType, varcharLength, primaryKey);
        dialog.getDialogPane().setContent(vbox);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String type = columnType.getValue();
                if (VARCHAR.equals(type) && !varcharLength.getText().isEmpty()) {
                    type += "(" + varcharLength.getText() + ")";
                }

                return List.of(
                        selectTableForColumnDropdown.getValue(),
                        columnName.getText(),
                        type,
                        String.valueOf(primaryKey.isSelected()));
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    // Dialog to delete a table
    public String showDeleteTableDialog(List<String> tableNames) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(null, tableNames);
        dialog.setTitle("Delete Table");
        dialog.setHeaderText("Select a Table to Delete:");
        dialog.setContentText(SELECT_TABLE_LABEL);
        return dialog.showAndWait().orElse(null);
    }

    // Dialog to delete a column
    public List<String> showDeleteColumnDialog(List<String> tableNames, Function<String, List<String>> columnProvider) {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Delete Column");

        // Dropdown to select the table
        ComboBox<String> tableDropdown = new ComboBox<>();
        tableDropdown.getItems().addAll(tableNames);
        tableDropdown.setPromptText(SELECT_TABLE_LABEL);

        // Dropdown to select the column
        ComboBox<String> columnDropdown = new ComboBox<>();
        columnDropdown.setPromptText("Select Column");

        // Update the columns dynamically when a table is selected
        tableDropdown.setOnAction(e -> {
            String selectedTable = tableDropdown.getValue();
            if (selectedTable != null) {
                List<String> updatedColumns = columnProvider.apply(selectedTable); // Fetch columns for the selected
                                                                                   // table
                columnDropdown.getItems().clear();
                columnDropdown.getItems().addAll(updatedColumns);
            }
        });

        VBox layout = new VBox(10, tableDropdown, columnDropdown);
        dialog.getDialogPane().setContent(layout);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK && tableDropdown.getValue() != null && columnDropdown.getValue() != null) {
                return List.of(tableDropdown.getValue(), columnDropdown.getValue());
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    // Dialog to delete a relationship
    public List<String> showDeleteRelationshipDialog(List<String> tableNames, List<String> relationships) {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Delete Relationship");

        ComboBox<String> tableDropdown = new ComboBox<>();
        tableDropdown.getItems().addAll(tableNames);
        tableDropdown.setPromptText("Select Table");

        relatedTableDropdown = new ComboBox<>();
        tableDropdown.setOnAction(e -> {
            relatedTableDropdown.getItems().clear();
            relatedTableDropdown.getItems().addAll(relationships);
        });
        relatedTableDropdown.setPromptText("Select Related Table");

        VBox layout = new VBox(10, tableDropdown, relatedTableDropdown);
        dialog.getDialogPane().setContent(layout);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return List.of(tableDropdown.getValue(), relatedTableDropdown.getValue());
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    public String showEditTableDialog(String oldTableName) {
        TextInputDialog dialog = new TextInputDialog(oldTableName);
        dialog.setTitle("Edit Table");
        dialog.setHeaderText("Edit Table Name");
        dialog.setContentText("New Table Name:");

        return dialog.showAndWait().orElse(null);
    }

    public List<String> showEditColumnDialog(String oldColumnName) {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Column");

        TextField columnName = new TextField(oldColumnName);
        ComboBox<String> columnType = new ComboBox<>();
        columnType.getItems().addAll("INT", VARCHAR, "DATE", "FLOAT", "DOUBLE");

        TextField varcharLength = new TextField();
        varcharLength.setPromptText("Length (only for VARCHAR)");
        varcharLength.setVisible(false);

        columnType.setOnAction(e -> varcharLength.setVisible(VARCHAR.equals(columnType.getValue())));

        CheckBox primaryKey = new CheckBox("Primary Key");

        VBox vbox = new VBox(new Label("Column Name:"), columnName,
                new Label("Column Type:"), columnType,
                varcharLength, primaryKey);
        dialog.getDialogPane().setContent(vbox);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                String type = columnType.getValue();
                if (VARCHAR.equals(type) && !varcharLength.getText().isEmpty()) {
                    type += "(" + varcharLength.getText() + ")";
                }
                return List.of(columnName.getText(), type, String.valueOf(primaryKey.isSelected()));
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    public List<String> showEditRelationshipDialog() {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Relationship");

        ComboBox<String> relationshipType = new ComboBox<>();
        relationshipType.getItems().addAll("one-to-one", "one-to-many", "many-to-one", "many-to-many");
        relationshipType.setPromptText("Relationship Type");

        TextField foreignKey = new TextField();
        foreignKey.setPromptText("Foreign Key");

        TextField relatedForeignKey = new TextField();
        relatedForeignKey.setPromptText("Related Foreign Key");

        VBox vbox = new VBox(new Label("Relationship Type:"), relationshipType,
                new Label("Foreign Key:"), foreignKey,
                new Label("Related Foreign Key:"), relatedForeignKey);
        dialog.getDialogPane().setContent(vbox);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return List.of(
                        relationshipType.getValue(),
                        foreignKey.getText(),
                        relatedForeignKey.getText());
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    public String showSelectTableDialog(List<String> tableNames, String header) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(null, tableNames);
        dialog.setTitle("Select Table");
        dialog.setHeaderText(header);
        dialog.setContentText(TABLE_NAME_LABEL);
        return dialog.showAndWait().orElse(null);
    }

    public String showSelectColumnDialog(List<String> columnNames, String header) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(null, columnNames);
        dialog.setTitle("Select Column");
        dialog.setHeaderText(header);
        dialog.setContentText("Column Name:");
        return dialog.showAndWait().orElse(null);
    }

    public String showSelectRelationshipDialog(List<String> relationships, String header) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(null, relationships);
        dialog.setTitle("Select Relationship");
        dialog.setHeaderText(header);
        dialog.setContentText("Related Table:");
        return dialog.showAndWait().orElse(null);
    }
}
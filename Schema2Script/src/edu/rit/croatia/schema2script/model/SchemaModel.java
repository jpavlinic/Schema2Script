package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import model.exception.SchemaParsingException;
import model.factory.GeneratorFactory;
import model.generator.SchemaGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The SchemaModel class represents the data model for a database schema.
 * It stores tables, the source schema file, and the generated SQL script.
 * This class also provides functionality to generate SQL scripts in different formats
 * and write them to a file.
 */
public class SchemaModel {

    private static final Logger logger = LogManager.getLogger(SchemaModel.class);

    private List<Table> tables;
    private File file;
    private String sqlScript;

    /**
     * Constructs an instance of SchemaModel with an empty table list.
     */
    public SchemaModel() {
        this.tables = new ArrayList<>();
        logger.info("SchemaModel initialized with an empty table list.");
    }

    /**
     * Returns the schema file associated with this model.
     *
     * @return the schema file, or {@code null} if no file is set
     */
    public File getFile() {
        logger.debug("getFile() called.");
        if (file == null) {
            logger.warn("getFile(): File is currently null.");
        }
        return file;
    }

    /**
     * Sets the schema file associated with this model.
     *
     * @param file the schema file to set
     */
    public void setFile(File file) {
        if (file == null) {
            logger.error("Attempted to set a null file.");
        } else {
            this.file = file;
            logger.info("File set to: {}", file.getAbsolutePath());
        }
    }

    /**
     * Returns the generated SQL script.
     *
     * @return the SQL script, or an empty string if it has not been generated
     */
    public String getSqlScript() {
        logger.debug("getSqlScript() called.");
        if (sqlScript == null || sqlScript.isEmpty()) {
            logger.warn("SQL script is currently empty.");
        }
        return sqlScript;
    }

    /**
     * Sets the generated SQL script.
     *
     * @param sqlScript the SQL script to set
     */
    public void setSqlScript(String sqlScript) {
        if (sqlScript == null || sqlScript.isEmpty()) {
            logger.error("Attempted to set an invalid SQL script (null or empty).");
        } else {
            this.sqlScript = sqlScript;
            logger.info("SQL script set.");
        }
    }

    /**
     * Adds a table to the schema model.
     *
     * @param table the {@link Table} to add
     * @throws IllegalArgumentException if the table is {@code null}
     */
    public void addTable(Table table) {
        if (table == null) {
            logger.error("Attempted to add a null table.");
            throw new IllegalArgumentException("Table cannot be null");
        } else {
            tables.add(table);
            updateSchemaFile();
            logger.info("Table added: {}", table);
        }
    }

    // Method to load table names from schema.json
    public List<String> loadTableNames(String schemaFilePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File schemaFile = new File(schemaFilePath);
            List<Table> schemaTables = mapper.readValue(schemaFile, new TypeReference<List<Table>>() {
            });
            this.tables = schemaTables;
            logger.info("Schema loaded with {} tables.", schemaTables.size());
            return schemaTables.stream().map(Table::getTableName).toList();
        } catch (IOException e) {
            logger.error("Failed to load schema");
            return new ArrayList<>();
        }
    }

    /**
     * Returns the list of tables in the schema.
     *
     * @return a list of {@link Table} objects
     */
    public List<Table> getTables() {
        logger.debug("getTables() called.");
        if (tables.isEmpty()) {
            logger.warn("No tables available in the list.");
        }
        return tables;
    }

    // Method to save tables to schema.json file
    public void saveToFile(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print the JSON

        try {
            // Write the tables list to the schema.json file
            mapper.writeValue(new File(filePath), tables);
            logger.info("Schema data saved to {}", filePath);
        } catch (IOException e) {
            logger.error("Failed to save schema data to file: {}", filePath, e);
        }
    }

    // Call this method after each update to tables (e.g., after addTable,
    // addColumn, addRelationship)
    public void updateSchemaFile() {
        saveToFile("schema/schema.json"); // Assuming this is the path of schema.json
    }

    // Store Database Modifications in Data Model - Updated Method for Issue 4
    public void storeData(Object data, String tableName) {
        if (data instanceof Table table) {
            tables.add(table);
            updateSchemaFile();
            logger.info("Table added to SchemaModel");
        } else if (data instanceof Column column) {
            for (Table table : tables) {
                if (table.getTableName().equals(tableName)) {
                    table.addColumn(column);
                    updateSchemaFile();
                    logger.info("Column added to Table {}: {}", table.getTableName(), ((Column) data).getName());
                    break;
                }
            }
        } else if (data instanceof Relationship relationship) {
            for (Table table : tables) {
                if (table.getTableName().equals(tableName)) {
                    table.addRelationship(relationship);
                    updateSchemaFile();
                    logger.info("Relationship added to Table {}: {}", table.getTableName(),
                            ((Relationship) data).getRelationshipType());
                    break;
                }
            }
        } else {
            logger.error("Unsupported data type provided to storeData: {}", data.getClass().getSimpleName());
        }
    }

    public String toScript(String format) {
        try {
            GeneratorFactory factory = new GeneratorFactory();
            SchemaGenerator generator = factory.get(format);
            this.sqlScript = generator.generate(this);
            logger.info("SQL script generated");

            String filePath = "script/schema.sql";
            File outputFile = new File(filePath);

            File directory = outputFile.getParentFile();
            if (directory != null && !directory.exists()) {
                if (directory.mkdirs()) {
                    logger.info("Directory created: {}", directory.getAbsolutePath());
                } else {
                    logger.error("Failed to create directory: {}", directory.getAbsolutePath());
                }
            }

            writeSqlToFile(filePath, this.sqlScript);

        } catch (SchemaParsingException e) {
            logger.error("Script generation failed.", e);
        }
        return sqlScript;
    }

    /**
     * Writes the generated SQL script to a file.
     *
     * @param filePath  the path of the file to write
     * @param sqlScript the SQL script to write
     */
    private void writeSqlToFile(String filePath, String sqlScript) {
        File outputFile = new File(filePath);

        try (FileWriter writer = new FileWriter(outputFile, false)) {
            writer.write(sqlScript);
            logger.info("SQL script written to file: {}", filePath);
        } catch (IOException e) {
            logger.error("Failed to write SQL script to file.", e);
        }
    }

    public void deleteTable(String tableName) {
        tables.removeIf(table -> table.getTableName().equals(tableName));
        updateSchemaFile();
        logger.info("Table removed: {}", tableName);
    }

    public void deleteColumn(String tableName, String columnName) {
        for (Table table : tables) {
            if (table.getTableName().equals(tableName)) {
                table.getColumns().removeIf(column -> column.getName().equals(columnName));
                updateSchemaFile();
                logger.info("Column {} removed from table {}", columnName, tableName);
                break;
            }
        }
    }

    public void deleteRelationship(String tableName, String relatedTable) {
        for (Table table : tables) {
            if (table.getTableName().equals(tableName)) {
                table.getRelationships().removeIf(relationship -> relationship.getRelatedTable().equals(relatedTable));
                updateSchemaFile();
                logger.info("Relationship to {} removed from table {}", relatedTable, tableName);
                break;
            }
        }
    }

    public void editTableName(String oldTableName, String newTableName) {
        for (Table table : tables) {
            if (table.getTableName().equals(oldTableName)) {
                table.setTableName(newTableName);
                updateSchemaFile(); // Write the changes to the schema file
                logger.info("Table name changed from {} to {}", oldTableName, newTableName);
                return;
            }
        }
        logger.warn("Table with name {} not found. Edit aborted.", oldTableName);
    }

    public void editColumn(String tableName, String oldColumnName, Column updatedColumn) {
        for (Table table : tables) {
            if (table.getTableName().equals(tableName)) {
                for (int i = 0; i < table.getColumns().size(); i++) {
                    if (table.getColumns().get(i).getName().equals(oldColumnName)) {
                        table.getColumns().set(i, updatedColumn);
                        updateSchemaFile();
                        logger.info("Column {} in table {} updated to {}", oldColumnName, tableName, updatedColumn);
                        return;
                    }
                }
            }
        }
        logger.warn("Column {} not found in table {}", oldColumnName, tableName);
    }

    public void editRelationship(String tableName, String relatedTable, Relationship updatedRelationship) {
        for (Table table : tables) {
            if (table.getTableName().equals(tableName)) {
                for (int i = 0; i < table.getRelationships().size(); i++) {
                    if (table.getRelationships().get(i).getRelatedTable().equals(relatedTable)) {
                        table.getRelationships().set(i, updatedRelationship);
                        updateSchemaFile(); // Save changes to the schema file
                        logger.info("Relationship with {} in table {} updated to {}", relatedTable, tableName,
                                updatedRelationship);
                        return;
                    }
                }
            }
        }
        logger.warn("Relationship with {} not found in table {}", relatedTable, tableName);
    }

}
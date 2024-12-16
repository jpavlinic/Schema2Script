package model.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Column;
import model.Relationship;
import model.SchemaModel;
import model.Table;
import model.exception.SchemaParsingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * The JsonParser class is responsible for parsing JSON schema files and converting
 * them into a {@link SchemaModel} object. It implements the {@link SchemaParser} interface.
 */
public class JsonParser implements SchemaParser {

    private static final Logger logger = LogManager.getLogger(JsonParser.class);
    public static final String COL = "columns";

    /**
     * Parses a JSON schema file and converts it into a {@link SchemaModel}.
     *
     * @param schemaFile the JSON file containing the schema
     * @return a {@link SchemaModel} representation of the schema
     * @throws SchemaParsingException if an error occurs during parsing or if the
     *                                JSON format is invalid
     */
    @Override
    public SchemaModel parse(File schemaFile) throws SchemaParsingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SchemaModel schema = new SchemaModel();
        logger.info("Starting JSON schema parsing for file: {}", schemaFile.getName());

        ArrayList<String> throughTableReferences = new ArrayList<>();

        try {
            JsonNode rootNode = objectMapper.readTree(schemaFile);
            validateRootNode(rootNode);

            logger.debug("Root node is valid. Beginning to parse tables.");
            parseTables(rootNode, schema, throughTableReferences);

            markJoinTables(schema, throughTableReferences);

            logger.info("JSON schema parsing completed successfully for file: {}", schemaFile.getName());
        } catch (IOException e) {
            logger.error("Error reading the JSON schema file: {}", schemaFile.getName(), e);
            throw new SchemaParsingException("Error reading the JSON schema file", e);
        }

        return schema;
    }

    /**
     * Validates the root node of the JSON schema file.
     *
     * @param rootNode the root {@link JsonNode} of the JSON schema
     * @throws SchemaParsingException if the root node is null or not an array
     */
    private void validateRootNode(JsonNode rootNode) throws SchemaParsingException {
        if (rootNode == null || !rootNode.isArray()) {
            logger.error("Invalid JSON format: Root node is not an array.");
            throw new SchemaParsingException("Invalid JSON format: Root node must be an array of tables.");
        }
    }

    /**
     * Parses the tables from the JSON schema and adds them to the
     * {@link SchemaModel}.
     *
     * @param rootNode               the root {@link JsonNode} of the JSON schema
     * @param schema                 the {@link SchemaModel} to populate
     * @param throughTableReferences a list to collect references to join tables
     * @throws SchemaParsingException if any table is invalid
     */
    private void parseTables(JsonNode rootNode, SchemaModel schema, ArrayList<String> throughTableReferences)
            throws SchemaParsingException {
        for (JsonNode tableNode : rootNode) {
            String tableName = extractTableName(tableNode);
            Table table = new Table(tableName);

            parseColumns(tableNode, table);
            parseRelationships(tableNode, table, throughTableReferences);

            schema.addTable(table);
        }
    }

    /**
     * Extracts the table name from a JSON node.
     *
     * @param tableNode the JSON node representing a table
     * @return the name of the table
     * @throws SchemaParsingException if the table name is missing
     */
    private String extractTableName(JsonNode tableNode) throws SchemaParsingException {
        if (!tableNode.has("tableName")) {
            logger.error("Invalid JSON format: Missing 'tableName' field.");
            throw new SchemaParsingException("Invalid JSON format: Missing 'tableName' field.");
        }
        return tableNode.get("tableName").asText();
    }

    /**
     * Parses the columns of a table and adds them to the table object.
     *
     * @param tableNode the JSON node representing a table
     * @param table     the {@link Table} to populate with columns
     * @throws SchemaParsingException if columns are missing or malformed
     */
    public void parseColumns(JsonNode tableNode, Table table) throws SchemaParsingException {
        JsonNode columnsNode = tableNode.get(COL);
        if (columnsNode == null || !columnsNode.isArray()) {
            logger.error("Invalid JSON format: Missing or malformed 'columns' array in table: {}",
                    table.getTableName());
            throw new SchemaParsingException("Invalid JSON format: Missing or malformed 'columns' array in table: "
                    + table.getTableName());
        }

        logger.debug("Table {} has {} columns.", table.getTableName(), columnsNode.size());
        for (JsonNode columnNode : columnsNode) {
            Column column = parseColumn(columnNode, table.getTableName());
            table.addColumn(column);
        }
    }

    /**
     * Parses a column from a JSON node and returns it as a {@link Column}.
     *
     * @param columnNode the JSON node representing a column
     * @param tableName  the name of the table the column belongs to
     * @return the {@link Column} object
     * @throws SchemaParsingException if required fields are missing
     */
    private Column parseColumn(JsonNode columnNode, String tableName) throws SchemaParsingException {
        if (!columnNode.has("name") || !columnNode.has("type")) {
            logger.error("Invalid JSON format: Missing 'name' or 'type' in columns of table: {}", tableName);
            throw new SchemaParsingException(
                    "Invalid JSON format: Missing 'name' or 'type' in columns of table: " + tableName);
        }

        String columnName = columnNode.get("name").asText();
        String columnType = columnNode.get("type").asText();
        boolean primaryKey = columnNode.has("primaryKey") && columnNode.get("primaryKey").asBoolean();

        logger.debug("Added column: {} of type: {} to table: {}", columnName, columnType, tableName);
        return new Column(columnName, columnType, primaryKey);
    }

    /**
     * Parses the relationships of a table and adds them to the table object.
     *
     * @param tableNode              the JSON node representing a table
     * @param table                  the {@link Table} to populate with
     *                               relationships
     * @param throughTableReferences a list to collect references to join tables
     * @throws SchemaParsingException if relationships are malformed
     */
    public void parseRelationships(JsonNode tableNode, Table table, List<String> throughTableReferences)
            throws SchemaParsingException {
        JsonNode relationshipsNode = tableNode.get("relationships");
        if (relationshipsNode == null || !relationshipsNode.isArray())
            return;

        logger.debug("Table {} has {} relationships.", table.getTableName(), relationshipsNode.size());
        for (JsonNode relationshipNode : relationshipsNode) {
            Relationship relationship = parseRelationship(relationshipNode, table.getTableName());
            if (relationship.getThroughTable() != null) {
                throughTableReferences.add(relationship.getThroughTable());
            }
            table.addRelationship(relationship);
        }
    }

    /**
     * Parses a relationship from a JSON node and returns it as a
     * {@link Relationship}.
     *
     * @param relationshipNode the JSON node representing a relationship
     * @param tableName        the name of the table the relationship belongs to
     * @return the {@link Relationship} object
     * @throws SchemaParsingException if required fields are missing
     */
    private Relationship parseRelationship(JsonNode relationshipNode, String tableName) throws SchemaParsingException {
        if (!relationshipNode.has("relationshipType") || !relationshipNode.has("relatedTable")
                || !relationshipNode.has("foreignKey")) {
            logger.error("Invalid JSON format: Missing required fields in relationships of table: {}", tableName);
            throw new SchemaParsingException(
                    "Invalid JSON format: Missing required fields in relationships of table: " + tableName);
        }

        String relationshipType = relationshipNode.get("relationshipType").asText();
        String relatedTable = relationshipNode.get("relatedTable").asText();
        String foreignKey = relationshipNode.get("foreignKey").asText();
        String relatedForeignKey = relationshipNode.has("relatedForeignKey")
                ? relationshipNode.get("relatedForeignKey").asText()
                : null;
        String throughTable = relationshipNode.has("throughTable")
                ? relationshipNode.get("throughTable").asText()
                : null;

        logger.debug("Added relationship: {} with table: {} and foreignKey: {} to table: {}", relationshipType,
                relatedTable,
                foreignKey, tableName);
        return new Relationship(relationshipType, relatedTable, foreignKey, relatedForeignKey, throughTable);
    }

    /**
     * Marks tables as join tables based on the through table references.
     *
     * @param schema                 the {@link SchemaModel} containing the tables
     * @param throughTableReferences a list of table names that are join tables
     */
    private void markJoinTables(SchemaModel schema, ArrayList<String> throughTableReferences) {
        for (String throughTable : throughTableReferences) {
            for (Table joinTable : schema.getTables()) {
                if (joinTable.getTableName().equals(throughTable)) {
                    joinTable.setJoinTable(true); // Mark the table as a join table
                    logger.debug("Marked table {} as a join table.", throughTable);
                    break;
                }
            }
        }
    }
}
package edu.rit.croatia.schema2script.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Table;
import model.exception.SchemaParsingException;
import model.parser.JsonParser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * The JsonParserTest class contains unit tests for the {@link JsonParser} class.
 * It tests the methods for parsing columns and relationships in JSON schema files.
 */
public class JsonParserTest {

    private JsonParser jsonParser;
    private ObjectMapper objectMapper;

    /**
     * Initializes the test environment before each test.
     * Creates a new instance of {@link JsonParser} and {@link ObjectMapper}.
     */
    @Before
    public void setUp() {
        jsonParser = new JsonParser();
        objectMapper = new ObjectMapper();
    }

    /**
     * Tests the {@link JsonParser#parseColumns(JsonNode, Table)} method
     * with valid column data.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testParseColumns_ValidData() throws Exception {
        String jsonString = "[{\"tableName\": \"valid_table\", \"columns\": [{\"name\": \"id\", \"type\": \"int\", \"primaryKey\": true}, {\"name\": \"name\", \"type\": \"string\"}]}]";
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode tableNode = rootNode.get(0);
        Table table = new Table("valid_table");

        jsonParser.parseColumns(tableNode, table);

        assertEquals("Table should have 2 columns", 2, table.getColumns().size());
        assertEquals("First column name should be 'id'", "id", table.getColumns().get(0).getName());
        assertEquals("Second column name should be 'name'", "name", table.getColumns().get(1).getName());
    }

    /**
     * Tests the {@link JsonParser#parseColumns(JsonNode, Table)} method
     * with missing 'columns' array, expecting {@link SchemaParsingException}.
     *
     * @throws Exception if the expected exception is not thrown
     */
    @Test(expected = SchemaParsingException.class)
    public void testParseColumns_MissingColumnsArray_ThrowsException() throws Exception {
        String jsonString = "[{\"tableName\": \"missing_columns_table\"}]";
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode tableNode = rootNode.get(0);
        Table table = new Table("missing_columns_table");

        jsonParser.parseColumns(tableNode, table);
    }

    /**
     * Tests the {@link JsonParser#parseColumns(JsonNode, Table)} method
     * with malformed 'columns' structure, expecting {@link SchemaParsingException}.
     *
     * @throws Exception if the expected exception is not thrown
     */
    @Test(expected = SchemaParsingException.class)
    public void testParseColumns_MalformedColumnsArray_ThrowsException() throws Exception {
        String jsonString = "[{\"tableName\": \"malformed_columns_table\", \"columns\": \"id, name\"}]";
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode tableNode = rootNode.get(0);
        Table table = new Table("malformed_columns_table");

        jsonParser.parseColumns(tableNode, table);
    }

    /**
     * Tests the {@link JsonParser#parseRelationships(JsonNode, Table, ArrayList)}
     * method
     * with valid relationship data.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testParseRelationships_ValidData() throws Exception {
        String jsonString = "[{\"tableName\": \"order\", \"columns\": [{\"name\": \"id\", \"type\": \"int\", \"primaryKey\": true}], \"relationships\": [{\"relationshipType\": \"one-to-many\", \"relatedTable\": \"customer\", \"foreignKey\": \"customer_id\"}]}]";
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode tableNode = rootNode.get(0);
        Table table = new Table("order");

        jsonParser.parseRelationships(tableNode, table, new ArrayList<>());

        assertEquals("Table should have 1 relationship", 1, table.getRelationships().size());
        assertEquals("First relationship type should be 'one-to-many'", "one-to-many",
                table.getRelationships().get(0).getRelationshipType());
        assertEquals("Related table should be 'customer'", "customer",
                table.getRelationships().get(0).getRelatedTable());
    }

    /**
     * Tests the {@link JsonParser#parseRelationships(JsonNode, Table, ArrayList)}
     * method
     * with missing required fields in relationships, expecting
     * {@link SchemaParsingException}.
     *
     * @throws Exception if the expected exception is not thrown
     */
    @Test(expected = SchemaParsingException.class)
    public void testParseRelationships_MissingRequiredFields_ThrowsException() throws Exception {
        String jsonString = "[{\"tableName\": \"order\", \"columns\": [{\"name\": \"id\", \"type\": \"int\", \"primaryKey\": true}], \"relationships\": [{\"relationshipType\": \"one-to-many\", \"relatedTable\": \"customer\"}]}]";
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode tableNode = rootNode.get(0);
        Table table = new Table("order");

        jsonParser.parseRelationships(tableNode, table, new ArrayList<>());
    }

    /**
     * Tests the {@link JsonParser#parseRelationships(JsonNode, Table, ArrayList)}
     * method
     * with an empty relationships array, expecting no exception and no
     * relationships added.
     *
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testParseRelationships_EmptyArray_NoException() throws Exception {
        String jsonString = "[{\"tableName\": \"order\", \"columns\": [{\"name\": \"id\", \"type\": \"int\", \"primaryKey\": true}], \"relationships\": []}]";
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode tableNode = rootNode.get(0);
        Table table = new Table("order");

        jsonParser.parseRelationships(tableNode, table, new ArrayList<>());

        assertEquals("Table should have 0 relationships", 0, table.getRelationships().size());
    }
}

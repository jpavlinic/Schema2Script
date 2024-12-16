package edu.rit.croatia.schema2script.model;

import model.Column;
import model.SchemaModel;
import model.Table;
import model.exception.SchemaParsingException;
import model.generator.SqlGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * The SqlGeneratorTest class contains unit tests for the {@link SqlGenerator} class.
 * It verifies the functionality of generating SQL scripts for tables and schemas,
 * ensuring proper handling of valid and invalid data.
 */
public class SqlGeneratorTest {

    private SqlGenerator sqlGenerator;
    private SchemaModel mockSchemaModel;

    /**
     * Sets up the test environment by initializing a new {@link SqlGenerator}
     * instance.
     */
    @Before
    public void setUp() {
        sqlGenerator = new SqlGenerator();

        // Mock SchemaModel to disable file-writing operations
        mockSchemaModel = Mockito.mock(SchemaModel.class);
    }

    /**
     * Tests the {@link SqlGenerator#generateCreateTableSQL(Table)} method with a
     * table
     * containing valid columns and a primary key. Verifies that the generated SQL
     * matches
     * the expected format.
     *
     * @throws SchemaParsingException if an error occurs during SQL generation
     */
    @Test
    public void testGenerateCreateTableSQL_GoodData() throws SchemaParsingException {
        // Arrange: A table with valid columns and a primary key
        Table table = new Table("employee");
        table.addColumn(new Column("id", "INT", true));
        table.addColumn(new Column("name", "VARCHAR(100)", false));

        String expectedSQL = """
            CREATE TABLE employee (
                id INT,
                name VARCHAR(100),
                PRIMARY KEY (id)
            );
            
            """;
        // Act
        String generatedSQL = sqlGenerator.generateCreateTableSQL(table);

        // Assert
        assertEquals(expectedSQL, generatedSQL);
    }

    /**
     * Tests the {@link SqlGenerator#generateCreateTableSQL(Table)} method with a
     * table
     * that has an empty name. Expects a {@link SchemaParsingException}.
     *
     * @throws SchemaParsingException expected exception
     */
    @Test(expected = SchemaParsingException.class)
    public void testGenerateCreateTableSQL_EmptyTableName_ThrowsException() throws SchemaParsingException {
        // Arrange: A table with an empty name
        Table table = new Table("");
        table.addColumn(new Column("id", "INT", true));

        // Act
        sqlGenerator.generateCreateTableSQL(table);
    }

    /**
     * Tests the {@link SqlGenerator#generateCreateTableSQL(Table)} method with a
     * table
     * that has no columns. Expects a {@link SchemaParsingException}.
     *
     * @throws SchemaParsingException expected exception
     */
    @Test(expected = SchemaParsingException.class)
    public void testGenerateCreateTableSQL_NoColumns_ThrowsException() throws SchemaParsingException {
        // Arrange: A table with no columns
        Table table = new Table("empty_columns_table");

        // Act
        sqlGenerator.generateCreateTableSQL(table);
    }

    /**
     * Tests the {@link SqlGenerator#generate(SchemaModel)} method with a schema
     * containing valid tables. Verifies that the generated SQL matches the expected
     * format.
     *
     * @throws SchemaParsingException if an error occurs during SQL generation
     */
    @Test
    public void testGenerate_GoodData() throws SchemaParsingException {
        // Arrange: Mock behavior for the SchemaModel
        Table table1 = new Table("employee");
        table1.addColumn(new Column("id", "INT", true));
        table1.addColumn(new Column("name", "VARCHAR(100)", false));

        Table table2 = new Table("department");
        table2.addColumn(new Column("dept_id", "INT", true));
        table2.addColumn(new Column("dept_name", "VARCHAR(100)", false));

        when(mockSchemaModel.getTables()).thenReturn(java.util.List.of(table1, table2));

        String expectedSQL = """
                CREATE TABLE employee (
                    id INT,
                    name VARCHAR(100),
                    PRIMARY KEY (id)
                );
                
                CREATE TABLE department (
                    dept_id INT,
                    dept_name VARCHAR(100),
                    PRIMARY KEY (dept_id)
                );
                
                """;

        // Act
        String generatedSQL = sqlGenerator.generate(mockSchemaModel);

        // Assert
        assertEquals(expectedSQL, generatedSQL);

        // Verify that no file-writing operations were triggered
        verify(mockSchemaModel, never()).saveToFile(anyString());
    }

    /**
     * Tests the {@link SqlGenerator#generate(SchemaModel)} method with a schema
     * containing a table with an empty name. Expects a
     * {@link SchemaParsingException}.
     *
     * @throws SchemaParsingException expected exception
     */
    @Test(expected = SchemaParsingException.class)
    public void testGenerate_TableWithEmptyNameInSchema_ThrowsException() throws SchemaParsingException {
        // Arrange: Mock behavior for the SchemaModel
        Table table = new Table("");
        table.addColumn(new Column("id", "INT", true));

        when(mockSchemaModel.getTables()).thenReturn(java.util.List.of(table));

        // Act
        sqlGenerator.generate(mockSchemaModel);
    }

    /**
     * Tests the {@link SqlGenerator#generate(SchemaModel)} method with a schema
     * containing a table that has no columns. Expects a
     * {@link SchemaParsingException}.
     *
     * @throws SchemaParsingException expected exception
     */
    @Test(expected = SchemaParsingException.class)
    public void testGenerate_TableWithNoColumnsInSchema_ThrowsException() throws SchemaParsingException {
        // Arrange: Mock behavior for the SchemaModel
        Table table = new Table("empty_table");

        when(mockSchemaModel.getTables()).thenReturn(java.util.List.of(table));

        // Act
        sqlGenerator.generate(mockSchemaModel);
    }
}

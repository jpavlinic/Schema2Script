package model.generator;

import java.util.List;

import model.Column;
import model.Relationship;
import model.SchemaModel;
import model.Table;
import model.exception.SchemaParsingException;

/**
 * Implementation of {@link SchemaGenerator} that generates SQL scripts
 * for creating tables and managing relationships in databases.
 */
public class SqlGenerator implements SchemaGenerator {

    /**
     * Generates SQL scripts for the entire schema.
     *
     * @param schema the schema model containing tables and relationships.
     * @return the SQL script as a string.
     * @throws SchemaParsingException if there are errors in the schema model.
     */
    public String generate(SchemaModel schema) throws SchemaParsingException {
        StringBuilder sqlBuilder = new StringBuilder();
        List<Table> tables = schema.getTables();

        // Loop through each table in the schema
        for (Table table : tables) {
            sqlBuilder.append(generateCreateTableSQL(table));
        }

        return sqlBuilder.toString();
    }

    /**
     * Generates the SQL script for creating a single table.
     *
     * @param table the table model containing columns and relationships.
     * @return the SQL script for the table as a string.
     * @throws SchemaParsingException if the table is invalid (e.g., no name or no
     *                                columns).
     */
    public String generateCreateTableSQL(Table table) throws SchemaParsingException {
        if (table.getTableName() == null || table.getTableName().trim().isEmpty()) {
            throw new SchemaParsingException("Table name cannot be empty or null.");
        }

        if (table.getColumns() == null || table.getColumns().isEmpty()) {
            throw new SchemaParsingException("Table must have at least one column.");
        }

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ").append(table.getTableName()).append(" (\n");

        String columnsSQL = generateColumnsSQL(table);
        String relationshipsSQL = generateRelationshipsSQL(table);
        String primaryKeysSQL = generatePrimaryKeysSQL(table);

        sqlBuilder.append(columnsSQL);
        sqlBuilder.append(relationshipsSQL);
        sqlBuilder.append(primaryKeysSQL);

        // Remove trailing comma and newline if necessary
        if (sqlBuilder.charAt(sqlBuilder.length() - 2) == ',') {
            sqlBuilder.setLength(sqlBuilder.length() - 2); // Remove the last comma and newline
        }

        sqlBuilder.append(");\n\n");
        return sqlBuilder.toString();
    }

    /**
     * Generates SQL for table columns.
     *
     * @param table the table model containing columns.
     * @return the SQL script for columns as a string.
     */
    private String generateColumnsSQL(Table table) {
        StringBuilder columnsSQL = new StringBuilder();
        for (Column column : table.getColumns()) {
            columnsSQL.append("    ")
                    .append(column.getName())
                    .append(" ")
                    .append(column.getType())
                    .append(",\n");
        }
        return columnsSQL.toString();
    }

    /**
     * Generates SQL for table relationships.
     *
     * @param table the table model containing relationships.
     * @return the SQL script for relationships as a string.
     */
    private String generateRelationshipsSQL(Table table) {
        StringBuilder relationshipsSQL = new StringBuilder();
        for (Relationship relationship : table.getRelationships()) {
            if (relationship.getRelationshipType().equals("many-to-one")) {
                if (relationship.getRelatedForeignKey() == null) {
                    relationship.setRelatedForeignKey(relationship.getForeignKey());
                }
                relationshipsSQL.append("    FOREIGN KEY (")
                        .append(relationship.getForeignKey())
                        .append(") REFERENCES ")
                        .append(relationship.getRelatedTable())
                        .append("(")
                        .append(relationship.getRelatedForeignKey())
                        .append("),\n");
            }
        }
        return relationshipsSQL.toString();
    }

    /**
     * Generates SQL for table primary keys.
     *
     * @param table the table model containing primary key columns.
     * @return the SQL script for primary keys as a string.
     */
    private String generatePrimaryKeysSQL(Table table) {
        StringBuilder primaryKeys = new StringBuilder();
        for (Column column : table.getColumns()) {
            if (column.isPrimaryKey()) {
                if (primaryKeys.length() > 0) {
                    primaryKeys.append(", ");
                }
                primaryKeys.append(column.getName());
            }
        }

        if (primaryKeys.length() > 0) {
            return "    PRIMARY KEY (" + primaryKeys.toString() + ")\n";
        }

        return "";
    }
}

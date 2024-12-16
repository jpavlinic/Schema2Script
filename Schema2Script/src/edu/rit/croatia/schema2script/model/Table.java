package model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Table class represents a database table in a schema.
 * It contains the table's name, columns, relationships, and a flag indicating if it is a join table.
 */
public class Table {
    private String tableName;
    private List<Column> columns;
    private List<Relationship> relationships;
    private boolean joinTable;

    /**
     * Constructs a Table with the specified name.
     * Initializes empty lists for columns and relationships.
     *
     * @param tableName the name of the table
     */
    public Table(String tableName) {
        this.tableName = tableName;
        this.columns = new ArrayList<>();
        this.relationships = new ArrayList<>();
    }

    /**
     * Returns the name of the table.
     *
     * @return the table name
     */
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Returns the list of columns in the table.
     *
     * @return a list of {@link Column} objects
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * Adds a column to the table.
     *
     * @param column the {@link Column} to add
     */
    public void addColumn(Column column) {
        columns.add(column);
    }

    /**
     * Returns the list of relationships for the table.
     *
     * @return a list of {@link Relationship} objects
     */
    public List<Relationship> getRelationships() {
        return relationships;
    }

    /**
     * Adds a relationship to the table.
     *
     * @param relationship the {@link Relationship} to add
     */
    public void addRelationship(Relationship relationship) {
        relationships.add(relationship);
    }

    /**
     * Returns whether the table is a join table.
     *
     * @return {@code true} if the table is a join table, {@code false} otherwise
     */
    public boolean isJoinTable() {
        return joinTable;
    }

    /**
     * Sets whether the table is a join table.
     *
     * @param joinTable {@code true} to mark the table as a join table, {@code false} otherwise
     */
    public void setJoinTable(boolean joinTable) {
        this.joinTable = joinTable;
    }
}
package model;

/**
 * The Column class represents a column in a database table.
 * It stores information about the column's name, type, and whether it is a primary key.
 */
public class Column {
    private String name;
    private String type;
    private boolean primaryKey;

    /**
     * Constructs a Column with the specified name and type. By default, the column
     * is not a primary key.
     *
     * @param name the name of the column
     * @param type the data type of the column
     */
    public Column(String name, String type) {
        this.name = name;
        this.type = type;
        this.primaryKey = false;
    }

    /**
     * Constructs a Column with the specified name, type, and primary key status.
     *
     * @param name       the name of the column
     * @param type       the data type of the column
     * @param primaryKey whether the column is a primary key
     */
    public Column(String name, String type, boolean primaryKey) {
        this.name = name;
        this.type = type;
        this.primaryKey = primaryKey;
    }

    /**
     * Returns the name of the column.
     *
     * @return the column name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the data type of the column.
     *
     * @return the column type
     */
    public String getType() {
        return type;
    }

    /**
     * Returns whether the column is a primary key.
     *
     * @return {@code true} if the column is a primary key, {@code false} otherwise
     */
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    /**
     * Sets whether the column is a primary key.
     *
     * @param primaryKey {@code true} to mark the column as a primary key, {@code false} otherwise
     */
    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
}


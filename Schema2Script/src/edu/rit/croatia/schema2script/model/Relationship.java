package model;

/**
 * The Relationship class represents a relationship between database tables.
 * It stores details about the relationship type, related table, foreign keys,
 * and any intermediate (through) table used in many-to-many relationships.
 */
public class Relationship {
    private String relationshipType;
    private String relatedTable;
    private String foreignKey;
    private String relatedForeignKey;
    private String throughTable;

    /**
     * Constructs a Relationship with the specified type, related table, foreign key,
     * and related foreign key. The through table is not specified in this constructor.
     *
     * @param relationshipType   the type of the relationship (e.g., "one-to-many", "many-to-many")
     * @param relatedTable       the name of the related table
     * @param foreignKey         the foreign key column in the current table
     * @param relatedForeignKey  the foreign key column in the related table
     */
    public Relationship(String relationshipType, String relatedTable, String foreignKey, String relatedForeignKey) {
        this(relationshipType, relatedTable, foreignKey, relatedForeignKey, null);
    }

    /**
     * Constructs a Relationship with the specified type, related table, foreign key,
     * related foreign key, and through table.
     *
     * @param relationshipType   the type of the relationship (e.g., "one-to-many", "many-to-many")
     * @param relatedTable       the name of the related table
     * @param foreignKey         the foreign key column in the current table
     * @param relatedForeignKey  the foreign key column in the related table
     * @param throughTable       the name of the intermediate table, if applicable
     */
    public Relationship(String relationshipType, String relatedTable, String foreignKey, String relatedForeignKey, String throughTable) {
        this.relationshipType = relationshipType;
        this.relatedTable = relatedTable;
        this.foreignKey = foreignKey;
        this.relatedForeignKey = relatedForeignKey;
        this.throughTable = throughTable;
    }

    /**
     * Returns the type of the relationship.
     *
     * @return the relationship type (e.g., "one-to-many", "many-to-many")
     */
    public String getRelationshipType() {
        return relationshipType;
    }

    /**
     * Returns the name of the related table.
     *
     * @return the related table name
     */
    public String getRelatedTable() {
        return relatedTable;
    }

    /**
     * Returns the foreign key column in the current table.
     *
     * @return the foreign key column name
     */
    public String getForeignKey() {
        return foreignKey;
    }

    /**
     * Returns the foreign key column in the related table.
     *
     * @return the related foreign key column name
     */
    public String getRelatedForeignKey() {
        return relatedForeignKey;
    }

    /**
     * Sets the foreign key column in the related table.
     *
     * @param relatedForeignKey the foreign key column name to set
     */
    public void setRelatedForeignKey(String relatedForeignKey) {
        this.relatedForeignKey = relatedForeignKey;
    }

    /**
     * Returns the name of the intermediate table used in many-to-many relationships.
     *
     * @return the through table name, or {@code null} if not applicable
     */
    public String getThroughTable() {
        return throughTable;
    }

    /**
     * Sets the name of the intermediate table used in many-to-many relationships.
     *
     * @param throughTable the through table name to set
     */
    public void setThroughTable(String throughTable) {
        this.throughTable = throughTable;
    }
}
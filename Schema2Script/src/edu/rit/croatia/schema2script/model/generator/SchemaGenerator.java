package model.generator;

import model.SchemaModel;
import model.exception.SchemaParsingException;

/**
 * Interface for schema generators that produce SQL scripts based on schema
 * models.
 */
public interface SchemaGenerator {
    /**
     * Generates SQL scripts to create database schema.
     *
     * @param schema the schema model containing tables and relationships.
     * @return the SQL script as a string.
     * @throws SchemaParsingException if there are errors in the schema model.
     */
    public String generate(SchemaModel schema) throws SchemaParsingException;
}

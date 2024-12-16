package model.parser;

import java.io.File;
import model.SchemaModel;
import model.exception.SchemaParsingException;

/**
 * The SchemaParser interface defines the contract for parsing schema files
 * into a {@link SchemaModel}. Implementations of this interface should handle
 * specific schema file formats (e.g., JSON, XML).
 */
public interface SchemaParser {

    /**
     * Parses a schema file and converts it into a {@link SchemaModel}.
     *
     * @param schemaFile the schema file to parse
     * @return a {@link SchemaModel} representation of the schema
     * @throws SchemaParsingException if an error occurs during parsing or if the
     *                                schema file format is invalid
     */
    public SchemaModel parse(File schemaFile) throws SchemaParsingException;
}
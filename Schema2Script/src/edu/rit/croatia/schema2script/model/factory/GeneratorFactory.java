package model.factory;

import model.exception.SchemaParsingException;
import model.generator.OracleGenerator;
import model.generator.SchemaGenerator;
import model.generator.SqlGenerator;

/**
 * The GeneratorFactory class is responsible for creating instances of {@link SchemaGenerator}
 * based on the specified database format. It acts as a factory for different types of SQL generators.
 */
public class GeneratorFactory {

    /**
     * Returns an instance of {@link SchemaGenerator} based on the specified format.
     *
     * @param format the database format (e.g., "mysql", "oracle")
     * @return an instance of a concrete {@link SchemaGenerator} implementation
     * @throws SchemaParsingException if the format is null or unsupported
     */
    public SchemaGenerator get(String format) throws SchemaParsingException {
        if (format == null) {
            throw new SchemaParsingException("Format cannot be null.");
        }

        switch (format.toLowerCase()) {
            case "mysql":
                return new SqlGenerator();
            case "oracle":
                return new OracleGenerator();
            default:
                throw new SchemaParsingException("Unsupported format: " + format);
        }
    }
}

package model.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.exception.SchemaParsingException;
import model.parser.JsonParser;
import model.parser.SchemaParser;
import model.parser.XMLParser;

/**
 * Factory class to create instances of {@link SchemaParser} based on the input format.
 * Supports formats such as "json" and "xml".
 */
public class ParserFactory {

    private static final Logger logger = LogManager.getLogger(ParserFactory.class);

    /**
     * Returns an instance of {@link SchemaParser} based on the provided format.
     *
     * @param format the format of the schema, e.g., "json" or "xml".
     * @return an instance of {@link SchemaParser}.
     * @throws SchemaParsingException if the format is null or unsupported.
     */
    public SchemaParser get(String format) throws SchemaParsingException {
        logger.info("Requesting parser for format: {}", format);

        if (format == null) {
            logger.error("Format cannot be null.");
            throw new SchemaParsingException("Format cannot be null.");
        }

        switch (format.toLowerCase()) {
            case "json":
                logger.debug("Returning JsonParser.");
                return new JsonParser();
            case "xml":
                logger.debug("Returning XMLParser.");
                return new XMLParser();
            default:
                logger.error("Unsupported format: {}", format);
                throw new SchemaParsingException("Unsupported format: " + format);
        }
    }
}

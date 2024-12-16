package edu.rit.croatia.schema2script.model;

import model.exception.SchemaParsingException;
import model.factory.ParserFactory;
import model.parser.SchemaParser;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The ParserFactoryTest class contains unit tests for the {@link ParserFactory} class.
 * It verifies the behavior of the factory when retrieving parsers based on schema formats.
 */
public class ParserFactoryTest {

    private final ParserFactory parserFactory = new ParserFactory();

    /**
     * Tests the {@link ParserFactory#get(String)} method with a valid format
     * ("json").
     * Verifies that a non-null {@link SchemaParser} is returned.
     *
     * @throws SchemaParsingException if the format is invalid (not expected in this
     *                                test)
     */
    @Test
    public void testGet_ValidFormat_Json() throws SchemaParsingException {
        SchemaParser parser = parserFactory.get("json");
        assertNotNull("Parser should not be null", parser);
    }

    /**
     * Tests the {@link ParserFactory#get(String)} method with a null format.
     * Expects a {@link SchemaParsingException} to be thrown.
     *
     * @throws SchemaParsingException expected exception
     */
    @Test(expected = SchemaParsingException.class)
    public void testGet_NullFormat_ThrowsException() throws SchemaParsingException {
        parserFactory.get(null);
    }

    /**
     * Tests the {@link ParserFactory#get(String)} method with an unsupported
     * format.
     * Expects a {@link SchemaParsingException} to be thrown.
     *
     * @throws SchemaParsingException expected exception
     */
    @Test(expected = SchemaParsingException.class)
    public void testGet_UnsupportedFormat_ThrowsException() throws SchemaParsingException {
        parserFactory.get("unsupported_format");
    }
}

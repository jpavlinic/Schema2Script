package edu.rit.croatia.schema2script.model;

import model.exception.SchemaParsingException;
import model.factory.GeneratorFactory;
import model.generator.SchemaGenerator;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The GeneratorFactoryTest class contains unit tests for the {@link GeneratorFactory} class.
 * It verifies the behavior of the factory when retrieving SQL generators based on supported formats.
 */
public class GeneratorFactoryTest {

    private final GeneratorFactory generatorFactory = new GeneratorFactory();

    /**
     * Tests the {@link GeneratorFactory#get(String)} method with a valid format ("mysql").
     * Verifies that a non-null {@link SchemaGenerator} is returned.
     *
     * @throws SchemaParsingException if the format is invalid (not expected in this test)
     */
    @Test
    public void testGet_ValidFormat() throws SchemaParsingException {
        SchemaGenerator generator = generatorFactory.get("mysql");
        assertNotNull("Generator should not be null", generator);
    }

    /**
     * Tests the {@link GeneratorFactory#get(String)} method with a null format.
     * Expects a {@link SchemaParsingException} to be thrown.
     *
     * @throws SchemaParsingException expected exception
     */
    @Test(expected = SchemaParsingException.class)
    public void testGet_NullFormat() throws SchemaParsingException {
        generatorFactory.get(null);
    }

    /**
     * Tests the {@link GeneratorFactory#get(String)} method with an unsupported format.
     * Expects a {@link SchemaParsingException} to be thrown.
     *
     * @throws SchemaParsingException expected exception
     */
    @Test(expected = SchemaParsingException.class)
    public void testGet_UnsupportedFormat() throws SchemaParsingException {
        generatorFactory.get("unsupported_format");
    }
}

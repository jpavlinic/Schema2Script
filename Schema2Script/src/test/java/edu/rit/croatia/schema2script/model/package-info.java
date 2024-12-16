/**
 * The {@code edu.rit.croatia.schema2script.model} package contains unit tests for
 * validating the functionality of the core classes and methods used in the schema-to-script
 * application. These tests ensure the correctness and robustness of schema processing,
 * SQL generation, and factory methods.
 *
 * <p>This package includes tests for various components of the application, such as:</p>
 * <ul>
 *   <li>{@link GeneratorFactoryTest}: Tests the functionality of the {@link model.factory.GeneratorFactory}
 *       class, verifying SQL generator retrieval based on supported formats.</li>
 *   <li>{@link JsonParserTest}: Tests the methods of the {@link model.parser.JsonParser} class,
 *       ensuring proper parsing of columns, relationships, and schema validation for JSON files.</li>
 *   <li>{@link ParserFactoryTest}: Tests the {@link model.factory.ParserFactory} class,
 *       verifying schema parser retrieval based on file formats like JSON and XML.</li>
 *   <li>{@link SqlGeneratorTest}: Tests the {@link model.generator.SqlGenerator} class,
 *       ensuring correct SQL script generation for valid and invalid schemas.</li>
 * </ul>
 *
 * <p>These unit tests are written using JUnit 4 and include assertions and exception handling
 * to verify expected outcomes for various scenarios.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     // Running a test for GeneratorFactory
 *     @Test
 *     public void testGet_ValidFormat() throws SchemaParsingException {
 *         GeneratorFactory generatorFactory = new GeneratorFactory();
 *         SchemaGenerator generator = generatorFactory.get("mysql");
 *         assertNotNull(generator);
 *     }
 *
 *     // Running a test for SqlGenerator
 *     @Test(expected = SchemaParsingException.class)
 *     public void testGenerateCreateTableSQL_NoColumns_ThrowsException() throws SchemaParsingException {
 *         Table table = new Table("empty_table");
 *         SqlGenerator sqlGenerator = new SqlGenerator();
 *         sqlGenerator.generateCreateTableSQL(table);
 *     }
 * </pre>
 *
 * <p>The tests in this package ensure the reliability of the schema-to-script application,
 * providing a foundation for future development and enhancements.</p>
 *
 * @since 1.0
 */
package edu.rit.croatia.schema2script.model;

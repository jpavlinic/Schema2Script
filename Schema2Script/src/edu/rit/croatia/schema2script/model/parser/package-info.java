/**
 * The {@code model.parser} package contains classes and interfaces for parsing
 * schema files and converting them into {@link model.SchemaModel} objects. This
 * package supports multiple file formats, including JSON and XML, and provides
 * a uniform interface for schema parsing.
 *
 * <p>This package is designed to handle different schema file formats, allowing
 * developers to use a consistent approach to convert schema files into a model
 * representation that can be used for further processing.</p>
 *
 * <p>Key classes and interfaces include:</p>
 * <ul>
 *   <li>{@link model.parser.SchemaParser}: An interface defining the contract for
 *       schema parsers, requiring a method to parse a schema file into a {@link model.SchemaModel}.</li>
 *   <li>{@link model.parser.JsonParser}: A concrete implementation of {@link SchemaParser}
 *       that parses JSON schema files into {@link model.SchemaModel} objects.</li>
 *   <li>{@link model.parser.XMLParser}: A concrete implementation of {@link SchemaParser}
 *       that parses XML schema files into {@link model.SchemaModel} objects. The implementation
 *       also includes predefined schema structures for student-course relationships.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 *     // Create a parser factory or instantiate a parser directly
 *     SchemaParser parser = new JsonParser();
 *     
 *     // Parse a schema file
 *     File schemaFile = new File("schema.json");
 *     SchemaModel schemaModel;
 *     try {
 *         schemaModel = parser.parse(schemaFile);
 *         System.out.println("Parsed schema: " + schemaModel);
 *     } catch (SchemaParsingException e) {
 *         System.err.println("Error parsing schema: " + e.getMessage());
 *     }
 * </pre>
 *
 * @since 1.0
 */
package model.parser;

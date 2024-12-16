/**
 * The {@code model.exception} package contains custom exceptions used for error handling
 * within the schema parsing and processing framework.
 *
 * <p>This package is designed to encapsulate specific error scenarios encountered
 * while working with schema files and provide meaningful feedback to the calling methods.</p>
 *
 * <p>Key classes include:</p>
 * <ul>
 *   <li>{@link model.exception.SchemaParsingException}: Represents a custom exception 
 *       thrown when an error occurs during the parsing of a schema file. It supports
 *       detailed messages and causes to aid in debugging.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 *     try {
 *         schemaParser.parse(new File("example.json"));
 *     } catch (SchemaParsingException e) {
 *         System.err.println("Error parsing schema: " + e.getMessage());
 *     }
 * </pre>
 *
 * @since 1.0
 */
package model.exception;

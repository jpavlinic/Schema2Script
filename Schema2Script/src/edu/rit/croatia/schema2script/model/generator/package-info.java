/**
 * The {@code model.generator} package contains classes and interfaces for generating
 * SQL scripts from schema models. These generators create database schema scripts
 * tailored to specific database platforms and formats.
 *
 * <p>This package simplifies the process of converting schema models into
 * executable SQL scripts that can be used to create and manage database schemas.</p>
 *
 * <p>Key classes and interfaces include:</p>
 * <ul>
 *   <li>{@link model.generator.SchemaGenerator}: An interface defining the contract for schema
 *       generators, requiring a method to generate SQL scripts from schema models.</li>
 *   <li>{@link model.generator.SqlGenerator}: A concrete implementation of {@link SchemaGenerator}
 *       that generates SQL scripts for relational databases. Handles tables, columns, and relationships.</li>
 *   <li>{@link model.generator.OracleGenerator}: A concrete implementation of {@link SchemaGenerator}
 *       designed to produce SQL scripts specifically for Oracle databases.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 *     // Create an instance of a generator
 *     SchemaGenerator generator = new SqlGenerator();
 *     
 *     // Generate SQL script from a schema model
 *     String sqlScript = generator.generate(schemaModel);
 *     System.out.println(sqlScript);
 * </pre>
 *
 * @since 1.0
 */
package model.generator;

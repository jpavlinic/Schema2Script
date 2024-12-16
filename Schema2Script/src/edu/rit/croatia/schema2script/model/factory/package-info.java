/**
 * The {@code model.factory} package contains factory classes responsible for creating
 * instances of generators and parsers used in schema processing.
 *
 * <p>This package simplifies the process of obtaining specific implementations of
 * schema generators and parsers based on database formats or schema file formats.</p>
 *
 * <p>Key classes include:</p>
 * <ul>
 *   <li>{@link model.factory.GeneratorFactory}: Creates instances of {@link model.generator.SchemaGenerator}
 *       based on the specified database format (e.g., MySQL, Oracle).</li>
 *   <li>{@link model.factory.ParserFactory}: Creates instances of {@link model.parser.SchemaParser}
 *       based on the schema file format (e.g., JSON, XML).</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 *     // Using GeneratorFactory
 *     GeneratorFactory generatorFactory = new GeneratorFactory();
 *     SchemaGenerator generator = generatorFactory.get("mysql");
 *     generator.generate(schema);
 *
 *     // Using ParserFactory
 *     ParserFactory parserFactory = new ParserFactory();
 *     SchemaParser parser = parserFactory.get("json");
 *     SchemaModel model = parser.parse(new File("schema.json"));
 * </pre>
 *
 * @since 1.0
 */
package model.factory;

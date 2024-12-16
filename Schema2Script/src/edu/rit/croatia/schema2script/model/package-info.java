/**
 * The {@code model} package contains the core data structures and logic for representing
 * and managing database schemas. It serves as the backbone of the application,
 * encapsulating schema-related entities like tables, columns, relationships, and SQL script
 * generation logic.
 *
 * <p>This package provides the following key classes:</p>
 * <ul>
 *   <li>{@link model.SchemaModel}: Represents the entire schema, including tables, the source schema file, 
 *       and the generated SQL script. It provides methods for managing tables and generating SQL scripts 
 *       in different database formats.</li>
 *   <li>{@link model.Table}: Represents a database table, storing its name, columns, relationships, 
 *       and a flag indicating whether it is a join table.</li>
 *   <li>{@link model.Column}: Represents a column in a database table, including its name, data type, 
 *       and whether it is a primary key.</li>
 *   <li>{@link model.Relationship}: Represents a relationship between database tables, including details 
 *       about foreign keys, related tables, and any intermediate (through) table used in many-to-many relationships.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 *     // Create a new SchemaModel
 *     SchemaModel schema = new SchemaModel();
 *
 *     // Define a table
 *     Table studentTable = new Table("student");
 *     studentTable.addColumn(new Column("student_id", "INT", true));
 *     studentTable.addColumn(new Column("name", "VARCHAR(100)"));
 *
 *     // Add the table to the schema
 *     schema.addTable(studentTable);
 *
 *     // Generate SQL script
 *     String sqlScript = schema.toScript("mysql");
 *     System.out.println(sqlScript);
 * </pre>
 *
 * <p>This package is a critical part of the application, enabling seamless integration 
 * of schema management and SQL generation functionality.</p>
 *
 * @since 1.0
 */
package model;

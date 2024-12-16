/**
 * The {@code view} package provides the user interface components for interacting
 * with the schema processing application. It is responsible for displaying schema
 * information, handling file uploads, and enabling the generation of SQL scripts.
 *
 * <p>This package contains classes that define the visual layout and interactive
 * elements of the application, allowing users to interact with schema files
 * dynamically.</p>
 *
 * <p>Key classes include:</p>
 * <ul>
 *   <li>{@link view.SchemaView}: Represents the main user interface for the application.
 *       It provides components for file upload, SQL type selection, message display, and
 *       dynamic table visualization.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 *     // Create the primary stage
 *     Stage primaryStage = new Stage();
 *
 *     // Initialize the SchemaView
 *     SchemaView schemaView = new SchemaView(primaryStage);
 *
 *     // Display the user interface
 *     schemaView.display();
 * </pre>
 *
 * <p>The {@code SchemaView} is used in conjunction with the {@code SchemaController} to
 * manage user interactions and update the UI dynamically.</p>
 *
 * @since 1.0
 */
package view;

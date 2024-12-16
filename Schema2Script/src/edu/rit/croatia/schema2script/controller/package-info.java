/**
 * The {@code controller} package serves as the core of the application,
 * managing the flow of data between the model and view components in the MVC architecture.
 *
 * <p>This package contains classes that handle user actions, manage schema processing,
 * and control the application lifecycle.</p>
 *
 * <p>Key classes include:</p>
 * <ul>
 *   <li>{@link controller.SchemaRunner}: The entry point for the application. It initializes the
 *       JavaFX application lifecycle and sets up the MVC components.</li>
 *   <li>{@link controller.SchemaController}: Acts as the controller in the MVC architecture,
 *       managing interactions between {@link model.SchemaModel} and {@link view.SchemaView}.
 *       It handles schema file uploads, parsing, and SQL generation.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 *     // Start the application
 *     public static void main(String[] args) {
 *         SchemaRunner.main(args);
 *     }
 *
 *     // Set up the controller
 *     SchemaController controller = new SchemaController(new SchemaModel(), new SchemaView(stage));
 *     controller.run();
 * </pre>
 *
 * @since 1.0
 */
package controller;

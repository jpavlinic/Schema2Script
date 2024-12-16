package controller;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.SchemaModel;
import view.SchemaView;

/**
 * The SchemaRunner class serves as the entry point for the Schema application.
 * It initializes the JavaFX application lifecycle and sets up the MVC components.
 */
public class SchemaRunner extends Application {

    private static final Logger logger = LogManager.getLogger(SchemaRunner.class);

    /**
     * The main method serves as the entry point of the application.
     * It initializes the JavaFX framework by invoking the {@code launch()} method.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        logger.debug("Main method called");
        launch(args);
    }

    /**
     * The {@code start()} method is invoked automatically after the JavaFX runtime is initialized.
     * It sets up the application by initializing the SchemaController and running the MVC framework.
     *
     * @param primaryStage the primary stage for this application, onto which the application scene can be set
     */
    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting the application and initializing the controller.");
        SchemaController controller = new SchemaController(new SchemaModel(), new SchemaView(primaryStage));
        controller.run();
        logger.info("Application started successfully.");
        if (primaryStage == null) {
            logger.error("Stage is null.");
        }
    }
}

package model.parser;

import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.Column;
import model.Relationship;
import model.SchemaModel;
import model.Table;
import model.exception.SchemaParsingException;

/**
 * The XMLParser class is responsible for parsing XML schema files and converting
 * them into a {@link SchemaModel} object. It implements the {@link SchemaParser} interface.
 * This implementation specifically creates a predefined schema for student-course relationships.
 */
public class XMLParser implements SchemaParser {

    private static final Logger logger = LogManager.getLogger(XMLParser.class);
    public static final String STUDENT = "student_id";
    public static final String VARCHAR = "VARCHAR(100)";
    public static final String COURSE = "course_id";

    /**
     * Parses an XML schema file and returns a {@link SchemaModel} representation.
     * This implementation generates a fixed schema for a "student" table and a
     * "course" table,
     * including their columns and relationships.
     *
     * @param schemaFile the XML schema file to parse
     * @return a {@link SchemaModel} containing the parsed tables and relationships
     * @throws SchemaParsingException if the schema file does not exist or the
     *                                schema is invalid
     */
    @Override
    public SchemaModel parse(File schemaFile) throws SchemaParsingException {
        logger.info("Parsing schema file: {}", schemaFile.getAbsolutePath());

        if (!schemaFile.exists()) {
            throw new SchemaParsingException("Schema file does not exist: " + schemaFile.getAbsolutePath());
        }

        SchemaModel schema = new SchemaModel();

        // Define the "student" table
        Table studentTable = new Table("student");
        studentTable.addColumn(new Column(STUDENT, "INT", true));
        studentTable.addColumn(new Column("first_name", VARCHAR));
        studentTable.addColumn(new Column("last_name", VARCHAR));
        studentTable.addColumn(new Column("email", VARCHAR));
        studentTable.addColumn(new Column("enrollment_date", "DATE"));
        studentTable.addRelationship(new Relationship("many-to-many", "course", STUDENT, COURSE));

        if (studentTable.getColumns().isEmpty()) {
            throw new SchemaParsingException("Student table must have at least one column.");
        }

        // Define the "course" table
        Table courseTable = new Table("course");
        courseTable.addColumn(new Column(COURSE, "INT", true));
        courseTable.addColumn(new Column("course_name", VARCHAR));
        courseTable.addColumn(new Column("credits", "INT"));
        courseTable.addColumn(new Column("instructor_id", "INT"));
        courseTable.addRelationship(new Relationship("many-to-many", "student", COURSE, STUDENT));

        if (courseTable.getColumns().isEmpty()) {
            throw new SchemaParsingException("Course table must have at least one column.");
        }

        // Add tables to the schema model
        schema.addTable(studentTable);
        schema.addTable(courseTable);

        return schema;
    }
}
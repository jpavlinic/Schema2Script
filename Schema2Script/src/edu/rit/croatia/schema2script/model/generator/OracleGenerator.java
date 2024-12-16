package model.generator;

import model.SchemaModel;
import model.exception.SchemaParsingException;

/**
 * Implementation of {@link SchemaGenerator} that generates SQL scripts for Oracle databases.
 */
public class OracleGenerator implements SchemaGenerator {

        /**
         * Generates SQL scripts to create database schema for Oracle.
         *
         * @param schema the schema model containing tables and relationships.
         * @return the SQL script as a string.
         * @throws SchemaParsingException if there are errors in the schema model.
         */
    @Override
    public String generate(SchemaModel schema) throws SchemaParsingException {
        StringBuilder sqlBuilder = new StringBuilder();
        String formatString = ");\n\n";


        sqlBuilder.append("CREATE TABLE student (\n")
                .append("    student_id NUMBER,\n")
                .append("    first_name VARCHAR2(100),\n")
                .append("    last_name VARCHAR2(100),\n")
                .append("    email VARCHAR2(100),\n")
                .append("    enrollment_date DATE,\n")
                .append("    CONSTRAINT pk_student PRIMARY KEY (student_id)\n")
                .append(formatString);

        sqlBuilder.append("CREATE TABLE course (\n")
                .append("    course_id NUMBER,\n")
                .append("    course_name VARCHAR2(100),\n")
                .append("    credits NUMBER,\n")
                .append("    instructor_id NUMBER,\n")
                .append("    CONSTRAINT pk_course PRIMARY KEY (course_id)\n")
                .append(formatString);

        sqlBuilder.append("CREATE TABLE enrollment (\n")
                .append("    student_id NUMBER,\n")
                .append("    course_id NUMBER,\n")
                .append("    enrollment_date DATE,\n")
                .append("    CONSTRAINT pk_enrollment PRIMARY KEY (student_id, course_id),\n")
                .append("    CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES student(student_id),\n")
                .append("    CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id) REFERENCES course(course_id)\n")
                .append(formatString);

        sqlBuilder.append("COMMIT;\n");

        return sqlBuilder.toString();
    
    }
    
}
🛠️ Schema2Script Project
A collaborative Java application that converts JSON schemas into SQL scripts for database creation and management, with a strong focus on tool integration and software development best practices.

🔥 About the Project
The Schema2Script project is a group-based Java application that translates a JSON schema into a SQL script capable of creating, editing, and managing database structures. This includes the ability to create, edit, add, delete tables, columns, and relationships.
The project leverages JavaFX for the user interface (UI) and employs design patterns such as MVC for clean separation of concerns and the Factory Pattern for the generation of parsers and script generators. The project focuses on advanced development tools and quality assurance processes, including:
GitLab CI/CD for continuous integration and delivery.
SonarQube for static code analysis.
JUnit for unit testing.
Log4j for logging application events.
Dynamic code analysis to ensure runtime correctness.
Maven for dependency management and builds.
Javadoc for code documentation.

🎉 Features
📜 JSON to SQL Translation: Converts JSON schema into SQL scripts to create relational databases.
🔄 Edit Database Schema: Add, delete, and modify tables, columns, and relationships dynamically.
🔍 Tool Integration: Utilizes CI/CD (GitLab), SonarQube for static analysis, JUnit for unit testing, and Log4j for logging.
📋 Design Patterns: Implements MVC for separation of logic and UI and Factory Pattern for parser and generator creation.
🖥️ Interactive JavaFX UI: User-friendly UI with interactive features for managing schemas and database structures.
🚀 Continuous Integration/Delivery: Uses GitLab CI/CD to automate builds, tests, and code quality checks.

🛠️ Tech Stack
Languages:
Java (core logic and functionality)

Frameworks & Libraries:
JavaFX (for UI design)
JUnit (for unit testing)
Log4j (for logging application events)

Tools & Automation:
GitLab CI/CD (for continuous integration and delivery)
SonarQube (for static code analysis and code quality)
Maven (for build automation and dependency management)

Other Tools:
Dynamic Code Analysis (to ensure runtime correctness)
Javadoc (for documentation)

📡 Core Functionalities
1️⃣ JSON to SQL Translation
Input: Users provide a JSON schema that defines the structure of the database.
Processing: The JSON is parsed using Factory Pattern-generated parsers, and SQL scripts are generated accordingly.
Output: SQL script to create, edit, or delete the database structure based on the JSON definition.

2️⃣ Database Schema Management
Add Tables: Users can add new tables to the schema.
Edit Columns: Modify existing columns in a table.
Delete Relationships: Remove relationships between tables as required.

3️⃣ Design Patterns in Use
MVC (Model-View-Controller): Ensures a clear separation between the user interface (View), the data model (Model), and the business logic (Controller).
Factory Pattern: Used for the generation of parsers and generators for dynamic creation of SQL scripts.

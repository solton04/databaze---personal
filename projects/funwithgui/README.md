# Library Management System

**Author:** Jules (Agent)

## Compilation and Execution
To build the project and create a fat JAR:
```bash
mvn clean package
```

To run the application:
```bash
java -jar target/fun-with-jpa-gui-1.0.0-SNAPSHOT.jar
```
Or you can use Maven:
```bash
mvn javafx:run
```

## Database
The application uses **H2 Relational Database**. It is automatically started in embedded/file mode depending on the configuration in `database.properties`.

## Known Issues
- Currently, deleting an author that has books will fail due to referential integrity if not properly handled (cascade deleting is enabled, so it should delete associated books and loans, but may throw exceptions if other constraints exist).

## Deviations from Assignment
- N/A

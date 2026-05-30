# Library Management System - User Manual

## Configuration
The application connects to an H2 database. The connection properties can be modified in `src/main/resources/database.properties`.
Make sure to apply the `prepare-db.sql` script to initialize the tables before first use if not automatically generated.

## Navigating the Application
The main window consists of three tabs:
1. **Authors:** Manage library authors.
2. **Books:** Manage books and assign them to existing authors.
3. **Loans:** Manage book loans by specifying a borrower and loan dates.

## Usage
- **Add:** Fill in the text fields (and select from dropdowns if necessary) and click "Add".
- **Update:** Select a record from the table, modify the values in the fields, and click "Update".
- **Delete:** Select a record from the table and click "Delete".
- **Refresh:** Click "Refresh" to fetch the latest data from the database. This is useful if the database was modified externally.

### Concurrency
If another user modifies the same record you are trying to update, an error alert will be shown indicating an "Optimistic locking failure". You must refresh the view to see the latest changes and apply your modifications again.

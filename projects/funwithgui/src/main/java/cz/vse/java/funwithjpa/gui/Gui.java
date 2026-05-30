package cz.vse.java.funwithjpa.gui;

import cz.vse.java.funwithjpa.controller.Controller;
import cz.vse.java.funwithjpa.controller.DatabaseException;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import cz.vse.java.funwithjpa.model.Author;
import cz.vse.java.funwithjpa.model.Book;
import cz.vse.java.funwithjpa.model.Loan;

/**
 * Main application GUI built with JavaFX.
 * Contains tabs for managing Authors, Books, and Loans.
 */
public class Gui {
    private Controller controller;
    private Pane pnlPane;

    public Gui(Controller controller) {
        this.controller = controller;
        initGui();
    }

    private void initGui() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab authorsTab = new Tab("Authors");
        authorsTab.setContent(createAuthorsPane());

        Tab booksTab = new Tab("Books");
        booksTab.setContent(createBooksPane());

        Tab loansTab = new Tab("Loans");
        loansTab.setContent(createLoansPane());

        tabPane.getTabs().addAll(authorsTab, booksTab, loansTab);
        root.setCenter(tabPane);

        pnlPane = root;
    }

    private Pane createAuthorsPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TableView<Author> table = new TableView<>();
        ObservableList<Author> authorsData = FXCollections.observableArrayList();

        TableColumn<Author, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Author, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Author, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        table.getColumns().addAll(idCol, firstNameCol, lastNameCol);
        table.setItems(authorsData);
        table.setPrefHeight(200);

        HBox form = new HBox(10);
        TextField txtFirstName = new TextField();
        txtFirstName.setPromptText("First Name");
        TextField txtLastName = new TextField();
        txtLastName.setPromptText("Last Name");
        Button btnAdd = new Button("Add");
        Button btnUpdate = new Button("Update");
        Button btnDelete = new Button("Delete");
        Button btnRefresh = new Button("Refresh");

        form.getChildren().addAll(txtFirstName, txtLastName, btnAdd, btnUpdate, btnDelete, btnRefresh);

        Runnable refreshAuthors = () -> {
            try {
                authorsData.setAll(controller.getAllAuthors());
            } catch (DatabaseException e) {
                handleDatabaseException(e);
            }
        };

        btnAdd.setOnAction(e -> {
            try {
                Author a = new Author();
                a.setFirstName(txtFirstName.getText());
                a.setLastName(txtLastName.getText());
                controller.createAuthor(a);
                refreshAuthors.run();
                txtFirstName.clear();
                txtLastName.clear();
            } catch (DatabaseException ex) {
                handleDatabaseException(ex);
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtFirstName.setText(newSelection.getFirstName());
                txtLastName.setText(newSelection.getLastName());
            }
        });

        btnUpdate.setOnAction(e -> {
            Author selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    selected.setFirstName(txtFirstName.getText());
                    selected.setLastName(txtLastName.getText());
                    controller.updateAuthor(selected);
                    refreshAuthors.run();
                } catch (DatabaseException ex) {
                    handleDatabaseException(ex);
                }
            }
        });

        btnDelete.setOnAction(e -> {
            Author selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    controller.deleteAuthor(selected);
                    refreshAuthors.run();
                    txtFirstName.clear();
                    txtLastName.clear();
                } catch (DatabaseException ex) {
                    handleDatabaseException(ex);
                }
            }
        });

        btnRefresh.setOnAction(e -> refreshAuthors.run());

        // Initial load
        refreshAuthors.run();

        vbox.getChildren().addAll(table, form);
        return vbox;
    }

    private Pane createBooksPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TableView<Book> table = new TableView<>();
        ObservableList<Book> booksData = FXCollections.observableArrayList();

        TableColumn<Book, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(cellData -> {
            Author a = cellData.getValue().getAuthor();
            return new SimpleStringProperty(a != null ? a.getFirstName() + " " + a.getLastName() : "");
        });

        table.getColumns().addAll(idCol, titleCol, authorCol);
        table.setItems(booksData);
        table.setPrefHeight(200);

        HBox form = new HBox(10);
        TextField txtTitle = new TextField();
        txtTitle.setPromptText("Title");
        ComboBox<Author> cbAuthor = new ComboBox<>();
        cbAuthor.setPromptText("Select Author");

        Button btnAdd = new Button("Add");
        Button btnUpdate = new Button("Update");
        Button btnDelete = new Button("Delete");
        Button btnRefresh = new Button("Refresh");

        form.getChildren().addAll(txtTitle, cbAuthor, btnAdd, btnUpdate, btnDelete, btnRefresh);

        Runnable refreshBooks = () -> {
            try {
                booksData.setAll(controller.getAllBooks());
                cbAuthor.setItems(FXCollections.observableArrayList(controller.getAllAuthors()));
            } catch (DatabaseException e) {
                handleDatabaseException(e);
            }
        };

        btnAdd.setOnAction(e -> {
            try {
                Book b = new Book();
                b.setTitle(txtTitle.getText());
                Author selectedAuthor = cbAuthor.getSelectionModel().getSelectedItem();
                if (selectedAuthor == null) {
                    showErrorAlert("Validation Error", "Please select an author.");
                    return;
                }
                b.setAuthor(selectedAuthor);
                controller.createBook(b);
                refreshBooks.run();
                txtTitle.clear();
                cbAuthor.getSelectionModel().clearSelection();
            } catch (DatabaseException ex) {
                handleDatabaseException(ex);
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtTitle.setText(newSelection.getTitle());
                cbAuthor.getSelectionModel().select(newSelection.getAuthor());
            }
        });

        btnUpdate.setOnAction(e -> {
            Book selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    selected.setTitle(txtTitle.getText());
                    Author selectedAuthor = cbAuthor.getSelectionModel().getSelectedItem();
                    if (selectedAuthor == null) {
                        showErrorAlert("Validation Error", "Please select an author.");
                        return;
                    }
                    selected.setAuthor(selectedAuthor);
                    controller.updateBook(selected);
                    refreshBooks.run();
                } catch (DatabaseException ex) {
                    handleDatabaseException(ex);
                }
            }
        });

        btnDelete.setOnAction(e -> {
            Book selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    controller.deleteBook(selected);
                    refreshBooks.run();
                    txtTitle.clear();
                    cbAuthor.getSelectionModel().clearSelection();
                } catch (DatabaseException ex) {
                    handleDatabaseException(ex);
                }
            }
        });

        btnRefresh.setOnAction(e -> refreshBooks.run());

        // Initial load
        refreshBooks.run();

        vbox.getChildren().addAll(table, form);
        return vbox;
    }

    private Pane createLoansPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TableView<Loan> table = new TableView<>();
        ObservableList<Loan> loansData = FXCollections.observableArrayList();

        TableColumn<Loan, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Loan, String> borrowerCol = new TableColumn<>("Borrower");
        borrowerCol.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));

        TableColumn<Loan, String> bookCol = new TableColumn<>("Book");
        bookCol.setCellValueFactory(cellData -> {
            Book b = cellData.getValue().getBook();
            return new SimpleStringProperty(b != null ? b.getTitle() : "");
        });

        TableColumn<Loan, LocalDate> loanDateCol = new TableColumn<>("Loan Date");
        loanDateCol.setCellValueFactory(new PropertyValueFactory<>("loanDate"));

        TableColumn<Loan, LocalDate> returnDateCol = new TableColumn<>("Return Date");
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        table.getColumns().addAll(idCol, borrowerCol, bookCol, loanDateCol, returnDateCol);
        table.setItems(loansData);
        table.setPrefHeight(200);

        HBox form = new HBox(10);
        TextField txtBorrower = new TextField();
        txtBorrower.setPromptText("Borrower Name");
        ComboBox<Book> cbBook = new ComboBox<>();
        cbBook.setPromptText("Select Book");
        DatePicker dpLoanDate = new DatePicker(LocalDate.now());
        DatePicker dpReturnDate = new DatePicker();
        dpReturnDate.setPromptText("Return Date");

        Button btnAdd = new Button("Add");
        Button btnUpdate = new Button("Update");
        Button btnDelete = new Button("Delete");
        Button btnRefresh = new Button("Refresh");

        form.getChildren().addAll(txtBorrower, cbBook, dpLoanDate, dpReturnDate, btnAdd, btnUpdate, btnDelete, btnRefresh);

        Runnable refreshLoans = () -> {
            try {
                loansData.setAll(controller.getAllLoans());
                cbBook.setItems(FXCollections.observableArrayList(controller.getAllBooks()));
            } catch (DatabaseException e) {
                handleDatabaseException(e);
            }
        };

        btnAdd.setOnAction(e -> {
            try {
                Loan l = new Loan();
                l.setBorrowerName(txtBorrower.getText());
                l.setLoanDate(dpLoanDate.getValue());
                l.setReturnDate(dpReturnDate.getValue());
                Book selectedBook = cbBook.getSelectionModel().getSelectedItem();
                if (selectedBook == null) {
                    showErrorAlert("Validation Error", "Please select a book.");
                    return;
                }
                l.setBook(selectedBook);
                controller.createLoan(l);
                refreshLoans.run();
                txtBorrower.clear();
                cbBook.getSelectionModel().clearSelection();
                dpLoanDate.setValue(LocalDate.now());
                dpReturnDate.setValue(null);
            } catch (DatabaseException ex) {
                handleDatabaseException(ex);
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtBorrower.setText(newSelection.getBorrowerName());
                cbBook.getSelectionModel().select(newSelection.getBook());
                dpLoanDate.setValue(newSelection.getLoanDate());
                dpReturnDate.setValue(newSelection.getReturnDate());
            }
        });

        btnUpdate.setOnAction(e -> {
            Loan selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    selected.setBorrowerName(txtBorrower.getText());
                    selected.setLoanDate(dpLoanDate.getValue());
                    selected.setReturnDate(dpReturnDate.getValue());
                    Book selectedBook = cbBook.getSelectionModel().getSelectedItem();
                    if (selectedBook == null) {
                        showErrorAlert("Validation Error", "Please select a book.");
                        return;
                    }
                    selected.setBook(selectedBook);
                    controller.updateLoan(selected);
                    refreshLoans.run();
                } catch (DatabaseException ex) {
                    handleDatabaseException(ex);
                }
            }
        });

        btnDelete.setOnAction(e -> {
            Loan selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    controller.deleteLoan(selected);
                    refreshLoans.run();
                    txtBorrower.clear();
                    cbBook.getSelectionModel().clearSelection();
                    dpLoanDate.setValue(LocalDate.now());
                    dpReturnDate.setValue(null);
                } catch (DatabaseException ex) {
                    handleDatabaseException(ex);
                }
            }
        });

        btnRefresh.setOnAction(e -> refreshLoans.run());

        // Initial load
        refreshLoans.run();

        vbox.getChildren().addAll(table, form);
        return vbox;
    }

    public Pane getPane() {
        return pnlPane;
    }

    public static void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Shows an error dialog for a generic DatabaseException.
     *
     * @param ex The DatabaseException to report.
     */
    public static void handleDatabaseException(DatabaseException ex) {
        showErrorAlert("Database Error", ex.getMessage());
    }
}

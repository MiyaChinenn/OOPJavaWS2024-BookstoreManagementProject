package org.example.bookstoremanagementsystemversion2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManagementController extends PageController implements Initializable, Notification {

    @FXML
    private TextField name_field;

    @FXML
    private TextField price_field;

    @FXML
    private TextField publisher_field;

    @FXML
    private TextField quantity_field;

    @FXML
    private Button signOut_button;

    @FXML
    private TableView<BookData> bookTable;

    @FXML
    private TableColumn<BookData, String> table_bookId_column;

    @FXML
    private TableColumn<BookData, String> table_name_column;

    @FXML
    private TableColumn<BookData, String> table_price_column;

    @FXML
    private TableColumn<BookData, String> table_publisher_column;

    @FXML
    private TableColumn<BookData, String> table_quantity_column;

    @FXML
    private TableColumn<BookData, String> table_type_column;

    @FXML
    private Label numCustomer_label;

    @FXML
    private Label numProductSold_label;

    @FXML
    private Label todayRevenue_label;

    @FXML
    private Label totalRevenue_label;

    @FXML
    private ComboBox<String> bookType_combobox;

    // Only here to track if a record has ever been selected
    private int bookId = -1;

    // Set the book type selection menu
    private void setBookTypeComboBox() {
        BookstoreDataModel dataModel = new BookstoreDataModel();
        ArrayList<String> categoryList = dataModel.getCategoryList();

        ObservableList<String> categoryArrayList = FXCollections.observableArrayList(categoryList);
        bookType_combobox.setItems(categoryArrayList);
    }

    // Get the entire book list from the database
    private void setBookList() {
           main_DataModel.setBookList(null);
    }

    // display the books on the table
    private void showBooks() {
        setBookList();
        ObservableList<BookData> bookList = main_DataModel.getBookList();

        table_bookId_column.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        table_name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        table_price_column.setCellValueFactory(new PropertyValueFactory<>("price"));
        table_publisher_column.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        table_quantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        table_type_column.setCellValueFactory(new PropertyValueFactory<>("type"));

        bookTable.setItems(bookList);
    }

    // Show how many customers are registered in the system
    private void showNumberOfCustomer() {
        int numberOfCustomer = main_DataModel.getNumberOfCustomer();
        numCustomer_label.setText(Integer.toString(numberOfCustomer));
    }

    // show how much we have sold today
    private void showTodayRevenue() {
        double todayRevenue = main_DataModel.getTodayRevenue();
        todayRevenue_label.setText(Double.toString(todayRevenue));
    }

    // show how much we have sold so far
    private void showTotalRevenue() {
        double totalRevenue = main_DataModel.getTotalRevenue();
        totalRevenue_label.setText(Double.toString(totalRevenue));
    }


    // show the number of product have been sold so far
    private void showNumberProductSold() {
        int numProductSold = main_DataModel.getNumberProductSold();
        numProductSold_label.setText(Integer.toString(numProductSold));
    }

    // When an item is selected from the table
    // Display its information on the appropriate fields
    public void getTableSelectionData() {
        BookData bookData = bookTable.getSelectionModel().getSelectedItem();
        int rowIndex = bookTable.getSelectionModel().getSelectedIndex();

        if (rowIndex -1 < -1) {
            return;
        }

        this.bookId = bookData.getBookId();
        name_field.setText(bookData.getName());
        price_field.setText(String.valueOf(bookData.getPrice()));
        publisher_field.setText(bookData.getPublisher());
        quantity_field.setText(String.valueOf(bookData.getQuantity()));

        for (String bookType : bookType_combobox.getItems()) {
            if (bookType.equals(bookData.getType())) {
                bookType_combobox.getSelectionModel().select(bookType);
            }
        }
    }

    // When the sign-out button is clicked
    // Prompt the user is they want are sure
    // Yes? Sign them out
    public void onSignOutButtonClick() {
        Alert alert = showConfirmMessage("Are you sure to sign out?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            switchPage("Login-view.fxml");
            signOut_button.getScene().getWindow().hide();

        }
    }

    // Add a book to the database
    private void addBook() {
        main_DataModel.insertToBooksTable(new String[] {bookType_combobox.getSelectionModel().getSelectedItem(), name_field.getText(), price_field.getText(),
                publisher_field.getText(), quantity_field.getText()});

        showInfoMessage("BookData added successfully");

        onClearButtonClick();
    }

    // When clicked add books with the provided information to the database
    public void onAddButtonClick() {
        // All fields must be entered
        if (name_field.getText().isEmpty() || bookType_combobox.getSelectionModel().getSelectedItem() == null ||
                price_field.getText().isEmpty() || publisher_field.getText().isEmpty() ||
                quantity_field.getText().isEmpty()) {
            showErrorMessage("Please fill all the fields");
            return;
        }

        // Do some verifications

        boolean isBookNameExist = main_DataModel.isBookNameExist(new String[] {bookType_combobox.getSelectionModel().getSelectedItem(),
                name_field.getText()});

        // When a book name and type has already in the database
        // Prompt the admin if he/she still want to add it in the database
        // Yes? Add to the database
        // No? Don't

        // Nothing happened. Just add
        if (isBookNameExist) {
            Alert alert = showConfirmMessage("BookData already exists \n" +
                                "Are you sure you want to add this book");
            if (alert.showAndWait().get() == ButtonType.OK) {
                addBook();
            }
        }
        else {
            addBook();
        }
        showBooks();
    }

    // Clear all the fields
    public void onClearButtonClick() {
        name_field.setText("");
        bookType_combobox.getSelectionModel().clearSelection();
        price_field.setText("");
        publisher_field.setText("");
        quantity_field.setText("");
        bookId = -1;
    }

    // Update the information of the selected book
    public void onUpdateButtonClick() {
        if (bookId == -1) {
            showErrorMessage("No book is selected");
            return;
        }

        main_DataModel.updateBooksTable(new String[] {bookType_combobox.getSelectionModel().getSelectedItem(), name_field.getText(),
                price_field.getText(), publisher_field.getText(),
                quantity_field.getText(), String.valueOf(bookId)});

        showInfoMessage("BookData updated successfully");

        showBooks();
    }

    // Delete the selected book from the database
    public void onDeleteButtonClick() {
        if (bookId == -1) {
            showErrorMessage("No book is selected");
            return;
        }

        main_DataModel.deleteFromBooksTable(String.valueOf(bookId));

        showInfoMessage("BookData deleted successfully");

        showBooks();

    }


    @Override
    public void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void showInfoMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public Alert showConfirmMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showBooks();
        showNumberOfCustomer();
        showTodayRevenue();
        showTotalRevenue();
        showNumberProductSold();
        setBookTypeComboBox();
    }
}

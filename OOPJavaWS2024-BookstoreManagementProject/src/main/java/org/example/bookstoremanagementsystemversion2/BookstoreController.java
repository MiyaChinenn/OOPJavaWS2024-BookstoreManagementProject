package org.example.bookstoremanagementsystemversion2;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class BookstoreController extends PageController implements Initializable, Notification {

    @FXML
    private Label cart_bookId_label;

    @FXML
    private Label cart_bookName_label;

    @FXML
    private Spinner<Integer> cart_quantityt_spinner;

    @FXML
    private Label cart_totalPrice_label;

    @FXML
    private ComboBox<String> catergory_combobox;

    @FXML
    private Button firstNameUpdate_button;

    @FXML
    private GridPane gridPane;

    @FXML
    private Button home_button;

    @FXML
    private AnchorPane home_screen;

    @FXML
    private Button lastNameUpdate_button;

    @FXML
    private Button phoneNumberUpdate_button;

    @FXML
    private TextField searchBar_field;

    @FXML
    private Button shoppingCart_button;

    @FXML
    private AnchorPane shoppingCart_screen;

    @FXML
    private TableView<ShoppingCartData> shoppingCart_table;

    @FXML
    private Button signOut_button;

    @FXML
    private TableColumn<ShoppingCartData, String> table_bookId_column;

    @FXML
    private TableColumn<ShoppingCartData, String> table_bookName_column;

    @FXML
    private TableColumn<ShoppingCartData, String> table_price_column;

    @FXML
    private TableColumn<ShoppingCartData, String> table_quantity_column;

    @FXML
    private Button userAccount_button;

    @FXML
    private AnchorPane userAccount_screen;

    @FXML
    private TextField user_firstName_field;

    @FXML
    private Label user_firstName_label;

    @FXML
    private TextField user_lastName_field;

    @FXML
    private Label user_lastName_label;

    @FXML
    private TextField user_newPassword_field;

    @FXML
    private TextField user_oldPassword_field;

    @FXML
    private TextField user_phoneNumber_field;

    @FXML
    private Label user_phoneNumber_label;

    @FXML
    private Label user_username_label;

    @FXML
    private Label username_label;

    @FXML
    private Label cart_totalCart_label;

       // Home screen methods

    // Overloading method
    // Set the bookList, which is use by the displayProductList(), according to the search filter applied by the user
    private void setBookList(String searchText) {
        main_DataModel.setBookList(searchText);
    }

    // Overloading method
    // Set the bookList, which is use by the displayProductList(), according to the search filter applied by the user
    private void setBookList(String category, String searchText) {
        main_DataModel.setBookList(category, searchText);
    }

    // Display the product of the bookstore on the screen
    private void displayProductList() {
        ObservableList<BookData> bookList = main_DataModel.getBookList();
        // Items are displayed in a grid pane
        int row = 0;
        int column = 0;

        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        // Load all product in the this.bookList, then display item to the screen
        // for each item, load a ProductDetailCard with the passed in information
        for (BookData item : bookList) {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("ProductDetailCard-view.fxml"));
                AnchorPane pane = fxmlLoader.load();

                ProductDetailCardController cardController = fxmlLoader.getController();
                cardController.setCardData(item);

                if (column == 5) {
                    column = 0;
                    row++;
                }
                gridPane.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(3));

            }

            catch(Exception e){
                System.out.println("DisplayProductList() Error" + e.getMessage());
            }
        }
    }

    // Display the current username when login
    private void displayUsername() {
        username_label.setText(main_Username);
    }

    // Set the values of the category selection menu
    private void setCategoryComboBox() {
        ArrayList<String> categoryList = main_DataModel.getCategoryList();
        categoryList.addFirst("All Categories");
        ObservableList<String> categoryComboBoxData = FXCollections.observableArrayList(categoryList);

        catergory_combobox.setItems(categoryComboBoxData);
        catergory_combobox.getSelectionModel().selectFirst();
    }

    // Display product according the searches
    public void onSearchButtonClick() {
        // This determines how the this.bookList is set
        // When the appropriate items is set the this.bookList
        // Call the displayProductList method to display items on the screen
        if (catergory_combobox.getSelectionModel().getSelectedItem().equals("All Categories")) {
            if (searchBar_field.getText().isEmpty()) {
                setBookList(null);
            }
            else {
                setBookList(searchBar_field.getText());
            }
            displayProductList();
        }
        else {
            if (searchBar_field.getText().isEmpty()) {
                setBookList(catergory_combobox.getSelectionModel().getSelectedItem(), "");
            }
            else {
                setBookList(catergory_combobox.getSelectionModel().getSelectedItem(), searchBar_field.getText());
            }
            displayProductList();
        }
    }

    // When sign out button is clicked
    // Prompt the user whether they are sure they want to sign out
    // Yes? sign them out
    public void onSignOutButtonClick() {
        Alert alert = showConfirmMessage("Are you sure to sign out?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            switchPage("Login-view.fxml");
            signOut_button.getScene().getWindow().hide();
        }
    }


    // User Account screen methods

    // Set the user details
    private void setUserScreenDetail() {
        try {
            ResultSet resultSet = main_DataModel.getUserData(main_Username);
            if (resultSet.next()) {
                user_username_label.setText(main_Username);
                user_firstName_label.setText(resultSet.getString("firstName"));
                user_lastName_label.setText(resultSet.getString("lastName"));
                user_phoneNumber_label.setText(resultSet.getString("phoneNumber"));
            }
        }
        catch (Exception e) {
            System.out.println("SetUserScreenDetail() Error" + e.getMessage());
        }
    }

    // Let the user change their password
    public void onUserScreenChangePasswordButtonClick() {
        // All fields must be entered
        if (user_oldPassword_field.getText().isEmpty() || user_newPassword_field.getText().isEmpty()) {
            showErrorMessage("Please enter both fields");
            return;
        }

        try {
            String userPassword = "";

            ResultSet resultSet = main_DataModel.getUserData(main_Username);

            // Check if the old password matches the existing one
            // No? Do not let them update
            // Yes? check if the new password violate any password rules ( less than 8 characters long, and/or contains
            // special characters
            if (resultSet.next()) {
                userPassword = resultSet.getString("password");
            }

            if (!user_oldPassword_field.getText().equals(userPassword)) {
                showErrorMessage("Incorrect password");
            }
            else if (isViolate(user_newPassword_field.getText()) || user_newPassword_field.getLength() < 8) {
                showErrorMessage("Password must be at least 8 characters and MUST" +
                        " NOT contain any special characters");
            }
            else {
                main_DataModel.updateUserPassword(new String[] {user_newPassword_field.getText(), main_Username});
                showInfoMessage("Password updated successfully");
                user_oldPassword_field.clear();
                user_newPassword_field.clear();
            }
        }
        catch (Exception e) {
            System.out.println("OnUserScreenChangePasswordButtonClick() Error" + e.getMessage());
        }


    }

    public void onUserScreenUpdateButtonClick(ActionEvent event) {
        // Check which update button is clicked
        // Then update the associated information accordingly
        if (event.getSource() == firstNameUpdate_button) {
            if (user_firstName_field.getText().isEmpty()) {
                showErrorMessage("First name field is empty");
            }
            else {
                main_DataModel.updateUserData(new String[] {user_firstName_field.getText(), user_lastName_label.getText(),
                                                user_phoneNumber_label.getText(), main_Username});
                showInfoMessage("First name updated");
                user_firstName_field.clear();
                setUserScreenDetail();
            }
        }
        else if (event.getSource() == lastNameUpdate_button) {
            if (user_lastName_field.getText().isEmpty()) {
                showErrorMessage("Last name field is empty");
            }
            else {
                main_DataModel.updateUserData(new String[] {user_firstName_label.getText(), user_lastName_field.getText(),
                        user_phoneNumber_label.getText(), main_Username});

                showInfoMessage("Last name updated");
                user_lastName_field.clear();
                setUserScreenDetail();
            }
        }
        else if (event.getSource() == phoneNumberUpdate_button) {
            if (user_phoneNumber_field.getText().isEmpty()) {
                showErrorMessage("Phone number field is empty");
            }
            else {
                main_DataModel.updateUserData(new String[] {user_firstName_label.getText(), user_lastName_label.getText(),
                        user_phoneNumber_field.getText(), main_Username});

                showInfoMessage("Phone number updated");
                user_phoneNumber_field.clear();
                setUserScreenDetail();
            }
        }
    }


    // Shopping Cart screen methods

    // Display the items in the shopping cart of the current user
    private void showShoppingCart() {
        ObservableList<ShoppingCartData> shoppingCartList = main_DataModel.getShoppingCartList();

        table_bookId_column.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        table_bookName_column.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        table_quantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        table_price_column.setCellValueFactory(new PropertyValueFactory<>("price"));

        shoppingCart_table.setItems(shoppingCartList);

        showTotalCart();

    }

    // Display the total cart label
    private void showTotalCart() {
        double totalCartAmount = main_DataModel.getTotalCartAmount(main_Username);
        cart_totalCart_label.setText(String.valueOf(totalCartAmount));
    }

    // When Pay button is clicked, proceed through the payment process
    public void onCartPayButtonClick() {
        // shopping is empty, payment error
        if (shoppingCart_table.getItems().isEmpty()) {
            showErrorMessage("Shopping cart is empty");
        }
        // When payment success
        else {
            try {
                // First we insert a new record to the orders table in our bookstore database
                Date today = new Date();
                java.sql.Date sqlDate = new java.sql.Date(today.getTime());
                main_DataModel.insertIntoOrdersTable(new String[] {main_Username, sqlDate.toString()});

                // We need the newly inserted bookId in the orders table to insert values to the orderDetails table

                int orderId = main_DataModel.getMaxOrderId();

                if (orderId == -1) {
                    showErrorMessage("System error, payment unsuccessful");
                    return;
                }

                // With the acquired orderId, we can now insert items into the orderDetails table
                // Since the shoppingcart table and the orderDetails are very much the same
                // We can take the values of shoppingcart to be the insertion values of orderDetails

                String selectStatement = "SELECT bookId, quantity, price FROM shoppingcart where username = ?";

                ResultSet resultSet = main_DataModel.getShoppingCartList(main_Username);


                double quantity;
                int bookId;
                double price;

                // When a customer pay for a certain number of products
                // These number of products must also be deducted from the books table as well
                // So, for each record in the shoppingcart table, we use that data to update the books table,
                // and insert data to the orderDetails table
                while (resultSet.next()) {
                    quantity = resultSet.getDouble("quantity");
                    bookId = resultSet.getInt("bookId");
                    price = resultSet.getDouble("price");

                    // update books table
                    main_DataModel.updateBooksTable(new String[] {String.valueOf(quantity), String.valueOf(bookId)});

                    // insert into orderDetails table
                    main_DataModel.insertIntoOrderDetailsTable(new String[] {String.valueOf(orderId), String.valueOf(bookId),
                            String.valueOf(quantity), String.valueOf(price)});
                }

                // When finished, remove record of the current user in the shoppingcart table
                main_DataModel.deleteFromShoppingCartTable(new String[] {main_Username});

                showInfoMessage("Payment complete \n$" + Double.parseDouble(cart_totalCart_label.getText()) +
                        " has been deducted from your account");

                showShoppingCart();
            }
            catch (Exception e) {
                System.out.println("OnCartPayButtonClick Error " + e.getMessage() );
            }
        }
    }

    // When the Remove button is clicked, remove the selected item from the cart
    // Then clear the info fields
    public void onCartRemoveButtonClick() {
        if (cart_bookId_label.getText().isEmpty()) {
            showErrorMessage("No item is selected");
            return;
        }

        main_DataModel.deleteFromShoppingCartTable(new String[] {main_Username, cart_bookId_label.getText()});

        cart_bookId_label.setText("");
        cart_bookName_label.setText("");
        cart_totalPrice_label.setText("");

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0);
        valueFactory.setValue(0);
        cart_quantityt_spinner.setValueFactory(valueFactory);

        showShoppingCart();
    }

    // When the Update button is clicked, update the quantity of the selected item
    // If the quantity is set to 0, remove it from the cart
    public void onCartUpdateButtonClick() {
        if (cart_bookId_label.getText().isEmpty()) {
            showErrorMessage("No item is selected");
            return;
        }

        if (cart_quantityt_spinner.getValue() == 0) {
            onCartRemoveButtonClick();
        }
        else {
            main_DataModel.updateShoppingCartTable(new String[] {cart_quantityt_spinner.getValue().toString(),
                    main_Username, cart_bookId_label.getText()});

            showShoppingCart();
        }
    }

    // When a record in the shopping cart is selected
    // Display its information on fields
    public void getCartTableSelectionData() {
        ShoppingCartData shoppingCartData = shoppingCart_table.getSelectionModel().getSelectedItem();
        int rowIndex = shoppingCart_table.getSelectionModel().getSelectedIndex();

        if (rowIndex -1 < -1) {
            return;
        }

        cart_bookId_label.setText(String.valueOf(shoppingCartData.getBookId()));
        cart_bookName_label.setText(shoppingCartData.getBookName());

        NumberFormat formater = new DecimalFormat("#0.00");
        double totalPrice = Double.parseDouble(formater.format(shoppingCartData.getPrice() *
                (double)shoppingCartData.getQuantity()));

        cart_totalPrice_label.setText("$" + totalPrice);

        int maxQuantity = main_DataModel.getBookQuantity(shoppingCartData.getBookId());

        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxQuantity);
        valueFactory.setValue(shoppingCartData.getQuantity());
        cart_quantityt_spinner.setValueFactory(valueFactory);

    }


    // Other methods

    public void switchScreen(ActionEvent event) {
        if (event.getSource() == home_button) {
            setBookList(null);
            displayProductList();
            home_screen.setVisible(true);
            shoppingCart_screen.setVisible(false);
            userAccount_screen.setVisible(false);
        }
        else if (event.getSource() == shoppingCart_button) {
            showShoppingCart();
            shoppingCart_screen.setVisible(true);
            userAccount_screen.setVisible(false);
            home_screen.setVisible(false);
        }
        else if (event.getSource() == userAccount_button) {
            userAccount_screen.setVisible(true);
            shoppingCart_screen.setVisible(false);
            home_screen.setVisible(false);
        }
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
        setBookList(null);
        displayProductList();

        displayUsername();
        setCategoryComboBox();

        setUserScreenDetail();

        showShoppingCart();
    }

}

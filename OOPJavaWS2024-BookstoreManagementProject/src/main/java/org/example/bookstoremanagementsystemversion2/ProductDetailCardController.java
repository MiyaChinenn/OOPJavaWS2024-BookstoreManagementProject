package org.example.bookstoremanagementsystemversion2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProductDetailCardController extends PageController implements Initializable {

    @FXML
    private Label bookName_label;

    @FXML
    private Label price_label;

    @FXML
    private Label quantity_label;

    @FXML
    private Spinner<Integer> quantity_spinner;

    private BookData bookData;


    // set the product cart information according to the bookData that passed in
    public void setCardData(BookData bookData) {
        System.out.println();
        this.bookData = bookData;

        bookName_label.setWrapText(true);
        bookName_label.setText(this.bookData.getName());
        price_label.setText("$" + this.bookData.getPrice());
        quantity_label.setText(String.valueOf(this.bookData.getQuantity()));

        SpinnerValueFactory<Integer> valueFactory;

        if (this.bookData.getQuantity() >=1) {
            valueFactory =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, this.bookData.getQuantity());
            valueFactory.setValue(1);
        }
        else {
            valueFactory =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(0, this.bookData.getQuantity());
            valueFactory.setValue(0);
        }

        quantity_spinner.setValueFactory(valueFactory);
    }

    // Add the associated item to the user shopping cart
    public void onAddToCartButtonClick() {
        if (this.bookData.getQuantity() == 0) {
            return;
        }

        // if the user has already added a book in his shopping cart,
        // we update the quantity of the product in his cart
        if (isInCart(main_Username, this.bookData.getBookId())) {

            int quantityInCart = getQuantityInCart(main_Username, this.bookData.getBookId());

            System.out.println("Quantity in car: " + quantityInCart);
            // if the quantity a user try to add plus the quantity already in cart exceed the max quantity
            // set the quantity in cart to be equal to the max quantity of an item
            if (quantityInCart + quantity_spinner.getValue() > this.bookData.getQuantity()) {

                System.out.println("If 1");
                main_DataModel.updateShoppingCartTable(new String[] {String.valueOf(this.bookData.getQuantity()),
                        main_Username, String.valueOf(this.bookData.getBookId())});

            }

            // else update the cart quantity equal to its quantity plus new quantity
            else {
                System.out.println("if 2");
                main_DataModel.updateShoppingCartTable(new String[] {String.valueOf(quantityInCart + quantity_spinner.getValue()),
                                                main_Username, String.valueOf(this.bookData.getBookId()) });
            }
        }

        // if that is not in the user shopping cart
        // then we add that book to his/her shopping cart
        else {

            main_DataModel.insertToShoppingCartTable(new String[] {main_Username, String.valueOf(this.bookData.getBookId()),
                    String.valueOf(quantity_spinner.getValue()), String.valueOf(this.bookData.getPrice())});
        }

    }

    // Check if an item is already in the current user shopping cart
    private boolean isInCart(String username, int bookId) {
        System.out.println("In IsInCart block");
        ResultSet resultSet = main_DataModel.getShoppingCartItem(username, bookId);
        try {
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    // Get the quantity of an existing item that is in the current user shopping cart
    private int getQuantityInCart(String username, int bookId) {
        try {
            System.out.println("In getQuantityInCart");
            ResultSet resultSet = main_DataModel.getShoppingCartItem(username, bookId);

            if (resultSet.next()) {
                return resultSet.getInt("quantity");
            }
        }
        catch (Exception e) {
            System.out.println("GetQuantityInCart Error " + e.getMessage());
        }

        System.out.println("quantity return 0");
        return 0;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

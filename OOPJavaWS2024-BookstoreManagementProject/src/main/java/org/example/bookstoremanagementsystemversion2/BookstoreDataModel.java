package org.example.bookstoremanagementsystemversion2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;


public class BookstoreDataModel {
    private final ObservableList<BookData> bookList = FXCollections.observableArrayList();
    private final ArrayList<String> categoryList = new ArrayList<>();
    private final ObservableList<ShoppingCartData> shoppingCartList = FXCollections.observableArrayList();

    // Polymorphism
    public void setBookList(String searchText) {
        bookList.clear();
        if (searchText == null) {
            searchText = "";
        }

        try {
            String selectStatement = "SELECT * FROM books WHERE name LIKE '%" + searchText + "%'";
            ResultSet resultSet = Database.selectQuery(selectStatement, new String[] {});

            BookData bookData;

            while (resultSet.next()) {
                bookData = new BookData(resultSet.getInt("bookId"),
                        resultSet.getString("type"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getString("publisher"),
                        resultSet.getInt("quantity"));

                bookList.add(bookData);
            }
        }
        catch (Exception e) {
            System.out.println("Failed to get book list");
        }
    }
    public void setBookList(String category, String searchText) {
        bookList.clear();
        if (searchText == null) {
            searchText = "";
        }


        try {
            String selectStatement = "SELECT * FROM books WHERE type = ? AND name LIKE '%" + searchText + "%'";
            ResultSet resultSet = Database.selectQuery(selectStatement, new String[] {category});

            BookData bookData;

            while (resultSet.next()) {
                bookData = new BookData(resultSet.getInt("bookId"),
                        resultSet.getString("type"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getString("publisher"),
                        resultSet.getInt("quantity"));

                bookList.add(bookData);
            }
        }
        catch (Exception e) {
            System.out.println("Failed to get book list");
        }


    }



    public ObservableList<BookData> getBookList() {
        return bookList;
    }

    private void setCategoryList() {
        String[] categoryListData = {"Biography", "Fantasy", "Fiction", "Manga", "Non-Fiction", "Science-Fiction"};
        categoryList.clear();
        categoryList.addAll(Arrays.asList(categoryListData));
    }

    public ArrayList<String> getCategoryList() {
        setCategoryList();
        return categoryList;
    }

    private void setShoppingCartList() {
        shoppingCartList.clear();
        String selectStatement = "SELECT s.username, s.bookId, b.name, s.quantity, s.price FROM shoppingCart s " +
                                    " JOIN books b ON s.bookId = b.bookId";

        try {
            ResultSet resultSet = Database.selectQuery(selectStatement, new String[] {});
            // ResultSet resultSet = preparedStatement.executeQuery();

            ShoppingCartData shoppingCartData;

            while (resultSet.next()) {
                shoppingCartData = new ShoppingCartData(resultSet.getString("username"),
                                        resultSet.getInt("bookId"),
                                        resultSet.getString("name"),
                                        resultSet.getInt("quantity"),
                                        resultSet.getDouble("price"));

                shoppingCartList.add(shoppingCartData);
            }
        }
        catch (Exception e) {
            System.out.println("Failed to get shopping cart list");
        }
    }

    public ObservableList<ShoppingCartData> getShoppingCartList() {
        setShoppingCartList();
        return shoppingCartList;
    }



    public ResultSet getUserData(String username) {
        try {
            String selectStatement = "SELECT * FROM users WHERE username = ?";
            return Database.selectQuery(selectStatement, new String[] {username});
        }
        catch (Exception e) {
            return null;
        }
    }

    public void updateUserData(String[] arguments) {
        try {

            String updateStatement = "UPDATE users SET firstName = ?, lastName = ?,  phoneNumber = ?" +
                    " WHERE username = ?";
            Database.updateQuery(updateStatement, arguments);
        }
        catch (Exception e) {
            System.out.println("Failed to update user data");
        }
    }

    public void updateUserPassword(String[] arguments) {
        try {
            String updatePassword = "UPDATE users SET password = ? WHERE username = ?";
            Database.updateQuery(updatePassword, arguments);
        }
        catch (Exception e) {
            System.out.println("Failed to update user password");
        }
    }



    public double getTodayRevenue() {
        try {
            String getTodayRevenue = "SELECT o.orderDate, SUM(quantity * price) " +
                    "FROM orderDetails od " +
                    "JOIN orders o ON od.orderId = o.orderId " +
                    "GROUP BY o.orderDate " +
                    "HAVING o.orderDate = CURDATE()";

            ResultSet resultSet = Database.selectQuery(getTodayRevenue, new String[] {});
            if (resultSet.next()) {
                NumberFormat formatter = new DecimalFormat("#0.00");
                return Double.parseDouble(formatter.format(resultSet.getDouble("SUM(quantity * price)")));
            }
        }
        catch (Exception e) {
            return 0.0;
        }

        return 0.0;
    }

    public double getTotalRevenue() {
        try {
            String getTotalRevenueStatement = "SELECT SUM(quantity * price) FROM orderDetails";
            ResultSet resultSet = Database.selectQuery(getTotalRevenueStatement, new String[] {});

            if (resultSet.next()) {
                NumberFormat formatter = new DecimalFormat("#0.00");
                return Double.parseDouble(formatter.format(resultSet.getDouble("SUM(quantity * price)")));
            }
        }
        catch (Exception e) {
            return 0.0;
        }
        return 0.0;
    }

    public int getNumberOfCustomer() {
        try {
            String selectStatement = "SELECT COUNT(*) FROM users";
            ResultSet resultSet  = Database.selectQuery(selectStatement, new String[] {});

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        catch (Exception e) {
            return 0;
        }

        return 0;
    }

    public int getNumberProductSold() {
        try {
            String getNumberProductSoldStatement = "SELECT SUM(quantity) FROM orderDetails";
            ResultSet resultSet = Database.selectQuery(getNumberProductSoldStatement, new String[] {});
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        catch (Exception e) {
            return 0;
        }
        return 0;
    }


    public int getMaxOrderId() {
        try {
            String getMaxOrderIdStatement = "SELECT MAX(orderId) FROM orders";
            ResultSet resultSet = Database.selectQuery(getMaxOrderIdStatement, new String[] {});
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        catch (Exception e) {
            return -1;
        }
        return -1;
    }

    public void insertIntoOrdersTable(String[] arguments) {
        try {
            String insertStatement = "INSERT INTO orders(username, orderDate) VALUES(?, ?)";
            Database.updateQuery(insertStatement, arguments);
        }
        catch (Exception e) {
            System.out.println("Failed to insert into orders table");
        }
    }

    public void insertIntoOrderDetailsTable(String[] arguments) {
        try {
            String insertToOrderDetails = "INSERT INTO orderDetails(orderId, bookId, quantity, price) VALUES(?, ?, ?, ?)";
            Database.updateQuery(insertToOrderDetails, arguments);
        }
        catch (Exception e) {
            System.out.println("Failed to insert into order details table");
        }
    }



    public double getTotalCartAmount(String username) {
        try {
            String selectStatement = "SELECT SUM(quantity * price) FROM shoppingcart WHERE username = ?";
            ResultSet resultSet = Database.selectQuery(selectStatement, new String[] {username});
            if (resultSet.next()) {
                NumberFormat formatter = new DecimalFormat("#0.00");
                return Double.parseDouble(formatter.format(resultSet.getDouble("SUM(quantity * price)")));
            }
        }
        catch (Exception e) {
            return 0.0;
        }
        return 0.0;
    }

    public ResultSet getShoppingCartItem(String username, int bookId) {
        try {
            String selectStatement = "SELECT * from shoppingcart where username = ? AND bookId = ?";
            return Database.selectQuery(selectStatement, new String[] {username, String.valueOf(bookId)});
        }
        catch(Exception e) {
            return null;
        }
    }

    public ResultSet getShoppingCartList(String username) {
        try {
            String selectStatement = "SELECT * FROM shoppingcart WHERE username = ?";
            return Database.selectQuery(selectStatement, new String[] {username});
        }
        catch (Exception e) {
            return null;
        }
    }

    public void insertToShoppingCartTable(String[] arguments) {
        try {
            String insertStatement = "INSERT INTO shoppingcart(username, bookId, quantity, price) VALUES (?,?,?,?)";
            Database.updateQuery(insertStatement, arguments);
        }
        catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateShoppingCartTable(String[] arguments) {
        try {
            String updateStatement = "UPDATE shoppingcart SET quantity = ? WHERE username = ? AND bookId = ?";
            Database.updateQuery(updateStatement, arguments);

        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteFromShoppingCartTable(String[] arguments) {
        try {
            if (arguments.length == 1) {
                String deleteStatement = "DELETE FROM shoppingcart WHERE username = ?";
                Database.updateQuery(deleteStatement, arguments);
            }
            else if (arguments.length == 2) {
                String deleteStatement = "DELETE FROM shoppingCart WHERE username = ? AND bookId = ?";
                Database.updateQuery(deleteStatement, arguments);
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public int getBookQuantity(int bookId) {
        try {
            String selectStatement = "SELECT quantity FROM books WHERE bookId = ?";
            ResultSet resultSet =  Database.selectQuery(selectStatement, new String[] {String.valueOf(bookId)});

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        catch (Exception e) {
            return 0;
        }
        return 0;
    }

    public boolean isBookNameExist(String[] arguments) {
        try {
            String bookCheck = "SELECT type, name FROM books WHERE type = ? AND name = ?";
            ResultSet resultSet = Database.selectQuery(bookCheck, arguments);
            if (resultSet.next()) {
                return true;
            }
        }
        catch (Exception e) {
            return false;
        }
        return false;
    }

    public void insertToBooksTable(String[] arguments) {
        try {
            String addBookStatement = "INSERT INTO books(type, name, price, publisher, quantity) VALUES (?,?,?,?,?) ";
            Database.updateQuery(addBookStatement, arguments);
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteFromBooksTable(String bookId) {
        try {
            String deleteStatement = "DELETE FROM books WHERE bookId = ?";
            Database.updateQuery(deleteStatement, new String[] {bookId});
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateBooksTable(String[] arguments) {
        try {
            if (arguments.length == 6) {
                String updateStatement = "UPDATE books SET type = ?, name = ?, price = ?, publisher = ?, quantity = ? " +
                        "WHERE bookId = ?";
                Database.updateQuery(updateStatement, arguments);
            }
            else if (arguments.length == 2) {
                String updateBooks = "UPDATE books SET quantity = quantity - ? WHERE bookId = ?";
                Database.updateQuery(updateBooks, arguments);
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

package org.example.bookstoremanagementsystemversion2;

public class ShoppingCartData {
    private final String username;
    private final int bookId;
    private final String bookName;
    private final int quantity;
    private final double price;

    public ShoppingCartData(String username, int bookId, String bookName, int quantity, double price) {
        this.username = username;
        this.bookId = bookId;
        this.bookName = bookName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getUsername() {
        return username;
    }

    public int getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}

package org.example.bookstoremanagementsystemversion2;

public class BookData {
    private final int bookId;
    private final String type;
    private final String name;
    private final double price;
    private final String publisher;
    private final int quantity;

    public BookData(int bookId, String type, String name, double price, String publisher, int quantity) {
        this.bookId = bookId;
        this.type = type;
        this.name = name;
        this.price = price;
        this.publisher = publisher;
        this.quantity = quantity;
    }

    public int getBookId() {
        return bookId;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getQuantity() {
        return quantity;
    }
}

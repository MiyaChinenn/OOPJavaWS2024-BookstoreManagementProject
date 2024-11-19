module org.example.bookstoremanagementsystemversion2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;
    requires transitive mysql.connector.j;
    requires java.management;

    exports org.example.bookstoremanagementsystemversion2;
    opens org.example.bookstoremanagementsystemversion2 to javafx.fxml;
}
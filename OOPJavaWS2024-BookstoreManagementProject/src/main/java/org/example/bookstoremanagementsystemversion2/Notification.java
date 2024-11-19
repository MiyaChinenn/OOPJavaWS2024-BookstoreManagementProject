package org.example.bookstoremanagementsystemversion2;

import javafx.scene.control.Alert;

public interface Notification {
    void showErrorMessage(String message);
    void showInfoMessage(String message);
    Alert showConfirmMessage(String message);

}

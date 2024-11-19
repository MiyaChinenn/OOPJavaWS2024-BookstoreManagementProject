package org.example.bookstoremanagementsystemversion2;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Objects;

public abstract class PageController {
    protected static String main_Username;

    protected static BookstoreDataModel main_DataModel = new BookstoreDataModel();


    // verify input, ensure no special character is allowed
    public boolean isViolate(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (    (input.charAt(i) == 32 || input.charAt(i) == 34) ||
                    (input.charAt(i) >= 39 && input.charAt(i) <= 47) ||
                    (input.charAt(i) >= 58 && input.charAt(i) <= 62) ||
                    (input.charAt(i) >= 91 && input.charAt(i) <= 94) ||
                    (input.charAt(i) == 96) ||
                    (input.charAt(i) >= 123 && input.charAt(i) <= 127) ) {
                return true;
            }
        }
        return false;
    }

    // switch to a view
    public void switchPage(String pageName) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(pageName)));

            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.setTitle("Management System");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
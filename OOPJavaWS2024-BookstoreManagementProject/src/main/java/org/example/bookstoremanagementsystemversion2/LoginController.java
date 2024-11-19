package org.example.bookstoremanagementsystemversion2;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.event.ActionEvent;


import java.sql.ResultSet;

public class LoginController extends PageController implements Notification {

    @FXML
    private Button createAccount_button;

    @FXML
    private Button haveAnAccount_button;

    @FXML
    private Button login_button;

    @FXML
    private AnchorPane login_form;

    @FXML
    private PasswordField login_password_field;

    @FXML
    private TextField login_username_field;

    @FXML
    private TextField reg_firstName_field;

    @FXML
    private TextField reg_lastName_field;

    @FXML
    private TextField reg_password_field;

    @FXML
    private TextField reg_phoneNumber_field;

    @FXML
    private TextField reg_username_field;

    @FXML
    private AnchorPane register_form;

    @FXML
    private AnchorPane slide_form;

    // Switch to the register screen
    public void onCreateAccountClick() {
        TranslateTransition slider = new TranslateTransition();
        slider.setNode(slide_form);
        slider.setToX(login_form.getLayoutX());
        slider.setDuration(Duration.seconds(0.5));

        slider.setOnFinished((ActionEvent event1) -> {
            haveAnAccount_button.setVisible(true);
            createAccount_button.setVisible(false);

        });

        slider.play();
    }

    // Switch to the login screen
    public void onHaveAccountClick() {
        TranslateTransition slider = new TranslateTransition();

        slider.setNode(slide_form);
        slider.setToX(register_form.getLayoutX());
        slider.setDuration(Duration.seconds(0.5));

        slider.setOnFinished((ActionEvent event1) -> {
            haveAnAccount_button.setVisible(false);
            createAccount_button.setVisible(true);
        });

        slider.play();
    }

    // Registration process
    public void onRegisterButtonClick() {
        // All fields must be entered
        if (reg_username_field.getText().isEmpty() ||reg_firstName_field.getText().isEmpty() ||
                reg_lastName_field.getText().isEmpty() || reg_phoneNumber_field.getText().isEmpty() ||
                reg_password_field.getText().isEmpty()) {

            showErrorMessage("Please fill all the fields");
            return;
        }

        try {
            ResultSet resultSet = main_DataModel.getUserData(reg_username_field.getText());

            // username and password must comply with the naming rules, no special character, and password must be >= 8
            // if a username exist, don't allow
            // Everything passed? Create a new account
            if (resultSet.next()) {
                showErrorMessage("Username already exists");

            }
            else if (reg_password_field.getLength() < 8 || isViolate(reg_password_field.getText())) {
                showErrorMessage("Password must be at least 8 characters and MUST" +
                        " NOT contain any special characters");
            }
            else if (isViolate(reg_username_field.getText())) {
                showErrorMessage("Username MUST NOT contain any special characters");
            }
            else {
                String regData = "INSERT INTO users(username, firstName, lastName, phoneNumber, password)" +
                        " VALUES(?, ?, ?, ?, ?)";

                Database.updateQuery(regData, new String[] {reg_username_field.getText().toLowerCase(), reg_firstName_field.getText(),
                        reg_lastName_field.getText(), reg_phoneNumber_field.getText(),
                        reg_password_field.getText()});

                showInfoMessage("Registration Successfully");

                reg_firstName_field.clear();
                reg_lastName_field.clear();
                reg_username_field.clear();
                reg_phoneNumber_field.clear();
                reg_password_field.clear();

                onHaveAccountClick();
            }

        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    // Login process
    public void onLoginButtonClick() {
        // All fields must be entered
        if (login_username_field.getText().isEmpty() || login_password_field.getText().isEmpty()) {
            showErrorMessage("Please fill all the fields");
            return;
        }

        // Check if the username and password entered matches any record in the database
        // No? Don't log them in
        // Yes? Log them in
        // If a user try to enter a special string or characters, prevent them from logging in as well

        String password = "";
        try {
            ResultSet resultSet = main_DataModel.getUserData(login_username_field.getText());
            if (resultSet.next()) {
                password = resultSet.getString("password");
            }

        }
        catch (Exception e) {
            throw new RuntimeException();
        }

        if (isViolate(login_username_field.getText())) {
            showErrorMessage("Username MUST NOT contain any special characters");
        }
        else if (isViolate(login_password_field.getText())) {
            showErrorMessage("Password MUST NOT contain any special characters");
        }
        else if (password.equals(login_password_field.getText())) {
            main_Username = login_username_field.getText().toLowerCase();
            // If admin login, show management view
            if (main_Username.equals("admin")) {
                switchPage("Management-view.fxml");
                login_button.getScene().getWindow().hide();
            }
            // Anyone else, show bookstore view
            else {
                switchPage("Bookstore-view.fxml");
                login_button.getScene().getWindow().hide();
            }
        }
        else {
            showErrorMessage("Username or Password is incorrect");
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
        return null;
    }

}
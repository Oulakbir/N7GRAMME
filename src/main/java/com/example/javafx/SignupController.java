package com.example.javafx;

import com.example.javafx.dao.UserDaoImpl;
import com.example.javafx.dao.entities.User;
import com.example.javafx.service.IUserService;
import com.example.javafx.service.IserviceUserImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.*;

public class SignupController implements Initializable {
    IUserService userService = new IserviceUserImpl(new UserDaoImpl());

    @FXML
    private TextField email;

    @FXML
    private TextField password;

    @FXML
    private TextField userName;
@FXML
private Label image;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void addImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif", "*.jpeg"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                image.setText(String.valueOf(selectedFile));
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors du chargement de l'image.", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    @FXML
    void signUp(ActionEvent event) {
        String userEmail = email.getText();

        if (userService.emailExists(userEmail) != null) {

            showAlert("Error", "Email already exists. Please use a different email.", AlertType.ERROR);

        } else {
            User user = new User();
            user.setNom(userName.getText());
            user.setEmail(userEmail);
            user.setPassword(password.getText());
            user.setImage(image.getText());
            userService.addUser(user);

            showAlert("Succes", "Your account has been added with successful", AlertType.INFORMATION);

        }
    }

    private void showAlert(String title, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

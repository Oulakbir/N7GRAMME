package com.example.javafx;

import com.example.javafx.dao.UserDaoImpl;
import com.example.javafx.dao.entities.User;
import com.example.javafx.service.IUserService;
import com.example.javafx.service.IserviceUserImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class addContactController {
    @FXML
    private TextField email;

    private  User user;
    IUserService userService = new IserviceUserImpl(new UserDaoImpl());

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    void addContact(ActionEvent event) {


            String emailContact = email.getText();

            if( userService.emailExists(emailContact) != null) {
                String id = userService.emailExists(emailContact);
                userService.addContact(user.getUser_id(), id);


                showAlert("Succès", "Le contact a été ajouté avec succès.", Alert.AlertType.INFORMATION);

                Stage stage = (Stage) email.getScene().getWindow();
                stage.close();
            } else
             showAlert("Erreur", "Veuillez vérifier l'email de votre contact", Alert.AlertType.ERROR);

    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void closeModal(ActionEvent event) {
        Stage stage = (Stage) email.getScene().getWindow();
        stage.close();
    }

}

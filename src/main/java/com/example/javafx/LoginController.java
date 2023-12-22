package com.example.javafx;

import com.example.javafx.Application;
import com.example.javafx.dao.UserDaoImpl;
import com.example.javafx.dao.entities.User;
import com.example.javafx.service.IUserService;
import com.example.javafx.service.IserviceUserImpl;
import com.opentok.Role;
import com.opentok.TokenOptions;
import com.opentok.exception.OpenTokException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.*;

import  com.opentok.OpenTok;

public class LoginController implements Initializable {
    IUserService userService = new IserviceUserImpl(new UserDaoImpl());

    @FXML
   private Circle circle;
 @FXML
 private TextField email;
 @FXML
 private TextField password;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image img = new Image(Objects.requireNonNull(getClass().getResource("/com/example/img/user1.png")).toExternalForm());
        circle.setFill(new ImagePattern(img));

    }

@FXML
void login(ActionEvent event) throws IOException{
    User user = userService.login(email.getText(), password.getText());

    if (user != null) {
        showAlert("Login Successful", "Welcome, " + user.getNom(), AlertType.CONFIRMATION);
        //user session
        Set<String> privileges = new HashSet<>(Arrays.asList("READ", "WRITE"));
        UserSession userSession = UserSession.getInstace(userService.getUserByEmail(email.getText()));
        System.out.println(userSession.toString());
        //
        if(userSession!=null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("App.fxml"));
            Parent homePageParent = loader.load();
            Controller controller = loader.getController();
            controller.initData(user);

            Scene homePageScene = new Scene(homePageParent);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(homePageScene);
            appStage.show();
        }else{
            System.out.println("Session n'a pas été crée");
        }
    } else {
        showAlert("Login Failed", "Incorrect email or password", AlertType.ERROR);
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

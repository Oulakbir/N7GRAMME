package com.example.javafx;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Application extends javafx.application.Application {
    private static Stage primaryStageObj;
    @Override
    public void start(Stage stage) throws IOException {
        primaryStageObj =stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(getClass().getClassLoader().getResource("img/logo.png").toString()));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setResizable(false);
        stage.setTitle("N7gramme!");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e -> Platform.exit());

    }

    public static void main(String[] args) {

        launch();


    }
    public static Stage getPrimaryStage() {
        return primaryStageObj;
    }
}
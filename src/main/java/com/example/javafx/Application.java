package com.example.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    private static Stage primaryStageObj;
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("N7gramme!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {

        launch();


    }
    public static Stage getPrimaryStage() {
        return primaryStageObj;
    }
}
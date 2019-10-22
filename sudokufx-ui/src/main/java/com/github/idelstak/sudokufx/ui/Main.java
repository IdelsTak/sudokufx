/*
 Copyright 2019.
 */
package com.github.idelstak.sudokufx.ui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class Main extends Application {

    /**
     @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        var location = getClass().getResource("/fxml/SudokuBoard.fxml");
        var root = FXMLLoader.<Parent>load(location);
        var scene = new Scene(root);

        primaryStage.setTitle("SudokuFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

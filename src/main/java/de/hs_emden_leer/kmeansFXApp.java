package de.hs_emden_leer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A application to draw a diagramm with several datapoints and calculating algorithm to visualise the k-means algorithm.
 * This application was developed for the business intelligence course of the University of Applied Sciences Emden-Leer
 * in the winter semester 20/21.
 *
 * @author Sven Wiemers
 */
public class kmeansFXApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("controller/MainWindowView.fxml"));
        Scene scene = new Scene(view);
        stage.setScene(scene);
        // set the title
        stage.setTitle("k-meansFX");
        // set the icon
        stage.getIcons().add(new Image(getClass().getResourceAsStream("controller/images/kmeansFX_icon.png")));
        // set window to non-resizable
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
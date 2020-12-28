package de.hs_emden_leer.controller;

import de.hs_emden_leer.model.Points;
import de.hs_emden_leer.util.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

import java.io.File;

/**
 * Controller for the Main-Window
 *
 * @author Sven Wiemers
 */
public class MainWindowController {

    @FXML
    private MenuItem menuOpenFile;

    @FXML
    private MenuItem menuQuit;

    /**
     * Initialize the view
     */
    public void initialize() {
        // add Eventhandler to handle the menu item "Quit"
        menuOpenFile.setOnAction(event -> openFile());
        menuQuit.setOnAction(event -> closeApplication());
    }

    /**
     * Handle action by clicking "Datei öffnen" in the menu => open the fileChooser
     */
    private void openFile() {
        File file = Utils.openFileChooser();
        Points.getInstance().setCoordinates(Utils.readFile(file));
    }

    /**
     * Handle action by clicking "Beenden" in the menu => quits the application
     */
    private void closeApplication() {
        System.exit(0);
    }
}

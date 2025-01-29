package org.AAKB.client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;


public class Controller {
    @FXML
    private Pane boardPane;
    @FXML
    private Label infoBar;
    @FXML
    private MenuItem undoOption;
    @FXML
    private MenuItem redoOption;
    @FXML
    private MenuItem quitOption;
    @FXML
    private MenuItem aboutOption;
    @FXML
    private MenuItem skipOption;

    public Pane getBoardPane() {
        return boardPane;
    }

    public MenuItem getUndoOption() {
        return undoOption;
    }

    public MenuItem getRedoOption() {
        return redoOption;
    }
  
    public MenuItem getQuitOption() {
        return quitOption;
    }

    public MenuItem getAboutOption() {
        return aboutOption;
    }
    
    public MenuItem getSkipOption() {
        return skipOption;
    }

    public Label getInfoBar() {
        return infoBar;
    }
}
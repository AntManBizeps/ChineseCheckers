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
    private MenuItem players2Option;
    @FXML
    private MenuItem players3Option;
    @FXML
    private MenuItem players4Option;
    @FXML
    private MenuItem players6Option;
    @FXML
    private MenuItem quitOption;
    @FXML
    private MenuItem aboutOption;
    
    
    public Pane getBoardPane() {
        return boardPane;
    }

    public MenuItem getPlayers2Option() {
        return players2Option;
    }

    public MenuItem getPlayers3Option() {
        return players3Option;
    }
    
    public MenuItem getPlayers4Option() {
        return players4Option;
    }

    public MenuItem getPlayers6Option() {
        return players6Option;
    }
  
    public MenuItem getQuitOption() {
        return quitOption;
    }

    public MenuItem getAboutOption() {
        return aboutOption;
    }
    
    
}
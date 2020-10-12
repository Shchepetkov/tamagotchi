package ru.game.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class BeginController extends BaseController {
    @FXML
    public Button start;
    @FXML
    public Button exit;

    @FXML
    void initialize() {
    }

    @FXML
    public void onStart() throws Exception {
        startUp();
    }

    @FXML
    public void onExit() {
        System.exit(0);
    }
}

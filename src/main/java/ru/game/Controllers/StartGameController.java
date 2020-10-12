package ru.game.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import ru.game.PersonageBuilder;
import ru.game.WorkWithJson;

import java.util.ArrayList;
import java.util.List;

public class StartGameController extends BaseController {
    private String personageName;
    private List<Image> image = new ArrayList<>();

    @FXML
    private Image im1;
    @FXML
    private Image im2;
    @FXML
    private Image im3;

    @FXML
    private Button bt1;
    @FXML
    private Button bt2;
    @FXML
    private Button bt3;
    @FXML
    private Button start;

    @FXML
    void initialize() {
        start.setVisible(false);
    }

    @FXML
    public void start() throws Exception {
        if (isNeedInitialize()) {
            PersonageBuilder builder = new PersonageBuilder(image);
            WorkWithJson.writeDataToJsonFile("data.json", builder.build(personageName));
            builder.getSelected();
            startUp();
        }
    }

    @FXML
    public void bt1() {
        start.setVisible(true);
        personageName = "player1";
        this.image.add(im3);
        bt1.setVisible(false);
        bt2.setVisible(false);
        bt3.setVisible(false);
    }

    @FXML
    public void bt2() {
        start.setVisible(true);
        personageName = "player2";
        this.image.add(im1);
        bt1.setVisible(false);
        bt2.setVisible(false);
        bt3.setVisible(false);
    }

    @FXML
    public void bt3() {
        start.setVisible(true);
        personageName = "player3";
        this.image.add(im2);
        bt1.setVisible(false);
        bt2.setVisible(false);
        bt3.setVisible(false);
    }
}
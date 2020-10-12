package ru.game.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.json.simple.parser.ParseException;
import ru.game.WorkWithJson;

import java.io.IOException;

public class GameController extends BaseController {
    @FXML
    public ProgressBar bar;
    @FXML
    public ImageView mainImage;
    @FXML
    public Label eat, label, limit;
    @FXML
    private Button feed, walking, clean, restart;

    private Thread threadEat;
    private static int counter = 0;
    private ActionsCharacterController test = new ActionsCharacterController();

    @FXML
    void initialize() throws Exception {
        walking.setVisible(false);
        test.minusProgressBarAndDeathInGame(label, bar, restart, mainImage, feed, walking); // Отнимает значение из прогресбара
        test.firstStart(feed, walking, mainImage, bar, restart);// Проверка что с персонажем

        restart.setDisable(true);
        walking.setDisable(true);

        mainImage.setImage(new Image(test.imageLinkEditing("/Walk.gif")));

        clean.setDisable(true);
    }

    @FXML
    public void onActionRestart() throws Exception // старт после смерти
    {
        if (test.differenceOfTwoDates() < 2000) // можно заново создать персонажа через ~=  33 min. 20 sec.
        {
            limit.setVisible(true);
            limit.setText("Вы не можете сразу создавать персонажа, попробуйте чуть позже");
        } else {
            test.threadMinusBarInterrupt(); // Поток минус завершен
            restart();
        }
    }

    @FXML
    public void onActionEat() throws IOException, ParseException // логика питания
    {
        test.threadMinusBarInterrupt(); // Поток минус завершен

        mainImage.setImage(new Image(test.imageLinkEditing("/eat.gif")));
        feed.setDisable(true);
        threadEat = new Thread(() ->
        {
            try {
                Thread.sleep(1000);
                while (true) {
                    counter++;
                    bar.setProgress(bar.getProgress() + 0.250); // сколько будет прибавляться к hp

                    if (bar.getProgress() > 1) // если перекормлен то грязный
                    {
                        try {
                            if (counter % 3 == 0) {
                                threadEatInterrupt(); // Поток кушать завершен
                                test.dirty(mainImage, walking, feed, clean);
                                test.minusProgressBarAndDeathInGame(label, bar, restart, mainImage, feed, walking);
                            } else {
                                mainImage.setImage(new Image(test.imageLinkEditing("/Walk.gif")));
                                threadEatInterrupt();
                                test.minusProgressBarAndDeathInGame(label, bar, restart, mainImage, feed, walking);
                                feed.setDisable(false);
                            }
                        } catch (IOException | ParseException e) {
                            e.printStackTrace();
                        } finally {
                            bar.setProgress(1);
                        }
                    }
                    WorkWithJson.updateDataToJsonFile("data.json"); // запись в json новой даты
                    Thread.sleep(5000);
                }
            } catch (InterruptedException | IOException | ParseException ignored) {
            }
        });
        threadEat.start(); // Поток кушать запущен
    }

    @FXML
    public void onActionWalk() throws IOException, ParseException // анимация прогулки
    {
        threadEatInterrupt(); // Поток кушать завершен
        mainImage.setImage(new Image(test.imageLinkEditing("/Walk.gif")));
        test.minusProgressBarAndDeathInGame(label, bar, restart, mainImage, feed, walking);
    }

    @FXML
    private void onActionClean() throws IOException, ParseException // анимация помыться
    {
        mainImage.setImage(new Image(test.imageLinkEditing("/Wash.gif")));
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(150 * 60), ae ->
                {
                    try {
                        mainImage.setImage(new Image(test.imageLinkEditing("/Walk.gif")));
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }

                    clean.setDisable(true);
                    feed.setDisable(false);
                    walking.setDisable(true);
                }));

        timeline.setCycleCount(1);
        timeline.play();
    }

    @FXML
    private void threadEatInterrupt() {
        threadEat.interrupt();
    }
}
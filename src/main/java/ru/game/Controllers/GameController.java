package ru.game.Controllers;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.json.simple.parser.ParseException;
import ru.game.PersonageSelect;
import ru.game.WorkWithJson;
import java.io.IOException;

public class GameController extends BaseController
    {
        @FXML public ProgressBar bar;
        @FXML public ImageView mainImage;
        @FXML public Label eat,label,limit;
        @FXML private Button feed,walking,clean,restart;

        private static int counter = 0;
        private Thread threadEat, threadMinusBar;
        private PersonageSelect personageSelect = new PersonageSelect();

        @FXML void initialize() throws Exception
            {
                walking.setVisible(false);
                minusProgressBarAndDeathInGame(); // Отнимает значение из прогресбара
                firstStart();// Проверка что с персонажем

                restart.setDisable(true);
                walking.setDisable(true);

                    mainImage.setImage(new Image(imageLinkEditing("/Walk.gif")));

                clean.setDisable(true);
            }

        private String imageLinkEditing(String link) throws IOException, ParseException
            {
                String name = WorkWithJson.getNameFromFileJson(); // Переменная имени из json
                String[] arr; // Стринговый массив для записи пути персонажа в массив
                String arr2;
                String imageRef = getImage(); // Получает путь картинки из json

                    arr = imageRef.split("image"); // делит путь на 2 части до image и после
                    arr2 = arr[0];

                    return arr2 + "image/" + name + link; // возвращаю первую часть (имя относится к названию папки) + аргумент
            }

        private void firstStart() throws ParseException, IOException // проверяет нужно ли рождение персонажа
            {
                if (!(getTime() == personageSelect.newDataFormat()))
                    {
                        progressBarMinusDate();
                        differenceOfTwoDates();
                    }
                else
                    {
                        birth();
                    }
            }

        private void birth() // анимация рождения персонажа
            {
                feed.setDisable(true);
                walking.setDisable(true);

                    Thread pressingThread7 = new Thread(() ->
                        {
                            try
                                {
                                    mainImage.setImage(new Image(imageLinkEditing("/egg.gif")));
                                    Thread.sleep(12000);
                                    mainImage.setImage(new Image(imageLinkEditing("/Walk.gif")));
                                    feed.setDisable(false);
                                } catch (InterruptedException | IOException | ParseException ignored) { }
                        });

                    pressingThread7.start();
            }

        private void dirty() throws IOException, ParseException // анимация переедания персонажа
            {
                mainImage.setImage(new Image(imageLinkEditing("/dirty.gif")));
                walking.setDisable(true);
                feed.setDisable(true);
                clean.setDisable(false);
            }

        private float differenceOfTwoDates() throws ParseException, IOException // посчитать разницу двух дат (из json и реальную)
            {
                long a = personageSelect.newDataFormat();
                long b = getTime();

                return a - b;
            }

        private void progressBarMinusDate() throws ParseException, IOException // устанавливает реальное hp персонажа( умрет за 16.6 минут )
            {
                bar.setProgress( bar.getProgress() - (differenceOfTwoDates()/1000)); // установить (полученное значение - разницу двух дат)

                    if (differenceOfTwoDates()/1000 > (bar.getProgress() + (differenceOfTwoDates()/1000)))// если разница > полученного + разницы
                        {
                            bar.setProgress(-1);
                            death();
                            restart.setDisable(false);
                        }
            }

        private void minusProgressBarAndDeathInGame()
            {
                threadMinusBar = new Thread(() ->
                    {
                        try
                            {
                                while (true)
                                    {
                                        Platform.runLater(() -> // счет в label голода
                                            {
                                                String formattedDouble = String.format("%.2f", (float) bar.getProgress());
                                                label.setText(formattedDouble);
                                            });

                                                if (bar.getProgress() > 0)
                                                    {
                                                        bar.setProgress(bar.getProgress() - 0.001); // сколько будет вычитать из hp(умрет ~= 16 min. 40 sec.)
                                                    }

                                                    else if (bar.getProgress() < 0) // если hp < 0 то смерть
                                                        {
                                                            try
                                                                {
                                                                    threadMinusBarInterrupt(); // Поток минус завершен
                                                                    death();
                                                                }
                                                            catch (IOException | ParseException e) { e.printStackTrace(); }

                                                            restart.setDisable(false);
                                                        }
                                                    else if (bar.getProgress() < 0.5) // если hp < 0.5 то смена настроения
                                                        {
                                                            try
                                                                {
                                                                    mainImage.setImage(new Image(imageLinkEditing("/cry.gif")));
                                                                }
                                                            catch (IOException | ParseException e) { e.printStackTrace(); }
                                                        }
                                        Thread.sleep(1000);
                                    }
                            } catch (InterruptedException ignored) {}
                    });
                threadMinusBar.start();
            }

        private void death() throws IOException, ParseException // анимация смерти
            {
                mainImage.setImage(new Image(imageLinkEditing("/kil.gif")));
                feed.setDisable(true);
                walking.setDisable(true);
            }

        @FXML public void onActionRestart() throws Exception // старт после смерти
            {
                if (differenceOfTwoDates() < 2000) // можно заново создать персонажа через ~=  33 min. 20 sec.
                    {
                        limit.setVisible(true);
                        limit.setText("Вы не можете сразу создавать персонажа, попробуйте чуть позже");
                    }
                else
                    {
                        threadMinusBarInterrupt(); // Поток минус завершен
                        restart();
                    }
            }

        @FXML public void onActionEat() throws IOException, ParseException // логика питания
            {
                threadMinusBarInterrupt(); // Поток минус завершен

                mainImage.setImage(new Image(imageLinkEditing("/eat.gif")));
                    threadEat = new Thread(() ->
                        {
                            try
                                {
                                    Thread.sleep(1000);
                                        while (true)
                                            {
                                                counter++;
                                                bar.setProgress(bar.getProgress() + 0.250); // сколько будет прибавляться к hp

                                                if (bar.getProgress() > 1) // если перекормлен то грязный
                                                    {
                                                        try
                                                            {
                                                                if (counter % 3 == 0)
                                                                    {
                                                                        threadEatInterrupt(); // Поток кушать завершен
                                                                        dirty();
                                                                        minusProgressBarAndDeathInGame();
                                                                    }
                                                               else
                                                                    {
                                                                        mainImage.setImage(new Image(imageLinkEditing("/Walk.gif")));
                                                                        threadEatInterrupt();
                                                                        minusProgressBarAndDeathInGame();
                                                                    }
                                                            }
                                                        catch (IOException | ParseException e) { e.printStackTrace(); }

                                                        finally
                                                            {
                                                                bar.setProgress(1);
                                                            }
                                                    }
                                                WorkWithJson.updateDataToJsonFile("data.json"); // запись в json новой даты
                                                Thread.sleep(5000);
                                            }
                                } catch (InterruptedException | IOException | ParseException ignored) {}
                        });
                    threadEat.start(); // Поток кушать запущен
            }

        @FXML public void onActionWalk() throws IOException, ParseException // анимация прогулки
            {
                threadEatInterrupt(); // Поток кушать завершен
                mainImage.setImage(new Image(imageLinkEditing("/Walk.gif")));
                minusProgressBarAndDeathInGame();
            }

        @FXML private void onActionClean() throws IOException, ParseException // анимация помыться
            {
                mainImage.setImage(new Image(imageLinkEditing("/Wash.gif")));
                    Timeline timeline = new Timeline(
                            new KeyFrame(
                                    Duration.millis(150 * 60), ae ->
                                        {
                                            try
                                                {
                                                    mainImage.setImage(new Image(imageLinkEditing("/Walk.gif")));
                                                }
                                            catch (IOException | ParseException e) { e.printStackTrace(); }

                                            clean.setDisable(true);
                                            feed.setDisable(false);
                                            walking.setDisable(true);
                                        }));

                    timeline.setCycleCount(1);
                    timeline.play();
            }

        @FXML private void threadEatInterrupt()
            {
                threadEat.interrupt();
            }

        @FXML private void threadMinusBarInterrupt()
            {
                threadMinusBar.interrupt();
            }
    }
package ru.game.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.simple.parser.ParseException;
import ru.game.PersonageSelect;
import ru.game.WorkWithJson;

import java.io.IOException;

class ActionsCharacterController extends BaseController
    {
        private PersonageSelect personageSelect = new PersonageSelect();
        private Thread threadMinusBar = new Thread();

        String imageLinkEditing(String link) throws IOException, ParseException
            {
                String name = WorkWithJson.getNameFromFileJson();
                String[] arr;
                String arr2;
                String imageRef = getImage();

                arr = imageRef.split("image");
                arr2 = arr[0];

                return arr2 + "image/" + name + link;
            }

        void firstStart(Button feed, Button walking, ImageView mainImage, ProgressBar bar, Button restart) throws ParseException, IOException
            {
                if (!(getTime() == personageSelect.newDataFormat()))
                    {
                        progressBarMinusDate(bar, restart, mainImage, feed, walking);
                        differenceOfTwoDates();
                    }
                else
                    {
                        birth(feed, walking, mainImage);
                    }
            }

        private void birth(Button feed, Button walking, ImageView mainImage) // анимация рождения персонажа
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

        void dirty(ImageView mainImage, Button walking, Button feed, Button clean) throws IOException, ParseException // анимация переедания персонажа
            {
                mainImage.setImage(new Image(imageLinkEditing("/dirty.gif")));
                walking.setDisable(true);
                feed.setDisable(true);
                clean.setDisable(false);
            }

        float differenceOfTwoDates() throws ParseException, IOException // посчитать разницу двух дат (из json и реальную)
            {
                long a = personageSelect.newDataFormat();
                long b = getTime();

                return a - b;
            }

        private void progressBarMinusDate(ProgressBar bar, Button restart, ImageView mainImage, Button feed, Button walking) throws ParseException, IOException // устанавливает реальное hp персонажа( умрет за 16.6 минут )
            {
                bar.setProgress( bar.getProgress() - (differenceOfTwoDates()/1000)); // установить (полученное значение - разницу двух дат)

                if (differenceOfTwoDates()/1000 > (bar.getProgress() + (differenceOfTwoDates()/1000)))// если разница > полученного + разницы
                    {
                        bar.setProgress(-1);
                        death(mainImage, feed, walking);
                        restart.setDisable(false);
                    }
            }

        void minusProgressBarAndDeathInGame(Label label, ProgressBar bar, Button restart, ImageView mainImage, Button feed, Button walking)
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
                                                        death(mainImage, feed, walking);
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

        private void death(ImageView mainImage, Button feed, Button walking) throws IOException, ParseException // анимация смерти
            {
                mainImage.setImage(new Image(imageLinkEditing("/kil.gif")));
                feed.setDisable(true);
                walking.setDisable(true);
            }


        @FXML void threadMinusBarInterrupt()
            {
                threadMinusBar.interrupt();
            }

    }

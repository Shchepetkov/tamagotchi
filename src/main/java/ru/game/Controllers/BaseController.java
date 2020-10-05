package ru.game.Controllers;

import org.json.simple.parser.ParseException;
import ru.game.Application;
import ru.game.Interfaces.SceneListener;
import ru.game.WorkWithJson;

import java.io.IOException;

public class BaseController implements SceneListener // Основной контроллер
    {
        private String name;
        private String path;

        void startUp() throws Exception
            {
                Application.addListener(this);
                Application application = new Application();

                this.name = WorkWithJson.getNameFromFileJson();

                    if (name == null || name.equals(""))
                            {
                                path = "/fxml/Main.fxml";
                                application.newScene();

                            }
                        else
                            {
                                path = "/fxml/Game.fxml";
                                application.newScene();
                            }
            }

        void restart() throws Exception
            {
                Application.addListener(this);
                Application application = new Application();
                WorkWithJson.restartDataToJsonFile("data.json");
                path = "/fxml/Main.fxml";
                application.newScene();
            }

        boolean isNeedInitialize() throws IOException, ParseException // если имя в json не
            {
                this.name = WorkWithJson.getNameFromFileJson();
                    return (name == null || name.equals(""));
            }

        @Override
            public String changeScene()
                {
                    return path;
                }// изменить сцену

        @Override
            public String getImage() throws IOException, ParseException //получить адрес картинки
                {
                    return WorkWithJson.getImageFromFileJson();
                }

        @Override
            public long getTime() throws IOException, ParseException  //получить адрес картинки
                {
                    return Long.parseLong(String.valueOf(WorkWithJson.getTimeFromFileJson()));
                }
    }

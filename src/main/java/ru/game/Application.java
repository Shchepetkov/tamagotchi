package ru.game;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.game.Interfaces.SceneListener;

/**
 * @author Shchepetkov
 */

public class Application extends javafx.application.Application
    {
        private String pathToFxml = "/fxml/Begin.fxml";
        private static Stage stage;
        private static SceneListener _listener;

        public static void main(String[] args)
            {
                Application.launch(args);
            }

        public static void addListener(SceneListener listener)
            {
                if (_listener == null)
                    {
                        _listener = listener;
                    }
            }

        @Override
            public void start(Stage primaryStage) throws Exception
                {
                    Parent root = FXMLLoader.load(getClass().getResource(pathToFxml));
                    Scene scene = new Scene(root);

                    primaryStage.setScene(scene);
                    primaryStage.setOnCloseRequest(t -> System.exit(0));
                    primaryStage.setTitle("Game");
                    primaryStage.show();
                    stage = primaryStage;
                }

            public void newScene() throws Exception // загрузка новой сцены
                {
                    stage.close();



                        if (_listener != null)
                            {
                                pathToFxml = _listener.changeScene();
                                start(new Stage());
                            }

                    _listener = null;
                }
    }
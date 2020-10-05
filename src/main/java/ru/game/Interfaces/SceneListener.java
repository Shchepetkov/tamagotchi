package ru.game.Interfaces;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface SceneListener {
    String changeScene();
    String getImage() throws IOException, ParseException;
    long getTime() throws IOException, ParseException;
}

package ru.game;

import com.google.gson.stream.JsonWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.game.Interfaces.PersonWritable;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class WorkWithJson {
    private static JsonWriter writer;

    public static void writeDataToJsonFile(String filePath, PersonageSelect personageSelect) throws IOException {
        writer = new JsonWriter(new FileWriter(filePath));
        writer.setIndent("    ");
        writer.beginObject();

        WriteValue(personageSelect);

        writer.endObject();
        writer.close();
    }

    private static <T extends PersonWritable> void WriteValue(T value) throws IOException {
        writer.name("name").value(value.getName());
        writer.name("time_of_birth_and_nutrition").value(value.getTime_of_birth_and_nutrition());
        writer.name("imageRef").value(value.getImageRef());
    }


    public static void restartDataToJsonFile(String filePath) throws IOException {
        writer = new JsonWriter(new FileWriter(filePath));
        writer.setIndent("    ");
        writer.beginObject();

        writer.name("name").value("");
        writer.name("time_of_birth_and_nutrition").value("");
        writer.name("imageRef").value("");

        writer.endObject();
        writer.close();
    }

    public static void updateDataToJsonFile(String filePath) throws IOException, ParseException {
        PersonageSelect personageSelect = new PersonageSelect();
        String name = getNameFromFileJson();
        String image = getImageFromFileJson();

        writer = new JsonWriter(new FileWriter(filePath));
        writer.setIndent("    ");
        writer.beginObject();

        writer.name("name").value(name);
        writer.name("time_of_birth_and_nutrition").value(personageSelect.getTime_of_birth_and_nutrition());
        writer.name("imageRef").value(image);

        writer.endObject();
        writer.close();
    }

    private static Object readerDataToJsonFile(String paramName) throws IOException, ParseException {
        Reader reader = new FileReader("data.json");
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(reader);

        JSONObject jsonObject = (JSONObject) jsonObj;

        return jsonObject.get(paramName);
    }

    public static String getNameFromFileJson() throws IOException, ParseException // читает из файла name
    {
        return (String) readerDataToJsonFile("name");
    }

    public static String getImageFromFileJson() throws IOException, ParseException {
        return (String) readerDataToJsonFile("imageRef");
    }

    public static Long getTimeFromFileJson() throws IOException, ParseException {
        return (Long) readerDataToJsonFile("time_of_birth_and_nutrition");
    }
}

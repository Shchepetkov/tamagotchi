package ru.game;

import ru.game.Interfaces.PersonWritable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PersonageSelect implements PersonWritable
    {
        private String name; // имя
        private int time_of_birth_and_nutrition; // дата рождения и питание
        private String imageRef; // ссылка на изображение

           public PersonageSelect()
                {
                    time_of_birth_and_nutrition = newDataFormat();
                }

            PersonageSelect(String name)
                {
                    this.name = name;
                    time_of_birth_and_nutrition = newDataFormat();
                }

        public int newDataFormat() // получение реального времени в секундах
            {
                String[] arr;
                DateFormat dateF = new SimpleDateFormat("DD:HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                String time = dateF.format(cal.getTime());

                arr=time.split(":");
                time_of_birth_and_nutrition = Integer.parseInt(arr[0]) * 86400 + Integer.parseInt(arr[1])*3600 + Integer.parseInt(arr[2]) * 60 + Integer.parseInt(arr[3]);

                    return time_of_birth_and_nutrition;
            }

        public String getName() {
                return name;
            }

        public String getImageRef() {
            return imageRef;
        }
        void setImageRef(String imageRef) {
            this.imageRef = imageRef;
        }

        public int getTime_of_birth_and_nutrition() {
                return time_of_birth_and_nutrition;
            }

    }

//    public int newDataFormat() // получение реального времени в секундах
//    {
//        String[] arr;
//        DateFormat dateF = new SimpleDateFormat("HH:mm:ss");
//        Calendar cal = Calendar.getInstance();
//        String time = dateF.format(cal.getTime());
//
//        if (time.matches("(\\d+):(\\d+):(\\d+)"))
//        {
//            arr=time.split(":");
//            time_of_birth_and_nutrition = Integer.parseInt(arr[0])*3600 + Integer.parseInt(arr[1])*60 + Integer.parseInt(arr[2]);
//        }
//
//        return time_of_birth_and_nutrition;
//    }
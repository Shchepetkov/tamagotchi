package ru.game;

import javafx.scene.image.Image;
import java.util.List;

public class PersonageBuilder
    {
        private PersonageSelect pers;
        private List<Image> select;

        public PersonageBuilder(List<Image> select)
                {
                    this.select = select;
                }

            public PersonageSelect build(String name)
                {
                    if (pers == null)
                        {
                            pers = new PersonageSelect(name);
                            pers.setImageRef(getSelected());
                        }
                    return pers;
                }

         public String getSelected()
             {
                return String.valueOf(select.get(0).impl_getUrl());
             }
    }

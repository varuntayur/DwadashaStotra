package com.vtayur.dwadashastotra.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by varuntayur on 4/5/2014.
 */
@Root
public class Shloka implements Serializable {


    public static final String EMPTY_STRING = "";
    @Attribute
    private String num;

    @Element(required = false)
    private String text;

    @Element(required = false)
    private ShlokaDescription explanation;

    public Shloka() {
    }

    @Override
    public String toString() {
        return "Shloka{" +
                "num='" + num + '\'' +
                ", text='" + text + '\'' +
                ", explanation=" + explanation +
                '}';
    }

    public String getNum() {
        return num;
    }


    public String getText() {
        if (text != null) {
            return text.replaceAll("[ ]+", " ").trim();
        }
        return EMPTY_STRING;
    }


    public String getFormattedExplanation() {

        StringBuilder stringBuilder = new StringBuilder();
        for (Note note : explanation.getNotesList()) {
            stringBuilder.append(note.getFormattedNote());
        }
        return stringBuilder.toString();
    }

}

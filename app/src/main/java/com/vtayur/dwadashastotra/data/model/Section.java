package com.vtayur.dwadashastotra.data.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by varuntayur on 4/5/2014.
 */
@Root
public class Section implements Serializable {


    @Attribute(name = "name")
    private String name;

    @ElementList(inline = true, name = "shloka")
    private List<Shloka> shlokaList;

    @Attribute
    private String num;

    public Section() {
    }

    @Override
    public String toString() {
        return "Section{" +
                "name='" + name + '\'' +
                ", shlokaList=" + shlokaList +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Shloka> getShlokaList() {
        return shlokaList;
    }

}

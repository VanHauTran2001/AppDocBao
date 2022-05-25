package com.example.appdocbao.Model;

import java.io.Serializable;

public class Item implements Serializable {
    public String title;
    public String description;

    public Item(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Item() {
    }

}

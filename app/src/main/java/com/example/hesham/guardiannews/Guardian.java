package com.example.hesham.guardiannews;

/**
 * Created by Hesham on 20-Aug-18.
 */

public class Guardian {
    private String author, url, title, data, category;

    ////////////////////////////////////////////////Constractor
    public Guardian(String title, String category, String data, String url, String author) {
        this.author = author;
        this.data = data;
        this.title = title;
        this.category = category;
        this.url = url;
    }

    ////////////////////////////////////////////////Get METHOD
    public String getAuthor() {
        return author;
    }
    public String getTitle() {
        return title;
    }
    public String getUrl() {
        return url;
    }
    public String getDate() {
        return data;
    }
    public String getCategory() {
        return category;
    }

}


package com.jnu.booklist;

public class BookItem {

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private int pictureId;

    public BookItem(String name, int pictureId) {
        this.name=name;
        this.pictureId=pictureId;
    }

    public String getName() { return name; }

    public int getPictureId() {
        return pictureId;
    }

}

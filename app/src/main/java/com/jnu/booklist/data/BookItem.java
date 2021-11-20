package com.jnu.booklist.data;

import java.io.Serializable;

public class BookItem implements Serializable {

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

    public void setName(String name) {
        this.name = name;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }
}

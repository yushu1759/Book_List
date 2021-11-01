package com.jnu.booklist;

public class BookItem {
    private String name;
    private int pictureId;

    public BookItem(String name, int pictureId) {
        this.setName(name);
        this.setPictureId(pictureId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }
}

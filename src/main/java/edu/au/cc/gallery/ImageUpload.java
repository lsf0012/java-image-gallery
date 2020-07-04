package edu.au.cc.gallery;

public class ImageUpload {
    private String userName;
    private String imageID;

    public ImageUpload(String userName, String imageID) {
        this.userName = userName;
        this.imageID = imageID;
    }

    public String getUserName() {
        return userName;
    }

    public String getImageID() {
        return imageID;
    }
}


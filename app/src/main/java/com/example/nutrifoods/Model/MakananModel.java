package com.example.nutrifoods.Model;

public class MakananModel {
    private String UserID;
    private String nama_makanan;
    private String image;
    private String usernamePublisher;

    public MakananModel() {
    }

    public MakananModel(String userID, String nama_makanan, String image, String usernamePublisher) {
        UserID = userID;
        this.nama_makanan = nama_makanan;
        this.image = image;
        this.usernamePublisher = usernamePublisher;
    }

    public String getUsernamePublisher() {
        return usernamePublisher;
    }

    public void setUsernamePublisher(String usernamePublisher) {
        this.usernamePublisher = usernamePublisher;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getNama_makanan() {
        return nama_makanan;
    }

    public void setNama_makanan(String nama_makanan) {
        this.nama_makanan = nama_makanan;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

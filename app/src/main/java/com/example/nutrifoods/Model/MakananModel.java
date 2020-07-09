package com.example.nutrifoods.Model;

//import com.google.gson.annotations.SerializedName;

public class MakananModel {
    //@SerializedName("userID")
    private String UserID;
    //@SerializedName("namaMakanan")
    private String namaMakanan;
    //@SerializedName("image")
    private String image;
    //@SerializedName("usernamePublisher")
    private String usernamePublisher;
    //@SerializedName("currentDate")
    private String currentDate;
    //@SerializedName("currentTime")
    private String currentTime;

    private String topMakanan;

    public MakananModel() {
    }

    public MakananModel(String userID, String namaMakanan, String image, String usernamePublisher, String currentDate, String currentTime, String topMakanan) {
        UserID = userID;
        this.namaMakanan = namaMakanan;
        this.image = image;
        this.usernamePublisher = usernamePublisher;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.topMakanan = topMakanan;
    }

    public String getTopMakanan() {
        return topMakanan;
    }

    public void setTopMakanan(String topMakanan) {
        this.topMakanan = topMakanan;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
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

    public String getNamaMakanan() {
        return namaMakanan;
    }

    public void setNamaMakanan(String namaMakanan) {
        this.namaMakanan = namaMakanan;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

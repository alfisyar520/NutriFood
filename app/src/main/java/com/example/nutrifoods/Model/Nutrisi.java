package com.example.nutrifoods.Model;

public class Nutrisi {
    private String nama;
    private int berat;
    private int tinggi;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getBerat() {
        return berat;
    }

    public void setBerat(int berat) {
        this.berat = berat;
    }

    public int getTinggi() {
        return tinggi;
    }

    public void setTinggi(int tinggi) {
        this.tinggi = tinggi;
    }

    @Override
    public String toString() {
        return "Nutrisi{" +
                "nama='" + nama + '\'' +
                ", berat=" + berat +
                ", tinggi=" + tinggi +
                '}';
    }
}

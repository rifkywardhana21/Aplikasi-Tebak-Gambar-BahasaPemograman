package com.example.tebakgambar;

public class Nilai {

    private int id;
    private int nilai;

    // Diperlukan oleh Firebase (default constructor)
    public Nilai() {}

    public Nilai(int id, int nilai) {
        this.id = id;
        this.nilai = nilai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNilai() {
        return nilai;
    }

    public void setNilai(int nilai) {
        this.nilai = nilai;
    }
}

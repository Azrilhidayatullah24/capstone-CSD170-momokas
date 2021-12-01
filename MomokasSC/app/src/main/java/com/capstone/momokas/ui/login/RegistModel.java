package com.capstone.momokas.ui.login;

public class RegistModel {

    private String username, nama, alamat, nohp, email;

    public RegistModel(String username, String nama, String alamat, String nohp, String email) {
        this.username = username;
        this.nama = nama;
        this.alamat = alamat;
        this.nohp = nohp;
        this.email = email;

    }

    public RegistModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

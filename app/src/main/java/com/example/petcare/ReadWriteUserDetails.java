package com.example.petcare;

public class ReadWriteUserDetails {
    public String nama, nohp, email;

    public ReadWriteUserDetails(String textnama, String textnohp){
        this.nama = textnama;
        this.nohp = textnohp;
    }
    public ReadWriteUserDetails(){};

    public ReadWriteUserDetails(String textnama){
        this.nama = textnama;
    }

    public ReadWriteUserDetails(String textnama, String textemail, String textnohp){
        this.nama = textnama;
        this.email = textemail;
        this.nohp = textnohp;
    }
}

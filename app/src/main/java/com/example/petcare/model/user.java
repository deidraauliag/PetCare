package com.example.petcare.model;

public class user {
    private String uname, email, nohp, avatar;

    public user(){

    }
    public user(String uname, String email, String nohp, String avatar){
        this.uname = uname;
        this.email = email;
        this.nohp = nohp;
        this.avatar = avatar;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

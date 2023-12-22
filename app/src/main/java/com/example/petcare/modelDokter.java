package com.example.petcare;

public class modelDokter {
    private String Nama;
    private String Tahun;
    private String Bidang;
    private String Harga;
    private String Url;
    public modelDokter() {
    }

    public modelDokter(String nama, String tahun, String bidang, String harga, String url) {
        Nama = nama;
        Tahun = tahun;
        Bidang = bidang;
        Harga = harga;
        Url = url;
    }

    public String getNama() {
        return Nama;
    }
    public void setNama(String nama) { Nama = nama; }
    public String getTahun () { return Tahun; }
    public void setTahun(String tahun){ Tahun = tahun; }
    public String getBidang () { return Bidang; }
    public void setBidang(String bidang){ Bidang = bidang; }
    public String getHarga () { return Harga; }
    public void setHarga(String harga){ Harga = harga; }
    public String getUrl () { return Url; }
    public void setUrl(String url){ Url = url; }
}
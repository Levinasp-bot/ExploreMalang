package com.example.exploremalangjava;

import android.os.Parcel;
import android.os.Parcelable;

public class Wisata implements Parcelable {
    private String nama;
    private String deskripsi;
    private String foto;
    private String kategori;
    private String lokasi;
    private String id;

    public Wisata() {
        // Default constructor required for calls to DataSnapshot.getValue(Wisata.class)
    }

    public Wisata(String nama, String deskripsi, String foto, String kategori, String lokasi, String id) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.foto = foto;
        this.kategori = kategori;
        this.lokasi = lokasi;
        this.id = id;
    }

    protected Wisata(Parcel in) {
        nama = in.readString();
        deskripsi = in.readString();
        foto = in.readString();
        kategori = in.readString();
        lokasi = in.readString();
        id = in.readString();
    }

    public static final Creator<Wisata> CREATOR = new Creator<Wisata>() {
        @Override
        public Wisata createFromParcel(Parcel in) {
            return new Wisata(in);
        }

        @Override
        public Wisata[] newArray(int size) {
            return new Wisata[size];
        }
    };

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nama);
        dest.writeString(deskripsi);
        dest.writeString(foto);
        dest.writeString(kategori);
        dest.writeString(lokasi);
        dest.writeString(id);
    }
}


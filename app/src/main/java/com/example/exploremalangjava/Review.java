package com.example.exploremalangjava;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    private String ulasan;
    private String foto;
    private String wisataId;
    private String email;
    private String id;

    public Review() {

    }

    public Review(String ulasan, String foto, String wisataid, String userEmail, String id) {
        this.ulasan = ulasan;
        this.foto = foto;
        this.wisataId = wisataid;
        this.email = userEmail;
        this.id = id;
    }

    protected Review(Parcel in) {
        ulasan = in.readString();
        foto = in.readString();
        wisataId = in.readString();
        email = in.readString();
        id = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getUlasan() {
        return ulasan;
    }

    public void setUlasan(String ulasan) {
        this.ulasan = ulasan;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getWisataId() {
        return wisataId;
    }

    public void setWisataId(String wisataId) {
        this.wisataId = wisataId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        dest.writeString(ulasan);
        dest.writeString(foto);
        dest.writeString(wisataId);
        dest.writeString(email);
        dest.writeString(id);
    }
}


package com.example.uas_mad.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.List;

public class ItemTerbeli implements Parcelable {

    private List<Data> data;

    protected ItemTerbeli(Parcel in) {
    }

    public static final Creator<ItemTerbeli> CREATOR = new Creator<ItemTerbeli>() {
        @Override
        public ItemTerbeli createFromParcel(Parcel in) {
            return new ItemTerbeli(in);
        }

        @Override
        public ItemTerbeli[] newArray(int size) {
            return new ItemTerbeli[size];
        }
    };

    public static ItemTerbeli objectFromData(String str) {

        return new Gson().fromJson(str, ItemTerbeli.class);
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public static class Data {
        private int terbeli_id;
        private int item_id_terbeli;
        private int student_gamedata_id_terbeli;
        private int harga;

        public static Data objectFromData(String str) {

            return new Gson().fromJson(str, Data.class);
        }

        public int getTerbeli_id() {
            return terbeli_id;
        }

        public void setTerbeli_id(int terbeli_id) {
            this.terbeli_id = terbeli_id;
        }

        public int getItem_id_terbeli() {
            return item_id_terbeli;
        }

        public void setItem_id_terbeli(int item_id_terbeli) {
            this.item_id_terbeli = item_id_terbeli;
        }

        public int getStudent_gamedata_id_terbeli() {
            return student_gamedata_id_terbeli;
        }

        public void setStudent_gamedata_id_terbeli(int student_gamedata_id_terbeli) {
            this.student_gamedata_id_terbeli = student_gamedata_id_terbeli;
        }

        public int getHarga() {
            return harga;
        }

        public void setHarga(int harga) {
            this.harga = harga;
        }
    }
}

package com.example.uas_mad.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.List;

public class Item implements Parcelable {

    private List<Data> data;

    protected Item(Parcel in) {
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public static Item objectFromData(String str) {

        return new Gson().fromJson(str, Item.class);
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
        private int item_id;
        private String item_name;
        private int base_harga;
        private int penambahan_damage;

        public static Data objectFromData(String str) {

            return new Gson().fromJson(str, Data.class);
        }

        public int getItem_id() {
            return item_id;
        }

        public void setItem_id(int item_id) {
            this.item_id = item_id;
        }

        public String getItem_name() {
            return item_name;
        }

        public void setItem_name(String item_name) {
            this.item_name = item_name;
        }

        public int getBase_harga() {
            return base_harga;
        }

        public void setBase_harga(int base_harga) {
            this.base_harga = base_harga;
        }

        public int getPenambahan_damage() {
            return penambahan_damage;
        }

        public void setPenambahan_damage(int penambahan_damage) {
            this.penambahan_damage = penambahan_damage;
        }
    }
}

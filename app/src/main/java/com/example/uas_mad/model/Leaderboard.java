package com.example.uas_mad.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.List;

public class Leaderboard implements Parcelable {

    private List<Data> data;

    protected Leaderboard(Parcel in) {
    }

    public static final Creator<Leaderboard> CREATOR = new Creator<Leaderboard>() {
        @Override
        public Leaderboard createFromParcel(Parcel in) {
            return new Leaderboard(in);
        }

        @Override
        public Leaderboard[] newArray(int size) {
            return new Leaderboard[size];
        }
    };

    public static Leaderboard objectFromData(String str) {

        return new Gson().fromJson(str, Leaderboard.class);
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
        private int leaderboard_id;
        private String name;
        private String total_damage;

        public static Data objectFromData(String str) {

            return new Gson().fromJson(str, Data.class);
        }

        public int getLeaderboard_id() {
            return leaderboard_id;
        }

        public void setLeaderboard_id(int leaderboard_id) {
            this.leaderboard_id = leaderboard_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTotal_damage() {
            return total_damage;
        }

        public void setTotal_damage(String total_damage) {
            this.total_damage = total_damage;
        }
    }
}

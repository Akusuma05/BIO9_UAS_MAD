package com.example.uas_mad.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.List;

public class GameData implements Parcelable {

    private List<Gamedata> gamedata;

    protected GameData(Parcel in) {
    }

    public static final Creator<GameData> CREATOR = new Creator<GameData>() {
        @Override
        public GameData createFromParcel(Parcel in) {
            return new GameData(in);
        }

        @Override
        public GameData[] newArray(int size) {
            return new GameData[size];
        }
    };

    public static GameData objectFromData(String str) {

        return new Gson().fromJson(str, GameData.class);
    }

    public List<Gamedata> getGamedata() {
        return gamedata;
    }

    public void setGamedata(List<Gamedata> gamedata) {
        this.gamedata = gamedata;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public static class Gamedata {
        private int gamedata_id;
        private int student_id;
        private int total_damage;
        private int health_left;
        private int money;
        private int time_left;
        private int current_damage;

        public Gamedata(int gamedata_id, int student_id, int total_damage, int health_left, int money, int time_left, int current_damage) {
            this.gamedata_id = gamedata_id;
            this.student_id = student_id;
            this.total_damage = total_damage;
            this.health_left = health_left;
            this.money = money;
            this.time_left = time_left;
            this.current_damage = current_damage;
        }

        public static Gamedata objectFromData(String str) {

            return new Gson().fromJson(str, Gamedata.class);
        }

        public int getGamedata_id() {
            return gamedata_id;
        }

        public void setGamedata_id(int gamedata_id) {
            this.gamedata_id = gamedata_id;
        }

        public int getStudent_id() {
            return student_id;
        }

        public void setStudent_id(int student_id) {
            this.student_id = student_id;
        }

        public int getTotal_damage() {
            return total_damage;
        }

        public void setTotal_damage(int total_damage) {
            this.total_damage = total_damage;
        }

        public int getHealth_left() {
            return health_left;
        }

        public void setHealth_left(int health_left) {
            this.health_left = health_left;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getTime_left() {
            return time_left;
        }

        public void setTime_left(int time_left) {
            this.time_left = time_left;
        }

        public int getCurrent_damage() {
            return current_damage;
        }

        public void setCurrent_damage(int current_damage) {
            this.current_damage = current_damage;
        }
    }
}

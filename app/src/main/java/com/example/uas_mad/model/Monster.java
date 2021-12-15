package com.example.uas_mad.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.List;

public class Monster implements Parcelable {

    private List<Data> data;

    protected Monster(Parcel in) {
    }

    public static final Creator<Monster> CREATOR = new Creator<Monster>() {
        @Override
        public Monster createFromParcel(Parcel in) {
            return new Monster(in);
        }

        @Override
        public Monster[] newArray(int size) {
            return new Monster[size];
        }
    };

    public static Monster objectFromData(String str) {

        return new Gson().fromJson(str, Monster.class);
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
        private int monster_id;
        private String monster_name;
        private String monster_image_path_idle;
        private String monster_image_path_attack;
        private String monster_image_path_dead;

        public static Data objectFromData(String str) {

            return new Gson().fromJson(str, Data.class);
        }

        public int getMonster_id() {
            return monster_id;
        }

        public void setMonster_id(int monster_id) {
            this.monster_id = monster_id;
        }

        public String getMonster_name() {
            return monster_name;
        }

        public void setMonster_name(String monster_name) {
            this.monster_name = monster_name;
        }

        public String getMonster_image_path_idle() {
            return monster_image_path_idle;
        }

        public void setMonster_image_path_idle(String monster_image_path_idle) {
            this.monster_image_path_idle = monster_image_path_idle;
        }

        public String getMonster_image_path_attack() {
            return monster_image_path_attack;
        }

        public void setMonster_image_path_attack(String monster_image_path_attack) {
            this.monster_image_path_attack = monster_image_path_attack;
        }

        public String getMonster_image_path_dead() {
            return monster_image_path_dead;
        }

        public void setMonster_image_path_dead(String monster_image_path_dead) {
            this.monster_image_path_dead = monster_image_path_dead;
        }
    }
}

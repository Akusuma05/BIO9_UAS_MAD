package com.example.uas_mad.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.List;

public class MonsterTerbunuh implements Parcelable {
    private List<Data> data;

    protected MonsterTerbunuh(Parcel in) {
    }

    public static final Creator<MonsterTerbunuh> CREATOR = new Creator<MonsterTerbunuh>() {
        @Override
        public MonsterTerbunuh createFromParcel(Parcel in) {
            return new MonsterTerbunuh(in);
        }

        @Override
        public MonsterTerbunuh[] newArray(int size) {
            return new MonsterTerbunuh[size];
        }
    };

    public static MonsterTerbunuh objectFromData(String str) {

        return new Gson().fromJson(str, MonsterTerbunuh.class);
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
        private int terbunuh_id;
        private int monster_id_terbunuh;
        private int student_gamedata_id_terbunuh;
        private int monster_base_health;
        private int monster_health_left;

        public static Data objectFromData(String str) {

            return new Gson().fromJson(str, Data.class);
        }

        public int getTerbunuh_id() {
            return terbunuh_id;
        }

        public void setTerbunuh_id(int terbunuh_id) {
            this.terbunuh_id = terbunuh_id;
        }

        public int getMonster_id_terbunuh() {
            return monster_id_terbunuh;
        }

        public void setMonster_id_terbunuh(int monster_id_terbunuh) {
            this.monster_id_terbunuh = monster_id_terbunuh;
        }

        public int getStudent_gamedata_id_terbunuh() {
            return student_gamedata_id_terbunuh;
        }

        public void setStudent_gamedata_id_terbunuh(int student_gamedata_id_terbunuh) {
            this.student_gamedata_id_terbunuh = student_gamedata_id_terbunuh;
        }

        public int getMonster_base_health() {
            return monster_base_health;
        }

        public void setMonster_base_health(int monster_base_health) {
            this.monster_base_health = monster_base_health;
        }

        public int getMonster_health_left() {
            return monster_health_left;
        }

        public void setMonster_health_left(int monster_health_left) {
            this.monster_health_left = monster_health_left;
        }
    }
}

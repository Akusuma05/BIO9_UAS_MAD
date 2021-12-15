package com.example.uas_mad.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.List;

public class PertanyaanTerjawab implements Parcelable {


    private List<Data> data;

    protected PertanyaanTerjawab(Parcel in) {
    }

    public static final Creator<PertanyaanTerjawab> CREATOR = new Creator<PertanyaanTerjawab>() {
        @Override
        public PertanyaanTerjawab createFromParcel(Parcel in) {
            return new PertanyaanTerjawab(in);
        }

        @Override
        public PertanyaanTerjawab[] newArray(int size) {
            return new PertanyaanTerjawab[size];
        }
    };

    public static PertanyaanTerjawab objectFromData(String str) {

        return new Gson().fromJson(str, PertanyaanTerjawab.class);
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
        private int terjawab_id;
        private int pertanyaan_id_terjawab;
        private int student_gamedata_id_terjawab;

        public static Data objectFromData(String str) {

            return new Gson().fromJson(str, Data.class);
        }

        public int getTerjawab_id() {
            return terjawab_id;
        }

        public void setTerjawab_id(int terjawab_id) {
            this.terjawab_id = terjawab_id;
        }

        public int getPertanyaan_id_terjawab() {
            return pertanyaan_id_terjawab;
        }

        public void setPertanyaan_id_terjawab(int pertanyaan_id_terjawab) {
            this.pertanyaan_id_terjawab = pertanyaan_id_terjawab;
        }

        public int getStudent_gamedata_id_terjawab() {
            return student_gamedata_id_terjawab;
        }

        public void setStudent_gamedata_id_terjawab(int student_gamedata_id_terjawab) {
            this.student_gamedata_id_terjawab = student_gamedata_id_terjawab;
        }
    }
}

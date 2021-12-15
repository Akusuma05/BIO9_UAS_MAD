package com.example.uas_mad.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.List;

public class Pertanyaan implements Parcelable {

    private List<Data> data;

    protected Pertanyaan(Parcel in) {
    }

    public static final Creator<Pertanyaan> CREATOR = new Creator<Pertanyaan>() {
        @Override
        public Pertanyaan createFromParcel(Parcel in) {
            return new Pertanyaan(in);
        }

        @Override
        public Pertanyaan[] newArray(int size) {
            return new Pertanyaan[size];
        }
    };

    public static Pertanyaan objectFromData(String str) {

        return new Gson().fromJson(str, Pertanyaan.class);
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
        private int pertanyaan_id;
        private String pertanyaan;
        private String jawaban_benar;
        private String jawaban_salah1;
        private String jawaban_salah2;
        private String jawaban_salah3;

        public static Data objectFromData(String str) {

            return new Gson().fromJson(str, Data.class);
        }

        public int getPertanyaan_id() {
            return pertanyaan_id;
        }

        public void setPertanyaan_id(int pertanyaan_id) {
            this.pertanyaan_id = pertanyaan_id;
        }

        public String getPertanyaan() {
            return pertanyaan;
        }

        public void setPertanyaan(String pertanyaan) {
            this.pertanyaan = pertanyaan;
        }

        public String getJawaban_benar() {
            return jawaban_benar;
        }

        public void setJawaban_benar(String jawaban_benar) {
            this.jawaban_benar = jawaban_benar;
        }

        public String getJawaban_salah1() {
            return jawaban_salah1;
        }

        public void setJawaban_salah1(String jawaban_salah1) {
            this.jawaban_salah1 = jawaban_salah1;
        }

        public String getJawaban_salah2() {
            return jawaban_salah2;
        }

        public void setJawaban_salah2(String jawaban_salah2) {
            this.jawaban_salah2 = jawaban_salah2;
        }

        public String getJawaban_salah3() {
            return jawaban_salah3;
        }

        public void setJawaban_salah3(String jawaban_salah3) {
            this.jawaban_salah3 = jawaban_salah3;
        }
    }
}

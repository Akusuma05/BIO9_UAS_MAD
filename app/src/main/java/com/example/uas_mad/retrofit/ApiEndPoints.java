package com.example.uas_mad.retrofit;

import com.example.uas_mad.model.GameData;
import com.example.uas_mad.model.Item;
import com.example.uas_mad.model.ItemTerbeli;
import com.example.uas_mad.model.Leaderboard;
import com.example.uas_mad.model.Monster;
import com.example.uas_mad.model.MonsterTerbunuh;
import com.example.uas_mad.model.Pertanyaan;
import com.example.uas_mad.model.PertanyaanTerjawab;
import com.example.uas_mad.model.Profile;
import com.example.uas_mad.model.RegisterResponse;
import com.example.uas_mad.model.TokenResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiEndPoints {
    @POST("login")
    @FormUrlEncoded
    Call<TokenResponse> login(@Field("email") String email, @Field("password") String password);

    @POST("register")
    @FormUrlEncoded
    Call<RegisterResponse> register(@Field("name") String name, @Field("email") String email,
                                    @Field("password") String password,
                                    @Field("password_confirmation") String password_confirmation,
                                    @Field("school") String school,
                                    @Field("city") String city,
                                    @Field("birthyear") int birthyear,
                                    @Field("username")String username);

    //Logout
    @POST("logout")
    Call<JsonObject> logout();

    //Gamedata{Studentid}
    @GET("gamedata/{student_id}")
    Call<GameData> getGameData(@Path("student_id") int student_id);

    //Student
    @GET("profiledata")
    Call<Profile> getStudent();

    //Pertanyaan
    @GET("pertanyaan")
    Call<Pertanyaan> getPertanyaan();

    //Item
    @GET("item")
    Call<Item> getItem();

    //Monster
    @GET("monster")
    Call<Monster> getMonster();

    //Terbeli
    @GET("terbeli")
    Call<ItemTerbeli> getTerbeli();

    //Terbunuh
    @GET("terbunuh")
    Call<MonsterTerbunuh> getTerbunuh();

    //Terjawab
    @GET("terjawab")
    Call<PertanyaanTerjawab> getTerjawab();

    //Create Game Data
    @POST("gamedata")
    Call<GameData.Gamedata> createGamedata(@Body GameData.Gamedata gamedata);

    //Leaderboard
    @GET("leaderboard")
    Call<Leaderboard> getLeaderboard();
}

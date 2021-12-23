package com.example.uas_mad.retrofit;

import com.example.uas_mad.helper.Const;
import com.example.uas_mad.model.GameData;
import com.example.uas_mad.model.Item;
import com.example.uas_mad.model.ItemTerbeli;
import com.example.uas_mad.model.Monster;
import com.example.uas_mad.model.MonsterTerbunuh;
import com.example.uas_mad.model.Pertanyaan;
import com.example.uas_mad.model.PertanyaanTerjawab;
import com.example.uas_mad.model.Profile;
import com.example.uas_mad.model.RegisterResponse;
import com.example.uas_mad.model.TokenResponse;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RetrofitService {
    private final ApiEndPoints api;
    private static RetrofitService service;
    private static final String TAG = "RetrofitService";

    //Add Header
    public RetrofitService(String token){
        OkHttpClient.Builder client = new OkHttpClient.Builder();

        if(token.equals("")){
            client.addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(request);
            });
        }else{
            client.addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", token)
                        .build();
                return chain.proceed(request);
            });
        }

        api = new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build().create(ApiEndPoints.class);
    }

    //Biar bisa dipanggil di AuthRepository buat token
    public static RetrofitService getInstance(String token){
        if (service == null){
            service = new RetrofitService(token);
        }else if(!token.equals("")){
            service = new RetrofitService(token);
        }
        return service;
    }

    //Login
    public Call<TokenResponse> login(String email, String password){
        return api.login(email, password);
    }

    //Register
    public Call<RegisterResponse> register(String name, String email, String password, String password_confirmation){
        return api.register(name, email, password, password_confirmation);
    }

    //Logout
    public Call<JsonObject> logout() {return api.logout();}

    //Game Data Student
    public Call<GameData> getGameData(int student_id){return api.getGameData(student_id);}

    //Student Data
    public Call<Profile> getStudentData(){return api.getStudent();}

    //Pertanyaan
    public Call<Pertanyaan> getPertanyaan(){return api.getPertanyaan();}

    //Item
    public Call<Item> getItem(){return api.getItem();}

    //Monster
    public Call<Monster> getMonster(){return api.getMonster();}

    //Terbeli
    public Call<ItemTerbeli> getTerbeli(){return api.getTerbeli();}

    //Terbunuh
    public Call<MonsterTerbunuh> getTerbunuh(){return api.getTerbunuh();}

    //Terjawab
    public Call<PertanyaanTerjawab> getTerjawab(){return api.getTerjawab();}

    //Create Game data
    public Call<GameData.Gamedata> createGamedata(GameData.Gamedata gamedata){return api.createGamedata(gamedata);}
}

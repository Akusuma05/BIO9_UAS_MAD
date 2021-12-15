package com.example.uas_mad.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.uas_mad.model.Monster;
import com.example.uas_mad.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonsterRepository {
    private static MonsterRepository monsterRepository;
    private RetrofitService apiService;
    private static final String TAG = "MonsterRepository";

    private MonsterRepository(String token){
        Log.d(TAG, "token: "+token);
        apiService = RetrofitService.getInstance(token);
    }

    public static MonsterRepository getInstance(String token){
        if (monsterRepository == null){
            monsterRepository = new MonsterRepository(token);
        }
        return monsterRepository;
    }

    //Get Monster Buat di View Model
    public MutableLiveData<Monster> getMonster(){
        final MutableLiveData<Monster> listMonster = new MutableLiveData<>();

        apiService.getMonster().enqueue(new Callback<Monster>() {
            @Override
            public void onResponse(Call<Monster> call, Response<Monster> response) {
                Log.d(TAG, "onResponse: "+response.code());
                if (response.isSuccessful()){
                    if (response.body() != null){
                        Log.d(TAG, "onResponse" + response.body().getData().size());
                        listMonster.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<Monster> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return listMonster;
    }
}

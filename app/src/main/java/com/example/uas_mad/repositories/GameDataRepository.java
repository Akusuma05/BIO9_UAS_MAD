package com.example.uas_mad.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.uas_mad.model.GameData;
import com.example.uas_mad.retrofit.RetrofitService;

import java.sql.ClientInfoStatus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameDataRepository {
    private static GameDataRepository gamedataRepository;
    private RetrofitService apiService;
    private static final String TAG = "gamedataRepository";

    private GameDataRepository(String token){
        Log.d(TAG,"token: "+token);
        apiService = RetrofitService.getInstance(token);
    }

    public static GameDataRepository getInstance(String token){
        if(gamedataRepository == null){
            gamedataRepository = new GameDataRepository(token);
        }
        return gamedataRepository;
    }

    public synchronized void resetInstance(){
        if(gamedataRepository != null){
            gamedataRepository = null;
        }else{
            gamedataRepository = null;
        }
    }

    //Get Game Data {Student Id}
    public MutableLiveData<GameData> getGameData(int student_id){
        final MutableLiveData<GameData> usergamedata = new MutableLiveData<>();

        apiService.getGameData(student_id).enqueue(new Callback<GameData>() {
            @Override
            public void onResponse(Call<GameData> call, Response<GameData> response) {
                Log.d(TAG, "OnResponse" + response.code());
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse" + response.body());
                    usergamedata.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<GameData> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return usergamedata;
    }

    //Create Game Data
    public MutableLiveData<GameData.Gamedata> createGameData(GameData.Gamedata gamedata){
        final MutableLiveData<GameData.Gamedata> listAddGamedata = new MutableLiveData<>();

        apiService.createGamedata(gamedata).enqueue(new Callback<GameData.Gamedata>() {
            @Override
            public void onResponse(Call<GameData.Gamedata> call, Response<GameData.Gamedata> response) {
                Log.d(TAG, "OnResponse: "+response.body());
                listAddGamedata.postValue(response.body());
            }

            @Override
            public void onFailure(Call<GameData.Gamedata> call, Throwable t) {
                Log.e(TAG, "OnFailure " + t.getMessage());
            }
        });
        return listAddGamedata;
    }

}

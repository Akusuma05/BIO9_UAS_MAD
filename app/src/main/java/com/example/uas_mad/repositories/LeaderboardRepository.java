package com.example.uas_mad.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.uas_mad.model.GameData;
import com.example.uas_mad.model.Item;
import com.example.uas_mad.model.Leaderboard;
import com.example.uas_mad.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardRepository {
    private static LeaderboardRepository leaderboardRepository;
    private RetrofitService apiService;
    private static final String TAG = "LeaderboardRepository";

    public LeaderboardRepository(String token){
        Log.d(TAG, "token: "+token);
        apiService = RetrofitService.getInstance(token);
    }

    public static LeaderboardRepository getInstance(String token){
        if (leaderboardRepository == null){
            leaderboardRepository = new LeaderboardRepository(token);
        }
        return leaderboardRepository;
    }

    public synchronized void resetInstance(){
        if(leaderboardRepository != null){
            leaderboardRepository = null;
        }else{
            leaderboardRepository = null;
        }
    }

    //Function Get Item Buat di View Model
    public MutableLiveData<Leaderboard> getLeaderboard(){
        final MutableLiveData<Leaderboard> listLeaderboard = new MutableLiveData<>();

        apiService.getLeaderboard().enqueue(new Callback<Leaderboard>() {
            @Override
            public void onResponse(Call<Leaderboard> call, Response<Leaderboard> response) {
                Log.d(TAG, "onResponse: "+response.code());
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse" + response.body().getData().size());
                    listLeaderboard.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Leaderboard> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return listLeaderboard;
    }

    //Create Leaderboard
    public MutableLiveData<Leaderboard.Data> createLeaderboard(Leaderboard.Data leaderboard){
        final MutableLiveData<Leaderboard.Data> listAddLeaderboard = new MutableLiveData<>();

        apiService.createLeaderboard(leaderboard).enqueue(new Callback<Leaderboard.Data>() {
            @Override
            public void onResponse(Call<Leaderboard.Data> call, Response<Leaderboard.Data> response) {
                Log.d(TAG, "OnResponse: "+response.body());
                listAddLeaderboard.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Leaderboard.Data> call, Throwable t) {
                Log.e(TAG, "OnFailure " + t.getMessage());
            }
        });
        return listAddLeaderboard;
    }
}

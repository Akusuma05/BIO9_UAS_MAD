package com.example.uas_mad.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.uas_mad.model.MonsterTerbunuh;
import com.example.uas_mad.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TerbunuhRepository {
    private static TerbunuhRepository terbunuhRepository;
    private RetrofitService apiService;
    private static final String TAG = "Terbunuh Repository";

    private TerbunuhRepository(String token){
        Log.d(TAG, "token: "+token);
        apiService = RetrofitService.getInstance(token);
    }

    public static TerbunuhRepository getInstance(String token){
        if (terbunuhRepository == null){
            terbunuhRepository = new TerbunuhRepository(token);
        }
        return terbunuhRepository;
    }

    //Get Terbunuh buat di View Model
    public MutableLiveData<MonsterTerbunuh> getTerbunuh(){
        final MutableLiveData<MonsterTerbunuh> listTerbunuh = new MutableLiveData<>();

        apiService.getTerbunuh().enqueue(new Callback<MonsterTerbunuh>() {
            @Override
            public void onResponse(Call<MonsterTerbunuh> call, Response<MonsterTerbunuh> response) {
                Log.d(TAG, "onResponse: "+response.code());
                if (response.isSuccessful()){
                    if (response.body() != null){
                        Log.d(TAG, "onResponse" + response.body().getData().size());
                        listTerbunuh.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<MonsterTerbunuh> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return listTerbunuh;
    }
}

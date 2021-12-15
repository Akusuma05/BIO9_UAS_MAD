package com.example.uas_mad.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.uas_mad.model.PertanyaanTerjawab;
import com.example.uas_mad.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TerjawabRepository {
    private static TerjawabRepository terjawabRepository;
    private RetrofitService apiService;
    private static final String TAG = "Terjawab Repository";

    private TerjawabRepository(String token){
        Log.d(TAG, "token: "+token);
        apiService = RetrofitService.getInstance(token);
    }

    public static TerjawabRepository getInstance(String token){
        if (terjawabRepository == null){
            terjawabRepository = new TerjawabRepository(token);
        }
        return terjawabRepository;
    }

    //Get Terjawab Buat di View Model
    public MutableLiveData<PertanyaanTerjawab> getTerjawab(){
        final MutableLiveData<PertanyaanTerjawab> listTerjawab = new MutableLiveData<>();

        apiService.getTerjawab().enqueue(new Callback<PertanyaanTerjawab>() {
            @Override
            public void onResponse(Call<PertanyaanTerjawab> call, Response<PertanyaanTerjawab> response) {
                Log.d(TAG, "onResponse: "+response.code());
                if (response.isSuccessful()){
                    if (response.body() != null){
                        Log.d(TAG, "onResponse" + response.body().getData().size());
                        listTerjawab.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<PertanyaanTerjawab> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return listTerjawab;
    }
}

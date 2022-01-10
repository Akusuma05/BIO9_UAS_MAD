package com.example.uas_mad.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.uas_mad.model.Pertanyaan;
import com.example.uas_mad.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PertanyaanRepository {
    private static PertanyaanRepository pertanyaanRepository;
    private RetrofitService apiService;
    private static final String TAG = "Pertanyaan Repository";

    private PertanyaanRepository(String token){
        Log.d(TAG, "token: "+token);
        apiService = RetrofitService.getInstance(token);
    }

    public static PertanyaanRepository getInstance(String token){
        if (pertanyaanRepository == null){
            pertanyaanRepository = new PertanyaanRepository(token);
        }
        return pertanyaanRepository;
    }

    public synchronized void resetInstance(){
        if(pertanyaanRepository != null){
            pertanyaanRepository = null;
        }else{
            pertanyaanRepository = null;
        }
    }

    //Get Pertanyaan Buat di View Model
    public MutableLiveData<Pertanyaan> getPertanyaan(){
        final MutableLiveData<Pertanyaan> listPertanyaan = new MutableLiveData<>();

        apiService.getPertanyaan().enqueue(new Callback<Pertanyaan>() {
            @Override
            public void onResponse(Call<Pertanyaan> call, Response<Pertanyaan> response) {
                Log.d(TAG, "onResponse: "+response.code());
                if (response.isSuccessful()){
                    if (response.body() != null){
                        Log.d(TAG, "onResponse" + response.body().getData().size());
                        listPertanyaan.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<Pertanyaan> call, Throwable t) {

            }
        });
        return listPertanyaan;
    }
}

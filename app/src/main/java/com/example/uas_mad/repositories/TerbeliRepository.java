package com.example.uas_mad.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.uas_mad.model.ItemTerbeli;
import com.example.uas_mad.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TerbeliRepository {
    private static TerbeliRepository terbeliRepository;
    private RetrofitService apiService;
    private static final String TAG = "Terbeli Repository";

    private TerbeliRepository(String token){
        Log.d(TAG, "token: "+token);
        apiService = RetrofitService.getInstance(token);
    }

    public static TerbeliRepository getInstance(String token){
        if (terbeliRepository == null){
            terbeliRepository = new TerbeliRepository(token);
        }
        return terbeliRepository;
    }

    public MutableLiveData<ItemTerbeli> getTerbeli(){
        final MutableLiveData<ItemTerbeli> listTerbeli = new MutableLiveData<>();

        apiService.getTerbeli().enqueue(new Callback<ItemTerbeli>() {
            @Override
            public void onResponse(Call<ItemTerbeli> call, Response<ItemTerbeli> response) {
                Log.d(TAG, "onResponse: "+response.code());
                if (response.isSuccessful()){
                    if (response.body() != null){
                        Log.d(TAG, "onResponse" + response.body().getData().size());
                        listTerbeli.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<ItemTerbeli> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return listTerbeli;
    }
}

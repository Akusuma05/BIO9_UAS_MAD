package com.example.uas_mad.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.uas_mad.model.Item;
import com.example.uas_mad.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemRepository {
    private static ItemRepository itemRepository;
    private RetrofitService apiService;
    private static final String TAG = "ItemRepository";

    public ItemRepository(String token){
        Log.d(TAG, "token: "+token);
        apiService = RetrofitService.getInstance(token);
    }

    public static ItemRepository getInstance(String token){
        if (itemRepository == null){
            itemRepository = new ItemRepository(token);
        }
        return itemRepository;
    }

    //Function Get Item Buat di View Model
    public MutableLiveData<Item> getItem(){
        final MutableLiveData<Item> listItem = new MutableLiveData<>();

        apiService.getItem().enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Log.d(TAG, "onResponse: "+response.code());
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse" + response.body().getData().size());
                    listItem.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        return listItem;
    }
}

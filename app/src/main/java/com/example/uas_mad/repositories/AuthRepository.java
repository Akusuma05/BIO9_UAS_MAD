package com.example.uas_mad.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.uas_mad.model.RegisterResponse;
import com.example.uas_mad.model.TokenResponse;
import com.example.uas_mad.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private static AuthRepository authRepository;
    private RetrofitService apiService;
    private static final String TAG = "AuthRepository";

    //Buat yang ndak perlu token
    private AuthRepository(){
        apiService = RetrofitService.getInstance("");
    }

    public static AuthRepository getInstance(){
        if (authRepository == null){
            authRepository = new AuthRepository();
        }
        return authRepository;
    }

    public MutableLiveData<TokenResponse> login(String email, String password){
        MutableLiveData<TokenResponse> tokenResponse = new MutableLiveData<>();

        apiService.login(email, password).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: "+response.code());
                    if (response.code() == 200){
                        if (response.body() != null){
                            Log.d(TAG, "onResponse: "+response.body().getAccess_token());
                            tokenResponse.postValue(response.body());
                        }
                    }
                }else{
                    Log.d(TAG, "onResponse: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });

        return tokenResponse;
    }

    public MutableLiveData<RegisterResponse> register (String name, String email, String password, String password_confirmation) {
        MutableLiveData<RegisterResponse> registerResponse = new MutableLiveData<>();
        apiService.register(name, email, password, password_confirmation).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse : "+response.code());
                    if (response.code() == 200){
                        if (response.body() != null){
                            Log.d(TAG, "onResponse : " + response.body());
                            registerResponse.postValue(response.body());
                        }
                    }else{
                        Log.d(TAG, "onResponse : "+ response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e(TAG, "onFailure : " +t.getMessage());
            }
        });
        return registerResponse;
    }
}

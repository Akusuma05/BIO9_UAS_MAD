package com.example.uas_mad.view.RegisterView;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.uas_mad.model.RegisterResponse;
import com.example.uas_mad.repositories.AuthRepository;

public class RegisterViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    private static final String TAG = "RegisterViewModel";

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        authRepository = AuthRepository.getInstance();
    }

    public MutableLiveData<RegisterResponse> register(String name, String email, String password, String password_confirmation, String school, String city, int birthyear, String username){
        return authRepository.register(name, email, password, password_confirmation, school, city, birthyear, username);
    }
}

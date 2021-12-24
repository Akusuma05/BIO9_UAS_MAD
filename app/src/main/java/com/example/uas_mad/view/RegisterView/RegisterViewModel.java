package com.example.uas_mad.view.RegisterView;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.uas_mad.model.GameData;
import com.example.uas_mad.model.Profile;
import com.example.uas_mad.model.RegisterResponse;
import com.example.uas_mad.model.TokenResponse;
import com.example.uas_mad.repositories.AuthRepository;
import com.example.uas_mad.repositories.GameDataRepository;
import com.example.uas_mad.repositories.ProfileRepository;

public class RegisterViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    private GameDataRepository gameDataRepository;
    private ProfileRepository profileRepository;
    private static final String TAG = "RegisterViewModel";

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        authRepository = AuthRepository.getInstance();
    }

    public void init(String token){
        Log.d(TAG, "init: "+token);
        gameDataRepository = GameDataRepository.getInstance(token);
        profileRepository = ProfileRepository.getInstance(token);
    }

    //Register
    public MutableLiveData<RegisterResponse> register(String name, String email, String password, String password_confirmation, String school, String city, int birthyear, String username){
        return authRepository.register(name, email, password, password_confirmation, school, city, birthyear, username);
    }

    //Login
    public MutableLiveData<TokenResponse> login(String email, String password){
        return authRepository.login(email, password);
    }

    //Create Game Data
    public MutableLiveData<GameData.Gamedata> createGamedata(GameData.Gamedata gamedata){
        return gameDataRepository.createGameData(gamedata);
    }

    //== Begin of view model to get Student data
    private LiveData<Profile> resultProfile = new MutableLiveData<>();
    public void getStudentData(){
        resultProfile = profileRepository.getStudentData();
    }
    public LiveData<Profile> getResultStudentData(){
        return resultProfile;
    }
    //== End of View Model to get Student Data

    //Logout
    public LiveData<String> logout(){
        profileRepository.resetInstance();
        gameDataRepository.resetInstance();
        return profileRepository.logout();
    }

    //Clear Access Token
    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
        profileRepository.resetInstance();
        gameDataRepository.resetInstance();
    }
}

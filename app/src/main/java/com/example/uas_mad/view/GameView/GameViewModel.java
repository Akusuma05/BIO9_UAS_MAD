package com.example.uas_mad.view.GameView;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.uas_mad.repositories.ProfileRepository;

public class GameViewModel extends AndroidViewModel {
    private ProfileRepository profileRepository;
    private static final String TAG = "GameViewModel";

    public GameViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(String token){
        Log.d(TAG, "token: "+token);
        profileRepository = ProfileRepository.getInstance(token);
    }

    public LiveData<String> logout(){
        profileRepository.resetInstance();
        return profileRepository.logout();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
        profileRepository.resetInstance();
    }
}

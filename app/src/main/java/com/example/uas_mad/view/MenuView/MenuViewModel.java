package com.example.uas_mad.view.MenuView;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.uas_mad.model.Profile;
import com.example.uas_mad.repositories.GameDataRepository;
import com.example.uas_mad.repositories.LeaderboardRepository;
import com.example.uas_mad.repositories.PertanyaanRepository;
import com.example.uas_mad.repositories.ProfileRepository;

public class MenuViewModel extends AndroidViewModel {
    private ProfileRepository profileRepository;
    private LeaderboardRepository leaderboardRepository;
    private GameDataRepository gameDataRepository;
    private PertanyaanRepository pertanyaanRepository;
    private static final String TAG = "MenuViewModel";

    public MenuViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(String token){
        Log.d(TAG, "token: "+token);
        profileRepository = ProfileRepository.getInstance(token);
        gameDataRepository = GameDataRepository.getInstance(token);
        leaderboardRepository = LeaderboardRepository.getInstance(token);
        pertanyaanRepository = PertanyaanRepository.getInstance(token);
    }

    public LiveData<String> logout(){
        profileRepository.resetInstance();
        gameDataRepository.resetInstance();
        leaderboardRepository.resetInstance();
        pertanyaanRepository.resetInstance();
        return profileRepository.logout();
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

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
        profileRepository.resetInstance();
        gameDataRepository.resetInstance();
        leaderboardRepository.resetInstance();
        pertanyaanRepository.resetInstance();
    }
}

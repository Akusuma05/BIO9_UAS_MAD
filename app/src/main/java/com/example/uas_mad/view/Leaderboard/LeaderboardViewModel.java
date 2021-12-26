package com.example.uas_mad.view.Leaderboard;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.uas_mad.model.Leaderboard;
import com.example.uas_mad.model.Pertanyaan;
import com.example.uas_mad.repositories.LeaderboardRepository;

public class LeaderboardViewModel extends AndroidViewModel {
    private LeaderboardRepository leaderboardRepository;

    private static final String TAG = "LeaderboardViewModel";

    public LeaderboardViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(String token){
        Log.d(TAG, "init: "+token);
        leaderboardRepository = LeaderboardRepository.getInstance(token);
    }

    //View Model Get Leaderboard
    private MutableLiveData<Leaderboard> resultLeaderboard = new MutableLiveData<>();
    public void getLeaderboard(){
        resultLeaderboard = leaderboardRepository.getLeaderboard();
    }
    public LiveData<Leaderboard> getResultLeaderboard(){return resultLeaderboard;}

    @Override
    protected void onCleared() {
        super.onCleared();
        leaderboardRepository.resetInstance();
    }
}

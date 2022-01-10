package com.example.uas_mad.view.GameView;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.uas_mad.model.GameData;
import com.example.uas_mad.model.Leaderboard;
import com.example.uas_mad.model.Pertanyaan;
import com.example.uas_mad.repositories.GameDataRepository;
import com.example.uas_mad.repositories.LeaderboardRepository;
import com.example.uas_mad.repositories.PertanyaanRepository;

public class GameViewModel extends AndroidViewModel {
    private GameDataRepository gameDataRepository;
    private PertanyaanRepository pertanyaanRepository;
    private LeaderboardRepository leaderboardRepository;

    private static final String TAG = "GameViewModel";

    public GameViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(String token){
        Log.d(TAG, "init: "+token);
        gameDataRepository = GameDataRepository.getInstance(token);
        pertanyaanRepository = PertanyaanRepository.getInstance(token);
        leaderboardRepository = LeaderboardRepository.getInstance(token);
    }

    //View Model Get Game Data {Student ID}
    private MutableLiveData<GameData> resultGameData = new MutableLiveData<>();
    public void getGamedata(int student_id){
        resultGameData = gameDataRepository.getGameData(student_id);
    }
    public LiveData<GameData> getResultGameData(){return resultGameData;}

    //View Model Get Pertanyaan
    private MutableLiveData<Pertanyaan> resultPertanyaan = new MutableLiveData<>();
    public void getPertanyaan(){
        resultPertanyaan = pertanyaanRepository.getPertanyaan();
    }
    public LiveData<Pertanyaan> getResultPertanyaan(){return resultPertanyaan;}


    //Create Leaderboard
    public MutableLiveData<Leaderboard.Data> createLeaderboard(Leaderboard.Data leaderboard){
        return leaderboardRepository.createLeaderboard(leaderboard);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        gameDataRepository.resetInstance();
        pertanyaanRepository.resetInstance();
        leaderboardRepository.resetInstance();
    }
}

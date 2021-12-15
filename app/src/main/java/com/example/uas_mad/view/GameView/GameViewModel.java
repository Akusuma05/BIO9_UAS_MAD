package com.example.uas_mad.view.GameView;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.uas_mad.model.GameData;
import com.example.uas_mad.model.Item;
import com.example.uas_mad.model.ItemTerbeli;
import com.example.uas_mad.model.Monster;
import com.example.uas_mad.model.MonsterTerbunuh;
import com.example.uas_mad.model.Pertanyaan;
import com.example.uas_mad.model.PertanyaanTerjawab;
import com.example.uas_mad.repositories.GameDataRepository;
import com.example.uas_mad.repositories.ItemRepository;
import com.example.uas_mad.repositories.MonsterRepository;
import com.example.uas_mad.repositories.PertanyaanRepository;
import com.example.uas_mad.repositories.ProfileRepository;
import com.example.uas_mad.repositories.TerbeliRepository;
import com.example.uas_mad.repositories.TerbunuhRepository;
import com.example.uas_mad.repositories.TerjawabRepository;

public class GameViewModel extends AndroidViewModel {
    private GameDataRepository gameDataRepository;
    private PertanyaanRepository pertanyaanRepository;
    private ItemRepository itemRepository;
    private MonsterRepository monsterRepository;
    private TerbeliRepository terbeliRepository;
    private TerbunuhRepository terbunuhRepository;
    private TerjawabRepository terjawabRepository;

    private static final String TAG = "GameViewModel";

    public GameViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(String token){
        Log.d(TAG, "init: "+token);
        gameDataRepository = gameDataRepository.getInstance(token);
        pertanyaanRepository = pertanyaanRepository.getInstance(token);
        itemRepository = itemRepository.getInstance(token);
        monsterRepository = monsterRepository.getInstance(token);
        terbeliRepository = terbeliRepository.getInstance(token);
        terbunuhRepository = terbunuhRepository.getInstance(token);
        terjawabRepository = terjawabRepository.getInstance(token);
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

    //View Model Get Item
    public MutableLiveData<Item> resultItem = new MutableLiveData<>();
    public void getItem(){resultItem = itemRepository.getItem();}
    public LiveData<Item> getResultItem(){return resultItem;}

    //View Model Get Monster
    public MutableLiveData<Monster> resultMonster = new MutableLiveData<>();
    public void getMonster(){resultMonster = monsterRepository.getMonster();}
    public LiveData<Monster> getResultMonster(){return resultMonster;}

    //View Model Get Terbeli
    public MutableLiveData<ItemTerbeli> resultTerbeli = new MutableLiveData<>();
    public void getTerbeli(){resultTerbeli = terbeliRepository.getTerbeli();}
    public LiveData<ItemTerbeli> getResultTerbeli(){return resultTerbeli;}

    //View Model Get Terbunuh
    public MutableLiveData<MonsterTerbunuh> resultTerbunuh = new MutableLiveData<>();
    public void getTerbunuh(){resultTerbunuh = terbunuhRepository.getTerbunuh();}
    public LiveData<MonsterTerbunuh> getResultTerbunuh(){return resultTerbunuh;}

    //View Model Get Terjawab
    public MutableLiveData<PertanyaanTerjawab> resultTerjawab = new MutableLiveData<>();
    public void getTerjawab(){resultTerjawab = terjawabRepository.getTerjawab();}
    public LiveData<PertanyaanTerjawab> getResultTerjawab(){return resultTerjawab;}
}

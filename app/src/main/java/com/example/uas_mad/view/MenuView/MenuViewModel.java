package com.example.uas_mad.view.MenuView;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.uas_mad.model.Profile;
import com.example.uas_mad.repositories.ProfileRepository;
import com.example.uas_mad.retrofit.RetrofitService;

public class MenuViewModel extends AndroidViewModel {
    private ProfileRepository profileRepository;
    private static final String TAG = "MenuViewModel";

    public MenuViewModel(@NonNull Application application) {
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
    }
}

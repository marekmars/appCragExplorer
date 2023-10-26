package com.heissen.cragexplorer.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.heissen.cragexplorer.request.ApiService;

import java.util.Base64;

public class HomeViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mText;
    private JsonObject jasonObject;
    private final Gson gson = new Gson();
    String[] tokenParts ;
    private String token;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        token = ApiService.leerToken(getApplication());
        tokenParts = token.split("\\.");

    }

    public JsonObject getJasonObject() {
        return jasonObject;
    }    public LiveData<String> getText() {
        return mText;
    }


}
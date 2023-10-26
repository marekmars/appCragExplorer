package com.heissen.cragexplorer.ui.loogbook;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogbookViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LogbookViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
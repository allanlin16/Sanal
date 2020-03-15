package com.allanlin97.ui.building;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BuildingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BuildingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Buildings");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
package com.allanlin97.ui.extinguisher;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExtinguisherViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ExtinguisherViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Extinguisher");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
package com.university.chat.ui.viewModel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.university.chat.ui.SingleLiveEvent;

public class GeneralChatViewModel extends ViewModel {
    private SingleLiveEvent<Uri> mutableLiveDataUri = new SingleLiveEvent<>();

    public void setMutableLiveDataUri(Uri uri){
        mutableLiveDataUri.setValue(uri);
    }

    public LiveData<Uri> getUriLivedata(){
        return mutableLiveDataUri;
    }
}

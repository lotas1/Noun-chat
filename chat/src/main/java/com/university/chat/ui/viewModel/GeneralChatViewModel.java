package com.university.chat.ui.viewModel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.university.chat.ui.SingleLiveEvent;

import java.util.HashMap;
import java.util.Map;

public class GeneralChatViewModel extends ViewModel {
    private SingleLiveEvent<Uri> mutableLiveDataUri = new SingleLiveEvent<>();
    private SingleLiveEvent<Map<String, String>> singleLiveEventReplyMessage = new SingleLiveEvent<>();

    public void setMutableLiveDataUri(Uri uri){
        mutableLiveDataUri.setValue(uri);
    }

    public LiveData<Uri> getUriLivedata(){
        return mutableLiveDataUri;
    }

    public void setUserReplyInfo(String username, String message){
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("message", message);
        singleLiveEventReplyMessage.setValue(map);
    }

    public LiveData<Map<String, String>> getUserReplyInfo(){
        return singleLiveEventReplyMessage;
    }

}

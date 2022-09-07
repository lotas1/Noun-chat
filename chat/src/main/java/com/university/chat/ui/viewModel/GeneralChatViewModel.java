package com.university.chat.ui.viewModel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.university.chat.utility.SingleLiveEvent;

import java.util.HashMap;
import java.util.Map;

public class GeneralChatViewModel extends ViewModel {
    private SingleLiveEvent<Map<String, Object>> singleLiveEventReplyMessage = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> singleLiveEventReplyPosition = new SingleLiveEvent<>();
    private MutableLiveData<Integer> fabBadgeLiveData = new MutableLiveData<>();


    public void setUserReplyInfo(String username, String message, int position, int color, boolean isUserAdmin, String messageKey){
        Map<String, Object> map = new HashMap<>();
        map.put("replyUsername", username);
        map.put("replyUsernameColor", color);
        map.put("replyMessage", message);
        map.put("replyPosition", position);
        map.put("replyUserAdmin", isUserAdmin);
        map.put("replyMessageKey", messageKey);
        singleLiveEventReplyMessage.setValue(map);
    }

    public LiveData<Map<String, Object>> getUserReplyInfo(){
        return singleLiveEventReplyMessage;
    }

    public void setReplyPosition(int position){
        singleLiveEventReplyPosition.setValue(position);
    }

    public LiveData<Integer> getReplyPositionLiveData(){
        return singleLiveEventReplyPosition;
    }

    public void setFabBadgeLiveData(int badgeNumber){
        fabBadgeLiveData.setValue(badgeNumber);
    }

    public LiveData<Integer> getFabBadgeNumber(){
        return fabBadgeLiveData;
    }

}

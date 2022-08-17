package com.university.chat.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.university.chat.R;

public class GeneralChatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private LinearLayout linearLayoutUser, linearLayoutAdmin;
    private TextView textViewMessage, textViewGroupInfoMessage;
    private ImageView imageViewGroupInfoEdit;
    private LinearLayout linearEditLayoutGroupInfo;
    private AutoCompleteTextView autoCompleteTextViewInfo;
    private TextInputLayout textInputLayoutInfo;
    private Button buttonCancel, buttonProceed;
    private Query queryBan;
    private FirebaseUser user;
    boolean isAdminOnly, isUserBan, isUserAdmin;
    String groupName, groupKey;
    int usersCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_chat);

        // instantiate views
        toolbar = findViewById(R.id.toolbar_general_chat);
        linearLayoutUser = findViewById(R.id.linearLayout_user_generalChat);
        linearLayoutAdmin = findViewById(R.id.linearLayout_adminOnly_generalChat);
        textViewMessage = findViewById(R.id.textView_message_generalChat);
        // close activity on click
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        // instance of bundle
        Bundle b = getIntent().getExtras();

        // variables holds value for is admin only
         isAdminOnly = b.getBoolean("adminOnly");
         isUserBan = b.getBoolean("isUserBan");
         isUserAdmin = b.getBoolean("isUserAdmin");
         groupName = b.getString("groupName");
         usersCount = b.getInt("usersCount");
         groupKey = b.getString("groupKey");

        // set toolbar title
        toolbar.setTitle(groupName);
        toolbar.setSubtitle(String.valueOf(usersCount).concat(" members"));

        // toolbar menu navigation
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.groupInfo){
                groupInfo();
            }else if (item.getItemId() == R.id.muteGroup) {
                // check if user is admin
                if (isUserAdmin){

                }else {
                    // notify user not admin
                    // notify user not admin
                    showAlertDialog(GeneralChatActivity.this, "Admin Feature", "Only admin can access this features.");
                }
            } else if (item.getItemId() == R.id.deleteGroup) {
                // check if user is admin
                if (isUserAdmin){

                }else {
                    // notify user not admin
                    // notify user not admin
                    showAlertDialog(GeneralChatActivity.this, "Admin Feature", "Only admin can access this features.");
                }
            }
            return false;
        });

        checkIsAdminOnly();

        // on click opens all users list.
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if user is admin
                if (isUserAdmin){
                    // navigate to view for creating group.
                    //Intent intent = new Intent(UserGroupsActivity.this, NewGroupActivity.class);
                    //startActivity(intent);
                    Toast.makeText(GeneralChatActivity.this, "clicked me", Toast.LENGTH_SHORT).show();
                }else {
                    // notify user not admin
                }
            }
        });


    }

    private void groupInfo(){
        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.group_info_custom_dialog, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();

        // instantiate views
        textViewGroupInfoMessage = customLayout.findViewById(R.id.textView_groupInfo);
        imageViewGroupInfoEdit = customLayout.findViewById(R.id.imageView_edit_groupInfo);
        linearEditLayoutGroupInfo = customLayout.findViewById(R.id.linearLayout_editLayout_groupInfo);
        autoCompleteTextViewInfo = customLayout.findViewById(R.id.autoCompleteTextview_info_groupInfo);
        textInputLayoutInfo = customLayout.findViewById(R.id.textInputLayout_info_groupInfo);
        buttonCancel = customLayout.findViewById(R.id.button_cancel_groupInfo);
        buttonProceed = customLayout.findViewById(R.id.button_proceed_groupInfo);

        // set edit layout visibility to GONE
        linearEditLayoutGroupInfo.setVisibility(View.GONE);

        // check if user is admin
        if (isUserAdmin){

        }else {
            // notify user not admin and set edit group button visibility to GONE
            imageViewGroupInfoEdit.setVisibility(View.GONE);
        }
        // on click set visible edit layout
        imageViewGroupInfoEdit.setOnClickListener(v -> {
            textViewGroupInfoMessage.setVisibility(View.GONE);
            linearEditLayoutGroupInfo.setVisibility(View.VISIBLE);
        });
        // proceed button
        buttonProceed.setOnClickListener(v -> {

        });
        // cancel button dismiss dialog
        buttonCancel.setOnClickListener(v -> dialog.dismiss());
        // show dialog
        dialog.show();

    }

    private void checkIsAdminOnly(){
        // checks if the group isAminOnly or not(for all users to chat)
        if (isAdminOnly){
            // checks if user is an admin and not also ban for violation
            if (isUserAdmin){
                // checks if user is not ban for violation
                if (isUserBan) {
                    linearLayoutUser.setVisibility(View.GONE);
                    // updates user for rule violation
                    textViewMessage.setText("For Violation of rules you have been ban from sending messages");
                    linearLayoutAdmin.setVisibility(View.VISIBLE);
                }else {
                    linearLayoutUser.setVisibility(View.VISIBLE);
                    linearLayoutAdmin.setVisibility(View.GONE);
                }
            }else {
                linearLayoutUser.setVisibility(View.GONE);
                linearLayoutAdmin.setVisibility(View.VISIBLE);
            }

        }else {
            // checks if user is not ban for violation
            if (isUserBan) {
                linearLayoutUser.setVisibility(View.GONE);
                // updates user for rule violation
                textViewMessage.setText("For Violation of rules you have been ban from sending messages");
                linearLayoutAdmin.setVisibility(View.VISIBLE);
            }else {
                linearLayoutUser.setVisibility(View.VISIBLE);
                linearLayoutAdmin.setVisibility(View.GONE);
            }

        }
    }
    // method for checking is user is banned or not
    private void checkIfUserIsBan(){
        // get current user details
        user = FirebaseAuth.getInstance().getCurrentUser();
        // firebase location path
        queryBan = FirebaseDatabase.getInstance().getReference("BanUser");
        // listener for changes in the data location
        queryBan.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isUserBan;
                if (snapshot.child(user.getUid()).exists()){
                    // update UI (blocked user)
                    isUserBan = true;
                }else {
                    // update UI (not blocked user)
                    isUserBan = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showAlertDialog(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("ok", (dialog, which) -> {

                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
package com.university.chat.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
    private MenuItem item1;
    private Query queryBan, queryUser;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    boolean  isUserBan, isUserAdmin;
    String groupName, groupKey, groupImage;
    int usersCount;
    private boolean isGroupMute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_chat);
        // retrieve an instance of your database
        database = FirebaseDatabase.getInstance();
        // location reference in database
        myRef = FirebaseDatabase.getInstance().getReference("Groups");

        // instantiate views
        toolbar = findViewById(R.id.toolbar_general_chat);
        linearLayoutUser = findViewById(R.id.linearLayout_user_generalChat);
        linearLayoutAdmin = findViewById(R.id.linearLayout_adminOnly_generalChat);
        textViewMessage = findViewById(R.id.textView_message_generalChat);
        item1 = toolbar.getMenu().findItem(R.id.muteGroup);

        // instance of bundle
        Bundle b = getIntent().getExtras();

        // variables holds value for is admin only
        //isAdminOnly = b.getBoolean("adminOnly");
        isUserBan = b.getBoolean("isUserBan");
        isUserAdmin = b.getBoolean("isUserAdmin");
        groupName = b.getString("groupName");
        usersCount = b.getInt("usersCount");
        groupKey = b.getString("groupKey");
        groupImage = b.getString("groupImage");


        // close activity on click
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        // on click opens all users list.
        toolbar.setOnClickListener(v -> {
            // check if user is admin
            if (isUserAdmin){
                // navigate to view for creating group.
                //Intent intent = new Intent(UserGroupsActivity.this, NewGroupActivity.class);
                //startActivity(intent);
                Toast.makeText(GeneralChatActivity.this, "clicked me", Toast.LENGTH_SHORT).show();
            }
        });

        // set toolbar title
        toolbar.setTitle(groupName);
        toolbar.setSubtitle(String.valueOf(usersCount).concat(" members"));

        // toolbar menu navigation
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.groupInfo){
                // display group info or description.
                groupInfo();
            }else if (item.getItemId() == R.id.muteGroup) {
                // check if user is admin
                if (isUserAdmin){
                    if (isGroupMute) {
                        // mute group
                        muteOrUnMuteGroup("UnMute", "Do you want to un-mute group?", "UnMute", false);
                    }else {
                        //UnMute group.
                        muteOrUnMuteGroup("Mute", "Do you want to mute group?", "Mute", true);
                    }
                }else {
                    // notify user not admin
                    showAlertDialog(GeneralChatActivity.this, "Admin Feature", "Only admin can access this features.");
                }
            } else if (item.getItemId() == R.id.deleteGroup) {
                // check if user is admin
                if (isUserAdmin){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Delete Group")
                            .setMessage("Do you want to delete group?")
                            .setNegativeButton("cancel", (dialog, which) -> {

                            })
                            .setPositiveButton("delete", (dialog, which) -> {
                                // delete group
                                deleteGroup();
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    // notify user not admin
                    showAlertDialog(GeneralChatActivity.this, "Admin Feature", "Only admin can access this features.");
                }
            }
            return false;
        });

        // init onCreate
        init();

    }

    private void init(){
        myRef.child(groupKey).child(getStringResource(R.string.adminOnly)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    isGroupMute = (boolean) snapshot.getValue();
                    // checks isGroupMute and update user messaging ui.
                    checkIsGroupAdminOnly(isGroupMute);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteGroup(){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl(groupImage);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // File deleted successfully
                // delete group
                myRef.child(groupKey).removeValue();
                finish();
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
        // clear error when clicked
        clearError(autoCompleteTextViewInfo, textInputLayoutInfo);

        // check if user is admin
        if (isUserAdmin){

        }else {
            // notify user not admin and set edit group button visibility to GONE
            imageViewGroupInfoEdit.setVisibility(View.GONE);
        }

        // read data(group info) from firebase
        queryUser = FirebaseDatabase.getInstance().getReference("Groups");
        queryUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(groupKey).child(getStringResource(R.string.groupInfo)).exists()){
                    // get group info or description from database and update user ui.
                    String info = snapshot.child(groupKey).child(getStringResource(R.string.groupInfo)).getValue().toString();
                    textViewGroupInfoMessage.setText(info);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // on click set visible group info  edit layout
        imageViewGroupInfoEdit.setOnClickListener(v -> {
            textViewGroupInfoMessage.setVisibility(View.GONE);
            linearEditLayoutGroupInfo.setVisibility(View.VISIBLE);
            // get info and write to edit auto complete textView
            autoCompleteTextViewInfo.setText(textViewGroupInfoMessage.getText());
        });

        // proceed button
        buttonProceed.setOnClickListener(v -> {
            if (isEmpty(autoCompleteTextViewInfo)){
                textInputLayoutInfo.setError("group info empty");
            }else {
                String info = autoCompleteTextViewInfo.getText().toString();

                myRef.child(groupKey).child(getStringResource(R.string.groupInfo)).setValue(info);
                dialog.dismiss();

            }
        });
        // cancel button dismiss dialog
        buttonCancel.setOnClickListener(v -> dialog.dismiss());
        // show dialog
        dialog.show();

    }
    private void checkIsGroupAdminOnly(boolean isGroupMute){
        // checks if the group isAminOnly or not(for all users to chat)
        if (isGroupMute){
            // update appbar menu
            item1.setTitle("UnMute");
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
            // update appbar menu
            item1.setTitle("Mute");
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

    private void muteOrUnMuteGroup(String title, String message, String positiveButton, boolean muteorunmute){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setNegativeButton("cancel", (dialog, which) -> {

                })
                .setPositiveButton(positiveButton, (dialog, which) -> {
                    myRef.child(groupKey).child(getStringResource(R.string.adminOnly)).setValue(muteorunmute);

                });
        AlertDialog dialog = builder.create();
        dialog.show();
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
    // returns string from resources
    private String getStringResource(int string){
        return getResources().getString(string);
    }

    // clears textInput layout error
    private void clearError(AutoCompleteTextView autoCompleteTextView, TextInputLayout textInputLayout){
        autoCompleteTextView.setOnClickListener(v -> textInputLayout.setError(null));
    }

    // checks if autocomplete text view is empty.
    private boolean isEmpty(AutoCompleteTextView autoCompleteTextView) {
        return TextUtils.isEmpty(autoCompleteTextView.getEditableText());
    }
}
package com.university.chat.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.UploadTask;
import com.university.chat.R;
import com.university.chat.data.model.ChatModel;
import com.university.chat.ui.adapter.RecyclerViewAdapterChat;
import com.university.chat.ui.viewModel.GeneralChatViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GeneralChatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private LinearLayout linearLayoutUser, linearLayoutAdmin;
    private TextView textViewMessage, textViewGroupInfoMessage;
    private ImageView imageViewGroupInfoEdit, imageViewDeleteGroupImage, imageViewGroupImage, imageViewSendUserData;
    private LinearLayout linearEditLayoutGroupInfo;
    private AutoCompleteTextView autoCompleteTextViewInfo;
    private TextInputLayout textInputLayoutInfo;
    private Button buttonCancel, buttonProceed;
    private EditText editTextUserMessage;
    private RecyclerView recyclerViewChatData;
    private RecyclerViewAdapterChat recyclerViewAdapterChat;
    private MenuItem item1;
    private Query queryBan, queryUser, queryChatMessages;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference groupRef, userRef, chatRef;
    boolean  isUserBan, isUserAdmin;
    private String groupName, groupKey, groupImage, username;
    int usersCount;
    private boolean isGroupMute;
    private static final int PICK_IMAGE = 100;
    private boolean isImageSelected, isPreviousImage = false;
    private Uri selectedImageUri;
    private GeneralChatViewModel generalChatViewModel;
    private StorageReference storageRef, groupImageRef;
    private FirebaseStorage storage;
    private UploadTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_chat);
        // retrieve an instance of your database
        database = FirebaseDatabase.getInstance();
        // location reference in database
        groupRef = database.getReference("Groups");
        userRef = database.getReference("Users");
        chatRef = database.getReference();
        // instance of firebase storage
        storage = FirebaseStorage.getInstance();
        // get current user details
        user = FirebaseAuth.getInstance().getCurrentUser();
        // Create a storage reference from our app
        storageRef = storage.getReference();
        // instantiate view model
        generalChatViewModel = new ViewModelProvider(this).get(GeneralChatViewModel.class);

        // instantiate views
        toolbar = findViewById(R.id.toolbar_general_chat);
        linearLayoutUser = findViewById(R.id.linearLayout_user_generalChat);
        linearLayoutAdmin = findViewById(R.id.linearLayout_adminOnly_generalChat);
        textViewMessage = findViewById(R.id.textView_message_generalChat);
        item1 = toolbar.getMenu().findItem(R.id.muteGroup);
        editTextUserMessage = findViewById(R.id.edittext_inputMessage_generalChat);
        imageViewSendUserData = findViewById(R.id.imageView_sendMessage_generalChat);
        recyclerViewChatData = findViewById(R.id.recyclerView_generalChat);

        // instance of bundle
        Bundle b = getIntent().getExtras();

        // variables holds value for is admin only
        //isAdminOnly = b.getBoolean("adminOnly");
        isUserBan = b.getBoolean("isUserBan");
        isUserAdmin = b.getBoolean("isUserAdmin");
        groupName = b.getString("groupName");
        usersCount = b.getInt("usersCount");
        groupKey = b.getString("groupKey");


        // close activity on click
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        // on click opens all users list.
        toolbar.setOnClickListener(v -> {
            // check if user is admin
            if (isUserAdmin){
                // navigate to user list.
                Intent intent = new Intent(GeneralChatActivity.this, GroupMemberActivity.class);
                startActivity(intent);
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
                                deleteGroup(groupImage);
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


        chatRecyclerView(GeneralChatActivity.this);

        imageViewSendUserData.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editTextUserMessage.getText())){

            }else {
                if (user != null){
                    String uid, message, key;

                    uid = user.getUid();
                    message = editTextUserMessage.getText().toString();
                    key = chatRef.push().getKey();

                    Map<String, Object> map = new HashMap<>();
                    map.put(getStringResource(R.string.username), username);
                    map.put(getStringResource(R.string.message), message);
                    map.put(getStringResource(R.string.time), getDate());
                    map.put(getStringResource(R.string.userId), uid);
                    chatRef.child(groupKey).child(key).setValue(map);
                    map.clear();
                    // update last message sender.
                    groupRef.child(groupKey).child(getStringResource(R.string.sender)).setValue(username);
                    groupRef.child(groupKey).child(getStringResource(R.string.lastMessage)).setValue(message);
                    // clear view.
                    editTextUserMessage.setText(null);

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerViewAdapterChat.stopListening();
    }

    private void chatRecyclerView(Context context){
        // To display the Recycler view linearly
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        recyclerViewChatData.setLayoutManager(layoutManager);

        // firebase location path
        queryChatMessages = FirebaseDatabase.getInstance().getReference(groupKey);
        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<ChatModel> options = new FirebaseRecyclerOptions.Builder<ChatModel>()
                .setQuery(queryChatMessages, ChatModel.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        recyclerViewAdapterChat = new RecyclerViewAdapterChat(options, GeneralChatActivity.this);
        // Connecting Adapter class with the Recycler view
        // Function to tell the app to start getting data from database
        recyclerViewChatData.setAdapter(recyclerViewAdapterChat);
        recyclerViewAdapterChat.startListening();
    }

    private void init(){
        groupRef.child(groupKey).child(getStringResource(R.string.adminOnly)).addValueEventListener(new ValueEventListener() {
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
        groupRef.child(groupKey).child(getStringResource(R.string.groupImage)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    groupImage = snapshot.getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userRef.child(user.getUid()).child(getStringResource(R.string.username)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    username = snapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // compare the requestCode with the
        // PICK_IMAGE constant
        if (requestCode == PICK_IMAGE) {
            if (data != null){
                // stores true if image is selected
                isImageSelected = true;
                // Get the url of the image from data
                // set uri to view model
                generalChatViewModel.setMutableLiveDataUri(data.getData());

            }else{
                // stores false if image is not selected
                isImageSelected = false;
            }

        }
    }

    private void deleteGroup(String groupImages){
        // check if group has image before deleting group
        if (groupImages != null){
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReferenceFromUrl(groupImages);
            storageReference.delete().addOnSuccessListener(unused -> {
                // File deleted successfully
                // delete group
                groupRef.child(groupKey).removeValue();
                finish();
            });
        }else {
            // delete group
            groupRef.child(groupKey).removeValue();
            finish();
        }


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
        imageViewGroupImage = customLayout.findViewById(R.id.imageView_groupImage_groupInfo);
        imageViewDeleteGroupImage = customLayout.findViewById(R.id.imageView_delete_groupInfo);
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
        if (!isUserAdmin){
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
                    String groupInfo = snapshot.child(groupKey).child(getStringResource(R.string.groupInfo)).getValue().toString();
                    textViewGroupInfoMessage.setText(groupInfo);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // get group profile pics and update ui
        if ( groupImage != null){
            isPreviousImage = true;
            Glide.with(GeneralChatActivity.this).load(groupImage).into(imageViewGroupImage);
        }
        // on click set visible group info  edit layout
        imageViewGroupInfoEdit.setOnClickListener(v -> {
            textViewGroupInfoMessage.setVisibility(View.GONE);
            linearEditLayoutGroupInfo.setVisibility(View.VISIBLE);
            dialog.setCancelable(false);
            // get info and write to edit auto complete textView
            autoCompleteTextViewInfo.setText(textViewGroupInfoMessage.getText());
        });
        // delete group image.
        imageViewDeleteGroupImage.setOnClickListener(v -> {
            imageViewGroupImage.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
            isImageSelected = false;
            isPreviousImage = false;
        });
        // on click open image chooser
        imageViewGroupImage.setOnClickListener(v -> {
            imageChooser();
        });
        // display selected image in image view
        generalChatViewModel.getUriLivedata().observe(this, uri -> {
            imageViewGroupImage.setImageURI(uri);
            selectedImageUri = uri;
        });

        // proceed button
        buttonProceed.setOnClickListener(v -> {
            if (isEmpty(autoCompleteTextViewInfo)){
                textInputLayoutInfo.setError("group info empty");
            }else {
                String info = autoCompleteTextViewInfo.getText().toString();
                // check if user selects new image to upload
                if (isImageSelected){
                    // check if group has image before deleting group
                    if (groupImage != null){
                        // delete initial group images if any
                        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                        StorageReference storageReference = firebaseStorage.getReferenceFromUrl(groupImage);
                        storageReference.delete().addOnSuccessListener(unused -> {
                            dialog.dismiss();
                            updateGroupInfoWithImage(GeneralChatActivity.this, selectedImageUri, info);
                        });
                    } else {
                        dialog.dismiss();
                        updateGroupInfoWithImage(GeneralChatActivity.this, selectedImageUri, info);
                    }

                }else{
                    // check if there is previous image.
                    if (isPreviousImage) {
                        // update group info
                        groupRef.child(groupKey).child(getStringResource(R.string.groupInfo)).setValue(info);
                        dialog.dismiss();
                        finish();
                    }else {
                        if (groupImage != null){
                            // delete initial group images if any
                            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                            StorageReference storageReference = firebaseStorage.getReferenceFromUrl(groupImage);
                            storageReference.delete().addOnSuccessListener(unused -> {
                                // update group info
                                groupRef.child(groupKey).child(getStringResource(R.string.groupImage)).setValue(null);
                                groupRef.child(groupKey).child(getStringResource(R.string.groupInfo)).setValue(info);
                                Toast.makeText(GeneralChatActivity.this, "updated successful", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                finish();
                            });
                        }else {
                            // update group info
                            groupRef.child(groupKey).child(getStringResource(R.string.groupImage)).setValue(null);
                            groupRef.child(groupKey).child(getStringResource(R.string.groupInfo)).setValue(info);
                            dialog.dismiss();
                            finish();
                        }
                    }
                }

            }
        });
        // cancel button dismiss dialog
        buttonCancel.setOnClickListener(v -> dialog.dismiss());
        isImageSelected = false;
        // show dialog
        dialog.show();

    }

    private void updateGroupInfoWithImage(Context context, Uri uri, String info){
        // instance of progress dialog
        ProgressDialog progressBar = new ProgressDialog(context);
        // Create a reference to group profile pics
        groupImageRef = storageRef.child("groupProfilePics/" + uri.getLastPathSegment());
        uploadTask = groupImageRef.putFile(uri);

        uploadTask.addOnFailureListener(e -> {
            // update user on failure
            showAlertDialog(context, "Error", "Updating of group info failed." );
        }).addOnProgressListener(snapshot -> {
            // update ui with progress bar
            progressBar.setCancelable(false);
            progressBar.setMessage("Updating group.........");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.show();
        }).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return groupImageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // gets image download url
                    Uri downloadUri = task.getResult();
                    // update group info
                    groupRef.child(groupKey).child(getStringResource(R.string.groupImage)).setValue(downloadUri.toString());
                    groupRef.child(groupKey).child(getStringResource(R.string.groupInfo)).setValue(info);
                    // dismiss progress bar
                    progressBar.dismiss();
                    Toast.makeText(context, "updated successful", Toast.LENGTH_SHORT).show();
                    // close activity
                    finish();
                } else {
                    // Handle failures
                    // ...
                }
            });
        });
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

    // this function is triggered when the Select Image Button is clicked
    private void imageChooser() {
        // create an instance of the intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        // pass the constant to compare it with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_IMAGE);
    }

    private void muteOrUnMuteGroup(String title, String message, String positiveButton, boolean muteorunmute){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setNegativeButton("cancel", (dialog, which) -> {

                })
                .setPositiveButton(positiveButton, (dialog, which) -> {
                    groupRef.child(groupKey).child(getStringResource(R.string.adminOnly)).setValue(muteorunmute);

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
    // return date in string.
    public String getDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d-hh:mmaaa");
        String date = simpleDateFormat.format(calendar.getTime());
        return date;
    }
}
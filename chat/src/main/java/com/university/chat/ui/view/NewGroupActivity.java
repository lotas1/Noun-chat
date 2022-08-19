package com.university.chat.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class NewGroupActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imageViewGroupPics;
    private SwitchCompat switchCompat;
    private FirebaseDatabase database;
    private DatabaseReference databaseGroupReference;
    private FirebaseStorage storage;
    private Query queryUserUsername;
    private StorageReference storageRef, groupImageRef;
    private UploadTask uploadTask;
    private FloatingActionButton fab;
    private FirebaseUser user;
    private EditText editTextGroupName;
    private String username = "";
    private static final int PICK_IMAGE = 100;
    private boolean isImageSelected = false;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        // retrieve an instance of your database
        database = FirebaseDatabase.getInstance();
        // location reference in database
        databaseGroupReference = database.getReference("Groups");
        // instance of firebase storage
        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage.getReference();
        // get current user details
        user = FirebaseAuth.getInstance().getCurrentUser();
        //get user username
        groupCreatorUsername();

        // instantiate views
        toolbar = findViewById(R.id.toolbar_newGroup);
        imageViewGroupPics = findViewById(R.id.imageView_new_group);
        fab = findViewById(R.id.floating_action_button_newGroup);
        editTextGroupName = findViewById(R.id.editText_new_group);
        switchCompat = findViewById(R.id.switch_new_group);

        // close view on click.
        toolbar.setNavigationOnClickListener(v -> finish());

        // select image on click
        imageViewGroupPics.setOnClickListener(v -> {
            // method for accessing images in device
            imageChooser();
            // boolean for identifying if image is selected for creating of group or not
            isImageSelected = true;


        });
        // on click on fab
        fab.setOnClickListener(v -> {
            if (editTextGroupName.getText().toString().equals("")){
                editTextGroupName.setError("Enter group name");
            }else {
                // check if image is selected or not
                if (isImageSelected){
                    createGroupWithImage(NewGroupActivity.this, selectedImageUri);
                }else {
                    createGroupWithOutImage();
                }
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
                // Get the url of the image from data
                selectedImageUri = data.getData();
                // send data to view model
                //mViewModel.ImageUri(selectedImageUri);
                imageViewGroupPics.setImageURI(selectedImageUri);
            }else{
                // stores false if image is not selected
                isImageSelected = false;
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

    private void groupCreatorUsername(){
        // firebase location path
        queryUserUsername = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        queryUserUsername.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                     username = Objects.requireNonNull(snapshot.child(getStringResource(R.string.username)).getValue()).toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    // method for user creating a group channel with out a group profile pics
    private void createGroupWithOutImage(){
        // get push key.
        String key = databaseGroupReference.push().getKey();
        // get group name
        String groupName = editTextGroupName.getText().toString();

        // hash map containing data to be sent to firebase.
        // send data to firebase cloud
        Map<String, Object> map = new HashMap<>();
        map.put(getStringResource(R.string.groupName), groupName);
        map.put(getStringResource(R.string.sender), username);
        map.put(getStringResource(R.string.key), key);
        map.put(getStringResource(R.string.time), getDate());
        map.put(getStringResource(R.string.groupImage), null);
        map.put(getStringResource(R.string.lastMessage), "welcome to " + groupName + " group");
        // checks if groups chat will be used by admin only or not.
        if (switchCompat.isChecked()){
            map.put(getStringResource(R.string.adminOnly), true);
        }else {
            map.put(getStringResource(R.string.adminOnly), false);
        }
        assert key != null;
        // write data to firebase
        databaseGroupReference.child(key).setValue(map);
        // clear map
        map.clear();

        // close activity
        finish();
    }

    // method for user creating a group channel with a group profile pics
    private void createGroupWithImage(Context context, Uri uri){
        // instance of progress dialog
        ProgressDialog progressBar = new ProgressDialog(context);
        // Create a reference to group profile pics
        groupImageRef = storageRef.child("groupProfilePics/" + uri.getLastPathSegment());
        uploadTask = groupImageRef.putFile(uri);

        uploadTask.addOnFailureListener(e -> {
            // update user on failure
            showAlertDialog(context, "Error", "Creating of group failed." );
        }).addOnProgressListener(snapshot -> {
            // update ui with progress bar
            progressBar.setCancelable(false);
            progressBar.setMessage("Creating group.........");
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
                    // get push key.
                    String key = databaseGroupReference.push().getKey();
                    // get group name
                    String groupName = editTextGroupName.getText().toString();

                    // hash map containing data to be sent to firebase.
                    // send data to firebase cloud
                    Map<String, Object> map = new HashMap<>();
                    map.put(getStringResource(R.string.groupName), groupName);
                    map.put(getStringResource(R.string.sender), username);
                    map.put(getStringResource(R.string.groupImage), downloadUri.toString());
                    map.put(getStringResource(R.string.key), key);
                    map.put(getStringResource(R.string.time), getDate());
                    map.put(getStringResource(R.string.lastMessage), "welcome to " + groupName + " group");
                    // checks if groups chat will be used by admin only or not.
                    if (switchCompat.isChecked()){
                        map.put(getStringResource(R.string.adminOnly), true);
                    }else {
                        map.put(getStringResource(R.string.adminOnly), false);
                    }
                    assert key != null;
                    // write data to firebase
                    databaseGroupReference.child(key).setValue(map);
                    // clear map
                    map.clear();
                    // dismiss progress bar
                    progressBar.dismiss();
                    // close activity
                    finish();

                } else {
                    // Handle failures
                    // ...
                }
            });
        });
    }

    // return date in string.
    public String getDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/MM/yy");
        String date = simpleDateFormat.format(calendar.getTime());
        return date;
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

    private String getStringResource(int string){
        return getResources().getString(string);
    }
}
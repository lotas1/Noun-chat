package com.university.chat.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.university.chat.R;

import java.util.HashMap;
import java.util.Map;


public class NewGroupActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imageViewGroupPics;
    private static final int PICK_IMAGE = 100;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageRef, groupImageRef;
    private Uri selectedImageUri;
    private UploadTask uploadTask;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        // retrieve an instance of your database
        database = FirebaseDatabase.getInstance();
        // location reference in database
        databaseReference = database.getReference("Groups");
        // instance of firebase storage
        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage.getReference();

        // instantiate views
        toolbar = findViewById(R.id.toolbar_newGroup);
        imageViewGroupPics = findViewById(R.id.imageView_new_group);
        fab = findViewById(R.id.floating_action_button_newGroup);

        // close view on click.
        toolbar.setNavigationOnClickListener(v -> finish());

        // select image on click
        imageViewGroupPics.setOnClickListener(v -> {
            imageChooser();

        });
        // on click on fab
        fab.setOnClickListener(v -> {

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

    private void sendWithoutImage(){}

    private void sendWithImage(Context context, Uri uri){
        // instance of progress dialog
        ProgressDialog progressBar = new ProgressDialog(context);
        // Create a reference to group profile pics
        groupImageRef = storageRef.child("groupProfilePics" + uri.getLastPathSegment());
        uploadTask = groupImageRef.putFile(uri);

        uploadTask.addOnFailureListener(e -> {

        }).addOnProgressListener(snapshot -> {
            // update ui with progress bar
            progressBar.setCancelable(false);
            progressBar.setMessage("Uploading.........");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.show();
        }).addOnSuccessListener(taskSnapshot -> {
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return groupImageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    // get push key.
                    String key = databaseReference.push().getKey();

                    // hash map containing data to be sent to firebase.
                    // send data to firebase cloud
                    Map<String, Object> map = new HashMap<>();
                    //map.put("groupName")

                } else {
                    // Handle failures
                    // ...
                }
            });
        });
    }
}
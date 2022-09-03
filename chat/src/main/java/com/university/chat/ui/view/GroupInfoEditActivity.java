package com.university.chat.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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

public class GroupInfoEditActivity extends AppCompatActivity {
    private ImageView imageViewGroupImage, imageViewDeleteGroupImage;
    private AutoCompleteTextView autoCompleteTextViewInfo, autoCompleteTextViewGroupName;
    private TextInputLayout textInputLayoutInfo, textInputLayoutGroupName;
    private Button buttonProceed;
    private String groupKey, groupImage;
    private Query queryUser;
    private FirebaseDatabase database;
    private DatabaseReference reference, groupRef;
    private Toolbar toolbar;
    private static final int PICK_IMAGE = 100;
    private boolean isGroupImageSelected, isPreviousImage;
    private UploadTask uploadTask;
    private FirebaseStorage storage;
    private StorageReference storageRef, groupImageStorageRef;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info_edit);

        // retrieve an instance of your database
        database = FirebaseDatabase.getInstance();
        // location reference in database
        reference = database.getReference();
        // location reference in database
        groupRef = database.getReference("Groups");
        // instance of firebase storage
        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage.getReference();
        // instance of bundle
        Bundle b = getIntent().getExtras();
        groupKey = b.getString("groupKey");

        // instantiate views
        imageViewGroupImage = findViewById(R.id.imageView_groupImage_groupEdit);
        imageViewDeleteGroupImage = findViewById(R.id.imageView_delete_groupEdit);
        autoCompleteTextViewInfo = findViewById(R.id.autoCompleteTextview_info_groupEdit);
        autoCompleteTextViewGroupName = findViewById(R.id.autoCompleteTextview_groupName_groupEdit);
        textInputLayoutGroupName = findViewById(R.id.textInputLayout_groupName_groupEdit);
        textInputLayoutInfo = findViewById(R.id.textInputLayout_info_groupEdit);
        buttonProceed = findViewById(R.id.button_proceed_groupEdit);
        toolbar = findViewById(R.id.toolbar_groupEdit);

        // on click for close navigation in toolbar
        toolbar.setNavigationOnClickListener(v -> finish());

        // clear error when clicked
        clearError(autoCompleteTextViewInfo, textInputLayoutInfo);
        clearError(autoCompleteTextViewGroupName, textInputLayoutGroupName);

        // read data(group info) from firebase
        queryUser = FirebaseDatabase.getInstance().getReference("Groups");
        // read group existing info from firebase.
        queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String groupName = snapshot.child(groupKey).child(getStringResource(R.string.groupName)).getValue().toString();
                autoCompleteTextViewGroupName.setText(groupName);
                if (snapshot.child(groupKey).child(getStringResource(R.string.groupInfo)).exists()){
                    // get group info or description from database and update user ui.
                    String groupInfo = snapshot.child(groupKey).child(getStringResource(R.string.groupInfo)).getValue().toString();
                    autoCompleteTextViewInfo.setText(groupInfo);
                    // gets group image.
                    if (snapshot.child(groupKey).child(getStringResource(R.string.groupImage)).exists()) {
                        groupImage = snapshot.child(groupKey).child(getStringResource(R.string.groupImage)).getValue().toString();
                        Glide.with(GroupInfoEditActivity.this).load(groupImage).into(imageViewGroupImage);
                        // sets true identifying that there is previous image of the group.
                        isPreviousImage = true;
                    }else {
                        // sets false identifying that there is no previous image of the group.
                        isPreviousImage = false;
                        //groupImage = null;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // delete group image.
        imageViewDeleteGroupImage.setOnClickListener(v -> {
            imageViewGroupImage.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
            isGroupImageSelected = false;
            isPreviousImage = false;
        });
        // on click open image chooser
        imageViewGroupImage.setOnClickListener(v -> {
            imageChooser();
        });
        // on click on proceed button for updating group info
        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(autoCompleteTextViewInfo)){
                    textInputLayoutInfo.setError("group info empty");
                } else if (isEmpty(autoCompleteTextViewGroupName)) {
                    textInputLayoutGroupName.setError("group name empty");
                } else {
                    String groupName = autoCompleteTextViewGroupName.getText().toString();
                    String info = autoCompleteTextViewInfo.getText().toString();
                    // check if user selects new image to upload
                    if (isGroupImageSelected) {
                        // if group has previous image before updating, delete old image before updating else update with new info.
                        if (groupImage != null) {
                            // deletes initial(old) group images.
                            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                            StorageReference storageReference = firebaseStorage.getReferenceFromUrl(groupImage);
                            storageReference.delete().addOnSuccessListener(unused -> {
                                //dialog.dismiss();
                                // on success of deleting previous image, update group info with new image and group info.
                                updateGroupInfoWithImage(GroupInfoEditActivity.this, selectedImageUri, info, groupName);
                            });
                        } else {
                            // update new group image
                            //dialog.dismiss();
                            // update group info with new image and group info.
                            updateGroupInfoWithImage(GroupInfoEditActivity.this, selectedImageUri, info, groupName);
                        }

                    } else {
                        // if there is previous group image update only group info.
                        if (isPreviousImage) {
                            // update group info
                            groupRef.child(groupKey).child(getStringResource(R.string.groupInfo)).setValue(info);
                            groupRef.child(groupKey).child(getStringResource(R.string.groupName)).setValue(groupName);
                            Toast.makeText(GroupInfoEditActivity.this, "Group update successful", Toast.LENGTH_SHORT).show();
                            //dialog.dismiss();
                            finish();
                        } else {
                            // else
                            // if there is existing group image delete before updating group info without image
                            if (groupImage != null) {
                                // delete initial group images if any
                                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                StorageReference storageReference = firebaseStorage.getReferenceFromUrl(groupImage);
                                storageReference.delete().addOnSuccessListener(unused -> {
                                    // update group info
                                    groupRef.child(groupKey).child(getStringResource(R.string.groupImage)).setValue(null);
                                    groupRef.child(groupKey).child(getStringResource(R.string.groupName)).setValue(groupName);
                                    groupRef.child(groupKey).child(getStringResource(R.string.groupInfo)).setValue(info);
                                    Toast.makeText(GroupInfoEditActivity.this, "Group update successful", Toast.LENGTH_SHORT).show();
                                    // dialog.dismiss();
                                    finish();
                                });
                            } else {
                                // update group info without image
                                groupRef.child(groupKey).child(getStringResource(R.string.groupImage)).setValue(null);
                                groupRef.child(groupKey).child(getStringResource(R.string.groupName)).setValue(groupName);
                                groupRef.child(groupKey).child(getStringResource(R.string.groupInfo)).setValue(info);
                                Toast.makeText(GroupInfoEditActivity.this, "Group update successful", Toast.LENGTH_SHORT).show();
                                // dialog.dismiss();
                                finish();
                            }
                        }
                    }

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
                // Get the uri of the image from data
                selectedImageUri = data.getData();
                // stores true if image is selected
                isGroupImageSelected = true;
                // set uri to image view for display.
                imageViewGroupImage.setImageURI(selectedImageUri);

            }else{
                // stores false if image is not selected
                isGroupImageSelected = false;
            }

        }

    }

    private void updateGroupInfoWithImage(Context context, Uri uri, String info, String groupName){
        // instance of progress dialog
        ProgressDialog progressBar = new ProgressDialog(context);
        // Create a reference to group profile pics
        groupImageStorageRef = storageRef.child("groupProfilePics/" + uri.getLastPathSegment());
        uploadTask = groupImageStorageRef.putFile(uri);

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
                return groupImageStorageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // gets image download url
                    Uri downloadUri = task.getResult();
                    // update group info with new selected image and group description.
                    groupRef.child(groupKey).child(getStringResource(R.string.groupImage)).setValue(downloadUri.toString());
                    groupRef.child(groupKey).child(getStringResource(R.string.groupName)).setValue(groupName);
                    groupRef.child(groupKey).child(getStringResource(R.string.groupInfo)).setValue(info);
                    // dismiss progress bar
                    progressBar.dismiss();
                    Toast.makeText(GroupInfoEditActivity.this, "Group update successful", Toast.LENGTH_SHORT).show();
                    // close activity
                    finish();
                } else {
                    // Handle failures
                    // ...
                }
            });
        });
    }

    // clears textInput layout error
    private void clearError(AutoCompleteTextView autoCompleteTextView, TextInputLayout textInputLayout){
        autoCompleteTextView.setOnClickListener(v -> textInputLayout.setError(null));
    }
    // returns string from resources
    private String getStringResource(int string){
        return getResources().getString(string);
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
    // checks if autocomplete text view is empty.
    private boolean isEmpty(AutoCompleteTextView autoCompleteTextView) {
        return autoCompleteTextView.getText().toString().trim().equals("");
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
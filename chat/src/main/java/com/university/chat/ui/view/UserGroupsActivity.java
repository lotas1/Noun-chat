package com.university.chat.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.university.chat.R;
import com.university.chat.utility.RandomColors;
import com.university.chat.utility.RecyclerViewItemDecoration;
import com.university.chat.data.model.UserGroupModel;
import com.university.chat.ui.adapter.UserGroupsRecyclerViewAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserGroupsActivity extends AppCompatActivity {
    private Button  buttonDialogProceed, buttonDialogCancel;
    private GoogleSignInClient mGoogleSignInClient;

    private TextInputLayout textInputLayoutUsername, textInputLayoutDepartment;
    private AutoCompleteTextView autoCompleteTextViewUsername, autoCompleteTextViewDepartment, autoCompleteTextViewUsernameMyProfile, autoCompleteTextViewDepartmentMyProfile;
    private ImageView imageViewEditMyProfile;
    private Toolbar toolbar;
    private RecyclerView recyclerViewUserGroups;
    private TextView textViewTitleProfile;
    private String eUsername, eDepartment;
    private CardView cardViewFAQEntry;
    private FloatingActionButton fab;

    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private Query queryUserGroup, queryUserProfile;
    private UserGroupsRecyclerViewAdapter userGroupsRecyclerViewAdapter;
    private boolean isUserAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_groups);
        // Initialize Google Sign In Client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // retrieve an instance of your database
        database = FirebaseDatabase.getInstance();
        // location reference in database
        myRef = database.getReference("Users");
        // get current user details
        user = FirebaseAuth.getInstance().getCurrentUser();

        // instantiate view
        toolbar = findViewById(R.id.toolbar_userGroup);
        recyclerViewUserGroups = findViewById(R.id.recyclerView_userGroup);
        cardViewFAQEntry = findViewById(R.id.cardView_FAQEntry);
        fab = findViewById(R.id.floating_action_button_userGroup);

        // open faq online activity on click in faq entry view
        cardViewFAQEntry.setOnClickListener(v -> {
            Intent i = new Intent(UserGroupsActivity.this, FaqOnlineActivity.class);
            startActivity(i);
        });

        // opens new group on click on fab
        fab.setOnClickListener(v -> {
            // check if user is admin
            if (isUserAdmin){
                // navigate to view for creating group.
                Intent intent = new Intent(UserGroupsActivity.this, NewGroupActivity.class);
                startActivity(intent);
            }else {
                // notify user not admin
                showAlertDialog(UserGroupsActivity.this, "Admin Feature", "Only admin can access this features.");
            }
        });

        // navigate back on click
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            //finishactivity();
        });

        // updates ui with user profile
        checkUserProfile();

        // toolbar menu navigation
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.profile) {
                // show user profile
                customDialogMyProfile();
            } else if (item.getItemId() == R.id.logOut) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserGroupsActivity.this);
                builder.setTitle("Confirm action")
                        .setMessage("Do you want to sign out?")
                        .setNegativeButton("CANCEL", (dialog, which) -> {

                        })
                        .setPositiveButton("SIGN OUT", (dialog, which) -> {
                            // log user out from email login
                            FirebaseAuth.getInstance().signOut();
                            // sign out user from google login
                            mGoogleSignInClient.signOut();
                            finish();
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            return false;
        });


        // To display the Recycler view linearly
        LinearLayoutManager layoutManager = new LinearLayoutManager(UserGroupsActivity.this,LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        recyclerViewUserGroups.setLayoutManager(layoutManager);

        // default divider line for recycler view.
        recyclerViewUserGroups.addItemDecoration(new RecyclerViewItemDecoration(ContextCompat.getDrawable(UserGroupsActivity.this, R.drawable.divider)));
        // firebase location path
        queryUserGroup = FirebaseDatabase.getInstance().getReference("Groups");
        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<UserGroupModel> options = new FirebaseRecyclerOptions.Builder<UserGroupModel>()
                .setQuery(queryUserGroup, UserGroupModel.class)
                        .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        userGroupsRecyclerViewAdapter = new UserGroupsRecyclerViewAdapter(options, UserGroupsActivity.this);
        // Connecting Adapter class with the Recycler view
        // Function to tell the app to start getting data from database
        recyclerViewUserGroups.setAdapter(userGroupsRecyclerViewAdapter);
        userGroupsRecyclerViewAdapter.startListening();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userGroupsRecyclerViewAdapter.stopListening();
    }



    // delete user account from firebase
    private void deleteUserAccount(){
        user.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                    }
                });
    }

    // custom dialog for user to create profile.
    private void customDialogCreateProfile(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.create_profile_dialog, null);
        builder.setView(customLayout);

        // instantiate views
        textInputLayoutUsername = customLayout.findViewById(R.id.textInputLayout_username_profile);
        textInputLayoutDepartment = customLayout.findViewById(R.id.textInputLayout_department_profile);
        autoCompleteTextViewUsername = customLayout.findViewById(R.id.autoCompleteTextview_username_profile);
        autoCompleteTextViewDepartment = customLayout.findViewById(R.id.autoCompleteTextview_department_profile);
        buttonDialogProceed = customLayout.findViewById(R.id.button_proceed_profile);
        buttonDialogCancel = customLayout.findViewById(R.id.button_cancel_profile);


        // Creating array adapter for both department, semester and level dropdown menu.
        ArrayAdapter<CharSequence> arrayAdapterDepartment = ArrayAdapter.createFromResource(UserGroupsActivity.this, com.university.theme.R.array.department, R.layout.list_item);
        // set adapter
        autoCompleteTextViewDepartment.setAdapter(arrayAdapterDepartment);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        // clear error for textInput layout
        clearError(autoCompleteTextViewUsername, textInputLayoutUsername);
        clearError(autoCompleteTextViewDepartment, textInputLayoutDepartment);
        //on click on cancel button
        buttonDialogCancel.setOnClickListener(v -> {
            // log user out from email login
            FirebaseAuth.getInstance().signOut();
            // sign out user from google login
            mGoogleSignInClient.signOut();
            // delete user account (for in-completion of profile)
            deleteUserAccount();
            finish();
        });
        // on click in proceed button
        buttonDialogProceed.setOnClickListener(v -> {
            // clear error in textInput layout
            textInputLayoutUsername.setError(null);
            textInputLayoutDepartment.setError(null);
            // validate user input.
            if (isEmpty(autoCompleteTextViewUsername)){
                textInputLayoutUsername.setError("Enter username");
            } else if (isEmpty(autoCompleteTextViewDepartment)) {
                textInputLayoutDepartment.setError("Select your department");
            }else {


                if (user != null) {
                    String uid, username, department;
                    RandomColors randomColors = new RandomColors();
                    int color = randomColors.getColor();

                    //get user details
                    username = autoCompleteTextViewUsername.getText().toString();
                    department = autoCompleteTextViewDepartment.getText().toString();

                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                    // authenticate with your backend server, if you have one. Use
                    // FirebaseUser.getIdToken() instead.
                    uid = user.getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put(getStringResource(R.string.username), username);
                    map.put(getStringResource(R.string.userId), uid);
                    map.put(getStringResource(R.string.department), department);
                    map.put(getStringResource(R.string.userBan), false);
                    map.put(getStringResource(R.string.userAdmin), false);
                    map.put(getStringResource(R.string.usernameColor), color);

                    // write to firebase
                    myRef.child(uid).setValue(map);
                    map.clear();

                    // close dialog
                    dialog.dismiss();
                }
            }
        });
    }
    // custom dialog for user view profile
    private void customDialogMyProfile(){
        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.user_profile, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();

        // instantiate views
        autoCompleteTextViewUsernameMyProfile = customLayout.findViewById(R.id.autoCompleteTextview_username_myProfile);
        autoCompleteTextViewDepartmentMyProfile = customLayout.findViewById(R.id.autoCompleteTextview_department_myProfile);
        imageViewEditMyProfile = customLayout.findViewById(R.id.imageView_edit_myProfile);

        myRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    autoCompleteTextViewUsernameMyProfile.setText(snapshot.child(getStringResource(R.string.username)).getValue().toString());
                    autoCompleteTextViewDepartmentMyProfile.setText(snapshot.child(getStringResource(R.string.department)).getValue().toString());
                    eUsername = snapshot.child(getStringResource(R.string.username)).getValue().toString();
                    eDepartment = snapshot.child(getStringResource(R.string.department)).getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageViewEditMyProfile.setOnClickListener(v -> {
            dialog.dismiss();
            // display edit profile dialog
            customDialogEditProfile();
        });

        dialog.show();


    }
    // custom dialog for user to edit profile
    private void customDialogEditProfile(){
        // dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.create_profile_dialog, null);
        builder.setView(customLayout);
        // instantiate views
        textInputLayoutUsername = customLayout.findViewById(R.id.textInputLayout_username_profile);
        textInputLayoutDepartment = customLayout.findViewById(R.id.textInputLayout_department_profile);
        autoCompleteTextViewUsername = customLayout.findViewById(R.id.autoCompleteTextview_username_profile);
        autoCompleteTextViewDepartment = customLayout.findViewById(R.id.autoCompleteTextview_department_profile);
        buttonDialogProceed = customLayout.findViewById(R.id.button_proceed_profile);
        buttonDialogCancel = customLayout.findViewById(R.id.button_cancel_profile);
        textViewTitleProfile = customLayout.findViewById(R.id.textView_title_profile);
        buttonDialogProceed.setText("Update");
        textViewTitleProfile.setText("Edit Your Profile");
        autoCompleteTextViewUsername.setText(eUsername);
        autoCompleteTextViewDepartment.setText(eDepartment);

        // Creating array adapter for both department, semester and level dropdown menu.
        ArrayAdapter<CharSequence> arrayAdapterDepartment = ArrayAdapter.createFromResource(UserGroupsActivity.this, com.university.theme.R.array.department, R.layout.list_item);
        // set adapter
        autoCompleteTextViewDepartment.setAdapter(arrayAdapterDepartment);

        AlertDialog dialog = builder.create();
        dialog.show();

        // clear error for textInput layout
        clearError(autoCompleteTextViewUsername, textInputLayoutUsername);
        clearError(autoCompleteTextViewDepartment, textInputLayoutDepartment);
        //on click on cancel button
        buttonDialogCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        // on click in proceed button
        buttonDialogProceed.setOnClickListener(v -> {
            // clear error in textInput layout
            textInputLayoutUsername.setError(null);
            textInputLayoutDepartment.setError(null);
            // validate user input.
            if (isEmpty(autoCompleteTextViewUsername)){
                textInputLayoutUsername.setError("Enter username");
            } else if (isEmpty(autoCompleteTextViewDepartment)) {
                textInputLayoutDepartment.setError("Select your department");
            }else {
                if (user != null) {
                    String username, department;
                    //get user details
                    username = autoCompleteTextViewUsername.getText().toString();
                    department = autoCompleteTextViewDepartment.getText().toString();
                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                    // authenticate with your backend server, if you have one. Use
                    // FirebaseUser.getIdToken() instead.
                    // write to firebase
                    myRef.child(user.getUid()).child(getStringResource(R.string.username)).setValue(username);
                    myRef.child(user.getUid()).child(getStringResource(R.string.department)).setValue(department);

                    // close dialog
                    dialog.dismiss();
                    // notify user
                    showSnackBar("Profile update successful.");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finishactivity();
    }

    private void finishactivity(){
        Intent intent = null;
        try {
            intent = new Intent(UserGroupsActivity.this, Class.forName("com.lotas.nounchat.MainActivity")); // com.university.noungpa.ui.view.MainActivity
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        startActivity(intent);
        finish();
    }
    // check if user has a profile and read profile.
    private void checkUserProfile(){
        // firebase location path
        queryUserProfile = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        queryUserProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // checks if user has a profile.
                if (!snapshot.exists()){
                    // user doesn't exist, create profile.
                    // show profile custom dialog
                    customDialogCreateProfile();
                }
                // retrieve current user username and update ui
                if (snapshot.exists()){
                    String username = Objects.requireNonNull(snapshot.child(getStringResource(R.string.username)).getValue()).toString();
                    toolbar.setSubtitle("@" + username);
                }
                // checks if user is an admin and update ui
                if (snapshot.child(getStringResource(R.string.userAdmin)).exists()){
                    isUserAdmin = (boolean) Objects.requireNonNull(snapshot.child(getStringResource(R.string.userAdmin)).getValue());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    // show snack bar
    private void showSnackBar(String label){
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout_userGroups),label,Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(getResources().getColor(com.university.theme.R.color.primaryDarkColor));
        snackbar.show();
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
    // checks if autocomplete text view is empty.
    private boolean isEmpty(AutoCompleteTextView autoCompleteTextView) {
        return TextUtils.isEmpty(autoCompleteTextView.getEditableText());
    }
    // clears textInput layout error
    private void clearError(AutoCompleteTextView autoCompleteTextView, TextInputLayout textInputLayout){
        autoCompleteTextView.setOnClickListener(v -> textInputLayout.setError(null));
    }

    private String getStringResource(int string){
        return getResources().getString(string);
    }
}
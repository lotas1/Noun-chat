package com.university.chat.ui.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.university.chat.R;

import java.util.HashMap;
import java.util.Map;

public class UserGroupsActivity extends AppCompatActivity {
    private Button buttonLogOut, buttonDialogProceed;
    private GoogleSignInClient mGoogleSignInClient;

    private TextInputLayout textInputLayoutUsername, textInputLayoutDepartment;
    private AutoCompleteTextView autoCompleteTextViewUsername, autoCompleteTextViewDepartment;
    private View customDialogView;
    private LinearLayout linearLayoutParentViewProfile;

    private DatabaseReference myRef;
    private FirebaseDatabase database;

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

        // instantiate view
        buttonLogOut = findViewById(R.id.button_log_out);

        buttonLogOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            mGoogleSignInClient.signOut();
            finish();
        });

        customDialogProfile();
    }

    // method for displaying custom dialog.
    private void customDialogProfile(){
        customDialogView = LayoutInflater.from(UserGroupsActivity.this).inflate(R.layout.create_profile_dialog, null);

        // instantiate views
        textInputLayoutUsername = customDialogView.findViewById(R.id.textInputLayout_username_profile);
        textInputLayoutDepartment = customDialogView.findViewById(R.id.textInputLayout_department_profile);
        autoCompleteTextViewUsername = customDialogView.findViewById(R.id.autoCompleteTextview_username_profile);
        autoCompleteTextViewDepartment = customDialogView.findViewById(R.id.autoCompleteTextview_department_profile);
        linearLayoutParentViewProfile = customDialogView.findViewById(R.id.linearLayout_parentView_profile);
        buttonDialogProceed = customDialogView.findViewById(R.id.button_proceed_profile);
        // set width of custom dialog
        linearLayoutParentViewProfile.setMinimumWidth(getScreenWidth());

        // Creating array adapter for both department, semester and level dropdown menu.
        ArrayAdapter<CharSequence> arrayAdapterDepartment = ArrayAdapter.createFromResource(UserGroupsActivity.this, com.university.theme.R.array.department, R.layout.list_item);
        // set adapter
        autoCompleteTextViewDepartment.setAdapter(arrayAdapterDepartment);

        // show custom dialog
        Dialog dialog = new Dialog(UserGroupsActivity.this);
        dialog.setContentView(customDialogView);
        //dialog.setCancelable(false);
        dialog.show();

        // clear error for textInput layout
        clearError(autoCompleteTextViewUsername, textInputLayoutUsername);
        clearError(autoCompleteTextViewDepartment, textInputLayoutDepartment);


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
                // location reference in database
                myRef = database.getReference("Users");

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String uid, username, department;
                    //get user details
                    username = autoCompleteTextViewUsername.getText().toString();
                    department = autoCompleteTextViewDepartment.getText().toString();

                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                    // authenticate with your backend server, if you have one. Use
                    // FirebaseUser.getIdToken() instead.
                    uid = user.getUid();
                    Map<String, String> map = new HashMap<>();
                    map.put("username", username);
                    map.put("userId", uid);
                    map.put("department", department);

                    // write to firebase
                    myRef.child(uid).setValue(map);
                    map.clear();

                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent intent = null;
        //try {
        //    intent = new Intent(UserGroupsActivity.this, Class.forName("com.lotas.nounchat.MainActivity")); // com.university.noungpa.ui.view.MainActivity
        //} catch (ClassNotFoundException e) {
        //    e.printStackTrace();
        //}
        //startActivity(intent);
        //finish();
    }

    // method for getting screen width
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    // checks if autocomplete text view is empty.
    private boolean isEmpty(AutoCompleteTextView autoCompleteTextView) {
        return TextUtils.isEmpty(autoCompleteTextView.getEditableText());
    }
    // clears textInput layout error
    private void clearError(AutoCompleteTextView autoCompleteTextView, TextInputLayout textInputLayout){
        autoCompleteTextView.setOnClickListener(v -> textInputLayout.setError(null));
    }
}
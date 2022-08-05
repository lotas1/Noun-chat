package com.university.chat.ui.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
import com.university.chat.R;

public class UserGroupsActivity extends AppCompatActivity {
    private Button buttonLogOut;
    private GoogleSignInClient mGoogleSignInClient;

    private TextInputLayout textInputLayoutUsername, textInputLayoutDepartment;
    private AutoCompleteTextView autoCompleteTextViewUsername, autoCompleteTextViewDepartment;
    private View customDialogView;
    private LinearLayout linearLayoutParentViewProfile;
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
        // set width of custom dialog
        linearLayoutParentViewProfile.setMinimumWidth(getScreenWidth());

        // Creating array adapter for both department, semester and level dropdown menu.
        ArrayAdapter<CharSequence> arrayAdapterDepartment = ArrayAdapter.createFromResource(UserGroupsActivity.this, com.university.theme.R.array.department, R.layout.list_item);
        // set adapter
        autoCompleteTextViewDepartment.setAdapter(arrayAdapterDepartment);

        // show custom dialog
        Dialog dialog = new Dialog(UserGroupsActivity.this);
        dialog.setContentView(customDialogView);
        dialog.show();
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
}
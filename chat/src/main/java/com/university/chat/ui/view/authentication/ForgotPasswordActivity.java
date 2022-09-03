package com.university.chat.ui.view.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ScrollView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.university.chat.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private AutoCompleteTextView autoCompleteTextViewEmailResetPassword;
    private TextInputLayout textInputLayoutEmailResetPassword;
    private Button buttonResetPassword;
    private String emailAddress;
    private CoordinatorLayout coordinatorLayoutForgotPasswordLayout;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // instantiate views
        toolbar = findViewById(R.id.toolbar_forgot_password);
        autoCompleteTextViewEmailResetPassword = findViewById(R.id.autoCompleteTextview_email_forgotPassword);
        textInputLayoutEmailResetPassword = findViewById(R.id.textInputLayout_email_forgotPassword);
        buttonResetPassword = findViewById(R.id.button_reset_password);
        coordinatorLayoutForgotPasswordLayout = findViewById(R.id.coordinatorLayout_forgotPassword);
        // non click navigates back
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        // clear error
        clearError(autoCompleteTextViewEmailResetPassword, textInputLayoutEmailResetPassword);

        // on click for password reset button
        buttonResetPassword.setOnClickListener(v -> {
            // clear error
            textInputLayoutEmailResetPassword.setError(null);

            // validate user input
            if (isEmpty(autoCompleteTextViewEmailResetPassword)){
                textInputLayoutEmailResetPassword.setError("Enter email");
            }else {
                if (!isValidEmail(autoCompleteTextViewEmailResetPassword.getText().toString())){
                    textInputLayoutEmailResetPassword.setError("Enter valid email");
                }else {
                    // show progressing dialog
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Sending.......");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    // stores user email
                    emailAddress = autoCompleteTextViewEmailResetPassword.getText().toString();
                    // send password reset email.
                    resetPassword(emailAddress);
                }
            }

        });
    }

    // method for sending reset password email
    private void resetPassword(String emailAddress){
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "Email sent.");
                            // On send password reset email successful update ui
                            progressDialog.dismiss();
                            showDialog(ForgotPasswordActivity.this, "Sent a password reset link to your email(Check Spam Folder)");
                        }else {
                            // if send fails, display a message to the user.
                            progressDialog.dismiss();
                            showDialog(ForgotPasswordActivity.this, "Account doesn't exist in our server.");
                        }
                    }
                });
    }
    // show dialog
    private void showDialog(Context context, String title){
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setPositiveButton("OK", (Dialog, which) -> {
                    Dialog.dismiss();
                });
        materialAlertDialogBuilder.show();
    }
    //show snack bar
    private void showSnackBar(ScrollView scrollViewLayout, String label){
        Snackbar snackbar = Snackbar.make(scrollViewLayout, label, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    // checks if autocomplete text view is empty.
    private boolean isEmpty(AutoCompleteTextView autoCompleteTextView) {
        return autoCompleteTextView.getText().toString().trim().equals("");
    }
    // validate if it is email
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    // clears textInput layout error
    private void clearError(AutoCompleteTextView autoCompleteTextView, TextInputLayout textInputLayout){
        autoCompleteTextView.setOnClickListener(v -> textInputLayout.setError(null));
    }
}
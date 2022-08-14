package com.university.chat.ui.view.authentication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.university.chat.R;
import com.university.chat.ui.view.UserGroupsActivity;

public class SignUpActivity extends AppCompatActivity {
    private AutoCompleteTextView autoCompleteTextViewEmail, autoCompleteTextViewPassword, autoCompleteTextViewPassword2;
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword, textInputLayoutPassword2;
    private Button buttonSignUp;
    private SignInButton signInButton;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private String username, email, password;

    private GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Google Sign In Client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // instantiate views
        //textViewLogin = findViewById(R.id.textView_login);
        autoCompleteTextViewEmail = findViewById(R.id.autoCompleteTextview_email_signUp);
        autoCompleteTextViewPassword = findViewById(R.id.autoCompleteTextview_password_signUp);
        autoCompleteTextViewPassword2 = findViewById(R.id.autoCompleteTextview_password2_signUp);

        textInputLayoutEmail = findViewById(R.id.textInputLayout_email_signup);
        textInputLayoutPassword = findViewById(R.id.textInputLayout_password_signup);
        textInputLayoutPassword2 = findViewById(R.id.textInputLayout_password2_signup);

        buttonSignUp = findViewById(R.id.button_signUp);
        signInButton = findViewById(R.id.sign_in_button);


        // clear error message in text input layout
        clearError(autoCompleteTextViewEmail, textInputLayoutEmail);
        clearError(autoCompleteTextViewPassword, textInputLayoutPassword);
        clearError(autoCompleteTextViewPassword2, textInputLayoutPassword2);

        // email button sign up
        buttonSignUp.setOnClickListener(v -> {
            // clear error message in text input layout
            textInputLayoutEmail.setError(null);
            textInputLayoutPassword.setError(null);
            textInputLayoutPassword2.setError(null);
            // validate user input
            if (isEmpty(autoCompleteTextViewEmail)){
                textInputLayoutEmail.setError("Enter email");
            } else if (isEmpty(autoCompleteTextViewPassword)) {
                textInputLayoutPassword.setError("Password must be at least 8 characters");
            } else {
                // validate email
                if (!isValidEmail(autoCompleteTextViewEmail.getText().toString())){
                    textInputLayoutEmail.setError("Enter valid email");
                }else if (autoCompleteTextViewPassword.getText().toString().length() < 8){
                    textInputLayoutPassword.setError("Password must be at least 8 characters");
                } else if (!autoCompleteTextViewPassword.getText().toString().equals(autoCompleteTextViewPassword2.getText().toString())) {
                    textInputLayoutPassword2.setError("Password does not match");
                }else {
                    // assign value to email and password variable
                    email = autoCompleteTextViewEmail.getText().toString().trim();
                    password = autoCompleteTextViewPassword.getText().toString().trim();

                    progressDialog = new ProgressDialog(SignUpActivity.this);
                    progressDialog.setMessage("Signing In.......");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    // signing up new users
                    createAccount(email, password);
                }
            }
        });

        // google button sign up
        signInButton.setOnClickListener(v -> {
            signIn();
        });



        /**textViewLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });**/
    }
    // SignIn method which we can call with the click of the SignIn button
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                //Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    //Authenticate GoogleSignInAccount with firebase to get Firebase user
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, authResult -> {
                    startActivity(new Intent(SignUpActivity.this, UserGroupsActivity.class));
                    finish();
                })
                .addOnFailureListener(this, e -> Toast.makeText(SignUpActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //reload();
            Intent intent = new Intent(SignUpActivity.this, UserGroupsActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // checks if autocomplete text view is empty.
    private boolean isEmpty(AutoCompleteTextView autoCompleteTextView) {
        return TextUtils.isEmpty(autoCompleteTextView.getEditableText());
    }
    // clears textInput layout error
    private void clearError(AutoCompleteTextView autoCompleteTextView, TextInputLayout textInputLayout){
        autoCompleteTextView.setOnClickListener(v -> textInputLayout.setError(null));
    }
    // validate if it is email
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    // clears autocomplete text view.
    private void clearTextView(AutoCompleteTextView autoCompleteTextView){
        autoCompleteTextView.setText(null);
    }
    // show snack bar
    private void showSnackBar(String label){
        Snackbar snackbar = Snackbar.make(findViewById(R.id.scrollView_signUp),label,Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(getResources().getColor(com.university.theme.R.color.secondaryDarkColor));
        snackbar.show();
    }
    // method for creating account
    private void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();

                        progressDialog.dismiss();

                        Intent intent = new Intent(SignUpActivity.this, UserGroupsActivity.class);
                        startActivity(intent);
                        finish();

                        clearTextView(autoCompleteTextViewEmail);
                        clearTextView(autoCompleteTextViewPassword);
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        progressDialog.dismiss();
                        showSnackBar("There is a problem creating your account. Check your email is spelled correctly or previously used to create an account already");
                        //updateUI(null);
                    }
                });
    }
}
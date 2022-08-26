package com.university.chat.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.university.chat.data.model.FaqOnlineModel;
import com.university.chat.ui.adapter.FaqOnlineRecyclerViewAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FaqOnlineActivity extends AppCompatActivity {
    private RecyclerView recyclerViewFaqOnline;
    private EditText editTextTitle, editTextBody;
    private ImageButton imageButtonSendFAQ;
    private LinearLayout linearLayoutParentMessageFAQ;
    private Toolbar toolbar;
    private Query queryFaq, queryUser;
    private FaqOnlineRecyclerViewAdapter faqOnlineRecyclerViewAdapter;
    private FirebaseUser user;
    private boolean isUserAdmin = false;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String faqInfoPushKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_online);

        // firebase location path
        queryFaq = FirebaseDatabase.getInstance().getReference("FaqOnline");
        // get current user details
        user = FirebaseAuth.getInstance().getCurrentUser();
        // retrieve an instance of your database
        database = FirebaseDatabase.getInstance();
        // location reference in database
        myRef = database.getReference("FaqOnline");

        // check if user is admin and update is user admin variable
        checkIfUserIsAdmin();

        // instantiate views
        recyclerViewFaqOnline = findViewById(R.id.recyclerView_FaqOnline);
        editTextTitle = findViewById(R.id.edittext_title_FaqOnline);
        editTextBody = findViewById(R.id.edittext_body_FaqOnline);
        imageButtonSendFAQ = findViewById(R.id.imageButton_sendMessage_FaqOnline);
        linearLayoutParentMessageFAQ = findViewById(R.id.linearLayout_parentMessage_FaqOnline);
        toolbar = findViewById(R.id.toolbar_FaqOnline);

        // finish activity on click on navigation toolbar
        toolbar.setNavigationOnClickListener(v -> finish());

        // recycler view with adapter
        updateRecyclerView(this, recyclerViewFaqOnline, queryFaq);



        // send info to faq
        imageButtonSendFAQ.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editTextTitle.getText())){
                Toast.makeText(FaqOnlineActivity.this, "Enter Faq Title", Toast.LENGTH_SHORT).show();
            }else {
                if (TextUtils.isEmpty(editTextBody.getText())){
                    Toast.makeText(FaqOnlineActivity.this, "Enter Faq Body", Toast.LENGTH_SHORT).show();
                }else{
                    String title = editTextTitle.getText().toString();
                    String body = editTextBody.getText().toString();
                    // send faq info to database
                    sendFaqInfo(title, body);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        faqOnlineRecyclerViewAdapter.startListening();
    }

    private void sendFaqInfo(String title, String body){

        // get push key.
        String key = myRef.push().getKey();

        Map<String, String> map = new HashMap<>();
        map.put(getStringResource(R.string.title), title);
        map.put(getStringResource(R.string.body), body);
        map.put(getStringResource(R.string.key), key);

        assert key != null;
        myRef.child(key).setValue(map).addOnSuccessListener(unused -> {
            editTextBody.setText(null);
            editTextTitle.setText(null);
        });

    }

    private void deleteFaqInfo(String pushKey) {
        // create instance of firebase database & database reference.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // reference for News/NounUpdate path.
        DatabaseReference myRef = database.getReference("FaqOnline");
        // delete message
        myRef.child(pushKey).removeValue();
    }

    private void updateRecyclerView(Context context, RecyclerView recyclerView, Query query){
        // To display the Recycler view linearly
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(layoutManager);

        // default divider line for recycler view.
        recyclerViewFaqOnline.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<FaqOnlineModel> options = new FirebaseRecyclerOptions.Builder<FaqOnlineModel>()
                .setQuery(query, FaqOnlineModel.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        faqOnlineRecyclerViewAdapter = new FaqOnlineRecyclerViewAdapter(options, context){
            @Override
            protected void onBindViewHolder(@NonNull FaqOnlineRecyclerViewAdapter.FaqOnlineViewHolder holder, int position, @NonNull FaqOnlineModel model) {
                super.onBindViewHolder(holder, position, model);

                holder.itemView.setOnLongClickListener(v -> {
                    // only admin can delete
                    if (isUserAdmin){
                        AlertDialog.Builder builder = new AlertDialog.Builder(FaqOnlineActivity.this);
                        builder.setTitle("Delete")
                                .setMessage("Do you want to delete faq info?")
                                .setNegativeButton("cancel", (dialog, which) -> {

                                })
                                .setPositiveButton("Delete", (dialog, which) -> {
                                    deleteFaqInfo(model.getKey());
                                    dialog.dismiss();
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    return false;
                });
            }
        };
        // Connecting Adapter class with the Recycler view
        // Function to tell the app to start getting data from database
        recyclerView.setAdapter(faqOnlineRecyclerViewAdapter);
        faqOnlineRecyclerViewAdapter.startListening();
    }

    // check if user has a profile and read profile.
    private void checkIfUserIsAdmin(){
        // firebase location path
        queryUser = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        queryUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // checks if user is an admin and update ui
                if (snapshot.child(getStringResource(R.string.userAdmin)).exists()){
                    isUserAdmin = (boolean) Objects.requireNonNull(snapshot.child(getStringResource(R.string.userAdmin)).getValue());
                    // if user is not admin, user cant send faq
                    if (isUserAdmin){
                        linearLayoutParentMessageFAQ.setVisibility(View.VISIBLE);
                    }else {
                        linearLayoutParentMessageFAQ.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getStringResource(int string){
        return getResources().getString(string);
    }
}
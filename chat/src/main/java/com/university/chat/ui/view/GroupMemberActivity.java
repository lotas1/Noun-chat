package com.university.chat.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.university.chat.R;
import com.university.chat.data.model.UserListModel;
import com.university.chat.ui.adapter.RecyclerViewAdapterGroupMember;
import com.university.theme.ItemClickSupport;

import java.util.ArrayList;

public class GroupMemberActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapterGroupMember recyclerViewAdapterGroupMember;
    private Query queryUser, queryBan;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceBan, databaseReferenceUserProfile;
    private ArrayList<String> usersArrayList;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);



        // instantiate view
        recyclerView = findViewById(R.id.recyclerView_group_members);
        toolbar = findViewById(R.id.toolbar_members);
        // close activity on click
        toolbar.setNavigationOnClickListener(v -> finish());

        //retrieve an instance of your database
        firebaseDatabase = FirebaseDatabase.getInstance();
        // location reference in database for banned users
        databaseReferenceBan = firebaseDatabase.getReference("BanUser");
        // location reference in database for all users
        databaseReferenceUserProfile = firebaseDatabase.getReference("Users");

        // To display the Recycler view linearly
        LinearLayoutManager layoutManager = new LinearLayoutManager(GroupMemberActivity.this,LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        // default divider line for recycler view.
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        // firebase location path
        queryBan = FirebaseDatabase.getInstance().getReference("BanUser");
        queryUser = FirebaseDatabase.getInstance().getReference("Users");
        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<UserListModel> options = new FirebaseRecyclerOptions.Builder<UserListModel>()
                .setQuery(queryUser, UserListModel.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        recyclerViewAdapterGroupMember = new RecyclerViewAdapterGroupMember(options, GroupMemberActivity.this);
        // Connecting Adapter class with the Recycler view
        // Function to tell the app to start getting data from database
        recyclerView.setAdapter(recyclerViewAdapterGroupMember);

        recyclerViewAdapterGroupMember.startListening();
        // instance of ArrayList
        usersArrayList = new ArrayList<>();
        // listener for changes in the data location
        queryUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot1: snapshot.getChildren()){
                    // stores data in location to array list.
                    usersArrayList.add(dataSnapshot1.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // on long click in recycler view
        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener((recyclerView, position, v) -> {
            // variable storing user userId.
            String userId = usersArrayList.get(position);
            // listener for changes in the data location
            queryBan.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(userId).exists()){
                        // update UI (un-block user)
                        showAlertDialog(false, userId, "Ban User", "Do you want to unblock user?", "unblock");
                    }else {
                        // update UI (block user)
                        showAlertDialog(true, userId, "Ban User", "Do you want to ban user?", "block");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return true;
        });

    }

    private void showAlertDialog(boolean blockUser, String userID, String title, String message, String positiveButton){
        AlertDialog.Builder builder = new AlertDialog.Builder(GroupMemberActivity.this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, (dialog, which) -> {
                    if (blockUser){
                        // add user to ban path for violation
                        databaseReferenceBan.child(userID).setValue(userID);
                    }else {
                        // remove user from ban path for violation
                        databaseReferenceBan.child(userID).removeValue();
                    }
                    // update user profile ban status
                    databaseReferenceUserProfile.child(userID).child(getStringResource(R.string.userBan)).setValue(blockUser);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerViewAdapterGroupMember.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerViewAdapterGroupMember.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewAdapterGroupMember.startListening();
    }
    private String getStringResource(int string){
        return getResources().getString(string);
    }
}
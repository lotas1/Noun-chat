package com.university.chat.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.university.chat.R;
import com.university.chat.data.model.ChatModel;
import com.university.chat.ui.adapter.RecyclerViewAdapterGroupMember;
import com.university.theme.ItemClickSupport;

import java.util.ArrayList;
import java.util.Objects;

public class GroupMemberActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapterGroupMember recyclerViewAdapterGroupMember;
    private Query queryUser, query;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceBan, databaseReferenceUserProfile;
    private Toolbar toolbar;
    private android.widget.SearchView simpleSearchView;
    private Button buttonAllUsers, buttonBanUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);



        // instantiate view
        recyclerView = findViewById(R.id.recyclerView_group_members);
        toolbar = findViewById(R.id.toolbar_members);
        simpleSearchView = findViewById(R.id.simpleSearchView);
        buttonAllUsers = findViewById(R.id.button_AllUsers_group_members);
        buttonBanUsers = findViewById(R.id.button_BanUsers_group_members);
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

        simpleSearchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.trim().equals("")) {
                    queryUser = FirebaseDatabase.getInstance().getReference("Users");
                }else {
                    queryUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild(getStringResource(R.string.username)).equalTo(query.trim());
                }
                membersRecyclerView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().equals("")) {
                    queryUser = FirebaseDatabase.getInstance().getReference("Users");
                }else {
                    queryUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild(getStringResource(R.string.username)).equalTo(newText.trim());
                }
                membersRecyclerView();
                return false;
            }
        });
        // on click filter query for all users
        buttonAllUsers.setOnClickListener(v -> {
            // firebase location path
            queryUser = FirebaseDatabase.getInstance().getReference("Users");
            membersRecyclerView();
        });
        // on click filter query for ban users
        buttonBanUsers.setOnClickListener(v -> {
            // firebase location path
            queryUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild(getStringResource(R.string.userBan)).equalTo(true);
            membersRecyclerView();
        });

        // firebase location path
        queryUser = FirebaseDatabase.getInstance().getReference("Users");
        membersRecyclerView();



    }

   private void membersRecyclerView(){
       // It is a class provide by the FirebaseUI to make a
       // query in the database to fetch appropriate data
       FirebaseRecyclerOptions<ChatModel> options = new FirebaseRecyclerOptions.Builder<ChatModel>()
               .setQuery(queryUser, ChatModel.class)
               .build();
       // Connecting object of required Adapter class to
       // the Adapter class itself
       recyclerViewAdapterGroupMember = new RecyclerViewAdapterGroupMember(options, GroupMemberActivity.this){
           @Override
           protected void onBindViewHolder(@NonNull RecyclerViewAdapterGroupMember.GroupMemberViewHolder holder, int position, @NonNull ChatModel model) {
               super.onBindViewHolder(holder, position, model);
               // ban or unban user
               holder.itemView.setOnLongClickListener(v -> {
                   query = FirebaseDatabase.getInstance().getReference("Users").child(model.getUserId());
                   query.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if (snapshot.exists()) {
                               boolean isUserBanned = (boolean) Objects.requireNonNull(snapshot.child(getStringResource(R.string.userBan)).getValue());
                               // check if user is blocked or not before updating ui
                               if (isUserBanned) {
                                   // update UI (un-block user)
                                   banUser(false, model.getUserId(), "Ban User", "Do you want to unblock user?", "unblock");
                               }else {
                                   // update UI (block user)
                                   banUser(true, model.getUserId(), "Ban User", "Do you want to ban user?", "block");
                               }
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });

                   return false;
               });
           }
       };
       // Connecting Adapter class with the Recycler view
       // Function to tell the app to start getting data from database
       recyclerView.setAdapter(recyclerViewAdapterGroupMember);
       recyclerViewAdapterGroupMember.startListening();
   }

    private void banUser(boolean blockUser, String userID, String title, String message, String positiveButton){
        AlertDialog.Builder builder = new AlertDialog.Builder(GroupMemberActivity.this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, (dialog, which) -> {
                    // update user profile ban status
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");
                    myRef.child(userID).child(getStringResource(R.string.userBan)).setValue(blockUser);
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

/**
 *
 * private void closeKeyboard(){
 *     View view = this.getCurrentFocus();
 *     if (view != null){
 *         InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
 *         if (inputMethodManager != null) {
 *             inputMethodManager.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
 *         }
 *     }
 * }**/
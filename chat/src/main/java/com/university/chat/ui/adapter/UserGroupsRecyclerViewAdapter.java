package com.university.chat.ui.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.university.chat.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.university.chat.data.model.UserGroupModel;
import com.university.chat.ui.view.GeneralChatActivity;
import com.university.chat.ui.view.ImageFullDisplayActivity;

import java.util.Objects;

public class UserGroupsRecyclerViewAdapter extends FirebaseRecyclerAdapter<UserGroupModel, UserGroupsRecyclerViewAdapter.UserGroupsViewHolder> {

    private Context context;
    private FirebaseUser user;
    private Query queryBan, queryUserProfile;
    private boolean isUserBan, isUserAdmin;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UserGroupsRecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<UserGroupModel> options, Context context) {
        super(options);
        this.context = context;
        // update ui
        checkIfUserIsBan();
        checkUserProfile();
    }

    @Override
    protected void onBindViewHolder(@NonNull UserGroupsViewHolder holder, int position, @NonNull UserGroupModel model) {
        // update profile pics UI
        if (model.getGroupImage() == null){
            holder.imageViewGroupProfilePics.setImageResource(R.drawable.noun_icon);
        }else {
            Glide.with(context).load(model.getGroupImage()).into(holder.imageViewGroupProfilePics);
        }

        if (model.getGroupName() == null){

        }else {
            holder.textViewGroupName.setText(model.getGroupName());
        }

        if (model.getTime() == null){

        }else {
            holder.textViewLastMessageTime.setText(model.getTime());
        }

        if (model.getLastMessage() == null){

        }else {
            if (model.getSender() == null){

            }else {
                String sender = model.getSender();
                String lastMessage = model.getLastMessage();
                holder.textViewLastMessage.setText(sender.concat(": ").concat(lastMessage));
            }
        }
        // on click on group profile pics.
        holder.imageViewGroupProfilePics.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageFullDisplayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("transitionName", "groupPic" + holder.getAbsoluteAdapterPosition());
            intent.putExtras(bundle);
            intent.putExtra("groupName", model.getGroupName());
            intent.putExtra("groupImage", model.getGroupImage());
            // create the transition animation - the images in the layouts
            // of both activities are defined

            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation((Activity) context, holder.imageViewGroupProfilePics, "groupPic" + holder.getAbsoluteAdapterPosition());
            // start the activity
            context.startActivity(intent, options.toBundle());
        });
        //on click on itemView
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, GeneralChatActivity.class);
            //Bundle bundle = new Bundle();
            //bundle.putString("transitionName", "groupPic" + holder.getAbsoluteAdapterPosition());
            //intent.putExtras(bundle);
            intent.putExtra("groupName", model.getGroupName());
            intent.putExtra("groupImage", model.getGroupImage());
            intent.putExtra("key", model.getKey());
            intent.putExtra("adminOnly", model.isAdminOnly());
            intent.putExtra("isUserBan", isUserBan);
            intent.putExtra("isUserAdmin", isUserAdmin);
            // create the transition animation - the images in the layouts
            // of both activities are defined

            //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, holder.imageViewGroupProfilePics, "groupPic" + holder.getAbsoluteAdapterPosition());
            // start the activity
            //context.startActivity(intent, options.toBundle());
            context.startActivity(intent);
        });

        holder.imageViewGroupProfilePics.setTransitionName("groupPic" + position);
    }


    @NonNull
    @Override
    public UserGroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_groups_list_layout, parent, false);
        return new UserGroupsRecyclerViewAdapter.UserGroupsViewHolder(view);
    }

    class UserGroupsViewHolder extends RecyclerView.ViewHolder{
        TextView textViewGroupName, textViewLastMessageTime, textViewLastMessage;
        ImageView imageViewGroupProfilePics;

        public UserGroupsViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewGroupName = itemView.findViewById(R.id.textView_group_name);
            textViewLastMessage = itemView.findViewById(R.id.textView_group_last_message);
            textViewLastMessageTime = itemView.findViewById(R.id.textView_group_last_message_time);
            imageViewGroupProfilePics = itemView.findViewById(R.id.imageView_group_profile_pics);
        }
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
    // method for checking is user is banned or not
    private void checkIfUserIsBan(){
        // get current user details
        user = FirebaseAuth.getInstance().getCurrentUser();
        // firebase location path
        queryBan = FirebaseDatabase.getInstance().getReference("BanUser");
        // listener for changes in the data location
        queryBan.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(user.getUid()).exists()){
                    // update UI (blocked user)
                    isUserBan = true;
                }else {
                    // update UI (not blocked user)
                    isUserBan = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    // check if user has a profile and read profile.
    private void checkUserProfile(){
        // firebase location path
        queryUserProfile = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        queryUserProfile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
    private String getStringResource(int string){
        return context.getResources().getString(string);
    }
}

package com.university.chat.ui.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
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
import com.university.chat.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.university.chat.data.model.UserGroupModel;
import com.university.chat.ui.view.ImageFullDisplayActivity;

public class UserGroupsRecyclerViewAdapter extends FirebaseRecyclerAdapter<UserGroupModel, UserGroupsRecyclerViewAdapter.UserGroupsViewHolder> {

    private Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UserGroupsRecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<UserGroupModel> options, Context context) {
        super(options);
        this.context = context;
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

        if (model.getGroupName() == null){

        }else {
            holder.textViewLastMessageTime.setText(model.getTime());
        }

        if (model.getGroupName() == null){

        }else {
            holder.textViewLastMessage.setText(model.getLastMessage());
        }


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
}

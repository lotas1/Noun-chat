package com.university.allgroupusers.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.university.allgroupusers.R;
import com.university.allgroupusers.data.model.UserListModel;

public class RecyclerViewAdapterGroupMember extends FirebaseRecyclerAdapter<UserListModel, RecyclerViewAdapterGroupMember.GroupMemberViewHolder> {
    private Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecyclerViewAdapterGroupMember(@NonNull FirebaseRecyclerOptions<UserListModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull GroupMemberViewHolder holder, int position, @NonNull UserListModel model) {
        // update UI
        if (model.getUsername() == null) {

        }else {
            holder.textViewUsername.setText(model.getUsername());
        }

        if (model.isUserBan()){
            holder.textViewUserBanStatus.setText("User is ban for violation");
        }else {
            holder.textViewUserBanStatus.setText("User is not Ban");
        }
    }

    @NonNull
    @Override
    public GroupMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.group_users_list_layout, parent, false);
        return new RecyclerViewAdapterGroupMember.GroupMemberViewHolder(view);
    }

    class GroupMemberViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewUsername, textViewUserBanStatus;

        public GroupMemberViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUsername = itemView.findViewById(R.id.textView_username_group_member_list);
            textViewUserBanStatus = itemView.findViewById(R.id.textView_ban_group_member_list);
        }
    }
}

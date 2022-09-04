package com.university.chat.ui.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.university.chat.R;
import com.university.chat.data.model.ChatModel;
import com.university.chat.ui.view.ChatFullImageDisplayActivity;
import com.university.chat.ui.viewModel.GeneralChatViewModel;

import java.util.Objects;

public class RecyclerViewAdapterChat extends FirebaseRecyclerAdapter<ChatModel, RecyclerViewAdapterChat.ChatViewHolderSent> {
    private Context context;
    private FirebaseUser user;
    private GeneralChatViewModel generalChatViewModel;



    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecyclerViewAdapterChat(@NonNull FirebaseRecyclerOptions<ChatModel> options, Context context) {
        super(options);
        this.context = context;
        // get current user details
        user = FirebaseAuth.getInstance().getCurrentUser();
        // instantiate view model
        generalChatViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(GeneralChatViewModel.class);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolderSent holder, int position, @NonNull ChatModel model) {
        ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) holder.linearParentLayout.getLayoutParams();
        if (Objects.equals(model.getUserId(), user.getUid())){
            //cardViewMarginParams.setMargins(100, 0, 0, 0);
            holder.linearParentLayout.setGravity(Gravity.RIGHT);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(com.university.theme.R.color.Tea_Green));
            // sending user cant see there username
            holder.linearLayoutUsernameBadge.setVisibility(View.GONE);
        }else{
            //cardViewMarginParams.setMargins(0, 0, 100, 0);
            holder.linearParentLayout.setGravity(Gravity.LEFT);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(com.university.theme.R.color.white));
            // user can see receiver username or badge.
            holder.linearLayoutUsernameBadge.setVisibility(View.VISIBLE);
        }

        if (model.isUserAdmin()){
            holder.imageViewBadge.setVisibility(View.VISIBLE);
        }else {
            holder.imageViewBadge.setVisibility(View.GONE);
        }

        if (model.getImage() != null){
            holder.cardViewImageFullDisplay.setVisibility(View.VISIBLE);
            Glide.with(context).load(model.getImage()).into(holder.imageViewImageFullDisplay);
        }else{
            holder.cardViewImageFullDisplay.setVisibility(View.GONE);
        }

        if (model.getUsername() != null) {
            holder.textViewUsername.setText("@".concat(model.getUsername()));
            holder.textViewUsername.setTextColor(model.getUsernameColor());
        }



        if (model.getMessage() != null){
            holder.textViewMessage.setText(model.getMessage());
        }

        if (model.getTime() != null) {
            holder.textViewDate.setText(model.getTime());
        }



        // open full image view display
        holder.imageViewImageFullDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getImage() != null) {

                }
                Intent intent = new Intent(context, ChatFullImageDisplayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("transitionName", "ChatImage" + holder.getAbsoluteAdapterPosition());
                intent.putExtras(bundle);
                intent.putExtra("image", model.getImage());
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation((Activity) context, holder.imageViewImageFullDisplay, "ChatImage" + holder.getAbsoluteAdapterPosition());
                // start the new activity
                context.startActivity(intent, options.toBundle());
            }
        });




    }

    @NonNull
    @Override
    public ChatViewHolderSent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_chat_list_item, parent, false);
        return new RecyclerViewAdapterChat.ChatViewHolderSent(view);
    }

    public class ChatViewHolderSent extends RecyclerView.ViewHolder{
        private TextView textViewMessage, textViewDate;
        public TextView textViewUsernameReply, textViewMessageReply, textViewUsername;
        private LinearLayout linearParentLayout, linearLayoutUsernameBadge;
        public CardView cardView, cardViewImageFullDisplay, cardViewReplyInfo;
        private ImageView imageViewImageFullDisplay, imageViewBadge;
        public ImageView imageViewReplyBadge;

        public ChatViewHolderSent(@NonNull View itemView) {
            super(itemView);

            textViewUsername = itemView.findViewById(R.id.textView_usernameChat_sent);
            textViewMessage = itemView.findViewById(R.id.textView_messageChat_sent);
            textViewDate = itemView.findViewById(R.id.textView_date_chat_sent);
            linearParentLayout = itemView.findViewById(R.id.linear_parentLayout_chat_sent);
            cardView = itemView.findViewById(R.id.cardView_chat);
            cardViewImageFullDisplay = itemView.findViewById(R.id.cardView_ImageFullDisplay_Chat);
            imageViewImageFullDisplay = itemView.findViewById(R.id.imageView_ImageFullDisplay_Chat);
            cardViewReplyInfo = itemView.findViewById(R.id.cardView_replyInfo_chat);
            textViewUsernameReply = itemView.findViewById(R.id.textView_usernameReply_chat);
            textViewMessageReply = itemView.findViewById(R.id.textView_messageReply_chat);
            linearLayoutUsernameBadge = itemView.findViewById(R.id.linearLayout_username_badge_chat);
            imageViewBadge = itemView.findViewById(R.id.imageView_badge);
            imageViewReplyBadge = itemView.findViewById(R.id.imageView_ReplyBadge);

            itemView.setClickable(true);
        }
    }
}

/**
 * class ChatViewHolderReceived extends RecyclerView.ViewHolder{
 *         private TextView textViewUsernameReceived, textViewMessageReceived, textViewDateReceived;
 *         private CardView cardViewParentLayoutReceived;
 *
 *         public ChatViewHolderReceived(@NonNull View itemView) {
 *             super(itemView);
 *             textViewUsernameReceived = itemView.findViewById(R.id.textView_usernameChat_received);
 *             textViewMessageReceived = itemView.findViewById(R.id.textView_messageChat_received);
 *             textViewDateReceived = itemView.findViewById(R.id.textView_date_chat_received);
 *             cardViewParentLayoutReceived = itemView.findViewById(R.id.cardView_parentLayout_chat_received);
 *         }
 *     }**/

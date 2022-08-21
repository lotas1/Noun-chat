package com.university.chat.ui.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.university.chat.R;
import com.university.chat.data.model.ChatModel;

import java.util.Objects;

public class RecyclerViewAdapterChat extends FirebaseRecyclerAdapter<ChatModel, RecyclerViewAdapterChat.ChatViewHolderSent> {
    private Context context;
    private FirebaseUser user;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;


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
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolderSent holder, int position, @NonNull ChatModel model) {

        ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) holder.linearParentLayoutSent.getLayoutParams();
        if (Objects.equals(model.getUserId(), user.getUid())){
            cardViewMarginParams.setMargins(100, 0, 0, 0);
            holder.linearParentLayoutSent.setGravity(Gravity.RIGHT);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(com.university.theme.R.color.Beige));
            holder.textViewUsernameSent.setVisibility(View.GONE);
        }else{
            cardViewMarginParams.setMargins(0, 0, 100, 0);
            holder.linearParentLayoutSent.setGravity(Gravity.LEFT);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(com.university.theme.R.color.white));
            holder.textViewUsernameSent.setVisibility(View.VISIBLE);
        }

        if (model.getImage() != null){
            holder.cardViewImageFullDisplay.setVisibility(View.VISIBLE);
            Glide.with(context).load(model.getImage()).into(holder.imageViewImageFullDisplay);
        }else{
            holder.cardViewImageFullDisplay.setVisibility(View.GONE);
        }

        if (model.getUsername() != null) {
            holder.textViewUsernameSent.setText("@".concat(model.getUsername()));
        }

        if (model.getMessage() != null){
            holder.textViewMessageSent.setText(model.getMessage());
        }

        if (model.getTime() != null) {
            holder.textViewDateSent.setText(model.getTime());
        }
    }

    @NonNull
    @Override
    public ChatViewHolderSent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_chat_list_item, parent, false);
        return new RecyclerViewAdapterChat.ChatViewHolderSent(view);
    }

    class ChatViewHolderSent extends RecyclerView.ViewHolder{
        private TextView textViewUsernameSent, textViewMessageSent, textViewDateSent;
        private LinearLayout linearParentLayoutSent;
        private CardView cardView, cardViewImageFullDisplay;
        private ImageView imageViewImageFullDisplay;

        public ChatViewHolderSent(@NonNull View itemView) {
            super(itemView);

            textViewUsernameSent = itemView.findViewById(R.id.textView_usernameChat_sent);
            textViewMessageSent = itemView.findViewById(R.id.textView_messageChat_sent);
            textViewDateSent = itemView.findViewById(R.id.textView_date_chat_sent);
            linearParentLayoutSent = itemView.findViewById(R.id.linear_parentLayout_chat_sent);
            cardView = itemView.findViewById(R.id.cardView_chat);
            cardViewImageFullDisplay = itemView.findViewById(R.id.cardView_ImageFullDisplay_Chat);
            imageViewImageFullDisplay = itemView.findViewById(R.id.imageView_ImageFullDisplay_Chat);
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

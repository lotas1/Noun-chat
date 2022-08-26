package com.university.chat.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
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
        ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) holder.linearParentLayoutSent.getLayoutParams();
        if (Objects.equals(model.getUserId(), user.getUid())){
            //cardViewMarginParams.setMargins(100, 0, 0, 0);
            holder.linearParentLayoutSent.setGravity(Gravity.RIGHT);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(com.university.theme.R.color.Tea_Green));
            holder.textViewUsernameSent.setVisibility(View.GONE);
        }else{
            //cardViewMarginParams.setMargins(0, 0, 100, 0);
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

        if (model.getReplyMessage() != null) {
            holder.cardViewReplyInfo.setVisibility(View.VISIBLE);
            holder.textViewUsernameReply.setText(model.getReplyUsername());
            holder.textViewMessageReply.setText(model.getReplyMessage());
            // on click on user reply it scrolls to reply message position
            holder.cardViewReplyInfo.setOnClickListener(v -> {
                // get reply message position
                generalChatViewModel.setReplyPosition(model.getReplyPosition());
            });
        }else {
            holder.cardViewReplyInfo.setVisibility(View.GONE);
        }





    }

    @NonNull
    @Override
    public ChatViewHolderSent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_chat_list_item, parent, false);
        return new RecyclerViewAdapterChat.ChatViewHolderSent(view);
    }

    public class ChatViewHolderSent extends RecyclerView.ViewHolder{
        private TextView textViewUsernameSent, textViewMessageSent, textViewDateSent, textViewUsernameReply, textViewMessageReply;
        private LinearLayout linearParentLayoutSent;
        public CardView cardView, cardViewImageFullDisplay, cardViewReplyInfo;
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
            cardViewReplyInfo = itemView.findViewById(R.id.cardView_replyInfo_chat);
            textViewUsernameReply = itemView.findViewById(R.id.textView_usernameReply_chat);
            textViewMessageReply = itemView.findViewById(R.id.textView_messageReply_chat);

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

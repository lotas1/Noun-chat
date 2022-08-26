package com.university.chat.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.university.chat.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.university.chat.data.model.FaqOnlineModel;

public class FaqOnlineRecyclerViewAdapter extends FirebaseRecyclerAdapter<FaqOnlineModel, FaqOnlineRecyclerViewAdapter.FaqOnlineViewHolder> {
    private Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FaqOnlineRecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<FaqOnlineModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FaqOnlineViewHolder holder, int position, @NonNull FaqOnlineModel model) {
        if (model.getTitle() != null) {
            holder.textViewFaqTitle.setText(model.getTitle());
        }

        if (model.getBody() != null) {
            holder.textViewFaqBody.setText(model.getBody());
        }
    }

    @NonNull
    @Override
    public FaqOnlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.faq_online_list_item, parent, false);
        return new FaqOnlineRecyclerViewAdapter.FaqOnlineViewHolder(view);
    }

    public class FaqOnlineViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewFaqTitle, textViewFaqBody;

        public FaqOnlineViewHolder(@NonNull View itemView) {
            super(itemView);

            // instantiate views
            textViewFaqTitle = itemView.findViewById(R.id.textView_faq_title);
            textViewFaqBody = itemView.findViewById(R.id.textView_faq_body);
        }
    }
}

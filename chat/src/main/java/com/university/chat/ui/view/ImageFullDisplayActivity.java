package com.university.chat.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.university.chat.R;

public class ImageFullDisplayActivity extends AppCompatActivity {
    private ImageView imageViewFullDisplay;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_display);

        // instantiate views
        imageViewFullDisplay = findViewById(R.id.image_full_display);
        toolbar = findViewById(R.id.toolbar_image_full_display);

        toolbar.setNavigationOnClickListener(v -> finish());

        // instance of bundle
        Bundle b = getIntent().getExtras();
        String transitionName = b.getString("transitionName");
        imageViewFullDisplay.setTransitionName(transitionName);

        if (b.getString("groupImage") == null){
            imageViewFullDisplay.setImageResource(R.drawable.noun_icon);
        }else{
            String image = b.getString("groupImage");
            Glide.with(this).load(image).into(imageViewFullDisplay);
        }
        String groupName = b.getString("groupName");
        toolbar.setTitle(groupName);


    }
}
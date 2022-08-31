package com.university.chat.ui.view;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.university.chat.R;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ChatFullImageDisplayActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button buttonSaveImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_full_image_display);

        // instantiate views
        buttonSaveImage = findViewById(R.id.button_saveImage_ChatImageFullDisplay);
        imageView = findViewById(R.id.imageView_FullDisplay_Chat);

        Bundle b = getIntent().getExtras();
        String image = b.getString("image");
        String transitionName = b.getString("transitionName");
        imageView.setTransitionName(transitionName);
        if (image!= null){
            Glide.with(this).load(image).into(imageView);
        }


        // save image to device on click
        buttonSaveImage.setOnClickListener(view -> {
            // save image to device
            if (imageView.getDrawable() == null){
                Toast.makeText(this, "Image doesn't exist", Toast.LENGTH_SHORT).show();
            }else {
                //convert the ImageView where you have the image to bitmap
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title" , "description");
                // updates user.
                Toast.makeText(ChatFullImageDisplayActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
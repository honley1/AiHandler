package com.honley.aihandler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.honley.aihandler.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final int defaultImageResourceId = R.drawable.add_image;

    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    binding.image.setImageURI(uri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.image.setOnClickListener(view -> pickImageLauncher.launch("image/*"));

        binding.button.setOnClickListener(view -> {
            if (isImageSet()) {
                /*
                Логика для оброботки фото
                 */
            } else {
                Toast.makeText(MainActivity.this, "Please set a photo.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isImageSet() {
        Drawable drawable = binding.image.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), defaultImageResourceId);
            return !bitmap.sameAs(defaultBitmap);
        }
        return false;
    }
}

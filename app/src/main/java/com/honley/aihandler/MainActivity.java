package com.honley.aihandler;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

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

        // Инициализация imageProcessing
        ImageProcessing imageProcessing = new ImageProcessing();

        binding.image.setOnClickListener(view -> pickImageLauncher.launch("image/*"));

        binding.button.setOnClickListener(view -> {
            if (isImageSet()) {
                imageProcessing.getObject(binding.image.getDrawable(), new ImageProcessing.ResultCallback() {
                    @Override
                    public void onSuccess(String resultText) {
                        runOnUiThread(() -> {
                            // Создаем и показываем AlertDialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage(resultText)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        });
                    }

                    @Override
                    public void onFailure(Throwable th) {
                        runOnUiThread(() -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Error: " + th.getMessage())
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        });
                    }
                });
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

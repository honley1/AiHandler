package com.honley.aihandler;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Executor;

public class ImageProcessing {

    private final String geminiApiKey = GeminiApiKey.geminiApiKey;

    public interface ResultCallback {
        void onSuccess(String resultText);
        void onFailure(Throwable th);
    }

    public void getObject(Drawable image, ResultCallback callback) {
        GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", geminiApiKey);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) image;
        Bitmap bitmap = bitmapDrawable.getBitmap();

        Content content = new Content.Builder()
                .addText("What do you see in this image? ответь и на русском и на английском")
                .addImage(bitmap)
                .build();

        Executor executor = MoreExecutors.newDirectExecutorService();
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        response.addListener(() -> {
            try {
                GenerateContentResponse result = response.get();
                String resultText = result.getText();
                callback.onSuccess(resultText);
            } catch (Exception e) {
                callback.onFailure(e);
            }
        }, executor);
    }
}

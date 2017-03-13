package com.fullmob.jiraboard.providers;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

public class BitmapsProvider {
    private Context appContext;

    public BitmapsProvider(Context appContext) {
        this.appContext = appContext;
    }

    public Bitmap getImageByUri(Uri photoURI) throws IOException {
        return MediaStore.Images.Media.getBitmap(appContext.getContentResolver(), photoURI);
    }
}

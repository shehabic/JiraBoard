package com.fullmob.jiraboard.managers.images;

import android.content.Context;
import android.widget.ImageView;

public interface SecuredImagesManagerInterface {
    void loadSVG(String url, Context activityContext, ImageView imageView);
    void loadImage(String url, Context activityContext, ImageView imageView);
}

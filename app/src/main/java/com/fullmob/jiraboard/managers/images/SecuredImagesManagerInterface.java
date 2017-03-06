package com.fullmob.jiraboard.managers.images;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;

public interface SecuredImagesManagerInterface {
    void loadSVG(String url, Context activityContext, AppCompatImageView imageView);
    void loadImage(String url, Context activityContext, AppCompatImageView imageView);
}

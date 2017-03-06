package com.fullmob.jiraboard.managers.images;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.util.Base64;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.request.target.Target;
import com.caverock.androidsvg.SVG;
import com.fullmob.jiraboard.managers.storage.EncryptedStorage;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class GlideSecuredImagesLoader implements SecuredImagesManagerInterface {

    private final EncryptedStorage encryptedStorage;

    public GlideSecuredImagesLoader(EncryptedStorage encryptedStorage) {
        this.encryptedStorage = encryptedStorage;
    }

    @Override
    public void loadSVG(final String url, final Context activityContext, final AppCompatImageView imageView) {
        GlideUrl glideUrl = createSecuredGlideUrl(getBiggerUrl(url));
        GenericRequestBuilder<GlideUrl, InputStream, SVG, PictureDrawable> requestBuilder = Glide.with(activityContext)
            .using(Glide.buildStreamModelLoader(GlideUrl.class, activityContext), InputStream.class)
            .from(GlideUrl.class)
            .as(SVG.class)
            .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
            .sourceEncoder(new StreamEncoder())
            .cacheDecoder(new FileToStreamDecoder<>(new SvgDecoder()))
            .decoder(new SvgDecoder())
            .animate(android.R.anim.fade_in)
            .listener(new SvgSoftwareLayerSetter<GlideUrl>() {
                @Override
                public boolean onException(Exception e, GlideUrl model, Target<PictureDrawable> target, boolean isFirstResource) {
                    loadImage(getBiggerUrl(url), activityContext, imageView);
                    return false;
                }
            });

        requestBuilder
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .load(glideUrl).into(imageView);
    }

    private String getBiggerUrl(String url) {
        return url.replace("size=xsmall&", "").replace("size=xsmall", "");
    }

    @Override
    public void loadImage(String url, Context activityContext, final AppCompatImageView imageView) {
        GlideUrl glideUrl = createSecuredGlideUrl(getBiggerUrl(url));
        Glide.with(activityContext).load(glideUrl).into(imageView);
    }

    @NonNull
    private GlideUrl createSecuredGlideUrl(String url) {
        GlideUrl glideUrl;
        try {
            glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization", "Basic " + createBasicAuthHeaderValue())
                .build());
        } catch (UnsupportedEncodingException exception) {
            glideUrl = new GlideUrl(url);
        }
        return glideUrl;
    }

    private String createBasicAuthHeaderValue() throws UnsupportedEncodingException {
        return Base64.encodeToString(
            (encryptedStorage.getUsername().trim() + ":" + encryptedStorage.getPassword().trim()).getBytes("UTF-8"),
            Base64.DEFAULT
        ).trim();
    }
}

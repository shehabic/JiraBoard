package com.fullmob.multiqr.processors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;

public class ImageProcessor {

    private Context context;

    public ImageProcessor(Context context) {
        this.context = context;
    }

    public Bitmap autoAdjustImage(Bitmap bitmap) {
        return adjustImage(bitmap, 3f, 0f, 0.1f);
    }

    public Bitmap adjustImage(Bitmap bitmap, float contrast, float saturation, float brightness) {

        GPUImage mGPUImage = new GPUImage(context);
        mGPUImage.setFilter(new GPUImageContrastFilter(contrast));

        GPUImage mGPUImage2 = new GPUImage(context);
        mGPUImage2.setFilter(new GPUImageSaturationFilter(saturation));

        GPUImage mGPUImage3 = new GPUImage(context);
        mGPUImage3.setFilter(new GPUImageBrightnessFilter(brightness));

        bitmap = mGPUImage3.getBitmapWithFilterApplied(bitmap);
        bitmap = mGPUImage2.getBitmapWithFilterApplied(bitmap);
        bitmap = mGPUImage.getBitmapWithFilterApplied(bitmap);

        return bitmap;
    }

    public Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
            matrix, true);
    }
}

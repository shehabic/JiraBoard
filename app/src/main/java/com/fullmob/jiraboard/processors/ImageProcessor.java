package com.fullmob.jiraboard.processors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.fullmob.jiraboard.data.Board;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;

public class ImageProcessor {

    private Context context;

    public ImageProcessor(Context context) {
        this.context = context;
    }

    public void autoAdjustImage(Board board) {
        adjustImage(board, 3f, 0f, 0.1f);
    }

    public void adjustImage(Board board, float contrast, float saturation, float brightness) {

        GPUImage mGPUImage = new GPUImage(context);
        mGPUImage.setFilter(new GPUImageContrastFilter(contrast));

//        GPUImage mGPUImage2 = new GPUImage(context);
//        mGPUImage2.setFilter(new GPUImageSaturationFilter(saturation));

        GPUImage mGPUImage3 = new GPUImage(context);
        mGPUImage3.setFilter(new GPUImageBrightnessFilter(brightness));

        board.setBitmap(mGPUImage3.getBitmapWithFilterApplied(board.getBitmap()));
//        bitmap = mGPUImage2.getBitmapWithFilterApplied(bitmap);
        board.setBitmap(mGPUImage.getBitmapWithFilterApplied(board.getBitmap()));
    }

    public Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}

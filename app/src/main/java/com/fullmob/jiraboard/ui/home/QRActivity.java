package com.fullmob.jiraboard.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullmob.jiraboard.BuildConfig;
import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.analyzer.BoardAnalyzer;
import com.fullmob.jiraboard.data.Column;
import com.fullmob.jiraboard.data.Board;
import com.fullmob.jiraboard.processors.ImageProcessor;

import java.io.File;
import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;


public class QRActivity extends AppCompatActivity {

    static final String PROJECT_PREFIX = "MOB";
    static final int CAPTURE_IMAGE_REQUEST = 1002;
    private static final int PICK_IMAGE_REQUEST = 1001;

    TextView mTextMessage;
    View capture;
    View pickImage;
    ImageView mImageView;

    Board project;
    BoardAnalyzer boardAnalyzer;
    ImageProcessor imageProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        project = new Board(PROJECT_PREFIX);
        boardAnalyzer = new BoardAnalyzer(BuildConfig.DEBUG);
        imageProcessor = new ImageProcessor(this);
        mTextMessage = (TextView) findViewById(R.id.message);
        capture = findViewById(R.id.capture);
        pickImage = findViewById(R.id.pick);
        mImageView = (ImageView) findViewById(R.id.image);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraCapture();
            }
        });
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPickingFile();
            }
        });
    }

    private void startPickingFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
        }
    }

    private void startCameraCapture() {
        mTextMessage.setText("");
        dispatchTakePictureIntent();
    }

    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (requestCode == PICK_IMAGE_REQUEST || requestCode == CAPTURE_IMAGE_REQUEST) {
                Uri imageUri = data.getData();
                try {
                    project.setBitmap(null);
                    mImageView.setImageBitmap(null);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    int rotate = 0;
                    if (requestCode == PICK_IMAGE_REQUEST) {
                        rotate = getCameraPhotoOrientation(this, imageUri, filePath);
                    } else if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                        rotate = 90;
                    }
                    if (rotate != 0) {
                        project.setBitmap(imageProcessor.rotateImage(bitmap, rotate));
                    }
                    try {
                        imageProcessor.autoAdjustImage(project);
                    } catch (OutOfMemoryError error) {
                        error.printStackTrace();
                    }
                    mTextMessage.setText("Loading...");
                    boardAnalyzer.analyzeProject(project)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultObserver<Board>() {
                            @Override
                            public void onNext(Board project) {
                                showOutput(project);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showOutput(Board project) {
        String headerOutput = "Columns: " + project.getColumns().size();
        String output = "";
        int ticketCount = 0;
        for (Column col : project.getColumns()) {
            ticketCount += col.tickets.size();
            output += "in [" + col.text.toUpperCase() + "]: ";
            for (int j = 0; j < col.tickets.size(); j++) {
                output += col.tickets.get(j).text + " , ";
            }
            output += " | ";
        }
        output = headerOutput + ", Tickets: " + ticketCount + "\n" + output;
        mTextMessage.setText(output);
        mImageView.setImageBitmap(project.getBitmap());
    }

}

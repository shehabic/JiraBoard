package com.fullmob.multiqr;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullmob.multiqr.analyzer.TicketsAnalyzer;
import com.fullmob.multiqr.data.Column;
import com.fullmob.multiqr.data.Project;
import com.fullmob.multiqr.processors.ImageProcessor;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;


public class QRActivity extends AppCompatActivity {

    static final String PROJECT_PREFIX = "MOB";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    TextView mTextMessage;
    View capture;
    ImageView mImageView;
    Project project;
    TicketsAnalyzer ticketsAnalyzer;
    ImageProcessor imageProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        project = new Project(PROJECT_PREFIX);
        ticketsAnalyzer = new TicketsAnalyzer(BuildConfig.DEBUG);
        imageProcessor = new ImageProcessor(this);
        mTextMessage = (TextView) findViewById(R.id.message);
        capture = findViewById(R.id.capture);
        mImageView = (ImageView) findViewById(R.id.image);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraCapture();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void startCameraCapture() {
        mTextMessage.setText("");
        dispatchTakePictureIntent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                project.setBitmap(null);
                mImageView.setImageBitmap(null);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
                    bitmap = imageProcessor.rotateImage(bitmap, 90);
                }
                bitmap = imageProcessor.autoAdjustImage(bitmap);
                project.setBitmap(bitmap);
                mTextMessage.setText("Loading...");
                ticketsAnalyzer.analyzeProject(project)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<Project>() {
                        @Override
                        public void onNext(Project project) {
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

    private void showOutput(Project project) {
        String headerOutput = "Columns: " + project.getColumns().size();
        String output = "";
        int ticketCount = 0;
        for (int i = 0; i < project.getColumns().size(); i++) {
            Column col = project.getColumns().get(i);
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

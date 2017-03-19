package com.fullmob.printable;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.fullmob.jiraboard.R;
import com.google.gson.Gson;

/**
 * Created by shehabic on 19/03/2017.
 */
public class PrintingTestActivity extends AppCompatActivity {

    ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printing_test);
        preview = (ImageView) findViewById(R.id.preview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderPrintable();
    }

    private void renderPrintable() {
        PrintableGroup group = PrintableInflator.from(this).inflate(R.xml.printables);
        PrintableImageGenerator printableImageGenerator = new PrintableImageGenerator();
        group.getPrintables().get(0).findElementById("summary").content = "test text";
        Bitmap bitmap
            = printableImageGenerator.createPrintable(group.getPrintables().get(0), PrintableGenerator.PaperSize.A6);
        preview.setImageBitmap(bitmap);
        Log.d("PRINT_GROUP", new Gson().toJson(group));
    }
}

package com.fullmob.printable;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.fullmob.jiraboard.R;
import com.fullmob.printable.generators.PrintableGenerator;
import com.fullmob.printable.generators.PrintableImageGenerator;
import com.fullmob.printable.inflators.PrintableInflator;
import com.google.gson.Gson;

/**
 * Created by shehabic on 19/03/2017.
 */
public class PrintingTestActivity extends AppCompatActivity {

    ImageView preview;
    PrintableImageGenerator printableImageGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printing_test);
        preview = (ImageView) findViewById(R.id.preview);
        printableImageGenerator = new PrintableImageGenerator(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderPrintable();
    }

    private void renderPrintable() {
        PrintableGroup group = PrintableInflator.from(this).inflate(R.xml.printables);
//        printSingleImage(group);
        printMultipleTexts(group);
    }

    private void printMultipleTexts(PrintableGroup group) {
        Printable printable = group.getPrintables().get(1);
        printable.findElementById("text1").content = "first text";
        printable.findElementById("text2").content = "second text";
        printable.findElementById("text3").content = "third text";
        printable.findElementById("text4").content = "fourth text";
        preview.setImageBitmap(
            printableImageGenerator.createPrintable(group.getPrintables().get(1), PrintableGenerator.PaperSize.A6)
        );
    }

    private void printSingleImage(PrintableGroup group) {
        group.getPrintables().get(0).findElementById("summary").content = "test text";
        Bitmap bitmap
            = printableImageGenerator.createPrintable(group.getPrintables().get(0), PrintableGenerator.PaperSize.A6);
        preview.setImageBitmap(bitmap);
        Log.d("PRINT_GROUP", new Gson().toJson(group));
    }
}

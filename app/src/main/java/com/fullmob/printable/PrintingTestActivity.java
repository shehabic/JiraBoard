package com.fullmob.printable;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.fullmob.jiraboard.R;
import com.fullmob.printable.generators.PrintableImageGenerator;
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
//        PrintableGroup group = PrintableInflator.from(this).inflate(R.xml.printables);
//        printSingleImage(group);
//        printMultipleTexts(group);
        printMultipleItemsProgrammatically();
    }

    private void printMultipleItemsProgrammatically() {
        Printable printable = new Printable("main");
        printable.orientation = Printable.PRINTABLE_ORIENTATION_PORTRAIT;
        printable.sizeString = "A4";
        printable.setAttribute("background", "#FFFFFF");
        Element parent = printable.PARENT;

        Element verticalSpacer = new Element(Element.TYPE_SPACER);
        verticalSpacer.alignTop = parent;
        verticalSpacer.alignBottom = parent;
        verticalSpacer.setRelativeDimenRule(parent, Element.RELATIVE_FIELD_WIDTH, Element.RELATIVE_FIELD_WIDTH, 0.30f);
        verticalSpacer.setRelativeDimenRule(parent, Element.RELATIVE_FIELD_HEIGHT, Element.RELATIVE_FIELD_HEIGHT, 1f);
        verticalSpacer.centerHorizontalIn = parent;
        verticalSpacer.id = "vertical_spacer";
        Element topSpacer = new Element(Element.TYPE_SPACER);
        topSpacer.alignTop = parent;
        topSpacer.alignLeft = parent;
        topSpacer.alignRight = parent;
        topSpacer.setRelativeDimenRule(parent, Element.RELATIVE_FIELD_HEIGHT, Element.RELATIVE_FIELD_HEIGHT, 0.01f);
        topSpacer.id = "top_spacer";
        Element lastElement = topSpacer;
        printable.addElement(verticalSpacer);
        printable.addElement(topSpacer);

        for (int i = 0; i < 10; i++) {
            String qrCode = "QR Code " + i;
            String ttl = "Element Status For " + (i + 1) + " Title ";

            Element qr = new Element(Element.TYPE_QR);
            qr.setRelativeDimenRule(parent, Element.RELATIVE_FIELD_WIDTH, Element.RELATIVE_FIELD_WIDTH, 0.20f);
            qr.setRelativeDimenRule(parent, Element.RELATIVE_FIELD_HEIGHT, Element.RELATIVE_FIELD_WIDTH, 0.20f);
            qr.below = lastElement;
            qr.marginTop = 20;
            qr.content = qrCode;
            qr.setParent(parent);
            qr.id = "qr" + i;
            Element title = new Element(Element.TYPE_TEXT);
            title.below = qr;
            title.content = ttl;
            title.textColor = Color.BLACK;
            title.textSize = 140;
            title.id = "title" + i;
            title.setRelativeDimenRule(parent, Element.RELATIVE_FIELD_WIDTH, Element.RELATIVE_FIELD_WIDTH, 0.49f);
            title.setRelativeDimenRule(parent, Element.RELATIVE_FIELD_HEIGHT, Element.RELATIVE_FIELD_HEIGHT, 0.03f);
            if (i % 2 == 0) {
                title.alignLeft = parent;
                qr.toLeftOf = verticalSpacer;
            } else {
                title.alignRight = parent;
                qr.toRightOf = verticalSpacer;
                lastElement = title;
            }
            title.setParent(parent);
            printable.addElement(qr);
            printable.addElement(title);
        }
        preview.setImageBitmap(printableImageGenerator.createPrintable(printable));
    }

    private void printMultipleTexts(PrintableGroup group) {
        Printable printable = group.getPrintables().get(1);
        printable.findElementById("text1").content = "first text";
        printable.findElementById("text2").content = "second text";
        printable.findElementById("text3").content = "third text";
        printable.findElementById("text4").content = "fourth text";
        preview.setImageBitmap(printableImageGenerator.createPrintable(group.getPrintables().get(1)));
    }

    private void printSingleImage(PrintableGroup group) {
        group.getPrintables().get(0).findElementById("summary").content = "test text";
        Bitmap bitmap = printableImageGenerator.createPrintable(group.getPrintables().get(0));
        preview.setImageBitmap(bitmap);
        Log.d("PRINT_GROUP", new Gson().toJson(group));
    }
}

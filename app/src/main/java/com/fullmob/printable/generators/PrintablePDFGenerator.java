package com.fullmob.printable.generators;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;

import com.fullmob.printable.Printable;

/**
 * Created by shehabic on 19/03/2017.
 */
public class PrintablePDFGenerator extends AbstractPrintableGenerator<PrintedPdfDocument> {

    private final PrintAttributes printAttributes;

    public PrintablePDFGenerator(Context context, PrintAttributes printAttributes) {
        super(context);
        this.printAttributes = printAttributes;
        scale = 0.1f;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public PrintedPdfDocument createPrintable(Printable printable, int width, int height) {
        PrintedPdfDocument pdfDocument = new PrintedPdfDocument(context.get(), printAttributes);
        int pageHeight = height;
        int pageWidth = width;
        if (printAttributes.getMediaSize() != null) {
            if (printable.isLandscape()) {
                pageHeight = printAttributes.getMediaSize().asLandscape().getHeightMils() / 1000 * 72;
                pageWidth = printAttributes.getMediaSize().asLandscape().getWidthMils() / 1000 * 72;
            } else {
                pageHeight = printAttributes.getMediaSize().getHeightMils() / 1000 * 72;
                pageWidth = printAttributes.getMediaSize().getWidthMils() / 1000 * 72;
            }
        }
        PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 0).create();
        PdfDocument.Page page = pdfDocument.startPage(newPage);
        drawPrintableInCanvas(printable, pageWidth, pageHeight, page.getCanvas());
        pdfDocument.finishPage(page);

        return pdfDocument;
    }
}

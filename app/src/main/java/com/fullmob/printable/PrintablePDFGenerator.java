package com.fullmob.printable;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;

import java.lang.ref.WeakReference;

public class PrintablePDFGenerator extends AbstractPrintableGenerator<PrintedPdfDocument> {

    private final PrintAttributes printAttributes;
    private WeakReference<Context> activityContext;

    public PrintablePDFGenerator(Context activityContext, PrintAttributes printAttributes) {
        this.activityContext = new WeakReference<>(activityContext);
        this.printAttributes = printAttributes;
        scale = 0.1f;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public PrintedPdfDocument createPrintable(Printable printable, int width, int height) {
        PrintedPdfDocument pdfDocument = new PrintedPdfDocument(activityContext.get(), printAttributes);
        int pageHeight = height;
        int pageWidth = width;
        if (printAttributes.getMediaSize() != null) {
            pageHeight = printAttributes.getMediaSize().asLandscape().getHeightMils() / 1000 * 72;
            pageWidth = printAttributes.getMediaSize().asLandscape().getWidthMils() / 1000 * 72;
        }
        PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 0).create();
        PdfDocument.Page page = pdfDocument.startPage(newPage);
        drawPrintableInCanvas(printable, pageWidth, pageHeight, page.getCanvas());
        pdfDocument.finishPage(page);

        return pdfDocument;
    }
}

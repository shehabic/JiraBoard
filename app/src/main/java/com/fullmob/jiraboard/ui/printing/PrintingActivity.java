package com.fullmob.jiraboard.ui.printing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.print.PrintHelper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.ui.BaseActivity;
import com.fullmob.jiraboard.ui.models.UIIssueStatus;
import com.fullmob.printable.Printable;
import com.fullmob.printable.PrintableGenerator;
import com.fullmob.printable.PrintableImageGenerator;
import com.fullmob.printable.PrintablePDFGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PrintingActivity extends BaseActivity {

    public static final String EXTRA_PAYLOAD_TYPE = "payload_type";
    public static final String EXTRA_TYPE_ISSUE = "type_issue";
    public static final String EXTRA_TYPE_ISSUE_STATUS = "type_issue_status";
    public static final String EXTRA_PAYLOAD = "payload";

    private String payloadType;
    private Parcelable payload;

    @BindView(R.id.qr_preview)
    ImageView qrPreview;

    TicketsPrintableGenerator printableGenerator;
    PrintableImageGenerator qrBitmapGenerator;
    PrintablePDFGenerator pdfTicketGenerator;
    private PrintedPdfDocument document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printing);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        printableGenerator = new TicketsPrintableGenerator();
        qrBitmapGenerator = new PrintableImageGenerator();
        Printable printable = null;
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            printable = createPrintable(intent);
        } else {
            printable = createPrintable(savedInstanceState);
        }
        if (printable != null) {
            generatePrintableImage(printable);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && printable != null) {
            generatePrintableGraphics(printable);
        }
        initUI();
    }

    private void generatePrintableImage(Printable printable) {
        showLoading();
        Observable.just(qrBitmapGenerator.createPrintable(printable, PrintableImageGenerator.PaperSize.A8))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Bitmap>() {
                @Override
                public void accept(Bitmap bitmap) throws Exception {
                    qrPreview.setImageBitmap(bitmap);
                    hideLoading();
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    hideLoading();
                }
            });
    }

    @Nullable
    private Printable createPrintable(Parcelable payload, String payloadType) {
        Printable printable = null;
        if (payloadType.equals(EXTRA_TYPE_ISSUE)) {
            printable = printableGenerator.buildPrintableTicket((Issue) payload);
        } else if (payloadType.equals(EXTRA_TYPE_ISSUE_STATUS)) {
            printable = printableGenerator.buildPrintableTicket((UIIssueStatus) payload);
        }

        return printable;
    }

    @Nullable
    private Printable createPrintable(Bundle intent) {
        payloadType = intent.getString(EXTRA_PAYLOAD_TYPE);
        payload = intent.getParcelable(EXTRA_PAYLOAD);

        return createPrintable(payload, payloadType);
    }

    @Nullable
    private Printable createPrintable(Intent intent) {
        payload = intent.getParcelableExtra(EXTRA_PAYLOAD);
        payloadType = intent.getStringExtra(EXTRA_PAYLOAD_TYPE);

        return createPrintable(payload, payloadType);
    }

    private void initUI() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (document != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        printPdf(document);
                    }
                } else if (qrPreview.getDrawable() != null) {
                    PrintHelper photoPrinter = new PrintHelper(PrintingActivity.this);
                    photoPrinter.setColorMode(PrintHelper.COLOR_MODE_MONOCHROME);
                    photoPrinter.printBitmap("print qr", ((BitmapDrawable) qrPreview.getDrawable()).getBitmap());
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void generatePrintableGraphics(Printable printable) {
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A6);
        builder.setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME);
        builder.setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0));
        PrintAttributes attributes = builder.build();
        pdfTicketGenerator = new PrintablePDFGenerator(this, attributes);
        Observable.just(pdfTicketGenerator.createPrintable(printable, PrintableGenerator.PaperSize.A6))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<PrintedPdfDocument>() {
                @Override
                public void accept(PrintedPdfDocument printedPdfDocument) throws Exception {
                    document = printedPdfDocument;
                }
            });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printPdf(PrintedPdfDocument document) {
        PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);
        String jobName = getString(R.string.app_name) + " Document";
        printManager.print(jobName, new JiraBoardPdfDocumentAdapter(document), null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_PAYLOAD_TYPE, payloadType);
        outState.putParcelable(EXTRA_PAYLOAD, payload);
    }
}

package com.fullmob.jiraboard.ui.printing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.print.PrintAttributes;
import android.print.PrintJob;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.print.PrintHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.ui.BaseActivity;
import com.fullmob.jiraboard.ui.models.PrintableIssueStatuses;
import com.fullmob.printable.Printable;
import com.fullmob.printable.generators.PrintableImageGenerator;
import com.fullmob.printable.generators.PrintablePDFGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PrintingActivity extends BaseActivity implements JiraBoardPdfDocumentAdapter.Listener {

    public static final String EXTRA_PAYLOAD_TYPE = "payload_type";
    public static final String EXTRA_TYPE_ISSUE = "type_issue";
    public static final String EXTRA_TYPE_ISSUE_STATUS = "type_issue_status";
    public static final String EXTRA_PAYLOAD = "payload";

    private String payloadType;
    private Parcelable payload;

    @BindView(R.id.qr_preview)
    ImageView qrPreview;

    JiraPrintablesGenerator printableGenerator;
    PrintableImageGenerator qrBitmapGenerator;
    PrintablePDFGenerator pdfTicketGenerator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printing);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        printableGenerator = new JiraPrintablesGenerator();
        qrBitmapGenerator = new PrintableImageGenerator(this);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            payload = intent.getParcelableExtra(EXTRA_PAYLOAD);
            payloadType = intent.getStringExtra(EXTRA_PAYLOAD_TYPE);
        } else {
            payload = savedInstanceState.getParcelable(EXTRA_PAYLOAD);
            payloadType = savedInstanceState.getString(EXTRA_PAYLOAD_TYPE);
        }
        Printable printable  = createPrintable(payload, payloadType);
        if (printable != null) {
            generatePrintableImage(printable);
        }
        initUI();
    }
    private void generatePrintableImage(Printable printable) {
        generatePrintableImage(printable, false);
    }

    private void generatePrintableImage(final Printable printable, final boolean print) {
        showLoading();
        Observable.just(qrBitmapGenerator.createPrintable(printable, PrintableImageGenerator.PaperSize.A8))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Bitmap>() {
                @Override
                public void accept(Bitmap bitmap) throws Exception {
                    if (print) {
                        printBitmap(printable, bitmap);
                    } else {
                        qrPreview.setImageBitmap(bitmap);
                    }
                    hideLoading();
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    hideLoading();
                }
            });
    }

    private void printBitmap(Printable printable, Bitmap bitmap) {
        PrintHelper photoPrinter = new PrintHelper(PrintingActivity.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.setColorMode(PrintHelper.COLOR_MODE_MONOCHROME);
        photoPrinter.setOrientation(
            printable.isLandscape() ? PrintHelper.ORIENTATION_LANDSCAPE : PrintHelper.ORIENTATION_PORTRAIT
        );
        photoPrinter.printBitmap("Test Print", bitmap);
    }

    @Nullable
    private Printable createPrintable(Parcelable payload, String payloadType) {
        Printable printable = null;
        if (payloadType.equals(EXTRA_TYPE_ISSUE)) {
            printable = printableGenerator.buildPrintableStatuses((Issue) payload);
        } else if (payloadType.equals(EXTRA_TYPE_ISSUE_STATUS)) {
            printable = printableGenerator.buildPrintableStatuses((PrintableIssueStatuses) payload);
        }

        return printable;
    }

    private void initUI() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Printable printable  = createPrintable(payload, payloadType);
                generatePrintableImage(printable, true);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    generatePrintableGraphics(printable);
//                } else {
//                    showToast("Your android version doesn't support PDF printing");
//                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void generatePrintableGraphics(Printable printable) {
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        setPrintingSize(builder, printable);
        builder.setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME);
        builder.setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0));
        final PrintAttributes attributes = builder.build();
        pdfTicketGenerator = new PrintablePDFGenerator(this, attributes);
        Observable.just(pdfTicketGenerator.createPrintable(printable))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<PrintedPdfDocument>() {
                @Override
                public void accept(PrintedPdfDocument printedPdfDocument) throws Exception {
                    printPdf(printedPdfDocument, attributes);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    showError(throwable);
                }
            });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setPrintingSize(PrintAttributes.Builder builder, Printable printable) {
        switch (printable.sizeString) {
            case "A4":
                builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
                break;
            case "A5":
                builder.setMediaSize(PrintAttributes.MediaSize.ISO_A5);
                break;
            case "A6":
                builder.setMediaSize(PrintAttributes.MediaSize.ISO_A6);
                break;
            case "A7":
                builder.setMediaSize(PrintAttributes.MediaSize.ISO_A7);
                break;
            case "A8":
                builder.setMediaSize(PrintAttributes.MediaSize.ISO_A8);
                break;
            case "A9":
                builder.setMediaSize(PrintAttributes.MediaSize.ISO_A9);
                break;
            case "A10":
                builder.setMediaSize(PrintAttributes.MediaSize.ISO_A10);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printPdf(PrintedPdfDocument document, PrintAttributes attributes) {
        PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);
        String jobName = getString(R.string.app_name) + " Document";
        PrintJob job = printManager.print(jobName, new JiraBoardPdfDocumentAdapter(document, this), attributes);
        if (job != null) {
            Log.d("PRINTING", job.getInfo().getLabel());
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_PAYLOAD_TYPE, payloadType);
        outState.putParcelable(EXTRA_PAYLOAD, payload);
    }

    @Override
    public void onPrintingStarted() {
        Log.d("PRINTING", "started");
    }

    @Override
    public void onPrintingFinished() {
        Log.d("PRINTING", "finished");
    }
}

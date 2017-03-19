package com.fullmob.jiraboard.ui.printing;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.printing.Printable;
import com.fullmob.jiraboard.ui.models.UIIssueStatus;

public class PrintableGenerator {

    private static final float TEXT_MULTIPLIER_SMALL = 0.04f;
    private static final float TEXT_MULTIPLIER_MEDIUM = 0.06f;
    private static final float TEXT_MULTIPLIER_LARGE = 0.09f;

    public Printable buildPrintableTicket(UIIssueStatus status) {
        Printable printable = new Printable();

        Printable.Element qr = new Printable.Element(Printable.SecType.QR, status.getName() + "||" + status.getId());
        qr.alignTop = printable.PARENT;
        qr.centerHorizontalOf = printable.PARENT;
        printable.addSection(qr);

        Printable.Element summary = new Printable.Element(
            Printable.SecType.TEXT,
            status.getName()
        );
        summary.below = qr;
        summary.alignLeft = printable.PARENT;
        summary.alignRight = printable.PARENT;
        summary.marginBottom = 20;
        summary.alignBottom = printable.PARENT;
        summary.textSize = TEXT_MULTIPLIER_MEDIUM;
        summary.textSizeMultiplier = printable.PARENT;
        summary.textHAlign = Printable.TextHAlign.CENTER;
        summary.textVAlign = Printable.TextVAlign.MIDDLE;
        printable.addSection(summary);

        return printable;
    }

    public Printable buildPrintableTicket(Issue issue) {
        Printable printable = new Printable();

        Printable.Element qr = new Printable.Element(Printable.SecType.QR, issue.getKey() + "||" + issue.getId());
        qr.alignTop = printable.PARENT;
        qr.centerHorizontalOf = printable.PARENT;
        printable.addSection(qr);

        Printable.Element summary = new Printable.Element(Printable.SecType.TEXT, issue.getIssueFields().getSummary());
        summary.below = qr;
        summary.alignLeft = printable.PARENT;
        summary.alignRight = printable.PARENT;
        summary.marginBottom = 0;
        summary.alignBottom = printable.PARENT;
        summary.textSize = TEXT_MULTIPLIER_MEDIUM;
        summary.textSizeMultiplier = printable.PARENT;
        summary.textHAlign = Printable.TextHAlign.CENTER;
        summary.textVAlign = Printable.TextVAlign.MIDDLE;
        printable.addSection(summary);

        Printable.Element projKey = new Printable.Element(Printable.SecType.TEXT, issue.getKey().split("-")[0]);
        projKey.toLeftOf = qr;
        projKey.alignLeft = printable.PARENT;
        projKey.alignTop = printable.PARENT;
        projKey.alignBottom = qr;
        projKey.textSize = TEXT_MULTIPLIER_LARGE;
        projKey.textSizeMultiplier = printable.PARENT;
        projKey.textHAlign = Printable.TextHAlign.RIGHT;
        projKey.textVAlign = Printable.TextVAlign.MIDDLE;
        printable.addSection(projKey);

        Printable.Element issueNum = new Printable.Element(Printable.SecType.TEXT, issue.getKey().split("-")[1]);
        issueNum.toRightOf = qr;
        issueNum.alignRight = printable.PARENT;
        issueNum.alignBottom = qr;
        issueNum.alignTop = printable.PARENT;
        issueNum.textSize = TEXT_MULTIPLIER_LARGE;
        issueNum.textSizeMultiplier = printable.PARENT;
        issueNum.textHAlign = Printable.TextHAlign.LEFT;
        issueNum.textVAlign = Printable.TextVAlign.MIDDLE;
        printable.addSection(issueNum);

        return printable;
    }
}
package com.fullmob.jiraboard.ui.printing;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.ui.models.UIIssueStatus;
import com.fullmob.printable.Element;
import com.fullmob.printable.Printable;

public class PrintableGenerator {

    private static final float TEXT_MULTIPLIER_SMALL = 0.04f;
    private static final float TEXT_MULTIPLIER_MEDIUM = 0.06f;
    private static final float TEXT_MULTIPLIER_LARGE = 0.09f;

    public Printable buildPrintableTicket(UIIssueStatus status) {
        Printable printable = new Printable();

        Element qr = new Element(Element.Type.QR, status.getName() + "||" + status.getId());
        qr.alignTop = printable.PARENT;
        qr.centerHorizontalIn = printable.PARENT;
        printable.addElement(qr);

        Element summary = new Element(
            Element.Type.TEXT,
            status.getName()
        );
        summary.below = qr;
        summary.alignLeft = printable.PARENT;
        summary.alignRight = printable.PARENT;
        summary.marginBottom = 20;
        summary.alignBottom = printable.PARENT;
        summary.textSize = TEXT_MULTIPLIER_MEDIUM;
        summary.textSizeMultiplier = printable.PARENT;
        summary.textHAlign = Element.TextHAlign.CENTER;
        summary.textVAlign = Element.TextVAlign.MIDDLE;
        printable.addElement(summary);

        return printable;
    }

    public Printable buildPrintableTicket(Issue issue) {
        Printable printable = new Printable();

        Element qr = new Element(Element.Type.QR, issue.getKey() + "||" + issue.getId());
        qr.alignTop = printable.PARENT;
        qr.centerHorizontalIn = printable.PARENT;
        printable.addElement(qr);

        Element summary = new Element(Element.Type.TEXT, issue.getIssueFields().getSummary());
        summary.below = qr;
        summary.alignLeft = printable.PARENT;
        summary.alignRight = printable.PARENT;
        summary.marginBottom = 0;
        summary.alignBottom = printable.PARENT;
        summary.textSize = TEXT_MULTIPLIER_MEDIUM;
        summary.textSizeMultiplier = printable.PARENT;
        summary.textHAlign = Element.TextHAlign.CENTER;
        summary.textVAlign = Element.TextVAlign.MIDDLE;
        printable.addElement(summary);

        Element projKey = new Element(Element.Type.TEXT, issue.getKey().split("-")[0]);
        projKey.toLeftOf = qr;
        projKey.alignLeft = printable.PARENT;
        projKey.alignTop = printable.PARENT;
        projKey.alignBottom = qr;
        projKey.textSize = TEXT_MULTIPLIER_LARGE;
        projKey.textSizeMultiplier = printable.PARENT;
        projKey.textHAlign = Element.TextHAlign.RIGHT;
        projKey.textVAlign = Element.TextVAlign.MIDDLE;
        printable.addElement(projKey);

        Element issueNum = new Element(Element.Type.TEXT, issue.getKey().split("-")[1]);
        issueNum.toRightOf = qr;
        issueNum.alignRight = printable.PARENT;
        issueNum.alignBottom = qr;
        issueNum.alignTop = printable.PARENT;
        issueNum.textSize = TEXT_MULTIPLIER_LARGE;
        issueNum.textSizeMultiplier = printable.PARENT;
        issueNum.textHAlign = Element.TextHAlign.LEFT;
        issueNum.textVAlign = Element.TextVAlign.MIDDLE;
        printable.addElement(issueNum);

        return printable;
    }
}
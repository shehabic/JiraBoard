package com.fullmob.jiraboard.ui.printing;

import android.graphics.Color;

import com.fullmob.jiraapi.models.Issue;
import com.fullmob.jiraboard.ui.models.PrintableIssueStatuses;
import com.fullmob.jiraboard.ui.models.UIIssueStatus;
import com.fullmob.printable.Element;
import com.fullmob.printable.Printable;

public class JiraPrintablesGenerator {

    private static final float TEXT_MULTIPLIER_TINY = 0.02f;
    private static final float TEXT_MULTIPLIER_SMALL = 0.04f;
    private static final float TEXT_MULTIPLIER_MEDIUM = 0.06f;
    private static final float TEXT_MULTIPLIER_LARGE = 0.09f;

    public Printable buildPrintableStatuses(PrintableIssueStatuses printableIssueStatuses) {
        Printable printable = new Printable("main");
        printable.orientation = Printable.PRINTABLE_ORIENTATION_PORTRAIT;
        printable.sizeString = "A4";
        printable.setAttribute("background", "#FFFFFF");
        Element parent = printable.PARENT;

        Element verticalSpacer = new Element(Element.TYPE_SPACER);
        verticalSpacer.alignTop = parent;
        verticalSpacer.alignBottom = parent;
        verticalSpacer.setRelativeDimenRule(parent, Element.RELATIVE_FIELD_WIDTH, Element.RELATIVE_FIELD_WIDTH, 0.20f);
        verticalSpacer.setRelativeDimenRule(parent, Element.RELATIVE_FIELD_HEIGHT, Element.RELATIVE_FIELD_HEIGHT, 1f);
        verticalSpacer.centerHorizontalIn = parent;
        verticalSpacer.id = "vertical_spacer";
        Element topSpacer = new Element(Element.TYPE_SPACER);
        topSpacer.alignTop = parent;
        topSpacer.alignLeft = parent;
        topSpacer.alignRight = parent;
        topSpacer.setRelativeDimenRule(parent, Element.RELATIVE_FIELD_HEIGHT, Element.RELATIVE_FIELD_HEIGHT, 0.001f);
        topSpacer.id = "top_spacer";
        Element lastElement = topSpacer;
        printable.addElement(verticalSpacer);
        printable.addElement(topSpacer);

        int i = 0;
        for (UIIssueStatus status : printableIssueStatuses.statuses) {
            String qrText = String.format("{\"id\":\"%s\", \"name\":\"%s\"}", status.getId(), status.getName());
            String stringTitle = status.getName();
            Element qr = new Element(Element.TYPE_QR);
            qr.setRelativeDimenRule(parent, Element.RELATIVE_FIELD_WIDTH, Element.RELATIVE_FIELD_WIDTH, 0.3f);
            qr.setRelativeDimenRule(parent, Element.RELATIVE_FIELD_HEIGHT, Element.RELATIVE_FIELD_WIDTH, 0.3f);
            qr.below = lastElement;
            qr.marginTop = 20;
            qr.content = qrText;
            qr.setParent(parent);
            qr.id = "qr" + i;
            Element title = new Element(Element.TYPE_TEXT);
            title.below = qr;
            title.content = stringTitle;
            title.textColor = Color.BLACK;
            title.textSize = TEXT_MULTIPLIER_SMALL;
            title.textSizeMultiplier = parent;
            title.id = "title" + i;
            title.setRelativeDimenRule(parent, Element.RELATIVE_FIELD_WIDTH, Element.RELATIVE_FIELD_WIDTH, 0.49f);
            title.setRelativeDimenRule(parent, Element.RELATIVE_FIELD_HEIGHT, Element.RELATIVE_FIELD_HEIGHT, 0.02f);
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
            i++;
        }

        return printable;
    }

    public Printable buildPrintableStatuses(Issue issue) {
        Printable printable = new Printable();

        Element qr = new Element(Element.TYPE_QR, issue.getKey() + "||" + issue.getId());
        qr.alignTop = printable.PARENT;
        qr.centerHorizontalIn = printable.PARENT;
        qr.id = "id";
        printable.addElement(qr);

        Element summary = new Element(Element.TYPE_TEXT, issue.getIssueFields().getSummary());
        summary.below = qr;
        summary.id = "summary";
        summary.alignLeft = printable.PARENT;
        summary.alignRight = printable.PARENT;
        summary.marginBottom = 0;
        summary.alignBottom = printable.PARENT;
        summary.textSize = TEXT_MULTIPLIER_MEDIUM;
        summary.textSizeMultiplier = printable.PARENT;
        summary.textHAlign = Element.CENTER;
        summary.textVAlign = Element.MIDDLE;
        printable.addElement(summary);

        Element projKey = new Element(Element.TYPE_TEXT, issue.getKey().split("-")[0]);
        projKey.toLeftOf = qr;
        projKey.id = "proj_key";
        projKey.alignLeft = printable.PARENT;
        projKey.alignTop = printable.PARENT;
        projKey.alignBottom = qr;
        projKey.textSize = TEXT_MULTIPLIER_LARGE;
        projKey.textSizeMultiplier = printable.PARENT;
        projKey.textHAlign = Element.RIGHT;
        projKey.textVAlign = Element.MIDDLE;
        printable.addElement(projKey);

        Element issueNum = new Element(Element.TYPE_TEXT, issue.getKey().split("-")[1]);
        issueNum.toRightOf = qr;
        issueNum.id = "issue_num";
        issueNum.alignRight = printable.PARENT;
        issueNum.alignBottom = qr;
        issueNum.alignTop = printable.PARENT;
        issueNum.textSize = TEXT_MULTIPLIER_LARGE;
        issueNum.textSizeMultiplier = printable.PARENT;
        issueNum.textHAlign = Element.LEFT;
        issueNum.textVAlign = Element.MIDDLE;
        printable.addElement(issueNum);

        return printable;
    }
}
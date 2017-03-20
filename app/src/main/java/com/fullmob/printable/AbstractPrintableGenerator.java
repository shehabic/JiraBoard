package com.fullmob.printable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.fullmob.printable.drawers.ElementDrawer;
import com.fullmob.printable.drawers.ImageElementDrawer;
import com.fullmob.printable.drawers.QRElementDrawer;
import com.fullmob.printable.drawers.TextElementDrawer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by shehabic on 19/03/2017.
 */
abstract class AbstractPrintableGenerator<T> implements PrintableGenerator<T> {

    float scale = 1f;

    private final HashSet<Element> drawn = new HashSet<>();
    private final HashSet<Element> measured = new HashSet<>();
    private final Map<String, ElementDrawer> elementDrawers = new HashMap<>();

    private String drawerType;

    public AbstractPrintableGenerator() {
        elementDrawers.put("qr", new QRElementDrawer());
        elementDrawers.put("text", new TextElementDrawer());
        elementDrawers.put("image", new ImageElementDrawer());
    }

    @Override
    public void addDrawer(String type, ElementDrawer drawer) {
        elementDrawers.put(type, drawer);
    }

    @Override
    public T createPrintable(Printable printable, PaperSize paperSize) {
        int[] size = findSize(paperSize, true);

        return createPrintable(printable, size[0] / 2, size[1] / 2);
    }

    public static int[] findSize(String sizeString, boolean landscape) {
        switch (sizeString.toUpperCase()) {
            case "A5":
                return findSize(PaperSize.A5, landscape);
            case "A6":
                return findSize(PaperSize.A6, landscape);
            case "A7":
                return findSize(PaperSize.A7, landscape);
            case "A8":
                return findSize(PaperSize.A8, landscape);
            case "A9":
                return findSize(PaperSize.A9, landscape);
            case "A10":
                return findSize(PaperSize.A10, landscape);
            default:
                throw new RuntimeException("Unsupported paper size: " + sizeString);
        }
    }

    public static int[] findSize(PaperSize paperSize, boolean landscape) {
        int h = 0, w = 0;
        switch (paperSize) {
            // TODO: add the missing sizes

            case A6:
                h = 4130;
                w = 5830;
                break;

            case A7:
                h = 2910;
                w = 4130;
                break;

            case A8:
                h = 2050;
                w = 2910;
                break;

            case A9:
                h = 1460;
                w = 2050;
                break;

            case A10:
                h = 1020;
                w = 1460;
                break;
        }
        return landscape ? new int[]{w, h} : new int[]{h, w};
    }

    @Override
    public void measureElement(Element element) {
        if (element.toLeftOf != null) {
            if (measured.contains(element.toLeftOf)) {
                element.right = element.toLeftOf.left - element.marginRight;
            }
        }
        if (element.toRightOf != null) {
            if (measured.contains(element.toRightOf)) {
                element.left = element.toRightOf.right + element.marginLeft;
            }
        }
        if (element.below != null) {
            if (measured.contains(element.below)) {
                element.top = element.below.bottom + element.marginTop;
            }
        }
        if (element.above != null) {
            if (measured.contains(element.above)) {
                element.bottom = element.above.top;
            }
        }
        if (element.alignBottom != null) {
            if (measured.contains(element.alignBottom)) {
                element.bottom = element.alignBottom.bottom - element.marginBottom;
            }
        }
        if (element.alignTop != null) {
            if (measured.contains(element.alignTop)) {
                element.top = element.alignTop.top + element.marginTop;
            }
        }
        if (element.alignLeft != null) {
            if (measured.contains(element.alignLeft)) {
                element.left = element.alignLeft.left + element.marginLeft;
            }
        }
        if (element.alignRight != null) {
            if (measured.contains(element.alignRight)) {
                element.right = element.alignRight.right - element.marginRight;
            }
        }

        if (element.centerHorizontalIn != null) {
            if (measured.contains(element.centerHorizontalIn)) {
                if (element.width > -1) {
                    element.left = ((element.centerHorizontalIn.left + element.centerHorizontalIn.right) / 2);
                    element.left -= (element.width / 2);
                    element.left += (element.marginLeft - element.marginRight);
                } else {
                    // TODO: determine how to measure width and get left
                }
            }
        }
        if (element.centerVerticalIn != null) {
            if (measured.contains(element.centerVerticalIn)) {
                if (element.height > -1) {
                    element.top = ((element.centerVerticalIn.top + element.centerVerticalIn.bottom) / 2);
                    element.top -= (element.height / 2);
                    element.top += (element.marginTop - element.marginBottom);
                } else {
                    // TODO: determine how to measure width and get left
                }
            }
        }
        if (element.width != -1) {
            if (element.right == -1) {
                element.right = element.left + element.width;
            } else if (element.left == -1) {
                element.left = element.right - element.width;
            }
        } else if (element.right != -1 && element.left != -1) {
            element.width = element.right - element.left;
        }
        if (element.height != -1) {
            if (element.bottom == -1) {
                element.bottom = element.top + element.height;
            } else if (element.top == -1) {
                element.top = element.bottom - element.height;
            }
        } else if (element.top != -1 && element.bottom != -1) {
            element.height = element.bottom - element.top;
        }
        if (element.height > -1 && element.width > -1) {
            measured.add(element);
        }
    }

    @Override
    public void prepareParent(Printable printable, int width, int height) {
        printable.PARENT.left = 0;
        printable.PARENT.top = 0;
        printable.PARENT.width = width;
        printable.PARENT.height = height;
        printable.PARENT.right = width;
        printable.PARENT.bottom = height;
        measured.add(printable.PARENT);
    }

    void drawPrintableInCanvas(Printable printable, int width, int height, Canvas canvas) {
        boolean allDrawn = false;
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, width, height, paint);
        prepareParent(printable, width, height);
        while (!allDrawn) {
            allDrawn = true;
            for (Element element : printable.elements) {
                allDrawn &= drawn.contains(element);
                if (!drawn.contains(element)) {
                    drawerType = element.isCustom() ? element.subType : element.type;
                    if (drawerType == null || !elementDrawers.containsKey(element.type)) {
                        throw new RuntimeException("Cannot draw element " + element.type);
                    }
                    elementDrawers.get(drawerType).layout(element);
                    if (!measured.contains(element)) {
                        measureElement(element);
                    }
                    if (measured.contains(element)) {
                        elementDrawers.get(element.type).draw(canvas, element);
                        drawn.add(element);
                    }
                }
            }
        }
        drawn.clear();
        measured.clear();
    }
}

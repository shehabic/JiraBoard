package com.fullmob.jiraboard;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.fullmob.printable.Printable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ExampleUnitTest {

    private Context instrumentationCtx;

    @Before
    public void setup() {
        instrumentationCtx = InstrumentationRegistry.getContext();
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void buildPrintable() {
        List<Printable> printables = new ArrayList<>();
        Resources res = instrumentationCtx.getResources();
        XmlResourceParser xrp = res.getXml(R.xml.printables);
        try{
            xrp.next(); // skip first 'mountains' element
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                xrp.next(); // get first 'mountain' element
                if(xrp.getEventType() == XmlResourceParser.START_TAG) {
                    // double check its the right element
                    if(xrp.getName().equals("printable")) {
                        Printable printable = new Printable();
                        String size = xrp.getAttributeValue(null, "size");
                        String width = xrp.getAttributeValue(null, "width");
                        String height = xrp.getAttributeValue(null, "height");
                        if (size != null) {
                            printable.setSize(size.trim());
                        }
                    }
                }
            }
        } catch (Exception e) {
//            Log.w(TAG, e.toString());
        } finally {
            xrp.close();
//            Log.i(TAG, elevations.toString());
        }
    }
}
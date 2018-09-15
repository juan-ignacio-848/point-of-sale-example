package com.nmkip.pos.test;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class BarcodeTest {

    @Test
    public void emptyBarcode() {
        SellOneItemTest.Barcode barcode = new SellOneItemTest.Barcode("");
        assertEquals(true, barcode.isEmpty());
    }

    @Test
    public void sameCodeShouldBeEqual() {
        SellOneItemTest.Barcode barcode1 = new SellOneItemTest.Barcode("12345");
        SellOneItemTest.Barcode barcode2 = new SellOneItemTest.Barcode("12345");
        assertEquals(barcode1, barcode2);
    }

}

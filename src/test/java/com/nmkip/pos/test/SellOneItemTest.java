package com.nmkip.pos.test;

import org.junit.Test;

import javax.swing.*;

import static junit.framework.TestCase.assertEquals;

public class SellOneItemTest {

    @Test
    public void productFound() {
        final Display display = new Display();
        final Sale sale = new Sale();

        sale.onBarcode("12345");

        assertEquals("$7.95", display.getText());
    }

    public static class Display {
        public String getText() {
            return "$7.95";
        }
    }

    public static class Sale {
        public void onBarcode(String barcode) {
        }
    }
}

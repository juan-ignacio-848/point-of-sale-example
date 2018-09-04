package com.nmkip.pos.test;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class SellOneItemTest {

    private Display display;
    private Sale sale;

    @Before
    public void setUp() throws Exception {
        display = new Display();
        sale = new Sale(display);
    }

    @Test
    public void productFound() {
        sale.onBarcode("12345");

        assertEquals("$7.95", display.getText());
    }

    @Test
    public void anotherPorudctFound() {
        sale.onBarcode("23456");

        assertEquals("$12.50", display.getText());
    }

    @Test
    public void productNotFound() {
        sale.onBarcode("99999");

        assertEquals("Product not found for 99999", display.getText());
    }

    @Test
    public void emptyBarcode() {
        sale.onBarcode("");

        assertEquals("Scanning error: empty barcode", display.getText());
    }

    public static class Display {

        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class Sale {
        private Display display;

        public Sale(Display display) {
            this.display = display;
        }

        public void onBarcode(String barcode) {
            if ("".equals(barcode)) {
                display.setText("Scanning error: empty barcode");
            } else {
                // introduce look up table
                final Map<String, String> pricesByBarcode = new HashMap<String, String> () {{
                    put("12345", "$7.95");
                    put("23456", "$12.50");
                }};
                if ("12345".equals(barcode)) {
                    display.setText(pricesByBarcode.get(barcode));
                } else if ("23456".equals(barcode)) {
                    display.setText(pricesByBarcode.get(barcode));
                } else {
                    display.setText("Product not found for " +
                            barcode);
                }
            }
        }
    }
}

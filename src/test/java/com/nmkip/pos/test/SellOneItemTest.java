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
    public void setUp() {
        display = new Display();
        sale = new Sale(display, new Catalog(new HashMap<String, String>() {{
            put("12345", "$7.95");
            put("23456", "$12.50");
        }}));
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
        final Sale sale = new Sale(display, new Catalog(null));

        sale.onBarcode("");

        assertEquals("Scanning error: empty barcode", display.getText());
    }

    public static class Display {

        private String text;

        public String getText() {
            return text;
        }

        public void displayPrice(String priceAsText) {
            this.text = priceAsText;
        }

        public void displayProductNotFoundMessage(String barcode) {
            this.text = "Product not found for " +
                    barcode;
        }

        public void displayEmptyBarcodeMessage() {
            this.text = "Scanning error: empty barcode";
        }
    }

    public static class Sale {
        private Display display;
        private Catalog catalog;

        public Sale(Display display, Catalog catalog) {
            this.display = display;
            this.catalog = catalog;
        }

        public void onBarcode(String barcode) {
            // SMELL Refused bequest; move this up the call stack?
            if ("".equals(barcode)) {
                display.displayEmptyBarcodeMessage();
                return;
            }

            final String priceAsText = catalog.findProduct(barcode);
            if (priceAsText == null) {
                display.displayProductNotFoundMessage(barcode);
            } else {
                display.displayPrice(priceAsText);
            }

        }

    }

    public static class Catalog {
        private final Map<String, String> pricesByBarcode;

        public Catalog(Map<String, String> pricesByBarcode) {
            this.pricesByBarcode = pricesByBarcode;
        }

        public String findProduct(String barcode) {
            return pricesByBarcode.get(barcode);
        }
    }
}
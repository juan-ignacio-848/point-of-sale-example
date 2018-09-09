package com.nmkip.pos.test;

import org.junit.Before;
import org.junit.Test;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class SellOneItemTest {

    private Display display;
    private Sale sale;

    @Before
    public void setUp() {
        display = new Display();
        sale = new Sale(display, new Catalog(new HashMap<String, Double>() {{
            put("12345", 7.95);
            put("23456", 12.50);
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
        private final NumberFormat currencyFormatter;

        public Display() {
            currencyFormatter = NumberFormat.getCurrencyInstance();
        }

        public String getText() {
            return text;
        }

        public void displayPrice(Double price) {
            this.text = currencyFormatter.format(price);
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

            final Double price = catalog.findPrice(barcode);
            if (price == null) {
                display.displayProductNotFoundMessage(barcode);
            } else {
                display.displayPrice(price);
            }

        }

    }

    public static class Catalog {
        private final Map<String, Double> pricesByBarcode;

        public Catalog(Map<String, Double> pricesByBarcode) {
            this.pricesByBarcode = pricesByBarcode;
        }

        public Double findPrice(String barcode) {
            return pricesByBarcode.get(barcode);
        }
    }
}
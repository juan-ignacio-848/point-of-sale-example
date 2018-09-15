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
        sale = new Sale(display, new Catalog(new HashMap<Barcode, Double>() {{
            put(new Barcode("12345"), 7.95);
            put(new Barcode("23456"), 12.50);
        }}));
    }

    @Test
    public void productFound() {
        sale.onBarcode(new Barcode("12345"));

        assertEquals("$7.95", display.getText());
    }

    @Test
    public void anotherPorudctFound() {
        sale.onBarcode(new Barcode("23456"));

        assertEquals("$12.50", display.getText());
    }

    @Test
    public void productNotFound() {
        sale.onBarcode(new Barcode("99999"));

        assertEquals("Product not found for 99999", display.getText());
    }

    @Test
    public void emptyBarcode() {
        final Sale sale = new Sale(display, new Catalog(null));

        sale.onBarcode(new Barcode(""));

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

        public void displayProductNotFoundMessage(Barcode barcode) {
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

        public void onBarcode(Barcode barcode) {
            // SMELL Refused bequest; move this up the call stack?
            if (barcode.isEmpty()) {
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
        private Map<Barcode, Double> pricesByBarcode;

        public Catalog(Map<Barcode, Double> pricesByBarcode) {
            this.pricesByBarcode = pricesByBarcode;
        }

        public Double findPrice(Barcode barcode) {
            return pricesByBarcode.get(barcode);
        }
    }

    public static class Barcode {
        private String code;

        public Barcode(String code) {
            this.code = code;
        }

        public boolean isEmpty() {
            return code.isEmpty();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Barcode barcode = (Barcode) o;

            return code != null ? code.equals(barcode.code) : barcode.code == null;
        }

        @Override
        public int hashCode() {
            return code != null ? code.hashCode() : 0;
        }

        @Override
        public String toString() {
            return code;
        }
    }
}
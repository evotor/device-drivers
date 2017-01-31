package ru.evotor.devices.drivers.priceprinter;

public interface IPricePrinter {

    void beforePrintPrices();

    void printPrice(String name, String price, String barcode, String code);

    void afterPrintPrices();
}

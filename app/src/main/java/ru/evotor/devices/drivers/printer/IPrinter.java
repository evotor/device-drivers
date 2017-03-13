package ru.evotor.devices.drivers.printer;

import ru.evotor.devices.commons.printer.PrinterDocument;

public interface IPrinter {

    int getAllowableSymbolsLineLength();

    int getAllowablePixelLineLength();

    void printDocument(PrinterDocument printerDocument);


}

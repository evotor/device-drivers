package ru.evotor.devices.drivers.printer;

import android.graphics.Bitmap;

import ru.evotor.devices.commons.printer.PrinterDocument;
import ru.evotor.devices.commons.printer.printable.Barcode;

public interface IPrinter {

    int getAllowableSymbolsLineLength();

    int getAllowablePixelLineLength();

    void printDocument(PrinterDocument printerDocument);


}

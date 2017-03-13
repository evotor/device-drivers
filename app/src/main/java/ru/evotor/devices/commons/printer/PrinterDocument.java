package ru.evotor.devices.commons.printer;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import ru.evotor.devices.commons.printer.printable.IPrintable;

@Getter
public class PrinterDocument implements Parcelable {

    /**
     * массив объектов для печати
     */
    private final IPrintable[] printables;

    public PrinterDocument(IPrintable... printables) {
        this.printables = printables;
    }

    private PrinterDocument(Parcel parcel) {
        printables = (IPrintable[]) parcel.readParcelableArray(IPrintable.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelableArray(printables, 0);
    }

    public static final Creator<PrinterDocument> CREATOR = new Creator<PrinterDocument>() {

        public PrinterDocument createFromParcel(Parcel in) {
            return new PrinterDocument(in);
        }

        public PrinterDocument[] newArray(int size) {
            return new PrinterDocument[size];
        }
    };

}

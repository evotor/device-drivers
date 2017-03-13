package ru.evotor.devices.commons.printer.printable;

import android.os.Parcel;

import lombok.Getter;

@Getter
public class Barcode implements IPrintable {

    /**
     * Значение штрихкода
     */
    private final String barcodeValue;

    /**
     * Тип штрихкода
     */
    private final BarcodeType barcodeType;

    public Barcode(String barcodeValue, BarcodeType barcodeType) {
        this.barcodeValue = barcodeValue;
        this.barcodeType = barcodeType;
    }

    private Barcode(Parcel parcel) {
        barcodeValue = parcel.readString();
        int barcodeTypeNumber = parcel.readInt();
        barcodeType = BarcodeType.values()[barcodeTypeNumber < BarcodeType.values().length ? barcodeTypeNumber : 0];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(barcodeValue);
        parcel.writeInt(barcodeType.ordinal());
    }

    public static final Creator<Barcode> CREATOR = new Creator<Barcode>() {

        public Barcode createFromParcel(Parcel in) {
            return new Barcode(in);
        }

        public Barcode[] newArray(int size) {
            return new Barcode[size];
        }
    };

    public enum BarcodeType {
        EAN8,
        UPCA,
        EAN13,
        CODE39
    }
}

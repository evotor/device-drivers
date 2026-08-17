package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;

// Новые значения добавлять только в конец
public enum CashlessMethod {

    UNKNOWN,
    QR,
    BIOMETRY,
    CARD,
    INTERNET_ACQUIRING,
    BANK_TRANSFER,
    BLUETOOTH,
    SBP_QR,
    DIGITAL_RUB;

    public static CashlessMethod readFrom(Parcel parcel) {
        int cashlessOrdinal = parcel.readInt();
        CashlessMethod method;
        if (cashlessOrdinal >= CashlessMethod.values().length || cashlessOrdinal < 0) {
            method = CashlessMethod.UNKNOWN;
        } else {
            method = CashlessMethod.values()[cashlessOrdinal];
        }
        return method;
    }

    public void writeTo(Parcel parcel) {
        parcel.writeInt(ordinal());
    }
}

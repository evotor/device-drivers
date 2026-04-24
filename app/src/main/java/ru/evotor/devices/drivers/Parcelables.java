package ru.evotor.devices.drivers;

import android.os.Parcel;
import android.os.Parcelable;

/* package */ class Parcelables {

    public static void writeAliased(Parcel p, Parcelable value, int flags) {
        p.writeParcelable(value, flags);
    }

}

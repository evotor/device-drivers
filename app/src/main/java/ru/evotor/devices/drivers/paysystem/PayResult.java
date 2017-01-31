package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;

@Getter
public class PayResult implements Parcelable {

    /**
     * ррн проведённой операции
     */
    private final String rrn;

    /**
     * количество строк банковского чека для печати
     */
    private final int slipLength;

    /**
     * строки банковского чека для печати
     */
    private final String[] slip;

    public PayResult(String rrn, String[] slip) {
        this.rrn = rrn;
        this.slip = slip;
        if (this.slip == null) {
            slipLength = 0;
        } else {
            slipLength = slip.length;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(rrn);
        parcel.writeInt(slip == null ? 0 : slip.length);
        parcel.writeStringArray(slip);
    }

    public static final Creator<PayResult> CREATOR = new Creator<PayResult>() {

        public PayResult createFromParcel(Parcel in) {
            return new PayResult(in);
        }

        public PayResult[] newArray(int size) {
            return new PayResult[size];
        }
    };

    private PayResult(Parcel parcel) {
        rrn = parcel.readString();
        slipLength = parcel.readInt();
        slip = new String[slipLength];
        if (slipLength > 0) {
            parcel.readStringArray(slip);
        }
    }

}

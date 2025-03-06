package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import ru.evotor.devices.drivers.ParcelableUtils;

public class AdditionalTransactionData implements Parcelable {

    private static final int VERSION = 1;

    @NotNull
    private final String tid;

    @Nullable
    private final String inn;

    @NotNull
    private final String primaryAccountNumber;

    @NotNull
    private final String issuerBik;

    @NotNull
    private final String issuerTransactionNumber;

    public AdditionalTransactionData(
            @NotNull String tid,
            @Nullable String inn,
            @NotNull String primaryAccountNumber,
            @NotNull String issuerBik,
            @NotNull String issuerTransactionNumber
    ) {
        this.tid = tid;
        this.inn = inn;
        this.primaryAccountNumber = primaryAccountNumber;
        this.issuerBik = issuerBik;
        this.issuerTransactionNumber = issuerTransactionNumber;
    }

    public String getTid() {
        return tid;
    }

    public String getInn() {
        return inn;
    }

    public String getPrimaryAccountNumber() {
        return primaryAccountNumber;
    }

    public String getIssuerBik() {
        return issuerBik;
    }

    public String getIssuerTransactionNumber() {
        return issuerTransactionNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(tid);
        dest.writeString(inn);
        dest.writeString(primaryAccountNumber);
        dest.writeString(issuerBik);
        dest.writeString(issuerTransactionNumber);
        ParcelableUtils.writeExpand(dest, VERSION, parcel -> {
            if (VERSION >= 1) {
                // nothing
            }
        });
    }

    public static final Creator<AdditionalTransactionData> CREATOR = new Creator<AdditionalTransactionData>() {

        public AdditionalTransactionData createFromParcel(Parcel in) {
            return new AdditionalTransactionData(in);
        }

        public AdditionalTransactionData[] newArray(int size) {
            return new AdditionalTransactionData[size];
        }
    };

    private AdditionalTransactionData(Parcel parcel) {
        tid = Objects.requireNonNull(parcel.readString());
        inn = parcel.readString();
        primaryAccountNumber = Objects.requireNonNull(parcel.readString());
        issuerBik = Objects.requireNonNull(parcel.readString());
        issuerTransactionNumber = Objects.requireNonNull(parcel.readString());

        ParcelableUtils.readExpand(parcel, VERSION, (parcel1, currentVersion) -> {
            if (currentVersion >= 1) {
                // nothing
            }
        });
    }

}

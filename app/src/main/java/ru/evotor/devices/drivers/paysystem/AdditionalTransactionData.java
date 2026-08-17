package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import ru.evotor.devices.drivers.ParcelableUtils;

@SuppressWarnings("unused")
public record AdditionalTransactionData(
        @Nullable String tid,
        long initialDatetime,
        @NotNull String paymentSystemCode,
        @NotNull String acquiringBankCode,
        @NotNull String authorizationCode,
        @Nullable String transactionId
) implements Parcelable {

    private static final int VERSION = 2;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        // fields from version 1, backward compatibility
        dest.writeString(""); // fake tid
        dest.writeString(""); // fake inn
        dest.writeString(""); // fake primaryAccountNumber
        dest.writeString(""); // fake issuerBik
        dest.writeString(""); // fake issuerTransactionNumber
        ParcelableUtils.writeExpand(dest, VERSION, () -> {
            dest.writeString(tid);
            dest.writeLong(initialDatetime);
            dest.writeString(paymentSystemCode);
            dest.writeString(acquiringBankCode);
            dest.writeString(authorizationCode);
            dest.writeString(transactionId);
        });
    }

    public static final Creator<AdditionalTransactionData> CREATOR = new Creator<>() {

        public AdditionalTransactionData createFromParcel(Parcel in) {
            // fields from version 1, backward compatibility
            in.readString(); // fake tid
            in.readString(); // fake inn
            in.readString(); // fake primaryAccountNumber
            in.readString(); // fake issuerBik
            in.readString(); // fake issuerTransactionNumber
            return ParcelableUtils.readExpandData(in, (version) -> {
                if (version >= 2) {
                    return new AdditionalTransactionData(
                            in.readString(),
                            in.readLong(),
                            Objects.requireNonNull(in.readString()),
                            Objects.requireNonNull(in.readString()),
                            Objects.requireNonNull(in.readString()),
                            in.readString()
                    );
                } else {
                    // return null for version 1
                    return null;
                }
            });
        }

        public AdditionalTransactionData[] newArray(int size) {
            return new AdditionalTransactionData[size];
        }
    };

}

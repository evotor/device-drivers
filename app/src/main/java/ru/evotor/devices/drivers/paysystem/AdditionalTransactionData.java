package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import ru.evotor.devices.drivers.ParcelableUtils;

public class AdditionalTransactionData implements Parcelable {

    private static final int VERSION = 2;

    @Nullable
    private final String tid;

    private final long initialDatetime;

    @NotNull
    private final String paymentSystemCode;

    @NotNull
    private final String acquiringBankCode;

    @NotNull
    private final String authorizationCode;

    @Nullable
    private final String transactionId;

    public AdditionalTransactionData(
            @Nullable String tid,
            long initialDatetime,
            @NotNull String paymentSystemCode,
            @NotNull String acquiringBankCode,
            @NotNull String authorizationCode,
            @Nullable String transactionId
    ) {
        this.tid = tid;
        this.initialDatetime = initialDatetime;
        this.paymentSystemCode = paymentSystemCode;
        this.acquiringBankCode = acquiringBankCode;
        this.authorizationCode = authorizationCode;
        this.transactionId = transactionId;
    }

    public String getTid() {
        return tid;
    }

    public long getInitialDatetime() {
        return initialDatetime;
    }

    public String getPaymentSystemCode() {
        return paymentSystemCode;
    }

    public String getAcquiringBankCode() {
        return acquiringBankCode;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(""); // fake tid
        dest.writeString(""); // fake inn
        dest.writeString(""); // fake primaryAccountNumber
        dest.writeString(""); // fake issuerBik
        dest.writeString(""); // fake issuerTransactionNumber
        ParcelableUtils.writeExpand(dest, VERSION, parcel -> {
            dest.writeString(tid);
            dest.writeLong(initialDatetime);
            dest.writeString(paymentSystemCode);
            dest.writeString(acquiringBankCode);
            dest.writeString(authorizationCode);
            dest.writeString(transactionId);
        });
    }

    public static final Creator<AdditionalTransactionData> CREATOR = new Creator<AdditionalTransactionData>() {

        public AdditionalTransactionData createFromParcel(Parcel in) {
            return create(in);
        }

        public AdditionalTransactionData[] newArray(int size) {
            return new AdditionalTransactionData[size];
        }
    };

    private static AdditionalTransactionData create(Parcel parcel) {
        return ParcelableUtils.readExpandData(parcel, VERSION, (parcel1, currentVersion) -> {
            if (currentVersion >= 2) {
                return new AdditionalTransactionData(
                        parcel1.readString(),
                        parcel1.readLong(),
                        Objects.requireNonNull(parcel1.readString()),
                        Objects.requireNonNull(parcel1.readString()),
                        Objects.requireNonNull(parcel1.readString()),
                        parcel1.readString()
                );
            } else {
                return null;
            }
        });
    }
}

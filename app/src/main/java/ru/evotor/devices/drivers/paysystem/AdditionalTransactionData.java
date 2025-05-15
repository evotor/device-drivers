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
    private String tid;

    private long initialDatetime;

    @NotNull
    private String paymentSystemCode;

    @NotNull
    private String acquiringBankCode;

    @NotNull
    private String authorizationCode;

    @Nullable
    private String transactionId;

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
            return new AdditionalTransactionData(in);
        }

        public AdditionalTransactionData[] newArray(int size) {
            return new AdditionalTransactionData[size];
        }
    };

    private AdditionalTransactionData(Parcel parcel) {
        ParcelableUtils.readExpand(parcel, VERSION, (parcel1, currentVersion) -> {
            if (currentVersion >= 2) {
                tid = parcel1.readString();
                initialDatetime = parcel1.readLong();
                paymentSystemCode = Objects.requireNonNull(parcel1.readString());
                acquiringBankCode = Objects.requireNonNull(parcel1.readString());
                authorizationCode = Objects.requireNonNull(parcel1.readString());
                transactionId = parcel1.readString();
            }
        });
    }

}

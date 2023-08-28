package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Date;

import ru.evotor.devices.drivers.ParcelableUtils;

public class PaymentRequest implements Parcelable {
    private static final int VERSION = 1;
    /**
     * Идентификатор устройства
     */
    private final int instanceId;

    /**
     * Сумма
     */
    private final BigDecimal sum;

    /**
     * Дата, до которой актуален запрос
     * Может быть null
     */
    private final Date expiredAt;

    /**
     * Дополнительное описание операции
     * Может быть null
     */
    private final String additionalDescription;

    public PaymentRequest(
            int instanceId,
            BigDecimal sum,
            Date expiredAt,
            String additionalDescription
    ) {
        this.instanceId = instanceId;
        this.sum = sum;
        this.expiredAt = expiredAt;
        this.additionalDescription = additionalDescription;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public String getAdditionalDescription() {
        return additionalDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        ParcelableUtils.writeExpand(parcel, VERSION, parcel1 -> {
            if (VERSION >= 1) {
                parcel1.writeInt(instanceId);
                parcel1.writeString(sum.toPlainString());
                parcel1.writeSerializable(expiredAt);
                parcel1.writeString(additionalDescription);
            }
        });
    }

    private static PaymentRequest create(Parcel parcel) {
        return ParcelableUtils.readExpand(
                parcel,
                VERSION,
                (parcel1, currentVersion) -> new PaymentRequest(
                        parcel1.readInt(),
                        new BigDecimal(parcel1.readString()),
                        (Date) parcel1.readSerializable(),
                        parcel1.readString()
                ));
    }

    public static final Creator<PaymentRequest> CREATOR = new Creator<PaymentRequest>() {

        public PaymentRequest createFromParcel(Parcel in) {
            return create(in);
        }

        public PaymentRequest[] newArray(int size) {
            return new PaymentRequest[size];
        }
    };

}

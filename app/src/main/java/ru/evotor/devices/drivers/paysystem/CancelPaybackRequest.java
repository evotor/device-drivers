package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Date;

import ru.evotor.devices.drivers.ParcelableUtils;

public class CancelPaybackRequest implements Parcelable {
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

    /**
     * RRN
     */
    private final String rrn;

    public CancelPaybackRequest(
            int instanceId,
            BigDecimal sum,
            Date expiredAt,
            String additionalDescription,
            String rrn
    ) {
        this.instanceId = instanceId;
        this.sum = sum;
        this.expiredAt = expiredAt;
        this.additionalDescription = additionalDescription;
        this.rrn = rrn;
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

    public String getRrn() {
        return rrn;
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
                parcel1.writeString(rrn);
            }
        });
    }

    private static CancelPaybackRequest create(Parcel parcel) {
        return ParcelableUtils.readExpandData(
                parcel,
                VERSION,
                (parcel1, currentVersion) -> new CancelPaybackRequest(
                        parcel1.readInt(),
                        new BigDecimal(parcel1.readString()),
                        (Date) parcel1.readSerializable(),
                        parcel1.readString(),
                        parcel1.readString()
                ));
    }

    public static final Creator<CancelPaybackRequest> CREATOR = new Creator<CancelPaybackRequest>() {

        public CancelPaybackRequest createFromParcel(Parcel in) {
            return create(in);
        }

        public CancelPaybackRequest[] newArray(int size) {
            return new CancelPaybackRequest[size];
        }
    };

}

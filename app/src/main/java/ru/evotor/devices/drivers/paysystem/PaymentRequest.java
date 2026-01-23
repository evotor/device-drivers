package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Date;

import ru.evotor.devices.drivers.ParcelableUtils;

public class PaymentRequest implements Parcelable {
    private static final int VERSION = 2;
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

    // VERSION = 2
    /**
     * id платёжной сессии для подтверждения платежа в состоянии NEED_CONFIRMATION
     */
    private final String paymentSessionId;
    /**
     * Id примененной карты лояльности
     */
    private final String loyaltyCardId;
    /**
     * Json, содержащий дополнительные данные о примененной лояльности
     */
    private final String additionalLoyaltyData;

    // Используйте конструктор
    // public PaymentRequest(
    //            int instanceId,
    //            BigDecimal sum,
    //            Date expiredAt,
    //            String additionalDescription,
    //            String paymentSessionId,
    //            String loyaltyCardId,
    //            BigDecimal earnedBonus,
    //            BigDecimal spentBonus
    //)
    @Deprecated
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
        this.paymentSessionId = null;
        this.loyaltyCardId = null;
        this.additionalLoyaltyData = null;
    }

    public PaymentRequest(
            int instanceId,
            BigDecimal sum,
            Date expiredAt,
            String additionalDescription,
            String paymentSessionId,
            String loyaltyCardId,
            String additionalLoyaltyData
    ) {
        this.instanceId = instanceId;
        this.sum = sum;
        this.expiredAt = expiredAt;
        this.additionalDescription = additionalDescription;
        this.paymentSessionId = paymentSessionId;
        this.loyaltyCardId = loyaltyCardId;
        this.additionalLoyaltyData = additionalLoyaltyData;
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

    public String getPaymentSessionId() {
        return paymentSessionId;
    }

    public String getLoyaltyCardId() {
        return loyaltyCardId;
    }
    public String getAdditionalLoyaltyData() {
        return additionalLoyaltyData;
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
            if (VERSION >= 2) {
                parcel1.writeString(paymentSessionId);
                parcel1.writeString(loyaltyCardId);
                parcel1.writeString(additionalLoyaltyData);
            }
        });
    }

    private static PaymentRequest create(Parcel parcel) {
        return ParcelableUtils.readExpandData(
                parcel,
                VERSION,
                (parcel1, version) ->
                        new PaymentRequest(
                                parcel1.readInt(),
                                new BigDecimal(parcel1.readString()),
                                (Date) parcel1.readSerializable(),
                                parcel1.readString(),
                                version >= 2 ? parcel1.readString() : null,
                                version >= 2 ? parcel1.readString() : null,
                                version >= 2 ? parcel1.readString() : null
                        )
        );
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

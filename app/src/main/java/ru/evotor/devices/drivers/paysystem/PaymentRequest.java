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
    private int instanceId;

    /**
     * Сумма
     */
    private BigDecimal sum;

    /**
     * Дата, до которой актуален запрос
     * Может быть null
     */
    private Date expiredAt;

    /**
     * Дополнительное описание операции
     * Может быть null
     */
    private String additionalDescription;

    // VERSION = 2
    /**
     * id платёжной сессии для подтверждения платежа в состоянии NEED_CONFIRMATION
     */
    private String paymentSessionId = null;
    /**
     * id карты лояльности для передачи его в фабрику СБЕРа (только для платежа в состоянии NEED_CONFIRMATION)
     */
    private String loyaltyCardId = null;
    /**
     * Сумма начисленных бонусов для отображения ее экране пинпада
     */
    private BigDecimal earnedBonus = BigDecimal.ZERO;
    /**
     * Сумма потраченных бонусов для отображения ее экране пинпада
     */
    private BigDecimal spentBonus = BigDecimal.ZERO;

    // Используйте конструктор
    // public PaymentRequest(
    //            int instanceId,
    //            BigDecimal sum,
    //            Date expiredAt,
    //            String additionalDescription,
    //            @Nullable String paymentSessionId
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
    }

    public PaymentRequest(
            int instanceId,
            BigDecimal sum,
            Date expiredAt,
            String additionalDescription,
            String paymentSessionId,
            String loyaltyCardId,
            BigDecimal earnedBonus,
            BigDecimal spentBonus
    ) {
        this.instanceId = instanceId;
        this.sum = sum;
        this.expiredAt = expiredAt;
        this.additionalDescription = additionalDescription;
        this.paymentSessionId = paymentSessionId;
        this.loyaltyCardId = loyaltyCardId;
        this.earnedBonus = earnedBonus;
        this.spentBonus = spentBonus;
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

    public BigDecimal getEarnedBonus() {
        return earnedBonus;
    }

    public BigDecimal getSpentBonus() {
        return spentBonus;
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
                parcel1.writeString(earnedBonus.toPlainString());
                parcel1.writeString(spentBonus.toPlainString());
            }
        });
    }

    private PaymentRequest(Parcel parcel) {
        ParcelableUtils.readExpand(parcel, VERSION, (parcel1, currentVersion) -> {
            instanceId = parcel1.readInt();
            sum = new BigDecimal(parcel1.readString());
            expiredAt = (Date) parcel1.readSerializable();
            additionalDescription = parcel1.readString();
            if (currentVersion >= 2) {
                paymentSessionId = parcel1.readString();
                loyaltyCardId = parcel1.readString();
                earnedBonus = new BigDecimal(parcel1.readString());
                spentBonus = new BigDecimal(parcel1.readString());
            }
        });
    }

    public static final Creator<PaymentRequest> CREATOR = new Creator<PaymentRequest>() {

        public PaymentRequest createFromParcel(Parcel in) {
            return new PaymentRequest(in);
        }

        public PaymentRequest[] newArray(int size) {
            return new PaymentRequest[size];
        }
    };

}

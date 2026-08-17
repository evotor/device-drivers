package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;
import android.os.Parcelable;

import ru.evotor.devices.drivers.ParcelableUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @param instanceId            Идентификатор устройства
 * @param sum                   Сумма
 * @param expiredAt             Дата, до которой актуален запрос
 *                              Может быть null
 * @param additionalDescription Дополнительное описание операции
 *                              Может быть null
 * @param paymentSessionId      id платёжной сессии для подтверждения платежа в состоянии NEED_CONFIRMATION
 * @param loyaltyCardId         Id примененной карты лояльности
 * @param additionalLoyaltyData Json, содержащий дополнительные данные о примененной лояльности
 * @param transactionId         Внешний идентификатор транзакции
 * @param tipsCode              Код сотрудника для чаевых
 */
public record PaymentRequest(int instanceId,
                             @NotNull BigDecimal sum,
                             @Nullable Date expiredAt,
                             @Nullable String additionalDescription,
                             @Nullable String paymentSessionId,
                             @Nullable String loyaltyCardId,
                             @Nullable String additionalLoyaltyData,
                             @Nullable String transactionId,
                             @Nullable String tipsCode) implements Parcelable {
    private static final int VERSION = 3;

    @SuppressWarnings("deprecation")
    @Deprecated(since = "VERSION 2")
    public PaymentRequest(int instanceId, @NotNull BigDecimal sum, @Nullable Date expiredAt,
                          @Nullable String additionalDescription) {
        this(instanceId, sum, expiredAt, additionalDescription,
                null, null, null);
    }

    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated(since = "VERSION 3")
    public PaymentRequest(int instanceId, @NotNull BigDecimal sum, @Nullable Date expiredAt,
                          @Nullable String additionalDescription, @Nullable String paymentSessionId,
                          @Nullable String loyaltyCardId, @Nullable String additionalLoyaltyData) {
        this(instanceId, sum, expiredAt, additionalDescription, paymentSessionId, loyaltyCardId,
                additionalLoyaltyData, null, null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    void writeTo(@NotNull Parcel parcel, int version) {
        ParcelableUtils.writeExpand(parcel, version, () -> {
            if (version >= 1) {
                parcel.writeInt(instanceId);
                parcel.writeString(sum.toPlainString());
                parcel.writeSerializable(expiredAt);
                parcel.writeString(additionalDescription);
            }
            if (version >= 2) {
                parcel.writeString(paymentSessionId);
                parcel.writeString(loyaltyCardId);
                parcel.writeString(additionalLoyaltyData);
            }
            if (version >= 3) {
                parcel.writeString(transactionId);
                parcel.writeString(tipsCode);
            }
        });
    }

    @Override
    public void writeToParcel(@NotNull Parcel parcel, int i) {
        writeTo(parcel, VERSION);
    }

    public static final Creator<PaymentRequest> CREATOR = new Creator<>() {

        public PaymentRequest createFromParcel(Parcel in) {
            return ParcelableUtils.readExpandData(in,
                    ( v) -> new PaymentRequest(
                            in.readInt(),
                            new BigDecimal(in.readString()),
                            (Date) in.readSerializable(),
                            in.readString(),
                            v >= 2 ? in.readString() : null,
                            v >= 2 ? in.readString() : null,
                            v >= 2 ? in.readString() : null,
                            v >= 3 ? in.readString() : null,
                            v >= 3 ? in.readString() : null
                    )
            );
        }

        public PaymentRequest[] newArray(int size) {
            return new PaymentRequest[size];
        }
    };

}

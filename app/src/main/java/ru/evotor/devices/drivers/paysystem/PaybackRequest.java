package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import ru.evotor.devices.drivers.ParcelableUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @param instanceId            Идентификатор устройства
 * @param sum                   Сумма
 * @param expiredAt             Дата, до которой актуален запрос
 *                              Может быть null
 * @param additionalDescription Дополнительное описание операции
 *                              Может быть null
 * @param rrn                   RRN
 * @param transactionId         Внешний идентификатор транзакции
 */
@SuppressWarnings("SameParameterValue")
public record PaybackRequest(
        int instanceId,
        @NotNull BigDecimal sum,
        @Nullable Date expiredAt,
        @Nullable String additionalDescription,
        @NotNull String rrn,
        @Nullable String transactionId
) implements Parcelable {
    private static final int VERSION = 2;

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
                parcel.writeString(rrn);
            }
            if (version >= 2) {
                parcel.writeString(transactionId);
            }
        });
    }

    @Override
    public void writeToParcel(@NotNull Parcel parcel, int i) {
        writeTo(parcel, VERSION);
    }
    
    public static final Creator<PaybackRequest> CREATOR = new Creator<>() {

        public PaybackRequest createFromParcel(Parcel in) {
            return ParcelableUtils.readExpandData(in,
                    (currentVersion) -> new PaybackRequest(
                            in.readInt(),
                            new BigDecimal(in.readString()),
                            (Date) in.readSerializable(),
                            in.readString(),
                            Objects.requireNonNull(in.readString()),
                            currentVersion >= 2 ? in.readString() : null
                    )
            );
        }

        public PaybackRequest[] newArray(int size) {
            return new PaybackRequest[size];
        }
    };

}

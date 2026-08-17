package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Date;

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
 */
@SuppressWarnings("SameParameterValue")
public record PayoutRequest(int instanceId,
                            @NotNull BigDecimal sum,
                            @Nullable Date expiredAt,
                            @Nullable String additionalDescription) implements Parcelable {
    private static final int VERSION = 1;

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
        });
    }

    @Override
    public void writeToParcel(@NotNull Parcel parcel, int i) {
        writeTo(parcel, VERSION);
    }

    public static final Creator<PayoutRequest> CREATOR = new Creator<>() {

        public PayoutRequest createFromParcel(Parcel in) {
            return ParcelableUtils.readExpandData(
                    in,
                    (currentVersion) -> new PayoutRequest(
                            in.readInt(),
                            new BigDecimal(in.readString()),
                            (Date) in.readSerializable(),
                            in.readString()
                    ));
        }

        public PayoutRequest[] newArray(int size) {
            return new PayoutRequest[size];
        }
    };

}

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
 */
@SuppressWarnings("SameParameterValue")
public record CancelPayoutRequest(
        int instanceId,
        @NotNull BigDecimal sum,
        @Nullable Date expiredAt,
        @Nullable String additionalDescription,
        @NotNull String rrn
) implements Parcelable {
    private static final int VERSION = 1;

    @Override
    public int describeContents() {
        return 0;
    }

    void writeTo(Parcel parcel, int version) {
        ParcelableUtils.writeExpand(parcel, version, () -> {
            if (version >= 1) {
                parcel.writeInt(instanceId);
                parcel.writeString(sum.toPlainString());
                parcel.writeSerializable(expiredAt);
                parcel.writeString(additionalDescription);
                parcel.writeString(rrn);
            }
        });
    }

    @Override
    public void writeToParcel(@NotNull Parcel parcel, int i) {
        writeTo(parcel, VERSION);
    }

    public static final Creator<CancelPayoutRequest> CREATOR = new Creator<>() {

        public CancelPayoutRequest createFromParcel(Parcel in) {
            return ParcelableUtils.readExpandData(
                    in,
                    ( currentVersion) -> new CancelPayoutRequest(
                            in.readInt(),
                            new BigDecimal(in.readString()),
                            (Date) in.readSerializable(),
                            in.readString(),
                            Objects.requireNonNull(in.readString())
                    ));
        }

        public CancelPayoutRequest[] newArray(int size) {
            return new CancelPayoutRequest[size];
        }
    };

}

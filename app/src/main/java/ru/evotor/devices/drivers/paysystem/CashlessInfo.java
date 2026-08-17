package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;
import android.os.Parcelable;

import ru.evotor.devices.drivers.ParcelableUtils;
import ru.evotor.devices.drivers.UuidValidationUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record CashlessInfo(
        @NotNull CashlessMethod method,
        @NotNull String description,
        @NotNull String uuid
) implements Parcelable {

    private static final int VERSION = 1;

    public CashlessInfo {
        UuidValidationUtils.checkUuid(uuid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NotNull Parcel dest, int flags) {
        method.writeTo(dest);
        dest.writeString(description);
        dest.writeString(uuid);
        ParcelableUtils.writeExpand(dest, VERSION, () -> {
            if (VERSION >= 1) {
                // nothing
            }
        });
    }

    public static final Creator<CashlessInfo> CREATOR = new Creator<>() {

        public CashlessInfo createFromParcel(Parcel in) {
            CashlessMethod method = CashlessMethod.readFrom(in);
            String description = Objects.requireNonNull(in.readString());
            String uuid = Objects.requireNonNull(in.readString());
            return ParcelableUtils.readExpandData(in, (version) -> {
                if (version >= 1) {
                    // do nothing
                }
                return new CashlessInfo(method, description, uuid);
            });
        }

        public CashlessInfo[] newArray(int size) {
            return new CashlessInfo[size];
        }
    };

}

package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import ru.evotor.devices.drivers.ParcelableUtils;

public class CashlessInfo implements Parcelable {

    private static final int VERSION = 1;

    @NotNull
    private final CashlessMethod method;

    @NotNull
    private final String description;

    @NotNull
    private final String uuid;

    @NotNull
    public CashlessMethod getMethod() {
        return method;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    @NotNull
    public String getUuid() {
        return uuid;
    }

    public CashlessInfo(@NotNull CashlessMethod method, @NotNull String description, @NotNull String uuid) {
        this.method = method;
        this.description = description;
        this.uuid = uuid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(method.ordinal());
        dest.writeString(description);
        dest.writeString(uuid);
        ParcelableUtils.writeExpand(dest, VERSION, parcel -> {
            if (VERSION >= 1) {
                // nothing
            }
        });
    }

    public static final Creator<CashlessInfo> CREATOR = new Creator<CashlessInfo>() {

        public CashlessInfo createFromParcel(Parcel in) {
            return new CashlessInfo(in);
        }

        public CashlessInfo[] newArray(int size) {
            return new CashlessInfo[size];
        }
    };

    private CashlessInfo(Parcel parcel) {
        int cashlessOrdinal = parcel.readInt();
        if (cashlessOrdinal >= CashlessMethod.values().length) {
            method = CashlessMethod.UNKNOWN;
        } else {
            method = CashlessMethod.values()[cashlessOrdinal];
        }
        description = Objects.requireNonNull(parcel.readString());
        uuid = Objects.requireNonNull(parcel.readString());

        ParcelableUtils.readExpand(parcel, VERSION, (parcel1, currentVersion) -> {
            if (currentVersion >= 1) {
                // nothing
            }
        });
    }
}

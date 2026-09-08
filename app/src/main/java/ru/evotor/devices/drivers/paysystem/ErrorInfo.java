package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;
import android.os.Parcelable;

import ru.evotor.devices.drivers.ParcelableUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record ErrorInfo(@NotNull String title, @NotNull String message) implements Parcelable {

    private static final int VERSION = 1;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NotNull Parcel dest, int flags) {
        ParcelableUtils.writeExpand(dest, VERSION, () -> {
            if (VERSION >= 1) {
                dest.writeString(title);
                dest.writeString(message);
            }
        });
    }

    public static final Creator<ErrorInfo> CREATOR = new Creator<>() {

        public ErrorInfo createFromParcel(Parcel in) {
            return ParcelableUtils.readExpandData(in, (version) -> {
                if (version >= 1) {
                    String title = Objects.requireNonNull(in.readString());
                    String message = Objects.requireNonNull(in.readString());
                    return new ErrorInfo(title, message);
                } else {
                    return null;
                }
            });
        }

        public ErrorInfo[] newArray(int size) {
            return new ErrorInfo[size];
        }
    };
}

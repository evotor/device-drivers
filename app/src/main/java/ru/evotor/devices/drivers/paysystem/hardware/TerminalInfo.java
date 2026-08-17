package ru.evotor.devices.drivers.paysystem.hardware;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.evotor.devices.drivers.ParcelableUtils;

public record TerminalInfo(
        @Nullable String terminalId,
        @Nullable String additionalJsonData
) implements Parcelable {
    private static final int VERSION = 1;

    @Override
    public void writeToParcel(@NotNull Parcel dest, int flags) {
        ParcelableUtils.writeExpand(dest, VERSION, () -> {
            dest.writeString(terminalId);
            dest.writeString(additionalJsonData);
        });
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TerminalInfo> CREATOR = new Creator<>() {
        @Override
        public TerminalInfo createFromParcel(Parcel in) {
            return ParcelableUtils.readExpandData(in, (v) -> new TerminalInfo(
                    in.readString(),
                    in.readString()
            ));
        }

        @Override
        public TerminalInfo[] newArray(int size) {
            return new TerminalInfo[size];
        }
    };
}

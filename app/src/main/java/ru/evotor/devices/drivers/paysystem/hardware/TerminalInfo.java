package ru.evotor.devices.drivers.paysystem.hardware;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.Nullable;

import ru.evotor.devices.drivers.ParcelableUtils;

public class TerminalInfo implements Parcelable {
    @Nullable
    private final String terminalId;

    @Nullable
    private final String additionalJsonData;

    public TerminalInfo(@Nullable String terminalId, @Nullable String additionalJsonData) {
        this.terminalId = terminalId;
        this.additionalJsonData = additionalJsonData;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelableUtils.writeExpand(dest, 1, parcel -> {
            parcel.writeString(terminalId);
            parcel.writeString(additionalJsonData);
        });

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TerminalInfo> CREATOR = new Creator<TerminalInfo>() {
        @Override
        public TerminalInfo createFromParcel(Parcel in) {
            return ParcelableUtils.readExpandData(in, 1, (p, v) -> {
                if (v == 1) {
                    String terminalId = in.readString();
                    String additionalJsonData = in.readString();
                    return new TerminalInfo(terminalId, additionalJsonData);
                }
                throw new IllegalStateException("not yet implemented read code for version " + v);
            });
        }

        @Override
        public TerminalInfo[] newArray(int size) {
            return new TerminalInfo[size];
        }
    };
}

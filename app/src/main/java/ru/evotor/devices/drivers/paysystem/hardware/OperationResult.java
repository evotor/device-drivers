package ru.evotor.devices.drivers.paysystem.hardware;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import ru.evotor.devices.drivers.ParcelableUtils;

@SuppressWarnings("unused")
public record OperationResult(
        int resultCode,
        @Nullable String rrn,
        @Nullable String additionalJsonData,
        @Nullable List<String> slip
) implements Parcelable {

    private static final int VERSION = 1;

    @Override
    public String rrn() {
        return rrn;
    }

    @Override
    public String additionalJsonData() {
        return additionalJsonData;
    }

    @Override
    public List<String> slip() {
        return slip;
    }

    public static final int RESULT_CODE_TIMEOUT = 0x80000001;
    public static final int RESULT_CODE_CANCELLED = 0x80000002;
    public static final int RESULT_CODE_UNKNOWN_ERROR = 0x80000003;

    @Override
    public void writeToParcel(@NotNull Parcel dest, int flags) {
        ParcelableUtils.writeExpand(dest, VERSION, () -> {
            dest.writeInt(resultCode);
            dest.writeString(rrn);
            dest.writeString(additionalJsonData);
            dest.writeStringList(slip);
        });

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OperationResult> CREATOR = new Creator<>() {
        @Override
        public OperationResult createFromParcel(Parcel in) {
            return ParcelableUtils.readExpandData(in, (v) -> new OperationResult(
                    in.readInt(),
                    in.readString(),
                    in.readString(),
                    in.createStringArrayList()));
        }

        @Override
        public OperationResult[] newArray(int size) {
            return new OperationResult[size];
        }
    };
}

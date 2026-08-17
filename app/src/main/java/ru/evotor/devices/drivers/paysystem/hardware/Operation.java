package ru.evotor.devices.drivers.paysystem.hardware;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.evotor.devices.drivers.ParcelableUtils;

@SuppressWarnings("unused")
public record Operation(
        int status,
        @Nullable OperationResult result
) implements Parcelable {

    private static final int VERSION = 1;

    public static final Creator<Operation> CREATOR = new Creator<>() {
        @Override
        public Operation createFromParcel(Parcel in) {
            return ParcelableUtils.readExpandData(in, (v) -> new Operation(
                    in.readInt(),
                    ParcelableUtils.readParcelable(in, OperationResult.CREATOR)));
        }

        @Override
        public Operation[] newArray(int size) {
            return new Operation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NotNull Parcel dest, int flags) {
        ParcelableUtils.writeExpand(dest, VERSION, () -> {
            dest.writeInt(status);
            ParcelableUtils.writeParcelable(dest, result, flags);
        });
    }

    public static final int STATUS_NOT_FOUND = 0;
    public static final int STATUS_NEW = 1;
    public static final int STATUS_WAIT_CARD = 2;
    public static final int STATUS_WAIT_PIN = 3;
    public static final int STATUS_WAIT_NETWORK = 4;
    public static final int STATUS_CANCEL_PLANNED = 5;
    public static final int STATUS_CANCELLED = 6;
    public static final int STATUS_FINISHED = 7;

}

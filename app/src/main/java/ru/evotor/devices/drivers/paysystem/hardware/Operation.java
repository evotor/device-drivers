package ru.evotor.devices.drivers.paysystem.hardware;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.Nullable;

import ru.evotor.devices.drivers.ParcelableUtils;

public class Operation implements Parcelable {
    private final int status;
    @Nullable
    private final OperationResult result;

    public Operation(int status, @Nullable OperationResult result) {
        this.status = status;
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    @Nullable
    public OperationResult getResult() {
        return result;
    }

    public static final Creator<Operation> CREATOR = new Creator<Operation>() {
        @Override
        public Operation createFromParcel(Parcel in) {
            return ParcelableUtils.readExpandData(in, 1, (p, v) -> {
                if (v == 1) {
                    int status = p.readInt();
                    OperationResult result = in.readParcelable(OperationResult.class.getClassLoader());
                    return new Operation(status, result);
                }
                throw new IllegalStateException("not yet implemented for version " + v);
            });
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
    public void writeToParcel(Parcel dest, int flags) {
        ParcelableUtils.writeExpand(dest, 1, (p) -> {
            p.writeInt(status);
            p.writeParcelable(result, flags);
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

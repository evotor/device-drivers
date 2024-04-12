package ru.evotor.devices.drivers.paysystem.hardware;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.evotor.devices.drivers.ParcelableUtils;

public class OperationResult implements Parcelable {

    private final int resultCode;
    @Nullable
    private final String rrn;
    @Nullable
    private final String additionalJsonData;
    @Nullable
    private final List<String> slip;

    public int getResultCode() {
        return resultCode;
    }

    public String getRrn() {
        return rrn;
    }

    public String getAdditionalJsonData() {
        return additionalJsonData;
    }

    public List<String> getSlip() {
        return slip;
    }

    public OperationResult(int resultCode, @Nullable String rrn, @Nullable String additionalJsonData, @Nullable List<String> slip) {
        this.resultCode = resultCode;
        this.rrn = rrn;
        this.additionalJsonData = additionalJsonData;
        this.slip = slip;
    }

    public static final int RESULT_CODE_TIMEOUT = 0x80000001;
    public static final int RESULT_CODE_CANCELLED = 0x80000002;
    public static final int RESULT_CODE_UNKNOWN_ERROR = 0x80000003;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelableUtils.writeExpand(dest, 1, p -> {
            p.writeInt(resultCode);
            p.writeString(rrn);
            p.writeString(additionalJsonData);
            p.writeStringList(slip);
        });

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OperationResult> CREATOR = new Creator<OperationResult>() {
        @Override
        public OperationResult createFromParcel(Parcel in) {
            return ParcelableUtils.readExpandData(in, 1, (p, v) -> {
                int resultCode = p.readInt();
                String rrn = p.readString();
                String additionalJsonData = p.readString();
                List<String> slip = p.createStringArrayList();
                return new OperationResult(resultCode, rrn, additionalJsonData, slip);
            });

        }

        @Override
        public OperationResult[] newArray(int size) {
            return new OperationResult[size];
        }
    };
}

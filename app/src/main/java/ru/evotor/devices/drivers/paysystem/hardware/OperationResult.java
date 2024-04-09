package ru.evotor.devices.drivers.paysystem.hardware;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.evotor.devices.drivers.ParcelableUtils;

public class OperationResult implements Parcelable {
    public static final Creator<OperationResult> CREATOR = new Creator<OperationResult>() {
        @Override
        public OperationResult createFromParcel(Parcel in) {
            return ParcelableUtils.readExpandData(in, 1, (parcel, currentVersion) -> {
                if (currentVersion <= 1) {
                    int type = in.readInt();
                    switch (type) {
                        case 0:
                            return OperationResult.NOT_FOUND;
                        case 1:
                            return OperationResult.IN_PROGRESS;
                        case 2:
                            return OperationResult.CANCEL_PLANNED;
                        case 3:
                        case 4:
                            return read(parcel, currentVersion, type == 3);
                        default:
                            throw new IllegalStateException("not yet implemented for type " + type);
                    }
                }
                throw new IllegalStateException("not yet implemented for version " + currentVersion);
            });
        }

        @Override
        public OperationResult[] newArray(int size) {
            return new OperationResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private static void write(Finished finished, Parcel dest, int currentVersion) {
        if (currentVersion >= 1) {
            dest.writeInt(finished.resultCode);
            dest.writeString(finished.rrn);
            dest.writeString(finished.additionalJsonData);
            List<String> slip = finished.slip;
            if (slip == null) {
                dest.writeInt(-1);
            } else {
                dest.writeInt(slip.size());
                for (String s : slip) {
                    dest.writeString(s);
                }
            }
        }
    }

    private static Finished read(Parcel in, int currentVersion, boolean isCancelled) {
        if (currentVersion <= 1) {
            int resultCode = in.readInt();
            String rrn = in.readString();
            String additionalJsonData = in.readString();
            List<String> slip;
            int slipSize = in.readInt();
            if (slipSize == -1) {
                slip = null;
            } else {
                slip = new ArrayList<>();
                for (int i = 0; i < slipSize; i++) {
                    slip.add(in.readString());
                }
            }
            return isCancelled ?
                    new Cancelled(resultCode, rrn, additionalJsonData, slip) :
                    new Finished(resultCode, rrn, additionalJsonData, slip);
        }
        throw new IllegalStateException("not yet implemented for version " + currentVersion);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int version = 1;
        ParcelableUtils.writeExpand(dest, version, parcel -> {
            if (this == NOT_FOUND) {
                parcel.writeInt(0);
            } else if (this == IN_PROGRESS) {
                parcel.writeInt(1);
            } else if (this == CANCEL_PLANNED) {
                parcel.writeInt(2);
            } else if (this instanceof Cancelled) {
                parcel.writeInt(3);
                write((Cancelled) this, parcel, version);
            } else if (this instanceof Finished) {
                parcel.writeInt(4);
                write((Finished) this, parcel, version);
            } else {
                parcel.writeInt(-1);
            }
        });
    }

    public static class Finished extends OperationResult {
        private final int resultCode;
        @Nullable
        private final String rrn;
        @Nullable
        private final String additionalJsonData;
        @Nullable
        private final List<String> slip;

        public Finished(
                int resultCode,
                @Nullable String rrn,
                @Nullable String additionalJsonData,
                @Nullable List<String> slip
        ) {
            this.resultCode = resultCode;
            this.rrn = rrn;
            this.additionalJsonData = additionalJsonData;
            this.slip = slip;
        }

        public int getResultCode() {
            return resultCode;
        }

        @Nullable
        public String getRRN() {
            return rrn;
        }

        @Nullable
        public String getAdditionalJsonData() {
            return additionalJsonData;
        }

        @Nullable
        public Collection<String> getSlip() {
            return slip;
        }
    }

    public static class Cancelled extends Finished {
        public Cancelled(
                int resultCode,
                @Nullable String rrn,
                @Nullable String additionalJsonData,
                @Nullable List<String> slip
        ) {
            super(resultCode, rrn, additionalJsonData, slip);
        }
    }

    public static final OperationResult IN_PROGRESS = new OperationResult();
    public static final OperationResult NOT_FOUND = new OperationResult();
    public static final OperationResult CANCEL_PLANNED = new OperationResult();


    public static final int RESULT_CODE_TIMEOUT = 0x80000001;
    public static final int RESULT_CODE_CANCELLED = 0x80000002;
}

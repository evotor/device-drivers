package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ReportType(
        @Nullable WrappedReportType wrappedReportType
) implements Parcelable {

    public static final Creator<ReportType> CREATOR = new Creator<>() {
        @Override
        public ReportType createFromParcel(Parcel in) {
            boolean isNull = in.readInt() == 0;
            if (isNull) {
                return new ReportType(null);
            }
            WrappedReportType wrapped = null;
            try {
                wrapped = WrappedReportType.values()[in.readInt()];
            } catch (Throwable ignore) {
            }
            return new ReportType(wrapped);
        }

        @Override
        public ReportType[] newArray(int size) {
            return new ReportType[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NotNull Parcel dest, int flags) {
        if (wrappedReportType != null) {
            dest.writeInt(1);
            dest.writeInt(wrappedReportType.ordinal());
        } else {
            dest.writeInt(0);
        }
    }

    public enum WrappedReportType {
        CONTROL_TAPE,
        SUMMARY_CHECK,
        HELP_REPORT
    }
}

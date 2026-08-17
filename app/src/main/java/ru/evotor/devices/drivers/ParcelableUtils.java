package ru.evotor.devices.drivers;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableUtils {

    /**
     * Magic number для идентификации использования версионирования объекта
     */
    private static int MAGIC_NUMBER = 8800;

    public static void writeExpand(Parcel p, int version, ParcelableWriter writer) {

        p.writeInt(MAGIC_NUMBER);
        p.writeInt(version);
        // Determine position in parcel for writing data size
        final int dataSizePosition = p.dataPosition();
        // Use integer placeholder for additional data size
        p.writeInt(0);
        //Determine position of data start
        final int startDataPosition = p.dataPosition();

        //Write additional data
        writer.write();

        // Calculate additional data size
        final int dataSize = p.dataPosition() - startDataPosition;
        // Save position at the end of data
        final int endOfDataPosition = p.dataPosition();
        //Set position to start to write additional data size
        p.setDataPosition(dataSizePosition);
        p.writeInt(dataSize);
        // Go back to the end of parcel
        p.setDataPosition(endOfDataPosition);

    }

    public static void readExpand(Parcel p, ParcelableReader reader) {
        readExpandData(p, (ParcelableDataReader<Void>) (version) -> {
            if (reader != null) {
                reader.read(version);
            }
            return null;
        });
    }

    /**
     * @param p       parcel
     * @param reader  объект, который будет вызван, если есть дополнительные данные.
     *                Если дополнительных данных нет, то объект вызван не будет!
     * @param <R>     возвращаемое значение
     * @return null если дополнительных данных нет или результат вызова reader, если дополнительные данные есть
     */
    public static <R> R readExpandData(Parcel p, ParcelableDataReader<R> reader) {

        final int startReadingPosition = p.dataPosition();

        // Check if available data size is more than integer size and versioning is supported
        if (p.dataAvail() <= 4 || p.readInt() != MAGIC_NUMBER) {
            // Versioning is not supported return pointer to start position and end reading
            p.setDataPosition(startReadingPosition);
            return null;
        }
        //Read object version
        final int currentVersion = p.readInt();
        final int dataSize = p.readInt();
        final int startDataPosition = p.dataPosition();

        R r = reader.read(currentVersion);
        p.setDataPosition(startDataPosition + dataSize);
        return r;
    }

    public interface ParcelableWriter {
        void write();
    }

    public interface ParcelableReader {
        void read(int version);
    }

    public interface ParcelableDataReader<R> {
        R read(int version);
    }

    public static void writeParcelable(Parcel parcel, Parcelable parcelable, int flags) {
        Parcelables.writeAliased(parcel, parcelable, flags);
    }

    public static <T extends Parcelable> T readParcelable(Parcel p, Parcelable.Creator<T> creator) {
        String ignoredClass = p.readString();
        if (ignoredClass == null) return null;
        return creator.createFromParcel(p);
    }

}

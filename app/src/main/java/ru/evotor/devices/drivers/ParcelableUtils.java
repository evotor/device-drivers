package ru.evotor.devices.drivers;

import android.os.Parcel;

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
        writer.write(p);

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

    public static void readExpand(Parcel p, int version, ParcelableReader reader) {

        final int startReadingPosition = p.dataPosition();

        // Check if available data size is more than integer size and versioning is supported
        if (p.dataAvail() <= 4 || p.readInt() != MAGIC_NUMBER) {
            // Versioning is not supported return pointer to start position and end reading
            p.setDataPosition(startReadingPosition);
            return;
        }
        //Read object version
        final int currentVersion = p.readInt();
        final int dataSize = p.readInt();
        final int startDataPosition = p.dataPosition();

        reader.read(p, currentVersion);
        if (currentVersion > version) {
            p.setDataPosition(startDataPosition + dataSize);
        }

    }

    public interface ParcelableWriter {
        void write(Parcel parcel);
    }

    public interface ParcelableReader {
        void read(Parcel parcel, int currentVersion);
    }

}

package ru.evotor.devices.drivers.scales;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(suppressConstructorProperties = true)
public class Weight implements Parcelable {

    // вес, возвращённый весами
    private final double originalWeight;
    // множитель, на который надо домножить оригинальный вес, чтобы получить вес в граммах
    private final double multiplierToGrams;
    // поддерживали ли весы флаг стабильности при последнем взвешивании
    private final boolean supportStable;
    // было ли последнее взвешивание стабильным
    private final boolean stable;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(originalWeight);
        parcel.writeDouble(multiplierToGrams);
        parcel.writeInt(supportStable ? 1 : 0);
        parcel.writeInt(stable ? 1 : 0);
    }

    public static final Creator<Weight> CREATOR = new Creator<Weight>() {

        public Weight createFromParcel(Parcel in) {
            return new Weight(in);
        }

        public Weight[] newArray(int size) {
            return new Weight[size];
        }
    };

    private Weight(Parcel parcel) {
        originalWeight = parcel.readDouble();
        multiplierToGrams = parcel.readDouble();
        supportStable = parcel.readInt() == 1;
        stable = parcel.readInt() == 1;
    }

}

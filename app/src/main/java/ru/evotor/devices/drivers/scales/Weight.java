package ru.evotor.devices.drivers.scales;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class Weight implements Parcelable {

    /**
     * вес, возвращённый весами
     */
    private final BigDecimal originalScalesWeight;

    /**
     * множитель, на который надо домножить оригинальный вес, чтобы получить вес в граммах
     */
    private final BigDecimal multiplierWeightToGrams;

    /**
     * поддерживали ли весы флаг стабильности при последнем взвешивании
     */
    private final boolean supportStable;

    /**
     * было ли последнее взвешивание стабильным
     */
    private final boolean stable;

    /**
     * @deprecated следует использовать  {@link #Weight(BigDecimal, BigDecimal, boolean, boolean)}
     */
    @Deprecated
    public Weight(double originalWeight, double multiplierToGrams, boolean supportStable, boolean stable) {
        this.supportStable = supportStable;
        this.stable = stable;
        this.originalScalesWeight = new BigDecimal(originalWeight);
        this.multiplierWeightToGrams = new BigDecimal(multiplierToGrams);
    }

    public Weight(BigDecimal originalScalesWeight, BigDecimal multiplierWeightToGrams, boolean supportStable, boolean stable) {
        this.originalScalesWeight = originalScalesWeight;
        this.multiplierWeightToGrams = multiplierWeightToGrams;
        this.supportStable = supportStable;
        this.stable = stable;
    }

    private Weight(Parcel parcel) {
        double originalWeight = parcel.readDouble();
        double multiplierToGrams = parcel.readDouble();
        supportStable = parcel.readInt() == 1;
        stable = parcel.readInt() == 1;
        BigDecimal tmpOriginalScalesWeight = (BigDecimal) parcel.readSerializable();
        BigDecimal tmpMultiplierWeightToGrams = (BigDecimal) parcel.readSerializable();
        if (tmpOriginalScalesWeight == null && tmpMultiplierWeightToGrams == null) {// support deprecated double originalWeight & multiplierToGrams
            originalScalesWeight = new BigDecimal(originalWeight);
            multiplierWeightToGrams = new BigDecimal(multiplierToGrams);
        } else {
            originalScalesWeight = tmpOriginalScalesWeight;
            multiplierWeightToGrams = tmpMultiplierWeightToGrams;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(0);// support deprecated double originalWeight & multiplierToGrams
        parcel.writeDouble(0);// support deprecated double originalWeight & multiplierToGrams
        parcel.writeInt(supportStable ? 1 : 0);
        parcel.writeInt(stable ? 1 : 0);
        parcel.writeSerializable(originalScalesWeight);
        parcel.writeSerializable(multiplierWeightToGrams);
    }

    public static final Creator<Weight> CREATOR = new Creator<Weight>() {

        public Weight createFromParcel(Parcel in) {
            return new Weight(in);
        }

        public Weight[] newArray(int size) {
            return new Weight[size];
        }
    };

}

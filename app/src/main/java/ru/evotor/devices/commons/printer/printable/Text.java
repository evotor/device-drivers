package ru.evotor.devices.commons.printer.printable;

import android.os.Parcel;

import lombok.Getter;

@Getter
public class Text implements IPrintable {

    /**
     * текст для печати
     */
    String text;

    public Text(String text) {
        this.text = text;
    }

    private Text(Parcel parcel) {
        text = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
    }

    public static final Creator<Text> CREATOR = new Creator<Text>() {

        public Text createFromParcel(Parcel in) {
            return new Text(in);
        }

        public Text[] newArray(int size) {
            return new Text[size];
        }
    };

}

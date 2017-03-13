package ru.evotor.devices.commons.printer.printable;

import android.graphics.Bitmap;
import android.os.Parcel;

import lombok.Getter;

@Getter
public class Image implements IPrintable {

    /**
     * картинка для печати
     */
    Bitmap bitmap;

    public Image(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private Image(Parcel parcel) {
        bitmap = parcel.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(bitmap, 0);
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {

        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

}

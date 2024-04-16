package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;
import android.os.Parcelable;

import ru.evotor.devices.drivers.ParcelableUtils;

public class PayResult implements Parcelable {

    private static final String RESULT_CODE_SUCCESS = "0";
    private static int VERSION = 3;

    /**
     * ррн проведённой операции
     */
    private final String rrn;

    /**
     * количество строк банковского чека для печати
     */
    private final int slipLength;

    /**
     * строки банковского чека для печати
     */
    private final String[] slip;

    // VERSION == 2
    /**
     * Код ответа.
     * Обычно при успешных транзакциях оплат/возвратов передаётся "00" или "000"
     * Может содержать не цифровые символы, например "Z3", в случае неуспешной транзакции
     * Если операция завершилась неуспешно, и slip != null или slip не пустой, печатаем slip.
     * Если операция завершилась неуспешно, и slip == null или slip пустой - не печатаем ничего.
     * Если драйвер вернул PayResult == null - не печатаем ничего!
     */
    private String resultCode = RESULT_CODE_SUCCESS;

    // VERSION == 3
    /**
     * json расширенного банковского чека для печати
     */
    private String extendedSlip = null;

    // Используйте конструктор PayResult(String resultCode, String rrn, String[] slip)
    @Deprecated
    public PayResult(String rrn, String[] slip) {
        this(RESULT_CODE_SUCCESS, rrn, slip, null);
    }

    public PayResult(String resultCode, String rrn, String[] slip) {
        this(resultCode, rrn, slip, null);
    }

    public PayResult(String resultCode, String rrn, String[] slip, String extendedSlip) {
        this.resultCode = resultCode;
        this.rrn = rrn;
        this.slip = slip;
        if (this.slip == null) {
            slipLength = 0;
        } else {
            slipLength = slip.length;
        }
        this.extendedSlip = extendedSlip;
    }

    public String getRrn() {
        return rrn;
    }

    public int getSlipLength() {
        return slipLength;
    }

    public String[] getSlip() {
        return slip;
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getExtendedSlip() { return extendedSlip; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(rrn);
        parcel.writeInt(slip == null ? 0 : slip.length);
        parcel.writeStringArray(slip);

        ParcelableUtils.writeExpand(parcel, VERSION, new ParcelableUtils.ParcelableWriter() {
            @Override
            public void write(Parcel parcel) {
                if (VERSION >= 2) {
                    parcel.writeString(resultCode);
                }
                if (VERSION >= 3) {
                    parcel.writeString(extendedSlip);
                }
            }
        });
    }

    public static final Creator<PayResult> CREATOR = new Creator<PayResult>() {

        public PayResult createFromParcel(Parcel in) {
            return new PayResult(in);
        }

        public PayResult[] newArray(int size) {
            return new PayResult[size];
        }
    };

    private PayResult(Parcel parcel) {
        rrn = parcel.readString();
        slipLength = parcel.readInt();
        int len = parcel.readInt();
        if (len != -1) {
            slip = new String[slipLength];
            for (int i = 0; i < len; i++) {
                slip[i] = parcel.readString();
            }
        } else {
            slip = null;
        }

        ParcelableUtils.readExpand(parcel, VERSION, (parcel1, currentVersion) -> {
            if (currentVersion >= 2) {
                resultCode = parcel1.readString();
            }
            if (currentVersion >= 3) {
                extendedSlip = parcel1.readString();
            }
        });
    }

}

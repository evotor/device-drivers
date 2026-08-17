package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;
import android.os.Parcelable;

import ru.evotor.devices.drivers.Constants;
import ru.evotor.devices.drivers.ParcelableUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"unused", "SameParameterValue"})
public class PayResult implements Parcelable {

    private static final String RESULT_CODE_SUCCESS = "0";
    private static final int VERSION = 8;

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
    @Nullable
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
    @Nullable
    private String extendedSlip = null;

    // VERSION == 4
    /**
     * расширенная информация о способе безналичной оплаты
     */
    @Nullable
    private CashlessInfo cashlessInfo = null;

    // VERSION == 5
    /**
     * расширенная информация о транзакции
     */
    @Nullable
    private AdditionalTransactionData additionalTransactionData = null;

    // VERSION == 6
    @Nullable
    private String maskedPan;

    private CardType cardType;

    @Nullable
    private String stan;

    @Nullable
    private String authCode;

    // VERSION == 7
    /**
     * состояние платежа
     */
    @Nullable
    private Constants.PaymentState paymentState = null;
    /**
     * id платёжной сессии, который надо будет передать при втором вызове оплаты для подтверждения платежа.
     */
    @Nullable
    private String paymentSessionId = null;
    /**
     * Внешний id карты лояльности
     */
    @Nullable
    private String loyaltyCardId = null;

    // VERSION == 8
    @Nullable
    private String terminalId = null;

    private boolean isOwn = false;

    @SuppressWarnings("deprecation")
    @Deprecated
    public PayResult(String rrn, String[] slip) {
        this(RESULT_CODE_SUCCESS, rrn, slip);
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public PayResult(String resultCode, String rrn, String[] slip) {
        this(resultCode, rrn, slip, null);
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public PayResult(String resultCode, String rrn, String[] slip, String extendedSlip) {
        this(resultCode, rrn, slip, extendedSlip, null);
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public PayResult(
            String resultCode,
            String rrn,
            String[] slip,
            @Nullable String extendedSlip,
            @Nullable CashlessInfo cashlessInfo
    ) {
        this(resultCode, rrn, slip, extendedSlip, cashlessInfo, null, "", CardType.UNKNOWN, "", "");
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public PayResult(
            String resultCode,
            String rrn,
            String[] slip,
            @Nullable String extendedSlip,
            @Nullable CashlessInfo cashlessInfo,
            @Nullable AdditionalTransactionData additionalTransactionData,
            @Nullable String maskedPan,
            @Nullable CardType cardType,
            @Nullable String stan,
            @Nullable String authCode
    ) {
        this(resultCode, rrn, slip, extendedSlip, cashlessInfo, additionalTransactionData, maskedPan, cardType, stan, authCode, null, null, null);
    }

    @Deprecated
    public PayResult(
            String resultCode,
            String rrn,
            String[] slip,
            @Nullable String extendedSlip,
            @Nullable CashlessInfo cashlessInfo,
            @Nullable AdditionalTransactionData additionalTransactionData,
            @Nullable String maskedPan,
            @Nullable CardType cardType,
            @Nullable String stan,
            @Nullable String authCode,
            @Nullable Constants.PaymentState paymentState,
            @Nullable String paymentSessionId,
            @Nullable String loyaltyCardId
    ) {
        this(resultCode, rrn, slip, extendedSlip, cashlessInfo, additionalTransactionData, maskedPan, cardType, stan, authCode, paymentState, paymentSessionId, loyaltyCardId, null, false);
    }

    public PayResult(
            String resultCode,
            String rrn,
            @Nullable String[] slip,
            @Nullable String extendedSlip,
            @Nullable CashlessInfo cashlessInfo,
            @Nullable AdditionalTransactionData additionalTransactionData,
            @Nullable String maskedPan,
            @Nullable CardType cardType,
            @Nullable String stan,
            @Nullable String authCode,
            @Nullable Constants.PaymentState paymentState,
            @Nullable String paymentSessionId,
            @Nullable String loyaltyCardId,
            @Nullable String terminalId,
            boolean isOwn
    ) {
        this.resultCode = resultCode;
        this.rrn = rrn;
        this.slip = slip;
        if (this.slip == null) {
            slipLength = 0;
        } else {
            slipLength = slip.length;
        }
        this.extendedSlip = extendedSlip;
        this.cashlessInfo = cashlessInfo;
        this.additionalTransactionData = additionalTransactionData;
        this.maskedPan = maskedPan;
        this.cardType = cardType != null ? cardType : CardType.UNKNOWN;
        this.stan = stan;
        this.authCode = authCode;
        this.paymentState = paymentState;
        this.paymentSessionId = paymentSessionId;
        this.loyaltyCardId = loyaltyCardId;
        this.terminalId = terminalId;
        this.isOwn = isOwn;
    }


    public String getRrn() {
        return rrn;
    }

    public int getSlipLength() {
        return slipLength;
    }

    @Nullable
    public String[] getSlip() {
        return slip;
    }

    public String getResultCode() {
        return resultCode;
    }

    @Nullable
    public String getExtendedSlip() {
        return extendedSlip;
    }

    @Nullable
    public String getAuthCode() {
        return authCode;
    }

    @Nullable
    public CardType getCardType() {
        return cardType;
    }

    @Nullable
    public String getMaskedPan() {
        return maskedPan;
    }

    @Nullable
    public String getStan() {
        return stan;
    }

    @Nullable
    public CashlessInfo getCashlessInfo() {
        return cashlessInfo;
    }

    @Nullable
    public AdditionalTransactionData getAdditionalTransactionData() {
        return additionalTransactionData;
    }

    @Nullable
    public Constants.PaymentState getPaymentState() {
        return paymentState;
    }

    @Nullable
    public String getPaymentSessionId() {
        return paymentSessionId;
    }

    @Nullable
    public String getLoyaltyCardId() {
        return loyaltyCardId;
    }

    @Nullable
    public String getTerminalId() {
        return terminalId;
    }

    public boolean isOwn() {
        return isOwn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    void writeTo(Parcel parcel, int version) {
        parcel.writeString(rrn);
        parcel.writeInt(slip == null ? 0 : slip.length);
        parcel.writeStringArray(slip);

        ParcelableUtils.writeExpand(parcel, version, () -> {
            if (version >= 2) {
                parcel.writeString(resultCode);
            }
            if (version >= 3) {
                parcel.writeString(extendedSlip);
            }
            if (version >= 4) {
                ParcelableUtils.writeParcelable(parcel, cashlessInfo, 0);
            }
            if (version >= 5) {
                ParcelableUtils.writeParcelable(parcel, additionalTransactionData, 0);
            }
            if (version >= 6) {
                parcel.writeString(maskedPan);
                parcel.writeString(cardType.card);
                parcel.writeString(stan);
                parcel.writeString(authCode);
            }
            if (version >= 7) {
                if (paymentState != null) {
                    parcel.writeString(paymentState.name());
                } else {
                    parcel.writeString(null);
                }
                parcel.writeString(paymentSessionId);
                parcel.writeString(loyaltyCardId);
            }
            if (version >= 8) {
                parcel.writeString(terminalId);
                parcel.writeInt(isOwn ? 1 : 0);
            }
        });
    }

    @Override
    public void writeToParcel(@NotNull Parcel parcel, int i) {
        writeTo(parcel, VERSION);
    }

    public static final Creator<PayResult> CREATOR = new Creator<>() {

        public PayResult createFromParcel(Parcel in) {
            return new PayResult(in);
        }

        public PayResult[] newArray(int size) {
            return new PayResult[size];
        }
    };

    PayResult(Parcel parcel) {
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

        ParcelableUtils.readExpand(parcel, (version) -> {
            if (version >= 2) {
                resultCode = parcel.readString();
            }
            if (version >= 3) {
                extendedSlip = parcel.readString();
            }
            if (version >= 4) {
                cashlessInfo = ParcelableUtils.readParcelable(parcel, CashlessInfo.CREATOR);
            }
            if (version >= 5) {
                additionalTransactionData = ParcelableUtils.readParcelable(parcel, AdditionalTransactionData.CREATOR);
            }
            if (version >= 6) {
                maskedPan = parcel.readString();
                cardType = CardType.fromName(parcel.readString(), CardType.UNKNOWN);
                stan = parcel.readString();
                authCode = parcel.readString();
            }
            if (version >= 7) {
                String paymentStateName = parcel.readString();
                if (paymentStateName != null) {
                    try {
                        paymentState = Constants.PaymentState.valueOf(paymentStateName);
                    } catch (IllegalArgumentException e) {
                        paymentState = null;
                    }
                } else {
                    paymentState = null;
                }
                paymentSessionId = parcel.readString();
                loyaltyCardId = parcel.readString();
            }
            if (version >= 8) {
                terminalId = parcel.readString();
                isOwn = parcel.readInt() != 0;
            }
        });
    }

}

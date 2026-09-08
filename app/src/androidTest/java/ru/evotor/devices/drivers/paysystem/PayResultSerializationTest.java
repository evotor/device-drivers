package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import ru.evotor.devices.drivers.Constants;
import ru.evotor.devices.drivers.ParcelableUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class PayResultSerializationTest {

    private static final long DATETIME_MILLIS = 1784194344425L;

    private PayResult createResult() {
        return new PayResult(
                "00",
                "123456789",
                new String[]{"line1", "line2"},
                "extendedSlip",
                new CashlessInfo(CashlessMethod.CARD, "card payment", "00000000-0000-0000-0000-000000000000"),
                new AdditionalTransactionData("tid", DATETIME_MILLIS, "VISA", "BANK", "AUTH12", "tx123"),
                "4111******5678",
                CardType.VISA,
                "654321",
                "AUTH12",
                Constants.PaymentState.NEED_CONFIRMATION,
                "sessionId",
                "loyaltyCardId",
                "terminalId",
                true,
                new Date(DATETIME_MILLIS),
                new ErrorInfo("error title", "error message")
        );
    }

    @Test
    public void serializeV9_deserializeV8() {
        PayResult original = createResult();
        byte[] data = serialize(original, 9);

        Parcel parcel = parcelFrom(data);
        try {
            V8Holder h = readV8(parcel);

            assertEquals(original.getRrn(), h.rrn);
            assertEquals(original.getSlipLength(), h.slipLength);
            assertArrayEquals(original.getSlip(), h.slip);
            assertEquals(original.getResultCode(), h.resultCode);
            assertEquals(original.getExtendedSlip(), h.extendedSlip);
            assertEquals(original.getCashlessInfo(), h.cashlessInfo);
            assertEquals(original.getAdditionalTransactionData(), h.additionalTransactionData);
            assertEquals(original.getMaskedPan(), h.maskedPan);
            assertEquals(original.getCardType(), h.cardType);
            assertEquals(original.getStan(), h.stan);
            assertEquals(original.getAuthCode(), h.authCode);
            assertEquals(original.getPaymentState(), h.paymentState);
            assertEquals(original.getPaymentSessionId(), h.paymentSessionId);
            assertEquals(original.getLoyaltyCardId(), h.loyaltyCardId);
            assertEquals(original.getTerminalId(), h.terminalId);
            assertEquals(original.isOwn(), h.isOwn);

            assertNull(h.datetime);
            assertNull(h.errorInfo);

            // v8-читатель не должен "споткнуться" о v9-данные: они пропускаются по размеру
            assertEquals(parcel.dataSize(), parcel.dataPosition());
        } finally {
            parcel.recycle();
        }
    }

    @Test
    public void serializeV9_deserializeV9() {
        PayResult original = createResult();
        byte[] data = serialize(original, 9);

        Parcel p = parcelFrom(data);
        try {
            PayResult restored = PayResult.CREATOR.createFromParcel(p);

            assertEquals(original.getDatetime(), restored.getDatetime());
            assertEquals(original.getError(), restored.getError());
            assertEquals(original.getRrn(), restored.getRrn());
            assertArrayEquals(original.getSlip(), restored.getSlip());
            assertEquals(original.getResultCode(), restored.getResultCode());
            assertEquals(original.getTerminalId(), restored.getTerminalId());
            assertEquals(original.isOwn(), restored.isOwn());
        } finally {
            p.recycle();
        }
    }

    @Test
    public void serializeV8_deserializeV9() {
        PayResult original = createResult();
        byte[] data = serialize(original, 8);

        Parcel p = parcelFrom(data);
        try {
            PayResult restored = PayResult.CREATOR.createFromParcel(p);

            assertNull(restored.getDatetime());
            assertNull(restored.getError());
            assertEquals(original.getRrn(), restored.getRrn());
            assertArrayEquals(original.getSlip(), restored.getSlip());
            assertEquals(original.getResultCode(), restored.getResultCode());
            assertEquals(original.getExtendedSlip(), restored.getExtendedSlip());
            assertEquals(original.getCashlessInfo(), restored.getCashlessInfo());
            assertEquals(original.getAdditionalTransactionData(), restored.getAdditionalTransactionData());
            assertEquals(original.getMaskedPan(), restored.getMaskedPan());
            assertEquals(original.getCardType(), restored.getCardType());
            assertEquals(original.getStan(), restored.getStan());
            assertEquals(original.getAuthCode(), restored.getAuthCode());
            assertEquals(original.getPaymentState(), restored.getPaymentState());
            assertEquals(original.getPaymentSessionId(), restored.getPaymentSessionId());
            assertEquals(original.getLoyaltyCardId(), restored.getLoyaltyCardId());
            assertEquals(original.getTerminalId(), restored.getTerminalId());
            assertEquals(original.isOwn(), restored.isOwn());
        } finally {
            p.recycle();
        }
    }

    private byte[] serialize(PayResult result, int version) {
        Parcel p = Parcel.obtain();
        try {
            result.writeTo(p, version);
            p.setDataPosition(0);
            return p.marshall();
        } finally {
            p.recycle();
        }
    }

    private Parcel parcelFrom(byte[] data) {
        Parcel p = Parcel.obtain();
        p.unmarshall(data, 0, data.length);
        p.setDataPosition(0);
        return p;
    }

    private V8Holder readV8(Parcel parcel) {
        V8Holder h = new V8Holder();
        h.rrn = parcel.readString();
        h.slipLength = parcel.readInt();
        int len = parcel.readInt();
        if (len != -1) {
            h.slip = new String[h.slipLength];
            for (int i = 0; i < len; i++) {
                h.slip[i] = parcel.readString();
            }
        }
        ParcelableUtils.readExpand(parcel, (version) -> {
            if (version >= 2) {
                h.resultCode = parcel.readString();
            }
            if (version >= 3) {
                h.extendedSlip = parcel.readString();
            }
            if (version >= 4) {
                h.cashlessInfo = ParcelableUtils.readParcelable(parcel, CashlessInfo.CREATOR);
            }
            if (version >= 5) {
                h.additionalTransactionData = ParcelableUtils.readParcelable(parcel, AdditionalTransactionData.CREATOR);
            }
            if (version >= 6) {
                h.maskedPan = parcel.readString();
                h.cardType = CardType.fromName(parcel.readString(), CardType.UNKNOWN);
                h.stan = parcel.readString();
                h.authCode = parcel.readString();
            }
            if (version >= 7) {
                String paymentStateName = parcel.readString();
                if (paymentStateName != null) {
                    try {
                        h.paymentState = Constants.PaymentState.valueOf(paymentStateName);
                    } catch (IllegalArgumentException e) {
                        h.paymentState = null;
                    }
                }
                h.paymentSessionId = parcel.readString();
                h.loyaltyCardId = parcel.readString();
            }
            if (version >= 8) {
                h.terminalId = parcel.readString();
                h.isOwn = parcel.readInt() != 0;
            }
        });
        return h;
    }

    private static class V8Holder {
        String rrn;
        int slipLength;
        String[] slip;
        String resultCode;
        String extendedSlip;
        CashlessInfo cashlessInfo;
        AdditionalTransactionData additionalTransactionData;
        String maskedPan;
        CardType cardType;
        String stan;
        String authCode;
        Constants.PaymentState paymentState;
        String paymentSessionId;
        String loyaltyCardId;
        String terminalId;
        boolean isOwn;
        Date datetime;
        ErrorInfo errorInfo;
    }

}

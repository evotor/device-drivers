package ru.evotor.devices.drivers.paysystem;

import android.os.Parcel;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@RunWith(AndroidJUnit4.class)
public class TestPaymentRequest {

    private final String v2PaymentRequest = "6022000002000000A4000000010000000400000032002E0030003000000000000E0000006A006100760061002E007500740069006C002E004400610074006500000000002E000000ACED00057372000E6A6176612E7574696C2E44617465686A81014B597419030000787077080000019F6A4525E978000002000000610064000000000004000000700073006900640000000000040000006C00630069006400000000000300000061006C0064000000";
    private final String v3PaymentRequest = "6022000003000000BC000000010000000400000032002E0030003000000000000E0000006A006100760061002E007500740069006C002E004400610074006500000000002E000000ACED00057372000E6A6176612E7574696C2E44617465686A81014B597419030000787077080000019F6A4525E978000002000000610064000000000004000000700073006900640000000000040000006C00630069006400000000000300000061006C0064000000030000007400690064000000020000007400630000000000";
    private final PaymentRequest v2Request = new PaymentRequest(
            1, new BigDecimal("2.00"), new Date(1784194344425L),
            "ad", "psid", "lcid", "ald"
    );

    private final PaymentRequest v3Request = new PaymentRequest(
            1, new BigDecimal("2.00"), new Date(1784194344425L),
            "ad", "psid", "lcid", "ald",
            "tid", "tc"
    );

    private byte[] prepare(String source) {
        int length = source.length();
        byte[] data = new byte[length / 2];
        for (int i = 0; i < length / 2; i++) {
            data[i] = (byte) Integer.parseInt(source.substring(i * 2, i * 2 + 2), 16);
        }
        return data;
    }

    private byte[] serialize(PaymentRequest request, int version) {
        Parcel p = Parcel.obtain();
        try {
            request.writeTo(p, version);
            p.setDataPosition(0);
            return p.marshall();
        } finally {
            p.recycle();
        }
    }

    private PaymentRequest deserialize(byte[] data) {
        Parcel p = Parcel.obtain();
        try {
            p.unmarshall(data, 0, data.length);
            p.setDataPosition(0);
            return PaymentRequest.CREATOR.createFromParcel(p);
        } finally {
            p.recycle();
        }
    }

    @Test
    public void v3SD() {
        byte[] data = serialize(v3Request, 3);
        PaymentRequest test = deserialize(data); // v3
        assert v3Request.equals(test);
    }
    @Test
    public void v2Sv3D() {
        byte[] data = serialize(v2Request, 2);
        PaymentRequest test = deserialize(data); // v3
        assert v2Request.equals(test);
    }
    @Test
    public void v3Sv2D() {
        byte[] data = serialize(v3Request, 3);
        PaymentRequest test = deserialize(data); // v3
        assert Objects.equals(v2Request.instanceId(), test.instanceId());
        assert Objects.equals(v2Request.sum(), test.sum());
        assert Objects.equals(v2Request.expiredAt(), test.expiredAt());
        assert Objects.equals(v2Request.additionalDescription(), test.additionalDescription());
        assert Objects.equals(v2Request.paymentSessionId(), test.paymentSessionId());
        assert Objects.equals(v2Request.loyaltyCardId(), test.loyaltyCardId());
        assert Objects.equals(v2Request.additionalLoyaltyData(), test.additionalLoyaltyData());
        assert Objects.equals(v2Request.transactionId(), null /* v2 has not test.transactionId() */);
        assert Objects.equals(v2Request.tipsCode(), null /* v2 has not test.tipsCode() */);
    }



    @Test
    public void v2Tov3() {
        byte[] v2Data = prepare(v2PaymentRequest);
        PaymentRequest test = deserialize(v2Data); // v3
        System.out.println(test);
        System.out.println(v2Request);
        assert v2Request.equals(test);
    }
    @Test
    public void v3Tov3() {
        byte[] v3Data = prepare(v3PaymentRequest);
        PaymentRequest test = deserialize(v3Data); // v3
        System.out.println(test);
        System.out.println(v3Request);
        assert v3Request.equals(test);
    }
    @Test
    public void v3Tov2() {
        byte[] v3Data = prepare(v3PaymentRequest);
        PaymentRequest test = deserialize(v3Data);
        System.out.println(test);
        System.out.println(v2Request);
        assert Objects.equals(v2Request.instanceId(), test.instanceId());
        assert Objects.equals(v2Request.sum(), test.sum());
        assert Objects.equals(v2Request.expiredAt(), test.expiredAt());
        assert Objects.equals(v2Request.additionalDescription(), test.additionalDescription());
        assert Objects.equals(v2Request.paymentSessionId(), test.paymentSessionId());
        assert Objects.equals(v2Request.loyaltyCardId(), test.loyaltyCardId());
//        assert Objects.equals(v2Request.transactionId(), null); // v2 не содержит поля transactionId()
//        assert Objects.equals(v2Request.tipsCode(), null); // v2 не содержит поля transactionId()
    }
    @Test
    public void v2Tov2() {
        byte[] v2Data = prepare(v2PaymentRequest);
        PaymentRequest test = deserialize(v2Data);
        System.out.println(test);
        System.out.println(v2Request);
        assert v2Request.equals(test);
    }

}

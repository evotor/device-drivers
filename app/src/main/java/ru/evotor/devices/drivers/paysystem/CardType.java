package ru.evotor.devices.drivers.paysystem;

public enum CardType {
    MASTER_CARD("MasterCard"),
    VISA("Visa"),
    ELCART("Elcart"),
    MIR("Mir"),
    CUP("CUP"),
    UNION_PAY("UnionPay"),
    UNKNOWN("Unknown");


    public final String card;

    CardType(String card) {
        this.card = card;
    }

    public static CardType fromName(String name, CardType defaultCardType) {
        if (name == null) return defaultCardType;
        for (CardType cardType : values()) {
            if (cardType.card.equalsIgnoreCase(name)) {
                return cardType;
            }
        }
        return defaultCardType;
    }
}

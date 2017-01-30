package ru.evotor.devices.drivers.paysystem;

public interface IPaySystem {

    PayResult payment(String sum);

    PayResult cancelPayment(String sum, String rrn);

    PayResult payback(String sum, String rrn);

    PayResult cancelPayback(String sum, String rrn);

    PayResult closeSession();

    void openServiceMenu();

    String getBankName();

    int getTerminalNumber();

    String getTerminalID();

    String getMerchNumber();

    String getMerchCategoryCode();

    String getMerchEngName();

    String getCashier();

    String getServerIP();

}

package ru.evotor.devices.drivers.paysystem;

public interface IPaySystem {

    PayResult payment(PayInfo payInfo);

    PayResult cancelPayment(PayInfo payInfo, String rrn);

    PayResult payback(PayInfo payInfo, String rrn);

    PayResult cancelPayback(PayInfo payInfo, String rrn);

    PayResult closeSession();

    void openServiceMenu();

    String getBankName();

    @Deprecated
    int getTerminalNumber();

    String getTerminalNumberAsString();

    String getTerminalID();

    String getMerchNumber();

    String getMerchCategoryCode();

    String getMerchEngName();

    String getCashier();

    String getServerIP();

    boolean isNotNeedRRN();

    PayResult execPaymentRequest(PaymentRequest request);

    PayResult execCancelPaymentRequest(CancelPaymentRequest request);

    PayResult execPaybackRequest(PaybackRequest request);

    PayResult execCancelPaybackRequest(CancelPaybackRequest request);


    //void openCashierMenu();
    PayResult openCashierMenu();

    /**
     * Получает от драйвера терминала список отчётов, которые он может сформировать
     */
    String getSupportedReports();

    /**
     * Формирует отчёт
     * @param report - имя отчета из getSupportedReports
     * @param args - json с параметрами формируемого отчёта
     */
    PayResult makeReport(String report, String args);

    PayResult checkTransactionStatus(String rrn);
}

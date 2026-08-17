package ru.evotor.devices.drivers.paysystem;

public interface IPaySystem {

    @Deprecated
    PayResult payment(PayInfo payInfo);

    @Deprecated
    PayResult cancelPayment(PayInfo payInfo, String rrn);

    @Deprecated
    PayResult payback(PayInfo payInfo, String rrn);

    @Deprecated
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
    ReportType[] getSupportedReports();

    /**
     * Формирует отчёт
     *
     * @param reportType - тип отчета из getSupportedReports
     */
    PayResult makeReport(ReportType reportType);

    /**
     * Производит проверку статуса текущей транзакции
     * @param rrn - идентификатор транзакции
     * @param transactionId - внешний идентификатор транзакции
     * @return    - результат проверки
     */
    PayResult checkTransactionStatus(String rrn, String transactionId);

    /**
     * Выйти из режима прерывания оплаты (Нужно для автономного POS в режиме спасибо)
     */
    void cancelPaymentConfirmationMode();

}

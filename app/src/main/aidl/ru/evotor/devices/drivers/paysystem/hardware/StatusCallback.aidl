// StatusCallback.aidl
package ru.evotor.devices.drivers.paysystem.hardware;

interface StatusCallback {

    /**
     * В этот метод будет передаваться статус текущей операции, и, возможно, дополнительные данные по ней.
     * Любая ошибка при вызове этого метода приведёт к удалению StatusCallback из системы,
     * и дальнейшие изменения статусов операций не будут приходить в данный экземпляр StatusCallback.
     */
    void onStatus(int status, /*nullable*/ String additionalJsonData) = 1;

    /**
     * Истёк таймаут оплаты картой, заданный в конфигурации терминала.
     * После получения этого статуса будет получен финальнй статус с соответствующим resultCode
     */
    const int STATUS_TIMEOUT = 0x01;

    /**
     * Ошибка чтения карты. EMV-ядро вернуло ошибку "используйте другой интерфейс карты".
     * Получение этой ошибки не прерывает выполнение операции, но может потребоваться перерисовка интерфейса.
     */
    const int STATUS_OTHER_INTERFACE = 0x02;

    /**
     * Ошибка чтения карты. EMV-ядро вернуло ошибку "используйте другую карту".
     * Получение этой ошибки не прерывает выполнение операции, но может потребоваться перерисовка интерфейса.
     */
    const int STATUS_OTHER_CARD = 0x03;

    /**
     * Пользователь отменил операцию по карте.
     * После получения этого статуса будет получен финальнй статус с соответствующим resultCode.
     */
    const int STATUS_USER_CANCEL = 0x04;

    /**
     * Карта прочитана (для NFC и магнитной полосы), или карточная транзакция начинается (для чипа).
     */
    const int STATUS_TRANSACTION_STARTED = 0x05;

    /**
     * Транзакция требует подтверждения ПИН-кодом. Начало ввода ПИН-кода пользователем.
     */
    const int STATUS_PIN_STARTED = 0x06;

    /**
     * Пользователь завершил ввод ПИН-кода (или пропустил ввод, или отменил).
     */
    const int STATUS_PIN_FINISHED = 0x07;

    /**
     * Терминал отправляет данные в банк. С этого момента вызов метода cancelOperation() не прервёт транзакцию, а поставит её в очередь автоотмен.
     */
    const int STATUS_NETWORK_TRANSACTION = 0x08;

    /**
     * Транзакция завершена.
     */
    const int STATUS_TRANSACTION_FINISHED = 0x09;

}
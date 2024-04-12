// EvoPayApi.aidl
package ru.evotor.devices.drivers.paysystem.hardware;

import ru.evotor.devices.drivers.paysystem.hardware.StatusCallback;
import ru.evotor.devices.drivers.paysystem.hardware.Operation;
import ru.evotor.devices.drivers.paysystem.hardware.OperationResult;
import ru.evotor.devices.drivers.paysystem.hardware.TerminalInfo;

interface EvoPayApi {

    /**
     * Регистрирует StatusCallback для асинхронного получения статусов операции.
     * Возвращает id зарегистрированного callback для удаления в будущем.
     */
    int addCallback(StatusCallback callback) = 5;

    /**
     * Удаляет callback из системы по его callbackId. После вызова этого метода новые статусы операций не будут приходить в StatusCallback.
     */
    void removeCallback(int callbackId) = 6;

    /**
     * Вызывает показ сервисного меню
     */
    void showServiceMenu() = 7;

    /**
     * Получает информацию о терминале
     */
    TerminalInfo getTerminalInfo() = 8;


    // После вызова любого из методов start* для фактического начала операции
    // необходимо вызвать метод getOperationResult, передав ему возвращённый operationId.

    /**
     * Запускает операцию оплаты картой.
     * Требует сумму оплаты @param(amount) в минимальных денежных единицах
     * и может принимать дополнительные параметры в виде @param(json).
     * Возвращает id операции оплаты в терминале.
     */
    long startPurchase(long amount, /*nullable*/ String json) = 21;

    /**
     * Запускает операцию возврата денежных средств на карту.
     * Требует сумму возврата @param(amount) в минимальных денежных единицах
     * и может принимать дополнительные параметры в виде @param(json).
     * Возвращает id операции возврата в терминале.
     */
    long startRefund(long amount, /*nullable*/ String json) = 22;

    /**
     * Отменяет успешно завершённую операцию путём отправки запроса отмены в банк.
     * Требует сумму отмены @param(amount) в минимальных денежных единицах
     * и требует дополнительные параметры в виде @param(json) - минимум, RRN отменяемой операции.
     * Возвращает id операции отмены в терминале.
     */
    long startReversal(long amount, /*notnull*/ String json) = 23;

    /**
     * Запускает операцию выдачи денежных средств со счёта карты.
     * Требует сумму выдачи @param(amount) в минимальных денежных единицах
     * и может принимать дополнительные параметры в виде @param(json).
     * Возвращает id операции выдачи наличных в терминале.
     */
    long startCashout(long amount, /*nullable*/ String json) = 24;

    /**
     * Запускает операцию сверки итогов банковского терминала.
     * Может принимать дополнительные параметры в виде @param(json).
     * Возвращает id операции сверки итогов в терминале.
     */
    long startReconcilation(/*nullable*/ String json) = 25;

    /**
     * Получает @return(Operation) статус операции по её @param(operationId).
     */
    Operation getOperationResult(long operationId) = 51;

    /**
     * Отменяет проводимую операцию по её @param(operationId).
     * Если транзакция в операции не начата, то сразу возвращает Cancelled.
     * Если транзакция в операции начата, то возвращает CANCEL_PLANNED и будет отменена автоматически после завершения транзакции.
     * Если транзакция в операции находится в финальном статусе (Finished/Cancel/NOT_FOUND), то вернётся текущий статус и автоматическая отмена произведена не будет.
     */
    Operation cancelOperation(long operationId) = 52;



}
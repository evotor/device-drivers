package ru.evotor.devices.drivers;

import ru.evotor.devices.drivers.paysystem.PayResult;
import ru.evotor.devices.drivers.paysystem.PayInfo;
import ru.evotor.devices.drivers.paysystem.PaymentRequest;
import ru.evotor.devices.drivers.paysystem.CancelPaymentRequest;
import ru.evotor.devices.drivers.paysystem.PaybackRequest;
import ru.evotor.devices.drivers.paysystem.CancelPaybackRequest;
import ru.evotor.devices.drivers.paysystem.PayoutRequest;
import ru.evotor.devices.drivers.paysystem.CancelPayoutRequest;
import ru.evotor.devices.drivers.paysystem.PayoutRevertRequest;
import ru.evotor.devices.drivers.paysystem.CancelPayoutRevertRequest;

interface IPaySystemDriverService {

    /**
	 * Производит оплату на указанную сумму
	 *
	 * @param instanceId    - номер экземпляра драйвера
	 * @param payinfo       - информация об оплате (сумма)
	 * @return              - параметры успешного завершения результата
	 * @throws              - RuntimeException в случае неуспешного завершения
	 */
	PayResult payment(int instanceId, in PayInfo payinfo) = 0;

	/**
	 * Производит отмену оплаты на указанную сумму
	 *
	 * @param instanceId    - номер экземпляра драйвера
	 * @param payinfo       - информация об оплате (сумма)
	 * @param rrn           - РРН отменяемой операции
	 * @return              - параметры успешного завершения результата
	 * @throws              - RuntimeException в случае неуспешного завершения
     */
	PayResult cancelPayment(int instanceId, in PayInfo payinfo, String rrn) = 1;

	/**
	 * Производит возврат на указанную сумму
	 *
	 * @param instanceId    - номер экземпляра драйвера
	 * @param payinfo       - информация об оплате (сумма)
	 * @param rrn           - РРН операции оплаты
	 * @return              - параметры успешного завершения результата
	 * @throws              - RuntimeException в случае неуспешного завершения
     */
	PayResult payback(int instanceId, in PayInfo payinfo, String rrn) = 2;

	/**
	 * Производит отмену возврата на указанную сумму
	 *
	 * @param instanceId    - номер экземпляра драйвера
	 * @param payinfo       - информация об оплате (сумма)
	 * @param rrn           - РРН отменяемой операции
	 * @return              - параметры успешного завершения результата
	 * @throws              - RuntimeException в случае неуспешного завершения
	 */
	PayResult cancelPayback(int instanceId, in PayInfo payinfo, String rrn) = 3;

	/**
	 * Производит закрытие банковской смены
	 *
	 * @param instanceId    - номер экземпляра драйвера
	 * @return              - параметры успешного завершения результата
	 * @throws              - RuntimeException в случае неуспешного завершения
     */
	PayResult closeSession(int instanceId) = 4;

    /**
     * Открывает на терминале сервисное меню
     *
	 * @param instanceId    - номер экземпляра драйвера
     */
	void openServiceMenu(int instanceId) = 5;

    /**
     * запрос свойств пинпада
	 *
	 * @param instanceId    - номер экземпляра драйвера
	 */
	String getBankName(int instanceId) = 6;

	/**
	* @deprecated используйте getTerminalNumberAsString
	*/
	int getTerminalNumber(int instanceId) = 7;
	String getTerminalID(int instanceId) = 8;
	String getMerchNumber(int instanceId) = 9;
	String getMerchCategoryCode(int instanceId) = 10;
	String getMerchEngName(int instanceId) = 11;
	String getCashier(int instanceId) = 12;
	String getServerIP(int instanceId) = 13;

    /**
	 * банковскому терминалу не требуется РРН для возврата/отмены операции
	 *
	 * @param instanceId    - номер экземпляра драйвера
	 * @return  true    -   РРН не требуется
	 *          false   -   РРН требуется
	 */
    boolean isNotNeedRRN(int instanceId) = 14;
    String getTerminalNumberAsString(int instanceId) = 15;

    /**
     * Производит оплату на указанную сумму
     *
     * @param request       - Запрос
     * @return              - параметры успешного завершения результата. Не может быть null!
     * @throws              - RuntimeException в случае неуспешного завершения
     */
    PayResult execPaymentRequest(in PaymentRequest request) = 16;

    /**
     * Производит отмену оплаты на указанную сумму
     *
     * @param request       - Запрос
     * @return              - параметры успешного завершения результата. Не может быть null!
     * @throws              - RuntimeException в случае неуспешного завершения
     */
    PayResult execCancelPaymentRequest(in CancelPaymentRequest request) = 17;

    /**
     * Производит возврат на указанную сумму
     *
     * @param request       - Запрос
     * @return              - параметры успешного завершения результата. Не может быть null!
     * @throws              - RuntimeException в случае неуспешного завершения
     */
    PayResult execPaybackRequest(in PaybackRequest request) = 18;

    /**
     * Производит отмену возврата на указанную сумму
     *
     * @param request       - Запрос
     * @return              - параметры успешного завершения результата. Не может быть null!
     * @throws              - RuntimeException в случае неуспешного завершения
     */
    PayResult execCancelPaybackRequest(in CancelPaybackRequest request) = 19;

    /**
     * Производит покупку у клиента на указанную сумму
     */
    PayResult execPayoutRequest(in PayoutRequest request) = 20;

    /**
     * Производит отмену покупки на указанную сумму
     */
    PayResult execCancelPayoutRequest(in CancelPayoutRequest request) = 21;


    /**
     * Производит возврат покупки у клиента на указанную сумму
     */
    PayResult execPayoutRevertRequest(in PayoutRevertRequest request) = 22;

    /**
     * Производит отмену возврата покупки на указанную сумму
     */
    PayResult execCancelPayoutRevertRequest(in CancelPayoutRevertRequest request) = 23;


    /**
    * Открывает на терминале меню кассира
    *
    * @param instanceId    - номер экземпляра драйвера
    */

    void openCashierMenu(int instanceId) = 24;
}

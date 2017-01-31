package ru.evotor.devices.drivers;

import ru.evotor.devices.drivers.paysystem.PayResult;
import ru.evotor.devices.drivers.paysystem.PayInfo;

interface IPaySystemDriverService {

    /**
	 * Производит оплату на указанную сумму
	 *
	 * @param instanceId    - номер экземпляра драйвера
	 * @param payinfo       - информация об оплате (сумма)
	 * @return              - параметры успешного завершения результата
	 * @throws              - RuntimeException в случае неуспешного завершения
	 */
	PayResult payment(int instanceId, in PayInfo payinfo);

	/**
	 * Производит отмену оплаты на указанную сумму
	 *
	 * @param instanceId    - номер экземпляра драйвера
	 * @param payinfo       - информация об оплате (сумма)
	 * @param rrn           - РРН отменяемой операции
	 * @return              - параметры успешного завершения результата
	 * @throws              - RuntimeException в случае неуспешного завершения
     */
	PayResult cancelPayment(int instanceId, in PayInfo payinfo, String rrn);

	/**
	 * Производит возврат на указанную сумму
	 *
	 * @param instanceId    - номер экземпляра драйвера
	 * @param payinfo       - информация об оплате (сумма)
	 * @param rrn           - РРН операции оплаты
	 * @return              - параметры успешного завершения результата
	 * @throws              - RuntimeException в случае неуспешного завершения
     */
	PayResult payback(int instanceId, in PayInfo payinfo, String rrn);

	/**
	 * Производит отмену возврата на указанную сумму
	 *
	 * @param instanceId    - номер экземпляра драйвера
	 * @param payinfo       - информация об оплате (сумма)
	 * @param rrn           - РРН отменяемой операции
	 * @return              - параметры успешного завершения результата
	 * @throws              - RuntimeException в случае неуспешного завершения
	 */
	PayResult cancelPayback(int instanceId, in PayInfo payinfo, String rrn);

	/**
	 * Производит закрытие банковской смены
	 *
	 * @param instanceId    - номер экземпляра драйвера
	 * @return              - параметры успешного завершения результата
	 * @throws              - RuntimeException в случае неуспешного завершения
     */
	PayResult closeSession(int instanceId);

    /**
     * Открывает на терминале сервисное меню
     *
	 * @param instanceId    - номер экземпляра драйвера
     */
	void openServiceMenu(int instanceId);

    /**
     * запрос свойств пинпада
	 *
	 * @param instanceId    - номер экземпляра драйвера
	 */
	String getBankName(int instanceId);
	int getTerminalNumber(int instanceId);
	String getTerminalID(int instanceId);
	String getMerchNumber(int instanceId);
	String getMerchCategoryCode(int instanceId);
	String getMerchEngName(int instanceId);
	String getCashier(int instanceId);
	String getServerIP(int instanceId);

}

package ru.evotor.devices.drivers;

import ru.evotor.devices.drivers.paysystem.PayResult;

interface IPaySystemDriverService {

    /**
	 * Производит оплату на указанную сумму
	 *
	 * @param instanceId    - номер экземпляра драйвера
	 * @param sum           - сумма в рублях, десятичный разделитель - точка
	 * @return              - параметры успешного завершения результата
	 * @throws              - RuntimeException в случае неуспешного завершения
	 */
	PayResult payment(int instanceId, String sum);

	/**
	 * Производит отмену оплаты на указанную сумму
	 *
	 * @param instanceId    - номер экземпляра драйвера
	 * @param sum           - сумма в рублях, десятичный разделитель - точка
	 * @param rrn           - РРН отменяемой операции
	 * @return              - параметры успешного завершения результата
	 * @throws              - RuntimeException в случае неуспешного завершения
     */
	PayResult cancelPayment(int instanceId, String sum, String rrn);

	/**
	 * Производит возврат на указанную сумму
	 *
	 * @param instanceId    - номер экземпляра драйвера

	 * @param sum           - сумма в рублях, десятичный разделитель - точка
	 * @param rrn           - РРН операции оплаты
	 * @return              - параметры успешного завершения результата
	 * @throws              - RuntimeException в случае неуспешного завершения
     */
	PayResult payback(int instanceId, String sum, String rrn);

	/**
	 * Производит отмену возврата на указанную сумму
	 *
	 * @param instanceId    - номер экземпляра драйвера
	 * @param sum           - сумма в рублях, десятичный разделитель - точка
	 * @param rrn           - РРН отменяемой операции
	 * @return              - параметры успешного завершения результата
	 * @throws              - RuntimeException в случае неуспешного завершения
	 */
	PayResult cancelPayback(int instanceId, String sum, String rrn);

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

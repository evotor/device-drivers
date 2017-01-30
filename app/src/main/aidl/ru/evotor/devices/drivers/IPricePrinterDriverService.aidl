package ru.evotor.devices.drivers;

import android.bluetooth.BluetoothDevice;
import android.hardware.usb.UsbDevice;

// Declare any non-default types here with import statements
interface IPricePrinterDriverService {

	/**
	 * Подготавливает принтер ценников к печати группы ценников
	 *
	 * @param instanceId    - номер экзмпляра драйвера
	 */
	void beforePrintPrices(int instanceId);

	/**
	 * Печатает 1 ценник
	 *
	 * @param instanceId    - номер экзмпляра драйвера
	 * @name	            - название товара
	 * @price	            - цена
	 * @barcode	            - штрихкод
	 * @code	            - код товара
	 */
	void printPrice(int instanceId, String name, String price, String barcode, String code);

	/**
	 * Завершает печать группы ценников
	 *
	 * @param instanceId    - номер экзмпляра драйвера
	 */
	void afterPrintPrices(int instanceId);

}
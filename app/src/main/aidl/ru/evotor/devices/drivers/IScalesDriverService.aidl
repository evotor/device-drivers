package ru.evotor.devices.drivers;

import android.bluetooth.BluetoothDevice;
import android.hardware.usb.UsbDevice;
import ru.evotor.devices.drivers.scales.Weight;

interface IScalesDriverService {

	/**
	* Получить вес и прочие характеристики
	*/
    Weight getWeight(int instanceId);

}

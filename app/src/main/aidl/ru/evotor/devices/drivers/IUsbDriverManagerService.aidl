package ru.evotor.devices.drivers;

import android.bluetooth.BluetoothDevice;
import android.hardware.usb.UsbDevice;

// Declare any non-default types here with import statements
interface IUsbDriverManagerService {

    /**
    * Возвращает индекс экземпляра драйвера, созданного на основе usb-девайса, по которому будет происходить дальнейшее обращение
    * usbPortPath - строка с путём usb-устройства в системе. уникально для всех usb-усройств в каждый момент времени, не изменяется при перезагрузке/переподключении устройства в тот же самый физический порт
    */
    int addUsbDevice(in UsbDevice usbDevice, String usbPortPath);

    /**
     * Уничтожает драйвер (при отключении оборудования или вручную пользователем)
     */
    void destroy(int instanceId);
}
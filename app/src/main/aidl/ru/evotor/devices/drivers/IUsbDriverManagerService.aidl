package ru.evotor.devices.drivers;

import android.bluetooth.BluetoothDevice;
import android.hardware.usb.UsbDevice;

// Declare any non-default types here with import statements
interface IUsbDriverManagerService {

    /**
    * Возвращает индекс экземпляра драйвера, созданного на основе usb-девайса, по которому будет происходить дальнейшее обращение
    * @usbDevice    - usbDevice, для которого надо создать драйвер. Permission на это устройтсво в момент вызова этой функции уже будет выдан
    * @usbPortPath  - строка с путём usb-устройства в системе. Уникально для всех usb-усройств в каждый момент времени, не изменяется при перезагрузке/переподключении устройства в тот же самый физический порт
    */
    int addUsbDevice(in UsbDevice usbDevice, String usbPortPath);

    /**
     * Уничтожает драйвер (при отключении оборудования или вручную пользователем)
     */
    oneway void destroy(int instanceId);
}
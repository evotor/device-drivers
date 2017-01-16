package ru.evotor.devices.drivers;

import android.bluetooth.BluetoothDevice;
import android.hardware.usb.UsbDevice;

// Declare any non-default types here with import statements
interface IVirtualDriverManagerService {

    /**
    * Возвращает индекс экземпляра виртуального (не имеющего реального usb-устройства под собой) драйвера, по которому будет происходить дальнейшее обращение.
    * Такой драйвер будет пересоздан, например, после перезагрузки. InstanceId будет сохранён и передан в метод recreateNewVirtualDevice.
    */
    int addNewVirtualDevice();

    /**
    * Пересоздаёт созданный ранее виртуальный драйвер.
    */
    void recreateNewVirtualDevice(int instanceId);

    /**
     * Уничтожает драйвер (при отключении оборудования или вручную пользователем)
     */
    void destroy(int instanceId);
}
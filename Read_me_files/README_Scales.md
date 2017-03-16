[Главная страница](https://github.com/Draudr/device-drivers/blob/New_structure_of_SDK_manual/README.md) > SDK для Весов
> Прежде чем изучать материал, представленный на данной странице, Вы должны убедиться, что были реализованы все шаги, описанные в пункте [Подготовка к разработке.](https://github.com/Draudr/device-drivers/blob/New_structure_of_SDK_manual/Read_me_files/Preparation_for_development.md#1101)
<a name="1501"></a>
# __1.3. SDK для весов.__
_Содержание:_  
1.3.1. [Определение внешнего сервиса в `AndroidManifest.xml`.](#307).  
1.3.2. [Определение роли и категории устройства.](#301)  
1.3.3. [Присвоение картинки для драйвера.](#302)  
1.3.4. [В реализации метода подключения к сервису для всех action указанных в интент-фильтрах укажите соответствующие `Binder'ы`.](#303)  
1.3.5. [Описание указанных `Binder'ов`.](#304)  
1.3.6. [Описание класса для работы с оборудованием.](#305)  
1.3.7. [Завершение работы.](#306)

<a name="307"></a>
### 1.3.1. Определение внешнего сервиса в `AndroidManifest.xml`.

Для сервиса должен быть указан хотя бы один из интент-фильтров `INTENT_FILTER_DRIVER_MANAGER` или `INTENT_FILTER_VIRTUAL_DRIVER_MANAGER`.

Пример объявленного сервиса:

```
<service
    android:name="ru.mycompany.drivers.MyDeviceService"
    android:enabled="true"
    android:exported="true"
    android:icon="@drawable/logo"
    android:label="@string/service_name">
    <intent-filter>
        <action android:name="ru.evotor.devices.drivers.DriverManager" />
        <action android:name="ru.evotor.devices.drivers.ScalesService" />
    </intent-filter>
    <meta-data
        android:name="vendor_name"
        android:value="CAS" />
    <meta-data
        android:name="model_name"
        android:value="AD" />
    <meta-data
        android:name="usb_device"
        android:value="VID_1659PID_8963" />
    <meta-data
        android:name="settings_activity"
        android:value="" />
    <meta-data
        android:name="device_categories"
        android:value="SCALES" />
</service>
```  
`vendor_name` - наименование производителя, которое будет отображаться при подключении устройства.

`model_name` - наименование модели устройства.


`INTENT_FILTER_DRIVER_MANAGER` - используется для драйверов, которые требуют для работы подключенное USB-оборудование. Вместе с этим необходимо указать для сервиса в `meta-data` характеристики VendorID и ProductID целевого устройства (десятичными числами):

```
<meta-data
    android:name="usb_device"
    android:value="VID_1659PID_8963" />
```

При необходимости, можно указать несколько устройств следующим образом: `"VID_1659PID_8963|VID_123PID_456|VID_1659PID_8964"`.

Экземпляр драйвера будет автоматически создан/удалён при подключении/отключении указанного оборудования к смарт-терминалу. При наличии нескольких подходящих драйверов, пользователю будет предложен выбор.

`INTENT_FILTER_VIRTUAL_DRIVER_MANAGER` - используется для драйверов, не требующих USB-оборудования (сетевое, bluetooth и др. оборудование). Вместе с этим необходимо указать в `meta-data`, что драйвер является виртуальным:

```
<meta-data
    android:name="virtual_device"
    android:value="true" />
```

Экземпляр такого драйвера пользователь может создать исключительно вручную через настройки оборудования. В этом случае все работы по подключению к нужному устройству берёт на себя производитель драйвера.

<a name="301"></a>
## 1.3.2. Определение роли и категории устройства  
Следующий интент-фильтр используется для реализации роли устройства для которого пишется драйвер:

```
`INTENT_FILTER_SCALES`
```

Вместе с этим необходимо указать в `meta-data` категорию устройства:

```
<meta-data
    android:name="device_categories"
    android:value="SCALES" />
```
Можно указать сразу несколько ролей устройству следующим образом: `"SCALES|PRICEPRINTER|CASHDRAWER"`_(примечение: весы | принтер чеков | денежный ящик)._
<a name="302"></a>
## 1.3.3. Присвоение картинки для драйвера
В манифесте приложения у сервиса должны быть указаны:  
* `android:icon` - картинка устройства, которая будет отображаться пользователю при инициализации устройства;  
* `android:label` - имя драйвера, которое будет отображаться пользователю при инициализации устройства

![Пример отображения иконки и имени драйвера](https://github.com/VedbeN/device-drivers/blob/master/icon_xmpl.png?raw=true "Пример отображения иконки и имени драйвера")

Можно задать `activity`  настроек, если это требуется:

```
<meta-data
    android:name="settings_activity"
    android:value="ru.mycompany.drivers.MySettingsActivity" />
```

Указанная `activity`  должна находиться в текущем `package` и будет вызвана при первом подключении устройства или при выборе устройства в меню настроек оборудования.

Версия драйвера (`versionCode` и `versionName`) берётся из `build.gradle`:

```
defaultConfig {
    applicationId "ru.mycompany.drivers.myscales"
    minSdkVersion 22
    targetSdkVersion 24
    versionCode 2
    versionName "1.0.1"
}
```

`MinSdkVersion` должна быть не выше версии 22!
<a name="303"></a>
## 1.3.4. В реализации метода подключения к сервису для всех action указанных в интент-фильтрах укажите соответствующие `Binder'ы`

для `INTENT_FILTER_DRIVER_MANAGER` - класс наследник `ru.evotor.devices.drivers.IUsbDriverManagerService.Stub`;

для `INTENT_FILTER_VIRTUAL_DRIVER_MANAGER` - класс наследник `ru.evotor.devices.drivers.IVirtualDriverManagerService.Stub`;

для `INTENT_FILTER_SCALES` - класс наследник `ru.evotor.devices.drivers.IScalesDriverService.Stub`;

Например:

```
public class MyDeviceService extends Service {

    private final Map<Integer, MyDevice> instances = new HashMap<>();
    private volatile AtomicInteger newDeviceIndex = new AtomicInteger(0);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case Constants.INTENT_FILTER_DRIVER_MANAGER:
                return new MyDriverManagerStub(MyDeviceService.this);
            case Constants.INTENT_FILTER_SCALES:
                return new MyScalesStub(MyDeviceService.this);
            default:
                return null;
        }
    }

    public int createNewDevice(UsbDevice usbDevice) {
        int currentIndex = newDeviceIndex.getAndIncrement();
        instances.put(currentIndex, new MyDevice(getApplicationContext(), usbDevice));
        return currentIndex;
    }

    public MyDevice getMyDevice(int instanceId) {
        return instances.get(instanceId);
    }

    public void destroy(int instanceId) {
		getMyDevice(instanceId).destroy();
        instances.remove(instanceId);
    }
}
```

В этом же сервисе удобно определить `Map` для хранения списка активных экземпляров драйверов (а их, потенциально, может быть больше, чем 1 в системе одновременно), т.к. обращаться к нему придётся из всех указанных Stub'ов.

<a name="304"></a>
## 1.3.5. Описание указанных `Binder'ов`.

Для всех описываемых методов, в случае невозможности выполнить требуемое действие (например, "взвесить" для метода `getWeight`), слудует использовать один из поддерживаемых типов `Exception` с легкочитаемым описанием.   

Перечень поддерживаемых типов `Exception`:
`BadParcelableException`;
`IllegalArgumentException`;
`IllegalStateException`;
`NullPointerException`;
`SecurityException`;
`NetworkOnMainThreadException`.


Подробное описание типов `Exception` на портале [developer.android.com](https://developer.android.com/reference/android/os/Parcel.html#writeException%28java.lang.Exception%29)


#### `IUsbDriverManagerService.Stub` - класс для управления драйверами usb-устройств: подключение и отключение устройств происходят здесь.  Требуется реализовать методы `addUsbDevice` и `destroy`.

```
import ru.evotor.devices.drivers.IUsbDriverManagerService;

public class MyDriverManagerStub extends IUsbDriverManagerService.Stub {

    private MyDeviceService myDeviceService;

    public MyDriverManagerStub(MyDeviceService myDeviceService) {
        this.myDeviceService = myDeviceService;
    }

    @Override
    public int addUsbDevice(UsbDevice usbDevice, String usbPortPath) throws RemoteException {
        return myDeviceService.createNewDevice(usbDevice);
    }

    @Override
    public void destroy(int instanceId) throws RemoteException {
        myDeviceService.destroy(instanceId);
    }
}
```

Метод `addUsbDevice` в `IUsbDriverManagerService` принимает на вход:

1) `UsbDevice`, для которого он создан;

2) некоторый строковый идентификатор номера физического usb-порта (может потребоваться, например, если  требуется сохранить какие-либо настройки оборудования и восстановить их после перезагрузки терминала). В этот момент у приложения-драйвера уже есть `permission` для работы с этим устройством.

Метод `addUsbDevice` возвращает номер экземпляра драйвера внутри приложения. По этому номеру будет происходить обращение к конкретному драйверу.

Метод `destroy` в `IUsbDriverManagerService` принимает на вход номер экземпляра драйвера. Вызов этого метода уведомляет приложение об отключении от устройства. В этот момент у приложения-драйвера уже нет `permission` для работы с этим устройством, само устройство уже может быть удалено из смарт-терминала.


#### `IVirtualDriverManagerService.Stub` - класс для управления драйверами виртуальных устройств:
подключение и отключение устройств происходят здесь. Требуется реализовать методы `addNewVirtualDevice`, `recreateNewVirtualDevice` и `destroy`.

```
import ru.evotor.devices.drivers.IVirtualDriverManagerService;

public class MyDriverManagerStub extends IVirtualDriverManagerService.Stub {

    private MyDeviceService myDeviceService;

    public MyDriverManagerStub(MyDeviceService myDeviceService) {
        this.myDeviceService = myDeviceService;
    }

    @Override
    public int addNewVirtualDevice() throws RemoteException {

            return myDeviceService.createNewDevice(usbDevice);
    }

    @Override
    public void recreateNewVirtualDevice(int instanceId) throws RemoteException {
        myDeviceService.recreateNewVirtualDevice(instanceId);
    }

    @Override
    public void destroy(int i) throws RemoteException {
        myDeviceService.destroy(instanceId);
    }
}
```

Метод `addNewVirtualDevice` возвращает номер экземпляра драйвера внутри приложения. По этому номеру будет происходить обращение к конкретному драйверу.

Метод `recreateNewVirtualDevice` принимает на вход номер экземпляра драйвера внутри приложения.

Метод `destroy` принимает на вход номер экземпляра драйвера. Вызов этого метода уведомляет приложение об отключении от устройства.

Для вновь созданного экземпляра драйвера (а виртуальные устройства могут создаваться только вручную пользователем через меню настроек оборудования) будет вызван метод `addNewVirtualDevice`.

Метод `recreateNewVirtualDevice` будет вызван для тех устройств, которые уже создавались пользователем ранее, но в данный момент подключения к таким драйверам нет. Например, после перезагрузки смарт-терминала, перезапуска сервиса работы с оборудованием или обновления приложения-драйвера.

Метод `destroy` будет вызван для устройства, которое пользователь вручную удалил из списка оборудования.

#### `IScalesDriverService.Stub` - класс для работы с конкретными экземплярами весов.  Требуется реализовать метод `getWeight`.

```
import ru.evotor.devices.drivers.IScalesDriverService;
import ru.evotor.devices.drivers.scales.Weight;

public class MyScalesStub extends IScalesDriverService.Stub {

    private MyDeviceService myDeviceService;

    public MyScalesStub(MyDeviceService myDeviceService) {
        this.myDeviceService = myDeviceService;
    }

    @Override
    public Weight getWeight(int instanceId) throws RemoteException {
        return myDeviceService.getMyDevice(instanceId).getWeight();
    }
}

```

Метод `getWeight` принимает на вход номер экземпляра драйвера (тот, который вернул `addUsbDevice` на прошлом шаге).

Метод `getWeight` возвращает объект класса `ru.evotor.devices.drivers.scales.Weight`. В конструкторе требуется указать:

 1) `originalWeight` - считанный вес, в тех единицах измерения, в которых его вернули весы;

 2) `multiplierToGrams` - коэффициент для приведения веса в граммы;

 3) `supportStable` - поддерживают ли весы флаг стабильности;

 4) `stable` - флаг стабильности взвешивания, если поддерживается. Иначе - любое значение.




Все методы принимают на вход номер экземпляра драйвера.

Метод оплаты принимает на вход информацию об оплате (сумму), методы возврата и отмены дополнительно к этому принимают на вход `РРН` прошлой операции.

<a name="305"></a>
## 1.3.6. Описание класса для работы с оборудованием.  
После того, как описаны все классы для взаимодействия с инфраструктурой смарт-терминала, можно описать сам класс работы с оборудованием:

Например, для USB-весов это выглядит следующим образом:

```
import ru.evotor.devices.drivers.scales.IScales;
import ru.evotor.devices.drivers.scales.Weight;

public class MyDevice implements IScales {

    private Context context;
    private UsbDevice usbDevice;

    public MyDevice(Context context, UsbDevice usbDevice) {
        super();
        this.context = context;
        this.usbDevice = usbDevice;
    }

	public void destroy(){
	}

    @Override
    public Weight getWeight() {
        //TODO Ваш код запроса веса
    }
}
```

Для устройств других категорий требуется реализовать соответствующие интерфейсы:

Весы - `ru.evotor.devices.drivers.scales.IScales`;

Денежный ящик - `ru.evotor.devices.drivers.cashdrawer.ICashDrawer`;

Принтер ценников - `ru.evotor.devices.drivers.priceprinter.IPricePrinter`;

Банковский терминал - `ru.evotor.devices.drivers.paysystem.IPaySystem`.

<a name="306"></a>
## 1.3.7. Завершение работы.  
Загрузите приложение на смарт-терминал, чтобы работать с Вашим драйвером.   

-----

###### Более подробную информацию по разрабатке своих решений для бизнеса на платформе Эвотор, Вы можете найти на нашем сайте для разработчиков: https://developer.evotor.ru/

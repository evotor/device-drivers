
[Главная страница](https://github.com/Draudr/device-drivers/blob/New_structure_of_SDK_manual/README.md) > SDK для Банковских Терминалов,

> Прежде чем изучать материал, представленный на данной странице, Вы должны убедиться, что были реализованы все шаги, описанные в пункте [Подготовка к разработке.](https://github.com/Draudr/device-drivers/blob/New_structure_of_SDK_manual/Read_me_files/Preparation_for_development.md#1101)  

<a name="1201"></a>
# __1.2. SDK для Банковских Терминалов.__
_Содержание:_  
1.2.1. [Определение внешнего сервиса в `AndroidManifest.xml`.](#201)  
1.2.2. [Определение роли и категории устройства.](#202)  
1.2.3. [Присвоение картинки для драйвера.](#203)  
1.2.4. [В реализации метода подключения к сервису для всех `action`, указанных в интент-фильтрах укажите соответствующие `Binder'ы`](#204)  
1.2.5. [Описание указанных `Binder'ов`.](#205)  
1.2.6. [Описание класса для работы с оборудованием.](#206)  
1.2.7. [Завершение работы.](#207)  

<a name="201"></a>
### 1.2.1. Определение внешнего сервиса в `AndroidManifest.xml`.

При разработке приложенияя необходимо указать хоят бы один  Intent filter: `INTENT_FILTER_DRIVER_MANAGER` или `INTENT_FILTER_VIRTUAL_DRIVER_MANAGER`.

Пример объявленного сервиса:

```
<service
      android:name="ru.mycompany.drivers.MyPaySystemService"
      android:enabled="true"
      android:exported="true"
      android:icon="@drawable/logo"
      android:label="@string/service_name">
            <intent-filter>
                <action android:name="ru.evotor.devices.drivers.DriverManager" />
                <action android:name="ru.evotor.devices.drivers.PaySystemService" />
            </intent-filter>

            <meta-data
                android:name="vendor_name"
                android:value="Ingenico" />
            <meta-data
                android:name="model_name"
                android:value="IPP320" />
            <meta-data
                android:name="usb_device"
                android:value="VID_1947PID_40" />
            <meta-data
                android:name="virtual_device"
                android:value="false" />
            <meta-data
                android:name="settings_activity"
                android:value="" />
            <meta-data
                android:name="device_categories"
                android:value="PAYSYSTEM" />
        </service>
```  
`vendor_name`- наименование производителя, которое будет отображаться при подключении устройства.

`model_name` - наименование модели устройства.  

`INTENT_FILTER_DRIVER_MANAGER` - используется для драйверов, для которых требуется подключение USB- оборудования. Помимо `Intent filter` в `meta-data` необходимо указать   характеристики `VendorID` и `ProductID` целевого устройства (десятичными числами):

```
<meta-data
    android:name="usb_device"
    android:value="VID_1659PID_8963" />
```

При необходимости, можно указать несколько устройств следующим образом: `"VID_1659PID_8963|VID_123PID_456|VID_1659PID_8964"`.

Экземпляр драйвера будет автоматически создан/удалён при подключении/отключении указанного оборудования к смарт-терминалу. При наличии нескольких подходящих драйверов, пользователю будет предложен выбор.

`INTENT_FILTER_VIRTUAL_DRIVER_MANAGER` - используется для драйверов, не требующих подключения USB-оборудования (сетевое, bluetooth и др.). Помимо `Intent filter` в `meta-data` необходимо указать, что драйвер является виртуальным:

```
<meta-data
    android:name="virtual_device"
    android:value="true" />
```

Экземпляр такого драйвера пользователь может создать исключительно вручную через настройки оборудования. В этом случае все работы по подключению к нужному устройству берёт на себя производитель драйвера.

<a name="202"></a>
## 1.2.2. Определение роли и категории устройства  
Для того, чтобы устройство было распознано, как банковский терминал используйте следующий `Intent filter`:

```
`INTENT_FILTER_PAY_SYSTEM`
```

Вместе с этим необходимо указать в `meta-data` категорию устройства:

```
<meta-data
    android:name="device_categories"
    android:value="PAYSYSTEM" />
```
Можно указать сразу несколько ролей устройству следующим образом: `"SCALES|PRICEPRINTER|CASHDRAWER"`_(примечение: весы | принтер чеков | денежный ящик)._

<a name="203"></a>
## 1.2.3. Присвоение картинки для драйвера.  
В манифесте приложения у сервиса должны быть указаны:  
* `android:icon` - картинка устройства, которая будет отображаться пользователю при инициализации устройства;  
* `android:label` - имя драйвера, которое будет отображаться пользователю при инициализации устройства  

![Пример отображения иконки и имени драйвера](https://github.com/Draudr/device-drivers/blob/New_structure_of_SDK_manual/Read_me_files/images/%D0%A1%D0%BD%D0%B8%D0%BC%D0%BE%D0%BA.PNG?raw=true "Пример отображения иконки и имени драйвера")

Можно задать `activity`  настроек, если это требуется:

```
<meta-data
    android:name="settings_activity"
    android:value="ru.mycompany.drivers.MySettingsActivity" />
```

Указанная `activity`  должна находиться в текущем `package` и будет вызвана при первом подключении устройства или по нажатию на строчку с оборудованием в меню настроек оборудования.

Версия драйвера (`versionCode` и `versionName`) берётся из `build.gradle`:

```
defaultConfig {
    applicationId "ru.mycompany.drivers.mypaysystem"
    minSdkVersion 22
    targetSdkVersion 24
    versionCode 2
    versionName "1.0.1"
}
```

`MinSdkVersion` должна быть не выше версии 22!

<a name="204"></a>
### 1.2.4. В реализации метода подключения к сервису для всех `action`, указанных в интент-фильтрах укажите соответствующие `Binder'ы`:

для `INTENT_FILTER_DRIVER_MANAGER` - класс наследник `ru.evotor.devices.drivers.IUsbDriverManagerService.Stub`;

для `INTENT_FILTER_VIRTUAL_DRIVER_MANAGER` - класс наследник `ru.evotor.devices.drivers.IVirtualDriverManagerService.Stub`;

для `INTENT_FILTER_PAY_SYSTEM` - класс наследник `ru.evotor.devices.drivers.IPaySystemDriverService.Stub`;


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
            case Constants.INTENT_FILTER_PAY_SYSTEM:
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

<a name="205"></a>
### 1.2.5.  Описание указанных `Binder'ов`.

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

#### `IPaySystemDriverService.Stub` - класс для работы с конкретными экземплярами банковских терминалов.

```
import ru.evotor.devices.drivers.IPaySystemDriverService;
import ru.evotor.devices.drivers.paysystem.PayResult;
import ru.evotor.devices.drivers.paysystem.PayInfo;

public class MyPaySystemStub extends IPaySystemDriverService.Stub {

    private MyPaySystemService paySystemService;

    public MyPaySystemStub(MyPaySystemService paySystemService) {
        this.paySystemService = paySystemService;
    }

    @Override
    public PayResult payment(int instanceId, PayInfo payInfo) throws RemoteException {
        return paySystemService.getPaySystem(instanceId).payment(payInfo);
    }

    @Override
    public PayResult cancelPayment(int instanceId, PayInfo payInfo, String rrn) throws RemoteException {
        return paySystemService.getPaySystem(instanceId).cancelPayment(payInfo, rrn);
    }

    @Override
    public PayResult payback(int instanceId, PayInfo payInfo, String rrn) throws RemoteException {
        return paySystemService.getPaySystem(instanceId).payback(payInfo, rrn);
    }

    @Override
    public PayResult cancelPayback(int instanceId, PayInfo payInfo, String rrn) throws RemoteException {
        return paySystemService.getPaySystem(instanceId).cancelPayback(payInfo, rrn);
    }

    @Override
    public PayResult closeSession(int instanceId) throws RemoteException {
        return paySystemService.getPaySystem(instanceId).closeSession();
    }

    @Override
    public void openServiceMenu(int instanceId) throws RemoteException {
        paySystemService.getPaySystem(instanceId).openServiceMenu();
    }

    @Override
    public String getBankName(int instanceId) throws RemoteException {
        return paySystemService.getPaySystem(instanceId).getBankName();
    }

    @Override
    public int getTerminalNumber(int instanceId) throws RemoteException {
        return paySystemService.getPaySystem(instanceId).getTerminalNumber();
    }

    @Override
    public String getTerminalID(int instanceId) throws RemoteException {
        return paySystemService.getPaySystem(instanceId).getTerminalID();
    }

    @Override
    public String getMerchNumber(int instanceId) throws RemoteException {
        return paySystemService.getPaySystem(instanceId).getMerchEngName();
    }

    @Override
    public String getMerchCategoryCode(int instanceId) throws RemoteException {
        return paySystemService.getPaySystem(instanceId).getMerchCategoryCode();
    }

    @Override
    public String getMerchEngName(int instanceId) throws RemoteException {
        return paySystemService.getPaySystem(instanceId).getMerchEngName();
    }

    @Override
    public String getCashier(int instanceId) throws RemoteException {
        return paySystemService.getPaySystem(instanceId).getCashier();
    }

    @Override
    public String getServerIP(int instanceId) throws RemoteException {
        return paySystemService.getPaySystem(instanceId).getServerIP();
    }

    @Override
    public boolean isNotNeedRRN(int instanceId) throws RemoteException {
        return paySystemService.getPaySystem(instanceId).isNotNeedRRN();
    }
}
```

Все методы принимают на вход номер экземпляра драйвера.

Метод оплаты принимает на вход информацию об оплате (сумму), методы возврата и отмены дополнительно к этому принимают на вход `РРН` прошлой операции.

<a name="206"></a>
### 1.2.6. Описание класса для работы с оборудованием.

Например, для Банковских Терминалов, работающих через USB, это выглядит следующим образом:

```
public class MyDevice implements IPaySystem {
    @Override
    public PayResult payment(PayInfo payInfo) {
        //TODO Ваш код
    }

    @Override
    public PayResult cancelPayment(PayInfo payInfo, String s) {
        //TODO Ваш код
    }

    @Override
    public PayResult payback(PayInfo payInfo, String s) {
        //TODO Ваш код
    }

    @Override
    public PayResult cancelPayback(PayInfo payInfo, String s) {
        //TODO Ваш код
    }

    @Override
    public PayResult closeSession() {
        //TODO Ваш код
    }

    @Override
    public void openServiceMenu() {
        //TODO Ваш код
    }

    @Override
    public String getBankName() {
        //TODO Ваш код
    }

    @Override
    public int getTerminalNumber() {
        //TODO Ваш код
    }

    @Override
    public String getTerminalID() {
        //TODO Ваш код
    }

    @Override
    public String getMerchNumber() {
        //TODO Ваш код
    }

    @Override
    public String getMerchCategoryCode() {
        //TODO Ваш код
    }

    @Override
    public String getMerchEngName() {
        //TODO Ваш код
    }

    @Override
    public String getCashier() {
        //TODO Ваш код
    }

    @Override
    public String getServerIP() {
        //TODO Ваш код
    }

    @Override
    public boolean isNotNeedRRN() {
        //TODO Ваш код
    }
}
```

Для устройств других категорий требуется реализовать соответствующие интерфейсы.

<a name="207"></a>
### 1.2.7. Завершение работы.

Загрузите приложение на смарт-терминал, чтобы работать с Вашим драйвером.

-----
###### Более подробную информацию по разрабатке своих решений для бизнеса на платформе Эвотор, Вы можете найти на нашем сайте для разработчиков: https://developer.evotor.ru/

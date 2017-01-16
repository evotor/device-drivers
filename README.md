# SDK для работы с оборудованием смарт-терминала Эвотор

[![Build Status](https://img.shields.io/travis/evotor/device-drivers/master.svg)](https://travis-ci.org/evotor/device-drivers)

В этом проекте описаны все необходимые интерфейсы, константы и пр., необходимые для работы с оборудованием на смарт-терминале Эвотор и разработки собственных драйверов для него.

## Разработка драйверов для смарт-терминала Эвотор

Разработка драйвера оборудования на примере драйвера для USB-весов:

**1.** Подключить к своему проекту библиотеку для работы с оборудованием. Для этого в build.gradle проекта добавьте ссылку репозиторий `jitpack`:

```
allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

и в `build.gradle` модуля добавьте зависимость следующим образом:

```
dependencies {
    compile 'com.github.evotor:device-drivers:+'
}
```

**2.** В AndroidManifest.xml приложения определите внешний сервис c интент-фильтрами `INTENT_FILTER_DRIVER_MANAGER` и `INTENT_FILTER_SCALES`. 
  >*Здесь и далее по тексту все имена констант указаны из ru.evotor.devices.drivers.Constants.*

```
<service
    android:name="ru.mycompany.drivers.MyScalesService"
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

В манифесте приложения у сервиса должны быть указаны `android:icon` и `android:label` - картинка и имя драйвера (показывается пользователю).

![Пример отображения иконки и имени драйвера](https://github.com/VedbeN/device-drivers/blob/master/icon_xmpl.png?raw=true "Пример отображения иконки и имени драйвера")

Также в манифесте в мета-дате должны быть указаны нужные параметры, имена которых хранятся в константах `SERVICE_META_DATA_*`, по которым сервис оборудования будет понимать для каких целей использовать этот дайвер и какую информацию о нём отобразить пользователю. Например, нужно указать имя и модель целевого устройства, `VID` и `PID`, категорию устройства (`SCALES` для весов).
Vendor ID (`VID`) и Product ID (`PID`) оборудования указываются в формате `VID_1659PID_8963` (десятичные числа).

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

**3.** В реализации метода подключения к сервису для указанных в интент-фильтрах action'ов укажите сответствующие `Binder`'ы:

 для `INTENT_FILTER_DRIVER_MANAGER` - класс наследник ru.evotor.devices.drivers.IUsbDriverManagerService,
 
 для `INTENT_FILTER_SCALES` - класс наследник ru.evotor.devices.drivers.IScalesDriverService.

```
import ru.evotor.devices.drivers.Constants;

public class MyScalesService extends Service {

    private final Map<Integer, MyScales> instances = new HashMap<>();
    private volatile AtomicInteger newDeviceIndex = new AtomicInteger(0);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case Constants.INTENT_FILTER_DRIVER_MANAGER:
                return new MyDriverManagerStub(MyScalesService.this);
            case Constants.INTENT_FILTER_SCALES:
                return new MyScalesStub(MyScalesService.this);
            default:
                return null;
        }
    }
	
    public int createNewDevice(UsbDevice usbDevice) {
        int currentIndex = newDeviceIndex.getAndIncrement();
        instances.put(currentIndex, new MyScales(getApplicationContext(), usbDevice));
        return currentIndex;
    }

    public MyScales getMyScales(int instanceId) {
        return instances.get(instanceId);
    }
}
```

В этом же сервисе удобно определить Map для хранения списка активных экземпляров драйверов (а их, потенциально, может быть больше чем 1 в системе одновременно), т.к. обращаться к нему придётся и из `MyDriverManagerStub`, и из `MyScalesStub`.

**4.** Опишите указанные Binder'ы.

`IUsbDriverManagerService.Stub` - класс для управления драйверами: подключение и отключение устройств происходят здесь. Надо реализовать методы `addUsbDevice` и `destroy`.

```
import ru.evotor.devices.drivers.IUsbDriverManagerService;

public class MyDriverManagerStub extends IUsbDriverManagerService.Stub {

    private MyScalesService myScalesService;

    public MyDriverManagerStub(MyScalesService myScalesService) {
        this.myScalesService = myScalesService;
    }

    @Override
    public int addUsbDevice(UsbDevice usbDevice, String usbPortPath) throws RemoteException {
        return myScalesService.createNewDevice(usbDevice);
    }

    @Override
    public void destroy(int i) throws RemoteException {

    }
}
```

Метод `addUsbDevice` в `IUsbDriverManagerService` принимает на вход 

1) `UsbDevice`, для которого он создан, 

2) Некоторый строковый идентификатор номера физического usb-порта (может потребоваться, например, если надо сохранить какие-либо настройки оборудования и восстановить их после перезагрузки терминала). В этот момент у приложения-драйвера уже есть permission для работы с этим устройством.
Метод `addUsbDevice` возвращает номер экземпляра драйвера внутри приложения. По этому номеру будет происходить обращение к конкретному драйверу.

Метод `destroy` в `IUsbDriverManagerService` принимает на вход номер экземпляра драйвера. Вызов этого метода уведомляет приложение об отключении от устройства. В этот момент у приложения-драйвера уже нет permission для работы с этим устройством, само устройство уже может быть удалено из смарт-терминала.

`IScalesDriverService.Stub` - класс для работы с конкретными экземплярами весов. Надо реализовать метод `getWeight`.

```
import ru.evotor.devices.drivers.IScalesDriverService;
import ru.evotor.devices.drivers.scales.Weight;

public class MyScalesStub extends IScalesDriverService.Stub {

    private MyScalesService myScalesService;

    public MyScalesStub(MyScalesService myScalesService) {
        this.myScalesService = myScalesService;
    }
	
    @Override
    public Weight getWeight(int instanceId) throws RemoteException {
        return myScalesService.getMyScales(instanceId).getWeight();
    }
}

```

Метод `getWeight` в `IScalesDriverService` принимает на вход номер экземпляра драйвера (тот, который вернул `addUsbDevice` на прошлом шаге).

Метод `getWeight` возвращает объект класса ru.evotor.devices.drivers.scales.Weight. В конструкторе требуется указать:

 1) `originalWeight` - считанный вес, в тех единицах измерения, в которых его вернули весы,

 2) `multiplierToGrams` - коэффициент для приведения веса в граммы,

 3) `supportStable` - поддерживают ли весы флаг стабильности,

 4) `stable` - флаг стабильности взвешивания, если поддерживается. Иначе - любое значение.

**5.** После того, как описаны все классы для взаимодействия с инфраструктурой смарт-терминала, можно описать сам класс работы с весами:

```
import ru.evotor.devices.drivers.scales.IScales;
import ru.evotor.devices.drivers.scales.Weight;

public class MyScales implements IScales {

    private Context context;
    private UsbDevice usbDevice;

    private static final boolean SUPPORT_STABLE = true;

    public MyScales(Context context, UsbDevice usbDevice) {
        super();
        this.context = context;
        this.usbDevice = usbDevice;
    }

    @Override
    public Weight getWeight() {
        //TODO Ваш код запроса веса
    }
}
```

При запросе на взвешивание со стороны кассового приложения будет вызван метод `getWeight`. В случае невозмжности вернуть считанный вес (например, ошибки связи) следует выбросить `RuntimeException`.

**6.** Всё готово. Загрузите приложение на смарт-терминал, чтобы работать с весами.

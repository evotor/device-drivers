
[Главная страница](https://github.com/Draudr/device-drivers/blob/New_structure_of_SDK_manual/README.md) > Подготовка к разработке
> * Ниже описаны шаги, которые необходимо выполнить прежде, чем приступтаь к разработке драйверов, которая описана в других разделах.  
> * Здесь и далее по тексту все имена констант указаны из `ru.evotor.devices.drivers.Constants`.

# 1. __Подготовка к разработке.__

_Содержание:_   
1.1. [Подключение к своему проекту библиотеку для работы с оборудованием.](#101)  
1.2. [Определение внешний сервис в `AndroidManifest.xml` приложения.](#102)  

<a name="101"></a>
## 1.1 Подключение к своему проекту библиотеку для работы с оборудованием.
Для этого в build.gradle проекта добавьте ссылку репозиторий jitpack:

```
allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```
Для этого в `build.gradle` проекта добавьте ссылку репозиторий jitpack:

```
allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

и в модуле `build.gradle` добавьте зависимость следующим образом:

```
dependencies {
    compile 'com.github.evotor:device-drivers:+'
}
```
<a name="102"></a>
### 2. Определение внешний сервис в `AndroidManifest.xml` приложения.

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

Такой драйвер может быть создан только пользователем вручную через меню настройки оборудования. В этом случае все работы по подключению к нужному устройству берёт на себя производитель драйвера.

-----
###### Более подробную информацию по разрабатке своих решений для бизнеса на платформе Эвотор, Вы можете найти на нашем сайте для разработчиков: https://developer.evotor.ru/

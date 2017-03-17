[Главная страница](https://github.com/Draudr/device-drivers/blob/New_structure_of_SDK_manual/README.md) > SSDK для принтера Эвотор


# __2.1. SDK для принтера Эвотор__
_Содержание:_  
2.1.1. [Подключение библиотеки для работы с оборудованием к своему проекту.](#2011)  
2.1.2. [Инициализация класса `ru.evotor.devices.commons.DeviceServiceConnector.`](#2012)
2.1.3. [Вызов метода `DeviceServiceConnector.getPrinterService()` и взаимодействие с его ответом.](#2013)  
2.1.4. [Передача данных в печать.  ](#2014)  
2.1.5. [Особенности работы оборудования с `DeviceServiceConnector`.](#2015)  


<a name="2011"></a>
### 2.1.1. Подключение библиотеки для работы с оборудованием к своему проекту.

Для этого в `build.gradle` проекта добавьте ссылку репозиторий jitpack:

```
allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

в модуле `build.gradle` добавьте зависимость и укажите точную версию (текущая: 1.2.0+):

```
dependencies {
    compile 'com.github.evotor:device-drivers:1.2.0+'
}
```
<a name="2012"></a>
### 2.1.2. Инициализация класса `ru.evotor.devices.commons.DeviceServiceConnector`  
Для того, что бы начать обращаться к принтеру достаточно проинициализировать класс `ru.evotor.devices.commons.DeviceServiceConnector`, который был подставлен в подключенную на прошлом шаге библиотеку. Проинициализирйте его при запуске приложения или старте activity:  
```  
DeviceServiceConnector.initConnections(getApplicationContext());
```
<a name="2013"></a>
### 2.1.3. Вызов метода `DeviceServiceConnector.getPrinterService()` и взаимодействие с его ответом.  
* В ответ на вызов метода  `DeviceServiceConnector.getPrinterService()` вернется объект `ru.evotor.devices.commons.IPrinterService`.  
* В свою очередь у объекта `ru.evotor.devices.commons.IPrinterService` вызвать методы:  
  * `int getAllowableSymbolsLineLength(int deviceId)` - возвращает количество печатных символов, которые помещаются на 1 строке;
  * `int getAllowablePixelLineLength(int deviceId)` - возвращает доступную для печати ширину бумаги в пикселях;  
  * `void printDocument(int deviceId, in PrinterDocument printerDocument)` - печатает указанный массив объектов (тест, штрихкоды, картинки).  

`deviceId` - первый аргумент каждой функции. Отвечает за указание конкретного устройства, для которого будет вызван метод.

При возникновении каких - либо проблем в ходе вызова любого из перечисленных ранее методов - система вернет значение = `Exception`.

> __На данный момент печатать можно только на встроенной в СТ2 ККМ, поэтому вместо номера устройства всегда следует передавать константу `ru.evotor.devices.commons.Constants.DEFAULT_DEVICE_INDEX_UNSET`.__  

<a name="2014"></a>
### 2.1.4. Передача данных в печать.  
Для печати Вам необходимо воспользоваться методом:
```
printDocument((int deviceId, in PrinterDocument printerDocument);
```
`PrinterDocument` - второй аргумент функции. Должен содержать список печатный элементов `IPrintable`:  
* Тексты - `ru.evotor.devices.commons.printer.printable.PrintableText`;
* Штрихкоды - `ru.evotor.devices.commons.printer.printable.PrintableBarcode`;
* Картинки - `ru.evotor.devices.commons.printer.printable.PrintableImage`.

Пример:
```
Bitmap bitmap1 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Pictures/1.png");
DeviceServiceConnector.getPrinterService().printDocument(
	DEFAULT_DEVICE_INDEX_UNSET,
    new PrinterDocument(
            new PrintableText("Первая строка"),
            new PrintableText("Довольно длинный текст, помещающийся лишь на несколько строк"),
            new PrintableBarcode("1234567890", PrintableBarcode.BarcodeType.CODE39),
            new PrintableImage(bitmap1)
    ));
```
<a name="2015"></a>
### 2.1.5. Особенности работы оборудования с `DeviceServiceConnector`.  
При работе оборудования с `DeviceServiceConnector` следует помнить, что методы, по факту, исполняются в другом приложении, в связи с этим могут возникать различные `Exception`. В связи с этим необходимо отслеживать и перехватывать любые `RuntimeException`, так как они могут быть проброшены сквозь приложения.  

Также при работе с другими методами удаленных сервисов стоит вызывать метод `DeviceServiceConnector.processException(Exception exc)` для логирования ошибок и сервисных операций (например, перезапуск соединения по DeadObjectException).

Пример кода печати:  
```
try {
	DeviceServiceConnector.getPrinterService().printDocument(
	DEFAULT_DEVICE_INDEX_UNSET,
  new PrinterDocument(new PrintableText("Первая строка")));
    } catch (RemoteException | RuntimeException exc)
{
  DeviceServiceConnector.processException(exc);
}
```

-----

###### Более подробную информацию по разрабатке своих решений для бизнеса на платформе Эвотор, Вы можете найти на нашем сайте для разработчиков: https://developer.evotor.ru/

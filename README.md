# SDK для работы с оборудованием смарт-терминала Эвотор


В этом проекте описаны все необходимые интерфейсы, константы и пр., необходимые для работы с оборудованием на смарт-терминале Эвотор и разработки собственных драйверов для него.

>_Правила работы с проектом при разработке драйверов:_    
>_1. Необходимо выполнить все действия, описанные в разделе  [Подготовка к _разработке](https://github.com/Draudr/device-drivers/blob/master/Preparation_for_development.md)  
>_2. После того, как подготовка завершена, Вам необходимо перейти в раздел, описывающий разработку для конкретного устройства и продолжить работу непосредственно с ним._  

Разделы проекта:

1. [Подготовка к разработке.](https://github.com/Draudr/device-drivers/blob/New_structure_of_SDK_manual/Preparation_for_development.md)  
1.1. [Подключение к своему проекту библиотеку для работы с оборудованием.](https://github.com/Draudr/device-drivers/blob/master/Preparation_for_development.md#101)  
1.2. [Определение внешний сервис в `AndroidManifest.xml` приложения.](https://github.com/Draudr/device-drivers/blob/New_structure_of_SDK_manual/Preparation_for_development.md#102)  
2. [SDK для Банковских Терминалов.](https://github.com/Draudr/device-drivers/blob/New_structure_of_SDK_manual/README_PinPad.md)
3. [SDK для Денежных Ящиков.]()
4. [SDK для Принтеров Ценников.]()
5. [SDK для Весов.](https://github.com/Draudr/device-drivers/blob/New_structure_of_SDK_manual/README_Scales.md)  
5.1. [Определение роли и категории устройства.](https://github.com/Draudr/device-drivers/blob/New_structure_of_SDK_manual/README_Scales.md#501)  
5.2. [Присвоение картинки для драйвера](https://github.com/Draudr/device-drivers/blob/New_structure_of_SDK_manual/README_Scales.md#502)  
5.3. [В реализации метода подключения к сервису для всех action указанных в интент-фильтрах укажите соответствующие `Binder'ы`](https://github.com/Draudr/device-drivers/blob/New_structure_of_SDK_manual/README_Scales.md#503)  
5.4. [Описание указанных `Binder'ов`.](https://github.com/Draudr/device-drivers/blob/New_structure_of_SDK_manual/README_Scales.md#504)  
5.5. [Описание класса для работы с оборудованием.](https://github.com/Draudr/device-drivers/blob/New_structure_of_SDK_manual/README_Scales.md#505)  
5.6. [Завершение работы.](https://github.com/Draudr/device-drivers/blob/New_structure_of_SDK_manual/README_Scales.md#506)
6. [SDK для других устройств работающих с USB-оборудованием.]()

-----
###### Более подробную информацию по разрабатке своих решений для бизнеса на платформе Эвотор, Вы можете найти на нашем сайте для разработчиков: https://developer.evotor.ru/

package ru.evotor.devices.drivers;

public abstract class Constants {
    public static final String INTENT_FILTER_DRIVER_MANAGER = "ru.evotor.devices.drivers.DriverManager";
    public static final String INTENT_FILTER_SCALES = "ru.evotor.devices.drivers.ScalesService";
    public static final String INTENT_FILTER_PRICE_PRINTER = "ru.evotor.devices.drivers.PricePrinterService";
    public static final String INTENT_FILTER_CASH_DRAWER = "ru.evotor.devices.drivers.CashDrawer";

    // (строка) название производителя устройства
    public static final String SERVICE_META_DATA_TAG_VENDOR_NAME = "vendor_name";
    // (строка) название модели устройства
    public static final String SERVICE_META_DATA_TAG_MODEL_NAME = "model_name";
    // (строка) PID и VID usb-устройства, для которого предназначен драйвер (указывается только для драйверов usb-устройств)/
    // значение указывается в формате "VID_{value_vid}PID_{value_pid}". Допускается несколько значений через "|", например: "VID_123PID_456|VID_789PID_0123".
    public static final String SERVICE_META_DATA_TAG_USB_VID_PID = "usb_device";
    // (булевый) флаг виртуального устройства (не связан с usb-портом, например, какое-то сетевое устройство)
    public static final String SERVICE_META_DATA_TAG_VIRTUAL_DEVICE = "virtual_device";
    // (строка) путь и имя активити настроек драйвера. если не требуется (нет никаких настраеваемых пользователем параметров), можно не указываеть или указать пустым
    public static final String SERVICE_META_DATA_TAG_SETTINGS_ACTIVITY = "settings_activity";
    // (DeviceCategory) категории устройств, прои добавлении которых будет предложено подключить этот драйвер. Может быть указано несколько категорий через символ "|" (например, "SCALES|CASHDRAWER").
    public static final String SERVICE_META_DATA_TAG_DEVICE_CATEGORIES = "device_categories";
    // (строка) имя extra, в которой будет передан номер экземпляра для окна настроек
    public static final String SETTINGS_ACTIVITY_EXTRA_INSTANCE_ID = "instance_id";

    // доступные категории устройств
    public enum DeviceCategory {
        SCALES,
        CASHDRAWER
    }

}

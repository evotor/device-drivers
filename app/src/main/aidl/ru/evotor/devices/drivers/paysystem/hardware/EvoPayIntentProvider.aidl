// EvoPayIntentProvider.aidl
package ru.evotor.devices.drivers.paysystem.hardware;

import android.content.Intent;

interface EvoPayIntentProvider {

    /*nullable*/ Intent provide() = 1;

    const String ACTION = "ru.evotor.action.PROVIDE_EVO_PAY_INTENT";
}

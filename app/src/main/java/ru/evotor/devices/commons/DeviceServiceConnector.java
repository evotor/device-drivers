package ru.evotor.devices.commons;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.util.Log;

import ru.evotor.devices.publics.R;

import static ru.evotor.devices.commons.Constants.ACTION_PRINTER_SERVICE;
import static ru.evotor.devices.commons.Constants.DEVICE_SERVICE_CLASS_NAME;
import static ru.evotor.devices.commons.Constants.DEVICE_SERVICE_PACKAGE;

public class DeviceServiceConnector {

    protected static final String TAG = "DeviceServiceConnector";
    protected static final String TARGET_PACKAGE = DEVICE_SERVICE_PACKAGE;
    protected static final String TARGET_CLASS_NAME = DEVICE_SERVICE_CLASS_NAME;

    protected static Context context;

    protected static volatile IPrinterService printerService;
    protected static volatile ServiceConnection printerServiceConnection;

    public static IPrinterService getPrinterService() {
        if (printerService == null) {
            initPrinterServiceConnection(false);
        }
        return printerService;
    }

    private static void initPrinterServiceConnection(boolean force) {
        if (context == null) {
            return;
        }
        if (printerServiceConnection == null || force) {
            printerServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    printerService = IPrinterService.Stub.asInterface(service);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    printerService = null;
                    initPrinterServiceConnection(false);
                }
            };
        }
        if (printerService == null || force) {
            Intent pr = new Intent(ACTION_PRINTER_SERVICE);
            pr.setPackage(TARGET_PACKAGE);
            pr.setClassName(TARGET_PACKAGE, TARGET_CLASS_NAME);
            boolean waitPrinterServiceConnection = context.bindService(pr, printerServiceConnection, Service.BIND_AUTO_CREATE);
        }
    }

    public static void initConnections(final Context appContext) {
        initConnections(appContext, false);
    }

    public static void initConnections(final Context appContext, boolean force) {
        if (appContext == null) {
            return;
        }
        DeviceServiceConnector.context = appContext;
        initPrinterServiceConnection(force);
    }


    public static void deInitConnections() {
        if (context == null) {
            return;
        }
        if (printerServiceConnection != null) {
            context.unbindService(printerServiceConnection);
            printerServiceConnection = null;
            printerService = null;
        }
    }

    public static void processException(Exception exc) {
        if (exc instanceof DeadObjectException) {
            DeviceServiceConnector.initConnections(context, true);
        }
        Log.e(TAG, getRemoteExceptionMessage(exc));
    }

    public static String getRemoteExceptionMessage(Exception exc) {

        if (exc == null || exc.getMessage() == null || exc.getMessage().isEmpty()) {
            return context.getString(R.string.error_in_remote_service_unknown);
        } else {
            return exc.getMessage();
        }
    }

}

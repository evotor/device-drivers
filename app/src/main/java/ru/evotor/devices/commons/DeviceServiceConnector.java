package ru.evotor.devices.commons;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DeviceServiceConnector {

    protected static final String TAG = "DeviceServiceConnector";

    public static final String TARGET_PACKAGE = "ru.evotor.devices";
    public static final String TARGET_CLASS_NAME = "ru.evotor.devices.DeviceService";

    public static final String ACTION_PRINTER_SERVICE = "ru.evotor.devices.PrintService";

    protected static Context context;

    protected static volatile IPrinterService printerService;
    protected static volatile ServiceConnection printerServiceConnection;

    protected static volatile List<ConnectionWrapper> connectionWrappers = new ArrayList<>();

    public static void addConnectionWrapper(ConnectionWrapper connectionWrapper) {
        connectionWrappers.add(connectionWrapper);
    }

    public static void removeConnectionWrapper(ConnectionWrapper connectionWrapper) {
        connectionWrappers.remove(connectionWrapper);
    }

    public static void clearConnectionWrappers() {
        connectionWrappers.clear();
    }

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
                    for (ConnectionWrapper connectionWrapper : connectionWrappers) {
                        connectionWrapper.onPrinterServiceConnected(printerService);
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    printerService = null;
                    initPrinterServiceConnection(false);
                    for (ConnectionWrapper connectionWrapper : connectionWrappers) {
                        connectionWrapper.onPrinterServiceDisconnected();
                    }
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
            for (ConnectionWrapper connectionWrapper : connectionWrappers) {
                connectionWrapper.onPrinterServiceDisconnected();
            }
        }
    }

    public static void processException(Exception exc) {
        if (exc instanceof DeadObjectException) {
            DeviceServiceConnector.initConnections(context, true);
        }
        Log.e(TAG, exc == null || exc.getMessage() == null ? "-" : exc.getMessage());
    }
}
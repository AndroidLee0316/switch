package com.pasc.debug.component.door;

import android.app.Application;

import com.pasc.lib.hiddendoor.utils.HiddenDoorManager;

public class TheApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initHiddenDoor();
    }

    private void initHiddenDoor() {
        HiddenDoorManager.getInstance().init(this,"https://apihost", "https://h5host", "gateway123");
        HiddenDoorManager.getInstance().setLogCatchCrashDefault(true);
        HiddenDoorManager.getInstance().setLogPrintAndroidDefault(true);
        HiddenDoorManager.getInstance().setLogReportNetDefault(false);
        HiddenDoorManager.getInstance().setLogPrintFileDefault(false);
        HiddenDoorManager.getInstance().saveValue("customString", HiddenDoorManager.getInstance().getValue("liss", 123));
    }
}

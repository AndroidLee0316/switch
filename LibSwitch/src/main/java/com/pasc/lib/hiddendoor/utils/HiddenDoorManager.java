package com.pasc.lib.hiddendoor.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class HiddenDoorManager {
    private static final String SP_HIDDEN_FILE = "hidden_door";
    private static final String SP_HIDDEN_CUSTOM_FILE = "hidden_door_custom";
    private static final String KEY_API_HOST = "api_host";
    private static final String KEY_H5_HOST = "h5_host";
    private static final String KEY_GATEWAY = "gate_way";
    private static final String KEY_LOG_PRINT_FILE = "log_print_file";
    private static final String KEY_LOG_PRINT_ANDROID = "log_print_android";
    private static final String KEY_LOG_REPORT_NET = "log_report_net";
    private static final String KEY_LOG_CATCH_CRASH = "log_catch_crash";

    private boolean mIsLogCatchCrash = true;
    private boolean mIsLogReportNet = false;
    private boolean mIsLogPrintFile = false;
    private boolean mIsLogPrintAndroid = false;
    private String mApiHost;
    private String mH5Host;
    private String mGateWay;
    private Context mContext;
    private SharedPreferences mSp;
    private SharedPreferences mCustomSp;
    private boolean mIsInited = false;

    public static HiddenDoorManager getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final HiddenDoorManager instance = new HiddenDoorManager();
    }

    public void init(Context context, String apiHost, String h5Host, String gateWay) {
        if (context == null) {
            throw new NullPointerException("hidden door init() context is null");
        }
        mContext = context;
        mApiHost = apiHost;
        mH5Host = h5Host;
        mGateWay = gateWay;
        mIsInited = true;
    }

    public boolean isInited() {
        return mIsInited;
    }

    public void setLogPrintFileDefault(boolean value) {
        mIsLogPrintFile = value;
    }

    public void setLogPrintAndroidDefault(boolean value) {
        mIsLogPrintAndroid = value;
    }

    public void setLogCatchCrashDefault(boolean value) {
        mIsLogCatchCrash = value;
    }

    public void setLogReportNetDefault(boolean value) {
        mIsLogReportNet = value;
    }

    private SharedPreferences obtainSp() {
        if (mSp == null) {
            mSp = mContext.getSharedPreferences(SP_HIDDEN_FILE, Context.MODE_PRIVATE);
        }
        return mSp;
    }

    private SharedPreferences obtainCustomSp() {
        if (mCustomSp == null) {
            mCustomSp = mContext.getSharedPreferences(SP_HIDDEN_CUSTOM_FILE, Context.MODE_PRIVATE);
        }
        return mCustomSp;
    }

    public String getApiHost() {
        if (mIsInited) {
            return obtainSp().getString(KEY_API_HOST, mApiHost);
        }
        return mApiHost;
    }

    public String getH5Host() {
        if (mIsInited) {
            return obtainSp().getString(KEY_H5_HOST, mH5Host);
        }
        return mH5Host;
    }

    public String getGateway() {
        if (mIsInited) {
            return obtainSp().getString(KEY_GATEWAY, mGateWay);
        }
        return mGateWay;
    }

    public boolean isOpenCatchCrash() {
        if (mIsInited) {
            return obtainSp().getBoolean(KEY_LOG_CATCH_CRASH, mIsLogCatchCrash);
        }
        return mIsLogCatchCrash;
    }

    public boolean isOpenFilePrinter() {
        if (mIsInited) {
            return obtainSp().getBoolean(KEY_LOG_PRINT_FILE, mIsLogPrintFile);
        }
        return mIsLogPrintFile;
    }

    public boolean isOpenAndroidPrinter() {
        if (mIsInited) {
            return obtainSp().getBoolean(KEY_LOG_PRINT_ANDROID, mIsLogPrintAndroid);
        }
        return mIsLogPrintAndroid;
    }

    public boolean isOpenReportNet() {
        if (mIsInited) {
            return obtainSp().getBoolean(KEY_LOG_REPORT_NET, mIsLogReportNet);
        }
        return mIsLogReportNet;
    }


    public void saveApiHost(String apiUrl) {
        if (mIsInited) {
            obtainSp().edit().putString(KEY_API_HOST, apiUrl).commit();
        }
    }

    public void saveH5Host(String h5Url) {
        if (mIsInited) {
            obtainSp().edit().putString(KEY_H5_HOST, h5Url).commit();
        }
    }

    public void saveGateWay(String gateWay) {
        if (mIsInited) {
            obtainSp().edit().putString(KEY_GATEWAY, gateWay).commit();
        }
    }

    public void saveOpenCatchCrash(boolean isOpen) {
        if (mIsInited) {
            obtainSp().edit().putBoolean(KEY_LOG_CATCH_CRASH, isOpen).commit();
        }
    }

    public void saveOpenReportLog(boolean isOpen) {
        if (mIsInited) {
            obtainSp().edit().putBoolean(KEY_LOG_REPORT_NET, isOpen).commit();
        }
    }

    public void saveOpenFilePrinter(boolean isOpen) {
        if (mIsInited) {
            obtainSp().edit().putBoolean(KEY_LOG_PRINT_FILE, isOpen).commit();
        }
    }

    public void saveOpenAndroidPrinter(boolean isOpen) {
        if (mIsInited) {
            obtainSp().edit().putBoolean(KEY_LOG_PRINT_ANDROID, isOpen).commit();
        }
    }

    public void saveValue(String key, boolean value) {
        if (mIsInited) {
            obtainCustomSp().edit().putBoolean(key, value).commit();
        }
    }

    public void saveValue(String key, String value) {
        if (mIsInited) {
            obtainCustomSp().edit().putString(key, value).commit();
        }
    }

    public void saveValue(String key, long value) {
        if (mIsInited) {
            obtainCustomSp().edit().putLong(key, value).commit();
        }
    }

    public void saveValue(String key, float value) {
        if (mIsInited) {
            obtainCustomSp().edit().putFloat(key, value).commit();
        }
    }

    public void saveValue(String key, int value) {
        if (mIsInited) {
            obtainCustomSp().edit().putInt(key, value).commit();
        }
    }

    public int getValue(String key, int defult) {
        if (mIsInited) {
            return obtainCustomSp().getInt(key, defult);
        }
        return defult;
    }

    public boolean getValue(String key, boolean defult) {
        if (mIsInited) {
            return obtainCustomSp().getBoolean(key, defult);
        }
        return defult;
    }

    public float getValue(String key, float defult) {
        if (mIsInited) {
            return obtainCustomSp().getFloat(key, defult);
        }
        return defult;
    }

    public long getValue(String key, long defult) {
        if (mIsInited) {
            return obtainCustomSp().getLong(key, defult);
        }
        return defult;
    }

    public String getValue(String key, String defult) {
        if (mIsInited) {
            return obtainCustomSp().getString(key, defult);
        }
        return defult;
    }

    public Map<String, ?> getAll() {
        return obtainCustomSp().getAll();
    }

}

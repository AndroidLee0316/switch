package com.pasc.lib.hiddendoor;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.pasc.lib.base.activity.BaseActivity;
import com.pasc.lib.base.util.StatusBarUtils;
import com.pasc.lib.hiddendoor.utils.HiddenDoorManager;
import com.pasc.lib.hiddendoor.utils.ProcessPhoenix;
import com.pasc.lib.widget.DensityUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SwitchActivity extends BaseActivity {
    private Switch mRestartAppSwtich;
    private Switch mFilePrinterSwtich;
    private Switch mAndroidPrinterSwtich;
    private Switch mCatchCrashSwtich;
    private Switch mReportNetSwtich;
    private Switch mHttpsSwtich;
    private EditText mApiHostEditText;
    private EditText mH5HostEditText;
    private EditText mGateWayEditText;
    private Button mButton;
    private LinearLayout mCustomLayout;
    private List<CustomView> mCustomViews = new ArrayList<>();

    @Override
    protected int layoutResId() {
        return R.layout.activity_switch;
    }

    @Override
    protected void onInit(@Nullable Bundle bundle) {
        mRestartAppSwtich = findViewById(R.id.restart_app);
        mHttpsSwtich = findViewById(R.id.http);

        mReportNetSwtich = findViewById(R.id.log_report_net);
        mFilePrinterSwtich = findViewById(R.id.log_file_printer);
        mAndroidPrinterSwtich = findViewById(R.id.log_android_printer);
        mCatchCrashSwtich = findViewById(R.id.log_catch_crash);
        mApiHostEditText = findViewById(R.id.et_api_host);
        mH5HostEditText = findViewById(R.id.et_h5_host);
        mGateWayEditText = findViewById(R.id.et_gate_way);
        mButton = findViewById(R.id.save);
        mCustomLayout = findViewById(R.id.custom_layout);

        mApiHostEditText.setText(HiddenDoorManager.getInstance().getApiHost());
        mH5HostEditText.setText(HiddenDoorManager.getInstance().getH5Host());
        mGateWayEditText.setText(HiddenDoorManager.getInstance().getGateway());

        mHttpsSwtich.setChecked(HiddenDoorManager.getInstance().getApiHost().contains("https"));
        mAndroidPrinterSwtich.setChecked(HiddenDoorManager.getInstance().isOpenAndroidPrinter());
        mFilePrinterSwtich.setChecked(HiddenDoorManager.getInstance().isOpenFilePrinter());
        mReportNetSwtich.setChecked(HiddenDoorManager.getInstance().isOpenReportNet());
        mCatchCrashSwtich.setChecked(HiddenDoorManager.getInstance().isOpenCatchCrash());

        mRestartAppSwtich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
                mApiHostEditText.setEnabled(flag);
                mH5HostEditText.setEnabled(flag);
                mGateWayEditText.setEnabled(flag);
                mHttpsSwtich.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRestartAppSwtich.isChecked()) {
                    String h5 = mH5HostEditText.getText().toString();
                    if (TextUtils.isEmpty(h5)) {
                        Toast.makeText(SwitchActivity.this, "h5地址不能为空", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String api = mApiHostEditText.getText().toString();
                    if (TextUtils.isEmpty(api)) {
                        Toast.makeText(SwitchActivity.this, "api地址不能为空", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String gateWay = mGateWayEditText.getText().toString();
                    HiddenDoorManager.getInstance().saveApiHost(api);
                    HiddenDoorManager.getInstance().saveH5Host(h5);
                    HiddenDoorManager.getInstance().saveGateWay(gateWay);
                }
                HiddenDoorManager.getInstance().saveOpenAndroidPrinter(mAndroidPrinterSwtich.isChecked());
                HiddenDoorManager.getInstance().saveOpenFilePrinter(mFilePrinterSwtich.isChecked());
                HiddenDoorManager.getInstance().saveOpenReportLog(mReportNetSwtich.isChecked());
                HiddenDoorManager.getInstance().saveOpenCatchCrash(mCatchCrashSwtich.isChecked());
                saveCustomView();
                if (mRestartAppSwtich.isChecked()) {
                    ProcessPhoenix.triggerRebirth(SwitchActivity.this);
                } else {
                    finish();
                }
            }
        });

        mHttpsSwtich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bool) {
                String h5 = mH5HostEditText.getText().toString();
                String api = mApiHostEditText.getText().toString();

                if (bool) {
                    if (!TextUtils.isEmpty(h5) && h5.contains("http:")) {
                        mH5HostEditText.setText(h5.replace("http", "https"));
                    }
                    if (!TextUtils.isEmpty(api) && api.contains("http:")) {
                        mApiHostEditText.setText(api.replace("http", "https"));
                    }
                } else {
                    if (!TextUtils.isEmpty(h5) && h5.contains("https:")) {
                        mH5HostEditText.setText(h5.replace("https", "http"));
                    }
                    if (!TextUtils.isEmpty(api) && api.contains("https:")) {
                        mApiHostEditText.setText(api.replace("https", "http"));
                    }
                }
            }
        });

        handleCustom();
    }

    private void saveCustomView() {
        for (CustomView customView : mCustomViews) {
            customView.save();
        }
    }

    private void handleCustom() {
        Map<String, ?> customMap = HiddenDoorManager.getInstance().getAll();
        if (customMap != null && customMap.size() > 0) {
            Iterator<String> iterator = customMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = customMap.get(key);
                addCustomLayout(key, value);
            }
            mCustomLayout.setVisibility(View.VISIBLE);
        } else {
            mCustomLayout.setVisibility(View.GONE);
        }
    }

    private void addCustomLayout(String key, Object value) {
        CustomView customView = new CustomView(key, value);
        LinearLayout.LayoutParams params = null;
        if (value instanceof Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                customView.setView(new Switch(this));
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        } else {
            customView.setView(new EditText(this));
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        if (customView.getView() != null) {
            params.leftMargin = DensityUtils.dp2px(10);
            mCustomLayout.addView(customView.getView(), params);
            mCustomViews.add(customView);
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtils.setStatusBarColor(this, true);
    }

    class CustomView {

        protected String tag;
        protected Object defaultValue;
        private View view;

        CustomView(String tag, Object defaultValue) {
            this.tag = tag;
            this.defaultValue = defaultValue;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
            if (view instanceof EditText) {
                ((EditText) view).setText("" + defaultValue);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    ((Switch)view).setText(tag + ": ");
                    ((Switch)view).setChecked((boolean)defaultValue);
                }
            }
        }

        public void save() {
            if (view instanceof EditText) {
                String value = ((EditText) view).getText().toString();
                try {
                    if (defaultValue instanceof String) {
                        HiddenDoorManager.getInstance().saveValue(tag, value);
                    } else if (defaultValue instanceof Integer) {
                        HiddenDoorManager.getInstance().saveValue(tag, Integer.valueOf(value));
                    } else if (defaultValue instanceof Float) {
                        HiddenDoorManager.getInstance().saveValue(tag, Float.valueOf(value));
                    } else if (defaultValue instanceof Long) {
                        HiddenDoorManager.getInstance().saveValue(tag, Long.valueOf(value));
                    }
                } catch (Exception e) {

                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    HiddenDoorManager.getInstance().saveValue(tag, ((Switch)view).isChecked());
                }
            }
        }
    }
}

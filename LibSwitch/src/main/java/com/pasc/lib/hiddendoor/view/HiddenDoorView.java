package com.pasc.lib.hiddendoor.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pasc.lib.hiddendoor.SwitchActivity;
import com.pasc.lib.hiddendoor.utils.HiddenDoorManager;
import com.pasc.lib.log.PascLog;

public class HiddenDoorView extends TextView {
    private int count = 0;
    private long lastClickTime;
    private boolean isEnterDoor;
    private boolean isResetConfig;
    private DoorCallBack mCallBack;
    private OnClickListener mListener;
    private boolean isDoorEnable;

    public HiddenDoorView(Context context) {
        super(context);
    }

    public HiddenDoorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HiddenDoorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void createClickListener() {
        mListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (System.currentTimeMillis() - lastClickTime > 1000) {
                    count = 0;
                }
                count++;
                lastClickTime = System.currentTimeMillis();
                if (count == 8) {
                    count = 0;
                    startSwitchActivity(getContext());
                } else if (count == 5) {
                    Toast.makeText(getContext(), "再快速点击三次进入暗门", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void startSwitchActivity(Context context) {
        Intent intent = new Intent(context, SwitchActivity.class);
        context.startActivity(intent);
        isEnterDoor = true;
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (!isDoorEnable) {
            return;
        }
        if (isResetConfig) {
            isEnterDoor = false;
            isResetConfig = false;
            if (mCallBack != null) {
                mCallBack.resetConfig();
            } else {
                PascLog.openFilePrinter(HiddenDoorManager.getInstance().isOpenFilePrinter());
                PascLog.openAndroidPrinter(HiddenDoorManager.getInstance().isOpenAndroidPrinter());
                PascLog.openCatchCrash(HiddenDoorManager.getInstance().isOpenCatchCrash());
                PascLog.openReportLog(HiddenDoorManager.getInstance().isOpenReportNet());
            }
        }
        isResetConfig = isEnterDoor;
    }

    public void setDoorEnable(boolean enable) {
        if (!HiddenDoorManager.getInstance().isInited()) {
            return;
        }
        isDoorEnable = enable;
        if (enable) {
            if (mListener == null) {
                createClickListener();
            }
            setOnClickListener(mListener);
        } else {
            setOnClickListener(null);
        }
    }

    public void setCallBack(DoorCallBack callBack) {
        mCallBack = callBack;
        setDoorEnable(true);
    }

    public static interface DoorCallBack {
        public void resetConfig();
    }

}

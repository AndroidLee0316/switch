package com.pasc.debug.component.door;

import android.app.Activity;
import android.os.Bundle;

import com.pasc.lib.hiddendoor.view.HiddenDoorView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((HiddenDoorView)findViewById(R.id.door)).setDoorEnable(true);
    }

}

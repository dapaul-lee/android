package com.dp.test.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.dp.test.R;
import com.dp.test.util.PermissionsUtil;
import com.dp.test.util.ToastUtil;

import static com.dp.test.util.PermissionsUtil.getAppDetailSettingIntent;

public class PermissionsActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check_permission_camera:
                if (PermissionsUtil.isCameraCanUse()) {
                    ToastUtil.showTaost(this, "Permission camera granted");
                } else {
                    ToastUtil.showTaost(this, "Permission camera denied");
                    getAppDetailSettingIntent(this);
                }
                break;
            case R.id.btn_request_permission_camera:
                break;
        }
    }
}

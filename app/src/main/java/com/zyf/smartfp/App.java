package com.zyf.smartfp;

import com.zyf.common.SharedPreferencesUtil;
import com.zyf.common.app.Application;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //开启一个伴随app的sharedpreference存储
        new SharedPreferencesUtil(getApplicationContext(), "local");
    }

}

package com.zyf.factory.presenter;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import com.zyf.common.app.Application;
import com.zyf.factory.contract.Contract_login;
import com.zyf.factory.service.SocketService;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

public class Presenter_login implements Contract_login.Presenter {

    static Context context;
    static Contract_login.View mView;
    static Presenter_login presenter;


    private Presenter_login(Contract_login.View view){
        mView = view;

    }



    //提供给View层的获取单例对象的方法
    public static Presenter_login getInstance(Contract_login.View view){
        if (presenter==null){
            presenter = new Presenter_login(view);
        }
        context = (Context) view;
        return presenter;
    }

    //登陆
    @Override
    public void login(String account,String password) {
        if(account.equals("root")&&password.equals("password"))
        {
            mView.showMainActivity();
        }

    }



    //销毁
    public void onDestroy(){

    }
}

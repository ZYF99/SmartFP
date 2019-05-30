package com.zyf.factory.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.zyf.common.app.Application;
import com.zyf.factory.contract.Contract_detail;
import com.zyf.factory.model.SmartData;
import com.zyf.factory.model.SmartEvent;
import com.zyf.factory.service.SocketService;

import java.util.Arrays;
import java.util.List;

public class Presenter_detail implements Contract_detail.Presenter {
    private final static String TAG = "Presenter_detail";
    static Context context;
    Contract_detail.View mView;
    static Presenter_detail presenter;
    SocketService service;
    SmartData smartData;
    int position;

    public int getPosition() {
        return position;
    }

    //Socket服务绑定的回调
    private ServiceConnection serviceConnection = new ServiceConnection() {
        //连接服务成功回调
        @Override
        public void onServiceConnected(ComponentName componentName, final IBinder iBinder) {
            SocketService.SocketBinder binder = (SocketService.SocketBinder) iBinder;
            service = binder.getService();
            //((SocketService.SocketBinder) iBinder).initSocket(Common.host,Common.port);
            service.setDetailListener(new SocketService.Listener() {
                //链接socket服务器成功的回调
                @Override
                public void onConnectSuccess() {


                }

                //Socket接收到消息的回调
                @Override
                public void onReceiveMsg(List<SmartData> list) {

                    if (list.size() > position) {
                        Log.d(TAG, "onReceiveMsg: " + list.get(position));
                        smartData = list.get(position);

                        Log.d(TAG, "mView: " + mView.toString());
                        mView.setText(smartData);
                    }
                }

                @Override
                public void onSocketError(Exception e) {
                    mView.showError(e);
                }
            });


        }


        //服务连接失败回调
        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    private Presenter_detail(Contract_detail.View view) {
        mView = view;
    }

    //提供给View层的获取单例对象的方法
    public static Presenter_detail getInstance(Contract_detail.View view, int position) {
        presenter = new Presenter_detail(view);
        presenter.position = position;
        context = (Context) view;
        return presenter;
    }

    public void initData() {
        context.bindService(new Intent(context.getApplicationContext(), SocketService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    //发送开关灯命令
    @Override
    public void turnLight() {
        if (smartData.isLightOn()) {
            //发关闭命令
            SmartEvent smartEvent = new SmartEvent(position + 1 + "", "10", "1");
            Log.d(TAG, Arrays.toString(smartEvent.getSmartBytes()));
            sendMsg(new SmartEvent(position + 1 + "", "10", "1"));

        } else {
            //发开启命令
            sendMsg(new SmartEvent(position + 1 + "", "10", "0"));
        }

    }

    void sendMsg(SmartEvent smartEvent) {
        SocketService.smartEvent = smartEvent;
        SocketService.write = true;
    }

    public void onDestroy() {
        Application.socketIsConnecting = false;
        service.setDetailListener(null);
        context.unbindService(serviceConnection);
    }

}

package com.zyf.factory.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.zyf.common.Common;
import com.zyf.common.app.Application;
import com.zyf.factory.contract.Contract_main;
import com.zyf.factory.model.SmartData;
import com.zyf.factory.service.SocketService;
import java.util.List;


public class Presenter_main implements Contract_main.Presenter {

    static Context context;
    static Contract_main.View mainView;
    static Presenter_main presenter;

    private Presenter_main(Contract_main.View view) {
        Application.socketIsConnecting = true;
        mainView = view;

    }

    //Socket服务绑定的回调
    private static ServiceConnection serviceConnection = new ServiceConnection() {
        //连接服务成功回调
        @Override
        public void onServiceConnected(ComponentName componentName, final IBinder iBinder) {
            SocketService.SocketBinder binder = (SocketService.SocketBinder) iBinder;
            SocketService service = binder.getService();
            ((SocketService.SocketBinder) iBinder).initSocket(Common.host,Common.port);
            service.setListener(new SocketService.Listener() {
                //链接socket服务器成功的回调
                @Override
                public void onConnectSuccess() {

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mainView.hideLoading();
                }

                //Socket接收到消息的回调
                @Override
                public void onReceiveMsg(List<SmartData> list) {
                    mainView.updateList(list);
                }

                @Override
                public void onSocketError(Exception e) {
                    mainView.showError(e);
                    mainView.showLoading();
                }
            });


        }


        //服务连接失败回调
        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    //提供给View层的获取单例对象的方法
    public static Contract_main.Presenter getInstance(Contract_main.View view) {
        if (presenter == null) {
            presenter = new Presenter_main(view);
        }
        context = (Context) view;

        return presenter;
    }

    @Override
    public void initSocket() {
        context.bindService(new Intent(context.getApplicationContext(), SocketService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    //销毁
    @Override
    public void onDestroy() {
        Application.socketIsConnecting = false;
        context.unbindService(serviceConnection);
    }
}

package com.zyf.factory.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.zyf.factory.model.SmartData;
import com.zyf.factory.model.SmartEvent;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class SocketService extends Service {


    public static final String TAG = "SocketService";
    private Listener listener;
    private Listener detailListener;
    private Socket socket;
    public boolean isFirstInit = true;
    public static boolean write = false;
    public static SmartEvent smartEvent;



    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "OnBind");
        return new SocketBinder();
    }


    public class SocketBinder extends Binder {

        public SocketService getService() {
            return SocketService.this;
        }

        public void initSocket( final String ip, final int port) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket = new Socket(ip, port);

                        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                        final DataInputStream dis = new DataInputStream(bis);

                        //运行View层连接成功的回调
                        listener.onConnectSuccess();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    while (true) {
                                        //通过while循环不断读取信息
                                        byte[] bytes = new byte[1]; // 一次读取一个byte
                                        String ret = "";
                                        while (dis.read(bytes) != -1) {
                                            ret += bytesToHexString(bytes) + " ";
                                            if (dis.available() == 0) { //一个请求
                                                if(listener!=null){
                                                    listener.onReceiveMsg(parseToObj(ret));
                                                }
                                                if(detailListener!=null){
                                                    detailListener.onReceiveMsg(parseToObj(ret));
                                                }

                                                ret = "";
                                            }
                                        }
                                    }
                                } catch (SocketException e) {
                                    e.printStackTrace();
                                    listener.onSocketError(e);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        //死循环监听是否有发送请求
                        while (true) {
                            OutputStream os = socket.getOutputStream();
                            DataOutputStream ds = new DataOutputStream(os);

                            if (isFirstInit) {
                                //链接时发个1给服务器标识手机端
                                ds.writeByte(1);
                                //连接成功即发查询指令
                                //ds.write(Common.select);
                                isFirstInit = false;
                            }
                            if (write) {
                                ds.write(smartEvent.getSmartBytes());
                                write = false;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //mListener.onSocketError();
                        //ExceptionUtil.decodeException(e);
                    }
                }
            }).start();
        }
    }

    //初始化Socket
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "OnCreate");
    }




    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setDetailListener(Listener detailListener) {
        this.detailListener = detailListener;
    }

    //抛给Presenter的接口
    public interface Listener {
        void onConnectSuccess();
        void onSocketError(Exception e);
        void onReceiveMsg(List<SmartData> list);

    }

    //byte[]数组转换为16进制的字符串
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();

    }

    //将接收的数据解析为列表
    private List<SmartData> parseToObj(String msg) {
        Log.d("SocketService: ", "parseToObj");
        msg = msg.replaceAll(" ", "");
        List<SmartData> list = new ArrayList<>();

        for (int i = 8; i < 39; i += 8) {
            String temp = "";
            for (int j = i; j < i + 8; j++) {
                try {
                    temp += msg.charAt(j);
                    list.add(new SmartData(temp));
                }catch (StringIndexOutOfBoundsException e)
                {
                    //e.printStackTrace();
                }
            }
        }
        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "SocketService onDestroy: ");
    }
}

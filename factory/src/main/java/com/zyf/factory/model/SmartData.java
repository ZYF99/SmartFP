package com.zyf.factory.model;


public class SmartData {

    int tempetature;//温度
    int humidity;//湿度
    boolean isGasAbnormal;//气体是否异常
    boolean isLightOn;//灯是否亮起
    boolean isAbnormal;

    public boolean isAbnormal() {
        return isAbnormal;
    }

    public SmartData(String msg) {

        tempetature = Integer.parseInt(msg.charAt(0)+""+msg.charAt(1), 16);
        humidity = Integer.parseInt(msg.charAt(2)+""+msg.charAt(3), 16);
        isGasAbnormal = Integer.parseInt(msg.charAt(4) + "" + msg.charAt(5),16) <= 0;
        isLightOn = Integer.parseInt(msg.charAt(6) + "" + msg.charAt(7),16) <= 0;

        if (tempetature> 40 || tempetature < 0 || humidity > 90 || humidity < 20||isGasAbnormal){
            isAbnormal = true;
        }else {
            isAbnormal = false;
        }

    }



    public int getTempetature() {
        return tempetature;
    }

    public int getHumidity() {
        return humidity;
    }

    public boolean isGasAbnormal() {
        return isGasAbnormal;
    }

    public boolean isLightOn() {
        return isLightOn;
    }

    @Override
    public String toString() {
        return "SmartData{" +
                "温度=" + tempetature +
                ", 湿度=" + humidity +
                "\n, 气体异常？=" + isGasAbnormal +
                ", 前照灯=" + isLightOn +
                '}';
    }
}

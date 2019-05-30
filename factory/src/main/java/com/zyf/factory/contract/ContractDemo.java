package com.zyf.factory.contract;

import android.os.Message;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ContractDemo implements InvocationHandler {
    private DemoMMM demoMMM;

    public ContractDemo() {
    }

    public ContractDemo(DemoMMM demoMMM) {
        this.demoMMM = demoMMM;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        if(Object.class.equals(method.getDeclaringClass())){
            return method.invoke(this,objects);
        }
        if(method.getName().equals("onDestroy")){
            System.out.println("XXXXXXXXXXXXXXXXXXXXxx");
        }
        if(method.getName().equals("print")){
            System.out.println("原来输入："+objects[0]);
            objects[0] = "张云峰牛逼";
            method.invoke(demoMMM,objects);
            System.out.println("原来方法执行后");
            return "全新的返回";
        }
        System.out.println("hello");
        return "HAHAHA";
    }

    public static <T>T getInstance(Class<T> clazz){
        ClassLoader classLoader = clazz.getClassLoader();
        Class<?>[] interfaces = new Class[]{clazz};
        ContractDemo proxy = new ContractDemo();
        return  (T)Proxy.newProxyInstance(classLoader,interfaces,proxy);
    }

    public static DemoMMM getInstance(DemoMMM clazz){
        ClassLoader classLoader = clazz.getClass().getClassLoader();
        ContractDemo proxy = new ContractDemo(clazz);
        return  (DemoMMM) Proxy.newProxyInstance(classLoader,clazz.getClass().getInterfaces(),proxy);
    }


}
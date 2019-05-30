package com.zyf.factory.contract;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class demo {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Contract_detail.Presenter kkkk = ContractDemo.getInstance(Contract_detail.Presenter.class);
        kkkk.onDestroy();
        System.out.println(kkkk.hashCode()+"\n");

        DemoMMM demoMMM = ContractDemo.getInstance(new DemoMMMImpl());
        System.out.println(demoMMM.print("HelloWorld"));

        System.out.println(Singleton.getInstace().setName("hello").toString());
        System.out.println(Singleton.getInstace().hashCode());
        System.out.println(Singleton.getInstace().hashCode());

        Constructor<?> constructor = Singleton.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Singleton singleton = (Singleton) constructor.newInstance();
        System.out.println(singleton.hashCode());



    }
}

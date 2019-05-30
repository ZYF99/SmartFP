package com.zyf.factory.contract;

public class Singleton {
    private static volatile Singleton intance;
    private String name;

    private Singleton() {
        if(intance != null){
            throw new RuntimeException("实例已经纯在");
        }
    }

    public static Singleton getInstace (){
        if(intance == null){
            synchronized (Singleton.class){
                if(intance == null){
                    intance = new Singleton();
                }
            }

        }
        return intance;
    }

    public String getName() {
        return name;
    }

    public Singleton setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "Singleton{" +
                "name='" + name + '\'' +
                '}';
    }
}

package com.zyf.factory.contract;

public class DemoMMMImpl implements DemoMMM{
    @Override
    public String print(String name) {
        System.out.println("我在输出:"+name);
        return "print";
    }
}

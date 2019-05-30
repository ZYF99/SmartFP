package com.zyf.factory.contract;

public interface Contract_login {
    interface Presenter{
        void login(String account,String password);
        public void onDestroy();
    }
    interface View{
        void showMainActivity();
    }
}

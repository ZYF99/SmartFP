package com.zyf.factory.contract;

import com.zyf.factory.model.SmartData;

import java.util.List;

public interface Contract_main {

    interface Presenter{
        void initSocket();
        void onDestroy();
    }
    interface View{
        void showLoading();
        void showError(Exception e);
        void hideLoading();
        void updateList(List<SmartData> list);
    }

}

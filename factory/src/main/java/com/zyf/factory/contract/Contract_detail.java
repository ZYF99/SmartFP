package com.zyf.factory.contract;


import com.zyf.factory.model.SmartData;

public interface Contract_detail {

    interface Presenter{
        void turnLight();
        void onDestroy();
    }
    interface View{
        void setText(SmartData smartData);
        void showError(Exception e);
    }

}

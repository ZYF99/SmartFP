package com.zyf.smartfp.activity.login;


import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import com.zyf.common.app.Activity;
import com.zyf.factory.contract.Contract_login;
import com.zyf.factory.presenter.Presenter_login;
import com.zyf.smartfp.R;
import com.zyf.smartfp.activity.main.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends Activity implements Contract_login.View{


    @BindView(R.id.edit_account)
    EditText edit_account;
    @BindView(R.id.edit_password)
    EditText edit_password;
    Presenter_login presenter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login;
    }

    //LoginActivity显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    //初始化界面控件
    @Override
    protected void initWidget() {
        super.initWidget();
    }

    //初始化界面控件后加载数据
    @Override
    protected void initData() {
        super.initData();
        presenter = Presenter_login.getInstance(this);
    }

    //登录按钮点击事件
    @OnClick(R.id.btn_login)
    void onLoginClick(){
        presenter.login(edit_account.getText().toString(),edit_password.getText().toString());
    }

    //跳转到主页活动
    @Override
    public void showMainActivity() {
        MainActivity.show(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}

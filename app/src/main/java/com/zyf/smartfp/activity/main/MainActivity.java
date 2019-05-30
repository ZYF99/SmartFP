package com.zyf.smartfp.activity.main;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zyf.common.app.Activity;
import com.zyf.common.app.Application;
import com.zyf.common.widget.Titanic;
import com.zyf.common.widget.TitanicTextView;
import com.zyf.factory.contract.Contract_main;
import com.zyf.factory.model.SmartData;
import com.zyf.factory.presenter.Presenter_main;
import com.zyf.smartfp.R;
import com.zyf.smartfp.activity.detail.DetailActivity;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends Activity implements Contract_main.View, RecAdapter.Listener {

    private long clickTime = 0; // 第一次点击的时间

    @BindView(R.id.cons)
    ConstraintLayout cons;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.titanic_tv)
    TitanicTextView titanicTextView;
    Titanic titanic;

    Contract_main.Presenter presenter;
    RecAdapter adapter;
    List<SmartData> mainList = new ArrayList<>();

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    //MainActivity显示的入口
    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    //初始化界面控件
    @Override
    protected void initWidget() {
        super.initWidget();
        //设置loading的字体
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Satisfy-Regular.ttf");
        titanicTextView.setTypeface(typeface);
        //设置loading的动画
        titanic = new Titanic();
        this.showLoading();
        initRec();
    }

    //初始化界面控件后的加载数据
    @Override
    protected void initData() {
        super.initData();
        presenter = Presenter_main.getInstance(this);
        presenter.initSocket();
    }


    //初始化recyclerView控件
    private void initRec() {
        adapter = new RecAdapter(this,R.layout.cell_list, mainList);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        adapter.bindToRecyclerView(recyclerView);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
    }


    //显示连接中
    @Override
    public void showLoading() {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                titanicTextView.setVisibility(View.VISIBLE);
                titanic.start(titanicTextView);
            }
        });

    }

    //错误提示
    @Override
    public void showError(Exception e) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        presenter.initSocket();

    }



    //隐藏加载
    @Override
    public void hideLoading() {

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                titanic.cancel();
                titanicTextView.setVisibility(View.GONE);
            }
        });



    }

    //更新列表数据（1秒一次）
    @Override
    public void updateList(List<SmartData> list) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                adapter.replaceData(list);
            }
        });

    }

    //列表单项点击事件
    @Override
    public void onCellClick(int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);
    }

    //back键监听
    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Application.showToast("再按一次退出");
            clickTime = System.currentTimeMillis();
        } else {
            this.finish();
        }
    }
}

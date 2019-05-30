package com.zyf.smartfp.activity.detail;


import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.zyf.common.app.Activity;
import com.zyf.factory.contract.Contract_detail;
import com.zyf.factory.model.SmartData;
import com.zyf.factory.presenter.Presenter_detail;
import com.zyf.smartfp.R;
import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import butterknife.BindView;
import butterknife.OnClick;

public class DetailActivity extends Activity implements Contract_detail.View {

    Presenter_detail presenter;

    @BindView(R.id.tv_device)
    TextView tv_device;

    @BindView(R.id.btn_light)
    Button btn_light;
    @BindView(R.id.detail_temp)
    TextView detail_temp;
    @BindView(R.id.detail_hum)
    TextView detail_hum;
    @BindView(R.id.detail_fire)
    TextView detail_fire;
    @BindView(R.id.detail_state_t)
    TextView detail_state_t;
    @BindView(R.id.detail_state_h)
    TextView detail_state_h;
    @BindView(R.id.detail_state_f)
    TextView detail_state_f;
    @BindView(R.id.lin_safe)
    LinearLayout lin_safe;
    @BindView(R.id.lin_warning)
    LinearLayout lin_warning;

    @OnClick(R.id.btn_return)
    void onReturnClick(){
        finish();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_detail;
    }



    //初始化界面控件后的加载数据
    @Override
    protected void initData() {
        super.initData();
        presenter = Presenter_detail.getInstance(this,getIntent().getIntExtra("position",0));
        presenter.initData();
    }

    //点灯按钮按下
    @OnClick(R.id.btn_light)
    void onLightClick(){
        presenter.turnLight();
    }


    //显示详情数据
    @Override
    public void setText(SmartData smartData) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                switch (presenter.getPosition()){
                    case 0:
                        tv_device.setText("客厅");
                        break;
                    case 1:
                        tv_device.setText("厨房");
                        break;
                    case 2:
                        tv_device.setText("卧室");
                        break;
                    case 3:
                        tv_device.setText("次卧");
                        break;
                }

                //前照灯
                if (smartData.isLightOn()) {
                    btn_light.setText("关灯");
                } else {
                    btn_light.setText("开灯");
                }

                //温度
                detail_temp.setText(String.valueOf(smartData.getTempetature()+""));
                if(smartData.getTempetature()> 40 || smartData.getTempetature() < 0 )
                {
                    if(smartData.getTempetature()>45){
                        lin_warning.setVisibility(View.VISIBLE);
                        lin_safe.setVisibility(View.GONE);
                    }else {
                        lin_warning.setVisibility(View.GONE);
                        lin_safe.setVisibility(View.VISIBLE);
                    }
                    detail_temp.setTextColor(Color.RED);
                    detail_state_t.setTextColor(Color.RED);
                    detail_state_t.setText("异常");
                }else {
                    detail_temp.setTextColor(Color.BLACK);
                    detail_state_t.setTextColor(Color.BLACK);
                    detail_state_t.setText("正常");
                }

                //湿度
                detail_hum.setText(String.valueOf(smartData.getHumidity()));
                if(smartData.getHumidity()> 90 || smartData.getHumidity() < 20 )
                {
                    detail_hum.setTextColor(Color.RED);
                    detail_state_h.setTextColor(Color.RED);
                    detail_state_h.setText("异常");
                }else {
                    detail_hum.setTextColor(Color.BLACK);
                    detail_state_h.setTextColor(Color.BLACK);
                    detail_state_h.setText("正常");
                }

                //可燃气体
                if(smartData.isGasAbnormal()){
                    detail_fire.setText("检测到可燃气体");
                    detail_state_f.setText("异常");
                    detail_fire.setTextColor(Color.RED);
                    detail_state_f.setTextColor(Color.RED);
                }else {
                    detail_fire.setText("未检测到可燃气体");
                    detail_state_f.setText("正常");
                    detail_fire.setTextColor(Color.BLACK);
                    detail_state_f.setTextColor(Color.BLACK);
                }
            }
        });

    }

    //显示错误
    @Override
    public void showError(Exception e) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                Toast.makeText(DetailActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DETAIL", "onDestroy: ");
        presenter.onDestroy();
    }
}

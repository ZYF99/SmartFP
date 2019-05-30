package com.zyf.smartfp.activity.main;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zyf.factory.model.SmartData;
import com.zyf.smartfp.R;
import java.util.List;

public class RecAdapter extends BaseQuickAdapter<SmartData, BaseViewHolder> {

    Listener listener;
    List<SmartData> data;
    RecAdapter(Listener listener,int layoutResId, @Nullable List<SmartData> data) {
        super(layoutResId, data);
        this.listener = listener;
        this.data = data;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(BaseViewHolder helper, SmartData item) {
        switch (data.indexOf(item)) {
            case 0:
                helper.setText(R.id.tv_address, "客厅");
                break;
            case 1:
                helper.setText(R.id.tv_address, "厨房");
                break;
            case 2:
                helper.setText(R.id.tv_address, "卧室");
                break;
            case 3:
                helper.setText(R.id.tv_address, "次卧");
                break;
        }


        if( item.isAbnormal()) {
            //显示异常

            //背景框
            helper.getView(R.id.cell_bg).setBackground(mContext.getResources().getDrawable(R.drawable.cornerbg_cell_solid));
            //状态图标
            helper.setImageResource(R.id.im_state, R.drawable.ic_error);
            //温度图标
            Drawable originalDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_temperature);
            Drawable tintDrawable = DrawableCompat.wrap(originalDrawable).mutate();
            DrawableCompat.setTint(tintDrawable, Color.WHITE);
            ((ImageView) helper.getView(R.id.im_temp)).setImageDrawable(tintDrawable);
            //湿度图标
            originalDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_humidity);
            tintDrawable = DrawableCompat.wrap(originalDrawable).mutate();
            DrawableCompat.setTint(tintDrawable, Color.WHITE);
            ((ImageView) helper.getView(R.id.im_humidity)).setImageDrawable(tintDrawable);
            //地址
            ((TextView) helper.getView(R.id.tv_address)).setTextColor(Color.WHITE);
            ((TextView) helper.getView(R.id.tv_temp)).setTextColor(Color.WHITE);
            ((TextView) helper.getView(R.id.tv_hum)).setTextColor(Color.WHITE);


        } else {
            //显示正常

            //背景框
            helper.getView(R.id.cell_bg).setBackground(mContext.getResources().getDrawable(R.drawable.cornerbg_cell_stroke));
            //状态图标
            helper.setImageResource(R.id.im_state, R.drawable.ic_smile);
            //温度图标
            Drawable originalDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_temperature);
            Drawable tintDrawable = DrawableCompat.wrap(originalDrawable).mutate();
            DrawableCompat.setTint(tintDrawable, Color.parseColor("#EC9303"));
            ((ImageView) helper.getView(R.id.im_temp)).setImageDrawable(tintDrawable);
            //湿度图标
            originalDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_humidity);
            tintDrawable = DrawableCompat.wrap(originalDrawable).mutate();
            DrawableCompat.setTint(tintDrawable, Color.parseColor("#EC9303"));
            ((ImageView) helper.getView(R.id.im_humidity)).setImageDrawable(tintDrawable);
            //地址
            ((TextView) helper.getView(R.id.tv_address)).setTextColor(Color.parseColor("#EC9303"));
            ((TextView) helper.getView(R.id.tv_temp)).setTextColor(Color.parseColor("#EC9303"));
            ((TextView) helper.getView(R.id.tv_hum)).setTextColor(Color.parseColor("#EC9303"));
        }

        helper.setText(R.id.tv_temp, item.getTempetature() + "℃");
        helper.setText(R.id.tv_hum, item.getHumidity() + "%");

        //helper.setText(R.id.tv_fire, item.isGasAbnormal()?"异常":"正常");


        helper.addOnClickListener(R.id.cell_bg);
        helper.getView(R.id.cell_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCellClick(data.indexOf(item));
            }
        });





    }

    interface Listener{
        void onCellClick(int position);

    }

}

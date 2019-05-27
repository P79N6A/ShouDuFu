package com.futuretongfu.ui.component.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.futuretongfu.R;

/**
 * Created by ChenXiaoPeng on 2017/6/16.
 */

public class AddressSelectDialog extends AbsCustomDialog implements View.OnClickListener {

    private TextView textConfirm;
    private TextView provincesView;
    private TextView cityView;
    private TextView aearView;
    private View line_provinces;
    private View line_city;
    private View line_area;

    public AddressSelectDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        textConfirm = (TextView) findViewById(R.id.text_confirm);
        provincesView = (TextView) findViewById(R.id.provinces);
        cityView = (TextView) findViewById(R.id.city);
        aearView = (TextView) findViewById(R.id.area);
        line_provinces = findViewById(R.id.line_provinces);
        line_city = findViewById(R.id.line_city);
        line_area = findViewById(R.id.line_area);
        provincesView.setOnClickListener(this);
        cityView.setOnClickListener(this);
        aearView.setOnClickListener(this);
        textConfirm.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.provinces:
                provincesView.setSelected(true);
                cityView.setSelected(false);
                aearView.setSelected(false);
                line_provinces.setVisibility(View.VISIBLE);
                line_city.setVisibility(View.GONE);
                line_area.setVisibility(View.GONE);
                break;
            case R.id.city:
                cityView.setSelected(true);
                provincesView.setSelected(false);
                aearView.setSelected(false);
                line_provinces.setVisibility(View.GONE);
                line_city.setVisibility(View.VISIBLE);
                line_area.setVisibility(View.GONE);
                break;
            case R.id.area:
                provincesView.setSelected(false);
                cityView.setSelected(false);
                aearView.setSelected(true);
                line_provinces.setVisibility(View.GONE);
                line_city.setVisibility(View.GONE);
                line_area.setVisibility(View.VISIBLE);
                break;
            case R.id.text_confirm:
                dismiss();
                break;
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public int getWindowAnimationsResId() {
        return R.style.BottomDialogAnim;
    }

    @Override
    public int getLayoutResID() {
        return R.layout.layout_dialog_address_select;
    }

    @Override
    public int getWidth() {
        return android.view.ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int getHeight() {
        return android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    }


}

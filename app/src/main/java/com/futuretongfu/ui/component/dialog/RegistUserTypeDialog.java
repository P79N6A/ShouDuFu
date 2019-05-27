package com.futuretongfu.ui.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.entity.RegistUserTypeBean;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.To;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册--选择收货地址
 */

public class RegistUserTypeDialog extends Dialog {

    @Bind(R.id.tv_usertype1)
    TextView tvUsertype1;
    @Bind(R.id.tv_usertype2)
    TextView tvUsertype2;
    @Bind(R.id.tv_usertype3)
    TextView tvUsertype3;
    @Bind(R.id.tv_usertype4)
    TextView tvUsertype4;
    private Context context;
    List<RegistUserTypeBean> data;

    public RegistUserTypeDialog(Context context,List<RegistUserTypeBean> data) {
        super(context, R.style.dialogWindowAnim);
        this.context = context;
        this.data = data;
        setContentView(R.layout.dialog_regist_usertype_layout);
        setCancelable(true);
        setCanceledOnTouchOutside(true);  //设置触摸对话框意外的地方取消对话框
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        if (null!=data&&data.size()>=3){
            tvUsertype1.setText(data.get(0).getName()+": "+data.get(0).getRemark());
            tvUsertype2.setText(data.get(1).getName()+": "+data.get(1).getRemark());
            tvUsertype3.setText(data.get(2).getName()+": "+data.get(2).getRemark());
            tvUsertype4.setText(data.get(3).getName()+": "+data.get(3).getRemark());
        }
    }


    @OnClick({R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                dismiss();
                break;
        }
    }

}
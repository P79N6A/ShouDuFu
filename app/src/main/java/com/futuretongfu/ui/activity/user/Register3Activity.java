package com.futuretongfu.ui.activity.user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.WeiLaiFuApplication;
import com.futuretongfu.iview.IRegistRealVerView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.eventType.ResgisterLoginEventType;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.RegistRealPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.MainActivity;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.ui.component.dialog.RegistAddressDialog;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.KeyboardUtil;
import com.futuretongfu.utils.MatchUtil;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.TimerUtil;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.UpgradePopupCityWindow;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.futuretongfu.R.id.btn_submit;
import static com.futuretongfu.R.id.tv_address;

/**
 * 注册---实名认证
 * @author zhanggf
 */
public class Register3Activity extends BaseActivity implements  UpgradePopupCityWindow.PopupWindowListener, IRegistRealVerView {

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.tv_people_id)
    public TextInputEditText tv_PeopleId;
    @Bind(btn_submit)
    public Button submit;
    @Bind(R.id.choose_huji_address)
    public TextView choose_huji_address;
    @Bind(R.id.choose_shouhuo_address)
    public TextView choose_shouhuo_address;  //收货地址
    @Bind(R.id.people_name)
    public EditText people_name;
    @Bind(R.id.root_layout)
    public LinearLayout rootView;

    private Context context;
    private RegistRealPresenter mPresenter;
    private UpgradePopupCityWindow popupWindow;
    private String proviceStringhuji = "";
    private String proviceIdhuji = "";
    private String cityStringhuji = "";
    private String cityIdhuji = "";
    @Bind(R.id.bt_right)
    public TextView textSkin;
    @Bind(R.id.bt_back)
    public ImageView imgvBack;
    String userId = "";
    String tellphone = "";
    String receiverName="",receiverMobile="",receiverAddress="";
    /***********************************************************************/
    @Override

    protected int getLayoutId() {
        return R.layout.activity_register3;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (mPresenter == null) {
            mPresenter = new RegistRealPresenter(this, this);
        }
        submit.setEnabled(false);
        userId = getIntent().getStringExtra("userId");
        tellphone = getIntent().getStringExtra("tellphone");
        textTitle.setText("实名认证");
        context = Register3Activity.this;
        CacheActivityUtil.addNewActivity(this);
    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    /***********************************************************************/

    @OnClick(R.id.choose_huji_address)
    public void onClickHujiAddress() {
        hideKeyboard();
        popupWindow = new UpgradePopupCityWindow(context,1);
        popupWindow.setPopupWindowListener(this);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(context, 1.0f);
            }
        });
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        Util.setAlpha(context, 0.5f);
    }

    /**
     * 扫一扫识别身份证
     */
    @OnClick(R.id.image_scan_id)
    public void onClickscan() {
        if (PermissionUtil.permissionCamera(this)) {
            mPresenter.checkCardNumber(this);
        }
    }


    @OnClick(R.id.bt_back)
    public void onClickBack() {
       showDialog();
    }


    @OnClick(R.id.choose_shouhuo_address)
    public void onClickChooseAddress() {
        RegistAddressDialog registAddressDialog = new RegistAddressDialog(this,receiverName,receiverMobile,receiverAddress, new RegistAddressDialog.OnPhoneConfirmListener() {
            @Override
            public void onSelectorAdddress(String name, String phone, String address) {
                receiverName = name;
                receiverMobile = phone;
                receiverAddress = address;
                choose_shouhuo_address.setText(name+phone);
            }
        });
        registAddressDialog.show();
    }


    @OnClick(btn_submit)
    public void onCommitClick() {
        String name = people_name.getText().toString().trim();
        if (isEmpty(name, "持卡人姓名")) {
            return;
        }
        String peopleid = tv_PeopleId.getText().toString().trim();
        if (isEmpty(peopleid, "身份证")) {
            return;
        }
        if (!MatchUtil.isName(name)) {
            showToast("姓名格式错误");
            return;
        }

        if (!MatchUtil.isValidatedAllIdcard(peopleid)) {
            showToast("身份证格式错误");
            return;
        }
        if (TextUtils.isEmpty(proviceStringhuji) || TextUtils.isEmpty(cityStringhuji)) {
            showToast("选择户籍所在地");
            return;
        }
        if (TextUtils.isEmpty(receiverName)||TextUtils.isEmpty(receiverMobile)||TextUtils.isEmpty(receiverAddress)) {
            showToast("收货信息不完善");
            return;
        }
        mPresenter.addUserOpen(userId, peopleid,name,tellphone, proviceIdhuji, cityIdhuji,receiverName,receiverMobile,receiverAddress);
    }

    private void skinNext() {
        EventBus.getDefault().post(new ResgisterLoginEventType(true));
        startMainActivity();
        delayFinish(200);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (requestCode == 100 && resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    mPresenter.uploadAndRecognize(extras.getString("path"));
                }
                break;
        }
    }

    /***********************************************************************/
    public static void startActivity(Context context,String userId,String tellphone) {
        Intent intent = new Intent(context, Register3Activity.class);
        intent.putExtra("userId",userId);
        intent.putExtra("tellphone",tellphone);
        context.startActivity(intent);
    }

    private void startMainActivity() {
        MainActivity.startActivity(this, true);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        TimerUtil.startTimer(400, new TimerUtil.TimerCallBackListener2() {
            @Override
            public void onEnd() {
                finish();
            }
        });
    }


    @Override
    public void onGetCardNumberFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onGetCardNumberSuccess(final String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_PeopleId.setText(data);
            }
        });
    }

    //开户成功
    @Override
    public void onaddUserOpenSuccess(FuturePayApiResult result) {
        showToast(result.getMsg());
        RegisterCardActivity.startActivity(context,userId,tv_PeopleId.getText().toString().trim(),people_name.getText().toString().trim());
    }

    @Override
    public void onaddUserOpenFaileUserExists(String msg) {
        showToast(msg);
        RegisterCardActivity.startActivity(context,userId,tv_PeopleId.getText().toString().trim(),people_name.getText().toString().trim());
    }

    @Override
    public void onaddUserOpenFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onChooseFinish(int type,String provices, String proId, String city, String ciId, String area, String aearCode) {
        //type  1户籍   2开户银行
        if (type==1){
            proviceStringhuji = provices;
            cityStringhuji = city;
            proviceIdhuji = proId;
            cityIdhuji = ciId;
            choose_huji_address.setText(provices + "  " + city + "  " + area);
        }
    }

    //禁止使用返回键返回到上一页,但是可以直接退出程序**
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            showDialog();
            return true;//不执行父类点击事件
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    private void showDialog() {
        new PromptDialog.Builder(this)
                .setMessage(getResources().getString(R.string.user_back_tip))
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
//                        skinNext();
                        CacheActivityUtil.newFinishActivity();
                        dialog.dismiss();
                    }
                }).setButton2TextColor(Color.parseColor("#0091EE")).show();
    }

    private void hideKeyboard() {
        KeyboardUtil.showKeyboard(this, people_name, false);
        KeyboardUtil.showKeyboard(this, tv_PeopleId, false);
    }


    @OnTextChanged({R.id.people_name, R.id.tv_people_id})
    public void onTextChange() {
        if (
                TextUtils.isEmpty(people_name.getText().toString().trim())
                        || TextUtils.isEmpty(tv_PeopleId.getText().toString().trim())) {
            submit.setEnabled(false);
            return;
        }
        submit.setEnabled(true);
    }

}


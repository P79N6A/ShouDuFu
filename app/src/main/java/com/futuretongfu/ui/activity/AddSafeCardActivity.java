package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.IBankVerView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.AddSafeCardPresenter;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.R;
import com.futuretongfu.WeiLaiFuApplication;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.model.entity.SearchBankInfo;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.UpgradePopupWindow;

import butterknife.Bind;
import butterknife.OnClick;

import static com.futuretongfu.R.id.btn_submit;

/**
 * 添加安全卡
 *
 * @author ChenXiaoPeng
 */
public class AddSafeCardActivity extends BaseActivity implements TextWatcher, IBankVerView, UpgradePopupWindow.PopupWindowListener {

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(btn_submit)
    public Button submit;
    @Bind(R.id.phone_number)
    public EditText phoneView;
    @Bind(R.id.card_name)
    public EditText cardView;
    @Bind(R.id.checkbox)
    public AppCompatCheckBox checkbox;

    @Bind(R.id.bank_name)
    public TextView bank_name;
    @Bind(R.id.choose_address)
    public TextView choose_address;
    @Bind(R.id.people_name)
    public TextView people_name;
    @Bind(R.id.root_layout)
    public LinearLayout rootView;

    private Context context;
    String innerCode = "";
    UserManager userManager;
    private AddSafeCardPresenter mPresenter;
    private UpgradePopupWindow popupWindow;
    private String proviceString = "";
    private String proviceId = "";
    private String cityString = "";
    private String cityId = "";
    /***********************************************************************/
    @Override

    protected int getLayoutId() {
        return R.layout.activity_add_safe_card;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (mPresenter == null) {
            mPresenter = new AddSafeCardPresenter(this, this);
        }
        textTitle.setText("新增银行卡");
        userManager = UserManager.getInstance();
        context = AddSafeCardActivity.this;
        WeiLaiFuApplication.getInstance().addActivity(this);
        String name = userManager.getRealName();
        if (!TextUtils.isEmpty(name)) {
            people_name.setText(StringUtil.setVisibilityName(name));
        }
        phoneView.addTextChangedListener(this);
        cardView.addTextChangedListener(this);
        popupWindow = new UpgradePopupWindow(context);
        popupWindow.setPopupWindowListener(this);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(context, 1.0f);
            }
        });

    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    /***********************************************************************/

    @OnClick(R.id.bank_name)
    public void onClickBank() {
        IntentUtil.startActivityForResult(AddSafeCardActivity.this, SearchBankActivity.class, 103);
    }

    @OnClick(R.id.choose_address)
    public void onClickAddress() {
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        Util.setAlpha(context, 0.5f);
    }
    @OnClick(R.id.image_scan_ic)
    public void onGetCardNumber() {
        mPresenter.getCardNumber(this);
    }

    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.text_agreement)
    public void onClickAgreement() {
        ShowWebViewActivity.startActivity(this, Constants.Url_Agreement_bindProtocol, "首都富绑卡协议", true);
    }

    @OnClick(btn_submit)
    public void onCommitClick() {
        if (TextUtils.isEmpty(cardView.getText().toString().trim())) {
            showToast(getResources().getString(R.string.hint_forget_card));
            return;
        }
        if (TextUtils.isEmpty(phoneView.getText().toString().trim())) {
            showToast(getResources().getString(R.string.hint_forget_phone));
            return;
        }
        if (TextUtils.isEmpty(innerCode)) {
            showToast("选择开户行");
            return;
        }
        if (TextUtils.isEmpty(proviceString)||TextUtils.isEmpty(cityString)) {
            showToast("选择开户地区");
            return;
        }
        if (!StringUtil.isPhoneNumber(phoneView.getText().toString().trim())) {
            showToast(getResources().getString(R.string.input_phone_number_error));
            return;
        }
        if (!checkbox.isChecked()) {
            showToast("请同意首都富绑卡协议");
            return;
        }
        SMSVerificationActivity.startActivity(context, 1, phoneView.getText().toString().trim(), cardView.getText().toString().trim(), innerCode,proviceId,cityId,Constants.Response_bank_description);
        //校验银行卡
//        showProgressDialog();
//        mPresenter.verBankCard(UserManager.getInstance().getUserId() + "", cardView.getText().toString().trim(), phoneView.getText().toString().trim());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 103://选择银行卡code
                if (data != null && data.getExtras() != null) {
                    Bundle bundle = data.getExtras();
                    SearchBankInfo dataBean = (SearchBankInfo) bundle.getSerializable(IntentKey.SET_BANK_INTENT);
                    if (dataBean != null) {
                        if (dataBean.getInnerCode() != null) {
                            innerCode = dataBean.getInnerCode();
                        }
                        if (dataBean.getBankName() != null) {
                            bank_name.setText(dataBean.getBankName());
                        }
                    }
                }
                break;
            case 100:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    mPresenter.uploadAndRecognize(extras.getString("path"));
                }
                break;
        }
    }

    /***********************************************************************/
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddSafeCardActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (phoneView.getText().toString().trim().length() == 11 && cardView.getText().toString().trim().length() >= 16) {
            submit.setEnabled(true);
        } else {
            submit.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    //校验 ---暂时无用
    @Override
    public void onBankVerSuccess(String innerCode, String bankName) {
        hideProgressDialog();
//        SMSVerificationActivity.startActivity(context, 1, phoneView.getText().toString().trim(), cardView.getText().toString().trim(), innerCode, bankName);
    }

    @Override
    public void onBankVerFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
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
                cardView.setText(data);
            }
        });
    }

    @Override
    public void onChooseFinish(String provices,String proId, String city,String ciId, String area, String aearCode) {
        proviceString = provices;
        cityString = city;
        proviceId = proId;
        cityId = ciId;
        choose_address.setText(provices + "  " + city + "  "+area );
    }
}

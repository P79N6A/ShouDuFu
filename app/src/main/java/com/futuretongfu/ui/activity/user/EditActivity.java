package com.futuretongfu.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.InputFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futuretongfu.presenter.user.EditPersonalDataPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.IEditPersonalDataView;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.component.ChineseLengthFilter;
import com.futuretongfu.ui.component.EditSpaceFilter;
import com.futuretongfu.utils.MatchUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 编辑个人资料
 *
 * @author ChenXiaoPeng
 */
public class EditActivity extends BaseActivity implements IEditPersonalDataView {

    public static final int Type_NickName = 0;
    public static final int Type_Sex = 1;
    public static final int Type_Mail = 2;
    public static final int Type_Phone = 3;

    private static final String Intent_Extra_Type = "type";
    private static final String Intent_Extra_NickName = "nickName";
    private static final String Intent_Extra_Gender = "gender";
    private static final String Intent_Extra_Phone = "phone";

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.text_save)
    public TextView textSave;

    @Bind(R.id.edit_name)
    public TextInputEditText editName;

    @Bind(R.id.linear_sex)
    public LinearLayout linearSex;

    @Bind(R.id.imgv_man)
    public ImageView imgvMan;
    @Bind(R.id.imgv_woman)
    public ImageView imgvWoman;
    @Bind(R.id.imgv_secrecy)
    public ImageView imgvSecrecy;

    private int type = Type_NickName;
    private ChineseLengthFilter nameLengthFilter;
    private EditPersonalDataPresenter editPersonalDataPresenter;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit;
    }


    @Override
    protected Presenter getPresenter() {
        return editPersonalDataPresenter;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        type = intent.getIntExtra(Intent_Extra_Type, Type_NickName);

        switch (type) {
            case Type_NickName:
                String nickName = intent.getStringExtra(Intent_Extra_NickName);
                textTitle.setText("修改昵称");
                textSave.setVisibility(View.VISIBLE);
                editName.setVisibility(View.VISIBLE);
                linearSex.setVisibility(View.GONE);
                editName.setHint(R.string.nick_name_hint);

                nameLengthFilter = new ChineseLengthFilter(14, true);
                editName.setFilters(new InputFilter[]{nameLengthFilter});

                editName.setText(nickName);
                editName.setSelection(nickName.length());

                break;

            case Type_Sex:
                int gender = intent.getIntExtra(Intent_Extra_Gender, 0);
                textTitle.setText("修改性别");
                textSave.setVisibility(View.GONE);
                editName.setVisibility(View.GONE);
                linearSex.setVisibility(View.VISIBLE);
                updateGenderView(gender);
                break;

            case Type_Mail:
                textTitle.setText("修改邮箱");
                textSave.setVisibility(View.VISIBLE);
                editName.setVisibility(View.VISIBLE);
                linearSex.setVisibility(View.GONE);
                editName.setHint("请输入您的有效邮箱地址");
                editName.setFilters(new InputFilter[]{new EditSpaceFilter()});
                break;

        }

        editPersonalDataPresenter = new EditPersonalDataPresenter(this, this);

    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.text_save)
    public void onClickSave() {
        if (Type_NickName == type) {
            String nickName = editName.getText().toString().trim();
            if (isEmpty(nickName, "昵称")) {
                return;
            }

            if (MatchUtil.isAllNumber(nickName)) {
                showToast("昵称不能为纯数字");
                return;
            }

            if (MatchUtil.isConSpeCharacters(nickName)) {
                showToast("昵称不能包含特殊符号");
                return;
            }
//
//            if (nameLengthFilter.getCurLength() < 4) {
//                showToast(R.string.nick_name_length);
//                return;
//            }

            showProgressDialog();
            editPersonalDataPresenter.setNickName(nickName);
        } else if (Type_Mail == type) {
            String email = StringUtil.replaceAllSpace(editName.getText().toString());
            if (isEmpty(email, "邮箱")) {
                return;
            }

            if (!MatchUtil.isEmail(email)) {
                showToast("邮箱格式错误");
                return;
            }
            showProgressDialog();
            editPersonalDataPresenter.setEmail(email);
        }
    }

    @OnClick(R.id.view_man)
    public void onClickSexMan() {
        updateGenderView(Constants.Gender_Man);
        editPersonalDataPresenter.setGender(Constants.Gender_Man);
    }

    @OnClick(R.id.view_woman)
    public void onClickSexWoman() {
        updateGenderView(Constants.Gender_Woman);
        editPersonalDataPresenter.setGender(Constants.Gender_Woman);
    }

    @OnClick(R.id.view_secrecy)
    public void onClickSexSecrecy() {
        updateGenderView(Constants.Gender_Secrecy);
        editPersonalDataPresenter.setGender(Constants.Gender_Secrecy);
    }

    /***********************************************************************/
    @Override
    public void onEditPersonalDataEditNameSuccess() {
        finish();
    }

    @Override
    public void onEditPersonalDataEditNameFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onEditPersonalDataEditEmailSuccess() {
        finish();
    }


    @Override
    public void onEditPersonalDataEditEmailFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onEditPersonalDataEditGenderSuccess() {
        finish();
    }

    @Override
    public void onEditPersonalDataEditGenderFaile(String msg) {
        showToast(msg);
    }

    /***********************************************************************/

    private void updateGenderView(int gender) {
        switch (gender) {
            case Constants.Gender_Secrecy:
                imgvMan.setVisibility(View.GONE);
                imgvWoman.setVisibility(View.GONE);
                imgvSecrecy.setVisibility(View.VISIBLE);
                break;
            case Constants.Gender_Man:
                imgvMan.setVisibility(View.VISIBLE);
                imgvWoman.setVisibility(View.GONE);
                imgvSecrecy.setVisibility(View.GONE);
                break;
            case Constants.Gender_Woman:
                imgvMan.setVisibility(View.GONE);
                imgvWoman.setVisibility(View.VISIBLE);
                imgvSecrecy.setVisibility(View.GONE);
                break;
            default:
                imgvMan.setVisibility(View.GONE);
                imgvWoman.setVisibility(View.GONE);
                imgvSecrecy.setVisibility(View.VISIBLE);
                break;
        }
    }

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(Intent_Extra_Type, type);
        context.startActivity(intent);
    }

    public static void startActivityNickName(Context context, String nickName) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(Intent_Extra_Type, Type_NickName);
        intent.putExtra(Intent_Extra_NickName, nickName);
        context.startActivity(intent);
    }

    public static void startActivityPhone(Context context, String phone) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(Intent_Extra_Type, Type_Phone);
        intent.putExtra(Intent_Extra_Phone, phone);
        context.startActivity(intent);
    }

    public static void startActivityGender(Context context, int gender) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(Intent_Extra_Type, Type_Sex);
        intent.putExtra(Intent_Extra_Gender, gender);
        context.startActivity(intent);
    }


//    private void showMissingPermissionDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionsActivity.this);
//        builder.setTitle(R.string.help);
//        builder.setMessage(R.string.string_help_text); // 拒绝, 退出应用
//        builder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                setResult(PERMISSIONS_DENIED);
//                finish();
//            }
//        });
//        builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startAppSettings();
//            }
//        });
//        builder.show();
//    }


}

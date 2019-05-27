package com.futuretongfu.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.R;
import com.futuretongfu.iview.IEditAddressView;
import com.futuretongfu.model.entity.AddressEntity;
import com.futuretongfu.presenter.user.EditAddressPresenter;
import com.futuretongfu.ui.adapter.AearListAdapter;
import com.futuretongfu.ui.adapter.CityListAdapter;
import com.futuretongfu.ui.adapter.ProviceListAdapter;
import com.futuretongfu.utils.To;

import java.util.ArrayList;
import java.util.List;

import static com.futuretongfu.R.id.area;


/**
 * 类:   BankPopupWindow
 * 描述: 选择城市（户籍/开户）
 * 作者： weiang on 2017/6/21
 */
public class UpgradePopupCityWindow extends PopupWindow implements View.OnClickListener, IEditAddressView {
    private View rootView;
    private Context context;
    private TextView textConfirm;
    private TextView provincesView;
    private TextView cityView;
    private TextView aearView;
    private View line_provinces;
    private View line_city;
    private View line_area;

    private List<AddressEntity> provinceList;
    private List<AddressEntity> cityList;
    private List<AddressEntity> areaList;

    private RecyclerView provinceListView;
    private RecyclerView cityListView;
    private RecyclerView areaListView;

    private AearListAdapter aearListAdapter;
    private CityListAdapter cityListAdapter;
    private ProviceListAdapter proviceListAdapter;
    private EditAddressPresenter presenter;
    PopupWindowListener popupWindowListener;

    private String proviceString = "";
    private String proviceId = "";
    private String cityString = "";
    private String cityId = "";
    private String aearString = "";
    private String aearCode = "";
    private int type;


    public UpgradePopupCityWindow(Context context,int type) {
        super(context);
        this.context = context;
        this.type = type;
        presenter = new EditAddressPresenter(context, this);
        init(context);
        setPopupWindow();
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //绑定布局
        rootView = inflater.inflate(R.layout.layout_pop_upgrade_city, null);
        textConfirm = (TextView) rootView.findViewById(R.id.text_confirm);
        provincesView = (TextView) rootView.findViewById(R.id.provinces);
        cityView = (TextView) rootView.findViewById(R.id.city);
        aearView = (TextView) rootView.findViewById(area);

        provinceListView = (RecyclerView) rootView.findViewById(R.id.provinces_list);
        cityListView = (RecyclerView) rootView.findViewById(R.id.citys_list);
        areaListView = (RecyclerView) rootView.findViewById(R.id.area_list);

        line_provinces = rootView.findViewById(R.id.line_provinces);
        line_city = rootView.findViewById(R.id.line_city);
        line_area = rootView.findViewById(R.id.line_area);
        provincesView.setOnClickListener(this);
        cityView.setOnClickListener(this);
        aearView.setOnClickListener(this);
        textConfirm.setOnClickListener(this);

        setContentView(rootView);
        presenter.getProvinceList();
        provinceList = new ArrayList<AddressEntity>();
        cityList = new ArrayList<AddressEntity>();
        areaList = new ArrayList<AddressEntity>();
        initRecycle();
    }

    /**
     * 设置窗口属性
     */
    private void setPopupWindow() {
        // 设置SelectPicPopupWindow的View
        this.setContentView(rootView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight((int) context.getResources().getDimension(R.dimen.height_size_popup_up));
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        setAnimationStyle(R.style.BottomDialogAnim);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable();
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    /**
     * 初始化recycleView
     */
    private void initRecycle() {
        aearListAdapter = new AearListAdapter(R.layout.item_edit_address, areaList);
        cityListAdapter = new CityListAdapter(R.layout.item_edit_address, cityList);
        proviceListAdapter = new ProviceListAdapter(R.layout.item_edit_address, provinceList);
        areaListView.setLayoutManager(new LinearLayoutManager(context));
        areaListView.setAdapter(aearListAdapter);
        cityListView.setLayoutManager(new LinearLayoutManager(context));
        cityListView.setAdapter(cityListAdapter);
        provinceListView.setLayoutManager(new LinearLayoutManager(context));
        provinceListView.setAdapter(proviceListAdapter);

        proviceListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                presenter.getCityList((int) provinceList.get(position).getAreaId());
                proviceString = provinceList.get(position).getAreaName() + "";
                proviceId = provinceList.get(position).getHfAreaId();
            }
        });
        cityListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                presenter.getDistrictList((int) cityList.get(position).getAreaId());
                cityString = cityList.get(position).getAreaName() + "";
                cityId = cityList.get(position).getHfAreaId();
            }
        });
        aearListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                aearString = areaList.get(position).getAreaName() + "";
                aearCode = areaList.get(position).getAreaId()+"";
                if (popupWindowListener != null) {
                    popupWindowListener.onChooseFinish(type,proviceString, proviceId,cityString, cityId,aearString,aearCode);
                    dismiss();
                }
            }
        });
    }

    /**
     * 设置监听器
     *
     * @param popupWindowListener
     */
    public void setPopupWindowListener(PopupWindowListener popupWindowListener) {
        this.popupWindowListener = popupWindowListener;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.provinces:
                proviceString = "";
                cityString = "";
                aearString = "";
                chooseProvice();
                break;
            case R.id.city:
                cityString = "";
                aearString = "";
                chooseCity();
                break;
            case area:
                aearString = "";
                chooseAear();
                break;
            case R.id.text_confirm:
                if (popupWindowListener != null) {
                    if (TextUtils.isEmpty(proviceString)) {
                        To.s("没有选择省份");
                    } else if (TextUtils.isEmpty(cityString)) {
                        To.s("没有选择城市");
                    } else {
                        popupWindowListener.onChooseFinish(type,proviceString,proviceId, cityString,cityId, aearString,aearCode);
                        dismiss();
                    }
                }
                break;
        }
    }

    private void chooseProvice() {
        cityList.clear();
        cityListAdapter.notifyDataSetChanged();
        areaList.clear();
        aearListAdapter.notifyDataSetChanged();
        provincesView.setSelected(true);
        cityView.setSelected(false);
        aearView.setSelected(false);
        line_provinces.setVisibility(View.VISIBLE);
        line_city.setVisibility(View.GONE);
        line_area.setVisibility(View.GONE);
        provinceListView.setVisibility(View.VISIBLE);
        areaListView.setVisibility(View.GONE);
        cityListView.setVisibility(View.GONE);
    }

    private void chooseCity() {
        if (cityList.isEmpty()) {
            return;
        }
        areaList.clear();
        aearListAdapter.notifyDataSetChanged();
        cityView.setSelected(true);
        provincesView.setSelected(false);
        aearView.setSelected(false);
        line_provinces.setVisibility(View.GONE);
        line_city.setVisibility(View.VISIBLE);
        line_area.setVisibility(View.GONE);
        provinceListView.setVisibility(View.GONE);
        areaListView.setVisibility(View.GONE);
        cityListView.setVisibility(View.VISIBLE);
    }

    private void chooseAear() {
        if (areaList.isEmpty()) {
            return;
        }
        provincesView.setSelected(false);
        cityView.setSelected(false);
        aearView.setSelected(true);
        line_provinces.setVisibility(View.GONE);
        line_city.setVisibility(View.GONE);
        line_area.setVisibility(View.VISIBLE);
        provinceListView.setVisibility(View.GONE);
        cityListView.setVisibility(View.GONE);
        areaListView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onEditAddressSuccess() {

    }

    @Override
    public void onEditAddressFaile(String msg) {

    }

    @Override
    public void onGetProvinceListSuccess(List<AddressEntity> datas) {
        provinceList.addAll(datas);
        proviceListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetProvinceListFaile(String msg) {

    }

    @Override
    public void onGetCityListSuccess(List<AddressEntity> datas) {
        cityList.clear();
        cityList.addAll(datas);
        chooseCity();
    }

    @Override
    public void onGetCityListFaile(String msg) {

    }

    @Override
    public void onGetDistrictListSuccess(List<AddressEntity> datas) {
        areaList.clear();
        areaList.addAll(datas);
        chooseAear();
    }

    @Override
    public void onGetDistrictListFaile(String msg) {

    }

    /**
     * 监听器
     */
    public interface PopupWindowListener {
        //键盘输入完
        void onChooseFinish(int type,String provices, String proviceId, String city, String cityId, String area, String aearCode);
    }

}

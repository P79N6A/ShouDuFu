package com.futuretongfu.ui.activity.goods;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.classification.ClassOneListViewBean;
import com.futuretongfu.bean.classification.ClassThreeListViewBean;
import com.futuretongfu.bean.classification.ClassTwoListViewBean;
import com.futuretongfu.iview.IClassGoodsView;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.ClassGoodsPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.adapter.ClassAdapter;
import com.futuretongfu.ui.adapter.ClassExpandableAdapter;
import com.futuretongfu.utils.ListDataSave;
import com.futuretongfu.utils.SharedPreferencesUtils;
import com.futuretongfu.view.MyExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/5/11.
 * 三级分类
 */

public class ClassGoodsActivity extends BaseActivity implements IClassGoodsView,ClassAdapter.OnItemClickListener {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rcv_class_listView)
    RecyclerView rcvClassListView;
    @Bind(R.id.elv_class)
    MyExpandableListView elvClass;
    private ClassAdapter classAdapter;  //一级分类
    private ClassExpandableAdapter expandableAdapter;  //二三级分类
    private ClassGoodsPresenter presenter;
    private List<ClassTwoListViewBean> groups = new ArrayList<>();
    private Map<String, List<ClassThreeListViewBean>> children = new HashMap<>();
    private List<ClassOneListViewBean> classList = new ArrayList<>();
    private String LastModifyTime;
    private ListDataSave listDataSave=null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_classgoods;
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tvTitle.setText("商品分类");
        presenter = new ClassGoodsPresenter(this,this);
        listDataSave = new ListDataSave(getContext(),"class");
        LastModifyTime = SharedPreferencesUtils.getString(getContext(),"LastModifyTime","");
        presenter.getClassLastModifyTime();
        classList = listDataSave.getDataList("savaclassList");
        if (listDataSave!=null&&classList.size()>0){
            showData(classList);
        }
    }

    private void initView() {
        //从布局设置纵向的ListView
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvClassListView.setLayoutManager(manager);
        classAdapter = new ClassAdapter(this);
        rcvClassListView.setAdapter(classAdapter);
        classAdapter.setOnItemClickLitener(this);
        expandableAdapter = new ClassExpandableAdapter(this,groups,children);
    }

    /**
     * 设置二三级数据
     * @param dataGroup
     */
    private void setExpandableListView(final List<ClassTwoListViewBean> dataGroup) {
        groups.clear();
        children.clear();
        for (int i = 0; i < dataGroup.size(); i++) {
            groups.add(new ClassTwoListViewBean(dataGroup.get(i).getCategoryCode(), dataGroup.get(i).getCategoryName()));
            List<ClassThreeListViewBean> child = new ArrayList<>();
            for (int j = 0; j < dataGroup.get(i).getChilds().size(); j++) {
                child.add(new ClassThreeListViewBean(dataGroup.get(i).getChilds().get(j).getCategoryCode(),
                        dataGroup.get(i).getChilds().get(j).getCategoryName(),dataGroup.get(i).getChilds().get(j).getCategoryImg()));
            }
            children.put(groups.get(i).getCategoryCode(), child);
        }
        elvClass.setAdapter(expandableAdapter);   //设置它的adapter
        expandableAdapter.notifyDataSetChanged();
        for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
            elvClass.expandGroup(i);
        }
        elvClass.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // TODO Auto-generated method stub
                //设置点击不关闭（不收回）
                v.setClickable(true);
                return true;
            }
        });
    }


    @OnClick(R.id.bt_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * 数据初始化展示
     * @param data
     */
    private void showData(List<ClassOneListViewBean> data) {
        initView();
        classAdapter.setData(data);
        if (data!=null&&data.size()>0)
            setExpandableListView(data.get(0).getChilds());
    }

    @Override
    public void onClassGoodsListViewSuccess(List<ClassOneListViewBean>  data) {
        hideProgressDialog();
        classList = data;
        showData(data);
        listDataSave.setDataList("savaclassList",data);
    }


    @Override
    public void onClassGoodsListViewFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onClassLastModifyTimeFail(int code, String msg) {
        showProgressDialog();
        presenter.getClassGoodsList();
        showToast(msg);
    }

    @Override
    public void gonClassLastModifyTimeSuccess(String data) {
        SharedPreferencesUtils.saveString(getContext(),"LastModifyTime",data);
        if (TextUtils.isEmpty(LastModifyTime)||!LastModifyTime.equals(data)){ //表示数据更新
            showProgressDialog();
            presenter.getClassGoodsList();
        }
    }

    //点击切换一级分类 ，切换二三级
    @Override
    public void onItemClick(View view, TextView textView, int position) {
        setExpandableListView(classList.get(position).getChilds());
    }
}

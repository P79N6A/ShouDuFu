package com.futuretongfu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.ShoppingGroupBean;
import com.futuretongfu.bean.onlinegoods.ShoppingGoodsBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.CheckBoxInterface;
import com.futuretongfu.iview.GoodsNumChangeInterface;
import com.futuretongfu.iview.IOnlineShoppingView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.OnlineShoppingPresenter;
import com.futuretongfu.ui.activity.goods.FirmOrderActivity;
import com.futuretongfu.ui.adapter.ShoppingCartGroupListAdapter;
import com.futuretongfu.utils.StringUtil;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/3/14.
 * 首页--购物车
 */

public class ShoppingFragment extends BaseFragment implements IOnlineShoppingView, GoodsNumChangeInterface, CheckBoxInterface {
//    @Bind(R.id.swp_list)
//    public SwipeRefreshLayout swpList;

    @Bind(R.id.layout_empty)
    LinearLayout layout_empty;
    @Bind(R.id.tv_fshopping_manage)
    TextView tvFshoppingManage;
    @Bind(R.id.sul_fshopping_manage_list)
    ExpandableListView sulFshoppingManageList;
    @Bind(R.id.cb_fshopping_all)
    CheckBox cbFshoppingAll;
    @Bind(R.id.tv_fshopping_payprice)
    TextView tvFshoppingPayprice;
    @Bind(R.id.tv_fshopping_commit)
    TextView tvFshoppingCommit;
    @Bind(R.id.ll_fshopping_commit)
    LinearLayout llFshoppingCommit;
    private ShoppingCartGroupListAdapter groupListAdapter;
    /**
     * 为分组元素和孩子元素声明List
     * 组元素 的JavaBean中含有iD和name
     * 孩子元素就是商品的具体属性，也包括有没有被选中
     * 为了将孩子元素与组元素的数据联合起来，这里要设置组元素的id为主键，并使之成为孩子元素的外键
     */
    List<ShoppingGroupBean> groups = new ArrayList<>();
    Map<String, List<ShoppingGoodsBean>> children = new HashMap<>();
    private OnlineShoppingPresenter mPresenter;
    String userId = "";
    private double goodsAllTongbei;
    private double goodsAllPrice;
    private int goodsAllNum;
    List<ShoppingGoodsBean> goodsList = new ArrayList();
    private List<String> storeIdList = new ArrayList<>();


    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shopping;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = new OnlineShoppingPresenter(getActivity(), this);
        userId = UserManager.getInstance().getUserId() + "";
        mPresenter.getOnlineShoppingList(UserManager.getInstance().getUserId() + "");
    }

    /**
     * 模拟数据<br>
     * 遵循适配器的数据列表填充原则，组元素被放在一个List中，对应的组元素下辖的子元素被放在Map中，<br>
     * 其键是组元素的Id(通常是一个唯一指定组元素身份的值)
     * @param dataGroup
     */
    private void initData(List<ShoppingGroupBean> dataGroup) {
        groups.clear();
        children.clear();
        for (int i = 0; i < dataGroup.size(); i++) {
            groups.add(new ShoppingGroupBean(dataGroup.get(i).getId(), dataGroup.get(i).getStoreName()));
            List<ShoppingGoodsBean> child = new ArrayList<>();
            for (int j = 0; j < dataGroup.get(i).getStoreList().size(); j++) {
                child.add(new ShoppingGoodsBean(dataGroup.get(i).getStoreList().get(j).getCarId(),
                        dataGroup.get(i).getStoreList().get(j).getSkuName(),
                        dataGroup.get(i).getStoreList().get(j).getProductId(),
                        dataGroup.get(i).getStoreList().get(j).getSkuImages(),
                        dataGroup.get(i).getStoreList().get(j).getPrice(),
                        dataGroup.get(i).getStoreList().get(j).getNum(),
                        dataGroup.get(i).getStoreList().get(j).getSendTongBei(),
                        dataGroup.get(i).getStoreList().get(j).getFormat()));
            }
            children.put(groups.get(i).getId(), child);
        }
    }



    @Override
    public void onOnlineShoppingListSuccess(List<ShoppingGroupBean> data) {
//        if (swpList != null) {
//            swpList.setRefreshing(false);
//        }
        clearData();
        if (data == null || data.size() <= 0) {
            layout_empty.setVisibility(View.VISIBLE);
            llFshoppingCommit.setVisibility(View.GONE);
            sulFshoppingManageList.setVisibility(View.GONE);
            return;
        } else {
            layout_empty.setVisibility(View.GONE);
            llFshoppingCommit.setVisibility(View.VISIBLE);
            sulFshoppingManageList.setVisibility(View.VISIBLE);
            initData(data);
            groupListAdapter = new ShoppingCartGroupListAdapter(getActivity(), groups, children);
            groupListAdapter.setCheckBoxInterface(this);
            groupListAdapter.setGoodsNumChangeInterface(this);
            sulFshoppingManageList.setAdapter(groupListAdapter);
            for (int i = 0; i < groupListAdapter.getGroupCount(); i++) {
                sulFshoppingManageList.expandGroup(i);
            }
        }
    }

    private void clearData() {
        goodsAllPrice = 0.0;
        goodsAllTongbei = 0.0;
        goodsAllNum = 0;
        if (goodsList != null) {
            goodsList.clear();
        }
        if (storeIdList != null) {
            storeIdList.clear();
        }
        cbFshoppingAll.setChecked(false);
        tvFshoppingPayprice.setText("￥" +StringUtil.fmtMicrometer(goodsAllPrice));
        tvFshoppingCommit.setText("结算(" + goodsAllNum + ")");
    }

    private void setCounter() {
        goodsAllPrice = 0.0;
        goodsAllTongbei = 0.0;
        goodsAllNum = 0;
        if (goodsList != null) {
            goodsList.clear();
        }
        for (int i = 0; i < groups.size(); i++) {
            String id = groups.get(i).getId();
            List<ShoppingGoodsBean> productInfos = children.get(id);
            for (int j = 0; j < productInfos.size(); j++) {
                ShoppingGoodsBean productInfo = productInfos.get(j);
                if (productInfo.getIsChoosed()) {
                    goodsAllNum += productInfo.getNum();
                    goodsAllPrice += productInfo.getPrice() * productInfo.getNum();
                    goodsAllTongbei += productInfo.getSendTongBei() * productInfo.getNum();
                    if (!goodsList.contains(productInfo)) {
                        goodsList.add(productInfo);
                    }
                }
            }
        }
        tvFshoppingPayprice.setText("￥" + StringUtil.fmtMicrometer(goodsAllPrice));
        tvFshoppingCommit.setText("结算(" + goodsAllNum + ")");
    }


    private boolean isAllChecked() {
        for (ShoppingGroupBean group : groups) {
            if (!group.getIsChoosed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 全选
     */
    private void setAllCheck() {
        for (int i = 0; i < groups.size(); i++) {
            String id = groups.get(i).getId();
            groups.get(i).setIsChoosed(cbFshoppingAll.isChecked());
            List<ShoppingGoodsBean> child = children.get(id);
            for (int j = 0; j < child.size(); j++) {
                child.get(j).setIsChoosed(cbFshoppingAll.isChecked());
            }
        }
        setCount();
        if (groupListAdapter != null)
            groupListAdapter.notifyDataSetChanged();
    }


    private void setCount() {
        /**
         * 统计操作<br>
         * 1.先清空全局计数器<br>
         * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
         * 3.给底部的textView进行数据填充
         * */
        goodsAllNum = 0;
        goodsAllPrice = 0.0;
        goodsAllTongbei = 0.0;
        if (goodsList != null) {
            goodsList.clear();
        }
        for (int i = 0; i < groups.size(); i++) {
            double groupsPrice = 0;
            String id = groups.get(i).getId();
            List<ShoppingGoodsBean> productInfos = children.get(id);
            for (int j = 0; j < productInfos.size(); j++) {
                ShoppingGoodsBean productInfo = productInfos.get(j);
                if (productInfo.getIsChoosed()) {
                    goodsAllNum += productInfo.getNum();
                    goodsAllPrice += productInfo.getPrice() * productInfo.getNum();
                    goodsAllTongbei += productInfo.getSendTongBei() * productInfo.getNum();
                    groupsPrice += productInfo.getPrice() * productInfo.getNum();
                    if (!goodsList.contains(productInfo)) {
                        goodsList.add(productInfo);
                    }
                }
            }
            groups.get(i).setGroupPrice(groupsPrice);
        }
        tvFshoppingPayprice.setText("￥" + StringUtil.fmtMicrometer(goodsAllPrice));
        tvFshoppingCommit.setText("结算(" + goodsAllNum + ")");
    }


    @Override
    public void onOnlineShoppingListFail(String msg) {
//        if (swpList != null) {
//            swpList.setRefreshing(false);
//        }
        showToast(msg);
    }


    @Override
    public void onOnlineShoppingUpdateSuccess(int type, int count, int groupPosition, int childPosition, FuturePayApiResult futurePayApiResult) {
        setCounter();
        children.get(groups.get(groupPosition).getId()).get(childPosition).setNum(count);
        showToast("修改成功");
        if (type == 1) {
            if (children.get(groups.get(groupPosition)
                    .getId()).get(childPosition).getIsChoosed()) {
                groups.get(groupPosition).setGroupPrice(groups.get(groupPosition).getGroupPrice() + children.get(groups.get(groupPosition)
                        .getId()).get(childPosition).getPrice());
            }
        } else {
            if (children.get(groups.get(groupPosition)
                    .getId()).get(childPosition).getIsChoosed()) {
                groups.get(groupPosition).setGroupPrice(groups.get(groupPosition).getGroupPrice() - children.get(groups.get(groupPosition)
                        .getId()).get(childPosition).getPrice());
            }
        }
        groupListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onOnlineShoppingUpdateFaile(String msg) {
        showToast(msg);

    }

    @Override
    public void onOnlineShoppingDeleteSuccess(FuturePayApiResult futurePayApiResult) {
        showToast("删除成功");
        mPresenter.getOnlineShoppingList(userId);
        //TODO  刷新购物车数量
        Intent intent = new Intent();
        intent.setAction(Constants.REQUEST_CODE_SHOPCARTNUM);
        getActivity().sendBroadcast(intent);
    }

    @Override
    public void onOnlineShoppingDeleteFaile(String msg) {
        showToast(msg);
    }


    private double calculateAllPrice() {
        double price = 0;
        for (int i = 0; i < groups.size(); i++) {
            String id = groups.get(i).getId();
            List<ShoppingGoodsBean> child = children.get(id);
            for (int j = 0; j < child.size(); j++) {
                price += child.get(j).getNum() * child.get(j).getPrice();
            }
        }
        return price;
    }


    @OnClick({R.id.cb_fshopping_all, R.id.tv_fshopping_commit,R.id.tv_fshopping_manage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_fshopping_all:
                //全选
                setAllCheck();
                break;
            case R.id.tv_fshopping_manage:
//                tvFshoppingCommit.setText("删除(" + goodsAllNum + ")");
                break;
            case R.id.tv_fshopping_commit:
                if (tvFshoppingCommit.getText().toString().contains("删除")) {
                    tvFshoppingCommit.setBackgroundResource(R.color.all_track_color);
                    loadDeleteData();
                } else if (tvFshoppingCommit.getText().toString().contains("结算")) {
                    //提交购物车
                    if (goodsAllNum > 0) {
                        setCounter();
                        Intent intent = new Intent(getActivity(), FirmOrderActivity.class);
                        intent.putExtra("totalMoney", goodsAllPrice);
                        intent.putExtra("hehe","hhh");
                        intent.putExtra("goodsAllTongbei", goodsAllTongbei);
                        intent.putExtra("goodsList", (Serializable) goodsList);
                        if (storeIdList!=null&&storeIdList.size()>0)
                            intent.putExtra("onlineStoreId", storeIdList.get(0).toString());
                        startActivity(intent);
                    } else {
                        showToast("请先选择");
                    }
                }
                break;
        }
    }

    private void loadDeleteData() {
        //遍历出勾选的数据
        List<String> deleteIds = new ArrayList<>();
        for (int i = 0; i < groups.size(); i++) {
            String id = groups.get(i).getId();
            List<ShoppingGoodsBean> productInfos = children.get(id);
            for (int j = 0; j < productInfos.size(); j++) {
                ShoppingGoodsBean productInfo = productInfos.get(j);
                if (productInfo.getIsChoosed()) {
                    deleteIds.add(productInfo.getCarId() + "");
                }
            }
        }
        StringBuffer sbf = new StringBuffer();
        if (deleteIds != null) {
            for (int i = 0; i < deleteIds.size(); i++) {
                if (i == 0) {
                    sbf.append(deleteIds.get(i));
                } else {
                    sbf.append("," + deleteIds.get(i));
                }
            }
            if (deleteIds.size() == 0) {
                showToast("请先选择");
            } else {
                mPresenter.deleteOnlineShopping(userId,sbf.toString());
            }
        }
    }

    private int count;

    @Override
    public void addGoodsNumChange(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        ShoppingGoodsBean child = (ShoppingGoodsBean) groupListAdapter.getChild(groupPosition, childPosition);
        count = child.getNum();
        count++;
        mPresenter.updateOnlineShopping(1, count, groupPosition, childPosition, userId, child.getCarId(), count);
        child.setNum(count);
        ((TextView) showCountView).setText(count + "");
    }

    @Override
    public void reduceGoodsNumChange(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        ShoppingGoodsBean child = (ShoppingGoodsBean) groupListAdapter.getChild(groupPosition, childPosition);
        int countReduce = child.getNum();
        if (countReduce == 1) {
            return;
        }
        countReduce--;
        child.setNum(countReduce);
        ((TextView) showCountView).setText(countReduce + "");
        mPresenter.updateOnlineShopping(2, countReduce, groupPosition, childPosition, userId, child.getCarId(), countReduce);
    }

    @Override
    public void deleteGoodsChange(String id) {
        mPresenter.deleteOnlineShopping(userId, id);
    }

    @Override
    public void groupCheckStatus(int groupPosition, boolean isChecked) {
        String id = groups.get(groupPosition).getId();
        if (storeIdList != null && storeIdList.size() > 0 && !storeIdList.contains(id)) {
            showToast("不能同时购买不同店铺商品");
            return;
        }
        if (isChecked) {
            storeIdList.add(id);
        } else {
            storeIdList.remove(id);
        }
        groups.get(groupPosition).setIsChoosed(isChecked);
        List<ShoppingGoodsBean> productInfos = children.get(id);
        for (int i = 0; i < productInfos.size(); i++) {
            productInfos.get(i).setIsChoosed(isChecked);
        }
        if (isAllChecked()) {
            cbFshoppingAll.setChecked(true);
        } else {
            cbFshoppingAll.setChecked(false);
        }
        groupListAdapter.notifyDataSetChanged();
        setCounter();
    }

    @Override
    public void childrenCheckStatus(int groupPosition, int childrenPosition, boolean isChecked) {
        String id = groups.get(groupPosition).getId();
        if (storeIdList != null && storeIdList.size() > 0 && !storeIdList.contains(id)) {
            showToast("不能同时购买不同店铺商品");
            return;
        }
        boolean allChildSameStatus = true;
        //分组下的子孩子拿出来
        List<ShoppingGoodsBean> childs = children.get(id);
        if (isChecked) {
            storeIdList.add(id);
        } else {
            storeIdList.remove(id);
        }
        childs.get(childrenPosition).setIsChoosed(isChecked);
        for (int i = 0; i < childs.size(); i++) {
            if (childs.get(i).getIsChoosed() != isChecked) {
                allChildSameStatus = false;
                break;
            }
        }

        if (allChildSameStatus) {
            groups.get(groupPosition).setIsChoosed(isChecked);
        } else {
            groups.get(groupPosition).setIsChoosed(false);
        }
        if (isAllChecked()) {
            cbFshoppingAll.setChecked(true);
        } else {
            cbFshoppingAll.setChecked(false);
        }
        groupListAdapter.notifyDataSetChanged();
        setCounter();
    }


    @Override
    public void onResume() {
        mPresenter.getOnlineShoppingList(userId);
        //TODO  刷新购物车数量
        Intent intent = new Intent();
        intent.setAction(Constants.REQUEST_CODE_SHOPCARTNUM);
        getActivity().sendBroadcast(intent);
        super.onResume();
    }
}

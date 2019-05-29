package com.futuretongfu.ui.activity.shopping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.ShoppingGoodsBean;
import com.futuretongfu.bean.onlinegoods.ShoppingGroupBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.CheckBoxInterface;
import com.futuretongfu.iview.GoodsNumChangeInterface;
import com.futuretongfu.iview.IOnlineShoppingView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.OnlineShoppingPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.adapter.ShoppingCartGroupListAdapter;
import com.futuretongfu.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class ShopActivity extends BaseActivity implements IOnlineShoppingView, CheckBoxInterface, GoodsNumChangeInterface {
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
    List<ShoppingGroupBean> groups = new ArrayList<>();
    Map<String, List<ShoppingGoodsBean>> children = new HashMap<>();
    String userId = "";

    private double goodsAllTongbei;
    private double goodsAllPrice;
    private int goodsAllNum;
    List<ShoppingGoodsBean> goodsList = new ArrayList();
    private List<String> storeIdList = new ArrayList<>();

    private OnlineShoppingPresenter mPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mPresenter = new OnlineShoppingPresenter(this, this);
        userId = UserManager.getInstance().getUserId() + "";
        mPresenter.getOnlineShoppingList(UserManager.getInstance().getUserId() + "");
    }

   public static void startActivity(Context context) {
       Intent intent = new Intent(context, ShopActivity.class);
       context.startActivity(intent);
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
            groupListAdapter = new ShoppingCartGroupListAdapter(this, groups, children);
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
        tvFshoppingPayprice.setText("￥" + StringUtil.fmtMicrometer(goodsAllPrice));
        tvFshoppingCommit.setText("结算(" + goodsAllNum + ")");
    }

    @Override
    public void onOnlineShoppingListFail(String msg) {
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
        //结算
    private void setCounter() {
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

    @Override
    public void onOnlineShoppingUpdateFaile(String msg) {

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
    public void onOnlineShoppingDeleteSuccess(FuturePayApiResult futurePayApiResult) {
        showToast("删除成功");
        mPresenter.getOnlineShoppingList(userId);
        //TODO  刷新购物车数量
        Intent intent = new Intent();
        intent.setAction(Constants.REQUEST_CODE_SHOPCARTNUM);
        sendBroadcast(intent);
    }

    @Override
    public void onOnlineShoppingDeleteFaile(String msg) {
        showToast(msg);
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

    private boolean isAllChecked() {
        for (ShoppingGroupBean group : groups) {
            if (!group.getIsChoosed()) {
                return false;
            }
        }
        return true;
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
}

package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.ShoppingGoodsBean;
import com.futuretongfu.bean.onlinegoods.ShoppingGroupBean;
import com.futuretongfu.iview.CheckBoxInterface;
import com.futuretongfu.iview.GoodsNumChangeInterface;
import com.futuretongfu.ui.activity.goods.GoodsDetailsActivity;
import com.futuretongfu.ui.component.SwipeMenuLayout;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2017/2/27.
 */
public class ShoppingCartGroupListAdapter extends BaseExpandableListAdapter {
    private Context ctx;
    private List<ShoppingGroupBean> groups;
    private Map<String, List<ShoppingGoodsBean>> children;
    private CheckBoxInterface checkBoxInterface;
    private GoodsNumChangeInterface goodsNumChangeInterface;
    private List<Double> prices = new ArrayList<>();

    public ShoppingCartGroupListAdapter(Context context, List<ShoppingGroupBean> groups, Map<String, List<ShoppingGoodsBean>> children) {
        this.ctx = context;
        this.groups = groups;
        this.children = children;
        for (int i = 0; i < groups.size(); i++) {
            prices.add(0.0);
        }
    }

    public void setCheckBoxInterface(CheckBoxInterface checkBoxInterface) {
        this.checkBoxInterface = checkBoxInterface;
    }

    public void setGoodsNumChangeInterface(GoodsNumChangeInterface goodsNumChangeInterface) {
        this.goodsNumChangeInterface = goodsNumChangeInterface;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String id = groups.get(groupPosition).getId();
        return children.get(id).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<ShoppingGoodsBean> childs = children.get(groups.get(groupPosition).getId());
        return childs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        GroupsViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_shopcart_group, null);
            holder = new GroupsViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupsViewHolder) convertView.getTag();
        }
        final ShoppingGroupBean group = (ShoppingGroupBean) getGroup(groupPosition);
        if (group != null) {
            holder.tvItemShopgroupStorename.setText(group.getStoreName());
            holder.cbItemShopgroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double totalPrice = 0;
                    for (int i = 0; i < children.get(groups.get(groupPosition).getId()).size(); i++) {
                        totalPrice += children.get(groups.get(groupPosition).getId()).get(i).getPrice() *
                                children.get(groups.get(groupPosition).getId()).get(i).getNum();
                    }
                    if (((CheckBox) view).isChecked()) {
                        groups.get(groupPosition).setGroupPrice(totalPrice);
                        notifyDataSetChanged();
                    } else {
                        groups.get(groupPosition).setGroupPrice(0);
                        notifyDataSetChanged();
                    }
                    checkBoxInterface.groupCheckStatus(groupPosition, ((CheckBox) view).isChecked());
                }
            });
        }
        holder.cbItemShopgroup.setChecked(group.getIsChoosed());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        final ChildrenViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_shopcart_children, null);
            holder = new ChildrenViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildrenViewHolder) convertView.getTag();
        }
        final ShoppingGoodsBean child = (ShoppingGoodsBean) getChild(groupPosition, childPosition);
        if (child != null) {
            holder.tvItemShopchildrenGoodsname.setText(child.getSkuName());
            holder.tvItemShopchildrenPrice.setText(+child.getPrice() + "");
            holder.tvItemShopchildrenNum.setText(child.getNum() + "");
            holder.tvItemShopchildrenWeight.setText(child.getFormat());
            String str[] = child.getSkuImages().split("\\|");
            GlideLoad.loadImage(str[0],holder.imgItemShopchildrenImage);
            holder.cb_check.setChecked(child.getIsChoosed());
        }

        holder.cb_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    groups.get(groupPosition).setGroupPrice(groups.get(groupPosition).getGroupPrice() + child.getPrice() * child.getNum());
                    notifyDataSetChanged();
                } else {
                    groups.get(groupPosition).setGroupPrice(groups.get(groupPosition).getGroupPrice() - child.getPrice() * child.getNum());
                    notifyDataSetChanged();
                }
                checkBoxInterface.childrenCheckStatus(groupPosition, childPosition,
                        ((CheckBox) view).isChecked());
            }
        });
        holder.tvItemShopchildrenAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goodsNumChangeInterface.addGoodsNumChange(groupPosition, childPosition,
                        holder.tvItemShopchildrenNum, holder.cb_check.isChecked());
            }
        });
        holder.tvItemShopchildrenReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goodsNumChangeInterface.reduceGoodsNumChange(groupPosition, childPosition,
                        holder.tvItemShopchildrenNum, holder.cb_check.isChecked());
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.view_swipe_menu.smoothClose();
                //获取children list
                List<ShoppingGoodsBean> childs = children.get(groups.get(groupPosition).getId());
                childs.remove(childPosition);
                if (childs.size() == 0) {
                    //孩子元素全部被删除，组元素应该也消失
                    groups.remove(groupPosition);
                }
                goodsNumChangeInterface.deleteGoodsChange(child.getCarId());
                notifyDataSetChanged();
            }
        });
        holder.ll_onClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(ctx, GoodsDetailsActivity.class, "id", child.getProductId());
            }
        });
        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }




    static class GroupsViewHolder {
        @Bind(R.id.cb_item_shopgroup)
        CheckBox cbItemShopgroup;
        @Bind(R.id.tv_item_shopgroup_storename)
        TextView tvItemShopgroupStorename;
        @Bind(R.id.SwipeMenuExpandableListView)
        LinearLayout SwipeMenuExpandableListView;

        GroupsViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildrenViewHolder {
        @Bind(R.id.cb_item_shopchildren_choose)
        CheckBox cb_check;
        @Bind(R.id.img_item_shopchildren_image)
        ImageView imgItemShopchildrenImage;
        @Bind(R.id.tv_item_shopchildren_ccovergoods)
        TextView tvItemShopchildrenCcovergoods;
        @Bind(R.id.tv_item_shopchildren_goodsname)
        TextView tvItemShopchildrenGoodsname;
        @Bind(R.id.tv_item_shopchildren_price)
        TextView tvItemShopchildrenPrice;
        @Bind(R.id.tv_item_shopchildren_weight)
        TextView tvItemShopchildrenWeight;
        @Bind(R.id.tv_item_shopchildren_reduce)
        TextView tvItemShopchildrenReduce;
        @Bind(R.id.tv_item_shopchildren_num)
        TextView tvItemShopchildrenNum;
        @Bind(R.id.tv_item_shopchildren_add)
        TextView tvItemShopchildrenAdd;
        @Bind(R.id.btn_delete)
        TextView btn_delete;
        @Bind(R.id.ll_onClick)
        LinearLayout ll_onClick;
        @Bind(R.id.view_swipe_menu)
        SwipeMenuLayout view_swipe_menu;
        ChildrenViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}

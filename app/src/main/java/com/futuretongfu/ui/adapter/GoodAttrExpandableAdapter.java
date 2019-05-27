package com.futuretongfu.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.classification.ClassThreeListViewBean;
import com.futuretongfu.bean.classification.ClassTwoListViewBean;
import com.futuretongfu.bean.onlinegoods.GoodsAttrValuesBean;
import com.futuretongfu.bean.onlinegoods.GoodsAttrValuesList;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.MyGridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2017/3/14.
 */
public class GoodAttrExpandableAdapter extends BaseExpandableListAdapter implements GoodsDesColorRvAdapter.MyItemListener {

    private Activity ctx;
    private List<GoodsAttrValuesList> groups;
    private Map<String, List<GoodsAttrValuesBean>> children;
    private String format="";

    public GoodAttrExpandableAdapter(Activity activity, MyItemListener myItemListener, List<GoodsAttrValuesList> groups,
                                     Map<String, List<GoodsAttrValuesBean>> children) {
        this.ctx = activity;
        this.myItemListener = myItemListener;
        this.groups = groups;
        this.children = children;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childrenPosition) {
        return childrenPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childrenPosition) {
        return childrenPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        GroupViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_classification_group, null);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.tvName.setText(groups.get(groupPosition).getAttributeName());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childrenPosition, boolean b, View convertView, ViewGroup viewGroup) {
        ChildrenViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_classification_children, null);
            holder = new ChildrenViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildrenViewHolder) convertView.getTag();
        }
        final List<GoodsAttrValuesBean> childrenInfoList = children.get(groups.get(groupPosition).getAttributeId());
        holder.gvClassification.setAdapter(new GoodsDesColorRvAdapter(ctx,groups.get(groupPosition).getAttributeId(), childrenInfoList,this,format));
        return convertView;
    }

    private MyItemListener myItemListener;

    @Override
    public void onitemAdapterClick(String attributeId,GoodsAttrValuesBean item,String formatInfo) {
            myItemListener.onitemAdapterClick(attributeId,item,formatInfo);
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public interface MyItemListener {
        public void onitemAdapterClick(String attributeId, GoodsAttrValuesBean item,String formatInfo);
    }

    static class GroupViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildrenViewHolder {
        @Bind(R.id.gv_classification)
        MyGridView gvClassification;

        ChildrenViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childrenPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

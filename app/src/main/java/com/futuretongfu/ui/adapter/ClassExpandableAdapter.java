package com.futuretongfu.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.TextView;


import com.futuretongfu.R;
import com.futuretongfu.bean.classification.ClassThreeListViewBean;
import com.futuretongfu.bean.classification.ClassTwoListViewBean;
import com.futuretongfu.view.MyGridView;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2017/3/14.
 */
public class ClassExpandableAdapter extends BaseExpandableListAdapter {

    private Activity ctx;
    private List<ClassTwoListViewBean> groups;
    private Map<String, List<ClassThreeListViewBean>> children;

    public ClassExpandableAdapter(Activity activity, List<ClassTwoListViewBean> groups,
                            Map<String, List<ClassThreeListViewBean>> children) {
        this.ctx = activity;
        this.groups = groups;
        this.children = children;
    }

    public void setClassData(List<ClassTwoListViewBean> groups, Map<String, List<ClassThreeListViewBean>> children) {
        this.groups = groups;
        this.children = children;
        notifyDataSetChanged();
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
        holder.tvName.setText(groups.get(groupPosition).getCategoryName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childrenPosition, boolean b, View convertView, ViewGroup viewGroup) {
        ChildrenViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_classification_children, null);
            holder = new ChildrenViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildrenViewHolder) convertView.getTag();
        }
        List<ClassThreeListViewBean> childrenInfoList = children.get(groups.get(groupPosition).getCategoryCode());
        holder.gvClassification.setAdapter(new ClassDataGridViewAdapter(ctx, childrenInfoList));
//        AdapterShowOneLines.setGridViewHeightBasedOnChildren(holder.gvClassification);
        return convertView;
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

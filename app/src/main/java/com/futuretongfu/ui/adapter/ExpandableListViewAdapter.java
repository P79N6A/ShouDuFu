package com.futuretongfu.ui.adapter;

/**
 * Created by GF-Zhang PC on 2017/12/18.
 */

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.ExchangeListBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 自定义Adapter
 */
public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    Activity context;
    private  String[] groups;
    private Map<String, List<ExchangeListBean>> children;

    public ExpandableListViewAdapter(Activity context, String[] groups, Map<String, List<ExchangeListBean>> children) {
        this.context = context;
        this.groups = groups;
        this.children = children;
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
    // TODO Auto-generated method stub
        return childPosition;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ChildrenViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_expandable_childen_list, null);
            holder = new ChildrenViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildrenViewHolder) convertView.getTag();
        }
        //从布局设置纵向的ListView
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.listview.setLayoutManager(manager);
        holder.listview.setNestedScrollingEnabled(false);
//        List<ExchangeListBean> childrenInfoList = children.get(groups.get(groupPosition).getId());
        List<ExchangeListBean> childrenInfoList = children.get(groups[groupPosition]);
        final ExpandableSecondsAdapter adapter = new ExpandableSecondsAdapter(context,childrenInfoList);
        holder.listview.setAdapter(adapter);
        return convertView;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int groupPosition, int position);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
    // TODO Auto-generated method stub
        return 1;
    }


    public Object getGroup(int groupPosition) {
        return children.get(groups[groupPosition]);
    }


    @Override
    public int getGroupCount() {
    // TODO Auto-generated method stub
        return groups.length;
    }


    @Override
    public long getGroupId(int groupPosition) {
    // TODO Auto-generated method stub
        return groupPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ViewChild mViewChild;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_expandable_group, null);
            mViewChild = new ViewChild(convertView);
            convertView.setTag(mViewChild);
        } else {
            mViewChild = (ViewChild) convertView.getTag();
        }
        if (isExpanded) {
            mViewChild.imageView.setImageResource(R.mipmap.xiangshang);
        } else {
            mViewChild.imageView.setImageResource(R.mipmap.xiangxia);
        }
        mViewChild.textView.setText(groups[groupPosition]);
        return convertView;
    }


    @Override
    public boolean hasStableIds() {
    // TODO Auto-generated method stub
        return true;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition){
        return false;
    }


    static class ViewChild {
        @Bind(R.id.channel_imageview_orientation)
        ImageView imageView;
        @Bind(R.id.channel_group_name)
        TextView textView;
        ViewChild(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildrenViewHolder {
        @Bind(R.id.channel_item_child_listview)
        RecyclerView listview;
        ChildrenViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

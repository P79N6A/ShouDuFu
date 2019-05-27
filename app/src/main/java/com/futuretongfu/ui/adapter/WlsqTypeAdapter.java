package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.ui.activity.ShangQuanActivity;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.R;
import com.futuretongfu.bean.WlsqTypeBean;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.ui.activity.SearchStoreActivity;
import com.futuretongfu.utils.GlideLoad;

import java.util.List;

/**
 * 未来商圈分类adapter
 *
 * @author DoneYang 2017/6/20
 */

public class WlsqTypeAdapter extends BaseAdapter {

    private  Context context;
    private List<WlsqTypeBean> mDatas;
    private LayoutInflater mLayoutInflater;
    /**
     * 页数下标,从0开始(通俗讲第几页)
     */
    private int mIndex;
    /**
     * 每页显示最大条目个数 ,默认是dimes.xml里 HomePageHeaderColumn 属性值的两倍(每页多少个图标)
     */
    private int mPageSize;

    public WlsqTypeAdapter(Context context, List<WlsqTypeBean> mDatas, int mIndex) {
        this.mDatas = mDatas;
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mIndex = mIndex;
        mPageSize = context.getResources().getInteger(R.integer.PageWlsqTypColumn) * 2;
    }

    /**
     * 先判断数据集的大小是否足够显示满本页？mDatas.size() > (mIndex+1)*mPageSize,
     * 如果够，则直接返回每一页显示的最大条目个数mPageSize,
     * 如果不够，则有几项返回几,(mDatas.size() - mIndex * mPageSize);(说白了 最后一页就显示剩余item)
     */
    @Override
    public int getCount() {
        return mDatas.size() > (mIndex + 1) * mPageSize ? mPageSize : (mDatas.size() - mIndex * mPageSize);

    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position + mIndex * mPageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + mIndex * mPageSize;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_wlsq_type_list, parent, false);
            vh = new ViewHolder();
            vh.tv = (TextView) convertView.findViewById(R.id.tv_item_wlsq_type_name);
            vh.iv = (ImageView) convertView.findViewById(R.id.image_item_wlsq_type_icon);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        /**
         * 在给View绑定显示的数据时，计算正确的position = position + mIndex * mPageSize，
         */
       final int pos = position + mIndex * mPageSize;
        vh.tv.setText(mDatas.get(pos).hyNamePc);
//        vh.iv.setImageResource(mDatas.get(pos).icon);
        GlideLoad.loadCropCircle(mDatas.get(pos).icon, vh.iv);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ShangQuanActivity.class);
                intent.putExtra("position",pos);
                context.startActivity(intent);
//                IntentUtil.startActivity(context, SearchStoreActivity.class, IntentKey.WLF_TYPE, mDatas.get(pos).hyNamePc, IntentKey.WLF_ID, "" + mDatas.get(pos).id);
            }
        });
        return convertView;
    }


    class ViewHolder {
        TextView tv;
        ImageView iv;
    }
}

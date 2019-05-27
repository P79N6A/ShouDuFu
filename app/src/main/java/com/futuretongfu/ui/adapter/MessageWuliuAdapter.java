package com.futuretongfu.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.model.entity.MessageListInfo;

import java.util.List;

/**
 * 消息物流
 * Created by zhanggf on 2017/6/19.
 */

public class MessageWuliuAdapter extends BaseQuickAdapter<MessageListInfo, BaseViewHolder> {

    private Context context;
    private String Yjjljf;

    public MessageWuliuAdapter(Context context, List<MessageListInfo> datas) {
        super(R.layout.item_system_notice_list, datas);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final MessageListInfo item) {
        holder.getView(R.id.image_item_system_notice_read);
        holder.setText(R.id.tv_item_system_notice_title, item.getTitle())
                .setText(R.id.tv_item_system_notice_date, item.getCreateTime())
                .setText(R.id.tv_item_system_notice_content, item.getContent());
    }

}

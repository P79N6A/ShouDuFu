package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.model.entity.SafeQuestion;
import com.futuretongfu.R;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/7/4.
 */

public class SafeQuestionAdapter extends BaseQuickAdapter<SafeQuestion, BaseViewHolder> {

    private Context context;

    public SafeQuestionAdapter(Context context, List<SafeQuestion> datas, SafeQuestionAdapterListener safeQuestionAdapterListener){
        super(R.layout.item_safe_question_list, datas);
        this.context = context;
        this.safeQuestionAdapterListener = safeQuestionAdapterListener;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final SafeQuestion item){
        holder.setText(R.id.text_title, item.getQuestion());

        holder.setOnClickListener(R.id.view_click, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(safeQuestionAdapterListener != null){
                    safeQuestionAdapterListener.onSafeQuestionSelect(item);
                }
            }
        });
    }

    private SafeQuestionAdapterListener safeQuestionAdapterListener;
    public interface SafeQuestionAdapterListener{
        public void onSafeQuestionSelect(SafeQuestion item);
    }
}

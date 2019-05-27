package com.futuretongfu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.wlsq.LookBigImageMaxActivity;
import com.futuretongfu.utils.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 商家介绍
 *
 * @author DoneYang 2017/6/19
 */

public class StoreIntroFragment extends BaseFragment {


    @Bind(R.id.tv_fragment_store_intro_content)
    TextView tv_content;

    @Bind(R.id.rv_store_intro)
    RecyclerView rv_intro;

    private List<String> stringList = new ArrayList<>();

    private ImageAdapter mAdapter;

//    private OnStoreDetailIntro onStoreDetailIntro;

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_store_intro;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mAdapter = new ImageAdapter(stringList);
        Util.setRecyclerViewHorizontal(getActivity(), rv_intro, R.color.transparent, 18);
        rv_intro.setAdapter(mAdapter);
        rv_intro.setNestedScrollingEnabled(false);
    }

    public void update(String intro) {
        tv_content.setText(intro);
    }

    public void updataList(List<String> list) {
        stringList.clear();
        stringList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    private class ImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        ImageAdapter(@Nullable List<String> data) {
            super(R.layout.item_store_intro_list, data);
        }

        @Override
        protected void convert(final BaseViewHolder helper, final String item) {
            ImageView image = helper.getView(R.id.image_item_store_intro1);
            if (!Util.isEmpty(item)) {
                GlideLoad.loadImage(item, image);
            }

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), LookBigImageMaxActivity.class);
                    intent.putExtra(IntentKey.LOOKMAX_IMAGE_POSITION, helper.getLayoutPosition());
                    intent.putExtra(IntentKey.LOOKMAX_IMAGE_URL, (Serializable) stringList);
                    startActivity(intent);
                }
            });
        }
    }

}

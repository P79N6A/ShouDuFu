package com.futuretongfu.ui.activity.wlsq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.view.zoompicture.PhotoView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 查看图片放大
 *
 * @author DoneYang 2017/7/1
 */

public class LookBigImageMaxActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.vp_lookbigimagemax)
    ViewPager vp_lookImage;

    private List<String> mCutedImageDatas;
    private int imagePosition;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lookbigimagemax;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initData();
        initViewPage();
    }

    private void initViewPage() {
        ViewPagerAdapter adapter = new ViewPagerAdapter();
        vp_lookImage.setAdapter(adapter);
        vp_lookImage.setCurrentItem(imagePosition);
        vp_lookImage.addOnPageChangeListener(this);
        vp_lookImage.setEnabled(false);
        // 设定当前的页数和总页数
        tv_title.setText((imagePosition + 1) + "/" + mCutedImageDatas.size());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tv_title.setText((position + 1) + "/" + mCutedImageDatas.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick({R.id.bt_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
        }
    }

    /**
     * ViewPager的适配器
     */
    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            PhotoView view = new PhotoView(LookBigImageMaxActivity.this);
            view.enable();
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            GlideLoad.loadRound(mCutedImageDatas.get(position), view);

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return mCutedImageDatas.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private void initData() {
        Intent intent = getIntent();
        mCutedImageDatas = (List<String>) intent.getSerializableExtra(IntentKey.LOOKMAX_IMAGE_URL);
        imagePosition = intent.getIntExtra(IntentKey.LOOKMAX_IMAGE_POSITION, 0);
    }
}

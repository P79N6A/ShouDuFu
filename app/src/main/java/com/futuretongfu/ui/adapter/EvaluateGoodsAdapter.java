package com.futuretongfu.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.OrderOnlineGoodsEvaluateBean;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.NineImgViewUtil;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.view.MyRatingBar;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPicker;

/**
 * Created by ChenXiaoPeng on 2017/6/19.
 */

public class EvaluateGoodsAdapter extends BaseAdapter implements NineImgViewUtil.NineImgViewUtilListener {
    private final Activity ctx;
    private EditText evaluate_info;
    private NineImgViewUtil nineImgViewUtil;

    public EvaluateGoodsAdapter(Activity ctx,MyAdapterListener myListener) {
        this.ctx = ctx;
        this.myListener = myListener;
    }

    public void setList(List<OrderOnlineGoodsEvaluateBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<OrderOnlineGoodsEvaluateBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    private List<OrderOnlineGoodsEvaluateBean> list = new ArrayList<OrderOnlineGoodsEvaluateBean>();

    public EditText getInfo() {
        return evaluate_info;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Integer index = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_recycleview_publishevaluate, null);
            holder = new ViewHolder(convertView);
            holder.etGoodsEvaluate.setTag(position);
            holder.etGoodsEvaluate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        index = (Integer) v.getTag();
                    }
                    return false;
                }
            });
            class MyTextWatcher implements TextWatcher {
                public MyTextWatcher(ViewHolder holder) {
                    mHolder = holder;
                }

                private ViewHolder mHolder;

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s != null && !"".equals(s.toString())) {
                        int position = (Integer) mHolder.etGoodsEvaluate.getTag();
                        list.get(position).setEvaluateInfo(s.toString());
                    }
                }
            }
            holder.etGoodsEvaluate.addTextChangedListener(new MyTextWatcher(holder));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.etGoodsEvaluate.setTag(position);
        }
        if (!TextUtils.isEmpty(list.get(position).getSkuImages())){
            String str[] =list.get(position).getSkuImages().split("\\|");
            GlideLoad.loadImage(str[0],holder.imgGoodsImage);
        }
        holder.etGoodsEvaluate.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
        evaluate_info = holder.etGoodsEvaluate;
        if (list.get(position).getEvaluateInfo() != null) {
            holder.etGoodsEvaluate.setText(list.get(position).getEvaluateInfo());
        } else {
            holder.etGoodsEvaluate.setText("");
        }
        holder.etGoodsEvaluate.clearFocus();
        if (index != -1 && index == position) {
            holder.etGoodsEvaluate.requestFocus();
        }
        holder.rbGoodsevaluateEvaluate.setOnRatingChangeListener(new MyRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float rating) {
                list.get(position).setSkuLevel((int) rating);
            }
        });
        holder.rbGoodsevaluateEvaluate.setStepSize(MyRatingBar.StepSize.Full);//设置每次点击增加一颗星还是半颗星
        holder.rbGoodsevaluateEvaluate.setOnRatingChangeListener(new MyRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float rating) {
                list.get(position).setSkuLevel((int) rating);
            }
        });
        nineImgViewUtil = new NineImgViewUtil(ctx, 3, true, R.mipmap.icon_comera, this);
        nineImgViewUtil.setImgaeView(holder.imgvPhoto1, holder.imgvPhoto2, holder.imgvPhoto3)
                .setDeleteView(holder.imgvClose1, holder.imgvClose2, holder.imgvClose3);
        myListener.onMyAdapter2Click(nineImgViewUtil);
        return convertView;
    }

    //图片的显示与隐藏
    public void setImageViewVisibility(ImageView img1, boolean boo1, ImageView img2, boolean boo2, ImageView img3, boolean boo3) {
        img1.setVisibility(boo1 ? View.VISIBLE : View.GONE);
        img2.setVisibility(boo2 ? View.VISIBLE : View.GONE);
        img3.setVisibility(boo3 ? View.VISIBLE : View.GONE);
    }

    private MyAdapterListener myListener;

    @Override
    public void onNineImgViewOpenImageSelector() {
        if (PermissionUtil.permissionReadStorage(ctx)) {
            openImageSingleSelector();
        }
    }

    /**
     * 打开图片选择器
     */
    private void openImageSingleSelector() {
        PhotoPicker.builder()
                .setPhotoCount(nineImgViewUtil.getCanSelecCount())
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(false)
                .start(ctx, PhotoPicker.REQUEST_CODE);
    }

    public interface MyAdapterListener {
        void onMyAdapterClick( RoundedImageView imgvPhoto1, RoundedImageView imgvPhoto2, RoundedImageView imgvPhoto3,
                                      ImageView imgvClose1,ImageView imgvClose2,ImageView imgvClose3);
        void onMyAdapter2Click(NineImgViewUtil nineImgViewUtil);
    }

    static class ViewHolder {
        @Bind(R.id.img_goods_image)
        ImageView imgGoodsImage;
        @Bind(R.id.rb_goodsevaluate_evaluate)
        MyRatingBar rbGoodsevaluateEvaluate;
        @Bind(R.id.et_goods_evaluate)
        EditText etGoodsEvaluate;
        @Bind(R.id.imgv_photo_1)
        RoundedImageView imgvPhoto1;
        @Bind(R.id.imgv_close_1)
        ImageView imgvClose1;
        @Bind(R.id.imgv_photo_2)
        RoundedImageView imgvPhoto2;
        @Bind(R.id.imgv_close_2)
        ImageView imgvClose2;
        @Bind(R.id.imgv_photo_3)
        RoundedImageView imgvPhoto3;
        @Bind(R.id.imgv_close_3)
        ImageView imgvClose3;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


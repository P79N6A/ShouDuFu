package com.futuretongfu.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.futuretongfu.R;
import com.futuretongfu.glide.transformations.CropCircleTransformation;
import com.futuretongfu.glide.transformations.RoundTransformation;


/**
 * 加载图片的工具类
 *
 * @author DoneYang
 */
public class GlideLoad {

    private static DiskCacheStrategy diskCache = DiskCacheStrategy.ALL;//磁盘缓存
    private static boolean isSkipMemoryCache = false;//禁止内存缓存

    public static void init() {

    }


    /**
     * Glide特点
     * 使用简单
     * 可配置度高，自适应程度高
     * 支持常见图片格式 Jpg png gif webp
     * 支持多种数据源  网络、本地、资源、Assets 等
     * 高效缓存策略    支持Memory和Disk图片缓存 默认Bitmap格式采用RGB_565内存使用至少减少一半
     * 生命周期集成   根据Activity/Fragment生命周期自动管理请求
     * 高效处理Bitmap  使用Bitmap Pool使Bitmap复用，主动调用recycle回收需要回收的Bitmap，减小系统回收压力
     * 这里默认支持Context，Glide支持Context,Activity,Fragment，FragmentActivity
     */

    //默认加载
    public static void loadImageView(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache).into(mImageView);
    }

    //默认加载 淡入效果
    public static void loadCrossFadeImageView(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache).crossFade().into(mImageView);
    }

    public static void loadCrossFadeImageView(String path, ImageView mImageView) {
        Glide.with(mImageView.getContext())
                .load(path).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .crossFade()
                .into(mImageView);
    }

    public static void loadCrossFadeImageView2(String path, ImageView mImageView) {
        Glide.with(mImageView.getContext())
                .load(path).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(R.mipmap.icon_bg)
                .error(R.mipmap.icon_bg)
                .crossFade()
                .into(mImageView);
    }

    public static void loadCrossFadeImageView(String path, int lodingImageID, int errorImageID, ImageView mImageView) {
        Glide.with(mImageView.getContext())
                .load(path).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(lodingImageID).error(errorImageID).crossFade().into(mImageView);
    }

    public static void loadCrossFadeCropCircleImageView(String path, int lodingImageID, int errorImageID, ImageView mImageView) {
        Glide.with(mImageView.getContext())
                .load(path).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(lodingImageID).error(errorImageID).crossFade()
                .bitmapTransform(new CropCircleTransformation(mImageView.getContext()))
                .into(mImageView);

    }


    //加载指定大小
    public static void loadImageViewSize(Context mContext, String path, int width, int height, ImageView mImageView) {
        Glide.with(mContext)
                .load(path).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .override(width, height).into(mImageView);
    }

    //设置加载中以及加载失败图片
    public static void loadImageViewLoding(Context mContext, String path, ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mContext)
                .load(path).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    //设置加载中以及加载失败图片并且指定大小
    public static void loadImageViewLodingSize(Context mContext, String path, int width, int height, ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mContext)
                .load(path).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .override(width, height).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    //设置跳过内存缓存
    public static void loadImageViewCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).skipMemoryCache(true).into(mImageView);
    }

    //设置下载优先级
    public static void loadImageViewPriority(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).priority(Priority.NORMAL).into(mImageView);
    }

    /**
     * 策略解说：
     * <p>
     * all:缓存源资源和转换后的资源
     * <p>
     * none:不作任何磁盘缓存
     * <p>
     * source:缓存源资源
     * <p>
     * result：缓存转换后的资源
     */

    //设置缓存策略
    public static void loadImageViewDiskCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
    }

    /**
     * api也提供了几个常用的动画：比如crossFade()
     */

    //设置加载动画
    public static void loadImageViewAnim(Context mContext, String path, int anim, ImageView mImageView) {
        Glide.with(mContext).load(path).animate(anim).into(mImageView);
    }

    /**
     * 会先加载缩略图
     */

    //设置缩略图支持
    public static void loadImageViewThumbnail(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).thumbnail(0.1f).into(mImageView);
    }

    /**
     * api提供了比如：centerCrop()、fitCenter()等
     */
    //设置动态转换
    public static void loadImageViewCrop(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().into(mImageView);
    }

    //设置动态GIF加载方式
    public static void loadImageViewDynamicGif(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).asGif().into(mImageView);
    }

    //设置静态GIF加载方式
    public static void loadImageViewStaticGif(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).asBitmap().into(mImageView);
    }

    //设置监听的用处 可以用于监控请求发生错误来源，以及图片来源 是内存还是磁盘

    //设置监听请求接口
    public static void loadImageViewListener(Context mContext, String path, ImageView mImageView, RequestListener<String, GlideDrawable> requstlistener) {
        Glide.with(mContext).load(path).listener(requstlistener).into(mImageView);
    }

    //项目中有很多需要先下载图片然后再做一些合成的功能，比如项目中出现的图文混排
    //设置要加载的内容
    public static void loadImageViewContent(Context mContext, String path, SimpleTarget<GlideDrawable> simpleTarget) {
        Glide.with(mContext).load(path).centerCrop().into(simpleTarget);
    }

    //清理磁盘缓存
    public static void GuideClearDiskCache(Context mContext) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }

    //清理内存缓存
    public static void GuideClearMemory(Context mContext) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }


    /**
     * 加载默认 图片
     *
     * @param imageView
     */
    public static void loadDefult(ImageView imageView) {
        load(R.mipmap.icon_pic2, imageView);
    }

    /**
     * 加载默认图片，为圆形
     *
     * @param imageView
     */
    public static void loadDefultCropCircle(ImageView imageView) {
        loadCropCircle(R.mipmap.icon_head_2, imageView);
    }

    /**
     * 加载图片
     *
     * @param url
     * @param imageView
     */
    public static void load(String url, ImageView imageView) {
        String imgUrl = url;
//        if (!Util.isEmpty(url)
//                && url.contains("/head/")
//                && !(url.contains("http://") || url.contains("https://"))) {
//            imgUrl = Config.getInstance().getIMAGE_URL() + url;
//        }
        Glide.with(imageView.getContext())
                .load(imgUrl).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .into(imageView);
    }

    /**
     * 加载banner图片
     */
    public static void loadBannerImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(R.mipmap.icon_pic_3)
                .error(R.mipmap.icon_pic_3)
                .into(imageView);
    }

    /**
     * 加载图片
     *
     * @param url
     * @param imageView
     */
    public static void loadImage(String url, ImageView imageView) {
        String imgUrl = url;
//        if (!Util.isEmpty(url)
////                && url.contains("/head/")
//                && !(url.contains("http://") || url.contains("https://"))) {
//            imgUrl = Config.getInstance().getIMAGE_URL() + url;
//        }
        Glide.with(imageView.getContext())
                .load(imgUrl).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(R.mipmap.icon_pic2)
                .error(R.mipmap.icon_pic2)
                .into(imageView);
    }


    /**
     * 加载项目下的本地图片
     *
     * @param resId
     * @param imageView
     */
    public static void load(Integer resId, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(resId).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .into(imageView);
    }

    /**
     * 加载图片，并处理成圆形
     *
     * @param url
     * @param imageView
     */
    public static void loadCropCircle(String url, ImageView imageView) {
        String imgUrl = url;
        Glide.with(imageView.getContext())
                .load(imgUrl).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(R.mipmap.icon_head_2)
                .error(R.mipmap.icon_head_2)
                .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 加载图片，并处理成圆形
     *
     * @param url
     * @param imageView
     */
    public static void loadCompanyHead(String url, ImageView imageView) {
        String imgUrl = url;
//        if (!Util.isEmpty(url)
////                && url.contains("/head/")
//                && !(url.contains("http://") || url.contains("https://"))) {
//        }
        Glide.with(imageView.getContext())
                .load(imgUrl).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(R.mipmap.icon_head_2)
                .error(R.mipmap.icon_head_2)
                .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 加载项目下的本地图片，并处理为圆形
     *
     * @param resId
     * @param imageView
     */
    public static void loadCropCircle(Integer resId, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(resId).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 加载项目下的本地图片，并处理为圆形
     *
     * @param imgUrl
     * @param imageView
     */
    public static void loadCropCircleHead(String imgUrl, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(imgUrl).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(R.mipmap.icon_head_2)
                .error(R.mipmap.icon_head_2)
                .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 加载图片，并处理成圆角
     * 默认角度--矩形圆角
     * @param imgUrl
     * @param imageView
     */
    public static void loadHead(String imgUrl, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(imgUrl).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(R.mipmap.icon_head_2)
                .error(R.mipmap.icon_head_2)
                .bitmapTransform(new RoundTransformation(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 加载头像图片，并处理成圆角
     * 默认角度--矩形圆角
     * @param imgUrl
     * @param imageView
     */
    public static void loadRound(String imgUrl, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(imgUrl).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(R.mipmap.icon_pic2)
                .error(R.mipmap.icon_pic2)
                .bitmapTransform(new RoundTransformation(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 加载图片，并处理成圆角
     * 自定义角度
     *
     * @param imgUrl
     * @param round
     * @param imageView
     */
    public static void loadRound(String imgUrl, int round, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(imgUrl).diskCacheStrategy(diskCache).skipMemoryCache(isSkipMemoryCache)
                .placeholder(R.mipmap.icon_pic2)
                .error(R.mipmap.icon_pic2)
                .bitmapTransform(new RoundTransformation(imageView.getContext(), round))
                .into(imageView);
    }


}

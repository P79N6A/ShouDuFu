package com.futuretongfu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.utils.Logger.Logger;

import java.util.List;

/**
 * 分享
 *
 * @author DoneYang.
 */
public class ShareUtil {
    private static String iamgeUrl = "";
    /**
     * 友盟分享
     * @param activity
     * @param platform
     */
    public static void uMengShareApp(Activity activity, SHARE_MEDIA platform) {
        final UMShareListener umShareListener = new CustomShareListener(activity);
//        if(!TextUtils.isEmpty(UserManager.getInstance().getUserKey())){
////            if(Constants.User_Type_XK!= UserManager.getInstance().getUserType() && !TextUtils.isEmpty(UserManager.getInstance().getUserKey())){
//           iamgeUrl = Constants.Url_share_register+"?referralCode="+UserManager.getInstance().getUserKey() ;
//        }else{
//           iamgeUrl = Constants.Url_share_register;
//        }
        iamgeUrl = Constants.Url_share_register;
        Logger.d("uMengShareApp",iamgeUrl);
        UMImage image = new UMImage(activity, iamgeUrl);//网络图片
        UMImage thumb = new UMImage(activity, R.mipmap.app_icon);
        image.setThumb(thumb);
        UMWeb web = new UMWeb(iamgeUrl);
        web.setTitle("你敢来 我敢送");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription("你来就送200体验金的神奇App");//描述
        new ShareAction(activity)
                .setPlatform(platform).withText("我正在用【首都富】新零售平台，消费多少返多少，分享会员、商家有奖励！凡注册为新用户，即可获赠200元超值体验金，你还不快来！")
                .setCallback(umShareListener)
                .withMedia(web)
                .share();
    }


    public static void uMengShareUrl(String ShareTitle,String ShareText,String ShareImage,String id,Activity activity, SHARE_MEDIA platform) {
        final UMShareListener umShareListener = new CustomShareListener(activity);
        String webUrl = SharedPreferencesUtils.getString(activity,"BaseWebUrl","");
        iamgeUrl = webUrl+Constants.Url_OnlineGoods_Share+id;
        UMImage image = new UMImage(activity, ShareImage);//网络图片
        UMWeb web = new UMWeb(iamgeUrl);
        web.setTitle(ShareTitle);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(ShareText);//描述
        new ShareAction(activity)
                .setPlatform(platform).withText(ShareText)
                .setCallback(umShareListener)
                .withMedia(web)
                .share();
    }

    /**
     * 友盟分享，以为分享不同内容调用方法
     * @param activity
     * @param platform
     */
    public static void uMengShare(final Activity activity, String text, SHARE_MEDIA platform) {
        final UMShareListener umShareListener = new CustomShareListener(activity);
//        if(!TextUtils.isEmpty(UserManager.getInstance().getUserKey())){
////            if(Constants.User_Type_XK!=UserManager.getInstance().getUserType() && !TextUtils.isEmpty(UserManager.getInstance().getUserKey())){
//            iamgeUrl = Constants.Url_share_register+"?referralCode="+UserManager.getInstance().getUserKey() ;
//        }else{
//            iamgeUrl = Constants.Url_share_register;
//        }
        iamgeUrl = Constants.Url_share_register;
        Logger.d("uMengShareApp",iamgeUrl);
        //我正在用【未来通付】新零售平台，消费多少返多少，分享会员、商家有奖励！凡注册为新用户，即可获赠200元超值体验金，你还不快来！
        UMImage image = new UMImage(activity, iamgeUrl);//网络图片
        UMImage thumb = new UMImage(activity, R.mipmap.app_icon);
        image.setThumb(thumb);
        UMWeb web = new UMWeb(iamgeUrl);
        web.setTitle("你敢来 我敢送");//标题
        web.setThumb(thumb);  //缩略图
//        web.setDescription("你来就送200体验金的神奇App");//描述
        web.setDescription("我正在用【首都富】新零售平台，消费多少返多少，分享会员、商家有奖励！凡注册为新用户，即可获赠200元超值体验金，你还不快来！");//描述
        new ShareAction(activity)
                .setPlatform(platform).withText("我正在用【首都富】新零售平台，消费多少返多少，分享会员、商家有奖励！凡注册为新用户，即可获赠200元超值体验金，你还不快来！")
                .setCallback(umShareListener)
                .withMedia(web)
                .share();
    }

    /**
     * 友盟分享，分享图片
     * @param activity
     * @param platform
     */
    public static void uMengShareImage(final Activity activity, Bitmap bitmap, SHARE_MEDIA platform) {
        final UMShareListener umShareListener = new CustomShareListener(activity);
        UMImage image = new UMImage(activity, bitmap);//bitmap文件
//        UMImage thumb = new UMImage(activity, R.mipmap.app_icon);
//        image.setThumb(thumb);
        new ShareAction(activity)
                .setPlatform(platform).withText("")
                .setCallback(umShareListener)
                .withMedia(image)
                .share();
    }

    /**
     * 检测app是否被安装
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppAvilible(Context context,String packageName) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }



}

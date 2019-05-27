package com.futuretongfu.bean;

import com.futuretongfu.base.BaseSerializable;

import java.util.List;

/**
 * 商家详情
 *
 * @author DoneYang 2017/6/25
 */

public class StoreDetailsBean extends BaseSerializable {

    public Base base;

    public List<SellGoods> sellGoods;//热销商品

    public class Base {

        public String userId;//用户的id

        public String userType;//角色类型：0普通会员,10创客,20创投

        public long storeId;//商家ID

        public int targetCount;//商家收藏数量

        public List<DetailImgs> detailImgs;

        public String storeName;//商品名称

        public String banner;

        public String hot;//是否热度 1 是 2 否

        public String address;//商家地址

        public String tell;//联系方式

        public double grade;//星级指数

        public int commentCount;//评价数

        public String storeInfo;//经营范围...

        public String siteUrl;//商家链接

        public int isFavorited;//1-收藏 0-未收藏

        public String logoUrl;//商家头像

        public String contacterType = "";//联系方式
        public int serviceChargeRatio;//联系方式

        public boolean isRecomUserId() {
            return isRecomUserId;
        }

        public void setRecomUserId(boolean recomUserId) {
            isRecomUserId = recomUserId;
        }

        public boolean isRecomUserId;


        public class DetailImgs {

            public String id;

            public String imageUrl;//商家介绍图片

            public String url;
        }
    }

    public class SellGoods {

        public String code;

        public String msg;

        public List<mData> data;

        public class mData {
            public String page;

            public String size;

            public String createTimeStart;

            public String createTimeEnd;

            public String updateTimeStart;

            public String updateTimeEnd;

            public int id;

            public long createTime;

            public long updateTime;

            public int goodId;

            public int storeId;

            public String name;

            public String code;

            public String adMsg;

            public int price;

            public String image;

            public String hot;

            public String putaway;

            public int commentNum;
        }
    }
}

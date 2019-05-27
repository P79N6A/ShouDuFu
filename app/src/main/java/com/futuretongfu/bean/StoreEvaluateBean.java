package com.futuretongfu.bean;

import com.futuretongfu.base.BaseSerializable;

import java.util.List;

/**
 * 商家评价
 *
 * @author DoneYang 2017/6/25
 */

public class StoreEvaluateBean extends BaseSerializable {

    public int id;//评论id

    public List<String> imgStr;//用户图片

    public long userId;//评论人id

    public int grade;//评分

    public long createTime;//时间

    public String content;//评论内容

    public String userName;//用户昵称

    public String faceUrl;



}

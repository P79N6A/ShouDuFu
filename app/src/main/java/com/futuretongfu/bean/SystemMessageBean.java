package com.futuretongfu.bean;

import com.futuretongfu.base.BaseSerializable;

import java.util.List;

/**
 * 系统消息
 *
 * @author DoneYang 2017/7/1
 */

public class SystemMessageBean extends BaseSerializable {

    public List<mList> list;

    public class mList {
        public int id;
        public String type;
        public String status;
        public String title;
        public String content;
        public long createTime;
    }
}

package com.futuretongfu.bean;

import com.futuretongfu.base.BaseSerializable;

/**
 * 二维码
 *
 * @author DoneYang 2017/6/25
 */

public class QrCodeBean extends BaseSerializable {

    public Data data;

    public class Data {

        public String qrCode;
    }

}

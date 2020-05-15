package com.guoxd.workframe.modles;

import java.io.Serializable;

/**
 * Created by guoxd on 2018/5/1.
 */

public class DeviceModle implements Serializable {
    String id;//ID
    String BianHao;//编号
    String IMEI;
    String QuanShuDanWei;
    String Onenet_dev_id;
    String JingDu;
    String WeiDu;
    String Sheng;
    String Shi;
    String Qu;
    String DianYaYuZhi;//电压
    String JiaoDuYuZhi;//角度
    String XinTiaoYuZhi;//心跳
    String LanYaMingCheng;//蓝牙名称
    int ShiFouBuFang;//是否布防

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBianHao() {
        return BianHao;
    }

    public void setBianHao(String bianHao) {
        BianHao = bianHao;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getQuanShuDanWei() {
        return QuanShuDanWei;
    }

    public void setQuanShuDanWei(String quanShuDanWei) {
        QuanShuDanWei = quanShuDanWei;
    }

    public String getOnenet_dev_id() {
        return Onenet_dev_id;
    }

    public void setOnenet_dev_id(String onenet_dev_id) {
        Onenet_dev_id = onenet_dev_id;
    }

    public String getJingDu() {
        return JingDu;
    }

    public void setJingDu(String jingDu) {
        JingDu = jingDu;
    }

    public String getWeiDu() {
        return WeiDu;
    }

    public void setWeiDu(String weiDu) {
        WeiDu = weiDu;
    }

    public String getSheng() {
        return Sheng;
    }

    public void setSheng(String sheng) {
        Sheng = sheng;
    }

    public String getShi() {
        return Shi;
    }

    public void setShi(String shi) {
        Shi = shi;
    }

    public String getQu() {
        return Qu;
    }

    public void setQu(String qu) {
        Qu = qu;
    }

    public String getDianYaYuZhi() {
        return DianYaYuZhi;
    }

    public void setDianYaYuZhi(String dianYaYuZhi) {
        DianYaYuZhi = dianYaYuZhi;
    }

    public String getJiaoDuYuZhi() {
        return JiaoDuYuZhi;
    }

    public void setJiaoDuYuZhi(String jiaoDuYuZhi) {
        JiaoDuYuZhi = jiaoDuYuZhi;
    }

    public String getXinTiaoYuZhi() {
        return XinTiaoYuZhi;
    }

    public void setXinTiaoYuZhi(String xinTiaoYuZhi) {
        XinTiaoYuZhi = xinTiaoYuZhi;
    }

    public String getLanYaMingCheng() {
        return LanYaMingCheng;
    }

    public void setLanYaMingCheng(String lanYaMingCheng) {
        LanYaMingCheng = lanYaMingCheng;
    }

    public int getShiFouBuFang() {
        return ShiFouBuFang;
    }

    public void setShiFouBuFang(int shiFouBuFang) {
        ShiFouBuFang = shiFouBuFang;
    }


    public DeviceModle(){}
    public DeviceModle(String ids, String bianHao, String imei, String quanShuDanWei, String onenet_dev_id, String jingDu, String weiDu, String sheng, String shi, String qu, String dianYaYuZhi, String jiaoDuYuZhi, String xinTiaoYuZhi, String lanYaMingCheng, int shiFouBuFang) {
        id = ids;
        BianHao = bianHao;
        IMEI = imei;
        QuanShuDanWei = quanShuDanWei;
        Onenet_dev_id = onenet_dev_id;
        JingDu = jingDu;
        WeiDu = weiDu;
        Sheng = sheng;
        Shi = shi;
        Qu = qu;
        DianYaYuZhi = dianYaYuZhi;
        JiaoDuYuZhi = jiaoDuYuZhi;
        XinTiaoYuZhi = xinTiaoYuZhi;
        LanYaMingCheng = lanYaMingCheng;
        ShiFouBuFang = shiFouBuFang;
    }
}

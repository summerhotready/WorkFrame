package com.guoxd.workframe.utils;

/**高德地图工具类
 * Created by guoxd on 2018/10/23.
 */

public class GaoDeMapUtils {
    /**城市名称兑换成简称
     * @param provinceName
     * @return
     */
    public static String cityToAbbreviation(String provinceName) {
        switch (provinceName) {
            case "北京市":
                provinceName = "京";
                break;
            case "天津市":
                provinceName = "津";
                break;
            case "上海市":
                provinceName = "沪";
                break;
            case "重庆市":
                provinceName = "渝";
                break;
            case "内蒙古自治区":
                provinceName = "蒙";
                break;
            case "新疆维吾尔自治区":
                provinceName = "新";
                break;
            case "西藏自治区":
                provinceName = "藏";
                break;
            case "宁夏回族自治区":
                provinceName = "宁";
                break;
            case "香港特别行政区":
                provinceName = "港";
                break;
            case "澳门特别行政区":
                provinceName = "澳";
                break;
            case "安徽省":
                provinceName = "皖";
                break;
            case "浙江省":
                provinceName = "浙";
                break;
            case "福建省":
                provinceName = "闽";
                break;
            case "江西省":
                provinceName = "赣";
                break;
            case "山东省":
                provinceName = "鲁";
                break;
            case "河南省":
                provinceName = "豫";
                break;
            case "湖北省":
                provinceName = "鄂";
                break;
            case "湖南省":
                provinceName = "湘";
                break;
            case "广东省":
                provinceName = "粤";
                break;
            case "海南省":
                provinceName = "琼";
                break;
            case "四川省":
                provinceName = "川";
                break;
            case "河北省":
                provinceName = "冀";
                break;
            case "贵州省":
                provinceName = "贵";
                break;
            case "吉林省":
                provinceName = "吉";
                break;
            case "山西省":
                provinceName = "晋";
                break;
            case "云南省":
                provinceName = "云";
                break;
            case "辽宁省":
                provinceName = "辽";
                break;
            case "陕西省":
                provinceName = "陕";
                break;
            case "甘肃省":
                provinceName = "甘";
                break;
            case "黑龙江省":
                provinceName = "黑";
                break;
            case "青海省":
                provinceName = "青";
                break;
            case "江苏省":
                provinceName = "苏";
                break;
            case "台湾省":
                provinceName = "台";
                break;
            default:
                provinceName = "CN";
                break;
        }
        return provinceName;
    }
}

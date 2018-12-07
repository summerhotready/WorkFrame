package com.guoxd.workframe.utils;

/**高德地图工具类
 * Created by guoxd on 2018/10/23.
 */

public class GaoDeMapUtils {
    /**城市名称兑换成简称
     * @param provinceName
     * @return
     */
    public static String cityToAbbreviationByName(String provinceName) {
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

    /**
     * 城市号码兑换简称
     * @param provinceCode
     * @return
     */
    public static String cityToAbbreviationByCode(String provinceCode) {
        String provinceName="";
        switch (provinceCode) {
            case "110000"://"北京市":
                provinceName = "京";
                break;
            case "120000"://"天津市":
                provinceName = "津";
                break;
            case "310000"://"上海市":
                provinceName = "沪";
                break;
            case "500000"://"重庆市":
                provinceName = "渝";
                break;
            case "150000"://"内蒙古自治区":
                provinceName = "蒙";
                break;
            case "650000"://"新疆维吾尔自治区":
                provinceName = "新";
                break;
            case "540000"://"西藏自治区":
                provinceName = "藏";
                break;
            case "640000"://"宁夏回族自治区":
                provinceName = "宁";
                break;
            case "810000"://"香港特别行政区":
                provinceName = "港";
                break;
            case "820000"://"澳门特别行政区":
                provinceName = "澳";
                break;
            case "340000"://"安徽省":
                provinceName = "皖";
                break;
            case "330000"://"浙江省":
                provinceName = "浙";
                break;
            case "350000"://"福建省":
                provinceName = "闽";
                break;
            case "360000"://"江西省":
                provinceName = "赣";
                break;
            case "370000"://"山东省":
                provinceName = "鲁";
                break;
            case "410000"://"河南省":
                provinceName = "豫";
                break;
            case "420000"://"湖北省":
                provinceName = "鄂";
                break;
            case "430000"://"湖南省":
                provinceName = "湘";
                break;
            case "440000"://"广东省":
                provinceName = "粤";
                break;
            case "460000"://"海南省":
                provinceName = "琼";
                break;
            case "510000"://"四川省":
                provinceName = "川";
                break;
            case "130000"://"河北省":
                provinceName = "冀";
                break;
            case "520000"://"贵州省":
                provinceName = "贵";
                break;
            case "220000"://"吉林省":
                provinceName = "吉";
                break;
            case "140000"://"山西省":
                provinceName = "晋";
                break;
            case "530000"://"云南省":
                provinceName = "云";
                break;
            case "210000"://"辽宁省":
                provinceName = "辽";
                break;
            case "610000"://"陕西省":
                provinceName = "陕";
                break;
            case "620000"://"甘肃省":
                provinceName = "甘";
                break;
            case "230000"://"黑龙江省":
                provinceName = "黑";
                break;
            case "630000"://"青海省":
                provinceName = "青";
                break;
            case "320000"://"江苏省":
                provinceName = "苏";
                break;
            case "710000"://"台湾省":
                provinceName = "台";
                break;
            default:
                provinceName = "中国";
                break;
        }
        return provinceName;
    }

}

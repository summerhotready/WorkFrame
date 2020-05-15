package com.guoxd.workframe.modles;

/**com.github.razerdp:BasePopup widge
 * Created by guoxd on 2019/1/2.
 */

public class BasePopItem {
    String key;
    String value;
    String tag;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public BasePopItem(String key, String value, String tag) {
        this.key = key;
        this.value = value;
        this.tag = tag;
    }
}

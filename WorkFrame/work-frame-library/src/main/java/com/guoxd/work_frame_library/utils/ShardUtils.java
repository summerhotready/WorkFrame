package com.guoxd.work_frame_library.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by guoxd on 2018/3/16.
 * SharedPreferences封装Utils
 */

public class ShardUtils {
    private static ShardUtils utils;
    private static SharedPreferences sharedPreferences;
    private static String mEdit;
    private ShardUtils(Context context){
        sharedPreferences = context.getSharedPreferences(mEdit,0);
    }
    public static ShardUtils init(Context context,String edit){
        if(sharedPreferences == null){
            mEdit = edit;
            utils =new ShardUtils(context);
        }
        return utils;
    }
//    字符型
    public  boolean setString(String key, String value){
        sharedPreferences.edit().putString(key,value).commit();
        return true;
    }
    public String getString(String key, String defValue){
        return sharedPreferences.getString(key,"");
    }
//    整形
    public  boolean setInt(String key, int value){
        sharedPreferences.edit().putInt(key,value).commit();
        return true;
    }
    public int getInt(String key, int defValue){
        return sharedPreferences.getInt(key,defValue);
    }
//    Long
    public  boolean setLong(String key, long value){
        sharedPreferences.edit().putLong(key,value).commit();
        return true;
    }
    public long getLong(String key, long defValue){
        return sharedPreferences.getLong(key,defValue);
    }
//  boolean
    public  boolean setBoolean(String key, boolean value){
        sharedPreferences.edit().putBoolean(key,value).commit();
        return true;
    }
    public boolean getBoolean(String key, boolean defValue){
        return sharedPreferences.getBoolean(key,defValue);
    }
//Float
    public  boolean setFloat(String key, float value){
        sharedPreferences.edit().putFloat(key,value).commit();
        return true;
    }
    public float getFloat(String key, float defValue){
        return sharedPreferences.getFloat(key,defValue);
    }
//StringSet
    public  boolean setStringSet(String key, Set<String> values){
        sharedPreferences.edit().putStringSet(key,values).commit();
        return true;
    }
    public Set<String> getStringSet(String key, Set<String> defValues){
        return sharedPreferences.getStringSet(key,defValues);
    }

    public  boolean set(String key, Object value){
        if(value instanceof String){
            return setString(key,String.valueOf(value));
        }
        if(value instanceof Integer){
            return setInt(key,Integer.valueOf(String.valueOf(value)));
        }
        if(value instanceof Long){
            return setLong(key,Long.valueOf(String.valueOf(value)));
        }
        if(value instanceof Float){
            return setFloat(key,Float.valueOf(String.valueOf(value)));
        }
        if(value instanceof Boolean){
            return setBoolean(key,Boolean.valueOf(String.valueOf(value)));
        }
        try{
            Set<String> values = (Set<String>)value;
            return setStringSet(key,values);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public Object get(String key, Object defValue){
        if(defValue instanceof String){
            return getString(key,String.valueOf(defValue));
        }
        if(defValue instanceof Integer){
            return getInt(key,Integer.valueOf(String.valueOf(defValue)));
        }
        if(defValue instanceof Long){
            return getLong(key,Long.valueOf(String.valueOf(defValue)));
        }
        if(defValue instanceof Float){
            return getFloat(key,Float.valueOf(String.valueOf(defValue)));
        }
        if(defValue instanceof Boolean){
            return getBoolean(key,Boolean.valueOf(String.valueOf(defValue)));
        }
        try{
            Set<String> values = (Set<String>)defValue;
            return getStringSet(key,values);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}

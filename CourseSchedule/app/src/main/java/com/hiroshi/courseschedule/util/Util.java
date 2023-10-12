package com.hiroshi.courseschedule.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
//保存登录信息
/**
 * 作者：${LCY+FT} on 2023/06/10
 *
 * @email: 765189866@qq.com
 *
 */
public class Util {
    public static void saveSetting(Context context, Boolean b) {
        SharedPreferences spSettingSave = context.getSharedPreferences("setting", MODE_PRIVATE);// 将需要记录的数据保存在setting.xml文件中
        SharedPreferences.Editor editor = spSettingSave.edit();
        editor.putBoolean("Boolean",b);
        editor.commit();
    }

    public static Boolean loadSetting(Context context) {
        SharedPreferences loadSettingLoad = context.getSharedPreferences("setting", MODE_PRIVATE);
        //这里是将setting.xml 中的数据读出来
        Boolean b=loadSettingLoad.getBoolean("Boolean",false);
        return b;
    }

    public static void saveTong(Context context, Boolean b) {
        SharedPreferences spSettingSave = context.getSharedPreferences("setting", MODE_PRIVATE);// 将需要记录的数据保存在setting.xml文件中
        SharedPreferences.Editor editor = spSettingSave.edit();
        editor.putBoolean("Tong",b);
        editor.commit();
    }

    public static Boolean loadTong(Context context) {
        SharedPreferences loadSettingLoad = context.getSharedPreferences("setting", MODE_PRIVATE);
        //这里是将setting.xml 中的数据读出来
        Boolean b=loadSettingLoad.getBoolean("Tong",false);
        return b;
    }
}

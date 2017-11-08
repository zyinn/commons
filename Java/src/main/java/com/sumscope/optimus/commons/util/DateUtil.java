package com.sumscope.optimus.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by simon.mao on 2016/2/22.
 * 提供了关于时间的一些工具方法
 */
public class DateUtil {

    /**
     * 某一天的最晚时间String表示法
     */
    public static final String LATEST_DATE_TIME_STRING_FORMAT = "yyyy-MM-dd 23:59:59";
    /**
     * 某一天的String表示法
     */
    public static final String GENERAL_TIME_STRING_FORMAT =  "yyyy_MM_dd";

    /**
     * 工具类使用静态方法，限制类的实例化
     */
    private DateUtil(){

    }

    /**
     * 获取当前日期
     * @return 当前日期
     */
    public static Date getCurrentDatetime(){
        Calendar today = Calendar.getInstance();
        return today.getTime();
    }

    /**
     * 根据输入的formate进行日期类型数据的字符串转换
     * @param date 日期
     * @param yyyy_mm_dd 日期format
     * @return 字符串形式的日期
     */
    public static String getFormatedDataString(Date date, String yyyy_mm_dd) {
        SimpleDateFormat formatter = new SimpleDateFormat(yyyy_mm_dd);
        return formatter.format(date);
    }


    public static String parseTimeString(String stdTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String withNoLetter = stdTime.replaceAll("[a-zA-Z+]", " ");
        try {
            Date date = simpleDateFormat.parse(withNoLetter);
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss");
            return simpleDateFormat2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getCurrentYear(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return year;
    }
}

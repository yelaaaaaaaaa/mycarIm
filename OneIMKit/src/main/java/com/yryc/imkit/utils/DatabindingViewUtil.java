package com.yryc.imkit.utils;

import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatabindingViewUtil {

    //聊天列表item未读消息数 ConversationList小于0不显示

    public static int UnreadMessagesVISIBLE(int numberOfUnreadMessages) {
        if (numberOfUnreadMessages <= 0) {
            return View.GONE;
        } else {
            return View.VISIBLE;
        }

    }


    //未读消息数大于99显示99+
    public static String NumberOfUnreadMessages(int numberOfUnreadMessages) {
        if (numberOfUnreadMessages > 99) {
            return "99+";
        } else {
            return numberOfUnreadMessages + "";
        }
    }

    public static String LongTimeToDetailedTime(Long lastWordsTime) {
        long timeStamp = System.currentTimeMillis();

        switch (sameData(stampToDate(timeStamp), stampToDate(lastWordsTime))) {
            case 0: //错误显示
                return "";
            case 1: //今天
                return stampToDateHourMinute(lastWordsTime);
            case 2://昨天
                return "昨天";
            case 3://周几
                return getWeek(stampToDate(lastWordsTime));
            case 4: //超过一周
                return stampToDateYMD(lastWordsTime);
            default:
                return "";
        }
    }

    public static int sameData(String now, String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        Date date2 = null;
        try {
            date = format.parse(now);
            date2 = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.setTime(date);//给calendar赋值
        int y1 = calendar.get(Calendar.YEAR);//获取年份
        int d1 = calendar.get(Calendar.DAY_OF_YEAR);//获取年中第几天
        calendar2.setTime(date2);
        int y2 = calendar2.get(Calendar.YEAR);
        int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
        //判断是不是同一年的同一天
        if (y1 == y2 && d1 == d2) { //今天
            return 1;
        } else if (y1 == y2 && d1 == d2 + 1) {  //昨天
            return 2;
        } else if (y1 == y2 && d1 <= d2 + 7) {//一周内
            return 3;
        } else {
            return 4;
        }
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillis);
        String a = simpleDateFormat.format(date);


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datea = new Date();
        try {
            date = dateFormat.parse(a);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return a;
    }

    /**
     * 将时间戳转换为时间yyyy-MM-dd
     */
    public static String stampToDateYMD(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    /**
     * 将时间戳转换为时间HH:mm
     */
    public static String stampToDateHourMinute(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    /**
     * 根据当前日期获得是周几
     * time=yyyy-MM-dd
     *
     * @return
     */
    public static String getWeek(String time) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int wek = c.get(Calendar.DAY_OF_WEEK);

        if (wek == 1) {
            Week += "周日";
        }
        if (wek == 2) {
            Week += "周一";
        }
        if (wek == 3) {
            Week += "周二";
        }
        if (wek == 4) {
            Week += "周三";
        }
        if (wek == 5) {
            Week += "周四";
        }
        if (wek == 6) {
            Week += "周五";
        }
        if (wek == 7) {
            Week += "周六";
        }
        return Week;
    }

}

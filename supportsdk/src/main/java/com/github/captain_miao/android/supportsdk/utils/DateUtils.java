package com.github.captain_miao.android.supportsdk.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Yanlu on 15/7/22.
 */
public class DateUtils {
    private static final String   TAG      = DateUtils.class.getSimpleName();
    private static TimeZone timeZone = TimeZone.getDefault();

    public static SimpleDateFormat getDateFormat(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(timeZone);
        return format;
    }

    /**
     * "2014-01-16 18:59:00"
     * ex. 可用于返回的时间格式的转换
     *
     * @param strTime
     * @return
     */
    public static Date parseCommonDateTime(String strTime) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            formatter.setTimeZone(timeZone);
            return formatter.parse(strTime);
        } catch (Throwable e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }


    /**
     * "2014-01-16"
     * ex. 可用于返回的时间格式的转换
     *
     * @param strTime
     * @return
     */

    public static Date parseyyyyMMdd(String strTime, TimeZone timeZone) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            formatter.setTimeZone(timeZone);
            return formatter.parse(strTime);
        } catch (Throwable e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    public static Date parseyyyyMMdd2(String strTime, TimeZone timeZone) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            formatter.setTimeZone(timeZone);
            return formatter.parse(strTime);
        } catch (Throwable e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public static Date parseToDate(String time, String rex) {
        return parseToDate(time, rex, timeZone);
    }

    public static Date parseToDate(String time, String rex, TimeZone timeZone) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(rex, Locale.getDefault());
            formatter.setTimeZone(timeZone);
            return formatter.parse(time);
        } catch (Throwable e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    /**
     * "2014-01-16 18:59:00" => "2014-01-16"
     *
     * @param strTime
     * @return
     */
    public static String tranDateTime2Date(String strTime) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            formatter.setTimeZone(timeZone);
            Date d = formatter.parse(strTime);
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            formatter2.setTimeZone(timeZone);
            return formatter2.format(d);
        } catch (Throwable e) {
            Log.e(TAG, e.toString());
            return "2014-01-01";
        }
    }

    /**
     * 2014-01-16 18:59:00 => "2014-01-16"
     *
     * @param date
     * @return
     */
    public static String tranDate2DateStr(Date date) {
        try {
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            formatter2.setTimeZone(timeZone);
            return formatter2.format(date);
        } catch (Throwable e) {
            Log.e(TAG, e.toString());
            return "2014-01-01";
        }
    }

    /**
     * 2014-01-16 18:59:00 => "2014-01"
     *
     * @param date
     * @return
     */
    public static String tranDate2DateMonthStr(Date date) {
        try {
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
            formatter2.setTimeZone(timeZone);
            return formatter2.format(date);
        } catch (Throwable e) {
            Log.e(TAG, e.toString());
            return "2015-10-10";
        }
    }
    /**
     * 2014-01-16 18:59:00 => "2014-01-16 18:59"
     *
     * @param date
     * @return
     */
    public static String tranDate2DateMinutesStr(Date date) {
        try {
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            formatter2.setTimeZone(timeZone);
            return formatter2.format(date);
        } catch (Throwable e) {
            Log.e(TAG, e.toString());
            return "2015-10-10";
        }
    }

    /**
     * 2014-01-16 18:59:00 => "2014-01-16 18:59:00"
     *
     * @param date
     * @return
     */
    public static String tranDate2DateTimeStr(Date date) {
        return tranDate2DateTimeStr(date, timeZone);
    }

    /**
     * 2014-01-16 18:59:00 => "2014-01-16 18:59:00"
     *
     * @param date
     * @return
     */
    public static String tranDate2DateTimeStr(Date date, TimeZone timeZone) {
        try {
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            formatter2.setTimeZone(timeZone);
            return formatter2.format(date);
        } catch (Throwable e) {
            Log.e(TAG, e.toString());
            return "2015-10-10";
        }
    }

    /**
     * 2014-01-16 18:59:00 => "2014-01-16 18:59:00"
     * 转换时区
     *
     * @return
     */
    public static String transDateTimeZone(String time, TimeZone t1, TimeZone t2) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            formatter.setTimeZone(t1);
            Date date = formatter.parse(time);
            formatter.setTimeZone(t2);
            return formatter.format(date);
        } catch (Throwable e) {
            Log.e(TAG, e.toString());
            return "2014-01-01";
        }
    }

    /**
     * 2014-01-16 18:59:00 => "2014-01-16 18:59:00"
     * 转换时区
     *
     * @return
     */
    public static String transDateZone(String time, TimeZone t1, TimeZone t2) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            formatter.setTimeZone(t1);
            Date date = formatter.parse(time);
            formatter.setTimeZone(t2);
            return formatter.format(date);
        } catch (Throwable e) {
            Log.e(TAG, e.toString());
            return "2014-01-01";
        }
    }


    /**
     * 2014-01-16 18:59:00 => "18:59"
     *
     * @param date
     * @return
     */
    public static String tranDate2TimeLabel(Date date) {
        try {
            SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm", Locale.getDefault());
            formatter2.setTimeZone(timeZone);
            return formatter2.format(date);
        } catch (Throwable e) {
            Log.e(TAG, e.toString());
            return "2014-01-01";
        }
    }

    public static String tranDate2TimeLabel(long second) {
        long min = second / 60;
        if(second < 0 ){
            return "00:00";
        } else if (second < 60){
            return "00:" + String.format("%02d", second);
        } else if (min < 60) {
            return String.format("%02d", min) + ":" + String.format("%02d", second % 60);
        } else if (min < 24 * 60) {
            return String.format("%02d", min / 60) + ":" + String.format("%02d", min % 60) + ":" + String.format("%02d", second % 60);
        } else {
            return String.format("%02d", min / 60 / 24) + ":" + String.format("%02d", ((min / 60) % 24)) + ":" + String.format("%02d", min % 60) + ":" + String.format("%02d", second % 60);
        }
    }
    public static String tranDate2TimePace(long second) {
        long min = second / 60;
        if(second < 0 ){
            return "00:00";
        } else if (second < 60){
            return "00:" + String.format("%02d", second);
        } else if (min < 60) {
            return String.format("%02d", min) + ":" + String.format("%02d", second % 60);
        } else {
            return "N/A";
        }
    }

    /**
     * 得到两个Date间的时间间隔 以分钟、小时等自动智能显示
     * 是 endtime - starttime + 1min
     *
     * @param startTime
     * @param endTime
     */
    public static String getTimeIntervalStr(Date startTime, Date endTime) {
        int mins = getTimeIntervalMinutes(startTime, endTime);
        if (mins < 60) {
            return mins + " 分钟";
        } else if (mins < 24 * 60) {
            return (mins / 60) + " 小时 " + (mins % 60) + " 分钟";
        } else {
            return ((mins / 60) / 24) + "天" + ((mins / 60) % 24) + "小时" + (mins % 60) + "分钟";
        }
    }

    /**
     * 得到两个Date间的时间间隔 以分钟、小时等自动智能显示 如 1’30
     * 是 endtime - starttime + 1min
     *
     * @param startTime
     * @param endTime
     */
    public static String getTimeIntervalSimpleStr(Date startTime, Date endTime) {
        int mins = getTimeIntervalMinutes(startTime, endTime);
        return getTimeIntervalSimpleStr(mins);
    }

    /**
     * 得到以分钟、小时等自动智能显示 如 1’30
     *
     * @param mins
     */
    public static String getTimeIntervalSimpleStr(int mins) {
        if (mins < 60) {
            return mins + "";
        } else if (mins < 24 * 60) {
            return (mins / 60) + "’" + (mins % 60) + "";
        } else {
            return ((mins / 60) / 24) + "d" + ((mins / 60) % 24) + "’" + (mins % 60) + "";
        }
    }

    /**
     * 是 endtime - starttime + 1min
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getTimeIntervalMinutes(Date startTime, Date endTime) {
        return (int) ((endTime.getTime() - startTime.getTime()) / 60000) + 1;
    }



    /**
     * 格式化时间值
     *
     * @param lDate
     * @return
     */
    public static String formatDateDefault(long lDate) {
        Date date = new Date(lDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "HH:mm:ss", Locale.getDefault());
//                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(date);
    }

    /**
     * 格式化时间值
     *
     * @param lDate
     * @return
     */
    public static String formatYMDDefault(long lDate) {
        Date date = new Date(lDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(date);
    }

    /**
     * <p>Checks if two date objects are on the same day ignoring time.</p>
     * <p/>
     * <p>28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true.
     * 28 Mar 2002 13:45 and 12 Mar 2002 13:45 would return false.
     * </p>
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either date is <code>null</code>
     * @since 2.1
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    /**
     * <p>Checks if two calendar objects are on the same day ignoring time.</p>
     * <p/>
     * <p>28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true.
     * 28 Mar 2002 13:45 and 12 Mar 2002 13:45 would return false.
     * </p>
     *
     * @param cal1 the first calendar, not altered, not null
     * @param cal2 the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     * @since 2.1
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }



    //获取 今天、昨天、x月x日
    /**
     * 格式化时间值
     *
     * @param lDate
     * @return
     */
    public static String formatYMForRunRecord(long lDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(lDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        return dateFormat.format(cal.getTime());
    }
    /**
     * 格式化时间值:
     *2014-01-16 18:59:00 => "1月16日  下午6:59"
     * @param lDate
     * @return
     */
    public static String formatDayForRunDetail(long lDate) {
        Date date = new Date(lDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat formatterAm = new SimpleDateFormat(" MM/dd ", Locale.getDefault());
        return formatterAm.format(date);
//        if(calendar.get(Calendar.AM_PM) == 0) {
//            SimpleDateFormat formatterAm = new SimpleDateFormat("dd日上午 ", Locale.getDefault());
//            return formatterAm.format(date);
//        } else {
//            if(calendar.get(Calendar.HOUR_OF_DAY) < 18) {
//                SimpleDateFormat formatterPm = new SimpleDateFormat("dd日下午 ", Locale.getDefault());
//                return formatterPm.format(date);
//            } else {
//                SimpleDateFormat formatterNight = new SimpleDateFormat("dd日晚上 ", Locale.getDefault());
//                return formatterNight.format(date);
//            }
//        }
    }

    /**
     * 格式化时间值:
     *2014-01-16 18:59:00 => "1月16日  下午6:59"
     * @param lDate
     * @return
     */
    public static String formatMDForTreadmillDetail(long lDate) {
        Date date = new Date(lDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat formatterAm = new SimpleDateFormat("MM/dd   HH:mm", Locale.getDefault());
        return formatterAm.format(date);
    }




    //获取星期几
    public static String getWeekDayName(long milliseconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliseconds);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
        switch (day_of_week) {
            case Calendar.MONDAY:
                return WEEK_MONDAY;
            case Calendar.TUESDAY:
                return WEEK_TUESDAY;
            case Calendar.WEDNESDAY:
                return WEEK_WEDNESDAY;
            case Calendar.THURSDAY:
                return WEEK_THURSDAY;
            case Calendar.FRIDAY:
                return WEEK_FRIDAY;
            case Calendar.SATURDAY:
                return WEEK_SATURDAY;
            case Calendar.SUNDAY:
                return WEEK_SUNDAY;
            default:
                return "";
        }
    }



    public static final long SECOND = 1000;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final String DAY_TODAY        = "今天";
    public static final String DAY_YESTERDAY    = "昨天";
    public static final String WEEK_MONDAY      = "周一";
    public static final String WEEK_TUESDAY     = "周二";
    public static final String WEEK_WEDNESDAY   = "周三";
    public static final String WEEK_THURSDAY    = "周四";
    public static final String WEEK_FRIDAY      = "周五";
    public static final String WEEK_SATURDAY    = "周六";
    public static final String WEEK_SUNDAY      = "周日";
}

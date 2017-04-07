package com.weijing.materialanimatedswitch.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 秋平 on 2015/10/9.
 */
public class DateUtils {

    private static Pattern p = null;
    private static Matcher m = null;
    /**
     * 一天的毫秒数
     */
    public static final long DAY_MILLIS = 24 * 60 * 60 * 1000;
    /**
     * 正则表达式 时间格式判断
     */
    // 2014-12-12
    private static final String strIsDate = "^((((19|20)(([02468][048])|([13579][26]))\\-02\\-29))|((20[0-9][0-9])|(19[0-9][0-9]))\\-((((0[1-9])|(1[0-2]))\\-((0[1-9])|(1\\d)|(2[0-8])))|((((0[13578])|(1[02]))\\-31)|(((01,3-9])|(1[0-2]))\\-(29|30)))))$";
    // 2014-12-12 12:12:12
    private static final String strIsDateAndTime = "^(\\d{4})\\-(\\d{2})\\-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})$";
    // 校验是否全由数字组成 20141212
    private static final String strIsDigit = "^[0-9]{1,20}$";
    // 20141212 12:12:12
    private static final String strIsDateAndTimeOtherStyle = "^(\\d{8}) (\\d{2}):(\\d{2}):(\\d{2})$";
    // 2014-12-12 121212
    private static final String strIsDateAndTimeAnotherStyle = "^(\\d{4})\\-(\\d{2})\\-(\\d{2}) [0-9]{6}$";

    // 2014-12-12 12:12:12
    private static final String strIsDateAndTimeOtherStyleFFF = "^(\\d{4})\\-(\\d{2})\\-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2}).(\\d{3})$";


    /**
     * 时间转换
     *
     * @param object 传入的String,Long的时间参数
     * @param fm     时间格式化参数, 传入你需要的格式化参数
     * @return
     * @since JDK 1.7
     */
    public static String formatDateForObject(Object object, String fm) {
        try {
            if (object instanceof Long) {
                return getDateForTimeMills((Long) object, fm);
            }
            if (object instanceof String) {
                String date = (String) object;
                // 数字
                if (isCompare(date, strIsDigit)) {
                    if (date.length() == 8) {
                        // yyyyMMdd
                        return getDataForString(date, "yyyyMMdd", fm);
                    } else if (date.length() == 14) {
                        // yyyyMMddHHmmss
                        return getDataForString(date, "yyyyMMddHHmmss", fm);
                    }
                } else if (isCompare(date, strIsDateAndTime)) {
                    // yyyy-MM-dd HH:mm:ss
                    return getDataForString(date, "yyyy-MM-dd HH:mm:ss", fm);
                } else if (isCompare(date, strIsDate)) {
                    // yyyy-MM-dd
                    return getDataForString(date, "yyyy-MM-dd", fm);
                } else if (isCompare(date, strIsDateAndTimeOtherStyle)) {
                    // yyyyMMdd HH:mm:ss
                    return getDataForString(date, "yyyyMMdd HH:mm:ss", fm);
                } else if (isCompare(date, strIsDateAndTimeAnotherStyle)) {
                    // yyyy-MM-dd HHmmss
                    return getDataForString(date, "yyyy-MM-dd HHmmss", fm);
                }else if(isCompare(date,strIsDateAndTimeOtherStyleFFF)){
                    // yyyy-MM-dd HHmmss
                    return getDataForString(date, "yyyy-MM-dd HH:mm:ss.FFF", fm);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getDateForTimeMills((new Date()).getTime(), fm);

        }

        return getDateForTimeMills((new Date()).getTime(), fm);
    }

    public static String getNetTime(String time){
        String ss= time.replace("T"," ");
        return formatDateTime(formatDateForObject(ss,"yyyy-MM-dd HH:mm"));
    }

    private static String formatDateTime(String time) {
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        if(time==null ||"".equals(time)){
            return "";
        }
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();	//今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set( Calendar.HOUR_OF_DAY, 0);
        today.set( Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();	//昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH)-1);
        yesterday.set( Calendar.HOUR_OF_DAY, 0);
        yesterday.set( Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);

        if(current.after(today)){
            return "今天 "+time.split(" ")[1];
        }else if(current.before(today) && current.after(yesterday)){

            return "昨天 "+time.split(" ")[1];
        }else{
            int index = time.indexOf("-")+1;
            return time.substring(index, time.length());
        }
    }


    /**
     * 获取时间 从Long型的时间格式中 getDateForTimeMills:
     *
     * @param timeMills
     * @param fm
     * @return
     * @author Administrator
     * @since JDK 1.7
     */
    public static String getDateForTimeMills(Long timeMills, String fm) {
        Date date = new Date(timeMills);
        String ss = new SimpleDateFormat(fm, Locale.getDefault()).format(date);
        return ss;
    }

//    public static boolean isEq(String date, LunarCalendar dat) {
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        Date date2 = new Date(dat.getTimeInMillis());
//        return sf.format(date2).equals(date);
//    }

    public static boolean isOutOfDate(String endTime) {
        long endtime = 0;
        try {
            endtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(endTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date().getTime() >= endtime;
    }

    /**
     * 从一种时间格式转换为另一种时间格式
     *
     * @param dates
     * @param fm1   第一中时间格式
     * @param fm2   第二种时间格式
     * @author Administrator
     * @since JDK 1.7
     */
    public static String getDataForString(String dates, String fm1, String fm2) {
        Date date = null;
        try {
            date = new SimpleDateFormat(fm1, Locale.getDefault()).parse(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(fm2, Locale.getDefault()).format(date);
    }

    /**
     * 正则表达式 匹配 isCompare:
     *
     * @param str
     * @param thisExpression
     */
    public static boolean isCompare(String str, String thisExpression) {
        p = Pattern.compile(thisExpression);
        m = p.matcher(str);
        boolean b = m.matches();
        return b;
    }

    public static Date getTime(String time, String fm) {
        SimpleDateFormat sdf = new SimpleDateFormat(fm, Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getTime(String startTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long getTimeMills(String date, String fm) {
        Date date2 = getTime(date, fm);
        return date2.getTime();

    }

    public static long addDay(int number) {
        return System.currentTimeMillis() + number * DAY_MILLIS;
    }

    public static long reduceDay(int number) {
        return System.currentTimeMillis() - number * DAY_MILLIS;
    }


}

package cn.com.showclear.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author YF-XIACHAOYANG
 * @date 2017/9/4 16:58
 */
public class TimeUtils {
    public static final DateFormat FMT_YMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat FMT_YMDHMS_S = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final DateFormat FMT_YMDHM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final DateFormat FMT_YMDHM_S = new SimpleDateFormat("yyyyMMddHHmm");
    public static final DateFormat FMT_YMD = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat FMT_YM = new SimpleDateFormat("yyyy-MM");
    public static final DateFormat FMT_Y = new SimpleDateFormat("yyyy");
    public static final DateFormat FMT_HMS = new SimpleDateFormat("HH:mm:ss");
    public static final DateFormat FMT_HM = new SimpleDateFormat("HH:mm");

    public TimeUtils() {
    }

    public static String currTime() {
        return currTime(FMT_YMDHMS);
    }

    public static String currTime(String dateFormatStr) {
        DateFormat format = new SimpleDateFormat(dateFormatStr);
        return currTime((DateFormat)format);
    }

    public static String currTime(DateFormat dateFormat) {
        return dateFormat.format(new Date());
    }

    public static long currTimeLong() {
        return (new Date()).getTime();
    }

    public static Date toDate(long time) {
        return new Date(time);
    }

    public static Date toDate(String timeStr) {
        return toDate(timeStr, FMT_YMDHMS);
    }

    public static Date toDate(String timeStr, String dateFormatStr) {
        DateFormat format = new SimpleDateFormat(dateFormatStr);
        return toDate(timeStr, (DateFormat)format);
    }

    public static Date toDate(String timeStr, DateFormat dateFormat) {
        try {
            return dateFormat.parse(timeStr);
        } catch (ParseException var3) {
            return null;
        }
    }

    public static String toString(Date time) {
        return toString(time, FMT_YMDHMS);
    }

    public static String toString(Date time, String dateFormatStr) {
        DateFormat format = new SimpleDateFormat(dateFormatStr);
        return toString(time, (DateFormat)format);
    }

    public static String toString(Date time, DateFormat dateFormat) {
        return time == null?"":dateFormat.format(time);
    }

    public static long beforeSecond(long secDiff) {
        return System.currentTimeMillis() / 1000L - secDiff;
    }

    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp toTimeStap(Date date) {
        return new Timestamp(date.getTime());
    }
}

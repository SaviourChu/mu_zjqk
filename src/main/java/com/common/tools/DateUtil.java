package com.common.tools;

import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtil {

  public static final String LOCALDATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


  public static String getStartStr(String date) {
    return date + " 00:00:00";
  }

  public static String getEndStr(String date) {
    return date + " 23:59:59";
  }

  public static long getStart(String date) {
    return DateUtil.getTime(date, " 00:00:00");
  }

  public static long getEnd(String date) {
    return DateUtil.getTime(date, " 23:59:59");
  }

  public static long getTime(String date, String time) {
    String datetime = date + time;
    DateTimeFormatter forPattern = DateTimeFormat.forPattern(DateUtil.LOCALDATETIME_FORMAT);
    Instant instant = Instant.parse(datetime, forPattern);
    return instant.getMillis() / 1000;
  }

  public static long getFromDate(String date) {
    Instant instant = Instant.parse(date);
    return instant.getMillis() / 1000;

  }

  public static long getNow() {
    return Instant.now().getMillis() / 1000;
  }


  /**
   * 
   * @Title
   * @Description 获取当前时间 格式为 yyyy-MM-dd
   * @author 兴岭
   * @date 2016年1月14日 下午5:01:47
   * @action getDefault
   * @return
   */
  public static String getToday() {
    return LocalDate.now().toString("yyyy-MM-dd");
  }

  public static String format(String format) {
    return LocalDateTime.now().toString(format);
  }

  /**
   * @Title
   * @Description 获取之前某一天的时间
   * @author 兴岭
   * @date 2016年1月14日 下午4:52:20
   * @action getBefore
   * @param before
   * @return
   */
  public static String getBefore(int before) {
    return LocalDate.now().minusDays(before).toString("yyyy-MM-dd");
  }

  public static String getYesterday() {
    return DateUtil.getBefore(1);
  }
}

package com.common.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.ext.kit.DateKit;
import com.jfinal.kit.StrKit;

public class MyDateKit extends DateKit {
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_FORMAT = "yyyy-MM-dd";

  private static final Logger LOG = LoggerFactory.getLogger(MyDateKit.class);

  public static String timeFormat(long time, String timeFormat) {
    SimpleDateFormat format = null;
    if (StrKit.isBlank(timeFormat)) {
      format = new SimpleDateFormat(MyDateKit.timeFormat);
    } else {
      format = new SimpleDateFormat(timeFormat);
    }
    Date date = new Date(time);
    String timeStr = format.format(date);
    return timeStr;
  }

  public static String timeFormat(long time) {
    String timeFormat = "";
    return MyDateKit.timeFormat(time, timeFormat);
  }

  public static long toLong(String dateStr) {
    Long result = 0L;
    SimpleDateFormat format = new SimpleDateFormat(timeFormat);
    try {
      Date date = format.parse(dateStr);
      result = date.getTime();
    } catch (ParseException e) {
      LOG.error(e.getMessage());
    }
    return result;
  }

  /**
   * 获取当前时间，格式为YYYY-MM-dd HH:mm:ss
   * 
   * @return
   */
  public static String now() {
    Long current = System.currentTimeMillis();
    String today = MyDateKit.timeFormat(current);
    return today;
  }

  /**
   * 
   * 获取当前时间，自定义格式
   * 
   * @return
   */
  public static String today(String format) {
    Long current = System.currentTimeMillis();
    String today = MyDateKit.timeFormat(current, format);
    return today;
  }


  /**
   * 获取当前时间秒数
   * 
   * @return
   */
  public static long currentTime() {
    long millis = System.currentTimeMillis();
    return millis / 1000;
  }

  public static String minus(int days) {
    LocalDate minusDay = LocalDate.now().minusDays(days);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String result = minusDay.format(formatter);
    return result;
  }

  public static String plus(int days) {
    LocalDate plusDay = LocalDate.now().plusDays(days);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String result = plusDay.format(formatter);
    return result;
  }

  /**
   * 邮箱验证的有效时间
   */

  public static String getActivateTime(int days, String format) {
    LocalDateTime now = LocalDateTime.now().plusDays(days);
    DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern(format);
    return now.format(ofPattern);
  }

  public static String getActivateTime(int days) {
    return MyDateKit.getActivateTime(days, DATE_TIME_FORMAT);

  }
}

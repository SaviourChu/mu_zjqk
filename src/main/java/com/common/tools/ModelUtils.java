package com.common.tools;

import java.util.Objects;

import com.google.common.base.Joiner;

public class ModelUtils {

  public static final String DATETIME_FORMAT = "%Y-%m-%d %H:%I:%s";
	public static String buildSql(String ...fields){
	  Objects.requireNonNull(fields, "查询的表字段不能为null");
		return Joiner.on(",").join(fields);
	}
	
	public static String fromUnixtime(final String field,String format){
		Objects.requireNonNull(field);
		Objects.requireNonNull(format);
		String type = "FROM_UNIXTIME(%s,'%s') as %s";
		return String.format(type, field,format,field);
	}
	
	/**
	 * 以下说明符可用在 format 字符串中：说明符说明
	 * 
		%a 工作日的缩写名称  (Sun..Sat)
		 
		%b 月份的缩写名称  (Jan..Dec)
		 
		%c 月份，数字形式(0..12)
		 
		%D 带有英语后缀的该月日期  (0th, 1st, 2nd, 3rd, ...)
		 
		%d 该月日期, 数字形式 (00..31)
		 
		%e 该月日期, 数字形式(0..31)
		 
		%f 微秒 (000000..999999)
		 
		%H 小时(00..23)
		 
		%h 小时(01..12)
		 
		%I 小时 (01..12)
		 
		%i 分钟,数字形式 (00..59)
		 
		%j 一年中的天数 (001..366)
		 
		%k 小时 (0..23)
		 
		%l 小时 (1..12)
		 
		%M 月份名称 (January..December)
		 
		%m 月份, 数字形式 (00..12)
		 
		%p 上午（AM）或下午（ PM）
		 
		%r 时间 , 12小时制 (小时hh:分钟mm:秒数ss 后加 AM或PM)
		 
		%S 秒 (00..59)
		 
		%s 秒 (00..59)
		 
		%T 时间 , 24小时制 (小时hh:分钟mm:秒数ss)
		 
		%U 周 (00..53), 其中周日为每周的第一天 
		 
		%u 周 (00..53), 其中周一为每周的第一天  
		 
		%V 周 (01..53), 其中周日为每周的第一天 ; 和 %X同时使用
		 
		%v 周 (01..53), 其中周一为每周的第一天 ; 和 %x同时使用
		 
		%W 工作日名称 (周日..周六)
		 
		%w 一周中的每日 (0=周日..6=周六)
		 
		%X 该周的年份，其中周日为每周的第一天, 数字形式,4位数;和%V同时使用
		 
		%x 该周的年份，其中周一为每周的第一天, 数字形式,4位数;和%v同时使用 
		 
		%Y 年份, 数字形式,4位数
		 
		%y 年份, 数字形式 (2位数)
		 
		%% ‘%’文字字符
	 * @param args
	 */
	public static void main(String[] args) {
		String fromUnixtime = ModelUtils.fromUnixtime("a", "%Y-%m-%d %H:%I");
		System.out.println(fromUnixtime);
	}
}

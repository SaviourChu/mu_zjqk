package com.common.kits;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class Tools {

	public static String statusConvertor(){
		return " (CASE `status` WHEN 1 THEN '未提交' WHEN 2 THEN '待审批' WHEN 3 THEN '待支付' "
			+ "WHEN 4 THEN '已支付未开票' WHEN 5 THEN '审批失败' WHEN 6 THEN '部分发票' ELSE '发票已全' END) AS `status` ";
	}

	public static String ymdStr(){
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}
	
	public static String ymdhmsStr(){
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	
	public static String reString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	public static Double reDouble(Object obj) {
		return obj == null ? 0.0 : (Double) obj;
	}

	public static Integer reInteger(Object obj) {
		return obj == null ? 0 : (Integer) obj;
	}

	/*
	 * 设置默认值
	 */
	public static Object defaultValue(Object obj) {
		if (obj instanceof String) {
			return obj == null ? "" : obj;
		} else if (obj instanceof Double) {
			return obj == null ? 0.0 : obj;
		} else if (obj instanceof Integer) {
			return obj == null ? 0 : obj;
		}
		return obj;
	}
	
	/*
	 * 对集合中的字段进行判空校验
	 */
	public static boolean checkFields(List<Object> list) {
		for (Object obj : list) {
			if (Objects.isNull(obj)) {
				return false;
			}
		}
		return true;
	}

	/*
	 * 验证金额格式
	 */
	public static boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
		return pattern.matcher(str).matches();
	}

	/*
	 * 验证日期格式
	 */
	public static boolean isDateStr(String str) {
		if (StringUtils.isBlank(str)) {
			System.out.println("字符串为空！");
		}
		boolean flag = false;
		int length = str.length();
		switch (length) {
		case 8:
			flag = isConvertDate(str, "yyyyMMdd");
			break;
		case 10:
			if(str.contains("/")){
				flag = isConvertDate(str, "yyyy/MM/dd");
			}else{
				flag = isConvertDate(str, "yyyy-MM-dd");
			}
			break;
		case 16:
			if(str.contains("/")){
				flag = isConvertDate(str, "yyyy/MM/dd HH:mm");
			}else{
				flag = isConvertDate(str, "yyyy-MM-dd HH:mm");
			}
			break;
		case 18:
			if(str.contains("/")){
				flag = isConvertDate(str, "yyyy/MM/dd HH:mm:ss");
			}else{
				flag = isConvertDate(str, "yyyy-MM-dd HH:mm:ss");
			}
			break;
		default:
			break;
		}
		return flag;
	}

	/**
	 * 验证是否能转换成日期
	 */
	private static boolean isConvertDate(String str, String dateFormatStr) {
		boolean flag = false;
		SimpleDateFormat format = new SimpleDateFormat(dateFormatStr);
		try {
			format.setLenient(false);
			format.parse(str);
			flag = true;
		} catch (ParseException e) {
			flag = false;
		}
		return flag;
	}
	
	public static int getDaysOfMonth(Date date) {  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
    }
	
	public static long getDaysByStrDateDiff(String s1, String s2){
		long days = 0l;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = sdf.parse(s1);
            Date d2 = sdf.parse(s2);
            days = (d2.getTime() - d1.getTime())/(3600L*1000*24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
	}
	
	public static int getDayOfMonth(int year,int month){  
	    Calendar c = Calendar.getInstance();  
	    c.set(year, month, 0); //输入类型为int类型  
	    return c.get(Calendar.DAY_OF_MONTH);  
	}
	
	public static String trimStr(String str){
		return StringUtils.isNoneBlank(str) ? str.trim() : "";
	}
}

package com.common.tools;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmsUtils {

	private static Integer COUNTNUM = 10000;
	
	public static String idGenerator(String entityName){
		COUNTNUM ++;
		String ymd = new SimpleDateFormat("yyyyMMdd").format(new Date());
		System.out.println(ymd);
		long timeMillis = System.currentTimeMillis();
		return entityName + timeMillis + COUNTNUM;
	}
	
	public static String operBillNo(String s) {
		String billNo = null;
		String yymm = new SimpleDateFormat("yyMM").format(new Date());
		String ym = s.substring(4, 8);
		String qz = s.substring(0, 4);
		if (!yymm.equals(ym)) {
			billNo = qz + yymm + "00001";
		} else {
			billNo = qz + yymm + new DecimalFormat("00000").format(1 + Integer.parseInt(s.substring(8, 13)));
		}
		return billNo;
	}
	
	public static void main(String[] args) {
		String s = operBillNo("PD20180700087");
		System.out.println(s);
	}
}

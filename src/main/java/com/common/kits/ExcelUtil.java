package com.common.kits;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.core.admin.model.Org;
import com.core.zjqk.model.CycBills;
import com.core.zjqk.model.CycDetails;
import com.core.zjqk.model.DispBills;
import com.core.zjqk.model.DispDetails;
import com.google.gson.Gson;

public class ExcelUtil {


	/**
	 * @author: SaviourChu
	 * @description: 导出Excel
	 * @date: 2017年9月4日 下午2:03:14
	 * @method: exportDatasExcel
	 * @return: void
	 */
	public static void exportDatasExcel(String fileName, Workbook workbook, HttpServletResponse response)
			throws IOException {
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition",
				"attachment;fileName=" + new String(fileName.getBytes(), "iso8859-1") + ".xlsx");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		BufferedInputStream bufferIn = new BufferedInputStream(new ByteArrayInputStream(out.toByteArray()));
		BufferedOutputStream bufferOut = new BufferedOutputStream(response.getOutputStream());
		byte[] buffer = new byte[1000];
		int bytesRead;
		while (-1 != (bytesRead = bufferIn.read(buffer))) {
			bufferOut.write(buffer, 0, bytesRead);
		}
		bufferOut.flush();
		bufferIn.close();
		bufferOut.close();
	}

	/**
	 * @author: SaviourChu
	 * @description: 先读取模板再填充数据
	 * @date: 2017年9月4日 下午2:03:35
	 * @method: fillDataWorkbook
	 * @return: Workbook
	 */
	@Test
	public static Workbook fillDataWorkbook(List<String> columns, List<?> objects) throws IOException {
		String fileName = "D:\\zoom\\temp.xlsx";
		Workbook workbook = null;
		if (fileName.matches("^.+\\.(?i)((xls)|(xlsx))$")) {
			boolean is03Excel = fileName.matches("^.+\\.(?i)(xls)$");
			FileInputStream inputStream = new FileInputStream(fileName);
			workbook = is03Excel ? new HSSFWorkbook(inputStream) : new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			Object object;
			Row row;
			Cell cell;
			JSONArray jsonArray = JSON.parseArray(new Gson().toJson(objects));
			for (int i = 2; i < jsonArray.size() + 2; i++) {
				object = jsonArray.getJSONObject(i - 2).get("attrs");
				JSONObject jsonObject = Objects.isNull(object) ? jsonArray.getJSONObject(i - 2) : (JSONObject) object;
				row = sheet.getRow(i);
				for (int j = 0; j < columns.size(); j++) {
					String value = jsonObject.get(columns.get(j)) == null ? ""
							: jsonObject.get(columns.get(j)).toString();
					cell = row.getCell(j);
					cell.setCellValue(value);
				}
			}
		}
		return workbook;
	}


	/**
	 * @author: SaviourChu
	 * @description: 直接构建Excel文件
	 * @date: 2017年9月4日 下午2:04:41
	 * @method: buildWorkbookCustomizable
	 * @return: Workbook
	 */
	public static Workbook buildWorkbookCustomizable(String sheetName, List<String> titles, List<String> keys,
			List<?> objects) {
		Workbook workBook = new XSSFWorkbook(); // or new HSSFWorkbook();
		Sheet sheet = workBook.createSheet(sheetName);
		Cell cell1 = sheet.createRow(0).createCell(0);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, titles.size() - 1));
		cell1.setCellStyle(createStyle(workBook, 1));
		cell1.setCellValue(sheetName);
		Row row1 = sheet.createRow(1);
		Row row2 = sheet.createRow(2);
		CellStyle titleStyle = createStyle(workBook, 2);
		CellStyle dataStyle = createStyle(workBook, 3);
		Cell cell2;
		Cell cell3;
		for (int t = 0; t < titles.size(); t++) {
			cell2 = row1.createCell(t);
			cell2.setCellValue(titles.get(t));
			cell2.setCellStyle(titleStyle);
			cell3 = row2.createCell(t);
			cell3.setCellStyle(titleStyle);
		}
		JSONArray jsonArray = JSON.parseArray(new Gson().toJson(objects));
		for (int r = 2; r < jsonArray.size() + 2; r++) {
			JSONObject jsonObject = jsonArray.getJSONObject(r - 2);
			if (Objects.nonNull(jsonObject.get("attrs"))) {
				jsonObject = (JSONObject) jsonObject.get("attrs");
			}
			row1 = sheet.createRow(r);
			for (int c = 0; c < keys.size(); c++) {
				Object object = jsonObject.get(keys.get(c));
				String str = object == null ? "" : object.toString();
				cell2 = row1.createCell(c);
				cell2.setCellStyle(dataStyle);
				if (Tools.isNumber(str)) {
					BigDecimal x = new BigDecimal("1000000000");
					BigDecimal y = new BigDecimal(str);
					if(y.compareTo(x)==1){
						cell2.setCellValue(str);
					}else{
						cell2.setCellValue(Double.valueOf(str));
					}
				} else {
					if(StringUtils.isNotBlank(str) && str.charAt(0) == '-'){
						cell2.setCellValue(Double.valueOf(str));
					}else{
						cell2.setCellValue(str);
					}
				}
			}
		}
		for (int i = 0; i < titles.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		return workBook;
	}
	
	/**
	 * @author: SaviourChu
	 * @description: 创建样式
	 * @date: 2017年9月4日 下午2:05:21
	 * @method: createStyle
	 * @return: CellStyle
	 */
	public static CellStyle createStyle(Workbook workBook, int i) {
		Font font = workBook.createFont();
		CellStyle style = workBook.createCellStyle();
		if (i == 1) {
			font.setFontHeightInPoints((short) 12);
		} else if (i == 2) {
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setFontHeightInPoints((short) 12);
			// style.setFillForegroundColor(HSSFColor.SEA_GREEN.index);
			// style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		} else {
			font.setFontHeightInPoints((short) 10);
		}
		font.setFontName("宋体");
		style.setFont(font);
		style.setAlignment(CellStyle.VERTICAL_CENTER);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		return style;
	}

	public static float getExcelCellAutoHeight(String str, float fontCountInline) {
		float defaultRowHeight = 12.00f;// 每一行的高度指定
		float defaultCount = 0.00f;
		for (int i = 0; i < str.length(); i++) {
			float ff = getRegex(str.substring(i, i + 1));
			defaultCount = defaultCount + ff;
		}
		return ((int) (defaultCount / fontCountInline) + 1) * defaultRowHeight;// 计算
	}

	public static float getRegex(String charStr) {
		if (charStr == " ") {
			return 0.5f;
		}
		// 判断是否为字母或字符
		if (Pattern.compile("^[A-Za-z0-9]+$").matcher(charStr).matches()) {
			return 0.5f;
		}
		// 判断是否为全角
		if (Pattern.compile("[\u4e00-\u9fa5]+$").matcher(charStr).matches()) {
			return 1.00f;
		}
		// 全角符号 及中文
		if (Pattern.compile("[^x00-xff]").matcher(charStr).matches()) {
			return 1.00f;
		}
		return 0.5f;
	}
	
	@SuppressWarnings("unused")
	@Test
	public static Workbook cycBillInfo2(Object o1, List<CycDetails> ls, String no, String signDate) throws IOException {
		InputStream inputStream = ExcelUtil.class.getClassLoader().getResourceAsStream("cycBill.xlsx");
		Workbook workbook = null;
		try {
			workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			
			CycBills cb = (CycBills) o1;
			
			sheet.getRow(2).getCell(3).setCellValue("日期：" + signDate);
			sheet.getRow(2).getCell(4).setCellValue("单号：" + cb.get("billNo"));

			sheet.getRow(4).getCell(1).setCellValue(Tools.reString(cb.get("brand")));
			sheet.getRow(5).getCell(1).setCellValue(Tools.reString(cb.get("sName")));
			sheet.getRow(6).getCell(1).setCellValue(Tools.reString(cb.get("contractSubject")));
			sheet.getRow(7).getCell(1).setCellValue(Tools.reString(cb.get("berthNo")));

			sheet.getRow(4).getCell(4).setCellValue(Tools.reString(cb.get("payDate")));
			sheet.getRow(5).getCell(4).setCellValue(Tools.reString(cb.get("aName")));
			sheet.getRow(6).getCell(4).setCellValue(Tools.reString(cb.get("account")));
			sheet.getRow(7).getCell(4).setCellValue(Tools.reString(cb.get("bank")));
			
			int i;
			for(i=11; i < ls.size()+11; i++){
				
				CycDetails cd = (CycDetails)ls.get(i-11);
				Row row = sheet.getRow(i);
				
				row.getCell(0).setCellValue(i-10);
				
				row.getCell(1).setCellValue(Tools.reString(cd.get("ciName")));
				
				row.getCell(2).setCellValue(cd.get("startTime") + "~" + cd.get("endTime"));
				
				row.getCell(3).setCellValue(Tools.reDouble(cd.get("money")));
				
				row.getCell(4).setCellValue(Tools.reString(cd.get("remarks")));
				
				Cell cell213 = sheet.getRow(21).getCell(3);
				cell213.setCellValue(Tools.reDouble(cd.get("totalMoney")));
			}
			String zdr = Tools.reString(cb.get("zdr"));
			sheet.getRow(25).getCell(1).setCellValue(Org.dao.getOrgName(zdr));
			sheet.getRow(26).getCell(1).setCellValue(zdr);
			sheet.getRow(27).getCell(1).setCellValue(Tools.reString(cb.get("spr")));
			Cell cell271 = sheet.getRow(28).getCell(1);

			sheet.getRow(26).getCell(4).setCellValue(Tools.reString(cb.get("createTime")));
			sheet.getRow(27).getCell(4).setCellValue(Tools.reString(cb.get("sprq")));
			Cell cell274 = sheet.getRow(28).getCell(4);
			
			CellStyle cellStyle1 = workbook.createCellStyle();
			
			cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle1.setAlignment(CellStyle.VERTICAL_CENTER);
			cellStyle1.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
			
			cellStyle1.setBorderTop(CellStyle.BORDER_THIN);
			cellStyle1.setBorderBottom(CellStyle.BORDER_THIN);
			cellStyle1.setBorderLeft(CellStyle.BORDER_THIN);
			cellStyle1.setBorderRight(CellStyle.BORDER_THIN);
			cellStyle1.setWrapText(true);// 设置自动换行
			
			CellStyle cellStyle2 = workbook.createCellStyle();
			cellStyle2.setAlignment(CellStyle.ALIGN_LEFT);
			cellStyle2.setBorderTop(CellStyle.BORDER_THIN);
			cellStyle2.setBorderBottom(CellStyle.BORDER_THIN);
			cellStyle2.setBorderLeft(CellStyle.BORDER_THIN);
			cellStyle2.setBorderRight(CellStyle.BORDER_THIN);
			cellStyle2.setWrapText(true);// 设置自动换行
			
			String s = cb.get("contractSubject");
			sheet.autoSizeColumn(s.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}
	
	@SuppressWarnings("unused")
	@Test
	public static Workbook cycBillInfo(Object o1, List<CycDetails> ls, String no, String signDate) throws IOException {
		InputStream inputStream = ExcelUtil.class.getClassLoader().getResourceAsStream("cycBill2.xlsx");
		Workbook workbook = null;
		try {
			workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			
			CycBills cb = (CycBills) o1;
			
			sheet.getRow(2).getCell(3).setCellValue("日期：" + signDate);
			sheet.getRow(2).getCell(4).setCellValue("单号：" + cb.get("billNo"));
			
			sheet.getRow(4).getCell(1).setCellValue(Tools.reString(cb.get("brand")));
			sheet.getRow(5).getCell(1).setCellValue(Tools.reString(cb.get("sName")));
			sheet.getRow(6).getCell(1).setCellValue(Tools.reString(cb.get("contractSubject")));
			sheet.getRow(7).getCell(1).setCellValue(Tools.reString(cb.get("berthNo")));
			
			sheet.getRow(4).getCell(4).setCellValue(Tools.reString(cb.get("payDate")));
			sheet.getRow(5).getCell(4).setCellValue(Tools.reString(cb.get("aName")));
			sheet.getRow(6).getCell(4).setCellValue(Tools.reString(cb.get("account")));
			sheet.getRow(7).getCell(4).setCellValue(Tools.reString(cb.get("bank")));
			
			int n = ls.size();
			if(n != 0){
				CycDetails cd = null;
				for(int i=11; i < n+11; i++){
					cd = (CycDetails)ls.get(i-11);
					Row row = sheet.getRow(i);
					row.getCell(0).setCellValue(i-10);
					row.getCell(1).setCellValue(Tools.reString(cd.get("ciName")));
					row.getCell(2).setCellValue(cd.get("startTime") + "~" + cd.get("endTime"));
					row.getCell(3).setCellValue(Tools.reDouble(cd.get("money")));
					row.getCell(4).setCellValue(Tools.reString(cd.get("remarks")));
				}
				sheet.getRow(29).getCell(3).setCellValue(Tools.reDouble(cd.get("totalMoney")));
			}else{
				sheet.getRow(29).getCell(3).setCellValue("无");
			}
			
			String zdr = Tools.reString(cb.get("zdr"));
			sheet.getRow(31).getCell(1).setCellValue(Org.dao.getOrgName(zdr));
			sheet.getRow(32).getCell(1).setCellValue(zdr);
			sheet.getRow(33).getCell(1).setCellValue(Tools.reString(cb.get("spr")));
			Cell cell341 = sheet.getRow(34).getCell(1);
			
			sheet.getRow(32).getCell(4).setCellValue(Tools.reString(cb.get("createTime")));
			sheet.getRow(33).getCell(4).setCellValue(Tools.reString(cb.get("sprq")));
			Cell cell344 = sheet.getRow(34).getCell(4);
			
			CellStyle cellStyle1 = workbook.createCellStyle();
			
			cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle1.setAlignment(CellStyle.VERTICAL_CENTER);
			cellStyle1.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
			
			cellStyle1.setBorderTop(CellStyle.BORDER_THIN);
			cellStyle1.setBorderBottom(CellStyle.BORDER_THIN);
			cellStyle1.setBorderLeft(CellStyle.BORDER_THIN);
			cellStyle1.setBorderRight(CellStyle.BORDER_THIN);
			cellStyle1.setWrapText(true);// 设置自动换行
			
			CellStyle cellStyle2 = workbook.createCellStyle();
			cellStyle2.setAlignment(CellStyle.ALIGN_LEFT);
			cellStyle2.setBorderTop(CellStyle.BORDER_THIN);
			cellStyle2.setBorderBottom(CellStyle.BORDER_THIN);
			cellStyle2.setBorderLeft(CellStyle.BORDER_THIN);
			cellStyle2.setBorderRight(CellStyle.BORDER_THIN);
			cellStyle2.setWrapText(true);// 设置自动换行
			
			String s = cb.get("contractSubject");
			sheet.autoSizeColumn(s.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}
	
	@SuppressWarnings("unused")
	@Test
	public static Workbook dispBillInfo2(Object o1, List<DispDetails> ls, String no, String signDate) throws IOException {
		InputStream inputStream = ExcelUtil.class.getClassLoader().getResourceAsStream("dispBill.xlsx");
		Workbook workbook = null;
		try {
			workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			DispBills db = (DispBills) o1;
			
			sheet.getRow(2).getCell(3).setCellValue("日期：" + signDate);
			sheet.getRow(2).getCell(4).setCellValue("单号：" + db.get("billNo"));
			
			sheet.getRow(4).getCell(1).setCellValue(Tools.reString(db.get("brand")));
			sheet.getRow(5).getCell(1).setCellValue(Tools.reString(db.get("sName")));
			sheet.getRow(6).getCell(1).setCellValue(Tools.reString(db.get("contractSubject")));
			sheet.getRow(7).getCell(1).setCellValue(Tools.reString(db.get("berthNo")));
			
			sheet.getRow(4).getCell(4).setCellValue(Tools.reString(db.get("payDate")));
			sheet.getRow(5).getCell(4).setCellValue(Tools.reString(db.get("aName")));
			sheet.getRow(6).getCell(4).setCellValue(Tools.reString(db.get("account")));
			sheet.getRow(7).getCell(4).setCellValue(Tools.reString(db.get("bank")));
			
			int i;
			for(i=11; i < ls.size()+11; i++){
				DispDetails dd = (DispDetails)ls.get(i-11);
				sheet.getRow(i).getCell(0).setCellValue(i-10);
				sheet.getRow(i).getCell(1).setCellValue(Tools.reString(dd.get("diName")));
				sheet.getRow(i).getCell(2).setCellValue(Tools.reDouble(dd.get("money")));
				sheet.getRow(i).getCell(3).setCellValue(Tools.reString(dd.get("remarks")));
				
				sheet.getRow(21).getCell(2).setCellValue(Tools.reDouble(dd.get("totalMoney")));
			}
			String zdr = Tools.reString(db.get("zdr"));
			sheet.getRow(25).getCell(1).setCellValue(Org.dao.getOrgName(zdr));
			sheet.getRow(26).getCell(1).setCellValue(zdr);
			sheet.getRow(27).getCell(1).setCellValue(Tools.reString(db.get("spr")));
			Cell cell271 = sheet.getRow(28).getCell(1);
			
			sheet.getRow(26).getCell(4).setCellValue(Tools.reString(db.get("createTime")));
			sheet.getRow(27).getCell(4).setCellValue(Tools.reString(db.get("zdrq")));
			Cell cell274 = sheet.getRow(28).getCell(4);
			
			CellStyle cellStyle1 = workbook.createCellStyle();
			
			cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle1.setAlignment(CellStyle.VERTICAL_CENTER);
			cellStyle1.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
			
			cellStyle1.setBorderTop(CellStyle.BORDER_THIN);
			cellStyle1.setBorderBottom(CellStyle.BORDER_THIN);
			cellStyle1.setBorderLeft(CellStyle.BORDER_THIN);
			cellStyle1.setBorderRight(CellStyle.BORDER_THIN);
			cellStyle1.setWrapText(true);// 设置自动换行
			
			CellStyle cellStyle2 = workbook.createCellStyle();
			cellStyle2.setAlignment(CellStyle.ALIGN_LEFT);
			cellStyle2.setBorderTop(CellStyle.BORDER_THIN);
			cellStyle2.setBorderBottom(CellStyle.BORDER_THIN);
			cellStyle2.setBorderLeft(CellStyle.BORDER_THIN);
			cellStyle2.setBorderRight(CellStyle.BORDER_THIN);
			cellStyle2.setWrapText(true);// 设置自动换行
			
			String s = db.get("contractSubject");
			sheet.autoSizeColumn(s.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}
	
	@SuppressWarnings("unused")
	@Test
	public static Workbook dispBillInfo(Object o1, List<DispDetails> ls, String no, String signDate) throws IOException {
		InputStream inputStream = ExcelUtil.class.getClassLoader().getResourceAsStream("dispBill2.xlsx");
		Workbook workbook = null;
		try {
			workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			DispBills db = (DispBills) o1;
			
			sheet.getRow(2).getCell(3).setCellValue("日期：" + signDate);
			sheet.getRow(2).getCell(4).setCellValue("单号：" + db.get("billNo"));
			
			sheet.getRow(4).getCell(1).setCellValue(Tools.reString(db.get("brand")));
			sheet.getRow(5).getCell(1).setCellValue(Tools.reString(db.get("sName")));
			sheet.getRow(6).getCell(1).setCellValue(Tools.reString(db.get("contractSubject")));
			sheet.getRow(7).getCell(1).setCellValue(Tools.reString(db.get("berthNo")));
			
			sheet.getRow(4).getCell(4).setCellValue(Tools.reString(db.get("payDate")));
			sheet.getRow(5).getCell(4).setCellValue(Tools.reString(db.get("aName")));
			sheet.getRow(6).getCell(4).setCellValue(Tools.reString(db.get("account")));
			sheet.getRow(7).getCell(4).setCellValue(Tools.reString(db.get("bank")));
			
			int n = ls.size();
			if(n != 0){
				DispDetails dd = null;
				for(int i=11; i < n+11; i++){
					dd = (DispDetails)ls.get(i-11);
					sheet.getRow(i).getCell(0).setCellValue(i-10);
					sheet.getRow(i).getCell(1).setCellValue(Tools.reString(dd.get("diName")));
					sheet.getRow(i).getCell(2).setCellValue(Tools.reDouble(dd.get("money")));
					sheet.getRow(i).getCell(3).setCellValue(Tools.reString(dd.get("remarks")));
				}
				sheet.getRow(29).getCell(2).setCellValue(Tools.reDouble(dd.get("totalMoney")));
			}else{
				sheet.getRow(29).getCell(2).setCellValue("无");
			}
			
			String zdr = Tools.reString(db.get("zdr"));
			sheet.getRow(31).getCell(1).setCellValue(Org.dao.getOrgName(zdr));
			sheet.getRow(32).getCell(1).setCellValue(zdr);
			sheet.getRow(33).getCell(1).setCellValue(Tools.reString(db.get("spr")));
			Cell cell341 = sheet.getRow(34).getCell(1);
			
			sheet.getRow(32).getCell(4).setCellValue(Tools.reString(db.get("createTime")));
			sheet.getRow(33).getCell(4).setCellValue(Tools.reString(db.get("zdrq")));
			Cell cell344 = sheet.getRow(34).getCell(4);
			
			CellStyle cellStyle1 = workbook.createCellStyle();
			
			cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle1.setAlignment(CellStyle.VERTICAL_CENTER);
			cellStyle1.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
			
			cellStyle1.setBorderTop(CellStyle.BORDER_THIN);
			cellStyle1.setBorderBottom(CellStyle.BORDER_THIN);
			cellStyle1.setBorderLeft(CellStyle.BORDER_THIN);
			cellStyle1.setBorderRight(CellStyle.BORDER_THIN);
			cellStyle1.setWrapText(true);// 设置自动换行
			
			CellStyle cellStyle2 = workbook.createCellStyle();
			cellStyle2.setAlignment(CellStyle.ALIGN_LEFT);
			cellStyle2.setBorderTop(CellStyle.BORDER_THIN);
			cellStyle2.setBorderBottom(CellStyle.BORDER_THIN);
			cellStyle2.setBorderLeft(CellStyle.BORDER_THIN);
			cellStyle2.setBorderRight(CellStyle.BORDER_THIN);
			cellStyle2.setWrapText(true);// 设置自动换行
			
			String s = db.get("contractSubject");
			sheet.autoSizeColumn(s.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}

}

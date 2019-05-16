package com.core.zjqk.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.common.config.JdbcUtils;
import com.common.kits.ReturnMsg;
import com.common.kits.Tools;
import com.core.admin.shiro.ShiroUtils;
import com.core.zjqk.model.base.BaseCycItems;
import com.google.common.collect.Lists;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class CycItems extends BaseCycItems<CycItems> {
	public static final CycItems dao = new CycItems();
	
	public Page<CycItems> page(Controller c){
		String ciName = c.getPara("ciName");
		String select = "SELECT id,`name` AS ciName ";
		StringBuilder sqlExceptSelect = new StringBuilder();
		List<Object> params = Lists.newArrayList();
		sqlExceptSelect.append("FROM cyc_items WHERE del_flag=1 ");
		if(StringUtils.isNotBlank(ciName)){
			sqlExceptSelect.append("AND INSTR(`name`, ?) ");
			params.add(ciName);
	    }
		sqlExceptSelect.append("ORDER BY id ");
		return this.paginate(c.getParaToInt("page"), c.getParaToInt("rows"), select, sqlExceptSelect.toString(), params.toArray());
	}
	
	public List<CycItems> list(){
		return this.find("SELECT `name` AS ciName,id AS ciId FROM cyc_items WHERE del_flag=1 ");
	}
	
	public List<CycItems> listByStore(){
		return this.find("SELECT `name` AS ciName,id AS ciId FROM cyc_items WHERE id>0 GROUP BY `name` ");
	}
	
	public List<CycItems> getCiNames(Controller c){
		return this.find("SELECT cd.id,CONCAT(cd.start_time,'至',cd.end_time,'__',ci.`name`,'__',cd.money) `name` FROM cyc_items ci "
				+ "LEFT JOIN cyc_details cd ON cd.c_id=ci.id "
				+ "WHERE cd.type='" + ShiroUtils.getClassify() + "' AND cd.flag=1 AND cd.bill_no=? ", c.getPara("billNo"));
	}
	
	public ReturnMsg saveOrUpdate(Controller c){
		Connection conn = JdbcUtils.getConnection();
		PreparedStatement psmt = null;
		boolean result = false;
		try {
			String id = c.getPara("id");
			String ciName = c.getPara("ciName");
			if(StringUtils.isAnyBlank(ciName)){
				return ReturnMsg.DATAERROR;
			}
			ciName = Tools.trimStr(ciName);
			Long count = this.findFirst("SELECT COUNT(1) AS count FROM cyc_items WHERE name=? AND del_flag=1 ", ciName).getLong("count");
			if(StringUtils.isBlank(id) && count > 0){
				return ReturnMsg.REPEATERROR;
			}else{
				CycItems item = new CycItems();
				item.set("name", ciName);
				if(StringUtils.isNoneBlank(id)){
					result = item.set("id", id).update();
				}else{
					result = item.save();
					List<Store> list = Store.dao.list();
					for (Store store : list) {
						Integer sId = (Integer)store.get("sId");
						Integer ciId = this.findCycItemsId(ciName);
						psmt = conn.prepareStatement("INSERT INTO cyc_initfees (s_id,c_id) VALUES(?,?) ");
						psmt.setInt(1, sId);
						psmt.setInt(2, ciId);
						psmt.executeUpdate();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JdbcUtils.closeAll(null, null, psmt, conn);
		}
		return result ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
	}
	
	public ReturnMsg deletes(String ids){
		Integer orgId = ShiroUtils.getOrgId();
		if(orgId == 1){
			boolean update = false;
			for (String id : ids.split(";")) {
				update = new CycItems().set("id", id).set("del_flag", 2).update();
				if(update) break;
			}
			return update ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
		}else{
			return ReturnMsg.NORIGHT;
		}
	}
	
	/**
	 * 根据费用项目名称查询对应的ID
	 */
	public Integer findCycItemsId(String name){
		CycItems ci = this.findFirst("SELECT id FROM cyc_items WHERE `name`=? AND del_flag=1", name);
		return ci != null ? ci.get("id") : null;
	}
	
	public ReturnMsg importCycItems(Controller c) {
		Workbook workbook = null;
		InputStream is = null;
		boolean save = false;
		try {
			is = new FileInputStream(c.getFile().getFile());
			workbook = new XSSFWorkbook(is);
			Sheet sheet = workbook.getSheetAt(0);
			Row row = null;
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				if(row != null){
					CycItems ci = new CycItems();
					Cell cell0 = row.getCell(0);
					if(Objects.nonNull(cell0.toString())){
						ci.set("name", cell0.toString().trim());
					}else{
						continue;
					}
					if(cell0 != null){
						Long count = this.findFirst("SELECT COUNT(1) AS count FROM cyc_items WHERE del_flag=1 AND name=? ", cell0.toString()).getLong("count");
						if (count > 0) {
							continue;
						} else {
							save = ci.save();
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return save ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
	}
	
}

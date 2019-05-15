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
import com.core.admin.shiro.ShiroUtils;
import com.core.zjqk.model.base.BaseCycItems;
import com.google.common.collect.Lists;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class CycInitfees extends BaseCycItems<CycInitfees> {
	
	public static final CycInitfees dao = new CycInitfees();

	public Page<CycInitfees> page(Controller c){
		String ciName = c.getPara("ciName");
		String sName = c.getPara("sName");
		String select = "SELECT cif.id,s.id AS sId,ci.id AS ciId,s.`name` AS sName,ci.`name` AS ciName,cif.init_fee AS initFee ";
		StringBuilder sqlExceptSelect = new StringBuilder();
		List<Object> params = Lists.newArrayList();
		String classify = ShiroUtils.getClassify();
		sqlExceptSelect.append("FROM cyc_initfees AS cif "
				+ "LEFT JOIN store AS s ON s.id=cif.s_id "
				+ "LEFT JOIN cyc_items AS ci ON ci.id=cif.c_id "
				+ "WHERE s.flag=1 AND ci.del_flag=1 AND cif.del_flag=1 AND s.classify='"+classify+"' ");
		if(StringUtils.isNotBlank(ciName)){
			sqlExceptSelect.append("AND INSTR(ci.`name`, ?) ");
			params.add(ciName);
	    }
		if(StringUtils.isNotBlank(sName)){
			sqlExceptSelect.append("AND INSTR(s.`name`, ?) ");
			params.add(sName);
		}
		sqlExceptSelect.append("ORDER BY s.id,ci.id ");
		return this.paginate(c.getParaToInt("page"), c.getParaToInt("rows"), select, sqlExceptSelect.toString(), params.toArray());
	}
	
	public ReturnMsg saveOrUpdate(Controller c){
		boolean result = false;
		String id = c.getPara("id");
		String ciName = c.getPara("ciName");
		String sName = c.getPara("sName");
		String initFee = c.getPara("initFee","0");
		if(StringUtils.isAnyBlank(ciName, sName)){
			return ReturnMsg.DATAERROR;
		}
		Integer sId = Store.dao.findStoreId(sName);
		Integer dId = CycItems.dao.findCycItemsId(ciName);
		Long count = this.findFirst("SELECT COUNT(1) AS count FROM cyc_initfees WHERE s_id=? AND c_id=? AND del_flag=1 ", sId, dId).getLong("count");
		if(StringUtils.isBlank(id) && count > 0){
			return ReturnMsg.REPEATERROR;
		}else{
			CycInitfees cif = new CycInitfees();
			cif.set("c_id", dId).set("s_id", sId).set("init_fee", initFee);
			result = StringUtils.isNoneBlank(id) ? cif.set("id", id).update() : cif.save();
		}
		return result ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
	}
	
	public ReturnMsg saveOrUpdateCIF(Controller c){
		Connection conn = JdbcUtils.getConnection();
		PreparedStatement psmt1 = null;
		PreparedStatement psmt2 = null;
		String ciIds = c.getPara("ciIds");
		Integer sId = Store.dao.findStoreId(c.getPara("sName"));
		try {
			for (String str : ciIds.split("@")) {
				if(str.lastIndexOf("=") != str.length()-1){
					String[] items = str.split("=");
					Integer dId = Integer.valueOf(items[0]);
					String initFee = items[1];
					Long count = this.findFirst("SELECT COUNT(1) AS count FROM cyc_initfees WHERE s_id=? AND c_id=? AND del_flag=1 ", sId, dId).getLong("count");
					if(count>0){
						psmt1 = conn.prepareStatement("UPDATE cyc_initfees SET init_fee=?,del_flag=1 WHERE s_id=? AND c_id=? ");
						psmt1.setString(1, initFee);
						psmt1.setInt(2, sId);
						psmt1.setInt(3, dId);
						psmt1.executeUpdate();
					}else{
						psmt1 = conn.prepareStatement("INSERT INTO cyc_initfees (s_id,c_id,init_fee) VALUES(?,?,?) ");
						psmt1.setInt(1, sId);
						psmt1.setInt(2, dId);
						psmt1.setString(3, initFee);
						psmt1.executeUpdate();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JdbcUtils.closeAll(null, null, psmt1, null);
			JdbcUtils.closeAll(null, null, psmt2, conn);
			return ReturnMsg.ERROR;
		}
		return ReturnMsg.SUCCESS;
	}
	
	public ReturnMsg deletes(String ids){
		Integer orgId = ShiroUtils.getOrgId();
		if(orgId == 1){
			boolean update = false;
			for (String id : ids.split(";")) {
				update = new CycInitfees().set("id", id).set("del_flag", 2).update();
				if(update == false){
					break;
				}
			}
			return update ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
		}else{
			return ReturnMsg.NORIGHT;
		}
	}
	
	public ReturnMsg importCycInitfees(Controller c) {
		boolean result = false;
		Workbook workbook = null;
		InputStream is = null;
		try {
			is = new FileInputStream(c.getFile().getFile());
			workbook = new XSSFWorkbook(is);
			Sheet sheet = workbook.getSheetAt(0);
			Row row = null;
			String[] fields = {"c_id","s_id","init_fee"};
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				if(row != null){
					CycInitfees cif = new CycInitfees();
					Cell cell0 = row.getCell(0);
					if(Objects.nonNull(cell0)){
						Integer cId = CycItems.dao.findCycItemsId(cell0.toString().trim());
						cif.set(fields[0], cId);
					}else{
						continue;
					}
					Cell cell1 = row.getCell(1);
					if(Objects.nonNull(cell1)){
						Integer sId = Store.dao.findStoreId(cell1.toString().trim());
						if(sId == null){
							continue;
						}
						cif.set(fields[1], sId);
					}else{
						continue;
					}
					Cell cell2 = row.getCell(2);
					if(Objects.nonNull(cell2)){
						cif.set(fields[2], cell2.toString());
					}else{
						continue;
					}
					result = cif.save();
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
		return result ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
	}
}

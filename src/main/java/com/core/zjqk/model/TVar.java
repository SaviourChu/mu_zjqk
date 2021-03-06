package com.core.zjqk.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.common.config.JdbcUtils;
import com.common.tools.EmsUtils;
import com.core.admin.shiro.ShiroUtils;
import com.core.zjqk.model.base.BaseTVar;
import com.jfinal.core.Controller;

@SuppressWarnings("serial")
public class TVar extends BaseTVar<TVar> {
	
	public static final TVar dao = new TVar();
	
	public String findBillNo(Controller c){
		Integer type = c.getParaToInt("type");
		String sql = "SELECT bill_no AS billNo FROM t_var WHERE type=? AND classify='"+ShiroUtils.getClassify()+"' ";
		String billNo = this.findFirst(sql, type).get("billNo");
		return EmsUtils.operBillNo(billNo);
	}
	
	public void saveBillNo(int type, String billNo){
		Connection conn = JdbcUtils.getConnection();
		PreparedStatement psmt = null;
		try {
			psmt = conn.prepareStatement("UPDATE t_var SET bill_no=? WHERE type=? AND classify='"+ShiroUtils.getClassify()+"' ");
			psmt.setString(1, billNo);
			psmt.setInt(2, type);
			psmt.executeUpdate();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("更新单号失败---TVar表");
		} finally {
			JdbcUtils.closeAll(null, null, psmt, conn);
		}
	}
	
	public List<TVar> classifys(){
		return this.find("SELECT DISTINCT(classify) FROM t_var ");
	}
	
}

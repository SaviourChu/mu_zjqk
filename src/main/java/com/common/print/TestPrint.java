package com.common.print;

import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestPrint {

	// 595×842
	static String dh; // 单号
	static String lxr; // 联系人
	static String tel;
	static String date;
	static String khm; // 客户名
	static String add; // 地址
	static String js = "现金"; // 结算方式
	static List<Object> kh = new ArrayList<Object>(); // 客户信息list
	static List<Object> sp = new ArrayList<Object>(); // 主体list
	static List<Object> hj = new ArrayList<Object>(); // 尾部List

	public static void setkhls(List<Object> kh) {
		TestPrint.kh = kh;
		dh = kh.get(0).toString().trim();
		khm = kh.get(1).toString().trim();
		lxr = kh.get(2).toString().trim();
		tel = kh.get(3).toString().trim();
		add = kh.get(4).toString().trim();
		Date d = new Date();
		date = String.format("%tF", d);
	}

	public static void setsp(List<Object> sp) {
		TestPrint.sp = sp;
	}

	public static void sethj(List<Object> hj) {
		TestPrint.hj = hj;
	}

	public TestPrint() {
		JFrame jf = new JFrame();
		jf.setSize(595, 842);
		Container c = jf.getContentPane();
		c.add(new Draw());
		
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		
		
		//printFrameAction(jf);
	}

	public static void main(String[] args) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add("张三");
		list.add("24");
		list.add("汉族");
		list.add("本科");
		list.add("IT");
		list.add("安徽");
		list.add("男");
		setkhls(list);
		sethj(list);
		setsp(list);
		
		new TestPrint();

	}

	class Draw extends JPanel {
		
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {
			super.paint(g);
			Font font, font2, font3;
			font = new Font("黑体", Font.PLAIN, 20);
			Graphics2D g2 = (Graphics2D) g;
			g2.setFont(font);
			g2.drawString("项目请款单", 220, 35);// 标题
			font2 = new Font("宋体", Font.PLAIN, 10);
			g2.setFont(font2);
			g2.drawString("日期：2018-02-03   单号：PE20180200001", 340, 50);
			font3 = new Font("宋体", Font.PLAIN, 10);
			g2.setFont(font3);
			g2.drawString("品    牌:", 20, 65);
			g2.drawString("申请支付日期:", 340, 65);
			g2.drawString("店铺名称:", 20, 85);
			g2.drawString("收款方名称:", 340, 85);
			g2.drawString("合同抬头:", 20, 105);
			g2.drawString("收款方账号:", 340, 105);
			g2.drawString("铺位号:", 20, 125);
			g2.drawString("开户行名称:", 340, 125);
			
			g2.drawString("恋暖初茶", 65, 65);
			g2.drawString("2018-01-25", 405, 65);
			g2.drawString("上海静安嘉里", 65, 85);
			g2.drawString("上海吉祥房地产有限公司", 405, 85);
			g2.drawString("上海利强餐饮管理有限公司", 65, 105);
			g2.drawString("1001281219006611171", 405, 105);
			g2.drawString("SB1-25A室", 65, 125);
			g2.drawString("工行上海市浦东开发区支行", 405, 125);
			List<String> ls = new ArrayList<String>();
			ls.add("序号");
			ls.add("付款项目");
			ls.add("所属期间");
			ls.add("请款金额");
			ls.add("备注");
			List<P> ps = new ArrayList<>();
			P p1 = new P();
			p1.setXh(1);
			p1.setFkxm("测试1");
			p1.setSsqj("2017-08-29~2018-01-22");
			p1.setQkje(56602.80);
			p1.setBz("备注信息1");
			P p2 = new P();
			p2.setXh(1);
			p2.setFkxm("测试1");
			p2.setSsqj("2017-08-29~2018-01-22");
			p2.setQkje(56602.80);
			p2.setBz("备注信息1");
			ps.add(p1);
			ps.add(p2);
			List<String> temp = new ArrayList<>();
			int n[] = new int[] { 0, 4, 6, 8, 6, 10 };
			int s = 0;
			int x = 20;
			int y = 130;
			int row = 0;
			int count = 0;
			for (int i = 0; i < ps.size() / 5 + 3; i++) { // 画横线
				y = 130 + row * 18;
				if (i == 1) {
					for (int j = 0; j < ls.size(); j++) {
						s = n[j] * 14;
						x = x + s;
						g2.drawString(ls.get(j), x + 3, y - 4); // 写入表头数据
					}
				}else{
					for(int j = 1; j > 1 && j < ps.size(); j++){
						P p = ps.get(j);
						Integer xh = p.getXh();
						String fkxm = p.getFkxm();
						String ssqj = p.getSsqj();
						Double qkje = p.getQkje();
						String bz = p.getBz();
						temp.add(xh.toString());
						temp.add(fkxm);
						temp.add(ssqj);
						temp.add(qkje.toString());
						temp.add(bz);
						for (int k = 0; k < 5; k++) {
							s = n[k] * 14;
							x = x + s;
							System.out.println(k + "    " + x);
							g2.drawString(temp.get(k), x + 3, y - 4); // 写入当行数据
						}
						count++;
					}
				}
				
				/*if (i == ps.size() / 5 + 2) {
					g2.drawString(hj.get(0).toString().trim(), 51, y - 4);
					g2.drawString(hj.get(1).toString().trim(), 387, y - 4);
					g2.drawString(hj.get(2).toString().trim(), 429, y - 4);
				}*/
				g2.drawLine(20, y, 538, y);// 横线
				row++;
			}
			s = 0;
			x = 20;
			for(int i = 1; i > 1 && i < ps.size() / 5 + 2; i++){
				P p = ps.get(i);
				Integer xh = p.getXh();
				String fkxm = p.getFkxm();
				String ssqj = p.getSsqj();
				Double qkje = p.getQkje();
				String bz = p.getBz();
				temp.add(xh.toString());
				temp.add(fkxm);
				temp.add(ssqj);
				temp.add(qkje.toString());
				temp.add(bz);
				for (int j = 0; j < 5; j++) {
					s = n[j] * 14;
					x = x + s;
					System.out.println(j + "    " + x);
					g2.drawString(temp.get(j), x + 3, y - 4); // 写入当行数据
				}
				count++;
			}
			count = 0;
			row = 0;
			s = 0;
			x = 20;
			for (int i = 0; i < ps.size() + 1; i++) { // 画竖线
				s = n[i] * 14;
				x = x + s;
				if (i < 2 || i > 5) {
					g2.drawLine(x, 130, x, y);// 竖线
				} else {
					g2.drawLine(x, 130, x, y - 18);// 竖线
				}
				if (i == 0) {
					g2.drawString("合计", x + 3, y - 4);
				}
			}
		}
	}

	/*
	 * 打印指定的窗体及其包含的组件
	 */
	public void printFrameAction(Frame f){
		Toolkit kit = Toolkit.getDefaultToolkit(); // 获取工具箱
		Properties props = new Properties(); 
		props.put("awt.print.printer", "durango");// 设置打印属性
		props.put("awt.print.numCopies", "2"); 
		if(kit != null){
	       // 获取工具箱自带的打印对象
		   PrintJob printJob = kit.getPrintJob(f, "Print Frame", props); 
		   if(printJob != null) {
			   Graphics pg = printJob.getGraphics();// 获取打印对象的图形环境
			   if(pg != null){
			       try{
			            f.printAll(pg);//打印该窗体及其所有的组件
				   } finally{
				        pg.dispose();//注销图形环境
			       }
			   }
			   printJob.end();//结束打印作业
		   }
	   }
	}
}

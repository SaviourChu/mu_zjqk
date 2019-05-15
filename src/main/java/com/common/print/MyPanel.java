package com.common.print;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MyPanel extends JPanel {

	public void paint(Graphics g) { // paint函数需重写
		Graphics2D g2 = (Graphics2D) g; // Graphics2D比Graphics更常用
		Font font, font2, font3;
		font = new Font("黑体", Font.PLAIN, 20);
		g2.setFont(font);
		g2.setFont(font);
		g2.drawString("*", 190, 35);// title
		font2 = new Font("宋体", Font.PLAIN, 10);
		g2.setFont(font2);
		g2.drawString("地址:  电话:  Fax:  ", 100, 50);
		font3 = new Font("宋体", Font.PLAIN, 10);
		g2.setFont(font3);
		g2.drawString("单号:", 20, 65);
		g2.drawString("日期:", 240, 65);
		g2.drawString("结算:", 420, 65);
		g2.drawString("客户:", 20, 85);
		g2.drawString("联系人:", 240, 85);
		g2.drawString("联系电话:", 420, 85);
		g2.drawString("地址:", 20, 105);
		g2.drawString("10001", 50, 65);
		g2.drawString("2017-12-09", 270, 65);
		g2.drawString("68909.00", 445, 65);
		g2.drawString("测试用户", 50, 85);
		g2.drawString("zhangSan", 280, 85);
		g2.drawString("18155779893", 470, 85);
		g2.drawString("漕宝路440号", 50, 105);
		List<String> ls = new ArrayList<String>();
		ls.add("序号");
		ls.add("商品种类");
		ls.add("商品名称");
		ls.add("单位");
		ls.add("折扣");
		ls.add("单价");
		ls.add("数量");
		ls.add("金额");
		ls.add("备注");
		int n[] = new int[] { 0, 2, 4, 10, 3, 3, 4, 3, 4, 4 };
		int s = 0;
		int x = 20;
		int y = 115;
		int row = 0;
		int count = 0;
		List<Object> lsx = new ArrayList<Object>();
		lsx = null;
		System.out.println(lsx.size());
		for (int i = 0; i < lsx.size() / 9 + 3; i++) { // 画横线
			y = 115 + row * 18;
			if (i == 1) {
				for (int j = 0; j < ls.size(); j++) {
					s = n[j] * 14;
					x = x + s;
					g2.drawString(ls.get(j), x + 3, y - 4); // 写入表头数据
				}
			}
			s = 0;
			x = 20;
			if (i > 1 && i < lsx.size() / 9 + 2) {
				for (int j = 0; j < 9; j++) {
					s = n[j] * 14;
					x = x + s;
					System.out.println(j + "    " + x);
					g2.drawString(lsx.get(j + count * 9).toString().trim(), x + 3, y - 4); // 写入当行数据
				}
				count++;
			}
			List<Double> heji = new ArrayList<>();
			heji.add(8989.00);
			heji.add(67262.00);
			heji.add(13799.00);
			if (i == lsx.size() / 9 + 2) {
				g2.drawString(heji.get(0).toString().trim(), 51, y - 4);
				g2.drawString(heji.get(1).toString().trim(), 387, y - 4);
				g2.drawString(heji.get(2).toString().trim(), 429, y - 4);
			}
			g2.drawLine(20, y, 538, y);// 横线
			row++;
		}
		count = 0;
		row = 0;
		s = 0;
		x = 20;
		for (int i = 0; i < ls.size() + 1; i++) { // 画竖线
			s = n[i] * 14;
			x = x + s;
			if (i < 2 || i > 5) {
				g2.drawLine(x, 115, x, y);// 竖线
			} else {
				g2.drawLine(x, 115, x, y - 18);// 竖线
			}
			if (i == 0) {
				g2.drawString("合计", x + 3, y - 4);
			}
		}
		System.out.println(y);
		g2.drawString("开单人:                                   经手人:                          收货人:", 20, y + 20);
		g2.drawString("白色:存根联         红色:记账联      蓝色:收款联         黄色:收货联", 20, y + 40);
	}

}

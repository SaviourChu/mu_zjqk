package com.common.print;

import java.awt.print.PrinterException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Demo extends JFrame {
	// rowData存放数据
	// columnName存放列名
	private Vector rowData, columnName;
	private JTable jt = null;
	private JScrollPane jsp = null;

	public static void main(String[] args) {
		Demo demo1 = new Demo();
	}

	// 构造函数
	public Demo() {
		columnName = new Vector();
		// 设置列名
		columnName.add("学号");
		columnName.add("名字");
		columnName.add("性别");
		columnName.add("年龄");
		columnName.add("籍贯");
		columnName.add("爱好");

		rowData = new Vector();
		// rowData可存放多行
		Vector line1 = new Vector();
		line1.add("D001");
		line1.add("囚牛");
		line1.add("公");
		line1.add("1500");
		line1.add("东海");
		line1.add("音乐");

		Vector line2 = new Vector();
		line2.add("D002");
		line2.add("睚眦");
		line2.add("公");
		line2.add("1300");
		line2.add("东海");
		line2.add("惩恶扬善");

		Vector line3 = new Vector();
		line3.add("D003");
		line3.add("嘲风");
		line3.add("公");
		line3.add("1200");
		line3.add("东海");
		line3.add("登高");

		rowData.add(line1);
		rowData.add(line2);
		rowData.add(line3);

		rowData.add(line1);
		rowData.add(line2);
		rowData.add(line3);
		rowData.add(line1);
		rowData.add(line2);
		rowData.add(line3);

		// 初始化JTable
		jt = new JTable(rowData, columnName);

		jsp = new JScrollPane(jt);

		this.add(jsp);

		this.setTitle("你好啊！");
		this.setSize(600, 300);
		this.setLocation(300, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		try {
			jt.print();
		} catch (PrinterException e) {
			e.printStackTrace();
		}
	}
}

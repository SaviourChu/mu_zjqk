package com.common.print;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class Test1 implements Printable {

	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		// 输出内容
		String str = "爱情里并没有谁对谁错！";
		Component c = null;
		// 转换Graphics2D
		Graphics2D graphics2d = (Graphics2D) graphics;
		// 设置打印属性
		graphics2d.setBackground(Color.blue);
		graphics2d.setColor(Color.black);
		// 打印起点坐标
		double x = pageFormat.getImageableX();
		double y = pageFormat.getImageableY();
		if (pageIndex == 0) {
			// 设置打印字体（字体名称、样式和点大小）（字体名称可以是物理或者逻辑名称）
			// Java平台所定义的五种字体系列：Serif、SansSerif、Monospaced、Dialog 和 DialogInput
			Font font = new Font("宋体", Font.PLAIN, 12);
			// 将字体样式set到 graphics2d 中
			graphics2d.setFont(font);
			// 设置打印线的属性
			float[] dash = { 2.0f };
			graphics2d.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash, 0.0f)); // 字体高度
			float heigth = font.getSize2D();
			// -1- 用Graphics2D直接输出
			// 首字符的基线(右下部)位于用户空间中的 (x, y) 位置处
			Image src = Toolkit.getDefaultToolkit().getImage("F:\\All Office Converter Platinum\\Image\\Information.jpg");
			graphics2d.drawImage(src, (int) x, (int) y, c);
			int img_Height = src.getHeight(c);
			int img_width = src.getWidth(c);
			graphics2d.drawString(str, (float) x, (float) y + 1 * heigth + img_Height);
			graphics2d.drawLine((int) x, (int) (y + 1 * heigth + img_Height + 10), (int) x + 200, (int) (y + 1 * heigth + img_Height + 10));
			graphics2d.drawImage(src, (int) x, (int) (y + 1 * heigth + img_Height + 11), c);
			return PAGE_EXISTS;
		}
		return NO_SUCH_PAGE;
	}

	public static void main(String[] args) {
		// 创建一个工作文档 book
		Book book = new Book();
		// 设置打印方式 竖打
		PageFormat format = new PageFormat();
		format.setOrientation(PageFormat.PORTRAIT);
		// 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符
		Paper paper = new Paper();
		// 纸张大小
		paper.setSize(590, 840);
		// A4(595 X 842)设置打印区域，其实0，0应该是72，72，因为A4纸的默认X,Y边距是72
		paper.setImageableArea(10, 10, 590, 840);
		format.setPaper(paper);
		// 将PageFormat和 Printable 添加到书中，组成一个页面
		book.append(new Test1(), format);
		// 获取打印服务对象
		PrinterJob job = PrinterJob.getPrinterJob();
		// 设置打印类
		job.setPageable(book);
		// 可以用printDialog显示打印对话框，在用户确认后打印；也可以直接打印
		try {
			job.print();
		} catch (PrinterException e) {
			e.printStackTrace();
		}
	}

}

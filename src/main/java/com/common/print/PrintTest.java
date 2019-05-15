package com.common.print;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class PrintTest extends JFrame implements ActionListener, Printable {
	private JButton printTextButton = new JButton("Print Text");
	private JButton previewButton = new JButton("Print Preview");
	private JButton printText2Button = new JButton("Print Text2");
	private JButton printFileButton = new JButton("Print File");
	private JButton printFrameButton = new JButton("Print Frame");
	private JButton exitButton = new JButton("Exit");
	private JLabel tipLabel = new JLabel("");
	private JTextArea area = new JTextArea();
	private JScrollPane scroll = new JScrollPane(area);
	private JPanel buttonPanel = new JPanel();

	private int PAGES = 0;
	private String printStr;

	public PrintTest() {
		this.setTitle("Print Test");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds((int) ((SystemProperties.SCREEN_WIDTH - 800) / 2),
				(int) ((SystemProperties.SCREEN_HEIGHT - 600) / 2), 800, 600);
		initLayout();
	}

	private void initLayout() {
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(scroll, BorderLayout.CENTER);
		printTextButton.setMnemonic('P');
		printTextButton.addActionListener(this);
		buttonPanel.add(printTextButton);
		previewButton.setMnemonic('v');
		previewButton.addActionListener(this);
		buttonPanel.add(previewButton);
		printText2Button.setMnemonic('e');
		printText2Button.addActionListener(this);
		buttonPanel.add(printText2Button);
		printFileButton.setMnemonic('i');
		printFileButton.addActionListener(this);
		buttonPanel.add(printFileButton);
		printFrameButton.setMnemonic('F');
		printFrameButton.addActionListener(this);
		buttonPanel.add(printFrameButton);
		exitButton.setMnemonic('x');
		exitButton.addActionListener(this);
		buttonPanel.add(exitButton);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent evt) {
		Object src = evt.getSource();
		if (src == printTextButton)
			printTextAction();
		else if (src == previewButton)
			previewAction();
		else if (src == printText2Button)
			printText2Action();
		else if (src == printFileButton)
			printFileAction();
		else if (src == printFrameButton)
			printFrameAction();
		else if (src == exitButton)
			exitApp();
	}

	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(Color.black);
		if (page >= PAGES){
			return Printable.NO_SUCH_PAGE;
		}else{
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
			g2.drawString("11", 50, 65);
			g2.drawString("11", 270, 65);
			g2.drawString("11", 445, 65);
			g2.drawString("11", 50, 85);
			g2.drawString("11", 280, 85);
			g2.drawString("11", 470, 85);
			g2.drawString("11", 50, 105);
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
						g2.drawString(lsx.get(j + count * 9).toString().trim(), x + 3,
								y - 4); // 写入当行数据
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
			g2.drawString(
					"开单人:                                   经手人:                          收货人:",
					20, y + 20);
			g2.drawString("白色:存根联         红色:记账联      蓝色:收款联         黄色:收货联", 20, y + 40);
		}
		/*g2.translate(pf.getImageableX(), pf.getImageableY());
		drawCurrentPageText(g2, pf, page);*/
		return Printable.PAGE_EXISTS;
	}

	private void drawCurrentPageText(Graphics2D g2, PageFormat pf, int page) {
		Font f = area.getFont();
		String s = getDrawText(printStr)[page];
		String drawText;
		float ascent = 16;
		int k, i = f.getSize(), lines = 0;
		while (s.length() > 0 && lines < 54) {
			k = s.indexOf('\n');
			if (k != -1) {
				lines += 1;
				drawText = s.substring(0, k);
				g2.drawString(drawText, 0, ascent);
				if (s.substring(k + 1).length() > 0) {
					s = s.substring(k + 1);
					ascent += i;
				}
			} else {
				lines += 1;
				drawText = s;
				g2.drawString(drawText, 0, ascent);
				s = "";
			}
		}
	}

	public String[] getDrawText(String s) {
		String[] drawText = new String[PAGES];
		for (int i = 0; i < PAGES; i++)
			drawText[i] = "";

		int k, suffix = 0, lines = 0;
		while (s.length() > 0) {
			if (lines < 54) {
				k = s.indexOf('\n');
				if (k != -1) {
					lines += 1;
					drawText[suffix] = drawText[suffix] + s.substring(0, k + 1);
					if (s.substring(k + 1).length() > 0)
						s = s.substring(k + 1);
				} else {
					lines += 1;
					drawText[suffix] = drawText[suffix] + s;
					s = "";
				}
			} else {
				lines = 0;
				suffix++;
			}
		}
		return drawText;
	}

	public int getPagesCount(String curStr) {
		int page = 0;
		int position, count = 0;
		String str = curStr;
		while (str.length() > 0) {
			position = str.indexOf('\n');
			count += 1;
			if (position != -1)
				str = str.substring(position + 1);
			else
				str = "";
		}

		if (count > 0)
			page = count / 54 + 1;

		return page;
	}

	private void printTextAction() {
		printStr = area.getText().trim();
		if (printStr != null && printStr.length() > 0) {
			PAGES = getPagesCount(printStr);
			PrinterJob myPrtJob = PrinterJob.getPrinterJob();
			PageFormat pageFormat = myPrtJob.defaultPage();
			myPrtJob.setPrintable(this, pageFormat);
			if (myPrtJob.printDialog()) {
				try {
					myPrtJob.print();
				} catch (PrinterException pe) {
					pe.printStackTrace();
				}
			}
		} else {
			JOptionPane.showConfirmDialog(null, "Sorry, Printer Job is Empty, Print Cancelled!", "Empty",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
		}
	}

	private void previewAction() {
		printStr = area.getText().trim();
		PAGES = getPagesCount(printStr);
		(new PrintPreviewDialog(this, "Print Preview", true, this, printStr)).setVisible(true);
	}

	private void printText2Action() {
		printStr = area.getText().trim();
		if (printStr != null && printStr.length() > 0) {
			PAGES = getPagesCount(printStr);
			DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
			PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
			DocPrintJob job = printService.createPrintJob();
			PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
			DocAttributeSet das = new HashDocAttributeSet();
			Doc doc = new SimpleDoc(this, flavor, das);

			try {
				job.print(doc, pras);
			} catch (PrintException pe) {
				pe.printStackTrace();
			}
		} else {
			JOptionPane.showConfirmDialog(null, "Sorry, Printer Job is Empty, Print Cancelled!", "Empty",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
		}
	}

	private void printFileAction() {
		JFileChooser fileChooser = new JFileChooser(SystemProperties.USER_DIR);
		//fileChooser.setFileFilter(new com.szallcom.file.JavaFilter());
		int state = fileChooser.showOpenDialog(this);
		if (state == fileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
			PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
			PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
			PrintService service = ServiceUI.printDialog(null, 200, 200, printService, defaultService, flavor, pras);
			if (service != null) {
				try {
					DocPrintJob job = service.createPrintJob();
					FileInputStream fis = new FileInputStream(file);
					DocAttributeSet das = new HashDocAttributeSet();
					Doc doc = new SimpleDoc(fis, flavor, das);
					job.print(doc, pras);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void printFrameAction() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Properties props = new Properties();
		props.put("awt.print.printer", "durango");
		props.put("awt.print.numCopies", "2");
		if (kit != null) {
			PrintJob printJob = kit.getPrintJob(this, "Print Frame", props);
			if (printJob != null) {
				Graphics pg = printJob.getGraphics();
				if (pg != null) {
					try {
						this.printAll(pg);
					} finally {
						pg.dispose();
					}
				}
				printJob.end();
			}
		}
	}

	private void exitApp() {
		this.setVisible(false);
		this.dispose();
		System.exit(0);
	}

	public static void main(String[] args) {
		(new PrintTest()).setVisible(true);
	}
}

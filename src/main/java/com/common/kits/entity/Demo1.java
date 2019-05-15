package com.common.kits.entity;

import javax.print.DocFlavor;
import javax.print.DocFlavor.URL;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

public class Demo1 {

	public static void main(String[] args) {

		// 第一步:定位打印服务(打印机)

		// 定义打印机支持的文档类型
		DocFlavor fr = DocFlavor.URL.GIF;
		/*
		 * 创建DocFlavor实例需要两个参数（String）， 一个是表示MIME类型，一个是表示类的名称
		 */
		// 很多情况下，不一定要创建DocFlavor实例
		PrintService[] allps = PrintServiceLookup.lookupPrintServices(null, null);
		// 检索所有的可用打印机服务
		for (int i = 0; i < allps.length; i++) {
			System.out.println(allps[i].getName());
		}
		// 通常情况下只要获取默认的打印服务就ok了
		PrintService defaultps = PrintServiceLookup.lookupDefaultPrintService();
		System.out.println(defaultps.getName());
		// 定义属性(javax.print.attribute包)
		PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
		// 这里的属性不能为null,否则,IIlegalArgumentException
		PrintService select = ServiceUI.printDialog(null, 100, 100, allps, defaultps, null, attrs);

		// 第二步:创建打印任务

		DocPrintJob job = defaultps.createPrintJob();// ?这里的services是调用哪个对象,需要进一步验证
		// 第三步:获取打印数据
		try {
			URL url = new URL("http://blog.csdn.net/yangxt/article/details/1747982");
			DocFlavor flavor = DocFlavor.URL.GIF;
			SimpleDoc doc = new SimpleDoc(url, flavor, null);

			// 第四步:初始化打印
			// javax.print.attribute.standard包中有很多有用的实现!
			// attrs.add(new Copies(2));
			attrs.add(OrientationRequested.LANDSCAPE);
			job.print(doc, attrs);

			// 监控打印任务(可选)
			job.addPrintJobListener(new PrintJobAdapter() {
				public void printDataTransferCompleted(PrintJobEvent event) {
					System.out.println("数据传输成功!!");
				}

				public void printJobNoMoreEvents(PrintJobEvent event) {
					System.out.println("该接口中没有更多的方法可以让打印机调用!!");
				}

				public void printJobCanceled(PrintJobEvent event) {
					System.out.println("取消打印服务!!");
				}

				public void printJobCompleted(PrintJobEvent event) {
					System.out.println("打印任务完成!!");
				}

				public void printJobFailed(PrintJobEvent event) {
					System.out.println("打印任务失败!!");
				}

				public void printJobRequiresAttention(PrintJobEvent event) {
					System.out.println("可以恢复的错误,如打印机缺纸!!");
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

package com.common.tools;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TemplateUtil {

	private static final Logger LOG = LoggerFactory.getLogger(TemplateUtil.class);

	public static String getHtmlFrom(ServletContext servletContext,String template, Map<String, Object> dataModel) {
		Writer out = null;
		String result = "";
		try {
			Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);
			cfg.setServletContextForTemplateLoading(servletContext, "template");
			Template t = cfg.getTemplate(template,"utf-8");
			out = new StringWriter();
			t.process(dataModel, out);
			result = out.toString();
		} catch (IOException e) {
			LOG.error(e.getMessage());
		} catch (TemplateException e) {
			LOG.error(e.getMessage());
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	
}

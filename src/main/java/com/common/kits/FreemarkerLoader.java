package com.common.kits;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.tools.MyException;
import com.jfinal.kit.PathKit;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerLoader {

  private static final Logger LOG = LoggerFactory.getLogger(FreemarkerLoader.class);

  private static final String TEMPLATEPATH = "template/";
  /**
   * 模板引擎配置
   */
  private Configuration configuration;
  /**
   * 参数
   */
  private Map<String, Object> parameters = new HashMap<>();

  private String file = "";

  public FreemarkerLoader(String file) {
    this(FreemarkerLoader.TEMPLATEPATH, file);
  }

  public FreemarkerLoader(String templatePath, String file) {
    this.file = file;
    configuration = new Configuration(Configuration.VERSION_2_3_23);
    /**
     * 模板加载位置
     */
    List<TemplateLoader> loaders = new ArrayList<>();
    try {
      Enumeration<URL> resources =
          FreemarkerLoader.class.getClassLoader().getResources(templatePath);
      URL resource = null;
      while (resources.hasMoreElements()) {
        resource = resources.nextElement();
        loaders.add(new FileTemplateLoader(new File(resource.getFile())));
      }
      File webDir = new File(PathKit.getWebRootPath() + File.separator + templatePath);
      if (webDir.exists()) {
        loaders.add(new FileTemplateLoader(webDir));
      };
      TemplateLoader[] templateLoaders = new TemplateLoader[loaders.size()];
      configuration.setTemplateLoader(new MultiTemplateLoader(loaders.toArray(templateLoaders)));
    } catch (IOException e) {
      LOG.error(e.getMessage());
      throw new IllegalArgumentException(e);
    }
    configuration.setEncoding(Locale.getDefault(), "UTF-8");
    configuration.setDateFormat("yyyy-MM-dd HH:mm:ss");
  }

  public FreemarkerLoader setEncoding(String encoding) {
    configuration.setEncoding(Locale.getDefault(), encoding);
    return this;
  }


  public FreemarkerLoader setDateFormat(String datefmt) {
    configuration.setDateFormat(datefmt);
    return this;
  }


  public FreemarkerLoader setValue(String attr, Object value) {
    parameters.put(attr, value);
    return this;
  }

  public FreemarkerLoader setValue(Map<String, Object> parameters) {
    this.parameters.putAll(parameters);
    return this;
  }

  public String getHtml() {
    try {
      Template template = configuration.getTemplate(file);
      StringWriter stringWriter = new StringWriter();
      template.process(parameters, stringWriter);
      return stringWriter.toString();
    } catch (IOException | TemplateException e) {
      LOG.error("getHtml", e.getMessage());
      throw new MyException(e);
    }
  }
}

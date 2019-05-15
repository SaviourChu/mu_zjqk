package com.common.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.tools.MyException;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.weixin.sdk.kit.IpKit;

public class IPInterceptor implements Interceptor {

  private static final Logger LOG = LoggerFactory.getLogger(IPInterceptor.class);

  public void intercept(Invocation vocationin) {
    HttpServletRequest request = vocationin.getController().getRequest();
    String address = IpKit.getRealIp(request);
    Long start = System.currentTimeMillis();
    LOG.warn("ip===" + address);
    try {
      vocationin.invoke();
    } catch (Exception e) {
      e.printStackTrace();
      throw new MyException();
    }

    Long end = System.currentTimeMillis();
    LOG.warn(vocationin.getActionKey() + "方法调用时间：" + (end - start));
  }

}

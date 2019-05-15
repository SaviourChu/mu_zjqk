package com.common.tools;

import com.jfinal.log.ILogFactory;
import com.jfinal.log.Log;


public class Slf4jLogFactory implements ILogFactory {

  public Log getLogger(Class<?> clazz) {
    return new Slf4jLogger(clazz);
  }

  @Override
  public Log getLog(Class<?> clazz) {
    return new Slf4jLogger(clazz);
  }

  @Override
  public Log getLog(String name) {
    return new Slf4jLogger(name);
  }
}

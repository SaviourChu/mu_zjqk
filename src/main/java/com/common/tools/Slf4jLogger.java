package com.common.tools;

import org.slf4j.LoggerFactory;

import com.jfinal.log.Log;

public class Slf4jLogger extends Log {
  private org.slf4j.Logger log;

  Slf4jLogger(Class<?> clazz) {
    this.log = LoggerFactory.getLogger(clazz);
  }

  Slf4jLogger(String name) {
    this.log = LoggerFactory.getLogger(name);
  }

  public void info(String message) {
    this.log.info(message);
  }

  public void info(String message, Throwable t) {
    this.log.info(message, t);
  }

  public void debug(String message) {
    this.log.debug(message);
  }

  public void debug(String message, Throwable t) {
    this.log.debug(message, t);
  }

  public void warn(String message) {
    this.log.warn(message);
  }

  public void warn(String message, Throwable t) {
    this.log.warn(message, t);
  }

  public void error(String message) {
    this.log.error(message);
  }

  public void error(String message, Throwable t) {
    this.log.error(message, t);
  }

  public void fatal(String message) {
    this.log.error(message);
  }

  public void fatal(String message, Throwable t) {
    this.log.error(message, t);
  }

  public boolean isDebugEnabled() {
    return this.log.isDebugEnabled();
  }

  public boolean isInfoEnabled() {
    return this.log.isInfoEnabled();
  }

  public boolean isWarnEnabled() {
    return this.log.isWarnEnabled();
  }

  public boolean isErrorEnabled() {
    return this.log.isErrorEnabled();
  }

  public boolean isFatalEnabled() {
    return this.log.isErrorEnabled();
  }
}

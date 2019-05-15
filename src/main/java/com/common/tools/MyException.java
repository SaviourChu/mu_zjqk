package com.common.tools;

public class MyException extends RuntimeException {

  private static final long serialVersionUID = -2594151435678157419L;

  public MyException() {
    super();
  }

  public MyException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }

  public MyException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public MyException(String arg0) {
    super(arg0);
  }

  public MyException(Throwable arg0) {
    super(arg0);
  }
}

package com.jfinal.ext.render.exception;

import com.jfinal.render.Render;

public abstract class ExceptionRender extends Render {
	
    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public ExceptionRender setException(Exception exception) {
        this.exception = exception;
        return this;
    }

}

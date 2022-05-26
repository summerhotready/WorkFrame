package com.guoxd.workframe.viewinject;
//基于afinal
public class AfinalException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public AfinalException() {
		super();
	}
	
	public AfinalException(String msg) {
		super(msg);
	}
	
	public AfinalException(Throwable ex) {
		super(ex);
	}
	
	public AfinalException(String msg, Throwable ex) {
		super(msg,ex);
	}

}

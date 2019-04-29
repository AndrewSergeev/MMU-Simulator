package com.hit.exception;

import java.io.IOException;
import java.io.Serializable;

public class HardDiskException extends IOException implements Serializable {	
	private static final long serialVersionUID = -6327773420350070699L;
	
	public static enum ActionError 
	{
		PAGE_FAULT,
		PAGE_REPLACEMENT;
	}

	public HardDiskException() {}
	
	public HardDiskException(String msg) {
		super(msg);
	}
	
	public HardDiskException(String msg, HardDiskException.ActionError status){
		super(msg + status.toString());
	}
}

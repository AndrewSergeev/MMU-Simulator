package com.hit.memoryunits;

import java.io.Serializable;
import java.util.Arrays;

public class Page<T> implements Serializable{		
	private static final long serialVersionUID = -616017516154926601L;
	private Long id;
	private T content;
	
	public Page(Long id, T content){
		this.id = id;
		this.content = content;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean ifEquals = false;		
		
		if(this.hashCode() == obj.hashCode())
		{
			ifEquals = true;
		}
		
		return ifEquals;
	}
	
	@Override
	public String toString() {
		return " [id:" + this.id + " content:" + Arrays.toString((byte[])this.content) + "]";
	}
	
	@Override
	public int hashCode() {
		return this.id.intValue();
	}
	
	public T getContent() {
		return this.content;
	}
	 
	public Long getPageId() {
		return this.id;
	}	
	 
	public void	setContent(T content) {
		this.content = content;
	}
	 
	public void	setPageId(Long pageId) {
		this.id = pageId;
	}
}

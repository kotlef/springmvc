package com.ynswet.common.push.domain;

import java.util.ArrayList;
import java.util.List;

public class ListJsonResult<T extends Object> {

	private final static int LIST_TOTAL=0;

	private final static Boolean LIST_SUCCESS=true;

	private final static int LIST_MSG=150;

	private Boolean success = LIST_SUCCESS;

	private int msgCode=LIST_MSG;

	private int total=LIST_TOTAL;
	
	private String debug = "debug info";
	
	private List<T> result= new ArrayList<T>(0);
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public int getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(int msgCode) {
		this.msgCode = msgCode;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}
	
	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	@Override
	public String toString() {
		return "ListJsonResult [success=" + success + ", msgCode=" + msgCode
				+ ", total=" + total + ", debug=" + debug + ", result="
				+ result + "]";
	}
}

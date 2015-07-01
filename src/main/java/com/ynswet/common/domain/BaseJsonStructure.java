package com.ynswet.common.domain;


public class BaseJsonStructure{

	private final static int LIST_TOTAL=0;

	private final static Boolean LIST_SUCCESS=true;

	private final static String LIST_MSG="Procedural Success";

	private Boolean success = LIST_SUCCESS;

	private String msg=LIST_MSG;

	private int total=LIST_TOTAL;

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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "BaseJsonStructure [success=" + success + ", msg=" + msg
				+ ", total=" + total + "]";
	}
}

package com.ynswet.common.domain;

public class SingleJsonStructure extends BaseJsonStructure{

	private Object rows;

	public SingleJsonStructure() {

	}

	public Object getRows() {
		return rows;
	}

	public void setRows(Object rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "RestJsonObject [success=" + getSuccess() + ", msg=" + getMsg()
				+ ", total=" + getTotal() + ", rows=" + rows + "]";
	}

	public String toObjectString(String rows) {
		return "{\"success\":" + getSuccess() + ", \"msg\":\"" + getMsg()
				+ "\", \"total\":" + getTotal() + ",\"rows\":\"" + rows + "\"}";
	}
}

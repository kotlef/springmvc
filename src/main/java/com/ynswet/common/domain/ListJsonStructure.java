package com.ynswet.common.domain;

import java.util.List;

public class ListJsonStructure<T extends Object> extends BaseJsonStructure{


	private List<T> rows;

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "ListJsonStructure [success=" + getSuccess() + ", msg=" + getMsg()
				+ ", total=" + getTotal() + ", rows=" + rows + "]";
	}

}

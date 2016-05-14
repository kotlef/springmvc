package com.ynswet.common.domain;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author root
 *
 * @param <T>
 */
public class SetJsonStructure<T extends Object> extends BaseJsonStructure{

	private Set<T> rows= new HashSet<T>(0);

	public Set<T> getRows() {
		return rows;
	}


	public void setRows(Set<T> rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "ObjectJsonStructure [success=" + getSuccess() + ", msg=" + getMsg()
				+ ", total=" + getTotal() + ", rows=" + rows + "]";
	}

}

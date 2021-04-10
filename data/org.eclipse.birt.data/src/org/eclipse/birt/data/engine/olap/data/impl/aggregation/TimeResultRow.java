package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

public class TimeResultRow {
	private Object[] value;

	TimeResultRow(Object[] value) {
		this.value = value;
	}

	public Object[] getValue() {
		return this.value;
	}
}

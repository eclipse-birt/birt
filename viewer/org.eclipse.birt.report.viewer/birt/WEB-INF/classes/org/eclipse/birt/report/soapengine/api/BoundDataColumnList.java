package org.eclipse.birt.report.soapengine.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "BoundDataColumnList")
@XmlAccessorType(XmlAccessType.FIELD)
public class BoundDataColumnList implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "BoundDataColumn")
	private List<BoundDataColumn> boundDataColumns = new ArrayList<>();

	public BoundDataColumnList() {
	}

	public BoundDataColumnList(List<BoundDataColumn> boundDataColumns) {
		this.boundDataColumns = boundDataColumns;
	}

	public List<BoundDataColumn> getBoundDataColumns() {
		return boundDataColumns;
	}

	public void setBoundDataColumns(List<BoundDataColumn> boundDataColumns) {
		this.boundDataColumns = boundDataColumns;
	}

	public void addBoundDataColumn(BoundDataColumn column) {
		this.boundDataColumns.add(column);
	}

	public BoundDataColumn get(int index) {
		return this.boundDataColumns.get(index);
	}

	public void set(int index, BoundDataColumn column) {
		this.boundDataColumns.set(index, column);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof BoundDataColumnList))
			return false;
		BoundDataColumnList that = (BoundDataColumnList) o;
		return Objects.equals(boundDataColumns, that.boundDataColumns);
	}

	@Override
	public int hashCode() {
		return Objects.hash(boundDataColumns);
	}

	@Override
	public String toString() {
		return "BoundDataColumnList{" + "boundDataColumns=" + boundDataColumns + '}';
	}
}

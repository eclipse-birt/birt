package org.eclipse.birt.report.soapengine.api;

import java.io.Serializable;
import java.util.Arrays;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ColumnDefinitionGroup")
@XmlAccessorType(XmlAccessType.NONE)
public class ColumnDefinitionGroup implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "ColumnDefinition")
	private ColumnDefinition[] columnDef;

	public ColumnDefinitionGroup() {
	}

	public ColumnDefinitionGroup(ColumnDefinition[] columnDef) {
		this.columnDef = columnDef;
	}

	public ColumnDefinition[] getColumnDef() {
		return columnDef;
	}

	public void setColumnDef(ColumnDefinition[] columnDef) {
		this.columnDef = columnDef;
	}

	public ColumnDefinition getColumnDef(int i) {
		return this.columnDef[i];
	}

	public void setColumnDef(int i, ColumnDefinition value) {
		this.columnDef[i] = value;
	}

	private Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ColumnDefinitionGroup))
			return false;
		if (this == obj)
			return true;
		ColumnDefinitionGroup other = (ColumnDefinitionGroup) obj;
		if (__equalsCalc != null)
			return (__equalsCalc == obj);
		__equalsCalc = obj;
		boolean _equals = (columnDef == null ? other.columnDef == null : Arrays.equals(columnDef, other.columnDef));
		__equalsCalc = null;
		return _equals;
	}

	private boolean __hashCodeCalc = false;

	@Override
	public synchronized int hashCode() {
		if (__hashCodeCalc)
			return 0;
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (columnDef != null) {
			for (ColumnDefinition c : columnDef) {
				if (c != null)
					_hashCode += c.hashCode();
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}
}

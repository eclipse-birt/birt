package org.eclipse.birt.report.soapengine.api;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ColumnProperties")
@XmlAccessorType(XmlAccessType.NONE)
public class ColumnProperties implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "Width")
	private double width;
	@XmlElement(name = "Alignment")
	private Alignment alignment;

	public ColumnProperties() {
	}

	public ColumnProperties(double width, Alignment alignment) {
		this.width = width;
		this.alignment = alignment;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	private Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ColumnProperties))
			return false;
		if (this == obj)
			return true;
		ColumnProperties other = (ColumnProperties) obj;
		if (__equalsCalc != null)
			return (__equalsCalc == obj);
		__equalsCalc = obj;
		boolean _equals = this.width == other.width
				&& (this.alignment == null ? other.alignment == null : this.alignment.equals(other.alignment));
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
		_hashCode += Double.valueOf(width).hashCode();
		if (alignment != null)
			_hashCode += alignment.hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}
}

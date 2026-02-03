package org.eclipse.birt.report.soapengine.api;

import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "BoundDataColumn")
@XmlAccessorType(XmlAccessType.FIELD)
public class BoundDataColumn {
	@XmlElement(name = "Name")
	private String name;
	@XmlElement(name = "Expression")
	private String expression;
	@XmlElement(name = "Type")
	private String type;

	public BoundDataColumn() {
	}

	public BoundDataColumn(String name, String expression, String type) {
		this.name = name;
		this.expression = expression;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof BoundDataColumn))
			return false;
		BoundDataColumn that = (BoundDataColumn) o;
		return Objects.equals(name, that.name) && Objects.equals(expression, that.expression)
				&& Objects.equals(type, that.type);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, expression, type);
	}

	@Override
	public String toString() {
		return "BoundDataColumn{" + "name='" + name + '\'' + ", expression='" + expression + '\'' + ", type='" + type
				+ '\'' + '}';
	}
}

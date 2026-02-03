package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "ChartType")
@XmlEnum(String.class)
public enum ChartType {

	@XmlEnumValue("VBar")
	VBAR("VBar"),

	@XmlEnumValue("Pie")
	PIE("Pie"),

	@XmlEnumValue("Area")
	AREA("Area"),

	@XmlEnumValue("Line")
	LINE("Line"),

	@XmlEnumValue("Scatter")
	SCATTER("Scatter"),

	@XmlEnumValue("Meter")
	METER("Meter"),

	@XmlEnumValue("Stock")
	STOCK("Stock");

	private final String value;

	ChartType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static ChartType fromValue(String value) {
		for (ChartType t : ChartType.values()) {
			if (t.value.equals(value)) {
				return t;
			}
		}
		throw new IllegalArgumentException("Unknown ChartType: " + value);
	}

	@Override
	public String toString() {
		return value;
	}
}
package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "ChartLocation")
@XmlEnum(String.class)
public enum ChartLocation {

	@XmlEnumValue("Above")
	ABOVE("Above"),

	@XmlEnumValue("Below")
	BELOW("Below"),

	@XmlEnumValue("Left")
	LEFT("Left"),

	@XmlEnumValue("Right")
	RIGHT("Right");

	private final String value;

	ChartLocation(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static ChartLocation fromValue(String value) {
		for (ChartLocation l : ChartLocation.values()) {
			if (l.value.equals(value)) {
				return l;
			}
		}
		throw new IllegalArgumentException("Unknown ChartLocation: " + value);
	}

	@Override
	public String toString() {
		return value;
	}
}

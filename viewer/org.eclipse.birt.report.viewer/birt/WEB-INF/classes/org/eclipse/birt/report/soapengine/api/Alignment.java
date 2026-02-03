package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "Alignment")
@XmlEnum(String.class)
public enum Alignment {

	@XmlEnumValue("left")
	LEFT("left"),

	@XmlEnumValue("right")
	RIGHT("right"),

	@XmlEnumValue("center")
	CENTER("center");

	private final String value;

	Alignment(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static Alignment fromValue(String value) {
		for (Alignment a : Alignment.values()) {
			if (a.value.equals(value)) {
				return a;
			}
		}
		throw new IllegalArgumentException("Unknown Alignment: " + value);
	}

	@Override
	public String toString() {
		return value;
	}
}

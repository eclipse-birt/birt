/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 * FilterType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "FilterType")
@XmlEnum(String.class)
public enum FilterType {

	@XmlEnumValue("Simple")
	SIMPLE("Simple"),

	@XmlEnumValue("Advanced")
	ADVANCED("Advanced");

	private final String value;

	FilterType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static FilterType fromValue(String value) {
		for (FilterType t : FilterType.values()) {
			if (t.value.equals(value)) {
				return t;
			}
		}
		throw new IllegalArgumentException("Unknown FilterType: " + value);
	}

	@Override
	public String toString() {
		return value;
	}
}
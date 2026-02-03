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
 * SortingDirection.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "SortingDirection")
@XmlEnum(String.class)
public enum SortingDirection {

	@XmlEnumValue("asc")
	ASC("asc"),

	@XmlEnumValue("desc")
	DESC("desc"),

	@XmlEnumValue("none")
	NONE("none");

	private final String value;

	SortingDirection(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static SortingDirection fromValue(String value) {
		for (SortingDirection d : SortingDirection.values()) {
			if (d.value.equals(value)) {
				return d;
			}
		}
		throw new IllegalArgumentException("Unknown SortingDirection: " + value);
	}

	@Override
	public String toString() {
		return value;
	}
}

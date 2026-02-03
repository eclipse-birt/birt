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
 * ReportIdType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "ReportIdType")
@XmlEnum
public enum ReportIdType {

	@XmlEnumValue("Chart")
	Chart("Chart"),

	@XmlEnumValue("Document")
	Document("Document"),

	@XmlEnumValue("Label")
	Label("Label"),

	@XmlEnumValue("Table")
	Table("Table"),

	@XmlEnumValue("Group")
	Group("Group"),

	@XmlEnumValue("ColumnInfo")
	ColumnInfo("ColumnInfo"),

	@XmlEnumValue("Chart_T")
	Chart_T("Chart_T"),

	@XmlEnumValue("Label_T")
	Label_T("Label_T"),

	@XmlEnumValue("Table_T")
	Table_T("Table_T"),

	@XmlEnumValue("Dataset")
	Dataset("Dataset"),

	@XmlEnumValue("Extended")
	Extended("Extended");

	private final String value;

	ReportIdType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static ReportIdType fromValue(String v) {
		for (ReportIdType c : ReportIdType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException("Unknown value: " + v);
	}
}

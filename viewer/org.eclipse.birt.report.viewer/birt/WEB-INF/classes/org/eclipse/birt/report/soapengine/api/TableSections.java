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
 * TableSections.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * list of S_Info
 */
@XmlRootElement(name = "TableSections")
@XmlAccessorType(XmlAccessType.NONE)
public class TableSections implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "section")
	private org.eclipse.birt.report.soapengine.api.S_Info[] section;

	public TableSections() {
	}

	public TableSections(org.eclipse.birt.report.soapengine.api.S_Info[] section) {
		this.section = section;
	}

	/**
	 * Gets the section value for this TableSections.
	 *
	 * @return section
	 */
	public org.eclipse.birt.report.soapengine.api.S_Info[] getSection() {
		return section;
	}

	/**
	 * Sets the section value for this TableSections.
	 *
	 * @param section
	 */
	public void setSection(org.eclipse.birt.report.soapengine.api.S_Info[] section) {
		this.section = section;
	}

	public org.eclipse.birt.report.soapengine.api.S_Info getSection(int i) {
		return this.section[i];
	}

	public void setSection(int i, org.eclipse.birt.report.soapengine.api.S_Info _value) {
		this.section[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof TableSections)) {
			return false;
		}
		TableSections other = (TableSections) obj;
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && ((this.section == null && other.getSection() == null)
				|| (this.section != null && java.util.Arrays.equals(this.section, other.getSection())));
		__equalsCalc = null;
		return _equals;
	}

	private boolean __hashCodeCalc = false;

	@Override
	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (getSection() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getSection()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getSection(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

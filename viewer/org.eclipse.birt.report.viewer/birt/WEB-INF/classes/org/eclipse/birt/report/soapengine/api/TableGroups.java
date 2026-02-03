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
 * TableGroups.java
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
 * list of G_Info
 */
@XmlRootElement(name = "TableGroups")
@XmlAccessorType(XmlAccessType.NONE)
public class TableGroups implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "group")
	private org.eclipse.birt.report.soapengine.api.G_Info[] group;

	public TableGroups() {
	}

	public TableGroups(org.eclipse.birt.report.soapengine.api.G_Info[] group) {
		this.group = group;
	}

	/**
	 * Gets the group value for this TableGroups.
	 *
	 * @return group
	 */
	public org.eclipse.birt.report.soapengine.api.G_Info[] getGroup() {
		return group;
	}

	/**
	 * Sets the group value for this TableGroups.
	 *
	 * @param group
	 */
	public void setGroup(org.eclipse.birt.report.soapengine.api.G_Info[] group) {
		this.group = group;
	}

	public org.eclipse.birt.report.soapengine.api.G_Info getGroup(int i) {
		return this.group[i];
	}

	public void setGroup(int i, org.eclipse.birt.report.soapengine.api.G_Info _value) {
		this.group[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof TableGroups)) {
			return false;
		}
		TableGroups other = (TableGroups) obj;
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
		_equals = true && ((this.group == null && other.getGroup() == null)
				|| (this.group != null && java.util.Arrays.equals(this.group, other.getGroup())));
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
		if (getGroup() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getGroup()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getGroup(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

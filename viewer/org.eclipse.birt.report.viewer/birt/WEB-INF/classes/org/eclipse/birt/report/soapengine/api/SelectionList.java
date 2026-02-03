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
 * SelectionList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SelectionList")
@XmlAccessorType(XmlAccessType.NONE)
public class SelectionList implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "name")
	private java.lang.String name;
	@XmlElement(name = "selections")
	private org.eclipse.birt.report.soapengine.api.SelectItemChoice[] selections;

	public SelectionList() {
	}

	public SelectionList(java.lang.String name, org.eclipse.birt.report.soapengine.api.SelectItemChoice[] selections) {
		this.name = name;
		this.selections = selections;
	}

	/**
	 * Gets the name value for this SelectionList.
	 *
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this SelectionList.
	 *
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the selections value for this SelectionList.
	 *
	 * @return selections
	 */
	public org.eclipse.birt.report.soapengine.api.SelectItemChoice[] getSelections() {
		return selections;
	}

	/**
	 * Sets the selections value for this SelectionList.
	 *
	 * @param selections
	 */
	public void setSelections(org.eclipse.birt.report.soapengine.api.SelectItemChoice[] selections) {
		this.selections = selections;
	}

	public org.eclipse.birt.report.soapengine.api.SelectItemChoice getSelections(int i) {
		return this.selections[i];
	}

	public void setSelections(int i, org.eclipse.birt.report.soapengine.api.SelectItemChoice _value) {
		this.selections[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof SelectionList)) {
			return false;
		}
		SelectionList other = (SelectionList) obj;
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
		_equals = true
				&& ((this.name == null && other.getName() == null)
						|| (this.name != null && this.name.equals(other.getName())))
				&& ((this.selections == null && other.getSelections() == null) || (this.selections != null
						&& java.util.Arrays.equals(this.selections, other.getSelections())));
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
		if (getName() != null) {
			_hashCode += getName().hashCode();
		}
		if (getSelections() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getSelections()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getSelections(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

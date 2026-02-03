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
 * TableLayoutList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TableLayoutList")
@XmlAccessorType(XmlAccessType.NONE)
public class TableLayoutList implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "tableLayout")
	private org.eclipse.birt.report.soapengine.api.TableLayout[] tableLayout;

	public TableLayoutList() {
	}

	public TableLayoutList(org.eclipse.birt.report.soapengine.api.TableLayout[] tableLayout) {
		this.tableLayout = tableLayout;
	}

	/**
	 * Gets the tableLayout value for this TableLayoutList.
	 *
	 * @return tableLayout
	 */
	public org.eclipse.birt.report.soapengine.api.TableLayout[] getTableLayout() {
		return tableLayout;
	}

	/**
	 * Sets the tableLayout value for this TableLayoutList.
	 *
	 * @param tableLayout
	 */
	public void setTableLayout(org.eclipse.birt.report.soapengine.api.TableLayout[] tableLayout) {
		this.tableLayout = tableLayout;
	}

	public org.eclipse.birt.report.soapengine.api.TableLayout getTableLayout(int i) {
		return this.tableLayout[i];
	}

	public void setTableLayout(int i, org.eclipse.birt.report.soapengine.api.TableLayout _value) {
		this.tableLayout[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof TableLayoutList)) {
			return false;
		}
		TableLayoutList other = (TableLayoutList) obj;
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
		_equals = true && ((this.tableLayout == null && other.getTableLayout() == null)
				|| (this.tableLayout != null && java.util.Arrays.equals(this.tableLayout, other.getTableLayout())));
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
		if (getTableLayout() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getTableLayout()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getTableLayout(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

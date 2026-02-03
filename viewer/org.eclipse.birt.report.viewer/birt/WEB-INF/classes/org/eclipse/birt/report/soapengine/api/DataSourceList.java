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
 * DataSourceList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DataSourceList")
@XmlAccessorType(XmlAccessType.NONE)
public class DataSourceList implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "DataSource")
	private org.eclipse.birt.report.soapengine.api.DataSource[] dataSource;

	public DataSourceList() {
	}

	public DataSourceList(org.eclipse.birt.report.soapengine.api.DataSource[] dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Gets the dataSource value for this DataSourceList.
	 *
	 * @return dataSource
	 */
	public org.eclipse.birt.report.soapengine.api.DataSource[] getDataSource() {
		return dataSource;
	}

	/**
	 * Sets the dataSource value for this DataSourceList.
	 *
	 * @param dataSource
	 */
	public void setDataSource(org.eclipse.birt.report.soapengine.api.DataSource[] dataSource) {
		this.dataSource = dataSource;
	}

	public org.eclipse.birt.report.soapengine.api.DataSource getDataSource(int i) {
		return this.dataSource[i];
	}

	public void setDataSource(int i, org.eclipse.birt.report.soapengine.api.DataSource _value) {
		this.dataSource[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof DataSourceList)) {
			return false;
		}
		DataSourceList other = (DataSourceList) obj;
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
		_equals = true && ((this.dataSource == null && other.getDataSource() == null)
				|| (this.dataSource != null && java.util.Arrays.equals(this.dataSource, other.getDataSource())));
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
		if (getDataSource() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getDataSource()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getDataSource(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

}

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
 * DataSetList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DataSetList")
@XmlAccessorType(XmlAccessType.NONE)
public class DataSetList implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "dataSet")
	private org.eclipse.birt.report.soapengine.api.DataSet[] dataSet;

	public DataSetList() {
	}

	public DataSetList(org.eclipse.birt.report.soapengine.api.DataSet[] dataSet) {
		this.dataSet = dataSet;
	}

	/**
	 * Gets the dataSet value for this DataSetList.
	 *
	 * @return dataSet
	 */
	public org.eclipse.birt.report.soapengine.api.DataSet[] getDataSet() {
		return dataSet;
	}

	/**
	 * Sets the dataSet value for this DataSetList.
	 *
	 * @param dataSet
	 */
	public void setDataSet(org.eclipse.birt.report.soapengine.api.DataSet[] dataSet) {
		this.dataSet = dataSet;
	}

	public org.eclipse.birt.report.soapengine.api.DataSet getDataSet(int i) {
		return this.dataSet[i];
	}

	public void setDataSet(int i, org.eclipse.birt.report.soapengine.api.DataSet _value) {
		this.dataSet[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof DataSetList)) {
			return false;
		}
		DataSetList other = (DataSetList) obj;
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
		_equals = true && ((this.dataSet == null && other.getDataSet() == null)
				|| (this.dataSet != null && java.util.Arrays.equals(this.dataSet, other.getDataSet())));
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
		if (getDataSet() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getDataSet()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getDataSet(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

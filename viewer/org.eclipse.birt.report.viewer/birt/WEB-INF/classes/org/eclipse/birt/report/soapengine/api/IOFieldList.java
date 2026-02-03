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
 * IOFieldList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "IOFieldList")
@XmlAccessorType(XmlAccessType.NONE)
public class IOFieldList implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "IOFullPath")
	private java.lang.String IOFullPath;
	@XmlElement(name = "fields")
	private org.eclipse.birt.report.soapengine.api.IOField[] fields;

	public IOFieldList() {
	}

	public IOFieldList(java.lang.String IOFullPath, org.eclipse.birt.report.soapengine.api.IOField[] fields) {
		this.IOFullPath = IOFullPath;
		this.fields = fields;
	}

	/**
	 * Gets the IOFullPath value for this IOFieldList.
	 *
	 * @return IOFullPath
	 */
	public java.lang.String getIOFullPath() {
		return IOFullPath;
	}

	/**
	 * Sets the IOFullPath value for this IOFieldList.
	 *
	 * @param IOFullPath
	 */
	public void setIOFullPath(java.lang.String IOFullPath) {
		this.IOFullPath = IOFullPath;
	}

	/**
	 * Gets the fields value for this IOFieldList.
	 *
	 * @return fields
	 */
	public org.eclipse.birt.report.soapengine.api.IOField[] getFields() {
		return fields;
	}

	/**
	 * Sets the fields value for this IOFieldList.
	 *
	 * @param fields
	 */
	public void setFields(org.eclipse.birt.report.soapengine.api.IOField[] fields) {
		this.fields = fields;
	}

	public org.eclipse.birt.report.soapengine.api.IOField getFields(int i) {
		return this.fields[i];
	}

	public void setFields(int i, org.eclipse.birt.report.soapengine.api.IOField _value) {
		this.fields[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof IOFieldList)) {
			return false;
		}
		IOFieldList other = (IOFieldList) obj;
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
				&& ((this.IOFullPath == null && other.getIOFullPath() == null)
						|| (this.IOFullPath != null && this.IOFullPath.equals(other.getIOFullPath())))
				&& ((this.fields == null && other.getFields() == null)
						|| (this.fields != null && java.util.Arrays.equals(this.fields, other.getFields())));
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
		if (getIOFullPath() != null) {
			_hashCode += getIOFullPath().hashCode();
		}
		if (getFields() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getFields()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getFields(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

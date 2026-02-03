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
 * IOInfoList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "IOInfoList")
@XmlAccessorType(XmlAccessType.NONE)
public class IOInfoList implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "IOFieldListArray")
	private org.eclipse.birt.report.soapengine.api.IOFieldList[] IOFieldListArray;

	public IOInfoList() {
	}

	public IOInfoList(org.eclipse.birt.report.soapengine.api.IOFieldList[] IOFieldListArray) {
		this.IOFieldListArray = IOFieldListArray;
	}

	/**
	 * Gets the IOFieldListArray value for this IOInfoList.
	 *
	 * @return IOFieldListArray
	 */
	public org.eclipse.birt.report.soapengine.api.IOFieldList[] getIOFieldListArray() {
		return IOFieldListArray;
	}

	/**
	 * Sets the IOFieldListArray value for this IOInfoList.
	 *
	 * @param IOFieldListArray
	 */
	public void setIOFieldListArray(org.eclipse.birt.report.soapengine.api.IOFieldList[] IOFieldListArray) {
		this.IOFieldListArray = IOFieldListArray;
	}

	public org.eclipse.birt.report.soapengine.api.IOFieldList getIOFieldListArray(int i) {
		return this.IOFieldListArray[i];
	}

	public void setIOFieldListArray(int i, org.eclipse.birt.report.soapengine.api.IOFieldList _value) {
		this.IOFieldListArray[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof IOInfoList)) {
			return false;
		}
		IOInfoList other = (IOInfoList) obj;
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
		_equals = true && ((this.IOFieldListArray == null && other.getIOFieldListArray() == null)
				|| (this.IOFieldListArray != null
						&& java.util.Arrays.equals(this.IOFieldListArray, other.getIOFieldListArray())));
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
		if (getIOFieldListArray() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getIOFieldListArray()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getIOFieldListArray(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

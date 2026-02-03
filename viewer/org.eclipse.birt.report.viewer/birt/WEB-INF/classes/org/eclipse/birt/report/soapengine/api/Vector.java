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
 * Vector.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Vector")
@XmlAccessorType(XmlAccessType.NONE)
public class Vector implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "value")
	private java.lang.String[] value;

	public Vector() {
	}

	public Vector(java.lang.String[] value) {
		this.value = value;
	}

	/**
	 * Gets the value value for this Vector.
	 *
	 * @return value
	 */
	public java.lang.String[] getValue() {
		return value;
	}

	/**
	 * Sets the value value for this Vector.
	 *
	 * @param value
	 */
	public void setValue(java.lang.String[] value) {
		this.value = value;
	}

	public java.lang.String getValue(int i) {
		return this.value[i];
	}

	public void setValue(int i, java.lang.String _value) {
		this.value[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof Vector)) {
			return false;
		}
		Vector other = (Vector) obj;
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
		_equals = true && ((this.value == null && other.getValue() == null)
				|| (this.value != null && java.util.Arrays.equals(this.value, other.getValue())));
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
		if (getValue() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getValue()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getValue(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

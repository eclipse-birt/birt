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
 * IOList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "IOList")
@XmlAccessorType(XmlAccessType.NONE)
public class IOList implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "IO")
	private org.eclipse.birt.report.soapengine.api.IOReference[] IO;

	public IOList() {
	}

	public IOList(org.eclipse.birt.report.soapengine.api.IOReference[] IO) {
		this.IO = IO;
	}

	/**
	 * Gets the IO value for this IOList.
	 *
	 * @return IO
	 */
	public org.eclipse.birt.report.soapengine.api.IOReference[] getIO() {
		return IO;
	}

	/**
	 * Sets the IO value for this IOList.
	 *
	 * @param IO
	 */
	public void setIO(org.eclipse.birt.report.soapengine.api.IOReference[] IO) {
		this.IO = IO;
	}

	public org.eclipse.birt.report.soapengine.api.IOReference getIO(int i) {
		return this.IO[i];
	}

	public void setIO(int i, org.eclipse.birt.report.soapengine.api.IOReference _value) {
		this.IO[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof IOList)) {
			return false;
		}
		IOList other = (IOList) obj;
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
		_equals = true && ((this.IO == null && other.getIO() == null)
				|| (this.IO != null && java.util.Arrays.equals(this.IO, other.getIO())));
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
		if (getIO() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getIO()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getIO(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

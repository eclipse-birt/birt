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
 * GetUpdatedObjectsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetUpdatedObjectsResponse")
@XmlAccessorType(XmlAccessType.NONE)
public class GetUpdatedObjectsResponse implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "Update")
	private org.eclipse.birt.report.soapengine.api.Update[] update;

	public GetUpdatedObjectsResponse() {
	}

	public GetUpdatedObjectsResponse(org.eclipse.birt.report.soapengine.api.Update[] update) {
		this.update = update;
	}

	/**
	 * Gets the update value for this GetUpdatedObjectsResponse.
	 *
	 * @return update
	 */
	public org.eclipse.birt.report.soapengine.api.Update[] getUpdate() {
		return update;
	}

	/**
	 * Sets the update value for this GetUpdatedObjectsResponse.
	 *
	 * @param update
	 */
	public void setUpdate(org.eclipse.birt.report.soapengine.api.Update[] update) {
		this.update = update;
	}

	public org.eclipse.birt.report.soapengine.api.Update getUpdate(int i) {
		return this.update[i];
	}

	public void setUpdate(int i, org.eclipse.birt.report.soapengine.api.Update _value) {
		this.update[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof GetUpdatedObjectsResponse)) {
			return false;
		}
		GetUpdatedObjectsResponse other = (GetUpdatedObjectsResponse) obj;
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
		_equals = true && ((this.update == null && other.getUpdate() == null)
				|| (this.update != null && java.util.Arrays.equals(this.update, other.getUpdate())));
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
		if (getUpdate() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getUpdate()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getUpdate(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

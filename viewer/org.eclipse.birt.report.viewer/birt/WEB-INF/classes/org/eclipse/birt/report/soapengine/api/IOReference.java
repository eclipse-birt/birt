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
 * IOReference.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "IOReference")
@XmlAccessorType(XmlAccessType.NONE)
public class IOReference implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "id")
	private java.lang.String id;
	@XmlElement(name = "fullPath")
	private java.lang.String fullPath;

	public IOReference() {
	}

	public IOReference(java.lang.String id, java.lang.String fullPath) {
		this.id = id;
		this.fullPath = fullPath;
	}

	/**
	 * Gets the id value for this IOReference.
	 *
	 * @return id
	 */
	public java.lang.String getId() {
		return id;
	}

	/**
	 * Sets the id value for this IOReference.
	 *
	 * @param id
	 */
	public void setId(java.lang.String id) {
		this.id = id;
	}

	/**
	 * Gets the fullPath value for this IOReference.
	 *
	 * @return fullPath
	 */
	public java.lang.String getFullPath() {
		return fullPath;
	}

	/**
	 * Sets the fullPath value for this IOReference.
	 *
	 * @param fullPath
	 */
	public void setFullPath(java.lang.String fullPath) {
		this.fullPath = fullPath;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof IOReference)) {
			return false;
		}
		IOReference other = (IOReference) obj;
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
				&& ((this.id == null && other.getId() == null) || (this.id != null && this.id.equals(other.getId())))
				&& ((this.fullPath == null && other.getFullPath() == null)
						|| (this.fullPath != null && this.fullPath.equals(other.getFullPath())));
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
		if (getId() != null) {
			_hashCode += getId().hashCode();
		}
		if (getFullPath() != null) {
			_hashCode += getFullPath().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

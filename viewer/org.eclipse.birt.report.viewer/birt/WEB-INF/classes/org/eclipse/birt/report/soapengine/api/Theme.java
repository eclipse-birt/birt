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
 * Theme.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Theme")
@XmlAccessorType(XmlAccessType.NONE)
public class Theme implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "Id")
	private long id;
	@XmlElement(name = "Name")
	private java.lang.String name;
	@XmlElement(name = "DisplayName")
	private java.lang.String displayName;

	public Theme() {
	}

	public Theme(long id, java.lang.String name, java.lang.String displayName) {
		this.id = id;
		this.name = name;
		this.displayName = displayName;
	}

	/**
	 * Gets the id value for this Theme.
	 *
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id value for this Theme.
	 *
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the name value for this Theme.
	 *
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this Theme.
	 *
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the displayName value for this Theme.
	 *
	 * @return displayName
	 */
	public java.lang.String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the displayName value for this Theme.
	 *
	 * @param displayName
	 */
	public void setDisplayName(java.lang.String displayName) {
		this.displayName = displayName;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof Theme)) {
			return false;
		}
		Theme other = (Theme) obj;
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
		_equals = true && this.id == other.getId()
				&& ((this.name == null && other.getName() == null)
						|| (this.name != null && this.name.equals(other.getName())))
				&& ((this.displayName == null && other.getDisplayName() == null)
						|| (this.displayName != null && this.displayName.equals(other.getDisplayName())));
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
		_hashCode += Long.valueOf(getId()).hashCode();
		if (getName() != null) {
			_hashCode += getName().hashCode();
		}
		if (getDisplayName() != null) {
			_hashCode += getDisplayName().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

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
 * RepositoryPathSegment.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RepositoryPathSegment")
@XmlAccessorType(XmlAccessType.NONE)
public class RepositoryPathSegment implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "index")
	private int index;
	@XmlElement(name = "name")
	private java.lang.String name;
	@XmlElement(name = "RPath")
	private java.lang.String RPath;

	public RepositoryPathSegment() {
	}

	public RepositoryPathSegment(int index, java.lang.String name, java.lang.String RPath) {
		this.index = index;
		this.name = name;
		this.RPath = RPath;
	}

	/**
	 * Gets the index value for this RepositoryPathSegment.
	 *
	 * @return index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the index value for this RepositoryPathSegment.
	 *
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Gets the name value for this RepositoryPathSegment.
	 *
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this RepositoryPathSegment.
	 *
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the RPath value for this RepositoryPathSegment.
	 *
	 * @return RPath
	 */
	public java.lang.String getRPath() {
		return RPath;
	}

	/**
	 * Sets the RPath value for this RepositoryPathSegment.
	 *
	 * @param RPath
	 */
	public void setRPath(java.lang.String RPath) {
		this.RPath = RPath;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof RepositoryPathSegment)) {
			return false;
		}
		RepositoryPathSegment other = (RepositoryPathSegment) obj;
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
		_equals = true && this.index == other.getIndex()
				&& ((this.name == null && other.getName() == null)
						|| (this.name != null && this.name.equals(other.getName())))
				&& ((this.RPath == null && other.getRPath() == null)
						|| (this.RPath != null && this.RPath.equals(other.getRPath())));
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
		_hashCode += getIndex();
		if (getName() != null) {
			_hashCode += getName().hashCode();
		}
		if (getRPath() != null) {
			_hashCode += getRPath().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

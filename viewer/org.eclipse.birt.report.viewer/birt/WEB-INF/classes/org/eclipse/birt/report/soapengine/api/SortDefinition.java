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
 * SortDefinition.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SortDefinition")
@XmlAccessorType(XmlAccessType.NONE)
public class SortDefinition implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "columnIndex")
	private int columnIndex;
	@XmlElement(name = "sortDir")
	private org.eclipse.birt.report.soapengine.api.SortingDirection sortDir;

	public SortDefinition() {
	}

	public SortDefinition(int columnIndex, org.eclipse.birt.report.soapengine.api.SortingDirection sortDir) {
		this.columnIndex = columnIndex;
		this.sortDir = sortDir;
	}

	/**
	 * Gets the columnIndex value for this SortDefinition.
	 *
	 * @return columnIndex
	 */
	public int getColumnIndex() {
		return columnIndex;
	}

	/**
	 * Sets the columnIndex value for this SortDefinition.
	 *
	 * @param columnIndex
	 */
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	/**
	 * Gets the sortDir value for this SortDefinition.
	 *
	 * @return sortDir
	 */
	public org.eclipse.birt.report.soapengine.api.SortingDirection getSortDir() {
		return sortDir;
	}

	/**
	 * Sets the sortDir value for this SortDefinition.
	 *
	 * @param sortDir
	 */
	public void setSortDir(org.eclipse.birt.report.soapengine.api.SortingDirection sortDir) {
		this.sortDir = sortDir;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof SortDefinition)) {
			return false;
		}
		SortDefinition other = (SortDefinition) obj;
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
		_equals = true && this.columnIndex == other.getColumnIndex()
				&& ((this.sortDir == null && other.getSortDir() == null)
						|| (this.sortDir != null && this.sortDir.equals(other.getSortDir())));
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
		_hashCode += getColumnIndex();
		if (getSortDir() != null) {
			_hashCode += getSortDir().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

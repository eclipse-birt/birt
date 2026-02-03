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
 * SectionDefinition.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SectionDefinition")
@XmlAccessorType(XmlAccessType.NONE)
public class SectionDefinition implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "id")
	private java.lang.String id;
	@XmlElement(name = "tableId")
	private java.lang.String tableId;
	@XmlElement(name = "level")
	private java.lang.Integer level;
	@XmlElement(name = "sortDir")
	private org.eclipse.birt.report.soapengine.api.SortingDirection sortDir;

	public SectionDefinition() {
	}

	public SectionDefinition(java.lang.String id, java.lang.String tableId, java.lang.Integer level,
			org.eclipse.birt.report.soapengine.api.SortingDirection sortDir) {
		this.id = id;
		this.tableId = tableId;
		this.level = level;
		this.sortDir = sortDir;
	}

	/**
	 * Gets the id value for this SectionDefinition.
	 *
	 * @return id
	 */
	public java.lang.String getId() {
		return id;
	}

	/**
	 * Sets the id value for this SectionDefinition.
	 *
	 * @param id
	 */
	public void setId(java.lang.String id) {
		this.id = id;
	}

	/**
	 * Gets the tableId value for this SectionDefinition.
	 *
	 * @return tableId
	 */
	public java.lang.String getTableId() {
		return tableId;
	}

	/**
	 * Sets the tableId value for this SectionDefinition.
	 *
	 * @param tableId
	 */
	public void setTableId(java.lang.String tableId) {
		this.tableId = tableId;
	}

	/**
	 * Gets the level value for this SectionDefinition.
	 *
	 * @return level
	 */
	public java.lang.Integer getLevel() {
		return level;
	}

	/**
	 * Sets the level value for this SectionDefinition.
	 *
	 * @param level
	 */
	public void setLevel(java.lang.Integer level) {
		this.level = level;
	}

	/**
	 * Gets the sortDir value for this SectionDefinition.
	 *
	 * @return sortDir
	 */
	public org.eclipse.birt.report.soapengine.api.SortingDirection getSortDir() {
		return sortDir;
	}

	/**
	 * Sets the sortDir value for this SectionDefinition.
	 *
	 * @param sortDir
	 */
	public void setSortDir(org.eclipse.birt.report.soapengine.api.SortingDirection sortDir) {
		this.sortDir = sortDir;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof SectionDefinition)) {
			return false;
		}
		SectionDefinition other = (SectionDefinition) obj;
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
				&& ((this.tableId == null && other.getTableId() == null)
						|| (this.tableId != null && this.tableId.equals(other.getTableId())))
				&& ((this.level == null && other.getLevel() == null)
						|| (this.level != null && this.level.equals(other.getLevel())))
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
		if (getId() != null) {
			_hashCode += getId().hashCode();
		}
		if (getTableId() != null) {
			_hashCode += getTableId().hashCode();
		}
		if (getLevel() != null) {
			_hashCode += getLevel().hashCode();
		}
		if (getSortDir() != null) {
			_hashCode += getSortDir().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

}

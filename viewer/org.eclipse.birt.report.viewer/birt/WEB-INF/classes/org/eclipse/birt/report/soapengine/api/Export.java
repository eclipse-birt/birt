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
 * Export.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Export")
@XmlAccessorType(XmlAccessType.NONE)
public class Export implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "AvailableColumns")
	private org.eclipse.birt.report.soapengine.api.Vector availableColumns;
	@XmlElement(name = "SelectedColumn")
	private org.eclipse.birt.report.soapengine.api.Vector selectedColumn;
	@XmlElement(name = "Criteria")
	private org.eclipse.birt.report.soapengine.api.ExportCriteria[] criteria;

	public Export() {
	}

	public Export(org.eclipse.birt.report.soapengine.api.Vector availableColumns,
			org.eclipse.birt.report.soapengine.api.Vector selectedColumn,
			org.eclipse.birt.report.soapengine.api.ExportCriteria[] criteria) {
		this.availableColumns = availableColumns;
		this.selectedColumn = selectedColumn;
		this.criteria = criteria;
	}

	/**
	 * Gets the availableColumns value for this Export.
	 *
	 * @return availableColumns
	 */
	public org.eclipse.birt.report.soapengine.api.Vector getAvailableColumns() {
		return availableColumns;
	}

	/**
	 * Sets the availableColumns value for this Export.
	 *
	 * @param availableColumns
	 */
	public void setAvailableColumns(org.eclipse.birt.report.soapengine.api.Vector availableColumns) {
		this.availableColumns = availableColumns;
	}

	/**
	 * Gets the selectedColumn value for this Export.
	 *
	 * @return selectedColumn
	 */
	public org.eclipse.birt.report.soapengine.api.Vector getSelectedColumn() {
		return selectedColumn;
	}

	/**
	 * Sets the selectedColumn value for this Export.
	 *
	 * @param selectedColumn
	 */
	public void setSelectedColumn(org.eclipse.birt.report.soapengine.api.Vector selectedColumn) {
		this.selectedColumn = selectedColumn;
	}

	/**
	 * Gets the criteria value for this Export.
	 *
	 * @return criteria
	 */
	public org.eclipse.birt.report.soapengine.api.ExportCriteria[] getCriteria() {
		return criteria;
	}

	/**
	 * Sets the criteria value for this Export.
	 *
	 * @param criteria
	 */
	public void setCriteria(org.eclipse.birt.report.soapengine.api.ExportCriteria[] criteria) {
		this.criteria = criteria;
	}

	public org.eclipse.birt.report.soapengine.api.ExportCriteria getCriteria(int i) {
		return this.criteria[i];
	}

	public void setCriteria(int i, org.eclipse.birt.report.soapengine.api.ExportCriteria _value) {
		this.criteria[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof Export)) {
			return false;
		}
		Export other = (Export) obj;
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
				&& ((this.availableColumns == null && other.getAvailableColumns() == null)
						|| (this.availableColumns != null && this.availableColumns.equals(other.getAvailableColumns())))
				&& ((this.selectedColumn == null && other.getSelectedColumn() == null)
						|| (this.selectedColumn != null && this.selectedColumn.equals(other.getSelectedColumn())))
				&& ((this.criteria == null && other.getCriteria() == null)
						|| (this.criteria != null && java.util.Arrays.equals(this.criteria, other.getCriteria())));
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
		if (getAvailableColumns() != null) {
			_hashCode += getAvailableColumns().hashCode();
		}
		if (getSelectedColumn() != null) {
			_hashCode += getSelectedColumn().hashCode();
		}
		if (getCriteria() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getCriteria()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getCriteria(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

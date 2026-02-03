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
 * ReportElement.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ReportElement")
@XmlAccessorType(XmlAccessType.NONE)
public class ReportElement implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "Id")
	private long id;
	@XmlElement(name = "Visible")
	private boolean visible;
	@XmlElement(name = "Name")
	private java.lang.String name;
	@XmlElement(name = "Description")
	private java.lang.String description;
	@XmlElement(name = "DataSetUsed")
	private java.lang.Long dataSetUsed;

	public ReportElement() {
	}

	public ReportElement(long id, boolean visible, java.lang.String name, java.lang.String description,
			java.lang.Long dataSetUsed) {
		this.id = id;
		this.visible = visible;
		this.name = name;
		this.description = description;
		this.dataSetUsed = dataSetUsed;
	}

	/**
	 * Gets the id value for this ReportElement.
	 *
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id value for this ReportElement.
	 *
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the visible value for this ReportElement.
	 *
	 * @return visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets the visible value for this ReportElement.
	 *
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Gets the name value for this ReportElement.
	 *
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this ReportElement.
	 *
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the description value for this ReportElement.
	 *
	 * @return description
	 */
	public java.lang.String getDescription() {
		return description;
	}

	/**
	 * Sets the description value for this ReportElement.
	 *
	 * @param description
	 */
	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	/**
	 * Gets the dataSetUsed value for this ReportElement.
	 *
	 * @return dataSetUsed
	 */
	public java.lang.Long getDataSetUsed() {
		return dataSetUsed;
	}

	/**
	 * Sets the dataSetUsed value for this ReportElement.
	 *
	 * @param dataSetUsed
	 */
	public void setDataSetUsed(java.lang.Long dataSetUsed) {
		this.dataSetUsed = dataSetUsed;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ReportElement)) {
			return false;
		}
		ReportElement other = (ReportElement) obj;
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
		_equals = true && this.id == other.getId() && this.visible == other.isVisible()
				&& ((this.name == null && other.getName() == null)
						|| (this.name != null && this.name.equals(other.getName())))
				&& ((this.description == null && other.getDescription() == null)
						|| (this.description != null && this.description.equals(other.getDescription())))
				&& ((this.dataSetUsed == null && other.getDataSetUsed() == null)
						|| (this.dataSetUsed != null && this.dataSetUsed.equals(other.getDataSetUsed())));
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
		_hashCode += (isVisible() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		if (getName() != null) {
			_hashCode += getName().hashCode();
		}
		if (getDescription() != null) {
			_hashCode += getDescription().hashCode();
		}
		if (getDataSetUsed() != null) {
			_hashCode += getDataSetUsed().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

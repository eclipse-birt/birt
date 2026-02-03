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
 * DataSetDefinition.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DataSetDefinition")
@XmlAccessorType(XmlAccessType.NONE)
public class DataSetDefinition implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "Name")
	private java.lang.String name;
	@XmlElement(name = "Description")
	private java.lang.String description;
	@XmlElement(name = "Folder")
	private org.eclipse.birt.report.soapengine.api.DataFieldFolder[] folder;
	@XmlElement(name = "Field")
	private org.eclipse.birt.report.soapengine.api.DataField[] field;

	public DataSetDefinition() {
	}

	public DataSetDefinition(java.lang.String name, java.lang.String description,
			org.eclipse.birt.report.soapengine.api.DataFieldFolder[] folder,
			org.eclipse.birt.report.soapengine.api.DataField[] field) {
		this.name = name;
		this.description = description;
		this.folder = folder;
		this.field = field;
	}

	/**
	 * Gets the name value for this DataSetDefinition.
	 *
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this DataSetDefinition.
	 *
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the description value for this DataSetDefinition.
	 *
	 * @return description
	 */
	public java.lang.String getDescription() {
		return description;
	}

	/**
	 * Sets the description value for this DataSetDefinition.
	 *
	 * @param description
	 */
	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	/**
	 * Gets the folder value for this DataSetDefinition.
	 *
	 * @return folder
	 */
	public org.eclipse.birt.report.soapengine.api.DataFieldFolder[] getFolder() {
		return folder;
	}

	/**
	 * Sets the folder value for this DataSetDefinition.
	 *
	 * @param folder
	 */
	public void setFolder(org.eclipse.birt.report.soapengine.api.DataFieldFolder[] folder) {
		this.folder = folder;
	}

	public org.eclipse.birt.report.soapengine.api.DataFieldFolder getFolder(int i) {
		return this.folder[i];
	}

	public void setFolder(int i, org.eclipse.birt.report.soapengine.api.DataFieldFolder _value) {
		this.folder[i] = _value;
	}

	/**
	 * Gets the field value for this DataSetDefinition.
	 *
	 * @return field
	 */
	public org.eclipse.birt.report.soapengine.api.DataField[] getField() {
		return field;
	}

	/**
	 * Sets the field value for this DataSetDefinition.
	 *
	 * @param field
	 */
	public void setField(org.eclipse.birt.report.soapengine.api.DataField[] field) {
		this.field = field;
	}

	public org.eclipse.birt.report.soapengine.api.DataField getField(int i) {
		return this.field[i];
	}

	public void setField(int i, org.eclipse.birt.report.soapengine.api.DataField _value) {
		this.field[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof DataSetDefinition)) {
			return false;
		}
		DataSetDefinition other = (DataSetDefinition) obj;
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
				&& ((this.name == null && other.getName() == null)
						|| (this.name != null && this.name.equals(other.getName())))
				&& ((this.description == null && other.getDescription() == null)
						|| (this.description != null && this.description.equals(other.getDescription())))
				&& ((this.folder == null && other.getFolder() == null)
						|| (this.folder != null && java.util.Arrays.equals(this.folder, other.getFolder())))
				&& ((this.field == null && other.getField() == null)
						|| (this.field != null && java.util.Arrays.equals(this.field, other.getField())));
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
		if (getName() != null) {
			_hashCode += getName().hashCode();
		}
		if (getDescription() != null) {
			_hashCode += getDescription().hashCode();
		}
		if (getFolder() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getFolder()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getFolder(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		if (getField() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getField()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getField(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

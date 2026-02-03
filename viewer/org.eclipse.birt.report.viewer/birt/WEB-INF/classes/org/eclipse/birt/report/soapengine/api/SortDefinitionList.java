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
 * SortDefinitionList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SortDefinitionList")
@XmlAccessorType(XmlAccessType.NONE)
public class SortDefinitionList implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "SortDefinition")
	private org.eclipse.birt.report.soapengine.api.SortDefinition[] sortDefinition;

	public SortDefinitionList() {
	}

	public SortDefinitionList(org.eclipse.birt.report.soapengine.api.SortDefinition[] sortDefinition) {
		this.sortDefinition = sortDefinition;
	}

	/**
	 * Gets the sortDefinition value for this SortDefinitionList.
	 *
	 * @return sortDefinition
	 */
	public org.eclipse.birt.report.soapengine.api.SortDefinition[] getSortDefinition() {
		return sortDefinition;
	}

	/**
	 * Sets the sortDefinition value for this SortDefinitionList.
	 *
	 * @param sortDefinition
	 */
	public void setSortDefinition(org.eclipse.birt.report.soapengine.api.SortDefinition[] sortDefinition) {
		this.sortDefinition = sortDefinition;
	}

	public org.eclipse.birt.report.soapengine.api.SortDefinition getSortDefinition(int i) {
		return this.sortDefinition[i];
	}

	public void setSortDefinition(int i, org.eclipse.birt.report.soapengine.api.SortDefinition _value) {
		this.sortDefinition[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof SortDefinitionList)) {
			return false;
		}
		SortDefinitionList other = (SortDefinitionList) obj;
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
				&& ((this.sortDefinition == null && other.getSortDefinition() == null) || (this.sortDefinition != null
						&& java.util.Arrays.equals(this.sortDefinition, other.getSortDefinition())));
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
		if (getSortDefinition() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getSortDefinition()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getSortDefinition(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

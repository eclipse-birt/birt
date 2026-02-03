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
 * ReportParameter.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ReportParameter")
@XmlAccessorType(XmlAccessType.NONE)
public class ReportParameter implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "name")
	private java.lang.String name;
	@XmlElement(name = "promptString")
	private java.lang.String promptString;
	@XmlElement(name = "defaultValue")
	private java.lang.String defaultValue;

	public ReportParameter() {
	}

	public ReportParameter(java.lang.String name, java.lang.String promptString, java.lang.String defaultValue) {
		this.name = name;
		this.promptString = promptString;
		this.defaultValue = defaultValue;
	}

	/**
	 * Gets the name value for this ReportParameter.
	 *
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this ReportParameter.
	 *
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the promptString value for this ReportParameter.
	 *
	 * @return promptString
	 */
	public java.lang.String getPromptString() {
		return promptString;
	}

	/**
	 * Sets the promptString value for this ReportParameter.
	 *
	 * @param promptString
	 */
	public void setPromptString(java.lang.String promptString) {
		this.promptString = promptString;
	}

	/**
	 * Gets the defaultValue value for this ReportParameter.
	 *
	 * @return defaultValue
	 */
	public java.lang.String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Sets the defaultValue value for this ReportParameter.
	 *
	 * @param defaultValue
	 */
	public void setDefaultValue(java.lang.String defaultValue) {
		this.defaultValue = defaultValue;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ReportParameter)) {
			return false;
		}
		ReportParameter other = (ReportParameter) obj;
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
				&& ((this.promptString == null && other.getPromptString() == null)
						|| (this.promptString != null && this.promptString.equals(other.getPromptString())))
				&& ((this.defaultValue == null && other.getDefaultValue() == null)
						|| (this.defaultValue != null && this.defaultValue.equals(other.getDefaultValue())));
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
		if (getPromptString() != null) {
			_hashCode += getPromptString().hashCode();
		}
		if (getDefaultValue() != null) {
			_hashCode += getDefaultValue().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

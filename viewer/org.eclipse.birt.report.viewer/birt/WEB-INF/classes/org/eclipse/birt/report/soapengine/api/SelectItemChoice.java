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
 * SelectItemChoice.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import org.eclipse.birt.report.IBirtConstants;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SelectItemChoice")
@XmlAccessorType(XmlAccessType.NONE)
public class SelectItemChoice implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "value")
	private java.lang.String value;
	@XmlElement(name = "label")
	private java.lang.String label;

	public static final SelectItemChoice NULL_VALUE = new SelectItemChoice(IBirtConstants.NULL_VALUE,
			IBirtConstants.NULL_VALUE_DISPLAY);
	public static final SelectItemChoice EMPTY_VALUE = new SelectItemChoice("", ""); //$NON-NLS-1$//$NON-NLS-2$

	public SelectItemChoice() {
	}

	public SelectItemChoice(java.lang.String value, java.lang.String label) {
		this.value = value;
		this.label = label;
	}

	/**
	 * Gets the value value for this SelectItemChoice.
	 *
	 * @return value
	 */
	public java.lang.String getValue() {
		return value;
	}

	/**
	 * Sets the value value for this SelectItemChoice.
	 *
	 * @param value
	 */
	public void setValue(java.lang.String value) {
		this.value = value;
	}

	/**
	 * Gets the label value for this SelectItemChoice.
	 *
	 * @return label
	 */
	public java.lang.String getLabel() {
		return label;
	}

	/**
	 * Sets the label value for this SelectItemChoice.
	 *
	 * @param label
	 */
	public void setLabel(java.lang.String label) {
		this.label = label;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof SelectItemChoice)) {
			return false;
		}
		SelectItemChoice other = (SelectItemChoice) obj;
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
				&& ((this.value == null && other.getValue() == null)
						|| (this.value != null && this.value.equals(other.getValue())))
				&& ((this.label == null && other.getLabel() == null)
						|| (this.label != null && this.label.equals(other.getLabel())));
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
		if (getValue() != null) {
			_hashCode += getValue().hashCode();
		}
		if (getLabel() != null) {
			_hashCode += getLabel().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

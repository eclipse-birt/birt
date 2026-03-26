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
 * NumberCategoryChoiceList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "NumberCategoryChoiceList")
@XmlAccessorType(XmlAccessType.NONE)
public class NumberCategoryChoiceList implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "NumberCategoryChoice")
	private org.eclipse.birt.report.soapengine.api.NumberCategoryChoice[] numberCategoryChoice;

	public NumberCategoryChoiceList() {
	}

	public NumberCategoryChoiceList(
			org.eclipse.birt.report.soapengine.api.NumberCategoryChoice[] numberCategoryChoice) {
		this.numberCategoryChoice = numberCategoryChoice;
	}

	/**
	 * Gets the numberCategoryChoice value for this NumberCategoryChoiceList.
	 *
	 * @return numberCategoryChoice
	 */
	public org.eclipse.birt.report.soapengine.api.NumberCategoryChoice[] getNumberCategoryChoice() {
		return numberCategoryChoice;
	}

	/**
	 * Sets the numberCategoryChoice value for this NumberCategoryChoiceList.
	 *
	 * @param numberCategoryChoice
	 */
	public void setNumberCategoryChoice(
			org.eclipse.birt.report.soapengine.api.NumberCategoryChoice[] numberCategoryChoice) {
		this.numberCategoryChoice = numberCategoryChoice;
	}

	public org.eclipse.birt.report.soapengine.api.NumberCategoryChoice getNumberCategoryChoice(int i) {
		return this.numberCategoryChoice[i];
	}

	public void setNumberCategoryChoice(int i, org.eclipse.birt.report.soapengine.api.NumberCategoryChoice _value) {
		this.numberCategoryChoice[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof NumberCategoryChoiceList)) {
			return false;
		}
		NumberCategoryChoiceList other = (NumberCategoryChoiceList) obj;
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
		_equals = true && ((this.numberCategoryChoice == null && other.getNumberCategoryChoice() == null)
				|| (this.numberCategoryChoice != null
						&& java.util.Arrays.equals(this.numberCategoryChoice, other.getNumberCategoryChoice())));
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
		if (getNumberCategoryChoice() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getNumberCategoryChoice()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getNumberCategoryChoice(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

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
 * Operation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Operation")
@XmlAccessorType(XmlAccessType.NONE)
public class Operation implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "Target")
	private org.eclipse.birt.report.soapengine.api.ReportId target;
	@XmlElement(name = "Operator")
	private java.lang.String operator;
	@XmlElement(name = "Oprand")
	private org.eclipse.birt.report.soapengine.api.Oprand[] oprand;
	@XmlElement(name = "Data")
	private org.eclipse.birt.report.soapengine.api.Data data;

	public Operation() {
	}

	public Operation(org.eclipse.birt.report.soapengine.api.ReportId target, java.lang.String operator,
			org.eclipse.birt.report.soapengine.api.Oprand[] oprand, org.eclipse.birt.report.soapengine.api.Data data) {
		this.target = target;
		this.operator = operator;
		this.oprand = oprand;
		this.data = data;
	}

	/**
	 * Gets the target value for this Operation.
	 *
	 * @return target
	 */
	public org.eclipse.birt.report.soapengine.api.ReportId getTarget() {
		return target;
	}

	/**
	 * Sets the target value for this Operation.
	 *
	 * @param target
	 */
	public void setTarget(org.eclipse.birt.report.soapengine.api.ReportId target) {
		this.target = target;
	}

	/**
	 * Gets the operator value for this Operation.
	 *
	 * @return operator
	 */
	public java.lang.String getOperator() {
		return operator;
	}

	/**
	 * Sets the operator value for this Operation.
	 *
	 * @param operator
	 */
	public void setOperator(java.lang.String operator) {
		this.operator = operator;
	}

	/**
	 * Gets the oprand value for this Operation.
	 *
	 * @return oprand
	 */
	public org.eclipse.birt.report.soapengine.api.Oprand[] getOprand() {
		return oprand;
	}

	/**
	 * Sets the oprand value for this Operation.
	 *
	 * @param oprand
	 */
	public void setOprand(org.eclipse.birt.report.soapengine.api.Oprand[] oprand) {
		this.oprand = oprand;
	}

	public org.eclipse.birt.report.soapengine.api.Oprand getOprand(int i) {
		return this.oprand[i];
	}

	public void setOprand(int i, org.eclipse.birt.report.soapengine.api.Oprand _value) {
		this.oprand[i] = _value;
	}

	/**
	 * Gets the data value for this Operation.
	 *
	 * @return data
	 */
	public org.eclipse.birt.report.soapengine.api.Data getData() {
		return data;
	}

	/**
	 * Sets the data value for this Operation.
	 *
	 * @param data
	 */
	public void setData(org.eclipse.birt.report.soapengine.api.Data data) {
		this.data = data;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof Operation)) {
			return false;
		}
		Operation other = (Operation) obj;
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
				&& ((this.target == null && other.getTarget() == null)
						|| (this.target != null && this.target.equals(other.getTarget())))
				&& ((this.operator == null && other.getOperator() == null)
						|| (this.operator != null && this.operator.equals(other.getOperator())))
				&& ((this.oprand == null && other.getOprand() == null)
						|| (this.oprand != null && java.util.Arrays.equals(this.oprand, other.getOprand())))
				&& ((this.data == null && other.getData() == null)
						|| (this.data != null && this.data.equals(other.getData())));
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
		if (getTarget() != null) {
			_hashCode += getTarget().hashCode();
		}
		if (getOperator() != null) {
			_hashCode += getOperator().hashCode();
		}
		if (getOprand() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getOprand()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getOprand(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		if (getData() != null) {
			_hashCode += getData().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

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
 * DesignState.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DesignState")
@XmlAccessorType(XmlAccessType.NONE)
public class DesignState implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "isBlank")
	private java.lang.Boolean isBlank;
	@XmlElement(name = "editable")
	private java.lang.Boolean editable;
	/** can create a new report */
	@XmlElement(name = "canNew")
	private java.lang.Boolean canNew;
	/** report name */
	@XmlElement(name = "rptName")
	private java.lang.String rptName;

	public DesignState() {
	}

	public DesignState(java.lang.Boolean isBlank, java.lang.Boolean editable, java.lang.Boolean canNew,
			java.lang.String rptName) {
		this.isBlank = isBlank;
		this.editable = editable;
		this.canNew = canNew;
		this.rptName = rptName;
	}

	/**
	 * Gets the isBlank value for this DesignState.
	 *
	 * @return isBlank
	 */
	public java.lang.Boolean getIsBlank() {
		return isBlank;
	}

	/**
	 * Sets the isBlank value for this DesignState.
	 *
	 * @param isBlank
	 */
	public void setIsBlank(java.lang.Boolean isBlank) {
		this.isBlank = isBlank;
	}

	/**
	 * Gets the editable value for this DesignState.
	 *
	 * @return editable
	 */
	public java.lang.Boolean getEditable() {
		return editable;
	}

	/**
	 * Sets the editable value for this DesignState.
	 *
	 * @param editable
	 */
	public void setEditable(java.lang.Boolean editable) {
		this.editable = editable;
	}

	/**
	 * Gets the canNew value for this DesignState.
	 *
	 * @return canNew can create a new report
	 */
	public java.lang.Boolean getCanNew() {
		return canNew;
	}

	/**
	 * Sets the canNew value for this DesignState.
	 *
	 * @param canNew can create a new report
	 */
	public void setCanNew(java.lang.Boolean canNew) {
		this.canNew = canNew;
	}

	/**
	 * Gets the rptName value for this DesignState.
	 *
	 * @return rptName report name
	 */
	public java.lang.String getRptName() {
		return rptName;
	}

	/**
	 * Sets the rptName value for this DesignState.
	 *
	 * @param rptName report name
	 */
	public void setRptName(java.lang.String rptName) {
		this.rptName = rptName;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof DesignState)) {
			return false;
		}
		DesignState other = (DesignState) obj;
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
				&& ((this.isBlank == null && other.getIsBlank() == null)
						|| (this.isBlank != null && this.isBlank.equals(other.getIsBlank())))
				&& ((this.editable == null && other.getEditable() == null)
						|| (this.editable != null && this.editable.equals(other.getEditable())))
				&& ((this.canNew == null && other.getCanNew() == null)
						|| (this.canNew != null && this.canNew.equals(other.getCanNew())))
				&& ((this.rptName == null && other.getRptName() == null)
						|| (this.rptName != null && this.rptName.equals(other.getRptName())));
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
		if (getIsBlank() != null) {
			_hashCode += getIsBlank().hashCode();
		}
		if (getEditable() != null) {
			_hashCode += getEditable().hashCode();
		}
		if (getCanNew() != null) {
			_hashCode += getCanNew().hashCode();
		}
		if (getRptName() != null) {
			_hashCode += getRptName().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

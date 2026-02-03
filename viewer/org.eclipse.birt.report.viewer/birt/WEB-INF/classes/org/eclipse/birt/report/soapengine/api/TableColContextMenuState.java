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
 * TableColContextMenuState.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TableColContextMenuState")
@XmlAccessorType(XmlAccessType.NONE)
public class TableColContextMenuState implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/** Can set NoRepeatValue in a table column */
	@XmlElement(name = "canNoRepeatValue")
	private boolean canNoRepeatValue;
	/** Can unset NoRepeatValue in a table column */
	@XmlElement(name = "canRepeatValue")
	private boolean canRepeatValue;
	/** Can expand a group column */
	@XmlElement(name = "canExpand")
	private boolean canExpand;
	/** Can collapse a group column */
	@XmlElement(name = "canCollapse")
	private boolean canCollapse;

	public TableColContextMenuState() {
	}

	public TableColContextMenuState(boolean canNoRepeatValue, boolean canRepeatValue, boolean canExpand,
			boolean canCollapse) {
		this.canNoRepeatValue = canNoRepeatValue;
		this.canRepeatValue = canRepeatValue;
		this.canExpand = canExpand;
		this.canCollapse = canCollapse;
	}

	/**
	 * Gets the canNoRepeatValue value for this TableColContextMenuState.
	 *
	 * @return canNoRepeatValue Can set NoRepeatValue in a table column
	 */
	public boolean isCanNoRepeatValue() {
		return canNoRepeatValue;
	}

	/**
	 * Sets the canNoRepeatValue value for this TableColContextMenuState.
	 *
	 * @param canNoRepeatValue Can set NoRepeatValue in a table column
	 */
	public void setCanNoRepeatValue(boolean canNoRepeatValue) {
		this.canNoRepeatValue = canNoRepeatValue;
	}

	/**
	 * Gets the canRepeatValue value for this TableColContextMenuState.
	 *
	 * @return canRepeatValue Can unset NoRepeatValue in a table column
	 */
	public boolean isCanRepeatValue() {
		return canRepeatValue;
	}

	/**
	 * Sets the canRepeatValue value for this TableColContextMenuState.
	 *
	 * @param canRepeatValue Can unset NoRepeatValue in a table column
	 */
	public void setCanRepeatValue(boolean canRepeatValue) {
		this.canRepeatValue = canRepeatValue;
	}

	/**
	 * Gets the canExpand value for this TableColContextMenuState.
	 *
	 * @return canExpand Can expand a group column
	 */
	public boolean isCanExpand() {
		return canExpand;
	}

	/**
	 * Sets the canExpand value for this TableColContextMenuState.
	 *
	 * @param canExpand Can expand a group column
	 */
	public void setCanExpand(boolean canExpand) {
		this.canExpand = canExpand;
	}

	/**
	 * Gets the canCollapse value for this TableColContextMenuState.
	 *
	 * @return canCollapse Can collapse a group column
	 */
	public boolean isCanCollapse() {
		return canCollapse;
	}

	/**
	 * Sets the canCollapse value for this TableColContextMenuState.
	 *
	 * @param canCollapse Can collapse a group column
	 */
	public void setCanCollapse(boolean canCollapse) {
		this.canCollapse = canCollapse;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof TableColContextMenuState)) {
			return false;
		}
		TableColContextMenuState other = (TableColContextMenuState) obj;
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
		_equals = true && this.canNoRepeatValue == other.isCanNoRepeatValue()
				&& this.canRepeatValue == other.isCanRepeatValue() && this.canExpand == other.isCanExpand()
				&& this.canCollapse == other.isCanCollapse();
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
		_hashCode += (isCanNoRepeatValue() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isCanRepeatValue() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isCanExpand() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isCanCollapse() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

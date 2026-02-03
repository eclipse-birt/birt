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
 * ToolbarState.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ToolbarState")
@XmlAccessorType(XmlAccessType.NONE)
public class ToolbarState implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/** can create a new report */
	@XmlElement(name = "canNew")
	private boolean canNew;
	@XmlElement(name = "canUndo")
	private boolean canUndo;
	@XmlElement(name = "canRedo")
	private boolean canRedo;
	/** where to save the design is known */
	@XmlElement(name = "canSave")
	private boolean canSave;
	/** the design is dirty */
	@XmlElement(name = "isDirty")
	private boolean isDirty;

	public ToolbarState() {
	}

	public ToolbarState(boolean canNew, boolean canUndo, boolean canRedo, boolean canSave, boolean isDirty) {
		this.canNew = canNew;
		this.canUndo = canUndo;
		this.canRedo = canRedo;
		this.canSave = canSave;
		this.isDirty = isDirty;
	}

	/**
	 * Gets the canNew value for this ToolbarState.
	 *
	 * @return canNew can create a new report
	 */
	public boolean isCanNew() {
		return canNew;
	}

	/**
	 * Sets the canNew value for this ToolbarState.
	 *
	 * @param canNew can create a new report
	 */
	public void setCanNew(boolean canNew) {
		this.canNew = canNew;
	}

	/**
	 * Gets the canUndo value for this ToolbarState.
	 *
	 * @return canUndo
	 */
	public boolean isCanUndo() {
		return canUndo;
	}

	/**
	 * Sets the canUndo value for this ToolbarState.
	 *
	 * @param canUndo
	 */
	public void setCanUndo(boolean canUndo) {
		this.canUndo = canUndo;
	}

	/**
	 * Gets the canRedo value for this ToolbarState.
	 *
	 * @return canRedo
	 */
	public boolean isCanRedo() {
		return canRedo;
	}

	/**
	 * Sets the canRedo value for this ToolbarState.
	 *
	 * @param canRedo
	 */
	public void setCanRedo(boolean canRedo) {
		this.canRedo = canRedo;
	}

	/**
	 * Gets the canSave value for this ToolbarState.
	 *
	 * @return canSave where to save the design is known
	 */
	public boolean isCanSave() {
		return canSave;
	}

	/**
	 * Sets the canSave value for this ToolbarState.
	 *
	 * @param canSave where to save the design is known
	 */
	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}

	/**
	 * Gets the isDirty value for this ToolbarState.
	 *
	 * @return isDirty the design is dirty
	 */
	public boolean isIsDirty() {
		return isDirty;
	}

	/**
	 * Sets the isDirty value for this ToolbarState.
	 *
	 * @param isDirty the design is dirty
	 */
	public void setIsDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ToolbarState)) {
			return false;
		}
		ToolbarState other = (ToolbarState) obj;
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
		_equals = true && this.canNew == other.isCanNew() && this.canUndo == other.isCanUndo()
				&& this.canRedo == other.isCanRedo() && this.canSave == other.isCanSave()
				&& this.isDirty == other.isIsDirty();
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
		_hashCode += (isCanNew() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isCanUndo() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isCanRedo() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isCanSave() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isIsDirty() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

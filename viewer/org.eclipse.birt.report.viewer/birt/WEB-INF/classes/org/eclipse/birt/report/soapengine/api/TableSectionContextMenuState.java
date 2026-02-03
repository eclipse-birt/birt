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
 * TableSectionContextMenuState.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TableSectionContextMenuState")
@XmlAccessorType(XmlAccessType.NONE)
public class TableSectionContextMenuState implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/** Can insert a chart in this section */
	@XmlElement(name = "canAddChart")
	private boolean canAddChart;
	/** Can edit a chart in this section */
	@XmlElement(name = "canEditChart")
	private boolean canEditChart;
	/** Can delete a chart in this section */
	@XmlElement(name = "canDeleteChart")
	private boolean canDeleteChart;
	/** Can expand a section */
	@XmlElement(name = "canExpand")
	private boolean canExpand;
	/** Can collapse a section */
	@XmlElement(name = "canCollapse")
	private boolean canCollapse;

	public TableSectionContextMenuState() {
	}

	public TableSectionContextMenuState(boolean canAddChart, boolean canEditChart, boolean canDeleteChart,
			boolean canExpand, boolean canCollapse) {
		this.canAddChart = canAddChart;
		this.canEditChart = canEditChart;
		this.canDeleteChart = canDeleteChart;
		this.canExpand = canExpand;
		this.canCollapse = canCollapse;
	}

	/**
	 * Gets the canAddChart value for this TableSectionContextMenuState.
	 *
	 * @return canAddChart Can insert a chart in this section
	 */
	public boolean isCanAddChart() {
		return canAddChart;
	}

	/**
	 * Sets the canAddChart value for this TableSectionContextMenuState.
	 *
	 * @param canAddChart Can insert a chart in this section
	 */
	public void setCanAddChart(boolean canAddChart) {
		this.canAddChart = canAddChart;
	}

	/**
	 * Gets the canEditChart value for this TableSectionContextMenuState.
	 *
	 * @return canEditChart Can edit a chart in this section
	 */
	public boolean isCanEditChart() {
		return canEditChart;
	}

	/**
	 * Sets the canEditChart value for this TableSectionContextMenuState.
	 *
	 * @param canEditChart Can edit a chart in this section
	 */
	public void setCanEditChart(boolean canEditChart) {
		this.canEditChart = canEditChart;
	}

	/**
	 * Gets the canDeleteChart value for this TableSectionContextMenuState.
	 *
	 * @return canDeleteChart Can delete a chart in this section
	 */
	public boolean isCanDeleteChart() {
		return canDeleteChart;
	}

	/**
	 * Sets the canDeleteChart value for this TableSectionContextMenuState.
	 *
	 * @param canDeleteChart Can delete a chart in this section
	 */
	public void setCanDeleteChart(boolean canDeleteChart) {
		this.canDeleteChart = canDeleteChart;
	}

	/**
	 * Gets the canExpand value for this TableSectionContextMenuState.
	 *
	 * @return canExpand Can expand a section
	 */
	public boolean isCanExpand() {
		return canExpand;
	}

	/**
	 * Sets the canExpand value for this TableSectionContextMenuState.
	 *
	 * @param canExpand Can expand a section
	 */
	public void setCanExpand(boolean canExpand) {
		this.canExpand = canExpand;
	}

	/**
	 * Gets the canCollapse value for this TableSectionContextMenuState.
	 *
	 * @return canCollapse Can collapse a section
	 */
	public boolean isCanCollapse() {
		return canCollapse;
	}

	/**
	 * Sets the canCollapse value for this TableSectionContextMenuState.
	 *
	 * @param canCollapse Can collapse a section
	 */
	public void setCanCollapse(boolean canCollapse) {
		this.canCollapse = canCollapse;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof TableSectionContextMenuState)) {
			return false;
		}
		TableSectionContextMenuState other = (TableSectionContextMenuState) obj;
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
		_equals = true && this.canAddChart == other.isCanAddChart() && this.canEditChart == other.isCanEditChart()
				&& this.canDeleteChart == other.isCanDeleteChart() && this.canExpand == other.isCanExpand()
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
		_hashCode += (isCanAddChart() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isCanEditChart() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isCanDeleteChart() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isCanExpand() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isCanCollapse() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

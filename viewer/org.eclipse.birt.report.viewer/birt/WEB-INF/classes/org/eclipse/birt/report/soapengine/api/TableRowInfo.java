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
 * TableRowInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * information of a table row
 */
@XmlRootElement(name = "TableRowInfo")
@XmlAccessorType(XmlAccessType.NONE)
public class TableRowInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * The group level of this row. -1 is used to indicated table level rows.
	 */
	@XmlElement(name = "level")
	private int level;
	/**
	 * The position of this row in the group header or group footer. Index starts
	 * from 0.
	 */
	@XmlElement(name = "index")
	private int index;
	/** Whether the row is in header or in footer */
	@XmlElement(name = "isHeader")
	private boolean isHeader;

	public TableRowInfo() {
	}

	public TableRowInfo(int level, int index, boolean isHeader) {
		this.level = level;
		this.index = index;
		this.isHeader = isHeader;
	}

	/**
	 * Gets the level value for this TableRowInfo.
	 *
	 * @return level The group level of this row. -1 is used to indicated table
	 *         level rows.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Sets the level value for this TableRowInfo.
	 *
	 * @param level The group level of this row. -1 is used to indicated table level
	 *              rows.
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Gets the index value for this TableRowInfo.
	 *
	 * @return index The position of this row in the group header or group footer.
	 *         Index starts from 0.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the index value for this TableRowInfo.
	 *
	 * @param index The position of this row in the group header or group footer.
	 *              Index starts from 0.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Gets the isHeader value for this TableRowInfo.
	 *
	 * @return isHeader Whether the row is in header or in footer
	 */
	public boolean isIsHeader() {
		return isHeader;
	}

	/**
	 * Sets the isHeader value for this TableRowInfo.
	 *
	 * @param isHeader Whether the row is in header or in footer
	 */
	public void setIsHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof TableRowInfo)) {
			return false;
		}
		TableRowInfo other = (TableRowInfo) obj;
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
		_equals = true && this.level == other.getLevel() && this.index == other.getIndex()
				&& this.isHeader == other.isIsHeader();
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
		_hashCode += getLevel();
		_hashCode += getIndex();
		_hashCode += (isIsHeader() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

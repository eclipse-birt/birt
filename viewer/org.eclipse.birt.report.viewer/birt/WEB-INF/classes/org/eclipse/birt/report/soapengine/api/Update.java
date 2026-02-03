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
 * Update.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Update")
@XmlAccessorType(XmlAccessType.NONE)
public class Update implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "UpdateContent")
	private org.eclipse.birt.report.soapengine.api.UpdateContent updateContent;
	@XmlElement(name = "UpdateDialog")
	private org.eclipse.birt.report.soapengine.api.UpdateDialog updateDialog;
	@XmlElement(name = "UpdateData")
	private org.eclipse.birt.report.soapengine.api.UpdateData updateData;

	public Update() {
	}

	public Update(org.eclipse.birt.report.soapengine.api.UpdateContent updateContent,
			org.eclipse.birt.report.soapengine.api.UpdateDialog updateDialog,
			org.eclipse.birt.report.soapengine.api.UpdateData updateData) {
		this.updateContent = updateContent;
		this.updateDialog = updateDialog;
		this.updateData = updateData;
	}

	/**
	 * Gets the updateContent value for this Update.
	 *
	 * @return updateContent
	 */
	public org.eclipse.birt.report.soapengine.api.UpdateContent getUpdateContent() {
		return updateContent;
	}

	/**
	 * Sets the updateContent value for this Update.
	 *
	 * @param updateContent
	 */
	public void setUpdateContent(org.eclipse.birt.report.soapengine.api.UpdateContent updateContent) {
		this.updateContent = updateContent;
	}

	/**
	 * Gets the updateDialog value for this Update.
	 *
	 * @return updateDialog
	 */
	public org.eclipse.birt.report.soapengine.api.UpdateDialog getUpdateDialog() {
		return updateDialog;
	}

	/**
	 * Sets the updateDialog value for this Update.
	 *
	 * @param updateDialog
	 */
	public void setUpdateDialog(org.eclipse.birt.report.soapengine.api.UpdateDialog updateDialog) {
		this.updateDialog = updateDialog;
	}

	/**
	 * Gets the updateData value for this Update.
	 *
	 * @return updateData
	 */
	public org.eclipse.birt.report.soapengine.api.UpdateData getUpdateData() {
		return updateData;
	}

	/**
	 * Sets the updateData value for this Update.
	 *
	 * @param updateData
	 */
	public void setUpdateData(org.eclipse.birt.report.soapengine.api.UpdateData updateData) {
		this.updateData = updateData;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof Update)) {
			return false;
		}
		Update other = (Update) obj;
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
				&& ((this.updateContent == null && other.getUpdateContent() == null)
						|| (this.updateContent != null && this.updateContent.equals(other.getUpdateContent())))
				&& ((this.updateDialog == null && other.getUpdateDialog() == null)
						|| (this.updateDialog != null && this.updateDialog.equals(other.getUpdateDialog())))
				&& ((this.updateData == null && other.getUpdateData() == null)
						|| (this.updateData != null && this.updateData.equals(other.getUpdateData())));
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
		if (getUpdateContent() != null) {
			_hashCode += getUpdateContent().hashCode();
		}
		if (getUpdateDialog() != null) {
			_hashCode += getUpdateDialog().hashCode();
		}
		if (getUpdateData() != null) {
			_hashCode += getUpdateData().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

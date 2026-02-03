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
 * UpdateDialog.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UpdateDialog")
@XmlAccessorType(XmlAccessType.NONE)
public class UpdateDialog implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "dialogId")
	private java.lang.String dialogId;
	@XmlElement(name = "content")
	private java.lang.String content;

	public UpdateDialog() {
	}

	public UpdateDialog(java.lang.String dialogId, java.lang.String content) {
		this.dialogId = dialogId;
		this.content = content;
	}

	/**
	 * Gets the dialogId value for this UpdateDialog.
	 *
	 * @return dialogId
	 */
	public java.lang.String getDialogId() {
		return dialogId;
	}

	/**
	 * Sets the dialogId value for this UpdateDialog.
	 *
	 * @param dialogId
	 */
	public void setDialogId(java.lang.String dialogId) {
		this.dialogId = dialogId;
	}

	/**
	 * Gets the content value for this UpdateDialog.
	 *
	 * @return content
	 */
	public java.lang.String getContent() {
		return content;
	}

	/**
	 * Sets the content value for this UpdateDialog.
	 *
	 * @param content
	 */
	public void setContent(java.lang.String content) {
		this.content = content;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof UpdateDialog)) {
			return false;
		}
		UpdateDialog other = (UpdateDialog) obj;
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
				&& ((this.dialogId == null && other.getDialogId() == null)
						|| (this.dialogId != null && this.dialogId.equals(other.getDialogId())))
				&& ((this.content == null && other.getContent() == null)
						|| (this.content != null && this.content.equals(other.getContent())));
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
		if (getDialogId() != null) {
			_hashCode += getDialogId().hashCode();
		}
		if (getContent() != null) {
			_hashCode += getContent().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

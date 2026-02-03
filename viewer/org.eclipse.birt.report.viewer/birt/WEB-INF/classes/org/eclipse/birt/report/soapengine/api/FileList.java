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
 * FileList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FileList")
@XmlAccessorType(XmlAccessType.NONE)
public class FileList implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "file")
	private org.eclipse.birt.report.soapengine.api.File[] file;

	public FileList() {
	}

	public FileList(org.eclipse.birt.report.soapengine.api.File[] file) {
		this.file = file;
	}

	/**
	 * Gets the file value for this FileList.
	 *
	 * @return file
	 */
	public org.eclipse.birt.report.soapengine.api.File[] getFile() {
		return file;
	}

	/**
	 * Sets the file value for this FileList.
	 *
	 * @param file
	 */
	public void setFile(org.eclipse.birt.report.soapengine.api.File[] file) {
		this.file = file;
	}

	public org.eclipse.birt.report.soapengine.api.File getFile(int i) {
		return this.file[i];
	}

	public void setFile(int i, org.eclipse.birt.report.soapengine.api.File _value) {
		this.file[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof FileList)) {
			return false;
		}
		FileList other = (FileList) obj;
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
		_equals = true && ((this.file == null && other.getFile() == null)
				|| (this.file != null && java.util.Arrays.equals(this.file, other.getFile())));
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
		if (getFile() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getFile()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getFile(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

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
 * File.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "File")
@XmlAccessorType(XmlAccessType.NONE)
public class File implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "index")
	private int index;
	@XmlElement(name = "isFile")
	private boolean isFile;
	@XmlElement(name = "name")
	private java.lang.String name;
	@XmlElement(name = "fileType")
	private java.lang.String fileType;
	@XmlElement(name = "displayName")
	private java.lang.String displayName;
	@XmlElement(name = "description")
	private java.lang.String description;

	public File() {
	}

	public File(int index, boolean isFile, java.lang.String name, java.lang.String fileType,
			java.lang.String displayName, java.lang.String description) {
		this.index = index;
		this.isFile = isFile;
		this.name = name;
		this.fileType = fileType;
		this.displayName = displayName;
		this.description = description;
	}

	/**
	 * Gets the index value for this File.
	 *
	 * @return index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the index value for this File.
	 *
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Gets the isFile value for this File.
	 *
	 * @return isFile
	 */
	public boolean isIsFile() {
		return isFile;
	}

	/**
	 * Sets the isFile value for this File.
	 *
	 * @param isFile
	 */
	public void setIsFile(boolean isFile) {
		this.isFile = isFile;
	}

	/**
	 * Gets the name value for this File.
	 *
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this File.
	 *
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the fileType value for this File.
	 *
	 * @return fileType
	 */
	public java.lang.String getFileType() {
		return fileType;
	}

	/**
	 * Sets the fileType value for this File.
	 *
	 * @param fileType
	 */
	public void setFileType(java.lang.String fileType) {
		this.fileType = fileType;
	}

	/**
	 * Gets the displayName value for this File.
	 *
	 * @return displayName
	 */
	public java.lang.String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the displayName value for this File.
	 *
	 * @param displayName
	 */
	public void setDisplayName(java.lang.String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the description value for this File.
	 *
	 * @return description
	 */
	public java.lang.String getDescription() {
		return description;
	}

	/**
	 * Sets the description value for this File.
	 *
	 * @param description
	 */
	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof File)) {
			return false;
		}
		File other = (File) obj;
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
		_equals = true && this.index == other.getIndex() && this.isFile == other.isIsFile()
				&& ((this.name == null && other.getName() == null)
						|| (this.name != null && this.name.equals(other.getName())))
				&& ((this.fileType == null && other.getFileType() == null)
						|| (this.fileType != null && this.fileType.equals(other.getFileType())))
				&& ((this.displayName == null && other.getDisplayName() == null)
						|| (this.displayName != null && this.displayName.equals(other.getDisplayName())))
				&& ((this.description == null && other.getDescription() == null)
						|| (this.description != null && this.description.equals(other.getDescription())));
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
		_hashCode += getIndex();
		_hashCode += (isIsFile() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		if (getName() != null) {
			_hashCode += getName().hashCode();
		}
		if (getFileType() != null) {
			_hashCode += getFileType().hashCode();
		}
		if (getDisplayName() != null) {
			_hashCode += getDisplayName().hashCode();
		}
		if (getDescription() != null) {
			_hashCode += getDescription().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

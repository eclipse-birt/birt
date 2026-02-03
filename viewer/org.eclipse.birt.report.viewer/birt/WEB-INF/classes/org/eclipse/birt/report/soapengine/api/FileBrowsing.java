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
 * FileBrowsing.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FileBrowsing")
@XmlAccessorType(XmlAccessType.NONE)
public class FileBrowsing implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "path")
	private java.lang.String path;
	@XmlElement(name = "fileName")
	private java.lang.String fileName;
	@XmlElement(name = "search")
	private org.eclipse.birt.report.soapengine.api.FileSearch search;
	@XmlElement(name = "pathSeparator")
	private java.lang.String pathSeparator;
	@XmlElement(name = "currentWorkingFolder")
	private org.eclipse.birt.report.soapengine.api.RepositoryPathSegmentList currentWorkingFolder;
	@XmlElement(name = "fileList")
	private org.eclipse.birt.report.soapengine.api.FileList fileList;

	public FileBrowsing() {
	}

	public FileBrowsing(java.lang.String path, java.lang.String fileName,
			org.eclipse.birt.report.soapengine.api.FileSearch search, java.lang.String pathSeparator,
			org.eclipse.birt.report.soapengine.api.RepositoryPathSegmentList currentWorkingFolder,
			org.eclipse.birt.report.soapengine.api.FileList fileList) {
		this.path = path;
		this.fileName = fileName;
		this.search = search;
		this.pathSeparator = pathSeparator;
		this.currentWorkingFolder = currentWorkingFolder;
		this.fileList = fileList;
	}

	/**
	 * Gets the path value for this FileBrowsing.
	 *
	 * @return path
	 */
	public java.lang.String getPath() {
		return path;
	}

	/**
	 * Sets the path value for this FileBrowsing.
	 *
	 * @param path
	 */
	public void setPath(java.lang.String path) {
		this.path = path;
	}

	/**
	 * Gets the fileName value for this FileBrowsing.
	 *
	 * @return fileName
	 */
	public java.lang.String getFileName() {
		return fileName;
	}

	/**
	 * Sets the fileName value for this FileBrowsing.
	 *
	 * @param fileName
	 */
	public void setFileName(java.lang.String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Gets the search value for this FileBrowsing.
	 *
	 * @return search
	 */
	public org.eclipse.birt.report.soapengine.api.FileSearch getSearch() {
		return search;
	}

	/**
	 * Sets the search value for this FileBrowsing.
	 *
	 * @param search
	 */
	public void setSearch(org.eclipse.birt.report.soapengine.api.FileSearch search) {
		this.search = search;
	}

	/**
	 * Gets the pathSeparator value for this FileBrowsing.
	 *
	 * @return pathSeparator
	 */
	public java.lang.String getPathSeparator() {
		return pathSeparator;
	}

	/**
	 * Sets the pathSeparator value for this FileBrowsing.
	 *
	 * @param pathSeparator
	 */
	public void setPathSeparator(java.lang.String pathSeparator) {
		this.pathSeparator = pathSeparator;
	}

	/**
	 * Gets the currentWorkingFolder value for this FileBrowsing.
	 *
	 * @return currentWorkingFolder
	 */
	public org.eclipse.birt.report.soapengine.api.RepositoryPathSegmentList getCurrentWorkingFolder() {
		return currentWorkingFolder;
	}

	/**
	 * Sets the currentWorkingFolder value for this FileBrowsing.
	 *
	 * @param currentWorkingFolder
	 */
	public void setCurrentWorkingFolder(
			org.eclipse.birt.report.soapengine.api.RepositoryPathSegmentList currentWorkingFolder) {
		this.currentWorkingFolder = currentWorkingFolder;
	}

	/**
	 * Gets the fileList value for this FileBrowsing.
	 *
	 * @return fileList
	 */
	public org.eclipse.birt.report.soapengine.api.FileList getFileList() {
		return fileList;
	}

	/**
	 * Sets the fileList value for this FileBrowsing.
	 *
	 * @param fileList
	 */
	public void setFileList(org.eclipse.birt.report.soapengine.api.FileList fileList) {
		this.fileList = fileList;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof FileBrowsing)) {
			return false;
		}
		FileBrowsing other = (FileBrowsing) obj;
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
				&& ((this.path == null && other.getPath() == null)
						|| (this.path != null && this.path.equals(other.getPath())))
				&& ((this.fileName == null && other.getFileName() == null)
						|| (this.fileName != null && this.fileName.equals(other.getFileName())))
				&& ((this.search == null && other.getSearch() == null)
						|| (this.search != null && this.search.equals(other.getSearch())))
				&& ((this.pathSeparator == null && other.getPathSeparator() == null)
						|| (this.pathSeparator != null && this.pathSeparator.equals(other.getPathSeparator())))
				&& ((this.currentWorkingFolder == null && other.getCurrentWorkingFolder() == null)
						|| (this.currentWorkingFolder != null
								&& this.currentWorkingFolder.equals(other.getCurrentWorkingFolder())))
				&& ((this.fileList == null && other.getFileList() == null)
						|| (this.fileList != null && this.fileList.equals(other.getFileList())));
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
		if (getPath() != null) {
			_hashCode += getPath().hashCode();
		}
		if (getFileName() != null) {
			_hashCode += getFileName().hashCode();
		}
		if (getSearch() != null) {
			_hashCode += getSearch().hashCode();
		}
		if (getPathSeparator() != null) {
			_hashCode += getPathSeparator().hashCode();
		}
		if (getCurrentWorkingFolder() != null) {
			_hashCode += getCurrentWorkingFolder().hashCode();
		}
		if (getFileList() != null) {
			_hashCode += getFileList().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

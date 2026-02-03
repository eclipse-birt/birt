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
 * Page.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Page")
@XmlAccessorType(XmlAccessType.NONE)
public class Page implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "PageNumber")
	private java.lang.String pageNumber;
	@XmlElement(name = "TotalPage")
	private java.lang.String totalPage;
	@XmlElement(name = "Completed")
	private java.lang.Boolean completed;
	@XmlElement(name = "rtl")
	private java.lang.Boolean rtl;

	public Page() {
	}

	public Page(java.lang.String pageNumber, java.lang.String totalPage, java.lang.Boolean completed,
			java.lang.Boolean rtl) {
		this.pageNumber = pageNumber;
		this.totalPage = totalPage;
		this.completed = completed;
		this.rtl = rtl;
	}

	/**
	 * Gets the pageNumber value for this Page.
	 *
	 * @return pageNumber
	 */
	public java.lang.String getPageNumber() {
		return pageNumber;
	}

	/**
	 * Sets the pageNumber value for this Page.
	 *
	 * @param pageNumber
	 */
	public void setPageNumber(java.lang.String pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * Gets the totalPage value for this Page.
	 *
	 * @return totalPage
	 */
	public java.lang.String getTotalPage() {
		return totalPage;
	}

	/**
	 * Sets the totalPage value for this Page.
	 *
	 * @param totalPage
	 */
	public void setTotalPage(java.lang.String totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * Gets the completed value for this Page.
	 *
	 * @return completed
	 */
	public java.lang.Boolean getCompleted() {
		return completed;
	}

	/**
	 * Sets the completed value for this Page.
	 *
	 * @param completed
	 */
	public void setCompleted(java.lang.Boolean completed) {
		this.completed = completed;
	}

	/**
	 * Gets the right-to-left value for this Page.
	 *
	 * @return rtl
	 */
	public java.lang.Boolean getRtl() {
		return rtl;
	}

	/**
	 * Sets the right-to-left value for this Page.
	 *
	 * @param rtl rtl
	 */
	public void setRtl(java.lang.Boolean rtl) {
		this.rtl = rtl;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof Page)) {
			return false;
		}
		Page other = (Page) obj;
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
				&& ((this.pageNumber == null && other.getPageNumber() == null)
						|| (this.pageNumber != null && this.pageNumber.equals(other.getPageNumber())))
				&& ((this.totalPage == null && other.getTotalPage() == null)
						|| (this.totalPage != null && this.totalPage.equals(other.getTotalPage())))
				&& ((this.completed == null && other.getCompleted() == null)
						|| (this.completed != null && this.completed.equals(other.getCompleted())))
				&& ((this.rtl == null && other.getRtl() == null)
						|| (this.rtl != null && this.rtl.equals(other.getRtl())));
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
		if (getPageNumber() != null) {
			_hashCode += getPageNumber().hashCode();
		}
		if (getTotalPage() != null) {
			_hashCode += getTotalPage().hashCode();
		}
		if (getCompleted() != null) {
			_hashCode += getCompleted().hashCode();
		}
		if (getRtl() != null) {
			_hashCode += getRtl().hashCode();
		}

		__hashCodeCalc = false;
		return _hashCode;
	}

	}

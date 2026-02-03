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
 * RepositoryPathSegmentList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RepositoryPathSegmentList")
@XmlAccessorType(XmlAccessType.NONE)
public class RepositoryPathSegmentList implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "segment")
	private org.eclipse.birt.report.soapengine.api.RepositoryPathSegment[] segment;

	public RepositoryPathSegmentList() {
	}

	public RepositoryPathSegmentList(org.eclipse.birt.report.soapengine.api.RepositoryPathSegment[] segment) {
		this.segment = segment;
	}

	/**
	 * Gets the segment value for this RepositoryPathSegmentList.
	 *
	 * @return segment
	 */
	public org.eclipse.birt.report.soapengine.api.RepositoryPathSegment[] getSegment() {
		return segment;
	}

	/**
	 * Sets the segment value for this RepositoryPathSegmentList.
	 *
	 * @param segment
	 */
	public void setSegment(org.eclipse.birt.report.soapengine.api.RepositoryPathSegment[] segment) {
		this.segment = segment;
	}

	public org.eclipse.birt.report.soapengine.api.RepositoryPathSegment getSegment(int i) {
		return this.segment[i];
	}

	public void setSegment(int i, org.eclipse.birt.report.soapengine.api.RepositoryPathSegment _value) {
		this.segment[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof RepositoryPathSegmentList)) {
			return false;
		}
		RepositoryPathSegmentList other = (RepositoryPathSegmentList) obj;
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
		_equals = true && ((this.segment == null && other.getSegment() == null)
				|| (this.segment != null && java.util.Arrays.equals(this.segment, other.getSegment())));
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
		if (getSegment() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getSegment()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getSegment(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

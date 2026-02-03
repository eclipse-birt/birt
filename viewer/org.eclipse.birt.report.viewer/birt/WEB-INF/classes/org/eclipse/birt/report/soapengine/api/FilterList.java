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
 * FilterList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FilterList")
@XmlAccessorType(XmlAccessType.NONE)
public class FilterList implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "filter")
	private org.eclipse.birt.report.soapengine.api.Filter[] filter;

	public FilterList() {
	}

	public FilterList(org.eclipse.birt.report.soapengine.api.Filter[] filter) {
		this.filter = filter;
	}

	/**
	 * Gets the filter value for this FilterList.
	 *
	 * @return filter
	 */
	public org.eclipse.birt.report.soapengine.api.Filter[] getFilter() {
		return filter;
	}

	/**
	 * Sets the filter value for this FilterList.
	 *
	 * @param filter
	 */
	public void setFilter(org.eclipse.birt.report.soapengine.api.Filter[] filter) {
		this.filter = filter;
	}

	public org.eclipse.birt.report.soapengine.api.Filter getFilter(int i) {
		return this.filter[i];
	}

	public void setFilter(int i, org.eclipse.birt.report.soapengine.api.Filter _value) {
		this.filter[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof FilterList)) {
			return false;
		}
		FilterList other = (FilterList) obj;
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
		_equals = true && ((this.filter == null && other.getFilter() == null)
				|| (this.filter != null && java.util.Arrays.equals(this.filter, other.getFilter())));
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
		if (getFilter() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getFilter()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getFilter(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

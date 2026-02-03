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
 * FilterExpression.java
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
 * A complex type to represent the condition expression used in filter.
 */
@XmlRootElement(name = "FilterExpression")
@XmlAccessorType(XmlAccessType.NONE)
public class FilterExpression implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/** Represents Equality operator, i.e., '=='. */
	@XmlElement(name = "Clause")
	private org.eclipse.birt.report.soapengine.api.FilterClause clause;
	@XmlElement(name = "Not")
	private org.eclipse.birt.report.soapengine.api.FilterExpression[] not;
	@XmlElement(name = "Or")
	private org.eclipse.birt.report.soapengine.api.FilterExpression[] or;
	/** Optional AND clause which can again be nested with AND/OR clauses */
	@XmlElement(name = "And")
	private org.eclipse.birt.report.soapengine.api.FilterExpression[] and;

	public FilterExpression() {
	}

	public FilterExpression(org.eclipse.birt.report.soapengine.api.FilterClause clause,
			org.eclipse.birt.report.soapengine.api.FilterExpression[] not,
			org.eclipse.birt.report.soapengine.api.FilterExpression[] or,
			org.eclipse.birt.report.soapengine.api.FilterExpression[] and) {
		this.clause = clause;
		this.not = not;
		this.or = or;
		this.and = and;
	}

	/**
	 * Gets the clause value for this FilterExpression.
	 *
	 * @return clause Represents Equality operator, i.e., '=='.
	 */
	public org.eclipse.birt.report.soapengine.api.FilterClause getClause() {
		return clause;
	}

	/**
	 * Sets the clause value for this FilterExpression.
	 *
	 * @param clause Represents Equality operator, i.e., '=='.
	 */
	public void setClause(org.eclipse.birt.report.soapengine.api.FilterClause clause) {
		this.clause = clause;
	}

	/**
	 * Gets the not value for this FilterExpression.
	 *
	 * @return not
	 */
	public org.eclipse.birt.report.soapengine.api.FilterExpression[] getNot() {
		return not;
	}

	/**
	 * Sets the not value for this FilterExpression.
	 *
	 * @param not
	 */
	public void setNot(org.eclipse.birt.report.soapengine.api.FilterExpression[] not) {
		this.not = not;
	}

	public org.eclipse.birt.report.soapengine.api.FilterExpression getNot(int i) {
		return this.not[i];
	}

	public void setNot(int i, org.eclipse.birt.report.soapengine.api.FilterExpression _value) {
		this.not[i] = _value;
	}

	/**
	 * Gets the or value for this FilterExpression.
	 *
	 * @return or
	 */
	public org.eclipse.birt.report.soapengine.api.FilterExpression[] getOr() {
		return or;
	}

	/**
	 * Sets the or value for this FilterExpression.
	 *
	 * @param or
	 */
	public void setOr(org.eclipse.birt.report.soapengine.api.FilterExpression[] or) {
		this.or = or;
	}

	public org.eclipse.birt.report.soapengine.api.FilterExpression getOr(int i) {
		return this.or[i];
	}

	public void setOr(int i, org.eclipse.birt.report.soapengine.api.FilterExpression _value) {
		this.or[i] = _value;
	}

	/**
	 * Gets the and value for this FilterExpression.
	 *
	 * @return and Optional AND clause which can again be nested with AND/OR clauses
	 */
	public org.eclipse.birt.report.soapengine.api.FilterExpression[] getAnd() {
		return and;
	}

	/**
	 * Sets the and value for this FilterExpression.
	 *
	 * @param and Optional AND clause which can again be nested with AND/OR clauses
	 */
	public void setAnd(org.eclipse.birt.report.soapengine.api.FilterExpression[] and) {
		this.and = and;
	}

	public org.eclipse.birt.report.soapengine.api.FilterExpression getAnd(int i) {
		return this.and[i];
	}

	public void setAnd(int i, org.eclipse.birt.report.soapengine.api.FilterExpression _value) {
		this.and[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof FilterExpression)) {
			return false;
		}
		FilterExpression other = (FilterExpression) obj;
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
				&& ((this.clause == null && other.getClause() == null)
						|| (this.clause != null && this.clause.equals(other.getClause())))
				&& ((this.not == null && other.getNot() == null)
						|| (this.not != null && java.util.Arrays.equals(this.not, other.getNot())))
				&& ((this.or == null && other.getOr() == null)
						|| (this.or != null && java.util.Arrays.equals(this.or, other.getOr())))
				&& ((this.and == null && other.getAnd() == null)
						|| (this.and != null && java.util.Arrays.equals(this.and, other.getAnd())));
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
		if (getClause() != null) {
			_hashCode += getClause().hashCode();
		}
		if (getNot() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getNot()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getNot(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		if (getOr() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getOr()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getOr(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		if (getAnd() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getAnd()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getAnd(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

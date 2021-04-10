/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.adapter.oda.impl;

import org.eclipse.birt.report.model.adapter.oda.IAmbiguousAttribute;

/**
 * Represents an ambiguous attribute
 * 
 */

class AmbiguousAttribute implements IAmbiguousAttribute {

	private String name;
	private Object oldValue;
	private Object toSetValue;
	private boolean isFromReportParameter;

	/**
	 * @param name
	 * @param oldValue
	 * @param toSet
	 * @param isFromReportParameter
	 */

	AmbiguousAttribute(String name, Object oldValue, Object toSet, boolean isFromReportParameter) {
		this.name = name;
		this.oldValue = oldValue;
		this.toSetValue = toSet;
		this.isFromReportParameter = isFromReportParameter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IAmbiguousAttribute#
	 * getAttributeName()
	 */
	public String getAttributeName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IAmbiguousAttribute#
	 * getPreviousValue()
	 */
	public Object getPreviousValue() {
		return oldValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.adapter.oda.IAmbiguousAttribute#getRevisedValue
	 * ()
	 */
	public Object getRevisedValue() {
		return toSetValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IAmbiguousAttribute#
	 * isLinkedReportParameterAttribute()
	 */
	public boolean isLinkedReportParameterAttribute() {
		return isFromReportParameter;
	}

}

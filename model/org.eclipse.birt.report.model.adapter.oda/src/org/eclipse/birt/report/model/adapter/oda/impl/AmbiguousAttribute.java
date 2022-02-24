/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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
	@Override
	public String getAttributeName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IAmbiguousAttribute#
	 * getPreviousValue()
	 */
	@Override
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
	@Override
	public Object getRevisedValue() {
		return toSetValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IAmbiguousAttribute#
	 * isLinkedReportParameterAttribute()
	 */
	@Override
	public boolean isLinkedReportParameterAttribute() {
		return isFromReportParameter;
	}

}

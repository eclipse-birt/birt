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

package org.eclipse.birt.report.model.adapter.oda;

/**
 * Interface that gives out a pair of values that is ambiguous between data set
 * parameter handle and data set design. when converting data set design to data
 * set handle.
 */

public interface IAmbiguousAttribute {

	/**
	 * The ROM property names.
	 * 
	 * @return
	 */

	public String getAttributeName();

	/**
	 * The new values from ODA parameter definition.
	 * 
	 * @return
	 */

	public Object getRevisedValue();

	/**
	 * The previous value on the ROM data set parameter.
	 * 
	 * @return
	 */

	public Object getPreviousValue();

	/**
	 * 
	 * @return
	 */
	public boolean isLinkedReportParameterAttribute();

}

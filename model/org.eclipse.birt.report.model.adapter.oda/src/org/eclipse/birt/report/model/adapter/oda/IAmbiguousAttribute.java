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

	String getAttributeName();

	/**
	 * The new values from ODA parameter definition.
	 *
	 * @return
	 */

	Object getRevisedValue();

	/**
	 * The previous value on the ROM data set parameter.
	 *
	 * @return
	 */

	Object getPreviousValue();

	/**
	 *
	 * @return
	 */
	boolean isLinkedReportParameterAttribute();

}


/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.report.data.adapter.api;

/**
 * Provide the meta info for a binding, including its referenced expression
 * type, etc.
 */

public interface IBindingMetaInfo {
	public int MEASURE_TYPE = 1;
	public int DIMENSION_TYPE = 2;
	public int GRAND_TOTAL_TYPE = 11;
	public int SUB_TOTAL_TYPE = 12;
	public int OTHER_TYPE = 13;

	/**
	 * The name of the binding.
	 * 
	 * @return
	 */
	public String getBindingName();

	/**
	 * The type of the binding.
	 * 
	 * @return
	 */
	public int getBindingType();
}

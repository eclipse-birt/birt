
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

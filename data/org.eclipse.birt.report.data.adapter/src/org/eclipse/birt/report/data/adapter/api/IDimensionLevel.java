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

package org.eclipse.birt.report.data.adapter.api;

/**
 * This interface provide api user a convenient way to access a cube dimension
 * as well as level and attribute.
 *
 * @author Administrator
 *
 */
public interface IDimensionLevel {
	/**
	 *
	 * @return
	 */
	String getDimensionName();

	/**
	 *
	 * @return
	 */
	String getLevelName();

	/**
	 *
	 * @return
	 */
	String getAttributeName();
}

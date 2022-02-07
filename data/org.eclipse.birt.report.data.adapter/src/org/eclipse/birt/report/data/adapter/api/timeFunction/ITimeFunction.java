/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
package org.eclipse.birt.report.data.adapter.api.timeFunction;

import java.util.List;

public interface ITimeFunction {
	/**
	 * Get the function name, see<code>IBuildInTimeFunction<code>
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Get the function display name.
	 * 
	 * @return
	 */
	public String getDisplayName();

	/**
	 * Get the function's description.
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * Get argument info
	 * 
	 * @return
	 */
	public List<IArgumentInfo> getArguments();
}

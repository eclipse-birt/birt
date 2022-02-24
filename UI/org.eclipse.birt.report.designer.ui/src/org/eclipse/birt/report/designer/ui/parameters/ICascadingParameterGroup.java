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

package org.eclipse.birt.report.designer.ui.parameters;

/**
 * Cascading parameter group interface.
 * 
 */

public interface ICascadingParameterGroup extends IParameterGroup {

	/**
	 * Get pre-parameter, if no pre-papameter, return null.
	 * 
	 * @param parameter
	 * @return pre-parameter
	 */

	public IParameter getPreParameter(IParameter parameter);

	/**
	 * Get post-parameter, if no post-papameter, return null.
	 * 
	 * @param parameter
	 * @return post-parameter
	 */

	public IParameter getPostParameter(IParameter parameter);

	/**
	 * Gets parameter in special position.
	 * 
	 * @param index
	 * @return parameter.
	 */

	public IParameter getParameter(int index);
}

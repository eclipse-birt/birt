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

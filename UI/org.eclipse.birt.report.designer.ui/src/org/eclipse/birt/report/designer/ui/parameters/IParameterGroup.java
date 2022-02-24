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

import java.util.List;

/**
 * Parameter group interface.
 * 
 */

public interface IParameterGroup {

	/**
	 * Gets fist level children of parameter .
	 * 
	 * @return children of parameter.
	 */

	public List getChildren();

	/**
	 * Adds Parameter
	 * 
	 * @param parameter
	 */

	public void addParameter(IParameter parameter);

}

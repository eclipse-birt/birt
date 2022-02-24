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

package org.eclipse.birt.report.engine.api;

import java.util.ArrayList;

/**
 * The interface for objects which visually groups report parameters.
 */
public interface IParameterGroupDefn extends IParameterDefnBase {
	/**
	 * returns the set of parameters that appear inside the group.
	 * 
	 * @return the set of parameters that appear inside the group.
	 */
	public ArrayList getContents();

	/**
	 * returns whether to display the parameter group in expanded form
	 * 
	 * @return whether to display the parameter group expanded
	 */
	public boolean displayExpanded();
}

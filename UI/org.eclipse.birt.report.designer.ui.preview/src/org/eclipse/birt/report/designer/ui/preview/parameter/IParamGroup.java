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

package org.eclipse.birt.report.designer.ui.preview.parameter;

import java.util.List;

/**
 * Parameter group interface.
 * 
 */

public interface IParamGroup {

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

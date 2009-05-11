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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * Defines constants for Variable.
 */

public interface IVariableElementModel
{

	/**
	 * Name of the "name" member.
	 */

	String VARIABLE_NAME_PROP = "variableName"; //$NON-NLS-1$

	/**
	 * Name of the "value" member.
	 */

	String VALUE_PROP = "value"; //$NON-NLS-1$

	/**
	 * Name of the work mode property which indicates how this variable works.
	 */
	String WORK_MODE_PROP = "workMode"; //$NON-NLS-1$

}

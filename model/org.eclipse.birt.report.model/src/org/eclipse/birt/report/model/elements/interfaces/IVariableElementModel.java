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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * Defines constants for Variable.
 */

public interface IVariableElementModel {

	/**
	 * Name of the "name" property.
	 */

	String VARIABLE_NAME_PROP = "variableName"; //$NON-NLS-1$

	/**
	 * Name of the "value" property.
	 */

	String VALUE_PROP = "value"; //$NON-NLS-1$

	/**
	 * Name of the type property which indicates how this variable works. It can be
	 * report variable or page variable.
	 */

	String TYPE_PROP = "type"; //$NON-NLS-1$

}

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

/**
 * defines one choice in a parameter selction value list
 */
public interface IParameterSelectionChoice {
	/**
	 * returns the value of the selection choice
	 * 
	 * @return the value of the selction choice
	 */
	Object getValue();

	/**
	 * returns the locale-specific label for a selection choice. The locale used is
	 * the locale in the parameter definition request.
	 * 
	 * @return the localized label for the parameter
	 */
	String getLabel();
}

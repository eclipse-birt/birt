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

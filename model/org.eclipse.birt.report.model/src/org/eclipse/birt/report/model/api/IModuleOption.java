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

package org.eclipse.birt.report.model.api;

/**
 * Implements to define the keys to do some setting in a module.
 */

public interface IModuleOption
{

	/**
	 * Key to control whether to call semantic-check when opening a module. True
	 * if user wants to do some semantic checks about the module when opening
	 * it; otherwise false.
	 */

	static final String PARSER_SEMANTIC_CHECK_KEY = "semanticCheck"; //$NON-NLS-1$

	/**
	 * Key to set the resource folder of the module.
	 */

	static final String RESOURCE_FOLDER_KEY = "resourceFolder"; //$NON-NLS-1$

	/**
	 * Key to indicate the resource locator of the module.
	 */

	static final String RESOURCE_LOCATOR_KEY = "resourceLocator"; //$NON-NLS-1$	

	/**
	 * Key to control whether to mark line number of the element in xml source.
	 * True if user wants to mark them during parsing and saving, false
	 * otherwise. Default value is true.
	 */

	static final String MARK_LINE_NUMBER_KEY = "markLineNumber"; //$NON-NLS-1$	
}

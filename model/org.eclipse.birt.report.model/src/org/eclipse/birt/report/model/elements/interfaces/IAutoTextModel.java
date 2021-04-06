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
 * The interface for autotext element to store the constants.
 */
public interface IAutoTextModel {

	/**
	 * Name of the autotext type property that supports page number and total page
	 */

	public static final String AUTOTEXT_TYPE_PROP = "type"; //$NON-NLS-1$

	/**
	 * Name of the page variable property which refers to the page variable name.
	 */
	public static final String PAGE_VARIABLE_PROP = "pageVariable"; //$NON-NLS-1$

}

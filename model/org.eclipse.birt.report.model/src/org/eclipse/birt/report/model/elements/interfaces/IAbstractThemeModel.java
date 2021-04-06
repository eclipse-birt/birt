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
 * The interface for theme elements to store the constants.
 */

public interface IAbstractThemeModel {

	/**
	 * Identifier of the slot that holds the cells in row.
	 */

	public static final int STYLES_SLOT = 0;

	/**
	 * The name of the default theme.
	 */

	public final static String DEFAULT_THEME_NAME = "Theme.defaultThemeName"; //$NON-NLS-1$

	/**
	 * css file property
	 */

	public static final String CSSES_PROP = "cssStyleSheets";//$NON-NLS-1$

}

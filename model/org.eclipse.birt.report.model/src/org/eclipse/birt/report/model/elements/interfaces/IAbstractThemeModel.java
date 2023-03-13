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
 * The interface for theme elements to store the constants.
 */

public interface IAbstractThemeModel {

	/**
	 * Identifier of the slot that holds the cells in row.
	 */

	int STYLES_SLOT = 0;

	/**
	 * The name of the default theme.
	 */

	String DEFAULT_THEME_NAME = "Theme.defaultThemeName"; //$NON-NLS-1$

	/**
	 * css file property
	 */

	String CSSES_PROP = "cssStyleSheets";//$NON-NLS-1$

}

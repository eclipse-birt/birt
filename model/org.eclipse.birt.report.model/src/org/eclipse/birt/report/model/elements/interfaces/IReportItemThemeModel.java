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

public interface IReportItemThemeModel {

	/**
	 * Name of the property that specifies the type of this report item theme.It can
	 * be one of the predefined choices: Table, Grid, List or some custome values.
	 */

	String TYPE_PROP = "type"; //$NON-NLS-1$

	/**
	 * Name of the property that specifies some custom values for this report item
	 * theme.
	 */
	String CUSTOM_VALUES_PROP = "customValues"; //$NON-NLS-1$

}

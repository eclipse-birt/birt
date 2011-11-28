/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.interfaces;

import org.eclipse.birt.chart.util.TriggerSupportMatrix;

/**
 * UI factory used to create all kinds of UI classes.
 */

public interface IChartUIFactory
{

	/**
	 * Returns the current UI helper
	 * 
	 * @return UI helper
	 */
	IChartUIHelper createUIHelper( );

	/**
	 * Creates instance of <code>TriggerSupportMatrix</code>.
	 * 
	 * @param outputFormat
	 *            output format
	 * @param iType
	 *            interactivity type
	 * @return instance
	 * @since 3.7
	 */
	TriggerSupportMatrix createSupportMatrix( String outputFormat, int iType );
}

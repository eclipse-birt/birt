/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation. 
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.type;

import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;

/**
 * BarChart
 */
public class BarChart extends AbstractBarChart {

	/**
	 * Comment for <code>TYPE_LITERAL</code>
	 */
	public static final String TYPE_LITERAL = ChartUIConstants.TYPE_BAR;

	/**
	 * Constructor of the class.
	 */
	public BarChart() {
		super("Bar", TYPE_LITERAL, RiserType.RECTANGLE_LITERAL); //$NON-NLS-1$
	}
}

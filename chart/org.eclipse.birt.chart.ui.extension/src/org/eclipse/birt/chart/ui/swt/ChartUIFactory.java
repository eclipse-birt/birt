/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt;

import org.eclipse.birt.chart.ui.integrate.ChartUIFactoryBase;
import org.eclipse.birt.chart.ui.swt.composites.ChartCheckbox;
import org.eclipse.swt.widgets.Composite;


/**
 * ChartUIFactory
 */

public class ChartUIFactory extends ChartUIFactoryBase
{

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory#createChartCheckbox(org.eclipse.swt.widgets.Composite, int, boolean)
	 */
	public AbstractChartCheckbox createChartCheckbox( Composite parent,
			int styles, boolean defaultSelection )
	{
		return new ChartCheckbox( parent, styles, defaultSelection );
	}
}

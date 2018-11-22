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

package org.eclipse.birt.chart.ui.integrate;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartUIHelper;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.core.exception.BirtException;

/**
 * Default implementation or base class of UI helper interface.
 */

public class ChartUIHelperBase implements IChartUIHelper
{

	public boolean isDefaultTitleSupported( )
	{
		return false;
	}

	public String getDefaultTitle( ChartWizardContext context )
	{
		return ""; //$NON-NLS-1$
	}

	public void updateDefaultTitle( Chart cm, Object extendedItem )
	{
		// Do nothing
	}

	public boolean canCombine( IChartType type, ChartWizardContext context )
	{
		return type.canCombine( );
	}

	@Override
	public boolean useDataSetRow( Object reportItem, String expression ) throws BirtException
	{
		// Default implementation, do nothing
		return false;
	}
}

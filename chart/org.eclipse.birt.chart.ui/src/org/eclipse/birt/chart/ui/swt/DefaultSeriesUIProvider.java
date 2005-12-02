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

package org.eclipse.birt.chart.ui.swt;

import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public abstract class DefaultSeriesUIProvider implements ISeriesUIProvider
{

	public Composite getSeriesAttributeSheet( Composite parent, Series series )
	{
		return null;
	}

	public Composite getSeriesAttributeSheet( Composite parent, Series series,
			IUIServiceProvider builder, Object oContext )
	{
		return null;
	}

	public Composite getSeriesDataSheet( Composite parent,
			SeriesDefinition seriesdefinition, IUIServiceProvider builder,
			Object oContext )
	{
		return null;
	}

	public String getSeriesClass( )
	{
		return null;
	}

	public ISelectDataComponent getSeriesDataComponent( int seriesType,
			SeriesDefinition seriesDefn, IUIServiceProvider builder,
			Object oContext, String sTitle )
	{
		return null;
	}

}

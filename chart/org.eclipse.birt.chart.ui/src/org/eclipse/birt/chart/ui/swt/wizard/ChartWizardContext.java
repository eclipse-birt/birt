/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.style.IStyleProcessor;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;

/**
 * @author Actuate Corporation
 * 
 */
public class ChartWizardContext implements IWizardContext
{

	private Chart chartModel = null;
	private IChartType chartType = null;
	private Object extendedItem = null;
	private String sDefaultOutputFormat = "SVG"; //$NON-NLS-1$
	private IUIServiceProvider uiProvider;
	private IDataServiceProvider dataProvider;
	private transient IStyleProcessor processor;

	public ChartWizardContext( Chart chartModel )
	{
		this.chartModel = chartModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.frameworks.taskwizard.interfaces.IWizardContext#getWizardID()
	 */
	public String getWizardID( )
	{
		return ChartWizard.WIZARD_ID;
	}

	public Chart getModel( )
	{
		return chartModel;
	}

	public void setModel( Chart model )
	{
		this.chartModel = model;
	}

	public Object getExtendedItem( )
	{
		return extendedItem;
	}

	public void setExtendedItem( Object extendedItem )
	{
		this.extendedItem = extendedItem;
	}

	public String getDefaultOutputFormat( )
	{
		return sDefaultOutputFormat;
	}

	public void setDefaultOutputFormat( String sOutputFormat )
	{
		this.sDefaultOutputFormat = sOutputFormat;
	}

	public IUIServiceProvider getUIServiceProvider( )
	{
		return uiProvider;
	}

	public void setUIServiceProvider( IUIServiceProvider uiProvider )
	{
		this.uiProvider = uiProvider;
	}

	public IDataServiceProvider getDataServiceProvider( )
	{
		return dataProvider;
	}

	public void setDataServiceProvider( IDataServiceProvider dataProvider )
	{
		this.dataProvider = dataProvider;
	}

	public void setChartType( IChartType chartType )
	{
		this.chartType = chartType;
	}

	public IChartType getChartType( )
	{
		return chartType;
	}

	/**
	 * @param processor
	 *            The processor to set.
	 */
	public void setProcessor( IStyleProcessor processor )
	{
		this.processor = processor;
	}

	/**
	 * @return Returns the processor.
	 */
	public IStyleProcessor getProcessor( )
	{
		return processor;
	}
}
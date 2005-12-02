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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.style.IStyleProcessor;
import org.eclipse.birt.chart.ui.swt.UIExtensionsImpl;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;

/**
 * ChartWizardContext
 */
public class ChartWizardContext implements IWizardContext
{

	private Chart chartModel = null;
	private IChartType chartType = null;
	private Object extendedItem = null;
	private String sDefaultOutputFormat = "SVG"; //$NON-NLS-1$
	private String sOutputFormat = sDefaultOutputFormat;
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

	public String getOutputFormat( )
	{
		return sOutputFormat;
	}

	public void setOutputFormat( String format )
	{
		this.sOutputFormat = format;
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
		if ( chartType == null )
		{
			// If chart type is not set, fetch the value from the model
			LinkedHashMap htTypes = new LinkedHashMap( );
			Collection cTypes = UIExtensionsImpl.instance( )
					.getUIChartTypeExtensions( );
			Iterator iterTypes = cTypes.iterator( );
			while ( iterTypes.hasNext( ) )
			{
				IChartType type = (IChartType) iterTypes.next( );
				htTypes.put( type.getName( ), type );
			}
			chartType = (IChartType) htTypes.get( chartModel.getType( ) );
		}
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
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
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;

/**
 * ChartWizardContext
 */
public class ChartWizardContext implements IWizardContext
{

	private transient Chart chartModel = null;
	private transient IChartType chartType = null;
	private transient Object extendedItem = null;
	private transient String sDefaultOutputFormat = "SVG"; //$NON-NLS-1$
	private transient String sOutputFormat = sDefaultOutputFormat;
	private transient IUIServiceProvider uiProvider;
	private transient IDataServiceProvider dataProvider;
	private transient IStyleProcessor processor;
	private transient boolean isMoreAxesSupported;
	private transient boolean isRtL;
	private transient ChartWizard chartWizard;

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
		return getExtendedItem( ) == null ? ChartWizard.WIZARD_ID
				: getExtendedItem( ).toString( );
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
			Collection cTypes = ChartUIExtensionsImpl.instance( )
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

	/**
	 * @param isMoreAxesSupported
	 *            The isMoreAxesSupported to set.
	 */
	public void setMoreAxesSupported( boolean isMoreAxesSupported )
	{
		this.isMoreAxesSupported = isMoreAxesSupported;
	}

	/**
	 * @return Returns the isMoreAxesSupported.
	 */
	public boolean isMoreAxesSupported( )
	{
		return isMoreAxesSupported;
	}

	/**
	 * @return Returns if RtL flag is set.
	 */
	public boolean isRtL( )
	{
		return isRtL;
	}

	/**
	 * Sets RtL flag.
	 * 
	 * @param isRtL
	 */
	public void setRtL( boolean isRtL )
	{
		this.isRtL = isRtL;
	}
	
	public void setChartWizard( ChartWizard chartWizard )
	{
		this.chartWizard = chartWizard;
	}
	
	public ChartWizard getChartWizard( )
	{
		return chartWizard;
	}
}
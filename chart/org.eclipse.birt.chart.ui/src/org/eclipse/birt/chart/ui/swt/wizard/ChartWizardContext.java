/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.style.IStyleProcessor;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
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
	private IStyleProcessor processor;
	private boolean isMoreAxesSupported;
	private boolean isRtL;
	private Map mSheetEnabled;
	private Map mQueries;

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

	/**
	 * Sets the UI enabled or not. The UI, including task, subtask or toggle
	 * button, is identified by the exclusive id.
	 * 
	 * @param id
	 *            the exclusive id to identify the UI
	 * @param bEnabled
	 *            the state to enable the UI
	 * @since 2.3
	 */
	public void setEnabled( String id, boolean bEnabled )
	{
		if ( mSheetEnabled == null )
		{
			mSheetEnabled = new HashMap( );
		}
		mSheetEnabled.put( id, new Boolean( bEnabled ) );
	}

	/**
	 * Returns if the UI is enabled or not.The UI, including task, subtask or
	 * toggle button, is identified by the exclusive id.
	 * 
	 * @param id
	 *            the exclusive id to identify the UI
	 * @return the UI enabled state
	 * @since 2.3
	 */
	public boolean isEnabled( String id )
	{
		if ( mSheetEnabled != null && mSheetEnabled.containsKey( id ) )
		{
			Boolean visible = (Boolean) mSheetEnabled.get( id );
			return visible.booleanValue( );
		}
		return true;
	}

	/**
	 * Adds predefined queries for later selection.
	 * 
	 * @param queryType
	 *            query type. See {@link ChartUIConstants#QUERY_CATEGORY},
	 *            {@link ChartUIConstants#QUERY_VALUE},
	 *            {@link ChartUIConstants#QUERY_OPTIONAL}
	 * @param expressions
	 *            expression array
	 * @since 2.3
	 */
	public void addPredefinedQuery( String queryType, String[] expressions )
	{
		if ( mQueries == null )
		{
			mQueries = new HashMap( );
		}
		mQueries.put( queryType, expressions );
	}

	/**
	 * Returns the predefined queries
	 * 
	 * @param queryType
	 *            query type. See {@link ChartUIConstants#QUERY_CATEGORY},
	 *            {@link ChartUIConstants#QUERY_VALUE},
	 *            {@link ChartUIConstants#QUERY_OPTIONAL}
	 * @return expression array
	 * @since 2.3
	 */
	public String[] getPredefinedQuery( String queryType )
	{
		if ( mQueries != null && mQueries.containsKey( queryType ) )
		{
			return (String[]) mQueries.get( queryType );
		}
		return null;
	}
}
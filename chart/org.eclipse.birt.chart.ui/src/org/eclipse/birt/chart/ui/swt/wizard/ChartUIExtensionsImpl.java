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
import java.util.Vector;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIExtensions;
import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ISubtaskSheet;
import org.eclipse.birt.core.ui.utils.UIHelper;

import com.ibm.icu.util.StringTokenizer;

/**
 * @author Actuate Corporation
 * 
 */
public class ChartUIExtensionsImpl implements IUIExtensions
{

	private Collection cSheets = null;

	private Collection cChartTypes = null;

	private Collection cListeners = null;

	private Collection cSeriesUI = null;

	private static final String[] saSheets = new String[]{
			"10/Series/ /org.eclipse.birt.chart.ui.swt.wizard.format.series.SeriesSheetImpl", //$NON-NLS-1$
			"11/Series.X Series/Category (X) Series/org.eclipse.birt.chart.ui.swt.wizard.format.series.SeriesXSheetImpl", //$NON-NLS-1$
			"12/Series.Y Series/Value (Y) Series/org.eclipse.birt.chart.ui.swt.wizard.format.series.SeriesYSheetImpl", //$NON-NLS-1$
			"13/Series.Category Series/ /org.eclipse.birt.chart.ui.swt.wizard.format.series.SeriesXSheetImpl", //$NON-NLS-1$
			"14/Series.Value Series/ /org.eclipse.birt.chart.ui.swt.wizard.format.series.SeriesYSheetImpl", //$NON-NLS-1$
			"20/Chart/Chart Area/org.eclipse.birt.chart.ui.swt.wizard.format.chart.ChartSheetImpl", //$NON-NLS-1$
			"21/Chart.Axis/ /org.eclipse.birt.chart.ui.swt.wizard.format.axis.AxisSheetImpl", //$NON-NLS-1$
			"22/Chart.Axis.X Axis/ /org.eclipse.birt.chart.ui.swt.wizard.format.axis.AxisXSheetImpl", //$NON-NLS-1$
			"23/Chart.Axis.Y Axis/ /org.eclipse.birt.chart.ui.swt.wizard.format.axis.AxisYSheetImpl", //$NON-NLS-1$
			"24/Chart.Axis.Z Axis/ /org.eclipse.birt.chart.ui.swt.wizard.format.axis.AxisZSheetImpl", //$NON-NLS-1$
			"25/Chart.Plot/ /org.eclipse.birt.chart.ui.swt.wizard.format.chart.ChartPlotSheetImpl", //$NON-NLS-1$
			"26/Chart.Legend/ /org.eclipse.birt.chart.ui.swt.wizard.format.chart.ChartLegendSheetImpl", //$NON-NLS-1$			
	};

	private static final String[] saTypes = new String[]{
			"org.eclipse.birt.chart.ui.swt.type.BarChart", "org.eclipse.birt.chart.ui.swt.type.LineChart", //$NON-NLS-1$ //$NON-NLS-2$
			"org.eclipse.birt.chart.ui.swt.type.AreaChart", "org.eclipse.birt.chart.ui.swt.type.PieChart",//$NON-NLS-1$ //$NON-NLS-2$
			"org.eclipse.birt.chart.ui.swt.type.MeterChart", "org.eclipse.birt.chart.ui.swt.type.ScatterChart",//$NON-NLS-1$ //$NON-NLS-2$ 
			"org.eclipse.birt.chart.ui.swt.type.StockChart", "org.eclipse.birt.chart.ui.swt.type.GanttChart",//$NON-NLS-1$ //$NON-NLS-2$ 
			"org.eclipse.birt.chart.ui.swt.type.BubbleChart" //$NON-NLS-1$
	};

	private static final String[] saListeners = new String[]{
		"org.eclipse.birt.chart.ui.event.ChangeListenerImpl" //$NON-NLS-1$
	};

	private static final String[] saSeriesUI = new String[]{
			"org.eclipse.birt.chart.ui.swt.series.SeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.AreaSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.BarSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.LineSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.MeterSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.PieSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.ScatterSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.StockSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.GanttSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.BubbleSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.DifferenceSeriesUIProvider" //$NON-NLS-1$
	};

	private static ChartUIExtensionsImpl uiExtensions = null;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.ui/swt.wizard" ); //$NON-NLS-1$

	/**
	 * 
	 */
	private ChartUIExtensionsImpl( )
	{
		super( );
	}

	public static synchronized ChartUIExtensionsImpl instance( )
	{
		if ( uiExtensions == null )
		{
			uiExtensions = new ChartUIExtensionsImpl( );
		}
		return uiExtensions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIExtensions#getUISheetExtensions()
	 */
	public Collection getUISheetExtensions( )
	{
		if ( cSheets == null )
		{
			cSheets = new Vector( );
			if ( UIHelper.isEclipseMode( ) )
			{
				IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry( );
				IExtensionPoint extensionPoint = pluginRegistry.getExtensionPoint( "org.eclipse.birt.chart.ui",//$NON-NLS-1$
						"uisheets" ); //$NON-NLS-1$
				IExtension[] extensions = extensionPoint.getExtensions( );

				for ( int iC = 0; iC < extensions.length; iC++ )
				{
					IExtension extension = extensions[iC];
					IConfigurationElement[] configElements = extension.getConfigurationElements( );

					for ( int i = 0; i < configElements.length; i++ )
					{
						IConfigurationElement currentTag = configElements[i];
						if ( currentTag.getName( ).equals( "propertySheet" ) ) //$NON-NLS-1$
						{
							try
							{
								cSheets.add( new DefaultRegisteredSubtaskEntryImpl( currentTag.getAttribute( "nodeIndex" ),//$NON-NLS-1$
										currentTag.getAttribute( "nodePath" ), currentTag.getAttribute( "displayName" ),//$NON-NLS-1$ //$NON-NLS-2$
										(ISubtaskSheet) currentTag.createExecutableExtension( "classDefinition" ) ) ); //$NON-NLS-1$
							}
							catch ( FrameworkException e )
							{
								logger.log( e );
							}
						}
					}
				}
			}
			else
			{
				for ( int iC = 0; iC < saSheets.length; iC++ )
				{
					try
					{
						StringTokenizer tokens = new StringTokenizer( saSheets[iC],
								"/" ); //$NON-NLS-1$
						String sNodeIndex = tokens.nextToken( );
						String sNodePath = tokens.nextToken( );
						String sDisplayName = tokens.nextToken( );
						String sSheetClass = tokens.nextToken( );
						DefaultRegisteredSubtaskEntryImpl entry = new DefaultRegisteredSubtaskEntryImpl( sNodeIndex,
								sNodePath,
								sDisplayName,
								(ISubtaskSheet) Class.forName( sSheetClass )
										.newInstance( ) );
						cSheets.add( entry );
					}
					catch ( InstantiationException e )
					{
						e.printStackTrace( );
					}
					catch ( IllegalAccessException e )
					{
						e.printStackTrace( );
					}
					catch ( ClassNotFoundException e )
					{
						// TODO remove comment
						// e.printStackTrace();
					}
				}
			}
		}
		return cSheets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIExtensions#getUIChartTypeExtensions()
	 */
	public Collection getUIChartTypeExtensions( )
	{
		if ( cChartTypes == null )
		{
			cChartTypes = new Vector( );
			if ( UIHelper.isEclipseMode( ) )
			{
				IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry( );
				IExtensionPoint extensionPoint = pluginRegistry.getExtensionPoint( "org.eclipse.birt.chart.ui", "types" ); //$NON-NLS-1$ //$NON-NLS-2$
				IExtension[] extensions = extensionPoint.getExtensions( );

				for ( int iC = 0; iC < extensions.length; iC++ )
				{
					IExtension extension = extensions[iC];
					IConfigurationElement[] configElements = extension.getConfigurationElements( );
					for ( int i = 0; i < configElements.length; i++ )
					{
						IConfigurationElement currentTag = configElements[i];
						if ( currentTag.getName( ).equals( "chartType" ) ) //$NON-NLS-1$
						{
							try
							{
								cChartTypes.add( currentTag.createExecutableExtension( "classDefinition" ) ); //$NON-NLS-1$
							}
							catch ( FrameworkException e1 )
							{
								e1.printStackTrace( );
							}
						}
					}
				}
			}
			else
			{
				for ( int iC = 0; iC < saTypes.length; iC++ )
				{
					try
					{
						cChartTypes.add( Class.forName( saTypes[iC] )
								.newInstance( ) );
					}
					catch ( InstantiationException e )
					{
						e.printStackTrace( );
					}
					catch ( IllegalAccessException e )
					{
						e.printStackTrace( );
					}
					catch ( ClassNotFoundException e )
					{
						e.printStackTrace( );
					}
				}
			}
		}
		return cChartTypes;
	}

	public Collection getUIListeners( )
	{
		if ( cListeners == null )
		{
			cListeners = new Vector( );
			if ( UIHelper.isEclipseMode( ) )
			{
				IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry( );
				IExtensionPoint extensionPoint = pluginRegistry.getExtensionPoint( "org.eclipse.birt.chart.ui", //$NON-NLS-1$
						"changelisteners" ); //$NON-NLS-1$
				IExtension[] extensions = extensionPoint.getExtensions( );
				for ( int iC = 0; iC < extensions.length; iC++ )
				{
					IExtension extension = extensions[iC];
					IConfigurationElement[] configElements = extension.getConfigurationElements( );
					for ( int i = 0; i < configElements.length; i++ )
					{
						IConfigurationElement currentTag = configElements[i];
						if ( currentTag.getName( ).equals( "changeListener" ) ) //$NON-NLS-1$
						{
							try
							{
								cListeners.add( currentTag.createExecutableExtension( "listenerClassDefinition" ) ); //$NON-NLS-1$
							}
							catch ( FrameworkException e )
							{
								e.printStackTrace( );
							}
						}
					}
				}
			}
			else
			{
				for ( int iC = 0; iC < saListeners.length; iC++ )
				{
					try
					{
						cListeners.add( Class.forName( saListeners[iC] )
								.newInstance( ) );
					}
					catch ( InstantiationException e )
					{
						e.printStackTrace( );
					}
					catch ( IllegalAccessException e )
					{
						e.printStackTrace( );
					}
					catch ( ClassNotFoundException e )
					{
						e.printStackTrace( );
					}
				}
			}
		}
		return cListeners;
	}

	public Collection getSeriesUIComponents( )
	{
		if ( cSeriesUI == null )
		{
			cSeriesUI = new Vector( );
			if ( UIHelper.isEclipseMode( ) )
			{
				IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry( );
				IExtensionPoint extensionPoint = pluginRegistry.getExtensionPoint( "org.eclipse.birt.chart.ui", //$NON-NLS-1$
						"seriescomposites" ); //$NON-NLS-1$
				IExtension[] extensions = extensionPoint.getExtensions( );

				for ( int iC = 0; iC < extensions.length; iC++ )
				{
					IExtension extension = extensions[iC];
					IConfigurationElement[] configElements = extension.getConfigurationElements( );
					for ( int i = 0; i < configElements.length; i++ )
					{
						IConfigurationElement currentTag = configElements[i];
						if ( currentTag.getName( ).equals( "seriescomposite" ) ) //$NON-NLS-1$
						{
							try
							{
								cSeriesUI.add( currentTag.createExecutableExtension( "seriesUIProvider" ) ); //$NON-NLS-1$
							}
							catch ( FrameworkException e1 )
							{
								e1.printStackTrace( );
							}
						}
					}
				}
			}
			else
			{
				for ( int iC = 0; iC < saSeriesUI.length; iC++ )
				{
					try
					{
						cSeriesUI.add( Class.forName( saSeriesUI[iC] )
								.newInstance( ) );
					}
					catch ( InstantiationException e )
					{
						e.printStackTrace( );
					}
					catch ( IllegalAccessException e )
					{
						e.printStackTrace( );
					}
					catch ( ClassNotFoundException e )
					{
						e.printStackTrace( );
					}
				}
			}
		}
		return cSeriesUI;
	}
}
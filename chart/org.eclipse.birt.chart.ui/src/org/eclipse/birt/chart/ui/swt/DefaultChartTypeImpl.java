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

package org.eclipse.birt.chart.ui.swt;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IHelpContent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.swt.graphics.Image;

/**
 * DefaultChartTypeImpl
 */
public class DefaultChartTypeImpl implements IChartType
{

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getDisplayName()
	 */
	public String getDisplayName() 
	{
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getName()
	 */
	public String getName( )
	{
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getImage()
	 */
	public Image getImage( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getChartSubtypes(java.lang.String,
	 *      org.eclipse.birt.chart.model.attribute.Orientation)
	 */
	public Collection getChartSubtypes( String Dimension,
			Orientation orientation )
	{
		return new Vector( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#canAdapt(org.eclipse.birt.chart.model.Chart,
	 *      java.util.Hashtable)
	 */
	public boolean canAdapt( Chart cModel, Hashtable htModelHints )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getModel(java.lang.String,
	 *      org.eclipse.birt.chart.model.attribute.Orientation,
	 *      java.lang.String, org.eclipse.birt.chart.model.Chart)
	 */
	public Chart getModel( String sType, Orientation Orientation,
			String Dimension, Chart currentChart )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSupportedDimensions()
	 */
	public String[] getSupportedDimensions( )
	{
		return new String[]{
			TWO_DIMENSION_TYPE
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getDefaultDimension()
	 */
	public String getDefaultDimension( )
	{
		return TWO_DIMENSION_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#supportsTransposition()
	 */
	public boolean supportsTransposition( )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#supportsTransposition(java.lang.String)
	 */
	public boolean supportsTransposition( String dimension )
	{
		return supportsTransposition( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getHelp()
	 */
	public IHelpContent getHelp( )
	{
		return new HelpContentImpl( "{Title}", "{Description}" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public ISelectDataComponent getBaseUI( Chart chart,
			ISelectDataCustomizeUI selectDataUI, IUIServiceProvider builder,
			Object oContext, String sTitle )
	{
		return new DefaultSelectDataComponent( );
	}

}
/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.script.internal;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.script.api.ChartComponentFactory;
import org.eclipse.birt.chart.script.api.IChart;
import org.eclipse.birt.chart.script.api.attribute.ILabel;
import org.eclipse.birt.chart.script.api.attribute.IText;
import org.eclipse.birt.chart.script.api.component.ILegend;
import org.eclipse.birt.chart.script.internal.component.LegendImpl;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.MultiRowItem;

/**
 * 
 */

public abstract class ChartImpl extends MultiRowItem implements IChart
{

	protected static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$
	protected Chart cm;
	protected ExtendedItemHandle eih;

	protected ChartImpl( ExtendedItemHandle eih, Chart cm )
	{
		super( eih );
		this.eih = eih;
		this.cm = cm;
	}

	public IText getDescription( )
	{
		return ChartComponentFactory.convertText( cm.getDescription( ) );
	}

	public ILegend getLegend( )
	{
		return new LegendImpl( cm.getLegend( ) );
	}

	public String getOutputType( )
	{
		return (String) eih.getProperty( "outputFormat" ); //$NON-NLS-1$
	}

	public ILabel getTitle( )
	{
		return ChartComponentFactory.convertLabel( cm.getTitle( ).getLabel( ) );
	}

	public boolean isColorByCategory( )
	{
		return cm.getLegend( ).getItemType( ) == LegendItemType.CATEGORIES_LITERAL;
	}

	public void setColorByCategory( boolean byCategory )
	{
		cm.getLegend( ).setItemType( byCategory
				? LegendItemType.CATEGORIES_LITERAL
				: LegendItemType.SERIES_LITERAL );
	}

	public void setDescription( IText label )
	{
		cm.setDescription( ChartComponentFactory.convertIText( label ) );
	}

	public void setOutputType( String type )
	{
		try
		{
			if ( !ChartUtil.isOutputFormatSupport( type ) )
			{
				type = "SVG"; //$NON-NLS-1$
			}
			eih.setProperty( "outputFormat", type );//$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			logger.log( e );
		}
		catch ( ChartException e )
		{
			logger.log( e );
		}
	}

	public void setTitle( ILabel title )
	{
		cm.getTitle( ).setLabel( ChartComponentFactory.convertILabel( title ) );
	}

	public String getDimension( )
	{
		return cm.getDimension( ).getName( );
	}

	public void setDimension( String dimensionName )
	{
		cm.setDimension( ChartDimension.getByName( dimensionName ) );
	}

}

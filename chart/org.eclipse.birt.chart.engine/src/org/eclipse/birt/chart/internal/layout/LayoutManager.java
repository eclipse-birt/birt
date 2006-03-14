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

package org.eclipse.birt.chart.internal.layout;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;

import com.ibm.icu.util.ULocale;

/**
 * 
 */
public final class LayoutManager
{

	// private transient final Block blRoot;

	/**
	 * 
	 * @param _blRoot
	 */
	public LayoutManager( Block _blRoot )
	{
		// blRoot = _blRoot;
	}

	/**
	 * 
	 * @param bo
	 * @param cm
	 * 
	 * @throws OverlapException
	 */
	public final void doLayout_tmp( IDisplayServer xs, Chart cm, Bounds boFull,
			RunTimeContext rtc ) throws ChartException
	{
		final boolean isRightToLeft = rtc.isRightToLeft( );

		Block bl = cm.getBlock( );
		bl.setBounds( boFull );
		Insets ins = bl.getInsets( );

		Bounds bo = boFull.adjustedInstance( ins );
		Legend lg = cm.getLegend( );
		Plot p = cm.getPlot( );

		TitleBlock tb = cm.getTitle( );
		Size szTitle = ( !tb.isVisible( ) ) ? SizeImpl.create( 0, 0 )
				: tb.getPreferredSize( xs, cm, rtc );

		Bounds boPlot = p.getBounds( );
		Bounds boLegend = lg.getBounds( );

		// always layout title first.
		Bounds boTitle = tb.getBounds( );
		boTitle.setLeft( bo.getLeft( ) );
		boTitle.setWidth( bo.getWidth( ) );
		boTitle.setTop( bo.getTop( ) );
		boTitle.setHeight( szTitle.getHeight( ) );

		Size szLegend = ( !lg.isVisible( ) ) ? SizeImpl.create( 0, 0 )
				: lg.getPreferredSize( xs, cm, rtc );

		if ( !lg.isSetPosition( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					Messages.getString( "error.legend.position", //$NON-NLS-1$
							ULocale.getDefault( ) ) );
		}

		double plotWidthHint = -1, plotHeightHint = -1;
		if ( p.isSetWidthHint( ) )
		{
			plotWidthHint = p.getWidthHint( );
		}
		if ( p.isSetHeightHint( ) )
		{
			plotHeightHint = p.getHeightHint( );
		}

		double plotLeft, plotTop;

		// SETUP THE POSITION OF THE LEGEND AND THE PLOT
		Position po = lg.getPosition( );

		// swap right/left
		if ( isRightToLeft )
		{
			if ( po == Position.RIGHT_LITERAL )
			{
				po = Position.LEFT_LITERAL;
			}
			else if ( po == Position.LEFT_LITERAL )
			{
				po = Position.RIGHT_LITERAL;
			}
		}

		Anchor anchor = p.getAnchor( );

		// swap west/east
		if ( isRightToLeft )
		{
			switch ( anchor.getValue( ) )
			{
				case Anchor.EAST :
					anchor = Anchor.WEST_LITERAL;
					break;
				case Anchor.NORTH_EAST :
					anchor = Anchor.NORTH_WEST_LITERAL;
					break;
				case Anchor.SOUTH_EAST :
					anchor = Anchor.SOUTH_WEST_LITERAL;
					break;
				case Anchor.WEST :
					anchor = Anchor.EAST_LITERAL;
					break;
				case Anchor.NORTH_WEST :
					anchor = Anchor.NORTH_EAST_LITERAL;
					break;
				case Anchor.SOUTH_WEST :
					anchor = Anchor.SOUTH_EAST_LITERAL;
					break;
			}
		}

		switch ( po.getValue( ) )
		{
			case Position.INSIDE :

				boPlot.setWidth( plotWidthHint < 0 ? bo.getWidth( )
						: plotWidthHint );
				boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( ) - szTitle.getHeight( ) )
						: plotHeightHint );

				plotLeft = bo.getLeft( );
				plotTop = bo.getTop( ) + szTitle.getHeight( );

				// adjust left.
				switch ( anchor.getValue( ) )
				{
					case Anchor.EAST :
					case Anchor.NORTH_EAST :
					case Anchor.SOUTH_EAST :
						plotLeft = plotLeft
								+ bo.getWidth( )
								- boPlot.getWidth( );
						break;
					case Anchor.NORTH :
					case Anchor.SOUTH :
						plotLeft = plotLeft
								+ ( bo.getWidth( ) - boPlot.getWidth( ) )
								/ 2;
						break;
				}

				// adjust top.
				switch ( anchor.getValue( ) )
				{
					case Anchor.SOUTH :
					case Anchor.SOUTH_WEST :
					case Anchor.SOUTH_EAST :
						plotTop = plotTop
								+ bo.getHeight( )
								- szTitle.getHeight( )
								- boPlot.getHeight( );
						break;
					case Anchor.WEST :
					case Anchor.EAST :
						plotTop = plotTop
								+ ( bo.getHeight( ) - szTitle.getHeight( ) - boPlot.getHeight( ) )
								/ 2;
						break;
				}

				boPlot.setLeft( plotLeft );
				boPlot.setTop( plotTop );

				boLegend.set( 0, 0, szLegend.getWidth( ), szLegend.getHeight( ) );
				break;

			case Position.RIGHT :
			case Position.OUTSIDE :
				boLegend.setTop( bo.getTop( ) + szTitle.getHeight( ) );
				boLegend.setLeft( bo.getLeft( )
						+ bo.getWidth( )
						- szLegend.getWidth( ) );
				boLegend.setWidth( szLegend.getWidth( ) );
				boLegend.setHeight( bo.getHeight( ) - szTitle.getHeight( ) );

				boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( ) - boLegend.getWidth( ) )
						: plotWidthHint );
				boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( ) - szTitle.getHeight( ) )
						: plotHeightHint );

				plotLeft = bo.getLeft( );
				plotTop = bo.getTop( ) + szTitle.getHeight( );

				// adjust left.
				switch ( anchor.getValue( ) )
				{
					case Anchor.EAST :
					case Anchor.NORTH_EAST :
					case Anchor.SOUTH_EAST :
						plotLeft = plotLeft
								+ bo.getWidth( )
								- boLegend.getWidth( )
								- boPlot.getWidth( );
						break;
					case Anchor.NORTH :
					case Anchor.SOUTH :
						plotLeft = plotLeft
								+ ( bo.getWidth( ) - boLegend.getWidth( ) - boPlot.getWidth( ) )
								/ 2;
						break;
				}

				// adjust top.
				switch ( anchor.getValue( ) )
				{
					case Anchor.SOUTH :
					case Anchor.SOUTH_WEST :
					case Anchor.SOUTH_EAST :
						plotTop = plotTop
								+ bo.getHeight( )
								- szTitle.getHeight( )
								- boPlot.getHeight( );
						break;
					case Anchor.WEST :
					case Anchor.EAST :
						plotTop = plotTop
								+ ( bo.getHeight( ) - szTitle.getHeight( ) - boPlot.getHeight( ) )
								/ 2;
						break;
				}

				boPlot.setLeft( plotLeft );
				boPlot.setTop( plotTop );

				break;

			case Position.LEFT :
				boLegend.setTop( bo.getTop( ) + szTitle.getHeight( ) );
				boLegend.setLeft( bo.getLeft( ) );
				boLegend.setWidth( szLegend.getWidth( ) );
				boLegend.setHeight( bo.getHeight( ) - szTitle.getHeight( ) );

				boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( ) - boLegend.getWidth( ) )
						: plotWidthHint );
				boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( ) - szTitle.getHeight( ) )
						: plotHeightHint );

				plotLeft = bo.getLeft( ) + szLegend.getWidth( );
				plotTop = bo.getTop( ) + szTitle.getHeight( );

				// adjust left.
				switch ( anchor.getValue( ) )
				{
					case Anchor.EAST :
					case Anchor.NORTH_EAST :
					case Anchor.SOUTH_EAST :
						plotLeft = plotLeft
								+ bo.getWidth( )
								- boLegend.getWidth( )
								- boPlot.getWidth( );
						break;
					case Anchor.NORTH :
					case Anchor.SOUTH :
						plotLeft = plotLeft
								+ ( bo.getWidth( ) - boLegend.getWidth( ) - boPlot.getWidth( ) )
								/ 2;
						break;
				}

				// adjust top.
				switch ( anchor.getValue( ) )
				{
					case Anchor.SOUTH :
					case Anchor.SOUTH_WEST :
					case Anchor.SOUTH_EAST :
						plotTop = plotTop
								+ bo.getHeight( )
								- szTitle.getHeight( )
								- boPlot.getHeight( );
						break;
					case Anchor.WEST :
					case Anchor.EAST :
						plotTop = plotTop
								+ ( bo.getHeight( ) - szTitle.getHeight( ) - boPlot.getHeight( ) )
								/ 2;
						break;
				}

				boPlot.setLeft( plotLeft );
				boPlot.setTop( plotTop );

				break;

			case Position.ABOVE :
				boLegend.setTop( bo.getTop( ) + szTitle.getHeight( ) );
				boLegend.setLeft( bo.getLeft( ) );
				boLegend.setWidth( bo.getWidth( ) );
				boLegend.setHeight( szLegend.getHeight( ) );

				boPlot.setWidth( plotWidthHint < 0 ? bo.getWidth( )
						: plotWidthHint );
				boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( )
						- szTitle.getHeight( ) - boLegend.getHeight( ) )
						: plotHeightHint );

				plotLeft = bo.getLeft( );
				plotTop = bo.getTop( )
						+ szTitle.getHeight( )
						+ boLegend.getHeight( );

				// adjust left.
				switch ( anchor.getValue( ) )
				{
					case Anchor.EAST :
					case Anchor.NORTH_EAST :
					case Anchor.SOUTH_EAST :
						plotLeft = plotLeft
								+ bo.getWidth( )
								- boPlot.getWidth( );
						break;
					case Anchor.NORTH :
					case Anchor.SOUTH :
						plotLeft = plotLeft
								+ ( bo.getWidth( ) - boPlot.getWidth( ) )
								/ 2;
						break;
				}

				// adjust top.
				switch ( anchor.getValue( ) )
				{
					case Anchor.SOUTH :
					case Anchor.SOUTH_WEST :
					case Anchor.SOUTH_EAST :
						plotTop = plotTop
								+ bo.getHeight( )
								- szTitle.getHeight( )
								- boLegend.getHeight( )
								- boPlot.getHeight( );
						break;
					case Anchor.WEST :
					case Anchor.EAST :
						plotTop = plotTop
								+ ( bo.getHeight( )
										- szTitle.getHeight( )
										- boLegend.getHeight( ) - boPlot.getHeight( ) )
								/ 2;
						break;
				}

				boPlot.setLeft( plotLeft );
				boPlot.setTop( plotTop );

				break;

			case Position.BELOW :
				boLegend.setTop( bo.getTop( )
						+ bo.getHeight( )
						- szLegend.getHeight( ) );
				boLegend.setLeft( bo.getLeft( ) );
				boLegend.setWidth( bo.getWidth( ) );
				boLegend.setHeight( szLegend.getHeight( ) );

				boPlot.setWidth( plotWidthHint < 0 ? bo.getWidth( )
						: plotWidthHint );
				boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( )
						- boTitle.getHeight( ) - boLegend.getHeight( ) )
						: plotHeightHint );

				plotLeft = bo.getLeft( );
				plotTop = bo.getTop( ) + szTitle.getHeight( );

				// adjust left.
				switch ( anchor.getValue( ) )
				{
					case Anchor.EAST :
					case Anchor.NORTH_EAST :
					case Anchor.SOUTH_EAST :
						plotLeft = plotLeft
								+ bo.getWidth( )
								- boPlot.getWidth( );
						break;
					case Anchor.NORTH :
					case Anchor.SOUTH :
						plotLeft = plotLeft
								+ ( bo.getWidth( ) - boPlot.getWidth( ) )
								/ 2;
						break;
				}

				// adjust top.
				switch ( anchor.getValue( ) )
				{
					case Anchor.SOUTH :
					case Anchor.SOUTH_WEST :
					case Anchor.SOUTH_EAST :
						plotTop = plotTop
								+ bo.getHeight( )
								- szTitle.getHeight( )
								- boLegend.getHeight( )
								- boPlot.getHeight( );
						break;
					case Anchor.WEST :
					case Anchor.EAST :
						plotTop = plotTop
								+ ( bo.getHeight( )
										- szTitle.getHeight( )
										- boLegend.getHeight( ) - boPlot.getHeight( ) )
								/ 2;
						break;
				}

				boPlot.setLeft( plotLeft );
				boPlot.setTop( plotTop );

				break;
		}

	}

	/**
	 * This method recursively walks down the chart layout and establishes
	 * bounds for each contained block based on the following rule:
	 * 
	 * All immediate children under 'blRoot' are added as ElasticLayout with
	 * appropriate constraints All other children (at deeper levels) are added
	 * as NullLayout with fixed 'relative' bounds
	 * 
	 * NOTE: This method is incomplete and not currently referenced
	 * 
	 * @param bo
	 * @throws OverlapException
	 */
	public final void doLayout( IDisplayServer xs, Chart cm, Bounds bo )
			throws ChartException
	{
		// TBD
	}
}
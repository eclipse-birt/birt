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

import java.util.Iterator;

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
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;

import com.ibm.icu.util.ULocale;

/**
 * A default layout policy implementation
 */
public final class LayoutManager
{

	/**
	 * The constructor.
	 * 
	 * @param _blRoot
	 */
	public LayoutManager( Block _blRoot )
	{
	}

	private void doLayout_tmp( IDisplayServer xs, Chart cm, Bounds boFull,
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

		// Layout title in North Anchor by default.
		Bounds boTitle = tb.getBounds( );
		Anchor titleAnchor = tb.getAnchor( );
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

		// restrict lenged size to 1/3 of the total block size.
		// TODO use better layout solution

		final double renderLegendHeight = szLegend.getHeight( );
		final double renderLegendWidth = szLegend.getWidth( );

		switch ( po.getValue( ) )
		{
			case Position.ABOVE :
			case Position.BELOW :
				// restrict height
				if ( szLegend.getHeight( ) > bo.getHeight( ) / 3 )
				{
					szLegend.setHeight( bo.getHeight( ) / 3 );
				}
				break;
			case Position.LEFT :
			case Position.RIGHT :
			case Position.OUTSIDE :
				// restrict width
				if ( szLegend.getWidth( ) > bo.getWidth( ) / 3 )
				{
					szLegend.setWidth( bo.getWidth( ) / 3 );
				}
				break;
			default :
				// ignore other cases
				break;
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
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.EAST :
					case Anchor.WEST :
						boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( ) - szTitle.getWidth( ) )
								: plotWidthHint );
						boPlot.setHeight( plotHeightHint < 0 ? bo.getHeight( )
								: plotHeightHint );
						boTitle.setHeight( bo.getHeight( ) );
						boTitle.setWidth( szTitle.getWidth( ) );
						break;
					case Anchor.SOUTH :
					case Anchor.SOUTH_EAST :
					case Anchor.SOUTH_WEST :
						boTitle.setTop( bo.getTop( )
								+ bo.getHeight( )
								- szTitle.getHeight( ) );
						break;
				}

				plotLeft = bo.getLeft( );
				plotTop = bo.getTop( );
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
						plotLeft = bo.getLeft( ) + szTitle.getWidth( );
						break;
					case Anchor.EAST :
						boTitle.setLeft( bo.getLeft( )
								+ bo.getWidth( )
								- szTitle.getWidth( ) );
						break;
					case Anchor.NORTH :
					case Anchor.NORTH_EAST :
					case Anchor.NORTH_WEST :
						plotTop = bo.getTop( ) + szTitle.getHeight( );
						break;
				}

				// adjust left.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( anchor.getValue( ) )
						{
							case Anchor.EAST :
							case Anchor.NORTH_EAST :
							case Anchor.SOUTH_EAST :
								plotLeft = plotLeft
										+ bo.getWidth( )
										- szTitle.getWidth( )
										- boPlot.getWidth( );
								break;
							case Anchor.NORTH :
							case Anchor.SOUTH :
								plotLeft = plotLeft
										+ ( bo.getWidth( ) - szTitle.getWidth( ) - boPlot.getWidth( ) )
										/ 2;
								break;
						}
						break;
					default :
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
						break;
				}

				// adjust top.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( anchor.getValue( ) )
						{
							case Anchor.SOUTH :
							case Anchor.SOUTH_WEST :
							case Anchor.SOUTH_EAST :
								plotTop = plotTop
										+ bo.getHeight( )
										- boPlot.getHeight( );
								break;
							case Anchor.WEST :
							case Anchor.EAST :
								plotTop = plotTop
										+ ( bo.getHeight( ) - boPlot.getHeight( ) )
										/ 2;
								break;
						}
						break;
					default :
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
										+ ( bo.getHeight( )
												- szTitle.getHeight( ) - boPlot.getHeight( ) )
										/ 2;
								break;
						}
						break;
				}

				boPlot.setLeft( plotLeft );
				boPlot.setTop( plotTop );

				boLegend.set( 0, 0, szLegend.getWidth( ), szLegend.getHeight( ) );
				break;

			case Position.RIGHT :
			case Position.OUTSIDE :
				boLegend.setWidth( szLegend.getWidth( ) );
				boLegend.setHeight( bo.getHeight( ) - szTitle.getHeight( ) );
				boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( ) - boLegend.getWidth( ) )
						: plotWidthHint );
				boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( ) - szTitle.getHeight( ) )
						: plotHeightHint );

				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.EAST :
					case Anchor.WEST :
						boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( )
								- boLegend.getWidth( ) - szTitle.getWidth( ) )
								: plotWidthHint );
						boPlot.setHeight( plotHeightHint < 0 ? bo.getHeight( )
								: plotHeightHint );
						boTitle.setHeight( bo.getHeight( ) );
						boTitle.setWidth( szTitle.getWidth( ) );
						boLegend.setHeight( bo.getHeight( ) );
						break;
					case Anchor.SOUTH :
					case Anchor.SOUTH_EAST :
					case Anchor.SOUTH_WEST :
						boTitle.setTop( bo.getTop( )
								+ bo.getHeight( )
								- szTitle.getHeight( ) );
						break;
				}

				boLegend.setTop( bo.getTop( ) );
				boLegend.setLeft( bo.getLeft( )
						+ bo.getWidth( )
						- szLegend.getWidth( ) );

				plotLeft = bo.getLeft( );
				plotTop = bo.getTop( );
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
						plotLeft = bo.getLeft( ) + szTitle.getWidth( );
						break;
					case Anchor.EAST :
						boTitle.setLeft( bo.getLeft( )
								+ bo.getWidth( )
								- szTitle.getWidth( ) );
						boLegend.setLeft( boTitle.getLeft( )
								- szLegend.getWidth( ) );
						break;
					case Anchor.NORTH :
					case Anchor.NORTH_EAST :
					case Anchor.NORTH_WEST :
						plotTop = bo.getTop( ) + szTitle.getHeight( );
						boLegend.setTop( bo.getTop( ) + szTitle.getHeight( ) );
						break;
				}

				// adjust left.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( anchor.getValue( ) )
						{
							case Anchor.EAST :
							case Anchor.NORTH_EAST :
							case Anchor.SOUTH_EAST :
								plotLeft = plotLeft
										+ bo.getWidth( )
										- szTitle.getWidth( )
										- boLegend.getWidth( )
										- boPlot.getWidth( );
								break;
							case Anchor.NORTH :
							case Anchor.SOUTH :
								plotLeft = plotLeft
										+ ( bo.getWidth( )
												- szTitle.getWidth( )
												- boLegend.getWidth( ) - boPlot.getWidth( ) )
										/ 2;
								break;
						}
						break;
					default :
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
										+ ( bo.getWidth( )
												- boLegend.getWidth( ) - boPlot.getWidth( ) )
										/ 2;
								break;
						}
						break;
				}

				// adjust top.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( anchor.getValue( ) )
						{
							case Anchor.SOUTH :
							case Anchor.SOUTH_WEST :
							case Anchor.SOUTH_EAST :
								plotTop = plotTop
										+ bo.getHeight( )
										- boPlot.getHeight( );
								break;
							case Anchor.WEST :
							case Anchor.EAST :
								plotTop = plotTop
										+ ( bo.getHeight( ) - boPlot.getHeight( ) )
										/ 2;
								break;
						}
						break;
					default :
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
										+ ( bo.getHeight( )
												- szTitle.getHeight( ) - boPlot.getHeight( ) )
										/ 2;
								break;
						}
						break;
				}

				boPlot.setLeft( plotLeft );
				boPlot.setTop( plotTop );

				break;

			case Position.LEFT :
				boLegend.setWidth( renderLegendWidth );
				boLegend.setHeight( bo.getHeight( ) - szTitle.getHeight( ) );

				boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( ) - boLegend.getWidth( ) )
						: plotWidthHint );
				boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( ) - szTitle.getHeight( ) )
						: plotHeightHint );

				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.EAST :
					case Anchor.WEST :
						boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( )
								- boLegend.getWidth( ) - szTitle.getWidth( ) )
								: plotWidthHint );
						boPlot.setHeight( plotHeightHint < 0 ? bo.getHeight( )
								: plotHeightHint );
						boTitle.setHeight( bo.getHeight( ) );
						boTitle.setWidth( szTitle.getWidth( ) );
						boLegend.setHeight( bo.getHeight( ) );
						break;
					case Anchor.SOUTH :
					case Anchor.SOUTH_EAST :
					case Anchor.SOUTH_WEST :
						boTitle.setTop( bo.getTop( )
								+ bo.getHeight( )
								- szTitle.getHeight( ) );
						break;
				}

				boLegend.setTop( bo.getTop( ) );
				boLegend.setLeft( bo.getLeft( )
						+ szLegend.getWidth( )
						- renderLegendWidth );

				plotLeft = bo.getLeft( ) + szLegend.getWidth( );
				plotTop = bo.getTop( );
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
						boLegend.setLeft( bo.getLeft( )
								+ szTitle.getWidth( )
								+ szLegend.getWidth( )
								- renderLegendWidth );
						plotLeft = bo.getLeft( )
								+ szTitle.getWidth( )
								+ szLegend.getWidth( );
						break;
					case Anchor.EAST :
						boTitle.setLeft( bo.getLeft( )
								+ bo.getWidth( )
								- szTitle.getWidth( ) );
						break;
					case Anchor.NORTH :
					case Anchor.NORTH_EAST :
					case Anchor.NORTH_WEST :
						plotTop = bo.getTop( ) + szTitle.getHeight( );
						boLegend.setTop( bo.getTop( ) + szTitle.getHeight( ) );
						break;
				}

				// adjust left.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( anchor.getValue( ) )
						{
							case Anchor.EAST :
							case Anchor.NORTH_EAST :
							case Anchor.SOUTH_EAST :
								plotLeft = plotLeft
										+ bo.getWidth( )
										- szTitle.getWidth( )
										- boLegend.getWidth( )
										- boPlot.getWidth( );
								break;
							case Anchor.NORTH :
							case Anchor.SOUTH :
								plotLeft = plotLeft
										+ ( bo.getWidth( )
												- szTitle.getWidth( )
												- boLegend.getWidth( ) - boPlot.getWidth( ) )
										/ 2;
								break;
						}
						break;
					default :
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
										+ ( bo.getWidth( )
												- boLegend.getWidth( ) - boPlot.getWidth( ) )
										/ 2;
								break;
						}
						break;
				}

				// adjust top.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( anchor.getValue( ) )
						{
							case Anchor.SOUTH :
							case Anchor.SOUTH_WEST :
							case Anchor.SOUTH_EAST :
								plotTop = plotTop
										+ bo.getHeight( )
										- boPlot.getHeight( );
								break;
							case Anchor.WEST :
							case Anchor.EAST :
								plotTop = plotTop
										+ ( bo.getHeight( ) - boPlot.getHeight( ) )
										/ 2;
								break;
						}
						break;
					default :
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
										+ ( bo.getHeight( )
												- szTitle.getHeight( ) - boPlot.getHeight( ) )
										/ 2;
								break;
						}
						break;
				}

				boPlot.setLeft( plotLeft );
				boPlot.setTop( plotTop );

				break;

			case Position.ABOVE :
				boLegend.setWidth( bo.getWidth( ) );
				boLegend.setHeight( renderLegendHeight );

				boPlot.setWidth( plotWidthHint < 0 ? bo.getWidth( )
						: plotWidthHint );
				boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( )
						- szTitle.getHeight( ) - szLegend.getHeight( ) )
						: plotHeightHint );

				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.EAST :
					case Anchor.WEST :
						boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( ) - szTitle.getWidth( ) )
								: plotWidthHint );
						boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( ) - boLegend.getHeight( ) )
								: plotHeightHint );
						boTitle.setHeight( bo.getHeight( ) );
						boTitle.setWidth( szTitle.getWidth( ) );
						boLegend.setWidth( bo.getWidth( ) - szTitle.getWidth( ) );
						break;
					case Anchor.SOUTH :
					case Anchor.SOUTH_EAST :
					case Anchor.SOUTH_WEST :
						boTitle.setTop( bo.getTop( )
								+ bo.getHeight( )
								- szTitle.getHeight( ) );
						break;
				}

				boLegend.setTop( bo.getTop( )
						+ szLegend.getHeight( )
						- renderLegendHeight );
				boLegend.setLeft( bo.getLeft( ) );
				plotLeft = bo.getLeft( );
				plotTop = bo.getTop( ) + szLegend.getHeight( );

				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
						boLegend.setLeft( bo.getLeft( ) + szTitle.getWidth( ) );
						plotLeft = bo.getLeft( ) + szTitle.getWidth( );
						break;
					case Anchor.EAST :
						boTitle.setLeft( bo.getLeft( )
								+ bo.getWidth( )
								- szTitle.getWidth( ) );
						break;
					case Anchor.NORTH :
					case Anchor.NORTH_EAST :
					case Anchor.NORTH_WEST :
						plotTop = bo.getTop( )
								+ szLegend.getHeight( )
								+ szTitle.getHeight( );
						boLegend.setTop( bo.getTop( )
								+ szTitle.getHeight( )
								+ szLegend.getHeight( )
								- renderLegendHeight );
						break;
				}

				// adjust left.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( anchor.getValue( ) )
						{
							case Anchor.EAST :
							case Anchor.NORTH_EAST :
							case Anchor.SOUTH_EAST :
								plotLeft = plotLeft
										+ bo.getWidth( )
										- szTitle.getWidth( )
										- boPlot.getWidth( );
								break;
							case Anchor.NORTH :
							case Anchor.SOUTH :
								plotLeft = plotLeft
										+ ( bo.getWidth( ) - szTitle.getWidth( ) - boPlot.getWidth( ) )
										/ 2;
								break;
						}
						break;
					default :
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
						break;
				}

				// adjust top.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( anchor.getValue( ) )
						{
							case Anchor.SOUTH :
							case Anchor.SOUTH_WEST :
							case Anchor.SOUTH_EAST :
								plotTop = plotTop
										+ bo.getHeight( )
										- boLegend.getHeight( )
										- boPlot.getHeight( );
								break;
							case Anchor.WEST :
							case Anchor.EAST :
								plotTop = plotTop
										+ ( bo.getHeight( )
												- boLegend.getHeight( ) - boPlot.getHeight( ) )
										/ 2;
								break;
						}
						break;
					default :
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
						break;
				}

				boPlot.setLeft( plotLeft );
				boPlot.setTop( plotTop );

				break;

			case Position.BELOW :
				boLegend.setWidth( bo.getWidth( ) );
				boLegend.setHeight( szLegend.getHeight( ) );

				boPlot.setWidth( plotWidthHint < 0 ? bo.getWidth( )
						: plotWidthHint );
				boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( )
						- boTitle.getHeight( ) - boLegend.getHeight( ) )
						: plotHeightHint );

				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.EAST :
					case Anchor.WEST :
						boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( ) - szTitle.getWidth( ) )
								: plotWidthHint );
						boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( ) - boLegend.getHeight( ) )
								: plotHeightHint );
						boTitle.setHeight( bo.getHeight( ) );
						boTitle.setWidth( szTitle.getWidth( ) );
						boLegend.setWidth( bo.getWidth( ) - szTitle.getWidth( ) );
						break;
					case Anchor.SOUTH :
					case Anchor.SOUTH_EAST :
					case Anchor.SOUTH_WEST :
						boTitle.setTop( bo.getTop( )
								+ bo.getHeight( )
								- szTitle.getHeight( ) );
						break;
				}

				boLegend.setTop( bo.getTop( )
						+ bo.getHeight( )
						- szLegend.getHeight( ) );
				boLegend.setLeft( bo.getLeft( ) );
				plotLeft = bo.getLeft( );
				plotTop = bo.getTop( );

				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
						boLegend.setLeft( bo.getLeft( ) + szTitle.getWidth( ) );
						plotLeft = bo.getLeft( ) + szTitle.getWidth( );
						break;
					case Anchor.EAST :
						boTitle.setLeft( bo.getLeft( )
								+ bo.getWidth( )
								- szTitle.getWidth( ) );
						break;
					case Anchor.NORTH :
					case Anchor.NORTH_EAST :
					case Anchor.NORTH_WEST :
						plotTop = bo.getTop( ) + szTitle.getHeight( );
						break;
					case Anchor.SOUTH :
					case Anchor.SOUTH_EAST :
					case Anchor.SOUTH_WEST :
						boLegend.setTop( boTitle.getTop( )
								- szLegend.getHeight( ) );
						break;
				}

				// adjust left.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( anchor.getValue( ) )
						{
							case Anchor.EAST :
							case Anchor.NORTH_EAST :
							case Anchor.SOUTH_EAST :
								plotLeft = plotLeft
										+ bo.getWidth( )
										- szTitle.getWidth( )
										- boPlot.getWidth( );
								break;
							case Anchor.NORTH :
							case Anchor.SOUTH :
								plotLeft = plotLeft
										+ ( bo.getWidth( ) - szTitle.getWidth( ) - boPlot.getWidth( ) )
										/ 2;
								break;
						}
						break;
					default :
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
						break;
				}

				// adjust top.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( anchor.getValue( ) )
						{
							case Anchor.SOUTH :
							case Anchor.SOUTH_WEST :
							case Anchor.SOUTH_EAST :
								plotTop = plotTop
										+ bo.getHeight( )
										- boLegend.getHeight( )
										- boPlot.getHeight( );
								break;
							case Anchor.WEST :
							case Anchor.EAST :
								plotTop = plotTop
										+ ( bo.getHeight( )
												- boLegend.getHeight( ) - boPlot.getHeight( ) )
										/ 2;
								break;
						}
						break;
					default :
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
						break;
				}

				boPlot.setLeft( plotLeft );
				boPlot.setTop( plotTop );

				break;
		}

		// layout custom blocks.
		for ( Iterator itr = bl.getChildren( ).iterator( ); itr.hasNext( ); )
		{
			Block cbl = (Block) itr.next( );

			if ( cbl != lg && cbl != p && cbl != tb )
			{
				layoutBlock( xs, cm, bl.getBounds( ), bl.getInsets( ), cbl, rtc );
			}
		}

		// layout custom legend blocks.
		for ( Iterator itr = lg.getChildren( ).iterator( ); itr.hasNext( ); )
		{
			Block cbl = (Block) itr.next( );

			layoutBlock( xs, cm, lg.getBounds( ), lg.getInsets( ), cbl, rtc );
		}

		// layout custom title blocks.
		for ( Iterator itr = tb.getChildren( ).iterator( ); itr.hasNext( ); )
		{
			Block cbl = (Block) itr.next( );

			layoutBlock( xs, cm, tb.getBounds( ), tb.getInsets( ), cbl, rtc );
		}

		// layout custom plot blocks.
		for ( Iterator itr = p.getChildren( ).iterator( ); itr.hasNext( ); )
		{
			Block cbl = (Block) itr.next( );

			layoutBlock( xs, cm, p.getBounds( ), p.getInsets( ), cbl, rtc );
		}

	}

	private void layoutBlock( IDisplayServer xs, Chart cm, Bounds bo,
			Insets ins, Block block, RunTimeContext rtc ) throws ChartException
	{
		if ( !block.isSetAnchor( ) )
		{
			return;
		}

		Bounds cbo = block.getBounds( );

		if ( cbo == null )
		{
			cbo = BoundsImpl.create( 0, 0, 0, 0 );
		}
		else if ( cbo.getLeft( ) != 0
				|| cbo.getTop( ) != 0
				|| cbo.getWidth( ) != 0
				|| cbo.getHeight( ) != 0 )
		{
			return;
		}

		bo = bo.adjustedInstance( ins );

		Anchor anchor = block.getAnchor( );

		// swap west/east
		if ( rtc != null && rtc.isRightToLeft( ) )
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

		Size sz = block.getPreferredSize( xs, cm, rtc );

		cbo.setWidth( sz.getWidth( ) );
		cbo.setHeight( sz.getHeight( ) );

		switch ( anchor.getValue( ) )
		{
			case Anchor.EAST :
				cbo.setLeft( bo.getLeft( ) + bo.getWidth( ) - sz.getWidth( ) );
				cbo.setTop( bo.getTop( )
						+ ( bo.getHeight( ) - sz.getHeight( ) )
						/ 2 );
				break;
			case Anchor.NORTH :
				cbo.setLeft( bo.getLeft( )
						+ ( bo.getWidth( ) - sz.getWidth( ) )
						/ 2 );
				cbo.setTop( bo.getTop( ) );
				break;
			case Anchor.NORTH_EAST :
				cbo.setLeft( bo.getLeft( ) + bo.getWidth( ) - sz.getWidth( ) );
				cbo.setTop( bo.getTop( ) );
				break;
			case Anchor.NORTH_WEST :
				cbo.setLeft( bo.getLeft( ) );
				cbo.setTop( bo.getTop( ) );
				break;
			case Anchor.SOUTH :
				cbo.setLeft( bo.getLeft( )
						+ ( bo.getWidth( ) - sz.getWidth( ) )
						/ 2 );
				cbo.setTop( bo.getTop( ) + bo.getHeight( ) - sz.getHeight( ) );
				break;
			case Anchor.SOUTH_EAST :
				cbo.setLeft( bo.getLeft( ) + bo.getWidth( ) - sz.getWidth( ) );
				cbo.setTop( bo.getTop( ) + bo.getHeight( ) - sz.getHeight( ) );
				break;
			case Anchor.SOUTH_WEST :
				cbo.setLeft( bo.getLeft( ) );
				cbo.setTop( bo.getTop( ) + bo.getHeight( ) - sz.getHeight( ) );
				break;
			case Anchor.WEST :
				cbo.setLeft( bo.getLeft( ) );
				cbo.setTop( bo.getTop( )
						+ ( bo.getHeight( ) - sz.getHeight( ) )
						/ 2 );
				break;
		}

		block.setBounds( cbo );
	}

	/**
	 * This method recursively walks down the chart layout and establishes
	 * bounds for each contained block based on the following rule:
	 * 
	 * All immediate children under 'blRoot' are added as ElasticLayout with
	 * appropriate constraints All other children (at deeper levels) are added
	 * as NullLayout with fixed 'relative' bounds
	 * 
	 * @param bo
	 * @throws OverlapException
	 */
	public void doLayout( IDisplayServer xs, Chart cm, Bounds bo,
			RunTimeContext rtc ) throws ChartException
	{
		doLayout_tmp( xs, cm, bo, rtc );
	}
}
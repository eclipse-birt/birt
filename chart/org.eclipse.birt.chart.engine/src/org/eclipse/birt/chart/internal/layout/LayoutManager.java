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
		Plot pl = cm.getPlot( );

		TitleBlock tb = cm.getTitle( );
		Size szTitle = ( !tb.isVisible( ) ) ? SizeImpl.create( 0, 0 )
				: tb.getPreferredSize( xs, cm, rtc );

		Bounds boPlot = pl.getBounds( );
		Bounds boLegend = lg.getBounds( );

		// always layout title block first, for legend computing need its
		// infomation.
		Bounds boTitle = tb.getBounds( );
		Anchor titleAnchor = tb.getAnchor( );
		boTitle.setLeft( bo.getLeft( ) );
		boTitle.setTop( bo.getTop( ) );
		boTitle.setWidth( szTitle.getWidth( ) );
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
		if ( pl.isSetWidthHint( ) )
		{
			plotWidthHint = pl.getWidthHint( );
		}
		if ( pl.isSetHeightHint( ) )
		{
			plotHeightHint = pl.getHeightHint( );
		}

		double plotLeft, plotTop;

		// SETUP THE POSITION OF THE LEGEND AND THE PLOT
		Position lgPos = lg.getPosition( );

		// swap right/left
		if ( isRightToLeft )
		{
			if ( lgPos == Position.RIGHT_LITERAL )
			{
				lgPos = Position.LEFT_LITERAL;
			}
			else if ( lgPos == Position.LEFT_LITERAL )
			{
				lgPos = Position.RIGHT_LITERAL;
			}
		}

		Anchor plotAnchor = pl.getAnchor( );

		// swap west/east
		if ( isRightToLeft )
		{
			switch ( plotAnchor.getValue( ) )
			{
				case Anchor.EAST :
					plotAnchor = Anchor.WEST_LITERAL;
					break;
				case Anchor.NORTH_EAST :
					plotAnchor = Anchor.NORTH_WEST_LITERAL;
					break;
				case Anchor.SOUTH_EAST :
					plotAnchor = Anchor.SOUTH_WEST_LITERAL;
					break;
				case Anchor.WEST :
					plotAnchor = Anchor.EAST_LITERAL;
					break;
				case Anchor.NORTH_WEST :
					plotAnchor = Anchor.NORTH_EAST_LITERAL;
					break;
				case Anchor.SOUTH_WEST :
					plotAnchor = Anchor.SOUTH_EAST_LITERAL;
					break;
			}
		}

		switch ( lgPos.getValue( ) )
		{
			case Position.INSIDE :

				boPlot.setWidth( plotWidthHint < 0 ? bo.getWidth( )
						: plotWidthHint );
				boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( ) - szTitle.getHeight( ) )
						: plotHeightHint );

				plotLeft = bo.getLeft( );
				plotTop = bo.getTop( );

				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.EAST :
						boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( ) - szTitle.getWidth( ) )
								: plotWidthHint );
						boPlot.setHeight( plotHeightHint < 0 ? bo.getHeight( )
								: plotHeightHint );
						boTitle.setLeft( bo.getLeft( )
								+ bo.getWidth( )
								- szTitle.getWidth( ) );
						boTitle.setHeight( bo.getHeight( ) );
						break;
					case Anchor.WEST :
						plotLeft = bo.getLeft( ) + szTitle.getWidth( );
						boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( ) - szTitle.getWidth( ) )
								: plotWidthHint );
						boPlot.setHeight( plotHeightHint < 0 ? bo.getHeight( )
								: plotHeightHint );
						boTitle.setHeight( bo.getHeight( ) );
						break;
					case Anchor.SOUTH :
					case Anchor.SOUTH_EAST :
					case Anchor.SOUTH_WEST :
						boTitle.setTop( bo.getTop( )
								+ bo.getHeight( )
								- szTitle.getHeight( ) );
						boTitle.setWidth( bo.getWidth( ) );
						break;
					case Anchor.NORTH :
					case Anchor.NORTH_EAST :
					case Anchor.NORTH_WEST :
						plotTop = bo.getTop( ) + szTitle.getHeight( );
						boTitle.setWidth( bo.getWidth( ) );
						break;
				}

				// adjust plot left.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( plotAnchor.getValue( ) )
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
						switch ( plotAnchor.getValue( ) )
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

				// adjust plot top.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( plotAnchor.getValue( ) )
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
						switch ( plotAnchor.getValue( ) )
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

				boLegend.setTop( bo.getTop( ) );
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
				plotTop = bo.getTop( );

				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.EAST :
						boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( )
								- boLegend.getWidth( ) - szTitle.getWidth( ) )
								: plotWidthHint );
						boPlot.setHeight( plotHeightHint < 0 ? bo.getHeight( )
								: plotHeightHint );
						boTitle.setHeight( bo.getHeight( ) );
						boTitle.setLeft( bo.getLeft( )
								+ bo.getWidth( )
								- szTitle.getWidth( ) );
						boLegend.setLeft( boTitle.getLeft( )
								- szLegend.getWidth( ) );
						boLegend.setHeight( bo.getHeight( ) );
						break;
					case Anchor.WEST :
						plotLeft = bo.getLeft( ) + szTitle.getWidth( );
						boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( )
								- boLegend.getWidth( ) - szTitle.getWidth( ) )
								: plotWidthHint );
						boPlot.setHeight( plotHeightHint < 0 ? bo.getHeight( )
								: plotHeightHint );
						boTitle.setHeight( bo.getHeight( ) );
						boLegend.setHeight( bo.getHeight( ) );
						break;
					case Anchor.SOUTH :
					case Anchor.SOUTH_EAST :
					case Anchor.SOUTH_WEST :
						boTitle.setTop( bo.getTop( )
								+ bo.getHeight( )
								- szTitle.getHeight( ) );
						boTitle.setWidth( bo.getWidth( ) );
						break;
					case Anchor.NORTH :
					case Anchor.NORTH_EAST :
					case Anchor.NORTH_WEST :
						plotTop = bo.getTop( ) + szTitle.getHeight( );
						boLegend.setTop( bo.getTop( ) + szTitle.getHeight( ) );
						boTitle.setWidth( bo.getWidth( ) );
						break;
				}

				// adjust plot left.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( plotAnchor.getValue( ) )
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
						switch ( plotAnchor.getValue( ) )
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

				// adjust plot top.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( plotAnchor.getValue( ) )
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
						switch ( plotAnchor.getValue( ) )
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

				boLegend.setTop( bo.getTop( ) );
				boLegend.setLeft( bo.getLeft( ) );
				boLegend.setWidth( szLegend.getWidth( ) );
				boLegend.setHeight( bo.getHeight( ) - szTitle.getHeight( ) );

				boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( ) - boLegend.getWidth( ) )
						: plotWidthHint );
				boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( ) - szTitle.getHeight( ) )
						: plotHeightHint );
				plotLeft = bo.getLeft( ) + szLegend.getWidth( );
				plotTop = bo.getTop( );

				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.EAST :
						boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( )
								- boLegend.getWidth( ) - szTitle.getWidth( ) )
								: plotWidthHint );
						boPlot.setHeight( plotHeightHint < 0 ? bo.getHeight( )
								: plotHeightHint );
						boTitle.setLeft( bo.getLeft( )
								+ bo.getWidth( )
								- szTitle.getWidth( ) );
						boTitle.setHeight( bo.getHeight( ) );
						boLegend.setHeight( bo.getHeight( ) );
						break;
					case Anchor.WEST :
						boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( )
								- boLegend.getWidth( ) - szTitle.getWidth( ) )
								: plotWidthHint );
						boPlot.setHeight( plotHeightHint < 0 ? bo.getHeight( )
								: plotHeightHint );
						boTitle.setHeight( bo.getHeight( ) );
						boLegend.setLeft( bo.getLeft( ) + szTitle.getWidth( ) );
						boLegend.setHeight( bo.getHeight( ) );
						plotLeft = bo.getLeft( )
								+ szTitle.getWidth( )
								+ szLegend.getWidth( );
						break;
					case Anchor.SOUTH :
					case Anchor.SOUTH_EAST :
					case Anchor.SOUTH_WEST :
						boTitle.setTop( bo.getTop( )
								+ bo.getHeight( )
								- szTitle.getHeight( ) );
						boTitle.setWidth( bo.getWidth( ) );
						break;
					case Anchor.NORTH :
					case Anchor.NORTH_EAST :
					case Anchor.NORTH_WEST :
						plotTop = bo.getTop( ) + szTitle.getHeight( );
						boLegend.setTop( bo.getTop( ) + szTitle.getHeight( ) );
						boTitle.setWidth( bo.getWidth( ) );
						break;
				}

				// adjust plot left.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( plotAnchor.getValue( ) )
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
						switch ( plotAnchor.getValue( ) )
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

				// adjust plot top.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( plotAnchor.getValue( ) )
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
						switch ( plotAnchor.getValue( ) )
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

				boLegend.setTop( bo.getTop( ) );
				boLegend.setLeft( bo.getLeft( ) );
				boLegend.setWidth( bo.getWidth( ) );
				boLegend.setHeight( szLegend.getHeight( ) );

				boPlot.setWidth( plotWidthHint < 0 ? bo.getWidth( )
						: plotWidthHint );
				boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( )
						- szTitle.getHeight( ) - szLegend.getHeight( ) )
						: plotHeightHint );
				plotLeft = bo.getLeft( );
				plotTop = bo.getTop( ) + szLegend.getHeight( );

				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.EAST :
						boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( ) - szTitle.getWidth( ) )
								: plotWidthHint );
						boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( ) - boLegend.getHeight( ) )
								: plotHeightHint );
						boTitle.setLeft( bo.getLeft( )
								+ bo.getWidth( )
								- szTitle.getWidth( ) );
						boTitle.setHeight( bo.getHeight( ) );
						boLegend.setWidth( bo.getWidth( ) - szTitle.getWidth( ) );
						break;
					case Anchor.WEST :
						boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( ) - szTitle.getWidth( ) )
								: plotWidthHint );
						boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( ) - boLegend.getHeight( ) )
								: plotHeightHint );
						boTitle.setHeight( bo.getHeight( ) );
						boLegend.setWidth( bo.getWidth( ) - szTitle.getWidth( ) );
						boLegend.setLeft( bo.getLeft( ) + szTitle.getWidth( ) );
						plotLeft = bo.getLeft( ) + szTitle.getWidth( );
						break;
					case Anchor.SOUTH :
					case Anchor.SOUTH_EAST :
					case Anchor.SOUTH_WEST :
						boTitle.setTop( bo.getTop( )
								+ bo.getHeight( )
								- szTitle.getHeight( ) );
						boTitle.setWidth( bo.getWidth( ) );
						break;
					case Anchor.NORTH :
					case Anchor.NORTH_EAST :
					case Anchor.NORTH_WEST :
						plotTop = bo.getTop( )
								+ szLegend.getHeight( )
								+ szTitle.getHeight( );
						boLegend.setTop( bo.getTop( ) + szTitle.getHeight( ) );
						boTitle.setWidth( bo.getWidth( ) );
						break;
				}

				// adjust plot left.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( plotAnchor.getValue( ) )
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
						switch ( plotAnchor.getValue( ) )
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

				// adjust plot top.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( plotAnchor.getValue( ) )
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
						switch ( plotAnchor.getValue( ) )
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

				boLegend.setLeft( bo.getLeft( ) );
				boLegend.setTop( bo.getTop( )
						+ bo.getHeight( )
						- szLegend.getHeight( ) );
				boLegend.setWidth( bo.getWidth( ) );
				boLegend.setHeight( szLegend.getHeight( ) );

				boPlot.setWidth( plotWidthHint < 0 ? bo.getWidth( )
						: plotWidthHint );
				boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( )
						- boTitle.getHeight( ) - boLegend.getHeight( ) )
						: plotHeightHint );
				plotLeft = bo.getLeft( );
				plotTop = bo.getTop( );

				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.EAST :
						boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( ) - szTitle.getWidth( ) )
								: plotWidthHint );
						boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( ) - boLegend.getHeight( ) )
								: plotHeightHint );
						boTitle.setLeft( bo.getLeft( )
								+ bo.getWidth( )
								- szTitle.getWidth( ) );
						boTitle.setHeight( bo.getHeight( ) );
						boLegend.setWidth( bo.getWidth( ) - szTitle.getWidth( ) );
						break;
					case Anchor.WEST :
						boPlot.setWidth( plotWidthHint < 0 ? ( bo.getWidth( ) - szTitle.getWidth( ) )
								: plotWidthHint );
						boPlot.setHeight( plotHeightHint < 0 ? ( bo.getHeight( ) - boLegend.getHeight( ) )
								: plotHeightHint );
						boTitle.setHeight( bo.getHeight( ) );
						boLegend.setWidth( bo.getWidth( ) - szTitle.getWidth( ) );
						boLegend.setLeft( bo.getLeft( ) + szTitle.getWidth( ) );
						plotLeft = bo.getLeft( ) + szTitle.getWidth( );
						break;
					case Anchor.SOUTH :
					case Anchor.SOUTH_EAST :
					case Anchor.SOUTH_WEST :
						boTitle.setTop( bo.getTop( )
								+ bo.getHeight( )
								- szTitle.getHeight( ) );
						boTitle.setWidth( bo.getWidth( ) );
						boLegend.setTop( boTitle.getTop( )
								- szLegend.getHeight( ) );
						break;
					case Anchor.NORTH :
					case Anchor.NORTH_EAST :
					case Anchor.NORTH_WEST :
						boTitle.setWidth( bo.getWidth( ) );
						plotTop = bo.getTop( ) + szTitle.getHeight( );
						break;
				}

				// adjust plot left.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( plotAnchor.getValue( ) )
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
						switch ( plotAnchor.getValue( ) )
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

				// adjust plot top.
				switch ( titleAnchor.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.EAST :
						switch ( plotAnchor.getValue( ) )
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
						switch ( plotAnchor.getValue( ) )
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

			if ( cbl != lg && cbl != pl && cbl != tb )
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
		for ( Iterator itr = pl.getChildren( ).iterator( ); itr.hasNext( ); )
		{
			Block cbl = (Block) itr.next( );

			layoutBlock( xs, cm, pl.getBounds( ), pl.getInsets( ), cbl, rtc );
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
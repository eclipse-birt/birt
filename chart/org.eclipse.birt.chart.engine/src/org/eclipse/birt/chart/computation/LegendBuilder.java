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

package org.eclipse.birt.chart.computation;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * A helper class for Legend computation.
 */
public final class LegendBuilder
{

	private final double dHorizontalSpacing = 4;

	private final double dVerticalSpacing = 4;

	private Size sz;

	/**
	 * The constructor.
	 */
	public LegendBuilder( )
	{
	}

	/**
	 * Computes the size of the legend
	 * 
	 * @param lg
	 * @param sea
	 * 
	 * @throws GenerationException
	 */
	public final Size compute( IDisplayServer xs, Chart cm,
			SeriesDefinition[] seda, RunTimeContext rtc ) throws ChartException
	{
		// THREE CASES:
		// 1. ALL SERIES IN ONE ARRAYLIST
		// 2. ONE SERIES PER ARRAYLIST
		// 3. ALL OTHERS

		Legend lg = cm.getLegend( );
		if ( !lg.isSetOrientation( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					"exception.legend.orientation.horzvert", //$NON-NLS-1$
					ResourceBundle.getBundle( Messages.ENGINE, xs.getLocale( ) ) );
		}
		if ( !lg.isSetDirection( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					"exception.legend.direction.tblr", //$NON-NLS-1$
					ResourceBundle.getBundle( Messages.ENGINE, xs.getLocale( ) ) );
		}

		// INITIALIZATION OF VARS USED IN FOLLOWING LOOPS
		Orientation orientation = lg.getOrientation( );
		Direction direction = lg.getDirection( );
		double maxWrappingSize = lg.getWrappingSize( );

		Label la = LabelImpl.create( );
		la.setCaption( (Text) EcoreUtil.copy( lg.getText( ) ) );

		ClientArea ca = lg.getClientArea( );
		LineAttributes lia = ca.getOutline( );
		double dSeparatorThickness = lia.getThickness( );
		double dWidth = 0, dHeight = 0;
		la.getCaption( ).setValue( "X" ); //$NON-NLS-1$
		final ITextMetrics itm = xs.getTextMetrics( la );
		double dItemHeight = itm.getFullHeight( );
		Series se;
		ArrayList al;
		double dScale = xs.getDpiResolution( ) / 72d;
		Insets insCA = ca.getInsets( ).scaledInstance( dScale );
		final boolean bPaletteByCategory = ( cm.getLegend( )
				.getItemType( )
				.getValue( ) == LegendItemType.CATEGORIES );

		Series seBase;

		// Get available maximum block width/height.
		Block bl = cm.getBlock( );
		Bounds boFull = bl.getBounds( ).scaledInstance( dScale );
		Insets ins = bl.getInsets( ).scaledInstance( dScale );
		Insets lgIns = lg.getInsets( ).scaledInstance( dScale );

		double dAvailableWidth = boFull.getWidth( )
				- ins.getLeft( )
				- ins.getRight( )
				- lgIns.getLeft( )
				- lgIns.getRight( );
		double dAvailableHeight = boFull.getHeight( )
				- ins.getTop( )
				- ins.getBottom( )
				- lgIns.getTop( )
				- lgIns.getBottom( )
				- cm.getTitle( )
						.getBounds( )
						.scaledInstance( dScale )
						.getHeight( );

		// Calculate if minSlice applicable.
		boolean bMinSliceDefined = false;
		double dMinSlice = 0;
		boolean bPercentageMinSlice = false;
		String sMinSliceLabel = null;
		boolean bMinSliceApplied = false;

		if ( cm instanceof ChartWithoutAxes )
		{
			bMinSliceDefined = ( (ChartWithoutAxes) cm ).isSetMinSlice( );
			dMinSlice = ( (ChartWithoutAxes) cm ).getMinSlice( );
			bPercentageMinSlice = ( (ChartWithoutAxes) cm ).isMinSlicePercent( );
			sMinSliceLabel = ( (ChartWithoutAxes) cm ).getMinSliceLabel( );
			if ( sMinSliceLabel == null || sMinSliceLabel.length( ) == 0 )
			{
				sMinSliceLabel = IConstants.UNDEFINED_STRING;
			}
			else
			{
				sMinSliceLabel = rtc.externalizedMessage( sMinSliceLabel );
			}
		}

		// calculate if need an extra legend item when minSlice defined.
		if ( bMinSliceDefined
				&& bPaletteByCategory
				&& cm instanceof ChartWithoutAxes )
		{
			if ( !( (ChartWithoutAxes) cm ).getSeriesDefinitions( ).isEmpty( ) )
			{
				// OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
				SeriesDefinition sdBase = (SeriesDefinition) ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
						.get( 0 );

				SeriesDefinition[] sdOrtho = (SeriesDefinition[]) sdBase.getSeriesDefinitions( )
						.toArray( );

				DataSetIterator dsiOrtho = null;
				double dCurrentMinSlice = 0;

				for ( int i = 0; i < sdOrtho.length && !bMinSliceApplied; i++ )
				{
					try
					{
						dsiOrtho = new DataSetIterator( ( (Series) sdOrtho[i].getRunTimeSeries( )
								.get( 0 ) ).getDataSet( ) );
					}
					catch ( Exception ex )
					{
						throw new ChartException( ChartEnginePlugin.ID,
								ChartException.RENDERING,
								ex );
					}

					// TODO Check dataSet type, throw exception or ignore?.

					if ( bPercentageMinSlice )
					{
						double total = 0;

						while ( dsiOrtho.hasNext( ) )
						{
							Object obj = dsiOrtho.next( );

							if ( obj instanceof Number )
							{
								total += ( (Number) obj ).doubleValue( );
							}
						}

						dsiOrtho.reset( );

						dCurrentMinSlice = total * dMinSlice / 100d;
					}
					else
					{
						dCurrentMinSlice = dMinSlice;
					}

					while ( dsiOrtho.hasNext( ) )
					{
						Object obj = dsiOrtho.next( );
						if ( obj instanceof Number )
						{
							double val = ( (Number) obj ).doubleValue( );

							if ( val < dCurrentMinSlice )
							{
								bMinSliceApplied = true;
								break;
							}
						}
					}
				}
			}
		}

		// COMPUTATIONS HERE MUST BE IN SYNC WITH THE ACTUAL RENDERER
		if ( orientation.getValue( ) == Orientation.VERTICAL )
		{
			double dW, dMaxW = 0;
			double dFullHeight = 0, dExtraWidth = 0, dDeltaHeight;

			if ( bPaletteByCategory )
			{
				SeriesDefinition sdBase = null;
				if ( cm instanceof ChartWithAxes )
				{
					final Axis axPrimaryBase = ( (ChartWithAxes) cm ).getBaseAxes( )[0]; // ONLY
					// SUPPORT
					// 1
					// BASE
					// AXIS
					// FOR
					// NOW
					if ( axPrimaryBase.getSeriesDefinitions( ).isEmpty( ) )
					{
						return SizeImpl.create( 0, 0 );
					}
					sdBase = (SeriesDefinition) axPrimaryBase.getSeriesDefinitions( )
							.get( 0 ); // OK TO ASSUME THAT 1 BASE SERIES
					// DEFINITION EXISTS
				}
				else if ( cm instanceof ChartWithoutAxes )
				{
					if ( ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
							.isEmpty( ) )
					{
						return SizeImpl.create( 0, 0 );
					}
					sdBase = (SeriesDefinition) ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
							.get( 0 ); // OK TO ASSUME THAT 1 BASE SERIES
					// DEFINITION EXISTS
				}
				seBase = (Series) sdBase.getRunTimeSeries( ).get( 0 ); // OK TO
				// ASSUME
				// THAT 1
				// BASE
				// RUNTIME
				// SERIES
				// EXISTS

				DataSetIterator dsiBase = null;
				try
				{
					dsiBase = new DataSetIterator( seBase.getDataSet( ) );
				}
				catch ( Exception ex )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.GENERATION,
							ex );
				}

				FormatSpecifier fs = null;
				if ( sdBase != null )
				{
					fs = sdBase.getFormatSpecifier( );
				}

				while ( dsiBase.hasNext( ) )
				{
					// TODO filter the not-used legend.

					Object obj = dsiBase.next( );
					String lgtext = String.valueOf( obj );
					if ( fs != null )
					{
						try
						{
							lgtext = ValueFormatter.format( obj,
									fs,
									Locale.getDefault( ),
									null );
						}
						catch ( ChartException e )
						{
							// ignore, use original text.
						}
					}
					la.getCaption( ).setValue( lgtext );
					itm.reuse( la, maxWrappingSize );

					dDeltaHeight = insCA.getTop( )
							+ itm.getFullHeight( )
							+ insCA.getBottom( );

					if ( dHeight + dDeltaHeight > dAvailableHeight )
					{
						dExtraWidth += dWidth
								+ insCA.getLeft( )
								+ insCA.getRight( )
								+ ( 3 * dItemHeight )
								/ 2
								+ dHorizontalSpacing;
						dWidth = itm.getFullWidth( );
						dFullHeight = Math.max( dFullHeight, dHeight );
						dHeight = dDeltaHeight;
					}
					else
					{
						dWidth = Math.max( itm.getFullWidth( ), dWidth );
						dHeight += dDeltaHeight;
					}
				}

				// compute the extra MinSlice legend item if applicable.
				if ( bMinSliceDefined && bMinSliceApplied )
				{
					la.getCaption( ).setValue( sMinSliceLabel );
					itm.reuse( la, maxWrappingSize );

					dDeltaHeight = insCA.getTop( )
							+ itm.getFullHeight( )
							+ insCA.getBottom( );

					if ( dHeight + dDeltaHeight > dAvailableHeight )
					{
						dExtraWidth += dWidth
								+ insCA.getLeft( )
								+ insCA.getRight( )
								+ ( 3 * dItemHeight )
								/ 2
								+ dHorizontalSpacing;
						dWidth = itm.getFullWidth( );
						dFullHeight = Math.max( dFullHeight, dHeight );
						dHeight = dDeltaHeight;
					}
					else
					{
						dWidth = Math.max( itm.getFullWidth( ), dWidth );
						dHeight += dDeltaHeight;
					}
				}

				dWidth += insCA.getLeft( )
						+ ( 3 * dItemHeight )
						/ 2
						+ dHorizontalSpacing
						+ insCA.getRight( )
						+ dExtraWidth;
				dHeight = Math.max( dFullHeight, dHeight );
			}
			else if ( direction.getValue( ) == Direction.TOP_BOTTOM ) // (VERTICAL
			// =>
			// TB)
			{
				dSeparatorThickness += dVerticalSpacing;
				for ( int j = 0; j < seda.length; j++ )
				{
					al = seda[j].getRunTimeSeries( );
					FormatSpecifier fs = seda[j].getFormatSpecifier( );
					for ( int i = 0; i < al.size( ); i++ )
					{
						se = (Series) al.get( i );
						Object obj = se.getSeriesIdentifier( );
						String lgtext = String.valueOf( obj );
						if ( fs != null )
						{
							try
							{
								lgtext = ValueFormatter.format( obj,
										fs,
										Locale.getDefault( ),
										null );
							}
							catch ( ChartException e )
							{
								// ignore, use original text.
							}
						}
						la.getCaption( ).setValue( lgtext );
						itm.reuse( la, maxWrappingSize );
						dW = itm.getFullWidth( );

						dDeltaHeight = insCA.getTop( )
								+ itm.getFullHeight( )
								+ insCA.getBottom( );

						if ( lg.isShowValue( ) )
						{
							DataSetIterator dsiBase = null;
							try
							{
								dsiBase = new DataSetIterator( se.getDataSet( ) );
							}
							catch ( Exception ex )
							{
								throw new ChartException( ChartEnginePlugin.ID,
										ChartException.GENERATION,
										ex );
							}

							// Use first value for each series.
							if ( dsiBase.hasNext( ) )
							{
								obj = dsiBase.next( );
								String valueText = String.valueOf( obj );
								if ( fs != null )
								{
									try
									{
										lgtext = ValueFormatter.format( obj,
												fs,
												Locale.getDefault( ),
												null );
									}
									catch ( ChartException e )
									{
										// ignore, use original text.
									}
								}

								Label seLabel = (Label) EcoreUtil.copy( se.getLabel( ) );
								seLabel.getCaption( ).setValue( valueText );
								itm.reuse( seLabel );
								dW = Math.max( dW, itm.getFullWidth( ) );

								dDeltaHeight += itm.getFullHeight( ) + 2;
							}
						}

						if ( dHeight + dDeltaHeight > dAvailableHeight )
						{
							dExtraWidth += dMaxW
									+ insCA.getLeft( )
									+ insCA.getRight( )
									+ ( 3 * dItemHeight )
									/ 2
									+ dHorizontalSpacing;
							dMaxW = dW;
							dFullHeight = Math.max( dFullHeight, dHeight );
							dHeight = dDeltaHeight;
						}
						else
						{
							dMaxW = Math.max( dW, dMaxW );
							dHeight += dDeltaHeight;
						}

					}

					// SETUP HORIZONTAL SEPARATOR SPACING
					if ( j < seda.length - 1 )
					{
						dHeight += dSeparatorThickness;
					}
				}

				// LEFT INSETS + LEGEND ITEM WIDTH + HORIZONTAL SPACING + MAX
				// ITEM WIDTH + RIGHT INSETS
				dWidth = insCA.getLeft( )
						+ ( 3 * dItemHeight )
						/ 2
						+ dHorizontalSpacing
						+ dMaxW
						+ insCA.getRight( )
						+ dExtraWidth;
				dHeight = Math.max( dFullHeight, dHeight );
			}
			else if ( direction.getValue( ) == Direction.LEFT_RIGHT ) // (VERTICAL
			// =>
			// LR)
			{
				dSeparatorThickness += dHorizontalSpacing;
				for ( int j = 0; j < seda.length; j++ )
				{
					al = seda[j].getRunTimeSeries( );
					FormatSpecifier fs = seda[j].getFormatSpecifier( );
					for ( int i = 0; i < al.size( ); i++ )
					{
						se = (Series) al.get( i );
						Object obj = se.getSeriesIdentifier( );
						String lgtext = String.valueOf( obj );
						if ( fs != null )
						{
							try
							{
								lgtext = ValueFormatter.format( obj,
										fs,
										Locale.getDefault( ),
										null );
							}
							catch ( ChartException e )
							{
								// ignore, use original text.
							}
						}
						la.getCaption( ).setValue( lgtext );
						itm.reuse( la, maxWrappingSize );

						dW = itm.getFullWidth( );
						dDeltaHeight = insCA.getTop( )
								+ itm.getFullHeight( )
								+ insCA.getBottom( );

						if ( lg.isShowValue( ) )
						{
							DataSetIterator dsiBase = null;
							try
							{
								dsiBase = new DataSetIterator( se.getDataSet( ) );
							}
							catch ( Exception ex )
							{
								throw new ChartException( ChartEnginePlugin.ID,
										ChartException.GENERATION,
										ex );
							}

							// Use first value for each series.
							if ( dsiBase.hasNext( ) )
							{
								obj = dsiBase.next( );
								String valueText = String.valueOf( obj );
								if ( fs != null )
								{
									try
									{
										lgtext = ValueFormatter.format( obj,
												fs,
												Locale.getDefault( ),
												null );
									}
									catch ( ChartException e )
									{
										// ignore, use original text.
									}
								}

								Label seLabel = (Label) EcoreUtil.copy( se.getLabel( ) );
								seLabel.getCaption( ).setValue( valueText );
								itm.reuse( seLabel );

								dW = Math.max( dW, itm.getFullWidth( ) );
								dDeltaHeight += itm.getFullHeight( ) + 2;
							}
						}

						if ( dHeight + dDeltaHeight > dAvailableHeight )
						{
							dExtraWidth += dMaxW
									+ insCA.getLeft( )
									+ insCA.getRight( )
									+ ( 3 * dItemHeight )
									/ 2
									+ dHorizontalSpacing;
							dMaxW = dW;
							dFullHeight = Math.max( dFullHeight, dHeight );
							dHeight = dDeltaHeight;
						}
						else
						{
							dMaxW = Math.max( dW, dMaxW );
							dHeight += dDeltaHeight;
						}

					}

					dExtraWidth += dMaxW
							+ insCA.getLeft( )
							+ insCA.getRight( )
							+ ( 3 * dItemHeight )
							/ 2
							+ dHorizontalSpacing;
					dMaxW = 0;
					dFullHeight = Math.max( dFullHeight, dHeight );
					dHeight = 0;

					// SETUP VERTICAL SEPARATOR SPACING
					if ( j < seda.length - 1 )
					{
						dExtraWidth += dSeparatorThickness;
					}
				}

				// LEFT INSETS + LEGEND ITEM WIDTH + HORIZONTAL SPACING +
				// MAX ITEM WIDTH + RIGHT INSETS
				dWidth += insCA.getLeft( )
						+ ( 3 * dItemHeight / 2 )
						+ dHorizontalSpacing
						+ insCA.getRight( )
						+ dExtraWidth;

				dHeight = Math.max( dFullHeight, dHeight );
			}
			else
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.GENERATION,
						"exception.illegal.rendering.direction", //$NON-NLS-1$
						new Object[]{
							direction.getName( )
						},
						ResourceBundle.getBundle( Messages.ENGINE,
								xs.getLocale( ) ) );
			}
		}
		else if ( orientation.getValue( ) == Orientation.HORIZONTAL )
		{
			double dH, dMaxH = 0;
			double dFullWidth = 0, dExtraHeight = 0, dDeltaWidth;

			if ( bPaletteByCategory )
			{
				SeriesDefinition sdBase = null;
				if ( cm instanceof ChartWithAxes )
				{
					final Axis axPrimaryBase = ( (ChartWithAxes) cm ).getBaseAxes( )[0]; // ONLY
					// SUPPORT
					// 1
					// BASE
					// AXIS
					// FOR
					// NOW
					if ( axPrimaryBase.getSeriesDefinitions( ).isEmpty( ) )
					{
						throw new ChartException( ChartEnginePlugin.ID,
								ChartException.GENERATION,
								"exception.base.axis.no.series.definitions", //$NON-NLS-1$ 
								ResourceBundle.getBundle( Messages.ENGINE,
										xs.getLocale( ) ) );
					}
					sdBase = (SeriesDefinition) axPrimaryBase.getSeriesDefinitions( )
							.get( 0 ); // OK TO ASSUME
					// THAT 1 BASE
					// SERIES
					// DEFINITION
					// EXISTS
				}
				else if ( cm instanceof ChartWithoutAxes )
				{
					if ( ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
							.isEmpty( ) )
					{
						throw new ChartException( ChartEnginePlugin.ID,
								ChartException.GENERATION,
								"exception.base.axis.no.series.definitions", //$NON-NLS-1$
								ResourceBundle.getBundle( Messages.ENGINE,
										xs.getLocale( ) ) );
					}
					sdBase = (SeriesDefinition) ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
							.get( 0 ); // OK TO ASSUME
					// THAT 1 BASE
					// SERIES
					// DEFINITION
					// EXISTS
				}
				seBase = (Series) sdBase.getRunTimeSeries( ).get( 0 ); // OK TO
				// ASSUME
				// THAT 1
				// BASE
				// RUNTIME
				// SERIES
				// EXISTS

				DataSetIterator dsiBase = null;
				try
				{
					dsiBase = new DataSetIterator( seBase.getDataSet( ) );
				}
				catch ( Exception ex )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.GENERATION,
							ex );
				}

				FormatSpecifier fs = null;
				if ( sdBase != null )
				{
					fs = sdBase.getFormatSpecifier( );
				}

				while ( dsiBase.hasNext( ) )
				{
					// TODO filter the not-used legend.

					Object obj = dsiBase.next( );
					String lgtext = String.valueOf( obj );
					if ( fs != null )
					{
						try
						{
							lgtext = ValueFormatter.format( obj,
									fs,
									Locale.getDefault( ),
									null );
						}
						catch ( ChartException e )
						{
							// ignore, use original text.
						}
					}
					la.getCaption( ).setValue( lgtext );
					itm.reuse( la, maxWrappingSize );

					dDeltaWidth = insCA.getLeft( )
							+ itm.getFullWidth( )
							+ ( 3 * dItemHeight )
							/ 2
							+ insCA.getRight( );

					if ( dWidth + dDeltaWidth > dAvailableWidth )
					{
						dExtraHeight += dHeight
								+ insCA.getTop( )
								+ insCA.getBottom( )
								+ dVerticalSpacing;
						dHeight = itm.getFullHeight( );
						dFullWidth = Math.max( dFullWidth, dWidth );
						dWidth = dDeltaWidth;
					}
					else
					{
						dHeight = Math.max( itm.getFullHeight( ), dHeight );
						dWidth += dDeltaWidth;
					}
				}

				// compute the extra MinSlice legend item if applicable.
				if ( bMinSliceDefined && bMinSliceApplied )
				{
					la.getCaption( ).setValue( sMinSliceLabel );
					itm.reuse( la, maxWrappingSize );

					dDeltaWidth = insCA.getLeft( )
							+ itm.getFullWidth( )
							+ ( 3 * dItemHeight )
							/ 2
							+ insCA.getRight( );

					if ( dWidth + dDeltaWidth > dAvailableWidth )
					{
						dExtraHeight += dHeight
								+ insCA.getTop( )
								+ insCA.getBottom( )
								+ dVerticalSpacing;
						dHeight = itm.getFullHeight( );
						dFullWidth = Math.max( dFullWidth, dWidth );
						dWidth = dDeltaWidth;
					}
					else
					{
						dHeight = Math.max( itm.getFullHeight( ), dHeight );
						dWidth += dDeltaWidth;
					}
				}

				dHeight += dExtraHeight
						+ insCA.getTop( )
						+ insCA.getBottom( )
						+ dVerticalSpacing;
				dWidth = Math.max( dWidth, dFullWidth );
			}
			else if ( direction.getValue( ) == Direction.TOP_BOTTOM ) // (HORIZONTAL
			// =>
			// TB)
			{
				dSeparatorThickness += dVerticalSpacing;
				for ( int j = 0; j < seda.length; j++ )
				{
					dWidth = 0;
					al = seda[j].getRunTimeSeries( );
					FormatSpecifier fs = seda[j].getFormatSpecifier( );
					for ( int i = 0; i < al.size( ); i++ )
					{
						se = (Series) al.get( i );
						Object obj = se.getSeriesIdentifier( );
						String lgtext = String.valueOf( obj );
						if ( fs != null )
						{
							try
							{
								lgtext = ValueFormatter.format( obj,
										fs,
										Locale.getDefault( ),
										null );
							}
							catch ( ChartException e )
							{
								// ignore, use original text.
							}
						}
						la.getCaption( ).setValue( lgtext );
						itm.reuse( la, maxWrappingSize );

						dH = itm.getFullHeight( );
						dDeltaWidth = insCA.getLeft( )
								+ ( 3 * dItemHeight )
								/ 2
								+ itm.getFullWidth( )
								+ insCA.getRight( );

						if ( lg.isShowValue( ) )
						{
							DataSetIterator dsiBase = null;
							try
							{
								dsiBase = new DataSetIterator( se.getDataSet( ) );
							}
							catch ( Exception ex )
							{
								throw new ChartException( ChartEnginePlugin.ID,
										ChartException.GENERATION,
										ex );
							}

							// Use first value for each series.
							if ( dsiBase.hasNext( ) )
							{
								obj = dsiBase.next( );
								String valueText = String.valueOf( obj );
								if ( fs != null )
								{
									try
									{
										lgtext = ValueFormatter.format( obj,
												fs,
												Locale.getDefault( ),
												null );
									}
									catch ( ChartException e )
									{
										// ignore, use original text.
									}
								}

								Label seLabel = (Label) EcoreUtil.copy( se.getLabel( ) );
								seLabel.getCaption( ).setValue( valueText );
								itm.reuse( seLabel );

								dH += itm.getFullHeight( ) + 2;
								dDeltaWidth = Math.max( dDeltaWidth,
										itm.getFullWidth( ) );
							}
						}

						if ( dWidth + dDeltaWidth > dAvailableWidth )
						{
							dExtraHeight += dMaxH
									+ insCA.getTop( )
									+ insCA.getBottom( )
									+ dVerticalSpacing;
							dMaxH = dH;
							dFullWidth = Math.max( dFullWidth, dWidth );
							dWidth = dDeltaWidth;
						}
						else
						{
							dMaxH = Math.max( dH, dMaxH );
							dWidth += dDeltaWidth;
						}
					}

					dExtraHeight += dMaxH
							+ insCA.getTop( )
							+ insCA.getBottom( )
							+ dVerticalSpacing;
					dMaxH = 0;
					dFullWidth = Math.max( dFullWidth, dWidth );
					dWidth = 0;

					// SETUP HORIZONTAL SEPARATOR SPACING
					if ( j < seda.length - 1 )
					{
						dHeight += dSeparatorThickness;
					}
				}

				dHeight += insCA.getTop( )
						+ dVerticalSpacing
						+ insCA.getBottom( )
						+ dExtraHeight;

				dWidth = Math.max( dFullWidth, dWidth );

			}
			else if ( direction.getValue( ) == Direction.LEFT_RIGHT ) // (HORIZONTAL
			// =>
			// LR)
			{
				dSeparatorThickness += dHorizontalSpacing;
				for ( int j = 0; j < seda.length; j++ )
				{
					al = seda[j].getRunTimeSeries( );
					FormatSpecifier fs = seda[j].getFormatSpecifier( );
					for ( int i = 0; i < al.size( ); i++ )
					{
						se = (Series) al.get( i );
						Object obj = se.getSeriesIdentifier( );
						String lgtext = String.valueOf( obj );
						if ( fs != null )
						{
							try
							{
								lgtext = ValueFormatter.format( obj,
										fs,
										Locale.getDefault( ),
										null );
							}
							catch ( ChartException e )
							{
								// ignore, use original text.
							}
						}
						la.getCaption( ).setValue( lgtext );
						itm.reuse( la, maxWrappingSize );

						dH = itm.getFullHeight( );
						dDeltaWidth = insCA.getLeft( )
								+ ( 3 * dItemHeight )
								/ 2
								+ itm.getFullWidth( )
								+ insCA.getRight( );

						if ( lg.isShowValue( ) )
						{
							DataSetIterator dsiBase = null;
							try
							{
								dsiBase = new DataSetIterator( se.getDataSet( ) );
							}
							catch ( Exception ex )
							{
								throw new ChartException( ChartEnginePlugin.ID,
										ChartException.GENERATION,
										ex );
							}

							// Use first value for each series.
							if ( dsiBase.hasNext( ) )
							{
								obj = dsiBase.next( );
								String valueText = String.valueOf( obj );
								if ( fs != null )
								{
									try
									{
										lgtext = ValueFormatter.format( obj,
												fs,
												Locale.getDefault( ),
												null );
									}
									catch ( ChartException e )
									{
										// ignore, use original text.
									}
								}

								Label seLabel = (Label) EcoreUtil.copy( se.getLabel( ) );
								seLabel.getCaption( ).setValue( valueText );
								itm.reuse( seLabel );

								dH += itm.getFullHeight( ) + 2;
								dDeltaWidth = Math.max( dDeltaWidth,
										itm.getFullWidth( ) );
							}
						}

						if ( dWidth + dDeltaWidth > dAvailableWidth )
						{
							dExtraHeight += dMaxH
									+ insCA.getTop( )
									+ insCA.getBottom( )
									+ dVerticalSpacing;
							dMaxH = dH;
							dFullWidth = Math.max( dFullWidth, dWidth );
							dWidth = dDeltaWidth;
						}
						else
						{
							dMaxH = Math.max( dH, dMaxH );
							dWidth += dDeltaWidth;
						}
					}

					// SETUP VERTICAL SEPARATOR SPACING
					if ( j < seda.length - 1 )
					{
						dWidth += dSeparatorThickness;
					}
				}

				dHeight += insCA.getTop( )
						+ dVerticalSpacing
						+ insCA.getBottom( )
						+ dMaxH
						+ dExtraHeight;

				dWidth = Math.max( dFullWidth, dWidth );
			}
			else
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.GENERATION,
						"exception.illegal.rendering.direction", //$NON-NLS-1$
						new Object[]{
							direction
						},
						ResourceBundle.getBundle( Messages.ENGINE,
								xs.getLocale( ) ) );
			}
		}
		else
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					"exception.illegal.rendering.orientation", //$NON-NLS-1$
					new Object[]{
						orientation
					},
					ResourceBundle.getBundle( Messages.ENGINE, xs.getLocale( ) ) );
		}

		// consider legend title size.
		Label lgTitle = lg.getTitle( );

		if ( lgTitle != null && lgTitle.isSetVisible( ) && lgTitle.isVisible( ) )
		{
			BoundingBox bb = null;
			try
			{
				bb = Methods.computeBox( xs, IConstants.ABOVE, lgTitle, 0, 0 );
			}
			catch ( IllegalArgumentException uiex )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.RENDERING,
						uiex );
			}

			switch ( lg.getTitlePosition( ).getValue( ) )
			{
				case Position.ABOVE :
				case Position.BELOW :
					dHeight += bb.getHeight( );
					dWidth = Math.max( dWidth, bb.getWidth( ) );
					break;
				case Position.LEFT :
				case Position.RIGHT :
					dWidth += bb.getWidth( );
					dHeight = Math.max( dHeight, bb.getHeight( ) );
					break;
			}
		}

		itm.dispose( ); // DISPOSE RESOURCE AFTER USE
		sz = SizeImpl.create( dWidth, dHeight );
		return sz;
	}

	/**
	 * Returns the size computed previously.
	 * 
	 * @return
	 */
	public final Size getSize( )
	{
		return sz;
	}
}
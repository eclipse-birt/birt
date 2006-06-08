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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.render.BaseRenderer;

/**
 * A helper class for Legend computation.
 */
public final class LegendBuilder implements IConstants
{

	private Size sz;

	/**
	 * The constructor.
	 */
	public LegendBuilder( )
	{
	}

	/**
	 * Computes the size of the legend. Note the computation relies on the title
	 * size, so the title block must be layouted first before this.
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

		final Legend lg = cm.getLegend( );
		if ( !lg.isSetOrientation( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					"exception.legend.orientation.horzvert", //$NON-NLS-1$
					Messages.getResourceBundle( xs.getULocale( ) ) );
		}
		if ( !lg.isSetDirection( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					"exception.legend.direction.tblr", //$NON-NLS-1$
					Messages.getResourceBundle( xs.getULocale( ) ) );
		}

		// INITIALIZATION OF VARS USED IN FOLLOWING LOOPS
		final Orientation orientation = lg.getOrientation( );
		final Direction direction = lg.getDirection( );
		final double maxWrappingSize = lg.getWrappingSize( );

		Label la = LabelImpl.create( );
		la.setCaption( TextImpl.copyInstance( lg.getText( ) ) );

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
		final List legendItems = new ArrayList( );

		final double dHorizontalSpacing = 3 * dScale;
		final double dVerticalSpacing = 3 * dScale;
		
		// Get maximum block width/height available
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
		String sMinSliceLabel = null;
		boolean bMinSliceApplied = false;
		int[] filteredMinSliceEntry = null;

		if ( cm instanceof ChartWithoutAxes )
		{
			bMinSliceDefined = ( (ChartWithoutAxes) cm ).isSetMinSlice( );
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
			Map renders = rtc.getSeriesRenderers( );

			if ( renders != null
					&& !( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
							.isEmpty( ) )
			{
				// OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
				SeriesDefinition sdBase = (SeriesDefinition) ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
						.get( 0 );
				SeriesDefinition[] sdOrtho = (SeriesDefinition[]) sdBase.getSeriesDefinitions( )
						.toArray( new SeriesDefinition[0] );

				DataSetIterator dsiOrtho = null;
				BaseRenderer br;
				boolean started = false;

				ENTRANCE: for ( int i = 0; i < sdOrtho.length; i++ )
				{
					Series[] alRuntimeSeries = (Series[]) sdOrtho[i].getRunTimeSeries( )
							.toArray( new Series[0] );
					for ( int j = 0; j < alRuntimeSeries.length; j++ )
					{
						try
						{
							dsiOrtho = new DataSetIterator( alRuntimeSeries[j].getDataSet( ) );

							LegendItemRenderingHints lirh = (LegendItemRenderingHints) renders.get( alRuntimeSeries[j] );

							if ( lirh == null )
							{
								filteredMinSliceEntry = null;
								break ENTRANCE;
							}

							br = lirh.getRenderer( );

							// ask each render for filtered min slice info
							int[] fsa = br.getFilteredMinSliceEntry( dsiOrtho );

							if ( fsa != null && fsa.length > 0 )
							{
								bMinSliceApplied = true;
							}

							if ( !started )
							{
								started = true;
								filteredMinSliceEntry = fsa;
							}
							else
							{
								// get duplicate indices for all renderers
								filteredMinSliceEntry = getDuplicateIndices( fsa,
										filteredMinSliceEntry );

								if ( filteredMinSliceEntry == null
										|| filteredMinSliceEntry.length == 0 )
								{
									filteredMinSliceEntry = null;
									break ENTRANCE;
								}
							}
						}
						catch ( Exception ex )
						{
							throw new ChartException( ChartEnginePlugin.ID,
									ChartException.RENDERING,
									ex );
						}
					}
				}

				// assign a zero-length array for successive convenience
				if ( filteredMinSliceEntry == null )
				{
					filteredMinSliceEntry = new int[0];
				}
			}
		}

		// COMPUTATIONS HERE MUST BE IN SYNC WITH THE ACTUAL RENDERER
		if ( orientation.getValue( ) == Orientation.VERTICAL )
		{
			double dW, dMaxW = 0;
			double dRealHeight = 0, dExtraWidth = 0, dDeltaHeight;

			if ( bPaletteByCategory )
			{
				SeriesDefinition sdBase = null;
				if ( cm instanceof ChartWithAxes )
				{
					// ONLY SUPPORT 1 BASE AXIS FOR NOW
					final Axis axPrimaryBase = ( (ChartWithAxes) cm ).getBaseAxes( )[0];
					if ( axPrimaryBase.getSeriesDefinitions( ).isEmpty( ) )
					{
						return SizeImpl.create( 0, 0 );
					}
					// OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
					sdBase = (SeriesDefinition) axPrimaryBase.getSeriesDefinitions( )
							.get( 0 );
				}
				else if ( cm instanceof ChartWithoutAxes )
				{
					if ( ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
							.isEmpty( ) )
					{
						return SizeImpl.create( 0, 0 );
					}
					// OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
					sdBase = (SeriesDefinition) ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
							.get( 0 );
				}
				// OK TO ASSUME THAT 1 BASE RUNTIME SERIES EXISTS
				seBase = (Series) sdBase.getRunTimeSeries( ).get( 0 );

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

				int pos = -1;
				while ( dsiBase.hasNext( ) )
				{
					Object obj = dsiBase.next( );

					pos++;

					// filter the not-used legend.
					if ( bMinSliceApplied
							&& Arrays.binarySearch( filteredMinSliceEntry, pos ) >= 0 )
					{
						continue;
					}

					String lgtext = String.valueOf( obj );
					if ( fs != null )
					{
						try
						{
							lgtext = ValueFormatter.format( obj,
									fs,
									rtc.getULocale( ),
									null );
						}
						catch ( ChartException e )
						{
							// ignore, use original text.
						}
					}
					la.getCaption( ).setValue( lgtext );
					itm.reuse( la, maxWrappingSize );

					BoundingBox bb = null;
					try
					{
						bb = Methods.computeBox( xs, IConstants.ABOVE, la, 0, 0 );
					}
					catch ( IllegalArgumentException uiex )
					{
						throw new ChartException( ChartEnginePlugin.ID,
								ChartException.RENDERING,
								uiex );
					}

					double dFWidth = bb.getWidth( );
					double dFHeight = bb.getHeight( );

					dDeltaHeight = insCA.getTop( )
							+ dFHeight
							+ insCA.getBottom( );

					if ( dHeight + dDeltaHeight > dAvailableHeight )
					{
						dExtraWidth += dWidth
								+ insCA.getLeft( )
								+ insCA.getRight( )
								+ ( 3 * dItemHeight )
								/ 2
								+ dHorizontalSpacing;
						dWidth = dFWidth;
						dRealHeight = Math.max( dRealHeight, dHeight );
						dHeight = dDeltaHeight;
					}
					else
					{
						dWidth = Math.max( dFWidth, dWidth );
						dHeight += dDeltaHeight;
					}

					legendItems.add( new LegendItemHints( LEGEND_ENTRY,
							new Point( dExtraWidth, dHeight - dDeltaHeight ),
							dFWidth,
							dFHeight,
							la.getCaption( ).getValue( ),
							pos ) );
				}

				// compute the extra MinSlice legend item if applicable.
				if ( bMinSliceApplied )
				{
					la.getCaption( ).setValue( sMinSliceLabel );
					itm.reuse( la, maxWrappingSize );

					BoundingBox bb = null;
					try
					{
						bb = Methods.computeBox( xs, IConstants.ABOVE, la, 0, 0 );
					}
					catch ( IllegalArgumentException uiex )
					{
						throw new ChartException( ChartEnginePlugin.ID,
								ChartException.RENDERING,
								uiex );
					}

					double dFWidth = bb.getWidth( );
					double dFHeight = bb.getHeight( );

					dDeltaHeight = insCA.getTop( )
							+ dFHeight
							+ insCA.getBottom( );

					if ( dHeight + dDeltaHeight > dAvailableHeight )
					{
						dExtraWidth += dWidth
								+ insCA.getLeft( )
								+ insCA.getRight( )
								+ ( 3 * dItemHeight )
								/ 2
								+ dHorizontalSpacing;
						dWidth = dFWidth;
						dRealHeight = Math.max( dRealHeight, dHeight );
						dHeight = dDeltaHeight;
					}
					else
					{
						dWidth = Math.max( dFWidth, dWidth );
						dHeight += dDeltaHeight;
					}

					legendItems.add( new LegendItemHints( LEGEND_MINSLICE_ENTRY,
							new Point( dExtraWidth, dHeight - dDeltaHeight ),
							dFWidth,
							dFHeight,
							la.getCaption( ).getValue( ),
							dsiBase.size( ) ) );
				}

				dWidth += insCA.getLeft( )
						+ ( 3 * dItemHeight )
						/ 2
						+ dHorizontalSpacing
						+ insCA.getRight( )
						+ dExtraWidth;
				dHeight = Math.max( dRealHeight, dHeight );
			}
			else if ( direction.getValue( ) == Direction.TOP_BOTTOM )
			{
				// (VERTICAL => TB)

				dSeparatorThickness += dVerticalSpacing;

				for ( int j = 0; j < seda.length; j++ )
				{
					al = seda[j].getRunTimeSeries( );
					FormatSpecifier fs = seda[j].getFormatSpecifier( );
					for ( int i = 0; i < al.size( ); i++ )
					{
						se = (Series) al.get( i );
						Object obj = se.getSeriesIdentifier( );
						String lgtext = rtc.externalizedMessage( String.valueOf( obj ) );
						if ( fs != null )
						{
							try
							{
								lgtext = ValueFormatter.format( lgtext,
										fs,
										rtc.getULocale( ),
										null );
							}
							catch ( ChartException e )
							{
								// ignore, use original text.
							}
						}
						la.getCaption( ).setValue( lgtext );
						itm.reuse( la, maxWrappingSize );

						BoundingBox bb = null;
						try
						{
							bb = Methods.computeBox( xs,
									IConstants.ABOVE,
									la,
									0,
									0 );
						}
						catch ( IllegalArgumentException uiex )
						{
							throw new ChartException( ChartEnginePlugin.ID,
									ChartException.RENDERING,
									uiex );
						}
						dW = bb.getWidth( );

						double dFHeight = bb.getHeight( );
						double dExtraHeight = 0;
						String extraText = null;

						dDeltaHeight = insCA.getTop( )
								+ dFHeight
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
												rtc.getULocale( ),
												null );
									}
									catch ( ChartException e )
									{
										// ignore, use original text.
									}
								}

								Label seLabel = LabelImpl.copyInstance( se.getLabel( ) );
								seLabel.getCaption( ).setValue( valueText );
								itm.reuse( seLabel );

								dW = Math.max( dW, itm.getFullWidth( ) );

								dExtraHeight = itm.getFullHeight( );
								extraText = seLabel.getCaption( ).getValue( );

								dDeltaHeight += dExtraHeight + 2 * dScale;
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
							dRealHeight = Math.max( dRealHeight, dHeight );
							dHeight = dDeltaHeight;
						}
						else
						{
							dMaxW = Math.max( dW, dMaxW );
							dHeight += dDeltaHeight;
						}

						legendItems.add( new LegendItemHints( LEGEND_ENTRY,
								new Point( dExtraWidth, dHeight - dDeltaHeight ),
								dW,
								dFHeight,
								la.getCaption( ).getValue( ),
								dExtraHeight,
								extraText ) );
					}

					// SETUP HORIZONTAL SEPARATOR SPACING
					if ( j < seda.length - 1
							&& ( lg.getSeparator( ) == null || lg.getSeparator( )
									.isVisible( ) ) )
					{
						dHeight += dSeparatorThickness;

						legendItems.add( new LegendItemHints( LEGEND_SEPERATOR,
								new Point( dExtraWidth, dHeight
										- dSeparatorThickness
										/ 2 ),
								dMaxW
										+ insCA.getLeft( )
										+ insCA.getRight( )
										+ ( 3 * dItemHeight )
										/ 2,
								0,
								null,
								0,
								null ) );
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
				dHeight = Math.max( dRealHeight, dHeight );
			}
			else if ( direction.getValue( ) == Direction.LEFT_RIGHT )
			{
				// (VERTICAL => LR)

				dSeparatorThickness += dHorizontalSpacing;

				for ( int j = 0; j < seda.length; j++ )
				{
					al = seda[j].getRunTimeSeries( );
					FormatSpecifier fs = seda[j].getFormatSpecifier( );
					for ( int i = 0; i < al.size( ); i++ )
					{
						se = (Series) al.get( i );
						Object obj = se.getSeriesIdentifier( );
						String lgtext = rtc.externalizedMessage( String.valueOf( obj ) );
						if ( fs != null )
						{
							try
							{
								lgtext = ValueFormatter.format( lgtext,
										fs,
										rtc.getULocale( ),
										null );
							}
							catch ( ChartException e )
							{
								// ignore, use original text.
							}
						}
						la.getCaption( ).setValue( lgtext );
						itm.reuse( la, maxWrappingSize );

						BoundingBox bb = null;
						try
						{
							bb = Methods.computeBox( xs,
									IConstants.ABOVE,
									la,
									0,
									0 );
						}
						catch ( IllegalArgumentException uiex )
						{
							throw new ChartException( ChartEnginePlugin.ID,
									ChartException.RENDERING,
									uiex );
						}
						dW = bb.getWidth( );

						double dFHeight = bb.getHeight( );
						double dExtraHeight = 0;
						String extraText = null;

						dDeltaHeight = insCA.getTop( )
								+ dFHeight
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
												rtc.getULocale( ),
												null );
									}
									catch ( ChartException e )
									{
										// ignore, use original text.
									}
								}

								Label seLabel = LabelImpl.copyInstance( se.getLabel( ) );
								seLabel.getCaption( ).setValue( valueText );
								itm.reuse( seLabel );

								dW = Math.max( dW, itm.getFullWidth( ) );

								dExtraHeight = itm.getFullHeight( );
								extraText = seLabel.getCaption( ).getValue( );

								dDeltaHeight += itm.getFullHeight( ) + 2 * dScale;
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
							dRealHeight = Math.max( dRealHeight, dHeight );
							dHeight = dDeltaHeight;
						}
						else
						{
							dMaxW = Math.max( dW, dMaxW );
							dHeight += dDeltaHeight;
						}

						legendItems.add( new LegendItemHints( LEGEND_ENTRY,
								new Point( dExtraWidth, dHeight - dDeltaHeight ),
								dW,
								dFHeight,
								la.getCaption( ).getValue( ),
								dExtraHeight,
								extraText ) );
					}

					dExtraWidth += dMaxW
							+ insCA.getLeft( )
							+ insCA.getRight( )
							+ ( 3 * dItemHeight )
							/ 2
							+ dHorizontalSpacing;
					dMaxW = 0;
					dRealHeight = Math.max( dRealHeight, dHeight );
					dHeight = 0;

					// SETUP VERTICAL SEPARATOR SPACING
					if ( j < seda.length - 1
							&& ( lg.getSeparator( ) == null || lg.getSeparator( )
									.isVisible( ) ) )
					{
						dExtraWidth += dSeparatorThickness;

						legendItems.add( new LegendItemHints( LEGEND_SEPERATOR,
								new Point( dExtraWidth
										- dSeparatorThickness
										/ 2, 0 ),
								0,
								dRealHeight,
								null,
								0,
								null ) );
					}
				}

				// LEFT INSETS + LEGEND ITEM WIDTH + HORIZONTAL SPACING +
				// MAX ITEM WIDTH + RIGHT INSETS
				dWidth += dExtraWidth;

				dHeight = Math.max( dRealHeight, dHeight );
			}
			else
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.GENERATION,
						"exception.illegal.rendering.direction", //$NON-NLS-1$
						new Object[]{
							direction.getName( )
						},
						Messages.getResourceBundle( xs.getULocale( ) ) );
			}
		}
		else if ( orientation.getValue( ) == Orientation.HORIZONTAL )
		{
			double dH, dMaxH = 0;
			double dRealWidth = 0, dExtraHeight = 0, dDeltaWidth;

			if ( bPaletteByCategory )
			{
				SeriesDefinition sdBase = null;
				if ( cm instanceof ChartWithAxes )
				{
					// ONLY SUPPORT 1 BASE AXIS FOR NOW
					final Axis axPrimaryBase = ( (ChartWithAxes) cm ).getBaseAxes( )[0];
					if ( axPrimaryBase.getSeriesDefinitions( ).isEmpty( ) )
					{
						throw new ChartException( ChartEnginePlugin.ID,
								ChartException.GENERATION,
								"exception.base.axis.no.series.definitions", //$NON-NLS-1$ 
								Messages.getResourceBundle( xs.getULocale( ) ) );
					}
					// OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
					sdBase = (SeriesDefinition) axPrimaryBase.getSeriesDefinitions( )
							.get( 0 );
				}
				else if ( cm instanceof ChartWithoutAxes )
				{
					if ( ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
							.isEmpty( ) )
					{
						throw new ChartException( ChartEnginePlugin.ID,
								ChartException.GENERATION,
								"exception.base.axis.no.series.definitions", //$NON-NLS-1$
								Messages.getResourceBundle( xs.getULocale( ) ) );
					}
					// OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
					sdBase = (SeriesDefinition) ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
							.get( 0 );
				}
				// OK TO ASSUME THAT 1 BASE RUNTIME SERIES EXISTS
				seBase = (Series) sdBase.getRunTimeSeries( ).get( 0 );

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

				int pos = -1;
				while ( dsiBase.hasNext( ) )
				{
					Object obj = dsiBase.next( );

					pos++;

					// filter the not-used legend.
					if ( bMinSliceApplied
							&& Arrays.binarySearch( filteredMinSliceEntry, pos ) >= 0 )
					{
						continue;
					}

					String lgtext = String.valueOf( obj );
					if ( fs != null )
					{
						try
						{
							lgtext = ValueFormatter.format( obj,
									fs,
									rtc.getULocale( ),
									null );
						}
						catch ( ChartException e )
						{
							// ignore, use original text.
						}
					}
					la.getCaption( ).setValue( lgtext );
					itm.reuse( la, maxWrappingSize );

					BoundingBox bb = null;
					try
					{
						bb = Methods.computeBox( xs, IConstants.ABOVE, la, 0, 0 );
					}
					catch ( IllegalArgumentException uiex )
					{
						throw new ChartException( ChartEnginePlugin.ID,
								ChartException.RENDERING,
								uiex );
					}

					double dFWidth = bb.getWidth( );
					double dFHeight = bb.getHeight( );

					dDeltaWidth = insCA.getLeft( )
							+ dFWidth
							+ ( 3 * dItemHeight )
							/ 2
							+ insCA.getRight( );

					if ( dWidth + dDeltaWidth > dAvailableWidth )
					{
						dExtraHeight += dHeight
								+ insCA.getTop( )
								+ insCA.getBottom( )
								+ dVerticalSpacing;
						dHeight = dFHeight;
						dRealWidth = Math.max( dRealWidth, dWidth );
						dWidth = dDeltaWidth;
					}
					else
					{
						dHeight = Math.max( dFHeight, dHeight );
						dWidth += dDeltaWidth;
					}

					legendItems.add( new LegendItemHints( LEGEND_ENTRY,
							new Point( dWidth - dDeltaWidth, dExtraHeight ),
							dFWidth,
							dFHeight,
							la.getCaption( ).getValue( ),
							pos ) );
				}

				// compute the extra MinSlice legend item if applicable.
				if ( bMinSliceApplied )
				{
					la.getCaption( ).setValue( sMinSliceLabel );
					itm.reuse( la, maxWrappingSize );

					BoundingBox bb = null;
					try
					{
						bb = Methods.computeBox( xs, IConstants.ABOVE, la, 0, 0 );
					}
					catch ( IllegalArgumentException uiex )
					{
						throw new ChartException( ChartEnginePlugin.ID,
								ChartException.RENDERING,
								uiex );
					}
					double dFWidth = bb.getWidth( );
					double dFHeight = bb.getHeight( );

					dDeltaWidth = insCA.getLeft( )
							+ dFWidth
							+ ( 3 * dItemHeight )
							/ 2
							+ insCA.getRight( );

					if ( dWidth + dDeltaWidth > dAvailableWidth )
					{
						dExtraHeight += dHeight
								+ insCA.getTop( )
								+ insCA.getBottom( )
								+ dVerticalSpacing;
						dHeight = dFHeight;
						dRealWidth = Math.max( dRealWidth, dWidth );
						dWidth = dDeltaWidth;
					}
					else
					{
						dHeight = Math.max( dFHeight, dHeight );
						dWidth += dDeltaWidth;
					}

					legendItems.add( new LegendItemHints( LEGEND_MINSLICE_ENTRY,
							new Point( dWidth - dDeltaWidth, dExtraHeight ),
							dFWidth,
							dFHeight,
							la.getCaption( ).getValue( ),
							dsiBase.size( ) ) );
				}

				dHeight += dExtraHeight
						+ insCA.getTop( )
						+ insCA.getBottom( )
						+ dVerticalSpacing;
				dWidth = Math.max( dWidth, dRealWidth );
			}
			else if ( direction.getValue( ) == Direction.TOP_BOTTOM )
			{
				// (HORIZONTAL => TB)

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
						String lgtext = rtc.externalizedMessage( String.valueOf( obj ) );
						if ( fs != null )
						{
							try
							{
								lgtext = ValueFormatter.format( lgtext,
										fs,
										rtc.getULocale( ),
										null );
							}
							catch ( ChartException e )
							{
								// ignore, use original text.
							}
						}
						la.getCaption( ).setValue( lgtext );
						itm.reuse( la, maxWrappingSize );

						BoundingBox bb = null;
						try
						{
							bb = Methods.computeBox( xs,
									IConstants.ABOVE,
									la,
									0,
									0 );
						}
						catch ( IllegalArgumentException uiex )
						{
							throw new ChartException( ChartEnginePlugin.ID,
									ChartException.RENDERING,
									uiex );
						}
						dH = bb.getHeight( );

						double dFHeight = dH;
						double dFWidth = bb.getWidth( );
						double dEHeight = 0;
						String extraText = null;

						dDeltaWidth = insCA.getLeft( )
								+ ( 3 * dItemHeight )
								/ 2
								+ dFWidth
								+ insCA.getRight( )
								+ dHorizontalSpacing;

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
												rtc.getULocale( ),
												null );
									}
									catch ( ChartException e )
									{
										// ignore, use original text.
									}
								}

								Label seLabel = LabelImpl.copyInstance( se.getLabel( ) );
								seLabel.getCaption( ).setValue( valueText );
								itm.reuse( seLabel );

								dEHeight = itm.getFullHeight( );
								extraText = seLabel.getCaption( ).getValue( );

								dH += dEHeight + 2 * dScale;
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
							dRealWidth = Math.max( dRealWidth, dWidth );
							dWidth = dDeltaWidth;
						}
						else
						{
							dMaxH = Math.max( dH, dMaxH );
							dWidth += dDeltaWidth;
						}

						legendItems.add( new LegendItemHints( LEGEND_ENTRY,
								new Point( dWidth - dDeltaWidth, dExtraHeight ),
								dFWidth,
								dFHeight,
								la.getCaption( ).getValue( ),
								dEHeight,
								extraText ) );
					}

					dExtraHeight += dMaxH
							+ insCA.getTop( )
							+ insCA.getBottom( )
							+ dVerticalSpacing;
					dMaxH = 0;
					dRealWidth = Math.max( dRealWidth, dWidth );
					dWidth = 0;

					// SETUP HORIZONTAL SEPARATOR SPACING
					if ( j < seda.length - 1
							&& ( lg.getSeparator( ) == null || lg.getSeparator( )
									.isVisible( ) ) )
					{
						dHeight += dSeparatorThickness;

						legendItems.add( new LegendItemHints( LEGEND_SEPERATOR,
								new Point( 0, dExtraHeight
										- dSeparatorThickness
										/ 2 ),
								dRealWidth,
								0,
								null,
								0,
								null ) );
					}
				}

				dHeight += dExtraHeight;

				dWidth = Math.max( dRealWidth, dWidth );

			}
			else if ( direction.getValue( ) == Direction.LEFT_RIGHT )
			{
				// (HORIZONTAL => LR)

				dSeparatorThickness += dHorizontalSpacing;

				for ( int j = 0; j < seda.length; j++ )
				{
					al = seda[j].getRunTimeSeries( );
					FormatSpecifier fs = seda[j].getFormatSpecifier( );
					for ( int i = 0; i < al.size( ); i++ )
					{
						se = (Series) al.get( i );
						Object obj = se.getSeriesIdentifier( );
						String lgtext = rtc.externalizedMessage( String.valueOf( obj ) );
						if ( fs != null )
						{
							try
							{
								lgtext = ValueFormatter.format( lgtext,
										fs,
										rtc.getULocale( ),
										null );
							}
							catch ( ChartException e )
							{
								// ignore, use original text.
							}
						}
						la.getCaption( ).setValue( lgtext );
						itm.reuse( la, maxWrappingSize );

						BoundingBox bb = null;
						try
						{
							bb = Methods.computeBox( xs,
									IConstants.ABOVE,
									la,
									0,
									0 );
						}
						catch ( IllegalArgumentException uiex )
						{
							throw new ChartException( ChartEnginePlugin.ID,
									ChartException.RENDERING,
									uiex );
						}
						dH = bb.getHeight( );

						double dFHeight = dH;
						double dFWidth = bb.getWidth( );
						double dEHeight = 0;
						String extraText = null;

						dDeltaWidth = insCA.getLeft( )
								+ ( 3 * dItemHeight )
								/ 2
								+ dFWidth
								+ insCA.getRight( )
								+ dHorizontalSpacing;

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
												rtc.getULocale( ),
												null );
									}
									catch ( ChartException e )
									{
										// ignore, use original text.
									}
								}

								Label seLabel = LabelImpl.copyInstance( se.getLabel( ) );
								seLabel.getCaption( ).setValue( valueText );
								itm.reuse( seLabel );

								dEHeight = itm.getFullHeight( );
								extraText = seLabel.getCaption( ).getValue( );

								dH += dEHeight + 2 * dScale;
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
							dRealWidth = Math.max( dRealWidth, dWidth );
							dWidth = dDeltaWidth;
						}
						else
						{
							dMaxH = Math.max( dH, dMaxH );
							dWidth += dDeltaWidth;
						}

						legendItems.add( new LegendItemHints( LEGEND_ENTRY,
								new Point( dWidth - dDeltaWidth, dExtraHeight ),
								dFWidth,
								dFHeight,
								la.getCaption( ).getValue( ),
								dEHeight,
								extraText ) );
					}

					// SETUP VERTICAL SEPARATOR SPACING
					if ( j < seda.length - 1
							&& ( lg.getSeparator( ) == null || lg.getSeparator( )
									.isVisible( ) ) )
					{
						dWidth += dSeparatorThickness;

						legendItems.add( new LegendItemHints( LEGEND_SEPERATOR,
								new Point( dWidth - dSeparatorThickness / 2,
										dExtraHeight ),
								0,
								dMaxH
										+ insCA.getTop( )
										+ insCA.getBottom( )
										+ dVerticalSpacing,
								null,
								0,
								null ) );

					}
				}

				dHeight += insCA.getTop( )
						+ dVerticalSpacing
						+ insCA.getBottom( )
						+ dMaxH
						+ dExtraHeight;

				dWidth = Math.max( dRealWidth, dWidth );
			}
			else
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.GENERATION,
						"exception.illegal.rendering.direction", //$NON-NLS-1$
						new Object[]{
							direction
						},
						Messages.getResourceBundle( xs.getULocale( ) ) );
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
					Messages.getResourceBundle( xs.getULocale( ) ) );
		}

		// consider legend title size.
		Label lgTitle = lg.getTitle( );

		Size titleSize = null;

		if ( lgTitle != null && lgTitle.isSetVisible( ) && lgTitle.isVisible( ) )
		{
			lgTitle = LabelImpl.copyInstance( lgTitle );

			// handle external resource string
			final String sPreviousValue = lgTitle.getCaption( ).getValue( );
			lgTitle.getCaption( )
					.setValue( rtc.externalizedMessage( sPreviousValue ) );

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

			int iTitlePos = lg.getTitlePosition( ).getValue( );

			// swap left/right
			if ( rtc.isRightToLeft( ) )
			{
				if ( iTitlePos == Position.LEFT )
				{
					iTitlePos = Position.RIGHT;
				}
				else if ( iTitlePos == Position.RIGHT )
				{
					iTitlePos = Position.LEFT;
				}
			}

			double shadowness = 3 * dScale;

			switch ( iTitlePos )
			{
				case Position.ABOVE :
				case Position.BELOW :
					dHeight += bb.getHeight( ) + 2 * shadowness;
					dWidth = Math.max( dWidth, bb.getWidth( ) + 2 * shadowness );
					break;
				case Position.LEFT :
				case Position.RIGHT :
					dWidth += bb.getWidth( ) + 2 * shadowness;
					dHeight = Math.max( dHeight, bb.getHeight( )
							+ 2
							* shadowness );
					break;
			}

			titleSize = SizeImpl.create( bb.getWidth( ) + 2 * shadowness,
					bb.getHeight( ) + 2 * shadowness );
		}

		itm.dispose( ); // DISPOSE RESOURCE AFTER USE

		if ( rtc != null )
		{
			LegendItemHints[] liha = (LegendItemHints[]) legendItems.toArray( new LegendItemHints[0] );

			// update context hints here.
			LegendLayoutHints lilh = new LegendLayoutHints( SizeImpl.create( dWidth,
					dHeight ),
					titleSize,
					bMinSliceApplied,
					sMinSliceLabel,
					liha );

			rtc.setLegendLayoutHints( lilh );
		}

		sz = SizeImpl.create( dWidth, dHeight );
		return sz;
	}

	private static int[] getDuplicateIndices( int[] a1, int[] a2 )
	{
		if ( a1 == null || a2 == null || a1.length == 0 || a2.length == 0 )
		{
			return null;
		}

		// sort first.
		Arrays.sort( a1 );
		Arrays.sort( a2 );

		if ( a1[a1.length - 1] < a2[0] || a1[0] > a2[a2.length - 1] )
		{
			return null;
		}

		// swap to keep a1 have the min length.
		if ( a1.length > a2.length )
		{
			int[] tmp = a1;
			a1 = a2;
			a2 = tmp;
		}

		List dup = new ArrayList( );

		// check duplicate
		for ( int i = 0; i < a1.length; i++ )
		{
			if ( Arrays.binarySearch( a2, a1[i] ) >= 0 )
			{
				dup.add( new Integer( a1[i] ) );
			}
		}

		if ( dup.size( ) == 0 )
		{
			return null;
		}
		else
		{
			Integer[] ia = (Integer[]) dup.toArray( new Integer[0] );
			int[] pia = new int[ia.length];

			for ( int i = 0; i < ia.length; i++ )
			{
				pia[i] = ia[i].intValue( );
			}
			return pia;
		}
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
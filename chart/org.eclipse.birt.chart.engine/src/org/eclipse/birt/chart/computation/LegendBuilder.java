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
import org.eclipse.birt.chart.model.attribute.Anchor;
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
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.render.BaseRenderer;
import org.eclipse.emf.common.util.EList;

/**
 * A helper class for Legend computation.
 */
public final class LegendBuilder implements IConstants
{

	/**
	 * inner class for legend data
	 */
	private class LegendData
	{

		private boolean bMinSliceApplied;
		private int[] filteredMinSliceEntry;
		private double maxWrappingSize;
		private double dHorizonalReservedSpace;
		private double dVerticalReservedSpace;
		private double dAvailableWidth;
		private double dAvailableHeight;
		private double dItemHeight;
		private double dVerticalSpacing;
		private double dHorizontalSpacing;
		private double dScale;
		private double dSeparatorThickness;
		private double dSafeSpacing;
		private double dEllipsisWidth;
		private List legendItems = new ArrayList( );
		private Insets insCa;
		private String sMinSliceLabel;

	}

	private static final String ELLIPSIS_STRING = "..."; //$NON-NLS-1$

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
		LegendData legendData = new LegendData( );
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
		final Position lgPosition = lg.getPosition( );
		final boolean bPaletteByCategory = ( lg.getItemType( ).getValue( ) == LegendItemType.CATEGORIES );

		Label la = LabelImpl.create( );
		la.setCaption( TextImpl.copyInstance( lg.getText( ) ) );

		ClientArea ca = lg.getClientArea( );
		LineAttributes lia = ca.getOutline( );
		legendData.dSeparatorThickness = lia.getThickness( );
		la.getCaption( ).setValue( "X" ); //$NON-NLS-1$
		final ITextMetrics itm = xs.getTextMetrics( la );
		legendData.dItemHeight = itm.getFullHeight( );

		la.getCaption( ).setValue( ELLIPSIS_STRING );
		itm.reuse( la );
		legendData.dEllipsisWidth = itm.getFullWidth( );

		legendData.dScale = xs.getDpiResolution( ) / 72d;
		legendData.insCa = ca.getInsets( ).scaledInstance( legendData.dScale );

		legendData.maxWrappingSize = lg.getWrappingSize( ) * legendData.dScale;

		legendData.dHorizontalSpacing = 3 * legendData.dScale;
		legendData.dVerticalSpacing = 3 * legendData.dScale;

		legendData.dSafeSpacing = 3 * legendData.dScale;

		legendData.dHorizonalReservedSpace = legendData.insCa.getLeft( )
				+ legendData.insCa.getRight( )
				+ ( 3 * legendData.dItemHeight )
				/ 2
				+ legendData.dHorizontalSpacing;
		legendData.dVerticalReservedSpace = legendData.insCa.getTop( )
				+ legendData.insCa.getBottom( )
				+ legendData.dVerticalSpacing;

		// Get maximum block width/height available
		final Block bl = cm.getBlock( );
		final Bounds boFull = bl.getBounds( )
				.scaledInstance( legendData.dScale );
		final Insets ins = bl.getInsets( ).scaledInstance( legendData.dScale );
		final Insets lgIns = lg.getInsets( ).scaledInstance( legendData.dScale );

		int titleWPos = 0;
		int titleHPos = 0;

		final TitleBlock titleBlock = cm.getTitle( );
		final Bounds titleBounds = titleBlock.getBounds( )
				.scaledInstance( legendData.dScale );

		if ( titleBlock.isVisible( ) )
		{
			switch ( titleBlock.getAnchor( ).getValue( ) )
			{
				case Anchor.EAST :
				case Anchor.WEST :
					titleWPos = 1;
					break;
				case Anchor.NORTH :
				case Anchor.NORTH_EAST :
				case Anchor.NORTH_WEST :
				case Anchor.SOUTH :
				case Anchor.SOUTH_EAST :
				case Anchor.SOUTH_WEST :
					titleHPos = 1;
					break;
			}
		}

		legendData.dAvailableWidth = boFull.getWidth( )
				- ins.getLeft( )
				- ins.getRight( )
				- lgIns.getLeft( )
				- lgIns.getRight( )
				- titleBounds.getWidth( )
				* titleWPos;

		legendData.dAvailableHeight = boFull.getHeight( )
				- ins.getTop( )
				- ins.getBottom( )
				- lgIns.getTop( )
				- lgIns.getBottom( )
				- titleBounds.getHeight( )
				* titleHPos;

		// TODO ...
		// check 1/3 chart block size constraint for legend block
		double dMaxLegendWidth = boFull.getWidth( ) / 3;
		double dMaxLegendHeight = boFull.getHeight( ) / 3;

		switch ( lgPosition.getValue( ) )
		{
			case Position.LEFT :
			case Position.RIGHT :
			case Position.OUTSIDE :
				if ( legendData.dAvailableWidth > dMaxLegendWidth )
				{
					legendData.dAvailableWidth = dMaxLegendWidth;
				}
				break;
			case Position.ABOVE :
			case Position.BELOW :
				if ( legendData.dAvailableHeight > dMaxLegendHeight )
				{
					legendData.dAvailableHeight = dMaxLegendHeight;
				}
				break;
		}

		// Calculate if minSlice applicable.
		boolean bMinSliceDefined = false;

		if ( cm instanceof ChartWithoutAxes )
		{
			bMinSliceDefined = ( (ChartWithoutAxes) cm ).isSetMinSlice( );
			legendData.sMinSliceLabel = ( (ChartWithoutAxes) cm ).getMinSliceLabel( );
			if ( legendData.sMinSliceLabel == null
					|| legendData.sMinSliceLabel.length( ) == 0 )
			{
				legendData.sMinSliceLabel = IConstants.UNDEFINED_STRING;
			}
			else
			{
				legendData.sMinSliceLabel = rtc.externalizedMessage( legendData.sMinSliceLabel );
			}
		}

		// calculate if need an extra legend item when minSlice defined.
		if ( bMinSliceDefined
				&& bPaletteByCategory
				&& cm instanceof ChartWithoutAxes )
		{
			calculateExtraLegend( cm, rtc, legendData );
		}

		// consider legend title size.
		Label lgTitle = lg.getTitle( );

		Size titleSize = null;
		BoundingBox titleBounding = null;
		int iTitlePos = -1;

		if ( lgTitle != null && lgTitle.isSetVisible( ) && lgTitle.isVisible( ) )
		{
			lgTitle = LabelImpl.copyInstance( lgTitle );

			// handle external resource string
			final String sPreviousValue = lgTitle.getCaption( ).getValue( );
			lgTitle.getCaption( )
					.setValue( rtc.externalizedMessage( sPreviousValue ) );

			try
			{
				titleBounding = Methods.computeBox( xs,
						IConstants.ABOVE,
						lgTitle,
						0,
						0 );
			}
			catch ( IllegalArgumentException uiex )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.RENDERING,
						uiex );
			}

			iTitlePos = lg.getTitlePosition( ).getValue( );

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

			double shadowness = 3 * legendData.dScale;

			switch ( iTitlePos )
			{
				case Position.ABOVE :
				case Position.BELOW :
					legendData.dAvailableHeight -= titleBounding.getHeight( )
							+ 2
							* shadowness;
					break;
				case Position.LEFT :
				case Position.RIGHT :
					legendData.dAvailableWidth -= titleBounding.getWidth( )
							+ 2
							* shadowness;
					break;
			}

			titleSize = SizeImpl.create( titleBounding.getWidth( )
					+ 2
					* shadowness, titleBounding.getHeight( ) + 2 * shadowness );
		}
		double[] size = null;
		// COMPUTATIONS HERE MUST BE IN SYNC WITH THE ACTUAL RENDERER
		if ( orientation.getValue( ) == Orientation.VERTICAL )
		{

			if ( bPaletteByCategory )
			{
				size = computeVerticalByCategory( xs,
						cm,
						rtc,
						itm,
						la,
						legendData );
			}
			else if ( direction.getValue( ) == Direction.TOP_BOTTOM )
			{
				size = computeVerticalByTopBottomValue( xs,
						cm,
						seda,
						rtc,
						itm,
						la,
						legendData );
			}
			else if ( direction.getValue( ) == Direction.LEFT_RIGHT )
			{
				size = computeVerticalByLeftRightValue( xs,
						cm,
						seda,
						rtc,
						itm,
						la,
						legendData );
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
			if ( bPaletteByCategory )
			{
				size = computeHorizalByCategory( xs,
						cm,
						rtc,
						itm,
						la,
						legendData );
			}
			else if ( direction.getValue( ) == Direction.TOP_BOTTOM )
			{
				size = computeHorizalByTopBottomValue( xs,
						cm,
						seda,
						rtc,
						itm,
						la,
						legendData );
			}
			else if ( direction.getValue( ) == Direction.LEFT_RIGHT )
			{
				size = computeHorizalByLeftRightValue( xs,
						cm,
						seda,
						rtc,
						itm,
						la,
						legendData );
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
		if ( size == null )
		{
			return SizeImpl.create( 0, 0 );
		}

		double dWidth = size[0], dHeight = size[1];
		if ( iTitlePos != -1 )
		{

			double shadowness = 3 * legendData.dScale;

			switch ( iTitlePos )
			{
				case Position.ABOVE :
				case Position.BELOW :
					dHeight += titleBounding.getHeight( ) + 2 * shadowness;
					dWidth = Math.max( dWidth, titleBounding.getWidth( )
							+ 2
							* shadowness );
					break;
				case Position.LEFT :
				case Position.RIGHT :
					dWidth += titleBounding.getWidth( ) + 2 * shadowness;
					dHeight = Math.max( dHeight, titleBounding.getHeight( )
							+ 2
							* shadowness );
					break;
			}
		}

		itm.dispose( ); // DISPOSE RESOURCE AFTER USE

		if ( rtc != null )
		{
			List legendItems = legendData.legendItems;
			LegendItemHints[] liha = (LegendItemHints[]) legendItems.toArray( new LegendItemHints[legendItems.size( )] );

			// update context hints here.
			LegendLayoutHints lilh = new LegendLayoutHints( SizeImpl.create( dWidth,
					dHeight ),
					titleSize,
					legendData.bMinSliceApplied,
					legendData.sMinSliceLabel,
					liha );

			rtc.setLegendLayoutHints( lilh );
		}

		sz = SizeImpl.create( dWidth, dHeight );
		return sz;
	}

	// calculate if need an extra legend item when minSlice defined.
	private void calculateExtraLegend( Chart cm, RunTimeContext rtc,
			LegendData legendData ) throws ChartException
	{
		Map renders = rtc.getSeriesRenderers( );

		if ( renders != null
				&& !( (ChartWithoutAxes) cm ).getSeriesDefinitions( ).isEmpty( ) )
		{
			// OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
			SeriesDefinition sdBase = (SeriesDefinition) ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
					.get( 0 );
			EList sdA = sdBase.getSeriesDefinitions( );
			SeriesDefinition[] sdOrtho = (SeriesDefinition[]) sdA.toArray( new SeriesDefinition[sdA.size( )] );

			DataSetIterator dsiOrtho = null;
			BaseRenderer br;
			boolean started = false;

			ENTRANCE: for ( int i = 0; i < sdOrtho.length; i++ )
			{
				List sdRuntimeSA = sdOrtho[i].getRunTimeSeries( );
				Series[] alRuntimeSeries = (Series[]) sdRuntimeSA.toArray( new Series[sdRuntimeSA.size( )] );

				for ( int j = 0; j < alRuntimeSeries.length; j++ )
				{
					try
					{
						dsiOrtho = new DataSetIterator( alRuntimeSeries[j].getDataSet( ) );

						LegendItemRenderingHints lirh = (LegendItemRenderingHints) renders.get( alRuntimeSeries[j] );

						if ( lirh == null )
						{
							legendData.filteredMinSliceEntry = null;
							break ENTRANCE;
						}

						br = lirh.getRenderer( );

						// ask each render for filtered min slice info
						int[] fsa = br.getFilteredMinSliceEntry( dsiOrtho );

						if ( fsa != null && fsa.length > 0 )
						{
							legendData.bMinSliceApplied = true;
						}

						if ( !started )
						{
							started = true;
							legendData.filteredMinSliceEntry = fsa;
						}
						else
						{
							// get duplicate indices for all renderers
							legendData.filteredMinSliceEntry = getDuplicateIndices( fsa,
									legendData.filteredMinSliceEntry );

							if ( legendData.filteredMinSliceEntry == null
									|| legendData.filteredMinSliceEntry.length == 0 )
							{
								legendData.filteredMinSliceEntry = null;
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
		}

		// assign a zero-length array for successive convenience
		if ( legendData.filteredMinSliceEntry == null )
		{
			legendData.filteredMinSliceEntry = new int[0];
		}
	}

	private double[] computeVerticalByCategory( IDisplayServer xs, Chart cm,
			RunTimeContext rtc, ITextMetrics itm, Label la,
			LegendData legendData ) throws ChartException
	{
		double dWidth = 0, dHeight = 0;
		double dColumnWidth;
		double dRealHeight = 0, dExtraWidth = 0, dDeltaHeight;
		ArrayList columnList = new ArrayList( );

		SeriesDefinition sdBase = null;
		if ( cm instanceof ChartWithAxes )
		{
			// ONLY SUPPORT 1 BASE AXIS FOR NOW
			final Axis axPrimaryBase = ( (ChartWithAxes) cm ).getBaseAxes( )[0];
			if ( axPrimaryBase.getSeriesDefinitions( ).isEmpty( ) )
			{
				return null;
			}
			// OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
			sdBase = (SeriesDefinition) axPrimaryBase.getSeriesDefinitions( )
					.get( 0 );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			if ( ( (ChartWithoutAxes) cm ).getSeriesDefinitions( ).isEmpty( ) )
			{
				return null;
			}
			// OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
			sdBase = (SeriesDefinition) ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
					.get( 0 );
		}
		// OK TO ASSUME THAT 1 BASE RUNTIME SERIES EXISTS
		Series seBase = (Series) sdBase.getRunTimeSeries( ).get( 0 );

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

			// Skip invalid data
			while ( !isValidValue( obj ) && dsiBase.hasNext( ) )
			{
				obj = dsiBase.next( );
			}

			pos++;

			// filter the not-used legend.
			if ( legendData.bMinSliceApplied
					&& Arrays.binarySearch( legendData.filteredMinSliceEntry,
							pos ) >= 0 )
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
			itm.reuse( la, legendData.maxWrappingSize );

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

			double dExceedingSpace = dExtraWidth
					+ dFWidth
					+ legendData.dHorizonalReservedSpace
					- legendData.dAvailableWidth
					- legendData.dSafeSpacing;
			double[] newMetrics = checkEllipsisText( dExceedingSpace,
					dFWidth,
					xs,
					itm,
					la,
					legendData.dEllipsisWidth,
					legendData.maxWrappingSize );
			if ( newMetrics != null )
			{
				dFWidth = newMetrics[0];
				dFHeight = newMetrics[1];
			}

			dDeltaHeight = legendData.insCa.getTop( )
					+ dFHeight
					+ legendData.insCa.getBottom( );

			if ( dHeight + dDeltaHeight > legendData.dAvailableHeight )
			{
				// check available bounds
				dColumnWidth = dWidth + legendData.dHorizonalReservedSpace;
				if ( dExtraWidth + dColumnWidth > legendData.dAvailableWidth
						+ legendData.dSafeSpacing )
				{
					dWidth = -legendData.dHorizonalReservedSpace;
					columnList.clear( );
					break;
				}
				else
				{
					legendData.legendItems.addAll( columnList );
					columnList.clear( );

					dExtraWidth += dColumnWidth;

					dExceedingSpace = dExtraWidth
							+ dFWidth
							+ legendData.dHorizonalReservedSpace
							- legendData.dAvailableWidth
							- legendData.dSafeSpacing;
					newMetrics = checkEllipsisText( dExceedingSpace,
							dFWidth,
							xs,
							itm,
							la,
							legendData.dEllipsisWidth,
							legendData.maxWrappingSize );
					if ( newMetrics != null )
					{
						dFWidth = newMetrics[0];
						dFHeight = newMetrics[1];

						dDeltaHeight = legendData.insCa.getTop( )
								+ dFHeight
								+ legendData.insCa.getBottom( );
					}

					dWidth = dFWidth;
					dRealHeight = Math.max( dRealHeight, dHeight );
					dHeight = dDeltaHeight;
				}
			}
			else
			{
				dWidth = Math.max( dFWidth, dWidth );
				dHeight += dDeltaHeight;
			}

			columnList.add( new LegendItemHints( LEGEND_ENTRY,
					new Point( dExtraWidth, dHeight - dDeltaHeight ),
					dFWidth,
					dFHeight,
					la.getCaption( ).getValue( ),
					pos ) );
		}

		// compute the extra MinSlice legend item if applicable.
		if ( legendData.bMinSliceApplied )
		{
			la.getCaption( ).setValue( legendData.sMinSliceLabel );
			itm.reuse( la, legendData.maxWrappingSize );

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

			double dExceedingSpace = dExtraWidth
					+ dFWidth
					+ legendData.dHorizonalReservedSpace
					- legendData.dAvailableWidth
					- legendData.dSafeSpacing;
			double[] newMetrics = checkEllipsisText( dExceedingSpace,
					dFWidth,
					xs,
					itm,
					la,
					legendData.dEllipsisWidth,
					legendData.maxWrappingSize );
			if ( newMetrics != null )
			{
				dFWidth = newMetrics[0];
				dFHeight = newMetrics[1];
			}

			dDeltaHeight = legendData.insCa.getTop( )
					+ dFHeight
					+ legendData.insCa.getBottom( );

			if ( dHeight + dDeltaHeight > legendData.dAvailableHeight )
			{
				// check available bounds
				dColumnWidth = dWidth + legendData.dHorizonalReservedSpace;
				if ( dExtraWidth + dColumnWidth > legendData.dAvailableWidth
						+ legendData.dSafeSpacing )
				{
					dWidth = -legendData.dHorizonalReservedSpace;
					columnList.clear( );

					// !not add the entry if it exceeds the available
					// bounds.
				}
				else
				{
					legendData.legendItems.addAll( columnList );
					columnList.clear( );

					dExtraWidth += dColumnWidth;

					dExceedingSpace = dExtraWidth
							+ dFWidth
							+ legendData.dHorizonalReservedSpace
							- legendData.dAvailableWidth
							- legendData.dSafeSpacing;
					newMetrics = checkEllipsisText( dExceedingSpace,
							dFWidth,
							xs,
							itm,
							la,
							legendData.dEllipsisWidth,
							legendData.maxWrappingSize );
					if ( newMetrics != null )
					{
						dFWidth = newMetrics[0];
						dFHeight = newMetrics[1];

						dDeltaHeight = legendData.insCa.getTop( )
								+ dFHeight
								+ legendData.insCa.getBottom( );
					}

					dWidth = dFWidth;
					dRealHeight = Math.max( dRealHeight, dHeight );
					dHeight = dDeltaHeight;

					columnList.add( new LegendItemHints( LEGEND_MINSLICE_ENTRY,
							new Point( dExtraWidth, dHeight - dDeltaHeight ),
							dFWidth,
							dFHeight,
							la.getCaption( ).getValue( ),
							dsiBase.size( ) ) );
				}
			}
			else
			{
				dWidth = Math.max( dFWidth, dWidth );
				dHeight += dDeltaHeight;

				columnList.add( new LegendItemHints( LEGEND_MINSLICE_ENTRY,
						new Point( dExtraWidth, dHeight - dDeltaHeight ),
						dFWidth,
						dFHeight,
						la.getCaption( ).getValue( ),
						dsiBase.size( ) ) );
			}
		}

		// check available bounds
		dColumnWidth = dWidth + legendData.dHorizonalReservedSpace;
		if ( dExtraWidth + dColumnWidth > legendData.dAvailableWidth
				+ legendData.dSafeSpacing )
		{
			dWidth = -legendData.dHorizonalReservedSpace;
		}
		else
		{
			legendData.legendItems.addAll( columnList );
		}
		columnList.clear( );

		dWidth += legendData.dHorizonalReservedSpace + dExtraWidth;
		dHeight = Math.max( dRealHeight, dHeight );

		return new double[]{
				dWidth, dHeight
		};
	}

	private double[] computeVerticalByTopBottomValue( IDisplayServer xs,
			Chart cm, SeriesDefinition[] seda, RunTimeContext rtc,
			ITextMetrics itm, Label la, LegendData legendData )
			throws ChartException
	{
		double dWidth = 0, dHeight = 0;
		double dW, dMaxW = 0, dColumnWidth;
		double dRealHeight = 0, dExtraWidth = 0, dDeltaHeight;
		ArrayList columnList = new ArrayList( );

		// (VERTICAL => TB)

		double dSeparatorThickness = legendData.dSeparatorThickness
				+ legendData.dVerticalSpacing;

		for ( int j = 0; j < seda.length; j++ )
		{
			List al = seda[j].getRunTimeSeries( );
			FormatSpecifier fs = seda[j].getFormatSpecifier( );

			boolean oneVisibleSerie = false;

			for ( int i = 0; i < al.size( ); i++ )
			{
				Series se = (Series) al.get( i );

				if ( se.isVisible( ) )
				{
					oneVisibleSerie = true;
				}
				else
				{
					continue;
				}

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
				itm.reuse( la, legendData.maxWrappingSize );

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
				dW = bb.getWidth( );

				double dFHeight = bb.getHeight( );
				double dExtraHeight = 0;
				String extraText = null;

				double dExceedingSpace = dExtraWidth
						+ dW
						+ legendData.dHorizonalReservedSpace
						- legendData.dAvailableWidth
						- legendData.dSafeSpacing;
				double[] newMetrics = checkEllipsisText( dExceedingSpace,
						dW,
						xs,
						itm,
						la,
						legendData.dEllipsisWidth,
						legendData.maxWrappingSize );
				if ( newMetrics != null )
				{
					dW = newMetrics[0];
					dFHeight = newMetrics[1];
				}

				dDeltaHeight = legendData.insCa.getTop( )
						+ dFHeight
						+ legendData.insCa.getBottom( );

				if ( cm.getLegend( ).isShowValue( ) )
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

						// Skip invalid data
						while ( !isValidValue( obj ) && dsiBase.hasNext( ) )
						{
							obj = dsiBase.next( );
						}

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

						BoundingBox bbV = null;
						try
						{
							bbV = Methods.computeBox( xs,
									IConstants.ABOVE,
									seLabel,
									0,
									0 );
						}
						catch ( IllegalArgumentException uiex )
						{
							throw new ChartException( ChartEnginePlugin.ID,
									ChartException.RENDERING,
									uiex );
						}
						double dWV = bbV.getWidth( );

						double dFHeightV = bbV.getHeight( );

						double dExceedingSpaceV = dExtraWidth
								+ dWV
								+ legendData.dHorizonalReservedSpace
								- legendData.dAvailableWidth
								- legendData.dSafeSpacing;
						newMetrics = checkEllipsisText( dExceedingSpaceV,
								dWV,
								xs,
								itm,
								seLabel,
								legendData.dEllipsisWidth,
								legendData.maxWrappingSize );
						if ( newMetrics != null )
						{
							dW = newMetrics[0];
							dFHeightV = newMetrics[1];
						}

						dW = Math.max( dW, itm.getFullWidth( ) );

						dExtraHeight = Math.max( itm.getFullHeight( ),
								dFHeightV );
						extraText = seLabel.getCaption( ).getValue( );

						dDeltaHeight += dExtraHeight + 2 * legendData.dScale;
					}
				}

				if ( dHeight + dDeltaHeight > legendData.dAvailableHeight )
				{
					// check available bounds
					dColumnWidth = dMaxW + legendData.dHorizonalReservedSpace;
					if ( dExtraWidth + dColumnWidth > legendData.dAvailableWidth
							+ legendData.dSafeSpacing )
					{
						dMaxW = -legendData.dHorizonalReservedSpace;
						columnList.clear( );
						break;
					}
					else
					{
						legendData.legendItems.addAll( columnList );
						columnList.clear( );

						dExtraWidth += dColumnWidth;

						dExceedingSpace = dExtraWidth
								+ dW
								+ legendData.dHorizonalReservedSpace
								- legendData.dAvailableWidth
								- legendData.dSafeSpacing;
						newMetrics = checkEllipsisText( dExceedingSpace,
								dW,
								xs,
								itm,
								la,
								legendData.dEllipsisWidth,
								legendData.maxWrappingSize );
						if ( newMetrics != null )
						{
							dW = newMetrics[0];
							dFHeight = newMetrics[1];

							dDeltaHeight = legendData.insCa.getTop( )
									+ dFHeight
									+ legendData.insCa.getBottom( );
						}

						dMaxW = dW;
						dRealHeight = Math.max( dRealHeight, dHeight );
						dHeight = dDeltaHeight;
					}
				}
				else
				{
					dMaxW = Math.max( dW, dMaxW );
					dHeight += dDeltaHeight;
				}

				columnList.add( new LegendItemHints( LEGEND_ENTRY,
						new Point( dExtraWidth, dHeight - dDeltaHeight ),
						dW,
						dFHeight,
						la.getCaption( ).getValue( ),
						dExtraHeight,
						extraText ) );
			}

			// check available bounds
			dColumnWidth = dMaxW + legendData.dHorizonalReservedSpace;
			if ( dExtraWidth + dColumnWidth > legendData.dAvailableWidth
					+ legendData.dSafeSpacing )
			{
				dMaxW = -legendData.dHorizonalReservedSpace;
			}
			else
			{
				legendData.legendItems.addAll( columnList );

				// SETUP HORIZONTAL SEPARATOR SPACING
				if ( oneVisibleSerie
						&& j < seda.length - 1
						&& ( cm.getLegend( ).getSeparator( ) == null || cm.getLegend( )
								.getSeparator( )
								.isVisible( ) ) )
				{
					dHeight += dSeparatorThickness;

					legendData.legendItems.add( new LegendItemHints( LEGEND_SEPERATOR,
							new Point( dExtraWidth, dHeight
									- dSeparatorThickness
									/ 2 ),
							dMaxW
									+ legendData.insCa.getLeft( )
									+ legendData.insCa.getRight( )
									+ ( 3 * legendData.dItemHeight )
									/ 2,
							0,
							null,
							0,
							null ) );
				}
			}
			columnList.clear( );
		}

		// LEFT INSETS + LEGEND ITEM WIDTH + HORIZONTAL SPACING + MAX
		// ITEM WIDTH + RIGHT INSETS
		dWidth = dMaxW + legendData.dHorizonalReservedSpace + dExtraWidth;
		dHeight = Math.max( dRealHeight, dHeight );

		return new double[]{
				dWidth, dHeight
		};

	}

	private double[] computeVerticalByLeftRightValue( IDisplayServer xs,
			Chart cm, SeriesDefinition[] seda, RunTimeContext rtc,
			ITextMetrics itm, Label la, LegendData legendData )
			throws ChartException
	{
		double dWidth = 0, dHeight = 0;
		double dW, dMaxW = 0, dColumnWidth;
		double dRealHeight = 0, dExtraWidth = 0, dDeltaHeight;
		ArrayList columnList = new ArrayList( );

		// (VERTICAL => LR)

		double dSeparatorThickness = legendData.dSeparatorThickness
				+ legendData.dHorizontalSpacing;

		for ( int j = 0; j < seda.length; j++ )
		{
			List al = seda[j].getRunTimeSeries( );
			FormatSpecifier fs = seda[j].getFormatSpecifier( );

			boolean oneVisibleSerie = false;

			for ( int i = 0; i < al.size( ); i++ )
			{
				Series se = (Series) al.get( i );

				if ( se.isVisible( ) )
				{
					oneVisibleSerie = true;
				}
				else
				{
					continue;
				}

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
				itm.reuse( la, legendData.maxWrappingSize );

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
				dW = bb.getWidth( );

				double dFHeight = bb.getHeight( );
				double dExtraHeight = 0;
				String extraText = null;

				double dExceedingSpace = dExtraWidth
						+ dW
						+ legendData.dHorizonalReservedSpace
						- legendData.dAvailableWidth
						- legendData.dSafeSpacing;
				double[] newMetrics = checkEllipsisText( dExceedingSpace,
						dW,
						xs,
						itm,
						la,
						legendData.dEllipsisWidth,
						legendData.maxWrappingSize );
				if ( newMetrics != null )
				{
					dW = newMetrics[0];
					dFHeight = newMetrics[1];
				}

				dDeltaHeight = legendData.insCa.getTop( )
						+ dFHeight
						+ legendData.insCa.getBottom( );

				if ( cm.getLegend( ).isShowValue( ) )
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

						// Skip invalid data
						while (!isValidValue( obj ) && dsiBase.hasNext( ) )
						{
							obj = dsiBase.next( );
						}

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

						dDeltaHeight += itm.getFullHeight( )
								+ 2
								* legendData.dScale;
					}
				}

				if ( dHeight + dDeltaHeight > legendData.dAvailableHeight )
				{
					// check available bounds
					dColumnWidth = dMaxW + legendData.dHorizonalReservedSpace;
					if ( dExtraWidth + dColumnWidth > legendData.dAvailableWidth
							+ legendData.dSafeSpacing )
					{
						dMaxW = -legendData.dHorizonalReservedSpace;
						columnList.clear( );
						break;
					}
					else
					{
						legendData.legendItems.addAll( columnList );
						columnList.clear( );

						dExtraWidth += dColumnWidth;

						dExceedingSpace = dExtraWidth
								+ dW
								+ legendData.dHorizonalReservedSpace
								- legendData.dAvailableWidth
								- legendData.dSafeSpacing;
						newMetrics = checkEllipsisText( dExceedingSpace,
								dW,
								xs,
								itm,
								la,
								legendData.dEllipsisWidth,
								legendData.maxWrappingSize );
						if ( newMetrics != null )
						{
							dW = newMetrics[0];
							dFHeight = newMetrics[1];

							dDeltaHeight = legendData.insCa.getTop( )
									+ dFHeight
									+ legendData.insCa.getBottom( );
						}

						dMaxW = dW;
						dRealHeight = Math.max( dRealHeight, dHeight );
						dHeight = dDeltaHeight;
					}
				}
				else
				{
					dMaxW = Math.max( dW, dMaxW );
					dHeight += dDeltaHeight;
				}

				columnList.add( new LegendItemHints( LEGEND_ENTRY,
						new Point( dExtraWidth, dHeight - dDeltaHeight ),
						dW,
						dFHeight,
						la.getCaption( ).getValue( ),
						dExtraHeight,
						extraText ) );
			}

			// refresh real height
			dRealHeight = Math.max( dRealHeight, dHeight );

			// check available bounds
			dColumnWidth = dMaxW + legendData.dHorizonalReservedSpace;
			if ( dExtraWidth + dColumnWidth > legendData.dAvailableWidth
					+ legendData.dSafeSpacing )
			{
				// do nothing
			}
			else
			{
				legendData.legendItems.addAll( columnList );

				if ( oneVisibleSerie )
				{
					dExtraWidth += dMaxW + legendData.dHorizonalReservedSpace;

					// SETUP VERTICAL SEPARATOR SPACING
					if ( j < seda.length - 1
							&& ( cm.getLegend( ).getSeparator( ) == null || cm.getLegend( )
									.getSeparator( )
									.isVisible( ) ) )
					{
						dExtraWidth += dSeparatorThickness;

						legendData.legendItems.add( new LegendItemHints( LEGEND_SEPERATOR,
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
			}
			columnList.clear( );

			// reset variables.
			dMaxW = 0;
			dHeight = 0;
		}

		// LEFT INSETS + LEGEND ITEM WIDTH + HORIZONTAL SPACING +
		// MAX ITEM WIDTH + RIGHT INSETS
		dWidth += dExtraWidth;
		dHeight = Math.max( dRealHeight, dHeight );

		return new double[]{
				dWidth, dHeight
		};
	}

	private double[] computeHorizalByCategory( IDisplayServer xs, Chart cm,
			RunTimeContext rtc, ITextMetrics itm, Label la,
			LegendData legendData ) throws ChartException
	{
		double dWidth = 0, dHeight = 0;
		double dRowHeight;
		double dRealWidth = 0, dExtraHeight = 0;
		ArrayList columnList = new ArrayList( );

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
			if ( ( (ChartWithoutAxes) cm ).getSeriesDefinitions( ).isEmpty( ) )
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
		Series seBase = (Series) sdBase.getRunTimeSeries( ).get( 0 );

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

			// Skip invalid data
			while (!isValidValue( obj ) && dsiBase.hasNext( ) )
			{
				obj = dsiBase.next( );
			}

			pos++;

			// filter the not-used legend.
			if ( legendData.bMinSliceApplied
					&& Arrays.binarySearch( legendData.filteredMinSliceEntry,
							pos ) >= 0 )
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
			itm.reuse( la, legendData.maxWrappingSize );

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
			double dDeltaWidth = legendData.insCa.getLeft( )
					+ dFWidth
					+ ( 3 * legendData.dItemHeight )
					/ 2
					+ legendData.insCa.getRight( );

			if ( dWidth + dDeltaWidth > legendData.dAvailableWidth )
			{
				// check available bounds
				dRowHeight = dHeight + legendData.dVerticalReservedSpace;
				if ( dExtraHeight + dRowHeight > legendData.dAvailableHeight
						+ legendData.dSafeSpacing )
				{
					dHeight = -legendData.dVerticalReservedSpace;
					columnList.clear( );
					break;
				}
				else
				{
					legendData.legendItems.addAll( columnList );
					columnList.clear( );

					dExtraHeight += dRowHeight;
					dHeight = dFHeight;
					dRealWidth = Math.max( dRealWidth, dWidth );
					dWidth = dDeltaWidth;
				}
			}
			else
			{
				dHeight = Math.max( dFHeight, dHeight );
				dWidth += dDeltaWidth;
			}

			columnList.add( new LegendItemHints( LEGEND_ENTRY,
					new Point( dWidth - dDeltaWidth, dExtraHeight ),
					dFWidth,
					dFHeight,
					la.getCaption( ).getValue( ),
					pos ) );
		}

		// compute the extra MinSlice legend item if applicable.
		if ( legendData.bMinSliceApplied )
		{
			la.getCaption( ).setValue( legendData.sMinSliceLabel );
			itm.reuse( la, legendData.maxWrappingSize );

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

			double dDeltaWidth = legendData.insCa.getLeft( )
					+ dFWidth
					+ ( 3 * legendData.dItemHeight )
					/ 2
					+ legendData.insCa.getRight( );

			if ( dWidth + dDeltaWidth > legendData.dAvailableWidth )
			{
				// check available bounds
				dRowHeight = dHeight + legendData.dVerticalReservedSpace;
				if ( dExtraHeight + dRowHeight > legendData.dAvailableHeight
						+ legendData.dSafeSpacing )
				{
					dHeight = -legendData.dVerticalReservedSpace;
					columnList.clear( );

					// !not add the entry if it exceeds the available
					// bounds.
				}
				else
				{
					legendData.legendItems.addAll( columnList );
					columnList.clear( );

					dExtraHeight += dRowHeight;
					dHeight = dFHeight;
					dRealWidth = Math.max( dRealWidth, dWidth );
					dWidth = dDeltaWidth;

					columnList.add( new LegendItemHints( LEGEND_MINSLICE_ENTRY,
							new Point( dWidth - dDeltaWidth, dExtraHeight ),
							dFWidth,
							dFHeight,
							la.getCaption( ).getValue( ),
							dsiBase.size( ) ) );
				}
			}
			else
			{
				dHeight = Math.max( dFHeight, dHeight );
				dWidth += dDeltaWidth;

				columnList.add( new LegendItemHints( LEGEND_MINSLICE_ENTRY,
						new Point( dWidth - dDeltaWidth, dExtraHeight ),
						dFWidth,
						dFHeight,
						la.getCaption( ).getValue( ),
						dsiBase.size( ) ) );
			}

		}

		// check available bounds
		dRowHeight = dHeight + legendData.dVerticalReservedSpace;
		if ( dExtraHeight + dRowHeight > legendData.dAvailableHeight
				+ legendData.dSafeSpacing )
		{
			dHeight = -legendData.dVerticalReservedSpace;
		}
		else
		{
			legendData.legendItems.addAll( columnList );
		}
		columnList.clear( );

		dHeight += dExtraHeight + legendData.dVerticalReservedSpace;
		dWidth = Math.max( dWidth, dRealWidth );

		return new double[]{
				dWidth, dHeight
		};

	}

	private double[] computeHorizalByTopBottomValue( IDisplayServer xs,
			Chart cm, SeriesDefinition[] seda, RunTimeContext rtc,
			ITextMetrics itm, Label la, LegendData legendData )
			throws ChartException
	{
		double dWidth = 0, dHeight = 0;
		double dH, dMaxH = 0, dRowHeight;
		double dRealWidth = 0, dExtraHeight = 0, dDeltaWidth;
		ArrayList columnList = new ArrayList( );

		// (HORIZONTAL => TB)

		legendData.dSeparatorThickness += legendData.dVerticalSpacing;

		for ( int j = 0; j < seda.length; j++ )
		{
			dWidth = 0;
			List al = seda[j].getRunTimeSeries( );
			FormatSpecifier fs = seda[j].getFormatSpecifier( );
			boolean oneVisibleSerie = false;

			for ( int i = 0; i < al.size( ); i++ )
			{
				Series se = (Series) al.get( i );

				if ( se.isVisible( ) )
				{
					oneVisibleSerie = true;
				}
				else
				{
					continue;
				}

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
				itm.reuse( la, legendData.maxWrappingSize );

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
				dH = bb.getHeight( );

				double dFHeight = dH;
				double dFWidth = bb.getWidth( );
				double dEHeight = 0;
				String extraText = null;

				dDeltaWidth = legendData.insCa.getLeft( )
						+ ( 3 * legendData.dItemHeight )
						/ 2
						+ dFWidth
						+ legendData.insCa.getRight( )
						+ legendData.dHorizontalSpacing;

				if ( cm.getLegend( ).isShowValue( ) )
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

						// Skip invalid data
						while (!isValidValue( obj ) && dsiBase.hasNext( ) )
						{
							obj = dsiBase.next( );
						}

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

						dH += dEHeight + 2 * legendData.dScale;
						dDeltaWidth = Math.max( dDeltaWidth, itm.getFullWidth( ) );
					}
				}

				if ( dWidth + dDeltaWidth > legendData.dAvailableWidth )
				{
					// check available bounds
					dRowHeight = dMaxH + legendData.dVerticalReservedSpace;
					if ( dExtraHeight + dRowHeight > legendData.dAvailableHeight
							+ legendData.dSafeSpacing )
					{
						dMaxH = -legendData.dVerticalReservedSpace;
						columnList.clear( );
						break;
					}
					else
					{
						legendData.legendItems.addAll( columnList );
						columnList.clear( );

						dExtraHeight += dRowHeight;
						dMaxH = dH;
						dRealWidth = Math.max( dRealWidth, dWidth );
						dWidth = dDeltaWidth;
					}
				}
				else
				{
					dMaxH = Math.max( dH, dMaxH );
					dWidth += dDeltaWidth;
				}

				columnList.add( new LegendItemHints( LEGEND_ENTRY,
						new Point( dWidth - dDeltaWidth, dExtraHeight ),
						dFWidth,
						dFHeight,
						la.getCaption( ).getValue( ),
						dEHeight,
						extraText ) );
			}

			// refresh real width
			dRealWidth = Math.max( dRealWidth, dWidth );

			// check available bounds
			dRowHeight = dMaxH + legendData.dVerticalReservedSpace;
			if ( dExtraHeight + dRowHeight > legendData.dAvailableHeight
					+ legendData.dSafeSpacing )
			{
				// do nothing
			}
			else
			{
				legendData.legendItems.addAll( columnList );

				if ( oneVisibleSerie )
				{
					dExtraHeight += dRowHeight;

					// SETUP HORIZONTAL SEPARATOR SPACING
					if ( j < seda.length - 1
							&& ( cm.getLegend( ).getSeparator( ) == null || cm.getLegend( )
									.getSeparator( )
									.isVisible( ) ) )
					{
						dHeight += legendData.dSeparatorThickness;

						legendData.legendItems.add( new LegendItemHints( LEGEND_SEPERATOR,
								new Point( 0, dExtraHeight
										- legendData.dSeparatorThickness
										/ 2 ),
								dRealWidth,
								0,
								null,
								0,
								null ) );
					}
				}
			}
			columnList.clear( );

			// reset variables
			dMaxH = 0;
			dWidth = 0;
		}

		dHeight += dExtraHeight;
		dWidth = Math.max( dRealWidth, dWidth );

		return new double[]{
				dWidth, dHeight
		};
	}

	private double[] computeHorizalByLeftRightValue( IDisplayServer xs,
			Chart cm, SeriesDefinition[] seda, RunTimeContext rtc,
			ITextMetrics itm, Label la, LegendData legendData )
			throws ChartException
	{
		double dWidth = 0, dHeight = 0;
		double dMaxH = 0, dRowHeight;
		double dRealWidth = 0, dExtraHeight = 0, dDeltaWidth;
		ArrayList columnList = new ArrayList( );

		// (HORIZONTAL => LR)

		double dSeparatorThickness = legendData.dSeparatorThickness
				+ legendData.dHorizontalSpacing;

		for ( int j = 0; j < seda.length; j++ )
		{
			List al = seda[j].getRunTimeSeries( );
			FormatSpecifier fs = seda[j].getFormatSpecifier( );
			boolean oneVisibleSerie = false;

			for ( int i = 0; i < al.size( ); i++ )
			{
				Series se = (Series) al.get( i );

				if ( se.isVisible( ) )
				{
					oneVisibleSerie = true;
				}
				else
				{
					continue;
				}

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
				itm.reuse( la, legendData.maxWrappingSize );

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
				double dH = bb.getHeight( );

				double dFHeight = dH;
				double dFWidth = bb.getWidth( );
				double dEHeight = 0;
				String extraText = null;

				dDeltaWidth = legendData.insCa.getLeft( )
						+ ( 3 * legendData.dItemHeight )
						/ 2
						+ dFWidth
						+ legendData.insCa.getRight( )
						+ legendData.dHorizontalSpacing;

				if ( cm.getLegend( ).isShowValue( ) )
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

						// Skip invalid data
						while (!isValidValue( obj ) && dsiBase.hasNext( ) )
						{
							obj = dsiBase.next( );
						}

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

						dH += dEHeight + 2 * legendData.dScale;
						dDeltaWidth = Math.max( dDeltaWidth, itm.getFullWidth( ) );
					}
				}

				if ( dWidth + dDeltaWidth > legendData.dAvailableWidth )
				{
					// check available bounds
					dRowHeight = dMaxH + legendData.dVerticalReservedSpace;
					if ( dExtraHeight + dRowHeight > legendData.dAvailableHeight
							+ legendData.dSafeSpacing )
					{
						dMaxH = -legendData.dVerticalReservedSpace;
						columnList.clear( );
						break;
					}
					else
					{
						legendData.legendItems.addAll( columnList );
						columnList.clear( );

						dExtraHeight += dRowHeight;
						dMaxH = dH;
						dRealWidth = Math.max( dRealWidth, dWidth );
						dWidth = dDeltaWidth;
					}
				}
				else
				{
					dMaxH = Math.max( dH, dMaxH );
					dWidth += dDeltaWidth;
				}

				columnList.add( new LegendItemHints( LEGEND_ENTRY,
						new Point( dWidth - dDeltaWidth, dExtraHeight ),
						dFWidth,
						dFHeight,
						la.getCaption( ).getValue( ),
						dEHeight,
						extraText ) );
			}

			// check available bounds
			dRowHeight = dMaxH + legendData.dVerticalReservedSpace;
			if ( dExtraHeight + dRowHeight > legendData.dAvailableHeight
					+ legendData.dSafeSpacing )
			{
				dMaxH = -legendData.dVerticalReservedSpace;
			}
			else
			{
				legendData.legendItems.addAll( columnList );

				// SETUP VERTICAL SEPARATOR SPACING
				if ( oneVisibleSerie
						&& j < seda.length - 1
						&& ( cm.getLegend( ).getSeparator( ) == null || cm.getLegend( )
								.getSeparator( )
								.isVisible( ) ) )
				{
					dWidth += dSeparatorThickness;

					legendData.legendItems.add( new LegendItemHints( LEGEND_SEPERATOR,
							new Point( dWidth - dSeparatorThickness / 2,
									dExtraHeight ),
							0,
							dMaxH
									+ legendData.insCa.getTop( )
									+ legendData.insCa.getBottom( ),
							null,
							0,
							null ) );

				}
			}
			columnList.clear( );
		}

		dHeight += legendData.dVerticalReservedSpace + dMaxH + dExtraHeight;
		dWidth = Math.max( dRealWidth, dWidth );

		return new double[]{
				dWidth, dHeight
		};

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
			int[] pia = new int[dup.size( )];

			for ( int i = 0; i < pia.length; i++ )
			{
				pia[i] = ( (Integer) dup.get( i ) ).intValue( );
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

	/**
	 * Checks if current label text should use ellipsis to shorten the length.
	 * 
	 * @return a double array contains the new string width and height metrics,
	 *         e.g. [dNewWidth, dNewHeight]
	 * @throws ChartException
	 */
	private double[] checkEllipsisText( double dExceedingSpace,
			double dOriginalTextWidth, IDisplayServer xs, ITextMetrics itm,
			Label la, double dEllipsisWidth, double maxWrappingSize )
			throws ChartException
	{
		// only check when original text length exceeds the bounds.
		if ( dExceedingSpace > 0 )
		{
			String firstRowText = itm.getLine( 0 );

			int nchars = (int) Math.round( 3
					* ( dExceedingSpace + dEllipsisWidth )
					/ dEllipsisWidth );

			// check available text length
			if ( firstRowText.length( ) >= nchars )
			{
				double dReducedSpace = 0;
				int idx = 0;
				BoundingBox bb = null;
				String newText;

				// incrementally reduce the text length
				while ( dReducedSpace < dExceedingSpace )
				{
					if ( firstRowText.length( ) >= nchars + idx )
					{
						newText = firstRowText.substring( 0,
								firstRowText.length( ) - nchars - idx )
								+ ELLIPSIS_STRING;
					}
					else
					{
						break;
					}

					la.getCaption( ).setValue( newText );
					itm.reuse( la, maxWrappingSize );

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

					dReducedSpace = dOriginalTextWidth - bb.getWidth( );
					idx++;
				}

				if ( bb != null )
				{
					return new double[]{
							bb.getWidth( ), bb.getHeight( )
					};
				}
			}
		}

		return null;
	}

	private boolean isValidValue( Object obj )
	{
		if ( obj == null )
		{
			return false;
		}
		if ( obj instanceof Double )
		{
			return !( (Double) obj ).isNaN( ) && !( (Double) obj ).isInfinite( );
		}
		if ( obj instanceof String )
		{
			return ( (String) obj ).length( ) != 0;
		}
		return true;
	}
}
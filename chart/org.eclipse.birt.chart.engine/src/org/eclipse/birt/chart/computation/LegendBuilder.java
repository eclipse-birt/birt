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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;

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

	private class InvertibleIterator implements Iterator
	{

		private boolean isInverse_ = false;
		private ListIterator lit_ = null;
		private int index_ = -1;

		/**
		 * The constructor.
		 */
		public InvertibleIterator( List tList, boolean isInverse, int index )
		{
			lit_ = tList.listIterator( index );
			isInverse_ = isInverse;
			if ( isInverse )
			{
				index_ = lit_.previousIndex( );
			}
			else
			{
				index_ = lit_.nextIndex( );
			}
		}

		public InvertibleIterator( List tList, boolean isInverse )
		{
			this( tList, isInverse, isInverse ? tList.size( ) : 0 );
		}

		/**
		 * Methods to implement Iterator.
		 */
		public boolean hasNext( )
		{
			return isInverse_ ? lit_.hasPrevious( ) : lit_.hasNext( );
		}

		public final Object next( ) throws NoSuchElementException
		{
			if ( isInverse_ )
			{
				index_ = lit_.previousIndex( );
				return lit_.previous( );
			}
			else
			{
				index_ = lit_.nextIndex( );
				return lit_.next( );
			}
		}

		public void remove( )
		{
		}

		/**
		 * Special Methods.
		 */
		public int getIndex( )
		{
			return index_;
		}

		public boolean getInverse( )
		{
			return isInverse_;
		}

		public void setInverse( boolean isInverse )
		{
			isInverse_ = isInverse;
		}

	}

	private class LabelItem
	{

		public static final String ELLIPSIS_STRING = "..."; //$NON-NLS-1$
		private IDisplayServer xs;
		private RunTimeContext rtc;
		private ITextMetrics itm;
		private Label la;
		private String text; // Text without considering about ellipsis
		private double maxWrappingSize = 0;
		private double dEllipsisWidth;
		BoundingBox bb = null;

		/**
		 * constructor
		 * 
		 * @param xs_
		 * @param rtc_
		 * @param itm_
		 * @param la_
		 */
		public LabelItem( IDisplayServer xs_, RunTimeContext rtc_,
				ITextMetrics itm_, Label la_, double maxWrappingSize_ )
		{
			xs = xs_;
			rtc = rtc_;
			itm = itm_;
			la = la_;
			maxWrappingSize = maxWrappingSize_;
			updateEllipsisWidth( );
		}

		/**
		 * constructor
		 * 
		 * @param xs_
		 * @param rtc_
		 * @param itm_
		 * @param la_
		 */
		public LabelItem( IDisplayServer xs_, RunTimeContext rtc_,
				ITextMetrics itm_, Label la_ )
		{
			this( xs_, rtc_, itm_, la_, 0 );
		}

		/**
		 * copy constructor
		 * 
		 * @param original
		 */
		public LabelItem( LabelItem original )
		{
			xs = original.xs;
			rtc = original.rtc;
			itm = original.itm;
			la = original.la;
			text = original.text;
			maxWrappingSize = original.maxWrappingSize;
			dEllipsisWidth = original.dEllipsisWidth;
			bb = original.bb;
		}

		/**
		 * set string value of the label
		 * 
		 * @param text_
		 * @param fs
		 * @throws ChartException
		 */
		public void setText( String text_, FormatSpecifier fs )
				throws ChartException
		{
			text = text_;

			// apply user defined format if exsists
			if ( fs != null )
			{
				try
				{
					text = ValueFormatter.format( text,
							fs,
							rtc.getULocale( ),
							null );
				}
				catch ( ChartException e )
				{
					// ignore, use original text.
				}
			}

			updateLabel( text );
		}

		public void restoreOriginalText( FormatSpecifier fs )
				throws ChartException
		{
			setText( text, fs );
		}

		/**
		 * set the label and text
		 * 
		 * @param la_
		 * @param text_
		 * @param fs
		 * @throws ChartException
		 */
		public void setLabel( Label la_, String text_, FormatSpecifier fs )
				throws ChartException
		{
			la = la_;
			updateEllipsisWidth( );
			setText( text_, fs );
		}

		public void setLabel( Label la_, String text_, FormatSpecifier fs,
				ITextMetrics itm_ ) throws ChartException
		{
			itm = itm_;
			setLabel( la_, text_, fs );
		}

		/**
		 * Checks if current label text should use ellipsis to shorten the
		 * length.
		 * 
		 * @param dExceedingSpace:
		 *            the expected width to be reduced from the text
		 * @throws ChartException
		 */
		public boolean checkEllipsis( double dWidthLimit )
				throws ChartException
		{
			if ( dWidthLimit < dEllipsisWidth )
			{
				return false;
			}

			double dWidth = getWidth( );

			if ( dWidth <= dWidthLimit )
			{
				return true;
			}

			String strText = itm.getLine( 0 );
			int nmax = strText.length( ) - 1;
			int nchar = (int) ( ( dWidthLimit / dWidth ) * strText.length( ) * 2.5 );
			if ( nchar > nmax )
				nchar = nmax;
			updateLabel( strText.substring( 0, nchar ) + ELLIPSIS_STRING );

			for ( ; getWidth( ) > dWidthLimit && nchar >= 0; nchar-- )
			{
				String newText = strText.substring( 0, nchar )
						+ ELLIPSIS_STRING;
				updateLabel( newText );
			}

			return ( getWidth( ) <= dWidthLimit );
		}

		public double getWidth( )
		{
			return bb.getWidth( );
		}

		public double getHeight( )
		{
			return bb.getHeight( );
		}

		/**
		 * get the display text of the label
		 * 
		 * @return
		 */
		public String getCaption( )
		{
			return la.getCaption( ).getValue( );
		}

		private void updateLabel( String strText ) throws ChartException
		{
			la.getCaption( ).setValue( strText );
			itm.reuse( la, maxWrappingSize );
			updateSize( );
		}

		private void updateSize( ) throws ChartException
		{
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
		}

		private void updateEllipsisWidth( )
		{
			la.getCaption( ).setValue( ELLIPSIS_STRING );
			itm.reuse( la );
			dEllipsisWidth = itm.getFullWidth( );
		}

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
		ITextMetrics itm = null;
		try
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
			itm = xs.getTextMetrics( la );
			legendData.dItemHeight = itm.getFullHeight( );

			la.getCaption( ).setValue( ELLIPSIS_STRING );
			itm.reuse( la );
			legendData.dEllipsisWidth = itm.getFullWidth( );

			legendData.dScale = xs.getDpiResolution( ) / 72d;
			legendData.insCa = ca.getInsets( )
					.scaledInstance( legendData.dScale );

			legendData.maxWrappingSize = lg.getWrappingSize( )
					* legendData.dScale;

			legendData.dHorizontalSpacing = 3 * legendData.dScale;
			legendData.dVerticalSpacing = 3 * legendData.dScale;

			legendData.dSafeSpacing = 3 * legendData.dScale;

			legendData.dHorizonalReservedSpace = legendData.insCa.getLeft( )
					+ legendData.insCa.getRight( )
					+ ( 3 * legendData.dItemHeight ) / 2
					+ legendData.dHorizontalSpacing;
			legendData.dVerticalReservedSpace = legendData.insCa.getTop( )
					+ legendData.insCa.getBottom( )
					+ legendData.dVerticalSpacing;

			// Get maximum block width/height available
			final Block bl = cm.getBlock( );
			final Bounds boFull = bl.getBounds( )
					.scaledInstance( legendData.dScale );
			final Insets ins = bl.getInsets( )
					.scaledInstance( legendData.dScale );
			final Insets lgIns = lg.getInsets( )
					.scaledInstance( legendData.dScale );

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
					- ins.getLeft( ) - ins.getRight( ) - lgIns.getLeft( )
					- lgIns.getRight( ) - titleBounds.getWidth( ) * titleWPos;

			legendData.dAvailableHeight = boFull.getHeight( )
					- ins.getTop( ) - ins.getBottom( ) - lgIns.getTop( )
					- lgIns.getBottom( ) - titleBounds.getHeight( ) * titleHPos;

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
					&& bPaletteByCategory && cm instanceof ChartWithoutAxes )
			{
				calculateExtraLegend( cm, rtc, legendData );
			}

			// consider legend title size.
			Label lgTitle = lg.getTitle( );

			Size titleSize = null;
			BoundingBox titleBounding = null;
			int iTitlePos = -1;

			if ( lgTitle != null
					&& lgTitle.isSetVisible( ) && lgTitle.isVisible( ) )
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
								+ 2 * shadowness;
						break;
					case Position.LEFT :
					case Position.RIGHT :
						legendData.dAvailableWidth -= titleBounding.getWidth( )
								+ 2 * shadowness;
						break;
				}

				titleSize = SizeImpl.create( titleBounding.getWidth( )
						+ 2 * shadowness, titleBounding.getHeight( )
						+ 2 * shadowness );
			}
			double[] size = null;
			// COMPUTATIONS HERE MUST BE IN SYNC WITH THE ACTUAL RENDERER
			boolean bNeedInvert = needInvert( bPaletteByCategory, cm, seda );
			rtc.putState( "[Legend]bNeedInvert", Boolean.toString( bNeedInvert ) );

			if ( orientation.getValue( ) == Orientation.VERTICAL )
			{

				if ( bPaletteByCategory )
				{
					size = computeVerticalByCategory( xs,
							cm,
							rtc,
							itm,
							la,
							legendData,
							bNeedInvert );
				}
				else if ( direction.getValue( ) == Direction.TOP_BOTTOM )
				{
					size = computeVerticalByValue( xs,
							cm,
							seda,
							rtc,
							itm,
							la,
							legendData,
							bNeedInvert,
							false );
				}
				else if ( direction.getValue( ) == Direction.LEFT_RIGHT )
				{
					size = computeVerticalByValue( xs,
							cm,
							seda,
							rtc,
							itm,
							la,
							legendData,
							bNeedInvert,
							true );
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
							legendData,
							bNeedInvert );
				}
				else if ( direction.getValue( ) == Direction.TOP_BOTTOM )
				{
					size = computeHorizalByValue( xs,
							cm,
							seda,
							rtc,
							itm,
							la,
							legendData,
							bNeedInvert,
							false );
				}
				else if ( direction.getValue( ) == Direction.LEFT_RIGHT )
				{
					size = computeHorizalByValue( xs,
							cm,
							seda,
							rtc,
							itm,
							la,
							legendData,
							bNeedInvert,
							true );
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
								+ 2 * shadowness );
						break;
					case Position.LEFT :
					case Position.RIGHT :
						dWidth += titleBounding.getWidth( ) + 2 * shadowness;
						dHeight = Math.max( dHeight, titleBounding.getHeight( )
								+ 2 * shadowness );
						break;
				}
			}

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

		}
		finally
		{
			itm.dispose( ); // DISPOSE RESOURCE AFTER USE
		}

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
			LegendData legendData, boolean bNeedInvert ) throws ChartException
	{
		double dX = 0, dY = 0;
		double dW = 0, dH = 0;
		double dMaxW = 0, dMaxH = 0;
		ArrayList columnList = new ArrayList( );

		LabelItem laiLegend = new LabelItem( xs,
				rtc,
				itm,
				la,
				legendData.maxWrappingSize );

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

		DataSetIterator dsiBase = createDataSetIterator( seBase, cm );

		FormatSpecifier fs = null;
		if ( sdBase != null )
		{
			fs = sdBase.getFormatSpecifier( );
		}

		int pos = -1;
		dsiBase.reverse( bNeedInvert );

		boolean bHasMoreData = true;
		all: while ( bHasMoreData )
		{
			String lgtext;
			int categoryIndex;

			if ( dsiBase.hasNext( ) )
			{
				Object obj = dsiBase.next( );

				// Replace with one space char if it is null, and it can be
				// dispalyed normally with empty label. - Henry
				obj = getNonEmptyValue( obj, IConstants.ONE_SPACE );

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

				lgtext = String.valueOf( obj );
				categoryIndex = LEGEND_ENTRY;
			}
			else if ( legendData.bMinSliceApplied )
			{
				lgtext = legendData.sMinSliceLabel;
				categoryIndex = LEGEND_MINSLICE_ENTRY;
				bHasMoreData = false;
				pos++;

			}
			else
			{
				break;
			}

			laiLegend.setText( lgtext, fs );

			// check available bounds
			for ( boolean bRedo = true; bRedo; )
			{
				// compute the size
				double[] dsize = getItemSizeCata( laiLegend, legendData, dX );
				dW = dsize[0];
				dH = dsize[1];

				if ( dX + dW > legendData.dAvailableWidth
						+ legendData.dSafeSpacing )
				{
					columnList.clear( );
					break all;
				}
				else
				{
					if ( dY + dH > legendData.dAvailableHeight )
					{
						legendData.legendItems.addAll( columnList );
						columnList.clear( );
						dX += dMaxW;

						dMaxH = Math.max( dMaxH, dY );
						dY = 0;
						dMaxW = 0;
						bRedo = true;
					}
					else
					{
						dMaxW = Math.max( dW, dMaxW );
						dY += dH;
						bRedo = false;
					}
				}
			}

			columnList.add( new LegendItemHints( categoryIndex,
					new Point( dX, dY - dH ),
					dW - legendData.dHorizonalReservedSpace,
					laiLegend.getHeight( ),
					laiLegend.getCaption( ),
					bNeedInvert ? dsiBase.size( ) - 1 - pos : pos ) );

		}

		// add the rest itmes
		legendData.legendItems.addAll( columnList );
		columnList.clear( );

		double dWidth = dX + dMaxW;
		double dHeight = Math.max( dMaxH, dY );

		return new double[]{
				dWidth, dHeight
		};
	}

	/**
	 * Returns a non empty value, if it is null or empty string, replace with
	 * specified value.
	 * 
	 * @param value
	 *            specified value.
	 * @param defaultValue
	 *            default return value.
	 * @return a non empty value.
	 */
	private Object getNonEmptyValue( Object value, Object defaultValue )
	{
		if ( value == null || value.toString( ).length( ) == 0 )
		{
			return defaultValue;
		}

		return value;
	}

	private double[] computeVerticalByValue( IDisplayServer xs, Chart cm,
			SeriesDefinition[] seda, RunTimeContext rtc, ITextMetrics itm,
			Label la, LegendData legendData, boolean bNeedInvert,
			boolean bIsLeftRight ) throws ChartException
	{
		double dX = 0, dY = 0;
		double dMaxW = 0, dMaxH = 0;
		ArrayList columnList = new ArrayList( );

		LabelItem laiLegend = new LabelItem( xs,
				rtc,
				itm,
				la,
				legendData.maxWrappingSize );
		LabelItem laiValue = new LabelItem( laiLegend );

		// a seperated itm for value text
		ITextMetrics itm_v = xs.getTextMetrics( la );
		boolean bIsShowValue = cm.getLegend( ).isShowValue( );

		// (VERTICAL => TB)
		double dSeparatorThickness = legendData.dSeparatorThickness
				+ ( bIsLeftRight ? legendData.dHorizontalSpacing
						: legendData.dVerticalSpacing );

		all: for ( int j = 0; j < seda.length; j++ )
		{
			int iSedaId = bNeedInvert ? seda.length - 1 - j : j;
			List al = seda[iSedaId].getRunTimeSeries( );
			FormatSpecifier fs = seda[iSedaId].getFormatSpecifier( );

			boolean oneVisibleSerie = false;

			InvertibleIterator it = new InvertibleIterator( al, bNeedInvert );

			while ( it.hasNext( ) )
			{
				Series se = (Series) it.next( );

				if ( se.isVisible( ) )
				{
					oneVisibleSerie = true;
				}
				else
				{
					continue;
				}

				// legend text
				Object obj = se.getSeriesIdentifier( );
				String lgtext = rtc.externalizedMessage( String.valueOf( obj ) );
				// value text
				String strExtText = getValueText( cm, se );

				{
					double dW = 0, dH = 0;
					laiLegend.setText( lgtext, fs );

					double dExtHeight = 0d;
					if ( bIsShowValue )
					{
						Label seLabel = LabelImpl.copyInstance( se.getLabel( ) );
						laiValue.setLabel( seLabel, strExtText, fs, itm_v );
					}

					// check available bounds
					for ( boolean bRedo = true; bRedo; )
					{
						// compute the size
						double[] dsize = getItemSize( laiLegend,
								laiValue,
								bIsShowValue,
								legendData,
								dX );
						dW = dsize[0];
						dH = dsize[1];

						if ( dX + dW > legendData.dAvailableWidth
								+ legendData.dSafeSpacing )
						{
							columnList.clear( );
							break all;
						}
						else
						{
							if ( dY + dH > legendData.dAvailableHeight )
							{
								legendData.legendItems.addAll( columnList );
								columnList.clear( );
								dX += dMaxW;

								dMaxH = Math.max( dMaxH, dY );
								dY = 0;
								dMaxW = 0;
								bRedo = true;
							}
							else
							{
								dMaxW = Math.max( dW, dMaxW );
								dY += dH;
								bRedo = false;
							}
						}
					}

					if ( bIsShowValue )
					{
						dExtHeight = laiValue.getHeight( );
						strExtText = laiValue.getCaption( );
					}

					columnList.add( new LegendItemHints( LEGEND_ENTRY,
							new Point( dX, dY - dH ),
							dW - legendData.dHorizonalReservedSpace,
							laiLegend.getHeight( ),
							laiLegend.getCaption( ),
							dExtHeight,
							strExtText,
							it.getIndex( ) ) );

				}
			}

			// add the rest itmes
			legendData.legendItems.addAll( columnList );
			columnList.clear( );

			boolean bNotLastSeda = ( j < seda.length - 1 );
			if ( bIsLeftRight )
			{
				if ( oneVisibleSerie )
				{
					dX += dMaxW;
					// SETUP VERTICAL SEPARATOR SPACING
					if ( bNotLastSeda && needSeparator( cm ) )
					{
						dX += dSeparatorThickness;

						legendData.legendItems.add( new LegendItemHints( LEGEND_SEPERATOR,
								new Point( dX - dSeparatorThickness / 2, 0 ),
								0,
								dMaxH,
								null,
								0,
								null ) );
					}
				}

				dY = 0;
			}
			else
			{
				// SETUP HORIZONTAL SEPARATOR SPACING
				dY = putHorizontalSeparator( oneVisibleSerie,
						bNotLastSeda,
						needSeparator( cm ),
						dX,
						dY,
						dSeparatorThickness,
						legendData,
						dMaxW );
			}

		}
		columnList.clear( );

		// LEFT INSETS + LEGEND ITEM WIDTH + HORIZONTAL SPACING + MAX
		// ITEM WIDTH + RIGHT INSETS
		double dWidth = bIsLeftRight ? dX : dX + dMaxW;
		double dHeight = Math.max( dMaxH, dY );

		return new double[]{
				dWidth, dHeight
		};

	}

	private double[] computeHorizalByCategory( IDisplayServer xs, Chart cm,
			RunTimeContext rtc, ITextMetrics itm, Label la,
			LegendData legendData, boolean bNeedInvert ) throws ChartException
	{
		double dX = 0, dY = 0;
		double dW = 0, dH = 0;
		double dMaxW = 0, dMaxH = 0;
		ArrayList columnList = new ArrayList( );

		LabelItem laiLegend = new LabelItem( xs,
				rtc,
				itm,
				la,
				legendData.maxWrappingSize );

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

		DataSetIterator dsiBase = createDataSetIterator( seBase, cm );

		FormatSpecifier fs = null;
		if ( sdBase != null )
		{
			fs = sdBase.getFormatSpecifier( );
		}

		int pos = -1;
		dsiBase.reverse( bNeedInvert );

		boolean bHasMoreData = true;
		all: while ( bHasMoreData )
		{
			String lgtext;
			int categoryIndex;

			if ( dsiBase.hasNext( ) )
			{
				Object obj = dsiBase.next( );

				obj = getNonEmptyValue( obj, IConstants.ONE_SPACE );

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

				lgtext = String.valueOf( obj );
				categoryIndex = LEGEND_ENTRY;
			}
			else if ( legendData.bMinSliceApplied )
			{
				lgtext = legendData.sMinSliceLabel;
				categoryIndex = LEGEND_MINSLICE_ENTRY;
				bHasMoreData = false;
				pos++;

			}
			else
			{
				break;
			}

			laiLegend.setText( lgtext, fs );

			// check available bounds
			for ( boolean bRedo = true; bRedo; )
			{
				// compute the size
				double[] dsize = getItemSizeCata( laiLegend, legendData, dX );
				dW = dsize[0];
				dH = dsize[1];

				if ( dY + dH > legendData.dAvailableHeight
						+ legendData.dSafeSpacing )
				{
					columnList.clear( );
					break all;
				}
				else
				{
					if ( dX + dW > legendData.dAvailableWidth )
					{
						legendData.legendItems.addAll( columnList );
						columnList.clear( );

						dY += dMaxH;
						dMaxH = 0;
						dMaxW = Math.max( dMaxW, dX );
						dX = 0;
						laiLegend.restoreOriginalText( fs );
						bRedo = true;
					}
					else
					{
						dX += dW;
						dMaxH = Math.max( dH, dMaxH );
						bRedo = false;
					}

				}
			}

			columnList.add( new LegendItemHints( categoryIndex,
					new Point( dX - dW, dY ),
					dW - legendData.dHorizonalReservedSpace,
					laiLegend.getHeight( ),
					laiLegend.getCaption( ),
					bNeedInvert ? dsiBase.size( ) - 1 - pos : pos ) );

		}

		legendData.legendItems.addAll( columnList );
		columnList.clear( );

		double dHeight = dMaxH + dY;
		double dWidth = Math.max( dMaxW, dX );

		return new double[]{
				dWidth, dHeight
		};

	}

	private double[] computeHorizalByValue( IDisplayServer xs, Chart cm,
			SeriesDefinition[] seda, RunTimeContext rtc, ITextMetrics itm,
			Label la, LegendData legendData, boolean bNeedInvert,
			boolean bIsLeftRight ) throws ChartException
	{
		double dX = 0, dY = 0;
		double dMaxH = 0, dMaxW = 0;
		ArrayList columnList = new ArrayList( );

		LabelItem laiLegend = new LabelItem( xs,
				rtc,
				itm,
				la,
				legendData.maxWrappingSize );
		LabelItem laiValue = new LabelItem( laiLegend );

		// a seperated itm for value text
		ITextMetrics itm_v = xs.getTextMetrics( la );
		boolean bIsShowValue = cm.getLegend( ).isShowValue( );
		// (HORIZONTAL => LR)

		double dSeparatorThickness = legendData.dSeparatorThickness
				+ ( bIsLeftRight ? legendData.dHorizontalSpacing
						: legendData.dVerticalSpacing );

		all: for ( int j = 0; j < seda.length; j++ )
		{
			int iSedaId = bNeedInvert ? seda.length - 1 - j : j;
			List al = seda[iSedaId].getRunTimeSeries( );
			FormatSpecifier fs = seda[iSedaId].getFormatSpecifier( );
			boolean oneVisibleSerie = false;

			InvertibleIterator it = new InvertibleIterator( al, bNeedInvert );

			while ( it.hasNext( ) )
			{
				Series se = (Series) it.next( );

				if ( se.isVisible( ) )
				{
					oneVisibleSerie = true;
				}
				else
				{
					continue;
				}

				// legend text
				Object obj = se.getSeriesIdentifier( );
				String lgtext = rtc.externalizedMessage( String.valueOf( obj ) );
				// value text
				String strExtText = getValueText( cm, se );

				// {
				double dW = 0, dH = 0;
				laiLegend.setText( lgtext, fs );

				double dExtHeight = 0d;
				if ( bIsShowValue )
				{
					Label seLabel = LabelImpl.copyInstance( se.getLabel( ) );
					laiValue.setLabel( seLabel, strExtText, fs, itm_v );
				}

				// check available bounds
				for ( boolean bRedo = true; bRedo; )
				{
					// compute the size
					double[] dsize = getItemSize( laiLegend,
							laiValue,
							bIsShowValue,
							legendData,
							dX );
					dW = dsize[0];
					dH = dsize[1];

					if ( dY + dH > legendData.dAvailableHeight
							+ legendData.dSafeSpacing )
					{
						columnList.clear( );
						break all;
					}
					else
					{
						if ( dX + dW > legendData.dAvailableWidth )
						{
							legendData.legendItems.addAll( columnList );
							columnList.clear( );

							dY += dMaxH;
							dMaxH = 0;
							dMaxW = Math.max( dMaxW, dX );
							dX = 0;
							laiLegend.restoreOriginalText( fs );
							laiValue.restoreOriginalText( fs );
							bRedo = true;
						}
						else
						{
							dX += dW;
							dMaxH = Math.max( dH, dMaxH );
							bRedo = false;
						}

					}

				}

				if ( bIsShowValue )
				{
					dExtHeight = laiValue.getHeight( );
					strExtText = laiValue.getCaption( );
				}

				columnList.add( new LegendItemHints( LEGEND_ENTRY,
						new Point( dX - dW, dY ),
						dW - legendData.dHorizonalReservedSpace,
						laiLegend.getHeight( ),
						laiLegend.getCaption( ),
						dExtHeight,
						strExtText,
						it.getIndex( ) ) );
			}

			legendData.legendItems.addAll( columnList );

			if ( bIsLeftRight )
			{
				// SETUP VERTICAL SEPARATOR SPACING
				if ( oneVisibleSerie
						&& j < seda.length - 1 && needSeparator( cm ) )
				{
					dX += dSeparatorThickness;

					legendData.legendItems.add( new LegendItemHints( LEGEND_SEPERATOR,
							new Point( dX - dSeparatorThickness / 2, dY ),
							0,
							dMaxH - legendData.dVerticalReservedSpace,
							null,
							0,
							null ) );

				}
			}
			else
			{
				if ( oneVisibleSerie )
				{
					dY += dMaxH;

					// SETUP HORIZONTAL SEPARATOR SPACING
					if ( j < seda.length - 1 && needSeparator( cm ) )
					{
						dY += legendData.dSeparatorThickness;

						legendData.legendItems.add( new LegendItemHints( LEGEND_SEPERATOR,
								new Point( 0, dY
										- legendData.dSeparatorThickness / 2 ),
								dMaxW,
								0,
								null,
								0,
								null ) );
					}
				}

			}

			columnList.clear( );
		}

		columnList.clear( );
		double dHeight = dMaxH + dY;
		double dWidth = Math.max( dMaxW, dX );

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
					* ( dExceedingSpace + dEllipsisWidth ) / dEllipsisWidth );

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

	/**
	 * return the extra value text, if it exsists and is visible
	 * 
	 * @param cm
	 * @param se
	 * @return Value Text
	 * @throws ChartException
	 */
	private String getValueText( Chart cm, Series se ) throws ChartException
	{
		String strValueText = null;

		if ( cm.getLegend( ).isShowValue( ) )
		{
			DataSetIterator dsiBase = createDataSetIterator( se, cm );

			// Use first value for each series.
			if ( dsiBase.hasNext( ) )
			{
				Object obj = dsiBase.next( );

				// Skip invalid data
				while ( !isValidValue( obj ) && dsiBase.hasNext( ) )
				{
					obj = dsiBase.next( );
				}

				strValueText = String.valueOf( obj );

			}
		}

		return strValueText;
	}

	private double[] getItemSize( LabelItem laiLegend, LabelItem laiValue,
			boolean bIsShowValue, LegendData legendData, double dX )
			throws ChartException
	{
		double dWidth = 0, dHeight = 0;

		laiLegend.checkEllipsis( getWidthLimit( dX, legendData ) );

		dWidth = laiLegend.getWidth( );
		dHeight = legendData.insCa.getTop( )
				+ laiLegend.getHeight( ) + legendData.insCa.getBottom( );

		if ( bIsShowValue )
		{
			laiValue.checkEllipsis( getWidthLimit( dX, legendData ) );

			dWidth = Math.max( dWidth, laiValue.getWidth( ) );
			dHeight += laiValue.getHeight( ) + 2 * legendData.dScale;
		}

		dWidth += legendData.dHorizonalReservedSpace;

		return new double[]{
				dWidth, dHeight
		};
	}

	private double[] getItemSizeCata( LabelItem laiLegend,
			LegendData legendData, double dX ) throws ChartException
	{
		double dWidth = 0, dHeight = 0;

		laiLegend.checkEllipsis( getWidthLimit( dX, legendData ) );

		dWidth = laiLegend.getWidth( );
		dHeight = legendData.insCa.getTop( )
				+ laiLegend.getHeight( ) + legendData.insCa.getBottom( );

		dWidth += legendData.dHorizonalReservedSpace;

		return new double[]{
				dWidth, dHeight
		};
	}

	private static double getWidthLimit( double dX, LegendData legendData )
	{
		return legendData.dAvailableWidth
				+ legendData.dSafeSpacing - legendData.dHorizonalReservedSpace
				- dX;
	}

	private double putHorizontalSeparator( boolean oneVisibleSerie,
			boolean bNotLastSeda, boolean bNeedSeparator, double dX, double dY,
			double dSeparatorThickness, LegendData legendData, double dMaxW )
	{
		if ( oneVisibleSerie && bNotLastSeda && bNeedSeparator )
		{
			dY += dSeparatorThickness;

			legendData.legendItems.add( new LegendItemHints( LEGEND_SEPERATOR,
					new Point( dX, dY - dSeparatorThickness / 2 ),
					dMaxW
							+ legendData.insCa.getLeft( )
							+ legendData.insCa.getRight( ),
					0,
					null,
					0,
					null ) );
		}

		return dY;
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

	/**
	 * Check if the legend items need display in a inverted order (Stack Bar)
	 */
	private boolean isStacked( final SeriesDefinition[] seda )
	{
		boolean bIsStack = true;

		for ( int i = 0; i < seda.length; i++ )
		{
			if ( bIsStack )
			{
				// check if the chart is stacked
				// TODO the logic of series stack may be changed.
				for ( Iterator iter = seda[i].getSeries( ).iterator( ); iter.hasNext( ); )
				{
					Series series = (Series) iter.next( );
					if ( !series.isStacked( ) )
					{
						bIsStack = false;
						break;
					}
				}
			}
		}

		return bIsStack;
	}

	/**
	 * Check if the legend items need display in a inverted order (Stack Bar)
	 */
	private boolean needInvert( final boolean bPaletteByCategory,
			final Chart cm, final SeriesDefinition[] seda )
	{
		boolean bNeedInvert = false; // return value

		if ( !( cm instanceof ChartWithAxes ) )
		{
			return false;
		}

		boolean bIsStacked = isStacked( seda );
		boolean bIsFliped = ( (ChartWithAxes) cm ).isTransposed( );

		if ( bPaletteByCategory )
		{ // by Category
			bNeedInvert = bIsFliped;
		}
		else
		{ // by Value
			bNeedInvert = ( bIsStacked && !bIsFliped )
					|| ( !bIsStacked && bIsFliped );
		}

		return bNeedInvert;
	}

	private static boolean needSeparator( final Chart cm )
	{
		return cm.getLegend( ).getSeparator( ) == null
				|| cm.getLegend( ).getSeparator( ).isVisible( );
	}

	private DataSetIterator createDataSetIterator( Series se, Chart cm )
			throws ChartException
	{
		DataSetIterator dsi = null;
		try
		{
			dsi = new DataSetIterator( se.getDataSet( ) );
			// Reverse Legend items if needed
			if ( cm instanceof ChartWithAxes )
			{
				dsi.reverse( ( (ChartWithAxes) cm ).isReverseCategory( ) );
			}
		}
		catch ( Exception ex )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					ex );
		}
		return dsi;
	}
}
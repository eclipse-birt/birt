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

package org.eclipse.birt.chart.render;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.Engine3D;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.computation.Vector;
import org.eclipse.birt.chart.computation.withaxes.AllAxes;
import org.eclipse.birt.chart.computation.withaxes.AutoScale;
import org.eclipse.birt.chart.computation.withaxes.Grid;
import org.eclipse.birt.chart.computation.withaxes.IntersectionValue;
import org.eclipse.birt.chart.computation.withaxes.OneAxis;
import org.eclipse.birt.chart.computation.withaxes.PlotWith2DAxes;
import org.eclipse.birt.chart.computation.withaxes.PlotWithAxes;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.event.BlockGenerationEvent;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.Line3DRenderEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.Polygon3DRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.Text3DRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.TransformationEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.DeferredCache;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ScriptHandler;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.Location3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataElement;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.Lowess;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Provides a base framework for custom series rendering extensions that are
 * interested in being rendered in a pre-computed plot containing axes. Series
 * type extensions could subclass this class to participate in the axes
 * rendering framework.
 */
public abstract class AxesRenderer extends BaseRenderer
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine/render" ); //$NON-NLS-1$

	private Axis ax;

	private Engine3D engine;

	private boolean leftWallFill = false;
	private boolean rightWallFill = false;
	private boolean floorFill = false;

	/**
	 * The constructor.
	 */
	public AxesRenderer( )
	{
		super( );
	}

	/**
	 * Overridden behavior for graphic element series that are plotted along
	 * axes
	 * 
	 * @param bo
	 */
	public final void render( Map htRenderers, Bounds bo )
			throws ChartException
	{
		final boolean bFirstInSequence = ( iSeriesIndex == 0 );
		final boolean bLastInSequence = ( iSeriesIndex == iSeriesCount - 1 );
		long lTimer = System.currentTimeMillis( );
		final Chart cm = getModel( );
		final IDeviceRenderer idr = getDevice( );
		final ScriptHandler sh = getRunTimeContext( ).getScriptHandler( );

		if ( bFirstInSequence ) // SEQUENCE OF MULTIPLE SERIES RENDERERS
		// (POSSIBLY PARTICIPATING IN A COMBINATION
		// CHART)
		{
			// SETUP A TIMER
			lTimer = System.currentTimeMillis( );
			htRenderers.put( TIMER, new Long( lTimer ) );

			// RENDER THE CHART BY WALKING THROUGH THE RECURSIVE BLOCK STRUCTURE
			Block bl = cm.getBlock( );
			final Enumeration e = bl.children( true );
			final BlockGenerationEvent bge = new BlockGenerationEvent( bl );

			// ALWAYS RENDER THE OUTERMOST BLOCK FIRST
			ScriptHandler.callFunction( sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl );
			getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
					bl );
			bge.updateBlock( bl );
			renderBlock( idr, bl, StructureSource.createUnknown( bl ) );
			ScriptHandler.callFunction( sh, ScriptHandler.AFTER_DRAW_BLOCK, bl );
			getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
					bl );

			while ( e.hasMoreElements( ) )
			{
				bl = (Block) e.nextElement( );

				bge.updateBlock( bl );
				if ( bl instanceof Plot )
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderPlot( idr, (Plot) bl );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
					if ( !bLastInSequence )
					{
						break; // STOP AT THE PLOT IF NOT ALSO THE LAST IN THE
						// SEQUENCE
					}
				}
				else if ( bl instanceof TitleBlock )
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderTitle( idr, (TitleBlock) bl );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
				}
				else if ( bl instanceof LabelBlock )
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderLabel( idr, bl, StructureSource.createUnknown( bl ) );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
				}
				else if ( bl instanceof Legend )
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderLegend( idr, (Legend) bl, htRenderers );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
				}
				else
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderBlock( idr, bl, StructureSource.createUnknown( bl ) );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
				}
			}
		}
		else if ( bLastInSequence )
		{
			Block bl = cm.getBlock( );
			final Enumeration e = bl.children( true );
			final BlockGenerationEvent bge = new BlockGenerationEvent( this );

			boolean bStarted = false;
			while ( e.hasMoreElements( ) )
			{
				bl = (Block) e.nextElement( );
				if ( !bStarted && !bl.isPlot( ) )
				{
					continue; // IGNORE ALL BLOCKS UNTIL PLOT IS ENCOUNTERED
				}
				bStarted = true;

				bge.updateBlock( bl );
				if ( bl instanceof Plot )
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderPlot( idr, (Plot) bl );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
				}
				else if ( bl instanceof TitleBlock )
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderTitle( idr, (TitleBlock) bl );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
				}
				else if ( bl instanceof LabelBlock )
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderLabel( idr, bl, StructureSource.createUnknown( bl ) );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
				}
				else if ( bl instanceof Legend )
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderLegend( idr, (Legend) bl, htRenderers );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
				}
				else
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderBlock( idr, bl, StructureSource.createUnknown( bl ) );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
				}
			}
		}
		else
		// FOR ALL SERIES IN-BETWEEN, ONLY RENDER THE PLOT
		{
			final BlockGenerationEvent bge = new BlockGenerationEvent( this );
			Plot p = cm.getPlot( );
			bge.updateBlock( p );
			ScriptHandler.callFunction( sh, ScriptHandler.BEFORE_DRAW_BLOCK, p );
			getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
					p );
			renderPlot( idr, p );
			ScriptHandler.callFunction( sh, ScriptHandler.AFTER_DRAW_BLOCK, p );
			getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
					p );
		}

		lTimer = System.currentTimeMillis( ) - lTimer;
		if ( htRenderers.containsKey( TIMER ) )
		{
			final Long l = (Long) htRenderers.get( TIMER );
			htRenderers.put( TIMER, new Long( l.longValue( ) + lTimer ) );
		}
		else
		{
			htRenderers.put( TIMER, new Long( lTimer ) );
		}
		if ( bLastInSequence )
		{
			Object obj = getComputations( );

			if ( obj instanceof PlotWith2DAxes )
			{
				final PlotWith2DAxes pw2da = (PlotWith2DAxes) getComputations( );
				pw2da.getStackedSeriesLookup( ).resetSubUnits( );
			}
			logger.log( ILogger.INFORMATION,
					Messages.getString( "info.elapsed.render.time", //$NON-NLS-1$
							new Object[]{
								new Long( lTimer )
							}, getRunTimeContext( ).getLocale( ) ) );
			htRenderers.remove( TIMER );
		}
	}

	private final int compare( DataElement de1, DataElement de2 )
			throws ChartException
	{
		if ( de1 == null && de2 == null )
			return IConstants.EQUAL;
		if ( de1 == null || de2 == null )
			return IConstants.SOME_NULL;
		final Class c1 = de1.getClass( );
		final Class c2 = de2.getClass( );
		if ( c1.equals( c2 ) )
		{
			if ( de1 instanceof NumberDataElement )
			{
				return Double.compare( ( (NumberDataElement) de1 ).getValue( ),
						( (NumberDataElement) de2 ).getValue( ) );
			}
			else if ( de1 instanceof DateTimeDataElement )
			{
				final long l1 = ( (DateTimeDataElement) de1 ).getValue( );
				final long l2 = ( (DateTimeDataElement) de1 ).getValue( );
				return ( l1 < l1 ? IConstants.LESS
						: ( l1 == l2 ? IConstants.EQUAL : IConstants.MORE ) );

			}
			else if ( de1 instanceof TextDataElement )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.DATA_FORMAT,
						"exception.unsupported.compare.text", //$NON-NLS-1$ 
						ResourceBundle.getBundle( Messages.ENGINE,
								getRunTimeContext( ).getLocale( ) ) );
			}
			else
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.DATA_FORMAT,
						"exception.unsupported.compare.unknown.objects", //$NON-NLS-1$
						new Object[]{
								de1, de2
						},
						ResourceBundle.getBundle( Messages.ENGINE,
								getRunTimeContext( ).getLocale( ) ) ); // i18n_CONCATENATIONS_REMOVED
			}
		}
		else
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_FORMAT,
					"exception.unsupported.compare.different.objects", //$NON-NLS-1$
					new Object[]{
							de1, de2
					},
					ResourceBundle.getBundle( Messages.ENGINE,
							getRunTimeContext( ).getLocale( ) ) ); // i18n_CONCATENATIONS_REMOVED
		}
	}

	/**
	 * 
	 * @param anc
	 * @return
	 */
	private static final TextAlignment anchorToAlignment( Anchor anc )
	{
		final TextAlignment ta = TextAlignmentImpl.create( ); // SET AS
		// CENTERED
		// HORZ/VERT
		if ( anc == null )
		{
			return ta;
		}

		// SETUP VERTICAL ALIGNMENT
		switch ( anc.getValue( ) )
		{
			case Anchor.NORTH :
			case Anchor.NORTH_EAST :
			case Anchor.NORTH_WEST :
				ta.setVerticalAlignment( VerticalAlignment.TOP_LITERAL );
				break;
			case Anchor.SOUTH :
			case Anchor.SOUTH_EAST :
			case Anchor.SOUTH_WEST :
				ta.setVerticalAlignment( VerticalAlignment.BOTTOM_LITERAL );
				break;
			default :
				ta.setVerticalAlignment( VerticalAlignment.CENTER_LITERAL );
		}

		// SETUP HORIZONTAL ALIGNMENT
		switch ( anc.getValue( ) )
		{
			case Anchor.EAST :
			case Anchor.NORTH_EAST :
			case Anchor.SOUTH_EAST :
				ta.setHorizontalAlignment( HorizontalAlignment.RIGHT_LITERAL );
				break;
			case Anchor.WEST :
			case Anchor.NORTH_WEST :
			case Anchor.SOUTH_WEST :
				ta.setHorizontalAlignment( HorizontalAlignment.LEFT_LITERAL );
				break;
			default :
				ta.setHorizontalAlignment( HorizontalAlignment.CENTER_LITERAL );
		}

		return ta;
	}

	private void sort( double[] a, double[] b, final boolean sortFirstArray )
	{
		double[][] sa = new double[a.length][2];

		for ( int i = 0; i < a.length; i++ )
		{
			double[] ca = new double[2];

			ca[0] = a[i];
			ca[1] = b[i];

			sa[i] = ca;
		}

		Arrays.sort( sa, new Comparator( ) {

			public int compare( Object o1, Object o2 )
			{
				double[] l1 = (double[]) o1;
				double[] l2 = (double[]) o2;

				if ( sortFirstArray )
				{
					if ( l1[0] == l2[0] )
					{
						return 0;
					}

					if ( l1[0] < l2[0] )
					{
						return -1;
					}
				}
				else
				{
					if ( l1[1] == l2[1] )
					{
						return 0;
					}

					if ( l1[1] < l2[1] )
					{
						return -1;
					}
				}

				return 1;
			}
		} );

		for ( int i = 0; i < a.length; i++ )
		{
			a[i] = sa[i][0];
			b[i] = sa[i][1];
		}

	}

	/**
	 * Renders the FittingCurve if defined for supported series.
	 * 
	 * @param ipr
	 * @param points
	 * @param curve
	 * @param bDeferred
	 * @throws ChartException
	 */
	protected final void renderFittingCurve( IPrimitiveRenderer ipr,
			Location[] points, CurveFitting curve, boolean bShowAsTape,
			boolean bDeferred ) throws ChartException
	{
		if ( !curve.getLineAttributes( ).isSetVisible( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.RENDERING,
					"exception.curve.line.visibility.not.defined", //$NON-NLS-1$
					ResourceBundle.getBundle( Messages.ENGINE,
							getRunTimeContext( ).getLocale( ) ) );
		}

		boolean isTransposed = ( (ChartWithAxes) getModel( ) ).isTransposed( );

		if ( curve.getLineAttributes( ).isVisible( ) )
		{
			ScriptHandler.callFunction( getRunTimeContext( ).getScriptHandler( ),
					ScriptHandler.BEFORE_DRAW_FITTING_CURVE,
					curve );
			getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_FITTING_CURVE,
					curve );

			// Render curve.
			double[] xArray = new double[points.length];
			double[] yArray = new double[points.length];

			for ( int i = 0; i < xArray.length; i++ )
			{
				xArray[i] = points[i].getX( );
				yArray[i] = points[i].getY( );
			}

			sort( xArray, yArray, !isTransposed );

			double[] baseArray = xArray, orthogonalArray = yArray;

			if ( isTransposed )
			{
				baseArray = yArray;
				orthogonalArray = xArray;
			}

			Lowess ls = new Lowess( baseArray, orthogonalArray, 0.33f );

			double[] fitYarray = ls.getYEst( );

			orthogonalArray = fitYarray;

			if ( isTransposed )
			{
				baseArray = fitYarray;
				orthogonalArray = yArray;

				sort( baseArray, orthogonalArray, false );
			}

			CurveRenderer crdr = new CurveRenderer( (ChartWithAxes) getModel( ),
					this,
					curve.getLineAttributes( ),
					LocationImpl.create( baseArray, orthogonalArray ),
					bShowAsTape,
					-1,
					bDeferred,
					false,
					null );
			crdr.draw( ipr );

			// Render curve label.
			if ( curve.getLabel( ).isSetVisible( )
					&& curve.getLabel( ).isVisible( ) )
			{
				Label lb = curve.getLabel( );

				if ( isTransposed )
				{
					// transpose text angel.
					lb = (Label) EcoreUtil.copy( lb );

					double rot = lb.getCaption( ).getFont( ).getRotation( );

					if ( rot >= 0 && rot <= 90 )
					{
						rot = -( 90 - rot );
					}
					else if ( rot < 0 && rot >= -90 )
					{
						rot = ( rot + 90 );
					}

					lb.getCaption( ).getFont( ).setRotation( rot );
				}

				BoundingBox bb = Methods.computeBox( getXServer( ),
						IConstants.LEFT/* DONT-CARE */,
						lb,
						0,
						0 );

				Anchor lbPosition = curve.getLabelAnchor( );

				if ( lbPosition == null )
				{
					lbPosition = Anchor.NORTH_LITERAL;
				}

				int horizontal = IConstants.CENTER;
				int vertical = IConstants.ABOVE;

				// convert anchor to position.
				switch ( lbPosition.getValue( ) )
				{
					case Anchor.WEST :
					case Anchor.NORTH_WEST :
					case Anchor.SOUTH_WEST :
						horizontal = IConstants.LEFT;
						break;
					case Anchor.NORTH :
					case Anchor.SOUTH :
						horizontal = IConstants.CENTER;
						break;
					case Anchor.EAST :
					case Anchor.NORTH_EAST :
					case Anchor.SOUTH_EAST :
						horizontal = IConstants.RIGHT;
						break;
				}

				switch ( lbPosition.getValue( ) )
				{
					case Anchor.NORTH :
					case Anchor.NORTH_WEST :
					case Anchor.NORTH_EAST :
					case Anchor.WEST :
					case Anchor.EAST :
						vertical = IConstants.ABOVE;
						break;
					case Anchor.SOUTH :
					case Anchor.SOUTH_WEST :
					case Anchor.SOUTH_EAST :
						vertical = IConstants.BELOW;
						break;
				}

				double xc, yc;

				if ( isTransposed )
				{
					if ( horizontal == IConstants.LEFT )
					{
						yc = orthogonalArray[orthogonalArray.length - 1]
								- bb.getHeight( );
					}
					else if ( horizontal == IConstants.RIGHT )
					{
						yc = orthogonalArray[0] + bb.getHeight( ) / 2d;
					}
					else
					{
						yc = orthogonalArray[0]
								+ ( orthogonalArray[orthogonalArray.length - 1] - orthogonalArray[0] )
								/ 2d;
					}

					xc = getFitYPosition( orthogonalArray,
							baseArray,
							yc,
							bb.getHeight( ),
							bb.getWidth( ),
							vertical == IConstants.BELOW );
				}
				else
				{
					if ( horizontal == IConstants.LEFT )
					{
						xc = xArray[0];
					}
					else if ( horizontal == IConstants.RIGHT )
					{
						xc = xArray[xArray.length - 1] - bb.getWidth( );
					}
					else
					{
						xc = xArray[0]
								+ ( xArray[xArray.length - 1] - xArray[0] )
								/ 2d;
					}

					yc = getFitYPosition( xArray,
							fitYarray,
							xc,
							bb.getWidth( ),
							bb.getHeight( ),
							vertical == IConstants.ABOVE );
				}

				bb.setLeft( xc );
				bb.setTop( yc );

				if ( ChartUtil.isShadowDefined( lb ) )
				{
					renderLabel( StructureSource.createSeries( getSeries( ) ),
							TextRenderEvent.RENDER_SHADOW_AT_LOCATION,
							lb,
							Position.RIGHT_LITERAL,
							LocationImpl.create( bb.getLeft( ), bb.getTop( ) ),
							BoundsImpl.create( bb.getLeft( ),
									bb.getTop( ),
									bb.getWidth( ),
									bb.getHeight( ) ) );
				}

				renderLabel( StructureSource.createSeries( getSeries( ) ),
						TextRenderEvent.RENDER_TEXT_AT_LOCATION,
						lb,
						Position.RIGHT_LITERAL,
						LocationImpl.create( bb.getLeft( ), bb.getTop( ) ),
						BoundsImpl.create( bb.getLeft( ),
								bb.getTop( ),
								bb.getWidth( ),
								bb.getHeight( ) ) );

			}

			ScriptHandler.callFunction( getRunTimeContext( ).getScriptHandler( ),
					ScriptHandler.AFTER_DRAW_FITTING_CURVE,
					curve );
			getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_FITTING_CURVE,
					curve );

		}

	}

	/**
	 * 
	 * @param xa
	 *            xa must be sorted from smallest to largest.
	 * @param ya
	 * @param center
	 * @param width
	 * @param height
	 * @return
	 */
	private double getFitYPosition( double[] xa, double[] ya, double center,
			double width, double height, boolean above )
	{
		int gap = 10;
		int startX = 0, endX = xa.length - 1;
		for ( int i = 0; i < xa.length; i++ )
		{
			if ( xa[i] >= center - width / 2d )
			{
				startX = ( i > 0 ) ? ( i - 1 ) : 0;
				break;
			}
		}

		for ( int i = 0; i < xa.length; i++ )
		{
			if ( xa[i] >= center + width / 2d )
			{
				endX = i;
				break;
			}
		}

		double yc = above ? Double.MAX_VALUE : 0;
		for ( int i = startX; i <= endX; i++ )
		{
			yc = above ? Math.min( yc, ya[i] ) : Math.max( yc, ya[i] );
		}

		return above ? ( yc - height - gap ) : ( yc + gap );
	}

	/**
	 * Renders all marker ranges associated with all axes (base and orthogonal)
	 * in the plot Marker ranges are drawn immediately (not rendered as
	 * deferred) at an appropriate Z-order immediately after the plot background
	 * is drawn.
	 * 
	 * @param oaxa
	 *            An array containing all axes
	 * @param boPlotClientArea
	 *            The bounds of the actual client area
	 * 
	 * @throws RenderingException
	 */
	private final void renderMarkerRanges( OneAxis[] oaxa,
			Bounds boPlotClientArea ) throws ChartException
	{
		Axis ax;
		EList el;
		int iRangeCount, iAxisCount = oaxa.length;
		MarkerRange mr;
		RectangleRenderEvent rre;
		DataElement deStart, deEnd;
		AutoScale asc;
		double dMin = 0, dMax = 0;
		int iOrientation, iCompare = IConstants.EQUAL;

		final Bounds bo = BoundsImpl.create( 0, 0, 0, 0 );
		final IDeviceRenderer idr = getDevice( );
		final ScriptHandler sh = getRunTimeContext( ).getScriptHandler( );
		final boolean bTransposed = ( (ChartWithAxes) getModel( ) ).isTransposed( );
		final PlotWithAxes pwa = (PlotWithAxes) getComputations( );
		final StringBuffer sb = new StringBuffer( );
		Bounds boText = BoundsImpl.create( 0, 0, 0, 0 );
		Anchor anc = null;
		Label la = null;
		TextRenderEvent tre;
		Orientation or;
		double dOriginalAngle = 0;

		for ( int i = 0; i < iAxisCount; i++ )
		{
			ax = oaxa[i].getModelAxis( );
			iOrientation = ax.getOrientation( ).getValue( );
			if ( bTransposed ) // TOGGLE ORIENTATION
			{
				iOrientation = ( iOrientation == Orientation.HORIZONTAL ) ? Orientation.VERTICAL
						: Orientation.HORIZONTAL;
			}

			asc = oaxa[i].getScale( );
			el = ax.getMarkerRanges( );
			iRangeCount = el.size( );

			for ( int j = 0; j < iRangeCount; j++ )
			{
				mr = (MarkerRange) el.get( j );
				ScriptHandler.callFunction( sh,
						ScriptHandler.BEFORE_DRAW_MARKER_RANGE,
						ax,
						mr );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_MARKER_RANGE,
						mr );

				deStart = (DataElement) mr.getStartValue( );
				deEnd = (DataElement) mr.getEndValue( );
				try
				{
					iCompare = compare( deStart, deEnd );
				}
				catch ( ChartException dfex )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.RENDERING,
							dfex );
				}

				// IF OUT OF ORDER, SWAP
				if ( iCompare == IConstants.MORE )
				{
					final DataElement deTemp = deStart;
					deStart = deEnd;
					deEnd = deTemp;
				}

				if ( isDimension3D( ) )
				{
					// TODO render 3D marker range
					return;
				}

				// COMPUTE THE START BOUND
				try
				{
					dMin = ( deStart == null ) ? ( ( iOrientation == Orientation.HORIZONTAL ) ? boPlotClientArea.getLeft( )
							: boPlotClientArea.getTop( )
									+ boPlotClientArea.getHeight( ) )
							: PlotWith2DAxes.getLocation( asc, deStart );
				}
				catch ( Exception ex )
				{
					logger.log( ILogger.WARNING,
							Messages.getString( "exception.cannot.locate.start.marker.range", //$NON-NLS-1$
									new Object[]{
											deStart, mr
									},
									getRunTimeContext( ).getLocale( ) ) );
					continue; // TRY NEXT MARKER RANGE
				}

				// COMPUTE THE END BOUND
				try
				{
					dMax = ( deEnd == null ) ? ( ( iOrientation == Orientation.HORIZONTAL ) ? boPlotClientArea.getLeft( )
							+ boPlotClientArea.getWidth( )
							: boPlotClientArea.getTop( ) )
							: PlotWith2DAxes.getLocation( asc, deEnd );
				}
				catch ( Exception ex )
				{
					logger.log( ILogger.WARNING,
							Messages.getString( "exception.cannot.locate.end.marker.range", //$NON-NLS-1$
									new Object[]{
											deEnd, mr
									},
									getRunTimeContext( ).getLocale( ) ) );
					continue; // TRY NEXT MARKER RANGE
				}

				rre = (RectangleRenderEvent) ( (EventObjectCache) idr ).getEventObject( StructureSource.createMarkerRange( mr ),
						RectangleRenderEvent.class );
				if ( iOrientation == Orientation.HORIZONTAL )
				{
					// RESTRICT RIGHT EDGE
					if ( dMax > boPlotClientArea.getLeft( )
							+ boPlotClientArea.getWidth( ) )
					{
						dMax = boPlotClientArea.getLeft( )
								+ boPlotClientArea.getWidth( );
					}

					// RESTRICT LEFT EDGE
					if ( dMin < boPlotClientArea.getLeft( ) )
					{
						dMax = boPlotClientArea.getLeft( );
					}
					bo.set( dMin,
							boPlotClientArea.getTop( ),
							dMax - dMin,
							boPlotClientArea.getHeight( ) );
				}
				else
				{
					// RESTRICT TOP EDGE
					if ( dMax < boPlotClientArea.getTop( ) )
					{
						dMax = boPlotClientArea.getTop( );
					}

					// RESTRICT BOTTOM EDGE
					if ( dMin > boPlotClientArea.getTop( )
							+ boPlotClientArea.getHeight( ) )
					{
						dMin = boPlotClientArea.getTop( )
								+ boPlotClientArea.getHeight( );
					}
					bo.set( boPlotClientArea.getLeft( ),
							dMax,
							boPlotClientArea.getWidth( ),
							dMin - dMax );
				}

				if ( pwa.getDimension( ) == IConstants.TWO_5_D )
				{
					if ( iOrientation == Orientation.HORIZONTAL )
					{
						bo.translate( pwa.getSeriesThickness( ), 0 );
					}
					else
					{
						bo.translate( 0, -pwa.getSeriesThickness( ) );
					}
				}

				// DRAW THE MARKER RANGE (RECTANGULAR AREA)
				rre.setBounds( bo );
				rre.setOutline( mr.getOutline( ) );
				rre.setBackground( mr.getFill( ) );
				idr.fillRectangle( rre );
				idr.drawRectangle( rre );

				la = mr.getLabel( );
				if ( la.isVisible( ) )
				{
					try
					{
						sb.delete( 0, sb.length( ) );
						sb.append( Messages.getString( "prefix.marker.range.caption", //$NON-NLS-1$ 
								getRunTimeContext( ).getLocale( ) ) );
						sb.append( ValueFormatter.format( deStart,
								mr.getFormatSpecifier( ),
								oaxa[i].getRunTimeContext( ).getLocale( ),
								null ) );
						sb.append( Messages.getString( "separator.marker.range.caption", //$NON-NLS-1$ 
								getRunTimeContext( ).getLocale( ) ) );
						sb.append( ValueFormatter.format( deEnd,
								mr.getFormatSpecifier( ),
								oaxa[i].getRunTimeContext( ).getLocale( ),
								null ) );
						sb.append( Messages.getString( "suffix.marker.range.caption", //$NON-NLS-1$ 
								getRunTimeContext( ).getLocale( ) ) );
						la.getCaption( ).setValue( sb.toString( ) );
					}
					catch ( ChartException dfex )
					{
						throw new ChartException( ChartEnginePlugin.ID,
								ChartException.RENDERING,
								dfex );
					}

					// DETERMINE THE LABEL ANCHOR (TRANSPOSE IF NEEDED)
					anc = mr.getLabelAnchor( );
					if ( bTransposed )
					{
						or = ax.getOrientation( ) == Orientation.HORIZONTAL_LITERAL ? Orientation.VERTICAL_LITERAL
								: Orientation.HORIZONTAL_LITERAL;
						dOriginalAngle = la.getCaption( )
								.getFont( )
								.getRotation( );
						try
						{
							la.getCaption( )
									.getFont( )
									.setRotation( pwa.getTransposedAngle( dOriginalAngle ) );
							anc = pwa.transposedAnchor( or, anc );
						}
						catch ( IllegalArgumentException uiex )
						{
							throw new ChartException( ChartEnginePlugin.ID,
									ChartException.RENDERING,
									uiex );
						}
					}

					BoundingBox bb = null;
					try
					{
						bb = Methods.computeBox( idr.getDisplayServer( ),
								IConstants.LEFT,
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

					boText.set( 0, 0, bb.getWidth( ), bb.getHeight( ) );

					// NOW THAT WE COMPUTED THE BOUNDS, RENDER THE ACTUAL TEXT
					tre = (TextRenderEvent) ( (EventObjectCache) idr ).getEventObject( StructureSource.createMarkerRange( mr ),
							TextRenderEvent.class );
					tre.setBlockBounds( bo );
					tre.setBlockAlignment( anchorToAlignment( anc ) );
					tre.setLabel( la );
					tre.setAction( TextRenderEvent.RENDER_TEXT_IN_BLOCK );
					idr.drawText( tre );

					if ( bTransposed ) // RESTORE ORIGINAL FONT ANGLE IF
					// TRANSPOSED
					{
						la.getCaption( )
								.getFont( )
								.setRotation( dOriginalAngle );
					}
				}

				ScriptHandler.callFunction( sh,
						ScriptHandler.AFTER_DRAW_MARKER_RANGE,
						ax,
						mr );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_MARKER_RANGE,
						mr );
			}
		}
	}

	/**
	 * Ths background is the first component rendered within the plot block.
	 * This is rendered with Z-order=0
	 */
	protected void renderBackground( IPrimitiveRenderer ipr, Plot p )
			throws ChartException
	{
		// PLOT BLOCK STUFF
		super.renderBackground( ipr, p );

		final ChartWithAxes cwa = (ChartWithAxes) getModel( );
		final PlotWithAxes pwa = (PlotWithAxes) getComputations( );

		// PLOT CLIENT AREA
		final ClientArea ca = p.getClientArea( );
		Bounds bo = pwa.getPlotBounds( );
		final RectangleRenderEvent rre = (RectangleRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
				RectangleRenderEvent.class );
		rre.setBounds( bo );
		rre.setOutline( ca.getOutline( ) );
		rre.setBackground( ca.getBackground( ) );
		ipr.fillRectangle( rre );

		// NOW THAT THE AXES HAVE BEEN COMPUTED, FILL THE INTERNAL PLOT AREA
		double dSeriesThickness = pwa.getSeriesThickness( );
		double[] daX = {
				bo.getLeft( ) - dSeriesThickness,
				bo.getLeft( ) + bo.getWidth( ) - dSeriesThickness
		};
		double[] daY = {
				bo.getTop( ) + bo.getHeight( ) + dSeriesThickness,
				bo.getTop( ) + dSeriesThickness
		};

		final AllAxes aax = pwa.getAxes( );
		AutoScale scPrimaryBase = null;
		AutoScale scPrimaryOrthogonal = null;
		AutoScale scAncillaryBase = null;
		double dXStart = 0;
		//double dXEnd = 0;
		double dYStart = 0;
		//double dYEnd = 0;
		double dZStart = 0;
		//double dZEnd = 0;
		int baseTickCount = 0;
		int ancillaryTickCount = 0;
		int orthogonalTickCount = 0;
		double xStep = 0;
		double yStep = 0;
		double zStep = 0;

		if ( isDimension3D( ) )
		{
			scPrimaryBase = aax.getPrimaryBase( ).getScale( );
			scPrimaryOrthogonal = aax.getPrimaryOrthogonal( ).getScale( );
			scAncillaryBase = aax.getAncillaryBase( ).getScale( );

			dXStart = scPrimaryBase.getNormalizedStart( );
			//dXEnd = scPrimaryBase.getNormalizedEnd( );
			dYStart = scPrimaryOrthogonal.getNormalizedStart( );
			//dYEnd = scPrimaryOrthogonal.getNormalizedEnd( );
			dZStart = scAncillaryBase.getNormalizedStart( );
			//dZEnd = scAncillaryBase.getNormalizedEnd( );

			baseTickCount = scPrimaryBase.getTickCordinates( ).length;
			ancillaryTickCount = scAncillaryBase.getTickCordinates( ).length;
			orthogonalTickCount = scPrimaryOrthogonal.getTickCordinates( ).length;

			xStep = scPrimaryBase.getUnitSize( );
			yStep = scPrimaryOrthogonal.getUnitSize( );
			zStep = scAncillaryBase.getUnitSize( );
		}

		if ( pwa.getDimension( ) == IConstants.TWO_5_D )
		{
			Location[] loa = null;

			// DRAW THE LEFT WALL
			if ( cwa.getWallFill( ) == null )
			{
				renderPlane( ipr,
						StructureSource.createPlot( p ),
						new Location[]{
								LocationImpl.create( daX[0], daY[0] ),
								LocationImpl.create( daX[0], daY[1] )
						},
						ca.getBackground( ),
						ca.getOutline( ),
						cwa.getDimension( ),
						dSeriesThickness,
						false );
			}
			else
			{
				loa = new Location[4];
				loa[0] = LocationImpl.create( daX[0], daY[0] );
				loa[1] = LocationImpl.create( daX[0], daY[1] );
				loa[2] = LocationImpl.create( daX[0] + dSeriesThickness, daY[1]
						- dSeriesThickness );
				loa[3] = LocationImpl.create( daX[0] + dSeriesThickness, daY[0]
						- dSeriesThickness );
				final PolygonRenderEvent pre = (PolygonRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
						PolygonRenderEvent.class );
				pre.setPoints( loa );
				pre.setBackground( cwa.getWallFill( ) );
				pre.setOutline( ca.getOutline( ) );
				ipr.fillPolygon( pre );
				ipr.drawPolygon( pre );
			}

			// DRAW THE FLOOR
			if ( cwa.getFloorFill( ) == null )
			{
				renderPlane( ipr,
						StructureSource.createPlot( p ),
						new Location[]{
								LocationImpl.create( daX[0], daY[0] ),
								LocationImpl.create( daX[1], daY[0] )
						},
						ca.getBackground( ),
						ca.getOutline( ),
						cwa.getDimension( ),
						dSeriesThickness,
						false );
			}
			else
			{
				if ( loa == null )
				{
					loa = new Location[4];
				}
				loa[0] = LocationImpl.create( daX[0], daY[0] );
				loa[1] = LocationImpl.create( daX[1], daY[0] );
				loa[2] = LocationImpl.create( daX[1] + dSeriesThickness, daY[0]
						- dSeriesThickness );
				loa[3] = LocationImpl.create( daX[0] + dSeriesThickness, daY[0]
						- dSeriesThickness );
				final PolygonRenderEvent pre = (PolygonRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
						PolygonRenderEvent.class );
				pre.setPoints( loa );
				pre.setBackground( cwa.getFloorFill( ) );
				pre.setOutline( ca.getOutline( ) );
				ipr.fillPolygon( pre );
				ipr.drawPolygon( pre );
			}
		}
		else if ( pwa.getDimension( ) == IConstants.THREE_D )
		{
			Location3D[] loa = null;

			final Polygon3DRenderEvent pre = (Polygon3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
					Polygon3DRenderEvent.class );
			pre.setDoubleSided( true );

			// DRAW THE WALL
			if ( cwa.getWallFill( ) != null )
			{
				loa = new Location3D[4];

				// Left Wall
				// split to small planes to render.
				for ( int i = 0; i < orthogonalTickCount - 1; i++ )
				{
					for ( int j = 0; j < ancillaryTickCount - 1; j++ )
					{
						loa[0] = Location3DImpl.create( dXStart, dYStart
								+ yStep
								* i, dZStart + zStep * j );
						loa[1] = Location3DImpl.create( dXStart, dYStart
								+ ( i + 1 )
								* yStep, dZStart + j * zStep );
						loa[2] = Location3DImpl.create( dXStart, dYStart
								+ ( i + 1 )
								* yStep, dZStart + ( j + 1 ) * zStep );
						loa[3] = Location3DImpl.create( dXStart, dYStart
								+ i
								* yStep, dZStart + ( j + 1 ) * zStep );
						pre.setPoints3D( loa );
						pre.setBackground( cwa.getWallFill( ) );
						pre.setOutline( ca.getOutline( ) );
						getDeferredCache( ).addPlane( pre,
								PrimitiveRenderEvent.DRAW
										| PrimitiveRenderEvent.FILL );
					}
				}

				// loa[0] = Location3DImpl.create( dXStart, dYStart, dZStart );
				// loa[1] = Location3DImpl.create( dXStart, dYEnd, dZStart );
				// loa[2] = Location3DImpl.create( dXStart, dYEnd, dZEnd );
				// loa[3] = Location3DImpl.create( dXStart, dYStart, dZEnd );
				// pre.setPoints3D( loa );
				// pre.setBackground( cwa.getWallFill( ) );
				// pre.setOutline( ca.getOutline( ) );
				// getDeferredCache( ).addPlane( pre,
				// PrimitiveRenderEvent.DRAW | PrimitiveRenderEvent.FILL );

				leftWallFill = true;

				// Right Wall
				// split to small planes to render.
				for ( int i = 0; i < orthogonalTickCount - 1; i++ )
				{
					for ( int j = 0; j < baseTickCount - 1; j++ )
					{
						loa[0] = Location3DImpl.create( dXStart + j * xStep,
								dYStart + i * yStep,
								dZStart );
						loa[1] = Location3DImpl.create( dXStart
								+ ( j + 1 )
								* xStep, dYStart + i * yStep, dZStart );
						loa[2] = Location3DImpl.create( dXStart
								+ ( j + 1 )
								* xStep, dYStart + ( i + 1 ) * yStep, dZStart );
						loa[3] = Location3DImpl.create( dXStart + j * xStep,
								dYStart + ( i + 1 ) * yStep,
								dZStart );
						pre.setPoints3D( loa );
						pre.setBackground( cwa.getWallFill( ) );
						pre.setOutline( ca.getOutline( ) );
						getDeferredCache( ).addPlane( pre,
								PrimitiveRenderEvent.DRAW
										| PrimitiveRenderEvent.FILL );
					}
				}

				// loa[0] = Location3DImpl.create( dXStart, dYStart, dZStart );
				// loa[1] = Location3DImpl.create( dXStart, dYEnd, dZStart );
				// loa[2] = Location3DImpl.create( dXEnd, dYEnd, dZStart );
				// loa[3] = Location3DImpl.create( dXEnd, dYStart, dZStart );
				// pre.setPoints3D( loa );
				// pre.setBackground( cwa.getWallFill( ) );
				// pre.setOutline( ca.getOutline( ) );
				// getDeferredCache( ).addPlane( pre,
				// PrimitiveRenderEvent.DRAW
				// | PrimitiveRenderEvent.FILL );

				rightWallFill = true;
			}

			// DRAW THE FLOOR
			if ( cwa.getFloorFill( ) != null )
			{
				if ( loa == null )
				{
					loa = new Location3D[4];
				}

				// split to small planes to render.
				for ( int i = 0; i < baseTickCount - 1; i++ )
				{
					for ( int j = 0; j < ancillaryTickCount - 1; j++ )
					{
						loa[0] = Location3DImpl.create( dXStart + i * xStep,
								dYStart,
								dZStart + j * zStep );
						loa[1] = Location3DImpl.create( dXStart + i * xStep,
								dYStart,
								dZStart + ( j + 1 ) * zStep );
						loa[2] = Location3DImpl.create( dXStart
								+ ( i + 1 )
								* xStep, dYStart, dZStart + ( j + 1 ) * zStep );
						loa[3] = Location3DImpl.create( dXStart
								+ ( i + 1 )
								* xStep, dYStart, dZStart + j * zStep );
						pre.setPoints3D( loa );
						pre.setBackground( cwa.getFloorFill( ) );
						pre.setOutline( ca.getOutline( ) );
						getDeferredCache( ).addPlane( pre,
								PrimitiveRenderEvent.DRAW
										| PrimitiveRenderEvent.FILL );
					}
				}

				// loa[0] = Location3DImpl.create( dXStart, dYStart, dZStart );
				// loa[1] = Location3DImpl.create( dXStart, dYStart, dZEnd );
				// loa[2] = Location3DImpl.create( dXEnd, dYStart, dZEnd );
				// loa[3] = Location3DImpl.create( dXEnd, dYStart, dZStart );
				// pre.setPoints3D( loa );
				// pre.setBackground( cwa.getFloorFill( ) );
				// pre.setOutline( ca.getOutline( ) );
				// getDeferredCache( ).addPlane( pre,
				// PrimitiveRenderEvent.DRAW
				// | PrimitiveRenderEvent.FILL );

				floorFill = true;
			}

			// Draw the Zero plane
			// IntersectionValue iv = aax.getPrimaryBase(
			// ).getIntersectionValue( );
			// if ( iv != null && iv.getType( ) == IntersectionValue.VALUE )
			// {
			// if ( loa == null )
			// {
			// loa = new Location3D[4];
			// }
			//
			// double yZeroValue = Methods.getNormalizedLocation(
			// scPrimaryOrthogonal,
			// 0 );
			//
			// for ( int i = 0; i < baseTickCount - 1; i++ )
			// {
			// for ( int j = 0; j < ancillaryTickCount - 1; j++ )
			// {
			// loa[0] = Location3DImpl.create( dXStart + i * xStep,
			// yZeroValue,
			// dZStart + j * zStep );
			// loa[1] = Location3DImpl.create( dXStart + i * xStep,
			// yZeroValue,
			// dZStart + ( j + 1 ) * zStep );
			// loa[2] = Location3DImpl.create( dXStart
			// + ( i + 1 )
			// * xStep, yZeroValue, dZStart
			// + ( j + 1 )
			// * zStep );
			// loa[3] = Location3DImpl.create( dXStart
			// + ( i + 1 )
			// * xStep, yZeroValue, dZStart + j * zStep );
			// pre.setPoints3D( loa );
			// pre.setBackground( ColorDefinitionImpl.create( 255,
			// 255,
			// 255,
			// 127 ) );
			// pre.setOutline( null );
			// getDeferredCache( ).addPlane( pre,
			// PrimitiveRenderEvent.FILL );
			// }
			// }
			// }
		}

		// SETUP AXIS ARRAY
		final OneAxis[] oaxa = new OneAxis[2
				+ aax.getOverlayCount( )
				+ ( aax.getAncillaryBase( ) != null ? 1 : 0 )];
		oaxa[0] = aax.getPrimaryBase( );
		oaxa[1] = aax.getPrimaryOrthogonal( );
		for ( int i = 0; i < aax.getOverlayCount( ); i++ )
		{
			oaxa[2 + i] = aax.getOverlay( i );
		}
		if ( aax.getAncillaryBase( ) != null )
		{
			oaxa[2 + aax.getOverlayCount( )] = aax.getAncillaryBase( );
		}

		// RENDER MARKER RANGES (MARKER LINES ARE DRAWN LATER)
		renderMarkerRanges( oaxa, bo );

		// RENDER GRID LINES (MAJOR=DONE; MINOR=DONE)
		double x = 0, y = 0;
		LineAttributes lia;
		LineRenderEvent lre;
		final Insets insCA = aax.getInsets( );

		// RENDER MINOR GRID LINES FIRST
		int iCount;
		Grid g;
		double[] doaMinor = null;
		for ( int i = 0; i < oaxa.length; i++ )
		{
			g = oaxa[i].getGrid( );
			iCount = g.getMinorCountPerMajor( );

			lia = oaxa[i].getGrid( ).getLineAttributes( IConstants.MINOR );
			if ( lia == null || !lia.isSetStyle( ) || !lia.isVisible( ) )
			{
				continue;
			}

			if ( iCount <= 0 )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.RENDERING,
						"exception.cannot.split.major", //$NON-NLS-1$
						new Object[]{
							new Integer( iCount )
						},
						ResourceBundle.getBundle( Messages.ENGINE,
								getRunTimeContext( ).getLocale( ) ) );
			}

			AutoScale sc = oaxa[i].getScale( );
			doaMinor = sc.getMinorCoordinates( iCount );

			if ( isDimension3D( ) )
			{
				Line3DRenderEvent lre3d = (Line3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
						Line3DRenderEvent.class );
				lre3d.setLineAttributes( lia );

				switch ( oaxa[i].getAxisType( ) )
				{
					case IConstants.BASE_AXIS :

						double[] xa = scPrimaryBase.getNormalizedTickCoordinates( );
						if ( floorFill )
						{
							for ( int k = 0; k < xa.length - 1; k++ )
							{
								for ( int j = 0; j < doaMinor.length - 1; j++ )
								{
									for ( int n = 0; n < ancillaryTickCount - 1; n++ )
									{
										lre3d.setStart3D( Location3DImpl.create( xa[k]
												+ doaMinor[j],
												dYStart,
												dZStart + n * zStep ) );
										lre3d.setEnd3D( Location3DImpl.create( xa[k]
												+ doaMinor[j],
												dYStart,
												dZStart + ( n + 1 ) * zStep ) );
										getDeferredCache( ).addLine( lre3d );
									}
								}
							}
						}

						if ( rightWallFill )
						{
							for ( int k = 0; k < xa.length - 1; k++ )
							{
								for ( int j = 0; j < doaMinor.length - 1; j++ )
								{
									for ( int n = 0; n < orthogonalTickCount - 1; n++ )
									{
										lre3d.setStart3D( Location3DImpl.create( xa[k]
												+ doaMinor[j],
												dYStart + n * yStep,
												dZStart ) );
										lre3d.setEnd3D( Location3DImpl.create( xa[k]
												+ doaMinor[j],
												dYStart + ( n + 1 ) * yStep,
												dZStart ) );
										getDeferredCache( ).addLine( lre3d );
									}
								}
							}
						}
						break;
					case IConstants.ORTHOGONAL_AXIS :
						double[] ya = scPrimaryOrthogonal.getNormalizedTickCoordinates( );
						if ( leftWallFill )
						{
							for ( int k = 0; k < ya.length - 1; k++ )
							{
								for ( int j = 0; j < doaMinor.length - 1; j++ )
								{
									for ( int n = 0; n < ancillaryTickCount - 1; n++ )
									{
										lre3d.setStart3D( Location3DImpl.create( dXStart,
												ya[k] + doaMinor[j],
												dZStart + n * zStep ) );
										lre3d.setEnd3D( Location3DImpl.create( dXStart,
												ya[k] + doaMinor[j],
												dZStart + ( n + 1 ) * zStep ) );
										getDeferredCache( ).addLine( lre3d );
									}
								}
							}
						}

						if ( rightWallFill )
						{
							for ( int k = 0; k < ya.length - 1; k++ )
							{
								for ( int j = 0; j < doaMinor.length - 1; j++ )
								{
									for ( int n = 0; n < baseTickCount - 1; n++ )
									{
										lre3d.setStart3D( Location3DImpl.create( dXStart
												+ n
												* xStep,
												ya[k] + doaMinor[j],
												dZStart ) );
										lre3d.setEnd3D( Location3DImpl.create( dXStart
												+ ( n + 1 )
												* xStep,
												ya[k] + doaMinor[j],
												dZStart ) );
										getDeferredCache( ).addLine( lre3d );
									}
								}
							}
						}
						break;
					case IConstants.ANCILLARY_AXIS :
						double[] za = scAncillaryBase.getNormalizedTickCoordinates( );
						if ( leftWallFill )
						{
							for ( int k = 0; k < za.length - 1; k++ )
							{
								for ( int j = 0; j < doaMinor.length - 1; j++ )
								{
									for ( int n = 0; n < orthogonalTickCount - 1; n++ )
									{
										lre3d.setStart3D( Location3DImpl.create( dXStart,
												dYStart + n * yStep,
												za[k] + doaMinor[j] ) );
										lre3d.setEnd3D( Location3DImpl.create( dXStart,
												dYStart + ( n + 1 ) * yStep,
												za[k] + doaMinor[j] ) );
										getDeferredCache( ).addLine( lre3d );
									}
								}
							}
						}

						if ( floorFill )
						{
							for ( int k = 0; k < za.length - 1; k++ )
							{
								for ( int j = 0; j < doaMinor.length - 1; j++ )
								{
									for ( int n = 0; n < baseTickCount - 1; n++ )
									{
										lre3d.setStart3D( Location3DImpl.create( dXStart
												+ n
												* xStep,
												dYStart,
												za[k] + doaMinor[j] ) );
										lre3d.setEnd3D( Location3DImpl.create( dXStart
												+ ( n + 1 )
												* xStep,
												dYStart,
												za[k] + doaMinor[j] ) );
										getDeferredCache( ).addLine( lre3d );
									}
								}
							}
						}
						break;
					default :
						break;
				}
			}
			else if ( oaxa[i].getOrientation( ) == IConstants.HORIZONTAL )
			{
				double[] da = sc.getTickCordinates( );
				double dY2 = bo.getTop( ) + 1, dY1 = bo.getTop( )
						+ bo.getHeight( )
						- 2;
				if ( pwa.getDimension( ) == IConstants.TWO_5_D )
				{
					for ( int j = 0; j < da.length - 1; j++ )
					{
						x = da[j];
						for ( int k = 0; k < doaMinor.length; k++ )
						{
							lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
									LineRenderEvent.class );
							lre.setLineAttributes( lia );
							lre.setStart( LocationImpl.create( x + doaMinor[k],
									dY1 + pwa.getSeriesThickness( ) ) );
							lre.setEnd( LocationImpl.create( x
									+ doaMinor[k]
									+ pwa.getSeriesThickness( ), dY1 ) );
							ipr.drawLine( lre );
						}
					}
				}

				for ( int j = 0; j < da.length - 1; j++ )
				{
					x = da[j];
					if ( pwa.getDimension( ) == IConstants.TWO_5_D )
					{
						x += pwa.getSeriesThickness( );
					}
					for ( int k = 0; k < doaMinor.length; k++ )
					{
						lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
								LineRenderEvent.class );
						lre.setLineAttributes( lia );
						lre.setStart( LocationImpl.create( x + doaMinor[k], dY1 ) );
						lre.setEnd( LocationImpl.create( x + doaMinor[k], dY2 ) );
						ipr.drawLine( lre );
					}
				}
			}
			else if ( oaxa[i].getOrientation( ) == IConstants.VERTICAL )
			{
				double[] da = sc.getTickCordinates( );
				double dX1 = bo.getLeft( ) + 1, dX2 = bo.getLeft( )
						+ bo.getWidth( )
						- 2;
				if ( pwa.getDimension( ) == IConstants.TWO_5_D )
				{
					for ( int j = 0; j < da.length - 1; j++ )
					{
						y = ( da[j] - pwa.getSeriesThickness( ) );
						for ( int k = 0; k < doaMinor.length; k++ )
						{
							lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
									LineRenderEvent.class );
							lre.setLineAttributes( lia );
							lre.setStart( LocationImpl.create( dX1, y
									- doaMinor[k] ) );
							lre.setEnd( LocationImpl.create( dX1
									- pwa.getSeriesThickness( ), y
									- doaMinor[k]
									+ pwa.getSeriesThickness( ) ) );
							ipr.drawLine( lre );
						}
					}
				}
				for ( int j = 0; j < da.length - 1; j++ )
				{
					y = da[j];
					if ( pwa.getDimension( ) == IConstants.TWO_5_D )
					{
						y -= pwa.getSeriesThickness( );
					}
					for ( int k = 0; k < doaMinor.length; k++ )
					{
						lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
								LineRenderEvent.class );
						lre.setLineAttributes( lia );
						lre.setStart( LocationImpl.create( dX1, y - doaMinor[k] ) );
						lre.setEnd( LocationImpl.create( dX2, y - doaMinor[k] ) );
						ipr.drawLine( lre );
					}
				}
			}
		}

		// RENDER MAJOR GRID LINES NEXT
		for ( int i = 0; i < oaxa.length; i++ )
		{
			lia = oaxa[i].getGrid( ).getLineAttributes( IConstants.MAJOR );
			if ( lia == null || !lia.isSetStyle( ) || !lia.isVisible( ) ) // GRID
			// LINE
			// UNDEFINED
			{
				continue;
			}

			AutoScale sc = oaxa[i].getScale( );
			if ( isDimension3D( ) )
			{
				Line3DRenderEvent lre3d = (Line3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
						Line3DRenderEvent.class );
				lre3d.setLineAttributes( lia );

				switch ( oaxa[i].getAxisType( ) )
				{
					case IConstants.BASE_AXIS :

						double[] xa = scPrimaryBase.getNormalizedTickCoordinates( );
						if ( floorFill )
						{
							for ( int k = 0; k < xa.length; k++ )
							{
								for ( int j = 0; j < ancillaryTickCount - 1; j++ )
								{
									lre3d.setStart3D( Location3DImpl.create( xa[k],
											dYStart,
											dZStart + j * zStep ) );
									lre3d.setEnd3D( Location3DImpl.create( xa[k],
											dYStart,
											dZStart + ( j + 1 ) * zStep ) );
									getDeferredCache( ).addLine( lre3d );
								}
							}
						}

						if ( rightWallFill )
						{
							for ( int k = 0; k < xa.length; k++ )
							{
								for ( int j = 0; j < orthogonalTickCount - 1; j++ )
								{
									lre3d.setStart3D( Location3DImpl.create( xa[k],
											dYStart + j * yStep,
											dZStart ) );
									lre3d.setEnd3D( Location3DImpl.create( xa[k],
											dYStart + ( j + 1 ) * yStep,
											dZStart ) );
									getDeferredCache( ).addLine( lre3d );
								}
							}
						}
						break;
					case IConstants.ORTHOGONAL_AXIS :
						double[] ya = scPrimaryOrthogonal.getNormalizedTickCoordinates( );
						if ( leftWallFill )
						{
							for ( int k = 0; k < ya.length; k++ )
							{
								for ( int j = 0; j < ancillaryTickCount - 1; j++ )
								{
									lre3d.setStart3D( Location3DImpl.create( dXStart,
											ya[k],
											dZStart + j * zStep ) );
									lre3d.setEnd3D( Location3DImpl.create( dXStart,
											ya[k],
											dZStart + ( j + 1 ) * zStep ) );
									getDeferredCache( ).addLine( lre3d );
								}
							}
						}

						if ( rightWallFill )
						{
							for ( int k = 0; k < ya.length; k++ )
							{
								for ( int j = 0; j < baseTickCount - 1; j++ )
								{
									lre3d.setStart3D( Location3DImpl.create( dXStart
											+ j
											* xStep,
											ya[k],
											dZStart ) );
									lre3d.setEnd3D( Location3DImpl.create( dXStart
											+ ( j + 1 )
											* xStep,
											ya[k],
											dZStart ) );
									getDeferredCache( ).addLine( lre3d );
								}
							}
						}
						break;
					case IConstants.ANCILLARY_AXIS :
						double[] za = scAncillaryBase.getNormalizedTickCoordinates( );
						if ( leftWallFill )
						{
							for ( int k = 0; k < za.length; k++ )
							{
								for ( int j = 0; j < orthogonalTickCount - 1; j++ )
								{
									lre3d.setStart3D( Location3DImpl.create( dXStart,
											dYStart + j * yStep,
											za[k] ) );
									lre3d.setEnd3D( Location3DImpl.create( dXStart,
											dYStart + ( j + 1 ) * yStep,
											za[k] ) );
									getDeferredCache( ).addLine( lre3d );
								}
							}
						}

						if ( floorFill )
						{
							for ( int k = 0; k < za.length; k++ )
							{
								for ( int j = 0; j < baseTickCount - 1; j++ )
								{
									lre3d.setStart3D( Location3DImpl.create( dXStart
											+ j
											* xStep,
											dYStart,
											za[k] ) );
									lre3d.setEnd3D( Location3DImpl.create( dXStart
											+ ( j + 1 )
											* xStep,
											dYStart,
											za[k] ) );
									getDeferredCache( ).addLine( lre3d );
								}
							}
						}
						break;
					default :
						break;
				}
			}
			else if ( oaxa[i].getOrientation( ) == IConstants.HORIZONTAL )
			{
				double[] da = sc.getTickCordinates( );
				double dY2 = bo.getTop( ) + 1, dY1 = bo.getTop( )
						+ bo.getHeight( )
						- 2;
				if ( pwa.getDimension( ) == IConstants.TWO_5_D )
				{
					for ( int j = 0; j < da.length; j++ )
					{
						if ( j == 0 && insCA.getBottom( ) < lia.getThickness( ) )
							continue;
						if ( j == da.length - 1
								&& insCA.getTop( ) < lia.getThickness( ) )
							continue;

						x = da[j];
						lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
								LineRenderEvent.class );
						lre.setLineAttributes( lia );
						lre.setStart( LocationImpl.create( x, dY1
								+ pwa.getSeriesThickness( ) ) );
						lre.setEnd( LocationImpl.create( x
								+ pwa.getSeriesThickness( ), dY1 ) );
						ipr.drawLine( lre );
					}
				}
				for ( int j = 0; j < da.length; j++ )
				{
					if ( j == 0 && insCA.getBottom( ) < lia.getThickness( ) )
						continue;
					if ( j == da.length - 1
							&& insCA.getTop( ) < lia.getThickness( ) )
						continue;

					x = da[j];
					if ( pwa.getDimension( ) == IConstants.TWO_5_D )
					{
						x += pwa.getSeriesThickness( );
					}
					lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
							LineRenderEvent.class );
					lre.setLineAttributes( lia );
					lre.setStart( LocationImpl.create( x, dY1 ) );
					lre.setEnd( LocationImpl.create( x, dY2 ) );
					ipr.drawLine( lre );
				}
			}
			else if ( oaxa[i].getOrientation( ) == IConstants.VERTICAL )
			{
				double[] da = sc.getTickCordinates( );
				double dX1 = bo.getLeft( ) + 1, dX2 = bo.getLeft( )
						+ bo.getWidth( )
						- 2;
				if ( pwa.getDimension( ) == IConstants.TWO_5_D )
				{
					for ( int j = 0; j < da.length; j++ )
					{
						if ( j == 0 && insCA.getLeft( ) < lia.getThickness( ) )
							continue;
						if ( j == da.length - 1
								&& insCA.getRight( ) < lia.getThickness( ) )
							continue;

						y = (int) ( da[j] - pwa.getSeriesThickness( ) );
						lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
								LineRenderEvent.class );
						lre.setLineAttributes( lia );
						lre.setStart( LocationImpl.create( dX1, y ) );
						lre.setEnd( LocationImpl.create( dX1
								- pwa.getSeriesThickness( ), y
								+ pwa.getSeriesThickness( ) ) );
						ipr.drawLine( lre );
					}
				}
				for ( int j = 0; j < da.length; j++ )
				{
					if ( j == 0 && insCA.getLeft( ) < lia.getThickness( ) )
						continue;
					if ( j == da.length - 1
							&& insCA.getRight( ) < lia.getThickness( ) )
						continue;

					y = (int) da[j];
					if ( pwa.getDimension( ) == IConstants.TWO_5_D )
					{
						y -= pwa.getSeriesThickness( );
					}
					lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
							LineRenderEvent.class );
					lre.setLineAttributes( lia );
					lre.setStart( LocationImpl.create( dX1, y ) );
					lre.setEnd( LocationImpl.create( dX2, y ) );
					ipr.drawLine( lre );
				}
			}
		}

		if ( p.getClientArea( ).getOutline( ).isVisible( ) )
		{
			rre.setBounds( bo );
			ipr.drawRectangle( rre );
		}
	}

	/**
	 * The axes correspond to the lines/planes being rendered within the plot
	 * block. This is rendered with Z-order=2
	 */
	private final void renderAxesStructure( IPrimitiveRenderer ipr, Plot p )
			throws ChartException
	{
		final PlotWithAxes pwa = (PlotWithAxes) getComputations( );
		final AllAxes aax = pwa.getAxes( );

		if ( pwa.getDimension( ) == IConstants.THREE_D )
		{
			renderEachAxis( ipr, p, aax.getPrimaryBase( ), IConstants.BASE_AXIS );
			renderEachAxis( ipr,
					p,
					aax.getAncillaryBase( ),
					IConstants.ANCILLARY_AXIS );
			renderEachAxis( ipr,
					p,
					aax.getPrimaryOrthogonal( ),
					IConstants.ORTHOGONAL_AXIS );
		}
		else
		{
			final int iCount = aax.getOverlayCount( ) + 2;
			final OneAxis[] oaxa = new OneAxis[iCount];
			oaxa[0] = aax.getPrimaryBase( );
			oaxa[1] = aax.getPrimaryOrthogonal( );
			for ( int i = 0; i < iCount - 2; i++ )
			{
				oaxa[i + 2] = aax.getOverlay( i );
			}

			// RENDER THE AXIS LINES FOR EACH AXIS IN THE PLOT
			for ( int i = 0; i < iCount; i++ )
			{
				renderEachAxis( ipr, p, oaxa[i], IConstants.AXIS );
			}
		}

	}

	/**
	 * The axes correspond to the lines/planes being rendered within the plot
	 * block. This is rendered with Z-order=2
	 */
	private final void renderAxesLabels( IPrimitiveRenderer ipr, Plot p,
			OneAxis[] oaxa ) throws ChartException
	{
		// RENDER THE AXIS LINES FOR EACH AXIS IN THE PLOT
		for ( int i = 0; i < oaxa.length; i++ )
		{
			renderEachAxis( ipr, p, oaxa[i], IConstants.LABELS );
		}
	}

	/**
	 * This method renders the bar graphic elements superimposed over the plot
	 * background and any previously rendered series' graphic elements.
	 */
	public final void renderPlot( IPrimitiveRenderer ipr, Plot p )
			throws ChartException
	{
		if ( !p.isVisible( ) ) // CHECK VISIBILITY
		{
			return;
		}

		final boolean bFirstInSequence = ( iSeriesIndex == 0 );
		final boolean bLastInSequence = ( iSeriesIndex == iSeriesCount - 1 );

		final Bounds boPlot = getPlotBounds( );

		if ( bFirstInSequence )
		{
			renderBackground( ipr, p );
			renderAxesStructure( ipr, p );

			// try
			// {
			// if ( isDimension3D( ) )
			// {
			// getDeferredCache( ).process3DEvent( get3DEngine( ),
			// boPlot.getLeft( ),
			// boPlot.getTop( ) );
			// }
			// getDeferredCache( ).flush( ); // FLUSH DEFERRED CACHE
			// }
			// catch ( ChartException ex ) // NOTE: RENDERING EXCEPTION ALREADY
			// // BEING THROWN
			// {
			// throw new ChartException( ChartEnginePlugin.ID,
			// ChartException.RENDERING,
			// ex );
			// }
		}

		ISeriesRenderingHints srh = null;
		try
		{
			srh = ( (PlotWithAxes) getComputations( ) ).getSeriesRenderingHints( getSeriesDefinition( ),
					getSeries( ) );
		}
		catch ( Exception ex )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.RENDERING,
					ex );
		}

		ScriptHandler.callFunction( getRunTimeContext( ).getScriptHandler( ),
				ScriptHandler.BEFORE_DRAW_SERIES,
				getSeries( ),
				this );
		getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_SERIES,
				getSeries( ) );
		renderSeries( ipr, p, srh ); // CALLS THE APPROPRIATE SUBCLASS FOR
		// GRAPHIC ELEMENT RENDERING
		ScriptHandler.callFunction( getRunTimeContext( ).getScriptHandler( ),
				ScriptHandler.AFTER_DRAW_SERIES,
				getSeries( ),
				this );
		getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_SERIES,
				getSeries( ) );

		if ( bLastInSequence )
		{
			try
			{
				if ( isDimension3D( ) )
				{
					getDeferredCache( ).process3DEvent( get3DEngine( ),
							boPlot.getLeft( ),
							boPlot.getTop( ) );
				}
				getDeferredCache( ).flush( ); // FLUSH DEFERRED CACHE
			}
			catch ( ChartException ex ) // NOTE: RENDERING EXCEPTION ALREADY
			// BEING THROWN
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.RENDERING,
						ex );
			}

			// SETUP AXIS ARRAY
			final PlotWithAxes pwa = (PlotWithAxes) getComputations( );
			final AllAxes aax = pwa.getAxes( );
			final OneAxis[] oaxa = new OneAxis[2
					+ aax.getOverlayCount( )
					+ ( aax.getAncillaryBase( ) != null ? 1 : 0 )];
			oaxa[0] = aax.getPrimaryBase( );
			oaxa[1] = aax.getPrimaryOrthogonal( );
			for ( int i = 0; i < aax.getOverlayCount( ); i++ )
			{
				oaxa[2 + i] = aax.getOverlay( i );
			}
			if ( aax.getAncillaryBase( ) != null )
			{
				oaxa[2 + aax.getOverlayCount( )] = aax.getAncillaryBase( );
			}
			Bounds bo = pwa.getPlotBounds( );

			// RENDER MARKER LINES
			renderMarkerLines( oaxa, bo );

			// RENDER AXIS LABELS LAST
			renderAxesLabels( ipr, p, oaxa );

			try
			{
				if ( isDimension3D( ) )
				{
					getDeferredCache( ).process3DEvent( get3DEngine( ),
							boPlot.getLeft( ),
							boPlot.getTop( ) );
				}
				getDeferredCache( ).flush( ); // FLUSH DEFERRED CACHE
			}
			catch ( ChartException ex ) // NOTE: RENDERING EXCEPTION ALREADY
			// BEING THROWN
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.RENDERING,
						ex );
			}

		}
	}

	/**
	 * Renders all marker lines (and labels at requested positions) associated
	 * with every axis in the plot Note that marker lines are drawn immediately
	 * (not rendered as deferred) at the appropriate Z-order
	 * 
	 * @param oaxa
	 * @param boPlotClientArea
	 * 
	 * @throws RenderingException
	 */
	private final void renderMarkerLines( OneAxis[] oaxa,
			Bounds boPlotClientArea ) throws ChartException
	{
		Axis ax;
		EList el;
		int iLineCount, iAxisCount = oaxa.length;
		MarkerLine ml;
		LineRenderEvent lre;
		DataElement deValue;
		AutoScale asc;
		double dCoordinate = 0;
		int iOrientation;

		final IDeviceRenderer idr = getDevice( );
		final ScriptHandler sh = getRunTimeContext( ).getScriptHandler( );
		final Location loStart = LocationImpl.create( 0, 0 );
		final Location loEnd = LocationImpl.create( 0, 0 );

		Anchor anc;
		Orientation or;
		TextRenderEvent tre = null;
		Label la = null;
		double dOriginalAngle = 0;
		final boolean bTransposed = ( (ChartWithAxes) getModel( ) ).isTransposed( );
		final PlotWithAxes pwa = (PlotWithAxes) getComputations( );
		final Bounds boText = BoundsImpl.create( 0, 0, 0, 0 );

		for ( int i = 0; i < iAxisCount; i++ )
		{
			ax = oaxa[i].getModelAxis( );
			iOrientation = ax.getOrientation( ).getValue( );
			if ( bTransposed ) // TOGGLE ORIENTATION
			{
				iOrientation = ( iOrientation == Orientation.HORIZONTAL ) ? Orientation.VERTICAL
						: Orientation.HORIZONTAL;
			}
			asc = oaxa[i].getScale( );
			el = ax.getMarkerLines( );
			iLineCount = el.size( );

			for ( int j = 0; j < iLineCount; j++ )
			{
				ml = (MarkerLine) el.get( j );
				ScriptHandler.callFunction( sh,
						ScriptHandler.BEFORE_DRAW_MARKER_LINE,
						ax,
						ml );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_MARKER_LINE,
						ml );

				deValue = (DataElement) ml.getValue( );
				if ( deValue == null )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.RENDERING,
							"exception.marker.line.null.value", //$NON-NLS-1$
							new Object[]{
								ml
							},
							ResourceBundle.getBundle( Messages.ENGINE,
									getRunTimeContext( ).getLocale( ) ) );
				}

				// UPDATE THE LABEL CONTENT ASSOCIATED WITH THE MARKER LINE
				la = ml.getLabel( );
				try
				{
					la.getCaption( ).setValue( ValueFormatter.format( deValue,
							ml.getFormatSpecifier( ),
							oaxa[i].getRunTimeContext( ).getLocale( ),
							null ) );
				}
				catch ( ChartException dfex )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.RENDERING,
							dfex );
				}

				if ( isDimension3D( ) )
				{
					// TODO render 3D marker line
					return;
				}

				// COMPUTE THE LOCATION
				try
				{
					dCoordinate = PlotWith2DAxes.getLocation( asc, deValue );
				}
				catch ( Exception ex )
				{
					logger.log( ILogger.WARNING,
							Messages.getString( "exception.cannot.locate.value.marker.line", //$NON-NLS-1$
									new Object[]{
											deValue, ml
									},
									getRunTimeContext( ).getLocale( ) ) );
					continue; // TRY NEXT MARKER RANGE
				}

				lre = (LineRenderEvent) ( (EventObjectCache) idr ).getEventObject( StructureSource.createMarkerLine( ml ),
						LineRenderEvent.class );
				if ( iOrientation == Orientation.HORIZONTAL )
				{
					// RESTRICT RIGHT EDGE
					if ( dCoordinate > boPlotClientArea.getLeft( )
							+ boPlotClientArea.getWidth( ) )
					{
						dCoordinate = boPlotClientArea.getLeft( )
								+ boPlotClientArea.getWidth( );
					}

					// RESTRICT LEFT EDGE
					if ( dCoordinate < boPlotClientArea.getLeft( ) )
					{
						dCoordinate = boPlotClientArea.getLeft( );
					}

					// SETUP THE TWO POINTS
					loStart.set( dCoordinate, boPlotClientArea.getTop( ) );
					loEnd.set( dCoordinate, boPlotClientArea.getTop( )
							+ boPlotClientArea.getHeight( ) );
				}
				else
				{
					// RESTRICT TOP EDGE
					if ( dCoordinate < boPlotClientArea.getTop( ) )
					{
						dCoordinate = boPlotClientArea.getTop( );
					}

					// RESTRICT BOTTOM EDGE
					if ( dCoordinate > boPlotClientArea.getTop( )
							+ boPlotClientArea.getHeight( ) )
					{
						dCoordinate = boPlotClientArea.getTop( )
								+ boPlotClientArea.getHeight( );
					}

					// SETUP THE TWO POINTS
					loStart.set( boPlotClientArea.getLeft( ), dCoordinate );
					loEnd.set( boPlotClientArea.getLeft( )
							+ boPlotClientArea.getWidth( ), dCoordinate );
				}

				// ADJUST FOR 2D PLOTS AS NEEDED
				if ( pwa.getDimension( ) == IConstants.TWO_5_D )
				{
					/*
					 * if (iOrientation == Orientation.HORIZONTAL) {
					 * loStart.translate(0, pw2da.getSeriesThickness());
					 * loEnd.translate(0, pw2da.getSeriesThickness()); } else {
					 * loStart.translate(-pw2da.getSeriesThickness(), 0);
					 * loEnd.translate(-pw2da.getSeriesThickness(), 0); }
					 */
				}

				// DRAW THE MARKER LINE
				lre.setStart( loStart );
				lre.setEnd( loEnd );
				lre.setLineAttributes( ml.getLineAttributes( ) );
				idr.drawLine( lre );

				// DRAW THE MARKER LINE LABEL AT THE APPROPRIATE LOCATION
				if ( la.isVisible( ) )
				{
					// DETERMINE THE LABEL ANCHOR (TRANSPOSE IF NEEDED)
					anc = ml.getLabelAnchor( );
					if ( bTransposed )
					{
						or = ax.getOrientation( ) == Orientation.HORIZONTAL_LITERAL ? Orientation.VERTICAL_LITERAL
								: Orientation.HORIZONTAL_LITERAL;
						la = ml.getLabel( );
						dOriginalAngle = la.getCaption( )
								.getFont( )
								.getRotation( );
						try
						{
							la.getCaption( )
									.getFont( )
									.setRotation( pwa.getTransposedAngle( dOriginalAngle ) );
							anc = pwa.transposedAnchor( or, anc );
						}
						catch ( IllegalArgumentException uiex )
						{
							throw new ChartException( ChartEnginePlugin.ID,
									ChartException.RENDERING,
									uiex );
						}
					}

					BoundingBox bb = null;
					try
					{
						bb = Methods.computeBox( idr.getDisplayServer( ),
								IConstants.LEFT,
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
					boText.set( 0, 0, bb.getWidth( ), bb.getHeight( ) );

					if ( iOrientation == Orientation.VERTICAL )
					{
						if ( anc != null )
						{
							switch ( anc.getValue( ) )
							{
								case Anchor.NORTH :
								case Anchor.NORTH_EAST :
								case Anchor.NORTH_WEST :
									boText.setTop( loStart.getY( )
											- boText.getHeight( ) );
									break;

								case Anchor.SOUTH :
								case Anchor.SOUTH_EAST :
								case Anchor.SOUTH_WEST :
									boText.setTop( loStart.getY( ) );
									break;

								default :
									boText.setTop( loStart.getY( )
											+ ( loEnd.getY( ) - loStart.getY( ) - boText.getHeight( ) )
											/ 2 );
									break;
							}

							switch ( anc.getValue( ) )
							{
								case Anchor.NORTH_EAST :
								case Anchor.SOUTH_EAST :
								case Anchor.EAST :
									boText.setLeft( loEnd.getX( )
											- boText.getWidth( ) );
									break;

								case Anchor.NORTH_WEST :
								case Anchor.SOUTH_WEST :
								case Anchor.WEST :
									boText.setLeft( loStart.getX( ) );
									break;

								default :
									boText.setLeft( loStart.getX( )
											+ ( loEnd.getX( ) - loStart.getX( ) - boText.getWidth( ) )
											/ 2 );
									break;
							}
						}
						else
						// CENTER ANCHORED
						{
							boText.setLeft( loStart.getX( )
									+ ( loEnd.getX( ) - loStart.getX( ) - boText.getWidth( ) )
									/ 2 );
							boText.setTop( loStart.getY( )
									+ ( loEnd.getY( ) - loStart.getY( ) - boText.getHeight( ) )
									/ 2 );
						}
					}
					else
					{
						if ( anc != null )
						{
							switch ( anc.getValue( ) )
							{
								case Anchor.NORTH :
								case Anchor.NORTH_EAST :
								case Anchor.NORTH_WEST :
									boText.setTop( loStart.getY( ) );
									break;

								case Anchor.SOUTH :
								case Anchor.SOUTH_EAST :
								case Anchor.SOUTH_WEST :
									boText.setTop( loEnd.getY( )
											- boText.getHeight( ) );
									break;

								default :
									boText.setTop( loStart.getY( )
											+ ( loEnd.getY( ) - loStart.getY( ) - boText.getHeight( ) )
											/ 2 );
									break;
							}

							switch ( anc.getValue( ) )
							{
								case Anchor.NORTH_EAST :
								case Anchor.SOUTH_EAST :
								case Anchor.EAST :
									boText.setLeft( loStart.getX( ) );
									break;

								case Anchor.NORTH_WEST :
								case Anchor.SOUTH_WEST :
								case Anchor.WEST :
									boText.setLeft( loEnd.getX( )
											- boText.getWidth( ) );
									break;

								default :
									boText.setLeft( loStart.getX( )
											+ ( loEnd.getX( ) - loStart.getX( ) - boText.getWidth( ) )
											/ 2 );
									break;
							}
						}
						else
						// CENTER ANCHORED
						{
							boText.setLeft( loStart.getX( )
									+ ( loEnd.getX( ) - loStart.getX( ) - boText.getWidth( ) )
									/ 2 );
							boText.setTop( loStart.getY( )
									+ ( loEnd.getY( ) - loStart.getY( ) - boText.getHeight( ) )
									/ 2 );
						}
					}

					// NOW THAT WE COMPUTED THE BOUNDS, RENDER THE ACTUAL TEXT
					tre = (TextRenderEvent) ( (EventObjectCache) idr ).getEventObject( StructureSource.createMarkerLine( ml ),
							TextRenderEvent.class );
					tre.setBlockBounds( boText );
					tre.setBlockAlignment( null );
					tre.setLabel( la );
					tre.setAction( TextRenderEvent.RENDER_TEXT_IN_BLOCK );
					idr.drawText( tre );

					if ( bTransposed ) // RESTORE FONT ANGLE IF TRANSPOSED
					{
						la.getCaption( )
								.getFont( )
								.setRotation( dOriginalAngle );
					}
				}
				ScriptHandler.callFunction( sh,
						ScriptHandler.AFTER_DRAW_MARKER_LINE,
						ax,
						ml );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_MARKER_LINE,
						ml );
			}
		}
	}

	/**
	 * 
	 * 
	 * @param ipr
	 * @param pl
	 * @param ax
	 * @param iWhatToDraw
	 * 
	 * @throws RenderingException
	 */
	public final void renderEachAxis( IPrimitiveRenderer ipr, Plot pl,
			OneAxis ax, int iWhatToDraw ) throws ChartException
	{
		final RunTimeContext rtc = getRunTimeContext( );
		final Axis axModel = ax.getModelAxis( );
		final PlotWithAxes pwa = (PlotWithAxes) getComputations( );
		final Insets insCA = pwa.getAxes( ).getInsets( );
		final ScriptHandler sh = getRunTimeContext( ).getScriptHandler( );
		double dLocation = ax.getAxisCoordinate( );
		double dAngleInDegrees = ax.getLabel( )
				.getCaption( )
				.getFont( )
				.getRotation( );
		AutoScale sc = ax.getScale( );
		IntersectionValue iv = ax.getIntersectionValue( );
		int iMajorTickStyle = ax.getGrid( ).getTickStyle( IConstants.MAJOR );
		int iMinorTickStyle = ax.getGrid( ).getTickStyle( IConstants.MINOR );
		int iLabelLocation = ax.getLabelPosition( );
		int iOrientation = ax.getOrientation( );
		IDisplayServer xs = this.getDevice( ).getDisplayServer( );

		double[] daEndPoints = sc.getEndPoints( );
		double[] da = sc.getTickCordinates( );
		double[] daMinor = sc.getMinorCoordinates( ax.getGrid( )
				.getMinorCountPerMajor( ) );
		String sText = null;

		int iDimension = pwa.getDimension( );
		double dSeriesThickness = pwa.getSeriesThickness( );
		final NumberDataElement nde = NumberDataElementImpl.create( 0 );
		final FormatSpecifier fs = ax.getModelAxis( ).getFormatSpecifier( );

		DecimalFormat df = null;

		LineAttributes lia = ax.getLineAttributes( );
		LineAttributes liaMajorTick = ax.getGrid( )
				.getTickAttributes( IConstants.MAJOR );
		LineAttributes liaMinorTick = ax.getGrid( )
				.getTickAttributes( IConstants.MINOR );

		if ( !lia.isSetVisible( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.RENDERING,
					"exception.unset.axis.visibility", //$NON-NLS-1$
					ResourceBundle.getBundle( Messages.ENGINE,
							getRunTimeContext( ).getLocale( ) ) );
		}
		Label la = ax.getLabel( );
		final boolean bRenderAxisLabels = ( ( iWhatToDraw & IConstants.LABELS ) == IConstants.LABELS && la.isVisible( ) );
		Location lo = LocationImpl.create( 0, 0 );

		final TransformationEvent trae = (TransformationEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
				TransformationEvent.class );
		final TextRenderEvent tre = (TextRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
				TextRenderEvent.class );
		tre.setLabel( la );
		tre.setTextPosition( iLabelLocation );
		tre.setLocation( lo );

		final LineRenderEvent lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
				LineRenderEvent.class );
		lre.setLineAttributes( lia );
		lre.setStart( LocationImpl.create( 0, 0 ) );
		lre.setEnd( LocationImpl.create( 0, 0 ) );

		// Prepare 3D rendering variables.
		final boolean bRendering3D = iDimension == IConstants.THREE_D;
		final boolean bRenderOrthogonal3DAxis = ( iWhatToDraw & IConstants.ORTHOGONAL_AXIS ) == IConstants.ORTHOGONAL_AXIS
				&& bRendering3D;
		final boolean bRenderBase3DAxis = ( iWhatToDraw & IConstants.BASE_AXIS ) == IConstants.BASE_AXIS
				&& bRendering3D;
		final boolean bRenderAncillary3DAxis = ( iWhatToDraw & IConstants.ANCILLARY_AXIS ) == IConstants.ANCILLARY_AXIS
				&& bRendering3D;

		final DeferredCache dc = getDeferredCache( );
		final int axisType = ax.getAxisType( );

		double[] daEndPoints3D = null;
		double[] da3D = null;
		Location3D lo3d = null;
		Text3DRenderEvent t3dre = null;
		Line3DRenderEvent l3dre = null;

		double dXStart = 0;
		double dZStart = 0;
		double dXEnd = 0;
		double dZEnd = 0;

		if ( iDimension == IConstants.THREE_D )
		{
			AllAxes aax = pwa.getAxes( );
			dXEnd = aax.getPrimaryBase( ).getScale( ).getNormalizedEnd( );
			dZEnd = aax.getAncillaryBase( ).getScale( ).getNormalizedEnd( );
			dXStart = aax.getPrimaryBase( ).getScale( ).getNormalizedStart( );
			dZStart = aax.getAncillaryBase( ).getScale( ).getNormalizedStart( );

			daEndPoints3D = sc.getNormalizedEndPoints( );
			da3D = sc.getNormalizedTickCoordinates( );

			lo3d = Location3DImpl.create( 0, 0, 0 );

			t3dre = (Text3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
					Text3DRenderEvent.class );
			t3dre.setLabel( la );
			t3dre.setAction( Text3DRenderEvent.RENDER_TEXT_AT_LOCATION );
			t3dre.setTextPosition( iLabelLocation );
			t3dre.setLocation3D( lo3d );

			l3dre = (Line3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
					Line3DRenderEvent.class );
			l3dre.setLineAttributes( lia );
			l3dre.setStart3D( Location3DImpl.create( 0, 0, 0 ) );
			l3dre.setEnd3D( Location3DImpl.create( 0, 0, 0 ) );
		}

		if ( iOrientation == IConstants.VERTICAL )
		{
			int y;
			int y3d = 0;
			double dX = dLocation;
			double dZ = 0;

			if ( bRendering3D )
			{
				Location3D l3d = ax.getAxisCoordinate3D( );
				dX = l3d.getX( );
				dZ = l3d.getZ( );
			}

			if ( iv != null
					&& iv.getType( ) == IntersectionValue.MAX
					&& iDimension == IConstants.TWO_5_D )
			{
				trae.setTransform( TransformationEvent.TRANSLATE );
				trae.setTranslation( dSeriesThickness, -dSeriesThickness );
				ipr.applyTransformation( trae );
			}

			double dXTick1 = ( ( iMajorTickStyle & IConstants.TICK_LEFT ) == IConstants.TICK_LEFT ) ? ( dX - IConstants.TICK_SIZE )
					: dX;
			double dXTick2 = ( ( iMajorTickStyle & IConstants.TICK_RIGHT ) == IConstants.TICK_RIGHT ) ? dX
					+ IConstants.TICK_SIZE
					: dX;

			if ( ( iWhatToDraw & IConstants.AXIS ) == IConstants.AXIS
					&& lia.isVisible( ) )
			{
				if ( bRenderOrthogonal3DAxis )
				{
					final double dStart = daEndPoints3D[0];
					final double dEnd = daEndPoints3D[1];
					l3dre.setLineAttributes( lia );

					// center
					l3dre.setStart3D( dX, dStart, dZ );
					l3dre.setEnd3D( dX, dEnd, dZ );
					dc.addLine( l3dre );

					// left
					l3dre.setStart3D( dXStart, dStart, dZEnd );
					l3dre.setEnd3D( dXStart, dEnd, dZEnd );
					dc.addLine( l3dre );

					// right
					l3dre.setStart3D( dXEnd, dStart, dZStart );
					l3dre.setEnd3D( dXEnd, dEnd, dZStart );
					dc.addLine( l3dre );
				}
				else
				{
					final double dStart = daEndPoints[0] + insCA.getBottom( ), dEnd = daEndPoints[1]
							- insCA.getTop( );
					if ( iv != null
							&& iv.getType( ) == IntersectionValue.VALUE
							&& iDimension == IConstants.TWO_5_D )
					{
						final Location[] loa = new Location[4];
						loa[0] = LocationImpl.create( dX, dStart );
						loa[1] = LocationImpl.create( dX + dSeriesThickness,
								dStart - dSeriesThickness );
						loa[2] = LocationImpl.create( dX + dSeriesThickness,
								dEnd - dSeriesThickness );
						loa[3] = LocationImpl.create( dX, dEnd );

						final PolygonRenderEvent pre = (PolygonRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
								PolygonRenderEvent.class );
						pre.setPoints( loa );
						pre.setBackground( ColorDefinitionImpl.create( 255,
								255,
								255,
								127 ) );
						pre.setOutline( lia );
						ipr.fillPolygon( pre );
					}
					lre.setLineAttributes( lia );
					lre.getStart( ).set( dX, dStart );
					lre.getEnd( ).set( dX, dEnd );
					ipr.drawLine( lre );
				}
			}

			if ( ( sc.getType( ) & IConstants.TEXT ) == IConstants.TEXT
					|| sc.isCategoryScale( ) )
			{
				double dAngleInRadians = ( -dAngleInDegrees * Math.PI ) / 180;
				double dSineTheta = Math.abs( Math.sin( dAngleInRadians ) );
				double dCosTheta = Math.abs( Math.cos( dAngleInRadians ) );
				double dOffset = 0, dW, dH, dHCosTheta;
				double dUnitSize = sc.getUnitSize( );
				DataSetIterator dsi = sc.getData( );
				final int iDateTimeUnit = ( sc.getType( ) == IConstants.DATE_TIME ) ? CDateTime.computeUnit( dsi )
						: IConstants.UNDEFINED;
				final ITextMetrics itmText = xs.getTextMetrics( la );

				if ( dAngleInDegrees == 90 || dAngleInDegrees == 0 )
				{
					dOffset = -dUnitSize / 2;
				}

				double x = ( iLabelLocation == IConstants.LEFT ) ? dXTick1 - 1
						: dXTick2 + 1;
				dsi.reset( );
				for ( int i = 0; i < da.length - 1; i++ )
				{
					if ( bRenderAxisLabels ) // PERFORM COMPUTATIONS ONLY IF
					// AXIS LABEL IS VISIBLE
					{
						la.getCaption( )
								.setValue( sc.formatCategoryValue( sc.getType( ),
										dsi.next( ),
										iDateTimeUnit ) );
						ScriptHandler.callFunction( sh,
								ScriptHandler.BEFORE_DRAW_AXIS_LABEL,
								axModel,
								la );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL,
								la );
						itmText.reuse( la ); // RECYCLED
						dH = itmText.getFullHeight( );
						dW = itmText.getFullWidth( );
						dHCosTheta = dH * dCosTheta;
						if ( dAngleInDegrees > 0 && dAngleInDegrees < 90 )
						{
							if ( iLabelLocation == IConstants.LEFT )
							{
								dOffset = ( dHCosTheta + dW * dSineTheta - dUnitSize )
										/ 2
										- dW
										* dSineTheta;
							}
							else if ( iLabelLocation == IConstants.RIGHT )
							{
								dOffset = ( dHCosTheta + dW * dSineTheta - dUnitSize )
										/ 2
										- dHCosTheta;
							}
						}
						else if ( dAngleInDegrees < 0 && dAngleInDegrees > -90 )
						{
							if ( iLabelLocation == IConstants.LEFT )
							{
								dOffset = ( dHCosTheta + dW * dSineTheta - dUnitSize )
										/ 2
										- dHCosTheta;
							}
							else if ( iLabelLocation == IConstants.RIGHT )
							{
								dOffset = ( dHCosTheta + dW * dSineTheta - dUnitSize )
										/ 2
										- dW
										* dSineTheta;
							}
						}
						else if ( dAngleInDegrees == 0
								|| dAngleInDegrees == 90
								|| dAngleInDegrees == -90 )
						{
							dOffset = -dUnitSize / 2;
						}
					}

					y = (int) da[i];
					if ( bRendering3D )
					{
						y3d = (int) da3D[i];
					}
					if ( ( iWhatToDraw & IConstants.AXIS ) == IConstants.AXIS )
					{
						double dXMinorTick1 = ( ( iMinorTickStyle & IConstants.TICK_LEFT ) == IConstants.TICK_LEFT ) ? ( dX - IConstants.TICK_SIZE )
								: dX;
						double dXMinorTick2 = ( ( iMinorTickStyle & IConstants.TICK_RIGHT ) == IConstants.TICK_RIGHT ) ? dX
								+ IConstants.TICK_SIZE
								: dX;
						if ( dXMinorTick1 != dXMinorTick2 )
						{
							// RENDER THE MINOR TICKS FIRST (For ALL but the
							// last Major tick)
							if ( i != da.length - 1 )
							{
								if ( bRenderOrthogonal3DAxis )
								{
									Line3DRenderEvent l3dreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										l3dreMinor = (Line3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												Line3DRenderEvent.class );
										l3dreMinor.setLineAttributes( liaMinorTick );
										l3dreMinor.setStart3D( Location3DImpl.create( dXMinorTick1,
												y3d + daMinor[k],
												dZ ) );
										l3dreMinor.setEnd3D( Location3DImpl.create( dXMinorTick2,
												y3d + daMinor[k],
												dZ ) );
										dc.addLine( l3dreMinor );
									}
								}
								else
								{
									LineRenderEvent lreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										lreMinor = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												LineRenderEvent.class );
										lreMinor.setLineAttributes( liaMinorTick );
										lreMinor.setStart( LocationImpl.create( dXMinorTick1,
												y - daMinor[k] ) );
										lreMinor.setEnd( LocationImpl.create( dXMinorTick2,
												y - daMinor[k] ) );
										ipr.drawLine( lreMinor );
									}
								}
							}
						}

						if ( dXTick1 != dXTick2 )
						{
							if ( bRenderOrthogonal3DAxis )
							{
								l3dre.setLineAttributes( liaMajorTick );
								l3dre.setStart3D( dXTick1, y3d, dZ );
								l3dre.setEnd3D( dXTick2, y3d, dZ );
								dc.addLine( l3dre );
							}
							else
							{
								lre.setLineAttributes( liaMajorTick );
								lre.getStart( ).set( dXTick1, y );
								lre.getEnd( ).set( dXTick2, y );
								ipr.drawLine( lre );
							}

							if ( iv != null
									&& iDimension == IConstants.TWO_5_D
									&& iv.getType( ) == IntersectionValue.VALUE )
							{
								lre.setStart( LocationImpl.create( dX, y ) );
								lre.setEnd( LocationImpl.create( dX
										+ dSeriesThickness, y
										- dSeriesThickness ) );
								ipr.drawLine( lre );
							}
						}
					}

					if ( bRenderAxisLabels ) // RENDER AXIS LABELS ONLY IF
					// REQUESTED
					{
						if ( bRendering3D )
						{
							// Left wall
							lo3d.set( x, y3d + dOffset, dZEnd );
							t3dre.setLocation3D( lo3d );
							t3dre.setTextPosition( TextRenderEvent.LEFT );
							t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							dc.addLabel( t3dre );

							// Right wall
							lo3d.set( dXEnd, y3d + dOffset, dZ );
							t3dre.setLocation3D( lo3d );
							t3dre.setTextPosition( TextRenderEvent.RIGHT );
							t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							dc.addLabel( t3dre );
						}
						else
						{
							lo.set( x, y + dOffset );
							tre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							ipr.drawText( tre );
						}

						ScriptHandler.callFunction( sh,
								ScriptHandler.AFTER_DRAW_AXIS_LABEL,
								axModel,
								la );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_AXIS_LABEL,
								la );
					}
				}
				y = (int) da[da.length - 1];
				if ( bRendering3D )
				{
					y3d = (int) da3D[da3D.length - 1];
				}
				if ( dXTick1 != dXTick2 )
				{
					if ( bRenderOrthogonal3DAxis )
					{
						l3dre.setLineAttributes( liaMajorTick );
						l3dre.setStart3D( dXTick1, y3d, dZ );
						l3dre.setEnd3D( dXTick2, y3d, dZ );
						dc.addLine( l3dre );
					}
					else
					{
						lre.setLineAttributes( liaMajorTick );
						lre.getStart( ).set( dXTick1, y );
						lre.getEnd( ).set( dXTick2, y );
						ipr.drawLine( lre );
					}

					if ( iv != null
							&& iDimension == IConstants.TWO_5_D
							&& iv.getType( ) == IntersectionValue.VALUE )
					{
						lre.setStart( LocationImpl.create( dX, y ) );
						lre.setEnd( LocationImpl.create( dX + dSeriesThickness,
								y - dSeriesThickness ) );
						ipr.drawLine( lre );
					}
				}
				itmText.dispose( );// DISPOSED
			}
			else if ( ( sc.getType( ) & IConstants.LOGARITHMIC ) == IConstants.LOGARITHMIC )
			{
				double dAxisValue = Methods.asDouble( sc.getMinimum( ) )
						.doubleValue( );
				final double dAxisStep = Methods.asDouble( sc.getStep( ) )
						.doubleValue( );

				dAxisValue = Methods.asDouble( sc.getMinimum( ) ).doubleValue( ); // RESET
				double x = ( iLabelLocation == IConstants.LEFT ) ? dXTick1 - 1
						: dXTick2 + 1;
				for ( int i = 0; i < da.length; i++ )
				{
					if ( bRenderAxisLabels ) // PERFORM COMPUTATIONS ONLY IF
					// AXIS LABEL IS VISIBLE
					{
						if ( fs == null )
						{
							df = new DecimalFormat( sc.getNumericPattern( dAxisValue ) );
						}
						nde.setValue( dAxisValue );
						try
						{
							sText = ValueFormatter.format( nde,
									fs,
									ax.getRunTimeContext( ).getLocale( ),
									df );
						}
						catch ( ChartException dfex )
						{
							logger.log( dfex );
							sText = IConstants.NULL_STRING;
						}
					}

					y = (int) da[i];
					if ( bRendering3D )
					{
						y3d = (int) da3D[i];
					}
					if ( ( iWhatToDraw & IConstants.AXIS ) == IConstants.AXIS )
					{
						double dXMinorTick1 = ( ( iMinorTickStyle & IConstants.TICK_LEFT ) == IConstants.TICK_LEFT ) ? ( dX - IConstants.TICK_SIZE )
								: dX;
						double dXMinorTick2 = ( ( iMinorTickStyle & IConstants.TICK_RIGHT ) == IConstants.TICK_RIGHT ) ? dX
								+ IConstants.TICK_SIZE
								: dX;
						if ( dXMinorTick1 != dXMinorTick2 )
						{
							// RENDER THE MINOR TICKS FIRST (For ALL but the
							// last Major tick)
							if ( i != da.length - 1 )
							{
								if ( bRenderOrthogonal3DAxis )
								{
									Line3DRenderEvent l3dreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										l3dreMinor = (Line3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												Line3DRenderEvent.class );
										l3dreMinor.setLineAttributes( liaMinorTick );
										l3dreMinor.setStart3D( Location3DImpl.create( dXMinorTick1,
												y3d + daMinor[k],
												dZ ) );
										l3dreMinor.setEnd3D( Location3DImpl.create( dXMinorTick2,
												y3d + daMinor[k],
												dZ ) );
										dc.addLine( l3dreMinor );
									}
								}
								else
								{
									LineRenderEvent lreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										lreMinor = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												LineRenderEvent.class );
										lreMinor.setLineAttributes( liaMinorTick );
										lreMinor.setStart( LocationImpl.create( dXMinorTick1,
												y - daMinor[k] ) );
										lreMinor.setEnd( LocationImpl.create( dXMinorTick2,
												y - daMinor[k] ) );
										ipr.drawLine( lreMinor );
									}
								}
							}
						}

						if ( dXTick1 != dXTick2 )
						{
							if ( bRenderOrthogonal3DAxis )
							{
								l3dre.setLineAttributes( liaMajorTick );
								l3dre.setStart3D( dXTick1, y3d, dZ );
								l3dre.setEnd3D( dXTick2, y3d, dZ );
								dc.addLine( l3dre );
							}
							else
							{
								lre.setLineAttributes( liaMajorTick );
								lre.getStart( ).set( dXTick1, y );
								lre.getEnd( ).set( dXTick2, y );
								ipr.drawLine( lre );
							}

							if ( iv != null
									&& iDimension == IConstants.TWO_5_D
									&& iv.getType( ) == IntersectionValue.VALUE )
							{
								lre.setLineAttributes( lia );
								lre.setStart( LocationImpl.create( dX, y ) );
								lre.setEnd( LocationImpl.create( dX
										+ dSeriesThickness, y
										- dSeriesThickness ) );
								ipr.drawLine( lre );
							}
						}
					}

					if ( bRenderAxisLabels ) // RENDER LABELS ONLY IF
					// REQUESTED
					{
						ScriptHandler.callFunction( sh,
								ScriptHandler.BEFORE_DRAW_AXIS_LABEL,
								axModel,
								la );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL,
								la );
						if ( bRendering3D )
						{
							// Left wall
							lo3d.set( x, y3d, dZEnd );
							la.getCaption( ).setValue( sText );
							t3dre.setLocation3D( lo3d );
							t3dre.setTextPosition( TextRenderEvent.LEFT );
							t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							dc.addLabel( t3dre );

							// Right wall
							lo3d.set( dXEnd, y3d, dZ );
							la.getCaption( ).setValue( sText );
							t3dre.setLocation3D( lo3d );
							t3dre.setTextPosition( TextRenderEvent.RIGHT );
							t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							dc.addLabel( t3dre );
						}
						else
						{
							lo.set( x, y );
							la.getCaption( ).setValue( sText );
							tre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							ipr.drawText( tre );
						}
						ScriptHandler.callFunction( sh,
								ScriptHandler.AFTER_DRAW_AXIS_LABEL,
								axModel,
								la );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_AXIS_LABEL,
								la );
					}
					dAxisValue *= dAxisStep;
				}
			}
			else if ( ( sc.getType( ) & IConstants.LINEAR ) == IConstants.LINEAR )
			{
				double dAxisValue = Methods.asDouble( sc.getMinimum( ) )
						.doubleValue( );
				final double dAxisStep = Methods.asDouble( sc.getStep( ) )
						.doubleValue( );
				if ( fs == null )
				{
					df = new DecimalFormat( sc.getNumericPattern( ) );
				}

				dAxisValue = Methods.asDouble( sc.getMinimum( ) ).doubleValue( ); // RESET
				double x = ( iLabelLocation == IConstants.LEFT ) ? dXTick1 - 1
						: dXTick2 + 1;
				for ( int i = 0; i < da.length; i++ )
				{
					nde.setValue( dAxisValue );
					try
					{
						sText = ValueFormatter.format( nde,
								fs,
								ax.getRunTimeContext( ).getLocale( ),
								df );
					}
					catch ( ChartException dfex )
					{
						logger.log( dfex );
						sText = IConstants.NULL_STRING;
					}

					y = (int) da[i];
					if ( bRendering3D )
					{
						y3d = (int) da3D[i];
					}
					if ( ( iWhatToDraw & IConstants.AXIS ) == IConstants.AXIS )
					{
						double dXMinorTick1 = ( ( iMinorTickStyle & IConstants.TICK_LEFT ) == IConstants.TICK_LEFT ) ? ( dX - IConstants.TICK_SIZE )
								: dX, dXMinorTick2 = ( ( iMinorTickStyle & IConstants.TICK_RIGHT ) == IConstants.TICK_RIGHT ) ? dX
								+ IConstants.TICK_SIZE
								: dX;
						if ( dXMinorTick1 != dXMinorTick2 )
						{
							// RENDER THE MINOR TICKS FIRST (For ALL but the
							// last Major tick)
							if ( i != da.length - 1 )
							{
								if ( bRenderOrthogonal3DAxis )
								{
									Line3DRenderEvent l3dreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										l3dreMinor = (Line3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												Line3DRenderEvent.class );
										l3dreMinor.setLineAttributes( liaMinorTick );
										l3dreMinor.setStart3D( Location3DImpl.create( dXMinorTick1,
												y3d + daMinor[k],
												dZ ) );
										l3dreMinor.setEnd3D( Location3DImpl.create( dXMinorTick2,
												y3d + daMinor[k],
												dZ ) );
										dc.addLine( l3dreMinor );
									}
								}
								else
								{
									LineRenderEvent lreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										lreMinor = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												LineRenderEvent.class );
										lreMinor.setLineAttributes( liaMinorTick );
										lreMinor.setStart( LocationImpl.create( dXMinorTick1,
												y - daMinor[k] ) );
										lreMinor.setEnd( LocationImpl.create( dXMinorTick2,
												y - daMinor[k] ) );
										ipr.drawLine( lreMinor );
									}
								}
							}
						}

						if ( dXTick1 != dXTick2 )
						{
							if ( bRenderOrthogonal3DAxis )
							{
								l3dre.setLineAttributes( liaMajorTick );
								l3dre.setStart3D( dXTick1, y3d, dZ );
								l3dre.setEnd3D( dXTick2, y3d, dZ );
								dc.addLine( l3dre );
							}
							else
							{
								lre.setLineAttributes( liaMajorTick );
								lre.getStart( ).set( dXTick1, y );
								lre.getEnd( ).set( dXTick2, y );
								ipr.drawLine( lre );
							}

							if ( iv != null
									&& iDimension == IConstants.TWO_5_D
									&& iv.getType( ) == IntersectionValue.VALUE )
							{
								lre.setLineAttributes( lia );
								lre.setStart( LocationImpl.create( dX, y ) );
								lre.setEnd( LocationImpl.create( dX
										+ dSeriesThickness, y
										- dSeriesThickness ) );
								ipr.drawLine( lre );
							}
						}
					}

					if ( bRenderAxisLabels )
					{
						ScriptHandler.callFunction( sh,
								ScriptHandler.BEFORE_DRAW_AXIS_LABEL,
								axModel,
								la );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL,
								la );
						if ( bRendering3D )
						{
							// Left wall
							lo3d.set( x, y3d, dZEnd );
							la.getCaption( ).setValue( sText );
							t3dre.setLocation3D( lo3d );
							t3dre.setTextPosition( TextRenderEvent.LEFT );
							t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							dc.addLabel( t3dre );

							// Right wall
							lo3d.set( dXEnd, y3d, dZ );
							la.getCaption( ).setValue( sText );
							t3dre.setLocation3D( lo3d );
							t3dre.setTextPosition( TextRenderEvent.RIGHT );
							t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							dc.addLabel( t3dre );
						}
						else
						{
							lo.set( x, y );
							la.getCaption( ).setValue( sText );
							tre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							ipr.drawText( tre );
						}
						ScriptHandler.callFunction( sh,
								ScriptHandler.AFTER_DRAW_AXIS_LABEL,
								axModel,
								la );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_AXIS_LABEL,
								la );
					}
					dAxisValue += dAxisStep;
				}
			}
			else if ( ( sc.getType( ) & IConstants.DATE_TIME ) == IConstants.DATE_TIME )
			{
				CDateTime cdt, cdtAxisValue = Methods.asDateTime( sc.getMinimum( ) );
				final int iUnit = Methods.asInteger( sc.getUnit( ) );
				final int iStep = Methods.asInteger( sc.getStep( ) );
				SimpleDateFormat sdf = null;
				if ( fs == null )
				{
					sdf = new SimpleDateFormat( CDateTime.getPreferredFormat( iUnit ) );
				}
				cdt = cdtAxisValue;

				double x = ( iLabelLocation == IConstants.LEFT ) ? dXTick1 - 1
						: dXTick2 + 1;
				for ( int i = 0; i < da.length; i++ )
				{
					try
					{
						sText = ValueFormatter.format( cdt,
								ax.getFormatSpecifier( ),
								ax.getRunTimeContext( ).getLocale( ),
								sdf );
					}
					catch ( ChartException dfex )
					{
						logger.log( dfex );
						sText = IConstants.NULL_STRING;
					}

					y = (int) da[i];
					if ( bRendering3D )
					{
						y3d = (int) da3D[i];
					}
					if ( ( iWhatToDraw & IConstants.AXIS ) == IConstants.AXIS )
					{
						double dXMinorTick1 = ( ( iMinorTickStyle & IConstants.TICK_LEFT ) == IConstants.TICK_LEFT ) ? ( dX - IConstants.TICK_SIZE )
								: dX, dXMinorTick2 = ( ( iMinorTickStyle & IConstants.TICK_RIGHT ) == IConstants.TICK_RIGHT ) ? dX
								+ IConstants.TICK_SIZE
								: dX;
						if ( dXMinorTick1 != dXMinorTick2 )
						{
							// RENDER THE MINOR TICKS FIRST (For ALL but the
							// last Major tick)
							if ( i != da.length - 1 )
							{
								if ( bRenderOrthogonal3DAxis )
								{
									Line3DRenderEvent l3dreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										l3dreMinor = (Line3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												Line3DRenderEvent.class );
										l3dreMinor.setLineAttributes( liaMinorTick );
										l3dreMinor.setStart3D( Location3DImpl.create( dXMinorTick1,
												y3d + daMinor[k],
												dZ ) );
										l3dreMinor.setEnd3D( Location3DImpl.create( dXMinorTick2,
												y3d + daMinor[k],
												dZ ) );
										dc.addLine( l3dreMinor );
									}
								}
								else
								{
									LineRenderEvent lreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										lreMinor = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												LineRenderEvent.class );
										lreMinor.setLineAttributes( liaMinorTick );
										lreMinor.setStart( LocationImpl.create( dXMinorTick1,
												y - daMinor[k] ) );
										lreMinor.setEnd( LocationImpl.create( dXMinorTick2,
												y - daMinor[k] ) );
										ipr.drawLine( lreMinor );
									}
								}
							}
						}

						if ( dXTick1 != dXTick2 )
						{
							if ( bRenderOrthogonal3DAxis )
							{
								l3dre.setLineAttributes( liaMajorTick );
								l3dre.setStart3D( dXTick1, y3d, dZ );
								l3dre.setEnd3D( dXTick2, y3d, dZ );
								dc.addLine( l3dre );
							}
							else
							{
								lre.setLineAttributes( liaMajorTick );
								lre.getStart( ).set( dXTick1, y );
								lre.getEnd( ).set( dXTick2, y );
								ipr.drawLine( lre );
							}

							if ( iv != null
									&& iDimension == IConstants.TWO_5_D
									&& iv.getType( ) == IntersectionValue.VALUE )
							{
								lre.setStart( LocationImpl.create( dX, y ) );
								lre.setEnd( LocationImpl.create( dX
										+ dSeriesThickness, y
										- dSeriesThickness ) );
								ipr.drawLine( lre );
							}
						}
					}

					if ( bRenderAxisLabels )
					{
						ScriptHandler.callFunction( sh,
								ScriptHandler.BEFORE_DRAW_AXIS_LABEL,
								axModel,
								la );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL,
								la );
						if ( bRendering3D )
						{
							// Left wall
							lo3d.set( x, y3d, dZEnd );
							la.getCaption( ).setValue( sText );
							t3dre.setLocation3D( lo3d );
							t3dre.setTextPosition( TextRenderEvent.LEFT );
							t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							dc.addLabel( t3dre );

							// Right wall
							lo3d.set( dXEnd, y3d, dZ );
							la.getCaption( ).setValue( sText );
							t3dre.setLocation3D( lo3d );
							t3dre.setTextPosition( TextRenderEvent.RIGHT );
							t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							dc.addLabel( t3dre );
						}
						else
						{
							lo.set( x, y );
							la.getCaption( ).setValue( sText );
							tre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							ipr.drawText( tre );
						}
						ScriptHandler.callFunction( sh,
								ScriptHandler.AFTER_DRAW_AXIS_LABEL,
								axModel,
								la );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_AXIS_LABEL,
								la );
					}

					cdt = cdtAxisValue.forward( iUnit, iStep * ( i + 1 ) ); // ALWAYS
					// W.R.T
					// START
					// VALUE
				}
			}

			la = ax.getTitle( ); // TEMPORARILY USE FOR AXIS TITLE
			if ( la.isVisible( ) && bRenderAxisLabels )
			{
				ScriptHandler.callFunction( sh,
						ScriptHandler.BEFORE_DRAW_AXIS_TITLE,
						axModel,
						la );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_TITLE,
						la );
				final String sRestoreValue = la.getCaption( ).getValue( );
				la.getCaption( )
						.setValue( rtc.externalizedMessage( sRestoreValue ) );
				BoundingBox bb = null;
				try
				{
					bb = Methods.computeBox( xs,
							ax.getTitlePosition( ),
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

				if ( bRendering3D )
				{
					Bounds cbo = getPlotBounds( );

					tre.setBlockBounds( BoundsImpl.create( cbo.getLeft( )
							+ ( cbo.getWidth( ) / 3d - bb.getWidth( ) )
							/ 2d,
							cbo.getTop( ) + 30,
							bb.getWidth( ),
							bb.getHeight( ) ) );

					tre.setLabel( la );
					tre.setBlockAlignment( null );
					tre.setAction( TextRenderEvent.RENDER_TEXT_IN_BLOCK );
					ipr.drawText( tre );

					tre.setBlockBounds( BoundsImpl.create( cbo.getLeft( )
							+ cbo.getWidth( )
							- bb.getWidth( ),
							cbo.getTop( ) + 30 * 2,
							bb.getWidth( ),
							bb.getHeight( ) ) );

					ipr.drawText( tre );
				}
				else
				{
					final Bounds bo = BoundsImpl.create( ax.getTitleCoordinate( ),
							daEndPoints[1],
							bb.getWidth( ),
							daEndPoints[0] - daEndPoints[1] );

					tre.setBlockBounds( bo );
					tre.setLabel( la );
					tre.setBlockAlignment( null );
					tre.setAction( TextRenderEvent.RENDER_TEXT_IN_BLOCK );
					ipr.drawText( tre );
				}

				la.getCaption( ).setValue( sRestoreValue );
				ScriptHandler.callFunction( sh,
						ScriptHandler.AFTER_DRAW_AXIS_TITLE,
						axModel,
						la );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_AXIS_TITLE,
						la );
			}
			la = ax.getLabel( );

			if ( iv != null
					&& iv.getType( ) == IntersectionValue.MAX
					&& iDimension == IConstants.TWO_5_D )
			{
				trae.setTranslation( -dSeriesThickness, dSeriesThickness );
				ipr.applyTransformation( trae );
			}
		}
		else if ( iOrientation == IConstants.HORIZONTAL )
		{
			int x;
			int x3d = 0;
			int z3d = 0;
			double dY = dLocation;
			double dX = 0;
			double dZ = 0;

			if ( bRendering3D )
			{
				Location3D l3d = ax.getAxisCoordinate3D( );

				dX = l3d.getX( );
				dY = l3d.getY( );
				dZ = l3d.getZ( );
			}

			double dYTick1 = ( ( iMajorTickStyle & IConstants.TICK_ABOVE ) == IConstants.TICK_ABOVE ) ? ( dY - IConstants.TICK_SIZE )
					: dY;
			double dYTick2 = ( ( iMajorTickStyle & IConstants.TICK_BELOW ) == IConstants.TICK_BELOW ) ? dY
					+ IConstants.TICK_SIZE
					: dY;

			if ( iv != null
					&& iv.getType( ) == IntersectionValue.MAX
					&& iDimension == IConstants.TWO_5_D )
			{
				trae.setTransform( TransformationEvent.TRANSLATE );
				trae.setTranslation( dSeriesThickness, -dSeriesThickness );
				ipr.applyTransformation( trae );
			}

			if ( ( iWhatToDraw & IConstants.AXIS ) == IConstants.AXIS
					&& lia.isVisible( ) )
			{
				if ( bRenderBase3DAxis )
				{
					final double dStart = daEndPoints3D[0];
					final double dEnd = daEndPoints3D[1];
					l3dre.setLineAttributes( lia );
					l3dre.setStart3D( dStart, dY, dZ );
					l3dre.setEnd3D( dEnd, dY, dZ );
					dc.addLine( l3dre );
				}
				else if ( bRenderAncillary3DAxis )
				{
					final double dStart = daEndPoints3D[0];
					final double dEnd = daEndPoints3D[1];
					l3dre.setLineAttributes( lia );
					l3dre.setStart3D( dX, dY, dStart );
					l3dre.setEnd3D( dX, dY, dEnd );
					dc.addLine( l3dre );
				}
				else
				{
					final double dStart = daEndPoints[0] - insCA.getLeft( ), dEnd = daEndPoints[1]
							+ insCA.getRight( );
					if ( iv != null
							&& iv.getType( ) == IntersectionValue.VALUE
							&& iDimension == IConstants.TWO_5_D )
					{
						// Zero plane.
						final Location[] loa = new Location[4];
						loa[0] = LocationImpl.create( dStart, dY );
						loa[1] = LocationImpl.create( dStart + dSeriesThickness,
								dY - dSeriesThickness );
						loa[2] = LocationImpl.create( dEnd + dSeriesThickness,
								dY - dSeriesThickness );
						loa[3] = LocationImpl.create( dEnd, dY );

						final PolygonRenderEvent pre = (PolygonRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
								PolygonRenderEvent.class );
						pre.setPoints( loa );
						pre.setBackground( ColorDefinitionImpl.create( 255,
								255,
								255,
								127 ) );
						pre.setOutline( lia );
						ipr.fillPolygon( pre );
					}
					lre.setLineAttributes( lia );
					lre.getStart( ).set( dStart, dY );
					lre.getEnd( ).set( dEnd, dY );
					ipr.drawLine( lre );
				}
			}

			if ( ( sc.getType( ) & IConstants.TEXT ) == IConstants.TEXT
					|| sc.isCategoryScale( ) )
			{
				double dAngleInRadians = ( -dAngleInDegrees * Math.PI ) / 180;
				double dSineTheta = Math.abs( Math.sin( dAngleInRadians ) );
				double dCosTheta = Math.abs( Math.cos( dAngleInRadians ) );
				double dOffset = 0, dW, dH, dHSineTheta;
				double dUnitSize = sc.getUnitSize( );
				DataSetIterator dsi = sc.getData( );
				final int iDateTimeUnit = ( sc.getType( ) == IConstants.DATE_TIME ) ? CDateTime.computeUnit( dsi )
						: IConstants.UNDEFINED;
				final ITextMetrics itmText = xs.getTextMetrics( la );

				if ( dAngleInDegrees == 90 || dAngleInDegrees == 0 )
				{
					dOffset = dUnitSize / 2;
				}

				double y = ( iLabelLocation == IConstants.ABOVE ) ? dYTick1 - 1
						: dYTick2 + 1;
				dsi.reset( );
				for ( int i = 0; i < da.length - 1; i++ )
				{
					x = (int) da[i];
					if ( bRendering3D )
					{
						x3d = (int) da3D[i];
						z3d = (int) da3D[i];
					}
					if ( ( iWhatToDraw & IConstants.AXIS ) == IConstants.AXIS )
					{
						double dYMinorTick1 = ( ( iMinorTickStyle & IConstants.TICK_ABOVE ) == IConstants.TICK_ABOVE ) ? ( dY - IConstants.TICK_SIZE )
								: dY, dYMinorTick2 = ( ( iMinorTickStyle & IConstants.TICK_BELOW ) == IConstants.TICK_BELOW ) ? dY
								+ IConstants.TICK_SIZE
								: dY;
						if ( dYMinorTick1 != -dYMinorTick2 )
						{
							// RENDER THE MINOR TICKS FIRST (For ALL but the
							// last Major tick)
							if ( i != da.length - 1 )
							{
								if ( bRenderBase3DAxis )
								{
									Line3DRenderEvent l3dreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										l3dreMinor = (Line3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												Line3DRenderEvent.class );
										l3dreMinor.setLineAttributes( liaMinorTick );
										l3dreMinor.setStart3D( Location3DImpl.create( x3d
												+ daMinor[k],
												dYMinorTick1,
												dZ ) );
										l3dreMinor.setEnd3D( Location3DImpl.create( x3d
												+ daMinor[k],
												dYMinorTick2,
												dZ ) );
										dc.addLine( l3dreMinor );
									}
								}
								else if ( bRenderAncillary3DAxis )
								{
									Line3DRenderEvent l3dreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										l3dreMinor = (Line3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												Line3DRenderEvent.class );
										l3dreMinor.setLineAttributes( liaMinorTick );
										l3dreMinor.setStart3D( Location3DImpl.create( dX,
												dYMinorTick1,
												z3d + daMinor[k] ) );
										l3dreMinor.setEnd3D( Location3DImpl.create( dX,
												dYMinorTick2,
												z3d + daMinor[k] ) );
										dc.addLine( l3dreMinor );
									}
								}
								else
								{
									LineRenderEvent lreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										lreMinor = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												LineRenderEvent.class );
										lreMinor.setLineAttributes( liaMinorTick );
										lreMinor.setStart( LocationImpl.create( x
												+ daMinor[k],
												dYMinorTick1 ) );
										lreMinor.setEnd( LocationImpl.create( x
												+ daMinor[k], dYMinorTick2 ) );
										ipr.drawLine( lreMinor );
									}
								}
							}
						}

						if ( dYTick1 != dYTick2 )
						{
							if ( bRenderBase3DAxis )
							{
								l3dre.setLineAttributes( liaMajorTick );
								l3dre.setStart3D( x3d, dYTick1, dZ );
								l3dre.setEnd3D( x3d, dYTick2, dZ );
								dc.addLine( l3dre );
							}
							else if ( bRenderAncillary3DAxis )
							{
								l3dre.setLineAttributes( liaMajorTick );
								l3dre.setStart3D( dX, dYTick1, z3d );
								l3dre.setEnd3D( dX, dYTick2, z3d );
								dc.addLine( l3dre );
							}
							else
							{
								lre.setLineAttributes( liaMajorTick );
								lre.getStart( ).set( x, dYTick1 );
								lre.getEnd( ).set( x, dYTick2 );
								ipr.drawLine( lre );
							}

							if ( iv != null
									&& iDimension == IConstants.TWO_5_D
									&& iv.getType( ) == IntersectionValue.VALUE )
							{
								lre.getStart( ).set( x, dY );
								lre.getEnd( ).set( x + dSeriesThickness,
										dY - dSeriesThickness );
								ipr.drawLine( lre );
							}
						}
					}

					if ( bRenderAxisLabels ) // OPTIMIZED: ONLY PROCESS IF
					// AXES
					// LABELS ARE VISIBLE OR REQUESTED
					// FOR
					{
						la.getCaption( )
								.setValue( sc.formatCategoryValue( sc.getType( ),
										dsi.next( ),
										iDateTimeUnit ) );
						ScriptHandler.callFunction( sh,
								ScriptHandler.BEFORE_DRAW_AXIS_LABEL,
								axModel,
								la );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL,
								la );
						itmText.reuse( la );// RECYCLED
						dH = itmText.getFullHeight( );
						dW = itmText.getFullWidth( );
						dHSineTheta = dH * dSineTheta;
						if ( dAngleInDegrees > 0 && dAngleInDegrees < 90 )
						{
							if ( iLabelLocation == IConstants.ABOVE )
							{
								dOffset = dUnitSize
										/ 2
										- ( dW * dCosTheta + dHSineTheta )
										/ 2
										+ dHSineTheta;
							}
							else if ( iLabelLocation == IConstants.BELOW )
							{
								dOffset = dUnitSize
										+ dHSineTheta
										- ( dUnitSize - dW * dCosTheta + dHSineTheta )
										/ 2
										- dHSineTheta;
							}
						}
						else if ( dAngleInDegrees < 0 && dAngleInDegrees > -90 )
						{
							if ( iLabelLocation == IConstants.ABOVE )
							{
								dOffset = dUnitSize
										/ 2
										- dHSineTheta
										/ 2
										+ ( dW * dCosTheta + dHSineTheta )
										/ 2;
							}
							else if ( iLabelLocation == IConstants.BELOW )
							{
								dOffset = ( dUnitSize - dW * dCosTheta + dHSineTheta ) / 2;
							}
						}
						else if ( dAngleInDegrees == 0
								|| dAngleInDegrees == 90
								|| dAngleInDegrees == -90 )
						{
							dOffset = dUnitSize / 2;
						}

						if ( bRendering3D )
						{
							if ( axisType == IConstants.BASE_AXIS )
							{
								lo3d.set( x3d + dOffset, y, dZEnd );
							}
							else
							{
								lo3d.set( dXEnd, y, z3d + dOffset );
							}
							t3dre.setLocation3D( lo3d );
							t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							dc.addLabel( t3dre );
						}
						else
						{
							lo.set( x + dOffset, y );
							tre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							ipr.drawText( tre );
						}
						ScriptHandler.callFunction( sh,
								ScriptHandler.AFTER_DRAW_AXIS_LABEL,
								axModel,
								la );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_AXIS_LABEL,
								la );
					}
				}

				// ONE LAST TICK
				x = (int) da[da.length - 1];
				if ( ( iWhatToDraw & IConstants.AXIS ) == IConstants.AXIS )
				{
					if ( dYTick1 != dYTick2 )
					{
						if ( bRenderBase3DAxis )
						{
							l3dre.setLineAttributes( liaMajorTick );
							l3dre.setStart3D( x3d, dYTick1, dZ );
							l3dre.setEnd3D( x3d, dYTick2, dZ );
							dc.addLine( l3dre );
						}
						else if ( bRenderAncillary3DAxis )
						{
							l3dre.setLineAttributes( liaMajorTick );
							l3dre.setStart3D( dX, dYTick1, z3d );
							l3dre.setEnd3D( dX, dYTick2, z3d );
							dc.addLine( l3dre );
						}
						else
						{
							lre.setLineAttributes( liaMajorTick );
							lre.getStart( ).set( x, dYTick1 );
							lre.getEnd( ).set( x, dYTick2 );
							ipr.drawLine( lre );
						}

						if ( iv != null
								&& iDimension == IConstants.TWO_5_D
								&& iv.getType( ) == IntersectionValue.VALUE )
						{
							lre.getStart( ).set( x, dY );
							lre.getEnd( ).set( x + dSeriesThickness,
									dY - dSeriesThickness );
							ipr.drawLine( lre );
						}
					}
				}
				itmText.dispose( ); // DISPOSED
			}
			else if ( ( sc.getType( ) & IConstants.LINEAR ) == IConstants.LINEAR )
			{
				double dAxisValue = Methods.asDouble( sc.getMinimum( ) )
						.doubleValue( );
				final double dAxisStep = Methods.asDouble( sc.getStep( ) )
						.doubleValue( );
				if ( fs == null )
				{
					df = new DecimalFormat( sc.getNumericPattern( ) );
				}

				dAxisValue = Methods.asDouble( sc.getMinimum( ) ).doubleValue( ); // RESET
				double y = ( iLabelLocation == IConstants.ABOVE ) ? dYTick1 - 1
						: dYTick2 + 1;
				for ( int i = 0; i < da.length; i++ )
				{
					x = (int) da[i];
					if ( bRendering3D )
					{
						x3d = (int) da3D[i];
						z3d = (int) da3D[i];
					}
					if ( ( iWhatToDraw & IConstants.AXIS ) == IConstants.AXIS )
					{
						double dYMinorTick1 = ( ( iMinorTickStyle & IConstants.TICK_ABOVE ) == IConstants.TICK_ABOVE ) ? ( dY - IConstants.TICK_SIZE )
								: dY, dYMinorTick2 = ( ( iMinorTickStyle & IConstants.TICK_BELOW ) == IConstants.TICK_BELOW ) ? dY
								+ IConstants.TICK_SIZE
								: dY;
						if ( dYMinorTick1 != -dYMinorTick2 )
						{
							// RENDER THE MINOR TICKS FIRST (For ALL but the
							// last Major tick)
							if ( i != da.length - 1 )
							{
								if ( bRenderBase3DAxis )
								{
									Line3DRenderEvent l3dreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										l3dreMinor = (Line3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												Line3DRenderEvent.class );
										l3dreMinor.setLineAttributes( liaMinorTick );
										l3dreMinor.setStart3D( Location3DImpl.create( x3d
												+ daMinor[k],
												dYMinorTick1,
												dZ ) );
										l3dreMinor.setEnd3D( Location3DImpl.create( x3d
												+ daMinor[k],
												dYMinorTick2,
												dZ ) );
										dc.addLine( l3dreMinor );
									}
								}
								else if ( bRenderAncillary3DAxis )
								{
									Line3DRenderEvent l3dreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										l3dreMinor = (Line3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												Line3DRenderEvent.class );
										l3dreMinor.setLineAttributes( liaMinorTick );
										l3dreMinor.setStart3D( Location3DImpl.create( dX,
												dYMinorTick1,
												z3d + daMinor[k] ) );
										l3dreMinor.setEnd3D( Location3DImpl.create( dX,
												dYMinorTick2,
												z3d + daMinor[k] ) );
										dc.addLine( l3dreMinor );
									}
								}
								else
								{
									LineRenderEvent lreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										lreMinor = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												LineRenderEvent.class );
										lreMinor.setLineAttributes( liaMinorTick );
										lreMinor.setStart( LocationImpl.create( x
												+ daMinor[k],
												dYMinorTick1 ) );
										lreMinor.setEnd( LocationImpl.create( x
												+ daMinor[k], dYMinorTick2 ) );
										ipr.drawLine( lreMinor );
									}
								}
							}
						}

						if ( dYTick1 != dYTick2 )
						{
							if ( bRenderBase3DAxis )
							{
								l3dre.setLineAttributes( liaMajorTick );
								l3dre.setStart3D( x3d, dYTick1, dZ );
								l3dre.setEnd3D( x3d, dYTick2, dZ );
								dc.addLine( l3dre );
							}
							else if ( bRenderAncillary3DAxis )
							{
								l3dre.setLineAttributes( liaMajorTick );
								l3dre.setStart3D( dX, dYTick1, z3d );
								l3dre.setEnd3D( dX, dYTick2, z3d );
								dc.addLine( l3dre );
							}
							else
							{
								lre.setLineAttributes( liaMajorTick );
								lre.getStart( ).set( x, dYTick1 );
								lre.getEnd( ).set( x, dYTick2 );
								ipr.drawLine( lre );
							}

							if ( iv != null
									&& iDimension == IConstants.TWO_5_D
									&& iv.getType( ) == IntersectionValue.VALUE )
							{
								lre.getStart( ).set( x, dY );
								lre.getEnd( ).set( x + dSeriesThickness,
										dY - dSeriesThickness );
								ipr.drawLine( lre );
							}
						}
					}

					if ( bRenderAxisLabels ) // OPTIMIZED: ONLY PROCESS IF
					// AXES
					// LABELS ARE VISIBLE OR REQUESTED
					// FOR
					{
						nde.setValue( dAxisValue );
						try
						{
							sText = ValueFormatter.format( nde,
									ax.getFormatSpecifier( ),
									ax.getRunTimeContext( ).getLocale( ),
									df );
						}
						catch ( ChartException dfex )
						{
							logger.log( dfex );
							sText = IConstants.NULL_STRING;
						}

						ScriptHandler.callFunction( sh,
								ScriptHandler.BEFORE_DRAW_AXIS_LABEL,
								axModel,
								la );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL,
								la );

						if ( bRendering3D )
						{
							if ( axisType == IConstants.BASE_AXIS )
							{
								lo3d.set( x3d, y, dZEnd );
							}
							else
							{
								lo3d.set( dXEnd, y, z3d );
							}
							la.getCaption( ).setValue( sText );
							t3dre.setLocation3D( lo3d );
							t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							dc.addLabel( t3dre );
						}
						else
						{
							lo.set( x, y );
							la.getCaption( ).setValue( sText );
							tre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							ipr.drawText( tre );
						}
						ScriptHandler.callFunction( sh,
								ScriptHandler.AFTER_DRAW_AXIS_LABEL,
								axModel,
								la );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_AXIS_LABEL,
								la );
					}
					dAxisValue += dAxisStep;
				}
			}
			else if ( ( sc.getType( ) & IConstants.LOGARITHMIC ) == IConstants.LOGARITHMIC )
			{
				double dAxisValue = Methods.asDouble( sc.getMinimum( ) )
						.doubleValue( );
				final double dAxisStep = Methods.asDouble( sc.getStep( ) )
						.doubleValue( );

				dAxisValue = Methods.asDouble( sc.getMinimum( ) ).doubleValue( ); // RESET
				double y = ( iLabelLocation == IConstants.ABOVE ) ? dYTick1 - 1
						: dYTick2 + 1;
				for ( int i = 0; i < da.length; i++ )
				{
					x = (int) da[i];
					if ( bRendering3D )
					{
						x3d = (int) da3D[i];
						z3d = (int) da3D[i];
					}

					if ( ( iWhatToDraw & IConstants.AXIS ) == IConstants.AXIS )
					{
						double dYMinorTick1 = ( ( iMinorTickStyle & IConstants.TICK_ABOVE ) == IConstants.TICK_ABOVE ) ? ( dY - IConstants.TICK_SIZE )
								: dY, dYMinorTick2 = ( ( iMinorTickStyle & IConstants.TICK_BELOW ) == IConstants.TICK_BELOW ) ? dY
								+ IConstants.TICK_SIZE
								: dY;
						if ( dYMinorTick1 != -dYMinorTick2 )
						{
							// RENDER THE MINOR TICKS FIRST (For ALL but the
							// last Major tick)
							if ( i != da.length - 1 )
							{
								if ( bRenderBase3DAxis )
								{
									Line3DRenderEvent l3dreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										l3dreMinor = (Line3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												Line3DRenderEvent.class );
										l3dreMinor.setLineAttributes( liaMinorTick );
										l3dreMinor.setStart3D( Location3DImpl.create( x3d
												+ daMinor[k],
												dYMinorTick1,
												dZ ) );
										l3dreMinor.setEnd3D( Location3DImpl.create( x3d
												+ daMinor[k],
												dYMinorTick2,
												dZ ) );
										dc.addLine( l3dreMinor );
									}
								}
								else if ( bRenderAncillary3DAxis )
								{
									Line3DRenderEvent l3dreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										l3dreMinor = (Line3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												Line3DRenderEvent.class );
										l3dreMinor.setLineAttributes( liaMinorTick );
										l3dreMinor.setStart3D( Location3DImpl.create( dX,
												dYMinorTick1,
												z3d + daMinor[k] ) );
										l3dreMinor.setEnd3D( Location3DImpl.create( dX,
												dYMinorTick2,
												z3d + daMinor[k] ) );
										dc.addLine( l3dreMinor );
									}
								}
								else
								{
									LineRenderEvent lreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										lreMinor = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												LineRenderEvent.class );
										lreMinor.setLineAttributes( liaMinorTick );
										lreMinor.setStart( LocationImpl.create( x
												+ daMinor[k],
												dYMinorTick1 ) );
										lreMinor.setEnd( LocationImpl.create( x
												+ daMinor[k], dYMinorTick2 ) );
										ipr.drawLine( lreMinor );
									}
								}
							}
						}

						if ( dYTick1 != dYTick2 )
						{
							if ( bRenderBase3DAxis )
							{
								l3dre.setLineAttributes( liaMajorTick );
								l3dre.setStart3D( x3d, dYTick1, dZ );
								l3dre.setEnd3D( x3d, dYTick2, dZ );
								dc.addLine( l3dre );
							}
							else if ( bRenderAncillary3DAxis )
							{
								l3dre.setLineAttributes( liaMajorTick );
								l3dre.setStart3D( dX, dYTick1, z3d );
								l3dre.setEnd3D( dX, dYTick2, z3d );
								dc.addLine( l3dre );
							}
							else
							{
								lre.setLineAttributes( lia );
								lre.getStart( ).set( x, dYTick1 );
								lre.getEnd( ).set( x, dYTick2 );
								ipr.drawLine( lre );
							}

							if ( iv != null
									&& iDimension == IConstants.TWO_5_D
									&& iv.getType( ) == IntersectionValue.VALUE )
							{
								lre.getStart( ).set( x, dY );
								lre.getEnd( ).set( x + dSeriesThickness,
										dY - dSeriesThickness );
								ipr.drawLine( lre );
							}
						}
					}

					if ( bRenderAxisLabels ) // OPTIMIZED: ONLY PROCESS IF
					// AXES
					// LABELS ARE VISIBLE OR REQUESTED
					// FOR
					{
						if ( fs == null )
						{
							df = new DecimalFormat( sc.getNumericPattern( dAxisValue ) );
						}
						nde.setValue( dAxisValue );
						try
						{
							sText = ValueFormatter.format( nde,
									ax.getFormatSpecifier( ),
									ax.getRunTimeContext( ).getLocale( ),
									df );
						}
						catch ( ChartException dfex )
						{
							logger.log( dfex );
							sText = IConstants.NULL_STRING;
						}

						ScriptHandler.callFunction( sh,
								ScriptHandler.BEFORE_DRAW_AXIS_LABEL,
								axModel,
								la );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL,
								la );
						if ( bRendering3D )
						{
							if ( axisType == IConstants.BASE_AXIS )
							{
								lo3d.set( x3d, y, dZEnd );
							}
							else
							{
								lo3d.set( dXEnd, y, z3d );
							}
							la.getCaption( ).setValue( sText );
							t3dre.setLocation3D( lo3d );
							t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							dc.addLabel( t3dre );
						}
						else
						{
							lo.set( x, y );
							la.getCaption( ).setValue( sText );
							tre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							ipr.drawText( tre );
						}
						ScriptHandler.callFunction( sh,
								ScriptHandler.AFTER_DRAW_AXIS_LABEL,
								axModel,
								la );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_AXIS_LABEL,
								la );
					}
					dAxisValue *= dAxisStep;
				}
			}
			else if ( ( sc.getType( ) & IConstants.DATE_TIME ) == IConstants.DATE_TIME )
			{
				CDateTime cdt, cdtAxisValue = Methods.asDateTime( sc.getMinimum( ) );
				final int iUnit = Methods.asInteger( sc.getUnit( ) );
				final int iStep = Methods.asInteger( sc.getStep( ) );
				SimpleDateFormat sdf = null;

				if ( fs == null )
				{
					sdf = new SimpleDateFormat( CDateTime.getPreferredFormat( iUnit ) );
				}
				cdt = cdtAxisValue;

				double y = ( iLabelLocation == IConstants.ABOVE ) ? dYTick1 - 1
						: dYTick2 + 1;
				for ( int i = 0; i < da.length; i++ )
				{
					x = (int) da[i];
					if ( bRendering3D )
					{
						x3d = (int) da3D[i];
						z3d = (int) da3D[i];
					}

					if ( ( iWhatToDraw & IConstants.AXIS ) == IConstants.AXIS )
					{
						double dYMinorTick1 = ( ( iMinorTickStyle & IConstants.TICK_ABOVE ) == IConstants.TICK_ABOVE ) ? ( dY - IConstants.TICK_SIZE )
								: dY, dYMinorTick2 = ( ( iMinorTickStyle & IConstants.TICK_BELOW ) == IConstants.TICK_BELOW ) ? dY
								+ IConstants.TICK_SIZE
								: dY;
						if ( dYMinorTick1 != -dYMinorTick2 )
						{
							// RENDER THE MINOR TICKS FIRST (For ALL but the
							// last Major tick)
							if ( i != da.length - 1 )
							{
								if ( bRenderBase3DAxis )
								{
									Line3DRenderEvent l3dreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										l3dreMinor = (Line3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												Line3DRenderEvent.class );
										l3dreMinor.setLineAttributes( liaMinorTick );
										l3dreMinor.setStart3D( Location3DImpl.create( x3d
												+ daMinor[k],
												dYMinorTick1,
												dZ ) );
										l3dreMinor.setEnd3D( Location3DImpl.create( x3d
												+ daMinor[k],
												dYMinorTick2,
												dZ ) );
										dc.addLine( l3dreMinor );
									}
								}
								else if ( bRenderAncillary3DAxis )
								{
									Line3DRenderEvent l3dreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										l3dreMinor = (Line3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												Line3DRenderEvent.class );
										l3dreMinor.setLineAttributes( liaMinorTick );
										l3dreMinor.setStart3D( Location3DImpl.create( dX,
												dYMinorTick1,
												z3d + daMinor[k] ) );
										l3dreMinor.setEnd3D( Location3DImpl.create( dX,
												dYMinorTick2,
												z3d + daMinor[k] ) );
										dc.addLine( l3dreMinor );
									}
								}
								else
								{

									LineRenderEvent lreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										lreMinor = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												LineRenderEvent.class );
										lreMinor.setLineAttributes( liaMinorTick );
										lreMinor.setStart( LocationImpl.create( x
												+ daMinor[k],
												dYMinorTick1 ) );
										lreMinor.setEnd( LocationImpl.create( x
												+ daMinor[k], dYMinorTick2 ) );
										ipr.drawLine( lreMinor );
									}
								}
							}
						}

						if ( dYTick1 != dYTick2 )
						{
							if ( bRenderBase3DAxis )
							{
								l3dre.setLineAttributes( liaMajorTick );
								l3dre.setStart3D( x3d, dYTick1, dZ );
								l3dre.setEnd3D( x3d, dYTick2, dZ );
								dc.addLine( l3dre );
							}
							else if ( bRenderAncillary3DAxis )
							{
								l3dre.setLineAttributes( liaMajorTick );
								l3dre.setStart3D( dX, dYTick1, z3d );
								l3dre.setEnd3D( dX, dYTick2, z3d );
								dc.addLine( l3dre );
							}
							else
							{
								lre.setLineAttributes( liaMajorTick );
								lre.getStart( ).set( x, dYTick1 );
								lre.getEnd( ).set( x, dYTick2 );
								ipr.drawLine( lre );
							}

							if ( iv != null
									&& iDimension == IConstants.TWO_5_D
									&& iv.getType( ) == IntersectionValue.VALUE )
							{
								lre.getStart( ).set( x, dY );
								lre.getEnd( ).set( x + dSeriesThickness,
										dY - dSeriesThickness );
								ipr.drawLine( lre );
							}
						}
					}

					if ( bRenderAxisLabels ) // OPTIMIZED: ONLY PROCESS IF
					// AXES
					// LABELS ARE VISIBLE OR REQUESTED
					// FOR
					{
						try
						{
							sText = ValueFormatter.format( cdt,
									ax.getFormatSpecifier( ),
									ax.getRunTimeContext( ).getLocale( ),
									sdf );
						}
						catch ( ChartException dfex )
						{
							logger.log( dfex );
							sText = IConstants.NULL_STRING;
						}

						ScriptHandler.callFunction( sh,
								ScriptHandler.BEFORE_DRAW_AXIS_LABEL,
								axModel,
								la );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL,
								la );
						if ( bRendering3D )
						{
							if ( axisType == IConstants.BASE_AXIS )
							{
								lo3d.set( x3d, y, dZEnd );
							}
							else
							{
								lo3d.set( dXEnd, y, z3d );
							}
							la.getCaption( ).setValue( sText );
							t3dre.setLocation3D( lo3d );
							t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							dc.addLabel( t3dre );
						}
						else
						{
							lo.set( x, y );
							la.getCaption( ).setValue( sText );
							tre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
							ipr.drawText( tre );
						}
						ScriptHandler.callFunction( sh,
								ScriptHandler.AFTER_DRAW_AXIS_LABEL,
								axModel,
								la );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_AXIS_LABEL,
								la );
					}

					cdt = cdtAxisValue.forward( iUnit, iStep * ( i + 1 ) ); // ALWAYS
					// W.R.T
					// START
					// VALUE
				}
			}

			// RENDER THE AXIS TITLE
			la = ax.getTitle( ); // TEMPORARILY USE FOR AXIS TITLE
			if ( la.isVisible( ) && bRenderAxisLabels )
			{
				ScriptHandler.callFunction( sh,
						ScriptHandler.BEFORE_DRAW_AXIS_TITLE,
						axModel,
						la );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_TITLE,
						la );
				final String sRestoreValue = la.getCaption( ).getValue( );
				la.getCaption( )
						.setValue( rtc.externalizedMessage( sRestoreValue ) ); // EXTERNALIZE
				BoundingBox bb = null;
				try
				{
					bb = Methods.computeBox( xs,
							ax.getTitlePosition( ),
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

				if ( bRendering3D )
				{
					Bounds cbo = getPlotBounds( );

					if ( axisType == IConstants.BASE_AXIS )
					{
						tre.setBlockBounds( BoundsImpl.create( cbo.getLeft( )
								+ ( cbo.getWidth( ) / 3d - bb.getWidth( ) ),
								cbo.getTop( )
										+ cbo.getHeight( )
										- Math.min( bb.getHeight( ),
												bb.getWidth( ) )
										- 30,
								bb.getWidth( ),
								bb.getHeight( ) ) );
					}
					else
					{
						tre.setBlockBounds( BoundsImpl.create( cbo.getLeft( )
								+ cbo.getWidth( )
								* 2
								/ 3d
								+ ( cbo.getWidth( ) / 3d - bb.getWidth( ) )
								/ 2d, cbo.getTop( )
								+ cbo.getHeight( )
								- Math.min( bb.getHeight( ), bb.getWidth( ) )
								- 30
								* 2, bb.getWidth( ), bb.getHeight( ) ) );
					}

					tre.setLabel( la );
					tre.setBlockAlignment( null );
					tre.setAction( TextRenderEvent.RENDER_TEXT_IN_BLOCK );
					ipr.drawText( tre );
				}
				else
				{
					final Bounds bo = BoundsImpl.create( daEndPoints[0],
							ax.getTitleCoordinate( ),
							daEndPoints[1] - daEndPoints[0],
							bb.getHeight( ) );

					tre.setBlockBounds( bo );
					tre.setLabel( la );
					tre.setBlockAlignment( null );
					tre.setAction( TextRenderEvent.RENDER_TEXT_IN_BLOCK );
					ipr.drawText( tre );
				}
				la.getCaption( ).setValue( sRestoreValue ); // RESTORE
				ScriptHandler.callFunction( sh,
						ScriptHandler.AFTER_DRAW_AXIS_TITLE,
						axModel,
						la );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_AXIS_TITLE,
						la );
			}
			la = ax.getLabel( ); // RESTORE BACK TO AXIS LABEL

			if ( iv != null
					&& iv.getType( ) == IntersectionValue.MAX
					&& iDimension == IConstants.TWO_5_D )
			{
				trae.setTranslation( -dSeriesThickness, dSeriesThickness );
				ipr.applyTransformation( trae );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.BaseRenderer#set(org.eclipse.birt.chart.model.Chart,
	 *      java.lang.Object, org.eclipse.birt.chart.model.component.Series,
	 *      org.eclipse.birt.chart.model.component.Axis,
	 *      org.eclipse.birt.chart.model.data.SeriesDefinition)
	 */
	public final void set( Chart _cm, Object _o, Series _se, Axis _ax,
			SeriesDefinition _sd )
	{
		super.set( _cm, _o, _se, _ax, _sd );
		ax = _ax; // HOLD AXIS HERE
	}

	/**
	 * Returns if its a 3D rendering.
	 * 
	 * @return
	 */
	public final boolean isDimension3D( )
	{
		return ( getModel( ).getDimension( ) == ChartDimension.THREE_DIMENSIONAL_LITERAL );
	}

	/**
	 * Returns the 3D engine for this render.
	 */
	public final Engine3D get3DEngine( )
	{
		if ( engine == null )
		{
			ChartWithAxes cwa = (ChartWithAxes) getModel( );

			// Use a fixed light direction here.
			Vector lightDirection = new Vector( -1, 1, -1, false );
			// Vector lightDirection = new Vector( 0, 0, -1, false );
			Bounds bo = getPlotBounds( );

			engine = new Engine3D( cwa.getRotation( ),
					lightDirection,
					bo.getWidth( ),
					bo.getHeight( ),
					500,
					1500,
					10,
					10000,
					100 );
		}

		return engine;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.IModelAccess#getAxis()
	 */
	public final Axis getAxis( )
	{
		return ax;
	}
}
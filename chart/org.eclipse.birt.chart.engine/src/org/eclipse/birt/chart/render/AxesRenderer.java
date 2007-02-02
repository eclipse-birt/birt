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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Map;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.Engine3D;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.computation.withaxes.AllAxes;
import org.eclipse.birt.chart.computation.withaxes.AutoScale;
import org.eclipse.birt.chart.computation.withaxes.Grid;
import org.eclipse.birt.chart.computation.withaxes.OneAxis;
import org.eclipse.birt.chart.computation.withaxes.PlotWith2DAxes;
import org.eclipse.birt.chart.computation.withaxes.PlotWith3DAxes;
import org.eclipse.birt.chart.computation.withaxes.PlotWithAxes;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.event.BlockGenerationEvent;
import org.eclipse.birt.chart.event.ClipRenderEvent;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.Line3DRenderEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.Oval3DRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.Polygon3DRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.internal.model.FittingCalculator;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.Location3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataElement;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.emf.common.util.EList;

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
		// (POSSIBLY PARTICIPATING IN A COMBINATION CHART)
		{
			// SETUP A TIMER
			lTimer = System.currentTimeMillis( );
			htRenderers.put( TIMER, new Long( lTimer ) );

			// RENDER THE CHART BY WALKING THROUGH THE RECURSIVE BLOCK STRUCTURE
			Block bl = cm.getBlock( );
			final Enumeration e = bl.children( true );
			final BlockGenerationEvent bge = new BlockGenerationEvent( bl );

			// ALWAYS RENDER THE OUTERMOST BLOCK FIRST
			ScriptHandler.callFunction( sh,
					ScriptHandler.BEFORE_DRAW_BLOCK,
					bl,
					getRunTimeContext( ).getScriptContext( ) );
			getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
					bl );
			bge.updateBlock( bl );
			renderChartBlock( idr, bl, StructureSource.createChartBlock( bl ) );
			ScriptHandler.callFunction( sh,
					ScriptHandler.AFTER_DRAW_BLOCK,
					bl,
					getRunTimeContext( ).getScriptContext( ) );
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
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderPlot( idr, (Plot) bl );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
					if ( !bLastInSequence )
					{
						// STOP AT THE PLOT IF NOT ALSO THE LAST IN THE
						// SEQUENCE
						break;
					}
				}
				else if ( bl instanceof TitleBlock )
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderTitle( idr, (TitleBlock) bl );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
				}
				else if ( bl instanceof LabelBlock )
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderLabel( idr, bl, StructureSource.createUnknown( bl ) );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
				}
				else if ( bl instanceof Legend )
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderLegend( idr, (Legend) bl, htRenderers );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
				}
				else
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderBlock( idr, bl, StructureSource.createUnknown( bl ) );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
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
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderPlot( idr, (Plot) bl );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
				}
				else if ( bl instanceof TitleBlock )
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderTitle( idr, (TitleBlock) bl );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
				}
				else if ( bl instanceof LabelBlock )
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderLabel( idr, bl, StructureSource.createUnknown( bl ) );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
				}
				else if ( bl instanceof Legend )
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderLegend( idr, (Legend) bl, htRenderers );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
				}
				else
				{
					ScriptHandler.callFunction( sh,
							ScriptHandler.BEFORE_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
							bl );
					renderBlock( idr, bl, StructureSource.createUnknown( bl ) );
					ScriptHandler.callFunction( sh,
							ScriptHandler.AFTER_DRAW_BLOCK,
							bl,
							getRunTimeContext( ).getScriptContext( ) );
					getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
							bl );
				}
			}
		}
		else
		{
			// FOR ALL SERIES IN-BETWEEN, ONLY RENDER THE PLOT
			final BlockGenerationEvent bge = new BlockGenerationEvent( this );
			Plot p = cm.getPlot( );
			bge.updateBlock( p );
			ScriptHandler.callFunction( sh,
					ScriptHandler.BEFORE_DRAW_BLOCK,
					p,
					getRunTimeContext( ).getScriptContext( ) );
			getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
					p );
			renderPlot( idr, p );
			ScriptHandler.callFunction( sh,
					ScriptHandler.AFTER_DRAW_BLOCK,
					p,
					getRunTimeContext( ).getScriptContext( ) );
			getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
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
							},
							getRunTimeContext( ).getULocale( ) ) );
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
				return ( l1 < l1 ? IConstants.LESS : ( l1 == l2
						? IConstants.EQUAL : IConstants.MORE ) );

			}
			else if ( de1 instanceof TextDataElement )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.DATA_FORMAT,
						"exception.unsupported.compare.text", //$NON-NLS-1$ 
						Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
			}
			else
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.DATA_FORMAT,
						"exception.unsupported.compare.unknown.objects", //$NON-NLS-1$
						new Object[]{
								de1, de2
						},
						Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) ); // i18n_CONCATENATIONS_REMOVED
			}
		}
		throw new ChartException( ChartEnginePlugin.ID,
				ChartException.DATA_FORMAT,
				"exception.unsupported.compare.different.objects", //$NON-NLS-1$
				new Object[]{
						de1, de2
				},
				Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) ); // i18n_CONCATENATIONS_REMOVED
	}

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
					Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
		}

		boolean isTransposed = ( (ChartWithAxes) getModel( ) ).isTransposed( );

		if ( curve.getLineAttributes( ).isVisible( ) )
		{
			ScriptHandler.callFunction( getRunTimeContext( ).getScriptHandler( ),
					ScriptHandler.BEFORE_DRAW_FITTING_CURVE,
					curve,
					getRunTimeContext( ).getScriptContext( ) );
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

			FittingCalculator fc = new FittingCalculator( baseArray,
					orthogonalArray,
					0.33 );

			double[] fitYarray = fc.getFittedValue( );

			orthogonalArray = fitYarray;

			if ( isTransposed )
			{
				baseArray = fitYarray;
				orthogonalArray = yArray;

				sort( baseArray, orthogonalArray, false );
			}

			if ( curve.getLineAttributes( ).getColor( ) != null )
			{
				CurveRenderer crdr = new CurveRenderer( (ChartWithAxes) getModel( ),
						this,
						curve.getLineAttributes( ),
						LocationImpl.create( baseArray, orthogonalArray ),
						bShowAsTape,
						-1,
						bDeferred,
						false,
						null,
						false,
						true );
				crdr.draw( ipr );
			}

			// Render curve label.
			if ( curve.getLabel( ).isSetVisible( )
					&& curve.getLabel( ).isVisible( ) )
			{
				Label lb = LabelImpl.copyInstance( curve.getLabel( ) );

				// handle external resource string
				final String sPreviousValue = lb.getCaption( ).getValue( );
				lb.getCaption( )
						.setValue( getRunTimeContext( ).externalizedMessage( sPreviousValue ) );

				BoundingBox bb = Methods.computeBox( getXServer( ),
						IConstants.LEFT/* DONT-CARE */,
						lb,
						0,
						0 );

				Anchor lbAnchor = curve.getLabelAnchor( );

				if ( lbAnchor == null )
				{
					lbAnchor = Anchor.NORTH_LITERAL;
				}

				int horizontal = IConstants.CENTER;
				int vertical = IConstants.ABOVE;

				// convert anchor to position.
				switch ( lbAnchor.getValue( ) )
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

				switch ( lbAnchor.getValue( ) )
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

				double xs, ys;

				if ( isTransposed )
				{
					if ( horizontal == IConstants.LEFT )
					{
						ys = orthogonalArray[orthogonalArray.length - 1]
								- bb.getHeight( );
						// switch left/right
						horizontal = IConstants.RIGHT;
					}
					else if ( horizontal == IConstants.RIGHT )
					{
						ys = orthogonalArray[0];
						// switch left/right
						horizontal = IConstants.LEFT;
					}
					else
					{
						ys = orthogonalArray[0]
								+ ( orthogonalArray[orthogonalArray.length - 1] - orthogonalArray[0] )
								/ 2d - bb.getHeight( ) / 2d;
					}

					xs = getFitYPosition( orthogonalArray,
							baseArray,
							horizontal,
							bb.getHeight( ),
							bb.getWidth( ),
							vertical == IConstants.BELOW );
				}
				else
				{
					if ( horizontal == IConstants.LEFT )
					{
						xs = xArray[0];
					}
					else if ( horizontal == IConstants.RIGHT )
					{
						xs = xArray[xArray.length - 1] - bb.getWidth( );
					}
					else
					{
						xs = xArray[0]
								+ ( xArray[xArray.length - 1] - xArray[0] )
								/ 2d - bb.getWidth( ) / 2d;
					}

					ys = getFitYPosition( xArray,
							fitYarray,
							horizontal,
							bb.getWidth( ),
							bb.getHeight( ),
							vertical == IConstants.ABOVE );
				}

				bb.setLeft( xs );
				bb.setTop( ys );

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
						TextRenderEvent.RENDER_TEXT_IN_BLOCK,
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
					curve,
					getRunTimeContext( ).getScriptContext( ) );
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
	 */
	private double getFitYPosition( double[] xa, double[] ya, int align,
			double width, double height, boolean above )
	{
		int gap = 10;

		double rt = 0;

		if ( align == IConstants.LEFT )
		{
			rt = ya[0];
		}
		else if ( align == IConstants.RIGHT )
		{
			rt = ya[ya.length - 1];
		}
		else
		{
			if ( ya.length % 2 == 1 )
			{
				rt = ya[ya.length / 2];
			}
			else
			{
				int x = ya.length / 2;
				rt = ( ya[x] + ya[x - 1] ) / 2;
			}
		}

		return above ? ( rt - height - gap ) : ( rt + gap );
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
		double dOriginalAngle = 0;

		for ( int i = 0; i < iAxisCount; i++ )
		{
			ax = oaxa[i].getModelAxis( );
			iOrientation = ax.getOrientation( ).getValue( );
			if ( bTransposed ) // TOGGLE ORIENTATION
			{
				iOrientation = ( iOrientation == Orientation.HORIZONTAL )
						? Orientation.VERTICAL : Orientation.HORIZONTAL;
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
						mr,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_MARKER_RANGE,
						mr );

				deStart = mr.getStartValue( );
				deEnd = mr.getEndValue( );
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
					dMin = ( deStart == null )
							? ( ( iOrientation == Orientation.HORIZONTAL )
									? boPlotClientArea.getLeft( )
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
									getRunTimeContext( ).getULocale( ) ) );
					continue; // TRY NEXT MARKER RANGE
				}

				// COMPUTE THE END BOUND
				try
				{
					dMax = ( deEnd == null )
							? ( ( iOrientation == Orientation.HORIZONTAL )
									? boPlotClientArea.getLeft( )
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
									getRunTimeContext( ).getULocale( ) ) );
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

				la = LabelImpl.copyInstance( mr.getLabel( ) );
				if ( la.isVisible( ) )
				{
					if ( la.getCaption( ).getValue( ) != null
							&& !IConstants.UNDEFINED_STRING.equals( la.getCaption( )
									.getValue( ) )
							&& la.getCaption( ).getValue( ).length( ) > 0 )
					{
						la.getCaption( ).setValue( oaxa[i].getRunTimeContext( )
								.externalizedMessage( la.getCaption( )
										.getValue( ) ) );
					}
					else
					{
						try
						{
							sb.delete( 0, sb.length( ) );
							sb.append( Messages.getString( "prefix.marker.range.caption", //$NON-NLS-1$ 
									getRunTimeContext( ).getULocale( ) ) );
							sb.append( ValueFormatter.format( deStart,
									mr.getFormatSpecifier( ),
									oaxa[i].getRunTimeContext( ).getULocale( ),
									null ) );
							sb.append( Messages.getString( "separator.marker.range.caption", //$NON-NLS-1$ 
									getRunTimeContext( ).getULocale( ) ) );
							sb.append( ValueFormatter.format( deEnd,
									mr.getFormatSpecifier( ),
									oaxa[i].getRunTimeContext( ).getULocale( ),
									null ) );
							sb.append( Messages.getString( "suffix.marker.range.caption", //$NON-NLS-1$ 
									getRunTimeContext( ).getULocale( ) ) );
							la.getCaption( ).setValue( sb.toString( ) );
						}
						catch ( ChartException dfex )
						{
							throw new ChartException( ChartEnginePlugin.ID,
									ChartException.RENDERING,
									dfex );
						}
					}

					// DETERMINE THE LABEL ANCHOR (TRANSPOSE IF NEEDED)
					anc = switchAnchor( mr.getLabelAnchor( ) );
					if ( bTransposed )
					{
						dOriginalAngle = la.getCaption( )
								.getFont( )
								.getRotation( );
						try
						{
							la.getCaption( )
									.getFont( )
									.setRotation( pwa.getTransposedAngle( dOriginalAngle ) );
							anc = ChartUtil.transposeAnchor( anc );
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
					getDeferredCache( ).addLabel( tre );
				}

				if ( isInteractivityEnabled( ) )
				{
					Trigger tg;
					EList elTriggers = mr.getTriggers( );

					if ( !elTriggers.isEmpty( ) )
					{
						final InteractionEvent iev = (InteractionEvent) ( (EventObjectCache) idr ).getEventObject( StructureSource.createMarkerRange( mr ),
								InteractionEvent.class );
						for ( int t = 0; t < elTriggers.size( ); t++ )
						{
							tg = TriggerImpl.copyInstance( (Trigger) elTriggers.get( t ) );
							processTrigger( tg,
									StructureSource.createMarkerRange( mr ) );
							iev.addTrigger( tg );
						}

						iev.setHotSpot( rre );
						idr.enableInteraction( iev );
					}
				}

				ScriptHandler.callFunction( sh,
						ScriptHandler.AFTER_DRAW_MARKER_RANGE,
						ax,
						mr,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_MARKER_RANGE,
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

		if ( !isDimension3D( ) )
		{
			// render client area shadow
			if ( ca.getShadowColor( ) != null )
			{
				rre.setBounds( bo.translateInstance( 3, 3 ) );
				rre.setBackground( ca.getShadowColor( ) );
				ipr.fillRectangle( rre );
			}

			// render client area
			rre.setBounds( bo );
			rre.setOutline( ca.getOutline( ) );
			rre.setBackground( ca.getBackground( ) );
			ipr.fillRectangle( rre );
		}

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
		double dYStart = 0;
		double dZStart = 0;
		double dXEnd = 0;
		double dYEnd = 0;
		double dZEnd = 0;
		int baseTickCount = 0;
		int ancillaryTickCount = 0;
		int orthogonalTickCount = 0;
		double xStep = 0;
		double yStep = 0;
		double zStep = 0;
		// Location panningOffset = null;

		if ( isDimension3D( ) )
		{
			scPrimaryBase = aax.getPrimaryBase( ).getScale( );
			scPrimaryOrthogonal = aax.getPrimaryOrthogonal( ).getScale( );
			scAncillaryBase = aax.getAncillaryBase( ).getScale( );

			dXStart = scPrimaryBase.getStart( );
			dYStart = scPrimaryOrthogonal.getStart( );
			dZStart = scAncillaryBase.getStart( );

			dXEnd = scPrimaryBase.getEnd( );
			dYEnd = scPrimaryOrthogonal.getEnd( );
			dZEnd = scAncillaryBase.getEnd( );

			baseTickCount = scPrimaryBase.getTickCordinates( ).length;
			ancillaryTickCount = scAncillaryBase.getTickCordinates( ).length;
			orthogonalTickCount = scPrimaryOrthogonal.getTickCordinates( ).length;

			xStep = scPrimaryBase.getUnitSize( );
			yStep = scPrimaryOrthogonal.getUnitSize( );
			zStep = scAncillaryBase.getUnitSize( );

			// panningOffset = getPanningOffset( );
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
			if ( ( cwa.getWallFill( ) instanceof ColorDefinition && ( (ColorDefinition) cwa.getWallFill( ) ).getTransparency( ) > 0 )
					|| ( !( cwa.getWallFill( ) instanceof ColorDefinition ) && cwa.getWallFill( ) != null ) )
			{
				loa = new Location3D[4];

				// Left Wall
				loa[0] = Location3DImpl.create( dXStart, dYStart, dZStart );
				loa[1] = Location3DImpl.create( dXStart, dYEnd, dZStart );
				loa[2] = Location3DImpl.create( dXStart, dYEnd, dZEnd );
				loa[3] = Location3DImpl.create( dXStart, dYStart, dZEnd );
				pre.setPoints3D( loa );
				pre.setBackground( cwa.getWallFill( ) );
				pre.setDoubleSided( true );
				pre.setOutline( ca.getOutline( ) );
				getDeferredCache( ).addPlane( pre,
						PrimitiveRenderEvent.DRAW | PrimitiveRenderEvent.FILL );

				// // split to small planes to render.
				// for ( int i = 0; i < orthogonalTickCount - 1; i++ )
				// {
				// for ( int j = 0; j < ancillaryTickCount - 1; j++ )
				// {
				// loa[0] = Location3DImpl.create( dXStart, dYStart
				// + yStep
				// * i, dZStart + zStep * j );
				// loa[1] = Location3DImpl.create( dXStart, dYStart
				// + ( i + 1 )
				// * yStep, dZStart + j * zStep );
				// loa[2] = Location3DImpl.create( dXStart, dYStart
				// + ( i + 1 )
				// * yStep, dZStart + ( j + 1 ) * zStep );
				// loa[3] = Location3DImpl.create( dXStart, dYStart
				// + i
				// * yStep, dZStart + ( j + 1 ) * zStep );
				// pre.setPoints3D( loa );
				// pre.setBackground( cwa.getWallFill( ) );
				// pre.setOutline( ca.getOutline( ) );
				// getDeferredCache( ).addPlane( pre,
				// PrimitiveRenderEvent.DRAW
				// | PrimitiveRenderEvent.FILL );
				// }
				// }
				leftWallFill = true;

				// Right Wall
				loa[0] = Location3DImpl.create( dXStart, dYStart, dZStart );
				loa[1] = Location3DImpl.create( dXEnd, dYStart, dZStart );
				loa[2] = Location3DImpl.create( dXEnd, dYEnd, dZStart );
				loa[3] = Location3DImpl.create( dXStart, dYEnd, dZStart );
				pre.setPoints3D( loa );
				pre.setBackground( cwa.getWallFill( ) );
				pre.setDoubleSided( true );
				pre.setOutline( ca.getOutline( ) );
				getDeferredCache( ).addPlane( pre,
						PrimitiveRenderEvent.DRAW | PrimitiveRenderEvent.FILL );

				// // split to small planes to render.
				// for ( int i = 0; i < orthogonalTickCount - 1; i++ )
				// {
				// for ( int j = 0; j < baseTickCount - 1; j++ )
				// {
				// loa[0] = Location3DImpl.create( dXStart + j * xStep,
				// dYStart + i * yStep,
				// dZStart );
				// loa[1] = Location3DImpl.create( dXStart
				// + ( j + 1 )
				// * xStep, dYStart + i * yStep, dZStart );
				// loa[2] = Location3DImpl.create( dXStart
				// + ( j + 1 )
				// * xStep, dYStart + ( i + 1 ) * yStep, dZStart );
				// loa[3] = Location3DImpl.create( dXStart + j * xStep,
				// dYStart + ( i + 1 ) * yStep,
				// dZStart );
				// pre.setPoints3D( loa );
				// pre.setBackground( cwa.getWallFill( ) );
				// pre.setOutline( ca.getOutline( ) );
				// getDeferredCache( ).addPlane( pre,
				// PrimitiveRenderEvent.DRAW
				// | PrimitiveRenderEvent.FILL );
				// }
				// }
				rightWallFill = true;
			}

			// DRAW THE FLOOR
			if ( ( cwa.getFloorFill( ) instanceof ColorDefinition && ( (ColorDefinition) cwa.getFloorFill( ) ).getTransparency( ) > 0 )
					|| ( !( cwa.getFloorFill( ) instanceof ColorDefinition ) && cwa.getFloorFill( ) != null ) )
			{
				if ( loa == null )
				{
					loa = new Location3D[4];
				}

				loa[0] = Location3DImpl.create( dXStart, dYStart, dZStart );
				loa[1] = Location3DImpl.create( dXStart, dYStart, dZEnd );
				loa[2] = Location3DImpl.create( dXEnd, dYStart, dZEnd );
				loa[3] = Location3DImpl.create( dXEnd, dYStart, dZStart );
				pre.setPoints3D( loa );
				pre.setBackground( cwa.getFloorFill( ) );
				pre.setDoubleSided( true );
				pre.setOutline( ca.getOutline( ) );
				getDeferredCache( ).addPlane( pre,
						PrimitiveRenderEvent.DRAW | PrimitiveRenderEvent.FILL );

				// // split to small planes to render.
				// for ( int i = 0; i < baseTickCount - 1; i++ )
				// {
				// for ( int j = 0; j < ancillaryTickCount - 1; j++ )
				// {
				// loa[0] = Location3DImpl.create( dXStart + i * xStep,
				// dYStart,
				// dZStart + j * zStep );
				// loa[1] = Location3DImpl.create( dXStart + i * xStep,
				// dYStart,
				// dZStart + ( j + 1 ) * zStep );
				// loa[2] = Location3DImpl.create( dXStart
				// + ( i + 1 )
				// * xStep, dYStart, dZStart + ( j + 1 ) * zStep );
				// loa[3] = Location3DImpl.create( dXStart
				// + ( i + 1 )
				// * xStep, dYStart, dZStart + j * zStep );
				// pre.setPoints3D( loa );
				// pre.setBackground( cwa.getFloorFill( ) );
				// pre.setOutline( ca.getOutline( ) );
				// getDeferredCache( ).addPlane( pre,
				// PrimitiveRenderEvent.DRAW
				// | PrimitiveRenderEvent.FILL );
				// }
				// }
				floorFill = true;
			}
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
		double x = 0, y = 0, vnext = 0;
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
						Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
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

						double[] xa = scPrimaryBase.getTickCordinates( );
						if ( floorFill )
						{
							for ( int k = 0; k < xa.length - 1; k++ )
							{
								for ( int j = 0; j < doaMinor.length - 1; j++ )
								{
									for ( int n = 0; n < ancillaryTickCount - 1; n++ )
									{
										if ( ChartUtil.mathGE( xa[k]
												+ doaMinor[j], xa[k + 1] ) )
										{
											// if current minor tick exceeds the
											// range of current unit, skip
											continue;
										}

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
										if ( ChartUtil.mathGE( xa[k]
												+ doaMinor[j], xa[k + 1] ) )
										{
											// if current minor tick exceeds the
											// range of current unit, skip
											continue;
										}

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
						double[] ya = scPrimaryOrthogonal.getTickCordinates( );
						if ( leftWallFill )
						{
							for ( int k = 0; k < ya.length - 1; k++ )
							{
								for ( int j = 0; j < doaMinor.length - 1; j++ )
								{
									for ( int n = 0; n < ancillaryTickCount - 1; n++ )
									{
										if ( ChartUtil.mathGE( ya[k]
												+ doaMinor[j], ya[k + 1] ) )
										{
											// if current minor tick exceeds the
											// range of current unit, skip
											continue;
										}

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
										if ( ChartUtil.mathGE( ya[k]
												+ doaMinor[j], ya[k + 1] ) )
										{
											// if current minor tick exceeds the
											// range of current unit, skip
											continue;
										}

										lre3d.setStart3D( Location3DImpl.create( dXStart
												+ n * xStep,
												ya[k] + doaMinor[j],
												dZStart ) );
										lre3d.setEnd3D( Location3DImpl.create( dXStart
												+ ( n + 1 ) * xStep,
												ya[k] + doaMinor[j],
												dZStart ) );
										getDeferredCache( ).addLine( lre3d );
									}
								}
							}
						}
						break;
					case IConstants.ANCILLARY_AXIS :
						double[] za = scAncillaryBase.getTickCordinates( );
						if ( leftWallFill )
						{
							for ( int k = 0; k < za.length - 1; k++ )
							{
								for ( int j = 0; j < doaMinor.length - 1; j++ )
								{
									for ( int n = 0; n < orthogonalTickCount - 1; n++ )
									{
										if ( ChartUtil.mathGE( za[k]
												+ doaMinor[j], za[k + 1] ) )
										{
											// if current minor tick exceeds the
											// range of current unit, skip
											continue;
										}

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
										if ( ChartUtil.mathGE( za[k]
												+ doaMinor[j], za[k + 1] ) )
										{
											// if current minor tick exceeds the
											// range of current unit, skip
											continue;
										}

										lre3d.setStart3D( Location3DImpl.create( dXStart
												+ n * xStep,
												dYStart,
												za[k] + doaMinor[j] ) );
										lre3d.setEnd3D( Location3DImpl.create( dXStart
												+ ( n + 1 ) * xStep,
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
				int iDirection = sc.getDirection( ) == IConstants.BACKWARD ? -1
						: 1;
				double[] da = sc.getTickCordinates( );
				double dY2 = bo.getTop( ) + 1, dY1 = bo.getTop( )
						+ bo.getHeight( ) - 2;
				if ( pwa.getDimension( ) == IConstants.TWO_5_D )
				{
					for ( int j = 0; j < da.length - 1; j++ )
					{
						x = da[j];
						for ( int k = 0; k < doaMinor.length; k++ )
						{
							if ( ( iDirection == 1 && ChartUtil.mathGE( x
									+ doaMinor[k], da[j + 1] ) )
									|| ( iDirection == -1 && ChartUtil.mathLE( x
											- doaMinor[k],
											da[j + 1] ) ) )
							{
								// if current minor tick exceeds the
								// range of current unit, skip
								continue;
							}

							lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
									LineRenderEvent.class );
							lre.setLineAttributes( lia );
							lre.setStart( LocationImpl.create( x
									+ iDirection * doaMinor[k], dY1
									+ pwa.getSeriesThickness( ) ) );
							lre.setEnd( LocationImpl.create( x
									+ iDirection * doaMinor[k]
									+ pwa.getSeriesThickness( ), dY1 ) );
							ipr.drawLine( lre );
						}
					}
				}

				for ( int j = 0; j < da.length - 1; j++ )
				{
					x = da[j];
					vnext = da[j + 1];
					if ( pwa.getDimension( ) == IConstants.TWO_5_D )
					{
						x += pwa.getSeriesThickness( );
						vnext += pwa.getSeriesThickness( );
					}
					for ( int k = 0; k < doaMinor.length; k++ )
					{
						if ( ( iDirection == 1 && ChartUtil.mathGE( x
								+ doaMinor[k], vnext ) )
								|| ( iDirection == -1 && ChartUtil.mathLE( x
										- doaMinor[k], vnext ) ) )
						{
							// if current minor tick exceeds the
							// range of current unit, skip
							continue;
						}

						lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
								LineRenderEvent.class );
						lre.setLineAttributes( lia );
						lre.setStart( LocationImpl.create( x
								+ iDirection * doaMinor[k], dY1 ) );
						lre.setEnd( LocationImpl.create( x
								+ iDirection * doaMinor[k], dY2 ) );
						ipr.drawLine( lre );
					}
				}
			}
			else if ( oaxa[i].getOrientation( ) == IConstants.VERTICAL )
			{
				int iDirection = sc.getDirection( ) != IConstants.FORWARD ? -1
						: 1;
				double[] da = sc.getTickCordinates( );
				double dX1 = bo.getLeft( ) + 1, dX2 = bo.getLeft( )
						+ bo.getWidth( ) - 2;
				if ( pwa.getDimension( ) == IConstants.TWO_5_D )
				{
					for ( int j = 0; j < da.length - 1; j++ )
					{
						y = da[j] - pwa.getSeriesThickness( );
						vnext = da[j + 1] - pwa.getSeriesThickness( );
						for ( int k = 0; k < doaMinor.length; k++ )
						{
							if ( ( iDirection == 1 && ChartUtil.mathGE( y
									+ doaMinor[k], vnext ) )
									|| ( iDirection == -1 && ChartUtil.mathLE( y
											- doaMinor[k],
											vnext ) ) )
							{
								// if current minor tick exceeds the
								// range of current unit, skip
								continue;
							}

							lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
									LineRenderEvent.class );
							lre.setLineAttributes( lia );
							lre.setStart( LocationImpl.create( dX1, y
									+ iDirection * doaMinor[k] ) );
							lre.setEnd( LocationImpl.create( dX1
									- pwa.getSeriesThickness( ), y
									+ iDirection * doaMinor[k]
									+ pwa.getSeriesThickness( ) ) );
							ipr.drawLine( lre );
						}
					}
				}
				for ( int j = 0; j < da.length - 1; j++ )
				{
					y = da[j];
					vnext = da[j + 1];
					if ( pwa.getDimension( ) == IConstants.TWO_5_D )
					{
						y -= pwa.getSeriesThickness( );
						vnext -= pwa.getSeriesThickness( );
					}
					for ( int k = 0; k < doaMinor.length; k++ )
					{
						if ( ( iDirection == 1 && ChartUtil.mathGE( y
								+ doaMinor[k], vnext ) )
								|| ( iDirection == -1 && ChartUtil.mathLE( y
										- doaMinor[k], vnext ) ) )
						{
							// if current minor tick exceeds the
							// range of current unit, skip
							continue;
						}

						lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
								LineRenderEvent.class );
						lre.setLineAttributes( lia );
						lre.setStart( LocationImpl.create( dX1, y
								+ iDirection * doaMinor[k] ) );
						lre.setEnd( LocationImpl.create( dX2, y
								+ iDirection * doaMinor[k] ) );
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

						double[] xa = scPrimaryBase.getTickCordinates( );
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
						double[] ya = scPrimaryOrthogonal.getTickCordinates( );
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
											+ j * xStep,
											ya[k],
											dZStart ) );
									lre3d.setEnd3D( Location3DImpl.create( dXStart
											+ ( j + 1 ) * xStep,
											ya[k],
											dZStart ) );
									getDeferredCache( ).addLine( lre3d );
								}
							}
						}
						break;
					case IConstants.ANCILLARY_AXIS :
						double[] za = scAncillaryBase.getTickCordinates( );
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
											+ j * xStep,
											dYStart,
											za[k] ) );
									lre3d.setEnd3D( Location3DImpl.create( dXStart
											+ ( j + 1 ) * xStep,
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
						+ bo.getHeight( ) - 2;
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
						+ bo.getWidth( ) - 2;
				if ( pwa.getDimension( ) == IConstants.TWO_5_D )
				{
					for ( int j = 0; j < da.length; j++ )
					{
						if ( j == 0 && insCA.getLeft( ) < lia.getThickness( ) )
							continue;
						if ( j == da.length - 1
								&& insCA.getRight( ) < lia.getThickness( ) )
							continue;

						y = ( da[j] - pwa.getSeriesThickness( ) );
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

					y = da[j];
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

		if ( !isDimension3D( ) && p.getClientArea( ).getOutline( ).isVisible( ) )
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

		// final ClipRenderEvent cre = (ClipRenderEvent) ( (EventObjectCache)
		// getDevice( ) ).getEventObject( StructureSource.createPlot( p ),
		// ClipRenderEvent.class );

		final boolean bFirstInSequence = ( iSeriesIndex == 0 );
		final boolean bLastInSequence = ( iSeriesIndex == iSeriesCount - 1 );

		final PlotWithAxes pwa = (PlotWithAxes) getComputations( );

		if ( bFirstInSequence )
		{
			renderBackground( ipr, p );
			renderAxesStructure( ipr, p );
		}

		if ( getSeries( ) != null )
		{

			// try
			// {
			// Bounds boClipping = p.getBounds( )
			// .scaledInstance( getDevice( ).getDisplayServer( )
			// .getDpiResolution( ) / 72d );
			//
			// Location[] loaClipping = new Location[4];
			// loaClipping[0] = LocationImpl.create( boClipping.getLeft( ),
			// boClipping.getTop( ) );
			// loaClipping[1] = LocationImpl.create( boClipping.getLeft( )
			// + boClipping.getWidth( ), boClipping.getTop( ) );
			// loaClipping[2] = LocationImpl.create( boClipping.getLeft( )
			// + boClipping.getWidth( ), boClipping.getTop( )
			// + boClipping.getHeight( ) );
			// loaClipping[3] = LocationImpl.create( boClipping.getLeft( ),
			// boClipping.getTop( ) + boClipping.getHeight( ) );
			//
			// cre.setVertices( loaClipping );
			// getDevice( ).setClip( cre );

			ScriptHandler.callFunction( getRunTimeContext( ).getScriptHandler( ),
					ScriptHandler.BEFORE_DRAW_SERIES,
					getSeries( ),
					this,
					getRunTimeContext( ).getScriptContext( ) );
			getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_SERIES,
					getSeries( ) );
			// CALLS THE APPROPRIATE SUBCLASS FOR GRAPHIC ELEMENT RENDERING
			renderSeries( ipr, p, srh );
			ScriptHandler.callFunction( getRunTimeContext( ).getScriptHandler( ),
					ScriptHandler.AFTER_DRAW_SERIES,
					getSeries( ),
					this,
					getRunTimeContext( ).getScriptContext( ) );
			getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_SERIES,
					getSeries( ) );

			// render axes decoration for each series.
			renderAxesDecoration( ipr, srh );
		}

		if ( bLastInSequence )
		{
			final Location panningOffset = getPanningOffset( );

			try
			{
				if ( isDimension3D( ) )
				{
					getDeferredCache( ).process3DEvent( get3DEngine( ),
							panningOffset.getX( ),
							panningOffset.getY( ) );
				}
				getDeferredCache( ).flush( ); // FLUSH DEFERRED CACHE
			}
			catch ( ChartException ex )
			{
				// NOTE: RENDERING EXCEPTION ALREADY BEING THROWN
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.RENDERING,
						ex );
			}

			// SETUP AXIS ARRAY
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

			// // restore clipping.
			// cre.setVertices( null );
			// getDevice( ).setClip( cre );

			// RENDER AXIS LABELS LAST
			renderAxesLabels( ipr, p, oaxa );

			try
			{
				if ( isDimension3D( ) )
				{
					getDeferredCache( ).process3DEvent( get3DEngine( ),
							panningOffset.getX( ),
							panningOffset.getY( ) );
				}
				getDeferredCache( ).flush( ); // FLUSH DEFERRED CACHE
			}
			catch ( ChartException ex )
			{
				// NOTE: RENDERING EXCEPTION ALREADY BEING THROWN
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.RENDERING,
						ex );
			}

		}
		// }
		// finally
		// {
		// // restore clipping.
		// cre.setVertices( null );
		// getDevice( ).setClip( cre );
		// }
	}

	/**
	 * Render axes decoration by each series.
	 */
	protected void renderAxesDecoration( IPrimitiveRenderer ipr,
			ISeriesRenderingHints srh ) throws ChartException
	{
		final PlotWithAxes pwa = (PlotWithAxes) getComputations( );
		final AllAxes aax = pwa.getAxes( );

		if ( pwa.getDimension( ) == IConstants.THREE_D )
		{
			// not apply to 3d chart.
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
				IAxesDecorator iad = getAxesDecorator( oaxa[i] );

				if ( iad != null )
				{
					iad.decorateAxes( ipr, srh, oaxa[i] );
				}
			}
		}
	}

	/**
	 * Returns the decorator renderer associated with current series, default is
	 * none.
	 */
	public IAxesDecorator getAxesDecorator( OneAxis ax )
	{
		return null;
	}

	/**
	 * Convenient routine to render a marker
	 */
	protected final void renderMarker( Object oParent, IPrimitiveRenderer ipr,
			Marker m, Location lo, LineAttributes lia, Fill fPaletteEntry,
			DataPointHints dph, Integer markerSize, boolean bDeferred,
			boolean bConsiderTranspostion ) throws ChartException
	{
		if ( dph != null
				&& ( isNaN( dph.getOrthogonalValue( ) ) || dph.isOutside( ) ) )
		{
			return;
		}

		// Convert Fill for negative value
		if ( dph != null && dph.getOrthogonalValue( ) instanceof Double )
		{
			fPaletteEntry = ChartUtil.convertFill( fPaletteEntry,
					( (Double) dph.getOrthogonalValue( ) ).doubleValue( ),
					null );
		}

		Series se = getSeries( );

		Object oSource = ( oParent instanceof Legend )
				? ( StructureSource.createLegend( (Legend) oParent ) )
				: ( WrappedStructureSource.createSeriesDataPoint( se, dph ) );
		boolean bTransposed = bConsiderTranspostion
				&& ( (ChartWithAxes) getModel( ) ).isTransposed( );
		final Location panningOffset = this.getPanningOffset( );
		PrimitiveRenderEvent preCopy = null;

		if ( m == null
				|| ( m != null && !( m.isSetVisible( ) && m.isVisible( ) ) ) )
		{
			int iSize = 5;
			if ( m != null )
			{
				iSize = m.getSize( );
			}

			// prepare hotspot only
			if ( lo instanceof Location3D )
			{
				final Oval3DRenderEvent ore = (Oval3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( oSource,
						Oval3DRenderEvent.class );
				Location3D lo3d = (Location3D) lo;
				ore.setLocation3D( new Location3D[]{
						Location3DImpl.create( lo3d.getX( ) - iSize,
								lo3d.getY( ) + iSize,
								lo3d.getZ( ) ),
						Location3DImpl.create( lo3d.getX( ) - iSize,
								lo3d.getY( ) - iSize,
								lo3d.getZ( ) ),
						Location3DImpl.create( lo3d.getX( ) + iSize,
								lo3d.getY( ) - iSize,
								lo3d.getZ( ) ),
						Location3DImpl.create( lo3d.getX( ) + iSize,
								lo3d.getY( ) + iSize,
								lo3d.getZ( ) )
				} );
				preCopy = ore.copy( );
			}
			else
			{
				final OvalRenderEvent ore = (OvalRenderEvent) ( (EventObjectCache) ipr ).getEventObject( oSource,
						OvalRenderEvent.class );
				ore.setBounds( BoundsImpl.create( lo.getX( ) - iSize, lo.getY( )
						- iSize, iSize * 2, iSize * 2 ) );
				preCopy = ore.copy( );
			}
		}
		else if ( m.isSetVisible( ) && m.isVisible( ) )
		{
			final MarkerRenderer mr = new MarkerRenderer( this.getDevice( ),
					oSource,
					lo,
					lia,
					fPaletteEntry,
					m,
					markerSize,
					this.getDeferredCache( ),
					bDeferred,
					bTransposed );
			mr.draw( ipr );
			preCopy = mr.getRenderArea( );
		}

		if ( this.isInteractivityEnabled( ) && dph != null )
		{
			if ( !( lo instanceof Location3D )
					|| ( ( lo instanceof Location3D ) && ( this.get3DEngine( )
							.processEvent( preCopy,
									panningOffset.getX( ),
									panningOffset.getY( ) ) != null ) ) )
			{
				final EList elTriggers = se.getTriggers( );
				if ( !elTriggers.isEmpty( ) )
				{
					final StructureSource iSource = ( oParent instanceof Legend )
							? ( StructureSource.createSeries( se ) )
							: ( WrappedStructureSource.createSeriesDataPoint( se,
									dph ) );
					final InteractionEvent iev = (InteractionEvent) ( (EventObjectCache) ipr ).getEventObject( iSource,
							InteractionEvent.class );
					Trigger tg;
					for ( int t = 0; t < elTriggers.size( ); t++ )
					{
						tg = TriggerImpl.copyInstance( (Trigger) elTriggers.get( t ) );
						this.processTrigger( tg, iSource );
						iev.addTrigger( tg );
					}
					iev.setHotSpot( preCopy );
					ipr.enableInteraction( iev );
				}
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
				iOrientation = ( iOrientation == Orientation.HORIZONTAL )
						? Orientation.VERTICAL : Orientation.HORIZONTAL;
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
						ml,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_MARKER_LINE,
						ml );

				deValue = ml.getValue( );
				if ( deValue == null )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.RENDERING,
							"exception.marker.line.null.value", //$NON-NLS-1$
							new Object[]{
								ml
							},
							Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
				}

				// UPDATE THE LABEL CONTENT ASSOCIATED WITH THE MARKER LINE
				la = LabelImpl.copyInstance( ml.getLabel( ) );

				if ( la.getCaption( ).getValue( ) != null
						&& !IConstants.UNDEFINED_STRING.equals( la.getCaption( )
								.getValue( ) )
						&& la.getCaption( ).getValue( ).length( ) > 0 )
				{
					la.getCaption( )
							.setValue( oaxa[i].getRunTimeContext( )
									.externalizedMessage( la.getCaption( )
											.getValue( ) ) );
				}
				else
				{
					try
					{
						la.getCaption( )
								.setValue( ValueFormatter.format( deValue,
										ml.getFormatSpecifier( ),
										oaxa[i].getRunTimeContext( )
												.getULocale( ),
										null ) );
					}
					catch ( ChartException dfex )
					{
						throw new ChartException( ChartEnginePlugin.ID,
								ChartException.RENDERING,
								dfex );
					}
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
									getRunTimeContext( ).getULocale( ) ) );
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
					if ( iOrientation == Orientation.HORIZONTAL )
					{
						loStart.translate( 0, pwa.getSeriesThickness( ) );
						loEnd.translate( 0, pwa.getSeriesThickness( ) );
					}
					else
					{
						loStart.translate( -pwa.getSeriesThickness( ), 0 );
						loEnd.translate( -pwa.getSeriesThickness( ), 0 );
					}
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
					anc = switchAnchor( ml.getLabelAnchor( ) );
					if ( bTransposed )
					{
						// la = ml.getLabel( );
						dOriginalAngle = la.getCaption( )
								.getFont( )
								.getRotation( );
						try
						{
							la.getCaption( )
									.getFont( )
									.setRotation( pwa.getTransposedAngle( dOriginalAngle ) );
							anc = ChartUtil.transposeAnchor( anc );
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
					getDeferredCache( ).addLabel( tre );
				}

				if ( isInteractivityEnabled( ) )
				{
					Trigger tg;
					EList elTriggers = ml.getTriggers( );

					if ( !elTriggers.isEmpty( ) )
					{
						final InteractionEvent iev = (InteractionEvent) ( (EventObjectCache) idr ).getEventObject( StructureSource.createMarkerLine( ml ),
								InteractionEvent.class );
						for ( int t = 0; t < elTriggers.size( ); t++ )
						{
							tg = TriggerImpl.copyInstance( (Trigger) elTriggers.get( t ) );
							processTrigger( tg,
									StructureSource.createMarkerLine( ml ) );
							iev.addTrigger( tg );
						}

						Location[] loaHotspot = new Location[4];

						if ( iOrientation == Orientation.HORIZONTAL )
						{
							loaHotspot[0] = LocationImpl.create( loStart.getX( )
									- IConstants.LINE_EXPAND_SIZE,
									loStart.getY( ) );
							loaHotspot[1] = LocationImpl.create( loStart.getX( )
									+ IConstants.LINE_EXPAND_SIZE,
									loStart.getY( ) );
							loaHotspot[2] = LocationImpl.create( loEnd.getX( )
									+ IConstants.LINE_EXPAND_SIZE, loEnd.getY( ) );
							loaHotspot[3] = LocationImpl.create( loEnd.getX( )
									- IConstants.LINE_EXPAND_SIZE, loEnd.getY( ) );
						}
						else
						{
							loaHotspot[0] = LocationImpl.create( loStart.getX( ),
									loStart.getY( )
											- IConstants.LINE_EXPAND_SIZE );
							loaHotspot[1] = LocationImpl.create( loEnd.getX( ),
									loEnd.getY( ) - IConstants.LINE_EXPAND_SIZE );
							loaHotspot[2] = LocationImpl.create( loEnd.getX( ),
									loEnd.getY( ) + IConstants.LINE_EXPAND_SIZE );
							loaHotspot[3] = LocationImpl.create( loStart.getX( ),
									loStart.getY( )
											+ IConstants.LINE_EXPAND_SIZE );
						}

						final PolygonRenderEvent pre = (PolygonRenderEvent) ( (EventObjectCache) idr ).getEventObject( StructureSource.createMarkerLine( ml ),
								PolygonRenderEvent.class );
						pre.setPoints( loaHotspot );
						iev.setHotSpot( pre );
						idr.enableInteraction( iev );
					}
				}

				ScriptHandler.callFunction( sh,
						ScriptHandler.AFTER_DRAW_MARKER_LINE,
						ax,
						ml,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_MARKER_LINE,
						ml );
			}
		}
	}

	/**
	 * Renders the axis.
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
		AxesRenderHelper.getInstance( this, ipr, pl, ax, iWhatToDraw )
				.renderEachAxis( );
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
		super.set( _cm, _o, _se, _sd );
		ax = _ax; // HOLD AXIS HERE
	}

	/**
	 * Returns if its a 3D rendering.
	 * 
	 */
	public final boolean isDimension3D( )
	{
		return ( getModel( ).getDimension( ) == ChartDimension.THREE_DIMENSIONAL_LITERAL );
	}

	/**
	 * Returns if current chart is transposed.
	 */
	public final boolean isTransposed( )
	{
		return ( (ChartWithAxes) getModel( ) ).isTransposed( );
	}

	/**
	 * Returns previous visible series index by given index.
	 * 
	 * @param currentIndex
	 * @return
	 */
	protected int getPrevVisibleSiblingSeriesIndex( int currentIndex )
	{
		SeriesDefinition sd = null;

		Series se = getSeries( );

		if ( se.eContainer( ) instanceof SeriesDefinition )
		{
			sd = (SeriesDefinition) se.eContainer( );
		}

		if ( sd != null )
		{
			int count = 0;
			int idx = sd.getRunTimeSeries( ).indexOf( se );
			if ( idx > 0 )
			{
				for ( int i = idx - 1; i >= 0; i-- )
				{
					count++;
					if ( ( (Series) sd.getRunTimeSeries( ).get( i ) ).isVisible( ) )
					{
						return currentIndex - count;
					}
				}
			}

			Axis cax = getAxis( );

			int iDefintionIndex = cax.getSeriesDefinitions( ).indexOf( sd );
			int iDefinitionCount = cax.getSeriesDefinitions( ).size( );

			if ( iDefinitionCount > 0 )
			{
				for ( int i = iDefintionIndex - 1; i >= 0; i-- )
				{
					sd = (SeriesDefinition) cax.getSeriesDefinitions( ).get( i );

					int runtimeSeriesCount = sd.getRunTimeSeries( ).size( );

					for ( int j = runtimeSeriesCount - 1; j >= 0; j-- )
					{
						count++;
						if ( ( (Series) sd.getRunTimeSeries( ).get( j ) ).isVisible( ) )
						{
							return currentIndex - count;
						}
					}
				}
			}
		}

		return -1;

	}

	/**
	 * @return Returns if current rendering is the last series in associated
	 *         axis.
	 */
	public final boolean isLastRuntimeSeriesInAxis( )
	{
		SeriesDefinition sd = null;

		Series se = getSeries( );

		if ( se.eContainer( ) instanceof SeriesDefinition )
		{
			sd = (SeriesDefinition) se.eContainer( );
		}

		if ( sd != null )
		{
			Axis cax = getAxis( );

			int iDefintionIndex = cax.getSeriesDefinitions( ).indexOf( sd );
			int iDefinitionCount = cax.getSeriesDefinitions( ).size( );

			if ( iDefinitionCount > 0
					&& iDefintionIndex == iDefinitionCount - 1 )
			{
				int iThisSeriesIndex = sd.getRunTimeSeries( ).indexOf( se );
				int iSeriesCount = sd.getRunTimeSeries( ).size( );

				if ( iSeriesCount > 0 && iThisSeriesIndex == iSeriesCount - 1 )
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Returns the 3D engine for this render.
	 */
	protected Engine3D get3DEngine( )
	{
		if ( isDimension3D( ) )
		{
			// delegate to 3d compuations.
			return ( (PlotWith3DAxes) oComputations ).get3DEngine( );
		}

		return null;
	}

	/**
	 * Returns the panning offset for 3D engine.
	 */
	protected Location getPanningOffset( )
	{
		if ( isDimension3D( ) )
		{
			// delegate to 3d compuations.
			return ( (PlotWith3DAxes) oComputations ).getPanningOffset( );
		}

		return null;
	}

	/**
	 * Gets current model Axis
	 * 
	 * @return Returns the axis associated with current renderer.
	 */
	public final Axis getAxis( )
	{
		return ax;
	}

	/**
	 * Gets current internal OneAxis
	 * 
	 * @return internal OneAxis
	 */
	protected final OneAxis getAxisInternal( )
	{
		final AllAxes allAxes = ( (PlotWithAxes) getComputations( ) ).getAxes( );
		if ( allAxes.getOverlayCount( ) == 0 )
		{
			return allAxes.getPrimaryOrthogonal( );
		}
		EList axesList = ( (Axis) getAxis( ).eContainer( ) ).getAssociatedAxes( );
		int index = axesList.indexOf( getAxis( ) );
		if ( index == 0 )
		{
			return allAxes.getPrimaryOrthogonal( );
		}
		return allAxes.getOverlay( index - 1 );
	}
	
	/**
	 * Checks out-of-range of each data point. If outside data is visible,
	 * adjust the coordinates; otherwise, clip the plot area. Note that
	 * coordinates array may be modified.
	 * 
	 * @param ipr
	 *            renderer
	 * @param srh
	 *            SeriesRenderingHints
	 * @param faX
	 *            X coordinates
	 * @param faY
	 *            Y coordinates
	 * @param bShowAsTape
	 *            indicates if it's 2d+ chart
	 */
	protected final void handleOutsideDataPoints( final IPrimitiveRenderer ipr,
			final SeriesRenderingHints srh, final double[] faX,
			final double[] faY, final boolean bShowAsTape )
	{
		final AutoScale scale = getAxisInternal( ).getScale( );
		if ( ( scale.getType( ) & IConstants.PERCENT ) == IConstants.PERCENT )
		{
			// Always inside in percent type
			return;
		}
		
		final boolean bHideOutside = !getAxis( ).getScale( ).isShowOutside( );
		final DataPointHints[] dpha = srh.getDataPoints( );
		final boolean isCategory = srh.isCategoryScale( );
		final Bounds boClientArea = srh.getClientAreaBounds( true );
		// Adjust the position in 2d+
		if ( bShowAsTape )
		{
			final double dSeriesThickness = srh.getSeriesThickness( );
			boClientArea.delta( -dSeriesThickness, dSeriesThickness, 0, 0 );
		}
		
		renderClipping( ipr, boClientArea );
		
		for ( int i = 0; i < dpha.length; i++ )
		{
			// Skip out-of-X-range data when non-category scale
			if ( !isCategory && dpha[i].getBaseValue( ) == null )
			{
				dpha[i].markOutside( );
				continue;
			}

			// 0 inside, 1 left outside, 2 right outside
			int iOutside = 0;
			
			if ( dpha[i].getStackOrthogonalValue( ) != null )
			{
				// Stack value
				double value = dpha[i].getStackOrthogonalValue( ).doubleValue( );
				double min = Methods.asDouble( scale.getMinimum( ) )
						.doubleValue( );
				double max = Methods.asDouble( scale.getMaximum( ) )
						.doubleValue( );
				if ( value < min )
				{
					iOutside = 1;
				}
				else if ( value > max )
				{
					iOutside = 2;
				}
			}
			else if ( dpha[i].getOrthogonalValue( ) == null )
			{
				// Null entry displays in the base line
				iOutside = 1;
			}
			else if ( dpha[i].getOrthogonalValue( ) instanceof Double )
			{
				// Double entry
				double value = ( (Double) dpha[i].getOrthogonalValue( ) ).doubleValue( );
				double min = Methods.asDouble( scale.getMinimum( ) )
						.doubleValue( );
				double max = Methods.asDouble( scale.getMaximum( ) )
						.doubleValue( );
				if ( value < min )
				{
					iOutside = 1;
				}
				else if ( value > max )
				{
					iOutside = 2;
				}
			}
			else if ( dpha[i].getOrthogonalValue( ) instanceof CDateTime )
			{
				// Datetime entry
				CDateTime value = (CDateTime) dpha[i].getOrthogonalValue( );
				CDateTime min = Methods.asDateTime( scale.getMinimum( ) );
				CDateTime max = Methods.asDateTime( scale.getMaximum( ) );
				if ( value.before( min ) )
				{
					iOutside = 1;
				}
				else if ( value.after( max ) )
				{
					iOutside = 2;
				}
			}
			else
			{
				// Complex entry
				iOutside = checkEntryInRange( dpha[i].getOrthogonalValue( ),
						scale.getMinimum( ),
						scale.getMaximum( ) );
			}

			if ( iOutside > 0 )
			{				
				if ( bHideOutside )
				{
					dpha[i].markOutside( );
				}
				else
				{
					if ( isTransposed( ) )
					{
						faX[i] = iOutside == 1 ? boClientArea.getLeft( )
								: boClientArea.getLeft( )
										+ boClientArea.getWidth( );
					}
					else
					{
						faY[i] = iOutside == 1 ? boClientArea.getTop( )
								+ boClientArea.getHeight( )
								: boClientArea.getTop( );
					}
				}
			}
		}
	}
	
	/**
	 * Clips the renderer. Need to restore the clipping after the use.
	 * 
	 * @param ipr
	 * @param boClientArea
	 */
	protected final void renderClipping( final IPrimitiveRenderer ipr,
			final Bounds boClientArea )
	{
		if ( !getAxis( ).getScale( ).isShowOutside( ) )
		{
			ClipRenderEvent clip = new ClipRenderEvent( this );
			Location[] locations = new Location[4];
			locations[0] = LocationImpl.create( boClientArea.getLeft( ),
					boClientArea.getTop( ) );
			locations[1] = LocationImpl.create( boClientArea.getLeft( ),
					boClientArea.getTop( ) + boClientArea.getHeight( ) );
			locations[2] = LocationImpl.create( boClientArea.getLeft( )
					+ boClientArea.getWidth( ), boClientArea.getTop( )
					+ boClientArea.getHeight( ) );
			locations[3] = LocationImpl.create( boClientArea.getLeft( )
					+ boClientArea.getWidth( ), boClientArea.getTop( ) );
			clip.setVertices( locations );
			ipr.setClip( clip );
		}
	}

	/**
	 * Restores the clipping
	 * 
	 * @param ipr
	 * @throws ChartException
	 */
	protected void restoreClipping( final IPrimitiveRenderer ipr )
			throws ChartException
	{
		if ( !getAxis( ).getScale( ).isShowOutside( ) )
		{
			getDeferredCache( ).flushPlaneAndLine( );
			ClipRenderEvent clip = new ClipRenderEvent( this );
			clip.setVertices( null );
			ipr.setClip( clip );
		}
	}
	
	/**
	 * Checks if the data point entry is in the range of plot area. Usually this
	 * method is overriden for complex entry. Default result is 0, inside.
	 * 
	 * @param entry
	 *            data point entry
	 * @param min
	 *            scale min
	 * @param max
	 *            scale max
	 * @return int indicates if data point entry is in the range of plot area. 0
	 *         inside, 1 left side, 2 outside
	 */
	protected int checkEntryInRange( Object entry, Object min, Object max )
	{
		return 0;
	}
	
}
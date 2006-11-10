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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Map;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.Engine3D;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.computation.withaxes.AllAxes;
import org.eclipse.birt.chart.computation.withaxes.AutoScale;
import org.eclipse.birt.chart.computation.withaxes.Grid;
import org.eclipse.birt.chart.computation.withaxes.IntersectionValue;
import org.eclipse.birt.chart.computation.withaxes.OneAxis;
import org.eclipse.birt.chart.computation.withaxes.PlotWith2DAxes;
import org.eclipse.birt.chart.computation.withaxes.PlotWith3DAxes;
import org.eclipse.birt.chart.computation.withaxes.PlotWithAxes;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.event.BlockGenerationEvent;
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
import org.eclipse.birt.chart.event.Text3DRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.TransformationEvent;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.internal.factory.DateFormatWrapperFactory;
import org.eclipse.birt.chart.internal.factory.IDateFormatWrapper;
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
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
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
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataElement;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
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

import com.ibm.icu.text.DecimalFormat;

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
				return ( l1 < l1 ? IConstants.LESS
						: ( l1 == l2 ? IConstants.EQUAL : IConstants.MORE ) );

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
								/ 2d
								- bb.getHeight( )
								/ 2d;
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
								/ 2d
								- bb.getWidth( )
								/ 2d;
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
									getRunTimeContext( ).getULocale( ) ) );
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
		//Location panningOffset = null;

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

			//panningOffset = getPanningOffset( );
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
				int iDirection = sc.getDirection( ) == IConstants.BACKWARD ? -1
						: 1;
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
									+ iDirection
									* doaMinor[k], dY1
									+ pwa.getSeriesThickness( ) ) );
							lre.setEnd( LocationImpl.create( x
									+ iDirection
									* doaMinor[k]
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
								+ iDirection
								* doaMinor[k], dY1 ) );
						lre.setEnd( LocationImpl.create( x
								+ iDirection
								* doaMinor[k], dY2 ) );
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
						+ bo.getWidth( )
						- 2;
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
									+ iDirection
									* doaMinor[k] ) );
							lre.setEnd( LocationImpl.create( dX1
									- pwa.getSeriesThickness( ), y
									+ iDirection
									* doaMinor[k]
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
								+ iDirection
								* doaMinor[k] ) );
						lre.setEnd( LocationImpl.create( dX2, y
								+ iDirection
								* doaMinor[k] ) );
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
	 * Convenient routine to render a marker
	 */
	protected final void renderMarker( Object oParent, IPrimitiveRenderer ipr,
			Marker m, Location lo, LineAttributes lia, Fill fPaletteEntry,
			DataPointHints dph, int markerSize, boolean bDeferred,
			boolean bConsiderTranspostion ) throws ChartException
	{
		if ( dph != null && isNaN( dph.getOrthogonalValue( ) ) )
		{
			return;
		}
		
		Series se = getSeries( );

		Object oSource = ( oParent instanceof Legend ) ? ( StructureSource.createLegend( (Legend) oParent ) )
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
					final StructureSource iSource = ( oParent instanceof Legend ) ? ( StructureSource.createSeries( se ) )
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
						or = ax.getOrientation( ) == Orientation.HORIZONTAL_LITERAL ? Orientation.VERTICAL_LITERAL
								: Orientation.HORIZONTAL_LITERAL;
						// la = ml.getLabel( );
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
		final RunTimeContext rtc = getRunTimeContext( );
		final Axis axModel = ax.getModelAxis( );
		final PlotWithAxes pwa = (PlotWithAxes) getComputations( );
		final Insets insCA = pwa.getAxes( ).getInsets( );
		final ScriptHandler sh = getRunTimeContext( ).getScriptHandler( );
		final double dLocation = ax.getAxisCoordinate( );
		final AutoScale sc = ax.getScale( );
		final IntersectionValue iv = ax.getIntersectionValue( );
		final int iMajorTickStyle = ax.getGrid( )
				.getTickStyle( IConstants.MAJOR );
		final int iMinorTickStyle = ax.getGrid( )
				.getTickStyle( IConstants.MINOR );
		final int iLabelLocation = ax.getLabelPosition( );
		final int iOrientation = ax.getOrientation( );
		final IDisplayServer xs = this.getDevice( ).getDisplayServer( );
		Label la = LabelImpl.copyInstance( ax.getLabel( ) );

		final double[] daEndPoints = sc.getEndPoints( );
		final double[] da = sc.getTickCordinates( );
		final double[] daMinor = sc.getMinorCoordinates( ax.getGrid( )
				.getMinorCountPerMajor( ) );
		String sText = null;

		final int iDimension = pwa.getDimension( );
		final double dSeriesThickness = pwa.getSeriesThickness( );
		final NumberDataElement nde = NumberDataElementImpl.create( 0 );
		final FormatSpecifier fs = ax.getModelAxis( ).getFormatSpecifier( );
		final double dStaggeredLabelOffset = sc.computeStaggeredAxisLabelOffset( xs,
				la,
				iOrientation );
		final boolean bAxisLabelStaggered = sc.isAxisLabelStaggered( );

		DecimalFormat df = null;

		final LineAttributes lia = ax.getLineAttributes( );
		final LineAttributes liaMajorTick = ax.getGrid( )
				.getTickAttributes( IConstants.MAJOR );
		final LineAttributes liaMinorTick = ax.getGrid( )
				.getTickAttributes( IConstants.MINOR );

		if ( !lia.isSetVisible( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.RENDERING,
					"exception.unset.axis.visibility", //$NON-NLS-1$
					Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
		}
		final boolean bRenderAxisLabels = ( ( iWhatToDraw & IConstants.LABELS ) == IConstants.LABELS && la.isVisible( ) );
		final boolean bRenderAxisTitle = ( ( iWhatToDraw & IConstants.LABELS ) == IConstants.LABELS );
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
		final Location panningOffset = getPanningOffset( );
		final boolean bTransposed = ( (ChartWithAxes) getModel( ) ).isTransposed( );

		double[] daEndPoints3D = null;
		double[] da3D = null;
		Location3D lo3d = null;
		Text3DRenderEvent t3dre = null;
		Line3DRenderEvent l3dre = null;

		double dXStart = 0;
		double dXEnd = 0;
		double dZStart = 0;
		double dZEnd = 0;

		if ( iDimension == IConstants.THREE_D )
		{
			AllAxes aax = pwa.getAxes( );
			dXEnd = aax.getPrimaryBase( ).getScale( ).getEnd( );
			dZEnd = aax.getAncillaryBase( ).getScale( ).getEnd( );
			dXStart = aax.getPrimaryBase( ).getScale( ).getStart( );
			dZStart = aax.getAncillaryBase( ).getScale( ).getStart( );

			daEndPoints3D = sc.getEndPoints( );
			da3D = sc.getTickCordinates( );

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
					l3dre.setStart3D( dX, dStart, dZEnd );
					l3dre.setEnd3D( dX, dEnd, dZEnd );
					dc.addLine( l3dre );

					// right
					l3dre.setStart3D( dXEnd, dStart, dZ );
					l3dre.setEnd3D( dXEnd, dEnd, dZ );
					dc.addLine( l3dre );

					if ( isInteractivityEnabled( ) )
					{
						Trigger tg;
						EList elTriggers = axModel.getTriggers( );

						if ( !elTriggers.isEmpty( ) )
						{
							ArrayList cachedTriggers = null;
							Location3D[] loaHotspot = new Location3D[4];
							Polygon3DRenderEvent pre3d = (Polygon3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
									Polygon3DRenderEvent.class );

							// process center y-axis.
							loaHotspot[0] = Location3DImpl.create( dX
									- IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dStart,
									dZ + IConstants.LINE_EXPAND_DOUBLE_SIZE );
							loaHotspot[1] = Location3DImpl.create( dX
									+ IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dStart,
									dZ - IConstants.LINE_EXPAND_DOUBLE_SIZE );
							loaHotspot[2] = Location3DImpl.create( dX
									+ IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dEnd,
									dZ - IConstants.LINE_EXPAND_DOUBLE_SIZE );
							loaHotspot[3] = Location3DImpl.create( dX
									- IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dEnd,
									dZ + IConstants.LINE_EXPAND_DOUBLE_SIZE );
							pre3d.setPoints3D( loaHotspot );
							pre3d.setDoubleSided( true );

							if ( get3DEngine( ).processEvent( pre3d,
									panningOffset.getX( ),
									panningOffset.getY( ) ) != null )
							{
								final InteractionEvent iev = (InteractionEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
										InteractionEvent.class );
								cachedTriggers = new ArrayList( );
								for ( int t = 0; t < elTriggers.size( ); t++ )
								{
									tg = TriggerImpl.copyInstance( (Trigger) elTriggers.get( t ) );
									processTrigger( tg,
											StructureSource.createAxis( axModel ) );
									cachedTriggers.add( tg );
									iev.addTrigger( TriggerImpl.copyInstance( tg ) );
								}

								iev.setHotSpot( pre3d );
								ipr.enableInteraction( iev );
							}

							// process left y-axis.
							pre3d = (Polygon3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
									Polygon3DRenderEvent.class );
							loaHotspot = new Location3D[4];

							loaHotspot[0] = Location3DImpl.create( dXStart
									- IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dStart,
									dZEnd + IConstants.LINE_EXPAND_DOUBLE_SIZE );
							loaHotspot[1] = Location3DImpl.create( dXStart
									+ IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dStart,
									dZEnd - IConstants.LINE_EXPAND_DOUBLE_SIZE );
							loaHotspot[2] = Location3DImpl.create( dXStart
									+ IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dEnd,
									dZEnd - IConstants.LINE_EXPAND_DOUBLE_SIZE );
							loaHotspot[3] = Location3DImpl.create( dXStart
									- IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dEnd,
									dZEnd + IConstants.LINE_EXPAND_DOUBLE_SIZE );
							pre3d.setPoints3D( loaHotspot );
							pre3d.setDoubleSided( true );

							if ( get3DEngine( ).processEvent( pre3d,
									panningOffset.getX( ),
									panningOffset.getY( ) ) != null )
							{
								final InteractionEvent iev = (InteractionEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
										InteractionEvent.class );

								if ( cachedTriggers == null )
								{
									cachedTriggers = new ArrayList( );
									for ( int t = 0; t < elTriggers.size( ); t++ )
									{
										tg = TriggerImpl.copyInstance( (Trigger) elTriggers.get( t ) );
										processTrigger( tg,
												StructureSource.createAxis( axModel ) );
										cachedTriggers.add( tg );
										iev.addTrigger( TriggerImpl.copyInstance( tg ) );
									}

								}
								else
								{
									for ( int t = 0; t < cachedTriggers.size( ); t++ )
									{
										iev.addTrigger( TriggerImpl.copyInstance( (Trigger) cachedTriggers.get( t ) ) );
									}
								}

								iev.setHotSpot( pre3d );
								ipr.enableInteraction( iev );
							}

							// process right y-axis.
							pre3d = (Polygon3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
									Polygon3DRenderEvent.class );
							loaHotspot = new Location3D[4];

							loaHotspot[0] = Location3DImpl.create( dXEnd
									- IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dStart,
									dZStart
											+ IConstants.LINE_EXPAND_DOUBLE_SIZE );
							loaHotspot[1] = Location3DImpl.create( dXEnd
									+ IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dStart,
									dZStart
											- IConstants.LINE_EXPAND_DOUBLE_SIZE );
							loaHotspot[2] = Location3DImpl.create( dXEnd
									+ IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dEnd,
									dZStart
											- IConstants.LINE_EXPAND_DOUBLE_SIZE );
							loaHotspot[3] = Location3DImpl.create( dXEnd
									- IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dEnd,
									dZStart
											+ IConstants.LINE_EXPAND_DOUBLE_SIZE );
							pre3d.setPoints3D( loaHotspot );
							pre3d.setDoubleSided( true );

							if ( get3DEngine( ).processEvent( pre3d,
									panningOffset.getX( ),
									panningOffset.getY( ) ) != null )
							{
								final InteractionEvent iev = (InteractionEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
										InteractionEvent.class );

								if ( cachedTriggers == null )
								{
									for ( int t = 0; t < elTriggers.size( ); t++ )
									{
										tg = TriggerImpl.copyInstance( (Trigger) elTriggers.get( t ) );
										processTrigger( tg,
												StructureSource.createAxis( axModel ) );
										iev.addTrigger( tg );
									}
								}
								else
								{
									for ( int t = 0; t < cachedTriggers.size( ); t++ )
									{
										iev.addTrigger( (Trigger) cachedTriggers.get( t ) );
									}
								}

								iev.setHotSpot( pre3d );
								ipr.enableInteraction( iev );
							}
						}
					}

				}
				else
				{
					double dStart = daEndPoints[0] + insCA.getBottom( ), dEnd = daEndPoints[1]
							- insCA.getTop( );

					if ( sc.getDirection( ) == IConstants.FORWARD )
					{
						dStart = daEndPoints[1] + insCA.getBottom( );
						dEnd = daEndPoints[0] - insCA.getTop( );
					}

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

					if ( isInteractivityEnabled( ) )
					{
						Trigger tg;
						EList elTriggers = axModel.getTriggers( );

						if ( !elTriggers.isEmpty( ) )
						{
							final InteractionEvent iev = (InteractionEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
									InteractionEvent.class );
							for ( int t = 0; t < elTriggers.size( ); t++ )
							{
								tg = TriggerImpl.copyInstance( (Trigger) elTriggers.get( t ) );
								processTrigger( tg,
										StructureSource.createAxis( axModel ) );
								iev.addTrigger( tg );
							}

							Location[] loaHotspot = new Location[4];

							loaHotspot[0] = LocationImpl.create( dX
									- IConstants.LINE_EXPAND_SIZE, dStart );
							loaHotspot[1] = LocationImpl.create( dX
									+ IConstants.LINE_EXPAND_SIZE, dStart );
							loaHotspot[2] = LocationImpl.create( dX
									+ IConstants.LINE_EXPAND_SIZE, dEnd );
							loaHotspot[3] = LocationImpl.create( dX
									- IConstants.LINE_EXPAND_SIZE, dEnd );

							final PolygonRenderEvent pre = (PolygonRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
									PolygonRenderEvent.class );
							pre.setPoints( loaHotspot );
							iev.setHotSpot( pre );
							ipr.enableInteraction( iev );
						}
					}

				}
			}

			// The vertical axis directon, -1 means bottom->top, 1 means
			// top->bottom.
			final int iDirection = sc.getDirection( ) != IConstants.FORWARD ? -1
					: 1;

			if ( ( sc.getType( ) & IConstants.TEXT ) == IConstants.TEXT
					|| sc.isCategoryScale( ) )
			{
				final double dUnitSize = iDirection * sc.getUnitSize( );
				final double dOffset = dUnitSize / 2;

				DataSetIterator dsi = sc.getData( );
				final int iDateTimeUnit = ( sc.getType( ) == IConstants.DATE_TIME ) ? CDateTime.computeUnit( dsi )
						: IConstants.UNDEFINED;
				final ITextMetrics itmText = xs.getTextMetrics( la );

				double x = ( iLabelLocation == IConstants.LEFT ) ? dXTick1 - 1
						: dXTick2 + 1;
				dsi.reset( );
				for ( int i = 0; i < da.length - 1; i++ )
				{
					if ( bRenderAxisLabels )
					{
						la.getCaption( )
								.setValue( sc.formatCategoryValue( sc.getType( ),
										dsi.next( ),
										iDateTimeUnit ) );

						if ( sc.isTickLabelVisible( i ) )
						{
							ScriptHandler.callFunction( sh,
									ScriptHandler.BEFORE_DRAW_AXIS_LABEL,
									axModel,
									la,
									getRunTimeContext( ).getScriptContext( ) );
							getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL,
									la );
							itmText.reuse( la ); // RECYCLED
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
									// !NOT RENDER TICKS FOR 3D AXES
									// Line3DRenderEvent l3dreMinor = null;
									// for ( int k = 0; k < daMinor.length - 1;
									// k++ )
									// {
									// l3dreMinor = (Line3DRenderEvent) (
									// (EventObjectCache) ipr ).getEventObject(
									// StructureSource.createAxis( axModel ),
									// Line3DRenderEvent.class );
									// l3dreMinor.setLineAttributes(
									// liaMinorTick );
									// l3dreMinor.setStart3D(
									// Location3DImpl.create( dXMinorTick1,
									// y3d + daMinor[k],
									// dZ ) );
									// l3dreMinor.setEnd3D(
									// Location3DImpl.create( dXMinorTick2,
									// y3d + daMinor[k],
									// dZ ) );
									// dc.addLine( l3dreMinor );
									// }
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
												y + iDirection * daMinor[k] ) );
										lreMinor.setEnd( LocationImpl.create( dXMinorTick2,
												y + iDirection * daMinor[k] ) );
										ipr.drawLine( lreMinor );
									}
								}
							}
						}

						if ( dXTick1 != dXTick2 )
						{
							if ( bRenderOrthogonal3DAxis )
							{
								// !NOT RENDER TICKS FOR 3D AXES
								// l3dre.setLineAttributes( liaMajorTick );
								// l3dre.setStart3D( dXTick1, y3d, dZ );
								// l3dre.setEnd3D( dXTick2, y3d, dZ );
								// dc.addLine( l3dre );
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

					if ( bRenderAxisLabels && sc.isTickLabelVisible( i ) )
					{
						double sx = x;
						double sx2 = dXEnd;
						if ( bAxisLabelStaggered && sc.isTickLabelStaggered( i ) )
						{
							if ( iLabelLocation == IConstants.LEFT )
							{
								sx -= dStaggeredLabelOffset;
								sx2 += dStaggeredLabelOffset;
							}
							else
							{
								sx += dStaggeredLabelOffset;
								sx2 -= dStaggeredLabelOffset;
							}
						}

						if ( ax.getLabel( ).isVisible( ) && la.isVisible( ) )
						{
							if ( bRendering3D )
							{
								// Left wall
								lo3d.set( sx
										- pwa.getHorizontalSpacingInPixels( ),
										y3d + dOffset,
										dZEnd
												+ pwa.getHorizontalSpacingInPixels( ) );
								t3dre.setLocation3D( lo3d );
								t3dre.setTextPosition( TextRenderEvent.LEFT );
								t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								dc.addLabel( t3dre );

								// Right wall
								lo3d.set( sx2
										+ pwa.getHorizontalSpacingInPixels( ),
										y3d + dOffset,
										dZ - pwa.getHorizontalSpacingInPixels( ) );
								t3dre.setLocation3D( lo3d );
								t3dre.setTextPosition( TextRenderEvent.RIGHT );
								t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								dc.addLabel( t3dre );
							}
							else
							{
								lo.set( sx, y + dOffset );
								tre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								ipr.drawText( tre );
							}
						}

						ScriptHandler.callFunction( sh,
								ScriptHandler.AFTER_DRAW_AXIS_LABEL,
								axModel,
								la,
								getRunTimeContext( ).getScriptContext( ) );
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
						// !NOT RENDER TICKS FOR 3D AXES
						// l3dre.setLineAttributes( liaMajorTick );
						// l3dre.setStart3D( dXTick1, y3d, dZ );
						// l3dre.setEnd3D( dXTick2, y3d, dZ );
						// dc.addLine( l3dre );
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
								ax.getRunTimeContext( ).getULocale( ),
								df );
					}
					catch ( ChartException dfex )
					{
						logger.log( dfex );
						sText = IConstants.NULL_STRING;
					}
					la.getCaption( ).setValue( sText );

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
									// !NOT RENDER TICKS FOR 3D AXES
									// Line3DRenderEvent l3dreMinor = null;
									// for ( int k = 0; k < daMinor.length - 1;
									// k++ )
									// {
									// if ( y3d + daMinor[k] >= da3D[i + 1] )
									// {
									// // if current minor tick exceed the
									// // range of current unit, skip
									// continue;
									// }
									//
									// l3dreMinor = (Line3DRenderEvent) (
									// (EventObjectCache) ipr ).getEventObject(
									// StructureSource.createAxis( axModel ),
									// Line3DRenderEvent.class );
									// l3dreMinor.setLineAttributes(
									// liaMinorTick );
									// l3dreMinor.setStart3D(
									// Location3DImpl.create( dXMinorTick1,
									// y3d + daMinor[k],
									// dZ ) );
									// l3dreMinor.setEnd3D(
									// Location3DImpl.create( dXMinorTick2,
									// y3d + daMinor[k],
									// dZ ) );
									// dc.addLine( l3dreMinor );
									// }
								}
								else
								{
									LineRenderEvent lreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										if ( ( iDirection == -1 && y
												- daMinor[k] <= da[i + 1] )
												|| ( iDirection == 1 && y
														+ daMinor[k] >= da[i + 1] ) )
										{
											// if current minor tick exceed the
											// range of current unit, skip
											continue;
										}

										lreMinor = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												LineRenderEvent.class );
										lreMinor.setLineAttributes( liaMinorTick );
										lreMinor.setStart( LocationImpl.create( dXMinorTick1,
												y + iDirection * daMinor[k] ) );
										lreMinor.setEnd( LocationImpl.create( dXMinorTick2,
												y + iDirection * daMinor[k] ) );
										ipr.drawLine( lreMinor );
									}
								}
							}
						}

						if ( dXTick1 != dXTick2 )
						{
							if ( bRenderOrthogonal3DAxis )
							{
								// !NOT RENDER TICKS FOR 3D AXES
								// l3dre.setLineAttributes( liaMajorTick );
								// l3dre.setStart3D( dXTick1, y3d, dZ );
								// l3dre.setEnd3D( dXTick2, y3d, dZ );
								// dc.addLine( l3dre );
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

					if ( bRenderAxisLabels && sc.isTickLabelVisible( i ) )
					{
						double sx = x;
						double sx2 = dXEnd;
						if ( bAxisLabelStaggered && sc.isTickLabelStaggered( i ) )
						{
							if ( iLabelLocation == IConstants.LEFT )
							{
								sx -= dStaggeredLabelOffset;
								sx2 += dStaggeredLabelOffset;
							}
							else
							{
								sx += dStaggeredLabelOffset;
								sx2 -= dStaggeredLabelOffset;
							}
						}
						ScriptHandler.callFunction( sh,
								ScriptHandler.BEFORE_DRAW_AXIS_LABEL,
								axModel,
								la,
								getRunTimeContext( ).getScriptContext( ) );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL,
								la );
						if ( ax.getLabel( ).isVisible( ) && la.isVisible( ) )
						{
							if ( bRendering3D )
							{
								// Left wall
								lo3d.set( sx
										- pwa.getHorizontalSpacingInPixels( ),
										y3d,
										dZEnd
												+ pwa.getHorizontalSpacingInPixels( ) );
								t3dre.setLocation3D( lo3d );
								t3dre.setTextPosition( TextRenderEvent.LEFT );
								t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								dc.addLabel( t3dre );

								// Right wall
								lo3d.set( sx2
										+ pwa.getHorizontalSpacingInPixels( ),
										y3d,
										dZ - pwa.getHorizontalSpacingInPixels( ) );
								la.getCaption( ).setValue( sText );
								t3dre.setLocation3D( lo3d );
								t3dre.setTextPosition( TextRenderEvent.RIGHT );
								t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								dc.addLabel( t3dre );
							}
							else
							{
								lo.set( sx, y );
								tre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								ipr.drawText( tre );
							}
						}

						ScriptHandler.callFunction( sh,
								ScriptHandler.AFTER_DRAW_AXIS_LABEL,
								axModel,
								la,
								getRunTimeContext( ).getScriptContext( ) );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_AXIS_LABEL,
								la );
					}

					if ( i == da.length - 2 )
					{
						// This is the last tick, use pre-computed value to
						// handle
						// non-equal scale unit case.
						dAxisValue = Methods.asDouble( sc.getMaximum( ) )
								.doubleValue( );
					}
					else
					{
						dAxisValue += dAxisStep;
					}
				}
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
									ax.getRunTimeContext( ).getULocale( ),
									df );
						}
						catch ( ChartException dfex )
						{
							logger.log( dfex );
							sText = IConstants.NULL_STRING;
						}
						la.getCaption( ).setValue( sText );
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
									// !NOT RENDER TICKS FOR 3D AXES
									// Line3DRenderEvent l3dreMinor = null;
									// for ( int k = 0; k < daMinor.length - 1;
									// k++ )
									// {
									// l3dreMinor = (Line3DRenderEvent) (
									// (EventObjectCache) ipr ).getEventObject(
									// StructureSource.createAxis( axModel ),
									// Line3DRenderEvent.class );
									// l3dreMinor.setLineAttributes(
									// liaMinorTick );
									// l3dreMinor.setStart3D(
									// Location3DImpl.create( dXMinorTick1,
									// y3d + daMinor[k],
									// dZ ) );
									// l3dreMinor.setEnd3D(
									// Location3DImpl.create( dXMinorTick2,
									// y3d + daMinor[k],
									// dZ ) );
									// dc.addLine( l3dreMinor );
									// }
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
												y + iDirection * daMinor[k] ) );
										lreMinor.setEnd( LocationImpl.create( dXMinorTick2,
												y + iDirection * daMinor[k] ) );
										ipr.drawLine( lreMinor );
									}
								}
							}
						}

						if ( dXTick1 != dXTick2 )
						{
							if ( bRenderOrthogonal3DAxis )
							{
								// !NOT RENDER TICKS FOR 3D AXES
								// l3dre.setLineAttributes( liaMajorTick );
								// l3dre.setStart3D( dXTick1, y3d, dZ );
								// l3dre.setEnd3D( dXTick2, y3d, dZ );
								// dc.addLine( l3dre );
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

					// RENDER LABELS ONLY IF REQUESTED
					if ( bRenderAxisLabels && sc.isTickLabelVisible( i ) )
					{
						double sx = x;
						double sx2 = dXEnd;
						if ( bAxisLabelStaggered && sc.isTickLabelStaggered( i ) )
						{
							if ( iLabelLocation == IConstants.LEFT )
							{
								sx -= dStaggeredLabelOffset;
								sx2 += dStaggeredLabelOffset;
							}
							else
							{
								sx += dStaggeredLabelOffset;
								sx2 -= dStaggeredLabelOffset;
							}
						}
						ScriptHandler.callFunction( sh,
								ScriptHandler.BEFORE_DRAW_AXIS_LABEL,
								axModel,
								la,
								getRunTimeContext( ).getScriptContext( ) );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL,
								la );
						if ( ax.getLabel( ).isVisible( ) && la.isVisible( ) )
						{
							if ( bRendering3D )
							{
								// Left wall
								lo3d.set( sx
										- pwa.getHorizontalSpacingInPixels( ),
										y3d,
										dZEnd
												+ pwa.getHorizontalSpacingInPixels( ) );
								la.getCaption( ).setValue( sText );
								t3dre.setLocation3D( lo3d );
								t3dre.setTextPosition( TextRenderEvent.LEFT );
								t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								dc.addLabel( t3dre );

								// Right wall
								lo3d.set( sx2
										+ pwa.getHorizontalSpacingInPixels( ),
										y3d,
										dZ - pwa.getHorizontalSpacingInPixels( ) );
								t3dre.setLocation3D( lo3d );
								t3dre.setTextPosition( TextRenderEvent.RIGHT );
								t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								dc.addLabel( t3dre );
							}
							else
							{
								lo.set( sx, y );
								tre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								ipr.drawText( tre );

							}
						}
						ScriptHandler.callFunction( sh,
								ScriptHandler.AFTER_DRAW_AXIS_LABEL,
								axModel,
								la,
								getRunTimeContext( ).getScriptContext( ) );
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
				IDateFormatWrapper sdf = null;
				if ( fs == null )
				{
					sdf = DateFormatWrapperFactory.getPreferredDateFormat( iUnit,
							rtc.getULocale( ) );
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
								ax.getRunTimeContext( ).getULocale( ),
								sdf );
					}
					catch ( ChartException dfex )
					{
						logger.log( dfex );
						sText = IConstants.NULL_STRING;
					}
					la.getCaption( ).setValue( sText );

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
									// !NOT RENDER TICKS FOR 3D AXES
									// Line3DRenderEvent l3dreMinor = null;
									// for ( int k = 0; k < daMinor.length - 1;
									// k++ )
									// {
									// l3dreMinor = (Line3DRenderEvent) (
									// (EventObjectCache) ipr ).getEventObject(
									// StructureSource.createAxis( axModel ),
									// Line3DRenderEvent.class );
									// l3dreMinor.setLineAttributes(
									// liaMinorTick );
									// l3dreMinor.setStart3D(
									// Location3DImpl.create( dXMinorTick1,
									// y3d + daMinor[k],
									// dZ ) );
									// l3dreMinor.setEnd3D(
									// Location3DImpl.create( dXMinorTick2,
									// y3d + daMinor[k],
									// dZ ) );
									// dc.addLine( l3dreMinor );
									// }
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
												y + iDirection * daMinor[k] ) );
										lreMinor.setEnd( LocationImpl.create( dXMinorTick2,
												y + iDirection * daMinor[k] ) );
										ipr.drawLine( lreMinor );
									}
								}
							}
						}

						if ( dXTick1 != dXTick2 )
						{
							if ( bRenderOrthogonal3DAxis )
							{
								// !NOT RENDER TICKS FOR 3D AXES
								// l3dre.setLineAttributes( liaMajorTick );
								// l3dre.setStart3D( dXTick1, y3d, dZ );
								// l3dre.setEnd3D( dXTick2, y3d, dZ );
								// dc.addLine( l3dre );
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

					if ( bRenderAxisLabels && sc.isTickLabelVisible( i ) )
					{
						double sx = x;
						double sx2 = dXEnd;
						if ( bAxisLabelStaggered && sc.isTickLabelStaggered( i ) )
						{
							if ( iLabelLocation == IConstants.LEFT )
							{
								sx -= dStaggeredLabelOffset;
								sx2 += dStaggeredLabelOffset;
							}
							else
							{
								sx += dStaggeredLabelOffset;
								sx2 -= dStaggeredLabelOffset;
							}
						}

						ScriptHandler.callFunction( sh,
								ScriptHandler.BEFORE_DRAW_AXIS_LABEL,
								axModel,
								la,
								getRunTimeContext( ).getScriptContext( ) );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL,
								la );
						if ( ax.getLabel( ).isVisible( ) && la.isVisible( ) )
						{
							if ( bRendering3D )
							{
								// Left wall
								lo3d.set( sx
										- pwa.getHorizontalSpacingInPixels( ),
										y3d,
										dZEnd
												+ pwa.getHorizontalSpacingInPixels( ) );
								t3dre.setLocation3D( lo3d );
								t3dre.setTextPosition( TextRenderEvent.LEFT );
								t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								dc.addLabel( t3dre );

								// Right wall
								lo3d.set( sx2
										+ pwa.getHorizontalSpacingInPixels( ),
										y3d,
										dZ - pwa.getHorizontalSpacingInPixels( ) );
								la.getCaption( ).setValue( sText );
								t3dre.setLocation3D( lo3d );
								t3dre.setTextPosition( TextRenderEvent.RIGHT );
								t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								dc.addLabel( t3dre );
							}
							else
							{
								lo.set( sx, y );
								tre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								ipr.drawText( tre );
							}
						}
						ScriptHandler.callFunction( sh,
								ScriptHandler.AFTER_DRAW_AXIS_LABEL,
								axModel,
								la,
								getRunTimeContext( ).getScriptContext( ) );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_AXIS_LABEL,
								la );
					}

					// ALWAYS W.R.T START VALUE
					cdt = cdtAxisValue.forward( iUnit, iStep * ( i + 1 ) );
				}
			}

			la = LabelImpl.copyInstance( ax.getTitle( ) ); // TEMPORARILY USE
			// FOR AXIS TITLE
			if ( la.isVisible( ) && bRenderAxisTitle )
			{
				ScriptHandler.callFunction( sh,
						ScriptHandler.BEFORE_DRAW_AXIS_TITLE,
						axModel,
						la,
						getRunTimeContext( ).getScriptContext( ) );
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

				if ( ax.getTitle( ).isVisible( ) && la.isVisible( ) )
				{
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
						tre.setBlockAlignment( la.getCaption( )
								.getFont( )
								.getAlignment( ) );
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
						tre.setBlockAlignment( la.getCaption( )
								.getFont( )
								.getAlignment( ) );
						tre.setAction( TextRenderEvent.RENDER_TEXT_IN_BLOCK );
						if ( ax.getTitle( ).isVisible( ) )
						{
							ipr.drawText( tre );
						}
					}
				}

				la.getCaption( ).setValue( sRestoreValue );
				ScriptHandler.callFunction( sh,
						ScriptHandler.AFTER_DRAW_AXIS_TITLE,
						axModel,
						la,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_AXIS_TITLE,
						la );
			}
			la = LabelImpl.copyInstance( ax.getLabel( ) );

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

			double dYTick1 = ( ( iMajorTickStyle & IConstants.TICK_ABOVE ) == IConstants.TICK_ABOVE ) ? ( bRendering3D ? dY
					+ IConstants.TICK_SIZE
					: dY - IConstants.TICK_SIZE )
					: dY;
			double dYTick2 = ( ( iMajorTickStyle & IConstants.TICK_BELOW ) == IConstants.TICK_BELOW ) ? ( bRendering3D ? dY
					- IConstants.TICK_SIZE
					: dY + IConstants.TICK_SIZE )
					: dY;

			if ( iv != null
					&& iDimension == IConstants.TWO_5_D
					&& ( ( bTransposed && isRightToLeft( ) && iv.getType( ) == IntersectionValue.MIN ) || ( !isRightToLeft( ) && iv.getType( ) == IntersectionValue.MAX ) ) )
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

					if ( isInteractivityEnabled( ) )
					{
						Trigger tg;
						EList elTriggers = axModel.getTriggers( );

						if ( !elTriggers.isEmpty( ) )
						{
							final Polygon3DRenderEvent pre3d = (Polygon3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
									Polygon3DRenderEvent.class );

							Location3D[] loaHotspot = new Location3D[4];
							loaHotspot[0] = Location3DImpl.create( dStart, dY
									- IConstants.LINE_EXPAND_DOUBLE_SIZE, dZ );
							loaHotspot[1] = Location3DImpl.create( dStart, dY
									+ IConstants.LINE_EXPAND_DOUBLE_SIZE, dZ );
							loaHotspot[2] = Location3DImpl.create( dEnd, dY
									+ IConstants.LINE_EXPAND_DOUBLE_SIZE, dZ );
							loaHotspot[3] = Location3DImpl.create( dEnd, dY
									- IConstants.LINE_EXPAND_DOUBLE_SIZE, dZ );
							pre3d.setPoints3D( loaHotspot );
							pre3d.setDoubleSided( true );

							if ( get3DEngine( ).processEvent( pre3d,
									panningOffset.getX( ),
									panningOffset.getY( ) ) != null )
							{
								final InteractionEvent iev = (InteractionEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
										InteractionEvent.class );
								for ( int t = 0; t < elTriggers.size( ); t++ )
								{
									tg = TriggerImpl.copyInstance( (Trigger) elTriggers.get( t ) );
									processTrigger( tg,
											StructureSource.createAxis( axModel ) );
									iev.addTrigger( tg );
								}

								iev.setHotSpot( pre3d );
								ipr.enableInteraction( iev );
							}
						}
					}

				}
				else if ( bRenderAncillary3DAxis )
				{
					final double dStart = daEndPoints3D[0];
					final double dEnd = daEndPoints3D[1];
					l3dre.setLineAttributes( lia );
					l3dre.setStart3D( dX, dY, dStart );
					l3dre.setEnd3D( dX, dY, dEnd );
					dc.addLine( l3dre );

					if ( isInteractivityEnabled( ) )
					{
						Trigger tg;
						EList elTriggers = axModel.getTriggers( );

						if ( !elTriggers.isEmpty( ) )
						{
							final Polygon3DRenderEvent pre3d = (Polygon3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
									Polygon3DRenderEvent.class );

							Location3D[] loaHotspot = new Location3D[4];
							loaHotspot[0] = Location3DImpl.create( dX,
									dY - IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dStart );
							loaHotspot[1] = Location3DImpl.create( dX,
									dY + IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dStart );
							loaHotspot[2] = Location3DImpl.create( dX, dY
									+ IConstants.LINE_EXPAND_DOUBLE_SIZE, dEnd );
							loaHotspot[3] = Location3DImpl.create( dX, dY
									- IConstants.LINE_EXPAND_DOUBLE_SIZE, dEnd );
							pre3d.setPoints3D( loaHotspot );
							pre3d.setDoubleSided( true );

							if ( get3DEngine( ).processEvent( pre3d,
									panningOffset.getX( ),
									panningOffset.getY( ) ) != null )
							{
								final InteractionEvent iev = (InteractionEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
										InteractionEvent.class );
								for ( int t = 0; t < elTriggers.size( ); t++ )
								{
									tg = TriggerImpl.copyInstance( (Trigger) elTriggers.get( t ) );
									processTrigger( tg,
											StructureSource.createAxis( axModel ) );
									iev.addTrigger( tg );
								}

								iev.setHotSpot( pre3d );
								ipr.enableInteraction( iev );
							}
						}
					}

				}
				else
				{
					double dStart = daEndPoints[0] - insCA.getLeft( ), dEnd = daEndPoints[1]
							+ insCA.getRight( );

					if ( sc.getDirection( ) == IConstants.BACKWARD )
					{
						dStart = daEndPoints[1] - insCA.getLeft( );
						dEnd = daEndPoints[0] + insCA.getRight( );
					}

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

					if ( isInteractivityEnabled( ) )
					{
						Trigger tg;
						EList elTriggers = axModel.getTriggers( );

						if ( !elTriggers.isEmpty( ) )
						{
							final InteractionEvent iev = (InteractionEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
									InteractionEvent.class );
							for ( int t = 0; t < elTriggers.size( ); t++ )
							{
								tg = TriggerImpl.copyInstance( (Trigger) elTriggers.get( t ) );
								processTrigger( tg,
										StructureSource.createAxis( axModel ) );
								iev.addTrigger( tg );
							}

							Location[] loaHotspot = new Location[4];

							loaHotspot[0] = LocationImpl.create( dStart, dY
									- IConstants.LINE_EXPAND_SIZE );
							loaHotspot[1] = LocationImpl.create( dEnd, dY
									- IConstants.LINE_EXPAND_SIZE );
							loaHotspot[2] = LocationImpl.create( dEnd, dY
									+ IConstants.LINE_EXPAND_SIZE );
							loaHotspot[3] = LocationImpl.create( dStart, dY
									+ IConstants.LINE_EXPAND_SIZE );

							final PolygonRenderEvent pre = (PolygonRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
									PolygonRenderEvent.class );
							pre.setPoints( loaHotspot );
							iev.setHotSpot( pre );
							ipr.enableInteraction( iev );
						}
					}

				}
			}

			// The horizontal axis direction. -1 means right->left, 1 means
			// left->right.
			final int iDirection = sc.getDirection( ) == IConstants.BACKWARD ? -1
					: 1;

			if ( ( sc.getType( ) & IConstants.TEXT ) == IConstants.TEXT
					|| sc.isCategoryScale( ) )
			{
				final double dUnitSize = iDirection * sc.getUnitSize( );
				final double dOffset = dUnitSize / 2;

				DataSetIterator dsi = sc.getData( );
				final int iDateTimeUnit = ( sc.getType( ) == IConstants.DATE_TIME ) ? CDateTime.computeUnit( dsi )
						: IConstants.UNDEFINED;
				final ITextMetrics itmText = xs.getTextMetrics( la );

				double y = ( iLabelLocation == IConstants.ABOVE ) ? ( bRendering3D ? dYTick1 + 1
						: dYTick1 - 1 )
						: ( bRendering3D ? dYTick2 - 1 : dYTick2 + 1 );
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
						double dYMinorTick1 = ( ( iMinorTickStyle & IConstants.TICK_ABOVE ) == IConstants.TICK_ABOVE ) ? ( bRendering3D ? dY
								+ IConstants.TICK_SIZE
								: dY - IConstants.TICK_SIZE )
								: dY;
						double dYMinorTick2 = ( ( iMinorTickStyle & IConstants.TICK_BELOW ) == IConstants.TICK_BELOW ) ? ( bRendering3D ? dY
								- IConstants.TICK_SIZE
								: dY + IConstants.TICK_SIZE )
								: dY;
						if ( dYMinorTick1 != -dYMinorTick2 )
						{
							// RENDER THE MINOR TICKS FIRST (For ALL but the
							// last Major tick)
							if ( i != da.length - 1 )
							{
								if ( bRenderBase3DAxis )
								{
									// !NOT RENDER TICKS FOR 3D AXES
									// Line3DRenderEvent l3dreMinor = null;
									// for ( int k = 0; k < daMinor.length - 1;
									// k++ )
									// {
									// l3dreMinor = (Line3DRenderEvent) (
									// (EventObjectCache) ipr ).getEventObject(
									// StructureSource.createAxis( axModel ),
									// Line3DRenderEvent.class );
									// l3dreMinor.setLineAttributes(
									// liaMinorTick );
									// l3dreMinor.setStart3D(
									// Location3DImpl.create( x3d
									// + daMinor[k],
									// dYMinorTick1,
									// dZ ) );
									// l3dreMinor.setEnd3D(
									// Location3DImpl.create( x3d
									// + daMinor[k],
									// dYMinorTick2,
									// dZ ) );
									// dc.addLine( l3dreMinor );
									// }
								}
								else if ( bRenderAncillary3DAxis )
								{
									// !NOT RENDER TICKS FOR 3D AXES
									// Line3DRenderEvent l3dreMinor = null;
									// for ( int k = 0; k < daMinor.length - 1;
									// k++ )
									// {
									// l3dreMinor = (Line3DRenderEvent) (
									// (EventObjectCache) ipr ).getEventObject(
									// StructureSource.createAxis( axModel ),
									// Line3DRenderEvent.class );
									// l3dreMinor.setLineAttributes(
									// liaMinorTick );
									// l3dreMinor.setStart3D(
									// Location3DImpl.create( dX,
									// dYMinorTick1,
									// z3d + daMinor[k] ) );
									// l3dreMinor.setEnd3D(
									// Location3DImpl.create( dX,
									// dYMinorTick2,
									// z3d + daMinor[k] ) );
									// dc.addLine( l3dreMinor );
									// }
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
												+ iDirection
												* daMinor[k],
												dYMinorTick1 ) );
										lreMinor.setEnd( LocationImpl.create( x
												+ iDirection
												* daMinor[k], dYMinorTick2 ) );
										ipr.drawLine( lreMinor );
									}
								}
							}
						}

						if ( dYTick1 != dYTick2 )
						{
							if ( bRenderBase3DAxis )
							{
								// !NOT RENDER TICKS FOR 3D AXES
								// l3dre.setLineAttributes( liaMajorTick );
								// l3dre.setStart3D( x3d, dYTick1, dZ );
								// l3dre.setEnd3D( x3d, dYTick2, dZ );
								// dc.addLine( l3dre );
							}
							else if ( bRenderAncillary3DAxis )
							{
								// !NOT RENDER TICKS FOR 3D AXES
								// l3dre.setLineAttributes( liaMajorTick );
								// l3dre.setStart3D( dX, dYTick1, z3d );
								// l3dre.setEnd3D( dX, dYTick2, z3d );
								// dc.addLine( l3dre );
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

					if ( bRenderAxisLabels )
					{
						la.getCaption( )
								.setValue( sc.formatCategoryValue( sc.getType( ),
										dsi.next( ), // step to next value.
										iDateTimeUnit ) );

						if ( sc.isTickLabelVisible( i ) )
						{
							ScriptHandler.callFunction( sh,
									ScriptHandler.BEFORE_DRAW_AXIS_LABEL,
									axModel,
									la,
									getRunTimeContext( ).getScriptContext( ) );
							getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL,
									la );
							itmText.reuse( la );// RECYCLED

							double sy = y;

							if ( bAxisLabelStaggered
									&& sc.isTickLabelStaggered( i ) )
							{
								if ( iLabelLocation == IConstants.ABOVE )
								{
									sy -= dStaggeredLabelOffset;
								}
								else
								{
									sy += dStaggeredLabelOffset;
								}
							}
							if ( ax.getLabel( ).isVisible( ) && la.isVisible( ) )
							{
								if ( bRendering3D )
								{
									if ( axisType == IConstants.BASE_AXIS )
									{
										lo3d.set( x3d + dOffset,
												sy
														- pwa.getVerticalSpacingInPixels( ),
												dZEnd
														+ pwa.getVerticalSpacingInPixels( ) );
									}
									else
									{
										lo3d.set( dXEnd
												+ pwa.getVerticalSpacingInPixels( ),
												sy
														- pwa.getVerticalSpacingInPixels( ),
												z3d + dOffset );
									}
									t3dre.setLocation3D( lo3d );
									t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
									dc.addLabel( t3dre );
								}
								else
								{
									lo.set( x + dOffset, sy );
									tre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
									ipr.drawText( tre );
								}
							}
							ScriptHandler.callFunction( sh,
									ScriptHandler.AFTER_DRAW_AXIS_LABEL,
									axModel,
									la,
									getRunTimeContext( ).getScriptContext( ) );
							getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_AXIS_LABEL,
									la );
						}
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
							// !NOT RENDER TICKS FOR 3D AXES
							// l3dre.setLineAttributes( liaMajorTick );
							// l3dre.setStart3D( x3d, dYTick1, dZ );
							// l3dre.setEnd3D( x3d, dYTick2, dZ );
							// dc.addLine( l3dre );
						}
						else if ( bRenderAncillary3DAxis )
						{
							// !NOT RENDER TICKS FOR 3D AXES
							// l3dre.setLineAttributes( liaMajorTick );
							// l3dre.setStart3D( dX, dYTick1, z3d );
							// l3dre.setEnd3D( dX, dYTick2, z3d );
							// dc.addLine( l3dre );
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
				double y = ( iLabelLocation == IConstants.ABOVE ) ? ( bRendering3D ? dYTick1 + 1
						: dYTick1 - 1 )
						: ( bRendering3D ? dYTick2 - 1 : dYTick2 + 1 );
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
						double dYMinorTick1 = ( ( iMinorTickStyle & IConstants.TICK_ABOVE ) == IConstants.TICK_ABOVE ) ? ( bRendering3D ? dY
								+ IConstants.TICK_SIZE
								: dY - IConstants.TICK_SIZE )
								: dY;
						double dYMinorTick2 = ( ( iMinorTickStyle & IConstants.TICK_BELOW ) == IConstants.TICK_BELOW ) ? ( bRendering3D ? dY
								- IConstants.TICK_SIZE
								: dY + IConstants.TICK_SIZE )
								: dY;
						if ( dYMinorTick1 != -dYMinorTick2 )
						{
							// RENDER THE MINOR TICKS FIRST (For ALL but the
							// last Major tick)
							if ( i != da.length - 1 )
							{
								if ( bRenderBase3DAxis )
								{
									// !NOT RENDER TICKS FOR 3D AXES
									// Line3DRenderEvent l3dreMinor = null;
									// for ( int k = 0; k < daMinor.length - 1;
									// k++ )
									// {
									// if ( x3d + daMinor[k] >= da3D[i + 1] )
									// {
									// // if current minor tick exceed the
									// // range of current unit, skip
									// continue;
									// }
									//
									// l3dreMinor = (Line3DRenderEvent) (
									// (EventObjectCache) ipr ).getEventObject(
									// StructureSource.createAxis( axModel ),
									// Line3DRenderEvent.class );
									// l3dreMinor.setLineAttributes(
									// liaMinorTick );
									// l3dreMinor.setStart3D(
									// Location3DImpl.create( x3d
									// + daMinor[k],
									// dYMinorTick1,
									// dZ ) );
									// l3dreMinor.setEnd3D(
									// Location3DImpl.create( x3d
									// + daMinor[k],
									// dYMinorTick2,
									// dZ ) );
									// dc.addLine( l3dreMinor );
									// }
								}
								else if ( bRenderAncillary3DAxis )
								{
									// !NOT RENDER TICKS FOR 3D AXES
									// Line3DRenderEvent l3dreMinor = null;
									// for ( int k = 0; k < daMinor.length - 1;
									// k++ )
									// {
									// if ( z3d + daMinor[k] >= da3D[i + 1] )
									// {
									// // if current minor tick exceed the
									// // range of current unit, skip
									// continue;
									// }
									//
									// l3dreMinor = (Line3DRenderEvent) (
									// (EventObjectCache) ipr ).getEventObject(
									// StructureSource.createAxis( axModel ),
									// Line3DRenderEvent.class );
									// l3dreMinor.setLineAttributes(
									// liaMinorTick );
									// l3dreMinor.setStart3D(
									// Location3DImpl.create( dX,
									// dYMinorTick1,
									// z3d + daMinor[k] ) );
									// l3dreMinor.setEnd3D(
									// Location3DImpl.create( dX,
									// dYMinorTick2,
									// z3d + daMinor[k] ) );
									// dc.addLine( l3dreMinor );
									// }
								}
								else
								{
									LineRenderEvent lreMinor = null;
									for ( int k = 0; k < daMinor.length - 1; k++ )
									{
										if ( ( iDirection == 1 && x
												+ daMinor[k] >= da[i + 1] )
												|| ( iDirection == -1 && x
														- daMinor[k] <= da[i + 1] ) )
										{
											// if current minor tick exceed the
											// range of current unit, skip
											continue;
										}

										lreMinor = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
												LineRenderEvent.class );
										lreMinor.setLineAttributes( liaMinorTick );
										lreMinor.setStart( LocationImpl.create( x
												+ iDirection
												* daMinor[k],
												dYMinorTick1 ) );
										lreMinor.setEnd( LocationImpl.create( x
												+ iDirection
												* daMinor[k], dYMinorTick2 ) );
										ipr.drawLine( lreMinor );
									}
								}
							}
						}

						if ( dYTick1 != dYTick2 )
						{
							if ( bRenderBase3DAxis )
							{
								// !NOT RENDER TICKS FOR 3D AXES
								// l3dre.setLineAttributes( liaMajorTick );
								// l3dre.setStart3D( x3d, dYTick1, dZ );
								// l3dre.setEnd3D( x3d, dYTick2, dZ );
								// dc.addLine( l3dre );
							}
							else if ( bRenderAncillary3DAxis )
							{
								// !NOT RENDER TICKS FOR 3D AXES
								// l3dre.setLineAttributes( liaMajorTick );
								// l3dre.setStart3D( dX, dYTick1, z3d );
								// l3dre.setEnd3D( dX, dYTick2, z3d );
								// dc.addLine( l3dre );
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

					// OPTIMIZED: ONLY PROCESS IF AXES LABELS ARE VISIBLE OR
					// REQUESTED FOR
					if ( bRenderAxisLabels && sc.isTickLabelVisible( i ) )
					{
						nde.setValue( dAxisValue );
						try
						{
							sText = ValueFormatter.format( nde,
									ax.getFormatSpecifier( ),
									ax.getRunTimeContext( ).getULocale( ),
									df );
						}
						catch ( ChartException dfex )
						{
							logger.log( dfex );
							sText = IConstants.NULL_STRING;
						}
						la.getCaption( ).setValue( sText );

						ScriptHandler.callFunction( sh,
								ScriptHandler.BEFORE_DRAW_AXIS_LABEL,
								axModel,
								la,
								getRunTimeContext( ).getScriptContext( ) );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL,
								la );

						double sy = y;
						if ( bAxisLabelStaggered && sc.isTickLabelStaggered( i ) )
						{
							if ( iLabelLocation == IConstants.ABOVE )
							{
								sy -= dStaggeredLabelOffset;
							}
							else
							{
								sy += dStaggeredLabelOffset;
							}
						}
						if ( ax.getLabel( ).isVisible( ) && la.isVisible( ) )
						{
							if ( bRendering3D )
							{
								if ( axisType == IConstants.BASE_AXIS )
								{
									lo3d.set( x3d,
											sy
													- pwa.getVerticalSpacingInPixels( ),
											dZEnd
													+ pwa.getVerticalSpacingInPixels( ) );
								}
								else
								{
									lo3d.set( dXEnd
											+ pwa.getVerticalSpacingInPixels( ),
											sy
													- pwa.getVerticalSpacingInPixels( ),
											z3d );
								}
								t3dre.setLocation3D( lo3d );
								t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								dc.addLabel( t3dre );

							}
							else
							{
								lo.set( x, sy );
								tre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								ipr.drawText( tre );
							}
						}
						ScriptHandler.callFunction( sh,
								ScriptHandler.AFTER_DRAW_AXIS_LABEL,
								axModel,
								la,
								getRunTimeContext( ).getScriptContext( ) );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_AXIS_LABEL,
								la );
					}

					if ( i == da.length - 2 )
					{
						// This is the last tick, use pre-computed value to
						// handle
						// non-equal scale unit case.
						dAxisValue = Methods.asDouble( sc.getMaximum( ) )
								.doubleValue( );
					}
					else
					{
						dAxisValue += dAxisStep;
					}
				}
			}
			else if ( ( sc.getType( ) & IConstants.LOGARITHMIC ) == IConstants.LOGARITHMIC )
			{
				double dAxisValue = Methods.asDouble( sc.getMinimum( ) )
						.doubleValue( );
				final double dAxisStep = Methods.asDouble( sc.getStep( ) )
						.doubleValue( );

				dAxisValue = Methods.asDouble( sc.getMinimum( ) ).doubleValue( ); // RESET
				double y = ( iLabelLocation == IConstants.ABOVE ) ? ( bRendering3D ? dYTick1 + 1
						: dYTick1 - 1 )
						: ( bRendering3D ? dYTick2 - 1 : dYTick2 + 1 );
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
						double dYMinorTick1 = ( ( iMinorTickStyle & IConstants.TICK_ABOVE ) == IConstants.TICK_ABOVE ) ? ( bRendering3D ? dY
								+ IConstants.TICK_SIZE
								: dY - IConstants.TICK_SIZE )
								: dY;
						double dYMinorTick2 = ( ( iMinorTickStyle & IConstants.TICK_BELOW ) == IConstants.TICK_BELOW ) ? ( bRendering3D ? dY
								- IConstants.TICK_SIZE
								: dY + IConstants.TICK_SIZE )
								: dY;
						if ( dYMinorTick1 != -dYMinorTick2 )
						{
							// RENDER THE MINOR TICKS FIRST (For ALL but the
							// last Major tick)
							if ( i != da.length - 1 )
							{
								if ( bRenderBase3DAxis )
								{
									// !NOT RENDER TICKS FOR 3D AXES
									// Line3DRenderEvent l3dreMinor = null;
									// for ( int k = 0; k < daMinor.length - 1;
									// k++ )
									// {
									// l3dreMinor = (Line3DRenderEvent) (
									// (EventObjectCache) ipr ).getEventObject(
									// StructureSource.createAxis( axModel ),
									// Line3DRenderEvent.class );
									// l3dreMinor.setLineAttributes(
									// liaMinorTick );
									// l3dreMinor.setStart3D(
									// Location3DImpl.create( x3d
									// + daMinor[k],
									// dYMinorTick1,
									// dZ ) );
									// l3dreMinor.setEnd3D(
									// Location3DImpl.create( x3d
									// + daMinor[k],
									// dYMinorTick2,
									// dZ ) );
									// dc.addLine( l3dreMinor );
									// }
								}
								else if ( bRenderAncillary3DAxis )
								{
									// !NOT RENDER TICKS FOR 3D AXES
									// Line3DRenderEvent l3dreMinor = null;
									// for ( int k = 0; k < daMinor.length - 1;
									// k++ )
									// {
									// l3dreMinor = (Line3DRenderEvent) (
									// (EventObjectCache) ipr ).getEventObject(
									// StructureSource.createAxis( axModel ),
									// Line3DRenderEvent.class );
									// l3dreMinor.setLineAttributes(
									// liaMinorTick );
									// l3dreMinor.setStart3D(
									// Location3DImpl.create( dX,
									// dYMinorTick1,
									// z3d + daMinor[k] ) );
									// l3dreMinor.setEnd3D(
									// Location3DImpl.create( dX,
									// dYMinorTick2,
									// z3d + daMinor[k] ) );
									// dc.addLine( l3dreMinor );
									// }
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
												+ iDirection
												* daMinor[k],
												dYMinorTick1 ) );
										lreMinor.setEnd( LocationImpl.create( x
												+ iDirection
												* daMinor[k], dYMinorTick2 ) );
										ipr.drawLine( lreMinor );
									}
								}
							}
						}

						if ( dYTick1 != dYTick2 )
						{
							if ( bRenderBase3DAxis )
							{
								// !NOT RENDER TICKS FOR 3D AXES
								// l3dre.setLineAttributes( liaMajorTick );
								// l3dre.setStart3D( x3d, dYTick1, dZ );
								// l3dre.setEnd3D( x3d, dYTick2, dZ );
								// dc.addLine( l3dre );
							}
							else if ( bRenderAncillary3DAxis )
							{
								// !NOT RENDER TICKS FOR 3D AXES
								// l3dre.setLineAttributes( liaMajorTick );
								// l3dre.setStart3D( dX, dYTick1, z3d );
								// l3dre.setEnd3D( dX, dYTick2, z3d );
								// dc.addLine( l3dre );
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

					// OPTIMIZED: ONLY PROCESS IF AXES LABELS ARE VISIBLE OR
					// REQUESTED FOR
					if ( bRenderAxisLabels && sc.isTickLabelVisible( i ) )
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
									ax.getRunTimeContext( ).getULocale( ),
									df );
						}
						catch ( ChartException dfex )
						{
							logger.log( dfex );
							sText = IConstants.NULL_STRING;
						}
						la.getCaption( ).setValue( sText );

						ScriptHandler.callFunction( sh,
								ScriptHandler.BEFORE_DRAW_AXIS_LABEL,
								axModel,
								la,
								getRunTimeContext( ).getScriptContext( ) );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL,
								la );

						double sy = y;
						if ( bAxisLabelStaggered && sc.isTickLabelStaggered( i ) )
						{
							if ( iLabelLocation == IConstants.ABOVE )
							{
								sy -= dStaggeredLabelOffset;
							}
							else
							{
								sy += dStaggeredLabelOffset;
							}
						}
						if ( ax.getLabel( ).isVisible( ) && la.isVisible( ) )
						{
							if ( bRendering3D )
							{
								if ( axisType == IConstants.BASE_AXIS )
								{
									lo3d.set( x3d,
											sy
													- pwa.getVerticalSpacingInPixels( ),
											dZEnd
													+ pwa.getVerticalSpacingInPixels( ) );
								}
								else
								{
									lo3d.set( dXEnd
											+ pwa.getVerticalSpacingInPixels( ),
											sy
													- pwa.getVerticalSpacingInPixels( ),
											z3d );
								}
								t3dre.setLocation3D( lo3d );
								t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								dc.addLabel( t3dre );
							}
							else
							{
								lo.set( x, sy );
								tre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								ipr.drawText( tre );
							}
						}
						ScriptHandler.callFunction( sh,
								ScriptHandler.AFTER_DRAW_AXIS_LABEL,
								axModel,
								la,
								getRunTimeContext( ).getScriptContext( ) );
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
				IDateFormatWrapper sdf = null;

				if ( fs == null )
				{
					sdf = DateFormatWrapperFactory.getPreferredDateFormat( iUnit,
							rtc.getULocale( ) );
				}
				cdt = cdtAxisValue;

				double y = ( iLabelLocation == IConstants.ABOVE ) ? ( bRendering3D ? dYTick1 + 1
						: dYTick1 - 1 )
						: ( bRendering3D ? dYTick2 - 1 : dYTick2 + 1 );
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
						double dYMinorTick1 = ( ( iMinorTickStyle & IConstants.TICK_ABOVE ) == IConstants.TICK_ABOVE ) ? ( bRendering3D ? dY
								+ IConstants.TICK_SIZE
								: dY - IConstants.TICK_SIZE )
								: dY;
						double dYMinorTick2 = ( ( iMinorTickStyle & IConstants.TICK_BELOW ) == IConstants.TICK_BELOW ) ? ( bRendering3D ? dY
								- IConstants.TICK_SIZE
								: dY + IConstants.TICK_SIZE )
								: dY;
						if ( dYMinorTick1 != -dYMinorTick2 )
						{
							// RENDER THE MINOR TICKS FIRST (For ALL but the
							// last Major tick)
							if ( i != da.length - 1 )
							{
								if ( bRenderBase3DAxis )
								{
									// !NOT RENDER TICKS FOR 3D AXES
									// Line3DRenderEvent l3dreMinor = null;
									// for ( int k = 0; k < daMinor.length - 1;
									// k++ )
									// {
									// l3dreMinor = (Line3DRenderEvent) (
									// (EventObjectCache) ipr ).getEventObject(
									// StructureSource.createAxis( axModel ),
									// Line3DRenderEvent.class );
									// l3dreMinor.setLineAttributes(
									// liaMinorTick );
									// l3dreMinor.setStart3D(
									// Location3DImpl.create( x3d
									// + daMinor[k],
									// dYMinorTick1,
									// dZ ) );
									// l3dreMinor.setEnd3D(
									// Location3DImpl.create( x3d
									// + daMinor[k],
									// dYMinorTick2,
									// dZ ) );
									// dc.addLine( l3dreMinor );
									// }
								}
								else if ( bRenderAncillary3DAxis )
								{
									// !NOT RENDER TICKS FOR 3D AXES
									// Line3DRenderEvent l3dreMinor = null;
									// for ( int k = 0; k < daMinor.length - 1;
									// k++ )
									// {
									// l3dreMinor = (Line3DRenderEvent) (
									// (EventObjectCache) ipr ).getEventObject(
									// StructureSource.createAxis( axModel ),
									// Line3DRenderEvent.class );
									// l3dreMinor.setLineAttributes(
									// liaMinorTick );
									// l3dreMinor.setStart3D(
									// Location3DImpl.create( dX,
									// dYMinorTick1,
									// z3d + daMinor[k] ) );
									// l3dreMinor.setEnd3D(
									// Location3DImpl.create( dX,
									// dYMinorTick2,
									// z3d + daMinor[k] ) );
									// dc.addLine( l3dreMinor );
									// }
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
												+ iDirection
												* daMinor[k],
												dYMinorTick1 ) );
										lreMinor.setEnd( LocationImpl.create( x
												+ iDirection
												* daMinor[k], dYMinorTick2 ) );
										ipr.drawLine( lreMinor );
									}
								}
							}
						}

						if ( dYTick1 != dYTick2 )
						{
							if ( bRenderBase3DAxis )
							{
								// !NOT RENDER TICKS FOR 3D AXES
								// l3dre.setLineAttributes( liaMajorTick );
								// l3dre.setStart3D( x3d, dYTick1, dZ );
								// l3dre.setEnd3D( x3d, dYTick2, dZ );
								// dc.addLine( l3dre );
							}
							else if ( bRenderAncillary3DAxis )
							{
								// !NOT RENDER TICKS FOR 3D AXES
								// l3dre.setLineAttributes( liaMajorTick );
								// l3dre.setStart3D( dX, dYTick1, z3d );
								// l3dre.setEnd3D( dX, dYTick2, z3d );
								// dc.addLine( l3dre );
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

					// OPTIMIZED: ONLY PROCESS IF AXES LABELS ARE VISIBLE OR
					// REQUESTED FOR
					if ( bRenderAxisLabels && sc.isTickLabelVisible( i ) )
					{
						try
						{
							sText = ValueFormatter.format( cdt,
									ax.getFormatSpecifier( ),
									ax.getRunTimeContext( ).getULocale( ),
									sdf );
						}
						catch ( ChartException dfex )
						{
							logger.log( dfex );
							sText = IConstants.NULL_STRING;
						}
						la.getCaption( ).setValue( sText );

						ScriptHandler.callFunction( sh,
								ScriptHandler.BEFORE_DRAW_AXIS_LABEL,
								axModel,
								la,
								getRunTimeContext( ).getScriptContext( ) );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL,
								la );

						double sy = y;
						if ( bAxisLabelStaggered && sc.isTickLabelStaggered( i ) )
						{
							if ( iLabelLocation == IConstants.ABOVE )
							{
								sy -= dStaggeredLabelOffset;
							}
							else
							{
								sy += dStaggeredLabelOffset;
							}
						}
						if ( ax.getLabel( ).isVisible( ) && la.isVisible( ) )
						{
							if ( bRendering3D )
							{
								if ( axisType == IConstants.BASE_AXIS )
								{
									lo3d.set( x3d,
											sy
													- pwa.getVerticalSpacingInPixels( ),
											dZEnd
													+ pwa.getVerticalSpacingInPixels( ) );
								}
								else
								{
									lo3d.set( dXEnd
											+ pwa.getVerticalSpacingInPixels( ),
											sy
													- pwa.getVerticalSpacingInPixels( ),
											z3d );
								}
								t3dre.setLocation3D( lo3d );
								t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								dc.addLabel( t3dre );
							}
							else
							{
								lo.set( x, sy );
								tre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
								ipr.drawText( tre );
							}
						}
						ScriptHandler.callFunction( sh,
								ScriptHandler.AFTER_DRAW_AXIS_LABEL,
								axModel,
								la,
								getRunTimeContext( ).getScriptContext( ) );
						getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_AXIS_LABEL,
								la );
					}

					// ALWAYS W.R.T START VALUE
					cdt = cdtAxisValue.forward( iUnit, iStep * ( i + 1 ) );
				}
			}

			// RENDER THE AXIS TITLE
			la = LabelImpl.copyInstance( ax.getTitle( ) ); // TEMPORARILY USE
			// FOR AXIS TITLE
			if ( la.isVisible( ) && bRenderAxisTitle )
			{
				ScriptHandler.callFunction( sh,
						ScriptHandler.BEFORE_DRAW_AXIS_TITLE,
						axModel,
						la,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_AXIS_TITLE,
						la );
				final String sRestoreValue = la.getCaption( ).getValue( );
				la.getCaption( )
						.setValue( rtc.externalizedMessage( sRestoreValue ) ); // EXTERNALIZE
				la.getCaption( )
						.getFont( )
						.setAlignment( switchTextAlignment( la.getCaption( )
								.getFont( )
								.getAlignment( ) ) );

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
				if ( ax.getTitle( ).isVisible( ) && la.isVisible( ) )
				{
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
									/ 2d,
									cbo.getTop( )
											+ cbo.getHeight( )
											- Math.min( bb.getHeight( ),
													bb.getWidth( ) )
											- 30
											* 2,
									bb.getWidth( ),
									bb.getHeight( ) ) );
						}

						tre.setLabel( la );
						tre.setBlockAlignment( la.getCaption( )
								.getFont( )
								.getAlignment( ) );
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
						tre.setBlockAlignment( la.getCaption( )
								.getFont( )
								.getAlignment( ) );
						tre.setAction( TextRenderEvent.RENDER_TEXT_IN_BLOCK );
						ipr.drawText( tre );
					}
				}

				ScriptHandler.callFunction( sh,
						ScriptHandler.AFTER_DRAW_AXIS_TITLE,
						axModel,
						la,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_AXIS_TITLE,
						la );
			}
			la = LabelImpl.copyInstance( ax.getLabel( ) ); // RESTORE BACK TO
			// AXIS LABEL

			if ( iv != null
					&& iDimension == IConstants.TWO_5_D
					&& ( ( bTransposed && isRightToLeft( ) && iv.getType( ) == IntersectionValue.MIN ) || ( !isRightToLeft( ) && iv.getType( ) == IntersectionValue.MAX ) ) )
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
	 * @return Returns the axis associated with current renderer.
	 */
	public final Axis getAxis( )
	{
		return ax;
	}
}
/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.render;

import java.util.ArrayList;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.computation.withaxes.AllAxes;
import org.eclipse.birt.chart.computation.withaxes.AutoScale;
import org.eclipse.birt.chart.computation.withaxes.IntersectionValue;
import org.eclipse.birt.chart.computation.withaxes.OneAxis;
import org.eclipse.birt.chart.computation.withaxes.PlotWithAxes;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.Line3DRenderEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.Polygon3DRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.Text3DRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.TransformationEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.internal.factory.DateFormatWrapperFactory;
import org.eclipse.birt.chart.internal.factory.IDateFormatWrapper;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.Location3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.text.DecimalFormat;

/**
 * Helper class for AxesRenderer. By providing interface and inner classes, to
 * refactor the complicated method in AxesRenderer.
 */

public final class AxesRenderHelper
{

	private static AxesRenderHelper instance = null;
	static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine/render" ); //$NON-NLS-1$

	AxesRenderer renderer;
	OneAxis ax;
	int iWhatToDraw;
	IPrimitiveRenderer ipr;

	private Axis axModel;
	private PlotWithAxes pwa;
	private Insets insCA;
	private ScriptHandler sh;
	private double dLocation;
	private AutoScale sc;
	private IntersectionValue iv;
	private int iMajorTickStyle;
	private int iMinorTickStyle;
	private int iLabelLocation;
	private int iOrientation;
	private IDisplayServer xs;
	private Label la;

	private double[] daEndPoints;
	private double[] da;
	private double[] daMinor;
	private String sText;

	private int iDimension;
	private double dSeriesThickness;
	private NumberDataElement nde;
	private FormatSpecifier fs;
	private boolean bAxisLabelStaggered;

	private DecimalFormat df;
	private LineAttributes lia;
	private LineAttributes liaMajorTick;
	private LineAttributes liaMinorTick;

	private boolean bRenderAxisLabels;
	private boolean bRenderAxisTitle;
	private Location lo;

	private TransformationEvent trae;
	private TextRenderEvent tre;
	private LineRenderEvent lre;

	// Prepare 3D rendering variables.
	private boolean bRendering3D;
	private boolean bRenderOrthogonal3DAxis;
	private boolean bRenderBase3DAxis;
	private boolean bRenderAncillary3DAxis;

	private DeferredCache dc;
	private int axisType;
	private Location panningOffset;
	private boolean bTransposed;

	private double[] daEndPoints3D;
	private double[] da3D;
	private Location3D lo3d;
	private Text3DRenderEvent t3dre;
	private Line3DRenderEvent l3dre;

	private AxesRenderHelper( )
	{

	}

	static AxesRenderHelper getInstance( AxesRenderer renderer,
			IPrimitiveRenderer ipr, Plot pl, OneAxis ax, int iWhatToDraw )
	{
		if ( instance == null )
		{
			instance = new AxesRenderHelper( );
		}
		instance.init( renderer, ipr, pl, ax, iWhatToDraw );
		return instance;
	}

	private void init( AxesRenderer renderer, IPrimitiveRenderer ipr, Plot pl,
			OneAxis ax, int iWhatToDraw )
	{
		this.renderer = renderer;
		this.ax = ax;
		this.iWhatToDraw = iWhatToDraw;
		this.ipr = ipr;

		axModel = ax.getModelAxis( );
		pwa = (PlotWithAxes) renderer.getComputations( );
		insCA = pwa.getAxes( ).getInsets( );
		sh = getRunTimeContext( ).getScriptHandler( );
		dLocation = ax.getAxisCoordinate( );
		sc = ax.getScale( );
		iv = ax.getIntersectionValue( );
		iMajorTickStyle = ax.getGrid( ).getTickStyle( IConstants.MAJOR );
		iMinorTickStyle = ax.getGrid( ).getTickStyle( IConstants.MINOR );
		iLabelLocation = ax.getLabelPosition( );
		iOrientation = ax.getOrientation( );
		xs = renderer.getDevice( ).getDisplayServer( );
		la = LabelImpl.copyInstance( ax.getLabel( ) );

		daEndPoints = sc.getEndPoints( );
		da = sc.getTickCordinates( );
		daMinor = sc.getMinorCoordinates( ax.getGrid( ).getMinorCountPerMajor( ) );
		sText = null;

		iDimension = pwa.getDimension( );
		nde = NumberDataElementImpl.create( 0 );
		dSeriesThickness = pwa.getSeriesThickness( );
		fs = ax.getModelAxis( ).getFormatSpecifier( );
		bAxisLabelStaggered = sc.isAxisLabelStaggered( );

		df = null;
		lia = ax.getLineAttributes( );
		liaMajorTick = ax.getGrid( ).getTickAttributes( IConstants.MAJOR );
		liaMinorTick = ax.getGrid( ).getTickAttributes( IConstants.MINOR );

		bRenderAxisLabels = ( ( iWhatToDraw & IConstants.LABELS ) == IConstants.LABELS && la.isVisible( ) );
		bRenderAxisTitle = ( ( iWhatToDraw & IConstants.LABELS ) == IConstants.LABELS );
		lo = LocationImpl.create( 0, 0 );

		trae = (TransformationEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
				TransformationEvent.class );
		tre = (TextRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
				TextRenderEvent.class );
		lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
				LineRenderEvent.class );

		// Prepare 3D rendering variables.
		bRendering3D = iDimension == IConstants.THREE_D;
		bRenderOrthogonal3DAxis = ( iWhatToDraw & IConstants.ORTHOGONAL_AXIS ) == IConstants.ORTHOGONAL_AXIS
				&& bRendering3D;
		bRenderBase3DAxis = ( iWhatToDraw & IConstants.BASE_AXIS ) == IConstants.BASE_AXIS
				&& bRendering3D;
		bRenderAncillary3DAxis = ( iWhatToDraw & IConstants.ANCILLARY_AXIS ) == IConstants.ANCILLARY_AXIS
				&& bRendering3D;

		dc = renderer.getDeferredCache( );
		axisType = ax.getAxisType( );
		panningOffset = renderer.getPanningOffset( );
		bTransposed = renderer.isTransposed( );

		daEndPoints3D = null;
		da3D = null;
		lo3d = null;
		t3dre = null;
		l3dre = null;
	}

	private RunTimeContext getRunTimeContext( )
	{
		return renderer.getRunTimeContext( );
	}

	private void processTrigger( Trigger tg, StructureSource source )
	{
		renderer.processTrigger( tg, source );
	}

	final class ComputationContext
	{

		// General
		boolean isVertical;
		double dTick1, dTick2;

		// Vertical
		double dX;
		double y3d;

		// Horizontal
		double dY;
		double x3d, z3d;

		public ComputationContext( boolean isVertical )
		{
			this.isVertical = isVertical;

		}
	}

	/** Interface for abstract method of different axes types */
	interface IAxisTypeComputation
	{

		/**
		 * Initializes when rendering an axes
		 * 
		 * @throws ChartException
		 */
		void initialize( ) throws ChartException;

		/**
		 * Last method of rendering an axes
		 * 
		 * @throws ChartException
		 */
		void close( ) throws ChartException;

		/**
		 * Handles computation before rendering each axes tick
		 * 
		 * @param i
		 *            tick index
		 * @throws ChartException
		 */
		void handlePreEachTick( int i ) throws ChartException;

		/**
		 * Handles computation after rendering each axes tick
		 * 
		 * @param i
		 *            tick index
		 * @throws ChartException
		 */
		void handlePostEachTick( int i ) throws ChartException;
	}

	private final class TextAxisTypeComputation implements IAxisTypeComputation
	{

		ComputationContext context;
		ITextMetrics itmText;
		int iDateTimeUnit;

		TextAxisTypeComputation( ComputationContext context )
		{
			this.context = context;
		}

		public void initialize( ) throws ChartException
		{
			iDateTimeUnit = ( sc.getType( ) == IConstants.DATE_TIME ) ? CDateTime.computeUnit( sc.getData( ) )
					: IConstants.UNDEFINED;
			itmText = xs.getTextMetrics( la );
			sc.getData( ).reset( );
		}

		public void close( ) throws ChartException
		{
			// ONE LAST TICK
			if ( context.isVertical )
			{
				int y = (int) da[da.length - 1];
				if ( bRendering3D )
				{
					context.y3d = (int) da3D[da3D.length - 1];
				}
				if ( context.dTick1 != context.dTick2 )
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
						lre.getStart( ).set( context.dTick1, y );
						lre.getEnd( ).set( context.dTick2, y );
						ipr.drawLine( lre );
					}

					if ( iv != null
							&& iDimension == IConstants.TWO_5_D
							&& iv.getType( ) == IntersectionValue.VALUE )
					{
						lre.setStart( LocationImpl.create( context.dX, y ) );
						lre.setEnd( LocationImpl.create( context.dX
								+ dSeriesThickness, y - dSeriesThickness ) );
						ipr.drawLine( lre );
					}
				}
			}
			else
			{
				int x = (int) da[da.length - 1];
				if ( ( iWhatToDraw & IConstants.AXIS ) == IConstants.AXIS )
				{
					if ( context.dTick1 != context.dTick2 )
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
							lre.getStart( ).set( x, context.dTick1 );
							lre.getEnd( ).set( x, context.dTick2 );
							ipr.drawLine( lre );
						}

						if ( iv != null
								&& iDimension == IConstants.TWO_5_D
								&& iv.getType( ) == IntersectionValue.VALUE )
						{
							lre.getStart( ).set( x, context.dY );
							lre.getEnd( ).set( x + dSeriesThickness,
									context.dY - dSeriesThickness );
							ipr.drawLine( lre );
						}
					}
				}
			}

			itmText.dispose( ); // DISPOSED
		}

		public void handlePostEachTick( int i ) throws ChartException
		{
			// TODO Auto-generated method stub

		}

		public void handlePreEachTick( int i ) throws ChartException
		{
			if ( bRenderAxisLabels )
			{
				la.getCaption( )
						.setValue( sc.formatCategoryValue( sc.getType( ),
								sc.getData( ).next( ),
								iDateTimeUnit ) );

				if ( sc.isTickLabelVisible( i ) )
				{
					itmText.reuse( la ); // RECYCLED
				}
			}

		}

	}

	private final class LinearAxisTypeComputation implements
			IAxisTypeComputation
	{

		double dAxisValue;
		double dAxisStep;

		public void close( ) throws ChartException
		{
			// TODO Auto-generated method stub

		}

		public void handlePostEachTick( int i ) throws ChartException
		{
			if ( i == da.length - 2 )
			{
				// This is the last tick, use pre-computed value to
				// handle non-equal scale unit case.
				dAxisValue = Methods.asDouble( sc.getMaximum( ) ).doubleValue( );
			}
			else
			{
				dAxisValue += dAxisStep;
			}
		}

		public void handlePreEachTick( int i ) throws ChartException
		{
			if ( bRenderAxisLabels && sc.isTickLabelVisible( i ) )
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
			}
		}

		public void initialize( ) throws ChartException
		{
			dAxisValue = Methods.asDouble( sc.getMinimum( ) ).doubleValue( );
			dAxisStep = Methods.asDouble( sc.getStep( ) ).doubleValue( );
			if ( fs == null )
			{
				df = sc.computeDecimalFormat( dAxisValue, dAxisStep );
			}
		}

	}

	private final class LogAxisTypeComputation implements IAxisTypeComputation
	{

		double dAxisValue;
		double dAxisStep;

		public void close( ) throws ChartException
		{
			// TODO Auto-generated method stub

		}

		public void handlePostEachTick( int i ) throws ChartException
		{
			dAxisValue *= dAxisStep;
		}

		public void handlePreEachTick( int i ) throws ChartException
		{
			// PERFORM COMPUTATIONS ONLY IF AXIS LABEL IS VISIBLE
			if ( bRenderAxisLabels )
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
			}
		}

		public void initialize( ) throws ChartException
		{
			dAxisValue = Methods.asDouble( sc.getMinimum( ) ).doubleValue( );
			dAxisStep = Methods.asDouble( sc.getStep( ) ).doubleValue( );
			if ( fs == null )
			{
				df = sc.computeDecimalFormat( dAxisValue, dAxisStep );
			}
		}

	}

	private final class DatetimeAxisTypeComputation implements
			IAxisTypeComputation
	{

		CDateTime cdt, cdtAxisValue;
		int iUnit, iStep;
		IDateFormatWrapper sdf;

		public void close( ) throws ChartException
		{
			// TODO Auto-generated method stub

		}

		public void handlePostEachTick( int i ) throws ChartException
		{
			// ALWAYS W.R.T START VALUE
			cdt = cdtAxisValue.forward( iUnit, iStep * ( i + 1 ) );
		}

		public void handlePreEachTick( int i ) throws ChartException
		{
			try
			{
				sText = ValueFormatter.format( cdt, fs, ax.getRunTimeContext( )
						.getULocale( ), sdf );
			}
			catch ( ChartException dfex )
			{
				logger.log( dfex );
				sText = IConstants.NULL_STRING;
			}
			la.getCaption( ).setValue( sText );
		}

		public void initialize( ) throws ChartException
		{
			cdtAxisValue = Methods.asDateTime( sc.getMinimum( ) );
			iUnit = Methods.asInteger( sc.getUnit( ) );
			iStep = Methods.asInteger( sc.getStep( ) );
			if ( fs == null )
			{
				sdf = DateFormatWrapperFactory.getPreferredDateFormat( iUnit,
						getRunTimeContext( ).getULocale( ) );
			}
			cdt = cdtAxisValue;
		}

	}

	IAxisTypeComputation createAxisTypeComputation( ComputationContext context )
			throws ChartException
	{
		if ( ( sc.getType( ) & IConstants.TEXT ) == IConstants.TEXT
				|| sc.isCategoryScale( ) )
		{
			return new TextAxisTypeComputation( context );
		}
		else if ( ( sc.getType( ) & IConstants.LINEAR ) == IConstants.LINEAR )
		{
			return new LinearAxisTypeComputation( );
		}
		else if ( ( sc.getType( ) & IConstants.LOGARITHMIC ) == IConstants.LOGARITHMIC )
		{
			return new LogAxisTypeComputation( );
		}
		else if ( ( sc.getType( ) & IConstants.DATE_TIME ) == IConstants.DATE_TIME )
		{
			return new DatetimeAxisTypeComputation( );
		}
		throw new ChartException( ChartEnginePlugin.ID,
				ChartException.RENDERING,
				"exception.undefined.axis.type", //$NON-NLS-1$
				Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
	}

	void renderVerticalAxisByType( ComputationContext context, double dXEnd,
			double dZEnd, double dZ, double dStaggeredLabelOffset )
			throws ChartException
	{
		// The vertical axis direction, -1 means bottom->top, 1 means
		// top->bottom.
		final int iDirection = sc.getDirection( ) != IConstants.FORWARD ? -1
				: 1;
		IAxisTypeComputation computation = createAxisTypeComputation( context );
		computation.initialize( );

		// Offset for Text axis type
		final double dOffset = axModel.isCategoryAxis( ) ? ( axModel.getScale( )
				.isTickBetweenCategories( ) ? iDirection : -iDirection )
				* sc.getUnitSize( )
				/ 2 : 0;
		// Tick size
		final int length = computation instanceof TextAxisTypeComputation ? da.length - 1
				: da.length;

		final double x = ( iLabelLocation == IConstants.LEFT ) ? context.dTick1 - 1
				: context.dTick2 + 1;
		for ( int i = 0; i < length; i++ )
		{
			computation.handlePreEachTick( i );

			int y = (int) da[i];
			if ( bRendering3D )
			{
				context.y3d = (int) da3D[i];
			}
			if ( ( iWhatToDraw & IConstants.AXIS ) == IConstants.AXIS )
			{
				double dXMinorTick1 = ( ( iMinorTickStyle & IConstants.TICK_LEFT ) == IConstants.TICK_LEFT ) ? ( context.dX - IConstants.TICK_SIZE )
						: context.dX;
				double dXMinorTick2 = ( ( iMinorTickStyle & IConstants.TICK_RIGHT ) == IConstants.TICK_RIGHT ) ? context.dX
						+ IConstants.TICK_SIZE
						: context.dX;
				if ( dXMinorTick1 != dXMinorTick2 )
				{
					// RENDER THE MINOR TICKS FIRST (For ALL but the
					// last Major tick)
					if ( i != da.length - 1 )
					{
						if ( bRenderOrthogonal3DAxis )
						{
							// !NOT RENDER TICKS FOR 3D AXES
						}
						else
						{
							LineRenderEvent lreMinor = null;
							for ( int k = 0; k < daMinor.length - 1; k++ )
							{
								if ( computation instanceof LinearAxisTypeComputation )
								{
									// Special case for linear type
									if ( ( iDirection == -1 && y - daMinor[k] <= da[i + 1] )
											|| ( iDirection == 1 && y
													+ daMinor[k] >= da[i + 1] ) )
									{
										// if current minor tick exceed
										// the range of current unit, skip
										continue;
									}
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

				if ( context.dTick1 != context.dTick2 )
				{
					if ( bRenderOrthogonal3DAxis )
					{
						// !NOT RENDER TICKS FOR 3D AXES
					}
					else
					{
						lre.setLineAttributes( liaMajorTick );
						lre.getStart( ).set( context.dTick1, y );
						lre.getEnd( ).set( context.dTick2, y );
						ipr.drawLine( lre );
					}

					if ( iv != null
							&& iDimension == IConstants.TWO_5_D
							&& iv.getType( ) == IntersectionValue.VALUE )
					{
						lre.setStart( LocationImpl.create( context.dX, y ) );
						lre.setEnd( LocationImpl.create( context.dX
								+ dSeriesThickness, y - dSeriesThickness ) );
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
						lo3d.set( sx - pwa.getHorizontalSpacingInPixels( ),
								context.y3d + dOffset,
								dZEnd + pwa.getHorizontalSpacingInPixels( ) );
						t3dre.setLocation3D( lo3d );
						t3dre.setTextPosition( TextRenderEvent.LEFT );
						t3dre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
						dc.addLabel( t3dre );

						// Right wall
						lo3d.set( sx2 + pwa.getHorizontalSpacingInPixels( ),
								context.y3d + dOffset,
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

			computation.handlePostEachTick( i );
		}

		computation.close( );
	}

	void renderHorizontalAxisByType( ComputationContext context, double dXEnd,
			double dZEnd, double dZ, double dStaggeredLabelOffset )
			throws ChartException
	{
		// The horizontal axis direction. -1 means right->left, 1 means
		// left->right.
		final int iDirection = sc.getDirection( ) == IConstants.BACKWARD ? -1
				: 1;
		IAxisTypeComputation computation = createAxisTypeComputation( context );
		computation.initialize( );

		// Offset for Text axis type
		final double dOffset = axModel.isCategoryAxis( )?
				( axModel.getScale( ).isTickBetweenCategories( ) ? iDirection:2*iDirection)
				* sc.getUnitSize( )
				/ 2 : 0;
		// Tick size
		final int length = computation instanceof TextAxisTypeComputation ? da.length - 1
				: da.length;

		double y = ( iLabelLocation == IConstants.ABOVE ) ? ( bRendering3D ? context.dTick1 + 1
				: context.dTick1 - 1 )
				: ( bRendering3D ? context.dTick2 - 1 : context.dTick2 + 1 );
		for ( int i = 0; i < length; i++ )
		{
			computation.handlePreEachTick( i );

			int x = (int) da[i];
			if ( bRendering3D )
			{
				context.x3d = (int) da3D[i];
				context.z3d = (int) da3D[i];
			}
			if ( ( iWhatToDraw & IConstants.AXIS ) == IConstants.AXIS )
			{
				double dYMinorTick1 = ( ( iMinorTickStyle & IConstants.TICK_ABOVE ) == IConstants.TICK_ABOVE ) ? ( bRendering3D ? context.dY
						+ IConstants.TICK_SIZE
						: context.dY - IConstants.TICK_SIZE )
						: context.dY;
				double dYMinorTick2 = ( ( iMinorTickStyle & IConstants.TICK_BELOW ) == IConstants.TICK_BELOW ) ? ( bRendering3D ? context.dY
						- IConstants.TICK_SIZE
						: context.dY + IConstants.TICK_SIZE )
						: context.dY;
				if ( dYMinorTick1 != -dYMinorTick2 )
				{
					// RENDER THE MINOR TICKS FIRST (For ALL but the
					// last Major tick)
					if ( i != da.length - 1 )
					{
						if ( bRenderBase3DAxis )
						{
							// !NOT RENDER TICKS FOR 3D AXES
						}
						else if ( bRenderAncillary3DAxis )
						{
							// !NOT RENDER TICKS FOR 3D AXES
						}
						else
						{
							LineRenderEvent lreMinor = null;
							for ( int k = 0; k < daMinor.length - 1; k++ )
							{
								// Special case for linear type
								if ( computation instanceof LinearAxisTypeComputation )
								{
									if ( ( iDirection == 1 && x + daMinor[k] >= da[i + 1] )
											|| ( iDirection == -1 && x
													- daMinor[k] <= da[i + 1] ) )
									{
										// if current minor tick exceed the
										// range of current unit, skip
										continue;
									}
								}

								lreMinor = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
										LineRenderEvent.class );
								lreMinor.setLineAttributes( liaMinorTick );
								lreMinor.setStart( LocationImpl.create( x
										+ iDirection
										* daMinor[k], dYMinorTick1 ) );
								lreMinor.setEnd( LocationImpl.create( x
										+ iDirection
										* daMinor[k], dYMinorTick2 ) );
								ipr.drawLine( lreMinor );
							}
						}
					}
				}

				if ( context.dTick1 != context.dTick2 )
				{
					if ( bRenderBase3DAxis )
					{
						// !NOT RENDER TICKS FOR 3D AXES
					}
					else if ( bRenderAncillary3DAxis )
					{
						// !NOT RENDER TICKS FOR 3D AXES
					}
					else
					{
						lre.setLineAttributes( liaMajorTick );
						lre.getStart( ).set( x, context.dTick1 );
						lre.getEnd( ).set( x, context.dTick2 );
						ipr.drawLine( lre );
					}

					if ( iv != null
							&& iDimension == IConstants.TWO_5_D
							&& iv.getType( ) == IntersectionValue.VALUE )
					{
						lre.getStart( ).set( x, context.dY );
						lre.getEnd( ).set( x + dSeriesThickness,
								context.dY - dSeriesThickness );
						ipr.drawLine( lre );
					}
				}
			}

			if ( bRenderAxisLabels && sc.isTickLabelVisible( i ) )
			{
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
							lo3d.set( context.x3d + dOffset, sy
									- pwa.getVerticalSpacingInPixels( ), dZEnd
									+ pwa.getVerticalSpacingInPixels( ) );
						}
						else
						{
							lo3d.set( dXEnd + pwa.getVerticalSpacingInPixels( ),
									sy - pwa.getVerticalSpacingInPixels( ),
									context.z3d + dOffset );
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

			computation.handlePostEachTick( i );
		}

		computation.close( );
	}

	/**
	 * Renders the axis.
	 * 
	 * @throws ChartException
	 */
	public final void renderEachAxis( ) throws ChartException
	{
		final double dStaggeredLabelOffset = sc.computeStaggeredAxisLabelOffset( xs,
				la,
				iOrientation );

		if ( !lia.isSetVisible( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.RENDERING,
					"exception.unset.axis.visibility", //$NON-NLS-1$
					Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
		}

		tre.setLabel( la );
		tre.setTextPosition( iLabelLocation );
		tre.setLocation( lo );

		lre.setLineAttributes( lia );
		lre.setStart( LocationImpl.create( 0, 0 ) );
		lre.setEnd( LocationImpl.create( 0, 0 ) );

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
			final ComputationContext context = new ComputationContext( true );

			context.y3d = 0;
			context.dX = dLocation;
			double dZ = 0;

			if ( bRendering3D )
			{
				Location3D l3d = ax.getAxisCoordinate3D( );
				context.dX = l3d.getX( );
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

			context.dTick1 = ( ( iMajorTickStyle & IConstants.TICK_LEFT ) == IConstants.TICK_LEFT ) ? ( context.dX - IConstants.TICK_SIZE )
					: context.dX;
			context.dTick2 = ( ( iMajorTickStyle & IConstants.TICK_RIGHT ) == IConstants.TICK_RIGHT ) ? context.dX
					+ IConstants.TICK_SIZE
					: context.dX;

			if ( ( iWhatToDraw & IConstants.AXIS ) == IConstants.AXIS
					&& lia.isVisible( ) )
			{
				if ( bRenderOrthogonal3DAxis )
				{
					final double dStart = daEndPoints3D[0];
					final double dEnd = daEndPoints3D[1];
					l3dre.setLineAttributes( lia );

					// center
					l3dre.setStart3D( context.dX, dStart, dZ );
					l3dre.setEnd3D( context.dX, dEnd, dZ );
					dc.addLine( l3dre );

					// left
					l3dre.setStart3D( context.dX, dStart, dZEnd );
					l3dre.setEnd3D( context.dX, dEnd, dZEnd );
					dc.addLine( l3dre );

					// right
					l3dre.setStart3D( dXEnd, dStart, dZ );
					l3dre.setEnd3D( dXEnd, dEnd, dZ );
					dc.addLine( l3dre );

					if ( renderer.isInteractivityEnabled( ) )
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
							loaHotspot[0] = Location3DImpl.create( context.dX
									- IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dStart,
									dZ + IConstants.LINE_EXPAND_DOUBLE_SIZE );
							loaHotspot[1] = Location3DImpl.create( context.dX
									+ IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dStart,
									dZ - IConstants.LINE_EXPAND_DOUBLE_SIZE );
							loaHotspot[2] = Location3DImpl.create( context.dX
									+ IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dEnd,
									dZ - IConstants.LINE_EXPAND_DOUBLE_SIZE );
							loaHotspot[3] = Location3DImpl.create( context.dX
									- IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dEnd,
									dZ + IConstants.LINE_EXPAND_DOUBLE_SIZE );
							pre3d.setPoints3D( loaHotspot );
							pre3d.setDoubleSided( true );

							if ( renderer.get3DEngine( ).processEvent( pre3d,
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

							if ( renderer.get3DEngine( ).processEvent( pre3d,
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

							if ( renderer.get3DEngine( ).processEvent( pre3d,
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
						loa[0] = LocationImpl.create( context.dX, dStart );
						loa[1] = LocationImpl.create( context.dX
								+ dSeriesThickness, dStart - dSeriesThickness );
						loa[2] = LocationImpl.create( context.dX
								+ dSeriesThickness, dEnd - dSeriesThickness );
						loa[3] = LocationImpl.create( context.dX, dEnd );

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
					lre.getStart( ).set( context.dX, dStart );
					lre.getEnd( ).set( context.dX, dEnd );
					ipr.drawLine( lre );

					if ( renderer.isInteractivityEnabled( ) )
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

							loaHotspot[0] = LocationImpl.create( context.dX
									- IConstants.LINE_EXPAND_SIZE, dStart );
							loaHotspot[1] = LocationImpl.create( context.dX
									+ IConstants.LINE_EXPAND_SIZE, dStart );
							loaHotspot[2] = LocationImpl.create( context.dX
									+ IConstants.LINE_EXPAND_SIZE, dEnd );
							loaHotspot[3] = LocationImpl.create( context.dX
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

			// Render by axis type
			renderVerticalAxisByType( context,
					dXEnd,
					dZEnd,
					dZ,
					dStaggeredLabelOffset );

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
						.setValue( getRunTimeContext( ).externalizedMessage( sRestoreValue ) );
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
						Bounds cbo = renderer.getPlotBounds( );

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
						if ( ax.getModelAxis( ).getAssociatedAxes( ).size( ) != 0 )
						{
							tre.setBlockAlignment( ChartUtil.transposeAlignment( la.getCaption( )
									.getFont( )
									.getAlignment( ) ) );
						}
						else
						{
							tre.setBlockAlignment( la.getCaption( )
									.getFont( )
									.getAlignment( ) );
						}
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
			final ComputationContext context = new ComputationContext( false );

			context.x3d = 0;
			context.z3d = 0;
			context.dY = dLocation;
			double dX = 0;
			double dZ = 0;

			if ( bRendering3D )
			{
				Location3D l3d = ax.getAxisCoordinate3D( );

				dX = l3d.getX( );
				context.dY = l3d.getY( );
				dZ = l3d.getZ( );
			}

			context.dTick1 = ( ( iMajorTickStyle & IConstants.TICK_ABOVE ) == IConstants.TICK_ABOVE ) ? ( bRendering3D ? context.dY
					+ IConstants.TICK_SIZE
					: context.dY - IConstants.TICK_SIZE )
					: context.dY;
			context.dTick2 = ( ( iMajorTickStyle & IConstants.TICK_BELOW ) == IConstants.TICK_BELOW ) ? ( bRendering3D ? context.dY
					- IConstants.TICK_SIZE
					: context.dY + IConstants.TICK_SIZE )
					: context.dY;

			if ( iv != null
					&& iDimension == IConstants.TWO_5_D
					&& ( ( bTransposed && renderer.isRightToLeft( ) && iv.getType( ) == IntersectionValue.MIN ) || ( !renderer.isRightToLeft( ) && iv.getType( ) == IntersectionValue.MAX ) ) )
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
					l3dre.setStart3D( dStart, context.dY, dZ );
					l3dre.setEnd3D( dEnd, context.dY, dZ );
					dc.addLine( l3dre );

					if ( renderer.isInteractivityEnabled( ) )
					{
						Trigger tg;
						EList elTriggers = axModel.getTriggers( );

						if ( !elTriggers.isEmpty( ) )
						{
							final Polygon3DRenderEvent pre3d = (Polygon3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
									Polygon3DRenderEvent.class );

							Location3D[] loaHotspot = new Location3D[4];
							loaHotspot[0] = Location3DImpl.create( dStart,
									context.dY
											- IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dZ );
							loaHotspot[1] = Location3DImpl.create( dStart,
									context.dY
											+ IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dZ );
							loaHotspot[2] = Location3DImpl.create( dEnd,
									context.dY
											+ IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dZ );
							loaHotspot[3] = Location3DImpl.create( dEnd,
									context.dY
											- IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dZ );
							pre3d.setPoints3D( loaHotspot );
							pre3d.setDoubleSided( true );

							if ( renderer.get3DEngine( ).processEvent( pre3d,
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
					l3dre.setStart3D( dX, context.dY, dStart );
					l3dre.setEnd3D( dX, context.dY, dEnd );
					dc.addLine( l3dre );

					if ( renderer.isInteractivityEnabled( ) )
					{
						Trigger tg;
						EList elTriggers = axModel.getTriggers( );

						if ( !elTriggers.isEmpty( ) )
						{
							final Polygon3DRenderEvent pre3d = (Polygon3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
									Polygon3DRenderEvent.class );

							Location3D[] loaHotspot = new Location3D[4];
							loaHotspot[0] = Location3DImpl.create( dX,
									context.dY
											- IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dStart );
							loaHotspot[1] = Location3DImpl.create( dX,
									context.dY
											+ IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dStart );
							loaHotspot[2] = Location3DImpl.create( dX,
									context.dY
											+ IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dEnd );
							loaHotspot[3] = Location3DImpl.create( dX,
									context.dY
											- IConstants.LINE_EXPAND_DOUBLE_SIZE,
									dEnd );
							pre3d.setPoints3D( loaHotspot );
							pre3d.setDoubleSided( true );

							if ( renderer.get3DEngine( ).processEvent( pre3d,
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
						loa[0] = LocationImpl.create( dStart, context.dY );
						loa[1] = LocationImpl.create( dStart + dSeriesThickness,
								context.dY - dSeriesThickness );
						loa[2] = LocationImpl.create( dEnd + dSeriesThickness,
								context.dY - dSeriesThickness );
						loa[3] = LocationImpl.create( dEnd, context.dY );

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
					lre.getStart( ).set( dStart, context.dY );
					lre.getEnd( ).set( dEnd, context.dY );
					ipr.drawLine( lre );

					if ( renderer.isInteractivityEnabled( ) )
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

							loaHotspot[0] = LocationImpl.create( dStart,
									context.dY - IConstants.LINE_EXPAND_SIZE );
							loaHotspot[1] = LocationImpl.create( dEnd,
									context.dY - IConstants.LINE_EXPAND_SIZE );
							loaHotspot[2] = LocationImpl.create( dEnd,
									context.dY + IConstants.LINE_EXPAND_SIZE );
							loaHotspot[3] = LocationImpl.create( dStart,
									context.dY + IConstants.LINE_EXPAND_SIZE );

							final PolygonRenderEvent pre = (PolygonRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createAxis( axModel ),
									PolygonRenderEvent.class );
							pre.setPoints( loaHotspot );
							iev.setHotSpot( pre );
							ipr.enableInteraction( iev );
						}
					}

				}
			}

			renderHorizontalAxisByType( context,
					dXEnd,
					dZEnd,
					dZ,
					dStaggeredLabelOffset );

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
						.setValue( getRunTimeContext( ).externalizedMessage( sRestoreValue ) ); // EXTERNALIZE
				la.getCaption( )
						.getFont( )
						.setAlignment( renderer.switchTextAlignment( la.getCaption( )
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
						Bounds cbo = renderer.getPlotBounds( );

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
						if ( ax.getModelAxis( ).getAssociatedAxes( ).size( ) != 0 )
						{
							tre.setBlockAlignment( la.getCaption( )
									.getFont( )
									.getAlignment( ) );
						}
						else
						{

							tre.setBlockAlignment( ChartUtil.transposeAlignment( la.getCaption( )
									.getFont( )
									.getAlignment( ) ) );
						}
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
					&& ( ( bTransposed && renderer.isRightToLeft( ) && iv.getType( ) == IntersectionValue.MIN ) || ( !renderer.isRightToLeft( ) && iv.getType( ) == IntersectionValue.MAX ) ) )
			{
				trae.setTranslation( -dSeriesThickness, dSeriesThickness );
				ipr.applyTransformation( trae );
			}
		}
	}
}

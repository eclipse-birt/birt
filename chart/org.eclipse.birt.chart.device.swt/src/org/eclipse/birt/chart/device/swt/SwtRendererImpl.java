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

package org.eclipse.birt.chart.device.swt;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.chart.device.DeviceAdapter;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.device.swt.i18n.Messages;
import org.eclipse.birt.chart.event.ArcRenderEvent;
import org.eclipse.birt.chart.event.AreaRenderEvent;
import org.eclipse.birt.chart.event.ClipRenderEvent;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.ImageRenderEvent;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.TransformationEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.EmbeddedImage;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Composite;

/**
 * This class implements the SWT primitive rendering code for each primitive
 * instruction sent out by the chart generation process.
 */
public class SwtRendererImpl extends DeviceAdapter
{

	/**
	 * A property name that identifies the double-buffered drawing capability.
	 */
	public static final String DOUBLE_BUFFERED = "device.double.buffered"; //$NON-NLS-1$

	private final LinkedHashMap _lhmAllTriggers = new LinkedHashMap( );

	private IDisplayServer _ids;

	private GC _gc = null;

	private IUpdateNotifier _iun = null;

	private SwtEventHandler _eh = null;

	private double dTranslateX = 0;

	private double dTranslateY = 0;

	private double dRotateInDegrees = 0;

	private double dScale = 1;

	static final int CEIL = 1;

	static final int TRUNCATE = 2;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.device.extension/swt" ); //$NON-NLS-1$

	/**
	 * The required zero-argument constructor
	 */
	public SwtRendererImpl( )
	{
		final PluginSettings ps = PluginSettings.instance( );
		try
		{
			_ids = ps.getDisplayServer( "ds.SWT" ); //$NON-NLS-1$
		}
		catch ( ChartException pex )
		{
			logger.log( pex );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IDeviceRenderer#getGraphicsContext()
	 */
	public Object getGraphicsContext( )
	{
		return _gc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IDeviceRenderer#getDisplayServer()
	 */
	public IDisplayServer getDisplayServer( )
	{
		return _ids;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderer#setClip(org.eclipse.birt.chart.event.ClipRenderEvent)
	 */
	public void setClip( ClipRenderEvent cre )
	{
		final Location[] loa = cre.getVertices( );

		if ( loa == null )
		{
			_gc.setClipping( (Region) null );
		}
		else
		{
			Region rgClipping = new Region( );
			rgClipping.add( getCoordinatesAsInts( loa,
					TRUNCATE,
					dTranslateX,
					dTranslateY,
					dScale ) );
			_gc.setClipping( rgClipping );
			rgClipping.dispose( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderer#drawImage(org.eclipse.birt.chart.event.ImageRenderEvent)
	 */
	public void drawImage( ImageRenderEvent pre ) throws ChartException
	{
		if ( pre.getImage( ) == null || pre.getLocation( ) == null )
		{
			return;
		}

		Image img = null;

		if ( pre.getImage( ) instanceof EmbeddedImage )
		{
			try
			{
				ByteArrayInputStream bis = new ByteArrayInputStream( Base64.decodeBase64( ( (EmbeddedImage) pre.getImage( ) ).getData( )
						.getBytes( ) ) );

				img = new org.eclipse.swt.graphics.Image( ( (SwtDisplayServer) _ids ).getDevice( ),
						bis );
			}
			catch ( Exception ilex )
			{
				throw new ChartException( ChartDeviceSwtActivator.ID,
						ChartException.RENDERING,
						ilex );
			}
		}
		else
		{
			try
			{
				final String sUrl = pre.getImage( ).getURL( );
				img = (Image) _ids.loadImage( new URL( sUrl ) );
			}
			catch ( MalformedURLException muex )
			{
				throw new ChartException( ChartDeviceSwtActivator.ID,
						ChartException.RENDERING,
						muex );
			}
		}

		if ( img == null )
		{
			return;
		}

		Location loc = pre.getLocation( );
		Position pos = pre.getPosition( );
		if ( pos == null )
		{
			pos = Position.INSIDE_LITERAL;
		}

		int width = img.getBounds( ).width;
		int height = img.getBounds( ).height;
		int x = (int) loc.getX( );
		int y = (int) loc.getY( );

		switch ( pos.getValue( ) )
		{
			case Position.INSIDE :
			case Position.OUTSIDE :
				x -= width / 2;
				y -= height / 2;
				break;
			case Position.LEFT :
				x -= width;
				y -= height / 2;
				break;
			case Position.RIGHT :
				y -= height / 2;
				break;
			case Position.ABOVE :
				x -= width / 2;
				y -= height;
				break;
			case Position.BELOW :
				x -= width / 2;
				break;
		}

		_gc.drawImage( img, x, y );

		img.dispose( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderer#drawLine(org.eclipse.birt.chart.event.LineRenderEvent)
	 */
	public void drawLine( LineRenderEvent lre ) throws ChartException
	{
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = lre.getLineAttributes( );
		if ( !validateLineAttributes( lre.getSource( ), lia )
				|| lia.getColor( ) == null )
		{
			return;
		}

		// DRAW THE LINE
		final int iOldLineStyle = _gc.getLineStyle( );
		final int iOldLineWidth = _gc.getLineWidth( );
		final Color cFG = (Color) _ids.getColor( lia.getColor( ) );
		int iLineStyle = SWT.LINE_SOLID;
		switch ( lia.getStyle( ).getValue( ) )
		{
			case ( LineStyle.DOTTED                                                                                                                                                                                                                                                                ) :
				iLineStyle = SWT.LINE_DOT;
				break;
			case ( LineStyle.DASH_DOTTED                                                                                                                                                                                                                                                                ) :
				iLineStyle = SWT.LINE_DASHDOT;
				break;
			case ( LineStyle.DASHED                                                                                                                                                                                                                                                                ) :
				iLineStyle = SWT.LINE_DASH;
				break;
		}
		_gc.setLineStyle( iLineStyle );
		_gc.setLineWidth( lia.getThickness( ) );
		final Location lo1 = lre.getStart( );
		final Location lo2 = lre.getEnd( );
		_gc.setForeground( cFG );

		R31Enhance.setAlpha( _gc, lia.getColor( ) );

		_gc.drawLine( (int) ( ( lo1.getX( ) + dTranslateX ) * dScale ),
				(int) ( ( lo1.getY( ) + dTranslateY ) * dScale ),
				(int) ( ( lo2.getX( ) + dTranslateX ) * dScale ),
				(int) ( ( lo2.getY( ) + dTranslateY ) * dScale ) );

		_gc.setLineStyle( iOldLineStyle );
		_gc.setLineWidth( iOldLineWidth );
		cFG.dispose( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderer#drawRectangle(org.eclipse.birt.chart.event.RectangleRenderEvent)
	 */
	public void drawRectangle( RectangleRenderEvent rre ) throws ChartException
	{
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = rre.getOutline( );
		if ( !validateLineAttributes( rre.getSource( ), lia ) )
		{
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor( lia.getColor( ),
				rre.getBackground( ),
				_ids );
		if ( cFG == null )
		{
			return;
		}

		// DRAW THE RECTANGLE WITH THE APPROPRIATE LINE STYLE
		final int iOldLineStyle = _gc.getLineStyle( );
		final int iOldLineWidth = _gc.getLineWidth( );
		int iLineStyle = SWT.LINE_SOLID;
		switch ( lia.getStyle( ).getValue( ) )
		{
			case ( LineStyle.DOTTED                                                                                                                                                                                                                                                                ) :
				iLineStyle = SWT.LINE_DOT;
				break;
			case ( LineStyle.DASH_DOTTED                                                                                                                                                                                                                                                                ) :
				iLineStyle = SWT.LINE_DASHDOT;
				break;
			case ( LineStyle.DASHED                                                                                                                                                                                                                                                                ) :
				iLineStyle = SWT.LINE_DASH;
				break;
		}
		_gc.setLineStyle( iLineStyle );
		_gc.setLineWidth( lia.getThickness( ) );
		final Bounds bo = normalizeBounds( rre.getBounds( ) );
		_gc.setForeground( cFG );

		R31Enhance.setAlpha( _gc, lia.getColor( ) );

		_gc.drawRectangle( (int) ( ( bo.getLeft( ) + dTranslateX ) * dScale ),
				(int) ( ( bo.getTop( ) + dTranslateY ) * dScale ),
				(int) ( bo.getWidth( ) * dScale ) - 1,
				(int) ( bo.getHeight( ) * dScale ) - 1 );

		_gc.setLineStyle( iOldLineStyle );
		_gc.setLineWidth( iOldLineWidth );
		cFG.dispose( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#fillRectangle(org.eclipse.birt.chart.event.RectangleRenderEvent)
	 */
	public void fillRectangle( RectangleRenderEvent rre ) throws ChartException
	{
		final Fill flBackground = validateMultipleFill( rre.getBackground( ) );

		if ( isFullTransparent( flBackground ) )
		{
			return;
		}

		final Bounds bo = normalizeBounds( rre.getBounds( ) );
		final Rectangle r = new Rectangle( (int) ( ( bo.getLeft( ) + dTranslateX ) * dScale ),
				(int) ( ( bo.getTop( ) + dTranslateY ) * dScale ),
				(int) Math.ceil( bo.getWidth( ) * dScale ),
				(int) Math.ceil( bo.getHeight( ) * dScale ) );

		if ( flBackground instanceof ColorDefinition )
		{
			final ColorDefinition cd = (ColorDefinition) flBackground;
			// SKIP FULLY TRANSPARENT
			if ( cd.isSetTransparency( ) && cd.getTransparency( ) == 0 )
			{
				return;
			}
			final Color cBG = (Color) _ids.getColor( cd );
			final Color cPreviousBG = _gc.getBackground( );
			_gc.setBackground( cBG );

			R31Enhance.setAlpha( _gc, cd );

			_gc.fillRectangle( r );

			cBG.dispose( );
			_gc.setBackground( cPreviousBG );
		}
		else if ( flBackground instanceof Gradient )
		{
			final Gradient g = (Gradient) flBackground;
			final ColorDefinition cdStart = g.getStartColor( );
			final ColorDefinition cdEnd = g.getEndColor( );
			double dAngleInDegrees = g.getDirection( );

			if ( dAngleInDegrees < -90 || dAngleInDegrees > 90 )
			{
				throw new ChartException( ChartDeviceSwtActivator.ID,
						ChartException.RENDERING,
						"SwtRendererImpl.exception.gradient.angle", //$NON-NLS-1$
						new Object[]{
							new Double( dAngleInDegrees )
						},
						Messages.getResourceBundle( getULocale( ) ) );
			}

			final Color cPreviousFG = _gc.getForeground( );
			final Color cPreviousBG = _gc.getBackground( );
			Color cFG = (Color) _ids.getColor( cdStart );
			Color cBG = (Color) _ids.getColor( cdEnd );
			final boolean bVertical = ( g.getDirection( ) < -45 || g.getDirection( ) > 45 );
			final boolean bSwapped = ( g.getDirection( ) > 45 );
			if ( bSwapped )
			{
				final Color c = cFG;
				cFG = cBG;
				cBG = c;
			}
			_gc.setForeground( cFG );
			_gc.setBackground( cBG );

			R31Enhance.setAlpha( _gc, g );

			_gc.fillGradientRectangle( r.x, r.y, r.width, r.height, bVertical );

			_gc.setForeground( cPreviousFG );
			_gc.setBackground( cPreviousBG );
			cFG.dispose( );
			cBG.dispose( );
		}
		else if ( flBackground instanceof org.eclipse.birt.chart.model.attribute.Image )
		{
			org.eclipse.swt.graphics.Image img = null;

			if ( flBackground instanceof EmbeddedImage )
			{
				try
				{
					ByteArrayInputStream bis = new ByteArrayInputStream( Base64.decodeBase64( ( (EmbeddedImage) flBackground ).getData( )
							.getBytes( ) ) );

					img = new org.eclipse.swt.graphics.Image( ( (SwtDisplayServer) _ids ).getDevice( ),
							bis );
				}
				catch ( Exception ilex )
				{
					throw new ChartException( ChartDeviceSwtActivator.ID,
							ChartException.RENDERING,
							ilex );
				}
			}
			else
			{
				final String sUrl = ( (org.eclipse.birt.chart.model.attribute.Image) flBackground ).getURL( );
				try
				{
					img = (org.eclipse.swt.graphics.Image) _ids.loadImage( new URL( sUrl ) );
				}
				catch ( ChartException ilex )
				{
					throw new ChartException( ChartDeviceSwtActivator.ID,
							ChartException.RENDERING,
							ilex );
				}
				catch ( MalformedURLException muex )
				{
					throw new ChartException( ChartDeviceSwtActivator.ID,
							ChartException.RENDERING,
							muex );
				}
			}

			final Region rgPreviousClip = new Region( );
			_gc.getClipping( rgPreviousClip );

			final Region rg = new Region( );
			rg.add( rgPreviousClip );
			rg.intersect( r );
			_gc.setClipping( rg );

			final Size szImage = _ids.getSize( img );
			final int iXRepeat = (int) ( Math.ceil( r.width
					/ szImage.getWidth( ) ) );
			final int iYRepeat = (int) ( Math.ceil( r.height
					/ szImage.getHeight( ) ) );
			for ( int i = 0; i < iXRepeat; i++ )
			{
				for ( int j = 0; j < iYRepeat; j++ )
				{
					_gc.drawImage( img,
							(int) ( r.x + i * szImage.getWidth( ) ),
							(int) ( r.y + j * szImage.getHeight( ) ) );
				}
			}

			img.dispose( );
			_gc.setClipping( rgPreviousClip );
			rg.dispose( );
			rgPreviousClip.dispose( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderer#drawPolygon(org.eclipse.birt.chart.event.PolygonRenderEvent)
	 */
	public void drawPolygon( PolygonRenderEvent pre ) throws ChartException
	{
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = pre.getOutline( );
		if ( !validateLineAttributes( pre.getSource( ), lia ) )
		{
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor( lia.getColor( ),
				pre.getBackground( ),
				_ids );
		if ( cFG == null )
		{
			return;
		}

		final int iOldLineStyle = _gc.getLineStyle( );
		final int iOldLineWidth = _gc.getLineWidth( );

		int iLineStyle = SWT.LINE_SOLID;
		switch ( lia.getStyle( ).getValue( ) )
		{
			case ( LineStyle.DOTTED                                                                                                                                                                                                                                                                ) :
				iLineStyle = SWT.LINE_DOT;
				break;
			case ( LineStyle.DASH_DOTTED                                                                                                                                                                                                                                                                ) :
				iLineStyle = SWT.LINE_DASHDOT;
				break;
			case ( LineStyle.DASHED                                                                                                                                                                                                                                                                ) :
				iLineStyle = SWT.LINE_DASH;
				break;
		}

		_gc.setLineStyle( iLineStyle );
		_gc.setLineWidth( lia.getThickness( ) );
		_gc.setForeground( cFG );

		R31Enhance.setAlpha( _gc, lia.getColor( ) );

		_gc.drawPolygon( getCoordinatesAsInts( pre.getPoints( ),
				TRUNCATE,
				dTranslateX,
				dTranslateY,
				dScale ) );

		_gc.setLineStyle( iOldLineStyle );
		_gc.setLineWidth( iOldLineWidth );
		cFG.dispose( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderer#fillPolygon(org.eclipse.birt.chart.event.PolygonRenderEvent)
	 */
	public void fillPolygon( PolygonRenderEvent pre ) throws ChartException
	{
		// DUE TO RESTRICTIVE SWT API, WE SET A CLIPPED POLYGON REGION
		// AND RENDER THE POLYGON BY RENDERING A CONTAINING RECTANGLE WHERE
		// THE RECTANGLE BOUNDS CORRESPOND TO THE POLYGON BOUNDS
		// NOTE: SOME INCOMPLETE PAINTING ERRORS SEEM TO EXIST FOR GRADIENT POLY
		// FILLS

		final Fill flBackground = validateMultipleFill( pre.getBackground( ) );

		if ( isFullTransparent( flBackground ) )
		{
			return;
		}

		final Region rgPreviousClipping = new Region( );
		_gc.getClipping( rgPreviousClipping );

		final Region rg = new Region( );
		rg.add( getCoordinatesAsInts( pre.getPoints( ),
				TRUNCATE,
				dTranslateX,
				dTranslateY,
				dScale ) );
		rg.intersect( rgPreviousClipping );
		_gc.setClipping( rg );

		RectangleRenderEvent rre = (RectangleRenderEvent) ( (EventObjectCache) this ).getEventObject( pre.getSource( ),
				RectangleRenderEvent.class );
		rre.setBackground( flBackground );
		try
		{
			rre.setBounds( pre.getBounds( ) );
			rre.setOutline( pre.getOutline( ) );

			fillRectangle( rre );
		}
		catch ( ChartException ufex )
		{
			throw new ChartException( ChartDeviceSwtActivator.ID,
					ChartException.RENDERING,
					ufex );
		}
		finally
		{
			_gc.setClipping( rgPreviousClipping );
			rg.dispose( );
			rgPreviousClipping.dispose( );
		}

	}

	/**
	 * Extra fix due to SWT arc rendering limitation.
	 * 
	 * @param _gc
	 * @param are
	 * @param dTranslateX
	 * @param dTranslateY
	 * @param dScale
	 */
	protected void drawArc( GC _gc, Device _dv, ArcRenderEvent are,
			double dTranslateX, double dTranslateY, double dScale )
	{
		if ( are.getInnerRadius( ) >= 0
				&& ( are.getOuterRadius( ) > 0 && are.getInnerRadius( ) < are.getOuterRadius( ) )
				|| ( are.getInnerRadius( ) > 0 && are.getOuterRadius( ) <= 0 ) )
		{
			Bounds bo = BoundsImpl.create( are.getTopLeft( ).getX( ),
					are.getTopLeft( ).getY( ),
					are.getWidth( ),
					are.getHeight( ) );

			Bounds rctOuter, rctInner;

			if ( are.getOuterRadius( ) > 0 )
			{
				rctOuter = BoundsImpl.create( ( ( bo.getLeft( ) + dTranslateX + ( bo.getWidth( ) / 2d - are.getOuterRadius( ) ) ) * dScale ),
						( ( bo.getTop( ) + dTranslateY + ( bo.getHeight( ) / 2d - are.getOuterRadius( ) ) ) * dScale ),
						( 2 * are.getOuterRadius( ) * dScale ),
						( 2 * are.getOuterRadius( ) * dScale ) );
			}
			else
			{
				rctOuter = BoundsImpl.create( ( ( bo.getLeft( ) + dTranslateX ) * dScale ),
						( ( bo.getTop( ) + dTranslateY ) * dScale ),
						( bo.getWidth( ) * dScale ),
						( bo.getHeight( ) * dScale ) );
			}

			if ( are.getInnerRadius( ) > 0 )
			{
				rctInner = BoundsImpl.create( ( ( bo.getLeft( ) + dTranslateX + ( bo.getWidth( ) / 2d - are.getInnerRadius( ) ) ) * dScale ),
						( ( bo.getTop( ) + dTranslateY + ( bo.getHeight( ) / 2d - are.getInnerRadius( ) ) ) * dScale ),
						( 2 * are.getInnerRadius( ) * dScale ),
						( 2 * are.getInnerRadius( ) * dScale ) );
			}
			else
			{
				rctInner = BoundsImpl.create( ( ( bo.getLeft( ) + dTranslateX + bo.getWidth( ) / 2d ) * dScale ),
						( ( bo.getTop( ) + dTranslateY + bo.getHeight( ) / 2d ) * dScale ),
						0,
						0 );
			}

			double startAngle = Math.toRadians( -are.getStartAngle( ) );
			double stopAngle = Math.toRadians( -are.getStartAngle( )
					- are.getAngleExtent( ) );

			double xsOuter = ( rctOuter.getLeft( ) + ( Math.cos( startAngle ) * 0.5 + 0.5 )
					* rctOuter.getWidth( ) );
			double ysOuter = ( rctOuter.getTop( ) + ( Math.sin( startAngle ) * 0.5 + 0.5 )
					* rctOuter.getHeight( ) );

			// double xeOuter = ( rctOuter.getLeft( ) + ( Math.cos( stopAngle )
			// * 0.5 + 0.5 )
			// * rctOuter.getWidth( ) );
			// double yeOuter = ( rctOuter.getTop( ) + ( Math.sin( stopAngle ) *
			// 0.5 + 0.5 )
			// * rctOuter.getHeight( ) );
			//
			// double xsInner = ( rctInner.getLeft( ) + ( Math.cos( startAngle )
			// * 0.5 + 0.5 )
			// * rctInner.getWidth( ) );
			// double ysInner = ( rctInner.getTop( ) + ( Math.sin( startAngle )
			// * 0.5 + 0.5 )
			// * rctInner.getHeight( ) );

			double xeInner = ( rctInner.getLeft( ) + ( Math.cos( stopAngle ) * 0.5 + 0.5 )
					* rctInner.getWidth( ) );
			double yeInner = ( rctInner.getTop( ) + ( Math.sin( stopAngle ) * 0.5 + 0.5 )
					* rctInner.getHeight( ) );

			Path pt = new Path( _dv );
			pt.addArc( (float) rctOuter.getLeft( ),
					(float) rctOuter.getTop( ),
					(float) rctOuter.getWidth( ),
					(float) rctOuter.getHeight( ),
					(float) are.getStartAngle( ),
					(float) are.getAngleExtent( ) );

			pt.lineTo( (float) xeInner, (float) yeInner );

			pt.addArc( (float) rctInner.getLeft( ),
					(float) rctInner.getTop( ),
					(float) rctInner.getWidth( ),
					(float) rctInner.getHeight( ),
					(float) ( are.getStartAngle( ) + are.getAngleExtent( ) ),
					(float) -are.getAngleExtent( ) );

			pt.lineTo( (float) xsOuter, (float) ysOuter );

			_gc.drawPath( pt );

			pt.dispose( );
		}
		else
		{
			if ( are.getStyle( ) == ArcRenderEvent.OPEN )
			{
				_gc.drawArc( (int) ( ( are.getTopLeft( ).getX( ) + dTranslateX ) * dScale ),
						(int) ( ( are.getTopLeft( ).getY( ) + dTranslateY ) * dScale ),
						(int) ( are.getWidth( ) * dScale ),
						(int) ( are.getHeight( ) * dScale ),
						(int) are.getStartAngle( ),
						(int) are.getAngleExtent( ) );
			}
			else
			{
				double xc = ( ( are.getTopLeft( ).getX( ) + dTranslateX + are.getWidth( ) / 2d ) * dScale );
				double yc = ( ( are.getTopLeft( ).getY( ) + dTranslateY + are.getHeight( ) / 2d ) * dScale );

				double xs = 0, ys = 0, xe = 0, ye = 0;

				double angle = Math.toRadians( -are.getStartAngle( ) );

				xs = ( ( are.getTopLeft( ).getX( ) + dTranslateX + ( Math.cos( angle ) * 0.5 + 0.5 )
						* are.getWidth( ) ) * dScale );
				ys = ( ( are.getTopLeft( ).getY( ) + dTranslateY + ( Math.sin( angle ) * 0.5 + 0.5 )
						* are.getHeight( ) ) * dScale );

				angle = Math.toRadians( -are.getStartAngle( )
						- are.getAngleExtent( ) );

				xe = ( ( are.getTopLeft( ).getX( ) + dTranslateX + ( Math.cos( angle ) * 0.5 + 0.5 )
						* are.getWidth( ) ) * dScale );
				ye = ( ( are.getTopLeft( ).getY( ) + dTranslateY + ( Math.sin( angle ) * 0.5 + 0.5 )
						* are.getHeight( ) ) * dScale );

				Path pt = new Path( _dv );
				if ( are.getStyle( ) == ArcRenderEvent.CLOSED )
				{
					pt.addArc( (float) ( ( are.getTopLeft( ).getX( ) + dTranslateX ) * dScale ),
							(float) ( ( are.getTopLeft( ).getY( ) + dTranslateY ) * dScale ),
							(float) ( are.getWidth( ) * dScale ),
							(float) ( are.getHeight( ) * dScale ),
							(float) are.getStartAngle( ),
							(float) are.getAngleExtent( ) );
					// fix in case angle extent is zero.
					pt.moveTo( (float) xe, (float) ye );
					pt.lineTo( (float) xs, (float) ys );

					_gc.drawPath( pt );
				}
				else if ( are.getStyle( ) == ArcRenderEvent.SECTOR )
				{
					pt.addArc( (float) ( ( are.getTopLeft( ).getX( ) + dTranslateX ) * dScale ),
							(float) ( ( are.getTopLeft( ).getY( ) + dTranslateY ) * dScale ),
							(float) ( are.getWidth( ) * dScale ),
							(float) ( are.getHeight( ) * dScale ),
							(float) are.getStartAngle( ),
							(float) are.getAngleExtent( ) );
					// fix in case angle extent is zero.
					pt.moveTo( (float) xe, (float) ye );
					pt.lineTo( (float) xc, (float) yc );
					pt.lineTo( (float) xs, (float) ys );

					_gc.drawPath( pt );
				}
				pt.dispose( );
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderer#drawArc(org.eclipse.birt.chart.event.ArcRenderEvent)
	 */
	public void drawArc( ArcRenderEvent are ) throws ChartException
	{
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = are.getOutline( );
		if ( !validateLineAttributes( are.getSource( ), lia ) )
		{
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor( lia.getColor( ),
				are.getBackground( ),
				_ids );
		if ( cFG == null )
		{
			return;
		}

		// DRAW THE ARC WITH THE SPECIFIED LINE ATTRIBUTES
		final int iOldLineStyle = _gc.getLineStyle( );
		final int iOldLineWidth = _gc.getLineWidth( );
		int iLineStyle = SWT.LINE_SOLID;
		switch ( lia.getStyle( ).getValue( ) )
		{
			case LineStyle.DOTTED :
				iLineStyle = SWT.LINE_DOT;
				break;
			case LineStyle.DASH_DOTTED :
				iLineStyle = SWT.LINE_DASHDOT;
				break;
			case LineStyle.DASHED :
				iLineStyle = SWT.LINE_DASH;
				break;
		}
		_gc.setLineStyle( iLineStyle );
		_gc.setLineWidth( lia.getThickness( ) );
		_gc.setForeground( cFG );

		R31Enhance.setAlpha( _gc, lia.getColor( ) );

		drawArc( _gc,
				( (SwtDisplayServer) _ids ).getDevice( ),
				are,
				dTranslateX,
				dTranslateY,
				dScale );

		_gc.setLineStyle( iOldLineStyle );
		_gc.setLineWidth( iOldLineWidth );
		cFG.dispose( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderer#fillArc(org.eclipse.birt.chart.event.ArcRenderEvent)
	 */
	public void fillArc( ArcRenderEvent are ) throws ChartException
	{
		Fill flBackground = validateMultipleFill( are.getBackground( ) );

		if ( isFullTransparent( flBackground ) )
		{
			return;
		}

		if ( are.getInnerRadius( ) >= 0
				&& ( are.getOuterRadius( ) > 0 && are.getInnerRadius( ) < are.getOuterRadius( ) )
				|| ( are.getInnerRadius( ) > 0 && are.getOuterRadius( ) <= 0 ) )
		{
			Bounds bo = BoundsImpl.create( are.getTopLeft( ).getX( ),
					are.getTopLeft( ).getY( ),
					are.getWidth( ),
					are.getHeight( ) );

			Bounds rctOuter, rctInner;

			if ( are.getOuterRadius( ) > 0 )
			{
				rctOuter = BoundsImpl.create( ( ( bo.getLeft( ) + dTranslateX + ( bo.getWidth( ) / 2d - are.getOuterRadius( ) ) ) * dScale ),
						( ( bo.getTop( ) + dTranslateY + ( bo.getHeight( ) / 2d - are.getOuterRadius( ) ) ) * dScale ),
						( 2 * are.getOuterRadius( ) * dScale ),
						( 2 * are.getOuterRadius( ) * dScale ) );
			}
			else
			{
				rctOuter = BoundsImpl.create( ( ( bo.getLeft( ) + dTranslateX ) * dScale ),
						( ( bo.getTop( ) + dTranslateY ) * dScale ),
						( bo.getWidth( ) * dScale ),
						( bo.getHeight( ) * dScale ) );
			}

			if ( are.getInnerRadius( ) > 0 )
			{
				rctInner = BoundsImpl.create( ( ( bo.getLeft( ) + dTranslateX + ( bo.getWidth( ) / 2d - are.getInnerRadius( ) ) ) * dScale ),
						( ( bo.getTop( ) + dTranslateY + ( bo.getHeight( ) / 2d - are.getInnerRadius( ) ) ) * dScale ),
						( 2 * are.getInnerRadius( ) * dScale ),
						( 2 * are.getInnerRadius( ) * dScale ) );
			}
			else
			{
				rctInner = BoundsImpl.create( ( ( bo.getLeft( ) + dTranslateX + bo.getWidth( ) / 2d ) * dScale ),
						( ( bo.getTop( ) + dTranslateY + bo.getHeight( ) / 2d ) * dScale ),
						0,
						0 );
			}

			double startAngle = Math.toRadians( -are.getStartAngle( ) );
			double stopAngle = Math.toRadians( -are.getStartAngle( )
					- are.getAngleExtent( ) );

			double xsOuter = ( rctOuter.getLeft( ) + ( Math.cos( startAngle ) * 0.5 + 0.5 )
					* rctOuter.getWidth( ) );
			double ysOuter = ( rctOuter.getTop( ) + ( Math.sin( startAngle ) * 0.5 + 0.5 )
					* rctOuter.getHeight( ) );

			double xeInner = ( rctInner.getLeft( ) + ( Math.cos( stopAngle ) * 0.5 + 0.5 )
					* rctInner.getWidth( ) );
			double yeInner = ( rctInner.getTop( ) + ( Math.sin( stopAngle ) * 0.5 + 0.5 )
					* rctInner.getHeight( ) );

			Path pt = new Path( ( (SwtDisplayServer) _ids ).getDevice( ) );
			pt.addArc( (float) rctOuter.getLeft( ),
					(float) rctOuter.getTop( ),
					(float) rctOuter.getWidth( ),
					(float) rctOuter.getHeight( ),
					(float) are.getStartAngle( ),
					(float) are.getAngleExtent( ) );

			pt.lineTo( (float) xeInner, (float) yeInner );

			pt.addArc( (float) rctInner.getLeft( ),
					(float) rctInner.getTop( ),
					(float) rctInner.getWidth( ),
					(float) rctInner.getHeight( ),
					(float) ( are.getStartAngle( ) + are.getAngleExtent( ) ),
					(float) -are.getAngleExtent( ) );

			pt.lineTo( (float) xsOuter, (float) ysOuter );

			if ( flBackground instanceof ColorDefinition )
			{
				final ColorDefinition cd = (ColorDefinition) flBackground;

				// skip full transparency for optimization.
				if ( !( cd.isSetTransparency( ) && cd.getTransparency( ) == 0 ) )
				{
					final Color cBG = (Color) _ids.getColor( cd );
					final Color cPreviousBG = _gc.getBackground( );
					_gc.setBackground( cBG );

					R31Enhance.setAlpha( _gc, cd );

					_gc.fillPath( pt );

					cBG.dispose( );
					_gc.setBackground( cPreviousBG );
				}
			}
			else if ( flBackground instanceof Gradient
					|| flBackground instanceof org.eclipse.birt.chart.model.attribute.Image )
			{
				Region previousClipping = new Region( );
				_gc.getClipping( previousClipping );

				// TODO intersect previous clipping with current path.
				_gc.setClipping( pt );

				RectangleRenderEvent rre = (RectangleRenderEvent) ( (EventObjectCache) this ).getEventObject( are.getSource( ),
						RectangleRenderEvent.class );
				rre.setBackground( are.getBackground( ) );
				try
				{
					rre.setBounds( are.getBounds( ) );
					rre.setOutline( are.getOutline( ) );

					fillRectangle( rre );
				}
				catch ( ChartException ufex )
				{
					pt.dispose( );
					throw new ChartException( ChartDeviceSwtActivator.ID,
							ChartException.RENDERING,
							ufex );
				}
				finally
				{
					_gc.setClipping( previousClipping );
					previousClipping.dispose( );
				}

			}

			pt.dispose( );
		}
		else
		{
			if ( are.getStyle( ) == ArcRenderEvent.SECTOR
					|| ( are.getStyle( ) == ArcRenderEvent.CLOSED && Math.abs( are.getAngleExtent( ) ) >= 360 ) )
			{
				if ( flBackground instanceof ColorDefinition )
				{
					final ColorDefinition cd = (ColorDefinition) flBackground;

					// skip full transparency for optimization.
					if ( !( cd.isSetTransparency( ) && cd.getTransparency( ) == 0 ) )
					{
						final Color cBG = (Color) _ids.getColor( cd );
						final Color cPreviousBG = _gc.getBackground( );
						_gc.setBackground( cBG );

						R31Enhance.setAlpha( _gc, cd );

						_gc.fillArc( (int) ( ( are.getTopLeft( ).getX( ) + dTranslateX ) * dScale ),
								(int) ( ( are.getTopLeft( ).getY( ) + dTranslateY ) * dScale ),
								(int) ( are.getWidth( ) * dScale ),
								(int) ( are.getHeight( ) * dScale ),
								(int) are.getStartAngle( ),
								(int) are.getAngleExtent( ) );

						cBG.dispose( );
						_gc.setBackground( cPreviousBG );
					}
				}
				else if ( flBackground instanceof Gradient
						|| flBackground instanceof org.eclipse.birt.chart.model.attribute.Image )
				{
					double xc = ( ( are.getTopLeft( ).getX( ) + dTranslateX + are.getWidth( ) / 2d ) * dScale );
					double yc = ( ( are.getTopLeft( ).getY( ) + dTranslateY + are.getHeight( ) / 2d ) * dScale );

					double xs = 0, ys = 0;
					double angle = Math.toRadians( -are.getStartAngle( ) );

					xs = ( ( are.getTopLeft( ).getX( ) + dTranslateX + ( Math.cos( angle ) * 0.5 + 0.5 )
							* are.getWidth( ) ) * dScale );
					ys = ( ( are.getTopLeft( ).getY( ) + dTranslateY + ( Math.sin( angle ) * 0.5 + 0.5 )
							* are.getHeight( ) ) * dScale );

					Path pt = new Path( ( (SwtDisplayServer) _ids ).getDevice( ) );

					if ( are.getStyle( ) == ArcRenderEvent.CLOSED )
					{
						pt.addArc( (float) ( ( are.getTopLeft( ).getX( ) + dTranslateX ) * dScale ),
								(float) ( ( are.getTopLeft( ).getY( ) + dTranslateY ) * dScale ),
								(float) ( are.getWidth( ) * dScale ),
								(float) ( are.getHeight( ) * dScale ),
								(float) are.getStartAngle( ),
								(float) are.getAngleExtent( ) );
						pt.lineTo( (float) xs, (float) ys );
					}
					else if ( are.getStyle( ) == ArcRenderEvent.SECTOR )
					{
						pt.addArc( (float) ( ( are.getTopLeft( ).getX( ) + dTranslateX ) * dScale ),
								(float) ( ( are.getTopLeft( ).getY( ) + dTranslateY ) * dScale ),
								(float) ( are.getWidth( ) * dScale ),
								(float) ( are.getHeight( ) * dScale ),
								(float) are.getStartAngle( ),
								(float) are.getAngleExtent( ) );
						pt.lineTo( (float) xc, (float) yc );
						pt.lineTo( (float) xs, (float) ys );
					}

					Region previousClipping = new Region( );
					_gc.getClipping( previousClipping );

					// TODO intersect previous clipping with current path.
					_gc.setClipping( pt );

					RectangleRenderEvent rre = (RectangleRenderEvent) ( (EventObjectCache) this ).getEventObject( are.getSource( ),
							RectangleRenderEvent.class );
					rre.setBackground( are.getBackground( ) );
					try
					{
						rre.setBounds( are.getBounds( ) );
						rre.setOutline( are.getOutline( ) );

						fillRectangle( rre );
					}
					catch ( ChartException ufex )
					{
						throw new ChartException( ChartDeviceSwtActivator.ID,
								ChartException.RENDERING,
								ufex );
					}
					finally
					{
						_gc.setClipping( previousClipping );
						previousClipping.dispose( );
						pt.dispose( );
					}
				}

				return;
			}

			// Extra fix due to SWT arc rendering limitation.
			if ( are.getStyle( ) == ArcRenderEvent.OPEN
					|| are.getStyle( ) == ArcRenderEvent.CLOSED )
			{
				double angle = Math.toRadians( -are.getStartAngle( ) );

				double xs = ( ( are.getTopLeft( ).getX( ) + dTranslateX + ( Math.cos( angle ) * 0.5 + 0.5 )
						* are.getWidth( ) ) * dScale );
				double ys = ( ( are.getTopLeft( ).getY( ) + dTranslateY + ( Math.sin( angle ) * 0.5 + 0.5 )
						* are.getHeight( ) ) * dScale );

				Path pt = new Path( ( (SwtDisplayServer) _ids ).getDevice( ) );
				pt.addArc( (float) ( ( are.getTopLeft( ).getX( ) + dTranslateX ) * dScale ),
						(float) ( ( are.getTopLeft( ).getY( ) + dTranslateY ) * dScale ),
						(float) ( are.getWidth( ) * dScale ),
						(float) ( are.getHeight( ) * dScale ),
						(float) are.getStartAngle( ),
						(float) are.getAngleExtent( ) );

				pt.lineTo( (float) xs, (float) ys );

				if ( flBackground instanceof ColorDefinition )
				{
					final ColorDefinition cd = (ColorDefinition) flBackground;

					// skip full transparency for optimization.
					if ( !( cd.isSetTransparency( ) && cd.getTransparency( ) == 0 ) )
					{
						final Color cBG = (Color) _ids.getColor( cd );
						final Color cPreviousBG = _gc.getBackground( );
						_gc.setBackground( cBG );

						R31Enhance.setAlpha( _gc, cd );

						_gc.fillPath( pt );

						cBG.dispose( );
						_gc.setBackground( cPreviousBG );
					}
				}
				else if ( flBackground instanceof Gradient
						|| flBackground instanceof org.eclipse.birt.chart.model.attribute.Image )
				{
					Region previousClipping = new Region( );
					_gc.getClipping( previousClipping );

					// TODO intersect previous clipping with current path.
					_gc.setClipping( pt );

					RectangleRenderEvent rre = (RectangleRenderEvent) ( (EventObjectCache) this ).getEventObject( are.getSource( ),
							RectangleRenderEvent.class );
					rre.setBackground( are.getBackground( ) );
					try
					{
						rre.setBounds( are.getBounds( ) );
						rre.setOutline( are.getOutline( ) );

						fillRectangle( rre );
					}
					catch ( ChartException ufex )
					{
						pt.dispose( );
						throw new ChartException( ChartDeviceSwtActivator.ID,
								ChartException.RENDERING,
								ufex );
					}
					finally
					{
						_gc.setClipping( previousClipping );
						previousClipping.dispose( );
					}
				}

				pt.dispose( );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderer#enableInteraction(org.eclipse.birt.chart.event.InteractionEvent)
	 */
	public void enableInteraction( InteractionEvent iev ) throws ChartException
	{
		if ( _iun == null )
		{
			logger.log( ILogger.WARNING,
					Messages.getString( "SwtRendererImpl.exception.missing.component.interaction", getULocale( ) ) ); //$NON-NLS-1$
			return;
		}

		final Trigger[] tga = iev.getTriggers( );
		if ( tga == null )
		{
			return;
		}

		Region clipping = new Region( );
		_gc.getClipping( clipping );

		// CREATE AND SETUP THE SHAPES FOR INTERACTION
		TriggerCondition tc;
		ArrayList al;
		final PrimitiveRenderEvent pre = iev.getHotSpot( );
		if ( pre instanceof PolygonRenderEvent )
		{
			final Location[] loa = ( (PolygonRenderEvent) pre ).getPoints( );

			for ( int i = 0; i < tga.length; i++ )
			{
				tc = tga[i].getCondition( );
				al = (ArrayList) _lhmAllTriggers.get( tc );
				if ( al == null )
				{
					al = new ArrayList( 4 ); // UNDER NORMAL CONDITIONS
					_lhmAllTriggers.put( tc, al );
				}
				al.add( new RegionAction( iev.getStructureSource( ),
						loa,
						tga[i].getAction( ),
						dTranslateX,
						dTranslateY,
						dScale,
						clipping ) );
			}
		}
		else if ( pre instanceof RectangleRenderEvent )
		{
			final Bounds bo = ( (RectangleRenderEvent) pre ).getBounds( );

			for ( int i = 0; i < tga.length; i++ )
			{
				tc = tga[i].getCondition( );
				al = (ArrayList) _lhmAllTriggers.get( tc );
				if ( al == null )
				{
					al = new ArrayList( 4 ); // UNDER NORMAL CONDITIONS
					_lhmAllTriggers.put( tc, al );
				}
				al.add( new RegionAction( iev.getStructureSource( ),
						bo,
						tga[i].getAction( ),
						dTranslateX,
						dTranslateY,
						dScale,
						clipping ) );
			}
		}
		else if ( pre instanceof OvalRenderEvent )
		{
			final Bounds boEllipse = ( (OvalRenderEvent) pre ).getBounds( );

			for ( int i = 0; i < tga.length; i++ )
			{
				tc = tga[i].getCondition( );
				al = (ArrayList) _lhmAllTriggers.get( tc );
				if ( al == null )
				{
					al = new ArrayList( 4 ); // UNDER NORMAL CONDITIONS
					_lhmAllTriggers.put( tc, al );
				}

				// using rectangle to simulate the oval due to swt limitation.
				al.add( new RegionAction( iev.getStructureSource( ),
						boEllipse,
						tga[i].getAction( ),
						dTranslateX,
						dTranslateY,
						dScale,
						clipping ) );
			}
		}
		else if ( pre instanceof ArcRenderEvent )
		{
			final ArcRenderEvent are = (ArcRenderEvent) pre;
			final Bounds boEllipse = are.getEllipseBounds( );
			double dStart = are.getStartAngle( );
			double dExtent = are.getAngleExtent( );
			int iArcType = are.getStyle( );

			for ( int i = 0; i < tga.length; i++ )
			{
				tc = tga[i].getCondition( );
				al = (ArrayList) _lhmAllTriggers.get( tc );
				if ( al == null )
				{
					al = new ArrayList( 4 ); // UNDER NORMAL CONDITIONS
					_lhmAllTriggers.put( tc, al );
				}

				// using rectangle to simulate the arc due to swt limitation.
				al.add( new RegionAction( iev.getStructureSource( ),
						boEllipse,
						dStart,
						dExtent,
						iArcType == ArcRenderEvent.SECTOR,
						tga[i].getAction( ),
						dTranslateX,
						dTranslateY,
						dScale,
						clipping ) );
			}
		}
		else if ( pre instanceof AreaRenderEvent )
		{
			final Bounds bo = ( (AreaRenderEvent) pre ).getBounds( );

			for ( int i = 0; i < tga.length; i++ )
			{
				tc = tga[i].getCondition( );
				al = (ArrayList) _lhmAllTriggers.get( tc );
				if ( al == null )
				{
					al = new ArrayList( 4 ); // UNDER NORMAL CONDITIONS
					_lhmAllTriggers.put( tc, al );
				}
				al.add( new RegionAction( iev.getStructureSource( ),
						bo,
						tga[i].getAction( ),
						dTranslateX,
						dTranslateY,
						dScale,
						clipping ) );
			}
		}

		// free the clip region resource.
		clipping.dispose( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderer#drawArea(org.eclipse.birt.chart.event.AreaRenderEvent)
	 */
	public void drawArea( AreaRenderEvent are ) throws ChartException
	{
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = are.getOutline( );
		if ( !validateLineAttributes( are.getSource( ), lia ) )
		{
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor( lia.getColor( ),
				are.getBackground( ),
				_ids );
		if ( cFG == null ) // IF UNDEFINED, EXIT
		{
			return;
		}

		// BUILD THE GENERAL PATH STRUCTURE
		final Path gp = new Path( ( (SwtDisplayServer) _ids ).getDevice( ) );
		PrimitiveRenderEvent pre;
		for ( int i = 0; i < are.getElementCount( ); i++ )
		{
			pre = are.getElement( i );
			if ( pre instanceof ArcRenderEvent )
			{
				final ArcRenderEvent acre = (ArcRenderEvent) pre;

				gp.addArc( (float) acre.getTopLeft( ).getX( ),
						(float) acre.getTopLeft( ).getY( ),
						(float) acre.getWidth( ),
						(float) acre.getHeight( ),
						(float) acre.getStartAngle( ),
						(float) acre.getAngleExtent( ) );
			}
			else if ( pre instanceof LineRenderEvent )
			{
				final LineRenderEvent lre = (LineRenderEvent) pre;
				gp.moveTo( (float) lre.getStart( ).getX( ),
						(float) lre.getStart( ).getY( ) );
				gp.lineTo( (float) lre.getEnd( ).getX( ), (float) lre.getEnd( )
						.getY( ) );
			}
		}

		// DRAW THE PATH
		final int iOldLineStyle = _gc.getLineStyle( );
		final int iOldLineWidth = _gc.getLineWidth( );
		int iLineStyle = SWT.LINE_SOLID;
		switch ( lia.getStyle( ).getValue( ) )
		{
			case ( LineStyle.DOTTED                                                                                                                                                                                                                                                                ) :
				iLineStyle = SWT.LINE_DOT;
				break;
			case ( LineStyle.DASH_DOTTED                                                                                                                                                                                                                                                                ) :
				iLineStyle = SWT.LINE_DASHDOT;
				break;
			case ( LineStyle.DASHED                                                                                                                                                                                                                                                                ) :
				iLineStyle = SWT.LINE_DASH;
				break;
		}
		_gc.setLineStyle( iLineStyle );
		_gc.setLineWidth( lia.getThickness( ) );
		_gc.setForeground( cFG );

		R31Enhance.setAlpha( _gc, lia.getColor( ) );

		_gc.drawPath( gp );

		// Restore state
		_gc.setLineStyle( iOldLineStyle );
		_gc.setLineWidth( iOldLineWidth );

		// Free resource
		gp.dispose( );
		cFG.dispose( );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderer#fillArea(org.eclipse.birt.chart.event.AreaRenderEvent)
	 */
	public void fillArea( AreaRenderEvent are ) throws ChartException
	{
		Fill flBackground = validateMultipleFill( are.getBackground( ) );

		if ( isFullTransparent( flBackground ) )
		{
			return;
		}

		// BUILD THE GENERAL PATH STRUCTURE
		final Path gp = new Path( ( (SwtDisplayServer) _ids ).getDevice( ) );
		PrimitiveRenderEvent pre;
		for ( int i = 0; i < are.getElementCount( ); i++ )
		{
			pre = are.getElement( i );
			if ( pre instanceof ArcRenderEvent )
			{
				final ArcRenderEvent acre = (ArcRenderEvent) pre;

				gp.addArc( (float) acre.getTopLeft( ).getX( ),
						(float) acre.getTopLeft( ).getY( ),
						(float) acre.getWidth( ),
						(float) acre.getHeight( ),
						(float) acre.getStartAngle( ),
						(float) acre.getAngleExtent( ) );
			}
			else if ( pre instanceof LineRenderEvent )
			{
				final LineRenderEvent lre = (LineRenderEvent) pre;
				if ( i == 0 )
				{
					gp.moveTo( (float) lre.getStart( ).getX( ),
							(float) lre.getStart( ).getY( ) );
				}
				gp.lineTo( (float) lre.getEnd( ).getX( ), (float) lre.getEnd( )
						.getY( ) );
			}
		}

		// DRAW THE PATH
		Region previousClipping = new Region( );
		_gc.getClipping( previousClipping );

		// TODO intersect previous clipping with current path.
		_gc.setClipping( gp );

		try
		{
			if ( flBackground instanceof ColorDefinition )
			{
				final ColorDefinition cd = (ColorDefinition) flBackground;

				// skip full transparency for optimization.
				if ( !( cd.isSetTransparency( ) && cd.getTransparency( ) == 0 ) )
				{
					final Color cBG = (Color) _ids.getColor( cd );
					final Color cPreviousBG = _gc.getBackground( );
					_gc.setBackground( cBG );

					R31Enhance.setAlpha( _gc, cd );

					_gc.fillPath( gp );

					cBG.dispose( );
					_gc.setBackground( cPreviousBG );
				}
			}
			else if ( flBackground instanceof Gradient
					|| flBackground instanceof org.eclipse.birt.chart.model.attribute.Image )
			{
				RectangleRenderEvent rre = (RectangleRenderEvent) ( (EventObjectCache) this ).getEventObject( are.getSource( ),
						RectangleRenderEvent.class );
				rre.setBackground( are.getBackground( ) );

				try
				{
					rre.setBounds( are.getBounds( ) );
					rre.setOutline( are.getOutline( ) );

					fillRectangle( rre );
				}
				catch ( ChartException ufex )
				{
					throw new ChartException( ChartDeviceSwtActivator.ID,
							ChartException.RENDERING,
							ufex );
				}
			}
		}
		finally
		{
			// Restore state
			_gc.setClipping( previousClipping );

			// Free resource
			previousClipping.dispose( );
			gp.dispose( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderer#drawOval(org.eclipse.birt.chart.event.OvalRenderEvent)
	 */
	public void drawOval( OvalRenderEvent ore ) throws ChartException
	{
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = ore.getOutline( );
		if ( !validateLineAttributes( ore.getSource( ), lia ) )
		{
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor( lia.getColor( ),
				ore.getBackground( ),
				_ids );
		if ( cFG == null )
		{
			return;
		}

		// DRAW THE OVAL WITH THE SPECIFIED LINE ATTRIBUTES
		final int iOldLineStyle = _gc.getLineStyle( );
		final int iOldLineWidth = _gc.getLineWidth( );
		int iLineStyle = SWT.LINE_SOLID;
		switch ( lia.getStyle( ).getValue( ) )
		{
			case ( LineStyle.DOTTED                                                                                                                                                                                                                                                                ) :
				iLineStyle = SWT.LINE_DOT;
				break;
			case ( LineStyle.DASH_DOTTED                                                                                                                                                                                                                                                                ) :
				iLineStyle = SWT.LINE_DASHDOT;
				break;
			case ( LineStyle.DASHED                                                                                                                                                                                                                                                                ) :
				iLineStyle = SWT.LINE_DASH;
				break;
		}
		_gc.setLineStyle( iLineStyle );
		_gc.setLineWidth( lia.getThickness( ) );
		final Bounds bo = ore.getBounds( );
		_gc.setForeground( cFG );

		R31Enhance.setAlpha( _gc, lia.getColor( ) );

		_gc.drawOval( (int) ( ( bo.getLeft( ) + dTranslateX ) * dScale ),
				(int) ( ( bo.getTop( ) + dTranslateY ) * dScale ),
				(int) ( bo.getWidth( ) * dScale ),
				(int) ( bo.getHeight( ) * dScale ) );

		_gc.setLineStyle( iOldLineStyle );
		_gc.setLineWidth( iOldLineWidth );
		cFG.dispose( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderer#fillOval(org.eclipse.birt.chart.event.OvalRenderEvent)
	 */
	public void fillOval( OvalRenderEvent ore ) throws ChartException
	{
		final Fill flBackground = validateMultipleFill( ore.getBackground( ) );

		if ( isFullTransparent( flBackground ) )
		{
			return;
		}

		final Bounds bo = ore.getBounds( );
		final Rectangle r = new Rectangle( (int) ( ( bo.getLeft( ) + dTranslateX ) * dScale ),
				(int) ( ( bo.getTop( ) + dTranslateY ) * dScale ),
				(int) ( bo.getWidth( ) * dScale ),
				(int) ( bo.getHeight( ) * dScale ) );

		if ( flBackground instanceof ColorDefinition )
		{
			final ColorDefinition cd = (ColorDefinition) flBackground;
			final Color cBG = (Color) _ids.getColor( cd );
			final Color cPreviousBG = _gc.getBackground( );
			_gc.setBackground( cBG );

			R31Enhance.setAlpha( _gc, cd );

			_gc.fillOval( r.x, r.y, r.width, r.height );

			cBG.dispose( );
			_gc.setBackground( cPreviousBG );
		}
		else if ( flBackground instanceof Gradient
				|| flBackground instanceof org.eclipse.birt.chart.model.attribute.Image )
		{
			Path pt = new Path( ( (SwtDisplayServer) _ids ).getDevice( ) );
			pt.addArc( r.x, r.y, r.width, r.height, 0, 360 );

			Region previousClipping = new Region( );
			_gc.getClipping( previousClipping );

			// TODO intersect previous clipping with current path.
			_gc.setClipping( pt );

			RectangleRenderEvent rre = (RectangleRenderEvent) ( (EventObjectCache) this ).getEventObject( ore.getSource( ),
					RectangleRenderEvent.class );
			rre.setBackground( ore.getBackground( ) );
			try
			{
				rre.setBounds( ore.getBounds( ) );
				rre.setOutline( ore.getOutline( ) );

				fillRectangle( rre );
			}
			catch ( ChartException ufex )
			{
				throw new ChartException( ChartDeviceSwtActivator.ID,
						ChartException.RENDERING,
						ufex );
			}
			finally
			{
				_gc.setClipping( previousClipping );
				previousClipping.dispose( );
				pt.dispose( );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderer#drawText(org.eclipse.birt.chart.event.TextRenderEvent)
	 */
	public void drawText( TextRenderEvent tre ) throws ChartException
	{
		SwtTextRenderer tr = SwtTextRenderer.instance( (SwtDisplayServer) _ids );
		switch ( tre.getAction( ) )
		{
			case TextRenderEvent.UNDEFINED :
				throw new ChartException( ChartDeviceSwtActivator.ID,
						ChartException.RENDERING,
						"SwtRendererImpl.exception.unspecified.text.rendering.action",//$NON-NLS-1$
						Messages.getResourceBundle( getULocale( ) ) );

			case TextRenderEvent.RENDER_SHADOW_AT_LOCATION :
				Location lo = (Location) EcoreUtil.copy( tre.getLocation( ) );
				lo.translate( dTranslateX, dTranslateY );
				lo.scale( dScale );
				tr.renderShadowAtLocation( this,
						tre.getTextPosition( ),
						lo,
						tre.getLabel( ) );
				break;

			case TextRenderEvent.RENDER_TEXT_AT_LOCATION :
				lo = (Location) EcoreUtil.copy( tre.getLocation( ) );
				lo.translate( dTranslateX, dTranslateY );
				lo.scale( dScale );

				tr.renderTextAtLocation( this,
						tre.getTextPosition( ),
						lo,
						tre.getLabel( ) );
				break;

			case TextRenderEvent.RENDER_TEXT_IN_BLOCK :
				final Bounds bo = BoundsImpl.copyInstance( tre.getBlockBounds( ) );
				bo.translate( dTranslateX, dTranslateY );
				bo.scale( dScale );

				tr.renderTextInBlock( this,
						bo,
						tre.getBlockAlignment( ),
						tre.getLabel( ) );
				break;
		}
	}

	/**
	 * Converts an array of high-res co-ordinates into a single dimensional
	 * integer array that represents consecutive X/Y co-ordinates associated
	 * with a polygon's vertices as required in SWT.
	 * 
	 * @param la
	 * @return
	 */
	static final int[] getCoordinatesAsInts( Location[] la, int iRoundingStyle,
			double dTranslateX, double dTranslateY, double dScale )
	{
		final int n = la.length * 2;
		final int[] iaXY = new int[n];

		if ( iRoundingStyle == CEIL )
		{
			for ( int i = 0; i < n / 2; i++ )
			{
				iaXY[2 * i] = (int) Math.ceil( ( la[i].getX( ) + dTranslateX )
						* dScale );
				iaXY[2 * i + 1] = (int) Math.ceil( ( la[i].getY( ) + dTranslateY )
						* dScale );
			}
		}
		else if ( iRoundingStyle == TRUNCATE )
		{
			for ( int i = 0; i < n / 2; i++ )
			{
				iaXY[2 * i] = (int) ( ( la[i].getX( ) + dTranslateX ) * dScale );
				iaXY[2 * i + 1] = (int) ( ( la[i].getY( ) + dTranslateY ) * dScale );
			}
		}

		return iaXY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#applyTransformation(org.eclipse.birt.chart.event.TransformationEvent)
	 */
	public void applyTransformation( TransformationEvent tev )
			throws ChartException
	{
		// NOTE: Transformations are accumulated
		switch ( tev.getTransform( ) )
		{
			case TransformationEvent.TRANSLATE :
				dTranslateX += tev.getTranslateX( );
				dTranslateY += tev.getTranslateY( );
				break;

			case TransformationEvent.ROTATE :
				dRotateInDegrees += tev.getRotation( );
				break;

			case TransformationEvent.SCALE :
				dScale *= tev.getScale( );
				( (SwtDisplayServer) _ids ).setScale( dScale ); // NEEDED TO
				// SCALE FONTS
				break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#start()
	 */
	public void before( ) throws ChartException
	{
		// Clean previous status.
		cleanUpTriggers( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#end()
	 */
	public void after( ) throws ChartException
	{
		// USED BY SUBCLASSES IF NEEDED
	}

	private void cleanUpTriggers( )
	{
		for ( Iterator itr = _lhmAllTriggers.values( ).iterator( ); itr.hasNext( ); )
		{
			List ralist = (List) itr.next( );

			if ( ralist != null )
			{
				for ( Iterator sitr = ralist.iterator( ); sitr.hasNext( ); )
				{
					RegionAction ra = (RegionAction) sitr.next( );
					ra.dispose( );
				}
			}
		}

		_lhmAllTriggers.clear( );
	}

	/**
	 * Free all allocated system resources.
	 */
	public void dispose( )
	{
		cleanUpTriggers( );

		if ( _iun != null )
		{
			Object obj = _iun.peerInstance( );

			if ( obj instanceof Composite )
			{
				Composite jc = (Composite) obj;

				if ( _eh != null )
				{
					if ( !jc.isDisposed( ) )
					{
						// We can't promise to remove all the old
						// swtEventHandler
						// due to SWT limitation here, so be sure to just attach
						// the
						// update_notifier only to one renderer.

						jc.removeMouseListener( _eh );
						jc.removeMouseMoveListener( _eh );
						jc.removeMouseTrackListener( _eh );
						jc.removeKeyListener( _eh );
					}

					_eh.dispose( );
					_eh = null;
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#setProperty(java.lang.String,
	 *      java.lang.Object)
	 */
	public final void setProperty( final String sProperty, final Object oValue )
	{
		if ( sProperty.equals( IDeviceRenderer.UPDATE_NOTIFIER ) )
		{
			_iun = (IUpdateNotifier) oValue;
			cleanUpTriggers( );
			Object obj = _iun.peerInstance( );

			if ( obj instanceof Composite )
			{
				Composite jc = (Composite) obj;

				if ( _eh != null )
				{
					// We can't promise to remove all the old swtEventHandler
					// due to SWT limitation here, so be sure to just attach the
					// update_notifier only to one renderer.

					jc.removeMouseListener( _eh );
					jc.removeMouseMoveListener( _eh );
					jc.removeMouseTrackListener( _eh );
					jc.removeKeyListener( _eh );

					_eh.dispose( );
				}

				_eh = new SwtEventHandler( _lhmAllTriggers, _iun, getULocale( ) );

				jc.addMouseListener( _eh );
				jc.addMouseMoveListener( _eh );
				jc.addMouseTrackListener( _eh );
				jc.addKeyListener( _eh );
			}
		}
		else if ( sProperty.equals( IDeviceRenderer.GRAPHICS_CONTEXT ) )
		{
			_gc = (GC) oValue;

			if ( R31Enhance.isR31Available( ) )
			{
				Region rg = new Region( );
				_gc.getClipping( rg );

				R31Enhance.setAdvanced( _gc, true, rg );
				R31Enhance.setAntialias( _gc, SWT.ON );
				R31Enhance.setTextAntialias( _gc, SWT.ON );

				rg.dispose( );
			}

			logger.log( ILogger.INFORMATION,
					Messages.getString( "SwtRendererImpl.info.graphics.context",//$NON-NLS-1$
							new Object[]{
									_gc.getClass( ).getName( ), _gc
							},
							getULocale( ) ) );
		}
		else if ( sProperty.equals( IDeviceRenderer.DPI_RESOLUTION ) )
		{
			getDisplayServer( ).setDpiResolution( ( (Integer) oValue ).intValue( ) );
		}
	}

	/**
	 * Make bounds height/width always positive.
	 * 
	 * @param bo
	 * @return
	 */
	protected static final Bounds normalizeBounds( Bounds bo )
	{
		if ( bo.getHeight( ) < 0 )
		{
			bo.setTop( bo.getTop( ) + bo.getHeight( ) );
			bo.setHeight( -bo.getHeight( ) );
		}

		if ( bo.getWidth( ) < 0 )
		{
			bo.setLeft( bo.getLeft( ) + bo.getWidth( ) );
			bo.setWidth( -bo.getWidth( ) );
		}

		return bo;
	}

}

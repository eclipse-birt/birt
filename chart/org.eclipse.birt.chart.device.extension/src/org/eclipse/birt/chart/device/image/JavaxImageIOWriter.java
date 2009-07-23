/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteWarningListener;
import javax.imageio.stream.ImageOutputStream;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IImageMapEmitter;
import org.eclipse.birt.chart.device.ImageWriterFactory;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.plugin.ChartDeviceExtensionPlugin;
import org.eclipse.birt.chart.device.swing.ShapedAction;
import org.eclipse.birt.chart.device.swing.SwingRendererImpl;
import org.eclipse.birt.chart.device.util.CSSHelper;
import org.eclipse.birt.chart.device.util.HTMLAttribute;
import org.eclipse.birt.chart.device.util.HTMLTag;
import org.eclipse.birt.chart.device.util.ScriptUtil;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.ActionValue;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.CursorType;
import org.eclipse.birt.chart.model.attribute.MultiURLValues;
import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.SecurityUtil;
import org.eclipse.birt.core.script.JavascriptEvalUtil;

/**
 * JavaxImageIOWriter
 */
public abstract class JavaxImageIOWriter extends SwingRendererImpl implements
		IIOWriteWarningListener,
		IImageMapEmitter
{

	protected Image _img = null;

	protected Object _oOutputIdentifier = null;

	private Bounds _bo = null;

	private boolean _bImageExternallySpecified = false;
	
	private boolean _bAltEnabled = false;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.device.extension/image" ); //$NON-NLS-1$

	private final static String NO_OP_JAVASCRIPT = "javascript:void(0);"; //$NON-NLS-1$

	private final static String POLY_SHAPE = "poly"; //$NON-NLS-1$

	// Use this registry to make sure one callback method only be added once
	private Map<String, Boolean> callbackMethodsRegistry = new HashMap<String, Boolean>( 5 );

	private volatile boolean hasMultipleMenu = false;
	
	private volatile boolean hasAddedMenuLib = false;
	
	/**
	 * Returns the output format string for this writer.
	 * 
	 * @return
	 */
	protected abstract String getFormat( );

	/**
	 * Returns the output image type for this writer.
	 * 
	 * @see java.awt.image.BufferedImage#TYPE_INT_RGB
	 * @see java.awt.image.BufferedImage#TYPE_INT_ARGB
	 * @see java.awt.image.BufferedImage#TYPE_INT_ARGB_PRE
	 * @see java.awt.image.BufferedImage#TYPE_INT_BGR
	 * @see java.awt.image.BufferedImage#TYPE_3BYTE_BGR
	 * @see java.awt.image.BufferedImage#TYPE_4BYTE_ABGR
	 * @see java.awt.image.BufferedImage#TYPE_4BYTE_ABGR_PRE
	 * @see java.awt.image.BufferedImage#TYPE_BYTE_GRAY
	 * @see java.awt.image.BufferedImage#TYPE_USHORT_GRAY
	 * @see java.awt.image.BufferedImage#TYPE_BYTE_BINARY
	 * @see java.awt.image.BufferedImage#TYPE_BYTE_INDEXED
	 * @see java.awt.image.BufferedImage#TYPE_USHORT_565_RGB
	 * @see java.awt.image.BufferedImage#TYPE_USHORT_555_RGB
	 * 
	 * @return
	 */
	protected abstract int getImageType( );
	
	/**
	 * Returns true if the image type supports transparency
	 * false otherwise
	 * @return
	 */
	protected boolean supportsTransparency( )
	{
		return true;
	}

	JavaxImageIOWriter( )
	{
		// By default do not cache images on disk
		ImageIO.setUseCache( false );
	}

	/**
	 * Updates the writer's parameters.
	 * 
	 * @param iwp
	 */
	protected void updateWriterParameters( ImageWriteParam iwp )
	{
		// OPTIONALLY IMPLEMENTED BY SUBCLASS
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IImageMapEmitter#getImageMap()
	 */
	public String getImageMap( )
	{
		List<ShapedAction> saList = getShapeActions( );

		Collections.sort( saList, new Comparator<ShapedAction>( ) {

			public int compare( ShapedAction o1, ShapedAction o2 )
			{

				return o2.getZOrder( ) - o1.getZOrder( );
			}

		} );

		if ( saList == null || saList.size( ) == 0 )
		{
			return null;
		}

		// Generate image map using associated trigger list.
		StringBuffer sb = new StringBuffer( );
		// reverse output
		for ( ListIterator<ShapedAction> iter = saList.listIterator( saList.size( ) ); iter.hasPrevious( ); )
		{
			ShapedAction sa = iter.previous( );
			userCallback( sa, sb );
			
			String coords = shape2polyCoords( sa.getShape( ) );
			if ( coords != null )
			{
				HTMLTag tag = new HTMLTag( "area" ); //$NON-NLS-1$
				tag.addAttribute( HTMLAttribute.SHAPE, POLY_SHAPE );
				tag.addAttribute( HTMLAttribute.COORDS, coords );
				// #258627 "area" must has a "alt" value.
				// Add alt value if specified via extend property
				tag.addAttribute( HTMLAttribute.ALT,
						_bAltEnabled ? sa.getSource( ).getSource( ).toString( ) : "" ); //$NON-NLS-1$
				
				// Update cursor.
				setCursorAttribute( tag, sa );
				
				boolean changed = false;
				changed |= processOnFocus( sa, tag );
				changed |= processOnBlur( sa, tag );
				changed |= processOnClick( sa, tag );
				changed |= processOnDoubleClick( sa, tag );
				changed |= processOnMouseOver( sa, tag );
				if ( changed )
				{
					sb.append( tag );
				}
			}
		}

		return sb.toString( );

	}
	
	private void setCursorAttribute( HTMLTag tag, ShapedAction sa )
	{
		if ( sa.getCursor( ) == null || sa.getCursor( ).getType( ) == CursorType.AUTO )
		{
			return;
		}
		
		String value = CSSHelper.getCSSCursorValue( sa.getCursor( ) );
		if ( value != null )
		{
			tag.addAttribute( HTMLAttribute.STYLE,  value );
		}
	}

	private boolean processCommonEvent( ShapedAction sa, HTMLTag tag,
			TriggerCondition condition, HTMLAttribute htmlAttr )
	{
		Action ac = sa.getActionForCondition( condition );
		if ( checkSupportedAction( ac ) )
		{
			switch ( ac.getType( ).getValue( ) )
			{
				case ActionType.URL_REDIRECT :
					if ( ac.getValue( ) instanceof MultiURLValues )
					{
						List<URLValue> validURLValues = MultiURLValuesScriptGenerator.getValidURLValues( (MultiURLValues) ac.getValue( ) );
						int size = validURLValues.size( );
						if ( size == 0 )
						{
							setTooltipAttribute( tag,
									( (MultiURLValues) ac.getValue( ) ).getTooltip( ) );
							return false;
						}
						else if ( size == 1 )
						{
							URLValue uv = validURLValues.get( 0 );
							setURLValueAttributes( tag, condition, htmlAttr, uv );
							setTooltipAttribute( tag, uv.getTooltip( ) );
							return true;
						}
						else
						{
							setTooltipAttribute( tag,
									( (MultiURLValues) ac.getValue( ) ).getTooltip( ) );
							setAttributesWithScript( sa,
									tag,
									condition,
									htmlAttr );
							return true;
						}
					}
					else
					{
						URLValue uv = (URLValue) ac.getValue( );
						setURLValueAttributes( tag, condition, htmlAttr, uv );
						return true;
					}
					
				case ActionType.SHOW_TOOLTIP :
					// for onmouseover only.
					return false;
				case ActionType.INVOKE_SCRIPT :
					setAttributesWithScript( sa,
							tag,
							condition,
							htmlAttr );
					return true;
			}
		}
		return false;
	}

	/**
	 * @param tag
	 * @param tooltip
	 */
	private void setTooltipAttribute( HTMLTag tag, String tooltip )
	{
		if ( tooltip != null && tooltip.trim( ).length( ) > 0 )
		{
			tag.addAttribute( HTMLAttribute.TITLE,
					eval2HTML( tooltip ) );
		}
	}

	/**
	 * @param sa
	 * @param tag
	 * @param condition
	 * @param htmlAttr
	 */
	private void setAttributesWithScript( ShapedAction sa, HTMLTag tag,
			TriggerCondition condition, HTMLAttribute htmlAttr )
	{
		tag.addAttribute( HTMLAttribute.HREF, NO_OP_JAVASCRIPT );
		final DataPointHints dph;
		if ( StructureType.SERIES_DATA_POINT.equals( sa.getSource( )
				.getType( ) ) )
		{
			dph = (DataPointHints) sa.getSource( ).getSource( );
		}
		else
		{
			dph = null;
		}
		StringBuffer callbackFunction = new StringBuffer( getJSMethodName( condition,
				sa ) );
		callbackFunction.append( "(event" );//$NON-NLS-1$
		ScriptUtil.script( callbackFunction, dph );
		callbackFunction.append( ");" ); //$NON-NLS-1$
		tag.addAttribute( htmlAttr,
				eval2JS( callbackFunction.toString( ), true ) );
	}

	/**
	 * @param tag
	 * @param condition
	 * @param htmlAttr
	 * @param uv
	 */
	private void setURLValueAttributes( HTMLTag tag,
			TriggerCondition condition, HTMLAttribute htmlAttr, URLValue uv )
	{
		// Add tooltip.
		String tooltip = uv.getTooltip( );
		setTooltipAttribute( tag, tooltip );

		if ( condition == TriggerCondition.ONCLICK_LITERAL )
		{
			// only click event uses href to redirect
			tag.addAttribute( HTMLAttribute.HREF,
					eval2HTML( uv.getBaseUrl( ) ) );
			// #258627: "target" can't be a empty String. You
			// shouldn't output target when it's empty.
			if ( uv.getTarget( ) != null )
			{
				tag.addAttribute( HTMLAttribute.TARGET, uv.getTarget( ) );
			}
		}
		else
		{
			tag.addAttribute( HTMLAttribute.HREF,
					NO_OP_JAVASCRIPT );
			String value = getJsURLRedirect( uv );
			if ( htmlAttr.equals( HTMLAttribute.ONFOCUS ) )
			{
				value = "this.blur();" + value;//$NON-NLS-1$
			}
			tag.addAttribute( htmlAttr, value );
		}
	}

	protected boolean processOnFocus( ShapedAction sa, HTMLTag tag )
	{
		// 1. onfocus
		return processCommonEvent( sa,
				tag,
				TriggerCondition.ONFOCUS_LITERAL,
				HTMLAttribute.ONFOCUS );
	}

	protected boolean processOnBlur( ShapedAction sa, HTMLTag tag )
	{
		// 2. onblur
		return processCommonEvent( sa,
				tag,
				TriggerCondition.ONBLUR_LITERAL,
				HTMLAttribute.ONBLUR );
	}

	protected boolean processOnClick( ShapedAction sa, HTMLTag tag )
	{
		// 3. onclick
		return processCommonEvent( sa,
				tag,
				TriggerCondition.ONCLICK_LITERAL,
				HTMLAttribute.ONCLICK );
	}
	
	protected boolean processOnDoubleClick( ShapedAction sa, HTMLTag tag )
	{
		// ondblclick
		return processCommonEvent( sa,
				tag,
				TriggerCondition.ONDBLCLICK_LITERAL,
				HTMLAttribute.ONDBLCLICK );
	}

	protected boolean processOnMouseOver( ShapedAction sa, HTMLTag tag )
	{
		// 4. onmouseover
		Action ac = sa.getActionForCondition( TriggerCondition.ONMOUSEOVER_LITERAL );
		if ( checkSupportedAction( ac ) )
		{
			switch ( ac.getType( ).getValue( ) )
			{
				case ActionType.URL_REDIRECT :
					// not for onmouseover.
					return false;
				case ActionType.SHOW_TOOLTIP :
					TooltipValue tv = (TooltipValue) ac.getValue( );
					// only add valid tooltip
					if ( tv.getText( ) != null && tv.getText( ).length( ) > 0 )
					{
						tag.addAttribute( HTMLAttribute.TITLE,
								eval2HTML( tv.getText( ) ) );
						return true;
					}
					return false;
				case ActionType.INVOKE_SCRIPT :
					final DataPointHints dph;
					if ( StructureType.SERIES_DATA_POINT.equals( sa.getSource( )
							.getType( ) ) )
					{
						dph = (DataPointHints) sa.getSource( ).getSource( );
					}
					else
					{
						dph = null;
					}
					StringBuffer callbackFunction = new StringBuffer( getJSMethodName( TriggerCondition.ONMOUSEOVER_LITERAL,
							sa ) );
					callbackFunction.append( "(event" ); //$NON-NLS-1$
					ScriptUtil.script( callbackFunction, dph );
					callbackFunction.append( ");" ); //$NON-NLS-1$
					tag.addAttribute( HTMLAttribute.ONMOUSEOVER,
							eval2JS( callbackFunction.toString(), true ) );
					return true;
			}
		}
		return false;
	}

	protected String getJsURLRedirect( URLValue uv )
	{
		if ( uv.getBaseUrl( ).startsWith( "javascript:" ) ) //$NON-NLS-1$
		{
			return uv.getBaseUrl( );
		}
		if ( uv.getBaseUrl( ).startsWith( "#" ) ) //$NON-NLS-1$
		{
			return "window.location='" + eval2HTML( uv.getBaseUrl( ) ) + "'"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return "window.open('" //$NON-NLS-1$
				+ eval2HTML( uv.getBaseUrl( ) )
				+ "','" + ( uv.getTarget( ) == null ? "self" : uv.getTarget( ) ) + "')"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

	}

	/**
	 * Convert AWT shape to image map coordinates.
	 * 
	 * @param shape
	 * @return
	 */
	private String shape2polyCoords( Shape shape )
	{
		if ( shape == null )
		{
			return null;
		}

		ArrayList<Double> al = new ArrayList<Double>( );

		FlatteningPathIterator pitr = new FlatteningPathIterator( shape.getPathIterator( null ),
				1 );
		double[] data = new double[6];

		while ( !pitr.isDone( ) )
		{
			int type = pitr.currentSegment( data );

			switch ( type )
			{
				case PathIterator.SEG_MOVETO :
					al.add( new Double( data[0] ) );
					al.add( new Double( data[1] ) );
					break;
				case PathIterator.SEG_LINETO :
					al.add( new Double( data[0] ) );
					al.add( new Double( data[1] ) );
					break;
				case PathIterator.SEG_QUADTO :
					al.add( new Double( data[0] ) );
					al.add( new Double( data[1] ) );
					al.add( new Double( data[2] ) );
					al.add( new Double( data[3] ) );
					break;
				case PathIterator.SEG_CUBICTO :
					al.add( new Double( data[0] ) );
					al.add( new Double( data[1] ) );
					al.add( new Double( data[2] ) );
					al.add( new Double( data[3] ) );
					al.add( new Double( data[4] ) );
					al.add( new Double( data[5] ) );
					break;
				case PathIterator.SEG_CLOSE :
					break;
			}

			pitr.next( );
		}

		if ( al.size( ) == 0 )
		{
			return null;
		}

		StringBuffer sb = new StringBuffer( );

		for ( int i = 0; i < al.size( ); i++ )
		{
			Double db = al.get( i );
			if ( i > 0 )
			{
				sb.append( "," ); //$NON-NLS-1$
			}
			sb.append( (int) db.doubleValue( ) );
		}

		return sb.toString( );
	}

	private boolean checkSupportedAction( Action action )
	{
		return ( action != null && ( action.getType( ) == ActionType.URL_REDIRECT_LITERAL
				|| action.getType( ) == ActionType.SHOW_TOOLTIP_LITERAL || action.getType( ) == ActionType.INVOKE_SCRIPT_LITERAL ) );
	}

	/**
	 * Returns if the given format type or MIME type is supported by the
	 * registered JavaxImageIO writers.
	 * 
	 * @return
	 */
	protected boolean isSupportedByJavaxImageIO( )
	{
		boolean supported = false;

		// Search for writers using format type.
		String s = getFormat( );
		if ( s != null )
		{
			Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName( s );
			if ( it.hasNext( ) )
			{
				supported = true;
			}
		}

		// Search for writers using MIME type.
		if ( !supported )
		{
			s = getMimeType( );
			if ( s != null )
			{
				Iterator<ImageWriter> it = ImageIO.getImageWritersByMIMEType( s );
				if ( it.hasNext( ) )
				{
					supported = true;
				}
			}
		}

		return supported;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#before()
	 */
	public void before( ) throws ChartException
	{
		super.before( );

		_bImageExternallySpecified = ( _img != null );

		// IF A CACHED IMAGE STRATEGY IS NOT USED, CREATE A NEW INSTANCE
		// EVERYTIME
		if ( !_bImageExternallySpecified )
		{
			if ( _bo == null ) // BOUNDS MUST BE SPECIFIED BEFORE RENDERING
			// BEGINS
			{
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.RENDERING,
						"JavaxImageIOWriter.exception.no.bounds", //$NON-NLS-1$
						Messages.getResourceBundle( getULocale( ) ) );
			}

			if ( (int) _bo.getWidth( ) < 0 || (int) _bo.getHeight( ) < 0 )
			{
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.INVALID_IMAGE_SIZE,
						"JavaxImageIOWriter.exception.invalid.image.size", //$NON-NLS-1$
						Messages.getResourceBundle( getULocale( ) ) );
			}
			
			if ( (int) _bo.getWidth( ) == 0 || (int) _bo.getHeight( ) == 0 )
			{
				// Zero size is forbidden in BufferedImage, so replace the size
				// with 1 to make it seem invisible
				_bo.setWidth( 1 );
				_bo.setHeight( 1 );
			}

			// CREATE THE IMAGE INSTANCE
			_img = new BufferedImage( (int) Math.round( _bo.getWidth( ) ),
					(int) Math.round( _bo.getHeight( ) ),
					getImageType( ) );
		}
		super.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, _img.getGraphics( ) );

		if ( !supportsTransparency( ) )
		{
			// Paint image white to avoid black background
			_g2d.setPaint( Color.WHITE );
			_g2d.fillRect( 0, 0, _img.getWidth( null ), _img.getHeight( null ) );
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#after()
	 */
	public void after( ) throws ChartException
	{
		super.after( );

		if ( _oOutputIdentifier != null )
		{

			// SEARCH FOR WRITER USING FORMAT
			ImageWriter iw = ImageWriterFactory.instance( )
					.createByFormatName( getFormat( ) );

			// SEARCH FOR WRITER USING MIME TYPE
			if ( iw == null )
			{
				String s = getMimeType( );

				if ( s == null )
				{
					throw new ChartException( ChartDeviceExtensionPlugin.ID,
							ChartException.RENDERING,
							"JavaxImageIOWriter.exception.no.imagewriter.mimetype.and.format",//$NON-NLS-1$
							new Object[]{
									getMimeType( ),
									getFormat( ),
									getClass( ).getName( )
							},
							Messages.getResourceBundle( getULocale( ) ) );
				}
				Iterator<ImageWriter> it = ImageIO.getImageWritersByMIMEType( s );
				if ( !it.hasNext( ) )
				{
					throw new ChartException( ChartDeviceExtensionPlugin.ID,
							ChartException.RENDERING,
							"JavaxImageIOWriter.exception.no.imagewriter.mimetype", //$NON-NLS-1$
							new Object[]{
								getMimeType( )
							},
							Messages.getResourceBundle( getULocale( ) ) );
				}

				iw = it.next( );
			}

			logger.log( ILogger.INFORMATION,
					Messages.getString( "JavaxImageIOWriter.info.using.imagewriter", getULocale( ) ) //$NON-NLS-1$
							+ getFormat( )
							+ iw.getClass( ).getName( ) );

			// WRITE TO SPECIFIC FILE FORMAT
			final Object o = ( _oOutputIdentifier instanceof String ) ? new File( (String) _oOutputIdentifier )
					: _oOutputIdentifier;
			try
			{
				final ImageOutputStream ios = SecurityUtil.newImageOutputStream( o );
				updateWriterParameters( iw.getDefaultWriteParam( ) ); // SET
				// ANY
				// OUTPUT
				// FORMAT
				// SPECIFIC
				// PARAMETERS
				// IF NEEDED
				iw.setOutput( ios );
				iw.write( (RenderedImage) _img );
				ios.close( );
			}
			catch ( Exception ex )
			{
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.RENDERING,
						ex );
			}
			finally
			{
				iw.dispose( );
			}
		}

		// FLUSH AND RESTORE STATE OF INTERNALLY CREATED IMAGE
		if ( !_bImageExternallySpecified )
		{
			_img.flush( );
			_img = null;
		}

		// ALWAYS DISPOSE THE GRAPHICS CONTEXT THAT WAS CREATED FROM THE IMAGE
		_g2d.dispose( );
		_g2d = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#setProperty(java.lang.String,
	 *      java.lang.Object)
	 */
	public void setProperty( String sProperty, Object oValue )
	{
		super.setProperty( sProperty, oValue );
		if ( sProperty.equals( IDeviceRenderer.EXPECTED_BOUNDS ) )
		{
			_bo = (Bounds) oValue;
		}
		else if ( sProperty.equals( IDeviceRenderer.CACHED_IMAGE ) )
		{
			_img = (Image) oValue;
		}
		else if ( sProperty.equals( IDeviceRenderer.FILE_IDENTIFIER ) )
		{
			_oOutputIdentifier = oValue;
		}
		else if ( sProperty.equals( IDeviceRenderer.CACHE_ON_DISK ) )
		{
			ImageIO.setUseCache( ( (Boolean) oValue ).booleanValue( ) );
		}
		else if ( sProperty.equals( IDeviceRenderer.AREA_ALT_ENABLED ) )
		{
			_bAltEnabled = ( (Boolean) oValue ).booleanValue( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.imageio.event.IIOWriteWarningListener#warningOccurred(javax.imageio.ImageWriter,
	 *      int, java.lang.String)
	 */
	public void warningOccurred( ImageWriter source, int imageIndex,
			String warning )
	{
		logger.log( ILogger.WARNING, warning );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#presentException(java.lang.Exception)
	 */
	public void presentException( Exception cexp )
	{
		if ( _bo == null )
		{
			_bo = BoundsImpl.create( 0, 0, 400, 300 );
		}
		String sWrappedException = cexp.getClass( ).getName( );
		while ( cexp.getCause( ) != null )
		{
			cexp = (Exception) cexp.getCause( );
		}
		String sException = cexp.getClass( ).getName( );
		if ( sWrappedException.equals( sException ) )
		{
			sWrappedException = null;
		}
		String sMessage = cexp.getMessage( );
		StackTraceElement[] stea = cexp.getStackTrace( );
		Dimension d = new Dimension( (int) _bo.getWidth( ),
				(int) _bo.getHeight( ) );

		Font fo = new Font( "Monospaced", Font.BOLD, 14 ); //$NON-NLS-1$
		_g2d.setFont( fo );
		FontMetrics fm = _g2d.getFontMetrics( );
		_g2d.setColor( Color.WHITE );
		_g2d.fillRect( 20, 20, d.width - 40, d.height - 40 );
		_g2d.setColor( Color.BLACK );
		_g2d.drawRect( 20, 20, d.width - 40, d.height - 40 );
		_g2d.setClip( 20, 20, d.width - 40, d.height - 40 );
		int x = 25, y = 20 + fm.getHeight( );
		_g2d.drawString( Messages.getString( "JavaxImageIOWriter.exception.caption", getULocale( ) ), x, y ); //$NON-NLS-1$
		x += fm.stringWidth( Messages.getString( "JavaxImageIOWriter.exception.caption",//$NON-NLS-1$
				getULocale( ) ) ) + 5;
		_g2d.setColor( Color.RED );
		_g2d.drawString( sException, x, y );
		x = 25;
		y += fm.getHeight( );
		if ( sWrappedException != null )
		{
			_g2d.setColor( Color.BLACK );
			_g2d.drawString( Messages.getString( "JavaxImageIOWriter.wrapped.caption", getULocale( ) ), x, y ); //$NON-NLS-1$
			x += fm.stringWidth( Messages.getString( "JavaxImageIOWriter.wrapped.caption",//$NON-NLS-1$
					getULocale( ) ) ) + 5;
			_g2d.setColor( Color.RED );
			_g2d.drawString( sWrappedException, x, y );
			x = 25;
			y += fm.getHeight( );
		}
		_g2d.setColor( Color.BLACK );
		y += 10;
		_g2d.drawString( Messages.getString( "JavaxImageIOWriter.message.caption", getULocale( ) ), x, y ); //$NON-NLS-1$
		x += fm.stringWidth( Messages.getString( "JavaxImageIOWriter.message.caption", getULocale( ) ) ) + 5; //$NON-NLS-1$
		_g2d.setColor( Color.BLUE );
		_g2d.drawString( sMessage, x, y );
		x = 25;
		y += fm.getHeight( );
		_g2d.setColor( Color.BLACK );
		y += 10;
		_g2d.drawString( Messages.getString( "JavaxImageIOWriter.trace.caption", getULocale( ) ), x, y );x = 40;y += fm.getHeight( ); //$NON-NLS-1$
		_g2d.setColor( Color.GREEN.darker( ) );
		for ( int i = 0; i < stea.length; i++ )
		{
			_g2d.drawString( Messages.getString( "JavaxImageIOWriter.trace.detail",//$NON-NLS-1$
					new Object[]{
							stea[i].getClassName( ),
							stea[i].getMethodName( ),
							String.valueOf( stea[i].getLineNumber( ) )
					},
					getULocale( ) ),
					x,
					y );
			x = 40;
			y += fm.getHeight( );
		}

	}

	protected String eval2HTML( String expr )
	{
		if ( expr == null )
		{
			return ""; //$NON-NLS-1$
		}
		StringBuffer result = null;
		char[] s2char = expr.toCharArray( );

		for ( int i = 0, max = s2char.length, delta = 0; i < max; i++ )
		{
			char c = s2char[i];
			String replacement = null;
			// Filters the char not defined.
			if ( !( c == 0x9 || c == 0xA || c == 0xD
					|| ( c >= 0x20 && c <= 0xD7FF ) || ( c >= 0xE000 && c <= 0xFFFD ) ) )
			{
				// Ignores the illegal character.
				replacement = ""; //$NON-NLS-1$
			}
			if ( c == '&' )
			{
				replacement = "&amp;"; //$NON-NLS-1$
			}
			else if ( c == '"' )
			{
				replacement = "&#34;"; //$NON-NLS-1$
			}
			else if ( c == '\r' )
			{
				replacement = "&#13;"; //$NON-NLS-1$
			}
			else if ( c == '<' )
			{
				replacement = "&lt;"; //$NON-NLS-1$
			}
			else if ( c >= 0x80 )
			{
				replacement = "&#x" + Integer.toHexString( c ) + ';'; //$NON-NLS-1$ 
			}
			if ( replacement != null )
			{
				if ( result == null )
				{
					result = new StringBuffer( expr );
				}
				result.replace( i + delta, i + delta + 1, replacement );
				delta += ( replacement.length( ) - 1 );
			}
		}
		if ( result == null )
		{
			return expr;
		}
		return result.toString( );
	}
	
	protected String eval2JS( String expr, boolean bCallback )
	{
		if ( expr == null )
		{
			return ""; //$NON-NLS-1$
		}
		if ( bCallback )
		{
			// Do not eval script since it's not quoted in callback method
			return expr;
		}
		return JavascriptEvalUtil.transformToJsConstants( expr );
	}

	/**
	 * When 1). The action is supported, and the action type is INVOKE_SCRIPT.
	 * 2). The script has not been added into ImageMap. 3). The action acts on
	 * the value series area. Add the script into ImageMap.
	 * 
	 * @param sa
	 *            ShapedAction
	 * @param sb
	 *            StringBuffer
	 */
	private void userCallback( ShapedAction sa, StringBuffer sb )
	{
		addCallbackMethod( sa, sb, TriggerCondition.ONCLICK_LITERAL );
		addCallbackMethod( sa, sb, TriggerCondition.ONDBLCLICK_LITERAL );
		addCallbackMethod( sa, sb, TriggerCondition.ONMOUSEOVER_LITERAL );
		addCallbackMethod( sa, sb, TriggerCondition.ONFOCUS_LITERAL );
		addCallbackMethod( sa, sb, TriggerCondition.ONBLUR_LITERAL );
		
		// Insert chart menu scripts.
		if ( hasMultipleMenu && !hasAddedMenuLib )
		{
			sb.insert( 0, "<Script>" + MultiURLValuesScriptGenerator.getBirtChartMenuLib( ) + "</Script>"); //$NON-NLS-1$ //$NON-NLS-2$
			hasAddedMenuLib = true;
		}
	}
	
	private void addCallbackMethod( ShapedAction sa, StringBuffer sb,
			TriggerCondition condition )
	{
		// Use the event type and action type as the key and function name to
		// handle multiple invoke script events in a chart
		String functionName = getJSMethodName( condition, sa );
		String key = condition.getLiteral( ) + functionName;
		if ( !callbackMethodsRegistry.containsKey( key ) )
		{
			addScriptCallBack( sa,
					sb,
					sa.getActionForCondition( condition ),
					functionName );
			callbackMethodsRegistry.put( key, Boolean.TRUE );
		}
	}
	
	private void addScriptCallBack( ShapedAction sa, StringBuffer sb,
			Action ac, String functionName )
	{
		if ( ac != null )
		{
			// Do not use callback methods for URL_redirect since the target
			// method may be in another page
			if ( ac.getType( ).getValue( ) == ActionType.INVOKE_SCRIPT )
			{
				sb.append( wrapJSMethod( functionName, generateJSContent( ac ) ) );
			}
			else if ( ac.getType( ).getValue( ) == ActionType.URL_REDIRECT ) 
			{
				// Only generate a menu in JS function for multiple URL values.
				if( ac.getValue( ) instanceof MultiURLValues 
						&& ((MultiURLValues)ac.getValue( )).getURLValues( ).size( ) > 1 )
				{
					sb.append( wrapJSMethod( functionName, generateJSContent( ac ) ) );
				}
			}
		}
	}

	private String generateUniqueJSKey( Action ac )
	{
		if ( ac == null )
		{
			return ""; //$NON-NLS-1$
		}
		
		if ( ! (ac.getValue( ) instanceof MultiURLValues ) )
		{
			return generateJSContent( ac );
		}
		MultiURLValues values = (MultiURLValues) ac.getValue( );
		if( values.getURLValues( ).size( ) <= 1 ) {
			return generateJSContent( ac );
		}
		
		// Generate a unique JS key for multiple URL values.
		return new MultiURLValuesScriptGenerator( values ).getJSKey( ) + this.hashCode( );
	}
	
	private String generateJSContent( Action ac )
	{
		if ( ac != null )
		{
			if ( ac.getType( ).getValue( ) == ActionType.INVOKE_SCRIPT )
			{
				ScriptValue sv = (ScriptValue) ac.getValue( );
				return sv.getScript( );
			}
			if ( ac.getType( ).getValue( ) == ActionType.URL_REDIRECT )
			{
				ActionValue value = ac.getValue( );
				if ( value instanceof URLValue )
				{
					URLValue uv = (URLValue) ac.getValue( );
					return getJsURLRedirect( uv );
				}
				else if ( value instanceof MultiURLValues )
				{
					List<URLValue> validURLValues = MultiURLValuesScriptGenerator.getValidURLValues( (MultiURLValues) value );
					if ( validURLValues.size( ) == 0 )
					{
						return ""; //$NON-NLS-1$
					}
					else if ( validURLValues.size( ) == 1 )
					{
						return getJsURLRedirect( validURLValues.get( 0 ) );
					}
					else
					{
						// Return multiple menu javascript.
						hasMultipleMenu  = true;
						return new MultiURLValuesScriptGenerator((MultiURLValues)value ).getJSContent( );
					}
				}
			}
		}
		return ""; //$NON-NLS-1$
	}

	private String wrapJSMethod( String functionName, String functionContent )
	{
		return "<Script>" //$NON-NLS-1$
				+ "function " + functionName + "(evt," + ScriptHandler.BASE_VALUE + ", " + ScriptHandler.ORTHOGONAL_VALUE + ", " + ScriptHandler.SERIES_VALUE + "){" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$  //$NON-NLS-4$  //$NON-NLS-5$
				+ eval2JS( functionContent, true ) + "};</Script>"; //$NON-NLS-1$
	}
	
	private String getJSMethodName( TriggerCondition tc, ShapedAction sa )
	{
		// Bugzilla#203044
		// Always use hashcode of script content to generate function name in
		// case that the functions of two charts may have the same name
		return "userCallBack" //$NON-NLS-1$
				+ Math.abs( generateUniqueJSKey( sa.getActionForCondition( tc ) ).hashCode( ) );
	}
}
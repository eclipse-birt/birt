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

package org.eclipse.birt.chart.device.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteWarningListener;
import javax.imageio.stream.ImageOutputStream;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IImageMapEmitter;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.plugin.ChartDeviceExtensionPlugin;
import org.eclipse.birt.chart.device.swing.ShapedAction;
import org.eclipse.birt.chart.device.swing.SwingRendererImpl;
import org.eclipse.birt.chart.device.util.HTMLAttribute;
import org.eclipse.birt.chart.device.util.HTMLTag;
import org.eclipse.birt.chart.device.util.ScriptUtil;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.data.Action;

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

	private transient boolean _bImageExternallySpecified = false;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.device.extension/image" ); //$NON-NLS-1$

	private final static String NO_OP_JAVASCRIPT = "javascript:void(0);"; //$NON-NLS-1$

	private final static String POLY_SHAPE = "poly"; // //$NON-NLS-1$

	private boolean bAddCallback = false;

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
	 * @see org.eclipse.birt.chart.device.IImageMapEmitter#getMimeType()
	 */
	public abstract String getMimeType( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IImageMapEmitter#getImageMap()
	 */
	public String getImageMap( )
	{
		List saList = getShapeActions( );

		if ( saList == null || saList.size( ) == 0 )
		{
			return null;
		}

		// Generate image map using associated trigger list.
		StringBuffer sb = new StringBuffer( );
		for ( Iterator iter = saList.iterator( ); iter.hasNext( ); )
		{
			ShapedAction sa = (ShapedAction) iter.next( );
			userCallback( sa, sb );

			String coords = shape2polyCoords( sa.getShape( ) );
			if ( coords != null )
			{
				HTMLTag tag = new HTMLTag( "AREA" ); //$NON-NLS-1$
				tag.addAttribute( HTMLAttribute.SHAPE, POLY_SHAPE );
				tag.addAttribute( HTMLAttribute.COORDS, coords );

				boolean changed = false;
				changed |= processOnFocus( sa, tag );
				changed |= processOnBlur( sa, tag );
				changed |= processOnClick( sa, tag );
				changed |= processOnMouseOver( sa, tag );
				if ( changed )
				{
					sb.append( tag );
				}
			}
		}

		return sb.toString( );

	}

	protected boolean processOnFocus( ShapedAction sa, HTMLTag tag )
	{

		// 1. onfocus
		Action ac = sa.getActionForCondition( TriggerCondition.ONFOCUS_LITERAL );
		if ( checkSupportedAction( ac ) )
		{
			switch ( ac.getType( ).getValue( ) )
			{
				case ActionType.URL_REDIRECT :
					URLValue uv = (URLValue) ac.getValue( );
					tag.addAttribute( HTMLAttribute.HREF, NO_OP_JAVASCRIPT );
					tag.addAttribute( HTMLAttribute.ONFOCUS,
							getJsURLRedirect( uv ) );
					return true;
				case ActionType.SHOW_TOOLTIP :
					// for onmouseover only.
					return false;
				case ActionType.INVOKE_SCRIPT :
					ScriptValue sv = (ScriptValue) ac.getValue( );
					tag.addAttribute( HTMLAttribute.HREF, NO_OP_JAVASCRIPT );
					tag.addAttribute( HTMLAttribute.ONFOCUS,
							eval( sv.getScript( ) ) );
					return true;
			}
		}
		return false;
	}

	protected boolean processOnBlur( ShapedAction sa, HTMLTag tag )
	{
		// 2. onblur
		Action ac = sa.getActionForCondition( TriggerCondition.ONFOCUS_LITERAL );
		if ( checkSupportedAction( ac ) )
		{
			switch ( ac.getType( ).getValue( ) )
			{
				case ActionType.URL_REDIRECT :
					URLValue uv = (URLValue) ac.getValue( );
					tag.addAttribute( HTMLAttribute.HREF, NO_OP_JAVASCRIPT );
					tag.addAttribute( HTMLAttribute.ONBLUR,
							getJsURLRedirect( uv ) );
					return true;
				case ActionType.SHOW_TOOLTIP :
					// for onmouseover only.
					return false;
				case ActionType.INVOKE_SCRIPT :
					ScriptValue sv = (ScriptValue) ac.getValue( );
					tag.addAttribute( HTMLAttribute.HREF, NO_OP_JAVASCRIPT );
					tag.addAttribute( HTMLAttribute.ONBLUR,
							eval( sv.getScript( ) ) );
					return true;
			}
		}
		return false;
	}

	protected boolean processOnClick( ShapedAction sa, HTMLTag tag )
	{
		// 3. onclick
		Action ac = sa.getActionForCondition( TriggerCondition.ONCLICK_LITERAL );
		if ( checkSupportedAction( ac ) )
		{
			switch ( ac.getType( ).getValue( ) )

			{
				case ActionType.URL_REDIRECT :
					URLValue uv = (URLValue) ac.getValue( );
					tag.addAttribute( HTMLAttribute.HREF,
							eval( uv.getBaseUrl( ) ) );
					tag.addAttribute( HTMLAttribute.TARGET,
							eval( uv.getTarget( ) ) );
					return true;
				case ActionType.SHOW_TOOLTIP :
					// for onmouseover only.
					return false;
				case ActionType.INVOKE_SCRIPT :
					ScriptValue sv = (ScriptValue) ac.getValue( );
					tag.addAttribute( HTMLAttribute.HREF, NO_OP_JAVASCRIPT );
					if ( StructureType.SERIES_DATA_POINT.equals( sa.getSource( )
							.getType( ) ) )
					{
						final DataPointHints dph = (DataPointHints) sa.getSource( )
								.getSource( );
						String callbackFunction = "userCallBack("; //$NON-NLS-1$
						callbackFunction = ScriptUtil.script( callbackFunction,
								dph );
						callbackFunction += ");"; //$NON-NLS-1$
						tag.addAttribute( HTMLAttribute.ONCLICK,
								eval( callbackFunction ) );
					}
					else
					{
						tag.addAttribute( HTMLAttribute.ONCLICK,
								eval( sv.getScript( ) ) );
					}
					return true;
			}
		}
		return false;
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
					tag.addAttribute( HTMLAttribute.TITLE, eval( tv.getText( ) ) );
					return true;
				case ActionType.INVOKE_SCRIPT :
					// not for onmouseover.
					return false;
			}
		}
		return false;
	}

	protected String getJsURLRedirect( URLValue uv )
	{
		StringBuffer js = new StringBuffer( "window.open('" ); //$NON-NLS-1$
		js.append( eval( uv.getBaseUrl( ) ) );
		js.append( "','" ); //$NON-NLS-1$
		js.append( uv.getTarget( ) == null ? "self" : uv.getTarget( ) ); //$NON-NLS-1$
		js.append( "');" );//$NON-NLS-1$
		return js.toString( );
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

		ArrayList al = new ArrayList( );

		PathIterator pitr = shape.getPathIterator( null );
		double[] data = new double[6];

		// TODO improve to support precise curve coordinates.

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
			Double db = (Double) al.get( i );
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
			Iterator it = ImageIO.getImageWritersByFormatName( s );
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
				Iterator it = ImageIO.getImageWritersByMIMEType( s );
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

			if ( (int) _bo.getWidth( ) <= 0 || (int) _bo.getHeight( ) <= 0 )
			{
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.INVALID_IMAGE_SIZE,
						"JavaxImageIOWriter.exception.invalid.image.size", //$NON-NLS-1$
						Messages.getResourceBundle( getULocale( ) ) );
			}

			// CREATE THE IMAGE INSTANCE
			_img = new BufferedImage( (int) _bo.getWidth( ),
					(int) _bo.getHeight( ),
					getImageType( ) );
		}
		super.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, _img.getGraphics( ) );

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
			Iterator it = null;
			String s = getFormat( );
			if ( s != null )
			{
				it = ImageIO.getImageWritersByFormatName( s );
				if ( !it.hasNext( ) )
				{
					it = null; // GET INTO NEXT CONSTRUCT; SEARCH BY MIME TYPE
				}
			}

			// SEARCH FOR WRITER USING MIME TYPE
			if ( it == null )
			{
				s = getMimeType( );
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
				it = ImageIO.getImageWritersByMIMEType( s );
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
			}
			final ImageWriter iw = (ImageWriter) it.next( );

			logger.log( ILogger.INFORMATION,
					Messages.getString( "JavaxImageIOWriter.info.using.imagewriter", getULocale( ) ) //$NON-NLS-1$
							+ getFormat( )
							+ iw.getClass( ).getName( ) );

			// WRITE TO SPECIFIC FILE FORMAT
			final Object o = ( _oOutputIdentifier instanceof String ) ? new File( (String) _oOutputIdentifier )
					: _oOutputIdentifier;
			try
			{
				final ImageOutputStream ios = ImageIO.createImageOutputStream( o );
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

	protected String eval( String expr )
	{
		if ( expr == null )
		{
			return ""; //$NON-NLS-1$
		}
		expr = expr.replaceAll( "\"", "&quot;" ); //$NON-NLS-1$ //$NON-NLS-2$
		return expr;
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
		Action ac = sa.getActionForCondition( TriggerCondition.ONCLICK_LITERAL );
		if ( !bAddCallback && checkSupportedAction( ac ) )
		{
			if ( ac.getType( ).getValue( ) == ActionType.INVOKE_SCRIPT
					&& StructureType.SERIES_DATA_POINT.equals( sa.getSource( )
							.getType( ) ) )
			{
				ScriptValue sv = (ScriptValue) ac.getValue( );
				sb.append( "<Script>" //$NON-NLS-1$
						+ "function userCallBack(categoryData, valueData, seriesValueName){" //$NON-NLS-1$
						+ eval( sv.getScript( ) )
						+ "}</Script>" ); //$NON-NLS-1$
				bAddCallback = true;
			}
		}
	}

}
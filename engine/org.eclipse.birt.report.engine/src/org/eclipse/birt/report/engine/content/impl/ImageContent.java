/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.content.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;

public class ImageContent extends AbstractContent implements IImageContent
{
	protected String helpTextKey;
	protected String extension;
	protected String uri;
	protected int sourceType = -1;
	transient protected byte[] data;
	/**
	 * The image map object (if any). Null means there's no image map. For HTML
	 * format, the image map is the HTML client-side image map string. It can be
	 * embedded into the HTML code directly.
	 */
	protected Object imageMap;

	protected String MIMEType;
	
	/**Resolution of the image*/
	private int resolution;
	
	ImageContent( IImageContent image )
	{
		super( image );
		helpTextKey = image.getHelpKey( );
		extension = image.getExtension( );
		uri = image.getURI( );
		sourceType = image.getImageSource( );
		data = image.getData( );
		imageMap = image.getImageMap( );
		MIMEType = image.getMIMEType( );
	}

	public int getContentType( )
	{
		return IMAGE_CONTENT;
	}

	ImageContent( ReportContent report )
	{
		super( report );
	}

	ImageContent( IContent content )
	{
		super( content );
	}

	public Object accept( IContentVisitor visitor, Object value )
			throws BirtException
	{
		return visitor.visitImage( this, value );
	}

	public String getAltText( )
	{
		// This is for backward compatibility. The alt text property was stored
		// as string and will not be written in the content.
		if ( altText == null )
		{
			if ( generateBy instanceof ImageItemDesign )
			{
				Expression expr = ( (ImageItemDesign) generateBy ).getAltText( );
				if ( expr != null && expr.getType( ) == Expression.CONSTANT )
				{
					return expr.getScriptText( );
				}
				return null;
			}
		}
		return altText;
	}

	public String getAltTextKey( )
	{
		if ( altTextKey == null )
		{
			if ( generateBy instanceof ImageItemDesign )
			{
				return ( (ImageItemDesign) generateBy ).getAltTextKey( );
			}
		}
		return altTextKey;
	}

	public void setAltTextKey( String key )
	{
		altTextKey = key;
	}

	public String getHelpText( )
	{
		if ( helpText == null )
		{
			if ( generateBy instanceof ImageItemDesign )
			{
				return ( (ImageItemDesign) generateBy ).getHelpText( );
			}
		}
		return helpText;
	}

	public String getHelpKey( )
	{
		if ( helpTextKey == null )
		{
			if ( generateBy instanceof ImageItemDesign )
			{
				return ( (ImageItemDesign) generateBy ).getHelpTextKey( );
			}
		}
		return helpTextKey;
	}

	public void setHelpKey( String key )
	{
		helpTextKey = key;
	}

	public byte[] getData( )
	{
		if ( sourceType == IImageContent.IMAGE_NAME )
		{
			Report reportDesign = report.getDesign( );
			if ( reportDesign != null )
			{
				ReportDesignHandle design = reportDesign.getReportDesign( );
				String imageName = getImageName( );
				EmbeddedImage embeddedImage = design.findImage( imageName );
				if ( embeddedImage != null )
				{
					return embeddedImage.getData( design.getModule( ) );
				}
			}
			return null;
		}
		return data;
	}

	public void setData( byte[] data )
	{
		this.data = data;
	}

	public String getExtension( )
	{
		return extension;
	}

	public String getURI( )
	{
		switch ( sourceType )
		{
			case IMAGE_NAME :
				return getImageName( );
			case IMAGE_FILE :
			case IMAGE_URL :
				return getImageURI( );
			default :
				return uri;
		}
	}

	public int getImageSource( )
	{
		return sourceType;
	}

	/**
	 * @param altText
	 *            The altText to set.
	 */
	public void setAltText( String altText )
	{
		this.altText = altText;
	}

	private void setImageName( String name )
	{
		assert ( sourceType == IMAGE_NAME );
		uri = name;
		if ( uri != null )
		{
			if ( generateBy instanceof ImageItemDesign )
			{
				Expression nameExpr = ( (ImageItemDesign) generateBy )
						.getImageName( );
				if ( nameExpr != null
						&& nameExpr.getType( ) == Expression.CONSTANT )
				{
					if ( uri.equals( nameExpr.getScriptText( ) ) )
					{
						uri = null;
					}
				}

			}
		}
		data = null;
	}

	private String getImageName( )
	{
		assert sourceType == IMAGE_NAME;
		if ( uri != null )
		{
			return uri;
		}
		if ( generateBy instanceof ImageItemDesign )
		{
			Expression nameExpr = ( (ImageItemDesign) generateBy )
					.getImageName( );
			if ( nameExpr != null )
			{
				return nameExpr.getScriptText( );
			}
		}
		return null;
	}

	private void setImageURI( String uri )
	{
		assert ( sourceType == IMAGE_FILE || sourceType == IMAGE_URL );
		if ( uri != null )
		{
			if ( generateBy instanceof ImageItemDesign )
			{
				Expression uriExpr = ( (ImageItemDesign) generateBy )
						.getImageUri( );
				if ( uriExpr != null
						&& uriExpr.getType( ) == Expression.CONSTANT )
				{
					if ( uri.equals( uriExpr.getScriptText( ) ) )
					{
						uri = null;
					}
				}

			}
		}
		this.uri = uri;
		data = null;
	}

	private String getImageURI( )
	{
		assert ( sourceType == IMAGE_FILE || sourceType == IMAGE_URL );
		if ( uri != null )
		{
			return uri;
		}
		if ( generateBy instanceof ImageItemDesign )
		{
			Expression uriExpr = ( (ImageItemDesign) generateBy ).getImageUri( );
			if ( uriExpr != null )
			{
				return uriExpr.getScriptText( );
			}
		}
		return null;
	}

	/**
	 * @param extension
	 *            The extension to set.
	 */
	public void setExtension( String extension )
	{
		this.extension = extension;
	}

	/**
	 * @param sourceType
	 *            The sourceType to set.
	 */
	public void setImageSource( int sourceType )
	{
		this.sourceType = sourceType;
	}

	/**
	 * @param uri
	 *            The uri to set.
	 */
	public void setURI( String uri )
	{
		switch ( sourceType )
		{
			case IMAGE_NAME :
				setImageName( uri );
				break;
			case IMAGE_FILE :
			case IMAGE_URL :
				setImageURI( uri );
				break;
			default :
				this.uri = uri;
				break;
		}
	}

	/**
	 * set the image map
	 * 
	 * @param imageMap -
	 *            the image map
	 */
	public void setImageMap( Object imageMap )
	{
		this.imageMap = imageMap;
	}

	/**
	 * get the image map
	 */
	public Object getImageMap( )
	{
		return imageMap;
	}

	/**
	 * set the MIME type
	 * 
	 * @param MIMEType -
	 *            the MIMEType
	 */
	public void setMIMEType( String MIMEType )
	{
		this.MIMEType = MIMEType;
	}

	/**
	 * get the MIMEType
	 */
	public String getMIMEType( )
	{
		return MIMEType;
	}

	static final protected short FIELD_ALTTEXT = 500;
	static final protected short FIELD_ALTTEXTKEY = 501;
	static final protected short FIELD_EXTENSEION = 502;
	static final protected short FIELD_URI = 503;
	static final protected short FIELD_SOURCETYPE = 504;
	static final protected short FIELD_IMAGEMAP = 505;
	static final protected short FIELD_MIMETYPE = 506;
	static final protected short FIELD_DATA = 507;

	protected void writeFields( DataOutputStream out ) throws IOException
	{
		super.writeFields( out );
		if ( extension != null )
		{
			IOUtil.writeShort( out, FIELD_EXTENSEION );
			IOUtil.writeString( out, extension );
		}
		if ( imageMap != null )
		{
			IOUtil.writeShort( out, FIELD_IMAGEMAP );
			IOUtil.writeObject( out, imageMap );
		}
		if ( sourceType != -1 )
		{
			IOUtil.writeShort( out, FIELD_SOURCETYPE );
			IOUtil.writeInt( out, sourceType );
		}
		switch ( sourceType )
		{
			case IImageContent.IMAGE_FILE :
			case IImageContent.IMAGE_NAME :
			case IImageContent.IMAGE_URL :
				if ( uri != null )
				{
					IOUtil.writeShort( out, FIELD_URI );
					IOUtil.writeString( out, uri );
				}
				break;
			case IImageContent.IMAGE_EXPRESSION :
				if ( data != null )
				{
					IOUtil.writeShort( out, FIELD_DATA );
					IOUtil.writeBytes( out, data );
				}
				break;
		}
		if ( MIMEType != null )
		{
			IOUtil.writeShort( out, FIELD_MIMETYPE );
			IOUtil.writeString( out, MIMEType );
		}
	}

	public boolean needSave( )
	{
		return true;
	}

	protected void readField( int version, int filedId, DataInputStream in,
			ClassLoader loader ) throws IOException
	{
		switch ( filedId )
		{
			case FIELD_EXTENSEION :
				extension = IOUtil.readString( in );
				break;
			case FIELD_URI :
				uri = IOUtil.readString( in );
				break;
			case FIELD_SOURCETYPE :
				sourceType = IOUtil.readInt( in );
				break;
			case FIELD_IMAGEMAP :
				imageMap = IOUtil.readObject( in, loader );
				break;
			case FIELD_MIMETYPE :
				MIMEType = IOUtil.readString( in );
				break;
			case FIELD_DATA :
				data = IOUtil.readBytes( in );
				break;
			default :
				super.readField( version, filedId, in, loader );
		}
	}
	
	protected IContent cloneContent()
	{
		return new ImageContent(this);
	}

	public int getResolution( )
	{
		return resolution;
	}

	public void setResolution( int resolution )
	{
		this.resolution = resolution;
	}
}

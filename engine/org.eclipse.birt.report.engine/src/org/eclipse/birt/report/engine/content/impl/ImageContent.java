/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

	protected String altText;
	protected String altTextKey;
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

	public int getContentType( )
	{
		return IMAGE_CONTENT;
	}

	public ImageContent( ReportContent report )
	{
		super( report );
	}

	public ImageContent( IContent content )
	{
		super( content );
	}

	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitImage( this, value );
	}

	public String getAltText( )
	{
		if ( altText == null )
		{
			if ( generateBy instanceof ImageItemDesign )
			{
				return ( (ImageItemDesign) generateBy ).getAltText( );
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
		if ( data == null && sourceType == IImageContent.IMAGE_NAME )
		{
			Report reportDesign = report.getDesign( );
			if ( reportDesign != null )
			{
				ReportDesignHandle design = reportDesign.getReportDesign( );
				EmbeddedImage embeddedImage = design.findImage( uri );
				if ( embeddedImage != null )
				{
					data = embeddedImage.getData( design.getModule( ) );
				}
			}
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
		return uri;
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

	public void setImageName( String name )
	{
		sourceType = IMAGE_NAME;
		uri = name;
	}

	public String getImageName( )
	{
		if ( sourceType == IMAGE_NAME )
		{
			if ( uri == null )
			{
				if ( generateBy instanceof ImageItemDesign )
					return ( (ImageItemDesign) generateBy ).getImageName( );
			}
			return uri;
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
		this.uri = uri;
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

	public Expression getExpression( )
	{
		if ( generateBy instanceof ImageItemDesign )
		{
			return ( (ImageItemDesign) generateBy ).getImageExpression( );
		}
		return null;
	}

	static final protected int FIELD_ALTTEXT = 500;
	static final protected int FIELD_ALTTEXTKEY = 501;
	static final protected int FIELD_EXTENSEION = 502;
	static final protected int FIELD_URI = 503;
	static final protected int FIELD_SOURCETYPE = 504;
	static final protected int FIELD_IMAGEMAP = 505;
	static final protected int FIELD_MIMETYPE = 506;
	static final protected int FIELD_DATA = 507;

	protected void writeFields( DataOutputStream out ) throws IOException
	{
		super.writeFields( out );
		if ( altText != null )
		{
			IOUtil.writeInt( out, FIELD_ALTTEXT );
			IOUtil.writeString( out, altText );
		}
		if ( altTextKey != null )
		{
			IOUtil.writeInt( out, FIELD_ALTTEXTKEY );
			IOUtil.writeString( out, altTextKey );
		}
		if ( extension != null )
		{
			IOUtil.writeInt( out, FIELD_EXTENSEION );
			IOUtil.writeString( out, extension );
		}
		if ( imageMap != null )
		{
			IOUtil.writeInt( out, FIELD_IMAGEMAP );
			IOUtil.writeObject( out, imageMap );
		}
		if ( sourceType != -1 )
		{
			IOUtil.writeInt( out, FIELD_SOURCETYPE );
			IOUtil.writeInt( out, sourceType );
		}
		switch ( sourceType )
		{
			case IImageContent.IMAGE_FILE :
			case IImageContent.IMAGE_NAME :
			case IImageContent.IMAGE_URI :
				if ( uri != null )
				{
					IOUtil.writeInt( out, FIELD_URI );
					IOUtil.writeString( out, uri );
				}
				break;
			case IImageContent.IMAGE_EXPRESSION :
				if ( data != null )
				{
					IOUtil.writeInt( out, FIELD_DATA );
					IOUtil.writeBytes( out, data );
				}
				break;
		}
		if ( MIMEType != null )
		{
			IOUtil.writeInt( out, FIELD_MIMETYPE );
			IOUtil.writeString( out, MIMEType );
		}
	}

	protected void readField( int version, int filedId, DataInputStream in )
			throws IOException
	{
		switch ( filedId )
		{
			case FIELD_ALTTEXT :
				altText = IOUtil.readString( in );
				break;
			case FIELD_ALTTEXTKEY :
				altTextKey = IOUtil.readString( in );
				break;
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
				imageMap = IOUtil.readObject( in );
				break;
			case FIELD_MIMETYPE :
				MIMEType = IOUtil.readString( in );
				break;
			case FIELD_DATA :
				data = IOUtil.readBytes( in );
				break;
			default :
				super.readField( version, filedId, in );
		}
	}

}


package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;

public class ImageContent extends AbstractContent implements IImageContent
{

	protected String altText;
	protected String altTextKey;
	protected String extension;
	protected String uri;
	protected int sourceType;
	protected byte[] data;
	/**
	 * The image map object (if any). Null means there's no image map. For HTML
	 * format, the image map is the HTML client-side image map string. It can be
	 * embedded into the HTML code directly.
	 */
	protected Object imageMap;

	protected String MIMEType;

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

	public byte[] getData( )
	{
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

}

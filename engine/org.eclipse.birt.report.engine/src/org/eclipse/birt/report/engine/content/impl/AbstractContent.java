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
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IBounds;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.dom.CompositeStyle;
import org.eclipse.birt.report.engine.css.dom.ComputedStyle;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.engine.css.engine.BIRTCSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.ir.DimensionType;

abstract public class AbstractContent extends AbstractElement
		implements
			IContent
{

	transient protected IReportContent report;

	transient protected CSSEngine cssEngine;

	protected String name;

	transient protected IBounds bounds;

	protected DimensionType x;

	protected DimensionType y;

	protected DimensionType width;

	protected DimensionType height;

	protected IHyperlinkAction hyperlink;

	protected String bookmark;

	protected String helpText;

	transient protected String styleClass;

	protected IStyle inlineStyle;

	transient protected IStyle style;

	transient protected IStyle computedStyle;

	transient protected Object generateBy;

	protected InstanceID instanceId;

	protected String toc;

	public AbstractContent( IReportContent report )
	{
		this.report = report;
		this.cssEngine = report.getCSSEngine( );
	}

	public void setReportContent( IReportContent report )
	{
		this.report = report;
		this.cssEngine = report.getCSSEngine( );
	}

	public AbstractContent( IContent content )
	{
		this( content.getReportContent( ) );
		this.name = content.getName( );
		this.bounds = content.getBounds( );
		this.x = content.getX( );
		this.y = content.getY( );
		this.width = content.getWidth( );
		this.height = content.getHeight( );
		this.hyperlink = content.getHyperlinkAction( );
		this.bookmark = content.getBookmark( );
		this.helpText = content.getHelpText( );
		this.inlineStyle = content.getInlineStyle( );
		this.generateBy = content.getGenerateBy( );
		this.styleClass = content.getStyleClass( );
		this.instanceId = content.getInstanceID( );
		this.toc = content.getTOC( );
	}

	public IReportContent getReportContent( )
	{
		return this.report;
	}

	public CSSEngine getCSSEngine( )
	{
		return this.cssEngine;
	}

	public String getName( )
	{
		return name;
	}

	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitContent( this, value );
	}

	public IBounds getBounds( )
	{
		return bounds;
	}

	/**
	 * @return the bookmark value
	 */
	public String getBookmark( )
	{
		return bookmark;
	}

	/**
	 * @return the actionString
	 */
	public IHyperlinkAction getHyperlinkAction( )
	{
		return hyperlink;
	}

	/**
	 * @return the height of the report item
	 */
	public DimensionType getHeight( )
	{
		return height;
	}

	/**
	 * @return the width of the report item
	 */
	public DimensionType getWidth( )
	{
		return width;
	}

	/**
	 * @return the x position of the repor titem.
	 */
	public DimensionType getX( )
	{
		return x;
	}

	/**
	 * @return Returns the y position of the repor titem.
	 */
	public DimensionType getY( )
	{
		return y;
	}

	/**
	 * @return Returns the helpText.
	 */
	public String getHelpText( )
	{
		return helpText;
	}

	public IStyle getComputedStyle( )
	{
		if ( computedStyle == null )
		{
			CSSEngine cssEngine = null;
			if ( report != null )
			{
				cssEngine = report.getCSSEngine( );
			}
			if ( cssEngine == null )
			{
				cssEngine = new BIRTCSSEngine( );
			}
			computedStyle = new ComputedStyle( this );
		}
		return computedStyle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IStyledContent#getStyle()
	 */
	public IStyle getStyle( )
	{
		if ( style == null )
		{
			if ( inlineStyle == null )
			{
				inlineStyle = report.createStyle( );
			}
			IStyle classStyle = report.findStyle( styleClass );
			style = new CompositeStyle( classStyle, inlineStyle );
		}
		return style;
	}

	public Object getGenerateBy( )
	{
		return generateBy;
	}

	/**
	 * @param bookmark
	 *            The bookmark to set.
	 */
	public void setBookmark( String bookmark )
	{
		this.bookmark = bookmark;
	}

	/**
	 * @param bounds
	 *            The bounds to set.
	 */
	public void setBounds( IBounds bounds )
	{
		this.bounds = bounds;
	}

	/**
	 * @param generateBy
	 *            The generateBy to set.
	 */
	public void setGenerateBy( Object generateBy )
	{
		this.generateBy = generateBy;
	}

	/**
	 * @param height
	 *            The height to set.
	 */
	public void setHeight( DimensionType height )
	{
		this.height = height;
	}

	/**
	 * @param helpText
	 *            The helpText to set.
	 */
	public void setHelpText( String helpText )
	{
		this.helpText = helpText;
	}

	/**
	 * @param hyperlink
	 *            The hyperlink to set.
	 */
	public void setHyperlinkAction( IHyperlinkAction hyperlink )
	{
		this.hyperlink = hyperlink;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName( String name )
	{
		this.name = name;
	}

	public void setStyleClass( String name )
	{
		this.styleClass = name;
		this.style = null;
		this.computedStyle = null;
	}

	public String getStyleClass( )
	{
		return this.styleClass;
	}

	/**
	 * @param style
	 *            The style to set.
	 */
	public void setInlineStyle( IStyle style )
	{

		this.inlineStyle = style;
		this.style = null;
		this.computedStyle = null;
	}

	public IStyle getInlineStyle( )
	{
		return inlineStyle;
	}

	/**
	 * @param width
	 *            The width to set.
	 */
	public void setWidth( DimensionType width )
	{
		this.width = width;
	}

	/**
	 * @param x
	 *            The x to set.
	 */
	public void setX( DimensionType x )
	{
		this.x = x;
	}

	/**
	 * @param y
	 *            The y to set.
	 */
	public void setY( DimensionType y )
	{
		this.y = y;
	}

	public InstanceID getInstanceID( )
	{
		return instanceId;
	}

	public void setInstanceID( InstanceID id )
	{
		this.instanceId = id;
	}

	public void setTOC( String toc )
	{
		this.toc = toc;
	}

	public String getTOC( )
	{
		return toc;
	}

	/**
	 * object document version
	 */
	static final protected int VERSION = 0;

	final static int FIELD_NONE = -1;
	final static int FIELD_NAME = 0;
	final static int FIELD_X = 1;
	final static int FIELD_Y = 2;
	final static int FIELD_WIDTH = 3;
	final static int FIELD_HEIGHT = 4;
	final static int FIELD_HYPERLINK = 5;
	final static int FIELD_BOOKMARK = 6;
	final static int FIELD_HELPTEXT = 7;
	final static int FIELD_INLINESTYLE = 8;
	final static int FIELD_INSTANCE_ID = 9;
	final static int FIELD_TOC = 10;

	protected void writeFields( DataOutputStream out ) throws IOException
	{
		if ( name != null )
		{
			IOUtil.writeInt( out, FIELD_NAME );
			IOUtil.writeString( out, name );
		}
		if ( x != null )
		{
			IOUtil.writeInt( out, FIELD_X );
			x.writeObject( out );
		}
		if ( y != null )
		{
			IOUtil.writeInt( out, FIELD_Y );
			y.writeObject( out );
		}
		if ( width != null )
		{
			IOUtil.writeInt( out, FIELD_WIDTH );
			width.writeObject( out );
		}
		if ( height != null )
		{
			IOUtil.writeInt( out, FIELD_HEIGHT );
			height.writeObject( out );
		}
		if ( hyperlink != null )
		{
			IOUtil.writeInt( out, FIELD_HYPERLINK );
			( (ActionContent) hyperlink ).writeObject( out );
		}
		if ( bookmark != null )
		{
			IOUtil.writeInt( out, FIELD_BOOKMARK );
			IOUtil.writeString( out, bookmark );
		}
		if ( helpText != null )
		{
			IOUtil.writeInt( out, FIELD_HELPTEXT );
			IOUtil.writeString( out, helpText );
		}
		if ( inlineStyle != null )
		{
			String cssText = inlineStyle.getCssText( );
			if ( cssText != null && cssText.length( ) != 0 )
			{
				IOUtil.writeInt( out, FIELD_INLINESTYLE );
				IOUtil.writeString( out, cssText );
			}
		}
		if ( instanceId != null )
		{
			IOUtil.writeInt( out, FIELD_INSTANCE_ID );
			IOUtil.writeString( out, instanceId.toString( ) );
		}
		if ( toc != null )
		{
			IOUtil.writeInt( out, FIELD_TOC );
			IOUtil.writeString( out, toc );
		}
	}

	protected void readField( int version, int filedId, DataInputStream in )
			throws IOException
	{
		switch ( filedId )
		{
			case FIELD_NAME :
				name = IOUtil.readString( in );
				break;
			case FIELD_X :
				x = new DimensionType( );
				x.readObject( in );
				break;
			case FIELD_Y :
				y = new DimensionType( );
				y.readObject( in );
				break;
			case FIELD_WIDTH :
				width = new DimensionType( );
				width.readObject( in );
				break;
			case FIELD_HEIGHT :
				height = new DimensionType( );
				height.readObject( in );
				break;
			case FIELD_HYPERLINK :
				ActionContent action = new ActionContent( );
				action.readObject( in );
				hyperlink = action;
				break;
			case FIELD_BOOKMARK :
				bookmark = IOUtil.readString( in );
				break;
			case FIELD_HELPTEXT :
				helpText = IOUtil.readString( in );
				break;
			case FIELD_INLINESTYLE :
				String style = IOUtil.readString( in );
				if ( style != null && style.length( ) != 0 )
				{
					inlineStyle = new StyleDeclaration( cssEngine );
					inlineStyle.setCssText( style );
				}
				break;
			case FIELD_INSTANCE_ID :
				String value = IOUtil.readString( in );
				instanceId = InstanceID.parse( value );
				break;
			case FIELD_TOC :
				toc = IOUtil.readString( in );
				break;
		}
	}

	public void readContent( DataInputStream in ) throws IOException
	{
		int version = IOUtil.readInt( in );
		int filedId = IOUtil.readInt( in );
		while ( filedId != FIELD_NONE )
		{
			readField( version, filedId, in );
			filedId = IOUtil.readInt( in );
		}
	}

	public void writeContent( DataOutputStream out ) throws IOException
	{
		IOUtil.writeInt( out, VERSION );
		writeFields( out );
		IOUtil.writeInt( out, FIELD_NONE );
	}

}

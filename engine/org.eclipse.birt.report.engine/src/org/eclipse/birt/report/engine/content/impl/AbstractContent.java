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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
import org.eclipse.birt.report.engine.ir.DimensionType;

abstract public class AbstractContent extends AbstractElement
		implements
			IContent
{
	transient protected IReportContent report;

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

	/**
	 * default contructor, used by serialize and deserialize.
	 */
	public AbstractContent( )
	{
	}

	public AbstractContent( IReportContent report )
	{
		this.report = report;
	}

	public void setReportContent( IReportContent report )
	{
		this.report = report;
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

	protected void writeFields( ObjectOutputStream out ) throws IOException
	{
		if ( name != null )
		{
			out.writeInt( FIELD_NAME );
			out.writeUTF( name );
		}
		if ( x != null )
		{
			out.writeInt( FIELD_X );
			out.writeUTF( x.toString() );
		}
		if ( y != null )
		{
			out.writeInt( FIELD_Y );
			out.writeUTF( y.toString() );
		}
		if ( width != null )
		{
			out.writeInt( FIELD_WIDTH );
			out.writeUTF( width.toString() );
		}
		if ( height != null )
		{
			out.writeInt( FIELD_HEIGHT );
			out.writeUTF( height.toString() );
		}
		if ( hyperlink != null )
		{
			out.writeInt( FIELD_HYPERLINK );
			hyperlink.writeContent( out );
		}
		if ( bookmark != null )
		{
			out.writeInt( FIELD_BOOKMARK );
			out.writeUTF( bookmark );
		}
		if ( helpText != null )
		{
			out.writeInt( FIELD_HELPTEXT );
			out.writeUTF( helpText );
		}
		if ( inlineStyle != null )
		{
			out.writeInt( FIELD_INLINESTYLE );
			out.writeUTF( inlineStyle.getCssText( ) );
		}
		if ( instanceId != null )
		{
			out.writeInt( FIELD_INSTANCE_ID );
			out.writeUTF( instanceId.toString( ) );
		}
		if ( toc != null )
		{
			out.writeInt( FIELD_TOC );
			out.writeUTF( toc );
		}
	}

	protected void readField( int version, int filedId, ObjectInputStream in )
			throws IOException, ClassNotFoundException
	{
		switch ( filedId )
		{
			case FIELD_NAME :
				name = in.readUTF( );
				break;
			case FIELD_X :
				String value = in.readUTF( );
				x = new DimensionType( value );
				break;
			case FIELD_Y :
				value = in.readUTF( );
				y = new DimensionType( value );
				break;
			case FIELD_WIDTH :
				value = in.readUTF( );
				width = new DimensionType( value );
				break;
			case FIELD_HEIGHT :
				value = in.readUTF( );
				height = new DimensionType( value );
				break;
			case FIELD_HYPERLINK :
				hyperlink = new ActionContent( );
				hyperlink.readContent( in );
				break;
			case FIELD_BOOKMARK :
				bookmark = in.readUTF( );
				break;
			case FIELD_HELPTEXT :
				helpText = in.readUTF( );
				break;
			case FIELD_INLINESTYLE :
				inlineStyle = new StyleDeclaration( );
				String style = in.readUTF( );
				inlineStyle.setCssText( style );
				break;
			case FIELD_INSTANCE_ID :
				value = in.readUTF( );
				instanceId = InstanceID.parse( value );
				break;
			case FIELD_TOC :
				toc = in.readUTF( );
				break;
		}
	}
	
	public void readContent( ObjectInputStream in ) throws IOException, ClassNotFoundException
	{
		int version = in.readInt( );
		int filedId = in.readInt( );
		while ( filedId != FIELD_NONE )
		{
			readField( version, filedId, in );
			filedId = in.readInt( );
		}
	}

	public void writeContent( ObjectOutputStream out ) throws IOException
	{
		out.writeInt( VERSION );
		writeFields( out );
		out.writeInt( FIELD_NONE );
	}
	


}

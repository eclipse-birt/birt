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

import java.io.Serializable;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IBounds;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.dom.CompositeStyle;
import org.eclipse.birt.report.engine.css.dom.ComputedStyle;
import org.eclipse.birt.report.engine.ir.DimensionType;

abstract public class AbstractContent extends AbstractElement
		implements
			IContent,
			Serializable
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

	public void setReport( IReportContent report )
	{
		this.report = report;
	}

	public AbstractContent( IContent content )
	{
		this( content.getReport( ) );
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

	public IReportContent getReport( )
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
}

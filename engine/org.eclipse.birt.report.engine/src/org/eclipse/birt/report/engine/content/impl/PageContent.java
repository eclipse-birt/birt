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

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;

public class PageContent extends AbstractContent implements IPageContent
{

	protected String orientation;
	protected String pageType;
	protected DimensionType pageHeight;
	protected DimensionType pageWidth;
	protected DimensionType headerHeight;
	protected DimensionType footerHeight;
	protected DimensionType leftWidth;
	protected DimensionType rightWidth;
	protected DimensionType marginTop;
	protected DimensionType marginLeft;
	protected DimensionType marginRight;
	protected DimensionType marginBottom;
	transient protected ArrayList header;
	transient protected ArrayList footer;
	transient protected IImageContent waterMark;
	transient protected IContent body;
	protected long pageNumber = -1;

	/**
	 * constructor. use by serialize and deserialize
	 */
	public PageContent( )
	{
	}

	public int getContentType( )
	{
		return PAGE_CONTENT;
	}

	public PageContent( ReportContent report )
	{
		super( report );
	}

	public void setGenerateBy( Object design )
	{
		super.setGenerateBy( design );
		if ( design instanceof MasterPageDesign )
		{
			MasterPageDesign page = (MasterPageDesign) design;
			orientation = page.getOrientation( );
			pageType = page.getPageType( );
			pageHeight = page.getPageHeight( );
			pageWidth = page.getPageWidth( );
			marginTop = page.getTopMargin( );
			marginLeft = page.getLeftMargin( );
			marginRight = page.getRightMargin( );
			marginBottom = page.getBottomMargin( );
			if ( page instanceof SimpleMasterPageDesign )
			{
				headerHeight = ( (SimpleMasterPageDesign) page )
						.getHeaderHeight( );
				footerHeight = ( (SimpleMasterPageDesign) page )
						.getFooterHeight( );
			}
			if ( headerHeight == null )
			{
				headerHeight = new DimensionType( 0.25f,
						EngineIRConstants.UNITS_IN );
			}
			if ( footerHeight == null )
			{
				footerHeight = new DimensionType( 0.25f,
						EngineIRConstants.UNITS_IN );
			}
		}
	}

	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitPage( this, value );
	}

	MasterPageDesign getMasterPage( )
	{
		return (MasterPageDesign) this.generateBy;
	}

	public String getOrientation( )
	{
		return orientation;
	}

	public String getPageType( )
	{
		return pageType;
	}

	public DimensionType getPageHeight( )
	{
		return pageHeight;
	}

	public DimensionType getPageWidth( )
	{
		return pageWidth;
	}

	public IImageContent getWaterMark( )
	{
		return waterMark;
	}

	public List getHeader( )
	{
		if ( header == null )
		{
			header = new ArrayList( );
		}
		return header;
	}

	public List getFooter( )
	{
		if ( footer == null )
		{
			footer = new ArrayList( );
		}
		return footer;
	}

	/**
	 * @param orientation
	 *            The orientation to set.
	 */
	public void setOrientation( String orientation )
	{
		this.orientation = orientation;
	}

	/**
	 * @param pageHeight
	 *            The pageHeight to set.
	 */
	public void setPageHeight( DimensionType pageHeight )
	{
		this.pageHeight = pageHeight;
	}

	/**
	 * @param pageType
	 *            The pageType to set.
	 */
	public void setPageType( String pageType )
	{
		this.pageType = pageType;
	}

	/**
	 * @param pageWidth
	 *            The pageWidth to set.
	 */
	public void setPageWidth( DimensionType pageWidth )
	{
		this.pageWidth = pageWidth;
	}

	/**
	 * @param waterMark
	 *            The waterMark to set.
	 */
	public void setWaterMark( IImageContent waterMark )
	{
		this.waterMark = waterMark;
	}

	public DimensionType getMarginTop( )
	{
		return this.marginTop;
	}

	public DimensionType getMarginBottom( )
	{
		return this.marginBottom;
	}

	public DimensionType getMarginLeft( )
	{
		return this.marginLeft;
	}

	public DimensionType getMarginRight( )
	{
		return this.marginRight;
	}

	public DimensionType getHeaderHeight( )
	{
		return headerHeight;
	}

	public DimensionType getFooterHeight( )
	{
		return footerHeight;
	}

	public DimensionType getLeftWidth( )
	{
		return leftWidth;
	}

	public DimensionType getRightWidth( )
	{
		return rightWidth;
	}

	public IStyle getContentComputedStyle( )
	{
		if ( body == null )
		{
			if ( generateBy instanceof MasterPageDesign )
			{
				body = report.createCellContent( );
				body.setInlineStyle( ( (MasterPageDesign) generateBy )
						.getContentStyle( ) );
			}
		}
		return body.getComputedStyle( );
	}
	
	public IStyle getContentStyle()
	{
		if ( generateBy instanceof MasterPageDesign )
		{
			return ( (MasterPageDesign) generateBy ).getContentStyle( );
		}
		return null;
	}

	public void setPageNumber( long pn )
	{
		this.pageNumber = pn;
	}

	public long getPageNumber( )
	{
		return this.pageNumber;
	}
	
	static final protected int FIELD_ORIENTATION = 700;
	static final protected int FIELD_PAGETYPE = 701;
	static final protected int FIELD_PAGEHEIGHT = 702;
	static final protected int FIELD_PAGEWIDTH = 703;
	static final protected int FIELD_HEADERHEIGHT = 704;
	static final protected int FIELD_FOOTERHEIGHT = 705;
	static final protected int FIELD_LEFTWIDTH = 706;
	static final protected int FIELD_RIGHTWIDTH = 707;
	static final protected int FIELD_MARGINTOP = 708;
	static final protected int FIELD_MARGINLEFT = 709;
	static final protected int FIELD_MARGINRIGHT = 710;
	static final protected int FIELD_MARGINBUTTOM = 711;
	static final protected int FIELD_PAGENUMBER = 712;
	

	protected void writeFields( ObjectOutputStream out ) throws IOException
	{
		super.writeFields( out );
		if ( orientation != null )
		{
			out.writeInt( FIELD_ORIENTATION );
			out.writeUTF( orientation );
		}
		if ( pageType != null )
		{
			out.writeInt( FIELD_PAGETYPE );
			out.writeUTF( pageType );
		}		
		if ( pageHeight != null )
		{
			out.writeInt( FIELD_PAGEHEIGHT );
			out.writeUTF( pageHeight.toString() );
		}				
		if ( pageWidth != null )
		{
			out.writeInt( FIELD_PAGEWIDTH );
			out.writeUTF( pageWidth.toString() );
		}			
		if ( headerHeight != null )
		{
			out.writeInt( FIELD_HEADERHEIGHT );
			out.writeUTF( headerHeight.toString() );
		}			
		if ( footerHeight != null )
		{
			out.writeInt( FIELD_FOOTERHEIGHT );
			out.writeUTF( footerHeight.toString() );
		}			
		if ( leftWidth != null )
		{
			out.writeInt( FIELD_LEFTWIDTH );
			out.writeUTF( leftWidth.toString() );
		}			
		if ( rightWidth != null )
		{
			out.writeInt( FIELD_RIGHTWIDTH );
			out.writeUTF( rightWidth.toString() );
		}			
		if ( marginTop != null )
		{
			out.writeInt( FIELD_MARGINTOP );
			out.writeUTF( marginTop.toString() );
		}			
		if ( marginLeft != null )
		{
			out.writeInt( FIELD_MARGINLEFT );
			out.writeUTF( marginLeft.toString() );
		}			
		if ( marginRight != null )
		{
			out.writeInt( FIELD_MARGINRIGHT );
			out.writeUTF( marginRight.toString() );
		}			
		if ( marginBottom != null )
		{
			out.writeInt( FIELD_MARGINBUTTOM );
			out.writeUTF( marginBottom.toString() );
		}	
		if ( pageNumber != -1 )
		{
			out.writeInt( FIELD_PAGENUMBER );
			out.writeLong( pageNumber );
		}
	}

	protected void readField( int version, int filedId, ObjectInputStream in )
			throws IOException, ClassNotFoundException
	{
		switch ( filedId )
		{
			case FIELD_ORIENTATION :
				orientation = in.readUTF( );
				break;
			case FIELD_PAGETYPE :
				pageType = in.readUTF( );
				break;
			case FIELD_PAGEHEIGHT :
				String value = in.readUTF( );
				pageHeight = new DimensionType( value );
				break;	
			case FIELD_PAGEWIDTH :
				value = in.readUTF( );
				pageWidth = new DimensionType( value );
				break;	
			case FIELD_HEADERHEIGHT :
				value = in.readUTF( );
				headerHeight = new DimensionType( value );
				break;	
			case FIELD_FOOTERHEIGHT :
				value = in.readUTF( );
				footerHeight = new DimensionType( value );
				break;	
			case FIELD_LEFTWIDTH :
				value = in.readUTF( );
				leftWidth = new DimensionType( value );
				break;	
			case FIELD_RIGHTWIDTH :
				value = in.readUTF( );
				rightWidth = new DimensionType( value );
				break;	
			case FIELD_MARGINTOP :
				value = in.readUTF( );
				marginTop = new DimensionType( value );
				break;	
			case FIELD_MARGINLEFT :
				value = in.readUTF( );
				marginLeft = new DimensionType( value );
				break;	
			case FIELD_MARGINRIGHT :
				value = in.readUTF( );
				marginRight = new DimensionType( value );
				break;	
			case FIELD_MARGINBUTTOM :
				value = in.readUTF( );
				marginBottom = new DimensionType( value );
				break;						
			case FIELD_PAGENUMBER :
				pageNumber = in.readLong( );
				break;
			default :
				super.readField( version, filedId, in );
		}
	}
	
}

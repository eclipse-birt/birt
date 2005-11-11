
package org.eclipse.birt.report.engine.content.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;

public class PageContent extends AbstractContent implements IPageContent
{

	protected String orientation;
	protected String pageType;
	protected String pageHeight;
	protected String pageWidth;
	protected ArrayList header;
	protected ArrayList footer;
	protected IImageContent waterMark;
	protected long pageNumber;

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
			pageHeight = page.getPageHeight( ).toString( );
			pageWidth = page.getPageWidth( ).toString( );
		}
	}

	public void accept( IContentVisitor visitor , Object value)
	{
		visitor.visitPage( this , value);
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

	public String getPageHeight( )
	{
		return pageHeight;
	}

	public String getPageWidth( )
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
	public void setPageHeight( String pageHeight )
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
	public void setPageWidth( String pageWidth )
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

	public String getMarginTop()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getMarginBottom()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getMarginLeft()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getMarginRight()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getHeaderHeight()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getFooterHeight()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getLeftWidth()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getRightWidth()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isShowFloatingFooter()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isShowFooterOnLast()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isShowHeaderOnFirst()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public IStyle getContentStyle()
	{
		return ( ( MasterPageDesign ) generateBy ).getContentStyle( );
	}
	
	public void setPageNumber(long pn)
	{
		this.pageNumber = pn;
	}
	
	public long getPageNumber()
	{
		return this.pageNumber;
	}
}

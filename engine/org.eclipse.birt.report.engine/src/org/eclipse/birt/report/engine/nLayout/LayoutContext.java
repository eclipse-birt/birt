/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout;

import java.util.HashMap;
import java.util.Locale;

import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.layout.pdf.font.FontMappingManager;
import org.eclipse.birt.report.engine.layout.pdf.font.FontMappingManagerFactory;

public class LayoutContext
{

	protected int maxWidth;

	protected int maxHeight;
	
	protected int maxBP;

	protected String format;

	protected IReportContent report;

	protected IContent unresolvedContent;
	
	protected Locale locale;
	
	protected long totalPage = 0;
	protected long pageCount = 0;
	protected long pageNumber = 1;
	
	protected boolean autoPageBreak = true;
	
	protected String supportedImageFormats = "PNG;GIF;JPG;BMP;";
	
	protected boolean finished = false;
	
	public boolean isFinished( )
	{
		return finished;
	}


	
	public void setFinished( boolean finished )
	{
		this.finished = finished;
	}

	public int getMaxBP( )
	{
		return maxBP;
	}

	public void setMaxBP( int maxBP )
	{
		this.maxBP = maxBP;
	}

	public void setAutoPageBreak(boolean autoPageBreak)
	{
		this.autoPageBreak = autoPageBreak;
	}

	
	public boolean isAutoPageBreak( )
	{
		return autoPageBreak;
	}

	public void addUnresolvedContent( IContent content )
	{
		this.unresolvedContent = content;
	}

	public IContent getUnresolvedContent( )
	{
		return unresolvedContent;
	}
	

	
	public long getTotalPage( )
	{
		return totalPage;
	}

	
	public void setTotalPage( long totalPage )
	{
		this.totalPage = totalPage;
	}

	
	public long getPageCount( )
	{
		return pageCount;
	}

	
	public void setPageCount( long pageCount )
	{
		this.pageCount = pageCount;
	}

	
	public long getPageNumber( )
	{
		return pageNumber;
	}

	
	public void setPageNumber( long pageNumber )
	{
		this.pageNumber = pageNumber;
	}

	public IReportContent getReport( )
	{
		return report;
	}

	public void setReport( IReportContent report )
	{
		this.report = report;
	}

	public String getFormat( )
	{
		return this.format;
	}

	public void setFormat( String format )
	{
		this.format = format;
	}

	public int getMaxHeight( )
	{
		return maxHeight;
	}

	public int getMaxWidth( )
	{
		return maxWidth;
	}

	public void setMaxHeight( int height )
	{
		this.maxHeight = height;
	}

	public void setMaxWidth( int width )
	{
		this.maxWidth = width;
	}

	protected boolean fitToPage = false;

	public void setFitToPage( boolean fitToPage )
	{
		this.fitToPage = fitToPage;
	}

	public boolean fitToPage( )
	{
		return this.fitToPage;
	}

	protected boolean pageBreakPaginationOnly = false;

	public void setPagebreakPaginationOnly( boolean pageBreakPaginationOnly )
	{
		this.pageBreakPaginationOnly = pageBreakPaginationOnly;
		setAutoPageBreak( !pageBreakPaginationOnly );
	}

	public boolean pagebreakPaginationOnly( )
	{
		return this.pageBreakPaginationOnly;
	}

	protected int pageOverflow = IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES;

	public int getPageOverflow( )
	{
		return this.pageOverflow;
	}

	public void setPageOverflow( int pageOverflow )
	{
		this.pageOverflow = pageOverflow;
		if ( pageOverflow != IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES )
		{
			autoPageBreak = false;
		}
	}

	protected int preferenceWidth = 0;

	public void setPreferenceWidth( int preferenceWidth )
	{
		this.preferenceWidth = preferenceWidth;
	}

	public int getPreferenceWidth( )
	{
		return this.preferenceWidth;
	}

	protected boolean textWrapping = true;

	public void setTextWrapping( boolean textWrapping )
	{
		this.textWrapping = textWrapping;
	}

	public boolean getTextWrapping( )
	{
		return this.textWrapping;
	}

	protected boolean fontSubstitution = true;

	public void setFontSubstitution( boolean fontSubstitution )
	{
		this.fontSubstitution = fontSubstitution;
	}

	public boolean getFontSubstitution( )
	{
		return this.fontSubstitution;
	}

	protected boolean bidiProcessing = true;

	public void setBidiProcessing( boolean bidiProcessing )
	{
		this.bidiProcessing = bidiProcessing;
	}

	public boolean getBidiProcessing( )
	{
		return this.bidiProcessing;
	}
	
	protected boolean enableHyphenation = false;
	
	public boolean isEnableHyphenation( )
	{
		return enableHyphenation;
	}
	
	public void setEnableHyphenation( boolean enableHyphenation )
	{
		this.enableHyphenation = enableHyphenation;
	}

	public Locale getLocale( )
	{
		return locale;
	}

	public void setLocale( Locale locale )
	{
		this.locale = locale;
	}

	private FontMappingManager fontManager;

	public FontMappingManager getFontManager( )
	{
		if ( fontManager == null )
		{
			fontManager = FontMappingManagerFactory.getInstance( )
					.getFontMappingManager( format, locale );
		}
		return fontManager;
	}

	// the dpi used to calculate image size.
	private int dpi = 0;

	public int getDpi( )
	{
		return dpi;
	}
	
	public void setDpi( int dpi )
	{
		this.dpi = dpi;
	}
	
	private int totalPageTemplateWidth;
	
	public void setTotalPageTemplateWidth( int totalPageTemplateWidth )
	{
		this.totalPageTemplateWidth = totalPageTemplateWidth;
	}
	
	public int getTotalPageTemplateWidth()
	{
		return this.totalPageTemplateWidth;
	}
	
	private HashMap cachedTableHeaders = null;
	private HashMap cachedGroupHeaders = null;
	
	public void setCachedHeaderMap( HashMap tableHeaders, HashMap groupHeaders )
	{
		this.cachedTableHeaders = tableHeaders;
		this.cachedGroupHeaders = groupHeaders;
	}
	
	protected ITableBandContent getWrappedTableHeader( InstanceID id )
	{
		if ( null != cachedTableHeaders )
		{
			Object cachedHeaders = cachedTableHeaders.get( id );
			if ( cachedHeaders != null )
			{
				return (ITableBandContent)cachedHeaders;
			}
		}
		return null;
	}
	
	protected ITableBandContent getWrappedGroupHeader( InstanceID id )
	{
		if ( null != cachedGroupHeaders )
		{
			Object cachedHeaders = cachedGroupHeaders.get( id );
			if ( cachedHeaders != null )
			{
				return (ITableBandContent)cachedHeaders;
			}
		}
		return null;
	}
	
	public String getSupportedImageFormats( )
	{
		return supportedImageFormats;
	}

	public void setSupportedImageFormats( String supportedImageFormats )
	{
		this.supportedImageFormats = supportedImageFormats;
	}
}

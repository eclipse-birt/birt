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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutContext;
import org.eclipse.birt.report.engine.layout.pdf.font.FontMappingManager;
import org.eclipse.birt.report.engine.layout.pdf.font.FontMappingManagerFactory;
import org.eclipse.birt.report.engine.nLayout.area.impl.FixedLayoutPageHintGenerator;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;

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
	protected long pageNumber = 0;
	
	protected boolean autoPageBreak = true;
	
	private boolean sizeOverflowPageBreak = false;
	
	protected String supportedImageFormats = "PNG;GIF;JPG;BMP;SVG"; //$NON-NLS-1$
	
	protected boolean finished = false;
	
	protected int engineTaskType = IEngineTask.TASK_RENDER;

	protected boolean isFixedLayout = false;
	
	protected boolean isInHtmlRender = false;

	protected HTMLLayoutContext htmlLayoutContext = null;
	
	protected HashMap<String, Long> bookmarkMap = new HashMap<String, Long>();
	
	protected boolean displayNone = false;
	
	
	public boolean isDisplayNone( )
	{
		return displayNone;
	}

	
	public void setDisplayNone( boolean displayNone )
	{
		this.displayNone = displayNone;
	}

	public HTMLLayoutContext getHtmlLayoutContext( )
	{
		return htmlLayoutContext;
	}
	
	public void addBookmarkMap(long pageNumber, String bookmark)
	{
		if ( !bookmarkMap.containsKey( bookmark ) )
		{
			bookmarkMap.put( bookmark, pageNumber );
		}
	}
	
	public Map<String, Long> getBookmarkMap()
	{
		return bookmarkMap;
	}
	
	public void setHtmlLayoutContext( HTMLLayoutContext htmlLayoutContext )
	{
		this.htmlLayoutContext = htmlLayoutContext;
	}

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
	
	public int getEngineTaskType( )
	{
		return engineTaskType;
	}

	public void setEngineTaskType( int engineTaskType )
	{
		this.engineTaskType = engineTaskType;
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
	
	protected int pageLimit = -1;
	
	public void setPageLimit( int pageLimit )
	{
		this.pageLimit = pageLimit;
	}

	public int getPageLimit( )
	{
		return this.pageLimit;
	}
	
	public boolean exceedPageLimit()
	{
		if ( ( engineTaskType == IEngineTask.TASK_RENDER || engineTaskType == IEngineTask.TASK_RUNANDRENDER )
				&& pageLimit > 0 && this.pageCount >= pageLimit )
		{
			return true;
		}
		return false;
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
	
	protected boolean enableWordbreak = false;
	
	public boolean isEnableWordbreak( )
	{
		return enableWordbreak;
	}
	
	public void setEnableWordbreak( boolean enableWordbreak )
	{
		this.enableWordbreak = enableWordbreak;
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
	
	private Boolean reserveDocumentPageNumbers = null;

	public boolean isReserveDocumentPageNumbers( )
	{
		if ( reserveDocumentPageNumbers == null )
		{
			if ( isFixedLayout )
			{
				// by default, fixed layout will always create pageExecutor
				// according to reporting's page number and page accordingly.
				// When repaginateForPDF is on, pageExecutor will not be created
				// and layout engine will re-paginate accordingly
				Object repaginateForPDF = htmlLayoutContext.getLayoutEngine( )
						.getOption( IPDFRenderOption.REPAGINATE_FOR_PDF );
				if ( repaginateForPDF != null
						&& ( (Boolean) repaginateForPDF ).booleanValue( ) )
				{
					return false;
				}
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return reserveDocumentPageNumbers.booleanValue( );
		}
	}

	public void setReserveDocumentPageNumbers(
			boolean reserveDocumentPageNumbers )
	{
		this.reserveDocumentPageNumbers = Boolean
				.valueOf( reserveDocumentPageNumbers );
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

	public boolean isFixedLayout( )
	{
		return isFixedLayout;
	}

	public void setFixedLayout( boolean isFixedLayout )
	{
		this.isFixedLayout = isFixedLayout;
	}
	
	// handle page hint.
	protected FixedLayoutPageHintGenerator pageHintGenerator = null;
	
	public void createPageHintGenerator( )
	{
		if ( isFixedLayout )
		{
			this.pageHintGenerator = new FixedLayoutPageHintGenerator( this );
		}
	}

	public FixedLayoutPageHintGenerator getPageHintGenerator( )
	{
		return pageHintGenerator;
	}
	
	public boolean isInHtmlRender( )
	{
		return isInHtmlRender;
	}
	
	public void setInHtmlRender( boolean isInHtmlRender )
	{
		this.isInHtmlRender = isInHtmlRender;
	}
	
	// The following methods are used in run task.
	public String getMasterPage( )
	{
		return htmlLayoutContext.getMasterPage( );
	}
	
	/**
	 * Gets the common hints.
	 */
	public ArrayList getPageHint()
	{
		return pageHintGenerator.getPageHint( );
	}
	
	/**
	 * Gets column hints.
	 */
	public List getTableColumnHints()
	{
		return htmlLayoutContext.getPageHintManager( ).getTableColumnHints( );
	}
	
	/**
	 * Gets unresolved hints.
	 * @return
	 */
	public List<UnresolvedRowHint> getUnresolvedRowHints( )
	{
		return pageHintGenerator.getUnresolvedRowHints( );
	}
	
	public void resetUnresolvedRowHints()
	{
		if ( pageHintGenerator != null )
		{
			pageHintGenerator.resetRowHint( );
		}
	}
	
	public long getTotalPage( )
	{
		return totalPage;
	}

	public void setTotalPage( long totalPage )
	{
		this.totalPage = totalPage;
	}

	/**
	 * Indicates if the page break is triggered by content size exceeding page
	 * size.
	 * 
	 * @return true when the page break is triggered by content size exceeding
	 *         page size
	 * @since 4.6
	 */
	public boolean isSizeOverflowPageBreak( )
	{
		return sizeOverflowPageBreak;
	}

	/**
	 * Sets the state if the page break is triggered by content size exceeding
	 * page size.
	 * 
	 * @param sizeOverflowPageBreak
	 *            true when the page break is triggered by content size
	 *            exceeding page size
	 * @since 4.6
	 */
	public void setSizeOverflowPageBreak( boolean sizeOverflowPageBreak )
	{
		this.sizeOverflowPageBreak = sizeOverflowPageBreak;
	}

}

/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel.layout;

import java.io.OutputStream;
import java.util.Locale;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IExcelRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.excel.ExcelUtil;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;


public class ExcelContext
{

	protected static final String DEFAULT_SHEET_NAME = "Report";

	private boolean wrappingText = true;
	private String officeVersion = "office2003";
	private String tempFileDir;
	private ULocale locale;
	private TimeZone timeZone;
	private Boolean hideGridlines = false;
	private boolean enableMultipleSheet = true;
	private boolean ignoreImage = true;
	private String sheetName;
	private String sheetPrefix;
	private OutputStream out;
	private IReportContent report;
	private int dpi;
	private boolean isAutoLayout = true;
	private int contentWidth;
	private int pageWidth;
	private int pageHeight;
	private int leftMargin;
	private int rightMargin;
	private int topMargin;
	private int bottomMargin;
	private int headerHeight;
	private int footerHeight;
	private int sheetIndex = 1;
	private boolean isRTL = false;
	private IReportContext reportContext;
	private Page page;

	public void initialize( IEmitterServices service ) throws EngineException
	{
		if ( service == null )
		{
			return;
		}
		out = EmitterUtil.getOuputStream( service, "report.xls" );
		if ( service.getReportEngine( ) != null )
		{
			this.tempFileDir = service.getReportEngine( ).getConfig( )
					.getTempDir( );
		}
		IReportContext reportContext = service.getReportContext( );
		if ( reportContext != null )
		{
			Locale locale = reportContext.getLocale( );
			this.locale = locale == null ? ULocale.getDefault( ) : ULocale
					.forLocale( locale );
			this.timeZone = reportContext.getTimeZone( );
			if ( timeZone == null )
			{
				timeZone = TimeZone.getDefault( );
			}
		}
		IRenderOption renderOption = service.getRenderOption( );
		Object option = renderOption
				.getOption( IExcelRenderOption.OPTION_MULTIPLE_SHEET );
		if ( option instanceof Boolean )
			enableMultipleSheet = (Boolean) option;
		this.reportContext = service.getReportContext( );
	}

	public IReportContext getReportContext()
	{
		return reportContext;
	}
	
	public Page getPage()
	{
		return page;
	}
	
	public void setPage( Page page )
	{
		this.page = page;
	}
	
	public void setReport( IReportContent report )
	{
		this.report = report;
		IRenderOption renderOptions = report.getReportContext( )
		        .getRenderOption( );
		Object dpi = renderOptions.getOption( IRenderOption.RENDER_DPI );
		int renderDpi = 0;
		if ( dpi != null && dpi instanceof Integer )
		{
			renderDpi = ( (Integer) dpi ).intValue( );
		}
		this.dpi = PropertyUtil.getRenderDpi( report, renderDpi );

		Object textWrapping = renderOptions
		        .getOption( IExcelRenderOption.WRAPPING_TEXT );
		if ( textWrapping instanceof Boolean )
		{
			this.wrappingText = ( (Boolean) textWrapping );
		}

		Object officeVersion = renderOptions
		        .getOption( IExcelRenderOption.OFFICE_VERSION );
		if ( "office2007".equals( officeVersion ) )
		{
			this.officeVersion = "office2007";
		}

		Object hideGridlines = renderOptions
		        .getOption( IExcelRenderOption.HIDE_GRIDLINES );
		if ( hideGridlines instanceof Boolean )
		{
			this.hideGridlines = (Boolean) hideGridlines;
		}
		
		Object ignoreImage = renderOptions
				.getOption( IExcelRenderOption.IGNORE_IMAGE );
		if ( ignoreImage instanceof Boolean )
		{
			this.ignoreImage = (Boolean) ignoreImage;
		}

		ReportDesignHandle designHandle = report.getDesign( ).getReportDesign( );
		parseReportOrientation( designHandle );
		parseReportLayout( designHandle );
		parseSheetName( designHandle );
	}

	private void parseSheetName( ReportDesignHandle designHandle )
	{
		String reportTitle = designHandle
		        .getStringProperty( IModuleModel.TITLE_PROP );
		if ( reportTitle != null )
		{
			sheetPrefix = reportTitle;
		}
		else
		{
			sheetPrefix = DEFAULT_SHEET_NAME;
		}
		sheetPrefix = ExcelUtil.getValidSheetName( sheetPrefix );
		sheetName = generateSheetName( );
	}

	private String generateSheetName( )
	{
		if ( sheetIndex == 1 )
		{
			return sheetPrefix;
		}
		else
		{
			int indexLength = String.valueOf( sheetIndex - 1 ).length( );
			if ( sheetPrefix.length( ) + indexLength > ExcelUtil.SHEETNAME_LENGTH )
			{
				return sheetPrefix.substring( 0, ExcelUtil.SHEETNAME_LENGTH
						- indexLength )
						+ ( sheetIndex - 1 );
			}
			else
				return sheetPrefix + ( sheetIndex - 1 );
		}
	}

	private void parseReportLayout( ReportDesignHandle designHandle )
	{
		String reportLayoutPreference = designHandle.getLayoutPreference( );
		if ( DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT
		        .equals( reportLayoutPreference ) )
		{
			isAutoLayout = false;
		}
	}

	private void parseReportOrientation( ReportDesignHandle designHandle )
	{
		String reportOrientation = designHandle.getBidiOrientation( );
		if ( "rtl".equalsIgnoreCase( reportOrientation ) )
		{
			isRTL = true;
		}
	}

	public void parsePageSize( IPageContent page )
    {
		this.pageWidth = ExcelUtil.convertDimensionType( page.getPageWidth( ),
		                                                 0, dpi );
		this.pageHeight = ExcelUtil
		        .convertDimensionType( page.getPageHeight( ), 0, dpi );
		leftMargin = ExcelUtil.convertDimensionType( page.getMarginLeft( ),
		                                             pageWidth, dpi );
		rightMargin = ExcelUtil.convertDimensionType( page.getMarginRight( ),
		                                              pageWidth, dpi );
		topMargin = ExcelUtil.convertDimensionType( page.getMarginTop( ),
		                                            pageHeight, dpi );
		bottomMargin = ExcelUtil.convertDimensionType( page.getMarginBottom( ),
		                                               pageHeight, dpi );
		headerHeight = ExcelUtil.convertDimensionType( page.getHeaderHeight( ),
				pageHeight,
				dpi );
		footerHeight = ExcelUtil.convertDimensionType( page.getFooterHeight( ),
				pageHeight,
				dpi );
		this.contentWidth = pageWidth - leftMargin - rightMargin;
    }

	public boolean getWrappingText()
	{
		return wrappingText;
	}
	
	public String getOfficeVersion()
	{
		return officeVersion;
	}
	
	public String getTempFileDir( )
	{
		return this.tempFileDir;
	}

	public ULocale getLocale( )
	{
		return this.locale;
	}

	public TimeZone getTimeZone( )
	{
		return this.timeZone;
	}

	public boolean isEnableMultipleSheet( )
	{
		return enableMultipleSheet;
	}

	public void setSheetName( String sheetName )
	{
		this.sheetName = sheetName;
	}

	public String getSheetName( )
	{
		return sheetName;
	}

	public boolean getHideGridlines( )
	{
		return this.hideGridlines;
	}

	public boolean isIgnoreImage( )
	{
		return this.ignoreImage;
	}

	public void setIgnoreImage( boolean isIgnoreImage )
	{
		this.ignoreImage = isIgnoreImage;
	}

	public OutputStream getOutputSteam( )
	{
		return out;
	}

	public IReportContent getReport( )
	{
		return report;
	}

	public int getDpi( )
	{
		return dpi;
	}

	public boolean isRTL( )
	{
		return isRTL;
	}

	public void setSheetIndex( int sheetIndex )
	{
		this.sheetIndex = sheetIndex;
		this.sheetName = generateSheetName( );
	}

	public int getSheetIndex( )
	{
		return sheetIndex;
	}

	public boolean isAutoLayout( )
	{
		return isAutoLayout;
	}

	public int getContentWidth( )
	{
		return contentWidth;
	}

	public int getPageWidth( )
	{
		return pageWidth;
	}

	public int getPageHeight( )
	{
		return pageHeight;
	}

	public float getTopMargin( )
	{
		return this.topMargin / 1000f;
	}

	public float getBottomMargin( )
	{
		return this.bottomMargin / 1000f;
	}

	public float getLeftMargin( )
	{
		return this.leftMargin / 1000f;
	}

	public float getRightMargin( )
	{
		return this.rightMargin / 1000f;
	}
	
	public float getHeaderHeight( )
	{
		return this.headerHeight / 1000f;
	}
	
	public float getFooterHeight( )
	{
		return this.footerHeight / 1000f;
	}
}

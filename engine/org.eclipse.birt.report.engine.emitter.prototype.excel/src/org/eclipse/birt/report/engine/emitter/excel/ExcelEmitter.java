
package org.eclipse.birt.report.engine.emitter.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IExcelRenderOption;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.excel.layout.ColumnsInfo;
import org.eclipse.birt.report.engine.emitter.excel.layout.ContainerSizeInfo;
import org.eclipse.birt.report.engine.emitter.excel.layout.ExcelContext;
import org.eclipse.birt.report.engine.emitter.excel.layout.ExcelLayoutEngine;
import org.eclipse.birt.report.engine.emitter.excel.layout.LayoutUtil;
import org.eclipse.birt.report.engine.emitter.excel.layout.PageDef;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.MapDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.layout.pdf.util.HTML2Content;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;


public class ExcelEmitter extends ContentEmitterAdapter
{

	private boolean isAuto = true;

	protected static Logger logger = Logger.getLogger( ExcelEmitter.class
			.getName( ) );
	
	protected IEmitterServices service = null;

	protected OutputStream out = null;

	protected ExcelLayoutEngine engine;

	ContentEmitterVisitor contentVisitor = new ContentEmitterVisitor( this );

	protected IExcelWriter writer;

	public ExcelContext context = new ExcelContext( );

	private String orientation = null;

	protected String pageHeader;

	protected String pageFooter;

	private boolean outputInMasterPage = false;
	protected boolean isRTLSheet = false;
	private int sheetIndex = 1;
	public String getOutputFormat( )
	{
		return "xls";
	}
	
	public void initialize( IEmitterServices service ) throws EngineException
	{
		this.service = service;
		if ( service != null )
		{
			this.out = EmitterUtil.getOuputStream( service, "report."
					+ getOutputFormat( ) );
		}
		context.setTempFileDir( service.getReportEngine( ).getConfig( )
				.getTempDir( ) );
		IReportContext reportContext = service.getReportContext( );
		if ( reportContext != null )
		{
			Locale locale = reportContext.getLocale( );
			if ( locale != null )
			{
				context.setLocale( ULocale.forLocale( locale ) );
			}
			else
				context.setLocale( ULocale.getDefault( ) );
		}
	}

	public void start( IReportContent report )
	{
		setupRenderOptions( );
		// We can the page size from the design, maybe there is a better way
		// to get the page definition.

		String reportOrientation = report.getDesign( ).getReportDesign( )
				.getBidiOrientation( );
		if ( "rtl".equalsIgnoreCase( reportOrientation ) )
			isRTLSheet = true;
		IStyle style = report.getRoot( ).getComputedStyle( );
		SimpleMasterPageDesign master = (SimpleMasterPageDesign) report
				.getDesign( ).getPageSetup( ).getMasterPage( 0 );
		engine = createLayoutEngine( context, this );
		engine.initalize( new PageDef( master, style ) );
		createWriter( );
	}

	protected ExcelLayoutEngine createLayoutEngine( ExcelContext context,
			ExcelEmitter emitter )
	{
		return new ExcelLayoutEngine( context, emitter );
	}

	private void setupRenderOptions()
	{
		IRenderOption renderOptions = service.getRenderOption( );
		Object textWrapping = renderOptions.getOption(IExcelRenderOption.WRAPPING_TEXT);
		if(textWrapping!=null && textWrapping instanceof Boolean)
		{
			context.setWrappingText((Boolean)textWrapping);
		}
		else
		{
			context.setWrappingText( ( Boolean )true);
		}
		Object officeVersion = renderOptions.getOption(IExcelRenderOption.OFFICE_VERSION);
		if(officeVersion!=null && officeVersion instanceof String)
		{
			if(officeVersion.equals( "office2007" ))
			{
				context.setOfficeVersion("office2007" );
			}
		}
		else
		{
			context.setOfficeVersion( "office2003" );
		}
	}

	public void startPage( IPageContent page ) throws BirtException
	{
		if ( orientation == null )
		{
			orientation = capitalize( page.getOrientation( ) );
		}
		if(needOutputInMasterPage(page.getPageHeader( ))&& needOutputInMasterPage(page.getPageFooter( )))
		{
			outputInMasterPage = true;
			pageHeader = formatHeaderFooter( page.getPageHeader( ),true );
			pageFooter = formatHeaderFooter( page.getPageFooter( ),false );
		}
		if ( !outputInMasterPage && page.getPageHeader( ) != null )
		{
			contentVisitor.visitChildren( page.getPageHeader( ), null );
		}
		engine.setPageStyle( page.getComputedStyle( ) );
	}

	public void endPage( IPageContent page ) throws BirtException
	{
		if(!outputInMasterPage && page.getPageFooter( ) != null)
		{
			contentVisitor.visitChildren( page.getPageFooter( ), null );
		}
	}

	public void startTable( ITableContent table )
	{
		ContainerSizeInfo sizeInfo = engine.getCurrentContainer( ).getSizeInfo( );
		int width = sizeInfo.getWidth( );
		ColumnsInfo info = null;
		if ( isAuto )
		{
			info = LayoutUtil.createTable( table, width );
		}
		else
		{
			int[] columns = LayoutUtil.createFixedTable( table, LayoutUtil
					.getElementWidth( table, width ) );
			info = new ColumnsInfo( columns );
		}
		if ( info == null )
			return;
		String caption = table.getCaption( );
		if(caption != null) 
		{			
			engine.addCaption( caption, table.getComputedStyle( ) );
		}
		engine.addTable( table, info, sizeInfo );
	}

	public void startRow( IRowContent row )
	{
		engine.addRow( row.getComputedStyle( ) );
	}

	public void endRow( IRowContent row )
	{
		DimensionType height = row.getHeight( );
		double rowHeight = height != null ? ExcelUtil.covertDimensionType(
				height, 0 ) : 0;
		engine.endRow( rowHeight );
	}

	public void startCell( ICellContent cell )
	{
		IStyle style = cell.getComputedStyle( );
		engine.addCell( cell, cell.getColumn( ), cell.getColSpan( ), cell
				.getRowSpan( ), style );
	}

	public void endCell( ICellContent cell )
	{
		engine.endCell( );
	}	

	public void endTable( ITableContent table )
	{		
		engine.endTable();
	}	

	public void startList( IListContent list )
	{		
		ContainerSizeInfo size = engine.getCurrentContainer( ).getSizeInfo( );
		ColumnsInfo table = LayoutUtil.createTable( list, size.getWidth( ) );
		engine.addTable( list, table, size );
		
		if(list.getChildren( ) == null)
		{
			HyperlinkDef link = parseHyperLink(list);
			BookmarkDef bookmark = getBookmark( list );
			engine.addData( ExcelLayoutEngine.EMPTY, 
							list.getComputedStyle( ), link, bookmark );
		}	
	}

	public void startListBand( IListBandContent listBand )
	{	
		engine.addCell( 0, 1, 0, listBand.getComputedStyle( ) );
	}
	
	public void endListBand( IListBandContent listBand )
	{	
		engine.endContainer( );
	}
	
	public void endList( IListContent list )
	{		
		engine.endTable( );
	}

	public void startForeign( IForeignContent foreign ) throws BirtException
	{
		if ( IForeignContent.HTML_TYPE.equalsIgnoreCase( foreign.getRawType( ) ) )
		{
			HTML2Content.html2Content( foreign );
			HyperlinkDef link = parseHyperLink(foreign);
			engine.addContainer( foreign.getComputedStyle( ), link );			
			contentVisitor.visitChildren( foreign, null );
			engine.endContainer( );
		}
	}

	public void startText( ITextContent text )
	{
		HyperlinkDef url = parseHyperLink( text );
		BookmarkDef bookmark = getBookmark( text );
		engine.addData( text.getText( ), text.getComputedStyle( ), url,
				bookmark );
	}

	public void startData( IDataContent data )
	{
		addDataContent( data );
	}

	protected Data addDataContent( IDataContent data )
	{
		HyperlinkDef url = parseHyperLink( data );
		BookmarkDef bookmark = getBookmark( data );
		Data excelData = null;
		Object generateBy = data.getGenerateBy( );
		IStyle style = data.getComputedStyle( );
		DataFormatValue dataformat = style.getDataFormat( );
		MapDesign map = null;
		if(generateBy instanceof DataItemDesign )
		{
			DataItemDesign design = (DataItemDesign) generateBy;
			map = design.getMap( );
		}
		if ( map != null && map.getRuleCount( ) > 0
				&& data.getLabelText( ) != null )
		{
			excelData = engine.addData( data.getLabelText( ).trim( ), style,
					url, bookmark );
		}
		else
		{
			String locale = null;
			int type = ExcelUtil.getType( data.getValue( ) );
			if ( type == SheetData.STRING )
			{
				if ( dataformat != null )
				{
					locale = dataformat.getStringLocale( );
				}
				excelData = engine.addData( data.getText( ), style, url,
						bookmark, locale );
			}
			else if ( type == Data.NUMBER )
			{
				if ( dataformat != null )
				{
					locale = dataformat.getNumberLocale( );
				}
				excelData = engine.addData( data.getValue( ), style, url,
						bookmark, locale );
			}
			else
			{
				if ( dataformat != null )
				{
					locale = dataformat.getDateTimeLocale( );
				}
				excelData = engine.addDateTime( data, style, url, bookmark, locale );
			}
		}
		return excelData;
	}
	
	public void startImage( IImageContent image )
	{
		IStyle style = image.getComputedStyle( );
		HyperlinkDef url = parseHyperLink( image );
		BookmarkDef bookmark = getBookmark( image );
		engine.addImageData( image, style, url, bookmark );
	}
	
	public void startLabel( ILabelContent label )
	{
		Object design = label.getGenerateBy( );
		IContent container = label;

		while ( design == null )
		{
			container = (IContent) container.getParent( );
			design = ( (IContent) container ).getGenerateBy( );
		}

		HyperlinkDef url = parseHyperLink( label );
		BookmarkDef bookmark = getBookmark( label );

		// If the text is BR and it generated by foreign,
		// ignore it
		if ( !( "\n".equalsIgnoreCase( label.getText( ) ) && 
				container instanceof IForeignContent ) )
		{
			engine.addData( label.getText( ), label.getComputedStyle( ), url, bookmark );
		}
	}	

	public void startAutoText( IAutoTextContent autoText )
	{
		HyperlinkDef link = parseHyperLink( autoText );
		BookmarkDef bookmark = getBookmark( autoText );
		engine.addData( autoText.getText( ) , 
				        autoText.getComputedStyle( ), link, bookmark );
	}

	public void outputSheet( )
	{
		engine.cacheBookmarks( sheetIndex );
		engine.complete( );
		try
		{
			outputCacheData( );
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, e.getLocalizedMessage( ), e );
		}
		sheetIndex++;
	}

	public void end( IReportContent report )
	{
		// Make sure the engine already calculates all data in cache.
		engine.cacheBookmarks( sheetIndex );
		engine.complete( );
		try
		{
			writer.start( report, engine.getStyleMap( ), engine
					.getAllBookmarks( ) );
			outputCacheData( );
			writer.end( );
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, e.getLocalizedMessage( ), e );
		}
	}

	protected void createWriter( )
	{
		writer = new ExcelWriter( out, context, isRTLSheet );
	}

	/**
	 * @throws IOException
	 * 
	 */
	public void outputCacheData( ) throws IOException
	{
		writer.startSheet( engine.getCoordinates( ), pageHeader, pageFooter );
		Iterator<RowData> it = engine.getIterator( );
		while ( it.hasNext( ) )
		{
			outputRowData( it.next( ) );
		}
		writer.endSheet( orientation );
	}

	protected void outputRowData( RowData rowData ) throws IOException
	{
		writer.startRow( rowData.getHeight( ) );
		SheetData[] data = rowData.getRowdata( );
		for ( int i = 0; i < data.length; i++ )
		{
			writer.outputData( data[i] );
		}
		writer.endRow( );
	}

	public HyperlinkDef parseHyperLink( IContent content )
	{
		HyperlinkDef hyperlink = null;
		IHyperlinkAction linkAction = content.getHyperlinkAction( );

		if ( linkAction != null )
		{
			String tooltip = linkAction.getTooltip( );
			String bookmark = linkAction.getBookmark( );
			IReportRunnable reportRunnable = service.getReportRunnable( );
			IReportContext reportContext = service.getReportContext( );
			IHTMLActionHandler actionHandler = (IHTMLActionHandler) service
					.getOption( RenderOption.ACTION_HANDLER );
			switch ( linkAction.getType( ) )
			{
				case IHyperlinkAction.ACTION_BOOKMARK :
					hyperlink = new HyperlinkDef( bookmark,
							IHyperlinkAction.ACTION_BOOKMARK, tooltip );

					break;
				case IHyperlinkAction.ACTION_HYPERLINK :
					String url = EmitterUtil.getHyperlinkUrl( linkAction,
							reportRunnable, actionHandler, reportContext );
					hyperlink = new HyperlinkDef( url,
							IHyperlinkAction.ACTION_HYPERLINK, tooltip );
					break;
				case IHyperlinkAction.ACTION_DRILLTHROUGH :
					url = EmitterUtil.getHyperlinkUrl( linkAction,
							reportRunnable, actionHandler, reportContext );
					hyperlink = new HyperlinkDef( url,
							IHyperlinkAction.ACTION_DRILLTHROUGH, tooltip );
					break;
			}
		}
		return hyperlink;
	}

	protected BookmarkDef getBookmark( IContent content )
	{
		String bookmarkName = content.getBookmark( );
		if (bookmarkName == null)
			return null;
		
		BookmarkDef bookmark=new BookmarkDef(content.getBookmark( ));
		if ( !ExcelUtil.isValidBookmarkName( bookmarkName ) )
		{
			bookmark.setGeneratedName( engine
					.getGenerateBookmark( bookmarkName ) );
		}
		
		// !( content.getBookmark( ).startsWith( "__TOC" ) ) )
		// bookmark starting with "__TOC" is not OK?
		return bookmark;
	}


	public String capitalize( String orientation )
	{
		if ( orientation.equalsIgnoreCase( "landscape" ) )
		{
			return "Landscape";
		}
		if(orientation.equalsIgnoreCase( "portrait" ))
		{
			return "Portrait";
		}
		return null;
	}
	
	public String formatHeaderFooter( IContent headerFooter, boolean isHeader )
	{
		StringBuffer headfoot = new StringBuffer( );
		if ( headerFooter != null )
		{
			Collection list = headerFooter.getChildren( );
			Iterator iter = list.iterator( );
			while ( iter.hasNext( ) )
			{
				Object child = iter.next( );
				if ( child instanceof ITableContent )
				{
					headfoot.append( getTableValue( (ITableContent) child ) );
				}
				else
					processText( headfoot, child );		
			}
			return headfoot.toString( );
		}
		return null;
	}

	private void processText( StringBuffer buffer, Object child )
	{
		if ( child instanceof IAutoTextContent )
		{
			buffer.append( getAutoText( (IAutoTextContent) child ) );
		}
		else if ( child instanceof ITextContent )
		{
			buffer.append( ( (ITextContent) child ).getText( ) );
		}
		else if ( child instanceof IForeignContent )
		{
			buffer.append( ( (IForeignContent) child ).getRawValue( ) );
		}
	}
	
	public boolean needOutputInMasterPage( IContent headerFooter )
	{
		if ( headerFooter != null )
		{
			Collection list = headerFooter.getChildren( );
			Iterator iter = list.iterator( );
			while ( iter.hasNext( ) )
			{
				Object child = iter.next( );
				if ( child instanceof ITableContent )
				{
					int columncount = ( (ITableContent) child )
							.getColumnCount( );
					int rowcount = ( (ITableContent) child ).getChildren( )
							.size( );
					if ( columncount > 3 || rowcount > 1 )
					{
						logger
								.log(
										Level.WARNING,
										"Excel page header or footer only accept a table no more than 1 row and 3 columns." );
						return false;
					}
					if ( isEmbededTable( (ITableContent) child ) )
					{
						logger
								.log( Level.WARNING,
										"Excel page header and footer don't support embeded grid." );
						return false;
					}
    			}
				if ( isHtmlText( child ) )
				{
					logger
							.log( Level.WARNING,
									"Excel page header and footer don't support html text." );
					return false;
				}
			}
		}
		return true;
	}

	private boolean isHtmlText( Object child )
	{
		return child instanceof IForeignContent
				&& IForeignContent.HTML_TYPE
						.equalsIgnoreCase( ( (IForeignContent) child )
								.getRawType( ) );
	}
	
	public String getTableValue( ITableContent table )
	{
		StringBuffer tableValue = new StringBuffer( );
		Collection list = table.getChildren( );
		Iterator iter = list.iterator( );
		while ( iter.hasNext( ) )
		{
			Object child = iter.next( );
			tableValue.append( getRowValue( (IRowContent) child ) );
		}
		return tableValue.toString( );

	}
	
	public String getRowValue( IRowContent row )
	{
		StringBuffer rowValue = new StringBuffer( );
		Collection list = row.getChildren( );
		Iterator iter = list.iterator( );
		int cellCount = list.size( );
		int currentCellCount = 0;
		while ( iter.hasNext( ) )
		{
			currentCellCount++;
			Object child = iter.next( );
			switch ( currentCellCount )
			{
				case 1 :
					rowValue.append( "&L" );
					break;
				case 2 :
					rowValue.append( "&C" );
					break;
				case 3 :
					rowValue.append( "&R" );
					break;
				default :
					break;
			}
			rowValue.append( getCellValue( (ICellContent) child ) );
		}
		return rowValue.toString( );
	}
	
	public String getCellValue( ICellContent cell )
	{
		StringBuffer cellValue = new StringBuffer( );
		Collection list = cell.getChildren( );
		Iterator iter = list.iterator( );
		while ( iter.hasNext( ) )
		{
			processText( cellValue, iter.next( ) );
		}
		return cellValue.toString( );
	}
	
	private String getAutoText( IAutoTextContent autoText )
	{
		String result = null;
		int type = autoText.getType( );
		if ( type == IAutoTextContent.PAGE_NUMBER )
		{
			result = "&P";
		}
		else if ( type == IAutoTextContent.TOTAL_PAGE )
		{
			result = "&N";
		}
		return result;
	}
	
	private boolean isEmbededTable(ITableContent table)
	{
		boolean isEmbeded = false;
		Collection list = table.getChildren( );
		Iterator iterRow = list.iterator( );
		while ( iterRow.hasNext( ) )
		{
			Object child = iterRow.next( );
			Collection listCell = ( (IRowContent) child ).getChildren( );
			Iterator iterCell = listCell.iterator( );
			while ( iterCell.hasNext( ) )
			{
				Object cellChild = iterCell.next( );
				Collection listCellChild = ( (ICellContent) cellChild )
						.getChildren( );
				Iterator iterCellChild = listCellChild.iterator( );
				while ( iterCellChild.hasNext( ) )
				{
					Object cellchild = iterCellChild.next( );
					if ( cellchild instanceof ITableContent )
					{
						isEmbeded = true;
					}
				}
			}
		}
		return isEmbeded;
	}
	
	public TimeZone getTimeZone()
	{
		if ( service != null )
		{
			IReportContext reportContext = service.getReportContext( );
			if ( reportContext != null )
			{
				return reportContext.getTimeZone( );
			}
		}
		return TimeZone.getDefault( );
	}

	public void endContainer( IContainerContent container )
	{
		engine.removeContainerStyle( );
	}

	public void startContainer( IContainerContent container )
	{
		engine.addContainerStyle( container.getComputedStyle( ) );
	}
}

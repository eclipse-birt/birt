
package org.eclipse.birt.report.engine.emitter.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.IExcelRenderOption;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.impl.Action;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
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
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.excel.layout.ExcelContext;
import org.eclipse.birt.report.engine.emitter.excel.layout.ExcelLayoutEngine;
import org.eclipse.birt.report.engine.emitter.excel.layout.LayoutUtil;
import org.eclipse.birt.report.engine.emitter.excel.layout.PageDef;
import org.eclipse.birt.report.engine.emitter.excel.layout.ContainerSizeInfo;
import org.eclipse.birt.report.engine.emitter.excel.layout.TableInfo;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.ir.StyledElementDesign;
import org.eclipse.birt.report.engine.layout.pdf.util.HTML2Content;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;

public class ExcelEmitter extends ContentEmitterAdapter
{
    private String tempfilePath;
	
	protected static Logger logger = Logger.getLogger( ExcelEmitter.class
			.getName( ) );

	private static int sheetIndex = 1;

	private IEmitterServices service = null;

	private OutputStream out = null;

	private ExcelLayoutEngine engine;	
	
	ContentEmitterVisitor contentVisitor = new ContentEmitterVisitor( this );
	
	private ExcelWriter tempWriter;
	
	public ExcelContext context = new ExcelContext();
	
	private String orientation = null;
	
	private String pageHeader;
	
	private String pageFooter;
	
	private boolean outputInMasterPage = false;
	
	public String getOutputFormat( )
	{
		return "xls";
	}
	
	public void initialize( IEmitterServices service )
	{
		this.service = service;
		if ( service != null )
		{
			this.out = EmitterUtil.getOuputStream( service, "report.xls" );
		}
	}

	//FIXME: CODE REVIEW: create engine to startPage
	public void start( IReportContent report )
	{
		setupRenderOptions( );
		// We can the page size from the design, maybe there is a better way
		// to get the page definition.
		IStyle style = report.getRoot( ).getComputedStyle( );
		SimpleMasterPageDesign master = (SimpleMasterPageDesign) report
				.getDesign( ).getPageSetup( ).getMasterPage( 0 );
		engine = new ExcelLayoutEngine( new PageDef( master, style ), context,
				this );
		tempfilePath = System.getProperty( "java.io.tmpdir" ) + File.separator
				+ "_BIRTEMITTER_EXCEL_TEMP_FILE"
				+ Thread.currentThread( ).getId( );
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

	public void startPage( IPageContent page )
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
		
	}

	public void endPage( IPageContent page )
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
		TableInfo info = LayoutUtil.createTable( table, width );
		
		if( info == null ) {
		   return;	
		}
		
		String caption = table.getCaption( );
		
		if(caption != null) 
		{			
			engine.addCaption( caption );
		}
		
		engine.addTable( info, table.getComputedStyle( ));
	}

	public void startRow( IRowContent row )
	{
		engine.addRow( row.getComputedStyle( ) );
	}

	public void endRow( IRowContent row )
	{
		engine.endRow( );
	}

	public void startCell( ICellContent cell )
	{
		IStyle style = cell.getComputedStyle( );
		engine.addCell( cell.getColumn( ), cell.getColSpan( ),cell.getRowSpan( ), style );
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
		ContainerSizeInfo rule = engine.getCurrentContainer( ).getSizeInfo( );
		TableInfo table = LayoutUtil.createTable( list, rule.getWidth( ) );
		engine.addTable( table, list.getComputedStyle( ) );						 
		
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
		engine.endCell( );
	}
	
	public void endList( IListContent list )
	{		
		engine.endTable( );
	}

	public void startForeign( IForeignContent foreign )
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
		super.startData( data );
		HyperlinkDef url = parseHyperLink( data );
		BookmarkDef bookmark = getBookmark( data );
		
		if ( ( (StyledElementDesign) data.getGenerateBy( ) ).getMap( ) != null
				&& ( (StyledElementDesign) data.getGenerateBy( ) ).getMap( )
						.getRuleCount( ) > 0 && data.getLabelText( ) != null )
		{
			engine.addData( data.getLabelText( ).trim( ), data.getComputedStyle( ),
					url, bookmark );
		}
		else if ( ExcelUtil.getType( data.getValue() ).equals( Data.STRING ) )
		{
			engine.addData( data.getText(), data.getComputedStyle( ), url, bookmark );
		}
		else if ( !ExcelUtil.getType( data.getValue() ).equals( Data.NUMBER ) )
		{
			engine.addDateTime( data, data.getComputedStyle( ), url, bookmark );
		}
		else 
		{
			engine.addData( data.getValue( ), data.getComputedStyle( ), url, bookmark );
		}
	}
	
	public void startImage( IImageContent image )
	{	
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

	public void outputSheet()
	{
		engine.complete( );
		try
		{
			if ( tempWriter == null )
			{
				FileOutputStream tempOut = new FileOutputStream( tempfilePath );
				tempWriter = new ExcelWriter( tempOut, context );
			}
			outputSheetData( tempWriter );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
		}
	}
	
	private void outputSheetData(ExcelWriter writer )
	{
		startSheet( writer );

		for ( int count = 0; count < engine.getRowCount( ); count++ )
		{
			outputData( engine.getRow( count ), writer );
		}

		endSheet( writer );		
	}
	
	public void end( IReportContent report )
	{
		//Make sure the engine already calculates all data in cache.
		engine.complete( );

		// bidi_acgc added start
		// Get the Report bidi Orientation property to be used for setting the
		// excel sheet Orientation.
		boolean isRTLSheet = false;
		String reportOrientation = report.getDesign( ).getReportDesign( )
				.getBidiOrientation( );
		if ( "rtl".equalsIgnoreCase( reportOrientation ) )
			isRTLSheet = true;
		ExcelWriter writer = new ExcelWriter( out, context, isRTLSheet );
		// ExcelWriter writer = new ExcelWriter( out , context); //bidi_acgc
		// commented
		// bidi_acgc added end
		writer.writeDeclarations( );
		writer.writeDocumentProperties( report );
		writer.declareStyles( engine.getStyleMap( ) );
		writer.defineNames( engine.getNamesRefer( ) );
		
		if(tempWriter!=null)
		{
			tempWriter.close( false );
			File file = new File( tempfilePath );
			writer.insertSheet( file );
			file.delete( );
			if ( engine.getRowCount( ) != 0 )
			{
				outputCacheData( writer );
			}
		}
		else
		{
			outputCacheData( writer );
		}

		writer.close( true );
		sheetIndex = 1;
	}

	private void outputCacheData( ExcelWriter writer )
	{
		startSheet( writer );
		for ( int count = 0; count < engine.getRowCount( ); count++ )
		{
			outputData( engine.getRow( count ), writer );
		}
		endSheet( writer );
	}

	private void startSheet( ExcelWriter writer )
	{
		writer.startSheet( sheetIndex );
		writer.startTable( engine.getCoordinates( ) );
		sheetIndex += 1;
	}

	private void endSheet( ExcelWriter writer )
	{
		writer.endTable( );
		writer.declareWorkSheetOptions( orientation, pageHeader, pageFooter );
		writer.closeSheet( );
	}

	private void outputData( Data[] row, ExcelWriter writer )
	{
		
		writer.startRow( );

		for ( int i = 0; i < row.length; i++ )
		{
			writer.writeTxtData( row[i] );
		}

		writer.endRow( );
	}

	private HyperlinkDef parseHyperLink( IContent content )
	{
		IHyperlinkAction linkaction = content.getHyperlinkAction( );

		if ( linkaction != null )
		{
			String toolTip = linkaction.getTooltip( );
			if ( linkaction.getType( ) == IHyperlinkAction.ACTION_BOOKMARK )
			{
				String bookmark = linkaction.getBookmark( );
				
				if (ExcelUtil.isValidBookmarkName( bookmark ))
				{
					return new HyperlinkDef(  linkaction.getBookmark( ), 
							IHyperlinkAction.ACTION_BOOKMARK, null ,toolTip );	
				}
				else
				{
					return null;
				}
				
			}
			else if ( linkaction.getType( ) == IHyperlinkAction.ACTION_HYPERLINK )
			{
				return new HyperlinkDef( linkaction.getHyperlink( ),
						IHyperlinkAction.ACTION_HYPERLINK, null , toolTip);
			}
			else if ( linkaction.getType( ) == IHyperlinkAction.ACTION_DRILLTHROUGH )
			{
				Action act = new Action( linkaction );
				IHTMLActionHandler actionHandler = null;
				Object ac = service.getOption( IRenderOption.ACTION_HANDLER );

				if ( ac != null && ac instanceof IHTMLActionHandler )
				{
					actionHandler = (IHTMLActionHandler) ac;
					String url = actionHandler.getURL( act, service
							.getReportContext( ) );
					if ( null != url && url.length( ) > 0 )
					{
						return new HyperlinkDef( url,
								IHyperlinkAction.ACTION_DRILLTHROUGH, null,
								toolTip );
					}
					else
					{
						return null;
					}
				}
			}
		}
		return null;
	}

	private BookmarkDef getBookmark( IContent content )
	{
		String bookmark = content.getBookmark( );
		if (bookmark == null)
			return null;
		
		if ( !ExcelUtil.isValidBookmarkName( bookmark ) )
		{
			logger.log( Level.WARNING, "Invalid bookmark name for Excel!" );
			return null;
		}
		
		// !( content.getBookmark( ).startsWith( "__TOC" ) ) )
		// bookmark starting with "__TOC" is not OK?
		return new BookmarkDef( content.getBookmark( ) );
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
			}
		}
		return true;
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
	
	public Locale getLocale( )
	{
		if ( service != null )
		{
			IReportContext reportContext = service.getReportContext( );
			if ( reportContext != null )
			{
				return reportContext.getLocale( );
			}
		}
		return Locale.getDefault( );
	}
}


package org.eclipse.birt.report.engine.emitter.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.IExcelRenderOption;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.impl.Action;
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
import org.eclipse.birt.report.engine.emitter.excel.layout.Rule;
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
		//contentVisitor.visitChildren(page.getPageHeader( ), null);
	}	

	public void endPage( IPageContent page )
	{
		//contentVisitor.visitChildren(page.getPageFooter( ), null);
	}

	public void startTable( ITableContent table )
	{
		Rule rule = engine.getCurrentContainer( ).getRule( );
		int width = rule.getWidth( );
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
		engine.addCell( cell.getColumn( ), cell.getColSpan( ), style );
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
		Rule rule = engine.getCurrentContainer( ).getRule( );
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
		engine.addCell( 0, 1, listBand.getComputedStyle( ) );
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
}

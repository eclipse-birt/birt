/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.wpml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.content.impl.TextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.i18n.EngineResourceHandle;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.layout.emitter.Image;
import org.eclipse.birt.report.engine.layout.pdf.font.FontMappingManager;
import org.eclipse.birt.report.engine.layout.pdf.font.FontMappingManagerFactory;
import org.eclipse.birt.report.engine.layout.pdf.font.FontSplitter;
import org.eclipse.birt.report.engine.layout.pdf.text.BidiSplitter;
import org.eclipse.birt.report.engine.layout.pdf.text.Chunk;
import org.eclipse.birt.report.engine.layout.pdf.text.LineBreakChunk;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;
import org.eclipse.birt.report.engine.util.FlashFile;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;

import com.ibm.icu.util.ULocale;

public abstract class AbstractEmitterImpl
{

	public final static int NORMAL = -1;

	public static enum InlineFlag {
		FIRST_INLINE, MIDDLE_INLINE, BLOCK
	};

	public static enum TextFlag {
		START, MIDDLE, END, WHOLE
	};

	private static Set<Integer> nonInherityStyles = new HashSet<Integer>( );

	static
	{
		nonInherityStyles.add( IStyle.STYLE_BORDER_BOTTOM_COLOR );
		nonInherityStyles.add( IStyle.STYLE_BORDER_BOTTOM_STYLE );
		nonInherityStyles.add( IStyle.STYLE_BORDER_BOTTOM_WIDTH );
		nonInherityStyles.add( IStyle.STYLE_BORDER_TOP_COLOR );
		nonInherityStyles.add( IStyle.STYLE_BORDER_TOP_STYLE );
		nonInherityStyles.add( IStyle.STYLE_BORDER_TOP_WIDTH );
		nonInherityStyles.add( IStyle.STYLE_BORDER_LEFT_COLOR );
		nonInherityStyles.add( IStyle.STYLE_BORDER_LEFT_STYLE );
		nonInherityStyles.add( IStyle.STYLE_BORDER_LEFT_WIDTH );
		nonInherityStyles.add( IStyle.STYLE_BORDER_RIGHT_COLOR );
		nonInherityStyles.add( IStyle.STYLE_BORDER_RIGHT_STYLE );
		nonInherityStyles.add( IStyle.STYLE_BORDER_RIGHT_WIDTH );
	}
	
	private static final HashMap<String, String> genericFontMapping = new HashMap<String, String>( );

	static
	{
		genericFontMapping.put( "sans-serif", "Arial" );
		genericFontMapping.put( "serif", "Times New Roman" );
		genericFontMapping.put( "monospace", "Courier New" );
		genericFontMapping.put( "cursive", "Comic Sans MS" );
		genericFontMapping.put( "fantasy", "Blackadder ITC" );
	}

	private static Logger logger = Logger.getLogger( AbstractEmitterImpl.class
			.getName( ) );

	protected OutputStream out = null;

	protected ContentEmitterVisitor contentVisitor;

	protected IWordWriter wordWriter = null;

	protected EmitterContext context = null;

	protected IPageContent previousPage = null;

	protected IReportContent reportContent;

	protected Stack<IStyle> styles = new Stack<IStyle>( );

	private int pageWidth = 0;

	private int pageHeight = 0;

	protected int contentWidth = 0;

	private int headerHeight = 0;

	private int footerHeight = 0;

	private int topMargin = 0;

	private int bottomMargin = 0;

	private int leftMargin = 0;

	private int rightMargin = 0;
	
	private int pageTopBorderWidth = 0;
	
	private int pageBottomBorderWidth = 0;
	
	private int pageLeftBorderWidth = 0;
	
	private int pageRightBorderWidth = 0;

	private String orientation = "portrait";

	private HashSet<String> bookmarks = new HashSet<String>( );

	private boolean rowFilledFlag = false;

	private ArrayList<InstanceID> groupIdList = new ArrayList<InstanceID>( );

	private int tocLevel = 1;

	private List<TocInfo> tableTocs = new ArrayList<TocInfo>( );

	private IReportRunnable reportRunnable;

	private IHTMLActionHandler actionHandler;

	private IReportContext reportContext;
	
	private String messageFlashObjectNotSupported;
	
	private String messageReportItemNotSupported;

	private String layoutPreference = null;

	private boolean fixedLayout;

	protected int reportDpi;

	protected static final String EMPTY_FOOTER = " ";

	public void initialize( IEmitterServices service ) throws EngineException
	{
		if ( service != null )
		{
			this.out = EmitterUtil.getOuputStream( service, "report."
					+ getOutputFormat( ) );
			this.reportRunnable = service.getReportRunnable( );
			this.actionHandler = (IHTMLActionHandler) service
					.getOption( RenderOption.ACTION_HANDLER );
			reportContext = service.getReportContext( );
			ULocale locale = null;
			if ( reportContext != null )
			{
				locale = ULocale.forLocale( reportContext.getLocale( ) );
			}
			if ( locale == null )
			{
				locale = ULocale.getDefault( );
			}
			EngineResourceHandle resourceHandle = new EngineResourceHandle(
					locale );
			messageFlashObjectNotSupported = resourceHandle
					.getMessage( MessageConstants.FLASH_OBJECT_NOT_SUPPORTED_PROMPT );
			messageReportItemNotSupported = resourceHandle
					.getMessage( MessageConstants.REPORT_ITEM_NOT_SUPPORTED_PROMPT );
			IRenderOption renderOption = service.getRenderOption( );
			if ( renderOption != null )
			{
				HTMLRenderOption htmlOption = new HTMLRenderOption(
						renderOption );
				layoutPreference = htmlOption.getLayoutPreference( );
			}
		}
		context = new EmitterContext( );
	}

	public void start( IReportContent report )
	{
		Object dpi = report
		.getReportContext( ).getRenderOption( ).getOption(
				IRenderOption.RENDER_DPI );
		int renderDpi =0;
		if ( dpi != null && dpi instanceof Integer )
		{
			renderDpi = ( (Integer) dpi ).intValue( );
		}
		reportDpi = PropertyUtil.getRenderDpi( report, renderDpi );
		this.reportContent = report;
		if ( null == layoutPreference )
		{
			ReportDesignHandle designHandle = report.getDesign( )
					.getReportDesign( );
			if ( designHandle != null )
			{
				String reportLayoutPreference = designHandle
						.getLayoutPreference( );
				if ( DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT
						.equals( reportLayoutPreference ) )
				{
					layoutPreference = HTMLRenderOption.LAYOUT_PREFERENCE_FIXED;
				}
				else if ( DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_AUTO_LAYOUT
						.equals( reportLayoutPreference ) )
				{
					layoutPreference = HTMLRenderOption.LAYOUT_PREFERENCE_AUTO;
				}
			}
			fixedLayout = HTMLRenderOption.LAYOUT_PREFERENCE_FIXED
					.equals( layoutPreference );
		}
	}

	public void startPage( IPageContent page ) throws IOException,
			BirtException
	{
		if ( previousPage != null )
		{
			outputPrePageProperties( );
			previousPage = page;
			context.resetWidth( );
		}
		else
		{
			previousPage = page;
			boolean isRtl = false;
			String creator = null;
			String title = null;
			String comments = null;
			String subject = null;
			if ( reportContent != null )
			{
				ReportDesignHandle designHandle = reportContent.getDesign( )
						.getReportDesign( );
				creator = designHandle.getAuthor( );
				title = reportContent.getTitle( );
				comments = designHandle.getComments( );
				subject = designHandle.getSubject( );
				IContent rootContent = reportContent.getRoot( );
				isRtl = rootContent != null && rootContent.isRTL( );
			}
			wordWriter.start( isRtl, creator, title, comments, subject );
			drawDocumentBackground( );
		}
		computePageProperties( page );
		context.addWidth( contentWidth );
		wordWriter.startPage( );
	}

	private void outputPrePageProperties( ) throws IOException, BirtException
	{
		adjustInline( );
		writeSectionInP( );
		wordWriter.endPage( );
	}

	public void end( IReportContent report ) throws IOException, BirtException
	{
		if ( previousPage != null )
		{
			adjustInline( );
			writeSectionInBody( );
			wordWriter.endPage( );
			wordWriter.end( );
		}
	}

	public void endContainer( IContainerContent container )
	{
		// Do nothing.
	}

	public void startContainer( IContainerContent container )
	{
		// Do nothing.
	}

	public abstract void endTable( ITableContent table );

	public abstract void startForeign( IForeignContent foreign )
			throws BirtException;

	protected abstract void writeContent( int type, String txt, IContent content );

	public abstract String getOutputFormat( );

	public void computePageProperties( IPageContent page )
	{
		// Default height/width is the width/height of A4, the width 595.275pt *
		// PT_TWIPS, the height is 841.889 * PT_TWIPS
		pageWidth = WordUtil.convertTo( page.getPageWidth( ), 11906, reportDpi );
		pageHeight = WordUtil.convertTo( page.getPageHeight( ), 16838,
				reportDpi );

		footerHeight = WordUtil.convertTo( page.getFooterHeight( ), 0,
				reportDpi );
		headerHeight = WordUtil.convertTo( page.getHeaderHeight( ), 0,
				reportDpi );

		topMargin = WordUtil.convertTo( page.getMarginTop( ), 0, reportDpi );
		bottomMargin = WordUtil.convertTo( page.getMarginBottom( ), 0,
				reportDpi );

		leftMargin = WordUtil.convertTo( page.getMarginLeft( ), 0, reportDpi );
		rightMargin = WordUtil.convertTo( page.getMarginRight( ), 0, reportDpi );
		
		pageTopBorderWidth = WordUtil.parseBorderSize( PropertyUtil
				.getDimensionValue( page.getComputedStyle( ).getProperty(
						StyleConstants.STYLE_BORDER_TOP_WIDTH ) ) );	
		
		pageBottomBorderWidth = WordUtil.parseBorderSize( PropertyUtil
				.getDimensionValue( page.getComputedStyle( ).getProperty(
						StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ) ) );
		
		pageLeftBorderWidth = WordUtil.parseBorderSize( PropertyUtil
				.getDimensionValue( page.getComputedStyle( ).getProperty(
						StyleConstants.STYLE_BORDER_LEFT_WIDTH ) ) );
		
		pageRightBorderWidth = WordUtil.parseBorderSize( PropertyUtil
				.getDimensionValue( page.getComputedStyle( ).getProperty(
						StyleConstants.STYLE_BORDER_RIGHT_WIDTH ) ) );
		
		contentWidth = pageWidth - leftMargin - rightMargin
				- pageLeftBorderWidth - pageRightBorderWidth;

		orientation = page.getOrientation( );
	}

	public void startAutoText( IAutoTextContent autoText )
	{
		writeContent( autoText.getType( ), autoText.getText( ), autoText );
	}

	public void startData( IDataContent data )
	{
		writeContent( AbstractEmitterImpl.NORMAL, data.getText( ), data );
	}

	public void startLabel( ILabelContent label )
	{
		String txt = label.getText( ) == null ? label.getLabelText( ) : label
				.getText( );
		txt = txt == null ? "" : txt;
		writeContent( AbstractEmitterImpl.NORMAL, txt, label );
	}

	public void startText( ITextContent text )
	{
		writeContent( AbstractEmitterImpl.NORMAL, text.getText( ), text );
	}

	public void startList( IListContent list )
	{
		adjustInline( );

		styles.push( list.getComputedStyle( ) );
		writeBookmark( list );
		Object listToc = list.getTOC( );
		if ( listToc != null && !hasTocOutputed( list ) )
		{
			tableTocs.add( new TocInfo( listToc.toString( ), tocLevel ) );
		}
		increaseTOCLevel( list );

		if ( context.isAfterTable( ) )
		{
			wordWriter.insertHiddenParagraph( );
			context.setIsAfterTable( false );
		}

		int width = WordUtil.convertTo( list.getWidth( ), context
				.getCurrentWidth( ), reportDpi );
		width = Math.min( width, context.getCurrentWidth( ) );
		wordWriter.startTable( list.getComputedStyle( ), width );
		context.startCell( );
		wordWriter.startTableRow( -1 );
		IStyle style = computeStyle( list.getComputedStyle( ) );
		wordWriter.startTableCell( context.getCurrentWidth( ), style, null );
		writeTableToc( );
	}

	public void startListBand( IListBandContent listBand )
	{
	}

	public void startListGroup( IListGroupContent group )
	{
		setGroupToc( group );
	}

	public void startRow( IRowContent row )
	{
		if ( !isHidden( row ) )
		{
			writeBookmark( row );
			rowFilledFlag = false;
			boolean isHeader = false;
			styles.push( row.getComputedStyle( ) );
			if ( row.getBand( ) != null
					&& row.getBand( ).getBandType( ) == IBandContent.BAND_HEADER )
			{
				isHeader = true;
			}

			double height = WordUtil.convertTo( row.getHeight( ), reportDpi );

			wordWriter.startTableRow( height, isHeader, row.getTable( )
					.isHeaderRepeat( ), fixedLayout );
			context.newRow( );
		}
	}

	public void startContent( IContent content )
	{
	}

	public void startGroup( IGroupContent group )
	{
		setGroupToc( group );
	}

	public void startCell( ICellContent cell )
	{
		rowFilledFlag = true;
		context.startCell( );
		writeBookmark( cell );
		int columnId = cell.getColumn( );
		List<SpanInfo> spans = context.getSpans( columnId );

		if ( spans != null )
		{
			for ( int i = 0; i < spans.size( ); i++ )
			{
				wordWriter.writeSpanCell( spans.get( i ) );
			}
		}
		int columnSpan = cell.getColSpan( );
		int rowSpan = cell.getRowSpan( );
		int cellWidth = context.getCellWidth( columnId, columnSpan );

		IStyle style = computeStyle( cell.getComputedStyle( ) );
//		style.get
			
		if ( rowSpan > 1 )
		{
			context.addSpan( columnId, columnSpan, cellWidth, rowSpan, style );
		}

		SpanInfo info = null;

		if ( columnSpan > 1 || rowSpan > 1 )
		{
			info = new SpanInfo( columnId, columnSpan, cellWidth, true, style );
		}
		wordWriter.startTableCell( cellWidth, style, info );
		context.addWidth( getCellWidth( cellWidth, style ) );
		writeTableToc( );
		if ( cell.getDiagonalNumber( ) != 0 && cell.getDiagonalStyle( ) != null
				&& !"none".equalsIgnoreCase( cell.getDiagonalStyle( ) ) )
		{
			drawDiagonalLine( cell, WordUtil.twipToPt( cellWidth ) );
		}
	}

	private boolean hasBorder( String borderStyle )
	{
		return !( borderStyle == null || "none".equalsIgnoreCase( borderStyle ) );
	}

	private void drawDiagonalLine( ICellContent cell, double cellWidth )
	{
		if ( cellWidth == 0 )
			return;
		int cellHeight = WordUtil.convertTo( getCellHeight( cell ), 0,
				reportDpi ) / 20;
		if ( cellHeight == 0 )
			return;

		DiagonalLineInfo diagonalLineInfo = new DiagonalLineInfo( );
		int diagonalWidth = PropertyUtil.getDimensionValue( cell, cell
				.getDiagonalWidth( ), (int) cellWidth ) / 1000;
		diagonalLineInfo.setDiagonalLine( cell.getDiagonalNumber( ), cell
				.getDiagonalStyle( ), diagonalWidth );
		diagonalLineInfo.setAntidiagonalLine( 0, null, 0 );
		diagonalLineInfo.setCoordinateSize( cellWidth, cellHeight );
		String lineColor = null;
		if ( cell.getDiagonalColor( ) != null )
		{
			lineColor = WordUtil.parseColor( cell.getDiagonalColor( ) );
		}
		else
		{
			lineColor = WordUtil.parseColor( cell.getComputedStyle( )
					.getColor( ) );
		}
		diagonalLineInfo.setColor( lineColor );
		wordWriter.drawDiagonalLine( diagonalLineInfo );
	}

	protected DimensionType getCellHeight( ICellContent cell )
	{
		IElement parent = cell.getParent( );
		while ( !( parent instanceof IRowContent ) )
		{
			parent = parent.getParent( );
		}
		return ( (IRowContent) parent ).getHeight( );
	}

	public void startTable( ITableContent table )
	{
		adjustInline( );
		styles.push( table.getComputedStyle( ) );

		writeBookmark( table );
		Object tableToc = table.getTOC( );
		if ( tableToc != null && !hasTocOutputed( table ) )
		{
			tableTocs.add( new TocInfo( tableToc.toString( ), tocLevel ) );
		}
		increaseTOCLevel( table );

		String caption = table.getCaption( );
		if ( caption != null )
		{
			wordWriter.writeCaption( caption );
		}

		if ( context.isAfterTable( ) )
		{
			wordWriter.insertHiddenParagraph( );
			context.setIsAfterTable( false );
		}


		int width = WordUtil.convertTo( table.getWidth( ), context
				.getCurrentWidth( ), reportDpi );
		int[] cols = computeTblColumnWidths( table, width );
		wordWriter
				.startTable( table.getComputedStyle( ), getTableWidth( cols ) );
		wordWriter.writeColumn( cols );
		context.addTable( cols, table.getComputedStyle( ) );
	}

	private int getTableWidth( int[] cols )
	{
		int tableWidth = 0;
		for ( int i = 0; i < cols.length; i++ )
		{
			tableWidth += cols[i];
		}
		return tableWidth;
	}

	public void startTableBand( ITableBandContent band )
	{
	}

	public void startTableGroup( ITableGroupContent group )
	{
		setGroupToc( group );
	}

	private void setGroupToc( IGroupContent group )
	{
		if ( group != null )
		{
			InstanceID groupId = group.getInstanceID( );
			if ( !groupIdList.contains( groupId ) )
			{
				groupIdList.add( groupId );
				Object groupToc = group.getTOC( );
				if ( groupToc != null && !hasTocOutputed( group ) )
				{
					tableTocs
							.add( new TocInfo( groupToc.toString( ), tocLevel ) );
				}
			}
			increaseTOCLevel( group );
		}
	}

	private void writeTableToc( )
	{
		if ( !tableTocs.isEmpty( ) )
		{
			for ( TocInfo toc : tableTocs )
			{
				if ( !"".equals( toc.tocValue ) )
				{
					wordWriter.writeTOC( toc.tocValue, toc.tocLevel );
				}
			}
			tableTocs.clear( );
		}
	}

	public void endCell( ICellContent cell )
	{
		adjustInline( );
		context.removeWidth( );
		wordWriter.endTableCell( context.needEmptyP( ) );
		context.endCell( );
	}

	public void endContent( IContent content )
	{
	}

	public void endGroup( IGroupContent group )
	{
		decreaseTOCLevel( group );
	}

	public void endList( IListContent list )
	{
		adjustInline( );
		wordWriter.endTableCell( context.needEmptyP( ) );
		context.endCell( );
		wordWriter.endTableRow( );
		if ( !styles.isEmpty( ) )
		{
			styles.pop( );
		}

		context.addContainer( true );
		wordWriter.endTable( );
		context.setIsAfterTable( true );
		decreaseTOCLevel( list );
	}

	public void endListBand( IListBandContent listBand )
	{
	}

	public void endListGroup( IListGroupContent group )
	{
		decreaseTOCLevel( group );
	}

	public void endRow( IRowContent row )
	{
		if ( !isHidden( row ) )
		{
			if ( !styles.isEmpty( ) )
			{
				styles.pop( );
			}

			int col = context.getCurrentTableColmns( ).length - 1;

			List<SpanInfo> spans = context.getSpans( col );

			if ( spans != null )
			{
				int spanSize = spans.size( );
				if ( spanSize > 0 )
				{
					rowFilledFlag = true;
				}
				for ( int i = 0; i < spanSize; i++ )
				{
					wordWriter.writeSpanCell( spans.get( i ) );
				}
			}
			if ( !rowFilledFlag )
			{
				wordWriter.writeEmptyCell( );
				rowFilledFlag = true;
			}
			wordWriter.endTableRow( );
		}
	}

	public void endTableBand( ITableBandContent band )
	{
	}

	public void endTableGroup( ITableGroupContent group )
	{
		decreaseTOCLevel( group );
	}

	public void endPage( IPageContent page )
	{
	}

	public void startImage( IImageContent image )
	{
		IStyle style = image.getComputedStyle( );
		InlineFlag inlineFlag = getInlineFlag( style );
		String uri = image.getURI( );
		String mimeType = image.getMIMEType( );
		String extension = image.getExtension( );
		String altText = image.getAltText( );
		double height = WordUtil.convertImageSize( image.getHeight( ), 0,
				reportDpi );
		int parentWidth = (int) ( WordUtil
				.twipToPt( context.getCurrentWidth( ) )
				* reportDpi / 72 );
		double width = WordUtil.convertImageSize( image.getWidth( ),
				parentWidth, reportDpi );
		context.addContainer( false );

		if ( FlashFile.isFlash( mimeType, uri, extension ) )
		{
			if ( altText == null )
			{
				altText = messageFlashObjectNotSupported;
			}
			wordWriter.drawImage( null, height, width, null, style, inlineFlag,
					altText, uri );
			return;
		}

		try
		{
			Image imageInfo = EmitterUtil.parseImage( image, image
					.getImageSource( ), uri, mimeType, extension );

			byte[] data = imageInfo.getData( );
			if ( data == null || data.length == 0 )
			{
				if(imageInfo.getFormat( ) < 0 && altText == null)
				{
					altText = messageReportItemNotSupported;
				}
				wordWriter.drawImage( null, 0.0, 0.0, null, style, inlineFlag,
							altText, uri );
				return;
			}

			int imageFileWidthDpi = imageInfo.getPhysicalWidthDpi( ) == -1
					? 0
					: imageInfo.getPhysicalWidthDpi( );
			int imageFileHeightDpi = imageInfo.getPhysicalHeightDpi( ) == -1
					? 0
					: imageInfo.getPhysicalHeightDpi( );
			if ( image.getHeight( ) == null && image.getWidth( ) == null )
			{
				height = WordUtil
						.convertImageSize( image.getHeight( ), imageInfo
								.getHeight( ), PropertyUtil.getImageDpi( image,
								imageFileHeightDpi, 0 ) );
				width = WordUtil
						.convertImageSize( image.getWidth( ), imageInfo
								.getWidth( ), PropertyUtil.getImageDpi( image,
								imageFileWidthDpi, 0 ) );
			}
			else if ( image.getWidth( ) == null )
			{
				float scale = ( (float) imageInfo.getHeight( ) )
						/ ( (float) imageInfo.getWidth( ) );
				width = height / scale;
			}
			else if ( image.getHeight( ) == null )
			{
				float scale = ( (float) imageInfo.getHeight( ) )
						/ ( (float) imageInfo.getWidth( ) );
				height = width * scale;
			}

			writeBookmark( image );
			writeToc( image );
			HyperlinkInfo hyper = getHyperlink( image );
			wordWriter.drawImage( data, height, width, hyper, style,
					inlineFlag, altText, uri );
		}
		catch ( IOException e )
		{
			logger.log( Level.WARNING, e.getLocalizedMessage( ) );
			wordWriter.drawImage( null, height, width, null, style, inlineFlag,
					altText, uri );
		}
	}

	protected void endTable( )
	{
		context.addContainer( true );
		if ( !styles.isEmpty( ) )
		{
			styles.pop( );
		}

		wordWriter.endTable( );
		context.setIsAfterTable( true );
		context.removeTable( );
	}

	protected void increaseTOCLevel( IContent content )
	{
		if ( content != null && content.getTOC( ) != null )
		{
			tocLevel += 1;
		}
	}

	protected void decreaseTOCLevel( IContent content )
	{
		if ( content != null && content.getTOC( ) != null )
		{
			tocLevel -= 1;
		}
	}

	protected void adjustInline( )
	{
		if ( !context.isFirstInline( ) )
		{
			wordWriter.endParagraph( );
			context.endInline( );
		}
	}

	protected void writeSectionInP( ) throws IOException, BirtException
	{
		wordWriter.startSectionInParagraph( );
		writeHeaderFooter( );
		// the border width /8*20 means to convert an eighth of a point to twips.
		wordWriter.writePageProperties( pageHeight, pageWidth, headerHeight,
				footerHeight, topMargin + (int)(pageTopBorderWidth/8*20), bottomMargin
						+ (int)(pageBottomBorderWidth/8*20), leftMargin
						+ (int)(pageLeftBorderWidth/8*20), rightMargin
						+ (int)(pageRightBorderWidth/8*20), orientation );
		wordWriter.writePageBorders( previousPage.getComputedStyle( ),
				topMargin, bottomMargin, leftMargin, rightMargin );
		wordWriter.endSectionInParagraph( );
	}

	protected void writeSectionInBody( ) throws IOException, BirtException
	{
		wordWriter.startSection( );
		writeHeaderFooter( );
		wordWriter.writePageProperties( pageHeight, pageWidth, headerHeight,
				footerHeight, topMargin + (int)(pageTopBorderWidth/8*20), bottomMargin
						+ (int)(pageBottomBorderWidth/8*20), leftMargin
						+ (int)(pageLeftBorderWidth/8*20), rightMargin
						+ (int)(pageRightBorderWidth/8*20), orientation );
		wordWriter.writePageBorders( previousPage.getComputedStyle( ),
				topMargin, bottomMargin, leftMargin, rightMargin );
		wordWriter.endSection( );
	}

	// TOC must not contain space,word may not process TOC with
	// space
	protected void writeToc( IContent content )
	{
		writeToc( content, false );
	}
	
	protected void writeToc( IContent content, boolean middleInline )
	{
		if ( content != null )
		{
			Object tocObj = content.getTOC( );
			if ( tocObj != null )
			{
				String toc = tocObj.toString( );
				String rgbcolor= content.getStyle( ).getBackgroundColor( );
				String color=EmitterUtil.parseColor( rgbcolor );
				toc = toc.trim( );

				if ( !"".equals( toc ) )
				{
					wordWriter.writeTOC( toc, color, tocLevel, middleInline );
				}
			}
		}
	}

	private InlineFlag getInlineFlag( IStyle style )
	{
		InlineFlag inlineFlag = InlineFlag.BLOCK;
		if ( "inline".equalsIgnoreCase( style.getDisplay( ) ) )
		{
			if ( context.isFirstInline( ) )
			{
				context.startInline( );
				inlineFlag = InlineFlag.FIRST_INLINE;
			}
			else
				inlineFlag = InlineFlag.MIDDLE_INLINE;
		}
		else
		{
			adjustInline( );
		}
		return inlineFlag;
	}

	private Set<String> set = new HashSet<String>( );

	private boolean hasTocOutputed( IContent content )
	{
		String bookmark = content.getBookmark( );
		if ( set.contains( bookmark ) )
		{
			return true;
		}
		else
		{
			set.add( bookmark );
			return false;
		}
	}

	protected void writeBookmark( IContent content )
	{
		String bookmark = content.getBookmark( );
		// birt use __TOC_X_X as bookmark for toc and thus it is not a
		// really bookmark
		if ( bookmark == null || bookmark.startsWith( "_TOC" ) )
		{
			return;
		}
		if ( bookmarks.contains( bookmark ) )
		{
			return;
		}
		bookmark = bookmark.replaceAll( " ", "_" );
		wordWriter.writeBookmark( bookmark );
		bookmarks.add( bookmark );
	}

	protected HyperlinkInfo getHyperlink( IContent content )
	{
		HyperlinkInfo hyperlink = null;
		IHyperlinkAction linkAction = content.getHyperlinkAction( );
		if ( linkAction != null )
		{
			String tooltip = linkAction.getTooltip( );
			String bookmark = linkAction.getBookmark( );
			switch ( linkAction.getType( ) )
			{
				case IHyperlinkAction.ACTION_BOOKMARK :
					bookmark = bookmark.replaceAll( " ", "_" );
					hyperlink = new HyperlinkInfo( HyperlinkInfo.BOOKMARK,
							bookmark, tooltip );
					break;
				case IHyperlinkAction.ACTION_HYPERLINK :
				case IHyperlinkAction.ACTION_DRILLTHROUGH :
					String url = EmitterUtil
							.getHyperlinkUrl( linkAction, reportRunnable,
									actionHandler, reportContext );
					hyperlink = new HyperlinkInfo( HyperlinkInfo.HYPERLINK,
							url, tooltip );
					break;
			}
		}
		if ( hyperlink != null )
		{
			String color = WordUtil
					.parseColor( content.getStyle( ).getColor( ) );
			hyperlink.setColor( color );
		}
		return hyperlink;
	}

	protected void writeText( int type, String txt, IContent content,
			InlineFlag inlineFlag, IStyle computedStyle, IStyle inlineStyle,
			String textAlign )
	{
		HyperlinkInfo hyper = getHyperlink( content );
		int paragraphWidth = (int) WordUtil
				.twipToPt( context.getCurrentWidth( ) );
		boolean rtl = content.isDirectionRTL( );
		if ( content instanceof TextContent )
		{
			TextFlag textFlag = TextFlag.START;
			String fontFamily = null;
			if ( "".equals( txt ) || txt == null || WordUtil.isField( content ) )
			{
				wordWriter.writeContent( type, txt, computedStyle, inlineStyle,
						getFontFamily( computedStyle ), hyper, inlineFlag,
						textFlag, paragraphWidth, rtl, textAlign );
			}
			else
			{
				int level = rtl ? 1 : 0;
				BidiSplitter bidiSplitter = new BidiSplitter( new Chunk( txt,
						0, level, level ) );
				if ( bidiSplitter.hasMore( ) )
				{
					do
					{
						Chunk ch = bidiSplitter.getNext( );
						level = ch.getRunLevel( );
						// TextContent contentClone = (TextContent)
						// content.cloneContent( false );
						// contentClone.setText( ch.getText( ) );
						FontSplitter fontSplitter = getFontSplitter( content,
								ch.getText( ) );
						while ( fontSplitter.hasMore( ) )
						{
							ch = fontSplitter.getNext( );
							wordWriter.writeContent( type, ch.getText( ),
									computedStyle, inlineStyle,
									getFontFamily( computedStyle ), hyper,
									inlineFlag, textFlag, paragraphWidth,
									// TODO: Revisit for more accurate level
									// computation
									( level & 1 ) != 0 || !rtl && level > 0,
									textAlign );
							textFlag = fontSplitter.hasMore( )
									|| bidiSplitter.hasMore( )
									? TextFlag.MIDDLE
									: TextFlag.END;
						}
					} while ( bidiSplitter.hasMore( ) );
				}
				else
				{
					// Should not get here - BidiSplitter will return at least 1 chunk
					FontSplitter fontSplitter = getFontSplitter( content, ( (TextContent) content ).getText( ) );
					while ( fontSplitter.hasMore( ) )
					{
						Chunk ch = fontSplitter.getNext( );
						int offset = ch.getOffset( );
						int length = ch.getLength( );
						fontFamily = getFontFamily( computedStyle );
						String string = null;
						if ( ch instanceof LineBreakChunk)
						{
							string = ch.getText();
						}
						else
						{
							string = txt.substring(offset, offset + length);
						}
						wordWriter.writeContent( type, string, computedStyle,
								inlineStyle, fontFamily, hyper, inlineFlag,
								textFlag, paragraphWidth, rtl, textAlign );
						textFlag = fontSplitter.hasMore( )
								? TextFlag.MIDDLE
								: TextFlag.END;
					}
				}
			}
			if ( inlineFlag == InlineFlag.BLOCK )
			{
				wordWriter.writeContent( type, null, computedStyle,
						inlineStyle, fontFamily, hyper, inlineFlag,
						TextFlag.END, paragraphWidth, rtl, textAlign );
			}
		}
		else
		{
			wordWriter.writeContent( type, txt, computedStyle, inlineStyle,
					getFontFamily( computedStyle ), hyper, inlineFlag,
					TextFlag.WHOLE, paragraphWidth, rtl, textAlign );
		}
	}

	private String getFontFamily( IStyle computedStyle )
	{
		String fontFamily = null;
		// use the user specified font family at first. If user defined a font
		// family list, use the first one, and rely on WORD to do font
		// selection.
		CSSValueList families = (CSSValueList) computedStyle
				.getProperty( StyleConstants.STYLE_FONT_FAMILY );
		if ( families != null && families.getLength( ) > 0 )
		{
			fontFamily = mapGenericFont( families.item( 0 ).getCssText( ) );
		}
		return fontFamily;
	}

	private String mapGenericFont( String font )
	{
		String fontName = genericFontMapping.get( font );
		if ( fontName == null )
		{
			return font;
		}
		else
		{
			return fontName;
		}
	}

	private FontSplitter getFontSplitter( IContent content, String text )
	{
		FontMappingManager fontManager = FontMappingManagerFactory
				.getInstance( ).getFontMappingManager( "doc",
						Locale.getDefault( ) );
		FontSplitter fontSplitter = new FontSplitter( fontManager,
				new Chunk( text ),
				(TextContent) content,
				false,
				false );
		return fontSplitter;
	}

	private boolean isHidden( IContent content )
	{
		if ( content != null )
		{
			IStyle style = content.getStyle( );
			if ( !IStyle.NONE_VALUE.equals( style
					.getProperty( IStyle.STYLE_DISPLAY ) ) )
			{
				return isHiddenByVisibility( content );
			}
			return true;
		}
		return false;
	}

	/**
	 * if the content is hidden
	 * 
	 * @return
	 */
	private boolean isHiddenByVisibility( IContent content )
	{
		assert content != null;
		IStyle style = content.getStyle( );
		String formats = style.getVisibleFormat( );
		return contains( formats, getOutputFormat( ) );
	}

	private boolean contains( String formats, String format )
	{
		if ( formats != null
				&& ( formats.indexOf( EngineIRConstants.FORMAT_TYPE_VIEWER ) >= 0
						|| formats.indexOf( BIRTConstants.BIRT_ALL_VALUE ) >= 0 || formats
						.indexOf( format ) >= 0 ) )
		{
			return true;
		}
		return false;
	}

	protected IStyle computeStyle( IStyle style )
	{
		if ( styles.size( ) == 0 )
		{
			return style;
		}

		for ( int i = 0; i < StyleConstants.NUMBER_OF_STYLE; i++ )
		{
			if ( isInherityProperty( i ) )
			{
				if ( isNullValue( style.getProperty( i ) ) )
				{
					style.setProperty( i, null );

					for ( int p = styles.size( ) - 1; p >= 0; p-- )
					{
						IStyle parent = styles.get( p );

						if ( !isNullValue( parent.getProperty( i ) ) )
						{
							style.setProperty( i, parent.getProperty( i ) );
							break;
						}
					}
				}
			}
		}
		return style;
	}

	protected boolean isNullValue( CSSValue value )
	{
		if ( value == null )
		{
			return true;
		}

		if ( value instanceof DataFormatValue )
		{
			return true;
		}

		if ( value instanceof FloatValue )
		{
			return false;
		}
		String cssText = value.getCssText( );
		return "none".equalsIgnoreCase( cssText )
				|| "transparent".equalsIgnoreCase( cssText );
	}

	private void writeHeaderFooter( ) throws IOException, BirtException
	{
		IStyle style = previousPage.getStyle( );
		String backgroundHeight = style.getBackgroundHeight( );
		String backgroundWidth = style.getBackgroundWidth( );

		SimpleMasterPageDesign master = (SimpleMasterPageDesign) previousPage
				.getGenerateBy( );
		
		if ( previousPage.getPageHeader( ) != null || backgroundHeight != null
				|| backgroundWidth != null )
		{
			wordWriter.startHeader( !master.isShowHeaderOnFirst( ) && previousPage.getPageNumber( ) == 1,
					headerHeight, contentWidth );

			if ( backgroundHeight != null || backgroundWidth != null )
			{
				String backgroundImageUrl = EmitterUtil.getBackgroundImageUrl(
						style, reportContent.getDesign( ).getReportDesign( ),
						reportContext.getAppContext( ) );
				wordWriter.drawDocumentBackgroundImage(
						backgroundImageUrl, backgroundHeight, backgroundWidth,
						WordUtil.twipToPt( topMargin ), WordUtil
								.twipToPt( leftMargin ), WordUtil
								.twipToPt( pageHeight ), WordUtil
								.twipToPt( pageWidth ) );
			}

			contentVisitor.visitChildren( previousPage.getPageHeader( ), null );
			wordWriter.endHeader( );
		}
		if ( previousPage.getPageFooter( ) != null )
		{
			if ( !master.isShowFooterOnLast( )
					&& previousPage.getPageNumber( ) == reportContent
							.getTotalPage( ) )
			{
				IContent footer = previousPage.getPageFooter( );
				ILabelContent emptyContent = footer.getReportContent( )
						.createLabelContent( );
				emptyContent.setText( this.EMPTY_FOOTER );
				wordWriter.startFooter( footerHeight, contentWidth );
				contentVisitor.visit( emptyContent, null );
				wordWriter.endFooter( );
			}
			else
			{
				wordWriter.startFooter( footerHeight, contentWidth );
				contentVisitor.visitChildren( previousPage.getPageFooter( ),
						null );
				wordWriter.endFooter( );
			}
		}
	}

	/**
	 * Transfer background for current page to Doc format. Now, the exported
	 * file will apply the first background properties, and followed background
	 * will ignore.
	 * 
	 * In addition, Since the Word only support fill-in background, the
	 * background attach, pos, posX, posY and repeat are not mapped to Word
	 * easyly. At present, ignore those properties.
	 * 
	 * @throws IOException
	 * 
	 * @TODO support background properties. attach, pos, posx, posy and repeat.
	 */

	protected void drawDocumentBackground( ) throws IOException
	{
		// Set the first page background which is not null to DOC
		IStyle style = previousPage.getStyle( );
		String backgroundColor = style.getBackgroundColor( );
		String backgroundImageUrl = EmitterUtil.getBackgroundImageUrl( style,
				reportContent.getDesign( ).getReportDesign( ), reportContext
						.getAppContext( ) );
		String height = style.getBackgroundHeight( );
		String width = style.getBackgroundWidth( );
		wordWriter.drawDocumentBackground( backgroundColor,
					backgroundImageUrl, height, width );
	}

	private boolean isInherityProperty( int propertyIndex )
	{
		return !nonInherityStyles.contains( propertyIndex );
	}

	private int getCellWidth( int cellWidth, IStyle style )
	{
		int leftPadding = getPadding( style
				.getProperty( IStyle.STYLE_PADDING_LEFT ) );
		int rightPadding = getPadding( style
				.getProperty( IStyle.STYLE_PADDING_RIGHT ) );

		if ( leftPadding > cellWidth )
		{
			leftPadding = 0;
		}

		if ( rightPadding > cellWidth )
		{
			rightPadding = 0;
		}

		if ( ( leftPadding + rightPadding ) > cellWidth )
		{
			rightPadding = 0;
		}

		return (int) ( cellWidth - leftPadding - rightPadding );
	}

	private int getPadding( CSSValue padding )
	{
		return PropertyUtil.getDimensionValue( padding ) / 50;
	}

	private int[] computeTblColumnWidths( ITableContent table, int tblWidth )
	{
		int colCount = table.getColumnCount( );
		int[] tblColumns = new int[colCount];
		int count = 0;
		int total = 0;
		for ( int i = 0; i < colCount; i++ )
		{
			IColumn col = table.getColumn( i );
			if ( col.getWidth( ) == null )
			{
				tblColumns[i] = -1;
				count++;
			}
			else
			{
				tblColumns[i] = WordUtil.convertTo( col.getWidth( ), tblWidth,
						reportDpi );
				total += tblColumns[i];
			}
		}

		if ( table.getWidth( ) == null && count == 0 )
		{
			return tblColumns;
		}
		tblWidth = Math.min( tblWidth, context.getCurrentWidth( ) );
		return EmitterUtil.resizeTableColumn( tblWidth, tblColumns, count,
				total );
	}

	static class TocInfo
	{

		String tocValue;
		int tocLevel;

		TocInfo( String tocValue, int tocLevel )
		{
			this.tocValue = tocValue;
			this.tocLevel = tocLevel;
		}
	}
}

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

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
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
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.layout.pdf.font.FontMappingManager;
import org.eclipse.birt.report.engine.layout.pdf.font.FontMappingManagerFactory;
import org.eclipse.birt.report.engine.layout.pdf.font.FontSplitter;
import org.eclipse.birt.report.engine.layout.pdf.text.Chunk;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;
import org.eclipse.birt.report.engine.util.FlashFile;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.w3c.dom.css.CSSValue;

import com.lowagie.text.pdf.BaseFont;

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

	private String orientation = "portrait";

	private HashSet<String> bookmarks = new HashSet<String>( );

	private boolean rowFilledFlag = false;

	private ArrayList<InstanceID> groupIdList = new ArrayList<InstanceID>( );

	private int tocLevel = 1;

	private IReportRunnable reportRunnable;

	private IHTMLActionHandler actionHandler;

	private IReportContext reportContext;

	public void initialize( IEmitterServices service )
	{
		if ( service != null )
		{
			this.out = EmitterUtil.getOuputStream( service, "report."
					+ getOutputFormat( ) );
			this.reportRunnable = service.getReportRunnable( );
			this.actionHandler = (IHTMLActionHandler) service
					.getOption( RenderOption.ACTION_HANDLER );
			this.reportContext = service.getReportContext( );
		}
		context = new EmitterContext( );
	}

	public void start( IReportContent report )
	{
		this.reportContent = report;
	}

	public void startPage( IPageContent page ) throws IOException
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
			String description = null;
			if ( reportContent != null )
			{
				ReportDesignHandle designHandle = reportContent.getDesign( )
						.getReportDesign( );
				creator = designHandle.getAuthor( );
				title = designHandle
						.getStringProperty( IModuleModel.TITLE_PROP );
				description = designHandle.getDescription( );
				IContent rootContent = reportContent.getRoot( );
				isRtl = rootContent != null && rootContent.isRTL( );
			}
			wordWriter.start( isRtl, creator, title, description );
			drawDocumentBackground( );
		}
		computePageProperties( page );
		context.addWidth( contentWidth );
		wordWriter.startPage( );
	}

	private void outputPrePageProperties( ) throws IOException
	{
		adjustInline( );
		writeSectionInP( );
		wordWriter.endPage( );
	}

	public void end( IReportContent report ) throws IOException
	{
		adjustInline( );
		writeSectionInBody( );
		wordWriter.endPage( );
		wordWriter.end( );
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

	public abstract void startForeign( IForeignContent foreign );

	protected abstract void writeContent( int type, String txt, IContent content );

	public abstract String getOutputFormat( );

	public void computePageProperties( IPageContent page )
	{
		pageWidth = WordUtil.convertTo( page.getPageWidth( ), 0 );
		// 11 inch * 1440
		pageHeight = WordUtil.convertTo( page.getPageHeight( ), 0 );

		footerHeight = WordUtil.convertTo( page.getFooterHeight( ), 0 );
		headerHeight = WordUtil.convertTo( page.getHeaderHeight( ), 0 );

		topMargin = WordUtil.convertTo( page.getMarginTop( ), 0 );
		bottomMargin = WordUtil.convertTo( page.getMarginBottom( ), 0 );

		leftMargin = WordUtil.convertTo( page.getMarginLeft( ), 0 );
		rightMargin = WordUtil.convertTo( page.getMarginRight( ), 0 );

		contentWidth = pageWidth - leftMargin - rightMargin;

		orientation = page.getOrientation( );
	}

	public void startAutoText( IAutoTextContent autoText )
	{
		writeContent( autoText.getType( ), null, autoText );
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
		writeToc( list );
		increaseTOCLevel( list );

		if ( context.isLastTable( ) )
		{
			wordWriter.insertHiddenParagraph( );
		}

		wordWriter.startTable( list.getComputedStyle( ), context
				.getCurrentWidth( ) );
	}

	public void startListBand( IListBandContent listBand )
	{
		context.startCell( );
		wordWriter.startTableRow( -1 );

		IStyle style = computeStyle( listBand.getComputedStyle( ) );

		wordWriter.startTableCell( context.getCurrentWidth( ), style, null );
	}

	public void startListGroup( IListGroupContent group )
	{
		writeGroupToc( group );
	}

	public void startRow( IRowContent row )
	{
		if ( !isHidden( row ) )
		{
			rowFilledFlag = false;
			boolean isHeader = false;
			styles.push( row.getComputedStyle( ) );
			if ( row.getBand( ) != null
					&& row.getBand( ).getBandType( ) == IBandContent.BAND_HEADER )
			{
				isHeader = true;
			}

			double height = WordUtil.convertTo( row.getHeight( ) );

			wordWriter.startTableRow( height, isHeader, row.getTable( )
					.isHeaderRepeat( ) );
			context.newRow( );
		}
	}

	public void startContent( IContent content )
	{
	}

	public void startGroup( IGroupContent group )
	{
		writeGroupToc( group );
	}

	public void startCell( ICellContent cell )
	{
		rowFilledFlag = true;
		context.startCell( );
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
	}

	public void startTable( ITableContent table )
	{
		adjustInline( );
		styles.push( table.getComputedStyle( ) );

		writeBookmark( table );
		writeToc( table );
		increaseTOCLevel( table );
		String caption = table.getCaption( );

		if ( caption != null )
		{
			wordWriter.writeCaption( caption );
		}

		if ( context.isLastTable( ) )
		{
			wordWriter.insertHiddenParagraph( );
		}

		int width = WordUtil.convertTo( table.getWidth( ), context
				.getCurrentWidth( ) );
		width = Math.min( width, context.getCurrentWidth( ) );
		wordWriter.startTable( table.getComputedStyle( ), width );

		int[] cols = computeTblColumnWidths( table, width );
		wordWriter.writeColumn( cols );
		context.addTable( cols, table.getComputedStyle( ) );
	}

	public void startTableBand( ITableBandContent band )
	{
	}

	public void startTableGroup( ITableGroupContent group )
	{
		writeGroupToc( group );
	}

	private void writeGroupToc( IGroupContent group )
	{
		if ( group != null )
		{
			InstanceID groupId = group.getInstanceID( );
			if ( !groupIdList.contains( groupId ) )
			{
				groupIdList.add( groupId );
				writeToc( group );
			}
			increaseTOCLevel( group );
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
		if ( !styles.isEmpty( ) )
		{
			styles.pop( );
		}

		context.addContainer( true );
		wordWriter.endTable( );
		context.setLastIsTable( true );
		decreaseTOCLevel( list );
	}

	public void endListBand( IListBandContent listBand )
	{
		adjustInline( );
		context.endCell( );
		wordWriter.endTableCell( true );
		wordWriter.endTableRow( );
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

		if ( FlashFile.isFlash( mimeType, uri, extension ) )
		{
			wordWriter.drawImage( null, 0.0, 0.0, null, style, inlineFlag,
					altText, uri );
			return;
		}

		byte[] data = org.eclipse.birt.report.engine.layout.emitter.EmitterUtil
				.parseImage( image, image.getImageSource( ), uri, mimeType,
						extension );
		if ( data == null || data.length == 0 )
		{
			wordWriter.drawImage( null, 0.0, 0.0, null, style, inlineFlag,
					altText, uri );
			return;
		}

		int defaultW = 0;
		int defaultH = 0;
		try
		{
			Image imageData = javax.imageio.ImageIO
					.read( new ByteArrayInputStream( data ) );

			if ( imageData != null )
			{
				defaultW = imageData.getWidth( null );// pix

				defaultH = imageData.getHeight( null );
			}
		}
		catch ( Exception e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
		}

		double height = WordUtil
				.convertImageSize( image.getHeight( ), defaultH );
		double width = WordUtil.convertImageSize( image.getWidth( ), defaultW );

		writeBookmark( image );
		writeToc( image );
		HyperlinkInfo hyper = getHyperlink( image );
		wordWriter.drawImage( data, height, width, hyper, style, inlineFlag,
				altText, uri );
	}

	protected void endTable( )
	{
		context.addContainer( true );
		if ( !styles.isEmpty( ) )
		{
			styles.pop( );
		}

		wordWriter.endTable( );
		context.setLastIsTable( true );
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

	protected void writeSectionInP( ) throws IOException
	{
		wordWriter.startSectionInParagraph( );
		writeHeaderFooter( );
		wordWriter.writePageProperties( pageHeight, pageWidth, headerHeight,
				footerHeight, topMargin, bottomMargin, leftMargin, rightMargin,
				orientation );
		wordWriter.endSectionInParagraph( );
	}

	protected void writeSectionInBody( ) throws IOException
	{
		wordWriter.startSection( );
		writeHeaderFooter( );
		wordWriter.writePageProperties( pageHeight, pageWidth, headerHeight,
				footerHeight, topMargin, bottomMargin, leftMargin, rightMargin,
				orientation );
		wordWriter.endSection( );
	}

	// TOC must not contain space,word may not process TOC with
	// space
	protected void writeToc( IContent content )
	{
		if ( content != null )
		{
			Object tocObj = content.getTOC( );
			if ( tocObj != null )
			{
				String toc = tocObj.toString( );
				toc = toc.trim( );

				if ( !"".equals( toc ) )
				{
					wordWriter.writeTOC( toc, tocLevel );
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
					String url = org.eclipse.birt.report.engine.layout.emitter.EmitterUtil
							.getHyperlinkUrl( linkAction, reportRunnable,
									actionHandler, reportContext );
					hyperlink = new HyperlinkInfo( HyperlinkInfo.HYPERLINK,
							url, tooltip );
					break;
			}
		}
		return hyperlink;
	}

	protected void writeText( int type, String txt, IContent content,
			InlineFlag inlineFlag, IStyle computedStyle, IStyle inlineStyle )
	{
		HyperlinkInfo hyper = getHyperlink( content );

		if ( content instanceof TextContent )
		{
			TextFlag textFlag = TextFlag.START;
			String fontFamily = null;
			if ( "".equals( txt ) )
			{
				wordWriter.writeContent( type, txt, computedStyle, inlineStyle,
						fontFamily, hyper, inlineFlag, textFlag );
			}
			else
			{
				FontSplitter fontSplitter = getFontSplitter( content );
				while ( fontSplitter.hasMore( ) )
				{
					Chunk ch = fontSplitter.getNext( );
					fontFamily = getFontFamily( computedStyle, ch );
					wordWriter.writeContent( type, ch.getText( ),
							computedStyle, inlineStyle, fontFamily, hyper,
							inlineFlag, textFlag );
					textFlag = fontSplitter.hasMore( )
							? TextFlag.MIDDLE
							: TextFlag.END;
				}
			}
			if ( inlineFlag == InlineFlag.BLOCK )
			{
				wordWriter.writeContent( type, null, computedStyle,
						inlineStyle, fontFamily, hyper, inlineFlag,
						TextFlag.END );
			}
		}
		else
		{
			wordWriter.writeContent( type, txt, computedStyle, inlineStyle,
					computedStyle.getFontFamily( ), hyper, inlineFlag,
					TextFlag.WHOLE );
		}
	}

	private String getFontFamily( IStyle c_style, Chunk ch )
	{
		String fontFamily;
		FontInfo info = ch.getFontInfo( );
		BaseFont basefont = null;
		if ( info != null )
		{
			basefont = info.getBaseFont( );
			String[][] fontName = basefont.getFamilyFontName( );
			int x = fontName.length - 1;
			int y = fontName[0].length - 1;
			fontFamily = fontName[x][y];
		}
		else
		{
			fontFamily = c_style.getFontFamily( );
		}
		return fontFamily;
	}

	private FontSplitter getFontSplitter( IContent content )
	{
		FontMappingManager fontManager = FontMappingManagerFactory
				.getInstance( ).getFontMappingManager( "doc",
						Locale.getDefault( ) );
		String text = ( (TextContent) content ).getText( );
		FontSplitter fontSplitter = new FontSplitter( fontManager, new Chunk(
				text ), (TextContent) content, true );
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
		String cssText = value.getCssText( );
		return "none".equalsIgnoreCase( cssText )
				|| "transparent".equalsIgnoreCase( cssText );
	}

	private void writeHeaderFooter( ) throws IOException
	{
		if ( previousPage.getPageHeader( ) != null )
		{
			SimpleMasterPageDesign master = (SimpleMasterPageDesign) previousPage
					.getGenerateBy( );
			wordWriter.startHeader( !master.isShowHeaderOnFirst( ) );
			contentVisitor.visitChildren( previousPage.getPageHeader( ), null );
			wordWriter.endHeader( );
		}
		if ( previousPage.getPageFooter( ) != null )
		{
			wordWriter.startFooter( );
			contentVisitor.visitChildren( previousPage.getPageFooter( ), null );
			wordWriter.endFooter( );
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
		IStyle style = previousPage.getComputedStyle( );
		String backgroundColor = style.getBackgroundColor( );
		String backgroundImageUrl = org.eclipse.birt.report.engine.layout.emitter.EmitterUtil
				.getBackgroundImageUrl( style, reportContent.getDesign( )
						.getReportDesign( ) );
		wordWriter.drawDocumentBackground( backgroundColor, backgroundImageUrl );
	}

	private boolean isInherityProperty( int propertyIndex )
	{
		return !nonInherityStyles.contains( propertyIndex );
	}

	private int getCellWidth( int cellWidth, IStyle style )
	{
		float leftPadding = getPadding( style.getPaddingLeft( ) );
		float rightPadding = getPadding( style.getPaddingRight( ) );

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

	private float getPadding( String padding )
	{
		float value = 0;
		// Percentage value will be omitted
		try
		{
			value = Float.parseFloat( padding ) / 50;
		}
		catch ( Exception e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
		}
		return value;
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
				tblColumns[i] = WordUtil.convertTo( col.getWidth( ), tblWidth );
				total += tblColumns[i];
			}
		}

		for ( int i = 0; i < tblColumns.length; i++ )
		{
			if ( tblColumns[i] == -1 )
			{
				tblColumns[i] = ( tblWidth - total ) / count;
			}
		}
		return tblColumns;
	}
}

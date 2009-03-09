
package org.eclipse.birt.report.engine.emitter.excel;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.emitter.XMLWriter;
import org.eclipse.birt.report.engine.emitter.excel.layout.ExcelContext;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;

public class ExcelXmlWriter implements IExcelWriter
{

	private boolean isRTLSheet = false; // bidi_acgc added
	public static final int rightToLeftisTrue = 1; // bidi_acgc added
	private final XMLWriterXLS writer = new XMLWriterXLS( );

	public XMLWriterXLS getWriter( )
	{
		return writer;
	}

	private String pageHeader, pageFooter, orientation;
	private int sheetIndex = 1;

	public class XMLWriterXLS extends XMLWriter
	{

		public PrintWriter getPrint( )
		{
			return printWriter;
		}

		@Override
		protected String getEscapedStr( String s, boolean whitespace )
		{
			s = super.getEscapedStr( s, whitespace );

			StringBuffer buffer = new StringBuffer();

			for ( int i = 0, max = s.length( ); i < max; i++ )
			{
				char c = s.charAt( i );

				if ( c == '\n' || c == '\r' )
				{
					buffer.append( "&#10;" ); //$NON-NLS-1$
					if ( c == '\r' && i + 1 < max && s.charAt( i + 1 ) == '\n')
					{
						i++;
					}
				}
				else
				{
					buffer.append( c );
				}

			}
			return buffer.toString( );
		}
	}

	protected static Logger logger = Logger.getLogger( ExcelXmlWriter.class
			.getName( ) );

	ExcelContext context = null;

	public ExcelXmlWriter( OutputStream out , ExcelContext context )
	{
		this( out, "UTF-8" , context);
	}

	public ExcelXmlWriter( OutputStream out )
	{
		writer.open( out, "UTF-8" );
	}


	public ExcelXmlWriter( OutputStream out, String encoding, ExcelContext context )
	{
		this.context = context;
		writer.open( out, encoding );
	}

	/**
	 * @author bidi_acgc
	 * @param isRTLSheet:
	 *            represents the direction of the excel sheet.
	 */
	public ExcelXmlWriter( OutputStream out, boolean isRTLSheet )
	{
		this.isRTLSheet = isRTLSheet;
		writer.open( out, "UTF-8" );
	}

	/**
	 * @author bidi_acgc
	 * @param orientation
	 * @param pageFooter
	 * @param pageHeader
	 * @param isRTLSheet
	 *            : represents the direction of the excel sheet.
	 */
	public ExcelXmlWriter( OutputStream out, ExcelContext context,
			boolean isRTLSheet )
	{
		this( out, "UTF-8", context );
		this.isRTLSheet = isRTLSheet;
	}

	/**
	 * @author bidi_acgc
	 * @param isRTLSheet:
	 *            represents the direction of the excel sheet.
	 */
	public ExcelXmlWriter( OutputStream out, String encoding,
			ExcelContext context, boolean isRTLSheet )
	{
		this.context = context;
		this.isRTLSheet = isRTLSheet;
		writer.open( out, encoding );
	}

	private void writeDocumentProperties( IReportContent reportContent )
	{
		ReportDesignHandle reportDesign = reportContent.getDesign( )
		.getReportDesign( );
		writer.openTag( "DocumentProperties" );
		writer.attribute( "xmlns", "urn:schemas-microsoft-com:office:office" );
		writer.openTag( "Author" );
		writer
		.text( reportDesign
				.getStringProperty( IModuleModel.AUTHOR_PROP ) );
		writer.closeTag( "Author" );
		writer.openTag( "Title" );
		writer.text( reportDesign.getStringProperty( IModuleModel.TITLE_PROP ) );
		writer.closeTag( "Title" );
		writer.openTag( "Description" );
		writer.text( reportDesign
				.getStringProperty( IModuleModel.DESCRIPTION_PROP ) );
		writer.closeTag( "Description" );
		writer.closeTag( "DocumentProperties" );
	}

	// If possible, we can pass a format according the data type
	private void writeText( Data d )
	{
		writer.openTag( "Data" );

		int type = d.getDatatype( );
		if ( type == SheetData.NUMBER )
		{
			if ( d.isNaN( ) || d.isBigNumber( ) || d.isInfility( ) )
			{
				writer.attribute( "ss:Type", "String" );
			}
			else
			{
				writer.attribute( "ss:Type", "Number" );
			}
		}
		else if ( type == SheetData.DATE )
		{
			writer.attribute( "ss:Type", "DateTime" );
		}
		else
		{
			writer.attribute( "ss:Type", "String" );
		}

		d.formatTxt( );
		String txt = d.getText( ).toString( );

		if ( CSSConstants.CSS_CAPITALIZE_VALUE.equalsIgnoreCase( d.getStyle( )
				.getProperty( StyleConstant.TEXT_TRANSFORM ) ) )
		{
			txt = capitalize( txt );
		}
		else if ( CSSConstants.CSS_UPPERCASE_VALUE.equalsIgnoreCase( d
				.getStyle( ).getProperty( StyleConstant.TEXT_TRANSFORM ) ) )
		{
			txt = txt.toUpperCase( );
		}
		else if ( CSSConstants.CSS_LOWERCASE_VALUE.equalsIgnoreCase( d
				.getStyle( ).getProperty( StyleConstant.TEXT_TRANSFORM ) ) )
		{
			txt = txt.toLowerCase( );
		}

		writer.text( txt );

		writer.closeTag( "Data" );
	}

	private String capitalize( String text )
	{
		boolean capitalizeNextChar = true;
		char[] array = text.toCharArray( );
		for ( int i = 0; i < array.length; i++ )
		{
			char c = text.charAt( i );
			if ( c == ' ' || c == '\n' || c == '\r' )
				capitalizeNextChar = true;
			else if ( capitalizeNextChar )
			{
				array[i] = Character.toUpperCase( array[i] );
				capitalizeNextChar = false;
			}
		}
		return new String( array );
	}

	public void startRow( double rowHeight )
	{
		writer.openTag( "Row" );
	}

	public void endRow( )
	{
		writer.closeTag( "Row" );
	}

	private void startCell( int cellindex, int colspan, int rowspan,
			int styleid, HyperlinkDef hyperLink ,BookmarkDef linkedBookmark)
	{
		writer.openTag( "Cell" );
		writer.attribute( "ss:Index", cellindex );
		writer.attribute( "ss:StyleID", styleid );

		if ( hyperLink != null )
		{
			String urlAddress = hyperLink.getUrl( );
			if ( hyperLink.getType( ) == IHyperlinkAction.ACTION_BOOKMARK )
			{
				if ( linkedBookmark != null )
					urlAddress = "#" + linkedBookmark.getValidName( );
				else
				{
					logger.log( Level.WARNING, "The bookmark: {" + urlAddress
							+ "} is not defined!" );
				}
			}
			if ( urlAddress != null && urlAddress.length( ) >= 255 )
			{
				logger.log( Level.WARNING, "The URL: {" + urlAddress
						+ "} is too long!" );
				urlAddress = urlAddress.substring( 0, 254 );
			}
			writer.attribute( "ss:HRef", urlAddress );
		}
		writer.attribute( "ss:MergeAcross", colspan );
		writer.attribute( "ss:MergeDown", rowspan );
	}

	public void writeDefaultCell( Data d )
	{
		writer.openTag( "Cell" );

		if ( d.getStyleId( ) != 0 )
		{
			writer.attribute( "ss:StyleID", d.getStyleId( ) );
		}

		writeText( d );
		writer.closeTag( "Cell" );
	}

	public void outputData( SheetData sheetData )
	{
		if ( sheetData.getDatatype( ) == SheetData.IMAGE )
			return;
		Data d = (Data) sheetData;
		startCell( d.span.getCol( ), d.span.getColSpan( ), d.getRowSpan( ),
				d.styleId, d.hyperLink, d.getLinkedBookmark( ) );
		writeText( d );
		if(d.hyperLink != null && d.hyperLink.getToolTip( ) != null)
		{
			writeComments(d.hyperLink);
		}
		endCell( );
	}

	protected void writeComments(HyperlinkDef linkDef)
	{
		String toolTip = linkDef.getToolTip( );
		writer.openTag( "Comment" );
		writer.openTag( "ss:Data" );
		writer.attribute( "xmlns", "http://www.w3.org/TR/REC-html40" );
		writer.openTag( "Font" );
		//		writer.attribute( "html:Face", "Tahoma" );
		//		writer.attribute( "x:CharSet", "1" );
		//		writer.attribute( "html:Size", "8" );
		//		writer.attribute( "html:Color", "#000000" );
		writer.text( toolTip );
		writer.closeTag( "Font" );
		writer.closeTag( "ss:Data" );
		writer.closeTag( "Comment" );
	}

	protected void writeFormulaData( Data d )
	{
		writer.openTag( "Cell" );
		writer.attribute( "ss:Index", d.span.getCol( ) );
		writer.attribute( "ss:Formula", d.txt.toString( ) );
		writer.attribute( "ss:MergeAcross", d.span.getColSpan( ) );
		writer.attribute( "ss:StyleID", d.styleId );
		writer.closeTag( "Cell" );
	}

	private void endCell( )
	{
		writer.closeTag( "Cell" );
	}

	private void writeAlignment( String horizontal, String vertical,
			String direction, boolean wrapText )
	{
		writer.openTag( "Alignment" );

		if ( isValid( horizontal ) )
		{
			writer.attribute( "ss:Horizontal", horizontal );
		}

		if ( isValid( vertical ) )
		{
			writer.attribute( "ss:Vertical", vertical );
		}

		if ( isValid( direction ) )
		{
			if ( CSSConstants.CSS_RTL_VALUE.equals( direction ) )
				writer.attribute( "ss:ReadingOrder", "RightToLeft" );
			else
				writer.attribute( "ss:ReadingOrder", "LeftToRight" );
		}
		if(wrapText)
		{
			writer.attribute( "ss:WrapText", "1" );
		}

		writer.closeTag( "Alignment" );
	}

	private void writeBorder( String position, String lineStyle, String weight,
			String color )
	{
		writer.openTag( "Border" );
		writer.attribute( "ss:Position", position );
		if ( isValid( lineStyle ) )
		{
			writer.attribute( "ss:LineStyle", lineStyle );
		}

		if ( isValid( weight ) )
		{
			writer.attribute( "ss:Weight", weight );
		}

		if ( isValid( color ) )
		{
			writer.attribute( "ss:Color", color );
		}

		writer.closeTag( "Border" );
	}

	private void writeFont( String fontName, String size, String bold,
			String italic, String strikeThrough, String underline, String color )
	{
		writer.openTag( "Font" );

		if ( isValid( fontName ) )
		{
			writer.attribute( "ss:FontName", fontName );
		}

		if ( isValid( size ) )
		{
			writer.attribute( "ss:Size", size );
		}

		if ( isValid( bold ) )
		{
			writer.attribute( "ss:Bold", bold );
		}

		if ( isValid( italic ) )
		{
			writer.attribute( "ss:Italic", italic );
		}

		if ( isValid( strikeThrough ) )
		{
			writer.attribute( "ss:StrikeThrough", strikeThrough );
		}

		if ( isValid( underline ) && !"0".equalsIgnoreCase( underline ) )
		{
			writer.attribute( "ss:Underline", "Single" );
		}

		if ( isValid( color ) )
		{
			writer.attribute( "ss:Color", color );
		}

		writer.closeTag( "Font" );
	}

	private void writeBackGroudColor( String bgColor )
	{
		if ( isValid( bgColor ) )
		{
			writer.openTag( "Interior" );
			writer.attribute( "ss:Color", bgColor );
			writer.attribute( "ss:Pattern", "Solid" );
			writer.closeTag( "Interior" );
		}
	}

	private boolean isValid( String value )
	{
		return !StyleEntry.isNull( value );
	}

	private void declareStyle( StyleEntry style, int id )
	{
		boolean wrapText = context.getWrappingText( );
		String whiteSpace = style.getProperty( StyleConstant.WHITE_SPACE );
		if ( CSSConstants.CSS_NOWRAP_VALUE.equals( whiteSpace ) )
		{
			wrapText = false;
		}

		writer.openTag( "Style" );
		writer.attribute( "ss:ID", id );
		if ( style.getName( ) == StyleEntry.ENTRYNAME_HYPERLINK )
		{
			writer.attribute( "ss:Parent", "HyperlinkId" );
		}

		if ( id >= StyleEngine.RESERVE_STYLE_ID )
		{
			String direction = style.getProperty( StyleConstant.DIRECTION_PROP ); // bidi_hcg
			String horizontalAlign = style
					.getProperty( StyleConstant.H_ALIGN_PROP );
			String verticalAlign = style
					.getProperty( StyleConstant.V_ALIGN_PROP );
			writeAlignment( horizontalAlign, verticalAlign, direction, wrapText );
			writer.openTag( "Borders" );
			String bottomColor = style
					.getProperty( StyleConstant.BORDER_BOTTOM_COLOR_PROP );
			String bottomLineStyle = style
					.getProperty( StyleConstant.BORDER_BOTTOM_STYLE_PROP );
			String bottomWeight = style
					.getProperty( StyleConstant.BORDER_BOTTOM_WIDTH_PROP );
			writeBorder( "Bottom", bottomLineStyle, bottomWeight, bottomColor );

			String topColor = style
					.getProperty( StyleConstant.BORDER_TOP_COLOR_PROP );
			String topLineStyle = style
					.getProperty( StyleConstant.BORDER_TOP_STYLE_PROP );
			String topWeight = style
					.getProperty( StyleConstant.BORDER_TOP_WIDTH_PROP );
			writeBorder( "Top", topLineStyle, topWeight, topColor );

			String leftColor = style
					.getProperty( StyleConstant.BORDER_LEFT_COLOR_PROP );
			String leftLineStyle = style
					.getProperty( StyleConstant.BORDER_LEFT_STYLE_PROP );
			String leftWeight = style
					.getProperty( StyleConstant.BORDER_LEFT_WIDTH_PROP );
			writeBorder( "Left", leftLineStyle, leftWeight, leftColor );

			String rightColor = style
					.getProperty( StyleConstant.BORDER_RIGHT_COLOR_PROP );
			String rightLineStyle = style
					.getProperty( StyleConstant.BORDER_RIGHT_STYLE_PROP );
			String rightWeight = style
					.getProperty( StyleConstant.BORDER_RIGHT_WIDTH_PROP );
			writeBorder( "Right", rightLineStyle, rightWeight, rightColor );
			writer.closeTag( "Borders" );

			String fontName = style
					.getProperty( StyleConstant.FONT_FAMILY_PROP );
			String size = style.getProperty( StyleConstant.FONT_SIZE_PROP );
			String fontStyle = style
					.getProperty( StyleConstant.FONT_STYLE_PROP );
			String fontWeight = style
					.getProperty( StyleConstant.FONT_WEIGHT_PROP );
			String strikeThrough = style
					.getProperty( StyleConstant.TEXT_LINE_THROUGH_PROP );
			String underline = style
					.getProperty( StyleConstant.TEXT_UNDERLINE_PROP );
			String color = style.getProperty( StyleConstant.COLOR_PROP );
			writeFont( fontName, size, fontWeight, fontStyle, strikeThrough,
					underline, color );
			String bgColor = style
					.getProperty( StyleConstant.BACKGROUND_COLOR_PROP );
			writeBackGroudColor( bgColor );
		}

		writeDataFormat( style );

		writer.closeTag( "Style" );
	}

	private void writeDataFormat( StyleEntry style )
	{
		String typeString = style.getProperty( StyleConstant.DATA_TYPE_PROP );
		if ( typeString == null )
			return;
		int type = Integer.parseInt( typeString );
		if ( type == SheetData.DATE
				&& style.getProperty( StyleConstant.DATE_FORMAT_PROP ) != null )
		{
			writer.openTag( "NumberFormat" );
			writer.attribute( "ss:Format", style
					.getProperty( StyleConstant.DATE_FORMAT_PROP ) );
			writer.closeTag( "NumberFormat" );

		}

		if ( type == Data.NUMBER
				&& style.getProperty( StyleConstant.NUMBER_FORMAT_PROP ) != null )
		{
			writer.openTag( "NumberFormat" );

			String numberStyle = style
			.getProperty( StyleConstant.NUMBER_FORMAT_PROP );
			writer.attribute( "ss:Format", numberStyle );
			writer.closeTag( "NumberFormat" );
		}
	}

	// here the user input can be divided into two cases :
	// the case in the birt input like G and the Currency
	// the case in excel format : like 0.00E00

	private void writeDeclarations( )
	{
		writer.startWriter( );
		writer.getPrint( ).println( );
		writer.getPrint( ).println(
		"<?mso-application progid=\"Excel.Sheet\"?>" );

		writer.openTag( "Workbook" );

		writer.attribute( "xmlns",
		"urn:schemas-microsoft-com:office:spreadsheet" );
		writer.attribute( "xmlns:o", "urn:schemas-microsoft-com:office:office" );
		writer.attribute( "xmlns:x", "urn:schemas-microsoft-com:office:excel" );
		writer.attribute( "xmlns:ss",
		"urn:schemas-microsoft-com:office:spreadsheet" );
		writer.attribute( "xmlns:html", "http://www.w3.org/TR/REC-html40" );
	}

	private void declareStyles( Map<StyleEntry, Integer> style2id )
	{
		writer.openTag( "Styles" );
		declareHyperlinkStyle( );
		Set<Entry<StyleEntry, Integer>> entrySet = style2id.entrySet( );
		for ( Map.Entry<StyleEntry, Integer> entry : entrySet )
		{
			declareStyle( entry.getKey( ), entry.getValue( ) );
		}

		writer.closeTag( "Styles" );
	}


	private void declareHyperlinkStyle( )
	{
		writer.openTag( "Style" );
		writer.attribute( "ss:ID", "HyperlinkId" );
		writer.attribute( "ss:Name", "Hyperlink" );
		writeFont( null, null, null, null, null, "Single", "#0000FF" );
		writer.closeTag( "Style" );
	}

	private void defineNames( Entry<String, BookmarkDef> bookmarkEntry )
	{
		BookmarkDef bookmark = bookmarkEntry.getValue( );
		String name = bookmark.getValidName( );
		String refer = getRefer( bookmark.getSheetIndex( ), bookmark );
		defineName( name, refer );
	}

	private String getRefer( int sheetIndex, BookmarkDef bookmark )
	{
		StringBuffer sb = new StringBuffer( "=Sheet" );
		sb.append( sheetIndex );
		sb.append( "!R" );
		sb.append( bookmark.getRowNo( ) );
		sb.append( "C" );
		sb.append( bookmark.getColumnNo( ) );
		return sb.toString( );
	}

	private void defineName( String name, String refer )
	{
		writer.openTag( "NamedRange" );
		writer.attribute( "ss:Name", name );
		writer.attribute( "ss:RefersTo", refer );
		writer.closeTag( "NamedRange" );
	}

	public void startSheet( String name )
	{
		writer.openTag( "Worksheet" );
		writer.attribute( "ss:Name", name );

		// Set the Excel Sheet RightToLeft attribute according to Report
		//if Report Bidi-Orientation is RTL, then Sheet is RTL.
		if ( isRTLSheet )
			writer.attribute( "ss:RightToLeft", rightToLeftisTrue );
		// else : do nothing i.e. LTR
	}

	public void closeSheet( )
	{
		writer.closeTag( "Worksheet" );
		writer.endWriter( );
	}

	public void ouputColumns( int[] width )
	{
		writer.openTag( "ss:Table" );

		if ( width == null )
		{
			logger.log( Level.SEVERE, "Invalid columns width" );
			return;
		}

		for ( int i = 0; i < width.length; i++ )
		{
			writer.openTag( "ss:Column" );
			writer.attribute( "ss:Width", width[i] );
			writer.closeTag( "ss:Column" );
		}
	}

	public void endTable( )
	{
		writer.closeTag( "ss:Table" );
	}

	public void insertHorizontalMargin( int height, int span )
	{
		writer.openTag( "Row" );
		writer.attribute( "ss:AutoFitHeight", 0 );
		writer.attribute( "ss:Height", height );

		writer.openTag( "Cell" );
		writer.attribute( " ss:MergeAcross", span );
		writer.closeTag( "Cell" );

		writer.closeTag( "Row" );
	}

	public void insertVerticalMargin( int start, int end, int length )
	{
		writer.openTag( "Row" );
		writer.attribute( "ss:AutoFitHeight", 0 );
		writer.attribute( "ss:Height", 1 );

		writer.openTag( "Cell" );
		writer.attribute( "ss:Index", start );
		writer.attribute( " ss:MergeDown", length );
		writer.closeTag( "Cell" );

		writer.openTag( "Cell" );
		writer.attribute( "ss:Index", end );
		writer.attribute( " ss:MergeDown", length );
		writer.closeTag( "Cell" );

		writer.closeTag( "Row" );
	}

	private void declareWorkSheetOptions( String orientation,
			String pageHeader, String pageFooter )
	{
		writer.openTag( "WorksheetOptions" );
		writer.attribute( "xmlns", "urn:schemas-microsoft-com:office:excel" );
		writer.openTag( "PageSetup" );

		if(orientation!=null)
		{
			writer.openTag( "Layout" );
			writer.attribute( "x:Orientation", orientation );
			writer.closeTag( "Layout" );
		}

		if(pageHeader!=null)
		{
			writer.openTag( "Header" );
			writer.attribute( "x:Data", pageHeader );
			writer.closeTag( "Header" );
		}

		if(pageFooter!=null)
		{
			writer.openTag( "Footer" );
			writer.attribute( "x:Data", pageFooter );
			writer.closeTag( "Footer" );
		}

		writer.closeTag( "PageSetup" );
		writer.closeTag( "WorksheetOptions" );
	}

	private void startSheet( int sheetIndex )
	{
		startSheet( "Sheet" + String.valueOf( sheetIndex ) );
	}

	public void startSheet( int[] coordinates, String pageHeader,
			String pageFooter )
	{
		this.pageHeader = pageHeader;
		this.pageFooter = pageFooter;
		startSheet( sheetIndex );
		ouputColumns( coordinates );
		sheetIndex += 1;
	}

	public void endSheet( String orientaion )
	{
		endTable( );
		declareWorkSheetOptions( orientation, pageHeader, pageFooter );
		closeSheet( );
	}

	public void start( IReportContent report, Map<StyleEntry, Integer> styles,
			HashMap<String, BookmarkDef> bookmarkList )
	{
		writeDeclarations( );
		writeDocumentProperties( report );
		declareStyles( styles);
		outputBookmarks( bookmarkList );
	}

	private void outputBookmarks( HashMap<String, BookmarkDef> bookmarkList )
	{
		if ( !bookmarkList.isEmpty( ) )
		{
			writer.openTag( "Names" );
			Set<Entry<String, BookmarkDef>> bookmarkEntry = bookmarkList
			.entrySet( );
			for ( Entry<String, BookmarkDef> bookmark : bookmarkEntry )
				defineNames( bookmark );
			writer.closeTag( "Names" );
		}
	}

	public void end( )
	{
		writer.closeTag( "Workbook" );
		close( );
	}

	public void close( )
	{
		writer.endWriter( );
		writer.close( );
	}

	public void setSheetIndex( int sheetIndex )
	{
		this.sheetIndex = sheetIndex;
	}

}

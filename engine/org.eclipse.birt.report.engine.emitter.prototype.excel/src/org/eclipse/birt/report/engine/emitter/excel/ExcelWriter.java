
package org.eclipse.birt.report.engine.emitter.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.emitter.XMLWriter;

public class ExcelWriter
{

	private XMLWriterXLS writer = new XMLWriterXLS( );

	private static HashSet splitChar = new HashSet( );

	static
	{
		splitChar.add( new Character( ' ' ) );
		splitChar.add( new Character( '\r' ) );
		splitChar.add( new Character( '\n' ) );
	};

	private class XMLWriterXLS extends XMLWriter
	{

		public PrintWriter getPrint( )
		{
			return printWriter;
		}

		protected String getEscapedStr( String s, boolean whitespace )
		{
			s = super.getEscapedStr( s, whitespace );

			StringBuffer result = null;

			char[] s2char = s.toCharArray( );

			for ( int i = 0, max = s2char.length, delta = 0; i < max; i++ )
			{
				char c = s2char[i];
				String replacement = null;

				if ( c == '\n' )
				{
					replacement = "&#10;"; //$NON-NLS-1$
				}

				if ( replacement != null )
				{
					if ( result == null )
					{
						result = new StringBuffer( s );
					}
					result.replace( i + delta, i + delta + 1, replacement );
					delta += ( replacement.length( ) - 1 );
				}
			}
			if ( result == null )
			{
				return s;
			}
			return result.toString( );
		}
	}

	protected static Logger logger = Logger.getLogger( ExcelWriter.class
			.getName( ) );

	public ExcelWriter( OutputStream out )
	{
		this( out, "UTF-8" );
	}

	public ExcelWriter( OutputStream out, String encoding )
	{
		writer.open( out, encoding );
	}

	// If possible, we can pass a format according the data type
	public void writeText( Data d )
	{
		writer.openTag( "Data" );

		if ( d.getDatatype( ).equals( Data.NUMBER )
				&& ExcelUtil.isNumber( d.getText( ) ) )
		{
			writer.attribute( "ss:Type", "Number" );
		}
		else if ( d.getDatatype( ).equals( Data.DATE ) )
		{
			writer.attribute( "ss:Type", "DateTime" );
		}
		else
		{
			writer.attribute( "ss:Type", "String" );
		}

		String txt = d.getText( );
		if ( CSSConstants.CSS_CAPITALIZE_VALUE.equalsIgnoreCase( d
				.getStyleEntry( ).getProperty( StyleConstant.TEXT_TRANSFORM ) ) )
		{
			txt = capitalize( txt );
		}
		else if ( CSSConstants.CSS_UPPERCASE_VALUE.equalsIgnoreCase( d
				.getStyleEntry( ).getProperty( StyleConstant.TEXT_TRANSFORM ) ) )
		{
			txt = txt.toUpperCase( );
		}
		else if ( CSSConstants.CSS_LOWERCASE_VALUE.equalsIgnoreCase( d
				.getStyleEntry( ).getProperty( StyleConstant.TEXT_TRANSFORM ) ) )
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
			Character c = new Character( text.charAt( i ) );
			if ( splitChar.contains( c ) )
				capitalizeNextChar = true;
			else if ( capitalizeNextChar )
			{
				array[i] = Character.toUpperCase( array[i] );
				capitalizeNextChar = false;
			}
		}
		return new String( array );
	}

	public void startRow( )
	{
		writer.openTag( "Row" );
	}

	public void endRow( )
	{
		writer.closeTag( "Row" );
	}

	public void startCell( int cellindex, int colspan, int rowspan,
			int styleid, HyperlinkDef hyperLink )
	{
		writer.openTag( "Cell" );
		writer.attribute( "ss:Index", cellindex );
		writer.attribute( "ss:StyleID", styleid );

		if ( hyperLink != null )
		{
			String urlAddress = hyperLink.getUrl( );
			if ( hyperLink.getType( ) == IHyperlinkAction.ACTION_BOOKMARK )
			{

				urlAddress = "#Sheet1!" + urlAddress;

			}
			if ( urlAddress.length( ) >= 255 )
			{
				logger.log( Level.WARNING, "The WRL: {" + urlAddress + "} is too long!" );
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

	protected void writeTxtData( Data d )
	{
		startCell( d.span.getCol( ), d.span.getColSpan( ), d.getRowSpan( ),
				d.styleId, d.url );
		writeText( d );
		endCell( );
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

	public void endCell( )
	{
		writer.closeTag( "Cell" );
	}

	public void writeAlignment( String horizontal, String vertical )
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

		writer.attribute( "ss:WrapText", "1" );
		writer.closeTag( "Alignment" );
	}

	public void writeBorder( String position, String lineStyle, String weight,
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

	public void writeFont( String fontName, String size, String bold,
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

	public void writeBackGroudColor( String bgColor )
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
		writer.openTag( "Style" );
		writer.attribute( "ss:ID", id );

		if ( id >= StyleEngine.RESERVE_STYLE_ID )
		{
			String horizontalAlign = style
					.getProperty( StyleConstant.H_ALIGN_PROP );
			String verticalAlign = style
					.getProperty( StyleConstant.V_ALIGN_PROP );
			writeAlignment( horizontalAlign, verticalAlign );

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

	public void writeDataFormat( StyleEntry style )
	{
		if ( style.getProperty( StyleConstant.DATA_TYPE_PROP ) == Data.DATE
				&& style.getProperty( StyleConstant.DATE_FORMAT_PROP ) != null )
		{
			writer.openTag( "NumberFormat" );
			writer.attribute( "ss:Format", style
					.getProperty( StyleConstant.DATE_FORMAT_PROP ) );
			writer.closeTag( "NumberFormat" );

		}

		if ( style.getProperty( StyleConstant.DATA_TYPE_PROP ) == Data.NUMBER
				&& style.getProperty( StyleConstant.NUMBER_FORMAT_PROP ) != null )
		{
			writer.openTag( "NumberFormat" );

			String numberStyle = style
					.getProperty( StyleConstant.NUMBER_FORMAT_PROP );
			numberStyle = format( numberStyle );
			writer.attribute( "ss:Format", numberStyle );
			writer.closeTag( "NumberFormat" );
		}
	}

	// here the user input can be divided into two cases :
	// the case in the birt input like G and the Currency
	// the case in excel format : like 0.00E00
	private String format( String givenValue )
	{
		String returnStr = "\\";
		if ( givenValue.length( ) == 1 )
		{
			char ch = givenValue.charAt( 0 );
			if ( ch == 'G' || ch == 'g' || ch == 'd' || ch == 'D' )
			{
				returnStr = givenValue + "###";
			}
			if ( ch == 'C' || ch == 'c' )
			{
				return "�###,##0.00";
			}
			if ( ch == 'f' || ch == 'F' )
			{
				return "#0.00";
			}
			if ( ch == 'N' || ch == 'n' )
			{
				return "###,##0.00";
			}
			if ( ch == 'p' || ch == 'P' )
			{
				return "###,##0.00 %";
			}
			if ( ch == 'e' || ch == 'E' )
			{
				return "0.000000E00";
			}
			if ( ch == 'x' || ch == 'X' )
			{
				returnStr = "####";
			}
			returnStr = returnStr + givenValue + "###";
		}
		else
		{
			if ( givenValue.equals( "Fixed" ) || givenValue.equals( "#0.00" ) )
				return "#0.00";
			if ( givenValue.equals( "Percent" ) || givenValue.equals( "0.00%" ) )
				return "0.00%";
			if ( givenValue.equals( "Scientific" )
					|| givenValue.equals( "0.00E00" ) )
				return "0.00E00";
			if ( givenValue.equals( "Standard" )
					|| givenValue.equals( "###,##0.00" ) )
				return "###,##0.00";
			if(givenValue.equals( "General Number" )){
				return "General";
			}
			
			if(validType(givenValue)){
				return givenValue + "###";
			}
			int count = givenValue.length( );
			for ( int num = 0; num < count - 1; num++ )
			{
				returnStr = returnStr + givenValue.charAt( num ) + "\\";
			}
			returnStr = returnStr + givenValue.charAt( count - 1 ) + "###";
		}
		return returnStr;
	}

	private boolean validType(String str){
		for(int count = 0 ; count < str.length( ) ; count ++){
			char ch = str.charAt( count );
			if(ch != '$' && ch != '0' && ch != '#' && ch != '?' && ch != '@' && ch != '%'
				&& ch != '.' && ch != ';' && ch != ' ' && ch!= ',' && ch != '+' && ch!= '/'
					&& ch != '_' && ch!= '*' && ch != '(' && ch != ')' && ch != '[' && ch != ']'
						&& ch != '"'){
				return false;
			}
		}
		return true;
	}
	
	public void writeDeclarations( )
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

	public void declareStyles( Map style2id )
	{
		writer.openTag( "Styles" );

		for ( Iterator it = style2id.entrySet( ).iterator( ); it.hasNext( ); )
		{
			Map.Entry entry = (Map.Entry) it.next( );

			Object style = entry.getKey( );
			int id = ( (Integer) entry.getValue( ) ).intValue( );
			declareStyle( (StyleEntry) style, id );
		}

		writer.closeTag( "Styles" );
	}

	public void defineNames( List namesRefer )
	{
		writer.openTag( "Names" );
		for ( Iterator it = namesRefer.iterator( ); it.hasNext( ); )
		{
			BookmarkDef bookmark = (BookmarkDef) it.next( );

			String name = bookmark.getName( );
			String refer = bookmark.getRefer( );
			defineName( name, refer );
		}
		writer.closeTag( "Names" );
	}

	private void defineName( String name, String refer )
	{
		writer.openTag( "NamedRange" );
		writer.attribute( "ss:Name", name );
		writer.attribute( "ss:RefersTo", refer );
		writer.closeTag( "NamedRange" );
	}

	public void close( boolean complete )
	{
		if ( complete )
		{
			writer.closeTag( "Workbook" );
		}

		writer.close( );
	}

	public void startSheet( String name )
	{
		writer.openTag( "Worksheet" );
		writer.attribute( "ss:Name", name );
	}

	public void startSheet( )
	{
		startSheet( "Sheet1" );
	}

	public void closeSheet( )
	{
		writer.closeTag( "Worksheet" );
	}

	public void startTable( int[] width )
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

	public void insertSheet( File file )
	{
		try
		{
			BufferedReader reader = new BufferedReader( new FileReader( file ) );
			String line = reader.readLine( );

			while ( line != null )
			{
				writer.literal( line );
				line = reader.readLine( );
			}

			reader.close( );

		}
		catch ( IOException e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
		}
	}
}

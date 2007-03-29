
package org.eclipse.birt.report.engine.emitter.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.emitter.XMLWriter;

public class ExcelWriter
{

	private XMLWriterXLS writer = new XMLWriterXLS( );

	private class XMLWriterXLS extends XMLWriter
	{

		public PrintWriter getPrint( )
		{
			return printWriter;
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

	// FIXME
	public void writeText( String txt )
	{
		writer.openTag( "Data" );

		try
		{
			Integer.parseInt( txt );
			writer.attribute( "ss:Type", "Number" );
		}
		catch ( Exception e )
		{
			writer.attribute( "ss:Type", "String" );
		}

		writer.text( txt );
		writer.closeTag( "Data" );
	}

	public void startRow( )
	{
		writer.openTag( "Row" );
	}

	public void endRow( )
	{
		writer.closeTag( "Row" );
	}

	public void startCell( int index, int colSpan, int rowSpan, int id,
			String hyperLink )
	{
		writer.openTag( "Cell" );
		writer.attribute( "ss:Index", index );
		writer.attribute( "ss:StyleID", id );
		if ( hyperLink != null )
		{
			writer.attribute( "ss:HRef", hyperLink );
		}

		writer.attribute( "ss:MergeAcross", colSpan );
		writer.attribute( "ss:MergeDown", rowSpan );
	}
	
	public void writeDefaultCell(String content) {
		writer.openTag("Cell");
		writeText(content);
		writer.closeTag( "Cell" );
	}

	protected void writeTxtData( Data d )
	{
		startCell( d.span.getCol( ), d.span.getColSpan( ), 0, d.styleId, d.url );
		writeText( d.txt );
		endCell( );
	}

	protected void writeFormulaData( Data d )
	{
		writer.openTag( "Cell" );
		writer.attribute( "ss:Index", d.span.getCol( ) );
		writer.attribute( "ss:Formula", d.txt );
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
		writer.attribute( "ss:Horizontal", horizontal );
		writer.attribute( "ss:Vertical", vertical );
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

		writer.attribute( "ss:Size", size );
		writer.attribute( "ss:Bold", bold );
		writer.attribute( "ss:Italic", italic );
		writer.attribute( "ss:StrikeThrough", strikeThrough );
		
		if(!"0".equalsIgnoreCase( underline )) {
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
		return StyleConstant.NULL != value;

	}

	private void declareStyle( StyleEntry style, int id )
	{
		writer.openTag( "Style" );
		writer.attribute( "ss:ID", id );

		String horizontalAlign = style.getProperty( StyleConstant.H_ALIGN_PROP );
		String verticalAlign = style.getProperty( StyleConstant.V_ALIGN_PROP );
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

		String fontName = style.getProperty( StyleConstant.FONT_FAMILY_PROP );
		String size = style.getProperty( StyleConstant.FONT_SIZE_PROP );
		String fontStyle = style.getProperty( StyleConstant.FONT_STYLE_PROP );
		String fontWeight = style.getProperty( StyleConstant.FONT_WEIGHT_PROP );
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

		writer.closeTag( "Style" );
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

	public void insert( File file )
	{
		try
		{
			BufferedReader reader = new BufferedReader( new FileReader( file ) );
			String line = reader.readLine( );
			
			while(line != null)			
			{
				writer.literal( line );
				line = reader.readLine();
			}			
			
			reader.close( );
			
		}
		catch ( IOException e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
		}
	}
}

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

package org.eclipse.birt.report.engine.emitter.wpml.writer;

import java.text.Bidi;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.emitter.XMLWriter;
import org.eclipse.birt.report.engine.emitter.wpml.DiagonalLineInfo;
import org.eclipse.birt.report.engine.emitter.wpml.HyperlinkInfo;
import org.eclipse.birt.report.engine.emitter.wpml.SpanInfo;
import org.eclipse.birt.report.engine.emitter.wpml.WordUtil;
import org.eclipse.birt.report.engine.emitter.wpml.AbstractEmitterImpl.TextFlag;
import org.eclipse.birt.report.engine.emitter.wpml.DiagonalLineInfo.Line;
import org.w3c.dom.css.CSSValue;

public abstract class AbstractWordXmlWriter
{

	protected XMLWriter writer;

	protected final String RIGHT = "right";

	protected final String LEFT = "left";

	protected final String TOP = "top";

	protected final String BOTTOM = "bottom";

	protected int imageId = 75;

	protected int bookmarkId = 0;

	private int lineId = 0;

	// Holds the global layout orientation.
	protected boolean rtl = false;

	// Holds Bidi text information
	protected String[] bidiChunks = new String[0];

	protected int[] bidiLevels = new int[0];

	protected abstract void writeTableLayout( );

	protected abstract void writeFontSize( IStyle style );

	protected abstract void writeFont( String fontFamily );
	
	protected abstract void writeFontStyle( IStyle style );
	
	protected abstract void writeFontWeight(IStyle style);

	protected abstract void openHyperlink( HyperlinkInfo info );

	protected abstract void closeHyperlink( HyperlinkInfo info );

	protected abstract void writeVmerge( SpanInfo spanInfo );

	public void startSectionInParagraph( )
	{
		writer.openTag( "w:p" );
		writer.openTag( "w:pPr" );
		startSection( );
	}

	public void endSectionInParagraph( )
	{
		endSection( );
		writer.closeTag( "w:pPr" );
		writer.closeTag( "w:p" );
	}

	public void startSection( )
	{
		writer.openTag( "w:sectPr" );
	}

	public void endSection( )
	{
		writer.closeTag( "w:sectPr" );
	}

	protected void drawImageShapeType( int imageId )
	{
		writer.openTag( "v:shapetype" );
		writer.attribute( "id", "_x0000_t" + imageId );
		writer.attribute( "coordsize", "21600,21600" );
		writer.attribute( "o:spt", "75" );
		writer.attribute( "o:preferrelative", "t" );
		writer.attribute( "path", "m@4@5l@4@11@9@11@9@5xe" );
		writer.attribute( "filled", "f" );
		writer.attribute( "stroked", "f" );
		writer.openTag( "v:stroke" );
		writer.attribute( "imagealignshape", "false" );
		writer.attribute( "joinstyle", "miter" );
		writer.closeTag( "v:stroke" );
		writer.openTag( "v:formulas" );
		writer.openTag( "v:f" );
		writer.attribute( "eqn", "if lineDrawn pixelLineWidth 0" );
		writer.closeTag( "v:f" );
		writer.openTag( "v:f" );
		writer.attribute( "eqn", "sum @0 1 0" );
		writer.closeTag( "v:f" );
		writer.openTag( "v:f" );
		writer.attribute( "eqn", "sum 0 0 @1" );
		writer.closeTag( "v:f" );
		writer.openTag( "v:f" );
		writer.attribute( "eqn", "prod @2 1 2" );
		writer.closeTag( "v:f" );
		writer.openTag( "v:f" );
		writer.attribute( "eqn", "prod @3 21600 pixelWidth" );
		writer.closeTag( "v:f" );
		writer.openTag( "v:f" );
		writer.attribute( "eqn", "prod @3 21600 pixelHeight" );
		writer.closeTag( "v:f" );
		writer.openTag( "v:f" );
		writer.attribute( "eqn", "sum @0 0 1" );
		writer.closeTag( "v:f" );
		writer.openTag( "v:f" );
		writer.attribute( "eqn", "prod @6 1 2" );
		writer.closeTag( "v:f" );
		writer.openTag( "v:f" );
		writer.attribute( "eqn", "prod @7 21600 pixelWidth" );
		writer.closeTag( "v:f" );
		writer.openTag( "v:f" );
		writer.attribute( "eqn", "sum @8 21600 0 " );
		writer.closeTag( "v:f" );
		writer.openTag( "v:f" );
		writer.attribute( "eqn", "prod @7 21600 pixelHeight" );
		writer.closeTag( "v:f" );
		writer.openTag( "v:f" );
		writer.attribute( "eqn", "sum @10 21600 0" );
		writer.closeTag( "v:f" );
		writer.closeTag( "v:formulas" );
		writer.openTag( "v:path" );
		writer.attribute( "o:extrusionok", "f" );
		writer.attribute( "gradientshapeok", "t" );
		writer.attribute( "o:connecttype", "rect" );
		writer.closeTag( "v:path" );
		writer.openTag( "o:lock" );
		writer.attribute( "v:ext", "edit" );
		writer.attribute( "aspectratio", "t" );
		writer.closeTag( "o:lock" );
		writer.closeTag( "v:shapetype" );
	}

	protected void drawImageBordersStyle( IStyle style )
	{
		drawImageBorderStyle( BOTTOM, style.getBorderBottomStyle( ), style
				.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ) );
		drawImageBorderStyle( TOP, style.getBorderTopStyle( ), style
				.getProperty( StyleConstants.STYLE_BORDER_TOP_WIDTH ) );
		drawImageBorderStyle( LEFT, style.getBorderLeftStyle( ), style
				.getProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH ) );
		drawImageBorderStyle( RIGHT, style.getBorderRightStyle( ), style
				.getProperty( StyleConstants.STYLE_BORDER_RIGHT_WIDTH ) );
	}

	private void drawImageBorderStyle( String pos, String style, CSSValue width )
	{
		String direct = "w10:border" + pos;
		writer.openTag( direct );
		writer.attribute( "type", WordUtil.parseImageBorderStyle( style ) );
		writer.attribute( "width", WordUtil
				.parseBorderSize( ( (FloatValue) width ).getFloatValue( ) ) );
		writer.closeTag( direct );
	}

	protected void drawImageBordersColor( IStyle style )
	{
		drawImageBorderColor( BOTTOM, style.getBorderBottomColor( ) );
		drawImageBorderColor( TOP, style.getBorderTopColor( ) );
		drawImageBorderColor( LEFT, style.getBorderLeftColor( ) );
		drawImageBorderColor( RIGHT, style.getBorderRightColor( ) );
	}

	private void drawImageBorderColor( String pos, String color )
	{
		String borderColor = "#" + WordUtil.parseColor( color );
		String direct = "o:border" + pos + "color";
		writer.attribute( direct, borderColor );
	}

	public void writePageProperties( int pageHeight, int pageWidth,
			int headerHeight, int footerHeight, int topMargin,
			int bottomMargin, int leftMargin, int rightMargin, String orient )
	{
		writer.openTag( "w:pgSz" );
		writer.attribute( "w:w", pageWidth );
		writer.attribute( "w:h", pageHeight );
		writer.attribute( "w:orient", orient );
		writer.closeTag( "w:pgSz" );

		writer.openTag( "w:pgMar" );
		writer.attribute( "w:top", topMargin );
		writer.attribute( "w:bottom", bottomMargin );
		writer.attribute( "w:left", leftMargin );
		writer.attribute( "w:right", rightMargin );
		writer.attribute( "w:header", topMargin );
		writer.attribute( "w:footer", bottomMargin );
		writer.closeTag( "w:pgMar" );
	}

	// write the table properties to the output stream
	public void startTable( IStyle style, int tablewidth )
	{
		writer.openTag( "w:tbl" );
		writer.openTag( "w:tblPr" );
		writeTableIndent( );
		writeAttrTag( "w:tblStyle", "TableGrid" );
		writeAttrTag( "w:tblOverlap", "Never" );
		writeBidiTable( );
		writeTableWidth( tablewidth );
		writeAttrTag( "w:tblLook", "01E0" );
		writeTableLayout( );
		writeTableBorders( style );
		writeBackgroundColor( style.getBackgroundColor( ) );
		writeAlign( style.getTextAlign( ), style.getDirection( ) );
		writer.closeTag( "w:tblPr" );
	}

	private void writeTableBorders( IStyle style )
	{
		writer.openTag( "w:tblBorders" );
		writeBorders( style, 0, 0, 0, 0 );
		writer.closeTag( "w:tblBorders" );
	}

	public void endTable( )
	{
		writer.closeTag( "w:tbl" );
	}

	private void writeTableWidth( int tablewidth )
	{
		writer.openTag( "w:tblW" );
		writer.attribute( "w:w", tablewidth );
		writer.attribute( "w:type", "dxa" );
		writer.closeTag( "w:tblW" );
	}

	private void writeTableIndent( )
	{
		writer.openTag( "w:tblInd" );
		writer.attribute( "w:w", 0 );
		writer.attribute( "w:type", "dxa" );
		writer.closeTag( "w:tblInd" );
	}

	protected void writeBorders( IStyle style, int bottomMargin, int topMargin,
			int leftMargin, int rightMargin )
	{
		String borderStyle = style.getBorderBottomStyle( );
		if ( hasBorder( borderStyle ) )
		{
			writeSingleBorder( BOTTOM, borderStyle, style
					.getBorderBottomColor( ), style
					.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ),
					bottomMargin );
		}

		borderStyle = style.getBorderTopStyle( );
		if ( hasBorder( borderStyle ) )
		{
			writeSingleBorder( TOP, borderStyle, style.getBorderTopColor( ),
					style.getProperty( StyleConstants.STYLE_BORDER_TOP_WIDTH ),
					topMargin );
		}

		borderStyle = style.getBorderLeftStyle( );
		if ( hasBorder( borderStyle ) )
		{
			writeSingleBorder( LEFT, borderStyle, style.getBorderLeftColor( ),
					style.getProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH ),
					leftMargin );
		}

		borderStyle = style.getBorderRightStyle( );
		if ( hasBorder( borderStyle ) )
		{
			writeSingleBorder( RIGHT, borderStyle,
					style.getBorderRightColor( ),
					style.getProperty( StyleConstants.STYLE_BORDER_RIGHT_WIDTH ),
					rightMargin );
		}
	}

	private void writeSingleBorder( String type, String borderStyle,
			String color, CSSValue width, int margin )
	{
		writer.openTag( "w:" + type );
		writeBorderProperty( borderStyle, color, width, margin );
		writer.closeTag( "w:" + type );
	}

	private void writeBorderProperty( String style, String color,
			CSSValue width, int margin )
	{
		writer.attribute( "w:val", WordUtil.parseBorderStyle( style ) );
		writer.attribute( "w:sz", WordUtil
				.parseBorderSize( ( (FloatValue) width ).getFloatValue( ) ) );
		writer.attribute( "w:space", validateBorderSpace( margin ) );
		writer.attribute( "w:color", WordUtil.parseColor( color ) );
	}

	private int validateBorderSpace( int margin )
	{
		// word only accept 0-31 pt
		int space = (int) WordUtil.twipToPt( margin );
		if ( space > 31 )
			space = 31;
		return space;
	}

	protected void writeAlign( String align, String direction )
	{
		if ( null == align )
		{
			return;
		}
		String textAlign = align;
		if ( "justify".equalsIgnoreCase( align ) )
		{
			textAlign = "both";
		}

		// Need to swap 'left' and 'right' when orientation is RTL.
		if ( CSSConstants.CSS_RTL_VALUE.equalsIgnoreCase( direction ) )
		{
			if ( IStyle.CSS_RIGHT_VALUE.equals( textAlign ) )
				writeAttrTag( "w:jc", IStyle.CSS_LEFT_VALUE );
			else if ( IStyle.CSS_LEFT_VALUE.equals( textAlign ) )
				writeAttrTag( "w:jc", IStyle.CSS_RIGHT_VALUE );
			else
				writeAttrTag( "w:jc", textAlign );
		}
		else
			writeAttrTag( "w:jc", textAlign );
	}

	protected void writeBackgroundColor( String color )
	{
		String cssColor = WordUtil.parseColor( color );
		if ( cssColor == null )
		{
			return;
		}
		writer.openTag( "w:shd" );
		writer.attribute( "w:val", "clear" );
		writer.attribute( "w:color", "auto" );
		writer.attribute( "w:fill", cssColor );
		writer.closeTag( "w:shd" );
	}

	/**
	 * @param direction
	 * 
	 * @author bidi_hcg
	 */
	private void writeBidiTable( )
	{
		if ( this.rtl )
		{
			writer.openTag( "w:bidiVisual" );
			writer.closeTag( "w:bidiVisual" );
		}
	}

	protected void writeRunBorders( IStyle style )
	{
		String borderStyle = style.getBorderTopStyle( );
		if ( hasBorder( borderStyle ) )
		{
			writeRunBorder( borderStyle, style.getBorderTopColor( ), style
					.getProperty( StyleConstants.STYLE_BORDER_TOP_WIDTH ) );
			return;
		}

		borderStyle = style.getBorderBottomStyle( );
		if ( hasBorder( borderStyle ) )
		{
			writeRunBorder( borderStyle, style.getBorderBottomColor( ), style
					.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ) );
			return;
		}

		borderStyle = style.getBorderLeftStyle( );
		if ( hasBorder( borderStyle ) )
		{
			writeRunBorder( borderStyle, style.getBorderLeftColor( ), style
					.getProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH ) );
			return;
		}

		borderStyle = style.getBorderRightStyle( );
		if ( hasBorder( borderStyle ) )
		{
			writeRunBorder( borderStyle, style.getBorderRightColor( ), style
					.getProperty( StyleConstants.STYLE_BORDER_RIGHT_WIDTH ) );
			return;
		}
	}

	private boolean hasBorder( String borderStyle )
	{
		return !( borderStyle == null || "none".equalsIgnoreCase( borderStyle ) );
	}

	private void writeRunBorder( String borderStyle, String color,
			CSSValue borderWidth )
	{
		writer.openTag( "w:bdr" );
		writeBorderProperty( borderStyle, color, borderWidth, 0 );
		writer.closeTag( "w:bdr" );
	}

	private boolean needNewParagraph( String txt )
	{
		return ( "\n".equals( txt ) || "\r".equalsIgnoreCase( txt ) || "\r\n"
				.equals( txt ) );
	}

	public void startParagraph( IStyle style, boolean isInline )
	{
		writer.openTag( "w:p" );
		writer.openTag( "w:pPr" );
		writeSpacing( ( style.getProperty( StyleConstants.STYLE_MARGIN_TOP ) ),
				( style.getProperty( StyleConstants.STYLE_MARGIN_BOTTOM ) ) );
		writeAlign( style.getTextAlign( ), style.getDirection( ) );
		if ( !isInline )
		{
			writeBackgroundColor( style.getBackgroundColor( ) );
			writeParagraphBorders( style );
		}
		writer.closeTag( "w:pPr" );
	}

	private void writeSpacing( CSSValue height )
	{
		// unit: twentieths of a point(twips)
		float spacingValue = ( (FloatValue) height ).getFloatValue( );
		int spacing = WordUtil.parseSpacing( spacingValue ) / 2;
		writeSpacing( spacing, spacing );
	}

	private void writeSpacing( CSSValue top, CSSValue bottom )
	{
		float topSpacingValue = ( (FloatValue) top ).getFloatValue( );
		float bottomSpacingValue = ( (FloatValue) bottom ).getFloatValue( );
		writeSpacing( WordUtil.parseSpacing( topSpacingValue ) / 2, WordUtil
				.parseSpacing( bottomSpacingValue ) / 2 );
	}

	private void writeSpacing( int beforeValue, int afterValue )
	{
		writer.openTag( "w:spacing" );
		writer.attribute( "w:before", beforeValue );
		writer.attribute( "w:after", afterValue );
		writer.closeTag( "w:spacing" );
	}

	protected void writeAutoText( int type )
	{
		writer.openTag( "w:instrText" );
		if ( type == IAutoTextContent.PAGE_NUMBER )
		{
			writer.text( "PAGE" );
		}
		else if ( type == IAutoTextContent.TOTAL_PAGE )
		{
			writer.text( "NUMPAGES" );
		}
		writer.closeTag( "w:instrText" );
	}

	private void writeString( String txt, IStyle style )
	{
		if ( txt == null )
		{
			return;
		}
		if ( style != null )
		{
			String textTransform = style.getTextTransform( );
			if ( CSSConstants.CSS_CAPITALIZE_VALUE
					.equalsIgnoreCase( textTransform ) )
			{
				txt = WordUtil.capitalize( txt );
			}
			else if ( CSSConstants.CSS_UPPERCASE_VALUE
					.equalsIgnoreCase( textTransform ) )
			{
				txt = txt.toUpperCase( );
			}
			else if ( CSSConstants.CSS_LOWERCASE_VALUE
					.equalsIgnoreCase( textTransform ) )
			{
				txt = txt.toLowerCase( );
			}
		}

		writer.openTag( "w:t" );
		boolean notFirst = false;

		for ( String st : txt.split( "\n" ) )
		{
			String row = "<![CDATA[" + st + "]]>";
			if ( notFirst )
			{
				row = "<w:br/>" + row;
			}
			else
			{
				notFirst = true;
			}
			writer.text( row, true, false );
		}
		writer.closeTag( "w:t" );
	}

	private void writeLetterSpacing( IStyle style )
	{
		CSSValue letterSpacing = style
				.getProperty( StyleConstants.STYLE_LETTER_SPACING );
		writeAttrTag( "w:spacing", WordUtil
				.parseSpacing( ( (FloatValue) letterSpacing ).getFloatValue( ) ) );
	}

	private void writeHyperlinkStyle( boolean isHyperlink, IStyle style )
	{
		// deal with hyperlink
		if ( isHyperlink )
		{
			writeAttrTag( "w:rStyle", "Hyperlink" );
		}
		else
		{
			writeTextUnderline( style );
			writeTextColor( style );
		}
	}

	protected void writeTocText( String tocText, int level )
	{
		writer.openTag( "w:r" );
		writer.openTag( "w:instrText" );
		writer.text( " TC \"" + tocText + "\"" + " \\f C \\l \""
				+ String.valueOf( level ) + "\"" );
		writer.closeTag( "w:instrText" );
		writer.closeTag( "w:r" );
	}

	/**
	 * @param direction
	 * 
	 * @author bidi_hcg
	 */
	protected void writeBidi( boolean rtl )
	{
		if ( rtl )
		{
			writer.openTag( "w:bidi" );
			writer.closeTag( "w:bidi" );
		}
		else
		{
			writeAttrTag( "w:bidi", "off" );
		}
	}

	private boolean isAutoText( int type )
	{
		return type == IAutoTextContent.PAGE_NUMBER
				|| type == IAutoTextContent.TOTAL_PAGE;
	}

	protected void writeField( boolean isStart )
	{
		String fldCharType = isStart ? "begin" : "end";
		writer.openTag( "w:r" );
		writer.openTag( "w:fldChar" );
		writer.attribute( "w:fldCharType", fldCharType );
		writer.closeTag( "w:fldChar" );
		writer.closeTag( "w:r" );
	}

	public void writeColumn( int[] cols )
	{
		// unit: twips
		writer.openTag( "w:tblGrid" );

		for ( int i = 0; i < cols.length; i++ )
		{
			writeAttrTag( "w:gridCol", cols[i] );
		}
		writer.closeTag( "w:tblGrid" );
	}

	/**
	 * 
	 * @param style
	 *            style of the row
	 * @param height
	 *            height of current row, if heigh equals 1 then ignore height
	 * @param type
	 *            header or normal
	 */

	public void startTableRow( double height, boolean isHeader,
			boolean repeatHeader )
	{
		writer.openTag( "w:tr" );

		// write the row height, unit: twips
		writer.openTag( "w:trPr" );

		if ( height != -1 )
		{
			writeAttrTag( "w:trHeight", height );
		}

		// if value is "off",the header will be not repeated
		if ( isHeader )
		{
			String headerOnOff = repeatHeader ? "on" : "off";
			writeAttrTag( "w:tblHeader", headerOnOff );
		}
		writer.closeTag( "w:trPr" );
	}

	public void endTableRow( )
	{
		writer.closeTag( "w:tr" );
	}

	public void startTableCell( int width, IStyle style, SpanInfo spanInfo )
	{
		writer.openTag( "w:tc" );
		writer.openTag( "w:tcPr" );
		writeCellWidth( width );
		if ( spanInfo != null )
		{
			writeGridSpan( spanInfo );
			writeVmerge( spanInfo );
		}
		writeCellProperties( style );
		writer.closeTag( "w:tcPr" );

		String align = style.getTextAlign( );
		if ( align == null )
		{
			return;
		}
		String direction = style.getDirection( ); // bidi_hcg
		if ( CSSConstants.CSS_LEFT_VALUE.equals( align ) )
		{
			if ( !CSSConstants.CSS_RTL_VALUE.equals( direction ) )
				return;
		}
		writer.openTag( "w:pPr" );
		writeAlign( align, direction );
		writer.closeTag( "w:pPr" );
	}

	private void writeCellWidth( int width )
	{
		writer.openTag( "w:tcW" );
		writer.attribute( "w:w", width );
		writer.attribute( "w:type", "dxa" );
		writer.closeTag( "w:tcW" );
	}

	private void writeGridSpan( SpanInfo spanInfo )
	{
		int columnSpan = spanInfo.getColumnSpan( );
		if ( columnSpan > 1 )
		{
			writeAttrTag( "w:gridSpan", columnSpan );
		}
	}

	public void writeSpanCell( SpanInfo info )
	{
		writer.openTag( "w:tc" );
		writer.openTag( "w:tcPr" );
		writeCellWidth( info.getCellWidth( ) );
		writeGridSpan( info );
		writeVmerge( info );
		writeCellProperties( info.getStyle( ) );
		writer.closeTag( "w:tcPr" );
		insertHiddenParagraph( );
		writer.closeTag( "w:tc" );
	}

	public void endTableCell( boolean empty )
	{
		if ( empty )
		{
			insertHiddenParagraph( );
		}

		writer.closeTag( "w:tc" );
	}

	public void writeEmptyCell( )
	{
		writer.openTag( "w:tc" );
		writer.openTag( "w:tcPr" );
		writer.openTag( "w:tcW" );
		writer.attribute( "w:w", 0 );
		writer.attribute( "w:type", "dxa" );
		writer.closeTag( "w:tcW" );
		writer.closeTag( "w:tcPr" );
		insertHiddenParagraph( );
		writer.closeTag( "w:tc" );
	}

	public void insertHiddenParagraph( )
	{
		writer.openTag( "w:p" );
		writeHiddenProperty( );
		writer.closeTag( "w:p" );
	}

	public void writeHiddenProperty( )
	{
		writer.openTag( "w:rPr" );
		writeAttrTag( "w:vanish", "on" );
		writer.closeTag( "w:rPr" );
	}

	public void endParagraph( )
	{
		writer.closeTag( "w:p" );
	}

	public void writeCaption( String txt )
	{
		writer.openTag( "w:p" );
		writer.openTag( "w:pPr" );
		writeAlign( "center", null );
		writer.closeTag( "w:pPr" );
		writer.openTag( "w:r" );
		writer.openTag( "w:rPr" );
		writeString( txt, null );
		writer.closeTag( "w:rPr" );
		writer.closeTag( "w:r" );
		writer.closeTag( "w:p" );
	}

	/**
	 * If the cell properties is not set, then check the row properties and
	 * write those properties.
	 * 
	 * @param style
	 *            this cell style
	 */
	private void writeCellProperties( IStyle style )
	{
		// A cell background color may inherit from row background,
		// so we should get the row background color here,
		// if the cell background is transparent
		if ( style == null )
		{
			return;
		}
		writeBackgroundColor( style.getBackgroundColor( ) );
		writeCellBorders( style );
		String verticalAlign = style.getVerticalAlign( );
		if ( verticalAlign != null )
		{
			writeAttrTag( "w:vAlign", WordUtil
					.parseVerticalAlign( verticalAlign ) );
		}
		String noWrap = CSSConstants.CSS_NOWRAP_VALUE.equalsIgnoreCase( style
				.getWhiteSpace( ) ) ? "on" : "off";
		writeAttrTag( "w:noWrap", noWrap );
	}

	private void writeCellBorders( IStyle style )
	{
		writer.openTag( "w:tcBorders" );
		writeBorders( style, 0, 0, 0, 0 );
		writer.closeTag( "w:tcBorders" );
	}

	/**
	 * @param text
	 * @param rtl
	 * @return
	 * 
	 * @author bidi_hcg
	 */
	protected void buildBidiChunks( String text, boolean rtl )
	{
		Bidi bidiObj = new Bidi( text, rtl
				? Bidi.DIRECTION_RIGHT_TO_LEFT
				: Bidi.DIRECTION_LEFT_TO_RIGHT );
		int nRuns = bidiObj.getRunCount( );
		if ( nRuns != bidiChunks.length )
		{
			bidiChunks = new String[nRuns];
			bidiLevels = new int[nRuns];
		}
		for ( int i = 0; i < nRuns; i++ )
		{
			bidiChunks[i] = text.substring( bidiObj.getRunStart( i ), bidiObj
					.getRunLimit( i ) );
			bidiLevels[i] = bidiObj.getRunLevel( i ) & 1;
		}
	}

	protected void writeAttrTag( String name, String val )
	{
		writer.openTag( name );
		writer.attribute( "w:val", val );
		writer.closeTag( name );
	}

	protected void writeAttrTag( String name, int val )
	{
		writer.openTag( name );
		writer.attribute( "w:val", val );
		writer.closeTag( name );
	}

	protected void writeAttrTag( String name, double val )
	{
		writer.openTag( name );
		writer.attribute( "w:val", val );
		writer.closeTag( name );
	}

	protected int getImageID( )
	{
		return imageId++;
	}

	private void writeTextInParagraph( int type, String txt, IStyle style,
			String fontFamily, HyperlinkInfo info )
	{
		writer.openTag( "w:p" );
		writer.openTag( "w:pPr" );

		CSSValue lineHeight = style
				.getProperty( StyleConstants.STYLE_LINE_HEIGHT );
		if ( !"normal".equalsIgnoreCase( lineHeight.getCssText( ) ) )
		{
			writeSpacing( lineHeight );
		}

		writeAlign( style.getTextAlign( ), style.getDirection( ) );
		writeBackgroundColor( style.getBackgroundColor( ) );
		writeParagraphBorders( style );
		writer.closeTag( "w:pPr" );
		writeTextInRun( type, txt, style, fontFamily, info, false );
	}

	private void writeParagraphBorders( IStyle style )
	{
		writer.openTag( "w:pBdr" );
		writeBorders( style, 0, 0, 0, 0 );
		writer.closeTag( "w:pBdr" );
	}

	public void writeText( int type, String txt, IStyle style,
			String fontFamily, HyperlinkInfo info, TextFlag flag )
	{
		if ( flag == TextFlag.START )
		{
			writeTextInParagraph( type, txt, style, fontFamily, info );
		}
		else if ( flag == TextFlag.END )
		{
			writer.closeTag( "w:p" );
		}
		else if ( flag == TextFlag.MIDDLE )
		{
			writeTextInRun( type, txt, style, fontFamily, info, false );
		}
		else
		{
			writeTextInParagraph( type, txt, style, fontFamily, info );
			writer.closeTag( "w:p" );
		}
	}

	public void writeTextInRun( int type, String txt, IStyle style,
			String fontFamily, HyperlinkInfo info, boolean isInline )
	{
		if ( "".equals( txt ) )
		{
			return;
		}
		if ( needNewParagraph( txt ) )
		{
			writer.closeTag( "w:p" );
			startParagraph( style, isInline );
			return;
		}

		openHyperlink( info );
		boolean isAutoText = isAutoText( type );
		String direction = style.getDirection( );
		boolean textIsRtl = CSSConstants.CSS_RTL_VALUE.equals( direction );
		int nChunks = isAutoText ? 1 : 0;

		writer.openTag( "w:pPr" );
		writeBidi( textIsRtl );
		writer.closeTag( "w:pPr" );

		if ( isAutoText )
		{
			writeField( true );
		}
		else
		{
			buildBidiChunks( txt, textIsRtl );
			nChunks = bidiChunks.length;
			assert nChunks > 0;
		}

		for ( int i = 0; i < nChunks; i++ )
		{
			writer.openTag( "w:r" );
			writer.openTag( "w:rPr" );
			writeRunProperties( style, fontFamily, info != null );
			if ( isInline )
			{
				writeAlign( style.getTextAlign( ), direction );
				writeBackgroundColor( style.getBackgroundColor( ) );
				writeRunBorders( style );
			}
			if ( !isAutoText && bidiLevels[i] == Bidi.DIRECTION_RIGHT_TO_LEFT )
			{
				writer.openTag( "w:rtl" );
				writer.closeTag( "w:rtl" );
			}
			writer.closeTag( "w:rPr" );

			if ( isAutoText )
			{
				writeAutoText( type );
			}
			else
			{
				writeString( bidiChunks[i], style );
			}
			writer.closeTag( "w:r" );
		}
		if ( isAutoText )
		{
			writeField( false );
		}
		closeHyperlink( info );
	}

	protected void writeRunProperties( IStyle style, String fontFamily,
			boolean ishyperlink )
	{
		writeHyperlinkStyle( ishyperlink, style );
		writeFont( fontFamily );
		writeFontSize( style );
		writeLetterSpacing( style );
		writeTextLineThrough( style );
		writeFontStyle( style );
		writeFontWeight( style );
		writer.openTag( "w:cs" );
		writer.closeTag( "w:cs" );
	}

	private void writeTextColor( IStyle style )
	{
		String val = WordUtil.parseColor( style.getColor( ) );
		if ( val != null )
		{
			writeAttrTag( "w:color", val );
		}
	}

	private void writeTextUnderline( IStyle style )
	{
		String val = WordUtil.removeQuote( style.getTextUnderline( ) );
		if ( !"none".equalsIgnoreCase( val ) )
		{
			writeAttrTag( "w:u", "single" );
		}
	}

	private void writeTextLineThrough( IStyle style )
	{
		String val = WordUtil.removeQuote( style.getTextLineThrough( ) );
		if ( !"none".equalsIgnoreCase( val ) )
		{
			writeAttrTag( "w:strike", "on" );
		}
	}

	protected void startHeaderFooterContainer( int headerHeight, int headerWidth )
	{
		writer.openTag( "w:tbl" );
		writer.openTag( "w:tblPr" );
		writeTableWidth( headerWidth );
		writeAttrTag( "w:tblLook", "01E0" );
		writeTableLayout( );
		writer.closeTag( "w:tblPr" );
		writer.openTag( "w:tr" );
		// write the row height, unit: twips
		writer.openTag( "w:trPr" );
		writeAttrTag( "w:trHeight", headerHeight );
		writer.closeTag( "w:trPr" );
		writer.openTag( "w:tc" );
		writer.openTag( "w:tcPr" );
		writeCellWidth( headerWidth );
		writer.closeTag( "w:tcPr" );
	}

	protected void endHeaderFooterContainer( )
	{
		insertHiddenParagraph( );
		writer.closeTag( "w:tc" );
		writer.closeTag( "w:tr" );
		writer.closeTag( "w:tbl" );
	}

	public void drawDiagonalLine( DiagonalLineInfo diagonalLineInfo )
	{
		if ( diagonalLineInfo.getDiagonalNumber( ) <= 0
				&& diagonalLineInfo.getAntiDiagonalNumber( ) <= 0 )
			return;
		writer.openTag( "w:p" );
		writeHiddenProperty( );
		writer.openTag( "w:r" );
		writer.openTag( "w:pict" );
		double diagonalLineWidth = diagonalLineInfo.getDiagonalLineWidth( );
		String diagonalLineStyle = diagonalLineInfo.getDiagonalStyle( );
		double antidiagonalLineWidth = diagonalLineInfo
				.getAntiDiagonalLineWidth( );
		String antidiagonalLineStyle = diagonalLineInfo.getAntiDiagonalStyle( );
		String lineColor = diagonalLineInfo.getColor( );
		for ( Line line : diagonalLineInfo
				.getDiagonalLine( ) )
		{
			drawLine( diagonalLineWidth, diagonalLineStyle, lineColor,
					line );
		}
		for ( Line antiLine : diagonalLineInfo
				.getAntidiagonalLine( ) )
		{
			drawLine( antidiagonalLineWidth, antidiagonalLineStyle,
					lineColor, antiLine );
		}
		writer.closeTag( "w:pict" );
		writer.closeTag( "w:r" );
		writer.closeTag( "w:p" );
	}

	private void drawLine( double width, String style, String color,
			Line line )
	{
		writer.openTag( "v:line" );
		writer.attribute( "id", "Line" + getLineId( ) );
		writer.attribute( "style",
				"position:absolute;left:0;text-align:left;z-index:1" );
		writer.attribute( "from", line.getXCoordinateFrom( ) + "pt,"
				+ line.getYCoordinateFrom( ) + "pt" );
		writer.attribute( "to", line.getXCoordinateTo( ) + "pt,"
				+ line.getYCoordinateTo( ) + "pt" );
		writer.attribute( "strokeweight", width + "pt" );
		writer.attribute( "strokecolor", "#" + color );
		writer.openTag( "v:stroke" );
		writer.attribute( "dashstyle", WordUtil.parseLineStyle( style ) );
		writer.closeTag( "v:stroke" );
		writer.closeTag( "v:line" );
	}

	private int getLineId( )
	{
		return lineId++;
	}
}

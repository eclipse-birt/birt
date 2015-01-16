
package org.eclipse.birt.report.engine.emitter.pptx;

import java.awt.Color;
import java.util.Iterator;

import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.ppt.util.PPTUtil.HyperlinkDef;
import org.eclipse.birt.report.engine.emitter.pptx.util.PPTXUtil;
import org.eclipse.birt.report.engine.emitter.pptx.writer.Slide;
import org.eclipse.birt.report.engine.layout.emitter.BorderInfo;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.BlockTextArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.TextArea;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;
import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;

import com.lowagie.text.Font;

public class TextWriter
{

	private PPTXRender render;
	private PPTXCanvas canvas;
	private OOXmlWriter writer;
	private boolean needGroup = false;
	private boolean needDrawLineBorder = false;
	private boolean needDrawSquareBorder = false;
	private BorderInfo[] borders = null;

	public TextWriter( PPTXRender render )
	{
		this.render = render;
		this.canvas = render.getCanvas();
		this.writer = canvas.getWriter();
	}

	private static boolean isSingleControl( BlockTextArea text )
	{
		return true;
	}
	
	private static boolean isSquareBorder(BorderInfo[] borders)
	{
		if(borders == null || borders.length != 4)return false;		
		Color color = borders[0].borderColor;
		if(color == null)return false;
		int style = borders[0].borderStyle;
		int type = borders[0].borderType;
		int width = borders[0].borderWidth;
		if(width == 0)return false;
		
		for(int i = 1;i<=3;i++)
		{
			BorderInfo info = borders[i];
			if(!color.equals(info.borderColor))return false;
			if(info.borderStyle != style)return false;
			if(info.borderWidth == 0 || info.borderWidth != width )return false;
		}		
		return true;
	}

	void writeBlockText( int startX,int startY, int width, int height, BlockTextArea text )
	{
		if(!isSingleControl( text ))
		{
			render.visitText( text );
			return;
		}
			
		parseText(text);
		
		startX = PPTXUtil.convertToEnums( startX );
		startY = PPTXUtil.convertToEnums( startY );
		width = PPTXUtil.convertToEnums( width );
		height = PPTXUtil.convertToEnums( height );


		if(needGroup){
			startGroup(startX, startY, width + 1, height);
			startX = 0;
			startY = 0;
		}
		drawLineBorder( text );
		startBlockText( startX, startY, width + 1, height, text );
		drawBlockTextChildren(text);
		endBlockText( text );
		if(needGroup)endGroup();
	}
	
	private void parseText( BlockTextArea text )
	{
		borders = render.cacheBorderInfo( text );
		if( borders != null)
		{
			if(isSquareBorder(borders))
			{
				needDrawLineBorder = false;
				needDrawSquareBorder = true;
			}
			else
			{
				needGroup = true;
				needDrawLineBorder = true;
				needDrawSquareBorder = false;
			}
		}
	}

	private void drawLineBorder(BlockTextArea text)
	{
		if(!needDrawLineBorder)return;
		BorderInfo[] borders = render.cacheBorderInfo( text );
		//set all the startX and startY to 0, since we wrap all the borders in a group
		for(BorderInfo info: borders){
			info.endX = info.endX - info.startX;
			info.endY = info.endY - info.startY;
			info.startX = 0;
			info.startY = 0;
		}
		render.drawBorder( borders );
	}
	
	private void startGroup(int startX, int startY, int width, int height)
	{	
		int shapeId = canvas.getPresentation( ).nextShapeId( );
		writer.openTag( "p:grpSp" );
		writer.openTag( "p:nvGrpSpPr" );
		writer.openTag( "p:cNvPr" );
		writer.attribute( "id", shapeId );
		writer.attribute( "name", "Group " + shapeId );
		writer.closeTag( "p:cNvPr" );
		writer.openTag( "p:cNvGrpSpPr" );
		writer.closeTag( "p:cNvGrpSpPr" );
		writer.openTag( "p:nvPr" );
		writer.closeTag( "p:nvPr" );
		writer.closeTag( "p:nvGrpSpPr" );
		writer.openTag( "p:grpSpPr" );
		canvas.setPosition( startX, startY, width + 1, height );
		writer.closeTag( "p:grpSpPr" );
	}
	
	private void endGroup()
	{
		if(!needGroup)return;	
		writer.closeTag( "p:grpSp" );
	}
	
	private void drawBlockTextChildren( IArea child)
	{
		if(child instanceof TextArea)
			writeTextRun( (TextArea)child );
		else if(child instanceof ContainerArea)
		{
			Iterator<IArea> iter = ((ContainerArea)child).getChildren( );
			while ( iter.hasNext( ) )	
			{
				IArea area = iter.next( );
				drawBlockTextChildren( area );
			}
			
		}
	}
	
	private void writeTextRun( TextArea text) {
		TextStyle style = text.getStyle( );
		FontInfo info = style.getFontInfo( );
		
		writer.openTag( "a:r" );
		setTextProperty( info.getFontName( ), info.getFontSize( ), info.getFontStyle( ), style.getColor(), style.isUnderline( ), style.isLinethrough( ), render.getGraphic( ).getLink() );
		writer.openTag( "a:t" );
		canvas.writeText(text.getText( ));
		writer.closeTag( "a:t" );
		writer.closeTag( "a:r" );
	}
	
	private void setTextProperty( String fontName, float fontSize,
			int fontStyle, Color color, boolean isUnderline,
			boolean isLineThrough, HyperlinkDef link )
	{
		writer.openTag( "a:rPr" );
		writer.attribute( "lang", "en-US" );
		writer.attribute( "altLang", "zh-CN" );
		writer.attribute( "dirty", "0" );
		writer.attribute( "smtClean", "0" );
		if ( isLineThrough )
		{
			writer.attribute( "strike", "sngStrike" );
		}
		if ( isUnderline )
		{
			writer.attribute( "u", "sng" );
		}
		writer.attribute( "sz", (int) ( fontSize * 100 ) );
		boolean isItalic = ( fontStyle & Font.ITALIC ) != 0;
		boolean isBold = ( fontStyle & Font.BOLD ) != 0;
		if ( isItalic )
		{
			writer.attribute( "i", 1 );
		}
		if ( isBold )
		{
			writer.attribute( "b", 1 );
		}
		setColor( color );
		setTextFont( fontName );
		canvas.setHyperlink( link );
		writer.closeTag( "a:rPr" );
	}
	

	private void setColor( Color color )
	{
		if ( color != null )
		{
			writer.openTag( "a:solidFill" );
			writer.openTag( "a:srgbClr" );
			writer.attribute( "val", EmitterUtil.getColorString( color ) );
			writer.closeTag( "a:srgbClr" );
			writer.closeTag( "a:solidFill" );
		}
	}
	
	private void setTextFont( String fontName )
	{
		writer.openTag( "a:latin" );
		writer.attribute( "typeface", fontName );
		writer.attribute( "pitchFamily", "18" );
		writer.attribute( "charset", "0" );
		writer.closeTag( "a:latin" );
		writer.openTag( "a:cs" );
		writer.attribute( "typeface", fontName );
		writer.attribute( "pitchFamily", "18" );
		writer.attribute( "charset", "0" );
		writer.closeTag( "a:cs" );
	}
	
	private void writeLineStyle( )
	{
		if(!needDrawSquareBorder)return;
		canvas.setProperty(borders[0].borderColor,  PPTXUtil.convertToEnums( borders[0].borderWidth ), borders[0].borderStyle);
	}
	
	private void startBlockText( int startX,int startY, int width, int height, BlockTextArea text)
	{		
		writer.openTag( "p:sp" );
		writer.openTag( "p:nvSpPr" );
		writer.openTag( "p:cNvPr" );
		int shapeId = canvas.getPresentation( ).nextShapeId( );
		writer.attribute( "id", shapeId );
		writer.attribute( "name", "TextBox " + shapeId );
		writer.closeTag( "p:cNvPr" );
		writer.openTag( "p:cNvSpPr" );
		writer.attribute( "txBox", "1" );
		writer.closeTag( "p:cNvSpPr" );
		writer.openTag( "p:nvPr" );
		writer.closeTag( "p:nvPr" );
		writer.closeTag( "p:nvSpPr" );
		writer.openTag( "p:spPr" );
		canvas.setPosition( startX, startY, width + 1, height );
		writer.openTag( "a:prstGeom" );
		writer.attribute( "prst", "rect" );
		writer.closeTag( "a:prstGeom" );
		
		Color color = text.getBoxStyle( ).getBackgroundColor( );
		if( color != null)
		{
			setColor( color );
		}
		
		writeLineStyle( );
		
		writer.closeTag( "p:spPr" );
		
		if ( needDrawSquareBorder )
		{
			writer.openTag( "p:style" );
			writer.openTag( "a:lnRef" );
			writer.attribute( "idx", "2" );
			writer.openTag( "a:schemeClr" );
			writer.attribute( "val", "dk1" );
			writer.closeTag( "a:schemeClr" );
			writer.closeTag( "a:lnRef" );
			writer.openTag( "a:fillRef" );
			writer.attribute( "idx", "1" );
			writer.openTag( "a:schemeClr" );
			writer.attribute( "val", "lt1" );
			writer.closeTag( "a:schemeClr" );
			writer.closeTag( "a:fillRef" );
			writer.openTag( "a:effectRef" );
			writer.attribute( "idx", "0" );
			writer.openTag( "a:schemeClr" );
			writer.attribute( "val", "dk1" );
			writer.closeTag( "a:schemeClr" );
			writer.closeTag( "a:effectRef" );
			writer.openTag( "a:fontRef" );
			writer.attribute( "idx", "minor" );
			writer.openTag( "a:schemeClr" );
			writer.attribute( "val", "dk1" );
			writer.closeTag( "a:schemeClr" );
			writer.closeTag( "a:fontRef" );
			writer.closeTag( "p:style" );
		}
		
		writer.openTag( "p:txBody" );
		writer.openTag( "a:bodyPr" );
		//writer.attribute( "wrap", "none" );
		writer.attribute( "wrap", "square" );
		//writer.attribute( "lIns", "0" );
		//writer.attribute( "tIns", "0" );
		//writer.attribute( "rIns", "0" );
		//writer.attribute( "bIns", "0" );
		writer.attribute( "rtlCol", "0" );
		writer.closeTag( "a:bodyPr" );
		writer.openTag( "a:p" );			
	}

	private void endBlockText(BlockTextArea text) {
		writer.closeTag( "a:p" );
		writer.closeTag( "p:txBody" );
		writer.closeTag( "p:sp" );
	}
	
	
}

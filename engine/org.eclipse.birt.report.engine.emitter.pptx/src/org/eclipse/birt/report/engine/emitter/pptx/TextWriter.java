
package org.eclipse.birt.report.engine.emitter.pptx;

import java.awt.Color;
import java.util.Iterator;

import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.pptx.util.PPTXUtil;
import org.eclipse.birt.report.engine.layout.emitter.BorderInfo;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.BlockTextArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.CellArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.InlineTextArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.TextArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.TextLineArea;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;
import org.eclipse.birt.report.engine.ooxml.ImageManager.ImagePart;
import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;

import com.lowagie.text.Font;

public class TextWriter
{

	private final PPTXRender render;
	private final PPTXCanvas canvas;
	private final OOXmlWriter writer;
	private boolean needShape = true;
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

	public static boolean isSingleTextControl( IContainerArea container )
 	{
		if ( container instanceof BlockTextArea)
		{
			Iterator<IArea> iter = container.getChildren( );
			while ( iter.hasNext( ) )
			{
				IArea area = iter.next( );
				if ( !(area instanceof TextLineArea)) {
					return false;
				}
			}
			return true;
		}
		if( container instanceof InlineTextArea )
		{
			Iterator<IArea> iter = container.getChildren( );
			while ( iter.hasNext( ) )
			{
				IArea area = iter.next( );
				if ( !(area instanceof TextLineArea)) {
					return false;
				}
			}
			return true;
		}
		return false;
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

	public void writeTextBlock( int startX, int startY, int width, int height,
			ContainerArea container )
	{
		parseBlockTextArea( container );

		startX = PPTXUtil.convertToEnums( startX );
		startY = PPTXUtil.convertToEnums( startY );
		width = PPTXUtil.convertToEnums( width );
		height = PPTXUtil.convertToEnums( height );

		if ( needGroup )
		{
			startGroup( startX, startY, width + 1, height );
			startX = 0;
			startY = 0;
		}
		drawLineBorder( container );
		startBlockText( startX, startY, width + 1, height, container );
		drawBlockTextChildren( container );
		endBlockText( container );
		if ( needGroup )
			endGroup( );
	}
	
	private void parseBlockTextArea( ContainerArea container )
	{
		if( container.getParent( ) instanceof CellArea)
		{
			needShape = false;
			return;
		}
		borders = render.cacheBorderInfo( container );
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

	private void drawLineBorder(ContainerArea container)
	{
		if(!needDrawLineBorder)return;
		BorderInfo[] borders = render.cacheBorderInfo( container );
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
		int shapeId = canvas.getPresentation( ).getNextShapeId( );
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
		writer.closeTag( "p:grpSp" );
	}
	
	private void drawBlockTextChildren( IArea child)
	{
		if(child instanceof TextArea)writeTextRun((TextArea)child);
		else if(child instanceof TextLineArea)
		{	
			startTextLineArea();
			Iterator<IArea> iter = ((TextLineArea)child).getChildren( );
			while ( iter.hasNext( ) )	
			{
				IArea area = iter.next( );
				drawBlockTextChildren( area );
			}
			
			endTextLineArea( (TextLineArea)child );
		}
		else if(child instanceof ContainerArea){
			Iterator<IArea> iter = (((ContainerArea)child).getChildren( ));
			while ( iter.hasNext( ) )	
			{
				IArea area = iter.next( );
				drawBlockTextChildren(area);
			}
		}		
	}
	
	private void startTextLineArea()
	{
		writer.openTag( "a:p" );
	}
	
	private void endTextLineArea( TextLineArea line)
	{
		writeTextLineBreak( ((TextArea)(line.getChild( line.getChildrenCount( )-1 ))).getStyle());
		writer.closeTag( "a:p" );
 	}	
	
	private void writeTextRun( TextArea text) 
	{

		writer.openTag( "a:r" );
		setTextProperty( "a:rPr", text.getStyle( ) );
		writer.openTag( "a:t" );
		canvas.writeText( text.getText( ) );
		writer.closeTag( "a:t" );
		writer.closeTag( "a:r" );
	}
	
	private void writeTextLineBreak( TextStyle style)
	{
		setTextProperty( "a:endParaRPr", style );
	/*	
		<a:endParaRPr lang="en-US" altLang="zh-CN" sz="1000" dirty="0" smtClean="0">
		<a:solidFill>
			<a:srgbClr val="000000"/>
		</a:solidFill>
		<a:latin typeface="Arial" pitchFamily="18" charset="0"/>
		<a:cs typeface="Arial" pitchFamily="18" charset="0"/>
		</a:endParaRPr>
	*/
	}
	
	private void setTextProperty( String tag, TextStyle style)
	{
		FontInfo info = style.getFontInfo( );

		writer.openTag( tag ); 
		//FIXME: retrieve language and not hard code values
		writer.attribute( "lang", "en-US" );
		writer.attribute( "altLang", "zh-CN" );
		writer.attribute( "dirty", "0" );
		writer.attribute( "smtClean", "0" );
		if ( style.isLinethrough( ) )
		{
			writer.attribute( "strike", "sngStrike" );
		}
		if ( style.isUnderline( ) )
		{
			writer.attribute( "u", "sng" );
		}
		writer.attribute( "sz", (int) ( info.getFontSize( ) * 100 ) );
		boolean isItalic = ( info.getFontStyle( ) & Font.ITALIC ) != 0;
		boolean isBold = ( info.getFontStyle( ) & Font.BOLD ) != 0;
		if ( isItalic )
		{
			writer.attribute( "i", 1 );
		}
		if ( isBold )
		{
			writer.attribute( "b", 1 );
		}
		setBackgroundColor( style.getColor( ) );
		setTextFont( info.getFontName( ) );
		canvas.setHyperlink( render.getGraphic( ).getLink() );
		writer.closeTag( tag );
	}
	

	private void setBackgroundColor( Color color )
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
	
	
	private void fillRectangleWithImage( ImagePart imageInfo, int x, int y,
			int width, int height, int offsetX, int offsetY )
	{
		writer.openTag( "a:blipFill" );
		writer.attribute( "dpi", "0" );
		writer.attribute( "rotWithShape", "1" );
		writer.openTag( "a:blip" );
		writer.attribute( "r:embed", imageInfo.getPart( ).getRelationshipId( ) );
		writer.closeTag( "a:blip" );

		// To stretch
		//writer.openTag( "a:stretch" );
		//writer.openTag( "a:fillRect" );
		//writer.closeTag( "a:fillRect" );
		//writer.closeTag( "a:stretch" );

		// To tile
		writer.openTag( "a:tile" );
		writer.attribute( "tx", offsetX );
		writer.attribute( "ty", offsetY );
		writer.closeTag( "a:tile" );
		writer.closeTag( "a:blipFill" );

	}
	
	private void setTextFont( String fontName )
	{
		writer.openTag( "a:latin" );
		writer.attribute( "typeface", fontName );
		writer.attribute( "pitchFamily", "18" );
		writer.attribute( "charset", "0" );
		writer.closeTag( "a:latin" );
		writer.openTag( "a:ea" );
		writer.attribute( "typeface", fontName );
		writer.attribute( "pitchFamily", "18" );
		writer.attribute( "charset", "0" );
		writer.closeTag( "a:ea" );
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
	
	private void startBlockText( int startX,int startY, int width, int height, ContainerArea container)
	{
		if ( needShape )
		{

			writer.openTag( "p:sp" );
			writer.openTag( "p:nvSpPr" );
			writer.openTag( "p:cNvPr" );
			int shapeId = canvas.getPresentation( ).getNextShapeId( );
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

			Color color = container.getBoxStyle( ).getBackgroundColor( );
			if ( color != null )
			{
				setBackgroundColor( color );
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
		}
		else{
			writer.openTag( "a:txBody" );
		}

		writer.openTag( "a:bodyPr" );
		//writer.attribute( "wrap", "none" );
		writer.attribute( "wrap", "square" );
		writer.attribute( "lIns", "0" );
		writer.attribute( "tIns", "0" );
		writer.attribute( "rIns", "0" );
		writer.attribute( "bIns", "0" );
		writer.attribute( "rtlCol", "0" );
		writer.closeTag( "a:bodyPr" );		
	}

	private void endBlockText( ContainerArea container )
	{
		if ( needShape )
		{
			writer.closeTag( "p:txBody" );
			writer.closeTag( "p:sp" );
		}
		else{
			writer.closeTag( "a:txBody" );
		}
	}
}

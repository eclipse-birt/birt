
package org.eclipse.birt.report.engine.emitter.pptx;

import java.awt.Color;
import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.impl.AutoTextContent;
import org.eclipse.birt.report.engine.emitter.ppt.util.PPTUtil.HyperlinkDef;
import org.eclipse.birt.report.engine.emitter.pptx.util.PPTXUtil;
import org.eclipse.birt.report.engine.emitter.pptx.writer.Presentation;
import org.eclipse.birt.report.engine.ir.AutoTextItemDesign;
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
import org.eclipse.birt.report.engine.nLayout.area.style.BackgroundImageInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;
import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;

import com.itextpdf.text.Font;

public class TextWriter
{

	private final PPTXRender render;
	private final PPTXCanvas canvas;
	private final OOXmlWriter writer;
	private boolean needShape = true;
	private boolean needGroup = false;
	private boolean needDrawLineBorder = false;
	private boolean needDrawSquareBorder = false;
	private boolean firstTextInCell = true;
	private BorderInfo[] borders = null;
	private static String DEFAULT_HALIGNMENT = "l"; 
	private String hAlign = DEFAULT_HALIGNMENT;
	private final int MAX_VAL = 158400;  //max size value for pptx val on Office Open XML
	
	private final String AUTOPAGENUMBER = "page-number";
	private final String TOTALPAGE = "total-page";
	
	private boolean hasParagraph = false;
	private HyperlinkDef link = null;
	private boolean needClip = false;
	private String bmk_relationshipid = null;
	private String autotexttype = null;	

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
			if(hasNonEmptyTextArea(container))return true;
			else return false;
		}
		else if( container instanceof InlineTextArea )
		{
			Iterator<IArea> iter = container.getChildren( );
			while ( iter.hasNext( ) )
			{
				IArea area = iter.next( );
				if ( !(area instanceof TextArea)) {
					return false;
				}
			}
			if(hasNonEmptyTextArea(container))return true;
			else return false;
		}
		return false;
	}
	
	private static boolean hasNonEmptyTextArea(IArea container)
	{
		if ( container instanceof TextArea )
		{
			if ( ( (TextArea) container ).getText( ) != null )
				return true;
			else
				return false;
		}

		if ( !( container instanceof IContainerArea ) )
			return false;
		Iterator<IArea> iter = ( (IContainerArea) container ).getChildren( );
		while ( iter.hasNext( ) )
		{
			IArea area = iter.next( );
			if ( hasNonEmptyTextArea( area ) )
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
		IHyperlinkAction linkact = container.getAction( );
		if ( linkact != null )
		{//add links
			String bmk = linkact.getBookmark( );
			if ( bmk != null )
			{
				bmk_relationshipid = canvas.getPresentation( )
						.getBookmarkRelationshipid( bmk );
			}
		}
		String bmk = container.getBookmark( );
		if ( bmk != null )
		{//addbookmarks
			Presentation presentation = canvas.getPresentation( );
			int currentslide = presentation.getCurrentSlideIdx( );
			presentation.addBookmark( bmk, currentslide );
		}
		
		parseBlockTextArea( container );
		needClip = container.needClip( );		
		startX = PPTXUtil.convertToEnums( startX );
		startY = PPTXUtil.convertToEnums( startY );
		width = PPTXUtil.convertToEnums( width );
		height = PPTXUtil.convertToEnums( height );

		if ( needGroup )
		{
			startGroup( startX, startY, width, height );
			startX = 0;
			startY = 0;
		}
		drawLineBorder( container );
		startBlockText( startX, startY, width, height, container );
		drawBlockTextChildren( container );
		endBlockText( container );
		if ( needGroup )
		{
			endGroup( );
		}
	}
	
	private void parseBlockTextArea( ContainerArea container )
	{
		if ( container.getParent( ) instanceof CellArea && firstTextInCell )
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

	private void drawLineBorder( ContainerArea container )
	{
		if ( !needDrawLineBorder )
		{
			return;
		}
		BorderInfo[] borders = render.cacheBorderInfo( container );
		// set all the startX and startY to 0, since we wrap all the borders in
		// a group
		for ( BorderInfo info : borders )
		{
			info.endX = info.endX - info.startX;
			info.endY = info.endY - info.startY;
			info.startX = 0;
			info.startY = 0;
		}
		render.drawBorder( borders );
	}
	
	private void startGroup( int startX, int startY, int width, int height )
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
		canvas.setPosition( startX, startY, width, height );
		writer.closeTag( "p:grpSpPr" );
	}
	
	private void endGroup( )
	{
		writer.closeTag( "p:grpSp" );
	}
	
	private void drawBlockTextChildren( IArea child )
	{
		if ( autotexttype != null )
		{
			writeAutoText( (ContainerArea) child );
		}
		else if ( child instanceof TextArea )
		{
			writeTextRun( (TextArea) child );
		}
		else if ( child instanceof TextLineArea
				|| child instanceof InlineTextArea )
		{

			Iterator<IArea> iter = ( (ContainerArea) child ).getChildren( );
			IArea grandchild = ( (ContainerArea) child ).getFirstChild( );
			if( grandchild instanceof TextArea )
			{
				//child.getHeight return value in 1/1000 pt
				//startTextLineArea accept value in 1/100 pt.
				startTextLineArea( grandchild.getHeight( ) / 10 );
			}
			hasParagraph = true;
			while ( iter.hasNext( ) )
			{
				IArea area = iter.next( );
				drawBlockTextChildren( area );
			}

			IArea lastchild = ( (ContainerArea) child ).getLastChild( );
			endTextLineArea( (TextArea) lastchild );
			hasParagraph = false;
		}
		else if ( child instanceof ContainerArea )
		{
			Iterator<IArea> iter = ( ( (ContainerArea) child ).getChildren( ) );
			while ( iter.hasNext( ) )
			{
				IArea area = iter.next( );
				drawBlockTextChildren( area );
			}
		}
	}
	
	private void writeAutoText( ContainerArea container )
	{
		TextArea text = null;
		IArea child = container.getFirstChild( );
		while ( child != null && !( child instanceof TextArea ) )
		{
			child = ( (ContainerArea) child ).getFirstChild( );
		}
		if ( child != null )
		{
			text = (TextArea) child;
		}
		if ( text == null )
		{
			return;
		}
		if ( autotexttype.equals( AUTOPAGENUMBER ) )
		{
			writer.openTag( "a:p" );
			writer.openTag( "a:fld" );
			writer.attribute( "id", "{AE09BA1C-136E-4F28-AF64-63E231249911}" );
			writer.attribute( "type", "slidenum" );
			setTextProperty( "a:rPr", text.getStyle( ) );
			writer.openTag( "a:pPr" );
			writer.closeTag( "a:pPr" );
			writer.openTag( "a:t" );
			canvas.writeText( "‹#›" );
			writer.closeTag( "a:t" );
			writer.closeTag( "a:fld" );
			endTextLineArea( text );
		}
		else if ( autotexttype.equals( TOTALPAGE ) )
		{
			Integer totalpage = canvas.getPresentation( ).getTotalSlides( );
			text.setText( totalpage.toString( ) );
			autotexttype = null;
			drawBlockTextChildren( container );
		}
		else
		{// back to default: for not implemented autotext
			autotexttype = null;
			drawBlockTextChildren( container );
		}
	}
	
	private void startTextLineArea( int lineHeight )
	{
		writer.openTag( "a:p" );
		writer.openTag( "a:pPr" );
		if ( hAlign != null )
		{
			writer.attribute( "algn", hAlign );
		}
		if ( render.isRTL( ) )
		{
			writer.attribute( "rtl", 1 );
		}
		if ( lineHeight > 0 && lineHeight < MAX_VAL )
		{
			writer.openTag( "a:lnSpc" );
			writer.openTag( "a:spcPts" );
			writer.attribute( "val", canvas.getScaledValue( lineHeight ) );
			writer.closeTag( "a:spcPts" );
			writer.closeTag( "a:lnSpc" );
		}
		writer.closeTag( "a:pPr" );
	}
	
	private void endTextLineArea( TextArea area )
	{
		writeTextLineBreak( area.getStyle( ) );
		writer.closeTag( "a:p" );
	}
	
	private void writeTextRun( TextArea text) 
	{
		if ( !hasParagraph )
		{
			// textHeight return value in 1/1000 pt, startTextLineArea need
			// value in 1/100 pt.
			startTextLineArea( text.getHeight( ) / 10 );
		}
		writer.openTag( "a:r" );
		setTextProperty( "a:rPr", text.getStyle( ) );
		writer.openTag( "a:t" );
		canvas.writeText( text.getText( ) );
		writer.closeTag( "a:t" );
		writer.closeTag( "a:r" );
		if ( !hasParagraph )
		{
			endTextLineArea( text );
		}
	}
	
	private void writeTextLineBreak( TextStyle style)
	{
		setTextProperty( "a:endParaRPr", style );
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
		writer.attribute( "sz",  canvas.getScaledValue( info.getFontSize( ) * 100 ) );
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
		canvas.setBackgroundColor( style.getColor( ) );
		setTextFont( info.getFontName( ) );
		canvas.setHyperlink( link );
		canvas.setBookmark( bmk_relationshipid);
		writer.closeTag( tag );
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

			BoxStyle style = container.getBoxStyle( );
			Color color = style.getBackgroundColor( );
			String imageRelationship = canvas
					.getImageRelationship( style.getBackgroundImage( ) );

			if( imageRelationship != null )
			{
				canvas.setBackgroundImg( imageRelationship, 0, 0 );
			}
			else if ( color != null )
			{
				canvas.setBackgroundColor( color );
			}
			else
			{
				writer.openTag( "a:noFill" );
				writer.closeTag( "a:noFill" );
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

		int leftPadding = 0;
		int rightPadding = 0;
		int topPadding = 0;
		int bottomPadding = 0;
		
		if ( container instanceof BlockTextArea )
		{
			IArea firstChild = container.getFirstChild( );
			IContent ic = container.getContent( );
			if ( ic != null && ic instanceof AutoTextContent )
			{
				AutoTextItemDesign id = (AutoTextItemDesign) ic.getGenerateBy( );
				autotexttype = id.getType( );
			}
			if ( firstChild != null )
			{
				leftPadding = PPTXUtil.convertToEnums( firstChild.getX( ));
				rightPadding = width
						- leftPadding
						- PPTXUtil.convertToEnums( firstChild.getWidth( ) );
				if ( rightPadding < 0 )
				{
					rightPadding = 0;
				}
				topPadding = PPTXUtil.convertToEnums( firstChild.getY( ) );
			}
			IArea lastChild = container.getChild( container.getChildrenCount( ) - 1 );
			if ( lastChild != null )
			{
				bottomPadding = height
						- PPTXUtil.convertToEnums( lastChild.getY( ) )
						- PPTXUtil.convertToEnums( lastChild.getHeight( ) );
				if( bottomPadding < 0 )
				{
					bottomPadding = 0;
				}
			}

		}
		writer.openTag( "a:bodyPr" );
		if ( needClip || !render.isTextWrap( ) )
		{
			writer.attribute( "vertOverflow", "clip" );
			writer.attribute( "wrap", "square" );
		}
		else
		{
			writer.attribute( "wrap", "none" );
		}
		writer.attribute( "lIns", leftPadding );
		writer.attribute( "tIns", topPadding );
		writer.attribute( "rIns", rightPadding );
		writer.attribute( "bIns", bottomPadding );
		writer.attribute( "rtlCol", "0" );
		
		IContent content = container.getContent( );
		String vAlign = null;
		if ( content != null )
		{
			vAlign = content.getComputedStyle( ).getVerticalAlign( );
			if ( vAlign != null && needShape )
			{
				if ( vAlign.equals( "bottom" ) )
				{
					writer.attribute( "anchor", "b" );
				}
				else if ( vAlign.equals( "middle" ) )
				{
					writer.attribute( "anchor", "ctr" );
				}
			}

			hAlign = content.getComputedStyle( ).getTextAlign( );
			if ( hAlign != null )
			{
				if ( hAlign.equals( "left" ) )
				{
					hAlign = "l";
				}
				else if ( hAlign.equals( "right" ) )
				{
					hAlign = "r";
				}
				else if ( hAlign.equals( "center" ) )
				{
					hAlign = "ctr";
				}
				else if ( hAlign.equals( "justify" ) )
				{
					hAlign = "just";
				}
				else
				{
					hAlign = DEFAULT_HALIGNMENT;
				}
			}
		}
		
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
	
	/**
	 * Create a blank text body on respective font size
	 * 
	 * @param fontSize
	 */
	public void writeBlankTextBlock( int fontSize )
	{
		writer.openTag( "a:txBody" );
		writer.openTag( "a:bodyPr" );
		writer.closeTag( "a:bodyPr" );
		writer.openTag( "a:p" );
		writer.openTag( "a:endParaRPr" );
		writer.attribute( "sz", fontSize );
		writer.closeTag( "a:endParaRPr" );
		writer.closeTag( "a:p" );
		writer.closeTag( "a:txBody" );
	}

	public void setNotFirstTextInCell( )
	{
		firstTextInCell = false;
	}
	
	public void setLink( HyperlinkDef link )
	{
		this.link = link;
	}

	public HyperlinkDef getLink( )
	{
		return link;
	}
}

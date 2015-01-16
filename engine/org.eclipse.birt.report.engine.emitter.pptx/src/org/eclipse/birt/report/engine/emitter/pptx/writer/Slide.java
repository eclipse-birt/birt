/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.pptx.writer;

import java.awt.Color;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.ppt.util.PPTUtil.HyperlinkDef;
import org.eclipse.birt.report.engine.emitter.pptx.TableWriter;
import org.eclipse.birt.report.engine.emitter.pptx.util.PPTXUtil;
import org.eclipse.birt.report.engine.layout.emitter.Image;
import org.eclipse.birt.report.engine.layout.emitter.util.BackgroundImageLayout;
import org.eclipse.birt.report.engine.layout.emitter.util.Position;
import org.eclipse.birt.report.engine.layout.pdf.font.FontInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BorderInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;
import org.eclipse.birt.report.engine.ooxml.IPart;
import org.eclipse.birt.report.engine.ooxml.ImageManager;
import org.eclipse.birt.report.engine.ooxml.ImageManager.ImagePart;
import org.eclipse.birt.report.engine.ooxml.constants.ContentTypes;
import org.eclipse.birt.report.engine.ooxml.constants.NameSpaces;
import org.eclipse.birt.report.engine.ooxml.constants.RelationshipTypes;
import org.eclipse.birt.report.engine.ooxml.util.OOXmlUtil;
import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;

import com.lowagie.text.Font;

public class Slide extends Component
{

	private static final String TAG_SLIDE = "p:sld";
	private static Logger logger = Logger.getLogger( Slide.class.getName( ) );
	private int index;

	private Presentation presentation;
	private int shapeCount = 1;
	private boolean isClosed = false;
	private ImageManager imageManager;

	private TableWriter table = null;
	
	public Slide( Presentation presentation, int slideIndex )
			throws IOException
	{
		this( presentation, slideIndex, null );
	}

	public Slide( Presentation presentation, int slideIndex, Color bgColor )
			throws IOException
	{
		this.index = slideIndex;
		this.presentation = presentation;
		String uri = getSlideUri( index );
		String relationShipType = RelationshipTypes.SLIDE;
		String type = ContentTypes.SLIDE;
		initialize( presentation.getPart( ), uri, type, relationShipType );
		new SlideLayout( this, index );
		this.imageManager = (ImageManager) part.getPackage( )
				.getExtensionData( );
		writer.startWriter( );
		writer.openTag( TAG_SLIDE );
		writer.nameSpace( "a", NameSpaces.DRAWINGML );
		writer.nameSpace( "r", NameSpaces.RELATIONSHIPS );
		writer.nameSpace( "p", NameSpaces.PRESENTATIONML );
		writer.openTag( "p:cSld" );
		if ( bgColor != null )
			drawSlideBackgroundColor( bgColor );
		writer.openTag( "p:spTree" );
		writer.openTag( "p:nvGrpSpPr" );
		writer.openTag( "p:cNvPr" );
		writer.attribute( "id", nextShapeId( ) );
		writer.attribute( "name", "" );
		writer.closeTag( "p:cNvPr" );
		writer.openTag( "p:cNvGrpSpPr" );
		writer.closeTag( "p:cNvGrpSpPr" );
		writer.openTag( "p:nvPr" );
		writer.closeTag( "p:nvPr" );
		writer.closeTag( "p:nvGrpSpPr" );
		writer.openTag( "p:grpSpPr" );
		writer.closeTag( "p:grpSpPr" );
	}

	private void drawSlideBackgroundColor( Color color )
	{
		writer.openTag( "p:bg" );
		writer.openTag( "p:bgPr" );
		setColor( color );
		writer.openTag( "a:effectLst" );
		writer.closeTag( "a:effectLst" );
		writer.closeTag( "p:bgPr" );
		writer.closeTag( "p:bg" );
	}

	public void drawLine( int startX, int startY, int endX, int endY,
			int width, Color color, int lineStyle )
	{
		table.drawLine(startX, startY, endX, endY, width, color, lineStyle);

	}

	public void drawText( String text, int textX, int textY, int width,
			int height, String fontName, float fontSize, int fontStyle,
			Color color, boolean isUnderline, boolean isLineThrough,
			HyperlinkDef link )
	{
		writer.openTag( "p:sp" );
		writer.openTag( "p:nvSpPr" );
		writer.openTag( "p:cNvPr" );
		int shapeId = nextShapeId( );
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
		setPosition( textX, textY, width + 1, height );
		writer.openTag( "a:prstGeom" );
		writer.attribute( "prst", "rect" );
		writer.closeTag( "a:prstGeom" );
		writer.closeTag( "p:spPr" );
		writer.openTag( "p:txBody" );
		writer.openTag( "a:bodyPr" );
		writer.attribute( "wrap", "none" );
		writer.attribute( "lIns", "0" );
		writer.attribute( "tIns", "0" );
		writer.attribute( "rIns", "0" );
		writer.attribute( "bIns", "0" );
		writer.attribute( "rtlCol", "0" );
		writer.closeTag( "a:bodyPr" );
		writer.openTag( "a:p" );
		writer.openTag( "a:r" );
		setTextProperty( fontName, fontSize, fontStyle, color, isUnderline,
				isLineThrough, link );
		writer.openTag( "a:t" );
		writeText(text);
		writer.closeTag( "a:t" );
		writer.closeTag( "a:r" );
		writer.closeTag( "a:p" );
		writer.closeTag( "p:txBody" );
		writer.closeTag( "p:sp" );
	}
	
	/**
	 * Word have extra limitation on text in run: a. it must following xml
	 * format. b. no ]]> so , we need replace all &, <,> in the text
	 * 
	 * @param text
	 */
	private void writeText(String text) {
		int length = text.length();
		StringBuilder sb = new StringBuilder(length * 2);
		for (int i = 0; i < length; i++) {
			char ch = text.charAt(i);
			switch (ch) {
			case '&':
				sb.append("&amp;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '<':
				sb.append("&lt;");
				break;
			default:
				sb.append(ch);
			}
		}
		writer.cdata(sb.toString());
	}

	public void drawImage( String uri, String extension, int imageX,
			int imageY, int height, int width, String helpText, HyperlinkDef link )
			throws IOException
	{
		byte[] imageData = EmitterUtil.getImageData( uri );
		IPart imagePart = imageManager.getImagePart( part, uri, imageData )
				.getPart( );
		drawImage( imagePart, imageX, imageY, height, width, helpText, true,
				link );
	}

	public void drawImage( String imageId, byte[] imageData, String extension,
			int imageX, int imageY, int height, int width, String helpText,
			HyperlinkDef link ) throws IOException
	{
		drawImage( imageId, imageData, extension, imageX, imageY, height,
				width, helpText, true, link );
	}

	private void drawImage( String imageId, byte[] imageData, String extension,
			int imageX, int imageY, int height, int width, String helpText,
			boolean stretch, HyperlinkDef link ) throws IOException
	{
		IPart imagePart = imageManager.getImagePart( part, imageId, imageData )
				.getPart( );
		drawImage( imagePart, imageX, imageY, height, width, helpText,
				stretch, link );
	}
	
	private Crop checkCrop( int x, int y, int width, int height )
	{
		if ( clipStack.isEmpty( ) )
		{
			return null;
		}
		ClipArea clip = clipStack.peek( );
		int left = 0, right = 0, top = 0, bottom = 0;
		if ( x < clip.x )
		{
			left = (int) ( ( clip.x - x ) / (float) width * 100000 );
		}
		if ( y < clip.y )
		{
			top = (int) ( ( clip.y - y ) / (float) height * 100000 );
		}
		if ( x + width > clip.x + clip.width )
		{
			right = (int) ( ( ( x + width ) - ( clip.x + clip.width ) )
					/ (float) width * 100000 );
		}
		if ( y + height > clip.y + clip.height )
		{
			bottom = (int) ( ( ( y + height ) - ( clip.y + clip.height ) )
					/ (float) height * 100000 );
		}
		if ( left != 0 || right != 0 || top != 0 || bottom != 0 )
		{
			return new Crop( left, right, top, bottom );
		}
		return null;
	}
	
	private class Crop
	{

		int left, right, top, bottom;

		Crop( int left, int right, int top, int bottom )
		{
			this.left = left;
			this.right = right;
			this.top = top;
			this.bottom = bottom;
		}
	}

	private void drawImage( IPart imagePart, int imageX, int imageY,
			int height, int width, String helpText, boolean stretch,
			HyperlinkDef link )
	{
		String relationshipId = imagePart.getRelationshipId( );
		writer.openTag( "p:pic" );
		writer.openTag( "p:nvPicPr" );
		writer.openTag( "p:cNvPr" );
		int shapeId = nextShapeId( );
		writer.attribute( "id", shapeId );
		writer.attribute( "name", "Image " + shapeId );
		writer.attribute( "descr", helpText );
		setHyperlink( link );
		writer.closeTag( "p:cNvPr" );
		writer.openTag( "p:cNvPicPr" );
		writer.openTag( "a:picLocks" );
		writer.attribute( "noChangeAspect", "1" );
		writer.closeTag( "a:picLocks" );
		writer.closeTag( "p:cNvPicPr" );
		writer.openTag( "p:nvPr" );
		writer.closeTag( "p:nvPr" );
		writer.closeTag( "p:nvPicPr" );
		writer.openTag( "p:blipFill" );
		Crop crop = checkCrop( imageX, imageY, width, height );
		if ( crop != null )
		{
			writer.attribute( "rotWithShape", "1" );	
		}
		writer.openTag( "a:blip" );
		writer.attribute( "r:embed", relationshipId );
		writer.closeTag( "a:blip" );
		if ( crop != null )
		{
			writer.openTag( "a:srcRect" );
			if ( crop.top != 0 )
				writer.attribute( "t", crop.top );
			if ( crop.left != 0 )
				writer.attribute( "l", crop.left );
			if ( crop.right != 0 )
				writer.attribute( "r", crop.right );
			if ( crop.bottom != 0 )
				writer.attribute( "b", crop.bottom );
			writer.closeTag( "a:srcRect" );
		}
		if ( stretch )
		{
			writer.openTag( "a:stretch" );
			// writer.openTag("a:fillRect");
			// writer.closeTag("a:fillRect");
			writer.closeTag( "a:stretch" );
		}
		writer.closeTag( "p:blipFill" );
		writer.openTag( "p:spPr" );
		if ( crop == null )
		{
			setPosition( imageX, imageY, width, height );
		}
		else
		{
			ClipArea clip = clipStack.peek( );
			int pX = Math.max( clip.x, imageX );
			int pY = Math.max( clip.y, imageY );
			int pWidth = Math.min( imageX + width, clip.x + clip.width ) - pX;
			int pHeight = Math.min( imageY + height, clip.y + clip.height )
					- pY;
			setPosition( pX, pY, pWidth, pHeight );
		}
		writer.openTag( "a:prstGeom" );
		writer.attribute( "prst", "rect" );
		writer.closeTag( "a:prstGeom" );
		writer.closeTag( "p:spPr" );
		writer.closeTag( "p:pic" );
	}

	public void drawBackgroundColor( Color color, int x, int y, int width,
			int height )
	{
		if ( color != null )
		{
			writer.openTag( "p:sp" );
			writer.openTag( "p:nvSpPr" );
			writer.openTag( "p:cNvPr" );
			int shapeId = nextShapeId( );
			writer.attribute( "id", shapeId );
			writer.attribute( "name", "Rectangle " + shapeId );
			writer.closeTag( "p:cNvPr" );
			writer.openTag( "p:cNvSpPr" );
			writer.closeTag( "p:cNvSpPr" );
			writer.openTag( "p:nvPr" );
			writer.closeTag( "p:nvPr" );
			writer.closeTag( "p:nvSpPr" );
			writer.openTag( "p:spPr" );
			setPosition( x, y, width, height );
			writer.openTag( "a:prstGeom" );
			writer.attribute( "prst", "rect" );
			writer.closeTag( "a:prstGeom" );
			setColor( color );
			writer.closeTag( "p:spPr" );
			writer.closeTag( "p:sp" );
		}
	}

	public void drawBackgroundImage( int x, int y, int width, int height,
			int imageWidth, int imageHeight, int repeat, String imageURI,
			byte[] imageData, int offsetX, int offsetY )
	{
		if ( imageURI == null || imageURI.length( ) == 0 )
		{
			return;
		}
		if ( imageData == null || imageData.length == 0 )
		{
			return;
		}
		try
		{
			if ( !imageManager.hasImage( imageURI ) )
			{
				org.eclipse.birt.report.engine.layout.emitter.Image image = EmitterUtil
						.parseImage( imageData, null, null );
				imageData = image.getData( );
			}

			ImagePart imagePartInfo = imageManager.getImagePart( part,
					imageURI, imageData );
			Image imageInfo = imagePartInfo.getImageInfo( );

			float originalImageWidth = imageWidth != 0 ? imageWidth : imageInfo
					.getWidth( );
			float originalImageHeight = imageHeight != 0
					? imageHeight
					: imageInfo.getHeight( );

			Position areaPosition = new Position( x, y );
			Position areaSize = new Position( width, height );
			Position imagePosition = new Position( x + offsetX, y + offsetY );
			Position imageSize = new Position( originalImageWidth, originalImageHeight );
			BackgroundImageLayout layout = new BackgroundImageLayout(
					areaPosition, areaSize, imagePosition, imageSize );
			Collection positions = layout.getImagePositions( repeat );
			Iterator iterator = positions.iterator( );
			while ( iterator.hasNext( ) )
			{
				Position position = (Position) iterator.next( );
				fillRectangleWithImage( imagePartInfo, (int) OOXmlUtil
						.convertPointerToEmus( position.getX( ) ), (int) OOXmlUtil
						.convertPointerToEmus( position.getY( ) ), (int) OOXmlUtil
						.convertPointerToEmus( originalImageWidth ), (int) OOXmlUtil
						.convertPointerToEmus( originalImageHeight ), 0, 0 );
			}
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, e.getLocalizedMessage( ), e );
		}
	}

	private float getImageRange( float maxRange, float offset, float imageSize )
	{
		float result = imageSize;
		if ( offset < 0 )
		{
			result = Math.max( 0, imageSize + offset );
		}
		else if ( offset + imageSize > maxRange )
		{
			result = Math.max( 0, maxRange - offset );
		}
		return result;
	}

	private boolean isOutOfRange( float maxRange, float offset, float imageSize )
	{
		return offset <= 0 - imageSize || offset >= maxRange;
	}

	private void fillRectangleWithImage( ImagePart imageInfo, int x, int y,
			int width, int height, int offsetX, int offsetY )
	{
		writer.openTag( "p:sp" );
		writer.openTag( "p:nvSpPr" );
		writer.openTag( "p:cNvPr" );
		int shapeId = nextShapeId( );
		writer.attribute( "id", shapeId );
		writer.attribute( "name", "Rectangle " + shapeId );
		writer.closeTag( "p:cNvPr" );
		writer.openTag( "p:cNvSpPr" );
		writer.closeTag( "p:cNvSpPr" );
		writer.openTag( "p:nvPr" );
		writer.closeTag( "p:nvPr" );
		writer.closeTag( "p:nvSpPr" );
		writer.openTag( "p:spPr" );
		setPosition( x, y, width, height );
		writer.openTag( "a:prstGeom" );
		writer.attribute( "prst", "rect" );
		writer.closeTag( "a:prstGeom" );
		writer.openTag( "a:blipFill" );
		writer.attribute( "dpi", "0" );
		writer.attribute( "rotWithShape", "1" );
		writer.openTag( "a:blip" );
		writer.attribute( "r:embed", imageInfo.getPart( ).getRelationshipId( ) );
		writer.closeTag( "a:blip" );
		
		//To stretch
		writer.openTag( "a:stretch" );
		writer.openTag( "a:fillRect" );
		writer.closeTag( "a:fillRect" );
		writer.closeTag( "a:stretch" );
		
		//To tile
//		writer.openTag( "a:tile" );
//		writer.attribute( "tx", offsetX );
//		writer.attribute( "ty", offsetY );
//		writer.closeTag( "a:tile" );
		writer.closeTag( "a:blipFill" );
		writer.openTag( "a:ln" );
		writer.openTag( "a:noFill" );
		writer.closeTag( "a:noFill" );
		writer.closeTag( "a:ln" );
		writer.closeTag( "p:spPr" );
		writer.closeTag( "p:sp" );
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
		setHyperlink( link );
		writer.closeTag( "a:rPr" );
	}

	private void setHyperlink( HyperlinkDef link )
	{
		if ( link != null )
		{
			String hyperlink = link.getLink( );
			if ( hyperlink != null )
			{
				if ( hyperlink.startsWith( "\"" ) && hyperlink.endsWith( "\"" ) )
				{
					hyperlink = hyperlink
							.substring( 1, hyperlink.length( ) - 1 );
				}
				writer.openTag( "a:hlinkClick" );
				writer.attribute( "r:id", part.getHyperlinkId( hyperlink ) );
				if ( link.getTooltip( ) != null )
				{
					writer.attribute( "tooltip", link.getTooltip( ) );
				}
				writer.closeTag( "a:hlinkClick" );
			}
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

	public void setPosition( int startX, int startY, int width, int height )
	{
		writer.openTag( "a:xfrm" );
		writer.openTag( "a:off" );
		writer.attribute( "x", startX );
		writer.attribute( "y", startY );
		writer.closeTag( "a:off" );
		writer.openTag( "a:ext" );
		writer.attribute( "cx", width );
		writer.attribute( "cy", height );
		writer.closeTag( "a:ext" );
		writer.closeTag( "a:xfrm" );
	}

	public void setProperty( Color color, int width, int style )
	{
		writer.openTag( "a:ln" );
		writer.attribute( "w", width );
		setColor( color );

		// the other line styles, e.g. 'ridge', 'outset', 'groove', 'insert'
		// is NOT supported now and all regarded with default style, i.e, solid.
		if ( style == BorderInfo.BORDER_STYLE_DOTTED )
		{
			setStyle( "sysDash" );
		}
		else if ( style == BorderInfo.BORDER_STYLE_DASHED )
		{
			setStyle( "dash" );
		}

		writer.closeTag( "a:ln" );
	}

	private void setStyle( String lineStyle )
	{
		writer.openTag( "a:prstDash" );
		writer.attribute( "val", lineStyle );
		writer.closeTag( "a:prstDash" );
	}

	public Presentation getPresentation( )
	{
		return presentation;
	}

	private String getSlideUri( int slideIndex )
	{
		return "slides/slide" + slideIndex + ".xml";
	}

	public int getSlideId( )
	{
		return 255 + index;
	}

	public void dispose( )
	{
		if ( !isClosed )
		{
			writer.closeTag( "p:spTree" );
			writer.closeTag( "p:cSld" );
			writer.openTag( "p:clrMapOvr" );
			writer.openTag( "a:masterClrMapping" );
			writer.closeTag( "a:masterClrMapping" );
			writer.closeTag( "p:clrMapOvr" );
			writer.closeTag( TAG_SLIDE );
			writer.endWriter( );
			writer.close( );
			writer = null;
			isClosed = true;
		}
	}

	public int nextShapeId( ) //change to public
	{
		return shapeCount++;
	}

	public void drawText( String text, int textX, int textY, int width,
			int height, TextStyle textStyle, HyperlinkDef link )
	{
		FontInfo fontInfo = textStyle.getFontInfo( );
		String fontName = fontInfo.getFontName( );
		float fontSize = fontInfo.getFontSize( );
		int fontStyle = fontInfo.getFontStyle( );
		Color color = textStyle.getColor( );
		drawText( text, textX, textY, width, height, fontName, fontSize,
				fontStyle, color, textStyle.isUnderline( ), textStyle
						.isLinethrough( ), link );
	}

	private Stack<ClipArea> clipStack = new Stack<ClipArea>( );
	
	private class ClipArea
	{
		int x, y, width, height;

		ClipArea( int x, int y, int width, int height )
		{
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
	}
	
	public void startClip( int startX, int startY, int width, int height )
	{
		startX = PPTXUtil.convertToEnums( startX );
		startY = PPTXUtil.convertToEnums( startY );
		width = PPTXUtil.convertToEnums( width );
		height = PPTXUtil.convertToEnums( height );
		
		if ( clipStack.isEmpty( ) )
		{
			clipStack.push( new ClipArea( startX, startY, width, height ) );
		}
		else
		{
			ClipArea parent = clipStack.peek( );
			int newX = Math.max( parent.x, startX );
			int newY = Math.max( parent.y, startY );
			int newWidth = Math.min( startX + width, parent.x + parent.width )
					- newX;
			int newHeight = Math
					.min( startY + height, parent.y + parent.height ) - newY;
			clipStack
					.push( new ClipArea( newX, newY, newWidth, newHeight ) );
		}
	}
	
	public void endClip( )
	{
		clipStack.pop( );
	}
	
	public OOXmlWriter getWriter( ) 
	{
		return writer;
	}

	public void setTable( TableWriter table){
		this.table = table;
	}
}

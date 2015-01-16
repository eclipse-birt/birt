
package org.eclipse.birt.report.engine.emitter.pptx;

import java.awt.Color;
import java.util.Iterator;
import java.util.Stack;

import org.eclipse.birt.report.engine.emitter.pptx.writer.Slide;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.CellArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.RowArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.TableArea;
import org.eclipse.birt.report.engine.nLayout.area.style.BackgroundImageInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BorderInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;
import org.eclipse.birt.report.engine.nLayout.area.style.DiagonalInfo;
import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;

public class TableWriter
{

	private final Slide slide;
	private int currentX;
	private int currentY;
	protected Stack<BoxStyle> rowStyleStack = new Stack<BoxStyle>( );
	private final float scale;
	private final PPTXRender render;
	private final PPTXPage pageGraphic;
	protected OOXmlWriter writer;

	public TableWriter( PPTXRender render )
	{
		this.render = render;
		slide = render.getSlide( );
		writer = slide.getWriter( );
		pageGraphic = render.getGraphic( );
		scale = render.getScale( );
		currentX = render.getCurrentX( );
		currentY = render.getCurrentY( );
	}

	public void outputTable( TableArea table )
	{
		slide.setTable( this ); // queue table
		drawTable( table );
	}

	public void drawTable( IContainerArea tableElement )
	{
		startContainer( tableElement );
		visitChildren( tableElement );
		endContainer( tableElement );
	}

	/**
	 * The container may be a TableArea, RowArea, etc. Or just the border of
	 * textArea/imageArea. This method draws the border and background of the
	 * given container.
	 * 
	 * @param TableArea
	 *            the TableArea specified from layout
	 */
	protected void startContainer( IContainerArea tableElement )
	{

		if ( tableElement.needClip( ) )
		{
			render.startClip( tableElement );

		}
		if ( tableElement instanceof RowArea )
		{
			rowStyleStack.push( tableElement.getBoxStyle( ) );
		}
		else if ( tableElement instanceof CellArea )
		{
			drawCell( (CellArea) tableElement );
		}

		else
		{

			render.drawContainer( tableElement );
		}
		currentX += getX( tableElement );
		currentY += getY( tableElement );
	}

	protected void visitChildren( IContainerArea container )
	{
		Iterator<IArea> iter = container.getChildren( );
		while ( iter.hasNext( ) )
		{
			IArea child = iter.next( );
			// if table, row or cell visit inside else out.
			if ( child instanceof CellArea || child instanceof RowArea )
			{
				drawTable( (IContainerArea) child );
			}
			else
			{
				updateRenderXY( );
				child.accept( render );
			}

		}
	}

	/**
	 * This method will be invoked while a containerArea ends.
	 * 
	 * @param container
	 *            the ContainerArea specified from layout
	 */
	protected void endContainer( IContainerArea container )
	{
		currentX -= getX( container );
		currentY -= getY( container );

		if ( container instanceof RowArea )
		{
			rowStyleStack.pop( );
		}
		if ( container instanceof TableArea )
		{
			updateRenderXY( );
			render.drawTableBorder( (TableArea) container );
		}
		else if ( !( container instanceof CellArea ) )
		{
			render.setCurrentX( currentX );
			render.setCurrentY( currentY );
			org.eclipse.birt.report.engine.layout.emitter.BorderInfo[] borders = render.cacheBorderInfo( container );
			render.drawBorder( borders );
		}
		if ( container.needClip( ) )
		{
			pageGraphic.endClip( );
		}

	}

	protected void drawCell( CellArea container )
	{
		drawCellDiagonal( container );
		Color rowbc = null;
		BackgroundImageInfo rowbi = null;
		BoxStyle rowStyle = null;
		// get the style of the row
		if ( rowStyleStack.size( ) > 0 )
		{
			rowStyle = rowStyleStack.peek( );
			if ( rowStyle != null )
			{
				rowbc = rowStyle.getBackgroundColor( );
				rowbi = rowStyle.getBackgroundImage( );
			}
		}

		BoxStyle style = container.getBoxStyle( );
		Color bc = style.getBackgroundColor( );
		BackgroundImageInfo bi = style.getBackgroundImage( );
		// String imageUrl = EmitterUtil.getBackgroundImageUrl(
		// style,reportDesign );

		if ( rowbc != null || rowbi != null || bc != null || bi != null )
		{
			// the container's start position (the left top corner of the
			// container)
			int startX = currentX + getX( container );
			int startY = currentY + getY( container );

			// the dimension of the container
			int width = getWidth( container );
			int height = getHeight( container );

			if ( rowbc != null )
			{
				pageGraphic.drawBackgroundColor( rowbc,
						startX,
						startY,
						width,
						height );
			}
			if ( rowbi != null )
			{
				render.drawBackgroundImage( rowbi,
						startX,
						startY,
						width,
						height );
			}
			if ( bc != null )
			{
				// Draws background color for the container, if the background
				// color is NOT set, draws nothing.
				pageGraphic.drawBackgroundColor( bc,
						startX,
						startY,
						width,
						height );
			}
			if ( bi != null )
			{
				// Draws background image for the container. if the background
				// image is NOT set, draws nothing.
				render.drawBackgroundImage( bi, startX, startY, width, height );
			}
		}

	}

	protected void drawCellDiagonal( CellArea cell )
	{
		DiagonalInfo diagonalInfo = cell.getDiagonalInfo( );
		if ( diagonalInfo != null )
		{
			int startX = currentX + getX( cell );
			int startY = currentY + getY( cell );

			// the dimension of the container
			int width = getWidth( cell );
			int height = getHeight( cell );
			int dw = diagonalInfo.getDiagonalWidth( );
			int ds = diagonalInfo.getDiagonalStyle( );
			// support double style, use solid style instead.
			if ( ds == DiagonalInfo.BORDER_STYLE_DOUBLE )
			{
				ds = DiagonalInfo.BORDER_STYLE_SOLID;
			}
			switch ( diagonalInfo.getDiagonalNumber( ) )
			{
				case 2 :
					drawLine( startX + width / 2,
							startY,
							startX + width,
							startY + height - dw / 2,
							getScaledValue( dw ),
							diagonalInfo.getDiagonalColor( ),
							ds );
					drawLine( startX,
							startY + height / 2,
							startX + width,
							startY + height - dw / 2,
							getScaledValue( dw ),
							diagonalInfo.getDiagonalColor( ),
							ds );
					break;
				case 1 :
					drawLine( startX,
							startY + dw / 2,
							startX + width,
							startY + height - dw / 2,
							getScaledValue( dw ),
							diagonalInfo.getDiagonalColor( ),
							ds );
					break;

				default :
					drawLine( startX,
							startY + dw / 2,
							startX + width,
							startY + height - dw / 2,
							getScaledValue( dw ),
							diagonalInfo.getDiagonalColor( ),
							ds );
					drawLine( startX + width / 2,
							startY + dw / 2,
							startX + width,
							startY + height - dw / 2,
							getScaledValue( dw ),
							diagonalInfo.getDiagonalColor( ),
							ds );
					drawLine( startX,
							startY + height / 2,
							startX + width,
							startY + height - dw / 2,
							getScaledValue( dw ),
							diagonalInfo.getDiagonalColor( ),
							ds );
					break;
			}

		}
	}

	/**
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param width
	 * @param color
	 * @param lineStyle
	 * 
	 *            pre: all are set in EMU units
	 */
	public void drawLine( int startX, int startY, int endX, int endY,
			int width, Color color, int lineStyle )
	{
		if ( color == null
				|| width == 0f
				|| lineStyle == BorderInfo.BORDER_STYLE_NONE )
		{
			return;
		}
		writer.openTag( "p:cxnSp" );
		writer.openTag( "p:nvCxnSpPr" );
		writer.openTag( "p:cNvPr" );
		int shapeId = slide.nextShapeId( );
		writer.attribute( "id", shapeId );
		writer.attribute( "name", "Line " + shapeId );
		writer.closeTag( "p:cNvPr" );
		writer.openTag( "p:cNvCxnSpPr" );
		writer.closeTag( "p:cNvCxnSpPr" );
		writer.openTag( "p:nvPr" );
		writer.closeTag( "p:nvPr" );
		writer.closeTag( "p:nvCxnSpPr" );
		writer.openTag( "p:spPr" );
		slide.setPosition( startX, startY, endX - startX, endY - startY );
		writer.openTag( "a:prstGeom" );
		writer.attribute( "prst", "line" );
		writer.closeTag( "a:prstGeom" );
		slide.setProperty( color, width, lineStyle );
		writer.closeTag( "p:spPr" );
		writer.closeTag( "p:cxnSp" );
	}

	private int getY( IContainerArea area )
	{
		return getScaledValue( area.getY( ) );
	}

	private int getX( IContainerArea area )
	{
		return getScaledValue( area.getX( ) );
	}

	private int getHeight( CellArea area )
	{
		return getScaledValue( area.getHeight( ) );
	}

	private int getWidth( CellArea area )
	{
		return getScaledValue( area.getWidth( ) );
	}

	protected int getScaledValue( int value )
	{
		return (int) ( value * scale );
	}

	protected void updateRenderXY( )
	{
		render.setCurrentX( currentX );
		render.setCurrentY( currentY );
	}

}


package org.eclipse.birt.report.engine.emitter.pptx;

import java.awt.Color;
import java.util.Iterator;
import java.util.Stack;

import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.CellArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.RowArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.TableArea;
import org.eclipse.birt.report.engine.nLayout.area.style.BackgroundImageInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;
import org.eclipse.birt.report.engine.nLayout.area.style.DiagonalInfo;
import org.eclipse.birt.report.engine.ooxml.writer.OOXmlWriter;

public class TableWriter
{

	private int currentX;
	private int currentY;
	protected Stack<BoxStyle> rowStyleStack = new Stack<BoxStyle>( );
	private final float scale;
	private final PPTXRender render;
	private final PPTXPage graphics;
	private final PPTXCanvas canvas;
	protected OOXmlWriter writer;

	public TableWriter( PPTXRender render )
	{
		this.render = render;
		this.graphics = render.getGraphic( );
		this.canvas = render.getCanvas( );
		this.writer = canvas.getWriter( );
		scale = render.getScale( );
		currentX = render.getCurrentX( );
		currentY = render.getCurrentY( );
	}

	public void outputTable( TableArea table )
	{
		drawTable( table );
	}

	protected void drawTable( TableArea table )
	{
		if ( table.needClip( ) )
		{
			render.startClip( table );

		}
		currentX += getX( table );
		currentY += getY( table );
		Iterator<IArea> iter = table.getChildren( );
		while ( iter.hasNext( ) )
		{
			IArea child = iter.next( );
			drawRow( (RowArea) child );
		}
		if ( table.needClip( ) )
		{
			// end clip...
		}
		updateRenderXY( );
		render.drawTableBorder( table );
		if ( table.needClip( ) )
		{
			graphics.endClip( );
		}
		currentX -= getX( table );
		currentY -= getY( table );
	}

	protected void drawRow( RowArea row )
	{
		currentX += getX( row );
		currentY += getY( row );
		rowStyleStack.push( row.getParent( ).getBoxStyle( ) );
		Iterator<IArea> iter = row.getChildren( );
		while ( iter.hasNext( ) )
		{
			IArea child = iter.next( );
			drawCell( (CellArea) child );
		}
		rowStyleStack.pop( );
		currentX -= getX( row );
		currentY -= getY( row );
	}

	protected void drawCell( CellArea cell )
	{
		currentX += getX( cell );
		currentY += getY( cell );
		drawCellBox( cell );
		visitChildren( cell );
		currentX -= getX( cell );
		currentY -= getY( cell );
	}

	protected void visitChildren( IContainerArea container )
	{
		updateRenderXY( );
		Iterator<IArea> iter = container.getChildren( );
		while ( iter.hasNext( ) )
		{
			IArea child = iter.next( );
			child.accept( render );
		}
		updateRenderXY( );
	}

	protected void drawCellBox( CellArea cell )
	{
		drawCellDiagonal( cell );
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

		BoxStyle style = cell.getBoxStyle( );
		Color bc = style.getBackgroundColor( );
		BackgroundImageInfo bi = style.getBackgroundImage( );
		// String imageUrl = EmitterUtil.getBackgroundImageUrl(
		// style,reportDesign );

		if ( rowbc != null || rowbi != null || bc != null || bi != null )
		{
			// the container's start position (the left top corner of the
			// container)
			int startX = currentX;
			int startY = currentY;

			// the dimension of the container
			int width = getWidth( cell );
			int height = getHeight( cell );

			if ( rowbc != null )
			{
				graphics.drawBackgroundColor( rowbc,
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
				graphics.drawBackgroundColor( bc, startX, startY, width, height );
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
			int startX = currentX;
			int startY = currentY;

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
					graphics.drawLine( startX + width / 2,
							startY,
							startX + width,
							startY + height - dw / 2,
							getScaledValue( dw ),
							diagonalInfo.getDiagonalColor( ),
							ds );
					graphics.drawLine( startX,
							startY + height / 2,
							startX + width,
							startY + height - dw / 2,
							getScaledValue( dw ),
							diagonalInfo.getDiagonalColor( ),
							ds );
					break;
				case 1 :
					graphics.drawLine( startX,
							startY + dw / 2,
							startX + width,
							startY + height - dw / 2,
							getScaledValue( dw ),
							diagonalInfo.getDiagonalColor( ),
							ds );
					break;

				default :
					graphics.drawLine( startX,
							startY + dw / 2,
							startX + width,
							startY + height - dw / 2,
							getScaledValue( dw ),
							diagonalInfo.getDiagonalColor( ),
							ds );
					graphics.drawLine( startX + width / 2,
							startY + dw / 2,
							startX + width,
							startY + height - dw / 2,
							getScaledValue( dw ),
							diagonalInfo.getDiagonalColor( ),
							ds );
					graphics.drawLine( startX,
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

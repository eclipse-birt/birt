/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.border;

import java.util.Iterator;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.util.TableBorderCollisionArbiter;
import org.eclipse.draw2d.geometry.Insets;

/**
 * A helper class cooperate to provide cell border calculation.
 */

public class TableBorderHelper
{

	private TableEditPart owner;

	private int[] maxHeights, maxWidths;
	private int[][] heights, widths;

	/**
	 * Use to store all actual border drawing data, array size:
	 * [2*colCount*rowCount+colCount+rowCount][5], the last dimension arranged
	 * as: [style][width][color][rowIndex][colIndex], index is Zero-based.
	 */
	private int[][] borderData;

	/**
	 * The constructor.
	 * 
	 * @param owner
	 */
	public TableBorderHelper( TableEditPart owner )
	{
		this.owner = owner;
	}

	/**
	 * Returns the maximum horizontal cell border height, index ranges from [0] -
	 * [table.rowCount], [0] means the toppest border which maybe collapsed by
	 * the table top border, [table.rowCount] means the bottomest border which
	 * maybe collapsed by the table bottom border.
	 * 
	 * @param rowNumber
	 * @return
	 */
	public int getHorizontalBorderHeight( int rowNumber )
	{
		if ( maxWidths == null )
		{
			initialize( );
		}

		if ( rowNumber >= 0 && rowNumber <= owner.getRowCount( ) )
		{
			return maxHeights[rowNumber];
		}

		return 0;
	}

	/**
	 * Returns the maximum vertical cell border width, index ranges from [0] -
	 * [table.columnCount], [0] means the leftest border which maybe collapsed
	 * by the table left border, [table.columnCount] means the rightest border
	 * which maybe collapsed by the table right border.
	 * 
	 * @param colNumber
	 * @return
	 */
	public int getVerticalBorderWidth( int colNumber )
	{
		if ( maxHeights == null )
		{
			initialize( );
		}

		if ( colNumber >= 0 && colNumber <= owner.getColumnCount( ) )
		{
			return maxWidths[colNumber];
		}

		return 0;
	}

	/**
	 * Initialize the helper.
	 */
	private void initialize( )
	{
		int rowCount = owner.getRowCount( );
		int colCount = owner.getColumnCount( );

		maxHeights = new int[rowCount + 1];
		maxWidths = new int[colCount + 1];

		heights = new int[colCount][rowCount + 1];
		widths = new int[rowCount][colCount + 1];

		borderData = new int[2 * colCount * rowCount + colCount + rowCount][5];

		// initialize all index data as -1.
		for ( int i = 0; i < rowCount; i++ )
		{
			for ( int j = 0; j < colCount; j++ )
			{
				//top
				borderData[i * ( 2 * colCount + 1 ) + j][3] = -1;
				borderData[i * ( 2 * colCount + 1 ) + j][4] = -1;

				//bottom
				borderData[( i + 1 ) * ( 2 * colCount + 1 ) + j][3] = -1;
				borderData[( i + 1 ) * ( 2 * colCount + 1 ) + j][4] = -1;

				//left
				borderData[i * ( 2 * colCount + 1 ) + colCount + j][3] = -1;
				borderData[i * ( 2 * colCount + 1 ) + colCount + j][4] = -1;

				//right
				borderData[i * ( 2 * colCount + 1 ) + colCount + j + 1][3] = -1;
				borderData[i * ( 2 * colCount + 1 ) + colCount + j + 1][4] = -1;
			}
		}

		// initialize all other border data.
		for ( Iterator itr = owner.getChildren( ).iterator( ); itr.hasNext( ); )
		{
			TableCellEditPart cellPart = (TableCellEditPart) itr.next( );

			int rowIndex = cellPart.getRowNumber( );
			int colIndex = cellPart.getColumnNumber( );
			int rowSpan = cellPart.getRowSpan( );
			int colSpan = cellPart.getColSpan( );

			CellBorder border = (CellBorder) cellPart.getFigure( ).getBorder( );
			Insets ins = border.getTrueBorderInsets( );

			int topStyle = border.getTopBorderStyle( );
			int topWidth = border.getTopBorderWidth( );
			int topColor = border.getTopBorderColor( );

			int bottomStyle = border.getBottomBorderStyle( );
			int bottomWidth = border.getBottomBorderWidth( );
			int bottomColor = border.getBottomBorderColor( );

			int leftStyle = border.getLeftBorderStyle( );
			int leftWidth = border.getLeftBorderWidth( );
			int leftColor = border.getLeftBorderColor( );

			int rightStyle = border.getRightBorderStyle( );
			int rightWidth = border.getRightBorderWidth( );
			int rightColor = border.getRightBorderColor( );

			maxHeights[rowIndex - 1] = Math.max( maxHeights[rowIndex - 1],
					ins.top );
			maxHeights[rowIndex + rowSpan - 1] = Math.max( maxHeights[rowIndex
					+ rowSpan
					- 1], ins.bottom );

			for ( int i = 0; i < colSpan; i++ )
			{
				// update border data using collision arbiter.
				TableBorderCollisionArbiter.refreshBorderData( borderData[( rowIndex - 1 )
						* ( 2 * colCount + 1 )
						+ colIndex
						- 1
						+ i],
						topStyle,
						topWidth,
						topColor,
						rowIndex - 1,
						colIndex - 1 + i );

				TableBorderCollisionArbiter.refreshBorderData( borderData[( rowIndex
						+ rowSpan - 1 )
						* ( 2 * colCount + 1 )
						+ colIndex
						- 1
						+ i],
						bottomStyle,
						bottomWidth,
						bottomColor,
						rowIndex - 1 + rowSpan - 1,
						colIndex - 1 + i );

				// update border insets data.
				heights[colIndex - 1 + i][rowIndex - 1] = Math.max( heights[colIndex
						- 1
						+ i][rowIndex - 1],
						ins.top );
				heights[colIndex - 1 + i][rowIndex + rowSpan - 1] = Math.max( heights[colIndex
						- 1
						+ i][rowIndex + rowSpan - 1],
						ins.bottom );
			}

			maxWidths[colIndex - 1] = Math.max( maxWidths[colIndex - 1],
					ins.left );
			maxWidths[colIndex + colSpan - 1] = Math.max( maxWidths[colIndex
					+ colSpan
					- 1], ins.right );

			for ( int i = 0; i < rowSpan; i++ )
			{
				// update border data using collision arbiter.
				TableBorderCollisionArbiter.refreshBorderData( borderData[( rowIndex - 1 + i )
						* ( 2 * colCount + 1 )
						+ colCount
						+ colIndex
						- 1],
						leftStyle,
						leftWidth,
						leftColor,
						rowIndex - 1 + i,
						colIndex - 1 );

				TableBorderCollisionArbiter.refreshBorderData( borderData[( rowIndex - 1 + i )
						* ( 2 * colCount + 1 )
						+ colCount
						+ colIndex
						+ colSpan
						- 1],
						rightStyle,
						rightWidth,
						rightColor,
						rowIndex - 1 + i,
						colIndex - 1 + colSpan - 1 );

				// update border insets data.
				widths[rowIndex - 1 + i][colIndex - 1] = Math.max( widths[rowIndex
						- 1
						+ i][colIndex - 1],
						ins.left );
				widths[rowIndex - 1 + i][colIndex + colSpan - 1] = Math.max( widths[rowIndex
						- 1
						+ i][colIndex + colSpan - 1],
						ins.right );
			}

		}

	}

	/**
	 * Updates all cell border insets.
	 */
	public void updateCellBorderInsets( )
	{
		if ( maxHeights == null || maxWidths == null )
		{
			initialize( );
		}

		int rowCount = owner.getRowCount( );
		int colCount = owner.getColumnCount( );

		for ( Iterator itr = owner.getChildren( ).iterator( ); itr.hasNext( ); )
		{
			TableCellEditPart cellPart = (TableCellEditPart) itr.next( );

			int rowIndex = cellPart.getRowNumber( );
			int colIndex = cellPart.getColumnNumber( );
			int rowSpan = cellPart.getRowSpan( );
			int colSpan = cellPart.getColSpan( );

			CellBorder border = (CellBorder) cellPart.getFigure( ).getBorder( );

			Insets borderInsets = new Insets( );

			// if it's a toppest and bottomest cell, don't give it the insets,
			// job is handled by Table border.
			if ( rowIndex == 1 && ( rowIndex + rowSpan - 1 ) == rowCount )
			{
				borderInsets.top = 0;
				borderInsets.bottom = 0;
			}
			else if ( rowIndex == 1 )
			{
				// if it's the toppest cell, don't give the top insets, but set
				// the bottom insets.

				borderInsets.top = 0;

				int bh = 0;

				for ( int i = 0; i < colSpan; i++ )
				{
					bh = Math.max( bh, heights[colIndex - 1 + i][rowIndex
							+ rowSpan
							- 1]
							/ 2
							+ heights[colIndex - 1 + i][rowIndex + rowSpan - 1]
							% 2 );
				}

				borderInsets.bottom = bh;
			}
			else if ( ( rowIndex + rowSpan - 1 ) == rowCount )
			{
				// if it's the bottomest cell, don't give the bottom insets, but
				// set the top insets.

				int th = 0;

				for ( int i = 0; i < colSpan; i++ )
				{
					th = Math.max( th,
							heights[colIndex - 1 + i][rowIndex - 1] / 2 );
				}

				borderInsets.top = th;

				borderInsets.bottom = 0;
			}
			else
			{
				// if neigher the toppest nor the bottomest cell, both set the
				// top and bottom insets.

				int bh = 0;
				int th = 0;

				for ( int i = 0; i < colSpan; i++ )
				{
					th = Math.max( th,
							heights[colIndex - 1 + i][rowIndex - 1] / 2 );
					bh = Math.max( bh, heights[colIndex - 1 + i][rowIndex
							+ rowSpan
							- 1]
							/ 2
							+ heights[colIndex - 1 + i][rowIndex + rowSpan - 1]
							% 2 );
				}

				borderInsets.top = th;
				borderInsets.bottom = bh;
			}

			// if it's a leftest and rightest cell, don't give it the insets,
			// job is handled by Table border.
			if ( colIndex == 1 && ( colIndex + colSpan - 1 ) == colCount )
			{
				borderInsets.left = 0;
				borderInsets.right = 0;
			}
			else if ( colIndex == 1 )
			{
				// if it's the leftest cell, don't give the left insets, but set
				// the right insets.

				borderInsets.left = 0;

				int rw = 0;

				for ( int i = 0; i < rowSpan; i++ )
				{
					rw = Math.max( rw, widths[rowIndex - 1 + i][colIndex
							+ colSpan
							- 1]
							/ 2
							+ widths[rowIndex - 1 + i][colIndex + colSpan - 1]
							% 2 );
				}
				borderInsets.right = rw;
			}
			else if ( ( colIndex + colSpan - 1 ) == colCount )
			{
				// if it's the rightest cell, don't give the right insets, but
				// set the left insets.

				int lw = 0;

				for ( int i = 0; i < rowSpan; i++ )
				{
					lw = Math.max( lw,
							widths[rowIndex - 1 + i][colIndex - 1] / 2 );
				}

				borderInsets.left = lw;
				borderInsets.right = 0;
			}
			else
			{
				// if neigher the leftest nor the rightest cell, both set the
				// left and right insets.

				int rw = 0;
				int lw = 0;

				for ( int i = 0; i < rowSpan; i++ )
				{
					rw = Math.max( rw, widths[rowIndex - 1 + i][colIndex
							+ colSpan
							- 1]
							/ 2
							+ widths[rowIndex - 1 + i][colIndex + colSpan - 1]
							% 2 );
					lw = Math.max( lw,
							widths[rowIndex - 1 + i][colIndex - 1] / 2 );
				}
				borderInsets.left = lw;
				borderInsets.right = rw;
			}

			border.setBorderInsets( borderInsets );
		}

	}

	/**
	 * Returns the actual border drawing data. especially for TableBorderLayer.
	 * 
	 * @return
	 */
	public int[][] getBorderData( )
	{
		if ( borderData == null )
		{
			initialize( );
		}

		return borderData;
	}

}
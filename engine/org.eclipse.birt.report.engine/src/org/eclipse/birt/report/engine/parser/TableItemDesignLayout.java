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
package org.eclipse.birt.report.engine.parser;

import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;


/**
 * calculate the cell id explictly.
 *
 * @version $Revision:$ $Date:$
 */
public class TableItemDesignLayout
{
	int[] columns;
	int bufferSize;
	int columnCount;
	int lastColumn;

	public void layout(TableItemDesign table)
	{
		ensureSize(table.getColumnCount());
		layoutBand(table.getHeader());
		for (int i = 0; i < table.getGroupCount(); i++)
		{
			layoutBand(table.getGroup(i).getHeader());
		}
		layoutBand(table.getDetail());
		for (int i = table.getGroupCount()-1; i >= 0; i--)
		{
			layoutBand(table.getGroup(i).getFooter());
		}
		layoutBand(table.getFooter());
		//should we reset the column size?
	}
	
	void layoutBand(TableBandDesign band)
	{
		if (band != null)
		{
			for (int i = 0; i < band.getRowCount(); i++)
			{
				layoutRow(band.getRow(i));
			}
		}
	}
	
	void layoutRow(RowDesign row)
	{
		createRow();
		for (int i = 0; i < row.getCellCount(); i++)
		{
			CellDesign cell = row.getCell(i);
			int columnNo = cell.getColumn();
			int rowSpan = cell.getRowSpan();
			int colSpan = cell.getColSpan();
			int columnId = createCell(columnNo - 1, rowSpan, colSpan);
			cell.setColumn(columnId + 1);
		}
	}
	
	void ensureSize(int columnSize)
	{
		if (bufferSize < columnSize)
		{
			int[] newColumns = new int[columnSize];
			if (columns != null)
			{
				System.arraycopy(columns, 0, newColumns, 0, bufferSize);
			}
			columns = newColumns;
			bufferSize = columnSize;
		}
	}
	
	void createRow()
	{
		for (int i = 0; i < columnCount; i++)
		{
			if (columns[i] > 0)
			{
				columns[i]--;
			}
		}
		lastColumn = 0;
	}
	
	int createCell(int columnId, int rowSpan, int colSpan )
	{
		if (columnId == -1)
		{
			columnId = getNextEmptyCell();
		}
		ensureSize(columnId + colSpan);
		for (int i = 0; i < colSpan; i++)
		{
			columns[columnId + i] = rowSpan;
		}
		lastColumn = columnId + 1;
		if (lastColumn > columnCount)
		{
			columnCount = lastColumn;
		}
		return columnId;
	}
	
	int getNextEmptyCell()
	{
		for (int i = lastColumn; i < columnCount; i++)
		{
			if (columns[i] == 0)
			{
				return i;
			}
		}
		return columnCount;
	}
	
	
}

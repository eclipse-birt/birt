
package org.eclipse.birt.report.engine.emitter.excel.layout;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.excel.ExcelUtil;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;


public class LayoutUtil
{
	public static ColumnsInfo createTable( int col, int width )
	{
		return new ColumnsInfo( col, width );
	}	
	
	public static ColumnsInfo createTable( IListContent list, int width )
	{
		width = getElementWidth(list, width);
		int[] column = new int[] {width};
		return new ColumnsInfo( column );
	}

	public static ColumnsInfo createChart( IForeignContent content, int width )
	{
		ExtendedItemDesign design = (ExtendedItemDesign) content
				.getGenerateBy( );
		DimensionType value = design.getWidth( );

		if ( value != null )
		{
			width = Math.min( ExcelUtil.covertDimensionType( value, width ),
					width );
		}
		int[] column = new int[]{width};
		return new ColumnsInfo( column );
	}
	
	public static int getElementWidth( IContent content, int width )
	{
		DimensionType value = content.getWidth( );
		
		if(value != null)
		{
			try 
			{
				width = Math.min( ExcelUtil.covertDimensionType( value, width ), 
						            width );
			}
			catch(Exception e) 
			{
				
			}
		}
		return width;
	}
	
	public static int[] createTable( ITableContent table, int tableWidth )
	{		
		int columnCount = table.getColumnCount( );
		int[] columns = new int[columnCount];
		int unassignedCount = 0;
		int totalAssigned = 0;
		
		for(int i = 0; i < columnCount; i++)
		{
			DimensionType value = table.getColumn( i ).getWidth( );  
			if( value == null)
			{
				columns[i] = -1;
				unassignedCount++;
			}
			else
			{				
				columns[i] = ExcelUtil.covertDimensionType( value, tableWidth );
				totalAssigned += columns[i];
			}	
		}		
		
		if ( table.getWidth( ) == null && unassignedCount == 0 )
		{
			return columns;
		}

		return EmitterUtil.resizeTableColumn( tableWidth, columns,
				unassignedCount, totalAssigned );
	}
}

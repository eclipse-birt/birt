
package org.eclipse.birt.report.engine.emitter.excel.layout;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.emitter.excel.ExcelUtil;
import org.eclipse.birt.report.engine.ir.DimensionType;


public class LayoutUtil
{
	public static TableInfo createTable(int col, int width)
	{
		return new DefaultTableInfo(col, width);
	}	
	
	public static TableInfo createTable(IListContent list, int width)
	{
		width = getElementWidth(list, width);
		int[] column = new int[] {width};
		return new DefaultTableInfo(column);
	}
	
	private static int getElementWidth(IContent content, int width)
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
	
	public static TableInfo createTable( ITableContent table, int width )
	{		
		int tableWidth = getElementWidth( table, width );
		
		int columnCount = table.getColumnCount( );
		if ( columnCount == 0 )
		{
			return null;
		}
		
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
		
		int leftWidth = tableWidth - totalAssigned;
		if ( leftWidth != 0 && unassignedCount == 0 )
		{
			for ( int i = 0; i < columnCount; i++ )
			{
				columns[i] = resize( columns[i], totalAssigned, leftWidth );
			}
		}
		else if ( leftWidth < 0 && unassignedCount > 0 )
		{
			for ( int i = 0; i < columnCount; i++ )
			{
				if ( columns[i] == -1 )
					columns[1] = 0;
				else
					columns[i] = resize( columns[i], totalAssigned, leftWidth );
			}
		}
		else if ( leftWidth >= 0 && unassignedCount > 0 )
		{
			int per = (int) leftWidth / unassignedCount;
			int index = 0;
			for ( int i = 0; i < columns.length; i++ )
			{
				if ( columns[i] == -1 )
				{
					columns[i] = per;
					index = i;
				}
			}
			columns[index] = leftWidth - per * ( unassignedCount - 1 );
		}
		return new DefaultTableInfo( columns );
	}
	
	private static int resize( int width, int total, int left )
	{
		return (int) ( width + (float) width / (float) total * left );
	}
}

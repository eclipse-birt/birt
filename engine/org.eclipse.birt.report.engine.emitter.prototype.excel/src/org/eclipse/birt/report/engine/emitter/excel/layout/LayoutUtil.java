
package org.eclipse.birt.report.engine.emitter.excel.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.emitter.excel.ExcelUtil;
import org.eclipse.birt.report.engine.ir.DimensionType;


public class LayoutUtil
{
	public static int[] getColumnWidth( TableInfo table, int width )
	{
		int[] col = new int[table.getColumnCount( )];
		int tmp = 0;
		int nullNumber = 0;

		for ( int i = 0; i < table.getColumnCount( ); i++ )
		{
			int colwidth = table.getColumnWidth( i );
			// record columns which is not set column's width in report design.
			if ( colwidth == 0 )
			{
				nullNumber++;
				col[i] = -1;
			}
			else
			{
				col[i] = colwidth;
				tmp += col[i];
			}
		}

		// If columns are not set width, set the average width to them.
		if ( nullNumber != 0 )
		{
			int aveWidth = ( width - tmp ) / nullNumber;
			tmp = 0;

			for ( int i = 0; i < col.length; i++ )
			{
				if ( col[i] == -1 )
				{
					col[i] = aveWidth;
				}
				tmp += col[i];
			}

		}

		// Set the left width to the last column.
		col[col.length - 1] += width - tmp;

		return col;
	}
	
	public static TableInfo createTable(int col, int width)
	{
		return new DefaultTableInfo(col, width);
	}	
	
	public static TableInfo createTable(ITableContent table, int width)
	{
		int colcount = table.getColumnCount( );
		
		if ( colcount == 0 )
		{
			return null;
		}
		
		int[] index = new int[colcount];
		int know = 0;
		List unmount = new ArrayList();
		
		for(int i = 0; i < colcount; i++)
		{
			DimensionType value = table.getColumn( i ).getWidth( );  
			if( value == null)
			{
				unmount.add( new Integer(i) );
			}
			else
			{				
				try {
					index[i] = ExcelUtil.covertDimensionType( value, width ); 
					know += index[i];
				}
				catch(IllegalArgumentException ex)
				{
					unmount.add( new Integer(i) );
				}
			}	
		}		
		
		int left = width - know;
		
		if(left > 0 && unmount.size( ) == 0)
		{
			index[index.length - 1] = index[index.length - 1] + left; 
			return new DefaultTableInfo(index);
		}
		else if(left < 0 )
		{
			return new DefaultTableInfo(split(width, colcount));
		}
		else if(left > 0 && unmount.size( ) > 0)
		{
			int[] size = split(left, unmount.size());			
			Iterator iter = unmount.iterator( );
			int i = 0;
			
			while(iter.hasNext( ))
			{
				int pos = ((Integer) iter.next()).intValue( );
				index[pos] = size[i];
				i++;
			}
			
			return new DefaultTableInfo(index);
		}
		else 
		{
			return new DefaultTableInfo(index);
		}	
	}
	
	public static int[] split(int width, int count)
	{
		int[] size = new int[count];		
		int per = (int) width / count;
		
		for(int i = 0; i < count - 1; i++)
		{
			size[i] = per;
		}	
		
		size[count - 1] = width - per * (count - 1);
		
		return size;
	}
}

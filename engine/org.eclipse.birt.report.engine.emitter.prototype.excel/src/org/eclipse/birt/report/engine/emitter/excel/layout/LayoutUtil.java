
package org.eclipse.birt.report.engine.emitter.excel.layout;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.excel.ExcelUtil;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.layout.emitter.Image;


public class LayoutUtil
{

	private static final Logger log = Logger.getLogger( LayoutUtil.class
			.getName( ) );

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
			width = getElementWidth( value, width );
		}
		int[] column = new int[]{width};
		return new ColumnsInfo( column );
	}
	
	public static ColumnsInfo createImage( IImageContent image, int width )
	{
		width = getImageWidth( image, width );
		int[] column = new int[]{width};
		return new ColumnsInfo( column );
	}

	public static int getImageWidth( IImageContent image, int width )
	{
		DimensionType value = image.getWidth( );
		if ( value != null )
		{
			width = getElementWidth( value, width );
		}
		else
		{
			try
			{
				Image imageInfo = EmitterUtil.parseImage( image, image
						.getImageSource( ), image.getURI( ), image
						.getMIMEType( ), image.getExtension( ) );
				width = (int) ( imageInfo.getWidth( ) * ExcelUtil.PX_PT );

			}
			catch ( IOException e1 )
			{
				log.log( Level.WARNING, e1.getLocalizedMessage( ) );
			}
		}
		return width;
	}

	public static int getElementWidth( IContent content, int width )
	{
		DimensionType value = content.getWidth( );
		if ( value != null )
		{
			return getElementWidth( value, width );
		}
		return width;
	}

	private static int getElementWidth( DimensionType contentWdith, int width )
	{
		try
		{
			width = Math.min( ExcelUtil.convertDimensionType( contentWdith,
					width ), width );
		}
		catch ( Exception e )
		{

		}
		return width;
	}
	
	public static int[] createFixedTable( ITableContent table, int tableWidth )
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
				columns[i] = ExcelUtil.convertDimensionType( value, tableWidth );
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

	public static ColumnsInfo createTable( ITableContent table, int width )
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

		for ( int i = 0; i < columnCount; i++ )
		{
			DimensionType value = table.getColumn( i ).getWidth( );
			if ( value == null )
			{
				columns[i] = -1;
				unassignedCount++;
			}
			else
			{
				columns[i] = ExcelUtil.convertDimensionType( value, tableWidth );
				totalAssigned += columns[i];
			}
		}

		int leftWidth = tableWidth - totalAssigned;
		if ( leftWidth != 0 && unassignedCount == 0 )
		{
			int totalResized = 0;
			for ( int i = 0; i < columnCount - 1; i++ )
			{
				columns[i] = resize( columns[i], totalAssigned, leftWidth );
				totalResized += columns[i];
			}
			columns[columnCount - 1] = tableWidth - totalResized;
		}
		else if ( leftWidth < 0 && unassignedCount > 0 )
		{
			int totalResized = 0;
			int lastAssignedIndex = 0;
			for ( int i = 0; i < columnCount; i++ )
			{
				if ( columns[i] == -1 )
				{
					columns[i] = 0;
				}
				else
				{
					columns[i] = resize( columns[i], totalAssigned, leftWidth );
					lastAssignedIndex = i;
				}
				totalResized += columns[i];
			}
			columns[lastAssignedIndex] += tableWidth - totalResized;
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
		return new ColumnsInfo( columns );
	}

	private static int resize( int width, int total, int left )
	{
		return Math.round( width + (float) width / (float) total * left );
	}
}

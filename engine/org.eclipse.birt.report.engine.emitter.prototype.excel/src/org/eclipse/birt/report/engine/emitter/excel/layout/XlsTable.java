package org.eclipse.birt.report.engine.emitter.excel.layout;

import org.eclipse.birt.report.engine.emitter.excel.StyleEntry;


public class XlsTable extends XlsContainer
{
	private int[] columnWidths;
	
	private int width;
	
	public XlsTable(StyleEntry entry, ContainerSizeInfo sizeInfo)
	{
		super(entry, sizeInfo);
	}
	
	public XlsTable(TableInfo table, StyleEntry entry, ContainerSizeInfo sizeInfo)
	{
		this(entry, sizeInfo);
		width = Math.min( table.getTableWidth( ), sizeInfo.getWidth() );
		this.columnWidths = LayoutUtil.getColumnWidth( table, width );
		this.width = table.getTableWidth( );
	}
	
	public XlsTable(TableInfo table, XlsContainer container)
	{
		this(table, container.getStyle( ), container.getSizeInfo( ));
	}
	
	public ContainerSizeInfo getColumnSizeInfo(int column, int span)
	{
		int startCoordinate = getSizeInfo().getStartCoordinate( );
		
		for(int i = 0; i < column; i++)
		{
			startCoordinate += columnWidths[i];
		}	
		
		int endCoordinate = 0;
		
		for(int i = column; i < column + span; i++)
		{
			endCoordinate += columnWidths[i];
		}	
		
		return new ContainerSizeInfo(startCoordinate, endCoordinate);
	}	
}

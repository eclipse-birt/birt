package org.eclipse.birt.report.engine.emitter.excel.layout;

import org.eclipse.birt.report.engine.emitter.excel.StyleEntry;


public class XlsTable extends XlsContainer
{
	private int[] columnWidths;
	
	private int width;
	
	public XlsTable(StyleEntry entry, ContainerSizeInfo sizeInfo, XlsContainer parent )
	{
		super(entry, sizeInfo, parent );
	}
	
	public XlsTable( ColumnsInfo table, StyleEntry entry,
			ContainerSizeInfo sizeInfo, XlsContainer parent )
	{
		this( entry, sizeInfo, parent );
		width = table.getTotalWidth( );
		columnWidths = table.getColumns( );
	}
	
	public XlsTable( ColumnsInfo table, XlsContainer container )
	{
		this( table, container.getStyle( ), container.getSizeInfo( ), container );
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

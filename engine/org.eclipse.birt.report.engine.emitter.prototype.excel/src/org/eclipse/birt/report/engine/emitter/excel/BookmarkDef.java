package org.eclipse.birt.report.engine.emitter.excel;


public class BookmarkDef
{
	private String name;
	private int sheetNo = 1;
	private int columnNo;
	private int rowNo;
	
	public BookmarkDef( String name )
	{
		this.name = name;
	}
	
	public String getName( )
	{
		return name;
	}
	
	public void setName( String name )
	{
		this.name = name;
	}
	
	public String getRefer( )
	{
		StringBuffer sb = new StringBuffer( "=Sheet" );
		sb.append( sheetNo );
		sb.append( "!R" );
		sb.append( rowNo );
		sb.append( "C" );
		sb.append( columnNo );
		return sb.toString( );
	}

	public int getSheetNo( )
	{
		return sheetNo;
	}

	public void setSheetNo( int sheetNo )
	{
		this.sheetNo = sheetNo;
	}

	public int getColumnNo( )
	{
		return columnNo;
	}

	public void setColumnNo( int columnNo )
	{
		this.columnNo = columnNo;
	}
	
	public int getRowNo( )
	{
		return rowNo;
	}

	public void setRowNo( int rowNo )
	{
		this.rowNo = rowNo;
	}
	
}

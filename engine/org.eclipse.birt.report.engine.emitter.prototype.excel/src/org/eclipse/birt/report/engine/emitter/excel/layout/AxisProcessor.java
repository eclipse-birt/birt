package org.eclipse.birt.report.engine.emitter.excel.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AxisProcessor
{	
	/**
	 * Each element of naxis is the start point of each column, 
	 * indexed by colId.
	 */
	private List naxis = new ArrayList();
	
	public AxisProcessor()
	{	
		addCoordinate(0);		
	}	
	
	public void addCoordinates(int[] values)
	{
		for(int i = 0; i < values.length; i++)
		{
			addCoordinateWithoutSort(values[i]);
		}
		Collections.sort( naxis );
	}
	
	public void addCoordinate(int value)
	{
		if(addCoordinateWithoutSort( value ))
		{
			Collections.sort(naxis);	
		}
	}
	
	public boolean addCoordinateWithoutSort(int value)
	{
		Integer index = new Integer(value);
		
		if(!naxis.contains( index ))
		{	
			naxis.add(index);
			return true;
		}
		return false;
	}
	
	/**
	 * Gets a subset of naxis.
	 * @param start
	 * @param end
	 * @return
	 */
	public int[] getRange(int start, int end)
	{
		int sp = getCoordinateIndex(start);
		int ep = getCoordinateIndex(end);
		
		List list = naxis.subList( sp, ep + 1);
		Integer[] values = new Integer[list.size()];
		values = (Integer[]) list.toArray( values );
		
		int[] pos = new int[values.length];
		
		for(int i = 0; i < pos.length ;i++)
		{
			pos[i] = values[i].intValue( );
		}	
		
		return pos;		
	}
	
	/**
	 * It is not going to get the coordinate, but the colId relative to this coordinate.
	 * Gets the colId of the given coordinate point
	 * @param value	 the coordinate point
	 * @return the colId
	 */
	public int getCoordinateIndex(int value)
	{
		int index = naxis.indexOf( new Integer(value) );
		return ( index == -1 ) ? 0 : index;
	}	
	
	public int[] getCoordinates()
	{
		Integer[] columns = new Integer[naxis.size( )];
		naxis.toArray( columns );
		int[] scale = new int[columns.length - 1];
		
		for ( int i = 0; i < columns.length - 1; i++ )
		{
			scale[i] = columns[i + 1].intValue( ) - columns[i].intValue( );
		}
		
		return scale;
	}	
}

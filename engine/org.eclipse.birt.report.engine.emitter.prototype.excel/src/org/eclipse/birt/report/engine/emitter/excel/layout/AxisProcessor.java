package org.eclipse.birt.report.engine.emitter.excel.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AxisProcessor
{	
	private List naxis = new ArrayList();
	
	public AxisProcessor()
	{	
		addCoordinate(0);		
	}	
	
	public void addCoordinates(int[] values)
	{
		for(int i = 0; i < values.length; i++)
		{
			addCoordinate(values[i]);
		}	
	}
	
	public void addCoordinate(int value)
	{
		Integer index = new Integer(value);
		
		if(!naxis.contains( index ))
		{	
			naxis.add(index);
			Collections.sort(naxis);
		}
	}
	
	public int[] getRange(int start, int end)
	{
		int sp = getCoordinate(start);
		int ep = getCoordinate(end);
		
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
	
	public int getCoordinate(int value)
	{
		return naxis.indexOf( new Integer(value) );
	}	
	
	public Rule getRule(int value)
	{
		int start = ((Integer)naxis.get( value )).intValue( );
		int end = ((Integer)naxis.get( value + 1)).intValue( );
		
		return new Rule(start, end - start);
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

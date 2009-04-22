
package org.eclipse.birt.report.designer.internal.ui.util;

import java.util.LinkedList;
import java.util.List;

public class SortMap
{

	private static class Entry
	{
		Object key;
		Object value;
	}

	private LinkedList keyList = new LinkedList( );
	private LinkedList entryList = new LinkedList( );

	public boolean containKey( Object key )
	{
		if ( key == null )
			return false;
		for ( int i = 0; i < keyList.size( ); i++ )
		{
			if ( keyList.get( i ).equals( key ) )
				return true;
		}
		return false;
	}

	public boolean containValue( Object value )
	{
		for ( int i = 0; i < entryList.size( ); i++ )
		{
			if ( ( (Entry) entryList.get( i ) ).value == value )
				return true;
		}
		return false;
	}

	public int getIndexOf( Object key )
	{
		if ( key == null )
			return -1;
		for ( int i = 0; i < keyList.size( ); i++ )
		{
			if ( keyList.get( i ).equals( key ) )
				return i;
		}
		return -1;
	}

	public void putAt( Object key, Object value, int index )
	{
		if ( key == null || value == null )
			return;
		if ( containValue( value )
				|| containValue( key )
				|| index < 0
				|| index > keyList.size( ) + 1 )
			return;
		Entry entry = new Entry( );
		entry.key = key;
		entry.value = value;
		entryList.add( entry );
		keyList.add( index, key );
	}

	public void put( Object key, Object value )
	{
		if ( key == null || value == null )
			return;
		if ( containValue( value ) )
			return;
		if ( containKey( key ) )
		{
			for ( int i = 0; i < entryList.size( ); i++ )
			{
				if ( ( (Entry) entryList.get( i ) ).key.equals( key ) )
					( (Entry) entryList.get( i ) ).value = value;
			}
		}
		else
		{
			Entry entry = new Entry( );
			entry.key = key;
			entry.value = value;
			entryList.add( entry );
			keyList.add( key );
		}
	}

	public void remove( Object key )
	{
		if ( key == null )
			return;
		if ( !containKey( key ) )
			return;
		for ( int i = 0; i < entryList.size( ); i++ )
		{
			if ( ( (Entry) entryList.get( i ) ).key.equals( key ) )
				entryList.remove( i );
		}
		keyList.remove( key );

	}

	public void remove( int index )
	{
		if ( index < 0 || index >= keyList.size( ) )
			return;
		Object key = keyList.get( index );
		for ( int i = 0; i < entryList.size( ); i++ )
		{
			if ( ( (Entry) entryList.get( i ) ).key.equals( key ) )
				entryList.remove( i );
		}
		keyList.remove( key );

	}

	public List getKeyList( )
	{
		return keyList;
	}

	public List getValueList( )
	{
		List valueList = new LinkedList( );
		for ( int i = 0; i < keyList.size( ); i++ )
		{
			valueList.add( getValue( keyList.get( i ) ) );
		}
		return valueList;
	}

	public Object getValue( Object key )
	{
		if ( key == null )
			return null;
		if ( !containKey( key ) )
			return null;
		for ( int i = 0; i < entryList.size( ); i++ )
		{
			if ( ( (Entry) entryList.get( i ) ).key.equals( key ) )
				return ( (Entry) entryList.get( i ) ).value;
		}
		return null;
	}

	public Object getValue( int index )
	{
		if ( index < 0 || index >= keyList.size( ) )
			return null;
		Object key = keyList.get( index );
		for ( int i = 0; i < entryList.size( ); i++ )
		{
			if ( ( (Entry) entryList.get( i ) ).key.equals( key ) )
				return ( (Entry) entryList.get( i ) ).value;
		}
		return null;
	}

	public void clear( )
	{
		keyList.clear( );
		entryList.clear( );
	}

	public int size( )
	{
		return keyList.size( );
	}

}

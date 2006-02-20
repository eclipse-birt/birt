/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Apache - initial API and implementation
 *  Actuate Corporation - changed by Actuate
 *******************************************************************************/
/*

   Copyright 1999-2003  The Apache Software Foundation 

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package org.eclipse.birt.report.engine.css.engine.value;

/**
 * A simple hashtable, not synchronized, with fixed load factor and with
 * equality test made with '=='.
 * 
 * @version $Id: StringMap.java,v 1.2 2005/11/22 09:59:57 wyan Exp $
 */
public class StringMap
{

	/**
	 * The initial capacity
	 */
	protected final static int INITIAL_CAPACITY = 11;

	/**
	 * The underlying array
	 */
	protected Entry[] table;

	/**
	 * The number of entries
	 */
	protected int count;

	/**
	 * Creates a new table.
	 */
	public StringMap( )
	{
		table = new Entry[INITIAL_CAPACITY];
	}

	/**
	 * Creates a copy of the given StringMap object.
	 * 
	 * @param t
	 *            The table to copy.
	 */
	public StringMap( StringMap t )
	{
		count = t.count;
		table = new Entry[t.table.length];
		for ( int i = 0; i < table.length; i++ )
		{
			Entry e = t.table[i];
			Entry n = null;
			if ( e != null )
			{
				n = new Entry( e.hash, e.key, e.value, null );
				table[i] = n;
				e = e.next;
				while ( e != null )
				{
					n.next = new Entry( e.hash, e.key, e.value, null );
					n = n.next;
					e = e.next;
				}
			}
		}
	}

	/**
	 * Gets the value corresponding to the given string.
	 * 
	 * @return the value or null
	 */
	public Object get( String key )
	{
		int hash = key.hashCode( ) & 0x7FFFFFFF;
		int index = hash % table.length;

		for ( Entry e = table[index]; e != null; e = e.next )
		{
			if ( ( e.hash == hash ) && e.key == key )
			{
				return e.value;
			}
		}
		return null;
	}

	/**
	 * Sets a new value for the given variable
	 * 
	 * @return the old value or null
	 */
	public Object put( String key, Object value )
	{
		int hash = key.hashCode( ) & 0x7FFFFFFF;
		int index = hash % table.length;

		for ( Entry e = table[index]; e != null; e = e.next )
		{
			if ( ( e.hash == hash ) && e.key == key )
			{
				Object old = e.value;
				e.value = value;
				return old;
			}
		}

		// The key is not in the hash table
		int len = table.length;
		if ( count++ >= ( len * 3 ) >>> 2 )
		{
			rehash( );
			index = hash % table.length;
		}

		Entry e = new Entry( hash, key, value, table[index] );
		table[index] = e;
		return null;
	}

	/**
	 * Rehash the table
	 */
	protected void rehash( )
	{
		Entry[] oldTable = table;

		table = new Entry[oldTable.length * 2 + 1];

		for ( int i = oldTable.length - 1; i >= 0; i-- )
		{
			for ( Entry old = oldTable[i]; old != null; )
			{
				Entry e = old;
				old = old.next;

				int index = e.hash % table.length;
				e.next = table[index];
				table[index] = e;
			}
		}
	}

	/**
	 * To manage collisions
	 */
	protected static class Entry
	{

		/**
		 * The hash code
		 */
		public int hash;

		/**
		 * The key
		 */
		public String key;

		/**
		 * The value
		 */
		public Object value;

		/**
		 * The next entry
		 */
		public Entry next;

		/**
		 * Creates a new entry
		 */
		public Entry( int hash, String key, Object value, Entry next )
		{
			this.hash = hash;
			this.key = key;
			this.value = value;
			this.next = next;
		}
	}
}

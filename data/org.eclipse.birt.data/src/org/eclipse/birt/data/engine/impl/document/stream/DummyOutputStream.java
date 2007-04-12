
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl.document.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This DummyOutputStream is used to cache user output to Memory.  
 */
public class DummyOutputStream extends OutputStream
{
	private static final int BUFF_SIZE = 4000;
	
	private List cachedByteArray;
	private int currentListIndex;
	private int nextArrayIndex;
	private byte[] currentArray;
	
	DummyOutputStream( )
	{
		this.cachedByteArray = new ArrayList(); 
		this.currentArray = new byte[BUFF_SIZE];
		this.cachedByteArray.add( this.currentArray );
		this.nextArrayIndex = 0;
		this.currentListIndex = 0;
	}
	
	
	public void write( int b ) throws IOException
	{
		if ( this.nextArrayIndex < BUFF_SIZE )
		{
			this.currentArray[this.nextArrayIndex] = (byte)b;
			this.nextArrayIndex ++;
		}else
		{
			this.currentArray = new byte[BUFF_SIZE];
			this.cachedByteArray.add( this.currentArray );
			this.currentListIndex++;
			this.nextArrayIndex = 0;
			this.write( b );
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public byte[] toByteArray()
	{
		byte[] result = new byte[this.currentListIndex*BUFF_SIZE + this.nextArrayIndex];
		for( int i = 0; i < this.cachedByteArray.size( ); i++ )
		{
			byte[] temp = (byte[])this.cachedByteArray.get( i );
			int count = BUFF_SIZE;
			if ( i == this.cachedByteArray.size( )-1)
				count = this.nextArrayIndex;
			for( int j = 0; j < count; j++ )
			{
				result[i*BUFF_SIZE+j] = temp[j];
			}
		}
		return result;
	}
}

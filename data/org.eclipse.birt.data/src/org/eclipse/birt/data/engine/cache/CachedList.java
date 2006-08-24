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

package org.eclipse.birt.data.engine.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import org.eclipse.birt.core.util.IOUtil;


/**
 * A List class providing the service of reading/writing objects from one file
 * when cache is not enough . It makes the reading/writing objects transparent.
 */

public class CachedList extends BasicCachedList
{
	private ICachedObjectCreator creator;
	
	/**
	 * 
	 * 
	 */
	public CachedList( ICachedObjectCreator creator )
	{
		super();
		this.creator = creator;
	}
	
	/**
	 * 
	 * @param list
	 */
	public CachedList( ICachedObjectCreator creator, List list )
	{
		super( list );
		this.creator = creator;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.cache.BasicCachedList#writeObject(java.io.DataOutputStream, java.lang.Object)
	 */
	protected void writeObject(DataOutputStream oos, Object object) throws IOException
	{
		if(object == null)
		{
			IOUtil.writeInt( oos, INT_NULL );
			return;
		}
		ICachedObject cachedObject = (ICachedObject) object;
		Object[] objects = cachedObject.getFieldValues( );
		IOUtil.writeInt( oos, objects.length );
		for(int i=0;i<objects.length;i++)
		{
			IOUtil.writeObject( oos, objects[i] );
		}
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.cache.BasicCachedList#readObject(java.io.DataInputStream)
	 */
	protected Object readObject(DataInputStream dis) throws IOException
	{
		int fieldCount = IOUtil.readInt( dis );
		if ( fieldCount == INT_NULL )
		{
			return null;
		}
		Object[] objects = new Object[fieldCount];
		for(int i=0;i<objects.length;i++)
		{
			objects[i] = IOUtil.readObject( dis );
		}
		return creator.createInstance( objects );
	}

}
/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * 
 */
public class ResultSetUtil
{	 	
	//----------------------service for result object save and load--------------
	
	/**
	 * @param dos
	 * @param resultObject
	 * @param count
	 * @throws DataException
	 * @throws IOException
	 */
	public static void writeResultObject( DataOutputStream dos,
			IResultObject resultObject, int count ) throws DataException,
			IOException
	{
		for ( int i = 1; i <= count; i++ )
			IOUtil.writeObject( dos, resultObject.getFieldValue( i ) );
	}

	/**
	 * @param dis
	 * @param rsMeta
	 * @param count
	 * @return
	 * @throws IOException
	 */
	public static IResultObject readResultObject( DataInputStream dis,
			IResultClass rsMeta, int count ) throws IOException
	{
		Object[] obs = new Object[count];

		for ( int i = 0; i < count; i++ )
			obs[i] = IOUtil.readObject( dis );

		return new ResultObject( rsMeta, obs );
	}
	
}

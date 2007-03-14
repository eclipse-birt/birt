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

package org.eclipse.birt.data.engine.olap.data.util;

import java.io.IOException;

/**
 * A List class providing the service of reading/writing objects from one file
 * when cache is not enough . It makes the reading/writing objects transparent.
 */

public class PrimitiveDiskArray extends BaseDiskArray
{

	private IObjectWriter fieldWriter = null;
	private IObjectReader fieldReader = null;

	/**
	 * @throws IOException 
	 * 
	 * 
	 */
	public PrimitiveDiskArray( ) throws IOException
	{
		super( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.BaseDiskArray#writeObject(java.lang.Object)
	 */
	protected void writeObject( Object object ) throws IOException
	{
		if ( object == null )
		{
			randomAccessFile.writeShort( NULL_VALUE );
			return;
		}
		randomAccessFile.writeShort( NORMAL_VALUE );
		if ( fieldWriter == null )
		{
			fieldWriter = IOUtil.getRandomWriter( DataType.getDataType( object.getClass( ) ) );
			fieldReader = IOUtil.getRandomReader( DataType.getDataType( object.getClass( ) ) );
		}
		fieldWriter.write( randomAccessFile, object );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.BaseDiskArray#readObject()
	 */
	protected Object readObject( ) throws IOException
	{
		short fieldCount = randomAccessFile.readShort( );
		if ( fieldCount == NULL_VALUE )
		{
			return null;
		}

		return fieldReader.read( randomAccessFile );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.util.BaseDiskArray#clear()
	 */
	public void clear( ) throws IOException
	{
		fieldWriter = null;
		fieldReader = null;
		super.clear( );
	}
}


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
package org.eclipse.birt.data.engine.olap.data.document;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * 
 */

public class SimpleRandomAccessObject implements IRandomAccessObject
{
	RandomAccessFile randomAccessFile = null;
	
	public SimpleRandomAccessObject( File file, String mode ) throws FileNotFoundException
	{
		this.randomAccessFile = new RandomAccessFile( file, mode );
	}

	public void close( ) throws IOException
	{
		randomAccessFile.close( );
	}

	public FileDescriptor getFD( ) throws IOException
	{
		return randomAccessFile.getFD( );
	}

	public long getFilePointer( ) throws IOException
	{
		return randomAccessFile.getFilePointer( );
	}

	public long length( ) throws IOException
	{
		return randomAccessFile.length( );
	}

	public int read( byte[] b, int off, int len ) throws IOException
	{
		return randomAccessFile.read( );
	}

	public int read( byte[] b ) throws IOException
	{
		return randomAccessFile.read( b );
	}

	public void seek( long pos ) throws IOException
	{
		randomAccessFile.seek( pos );
	}

	public void setLength( long newLength ) throws IOException
	{
		randomAccessFile.setLength( newLength );
	}

	public void write( byte[] b, int off, int len ) throws IOException
	{
		randomAccessFile.write( b, off, len );
	}

}

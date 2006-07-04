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

package org.eclipse.birt.data.engine.executor.cache.disk;

import java.io.IOException;

import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Provide the service of sorting objects existed in several files. The objects
 * in every file are sorted. It makes the reading objects transparent to
 * DiskMergeSort.
 */
class MergeSortRowFiles implements IRowIterator
{
	private IRowIterator[] subRowIterators = null;
	private MergeSortUtil mergeSortUtil = null;
	private IResultObject[] rowBuffer = null;
	
	/**
	 * @param rowFiles
	 *            The objects in every file are sorted.
	 * @param mergeSortUtil
	 */
	MergeSortRowFiles( IRowIterator[] subRowIterators, MergeSortUtil mergeSortUtil )
	{
		assert subRowIterators != null;
		
		this.subRowIterators = subRowIterators;
		this.mergeSortUtil = mergeSortUtil;
	}
	
	/*
	 * Moves the cursor to the first object in this MergeSortObjectFile object.
	 * 
	 * @see org.eclipse.birt.data.engine.executor.cache.IRowIterator#first()
	 */
	public void reset( )
	{
		for ( int i = 0; i < subRowIterators.length; i++ )
		{
			subRowIterators[i].reset( );
		}
		
		rowBuffer = null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.IRowIterator#next()
	 */
	public IResultObject fetch( ) throws IOException
	{
		int minObjectPos = 0;
		IResultObject resultObject = null;
		
		if ( rowBuffer == null )
		{
			prepareFirstFetch( );
		}
		
		minObjectPos = mergeSortUtil.getMinResultObject( rowBuffer, rowBuffer.length );
		if ( minObjectPos < 0 )
			return null;
		
		resultObject = rowBuffer[minObjectPos];
		rowBuffer[minObjectPos] = subRowIterators[minObjectPos].fetch( );
		
		return resultObject;
	}
	
	/**
	 * @throws IOException
	 */
	private void prepareFirstFetch( ) throws IOException
	{
		rowBuffer = new IResultObject[subRowIterators.length];
		for ( int i = 0; i < subRowIterators.length; i++ )
		{
			rowBuffer[i] = subRowIterators[i].fetch( );
		}
	}
	
	/*
	 * Delete all the files correlated with this MergeSortObjectFile object.
	 * 
	 * @see org.eclipse.birt.data.engine.executor.cache.IRowIterator#close()
	 */
	public void close( )
	{
		for ( int i = 0; i < subRowIterators.length; i++ )
		{
			subRowIterators[i].close( );
		}
		
		subRowIterators = null;
	}

}

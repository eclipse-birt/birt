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

package org.eclipse.birt.data.engine.impl.document;

import java.io.OutputStream;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;

/**
 * 
 */
public class StreamWrapper
{
	private OutputStream streamForResultClass;
	private OutputStream streamForDataSet;
	private OutputStream streamForGroupInfo;
	private OutputStream streamForRowIndexInfo;
	private OutputStream streamForParentIndex;
	private StreamManager manager;
	/**
	 * @param streamForResultClass
	 * @param streamForDataSet
	 * @param streamForGroupInfo
	 * @param streamForRowIndexInfo
	 */
	public StreamWrapper( StreamManager manager,
			OutputStream streamForResultClass, OutputStream streamForGroupInfo,
			OutputStream streamForRowIndexInfo,
			OutputStream streamForParentIndex )
	{
		this.streamForResultClass = streamForResultClass;
		this.streamForGroupInfo = streamForGroupInfo;
		this.streamForRowIndexInfo = streamForRowIndexInfo;
		this.streamForParentIndex = streamForParentIndex;
		this.manager = manager;
	}

	/**
	 * @return
	 */
	public OutputStream getStreamForResultClass( )
	{
		return this.streamForResultClass;
	}

	/**
	 * @return
	 * @throws DataException 
	 */
	public OutputStream getStreamForDataSet( ) throws DataException
	{
		if( this.streamForResultClass!= null && this.streamForDataSet == null )
		{
			this.streamForDataSet = manager.getOutStream( DataEngineContext.DATASET_DATA_STREAM,
					StreamManager.ROOT_STREAM,
					StreamManager.SELF_SCOPE );
		}
		return this.streamForDataSet;
	}

	/**
	 * @return
	 */
	public OutputStream getStreamForGroupInfo( )
	{
		return this.streamForGroupInfo;
	}

	/**
	 * @return
	 */
	public OutputStream getStreamForRowIndexInfo( )
	{
		return streamForRowIndexInfo;
	}
	
	/**
	 * @return
	 */
	public OutputStream getStreamForParentIndex( )
	{
		return streamForParentIndex;
	}

}

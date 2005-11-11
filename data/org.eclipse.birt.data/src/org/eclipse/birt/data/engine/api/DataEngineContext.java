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
package org.eclipse.birt.data.engine.api;

import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineContextImpl;
import org.mozilla.javascript.Scriptable;

/**
 * Define in which context Data Engine is running. The context can be divided
 * into three types: generation, presentation and both. 
 */
public abstract class DataEngineContext
{
	// mode
	protected int mode;
	
	/** three defined mode*/
	public final static int MODE_GENERATION = 1;
	public final static int MODE_PRESENTATION = 2;
	public final static int MODE_GENANDPRESENT = 3;

	/**
	 * @param mode
	 * @param archive
	 * @return an instance of DataEngineContext
	 */
	public static DataEngineContext newInstance( int mode, Scriptable scope,
			Object archive ) throws BirtException
	{
		return new DataEngineContextImpl( mode, scope, archive );
	}

	/**
	 * @return mode of context
	 * @throws DataException 
	 */
	public DataEngineContext( int mode ) throws DataException
	{
		if ( !( mode == MODE_GENERATION || mode == MODE_PRESENTATION || mode == MODE_GENANDPRESENT ) )
			throw new DataException( "invalid mode " + mode );

		this.mode = mode;
	}

	/**
	 * @return
	 */
	public int getMode( )
	{
		return mode;
	}
	
	/**
	 * @return socpe
	 */
	public abstract Scriptable getJavaScriptScope( );
	
	
	/**
	 * According to the paramters of streamID and streamType, an output stream
	 * will be created for it. To make stream close simply, the output stream
	 * needs to be closed by caller, and then caller requires to add buffer
	 * stream layer when it is used.
	 * 
	 * @param streamID
	 * @param subName
	 * @param streamType
	 * @return output stream for specified streamID and streamType
	 */
	public abstract OutputStream getOutputStream( String streamID, String queryName, int streamType );
	
	/**
	 * Determins whether one particula stream exists
	 * 
	 * @param streamID
	 * @param queryName
	 * @param streamType
	 * @return boolean value
	 */
	public abstract boolean isStreamExists( String streamID, String queryName, int streamType );
	
	/**
	 * According to the paramters of streamID and streamType, an input stream
	 * will be created for it. To make stream close simply, the input stream
	 * needs to be closed by caller, and then caller requires to add buffer
	 * stream layer when it is used.
	 * 
	 * @param streamID
	 * @param subName
	 * @param streamType
	 * @return input stream for specified streamID and streamType
	 */
	public abstract InputStream getInputStream( String streamID, String queryName, int streamType );
	
}

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

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.impl.DataEngineContextImpl;
import org.mozilla.javascript.Scriptable;

/**
 * Define in which context Data Engine is running. The context can be divided
 * into three types: generation, presentation and both. 
 */
public abstract class DataEngineContext
{
	/** three defined mode*/
	public final static int MODE_GENERATION = 1;
	public final static int MODE_PRESENTATION = 2;
	public final static int DIRECT_PRESENTATION  = 3;

	/**
	 * When mode is MODE_GENERATION, the output stream of archive will be used.
	 * When mode is MODE_PRESENTATION, the input stream of archive will be used.
	 * When mode is DIRECT_PRESENTATION, the archive will not be used.
	 * 
	 * @param mode
	 * @param scope
	 * @param archive
	 * @return an instance of DataEngineContext
	 */
	public static DataEngineContext newInstance( int mode, Scriptable scope,
			IDocArchiveReader reader, IDocArchiveWriter writer ) throws BirtException
	{
		return new DataEngineContextImpl( mode, scope, reader, writer );
	}

	/**
	 * @return mode
	 */
	public abstract int getMode( );

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
	 * @param subStreamID
	 * @param streamType
	 * @return output stream for specified streamID and streamType
	 */
	public abstract OutputStream getOutputStream( String streamID,
			String subStreamID, int streamType );

	/**
	 * Determins whether one particula stream exists
	 * 
	 * @param streamID
	 * @param subStreamID
	 * @param streamType
	 * @return boolean value
	 */
	public abstract boolean hasStream( String streamID, String subStreamID,
			int streamType );

	/**
	 * According to the paramters of streamID and streamType, an input stream
	 * will be created for it. To make stream close simply, the input stream
	 * needs to be closed by caller, and then caller requires to add buffer
	 * stream layer when it is used.
	 * 
	 * @param streamID
	 * @param subStreamID
	 * @param streamType
	 * @return input stream for specified streamID and streamType
	 */
	public abstract InputStream getInputStream( String streamID,
			String subStreamID, int streamType );

}

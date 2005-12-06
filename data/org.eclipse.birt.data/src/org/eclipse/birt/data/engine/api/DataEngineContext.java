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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.mozilla.javascript.Scriptable;

/**
 * Define in which context Data Engine is running. The context can be divided
 * into three types: generation, presentation and direct presentation. 
 */
public class DataEngineContext
{
	/** three defined mode */
	public final static int MODE_GENERATION = 1;
	public final static int MODE_PRESENTATION = 2;
	public final static int DIRECT_PRESENTATION  = 3;
	
	/** some fields */
	private int mode;
	private Scriptable scope;	
	private IDocArchiveReader reader;
	private IDocArchiveWriter writer;
	
	/** stream id for internal use, don't use it externally */
	public final static int EXPR_VALUE_STREAM = 11;
	public final static int RESULTCLASS_STREAM = 12;
	public final static int GROUP_INFO_STREAM = 13;
	public final static int SUBQUERY_INFO_STREAM = 14;

	/**
	 * When mode is MODE_GENERATION, the writer stream of archive will be used.
	 * When mode is MODE_PRESENTATION, the reader stream of archive will be used.
	 * When mode is DIRECT_PRESENTATION, the archive will not be used.
	 * 
	 * @param mode
	 * @param scope
	 * @param reader
	 * @param writer
	 * @return an instance of DataEngineContext
	 */
	public static DataEngineContext newInstance( int mode, Scriptable scope,
			IDocArchiveReader reader, IDocArchiveWriter writer )
			throws BirtException
	{
		return new DataEngineContext( mode, scope, reader, writer );
	}
	
	/**
	 * @param mode
	 * @param scope
	 * @param reader
	 * @param writer
	 * @throws BirtException
	 */
	private DataEngineContext( int mode, Scriptable scope,
			IDocArchiveReader reader, IDocArchiveWriter writer )
			throws BirtException
	{
		if ( !( mode == MODE_GENERATION || mode == MODE_PRESENTATION || mode == DIRECT_PRESENTATION ) )
			throw new DataException( ResourceConstants.RD_INVALID_MODE );

		if ( writer == null && mode == MODE_GENERATION )
			throw new DataException( ResourceConstants.RD_INVALID_ARCHIVE );
		
		if ( reader == null && mode == MODE_PRESENTATION )
			throw new DataException( ResourceConstants.RD_INVALID_ARCHIVE );
		
		this.mode = mode;
		this.scope = scope;
		this.reader = reader;
		this.writer = writer;
	}

	/** 
	 * @return current context mode
	 */
	public int getMode( )
	{
		return mode;
	}

	/**
	 * @return current top scope
	 */
	public Scriptable getJavaScriptScope( )
	{
		return scope;
	}

	/**
	 * According to the paramters of streamID, subStreamID and streamType, an
	 * output stream will be created for it. To make stream close simply, the
	 * stream needs to be closed by caller, and then caller requires to add
	 * buffer stream layer when needed.
	 * 
	 * @param streamID
	 * @param subStreamID
	 * @param streamType
	 * @return output stream for specified streamID, subStreamID and streamType
	 */
	public OutputStream getOutputStream( String streamID, String subStreamID,
			int streamType ) throws DataException
	{
		assert writer != null;
		
		String relativePath = getPath( streamID, subStreamID, streamType );
		
		try
		{
			OutputStream outputStream = writer.createRandomAccessStream( relativePath );

			if ( outputStream == null )
				throw new DataException( ResourceConstants.RD_SAVE_STREAM_ERROR );

			return outputStream;
		}
		catch (IOException e)
		{
			throw new DataException( ResourceConstants.RD_SAVE_STREAM_ERROR, e );
		}
	}

	/**
	 * Determins whether one particular stream exists
	 * 
	 * @param streamID
	 * @param subStreamID
	 * @param streamType
	 * @return boolean value
	 */
	public boolean hasStream( String streamID, String subStreamID,
			int streamType )
	{
		String relativePath = getPath( streamID, subStreamID, streamType );

		if ( mode == MODE_GENERATION )
			return writer.exists( relativePath );
		else if ( mode == MODE_GENERATION )
			return reader.exists( relativePath );
		else
			return false;
	}

	/**
	 * According to the paramters of streamID, subStreamID and streamType, an
	 * input stream will be created for it. To make stream close simply, the
	 * stream needs to be closed by caller, and then caller requires to add
	 * buffer stream layer when needed.
	 * 
	 * @param streamID
	 * @param subStreamID
	 * @param streamType
	 * @return input stream for specified streamID, subStreamID and streamType
	 */
	public InputStream getInputStream( String streamID, String subStreamID,
			int streamType ) throws DataException
	{
		assert reader != null;

		String relativePath = getPath( streamID, subStreamID, streamType );
		
		try
		{
			InputStream inputStream = reader.getStream( relativePath );
			
			if ( inputStream == null )
				throw new DataException( ResourceConstants.RD_LOAD_STREAM_ERROR );
			
			return inputStream;
		}
		catch (IOException e)
		{
			throw new DataException( ResourceConstants.RD_LOAD_STREAM_ERROR, e );
		}
	}

	/**
	 * @param streamType
	 * @return relative path, notice in reading data from file, directory can
	 *         not be created.
	 */
	private static String getPath( String streamID, String subStreamID, int streamType )
	{
		String relativePath = null;
		
		String streamRoot = "/" + streamID + "/";
		if ( subStreamID != null )
			streamRoot += subStreamID + "/";

		switch ( streamType )
		{
			case EXPR_VALUE_STREAM :
				relativePath = streamRoot + "ExprValue";
				break;
			case RESULTCLASS_STREAM :
				relativePath = streamRoot + "ResultClass";
				break;
			case GROUP_INFO_STREAM :
				relativePath = streamRoot + "GroupInfo";
				break;
			case SUBQUERY_INFO_STREAM :
				relativePath = streamRoot + "SubQueryInfo";
				break;
			default :
				assert false;
		}

		return relativePath;
	}
	

}

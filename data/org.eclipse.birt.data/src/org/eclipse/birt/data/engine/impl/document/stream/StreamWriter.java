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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public class StreamWriter
{

	private StreamID id;
	private HashMap cachedStreams;
	private DataEngineContext context;

	/**
	 * 
	 * @param context
	 * @param id
	 */
	public StreamWriter( DataEngineContext context, StreamID id )
	{
		this.id = id;
		this.cachedStreams = new HashMap( );
		this.context = context;
	}

	/**
	 * 
	 * @param streamID
	 * @return
	 */
	public boolean hasOutputStream( StreamID streamID )
	{
		return this.cachedStreams.get( streamID ) != null;
	}

	/**
	 * 
	 * @param streamType
	 * @return
	 */
	public OutputStream getOutputStream( int streamType )
	{
		assert id != null;

		OutputStream os = new DummyOutputStream( );
		this.cachedStreams.put( new Integer( streamType ), os );
		return os;
	}

	/**
	 * 
	 * @throws DataException
	 */
	public void saveToReportDocument( ) throws DataException
	{
		try
		{
			RAOutputStream raMetaOs = context.getOutputStream( id.getStartStream( ),
					id.getSubQueryStream( ),
					DataEngineContext.META_STREAM );
			DataOutputStream metaOs = new DataOutputStream( raMetaOs );

			if ( context.hasOutStream( id.getStartStream( ),
					id.getSubQueryStream( ),
					DataEngineContext.META_STREAM ) )
				raMetaOs.seek( raMetaOs.length( ) );

			RAOutputStream raMetaIndexOs = context.getOutputStream( id.getStartStream( ),
					id.getSubQueryStream( ),
					DataEngineContext.META_INDEX_STREAM );

			DataOutputStream metaIndexOs = new DataOutputStream( raMetaIndexOs );
			if ( context.hasOutStream( id.getStartStream( ),
					id.getSubQueryStream( ),
					DataEngineContext.META_INDEX_STREAM ) )
				raMetaIndexOs.seek( raMetaIndexOs.length( ) );

			Iterator it = this.cachedStreams.keySet( ).iterator( );
			long offset = raMetaOs.length( );
			while ( it.hasNext( ) )
			{
				Integer streamType = (Integer) it.next( );

				DummyOutputStream dos = (DummyOutputStream) this.cachedStreams.get( streamType );
				byte[] temp = dos.toByteArray( );
				int size = temp.length;

				IOUtil.writeInt( metaIndexOs, streamType.intValue( ) );
				IOUtil.writeLong( metaIndexOs, offset );
				IOUtil.writeInt( metaIndexOs, size );
				offset = offset + size;
				metaOs.write( temp );
			}

			metaOs.close( );
			metaIndexOs.close( );

		}
		catch ( IOException e )
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
	}
}

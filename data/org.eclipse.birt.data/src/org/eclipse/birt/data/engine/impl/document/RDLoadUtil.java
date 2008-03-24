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

import java.io.IOException;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;

/**
 * 
 */
public class RDLoadUtil
{
	/**
	 * @param streamManager
	 * @param streamPos
	 * @param streamScope
	 * @return
	 * @throws DataException
	 */
	public static RDGroupUtil loadGroupUtil(String tempDir, StreamManager streamManager,
			int streamPos, int streamScope ) throws DataException
	{
		RAInputStream stream = streamManager.getInStream( DataEngineContext.GROUP_INFO_STREAM,
				streamPos,
				streamScope );
		
		RAInputStream[] groupStreams = null;
		int gNumber = 0;
		try
		{
			gNumber = IOUtil.readInt( stream );
			groupStreams = new RAInputStream[gNumber];
			long nextOffset = IOUtil.INT_LENGTH;
			for ( int i = 0; i < gNumber; i++ )
			{
				RAInputStream rain = streamManager.getInStream( DataEngineContext.GROUP_INFO_STREAM,
						streamPos,
						streamScope );
				rain.seek( nextOffset );
				groupStreams[i] = rain;
				int asize = IOUtil.readInt( stream );
				stream.seek( nextOffset );
				nextOffset = nextOffset
						+ IOUtil.INT_LENGTH + 2
						* IOUtil.INT_LENGTH * asize;
			}

			stream.close( );
		}
		catch ( IOException e )
		{
			// ignore it
		}
		return new RDGroupUtil( tempDir, gNumber, groupStreams );
	}
	
}

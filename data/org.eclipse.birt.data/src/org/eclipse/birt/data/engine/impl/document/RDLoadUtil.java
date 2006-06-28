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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;

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
	public static RDGroupUtil loadGroupUtil( StreamManager streamManager,
			int streamPos, int streamScope ) throws DataException
	{
		InputStream stream = streamManager.getInStream( DataEngineContext.GROUP_INFO_STREAM,
				streamPos,
				streamScope );
		BufferedInputStream buffStream = new BufferedInputStream( stream );
		RDGroupUtil rdGroupUtil = new RDGroupUtil( buffStream );
		try
		{
			buffStream.close( );
			stream.close( );
		}
		catch ( IOException e )
		{
			// ignore it
		}

		return rdGroupUtil;
	}
	
}

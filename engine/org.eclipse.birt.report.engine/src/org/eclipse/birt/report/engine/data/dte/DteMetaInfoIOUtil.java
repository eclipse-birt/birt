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

package org.eclipse.birt.report.engine.data.dte;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;

public class DteMetaInfoIOUtil
{
	/**
	 * Meta information's version. From version 1, rowId will be stored as a
	 * String by supporting cube result set, while it is Long before.
	 */
	protected final static String VERSION_1 = "__version__1";

	/**
	 * save the metadata into the streams.
	 * 
	 * @param key
	 */
	static public void storeMetaInfo( DataOutputStream dos, String pRsetId,
			String rowId, String queryId, String rsetId ) throws IOException
	{
		IOUtil.writeString( dos, pRsetId );
		IOUtil.writeString( dos, rowId );
		IOUtil.writeString( dos, queryId );
		IOUtil.writeString( dos, rsetId );
	}

	static public void startMetaInfo( DataOutputStream dos ) throws IOException
	{
		IOUtil.writeString( dos, VERSION_1 );
	}

	static public ArrayList loadDteMetaInfo( IDocArchiveReader archive )
			throws IOException
	{
		ArrayList result = new ArrayList( );

		if ( archive.exists( ReportDocumentConstants.DATA_SNAP_META_STREAM ) )
		{
			InputStream in = archive
					.getStream( ReportDocumentConstants.DATA_SNAP_META_STREAM );
			try
			{
				loadDteMetaInfo( result, new DataInputStream( in ) );
			}
			finally
			{
				in.close( );
			}
		}
		else if ( archive.exists( ReportDocumentConstants.DATA_META_STREAM ) )
		{
			InputStream in = archive
					.getStream( ReportDocumentConstants.DATA_META_STREAM );
			try
			{
				loadDteMetaInfo( result, new DataInputStream( in ) );
			}
			finally
			{
				in.close( );
			}
		}
		return result;
	}

	static public void loadDteMetaInfo( ArrayList result, DataInputStream dis )
			throws IOException
	{
		try
		{
			String version = IOUtil.readString( dis );
			boolean version1 = VERSION_1.equals( version );

			String pRsetId;
			String rowId;

			if ( version1 )
			{
				pRsetId = IOUtil.readString( dis );
				rowId = IOUtil.readString( dis );
			}
			else
			{
				pRsetId = version;
				rowId = String.valueOf( IOUtil.readLong( dis ) );
			}
			String queryId = IOUtil.readString( dis );
			String rsetId = IOUtil.readString( dis );

			result.add( new String[]{pRsetId, rowId, queryId, rsetId} );

			while ( true )
			{
				pRsetId = IOUtil.readString( dis );
				if ( version1 )
				{
					rowId = IOUtil.readString( dis );
				}
				else
				{
					rowId = String.valueOf( IOUtil.readLong( dis ) );
				}
				queryId = IOUtil.readString( dis );
				rsetId = IOUtil.readString( dis );
				result.add( new String[]{pRsetId, rowId, queryId, rsetId} );
			}
		}
		catch ( EOFException eofe )
		{
			// we expect that there should be an EOFexception
		}
	}
}

/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.IReportDocument;

/**
 * page hint reader
 *
 * It can support multiple versions.
 *
 */
public class RunStatusReader
{
	protected RAInputStream runStatusStream;
	static protected Logger logger = Logger.getLogger( RunStatusReader.class
			.getName( ) );

	public RunStatusReader( IReportDocument document )
	{
		try
		{
			IDocArchiveReader reader = document.getArchive( );
			runStatusStream = reader
					.getStream( ReportDocumentConstants.RUN_STATUS_STREAM );
		}
		catch ( IOException e )
		{
			logger.log( Level.WARNING,
					"Unable to create stream to read run task status" ); //$NON-NLS-1$
			close( );
		}
	}

	public void close( )
	{
		try
		{
			if ( runStatusStream != null )
			{
				runStatusStream.close( );
				runStatusStream = null;
			}
		}
		catch ( IOException e )
		{
			logger.log( Level.WARNING,
					"Unable to close the stream reading run task status" ); //$NON-NLS-1$
		}
	}

	public String getStuats( )
	{
		if ( runStatusStream == null )
		{
			return null;
		}
		try
		{
			DataInputStream in = new DataInputStream( runStatusStream );
			int errorSize = IOUtil.readInt( in );
			StringBuilder message = new StringBuilder( );
			for ( int i = 0; i < errorSize; i++ )
			{
				message.append( IOUtil.readString( in ) )
				        .append( System.getProperty( "line.separator" ) );
			}
			return message.toString( );
		}
		catch ( IOException e )
		{
			logger.log( Level.WARNING,
			            "Exception occured during reading run task status" ); //$NON-NLS-1$
		}
		return null;
	}

}

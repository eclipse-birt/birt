/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.RenderOption;

public class EmitterUtil
{

	protected static Logger logger = Logger.getLogger( EmitterUtil.class
			.getName( ) );

	public static OutputStream getOuputStream( IEmitterServices services,
			String defaultOutputFile )
	{
		OutputStream out = null;
		Object fd = services.getOption( RenderOption.OUTPUT_FILE_NAME );
		File file = null;
		try
		{
			if ( fd != null )
			{
				file = new File( fd.toString( ) );
				File parent = file.getParentFile( );
				if ( parent != null && !parent.exists( ) )
				{
					parent.mkdirs( );
				}
				out = new BufferedOutputStream( new FileOutputStream( file ) );
			}
		}
		catch ( FileNotFoundException e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
		}

		if ( out == null )
		{
			Object value = services.getOption( RenderOption.OUTPUT_STREAM );
			if ( value != null && value instanceof OutputStream )
			{
				Object closeOnExitValue = services
						.getOption( RenderOption.CLOSE_OUTPUTSTREAM_ON_EXIT );
				boolean closeOnExit = false;
				if ( closeOnExitValue != null
						&& closeOnExitValue instanceof Boolean )
				{
					closeOnExit = (Boolean) closeOnExitValue;
				}
				out = new EmitterOutputStream( (OutputStream) value,
						closeOnExit );
			}
			else
			{
				try
				{
					// FIXME
					file = new File( defaultOutputFile );
					out = new BufferedOutputStream( new FileOutputStream( file ) );
				}
				catch ( FileNotFoundException e )
				{
					// FIXME
					logger.log( Level.SEVERE, e.getMessage( ), e );
				}
			}
		}
		return out;
	}
	
	private static class EmitterOutputStream extends FilterOutputStream
	{
		private boolean closeOutputStreamOnExit;
		
		public EmitterOutputStream( OutputStream out,
				boolean closeOutputStreamOnExit )
		{
			super( out );
			this.closeOutputStreamOnExit = closeOutputStreamOnExit;
		}

		public void close( ) throws IOException
		{
			try
			{
				flush( );
			}
			catch ( IOException ignored )
			{
			}
			if ( closeOutputStreamOnExit )
			{
				out.close( );
			}
		}
	}
}

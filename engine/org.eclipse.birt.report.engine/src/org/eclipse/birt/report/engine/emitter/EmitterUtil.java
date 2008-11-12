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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.util.FlashFile;
import org.eclipse.birt.report.engine.util.SvgFile;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

import com.lowagie.text.Image;

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
					closeOnExit = ( (Boolean) closeOnExitValue ).booleanValue( );
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
	
	public static Image getImage( IImageContent content )
	{
		Image image = null;
		try
		{
			String uri = content.getURI( );
			String mimeType = content.getMIMEType( );
			String extension = content.getExtension( );
			switch ( content.getImageSource( ) )
			{
				case IImageContent.IMAGE_FILE :
					ReportDesignHandle design = content.getReportContent( )
							.getDesign( ).getReportDesign( );
					URL url = design.findResource( uri, IResourceLocator.IMAGE );
					InputStream in = url.openStream( );
					try
					{
						byte[] buffer;
						if ( SvgFile.isSvg( content.getURI( ) ) )
						{
							buffer = SvgFile.transSvgToArray( in );
						}
						else
						{
							ArrayList<Byte> bytes = new ArrayList<Byte>( );
							int data = in.read( );
							while ( data != -1 )
							{
								bytes.add( (byte) data );
								data = in.read( );
							}
							buffer = new byte[bytes.size( )];
							for ( int i = 0; i < buffer.length; i++ )
							{
								buffer[i] = bytes.get( i );
							}
						}
						image = Image.getInstance( buffer );
					}
					catch ( Exception ex )
					{
						logger.log( Level.WARNING, ex.getMessage( ), ex );
					}
					finally
					{
						in.close( );
					}
					break;
				case IImageContent.IMAGE_NAME :
				case IImageContent.IMAGE_EXPRESSION :
					byte[] data = content.getData( );
					in = new ByteArrayInputStream( data );
					if ( SvgFile.isSvg( mimeType, uri, extension ) )
						data = SvgFile.transSvgToArray( in );
					image = Image.getInstance( data );
					break;

				case IImageContent.IMAGE_URL :
					if ( SvgFile.isSvg( uri ) )
					{
						image = Image.getInstance( SvgFile
								.transSvgToArray( uri ) );
					}
					else
					{
						image = Image
								.getInstance( new URL( content.getURI( ) ) );
					}

					break;
				default :
					assert ( false );
			}
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, e.getLocalizedMessage( ) );
		}
		return image;
	}
}

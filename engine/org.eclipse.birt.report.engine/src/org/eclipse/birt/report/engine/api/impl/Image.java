/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.IImage;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.content.IImageItemContent;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.util.FileUtil;

/**
 * image content object
 * 
 * @version $Revision: 1.4 $ $Date: 2005/04/29 03:37:15 $
 */
public class Image implements IImage
{
	protected static Logger logger = Logger.getLogger( Image.class
			.getName( ) );
	protected String id = null;
	
	protected int source = IImage.INVALID_IMAGE;
	
	protected byte[] data = null;
	
	protected InputStream in = null;
	
	protected IReportRunnable runnable;
	
	protected IRenderOption renderOption;
	
	
	/**
	 * the file uri
	 * @param uri
	 */
	public Image(String uri)
	{
		if( uri == null || uri.length( ) == 0 )
		{
			return;
		}
		
		id = uri;
		if(FileUtil.isLocalResource(uri))
		{
			try
			{
				this.in = new BufferedInputStream(new FileInputStream(new File(uri)));
				this.source = IImage.FILE_IMAGE;
			}
			catch (FileNotFoundException e)
			{
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
			
		}
		else
		{
			this.source = IImage.URL_IMAGE;
		}
	}
	
	/**
	 * 
	 * @param data
	 * @param name
	 */
	public Image(byte[] data, String name)
	{
		if( data == null )
		{
			return;
		}
		
		id = name;
		this.data = data;
		this.source = IImage.CUSTOM_IMAGE;
		this.in = new ByteArrayInputStream(this.data);
	}
	

	public Image(IImageItemContent content)
	{
		String imgUri = content.getUri( );
		byte[] imgData = content.getData( );
		
		switch(content.getImageSource())
		{
		case ImageItemDesign.IMAGE_FILE:
			if( imgUri != null )
			{
				try
				{
					in = new BufferedInputStream(new FileInputStream(new File(imgUri)));
					this.id = imgUri;
					this.source = IImage.FILE_IMAGE;
				}
				catch (FileNotFoundException e)
				{
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}
			break;
		case ImageItemDesign.IMAGE_NAME:
			if( imgData != null )
			{
				this.in = new ByteArrayInputStream(imgData);
				this.data = imgData;
				this.source = IImage.DESIGN_IMAGE;
				this.id = imgUri;
			}
			break;
		case ImageItemDesign.IMAGE_EXPRESSION:
			if( imgData != null )
			{
				this.in = new ByteArrayInputStream(imgData);
				this.data = imgData;
				this.source = IImage.CUSTOM_IMAGE;
			}
			break;
		case ImageItemDesign.IMAGE_URI:
			if( imgUri != null )
			{
				this.id = imgUri;
				this.source = IImage.URL_IMAGE;
			}
			break;
		default:
			assert(false);
		}
		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IImage#getID()
	 */
	public String getID()
	{
		return id;
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IImage#getSource()
	 */
	public int getSource()
	{
		return source;
	}
	
	public void setSource(int source)
	{
		this.source = source;
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IImage#getImageData()
	 */
	public byte[] getImageData() throws OutOfMemoryError
	{
		return data;
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IImage#getImageStream()
	 */
	public InputStream getImageStream()
	{
		return in;
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IImage#writeImage(java.io.File)
	 */
	public void writeImage(File dest) throws IOException
	{
		if( source == IImage.INVALID_IMAGE )
		{
			logger.log(Level.SEVERE, "image source {0} is not valid!", id); //$NON-NLS-1$
			return;
		}
		
		InputStream input = null;
		if(in!=null)
		{
			input = in;
		}
		else if(data!=null)
		{
			input = new ByteArrayInputStream( data );
		}
		else
		{
			logger.log(Level.SEVERE, "image source {0} is not found!", id); //$NON-NLS-1$
		}
		if(!dest.exists())
		{
			
			String parent = new File(dest.getAbsolutePath()).getParent();
			File parentDir = new File(parent);
			if(!parentDir.exists())
			{
				parentDir.mkdirs();
			}
			OutputStream output = null;
			try
			{
				output = new BufferedOutputStream( new FileOutputStream( dest ) );
				copyStream(input, output);
			}
			catch(IOException ex)
			{
				logger.log(Level.SEVERE, ex.getMessage(), ex);
			}
			finally
			{
				if( input != null )
				{
					input.close();
				}
				if( output != null )
				{
					output.close();	
				}
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IReportItemPart#getReportRunnable()
	 */
	public IReportRunnable getReportRunnable()
	{
		return runnable;
	}
	
	public void setReportRunnable(IReportRunnable runnable)
	{
		this.runnable = runnable;
	}

	/**
	 * Copies the stream from the source to the target
	 * 
	 * @param src
	 *            the source stream
	 * @param tgt
	 *            the target stream
	 * @throws IOException
	 */
	protected void copyStream( InputStream src, OutputStream tgt )
			throws IOException
	{
		// copy the file content
		byte[] buffer = new byte[1024];
		int size = 0;
		do
		{
			size = src.read( buffer );
			if ( size > 0 )
			{
				tgt.write( buffer, 0, size );
			}
		} while ( size > 0 );
	}
	/**
	 * @return Returns the renderOption.
	 */
	public IRenderOption getRenderOption()
	{
		return renderOption;
	}
	/**
	 * @param renderOption The renderOption to set.
	 */
	public void setRenderOption(IRenderOption renderOption)
	{
		this.renderOption = renderOption;
	}
}
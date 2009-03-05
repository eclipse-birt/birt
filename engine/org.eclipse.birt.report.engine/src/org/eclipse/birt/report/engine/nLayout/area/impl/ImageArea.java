/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.util.HashMap;

import org.eclipse.birt.report.engine.nLayout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.nLayout.area.IImageArea;

public class ImageArea extends AbstractArea implements IImageArea
{

	protected String url;

	protected byte[] data;
	
	protected String extension;
	
	protected String helpText;
	
	protected String mimetype;
	
	protected HashMap<String, String> params;

	public ImageArea( )
	{
		super( );
	}

	public ImageArea( ImageArea area )
	{
		super( area );
		this.url = area.getImageUrl( );
		this.data = area.getImageData( );
	}

	public void setUrl( String url )
	{
		this.url = url;
	}

	public void setData( byte[] data )
	{
		this.data = data;
	}

	public void accept( IAreaVisitor visitor )
	{
		visitor.visitImage( this );
	}

	public byte[] getImageData( )
	{
		return data;
	}

	public String getImageUrl( )
	{
		return url;
	}

	public AbstractArea cloneArea( )
	{
		return new ImageArea( this );
	}

	public String getExtension( )
	{
		return extension;
	}
	
	public void setExtension(String extension)
	{
		this.extension = extension;
	}

	public String getHelpText( )
	{
		return helpText;
	}
	
	public void setHelpText(String helpText)
	{
		this.helpText = helpText;
	}

	public String getMIMEType( )
	{
		return mimetype;
	}
	
	public void setMIMEType(String mimetype)
	{
		this.mimetype = mimetype;
	}
	
	public HashMap<String, String> getParameters( )
	{
		return params;
	}
	
	public void setParameters( HashMap<String, String> params )
	{
		this.params = params;
	}

}

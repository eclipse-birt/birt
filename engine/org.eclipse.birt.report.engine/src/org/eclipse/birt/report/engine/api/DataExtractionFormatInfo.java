/*******************************************************************************
 * Copyright (c)2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.core.framework.IConfigurationElement;


public class DataExtractionFormatInfo
{
	private String format;
	private String id;
	private String mimeType;
	private IConfigurationElement dataExtractionExtension;
	
	public DataExtractionFormatInfo( String id, String format,
			String mimeType, IConfigurationElement dataExtractionExtension )
	{
		this.id = id;
		this.format = format;
		this.mimeType = mimeType;
		this.dataExtractionExtension = dataExtractionExtension;
	}
	
	public String getFormat( )
	{
		return format;
	}
	
	public String getId( )
	{
		return id;
	}
	
	public String getMimeType( )
	{
		return mimeType;
	}
	
	public IConfigurationElement getDataExtractionExtension( )
	{
		return dataExtractionExtension;
	}
}

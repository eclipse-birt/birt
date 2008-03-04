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

package org.eclipse.birt.report.engine.api;

import java.io.OutputStream;

public class DataExtractionOption extends TaskOption
		implements
			IDataExtractionOption
{

	public void setExtension( String extension )
	{
		setOption( EXTENSION, extension );
	}

	public void setOutputFile( String filename )
	{
		setOption( OUTPUT_FILE_NAME, filename );
	}

	public void setOutputFormat( String format )
	{
		setOption( OUTPUT_FORMAT, format );
	}

	public void setOutputStream( OutputStream out )
	{
		setOption( OUTPUT_STREAM, out );
	}

	public String getExtension( )
	{
		return getStringOption( EXTENSION );
	}

	public String getOutputFile( )
	{
		return getStringOption( OUTPUT_FILE_NAME );
	}

	public String getOutputFormat( )
	{
		return getStringOption( OUTPUT_FORMAT );
	}

	public OutputStream getOutputStream( )
	{
		Object value = getOption( OUTPUT_STREAM );
		if ( value instanceof OutputStream )
		{
			return (OutputStream) value;
		}
		return null;
	}
}

/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.data.dte;

import java.io.IOException;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.report.engine.api.InstanceID;

public class DocumentDataSource
{

	IDocArchiveReader dataSource;
	InstanceID[] parents;

	public DocumentDataSource( IDocArchiveReader dataSource,
			InstanceID[] parents )
	{
		this.dataSource = dataSource;
		this.parents = parents;
	}

	public void open( ) throws IOException
	{
		dataSource.open( );
	}

	public void close( ) throws IOException
	{
		dataSource.close( );
	}

	public IDocArchiveReader getDataSource( )
	{
		return dataSource;
	}

	public InstanceID[] getReportletParents( )
	{
		return parents;
	}
}

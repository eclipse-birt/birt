/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.adapter.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;

/**
	 * 
	 */

public class SecurityListener
{

	public SecurityListener( DataRequestSession session )
	{

	}

	public void start( TabularCubeHandle cubeHandle ) throws BirtException
	{
	}

	public void process( DimensionHandle dimHandle, IDatasetIterator iterator )
			throws BirtException
	{

	}

	public void end( ) throws BirtException
	{
	}
}

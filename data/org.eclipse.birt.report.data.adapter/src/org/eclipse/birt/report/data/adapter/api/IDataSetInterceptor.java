/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *
 *************************************************************************
 */
package org.eclipse.birt.report.data.adapter.api;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;

/**
 * Interceptor for one type of data set.
 * 
 *
 */
public interface IDataSetInterceptor
{
	/**
	 * 
	 * @param dsource
	 * @param dset
	 * @param query
	 * @param registedQueries
	 * @param dContext
	 * @param session
	 * @throws BirtException
	 */
	void preDefineDataSet( IBaseDataSourceDesign dsource,
			IBaseDataSetDesign dset, IDataQueryDefinition query,
			IDataQueryDefinition[] registedQueries,
			DataSessionContext dContext, String tempDir,
			IDataSetInterceptorContext interceptorContext )
			throws BirtException;

	/**
	 * release resources
	 * @throws BirtException
	 */
	void close( ) throws BirtException;
}

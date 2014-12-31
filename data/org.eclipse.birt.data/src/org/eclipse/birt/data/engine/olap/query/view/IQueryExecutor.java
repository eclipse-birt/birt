/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.query.view;

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.IBindingValueFetcher;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.driver.IResultSet;


public interface IQueryExecutor
{
	public IResultSet execute( BirtCubeView view, StopSign stopSign, ICube cube, IBindingValueFetcher fetcher )
			throws IOException, BirtException;
	
	public IResultSet executeSubQuery( IResultSet parentResultSet,
			BirtCubeView view, int startingColumnLevelIndex,
			int startingRowLevelIndex ) throws IOException;
}

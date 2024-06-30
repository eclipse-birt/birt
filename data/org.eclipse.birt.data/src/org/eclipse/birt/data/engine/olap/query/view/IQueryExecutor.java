/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
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

public interface IQueryExecutor {
	IResultSet execute(BirtCubeView view, StopSign stopSign, ICube cube, IBindingValueFetcher fetcher)
			throws IOException, BirtException;

	IResultSet executeSubQuery(IResultSet parentResultSet, BirtCubeView view, int startingColumnLevelIndex,
			int startingRowLevelIndex) throws IOException;
}

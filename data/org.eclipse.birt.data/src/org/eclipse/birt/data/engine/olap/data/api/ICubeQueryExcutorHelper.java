/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.data.api;

import java.io.IOException;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.LevelFilter;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.SimpleLevelFilter;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFacttableFilterEvalHelper;

/**
 * 
 */

public interface ICubeQueryExcutorHelper {
	/**
	 * 
	 * @param simpleLevelFilter
	 */
	public void addSimpleLevelFilter(SimpleLevelFilter simpleLevelFilter);

	/**
	 * 
	 * @param levelFilter
	 */
	public void addFilter(LevelFilter levelFilter);

	/**
	 * 
	 * @param evalHelpers
	 */
	public void addMeasureFilter(List<IJSFacttableFilterEvalHelper> evalHelpers);

	/**
	 * 
	 * @param filterEvalHelper
	 */
	public void addJSFilter(IJSFilterHelper filterEvalHelper);

	/**
	 * 
	 * @param filterEvalHelperList
	 */
	public void addJSFilter(List filterEvalHelperList);

	/**
	 * 
	 * @param stopSign
	 * @return
	 * @throws IOException
	 * @throws BirtOlapException
	 * @throws BirtException
	 */
	public IAggregationResultSet[] execute(AggregationDefinition[] aggregation, StopSign stopSign)
			throws DataException, IOException, BirtException;

	/**
	 * 
	 *
	 */
	public void clear();

	/**
	 * 
	 *
	 */
	public void close();
}

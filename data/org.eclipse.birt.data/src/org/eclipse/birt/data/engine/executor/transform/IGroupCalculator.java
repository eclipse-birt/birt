/*
 *************************************************************************
 * Copyright (c) 2004, 2011 Actuate Corporation.
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
 *
 *************************************************************************
 */

package org.eclipse.birt.data.engine.executor.transform;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.aggregation.IProgressiveAggregationHelper;
import org.eclipse.birt.data.engine.executor.cache.RowResultSet;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.odi.IResultObject;

public interface IGroupCalculator {
	void registerPreviousResultObject(IResultObject previous);

	void registerCurrentResultObject(IResultObject current);

	void registerNextResultObject(RowResultSet rowResultSet) throws DataException;

	void next(int rowId) throws DataException;

	int getStartingGroup() throws DataException;

	int getEndingGroup() throws DataException;

	void close() throws DataException;

	void doSave(StreamManager manager) throws DataException;

	void setAggrHelper(IProgressiveAggregationHelper aggrHelper) throws DataException;

	boolean isAggrAtIndexAvailable(String aggrName, int currentIndex) throws DataException;

	Integer[] getGroupInstanceIndex();
}

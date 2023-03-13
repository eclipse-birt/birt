/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

package org.eclipse.birt.data.engine.executor.transform.pass;

import java.util.Arrays;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.OdiResultSetWrapper;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.impl.DataEngineSession;

public class NoRecalculatePassManager extends PassManager {

	public static void populateResultSet(ResultSetPopulator populator, OdiResultSetWrapper odaResultSet,
			DataEngineSession session) throws DataException {
		new NoRecalculatePassManager(populator).pass(odaResultSet);
	}

	private NoRecalculatePassManager(ResultSetPopulator populator) {
		super(populator);
	}

	@Override
	protected void prepareQueryResultSet() throws DataException {
		populator.getExpressionProcessor().setDataSetMode(false);
		ResultSetProcessUtil.doPopulateNoUpdateAggrFiltering(populator, iccState, computedColumnHelper, filterByRow,
				psController, Arrays.asList(populator.getQuery().getOrdering()));
	}
}

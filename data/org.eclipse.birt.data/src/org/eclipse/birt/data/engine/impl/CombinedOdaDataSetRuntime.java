/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl;

import java.util.Set;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.ICombinedOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.core.DataException;

public class CombinedOdaDataSetRuntime extends OdaDataSetRuntime {
	private DataSetRuntime[] dataSetRuntimes;

	public CombinedOdaDataSetRuntime(ICombinedOdaDataSetDesign dataSet, IQueryExecutor executor,
			DataEngineSession session) throws DataException {
		this((IOdaDataSetDesign) dataSet, executor, session);
	}

	private CombinedOdaDataSetRuntime(IOdaDataSetDesign dataSet, IQueryExecutor executor, DataEngineSession session)
			throws DataException {
		super(dataSet, executor, session);
		ICombinedOdaDataSetDesign design = (ICombinedOdaDataSetDesign) dataSet;
		Set<IOdaDataSetDesign> childDesigns = design.getDataSetDesigns();
		dataSetRuntimes = new DataSetRuntime[childDesigns.size()];
		int i = 0;
		for (IBaseDataSetDesign childDesign : childDesigns) {
			dataSetRuntimes[i++] = DataSetRuntime.newInstance(childDesign, queryExecutor, session);
		}
	}

	public String getQueryText(String dataSetName) {
		for (DataSetRuntime dataSetRuntime : dataSetRuntimes) {
			if (dataSetRuntime.getName().equals(dataSetName)) {
				return dataSetRuntime.getQueryText();
			}
		}
		return null;
	}

	@Override
	public void beforeOpen() throws DataException {
		for (DataSetRuntime dataSetRuntime : dataSetRuntimes) {
			dataSetRuntime.beforeOpen();
		}
	}

	@Override
	public void beforeClose() throws DataException {
		for (DataSetRuntime dataSetRuntime : dataSetRuntimes) {
			dataSetRuntime.beforeClose();
		}
	}

}

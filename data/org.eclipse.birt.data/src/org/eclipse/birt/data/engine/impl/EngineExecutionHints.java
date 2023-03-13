/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
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

package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.ICacheable;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.core.DataException;

/**
 *
 *
 */
public class EngineExecutionHints implements IEngineExecutionHints {
	private Set cachedDataSetNames;
	private List<IDataQueryDefinition> queryDefns = new ArrayList<>();

	/**
	 *
	 */
	EngineExecutionHints() {
		this.cachedDataSetNames = new HashSet();
	}

	/**
	 *
	 * @param dataEngine
	 * @param queryDefns
	 * @throws DataException
	 */
	void populateCachedDataSets(DataEngineImpl dataEngine, IDataQueryDefinition[] qds) throws DataException {
		if (qds != null) {
			queryDefns.addAll(Arrays.asList(qds));
			List temp = new ArrayList();
			List temp2 = new ArrayList();

			this.cachedDataSetNames.clear();

			for (IDataQueryDefinition query : queryDefns) {
				if (query instanceof IQueryDefinition) {
					IQueryDefinition qd = (IQueryDefinition) query;
					String dataSetName = qd.getDataSetName();
					if (dataSetName != null) {
						IBaseDataSetDesign design = dataEngine.getDataSetDesign(dataSetName);

						if (design instanceof IScriptDataSetDesign) {
							continue;
						}

						if (design instanceof ICacheable) {
							DataSetDesignHelper.populateDataSetNames(dataEngine.getDataSetDesign(dataSetName),
									dataEngine, temp2);
						}

						if (qd.getParentQuery() != null && qd.getInputParamBindings().size() == 0) {
							for (int i = 0; i < temp2.size(); i++) {
								if (((BaseDataSetDesign) dataEngine.getDataSetDesign(temp2.get(i).toString()))
										.needCache()) {
									this.cachedDataSetNames.add(temp2.get(i));
								}
							}
						}
						for (int i = 0; i < temp2.size(); i++) {
							if (((BaseDataSetDesign) dataEngine.getDataSetDesign(temp2.get(i).toString()))
									.needCache()) {
								temp.add(temp2.get(i));
							}
						}
					}
				}
				temp2.clear();
			}

			Set tempSet = new HashSet();
			for (int i = 0; i < temp.size(); i++) {
				if (tempSet.contains(temp.get(i))) {
					this.cachedDataSetNames.add(temp.get(i));
				} else {
					tempSet.add(temp.get(i));
				}
			}

			// The query filter's type is Only supported in extension, so data set should
			// not be cached anymore.
			for (IDataQueryDefinition query : queryDefns) {
				if (query instanceof IQueryDefinition) {
					IQueryDefinition q = (IQueryDefinition) query;
					String dataSetName = q.getDataSetName();
					if (dataSetName != null && this.cachedDataSetNames.contains(dataSetName)) {
						IBaseDataSetDesign dataSet = dataEngine.getDataSetDesign(dataSetName);
						if (dataSet instanceof IOdaDataSetDesign) {
							IBaseDataSourceDesign source = dataEngine.getDataSourceDesign(dataSet.getDataSourceName());
							boolean supportInExtensionOnly = FilterPrepareUtil.containsExternalFilter(
									((IQueryDefinition) query).getFilters(),
									((IOdaDataSetDesign) dataSet).getExtensionID(),
									((IOdaDataSourceDesign) source).getExtensionID());
							if (supportInExtensionOnly) {
								this.cachedDataSetNames.remove(dataSet.getName());
							}
						}
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.impl.IQueryExecutionHints#needCacheDataSet(java.
	 * lang.String)
	 */
	@Override
	public boolean needCacheDataSet(String dataSetName) {
		return this.cachedDataSetNames.contains(dataSetName);
	}

}

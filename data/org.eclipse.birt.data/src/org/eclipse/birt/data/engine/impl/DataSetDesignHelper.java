/**************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
 **************************************************************************/
package org.eclipse.birt.data.engine.impl;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSourceDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;

public class DataSetDesignHelper {

	protected static Logger logger = Logger.getLogger(DataSetDesignHelper.class.getName());

	public static void vailidateDataSetDesign(IBaseDataSetDesign design, Map dataSources) throws DataException {
		if (!(design instanceof IJointDataSetDesign)) {
			// Sanity check: a data set must have a data source with the proper
			// type, and the data source must have be defined
			String dataSourceName = design.getDataSourceName();
			BaseDataSourceDesign dsource = (BaseDataSourceDesign) dataSources.get(dataSourceName);
			if (dsource == null) {
				DataException e = new DataException(ResourceConstants.UNDEFINED_DATA_SOURCE, dataSourceName);
				logger.logp(Level.WARNING, DataEngineImpl.class.getName(), "defineDataSet",
						"Data source {" + dataSourceName + "} is not defined", e);
				throw e;
			}
		}
	}

	public static DataSetRuntime createExtenalInstance(IBaseDataSetDesign dataSetDefn, IQueryExecutor queryExecutor,
			DataEngineSession session) {
		return null;
	}

	public static IPreparedQuery createPreparedQueryInstance(IBaseDataSetDesign des, DataEngineImpl dataEngine,
			IQueryDefinition queryDefn, Map appContext) throws DataException {
		return null;
	}

	public static IBaseDataSetDesign createAdapter(IBaseDataSetDesign dataSetDesign) {
		return null;
	}

	public static IResultMetaData getResultMetaData(IBaseQueryDefinition baseQueryDefn, IQuery odiQuery)
			throws DataException {
		return null;
	}

	public static IResultClass getResultClass(IQuery odiQuery) {
		return null;
	}

	public static void populateDataSetNames(IBaseDataSetDesign design, DataEngineImpl engine, List names)
			throws DataException {
		if (design == null) {
			return;
		}
		names.add(design.getName());
		if (design instanceof IJointDataSetDesign) {
			IJointDataSetDesign jointDesign = (IJointDataSetDesign) design;

			populateDataSetNames(engine.getDataSetDesign(jointDesign.getLeftDataSetDesignQulifiedName()), engine,
					names);
			populateDataSetNames(engine.getDataSetDesign(jointDesign.getRightDataSetDesignQulifiedName()), engine,
					names);
		}
	}
}

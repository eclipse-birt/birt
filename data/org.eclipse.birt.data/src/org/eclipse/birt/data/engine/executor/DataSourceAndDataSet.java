/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.data.engine.executor;

import java.util.Collection;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;

/**
 * Wrap the design of data source and data set. This class will determins
 * whether the equality of two data sets, which will be used if data set needs
 * to be put into map. To ensure it works correctly, a comprehensive junit test
 * case should be designed for it.
 */
public class DataSourceAndDataSet {
	/** current data source and data set for cache */
	private IBaseDataSourceDesign dataSourceDesign;
	private IBaseDataSetDesign dataSetDesign;
	private Collection paramterHints;
	private String cacheScopeID;
	private boolean enableSamplePreview = false;

	/**
	 * @param dataSourceDesign
	 * @param dataSetDesign
	 * @return
	 */
	public static DataSourceAndDataSet newInstance(IBaseDataSourceDesign dataSourceDesign,
			IBaseDataSetDesign dataSetDesign, Collection paramterHints, String cacheScopeID,
			boolean enableSamplePreview) {
		DataSourceAndDataSet dataSourceAndSet = new DataSourceAndDataSet();
		dataSourceAndSet.dataSourceDesign = dataSourceDesign;
		dataSourceAndSet.dataSetDesign = dataSetDesign;
		dataSourceAndSet.paramterHints = paramterHints;
		dataSourceAndSet.cacheScopeID = cacheScopeID;
		dataSourceAndSet.enableSamplePreview = enableSamplePreview;
		return dataSourceAndSet;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((dataSourceDesign == null) ? 0 : dataSourceDesign.getName().hashCode());
		result = prime * result + ((dataSetDesign == null) ? 0 : dataSetDesign.getName().hashCode());
		result = prime * result + ((cacheScopeID == null) ? 0 : cacheScopeID.hashCode());

		return result;
	}

	/**
	 *
	 *
	 * @param obj
	 * @param considerParam
	 * @return
	 */
	public boolean isDataSourceDataSetEqual(DataSourceAndDataSet obj, boolean considerParam) {
		IBaseDataSourceDesign dataSourceDesign2 = obj.dataSourceDesign;
		IBaseDataSetDesign dataSetDesign2 = obj.dataSetDesign;
		Collection paramterHints2 = obj.paramterHints;

		if (this.dataSourceDesign == dataSourceDesign2) {
			if (this.dataSetDesign == dataSetDesign2) {
				if (!considerParam) {
					return true;
				}

				if (ComparatorUtil.isEqualParameterHints(this.paramterHints, paramterHints2)) {
					return true;
				}
			} else if (this.dataSetDesign == null || dataSetDesign2 == null) {
				return false;
			}
		} else if (this.dataSourceDesign == null || dataSourceDesign2 == null) {
			return false;
		} else if ((this.dataSetDesign != dataSetDesign2) && (this.dataSetDesign == null || dataSetDesign2 == null)) {
			return false;
		}

		// data source compare
		// data set compare
		if (!isEqualDataSourceDesign(dataSourceDesign, dataSourceDesign2) || !isEqualDataSetDesign(dataSetDesign, dataSetDesign2)) {
			return false;
		}

		if (!considerParam) {
			return true;
		}

		// parameter bindings compare
		if (!ComparatorUtil.isEqualParameterHints(this.paramterHints, paramterHints2)) {
			return false;
		}

		return true;

	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof DataSourceAndDataSet)) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		DataSourceAndDataSet candidate = (DataSourceAndDataSet) obj;

		// If not managed by cache scope id
		if (this.cacheScopeID == null || candidate.cacheScopeID == null) {
			return this.isDataSourceDataSetEqual((DataSourceAndDataSet) obj, true);
		}

		// If managed by cache scope id
		if (!this.enableSamplePreview) {
			if (!this.cacheScopeID.equals(candidate.cacheScopeID)) {
				return false;
			}
			return this.isDataSourceDataSetEqual((DataSourceAndDataSet) obj, true);
		}
		// If managed by cache scope id, we only evaluate the cache scope ID.
		// If a cache has a scope id, then there is one and only one instance of cache
		// for each Data Set exist in
		// cache map. In that case, the clean of cache is managed by
		return this.cacheScopeID.equals(candidate.cacheScopeID);
	}

	/**
	 * compare data source design
	 *
	 * @param dataSourceDesign
	 * @param dataSourceDesign2
	 * @return
	 */
	private boolean isEqualDataSourceDesign(IBaseDataSourceDesign dataSourceDesign,
			IBaseDataSourceDesign dataSourceDesign2) {
		return DataSourceDesignComparator.isEqualDataSourceDesign(dataSourceDesign, dataSourceDesign2);
	}

	/**
	 * compare data set design
	 *
	 * @param dataSetDesign
	 * @param dataSetDesign2
	 * @return
	 */
	private boolean isEqualDataSetDesign(IBaseDataSetDesign dataSetDesign, IBaseDataSetDesign dataSetDesign2) {
		return DataSetDesignComparator.isEqualDataSetDesign(dataSetDesign, dataSetDesign2);
	}

	public String getCacheScopeID() {
		return this.cacheScopeID;
	}

}

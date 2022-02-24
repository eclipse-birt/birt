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

public class DataSourceAndDataSetNames {
	private String dataSourceName;
	private String dataSetName;

	public DataSourceAndDataSetNames(String dataSource, String dataSet) {
		super();
		this.dataSourceName = dataSource;
		this.dataSetName = dataSet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataSetName == null) ? 0 : dataSetName.hashCode());
		result = prime * result + ((dataSourceName == null) ? 0 : dataSourceName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataSourceAndDataSetNames other = (DataSourceAndDataSetNames) obj;
		if (dataSetName == null) {
			if (other.dataSetName != null)
				return false;
		} else if (!dataSetName.equals(other.dataSetName))
			return false;
		if (dataSourceName == null) {
			if (other.dataSourceName != null)
				return false;
		} else if (!dataSourceName.equals(other.dataSourceName))
			return false;
		return true;
	}

}

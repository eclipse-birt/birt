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

import java.util.Objects;

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
		return Objects.hash(dataSetName, dataSourceName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		DataSourceAndDataSetNames other = (DataSourceAndDataSetNames) obj;
		if (!Objects.equals(dataSetName, other.dataSetName)) {
			return false;
		}
		if (!Objects.equals(dataSourceName, other.dataSourceName)) {
			return false;
		}
		return true;
	}

}

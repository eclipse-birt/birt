/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.core.model.views.data;

import org.eclipse.birt.report.model.api.DataSetHandle;

/**
 * Prosents the column nodes on data explorer view.
 */

public class DataSetSchemaModel {

	private DataSetHandle dataSetHandle;

	private String schemaName;

	/**
	 * Constructor
	 * 
	 * @param dataSetHandle
	 * @param schemaName
	 */
	public DataSetSchemaModel(DataSetHandle dataSetHandle, String schemaName) {
		this.dataSetHandle = dataSetHandle;
		this.schemaName = schemaName;
	}

	/**
	 * Get dataset handle
	 * 
	 * @return Data set handle
	 */
	public DataSetHandle getDataSetHandle() {
		return dataSetHandle;
	}

	/**
	 * Get schema name
	 * 
	 * @return Returns the schmma name.
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return schemaName;
	}
}
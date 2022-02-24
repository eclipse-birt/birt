/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import java.io.Serializable;

/**
 * TODO: Please document
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/05 07:24:57 $
 */

public class Column implements Serializable {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1367008141750802405L;
	private String name = null;
	private String DbType = null;
	private String schemaName = null;
	private String tableName = null;

	/**
	 *  
	 */
	public Column() {
		super();
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the dbType.
	 */
	public String getDbType() {
		return DbType;
	}

	/**
	 * @param dbType The dbType to set.
	 */
	public void setDbType(String dbType) {
		DbType = dbType;
	}

	/**
	 * @return Returns the schemaName.
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * @param schemaName The schemaName to set.
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	/**
	 * @return Returns the tableName.
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName The tableName to set.
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}

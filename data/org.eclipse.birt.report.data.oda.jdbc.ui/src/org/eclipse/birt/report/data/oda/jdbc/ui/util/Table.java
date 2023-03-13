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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * TODO: Please document
 *
 * @version $Revision: 1.2 $ $Date: 2007/01/05 07:24:54 $
 */

public class Table implements Serializable {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 8386854614853850187L;
	private String name = null;
	private String type = null;
	private ArrayList columns = null;
	private Schema schema = null;

	/**
	 *
	 */
	public Table(Schema schema) {
		super();
		this.schema = schema;
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
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return Returns the columns.
	 * @throws SQLException
	 */
	public ArrayList getColumns() throws SQLException {
		if (columns == null) {
			retrieveColumns();
		}
		return columns;
	}

	private synchronized void retrieveColumns() throws SQLException {
		if (columns == null) {
			columns = new ArrayList();
			ResultSet resultSet = schema.getMetaData().getMetaData().getColumns(schema.getMetaData().getCatalogname(),
					schema.getName(), getName(), null);
			while (resultSet.next()) {
				Column column = new Column();
				column.setName(resultSet.getString("COLUMN_NAME"));//$NON-NLS-1$
				columns.add(column);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		columns.clear();
		super.finalize();
	}
}

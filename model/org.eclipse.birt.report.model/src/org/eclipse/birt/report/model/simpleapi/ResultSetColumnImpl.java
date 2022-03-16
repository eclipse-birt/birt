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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.simpleapi.IResultSetColumn;

/**
 *
 * Implements of ResultSetColumn.
 *
 */

public class ResultSetColumnImpl extends Structure implements IResultSetColumn {

	/**
	 * ResultSetColumn instance.
	 */

	private ResultSetColumn column;

	/**
	 * Constructor
	 *
	 */

	public ResultSetColumnImpl() {
		super(null);
		this.column = createResultSetColumn();
	}

	/**
	 * Constructor
	 *
	 * @param columnHandle
	 */

	public ResultSetColumnImpl(ResultSetColumnHandle columnHandle) {
		super(columnHandle);

		if (columnHandle == null) {
			this.column = createResultSetColumn();
		} else {
			structureHandle = columnHandle;
			this.column = (ResultSetColumn) columnHandle.getStructure();
		}
	}

	/**
	 * Create instance of <code>ResultSetColumn</code>
	 *
	 * @return instance
	 */

	private ResultSetColumn createResultSetColumn() {
		ResultSetColumn c = new ResultSetColumn();
		return c;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.api.script.element.IResultSetColumn#
	 * getColumnDataType()
	 */

	@Override
	public String getColumnDataType() {
		return column.getDataType();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IResultSetColumn#getName ()
	 */

	@Override
	public String getName() {
		return column.getColumnName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.api.script.element.IResultSetColumn#
	 * getNativeDataType()
	 */

	@Override
	public Integer getNativeDataType() {
		return column.getNativeDataType();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.api.script.element.IResultSetColumn#
	 * getPosition()
	 */

	@Override
	public Integer getPosition() {
		return column.getPosition();
	}

}

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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.element.IResultSetColumn;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

/**
 * Implements of ResultSetColumn.
 */

public class ResultSetColumnImpl implements IResultSetColumn {

	/**
	 * IResultSetColumn instance.
	 */

	private org.eclipse.birt.report.model.api.simpleapi.IResultSetColumn resultSetColumnImpl;

	/**
	 * Constructor
	 */

	public ResultSetColumnImpl() {
		resultSetColumnImpl = SimpleElementFactory.getInstance().createResultSetColumn();
	}

	/**
	 * Constructor
	 *
	 * @param columnHandle
	 */

	public ResultSetColumnImpl(ResultSetColumnHandle columnHandle) {
		resultSetColumnImpl = SimpleElementFactory.getInstance().createResultSetColumn(columnHandle);
	}

	/**
	 * Constructor
	 *
	 * @param columnHandle
	 */

	public ResultSetColumnImpl(org.eclipse.birt.report.model.api.simpleapi.IResultSetColumn resultSetColumn) {
		resultSetColumnImpl = resultSetColumn;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IResultSetColumn#
	 * getColumnDataType()
	 */

	@Override
	public String getColumnDataType() {
		return resultSetColumnImpl.getColumnDataType();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IResultSetColumn#getName()
	 */

	@Override
	public String getName() {
		return resultSetColumnImpl.getName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IResultSetColumn#
	 * getNativeDataType()
	 */

	@Override
	public Integer getNativeDataType() {
		return resultSetColumnImpl.getNativeDataType();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IResultSetColumn#
	 * getPosition()
	 */

	@Override
	public Integer getPosition() {
		return resultSetColumnImpl.getPosition();
	}

}

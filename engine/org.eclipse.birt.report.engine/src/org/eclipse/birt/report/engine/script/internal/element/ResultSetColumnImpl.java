/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public String getColumnDataType() {
		return resultSetColumnImpl.getColumnDataType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IResultSetColumn#getName()
	 */

	public String getName() {
		return resultSetColumnImpl.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IResultSetColumn#
	 * getNativeDataType()
	 */

	public Integer getNativeDataType() {
		return resultSetColumnImpl.getNativeDataType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IResultSetColumn#
	 * getPosition()
	 */

	public Integer getPosition() {
		return resultSetColumnImpl.getPosition();
	}

}

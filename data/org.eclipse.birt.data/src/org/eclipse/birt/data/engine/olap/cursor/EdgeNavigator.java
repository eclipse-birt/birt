/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.cursor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.olap.OLAPException;

import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.driver.IEdgeAxis;

/**
 *
 * An EdgeNavigator maintains a cursor pointing to the Edge object. It will
 * navigate along the edge.
 *
 */
class EdgeNavigator implements INavigator {

	private IRowDataAccessor dataAccessor;
	private IAggregationResultSet rs;
	private List warnings;

	EdgeNavigator(IEdgeAxis axis) {
		this.dataAccessor = axis.getRowDataAccessor();
		this.rs = axis.getQueryResultSet();
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#afterLast()
	 */
	@Override
	public void afterLast() throws OLAPException {
		dataAccessor.edge_afterLast();
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#beforeFirst()
	 */
	@Override
	public void beforeFirst() throws OLAPException {
		dataAccessor.edge_beforeFirst();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#close()
	 */
	@Override
	public void close() throws OLAPException {
		try {
			this.rs.close();
		} catch (IOException e) {
			throw new OLAPException(e.getLocalizedMessage());
		}
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#first()
	 */
	@Override
	public boolean first() throws OLAPException {
		return dataAccessor.edge_first();
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#getExtend()
	 */
	@Override
	public long getExtend() {
		return 0;
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#getPosition()
	 */
	@Override
	public long getPosition() throws OLAPException {
		return dataAccessor.getEdgePostion();
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#getType()
	 */
	@Override
	public int getType() {
		return 0;
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#isAfterLast()
	 */
	@Override
	public boolean isAfterLast() throws OLAPException {
		return this.dataAccessor.edge_isAfterLast();
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#isBeforeFirst()
	 */
	@Override
	public boolean isBeforeFirst() {
		return this.dataAccessor.edge_isBeforeFirst();
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#isFirst()
	 */
	@Override
	public boolean isFirst() throws OLAPException {
		return this.dataAccessor.edge_isFirst();
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#isLast()
	 */
	@Override
	public boolean isLast() throws OLAPException {
		return this.dataAccessor.edge_isLast();
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#last()
	 */
	@Override
	public boolean last() throws OLAPException {
		return this.dataAccessor.edge_last();
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#next()
	 */
	@Override
	public boolean next() throws OLAPException {
		return this.dataAccessor.edge_next();
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#previous()
	 */
	@Override
	public boolean previous() throws OLAPException {
		return this.dataAccessor.edge_previous();
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#relative(int)
	 */
	@Override
	public boolean relative(int arg0) throws OLAPException {
		return this.dataAccessor.edge_relative(arg0);
	}

	/*
	 * @see org.eclipse.birt.data.jolap.cursor.INavigator#setPosition(long)
	 */
	@Override
	public void setPosition(long position) throws OLAPException {
		this.dataAccessor.edge_setPostion(position);
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#clearWarnings()
	 */
	@Override
	public void clearWarnings() throws OLAPException {
		if (warnings != null) {
			this.warnings.clear();
		}
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#getWarnings()
	 */
	@Override
	public Collection getWarnings() throws OLAPException {
		return warnings == null ? new ArrayList() : warnings;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.cursor.INavigator#synchronizedPages(int)
	 */
	@Override
	public void synchronizedPages(int position) {
		dataAccessor.sychronizedWithPage(position);
	}
}

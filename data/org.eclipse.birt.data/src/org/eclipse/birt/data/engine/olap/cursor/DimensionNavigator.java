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

import java.util.ArrayList;
import java.util.Collection;

import jakarta.olap.OLAPException;

import org.eclipse.birt.data.engine.olap.driver.DimensionAxis;

/**
 * A dimensionNavigator maintains a cursor pointing to the dimension result set
 * object.
 *
 */
class DimensionNavigator implements INavigator {
	private DimensionAxis dimensionAxis;

	/**
	 *
	 * @param dimensionAxis
	 */
	DimensionNavigator(DimensionAxis dimensionAxis) {
		this.dimensionAxis = dimensionAxis;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#next()
	 */
	@Override
	public boolean next() throws OLAPException {
		return dimensionAxis.next();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#previous()
	 */
	@Override
	public boolean previous() throws OLAPException {
		return dimensionAxis.previous();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#relative(int)
	 */
	@Override
	public boolean relative(int arg0) throws OLAPException {
		return dimensionAxis.relative(arg0);
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#first()
	 */
	@Override
	public boolean first() throws OLAPException {
		return dimensionAxis.first();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#last()
	 */
	@Override
	public boolean last() throws OLAPException {
		return dimensionAxis.last();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#isBeforeFirst()
	 */
	@Override
	public boolean isBeforeFirst() {
		return dimensionAxis.isBeforeFirst();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#isAfterLast()
	 */
	@Override
	public boolean isAfterLast() throws OLAPException {
		return dimensionAxis.isAfterLast();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#isFirst()
	 */
	@Override
	public boolean isFirst() throws OLAPException {
		return dimensionAxis.isFirst();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#isLast()
	 */
	@Override
	public boolean isLast() throws OLAPException {
		return dimensionAxis.isLast();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#afterLast()
	 */
	@Override
	public void afterLast() throws OLAPException {
		dimensionAxis.afterLast();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#beforeFirst()
	 */
	@Override
	public void beforeFirst() throws OLAPException {
		dimensionAxis.beforeFirst();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#setPosition(long)
	 */
	@Override
	public void setPosition(long position) throws OLAPException {
		dimensionAxis.setPosition(position);
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#getPosition()
	 */
	@Override
	public long getPosition() throws OLAPException {
		return dimensionAxis.getPosition();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#close()
	 */
	@Override
	public void close() throws OLAPException {
		dimensionAxis.close();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#getExtend()
	 */
	@Override
	public long getExtend() {
		return dimensionAxis.getExtend();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#getType()
	 */
	@Override
	public int getType() {
		return dimensionAxis.getType();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#clearWarnings()
	 */
	@Override
	public void clearWarnings() throws OLAPException {
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#getWarnings()
	 */
	@Override
	public Collection getWarnings() throws OLAPException {
		return new ArrayList();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#setFetchSize(int)
	 */
	public void setFetchSize(int arg0) throws OLAPException {
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.cursor.INavigator#synchronizedPages(int)
	 */
	@Override
	public void synchronizedPages(int position) {
	}
}

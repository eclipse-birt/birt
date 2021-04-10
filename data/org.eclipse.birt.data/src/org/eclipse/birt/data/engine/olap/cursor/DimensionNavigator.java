/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.cursor;

import java.util.ArrayList;
import java.util.Collection;

import javax.olap.OLAPException;

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
	public boolean next() throws OLAPException {
		return dimensionAxis.next();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#previous()
	 */
	public boolean previous() throws OLAPException {
		return dimensionAxis.previous();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#relative(int)
	 */
	public boolean relative(int arg0) throws OLAPException {
		return dimensionAxis.relative(arg0);
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#first()
	 */
	public boolean first() throws OLAPException {
		return dimensionAxis.first();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#last()
	 */
	public boolean last() throws OLAPException {
		return dimensionAxis.last();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#isBeforeFirst()
	 */
	public boolean isBeforeFirst() {
		return dimensionAxis.isBeforeFirst();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#isAfterLast()
	 */
	public boolean isAfterLast() throws OLAPException {
		return dimensionAxis.isAfterLast();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#isFirst()
	 */
	public boolean isFirst() throws OLAPException {
		return dimensionAxis.isFirst();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#isLast()
	 */
	public boolean isLast() throws OLAPException {
		return dimensionAxis.isLast();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#afterLast()
	 */
	public void afterLast() throws OLAPException {
		dimensionAxis.afterLast();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#beforeFirst()
	 */
	public void beforeFirst() throws OLAPException {
		dimensionAxis.beforeFirst();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#setPosition(long)
	 */
	public void setPosition(long position) throws OLAPException {
		dimensionAxis.setPosition(position);
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#getPosition()
	 */
	public long getPosition() throws OLAPException {
		return dimensionAxis.getPosition();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#close()
	 */
	public void close() throws OLAPException {
		dimensionAxis.close();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#getExtend()
	 */
	public long getExtend() {
		return dimensionAxis.getExtend();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#getType()
	 */
	public int getType() {
		return dimensionAxis.getType();
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#clearWarnings()
	 */
	public void clearWarnings() throws OLAPException {
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.INavigator#getWarnings()
	 */
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
	public void synchronizedPages(int position) {
	}
}

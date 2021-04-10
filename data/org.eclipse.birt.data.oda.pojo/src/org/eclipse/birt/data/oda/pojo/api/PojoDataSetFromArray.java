
/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.oda.pojo.api;

import java.util.Map;

import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * A POJO data set from an array. All the null values in the array are omitted.
 * Say the array is {null, object1, null, null, object2, null, object3}, then
 * the POJO data set from that array is {object1, object2, object3}
 */
public abstract class PojoDataSetFromArray implements IPojoDataSet {

	private Object[] objects; // all POJOs
	private int curIndex = -1; // current index

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.oda.pojo.api.IPojoDataSet#open(java.lang.Object,Map<
	 * String, Object>)
	 */
	public void open(Object appContext, Map<String, Object> dataSetParamValues) throws OdaException {
		curIndex = -1;
		objects = fetchPojos();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.pojo.api.IPojoDataSet#next()
	 */
	public Object next() throws OdaException {
		curIndex++;
		if (objects == null) {
			return null;
		} else {
			while (curIndex < objects.length && objects[curIndex] == null) // omit the null value
			{
				curIndex++;
			}
			if (curIndex >= objects.length) {
				return null;
			} else {
				return objects[curIndex];
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.pojo.api.IPojoDataSet#close()
	 */
	public void close() throws OdaException {
		objects = null;
	}

	/**
	 * @return the array which all POJOs are from
	 * @throws OdaException
	 */
	protected abstract Object[] fetchPojos() throws OdaException;
}

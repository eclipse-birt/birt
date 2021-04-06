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

import java.util.Iterator;
import java.util.Map;

import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * A POJO data set from an Iterator. All the null values in the iterator are
 * omitted. Say the iterator is {null, object1, null, null, object2, null,
 * object3}, then the POJO data set from that iterator is {object1, object2,
 * object3}
 */
public abstract class PojoDataSetFromIterator implements IPojoDataSet {
	@SuppressWarnings("unchecked")
	private Iterator iterator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.pojo.api.IPojoDataSet#open(java.lang.Object,
	 * Map<String, Object>)
	 */
	public void open(Object appContext, Map<String, Object> dataSetParamValues) throws OdaException {
		iterator = fetchPojos();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.pojo.api.IPojoDataSet#next()
	 */
	public Object next() throws OdaException {
		if (iterator == null) {
			return null;
		} else {
			while (iterator.hasNext()) {
				Object o = iterator.next();
				if (o != null) // omit null value
				{
					return o;
				}
			}
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.pojo.api.IPojoDataSet#close()
	 */
	public void close() throws OdaException {
		iterator = null;
	}

	/**
	 * @return the iterator which all POJOs are from
	 * @throws OdaException
	 */
	@SuppressWarnings("unchecked")
	protected abstract Iterator fetchPojos() throws OdaException;
}

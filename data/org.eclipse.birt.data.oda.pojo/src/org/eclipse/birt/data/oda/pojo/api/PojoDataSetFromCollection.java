/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
package org.eclipse.birt.data.oda.pojo.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * A POJO data set from a Collection. All the null values in the collection are
 * omitted. Say the collection is {null, object1, null, null, object2, null,
 * object3}, then the POJO data set from that array is {object1, object2,
 * object3}
 */
public abstract class PojoDataSetFromCollection implements IPojoDataSet {
	private IPojoDataSet pds;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.oda.pojo.api.IPojoDataSet#open(java.lang.Object,Map<
	 * String, Object>)
	 */
	@SuppressWarnings("unchecked")
	public void open(Object appContext, Map<String, Object> dataSetParamValues) throws OdaException {
		final Collection pojos = fetchPojos();
		pds = new PojoDataSetFromIterator() {
			@Override
			protected Iterator fetchPojos() throws OdaException {
				return pojos == null ? null : pojos.iterator();
			}
		};
		pds.open(appContext, dataSetParamValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.pojo.api.IPojoDataSet#next()
	 */
	public Object next() throws OdaException {
		return pds.next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.pojo.api.IPojoDataSet#close()
	 */
	public void close() throws OdaException {
		pds.close();
		pds = null;
	}

	/**
	 * @return the collection which all POJOs are from
	 * @throws OdaException
	 */
	@SuppressWarnings("unchecked")
	protected abstract Collection fetchPojos() throws OdaException;
}

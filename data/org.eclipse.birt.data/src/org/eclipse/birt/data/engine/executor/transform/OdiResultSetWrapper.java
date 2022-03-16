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

package org.eclipse.birt.data.engine.executor.transform;

import org.eclipse.birt.data.engine.executor.dscache.DataSetFromCache;
import org.eclipse.birt.data.engine.executor.dscache.DataSetToCache;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 * This is a wrapper class of all odi result set.It is used to enhance
 * result-set processing algorithm.
 */
public class OdiResultSetWrapper {
	private Object resultSource;

	/**
	 *
	 * @param rs
	 */
	OdiResultSetWrapper(ResultSet rs) {
		this.resultSource = rs;
	}

	/**
	 *
	 * @param rs
	 */
	OdiResultSetWrapper(DataSetToCache rs) {
		this.resultSource = rs;
	}

	/**
	 *
	 * @param rs
	 */
	OdiResultSetWrapper(DataSetFromCache rs) {
		this.resultSource = rs;
	}

	/**
	 *
	 * @param rs
	 */
	OdiResultSetWrapper(ICustomDataSet rs) {
		this.resultSource = rs;
	}

	/**
	 *
	 * @param rs
	 */
	public OdiResultSetWrapper(IResultIterator rs) {
		this.resultSource = rs;
	}

	/**
	 *
	 * @param rs
	 */
	OdiResultSetWrapper(Object[] rs) {
		this.resultSource = rs;
	}

	/**
	 *
	 */
	OdiResultSetWrapper(IDataSetPopulator rs) {
		this.resultSource = rs;
	}

	/**
	 *
	 * @return
	 */
	public Object getWrappedOdiResultSet() {
		return this.resultSource;
	}
}

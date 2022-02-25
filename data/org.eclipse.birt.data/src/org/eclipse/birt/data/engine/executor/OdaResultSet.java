/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.data.engine.executor;

import java.util.ArrayList;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * The oda result set which will retrieve resultset from dataset resultSet. If
 * there is non-null resultObject in resultSet, it will return the result
 * object. Or a dummy result object will be returned to enable the output
 * parameter could be retrieved.
 */
class OdaResultSet implements IDataSetPopulator {
	private ResultSet resultSet;

	private int status;

	private final static int UNKNOWN = -1;
	private final static int ODA_DATA = 0;
	private final static int ODA_PARAM = 1;

	/**
	 * constructor
	 *
	 * @param rs
	 * @throws DataException
	 */
	OdaResultSet(ResultSet rs) {
		this.resultSet = rs;
		this.status = UNKNOWN;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IDataSetPopulator#next()
	 */
	@Override
	public IResultObject next() throws DataException {
		if (status == UNKNOWN) {
			IResultObject resultObj = resultSet.fetch();
			if (resultObj != null) {
				status = ODA_DATA;
				return resultObj;
			} else {
				status = ODA_PARAM;
				return new DummyResultObject();
			}
		} else if (status == ODA_DATA) {
			return this.resultSet.fetch();
		} else {
			return null;
		}
	}

	/**
	 * The dummy result object to enable fetch the output parameter value
	 */
	static class DummyResultObject implements IResultObject {

		/*
		 * @see org.eclipse.birt.data.engine.odi.IResultObject#getFieldValue(java.lang.
		 * String)
		 */
		@Override
		public Object getFieldValue(String fieldName) throws DataException {
			return null;
		}

		/*
		 * @see org.eclipse.birt.data.engine.odi.IResultObject#getFieldValue(int)
		 */
		@Override
		public Object getFieldValue(int fieldIndex) throws DataException {
			return null;
		}

		/*
		 * @see org.eclipse.birt.data.engine.odi.IResultObject#getResultClass()
		 */
		@Override
		public IResultClass getResultClass() {
			try {
				// return empty ResultClass object
				return new ResultClass(new ArrayList());
			} catch (DataException e) {
				assert false;
				return null;
			}
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.odi.IResultObject#setCustomFieldValue(java.lang.
		 * String, java.lang.Object)
		 */
		@Override
		public void setCustomFieldValue(String fieldName, Object value) throws DataException {
			// do nothing
		}

		/*
		 * @see org.eclipse.birt.data.engine.odi.IResultObject#setCustomFieldValue(int,
		 * java.lang.Object)
		 */
		@Override
		public void setCustomFieldValue(int fieldIndex, Object value) throws DataException {
			// do nothing
		}

	}
}

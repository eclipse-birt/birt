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

package org.eclipse.birt.report.model.simpleapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IDataSet;
import org.eclipse.birt.report.model.api.simpleapi.IDataSource;
import org.eclipse.birt.report.model.api.simpleapi.IResultSetColumn;

public class DataSet implements IDataSet {

	private DataSetHandle dataSet;

	public DataSet(DataSetHandle dataSet) {
		this.dataSet = dataSet;
	}

	public IDataSource getDataSource() {
		if (!(dataSet instanceof OdaDataSetHandle))
			return null;

		DataSourceHandle dataSource = dataSet.getDataSource();
		if (dataSource == null)
			return null;

		return new DataSource(dataSource);
	}

	public String getQueryText() {
		if (dataSet instanceof OdaDataSetHandle)
			return ((OdaDataSetHandle) dataSet).getQueryText();
		return null;
	}

	public void setQueryText(String query) throws SemanticException {
		if (dataSet instanceof OdaDataSetHandle) {
			ActivityStack cmdStack = dataSet.getModule().getActivityStack();

			cmdStack.startNonUndoableTrans(null);
			try {
				((OdaDataSetHandle) dataSet).setQueryText(query);
			} catch (SemanticException e) {
				cmdStack.rollback();
				throw e;
			}

			cmdStack.commit();
		}
	}

	public String getPrivateDriverProperty(String name) {
		if (dataSet instanceof OdaDataSetHandle)
			return ((OdaDataSetHandle) dataSet).getPrivateDriverProperty(name);
		return null;
	}

	public void setPrivateDriverProperty(String name, String value) throws SemanticException {
		if (dataSet instanceof OdaDataSetHandle) {
			ActivityStack cmdStack = dataSet.getModule().getActivityStack();

			cmdStack.startNonUndoableTrans(null);
			try {
				((OdaDataSetHandle) dataSet).setPrivateDriverProperty(name, value);
			} catch (SemanticException e) {
				cmdStack.rollback();
				throw e;
			}

			cmdStack.commit();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IDataSet#getResultSetColumn
	 * ()
	 */

	public List getCachedResultSetColumns() {
		List values = new ArrayList();
		CachedMetaDataHandle metaDataHandle = dataSet.getCachedMetaDataHandle();
		if (metaDataHandle == null)
			return values;
		MemberHandle memberHandle = metaDataHandle.getResultSet();
		if (memberHandle == null)
			return values;
		Iterator iterator = memberHandle.iterator();
		while (iterator.hasNext()) {
			ResultSetColumnHandle columnHandle = (ResultSetColumnHandle) iterator.next();
			IResultSetColumn column = new ResultSetColumnImpl(columnHandle);
			values.add(column);
		}
		return Collections.unmodifiableList(values);
	}

}

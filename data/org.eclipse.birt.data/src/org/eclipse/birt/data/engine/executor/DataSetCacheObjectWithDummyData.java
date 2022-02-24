/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultClass;

public class DataSetCacheObjectWithDummyData implements IDataSetCacheObject {

	private IResultClass resultClass;
	private IDataSetCacheObject base;

	public DataSetCacheObjectWithDummyData(IBaseDataSetDesign dataSetDesign, IDataSetCacheObject base)
			throws DataException {
		this.base = base;
		this.resultClass = this.populateResultClass(dataSetDesign, base.getResultClass());
	}

	public boolean isCachedDataReusable(int requiredCapability) {
		return this.base.isCachedDataReusable(requiredCapability);
	}

	public boolean needUpdateCache(int requiredCapability) {
		return this.base.needUpdateCache(requiredCapability);
	}

	public IResultClass getResultClass() throws DataException {
		return this.resultClass;
	}

	public void release() {
		this.base.release();
	}

	public IDataSetCacheObject getSourceDataSetCacheObject() {
		return this.base;
	}

	private IResultClass populateResultClass(IBaseDataSetDesign dataSetDesign, IResultClass baseResultClass)
			throws DataException {
		List resultHints = dataSetDesign.getResultSetHints();
		List computedColumns = dataSetDesign.getComputedColumns();
		List columnsList = new ArrayList();
		for (int i = 1; i <= baseResultClass.getFieldCount(); i++) {
			columnsList.add(baseResultClass.getFieldMetaData(i));
		}

		Iterator it = resultHints.iterator();
		for (int j = 0; it.hasNext(); j++) {
			IColumnDefinition columnDefn = (IColumnDefinition) it.next();
			// data object with alias is retrieve as metadata on base
			// add check on columnDefn alias name
			if (baseResultClass.getFieldIndex(columnDefn.getColumnName()) != -1
					|| baseResultClass.getFieldIndex(columnDefn.getAlias()) != -1)
				continue;

			ResultFieldMetadata columnMetaData = new ResultFieldMetadata(j + 1, columnDefn.getColumnName(),
					columnDefn.getDisplayName(), DataType.getClass(columnDefn.getDataType()), null /* nativeTypeName */,
					true, columnDefn.getAnalysisType(), columnDefn.getAnalysisColumn(), columnDefn.isIndexColumn(),
					columnDefn.isCompressedColumn());
			columnsList.add(columnMetaData);
			columnMetaData.setAlias(columnDefn.getAlias());
		}

		// Add computed columns
		int count = columnsList.size();
		it = computedColumns.iterator();
		for (int j = resultHints.size(); it.hasNext(); j++) {
			IComputedColumn compColumn = (IComputedColumn) it.next();
			if (baseResultClass.getFieldIndex(compColumn.getName()) != -1)
				continue;
			ResultFieldMetadata columnMetaData = new ResultFieldMetadata(++count, compColumn.getName(),
					compColumn.getName(), DataType.getClass(compColumn.getDataType()), null /* nativeTypeName */, true,
					-1);
			columnsList.add(columnMetaData);
		}

		return new ResultClass(columnsList);
	}
}

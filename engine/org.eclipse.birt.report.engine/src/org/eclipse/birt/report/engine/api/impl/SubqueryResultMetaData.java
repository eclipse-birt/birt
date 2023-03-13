/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.report.engine.api.IResultMetaData;

public class SubqueryResultMetaData implements IResultMetaData {

	ArrayList<MetaData> metas;

	public SubqueryResultMetaData(ISubqueryDefinition subquery, HashMap map) throws BirtException {
		metas = new ArrayList<>();
		HashSet<String> names = new HashSet<>();
		IBaseQueryDefinition tmpQuery = subquery;
		while (tmpQuery instanceof ISubqueryDefinition) {
			ResultMetaData metaData = (ResultMetaData) map.get(tmpQuery);
			int columnCount = metaData.getColumnCount();
			for (int index = 0; index < columnCount; index++) {
				String columnName = metaData.getColumnName(index);
				if (!names.contains(columnName)) {
					if (tmpQuery != subquery) {
						if (columnIsAggregateOn(columnName, tmpQuery)) {
							continue;
						}
					}
					MetaData meta = new MetaData();
					meta.columnName = columnName;
					meta.columnAlias = metaData.getColumnAlias(index);
					meta.columnLabel = metaData.getColumnLabel(index);
					meta.columnType = metaData.getColumnType(index);
					meta.columnTypeName = metaData.getColumnTypeName(index);

					metas.add(meta);
					names.add(columnName);
				}
			}
			tmpQuery = tmpQuery.getParentQuery();
		}
		// tmpQuery is a QueryDefinition now
		ResultMetaData metaData = (ResultMetaData) map.get(tmpQuery);
		int columnCount = metaData.getColumnCount();
		for (int index = 0; index < columnCount; index++) {
			String columnName = metaData.getColumnName(index);
			if (!names.contains(columnName)) {
				if (tmpQuery != subquery) {
					if (columnIsAggregateOn(columnName, tmpQuery)) {
						continue;
					}
				}
				MetaData meta = new MetaData();
				meta.columnName = columnName;
				meta.columnAlias = metaData.getColumnAlias(index);
				meta.columnLabel = metaData.getColumnLabel(index);
				meta.columnType = metaData.getColumnType(index);
				meta.columnTypeName = metaData.getColumnTypeName(index);

				metas.add(meta);
				names.add(columnName);
			}
		}
	}

	private boolean columnIsAggregateOn(String column, IBaseQueryDefinition query) throws BirtException {
		boolean result = false;
		Map bindings = query.getBindings();
		{
			IBinding binding = (IBinding) bindings.get(column);
			if (binding != null) {
				List aggregates = binding.getAggregatOns();
				if ((aggregates != null && aggregates.size() > 0) || binding.getAggrFunction() != null) {
					result = true;
				} else {
					IBaseExpression expr = binding.getExpression();
					if (expr instanceof IScriptExpression) {
						if (ExpressionUtil.hasAggregation(((IScriptExpression) expr).getText())) {
							result = true;
						}
					}
				}
			}
		}

		return result;
	}

	@Override
	public String getColumnAlias(int index) throws BirtException {
		return metas.get(index).columnAlias;
	}

	@Override
	public int getColumnCount() {
		return metas.size();
	}

	@Override
	public String getColumnLabel(int index) throws BirtException {
		return metas.get(index).columnLabel;
	}

	@Override
	public String getColumnName(int index) throws BirtException {
		return metas.get(index).columnName;
	}

	@Override
	public int getColumnType(int index) throws BirtException {
		return metas.get(index).columnType;
	}

	@Override
	public String getColumnTypeName(int index) throws BirtException {
		return metas.get(index).columnTypeName;
	}

	@Override
	public boolean getAllowExport(int index) throws BirtException {
		return true;
	}

	private static class MetaData {

		String columnName;
		String columnAlias;
		String columnLabel;
		int columnType;
		String columnTypeName;
	}
}

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

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

public class ResultMetaData implements IResultMetaData {

	protected IResultMetaData parentMetaData;
	protected String[] selectedColumns;

	public ResultMetaData(IBaseQueryDefinition query, String[] selectedColumns) {
		initializeMetaData(query);
		this.selectedColumns = selectedColumns;
	}

	public ResultMetaData(IBaseQueryDefinition query) {
		initializeMetaData(query);
		this.selectedColumns = null;
	}

	public ResultMetaData(IBaseQueryDefinition query, DesignElementHandle handle) {
		initializeMetaData(query, handle);
		this.selectedColumns = null;
	}

	public ResultMetaData(IResultMetaData parentMetaData, String[] selectedColumns) {
		this.parentMetaData = parentMetaData;
		this.selectedColumns = selectedColumns;
	}

	public ResultMetaData(org.eclipse.birt.data.engine.api.IResultMetaData dteMeta) {
		this(dteMeta, null);
	}

	public ResultMetaData(org.eclipse.birt.data.engine.api.IResultMetaData dteMeta, String[] selectColumns) {
		this.selectedColumns = selectColumns;
		adaptDTE(dteMeta);
	}

	private void adaptDTE(org.eclipse.birt.data.engine.api.IResultMetaData dteMeta) {
		if (dteMeta == null) {
			return;
		}
		int count = dteMeta.getColumnCount();
		for (int index = 0; index < count; index++) {
			try {
				metaEntries.add(new MetaDataEntry(dteMeta.getColumnName(index + 1), dteMeta.getColumnLabel(index + 1),
						dteMeta.getColumnType(index + 1)));
			} catch (BirtException ex) {

			}
		}
	}

	protected void initializeMetaData(IBaseQueryDefinition query) {
		initializeMetaData(query, null);
	}

	protected void initializeMetaData(IBaseQueryDefinition query, DesignElementHandle handle) {
		appendMetaData(query, handle);
	}

	private ArrayList metaEntries = new ArrayList();

	private static class MetaDataEntry {

		String name;
		String displayName;
		int type;
		boolean allowExport;

		MetaDataEntry(String name, String displayName, int type) {
			this(name, displayName, type, true);
		}

		MetaDataEntry(String name, String displayName, int type, boolean allowExport) {
			this.name = name;
			this.displayName = displayName;
			this.type = type;
			this.allowExport = allowExport;
		}
	}

	protected void appendMetaData(IBaseQueryDefinition query, DesignElementHandle handle) {

		ArrayList<ComputedColumn> columnList = null;
		ArrayList<String> notAllowed = new ArrayList<>();
		if (handle instanceof TableHandle || handle instanceof DataItemHandle) {
			columnList = (ArrayList<ComputedColumn>) handle.getProperty(IReportItemModel.BOUND_DATA_COLUMNS_PROP);
		}

		if (columnList != null) {
			for (int i = 0; i < columnList.size(); i++) {
				if (!columnList.get(i).allowExport()) {
					notAllowed.add(columnList.get(i).getName());
				}
			}
		}

		Map bindings = query.getBindings();
		Iterator iter = bindings.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String name = (String) entry.getKey();
			IBinding binding = (IBinding) entry.getValue();
			try {
				if (binding.exportable()) {
					metaEntries.add(new MetaDataEntry(name, binding.getDisplayName(), binding.getDataType(),
							isColumnAllowedExport(name, notAllowed)));
				}
			} catch (DataException ex) {
				// FIXME: process exception.
			}
		}
	}

	private boolean isColumnAllowedExport(String columnName, ArrayList<String> notAllowed) {
		if (notAllowed == null || notAllowed.size() <= 0) {
			return true;
		}
		for (int i = 0; i < notAllowed.size(); i++) {
			if (columnName.equals(notAllowed.get(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getColumnCount() {
		if (selectedColumns != null) {
			return selectedColumns.length;
		}
		if (null != parentMetaData) {
			return parentMetaData.getColumnCount();
		}
		return metaEntries.size();
	}

	@Override
	public String getColumnName(int index) throws BirtException {
		index = getColumnIndex(index);
		if (null != parentMetaData) {
			return parentMetaData.getColumnName(index);
		} else {
			MetaDataEntry entry = (MetaDataEntry) metaEntries.get(index);
			return entry.name;
		}
	}

	@Override
	public String getColumnAlias(int index) throws BirtException {
		return getColumnName(index);
	}

	@Override
	public int getColumnType(int index) throws BirtException {
		index = getColumnIndex(index);
		if (null != parentMetaData) {
			return parentMetaData.getColumnType(index);
		} else {
			MetaDataEntry entry = (MetaDataEntry) metaEntries.get(index);
			return entry.type;
		}
	}

	@Override
	public String getColumnTypeName(int index) throws BirtException {
		int type = getColumnType(index);
		return DataType.getName(type);
	}

	@Override
	public String getColumnLabel(int index) throws BirtException {
		index = getColumnIndex(index);
		String columnLabel;
		if (null != parentMetaData) {
			columnLabel = parentMetaData.getColumnLabel(index);
		} else {
			MetaDataEntry entry = (MetaDataEntry) metaEntries.get(index);
			columnLabel = entry.displayName;
		}
		if (columnLabel == null) {
			columnLabel = getColumnName(index);
		}
		return columnLabel;
	}

	@Override
	public boolean getAllowExport(int index) throws BirtException {
		index = getColumnIndex(index);
		boolean allowExport;
		if (null != parentMetaData) {
			allowExport = parentMetaData.getAllowExport(index);
		} else {
			MetaDataEntry entry = (MetaDataEntry) metaEntries.get(index);
			allowExport = entry.allowExport;
		}
		return allowExport;
	}

	private int getColumnIndex(int index) throws BirtException {
		if (selectedColumns == null) {
			return index;
		}

		String name = selectedColumns[index];
		if (null != parentMetaData) {
			for (int i = 0; i < parentMetaData.getColumnCount(); i++) {
				String columnName = parentMetaData.getColumnName(i);
				if (columnName.equals(name)) {
					return i;
				}
			}
		} else {
			for (int i = 0; i < metaEntries.size(); i++) {
				MetaDataEntry entry = (MetaDataEntry) metaEntries.get(i);
				if (entry.name.equals(name)) {
					return i;
				}
			}
		}
		throw new EngineException(MessageConstants.INVALID_COLUMN_INDEX_ERROR);
	}

}

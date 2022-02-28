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

package org.eclipse.birt.report.designer.ui.cubebuilder.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DeleteWarningDialog;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.PlatformUI;

public class OlapUtil {

	public static String[] getDataFieldNames(DataSetHandle dataset) {
		if (dataset == null) {
			return new String[0];
		}

		String[] columns = {};
		try {
			List columnList = DataUtil.getColumnList(dataset);
			columns = new String[columnList.size()];
			for (int i = 0; i < columnList.size(); i++) {
				ResultSetColumnHandle resultSetColumn = (ResultSetColumnHandle) columnList.get(i);
				columns[i] = resultSetColumn.getColumnName();
			}
		} catch (SemanticException e) {
			ExceptionUtil.handle(e);
		}
		return columns;
	}

	public static String getDataFieldDisplayName(ResultSetColumnHandle column) {
		if (column == null) {
			return null;
		}

		DataSetHandle dataset = (DataSetHandle) column.getElementHandle();
		for (Iterator iter = dataset.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP).iterator(); iter.hasNext();) {
			ColumnHintHandle element = (ColumnHintHandle) iter.next();
			if (element.getColumnName().equals(column.getColumnName())
					|| column.getColumnName().equals(element.getAlias())) {
				if (element.getDisplayName() != null && element.getDisplayName().length() > 0) {
					return element.getDisplayName();
				}
			}
		}
		return column.getColumnName();
	}

	public static String getDataFieldName(ResultSetColumnHandle column) {
		if (column == null) {
			return null;
		}

		DataSetHandle dataset = (DataSetHandle) column.getElementHandle();
		for (Iterator iter = dataset.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP).iterator(); iter.hasNext();) {
			ColumnHintHandle element = (ColumnHintHandle) iter.next();
			if (element.getColumnName().equals(column.getColumnName())
					|| column.getColumnName().equals(element.getAlias())) {
				return element.getColumnName();
			}
		}
		return column.getColumnName();
	}

	public static ColumnHintHandle getColumnHintHandle(ResultSetColumnHandle column) {
		if (column == null) {
			return null;
		}

		DataSetHandle dataset = (DataSetHandle) column.getElementHandle();
		for (Iterator iter = dataset.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP).iterator(); iter.hasNext();) {
			ColumnHintHandle element = (ColumnHintHandle) iter.next();
			if (element.getColumnName().equals(column.getColumnName())
					|| column.getColumnName().equals(element.getAlias())) {
				return element;
			}
		}
		return null;
	}

	public static String[] getDataFieldDisplayNames(DataSetHandle dataset) {
		if (dataset == null) {
			return new String[0];
		}

		String[] columns = {};
		try {
			List columnList = DataUtil.getColumnList(dataset);
			columns = new String[columnList.size()];
			for (int i = 0; i < columnList.size(); i++) {
				ResultSetColumnHandle resultSetColumn = (ResultSetColumnHandle) columnList.get(i);
				columns[i] = getDataFieldDisplayName(resultSetColumn);
			}
		} catch (SemanticException e) {
			ExceptionUtil.handle(e);
		}
		return columns;
	}

	public static ResultSetColumnHandle[] getDataFields(DataSetHandle dataset) {
		if (dataset == null) {
			return new ResultSetColumnHandle[0];
		}

		ResultSetColumnHandle[] columns = {};
		try {
			List columnList = DataUtil.getColumnList(dataset);
			columns = new ResultSetColumnHandle[columnList.size()];
			for (int i = 0; i < columnList.size(); i++) {
				ResultSetColumnHandle resultSetColumn = (ResultSetColumnHandle) columnList.get(i);
				columns[i] = resultSetColumn;
			}
		} catch (SemanticException e) {
			ExceptionUtil.handle(e);
		}
		return columns;
	}

	public static ResultSetColumnHandle getDataField(DataSetHandle dataset, String fieldName) {
		if (dataset == null || fieldName == null) {
			return null;
		}
		try {
			List columnList = DataUtil.getColumnList(dataset);
			for (int i = 0; i < columnList.size(); i++) {
				ResultSetColumnHandle resultSetColumn = (ResultSetColumnHandle) columnList.get(i);
				if (fieldName.equals(resultSetColumn.getColumnName())) {
					return resultSetColumn;
				}
			}
		} catch (SemanticException e) {
			ExceptionUtil.handle(e);
		}
		return null;
	}

	public static String[] getAvailableDatasetNames() {
		SlotHandle slot = SessionHandleAdapter.getInstance().getReportDesignHandle().getDataSets();
		if (slot == null || slot.getCount() == 0) {
			return new String[0];
		}
		String[] datasets = new String[slot.getCount()];
		for (int i = 0; i < slot.getCount(); i++) {
			DataSetHandle dataset = (DataSetHandle) slot.get(i);
			datasets[i] = dataset.getName();
		}
		return datasets;
	}

	public static DataSetHandle[] getAvailableDatasets() {
		SlotHandle slot = SessionHandleAdapter.getInstance().getReportDesignHandle().getDataSets();
		if (slot == null || slot.getCount() == 0) {
			return new DataSetHandle[0];
		}
		DataSetHandle[] datasets = new DataSetHandle[slot.getCount()];
		for (int i = 0; i < slot.getCount(); i++) {
			datasets[i] = (DataSetHandle) slot.get(i);
		}
		return datasets;
	}

	public static int getIndexOfPrimaryDataset(DataSetHandle dataset) {
		SlotHandle slot = SessionHandleAdapter.getInstance().getReportDesignHandle().getDataSets();
		if (slot == null || slot.getCount() == 0) {
			return -1;
		}
		for (int i = 0; i < slot.getCount(); i++) {
			if (slot.get(i) == dataset) {
				return i;
			}
		}
		return -1;
	}

	public static DataSetHandle getDataset(String datasetName) {
		SlotHandle slot = SessionHandleAdapter.getInstance().getReportDesignHandle().getDataSets();
		if (slot == null || slot.getCount() == 0) {
			return null;
		}
		for (int i = 0; i < slot.getCount(); i++) {
			if (((DataSetHandle) slot.get(i)).getName().equals(datasetName)) {
				return (DataSetHandle) slot.get(i);
			}
		}
		return null;
	}

	public static final String DLG_REFERENCE_FOUND_TITLE = Messages.getString("GroupsPage.Reference"); //$NON-NLS-1$
	public static final String DLG_HAS_FOLLOWING_CLIENTS_MSG = Messages.getString("GroupsPage.Clients"); //$NON-NLS-1$
	public static final String DLG_CONFIRM_MSG = Messages.getString("GroupsPage.Dlg.Confirm"); //$NON-NLS-1$

	public static boolean enableDrop(Object model) {
		if (model instanceof DesignElementHandle) {
			DesignElementHandle handle = (DesignElementHandle) model;
			ArrayList referenceList = new ArrayList();
			for (Iterator itor = handle.clientsIterator(); itor.hasNext();) {
				referenceList.add(itor.next());
			}
			if (!referenceList.isEmpty()) {
				DeleteWarningDialog dialog = new DeleteWarningDialog(
						PlatformUI.getWorkbench().getDisplay().getActiveShell(), DLG_REFERENCE_FOUND_TITLE,
						referenceList);
				dialog.setPreString(DEUtil.getDisplayLabel(handle) + DLG_HAS_FOLLOWING_CLIENTS_MSG);
				dialog.setSufString(DLG_CONFIRM_MSG);
				return dialog.open() != Dialog.CANCEL;
			}
			return true;
		}
		return true;
	}

	public static boolean isFromLibrary(Object model) {
		if (model instanceof DesignElementHandle) {
			return DEUtil.isLinkedElement((DesignElementHandle) model);
		}
		return false;
	}

	public static DataSetHandle getHierarchyDataset(TabularHierarchyHandle hierarchy) {
		if (hierarchy == null) {
			return null;
		}
		DataSetHandle dataset = hierarchy.getDataSet();
		if (dataset == null && hierarchy.getLevelCount() > 0
				&& hierarchy.getContainer().getContainer() instanceof TabularCubeHandle) {
			dataset = ((TabularCubeHandle) hierarchy.getContainer().getContainer()).getDataSet();
		}
		return dataset;
	}

	private static IChoiceSet choiceSet = MetaDataDictionary.getInstance()
			.getElement(ReportDesignConstants.TABULAR_LEVEL_ELEMENT)
			.getProperty(DesignChoiceConstants.CHOICE_DATE_TIME_LEVEL_TYPE).getAllowedChoices();
	private static IChoice[] DATE_TIME_LEVEL_TYPE_ALL = choiceSet.getChoices();

	private static IChoice[] DATE_LEVEL_TYPE_ALL;

	private static IChoice[] TIME_LEVEL_TYPE_ALL;
	static {
		List choiceList = new ArrayList(Arrays.asList(choiceSet.getChoices()));
		choiceList.remove(choiceSet.findChoice(DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_HOUR));
		choiceList.remove(choiceSet.findChoice(DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MINUTE));
		choiceList.remove(choiceSet.findChoice(DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_SECOND));
		DATE_LEVEL_TYPE_ALL = (IChoice[]) choiceList.toArray(new IChoice[0]);

		choiceList.clear();
		choiceList.add(choiceSet.findChoice(DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_HOUR));
		choiceList.add(choiceSet.findChoice(DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MINUTE));
		choiceList.add(choiceSet.findChoice(DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_SECOND));
		TIME_LEVEL_TYPE_ALL = (IChoice[]) choiceList.toArray(new IChoice[0]);
	}

	public static IChoiceSet getDateTimeLevelTypeChoiceSet() {
		return choiceSet;
	}

	public static IChoice[] getDateTimeLevelTypeChoices() {
		return DATE_TIME_LEVEL_TYPE_ALL;
	}

	public static IChoice[] getDateLevelTypeChoices() {
		return DATE_LEVEL_TYPE_ALL;
	}

	public static IChoice[] getTimeLevelTypeChoices() {
		return TIME_LEVEL_TYPE_ALL;
	}

}

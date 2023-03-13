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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;

/**
 *
 */

public class DataSetColumnBindingsFormHandleProvider extends AbstractDatasetSortingFormHandleProvider {

	private static final String ALL = Messages.getString("DataSetColumnBindingsFormHandleProvider.ALL");//$NON-NLS-1$
	private static final String NONE = Messages.getString("DataSetColumnBindingsFormHandleProvider.NONE");//$NON-NLS-1$

	private String[] columnNames = { Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.Position"), //$NON-NLS-1$
			Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.Name"), //$NON-NLS-1$
			Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.DisplayNameID"), //$NON-NLS-1$
			Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.DisplayName"), //$NON-NLS-1$
			Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.DataType"), //$NON-NLS-1$
			Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.Expression"), //$NON-NLS-1$
	};

	private CellEditor[] editors;

	private static int[] columnWidth = { 80, 140, 140, 140, 80, 200 };

	// object to add data binding.
	private ReportElementHandle bindingObject;

	private static final IChoice[] DATA_TYPE_CHOICES = DEUtil.getMetaDataDictionary()
			.getStructure(ComputedColumn.COMPUTED_COLUMN_STRUCT).getMember(ComputedColumn.DATA_TYPE_MEMBER)
			.getAllowedChoices().getChoices();

	Map<String, Integer> columnBindingNameToPositionMap = new HashMap<>();

	@Override
	public boolean isEnable() {
		if (DEUtil.getInputSize(input) != 1) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean isEditable() {
		if (input == null || !(DEUtil.getInputFirstElement(input) instanceof ReportItemHandle)) {
			return false;
		} else if (((ReportItemHandle) DEUtil.getInputFirstElement(input))
				.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF) {
			return false;
		} else if (((ReportItemHandle) DEUtil.getInputFirstElement(input))
				.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_NONE) {
			if (DEUtil.getBindingHolder((ReportItemHandle) DEUtil.getInputFirstElement(input), true) != null
					&& DEUtil.getBindingHolder((ReportItemHandle) DEUtil.getInputFirstElement(input), true)
							.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	public DataSetColumnBindingsFormHandleProvider() {
	}

	/**
	 * @return the bindingObject
	 */
	public ReportElementHandle getBindingObject() {
		return bindingObject;
	}

	/**
	 * @param bindingObject the bindingObject to set
	 */

	@Override
	public void setBindingObject(DesignElementHandle bindingObject) {
		if (bindingObject instanceof ReportElementHandle) {
			this.bindingObject = (ReportElementHandle) bindingObject;
			this.columnBindingNameToPositionMap.clear();
		}
	}

	@Override
	public String[] getColumnNames() {
		return columnNames;
	}

	@Override
	public int[] getColumnWidths() {
		return columnWidth;
	}

	@Override
	public String getDisplayName() {
		if (isEditable()) {
			return Messages.getString("DataSetColumnBindingsFormHandleProvider.DatasetTitle"); //$NON-NLS-1$
		} else {
			return Messages.getString("DataSetColumnBindingsFormHandleProvider.ReportItemTitle"); //$NON-NLS-1$
		}
	}

	public CellEditor[] getEditors(Table table) {
		if (editors == null) {
			editors = new CellEditor[columnNames.length];

			for (int i = 0; i < editors.length; i++) {
				editors[i] = new TextCellEditor(table);
			}
		}
		return editors;
	}

	public boolean doMoveItem(int oldPos, int newPos) throws Exception {
		return false;
	}

	@Override
	public boolean doDeleteItem(int pos) throws Exception {
		pos = getOriginalIndex(pos);
		if (pos > -1) {
			if (bindingObject instanceof ReportItemHandle) {
				((ReportItemHandle) bindingObject).getColumnBindings().getAt(pos).drop();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean doAddItem(int pos) throws Exception {
		DataColumnBindingDialog dialog = new DataColumnBindingDialog(true, true);
		dialog.setInput((ReportItemHandle) bindingObject);
		if (dialog.open() == Dialog.OK) {
			if (viewer != null) {
				viewer.refresh(true);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean doEditItem(int pos) {
		ComputedColumnHandle bindingHandle = null;
		pos = getOriginalIndex(pos);
		if (pos > -1) {
			if (bindingObject instanceof ReportItemHandle) {
				bindingHandle = (ComputedColumnHandle) ((ReportItemHandle) bindingObject).getColumnBindings()
						.getAt(pos);
			}
		}
		if (bindingHandle == null) {
			return false;
		}

		boolean isResultSetColumn = false;
		String resultSetName = null;
		if (bindingObject instanceof DataItemHandle) {
			resultSetName = ((DataItemHandle) bindingObject).getResultSetColumn();
		}
		if (resultSetName != null && bindingHandle.getName().equals(resultSetName)) {
			isResultSetColumn = true;
		}

		DataColumnBindingDialog dialog = new DataColumnBindingDialog(false);
		dialog.setInput((ReportItemHandle) bindingObject, bindingHandle);

		if (dialog.open() == Dialog.OK) {
			if (isResultSetColumn) {
				try {
					((DataItemHandle) bindingObject).setResultSetColumn(bindingHandle.getName());
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
			if (viewer != null) {
				viewer.refresh(true);
				return true;
			}
		}
		return false;
	}

	private Integer getColumnPosition(ComputedColumnHandle handle) {
		String name = handle.getName();
		if (name == null) {
			return 0;
		}

		Map<String, Integer> map = getColumnBindingMap();
		if (map.containsKey(name)) {
			return map.get(name);
		} else {
			return map.size() + 1;
		}
	}

	private Map<String, Integer> getColumnBindingMap() {
		if (this.columnBindingNameToPositionMap.isEmpty()) {
			DesignElementHandle eHandle = this.getBindingObject();
			if (eHandle instanceof ReportItemHandle) {
				int position = 1;
				ReportItemHandle handle = (ReportItemHandle) this.getBindingObject();
				for (Iterator iter = handle.getColumnBindings().iterator(); iter.hasNext();) {
					ComputedColumnHandle col = (ComputedColumnHandle) iter.next();
					String name = col.getName();
					this.columnBindingNameToPositionMap.put(col.getName(), position);
					position++;
				}
			}
		}
		return this.columnBindingNameToPositionMap;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return getColumnPosition((ComputedColumnHandle) element).toString();
		case 1:
			return ((ComputedColumnHandle) element).getName();
		case 2:
			return ((ComputedColumnHandle) element).getDisplayNameID();
		case 3:
			return ((ComputedColumnHandle) element).getDisplayName();
		case 4:
			return getDataTypeDisplayName(((ComputedColumnHandle) element).getDataType());
		case 5:
			return DataUtil.getAggregationExpression((ComputedColumnHandle) element);
		case 6:
			try {
				String function = ((ComputedColumnHandle) element).getAggregateFunction();
				if (function != null) {
					function = DataAdapterUtil.adaptModelAggregationType(function);
					if (function != null && DataUtil.getAggregationManager().getAggregation(function) != null) {
						return DataUtil.getAggregationManager().getAggregation(function).getDisplayName();
					}
				}
			} catch (BirtException e) {
				ExceptionHandler.handle(e);
			}
			return null;
		case 7:
			String ExpValue = ((ComputedColumnHandle) element).getFilterExpression();
			if (ExpValue != null && ExpValue.length() > 0) {
				return ExpValue;
			} else {
				return null;
			}
		case 8:
			String value = DEUtil.getAggregateOn((ComputedColumnHandle) element);
			String text;
			if (value == null) {
				if (((ComputedColumnHandle) element).getAggregateFunction() != null) {
					text = ALL;
				} else {
					text = NONE;
				}
			} else {
				text = value;
			}

			return text;
		default:
			break;
		}
		return null;
	}

	private String getDataTypeDisplayName(String dataType) {
		for (int i = 0; i < DATA_TYPE_CHOICES.length; i++) {
			IChoice choice = DATA_TYPE_CHOICES[i];
			if (choice.getName().equals(dataType)) {
				return choice.getDisplayName();
			}
		}
		return dataType;
	}

	@Override
	public String getImagePath(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Object[] && ((Object[]) inputElement).length > 0) {
			return getElements(((Object[]) inputElement)[0]);
		}
		if (inputElement instanceof List) {
			return getElements(((List) inputElement).get(0));
		}
		if (inputElement instanceof ReportItemHandle) {
			ReportItemHandle reportHandle = (ReportItemHandle) inputElement;
			this.bindingObject = reportHandle;
			List children = new ArrayList();
			for (Iterator iter = reportHandle.getColumnBindings().iterator(); iter.hasNext();) {
				children.add(iter.next());
			}

			Object[] arrays = children.toArray();
			Arrays.sort(arrays, new BindingComparator());
			return arrays;
		}
		return new Object[] {};
	}

	@Override
	public int getOriginalIndex(int pos) {
		List children = new ArrayList();
		for (Iterator iter = ((ReportItemHandle) bindingObject).getColumnBindings().iterator(); iter.hasNext();) {
			children.add(iter.next());
		}

		Object[] arrays = children.toArray();
		Arrays.sort(arrays, new BindingComparator());
		return children.indexOf(Arrays.asList(arrays).get(pos));
	}

	private class BindingComparator implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			ComputedColumnHandle binding1 = (ComputedColumnHandle) o1;
			ComputedColumnHandle binding2 = (ComputedColumnHandle) o2;

			int result = 0;

			if (sortingColumnIndex == 0) // Sort by position
			{
				Integer col1 = getColumnPosition(binding1);
				Integer col2 = getColumnPosition(binding2);
				result = col1.compareTo(col2);
			} else {
				String columnText1 = getColumnText(binding1, sortingColumnIndex);
				String columnText2 = getColumnText(binding2, sortingColumnIndex);
				result = (columnText1 == null ? "" : columnText1).compareTo((columnText2 == null ? "" //$NON-NLS-1$ //$NON-NLS-2$
						: columnText2));
			}
			if (sortDirection == SWT.UP) {
				return result;
			} else {
				return 0 - result;
			}
		}
	}

	@Override
	public Object getValue(Object element, String property) {
		int index = Arrays.asList(columnNames).indexOf(property);

		String columnText = getColumnText(element, index);
		return columnText;
	}

	@Override
	public boolean needRefreshed(NotificationEvent event) {
		if (event.getEventType() == NotificationEvent.PROPERTY_EVENT) {
			PropertyEvent ev = (PropertyEvent) event;
			String propertyName = ev.getPropertyName();
			if (ReportItemHandle.BOUND_DATA_COLUMNS_PROP.equals(propertyName)
					|| ReportItemHandle.DATA_SET_PROP.equals(propertyName)) {
				return true;
			}
		}
		return false;
	}

	private void generateOutputParmsBindings(DataSetHandle datasetHandle) {
		List<DataSetParameterHandle> outputParams = new ArrayList<>();
		for (Iterator iter = datasetHandle.parametersIterator(); iter.hasNext();) {
			Object obj = iter.next();
			if ((obj instanceof DataSetParameterHandle) && ((DataSetParameterHandle) obj).isOutput()) {
				outputParams.add((DataSetParameterHandle) obj);
			}
		}

		int ret = -1;
		if (outputParams.size() > 0) {
			MessageDialog prefDialog = new MessageDialog(UIUtil.getDefaultShell(),
					Messages.getString("dataBinding.title.generateOutputParam"), //$NON-NLS-1$
					null, Messages.getString("dataBinding.msg.generateOutputParam"), //$NON-NLS-1$
					MessageDialog.QUESTION, new String[] { Messages.getString("AttributeView.dialg.Message.Yes"), //$NON-NLS-1$
							Messages.getString("AttributeView.dialg.Message.No") }, //$NON-NLS-1$
					0);// $NON-NLS-1$

			ret = prefDialog.open();
		}

		if (ret == 0) {
			for (int i = 0; i < outputParams.size(); i++) {
				DataSetParameterHandle param = outputParams.get(i);
				ComputedColumn bindingColumn = StructureFactory.newComputedColumn(bindingObject, param.getName());
				bindingColumn.setDataType(param.getDataType());
				String groupType = DEUtil.getGroupControlType(bindingObject);
				List groupList = DEUtil.getGroups(bindingObject);
				ExpressionUtility.setBindingColumnExpression(param, bindingColumn, true);

				if (bindingObject instanceof ReportItemHandle) {
					try {
						((ReportItemHandle) bindingObject).addColumnBinding(bindingColumn, false);
					} catch (SemanticException e) {
						ExceptionHandler.handle(e);
					}
					continue;
				}

				if (ExpressionUtil.hasAggregation(bindingColumn.getExpression())) {
					if (groupType.equals(DEUtil.TYPE_GROUP_GROUP)) {
						bindingColumn.setAggregrateOn(((GroupHandle) groupList.get(0)).getName());
					} else if (groupType.equals(DEUtil.TYPE_GROUP_LISTING)) {
						bindingColumn.setAggregrateOn(null);
					}
				}
			}
		}
	}

	@Override
	public void generateAllBindingColumns() {
		if (bindingObject != null) {
			DataSetHandle datasetHandle = null;
			if (bindingObject instanceof ReportItemHandle) {
				datasetHandle = ((ReportItemHandle) bindingObject).getDataSet();
			} else if (bindingObject instanceof GroupHandle) {
				datasetHandle = ((ReportItemHandle) ((GroupHandle) bindingObject).getContainer()).getDataSet();
			}
			if (datasetHandle != null) {
				CommandStack stack = getActionStack();
				stack.startTrans(Messages.getString(
						Messages.getString("DataSetColumnBindingsFormHandleProvider.Trans.RefreshBindingColumns"))); //$NON-NLS-1$
				try {

					Iterator iter = getLinkedDataSetColumnIterator(datasetHandle);
					if (iter == null) {
						CachedMetaDataHandle cmdh = DataSetUIUtil.getCachedMetaDataHandle(datasetHandle);
						iter = cmdh.getResultSet().iterator();
					}

					for (; iter.hasNext();) {
						ResultSetColumnHandle element = (ResultSetColumnHandle) iter.next();
						ComputedColumn bindingColumn = StructureFactory.newComputedColumn(bindingObject,
								element.getColumnName());
						bindingColumn.setDataType(element.getDataType());
						String groupType = DEUtil.getGroupControlType(bindingObject);
						List groupList = DEUtil.getGroups(bindingObject);
						ExpressionUtility.setBindingColumnExpression(element, bindingColumn);

						bindingColumn.setDisplayName(UIUtil.getColumnDisplayName(element));
						String displayKey = UIUtil.getColumnDisplayNameKey(element);
						if (displayKey != null) {
							bindingColumn.setDisplayNameID(displayKey);
						}

						if (bindingObject instanceof ReportItemHandle) {
							((ReportItemHandle) bindingObject).addColumnBinding(bindingColumn, false);
							continue;
						}
						// if ( bindingObject instanceof GroupHandle )
						// {
						// ( (GroupHandle) bindingObject ).addColumnBinding(
						// bindingColumn,
						// false );
						// }
						if (ExpressionUtil.hasAggregation(bindingColumn.getExpression())) {
							if (groupType.equals(DEUtil.TYPE_GROUP_GROUP)) {
								bindingColumn.setAggregrateOn(((GroupHandle) groupList.get(0)).getName());
							} else if (groupType.equals(DEUtil.TYPE_GROUP_LISTING)) {
								bindingColumn.setAggregrateOn(null);
							}
						}

					}

					generateOutputParmsBindings(datasetHandle);
					stack.commit();

				} catch (SemanticException e) {
					ExceptionHandler.handle(e);
					stack.rollback();
				}
			}
		}
	}

	public void generateBindingColumns(Object[] columns) {
		if (columns != null && columns.length > 0) {
			CommandStack stack = getActionStack();
			stack.startTrans(Messages.getString(
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Trans.GenerateBindingColumns"))); //$NON-NLS-1$
			try {

				for (int i = 0; i < columns.length; i++) {
					ResultSetColumnHandle element = (ResultSetColumnHandle) columns[i];
					ComputedColumn bindingColumn = StructureFactory.newComputedColumn(bindingObject,
							element.getColumnName());
					bindingColumn.setDataType(element.getDataType());
					String groupType = DEUtil.getGroupControlType(bindingObject);
					List groupList = DEUtil.getGroups(bindingObject);
					ExpressionUtility.setBindingColumnExpression(element, bindingColumn);

					bindingColumn.setDisplayName(UIUtil.getColumnDisplayName(element));
					String displayKey = UIUtil.getColumnDisplayNameKey(element);
					if (displayKey != null) {
						bindingColumn.setDisplayNameID(displayKey);
					}

					if (bindingObject instanceof ReportItemHandle) {
						((ReportItemHandle) bindingObject).addColumnBinding(bindingColumn, false);
						continue;
					}
					// if ( bindingObject instanceof GroupHandle )
					// {
					// ( (GroupHandle) bindingObject ).addColumnBinding(
					// bindingColumn,
					// false );
					// }
					if (ExpressionUtil.hasAggregation(bindingColumn.getExpression())) {
						if (groupType.equals(DEUtil.TYPE_GROUP_GROUP)) {
							bindingColumn.setAggregrateOn(((GroupHandle) groupList.get(0)).getName());
						} else if (groupType.equals(DEUtil.TYPE_GROUP_LISTING)) {
							bindingColumn.setAggregrateOn(null);
						}
					}

				}
				stack.commit();

			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
				stack.rollback();
			}
		}
	}

	private Iterator getLinkedDataSetColumnIterator(DataSetHandle datasetHandle) {
		IDataSetColumnBindingsFormHandleProviderHelper helper = (IDataSetColumnBindingsFormHandleProviderHelper) ElementAdapterManager
				.getAdapter(this, IDataSetColumnBindingsFormHandleProviderHelper.class);
		if (helper != null) {
			return helper.getResultSetIterator(datasetHandle);
		}

		return null;
	}

	@Override
	public void clearAllBindingColumns() {
		if (bindingObject instanceof ReportItemHandle) {
			try {
				while (((ReportItemHandle) bindingObject).getColumnBindings().getItems() != null
						&& !((ReportItemHandle) bindingObject).getColumnBindings().getItems().isEmpty()) {
					((ReportItemHandle) bindingObject).getColumnBindings().clearValue();
				}
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
			}
		}
	}

	public void removedUnusedColumnBindings(List inputElement) {
		if (inputElement.size() > 0) {
			Object element = inputElement.get(0);
			if (element instanceof ReportElementHandle) {
				try {
					if (element instanceof GroupHandle) {
						DesignElementHandle parentHandle = ((GroupHandle) element).getContainer();
						if (parentHandle instanceof ReportItemHandle) {
							((ReportItemHandle) parentHandle).removedUnusedColumnBindings();
						}
					} else if (element instanceof ReportItemHandle) {
						((ReportItemHandle) element).removedUnusedColumnBindings();
					}
				} catch (SemanticException e) {
					ExceptionHandler.handle(e);
				}
			}
		}
	}

	protected TableViewer viewer;

	@Override
	public void setTableViewer(TableViewer viewer) {
		this.viewer = viewer;
	}

	public void setShowAggregation(boolean showAggregation) {
		if (showAggregation) {
			columnNames = new String[] { Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.Position"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.Name"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.DisplayNameID"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.DisplayName"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.DataType"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.Expression"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.Function"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.Filter"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.AggregateOn")//$NON-NLS-1$
																									// //$NON-NLS-3$
			};
			columnWidth = new int[] { 80, 110, 110, 110, 80, 110, 110, 110, 90 };
		}
	}

	private int sortingColumnIndex;

	@Override
	public void setSortingColumnIndex(int index) {
		this.sortingColumnIndex = index;
	}

	private int sortDirection = SWT.UP;

	@Override
	public void setSortDirection(int dir) {
		sortDirection = dir;
	}

	@Override
	public int getShowIndex(int pos) {
		List children = new ArrayList();
		for (Iterator iter = ((ReportItemHandle) bindingObject).getColumnBindings().iterator(); iter.hasNext();) {
			children.add(iter.next());
		}
		if (pos < 0 || pos >= children.size()) {
			return -1;
		}
		Object[] arrays = children.toArray();
		Arrays.sort(arrays, new BindingComparator());
		return Arrays.asList(arrays).indexOf(children.get(pos));
	}

	@Override
	public boolean isClearEnable() {
		return true;
	}

}

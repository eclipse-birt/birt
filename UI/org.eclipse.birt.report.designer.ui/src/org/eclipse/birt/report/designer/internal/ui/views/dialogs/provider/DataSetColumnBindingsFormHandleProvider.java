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

package org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
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
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;

/**
 * 
 */

public class DataSetColumnBindingsFormHandleProvider implements IFormHandleProvider {

	protected static final Logger logger = Logger.getLogger(DataSetColumnBindingsFormHandleProvider.class.getName());

	private static final String ALL = Messages.getString("DataSetColumnBindingsFormHandleProvider.ALL");//$NON-NLS-1$
	private static final String NONE = Messages.getString("DataSetColumnBindingsFormHandleProvider.NONE");//$NON-NLS-1$

	private String[] columnNames;

	// object to add data binding.
	private ReportElementHandle bindingObject;

	private static final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary()
			.getStructure(ComputedColumn.COMPUTED_COLUMN_STRUCT).getMember(ComputedColumn.DATA_TYPE_MEMBER)
			.getAllowedChoices();

	public DataSetColumnBindingsFormHandleProvider() {
	}

	private boolean canAggregation = true;

	public DataSetColumnBindingsFormHandleProvider(boolean canAggregation) {
		this.canAggregation = canAggregation;
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
	public void setBindingObject(ReportElementHandle bindingObject) {
		this.bindingObject = bindingObject;
	}

	public String[] getColumnNames() {
		if (canAggregation)
			columnNames = new String[] { Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.Name"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.DisplayNameID"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.DisplayName"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.DataType"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.Expression"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.Function"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.Filter"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.AggregateOn")//$NON-NLS-1$
			};
		else
			columnNames = new String[] { Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.Name"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.DisplayNameID"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.DisplayName"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.DataType"), //$NON-NLS-1$
					Messages.getString("DataSetColumnBindingsFormHandleProvider.Column.Expression"),//$NON-NLS-1$
			};
		return columnNames;
	}

	public int[] getColumnWidths() {
		if (canAggregation)
			return new int[] { 120, 120, 120, 80, 120, 100, 120, 120 };
		else
			return new int[] { 150, 150, 150, 150, 150 };
	}

	public String getTitle() {
		if (isEditable())
			return Messages.getString("DataSetColumnBindingsFormHandleProvider.DatasetTitle"); //$NON-NLS-1$
		else
			return Messages.getString("DataSetColumnBindingsFormHandleProvider.ReportItemTitle"); //$NON-NLS-1$
	}

	public boolean isEditable() {
		if (bindingObject == null)
			return false;
		else if (((ReportItemHandle) DEUtil.getInputFirstElement(bindingObject))
				.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF)
			return false;
		else if (((ReportItemHandle) DEUtil.getInputFirstElement(bindingObject))
				.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_NONE) {
			if (DEUtil.getBindingHolder((ReportItemHandle) DEUtil.getInputFirstElement(bindingObject), true) != null
					&& DEUtil.getBindingHolder((ReportItemHandle) DEUtil.getInputFirstElement(bindingObject), true)
							.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF)
				return false;
			else
				return true;
		} else
			return true;
	}

	public boolean doDeleteItem(int pos) throws Exception {
		int modelPos = getOriginalIndex(pos);
		if (modelPos > -1) {
			if (bindingObject instanceof ReportItemHandle) {
				((ReportItemHandle) bindingObject).getColumnBindings().getAt(modelPos).drop();
				if (viewer != null) {
					viewer.refresh(true);
					if (pos - 1 > -1 || viewer.getTable().getItemCount() == 0)
						viewer.getTable().setSelection(pos - 1);
					else {
						viewer.getTable().setSelection(0);
					}
					return true;
				}
				return true;
			}
		}
		return false;
	}

	public boolean doAddItem(int pos) throws Exception {

		DataColumnBindingDialog dialog = new DataColumnBindingDialog(true);
		dialog.setInput((ReportItemHandle) bindingObject);

		if (dialog.open() == Dialog.OK) {
			if (viewer != null) {
				viewer.refresh(true);
				return true;
			}
		}
		return false;

	}

	public boolean doEditItem(int pos) {
		ComputedColumnHandle bindingHandle = null;
		pos = getOriginalIndex(pos);
		if (pos > -1) {
			if (bindingObject instanceof ReportItemHandle) {
				bindingHandle = (ComputedColumnHandle) ((ReportItemHandle) bindingObject).getColumnBindings()
						.getAt(pos);
			}
		}
		if (bindingHandle == null)
			return false;

		boolean isResultSetColumn = false;
		String resultSetName = null;
		if (bindingObject instanceof DataItemHandle)
			resultSetName = ((DataItemHandle) bindingObject).getResultSetColumn();
		if (resultSetName != null && bindingHandle.getName().equals(resultSetName))
			isResultSetColumn = true;

		DataColumnBindingDialog dialog = new DataColumnBindingDialog(false);
		dialog.setInput((ReportItemHandle) bindingObject, bindingHandle);

		if (dialog.open() == Dialog.OK) {
			if (isResultSetColumn) {
				try {
					((DataItemHandle) bindingObject).setResultSetColumn(bindingHandle.getName());
				} catch (Exception e) {
					ExceptionHandler.handle(e);
				}
			}
			if (viewer != null) {
				viewer.refresh(true);
				return true;
			}
		}
		return false;
	}

	public int getOriginalIndex(int pos) {
		return pos;
	}

	public int getShowIndex(int pos) {
		return pos;
	}

	public String getColumnText(Object element, int columnIndex) {
		ComputedColumnHandle handle = ((ComputedColumnHandle) element);
		String text = null;

		switch (columnIndex) {
		case 0:
			text = handle.getName();
			break;
		case 1:
			text = handle.getDisplayNameID();
			break;
		case 2:
			text = handle.getDisplayName();
			break;
		case 3:
			text = ChoiceSetFactory.getDisplayNameFromChoiceSet(handle.getDataType(), DATA_TYPE_CHOICE_SET);
			break;
		case 4:
			text = org.eclipse.birt.report.designer.data.ui.util.DataUtil.getAggregationExpression(handle);
			break;
		case 5:
			try {
				String function = handle.getAggregateFunction();
				if (function != null) {
					function = DataAdapterUtil.adaptModelAggregationType(function);
					if (function != null && DataUtil.getAggregationManager().getAggregation(function) != null)
						return DataUtil.getAggregationManager().getAggregation(function).getDisplayName();
				}
			} catch (BirtException e) {
				ExceptionHandler.handle(e);
			}
			break;
		case 6:
			text = handle.getFilterExpression();
			break;
		case 7:
			String value = DEUtil.getAggregateOn(handle);
			if (value == null) {
				if (handle.getAggregateFunction() != null) {
					text = ALL;
				} else
					text = NONE;
			} else {
				text = value;
			}

			break;
		}

		if (text == null) {
			text = ""; //$NON-NLS-1$
		}
		return text;
	}

	public String getImagePath(Object element, int columnIndex) {
		return null;
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Object[] && ((Object[]) inputElement).length > 0) {
			return getElements(((Object[]) inputElement)[0]);
		}
		if (inputElement instanceof List) {
			return getElements(((List) inputElement).get(0));
		}
		if (inputElement instanceof ReportItemHandle) {
			ReportItemHandle reportHandle = DEUtil.getBindingHolder((ReportItemHandle) inputElement);
			this.bindingObject = reportHandle;
			List children = new ArrayList();
			if (reportHandle != null) {
				for (Iterator iter = reportHandle.getColumnBindings().iterator(); iter.hasNext();) {
					children.add(iter.next());
				}
			}
			Object[] arrays = children.toArray();
			return arrays;
		}
		return new Object[] {};
	}

	public Object getValue(Object element, String property) {
		int index = Arrays.asList(columnNames).indexOf(property);

		String columnText = getColumnText(element, index);
		return columnText;
	}

	public boolean needRefreshed(NotificationEvent event) {
		if (event.getEventType() == NotificationEvent.PROPERTY_EVENT) {
			PropertyEvent ev = (PropertyEvent) event;
			String propertyName = ev.getPropertyName();
			if (ReportItemHandle.BOUND_DATA_COLUMNS_PROP.equals(propertyName)
					|| ReportItemHandle.DATA_SET_PROP.equals(propertyName)
					|| ReportItemHandle.DATA_BINDING_REF_PROP.equals(propertyName)) {
				return true;
			}
		}
		return false;
	}

	public void generateBindingColumns(Object[] columns) {
		if (columns != null && columns.length > 0) {
			CommandStack stack = getActionStack();
			stack.startTrans(Messages
					.getString(Messages.getString("DataSetColumnBindingsFormHandleProvider.GenerateBindingColumns"))); //$NON-NLS-1$
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
					if (displayKey != null)
						bindingColumn.setDisplayNameID(displayKey);

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
						if (groupType.equals(DEUtil.TYPE_GROUP_GROUP))
							bindingColumn.setAggregrateOn(((GroupHandle) groupList.get(0)).getName());
						else if (groupType.equals(DEUtil.TYPE_GROUP_LISTING))
							bindingColumn.setAggregrateOn(null);
					}

				}
				stack.commit();

			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
				stack.rollback();
			}
		}
	}

	public void generateAllBindingColumns() {
		if (bindingObject != null) {
			DataSetHandle datasetHandle = null;
			if (bindingObject instanceof ReportItemHandle) {
				ReportItemHandle root = DEUtil.getBindingHolder((ReportItemHandle) bindingObject);
				if (root != null) {
					datasetHandle = root.getDataSet();
				}
			} else if (bindingObject instanceof GroupHandle) {
				ReportItemHandle root = DEUtil
						.getBindingHolder((ReportItemHandle) ((GroupHandle) bindingObject).getContainer());
				if (root != null) {
					datasetHandle = root.getDataSet();
				}
			}
			if (datasetHandle != null) {
				CommandStack stack = getActionStack();
				stack.startTrans(Messages.getString(
						Messages.getString("DataSetColumnBindingsFormHandleProvider.RefreshBindingColumns"))); //$NON-NLS-1$
				try {
					CachedMetaDataHandle cmdh = DataSetUIUtil.getCachedMetaDataHandle(datasetHandle);
					for (Iterator iter = cmdh.getResultSet().iterator(); iter.hasNext();) {
						ResultSetColumnHandle element = (ResultSetColumnHandle) iter.next();
						ComputedColumn bindingColumn = StructureFactory.newComputedColumn(bindingObject,
								element.getColumnName());
						bindingColumn.setDataType(element.getDataType());
						String groupType = DEUtil.getGroupControlType(bindingObject);
						List groupList = DEUtil.getGroups(bindingObject);

						ExpressionUtility.setBindingColumnExpression(element, bindingColumn);

						bindingColumn.setDisplayName(UIUtil.getColumnDisplayName(element));
						String displayKey = UIUtil.getColumnDisplayNameKey(element);
						if (displayKey != null)
							bindingColumn.setDisplayNameID(displayKey);

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
							if (groupType.equals(DEUtil.TYPE_GROUP_GROUP))
								bindingColumn.setAggregateOn(((GroupHandle) groupList.get(0)).getName());
							else if (groupType.equals(DEUtil.TYPE_GROUP_LISTING))
								bindingColumn.setAggregateOn(null);
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

	private void generateOutputParmsBindings(DataSetHandle datasetHandle) {
		List<DataSetParameterHandle> outputParams = new ArrayList<DataSetParameterHandle>();
		for (Iterator iter = datasetHandle.parametersIterator(); iter.hasNext();) {
			Object obj = iter.next();
			if ((obj instanceof DataSetParameterHandle) && ((DataSetParameterHandle) obj).isOutput() == true) {
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

		if (ret == 0)
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
					if (groupType.equals(DEUtil.TYPE_GROUP_GROUP))
						bindingColumn.setAggregrateOn(((GroupHandle) groupList.get(0)).getName());
					else if (groupType.equals(DEUtil.TYPE_GROUP_LISTING))
						bindingColumn.setAggregrateOn(null);
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
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
	}

	private TableViewer viewer;

	public void setTableViewer(TableViewer viewer) {
		this.viewer = viewer;
	}

	public boolean canAggregation() {
		return canAggregation;
	}

	public void addAggregateOn(int pos) throws Exception {
		boolean sucess = false;
		CommandStack stack = getActionStack();
		stack.startTrans(Messages.getString("FormPage.Menu.ModifyProperty")); //$NON-NLS-1$
		try {
			sucess = doAddAggregateOnItem(pos);
		} catch (Exception e) {
			stack.rollback();
			throw new Exception(e);
		}
		if (sucess) {
			stack.commit();
		} else {
			stack.rollback();
		}
	}

	public boolean doAddAggregateOnItem(int pos) {
		DataColumnBindingDialog dialog = new DataColumnBindingDialog(true);
		dialog.setAggreate(true);
		dialog.setInput((ReportItemHandle) getBindingObject());
		if (dialog.open() == Dialog.OK) {
			if (viewer != null) {
				viewer.refresh(true);
				return true;
			}
		}
		return false;
	}

	public boolean doAddMeasureOnItem(int pos) {
		DataColumnBindingDialog dialog = new DataColumnBindingDialog(true);
		dialog.setAggreate(true);
		dialog.setMeasure(true);
		dialog.setInput((ReportItemHandle) getBindingObject());
		if (dialog.open() == Dialog.OK) {
			if (viewer != null) {
				viewer.refresh(true);
				return true;
			}
		}
		return false;
	}

	protected CommandStack getActionStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	private int sortingColumnIndex;

	public void setSortingColumnIndex(int index) {
		this.sortingColumnIndex = index;
	}

	private int sortDirection = SWT.UP;

	public void setSortDirection(int dir) {
		sortDirection = dir;
	}

	@Override
	public CellEditor[] getEditors(Table table) {
		return null;
	}

	@Override
	public boolean doMoveItem(int oldPos, int newPos) throws Exception {
		if (Math.abs(oldPos - newPos) > 1)
			return false;

		ReportElementHandle elementHandle = this.getBindingObject();
		if (elementHandle instanceof ReportItemHandle) {
			ReportItemHandle itemHandle = (ReportItemHandle) elementHandle;
			List<ComputedColumn> list = itemHandle.getColumnBindings().getItems();

			ComputedColumn itemToMove = list.get(oldPos);
			ComputedColumn displacedItem = list.get(newPos);

			// Move the item that is moving
			list.set(newPos, itemToMove);
			// Restore the item that was displaced
			list.set(oldPos, displacedItem);

			// Wipe out all the bound columns
			int size = list.size();
			for (int index = 0; index < size; index++) {
				((ReportItemHandle) bindingObject).getColumnBindings().getAt(0).drop();
//				itemHandle.getColumnBindings( ).getAt( 0 ).drop( );
			}

			// Add back all the items in the new list order
			Iterator<ComputedColumn> it = list.iterator();
			while (it.hasNext()) {
				ComputedColumn next = it.next();
				itemHandle.addColumnBinding(next, true);
			}

			if (viewer != null) {
				viewer.refresh(true);
				viewer.getTable().setSelection(newPos);
			}
			return true;
		}
		return false;
	}

	@Override
	public Image getImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public boolean canModify(Object element, String property) {
		return false;
	}

	@Override
	public boolean modify(Object data, String property, Object value) throws Exception {
		return false;
	}

	public void addMeasureOn(int pos) throws Exception {
		boolean sucess = false;
		CommandStack stack = getActionStack();
		stack.startTrans(Messages.getString("FormPage.Menu.ModifyProperty")); //$NON-NLS-1$
		try {
			sucess = doAddMeasureOnItem(pos);
		} catch (Exception e) {
			stack.rollback();
			throw new Exception(e);
		}
		if (sucess) {
			stack.commit();
		} else {
			stack.rollback();
		}
	}

}
